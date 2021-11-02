package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.impl.CommandContext
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.metrics.ICommandMetrics
import dev.nanabell.jda.command.manager.permission.IPermissionHandler
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory

class CommandManager(
    private val prefix: String,
    private val allowMention: Boolean = false,
    private val autoRegisterCommands: Boolean = false,
    private val ownerIds: Set<Long>,
    private val listener: ICommandListener,
    provider: ICommandProvider,
    compiler: ICommandCompiler,
    private val permissionHandler: IPermissionHandler,
    private val metrics: ICommandMetrics
) : ListenerAdapter() {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    private val slashCommands: MutableList<CompiledCommand> = mutableListOf()
    private val textCommands: MutableList<CompiledCommand> = mutableListOf()

    init {
        // Load Commands
        logger.info("Initializing ${this::class.simpleName}")

        val commands = provider.provide()
        logger.debug("Compiling ${commands.size} command/s")

        for (command in commands) {
            val compiled = compiler.compile(command)
            when (compiled.isSlashCommand) {
                false -> textCommands.add(compiled)
                true -> slashCommands.add(compiled)
            }
        }

        // TODO: Register Slash Commands if enabled
        // TODO: Ensure Prefix has no Invalid Characters
        logger.info("Finished ${this::class.simpleName} Initialization with ${getCommands().size} Command/s")
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // TODO: Move this out of event Thread

        // Ignore Bots, System & Webhook Messages
        if (event.author.isBot || event.author.isSystem || event.isWebhookMessage) {
            logger.trace(
                "Ignoring Message {}. Bot={}, System={}, Webhook={}",
                event.messageIdLong,
                event.author.isBot,
                event.author.isSystem,
                event.isWebhookMessage
            )
            return
        }

        // Prefix
        // TODO: Handle Mention Prefix
        var prefixed = false
        var content = event.message.contentRaw

        val message = event.message
        if (message.contentRaw.startsWith(prefix)) {
            content = content.substring(prefix.length)
            prefixed = true
        }

        if (!prefixed) {
            logger.trace("Ignoring Message {}. Prefix=false", event.messageIdLong)
            return
        }

        // Parse Command Path
        logger.trace("Found correctly prefixed message {}, beginning command Parsing", message.idLong)
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

        executeCommand(compiled, CommandContext(event, ownerIds, arguments))
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

        executeCommand(compiled, CommandContext(event, ownerIds))
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
