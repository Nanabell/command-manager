package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.ICommandContextBuilder
import dev.nanabell.jda.command.manager.event.IEventListener
import dev.nanabell.jda.command.manager.event.IEventMediator
import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.metrics.ICommandMetrics
import dev.nanabell.jda.command.manager.permission.IPermissionHandler
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import org.slf4j.LoggerFactory

class CommandManager(
    private val prefix: String,
    private val allowMention: Boolean = false,
    private val autoRegisterCommands: Boolean = false,
    val ownerIds: Set<Long>,
    private val listener: ICommandListener,
    provider: ICommandProvider,
    compiler: ICommandCompiler,
    private val permissionHandler: IPermissionHandler,
    private val metrics: ICommandMetrics,
    private val contextBuilder: ICommandContextBuilder,
    eventMediator: IEventMediator
) : IEventListener {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    private val slashCommands: MutableList<CompiledCommand> = mutableListOf()
    private val textCommands: MutableList<CompiledCommand> = mutableListOf()

    init {
        // Load Commands
        logger.info("Initializing ${this::class.simpleName}")

        @Suppress("UNCHECKED_CAST") // Validated at ICommand<in T : ICommandContext> Interface
        val commands = provider.provide() as Collection<ICommand<ICommandContext>>
        logger.debug("Compiling ${commands.size} command/s")

        for (command in commands) {
            val compiled = compiler.compile(command, this)
            when (compiled.isSlashCommand) {
                false -> textCommands.add(compiled)
                true -> slashCommands.add(compiled)
            }
        }

        // TODO: Register Slash Commands if enabled
        // TODO: Ensure Prefix has no Invalid Characters
        logger.debug("Registering self at event Mediator: ${eventMediator::class.simpleName}")
        eventMediator.registerCommandManager(this)

        logger.info("Finished ${this::class.simpleName} Initialization with ${getCommands().size} Command/s")
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // TODO: Move this out of event Thread

        // Ignore Bots, System & Webhook Messages
        if (event.isBot || event.isSystem || event.isWebhook) {
            logger.trace(
                "Ignoring Message {}. Bot={}, System={}, Webhook={}",
                event.messageId,
                event.isBot,
                event.isSystem,
                event.isWebhook
            )
            return
        }

        // Prefix
        // TODO: Handle Mention Prefix
        var prefixed = false
        var content = event.content
        if (content.startsWith(prefix)) {
            content = content.substring(prefix.length)
            prefixed = true
        }

        if (!prefixed) {
            logger.trace("Ignoring Message {}. Prefix=false", event.messageId)
            return
        }

        // Parse Command Path
        logger.trace("Found correctly prefixed message {}, beginning command Parsing", event.messageId)
        val commandPath = content.replace(' ', '/')
        val paths = commandPath.split('/')
        logger.trace("Built TextCommandPath: /{}", commandPath)

        // Parse Command from CommandPath
        var compiled: CompiledCommand? = null
        var currentPath = paths[0]

        for (path in paths) {
            if (currentPath != path) currentPath += "/$path"

            val current = textCommands.firstOrNull {
                (it.guildOnly == event.isFromGuild || !it.guildOnly) && it.commandPath == currentPath
            } ?: break

            logger.trace("Parsed Command: {}", current)
            compiled = current
        }

        if (compiled == null) {
            logger.debug("Unable to find Command with Path: /$commandPath")
            listener.onUnknown(commandPath)
            metrics.incUnknown()
            return
        }

        val arguments = paths.subList(currentPath.count { it == '/' }.coerceAtLeast(1), paths.size).toTypedArray()

        executeCommand(compiled, contextBuilder.fromMessage(event, arguments))
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        // TODO: Move this out of event Thread

        logger.trace("Received Slash Command: $event")
        val compiled = slashCommands.firstOrNull { (it.guildOnly == event.isFromGuild || !it.guildOnly) && it.commandPath == event.commandPath }

        if (compiled == null) {
            logger.debug("Unable to find Command with Path: /${event.commandPath}")
            listener.onUnknown(event.commandPath)
            metrics.incUnknown()
            return
        }

        executeCommand(compiled, contextBuilder.fromCommand(event))
    }

    private fun executeCommand(compiled: CompiledCommand, context: ICommandContext) {
        val command = compiled.command

        if (!permissionHandler.handle(compiled, context)) {
            listener.onRejected(compiled, context, CommandRejectedException("Think of something here")) // STOPSHIP: 01/11/2021
            metrics.incRejected()
            return
        }

        try {
            logger.debug("Executing Command: $command")
            listener.onExecute(compiled, context)

            val start = System.currentTimeMillis()
            metrics.record { command.execute(context) }
            val duration = start - System.currentTimeMillis()

            logger.debug("Command ${command::class.qualifiedName} has finished Executing in ${duration}ms")
            listener.onExecuted(compiled, context)
            metrics.incExecuted()

        } catch (e: CommandRejectedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Rejected", e)
            listener.onRejected(compiled, context, e)
            metrics.incRejected()
            return

        } catch (e: CommandAbortedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Aborted", e)
            listener.onAborted(compiled, context, e)
            metrics.incAborted()
            return

        } catch (e: Throwable) {
            logger.error("Command ${command::class.qualifiedName} has failed!", e)
            listener.onFailed(compiled, context, e)
            metrics.incFailed()
            return

        }
    }

    fun getCommands(): List<CompiledCommand> {
        val commands = ArrayList(textCommands)
        commands.addAll(slashCommands)
        return commands
    }
}
