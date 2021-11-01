package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.command.listener.ICommandListener
import dev.nanabell.jda.command.manager.context.*
import dev.nanabell.jda.command.manager.context.impl.*
import dev.nanabell.jda.command.manager.exception.CommandPathLoopException
import dev.nanabell.jda.command.manager.exception.MissingParentException
import dev.nanabell.jda.command.manager.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class CommandManager(
    private val prefix: String,
    private val ownerId: Long,
    private val coOwnerIds: Set<Long> = emptySet(),
    private val allowMention: Boolean = false,
    private val autoRegisterCommands: Boolean = false,
    private val listener: ICommandListener,
    provider: ICommandProvider
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
                (it.isGuildCommand == event.isFromGuild || !it.isGuildCommand) && it.commandPath == currentPath
            }
            logger.trace("Parsed Command: {}", current)

            if (current == null) {
                break
            }

            compiled = current
        }

        if (compiled == null) {
            listener.onUnknown(commandPath)
            return
        }

        val arguments = paths.subList(currentPath.count { it == '/' }.coerceAtLeast(1), paths.size).toTypedArray()

        // Execute Command
        val context = if (event.isFromGuild) GuildTextCommandContext(event, arguments) else TextCommandContext(event, arguments)
        executeCommand(compiled, context)
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        // TODO: Move this out of event Thread

        logger.trace("Received Slash Command: $event")
        val compiled = slashCommands.firstOrNull { (it.isGuildCommand == event.isFromGuild || !it.isGuildCommand) && it.commandPath == event.commandPath }

        if (compiled == null) {
            listener.onUnknown(event.commandPath)
            return
        }

        val context = if (event.isFromGuild) GuildSlashCommandContext(event) else SlashCommandContext(event)
        executeCommand(compiled, context)
    }

    private fun executeCommand(compiled: CompiledCommand, context: ICommandContext) {
        val command = compiled.command
        // TODO: Handle Predicates like Permissions etc

        // Check Owner Only Commands
        if (compiled.ownerOnly) {
            val authorId = context.author.idLong
            if (authorId != ownerId || !coOwnerIds.contains(authorId)) {
                listener.onRejected(compiled, context, CommandRejectedException("This command can only be ran by the Bot Owner!"))
                return
            }
        }

        val userPerms = compiled.userPermission
        if (userPerms.isNotEmpty()) {
            if (!context.isFromGuild) {
                listener.onRejected(compiled, context, CommandRejectedException("Global Command has Guild specific Predicates! $compiled"))
                return
            }

            if (!context.hasPermission(context.member!!, *userPerms)) {
                listener.onRejected(compiled, context, CommandRejectedException("User Missing Permission Requirement"))
                return
            }
        }

        val botPerms = compiled.botPermission
        if (botPerms.isNotEmpty()) {
            if (!context.isFromGuild) {
                listener.onRejected(compiled, context, CommandRejectedException("Global Command has Guild specific Predicates! $compiled"))
                return
            }

            if (!context.hasPermission(context.selfMember!!, *botPerms)) {
                listener.onRejected(compiled, context, CommandRejectedException("Bot Missing Permission Requirement"))
                return
            }
        }

        try {
            listener.onExecute(compiled, context)
            when (command) {
                is ITextCommand -> command.execute(context as ITextCommandContext)
                is IGuildTextCommand -> command.execute(context as IGuildTextCommandContext)
                is ISlashCommand -> command.execute(context as ISlashCommandContext)
                is IGuildSlashCommand -> command.execute(context as IGuildSlashCommandContext)
            }
        } catch (e: CommandRejectedException) {
            listener.onRejected(compiled, context, e)
            return

        } catch (e: CommandAbortedException) {
            listener.onAborted(compiled, context, e)
            return

        } catch (e: Throwable) {
            listener.onFailed(compiled, context, e)
            return

        }

        listener.onExecuted(compiled, context)
    }

    fun getCommands(): List<CompiledCommand> {
        val commands = ArrayList(textCommands)
        commands.addAll(slashCommands)
        return commands
    }
}
