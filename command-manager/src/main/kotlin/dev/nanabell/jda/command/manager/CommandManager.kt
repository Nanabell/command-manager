package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.IGuildCommandContext
import dev.nanabell.jda.command.manager.context.ISlashCommandContext
import dev.nanabell.jda.command.manager.context.impl.CommandContext
import dev.nanabell.jda.command.manager.context.impl.GuildCommandContext
import dev.nanabell.jda.command.manager.context.impl.GuildSlashCommandContext
import dev.nanabell.jda.command.manager.context.impl.SlashCommandContext
import dev.nanabell.jda.command.manager.exception.CommandPathLoopException
import dev.nanabell.jda.command.manager.exception.MissingParentException
import dev.nanabell.jda.command.manager.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class CommandManager(
    private val prefix: String,
    provider: ICommandProvider,
    private val allowMention: Boolean = false,
    private val autoRegisterCommands: Boolean = false,
    registry: MeterRegistry = Metrics.globalRegistry
) : ListenerAdapter() {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    private val slashCommands: MutableList<CompiledCommand> = mutableListOf()
    private val textCommands: MutableList<CompiledCommand> = mutableListOf()

    private val commandSuccessCount = registry.counter("command.executed", "status", "success")
    private val commandRejectCount = registry.counter("command.executed", "status", "rejected")
    private val commandAbortCount = registry.counter("command.executed", "status", "aborted")
    private val commandFailCount = registry.counter("command.executed", "status", "failed")
    private val unknownCommandCount = registry.counter("command.unknown")
    private val commandExecuteTimer = registry.timer("command.executed.time")

    init {
        // Load Commands
        logger.info("Initializing ${this::class.simpleName}")

        val commands = provider.provide()
        logger.debug("CommandProvider found ${commands.size} command/s")
        for (command in commands) {
            logger.trace("Compiling Command ${command::class.simpleName}")

            when (command) {
                is ITextCommand, is IGuildTextCommand  -> textCommands.add(CompiledCommand(command))
                is ISlashCommand, is IGuildSlashCommand -> slashCommands.add(CompiledCommand(command))
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

    private fun updateCommandPath(compiled: CompiledCommand, list: List<CompiledCommand>, seen: MutableList<KClass<*>>): String {
        if (seen.contains(compiled.command::class)) {
            throw CommandPathLoopException(compiled)
        }
        seen.add(compiled.command::class)

        if (compiled.subcommandOf == NullCommand::class) return compiled.commandPath
        val parent = list.firstOrNull { it.command::class == compiled.subcommandOf } ?: throw MissingParentException(compiled)
        val parentPath = updateCommandPath(parent, list, seen)

        compiled.commandPath = "$parentPath/${compiled.name}"
        return compiled.commandPath
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // TODO: Move this out of event Thread

        // Ignore Bots, System & Webhook Messages
        if (event.author.isBot || event.author.isSystem || event.isWebhookMessage) {
            logger.trace("Ignoring Message {}. Bot={}, System={}, Webhook={}", event.messageIdLong, event.author.isBot, event.author.isSystem, event.isWebhookMessage)
            commandRejectCount.increment()
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
        logger.trace("Build TextCommandPath: /{}", commandPath)

        // Parse Command from CommandPath
        var compiled: CompiledCommand? = null
        var currentPath = paths[0]

        for (path in paths) {
            if (currentPath != path) currentPath += "/$path"

            val current = textCommands.firstOrNull {
                (it.isGuildCommand == event.isFromGuild || !it.isGuildCommand) && it.commandPath == currentPath
            }
            logger.trace("Parsed Command: {}", current)

            if (current == null) {
                break
            }

            compiled = current
        }

        if (compiled == null) {
            logger.trace("Command parse failed to find any command for {}", commandPath)
            unknownCommandCount.increment()
            return
        }

        val arguments = paths.subList(currentPath.count { it == '/' }.coerceAtLeast(1), paths.size).toTypedArray()

        // Execute Command
        logger.debug("Executing TextCommand: $compiled")
        val context = if (event.isFromGuild) GuildCommandContext(event, arguments) else CommandContext(event, arguments)
        executeCommand(compiled.command, context)
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        // TODO: Move this out of event Thread

        logger.trace("Received Slash Command: $event")
        val compiled = slashCommands.firstOrNull { it.commandPath == event.commandPath }

        if (compiled == null) {
            logger.warn("Received Unknown Slash Command with path: ${event.commandPath}")
            unknownCommandCount.increment()
            return
        }

        logger.debug("Executing SlashCommand: $compiled")
        val context = if (event.isFromGuild) GuildSlashCommandContext(event) else SlashCommandContext(event)
        executeCommand(compiled.command, context)
    }

    private fun executeCommand(command: IBaseCommand<out ICommandContext>, context: ICommandContext) {
        // TODO: Handle Predicates like Permissions etc

        var success = false
        val start = System.currentTimeMillis()
        try {
            when (command) {
                is ITextCommand -> command.execute(context)
                is IGuildTextCommand -> command.execute(context as IGuildCommandContext)
                is ISlashCommand -> command.execute(context as ISlashCommandContext)
                is IGuildSlashCommand -> command.execute(context as IGuildCommandContext)
            }

            success = true
            commandSuccessCount.increment()
        } catch (e: CommandRejectedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Rejected", e)
            commandRejectCount.increment()
        } catch (e: CommandAbortedException) {
            logger.debug("Command ${command::class.qualifiedName} has been Aborted", e)
            commandAbortCount.increment()
        } catch (e: Throwable) {
            logger.error("Command ${command::class.qualifiedName} has failed!", e)
            commandFailCount.increment()
        } finally {
            val stop = System.currentTimeMillis()
            commandExecuteTimer.record(stop - start, TimeUnit.MILLISECONDS)

            if (success)
                logger.debug("Command ${command::class.qualifiedName} has finished Executing in ${stop - start}ms")
        }
    }

    fun getCommands(): List<CompiledCommand> {
        val commands = ArrayList(textCommands)
        commands.addAll(slashCommands)
        return commands
    }
}
