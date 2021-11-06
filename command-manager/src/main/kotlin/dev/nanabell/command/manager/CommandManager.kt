package dev.nanabell.command.manager

import dev.nanabell.command.manager.command.ICommand
import dev.nanabell.command.manager.command.exception.CommandAbortedException
import dev.nanabell.command.manager.command.exception.CommandRejectedException
import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.compile.ICommandCompiler
import dev.nanabell.command.manager.context.ICommandContext
import dev.nanabell.command.manager.context.ICommandContextBuilder
import dev.nanabell.command.manager.event.IEventListener
import dev.nanabell.command.manager.event.IEventMediator
import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent
import dev.nanabell.command.manager.listener.ICommandListener
import dev.nanabell.command.manager.metrics.ICommandMetrics
import dev.nanabell.command.manager.permission.IPermissionHandler
import dev.nanabell.command.manager.provider.ICommandProvider
import dev.nanabell.command.manager.registry.ICommandRegistry
import kotlinx.coroutines.*
import org.jetbrains.annotations.TestOnly
import org.slf4j.LoggerFactory

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class CommandManager(
    val prefix: String,
    val allowMention: Boolean = false,
    val autoRegisterCommands: Boolean = false,
    val ownerIds: Set<Long>,
    val provider: ICommandProvider,
    val compiler: ICommandCompiler,
    val registry: ICommandRegistry,
    val mediator: IEventMediator,
    val contexts: ICommandContextBuilder,
    val permissions: IPermissionHandler,
    val listener: ICommandListener,
    val metrics: ICommandMetrics
) : IEventListener {

    private val logger = LoggerFactory.getLogger(CommandManager::class.java)

    private var executionScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        logger.info("Initializing ${this::class.simpleName}")

        @Suppress("UNCHECKED_CAST") // Validated at ICommand<in T : ICommandContext> Interface
        val commands = provider.provide() as Collection<ICommand<ICommandContext>>
        logger.debug("Compiling ${commands.size} command/s")

        for (command in commands) {
            val compiled = compiler.compile(command, this)
            registry.registerCommand(compiled)
        }

        // TODO: Register Slash Commands if enabled
        // TODO: Ensure Prefix has no Invalid Characters
        logger.debug("Registering EventMediator: ${mediator::class.simpleName}")
        mediator.registerCommandManager(this)

        logger.info("Finished ${this::class.simpleName} Initialization with ${registry.size} Command/s")
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        executionScope.launch {
            // Ignore Bots, System & Webhook Messages
            if (event.isBot || event.isSystem || event.isWebhook) {
                logger.trace(
                    "Ignoring Message {}. Bot={}, System={}, Webhook={}",
                    event.messageId,
                    event.isBot,
                    event.isSystem,
                    event.isWebhook
                )
                return@launch
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
                return@launch
            }

            // Parse Command Path
            logger.trace("Parsing command in Message {}", event.messageId)
            val commandPath = content.replace(' ', '/')
            val paths = commandPath.split('/')
            logger.trace("Built TextCommandPath: /{}", commandPath)

            // Parse Command from CommandPath
            var compiled: CompiledCommand? = null
            var currentPath = paths[0]

            for (path in paths) {
                if (currentPath != path) currentPath += "/$path"
                val current = registry.findTextCommand(currentPath) { it.guildOnly == event.isFromGuild || !it.guildOnly } ?: break

                logger.trace("Parsed Command: {}", current)
                compiled = current
            }

            if (compiled == null) {
                logger.debug("Unable to find Command with Path: /$commandPath")
                listener.onUnknown(commandPath)
                metrics.incUnknown()
                return@launch
            }

            val arguments = paths.subList(currentPath.count { it == '/' }.coerceAtLeast(1), paths.size).toTypedArray()

            launch(CoroutineName(compiled.commandPath)) { executeCommand(compiled, contexts.fromMessage(event, arguments)) }
        }

    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        executionScope.launch {
            logger.trace("Received Slash Command: $event")
            val command = registry.findSlashCommand(event.commandPath) { it.guildOnly == event.isFromGuild || !it.guildOnly }

            if (command == null) {
                logger.debug("Unable to find Command with Path: /${event.commandPath}")
                listener.onUnknown(event.commandPath)
                metrics.incUnknown()
                return@launch
            }

            launch(CoroutineName(command.commandPath)) { executeCommand(command, contexts.fromCommand(event)) }
        }
    }

    private suspend fun executeCommand(command: CompiledCommand, context: ICommandContext) {
        logger.debug("Checking Permissions for $command in $context")
        if (!permissions.handle(command, context)) {
            listener.onRejected(command, context, CommandRejectedException("Think of something here"))
            metrics.incRejected()
            return
        }

        try {
            logger.debug("Executing Command: $command")
            listener.onExecute(command, context)

            val duration = command.execute(context)
            metrics.record(duration)

            logger.debug("Command $command has finished Executing in ${duration}ms")
            listener.onExecuted(command, context)
            metrics.incExecuted()

        } catch (e: CommandRejectedException) {
            logger.debug("Command $command has been Rejected", e)
            listener.onRejected(command, context, e)
            metrics.incRejected()
            return

        } catch (e: CommandAbortedException) {
            logger.debug("Command $command has been Aborted", e)
            listener.onAborted(command, context, e)
            metrics.incAborted()
            return

        } catch (e: Throwable) {
            logger.error("Command $command has failed!", e)
            listener.onFailed(command, context, e)
            metrics.incFailed()
            return

        }
    }

    @TestOnly
    internal fun overrideScope(scope: CoroutineScope) {
        executionScope.cancel()
        this.executionScope = scope
    }
}
