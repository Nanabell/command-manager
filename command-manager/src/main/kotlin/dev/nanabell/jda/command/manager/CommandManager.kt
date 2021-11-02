package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.impl.CommandContext
import dev.nanabell.jda.command.manager.exception.CommandPathLoopException
import dev.nanabell.jda.command.manager.exception.MissingParentException
import dev.nanabell.jda.command.manager.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.permission.IPermissionHandler
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class CommandManager(
    private val prefix: String,
    private val allowMention: Boolean = false,
    private val autoRegisterCommands: Boolean = false,
    private val ownerIds: Set<Long>,
    private val listener: ICommandListener,
    provider: ICommandProvider,
    compiler: ICommandCompiler,
    private val permissionHandler: IPermissionHandler
) : ListenerAdapter() {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    private val slashCommands: MutableList<CompiledCommand> = mutableListOf()
    private val textCommands: MutableList<CompiledCommand> = mutableListOf()

    init {
        // Load Commands
        logger.info("Initializing ${this::class.simpleName}")

        val commands = provider.provide()
        logger.debug("CommandProvider found ${commands.size} command/s")
        for (command in commands) {
            logger.trace("Compiling Command ${command::class.simpleName}")

            val compiled = compiler.compile(command)
            when (compiled.isSlashCommand) {
                false -> textCommands.add(compiled)
                true -> slashCommands.add(compiled)
            }
        }

        logger.info("Compiled ${textCommands.size} Text Command/s and ${slashCommands.size} Slash Command/s")

        // Build CommandPath Tree
        logger.debug("Building Text Command Paths")
        for (textCommand in textCommands) {
            logger.trace("Building CommandPath for Command: ${textCommand.name}")
            updateCommandPath(textCommand, textCommands, mutableListOf())
            logger.trace("CommandPath for Command: ${textCommand.name} = /${textCommand.commandPath}")
        }

        logger.debug("Building Slash Command Paths")
        for (slashCommand in slashCommands) {
            logger.trace("Building CommandPath for Command: ${slashCommand.name}")
            updateCommandPath(slashCommand, slashCommands, mutableListOf())
            logger.trace("CommandPath for Command: ${slashCommand.name} = /${slashCommand.commandPath}")
        }

        // Ensure Slash Commands don't have a Path longer than 3 (Discord Limitation!)
        for (slashCommand in slashCommands) {
            val depth = slashCommand.commandPath.count { it == '/' }
            if (depth > 2) {
                throw SlashCommandDepthException(depth, slashCommand.command)
            }
        }

        // TODO: Register Slash Commands if enabled
        // TODO: Ensure Prefix has no Invalid Characters
    }

    private fun updateCommandPath(
        compiled: CompiledCommand,
        list: List<CompiledCommand>,
        seen: MutableList<KClass<*>>
    ): String {
        if (seen.contains(compiled.command::class)) {
            throw CommandPathLoopException(compiled)
        }
        seen.add(compiled.command::class)

        if (compiled.subcommandOf == null) return compiled.commandPath
        val parent =
            list.firstOrNull { it.command::class == compiled.subcommandOf } ?: throw MissingParentException(compiled)
        val parentPath = updateCommandPath(parent, list, seen)

        compiled.commandPath = "$parentPath/${compiled.name}"
        return compiled.commandPath
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
            return
        }

        executeCommand(compiled, CommandContext(event, ownerIds))
    }

    private fun executeCommand(compiled: CompiledCommand, context: ICommandContext) {
        val command = compiled.command

        if (!permissionHandler.handle(compiled, context)) {
            listener.onRejected(compiled, context, CommandRejectedException("Think of something here")) // STOPSHIP: 01/11/2021
            return
        }

        try {
            logger.debug("Executing Command: $command")
            listener.onExecute(compiled, context)

            val start = System.currentTimeMillis()
            command.execute(context)
            val duration = start - System.currentTimeMillis()

            logger.debug("Command ${command::class.qualifiedName} has finished Executing in ${duration}ms")
            listener.onExecuted(compiled, context)

        } catch (e: CommandRejectedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Rejected", e)
            listener.onRejected(compiled, context, e)
            return

        } catch (e: CommandAbortedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Aborted", e)
            listener.onAborted(compiled, context, e)
            return

        } catch (e: Throwable) {
            logger.error("Command ${command::class.qualifiedName} has failed!", e)
            listener.onFailed(compiled, context, e)
            return

        }
    }

    fun getCommands(): List<CompiledCommand> {
        val commands = ArrayList(textCommands)
        commands.addAll(slashCommands)
        return commands
    }
}
