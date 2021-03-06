package dev.nanabell.command.manager

import dev.nanabell.command.manager.compile.ICommandCompiler
import dev.nanabell.command.manager.compile.impl.AnnotationCommandCompiler
import dev.nanabell.command.manager.context.ICommandContextBuilder
import dev.nanabell.command.manager.context.impl.BasicCommandContextBuilder
import dev.nanabell.command.manager.event.IEventMediator
import dev.nanabell.command.manager.event.impl.NoOpMediator
import dev.nanabell.command.manager.listener.ICommandListener
import dev.nanabell.command.manager.listener.impl.CompositeCommandListener
import dev.nanabell.command.manager.metrics.ICommandMetrics
import dev.nanabell.command.manager.metrics.impl.SimpleCommandMetrics
import dev.nanabell.command.manager.permission.IPermissionHandler
import dev.nanabell.command.manager.permission.impl.DefaultPermissionHandlerBuilder
import dev.nanabell.command.manager.provider.ICommandProvider
import dev.nanabell.command.manager.provider.impl.StaticCommandProvider
import dev.nanabell.command.manager.registry.ICommandRegistry
import dev.nanabell.command.manager.registry.impl.MemoryCommandRegistry
import org.slf4j.LoggerFactory

@Suppress("unused")
class CommandManagerBuilder(private var prefix: String, vararg ownerIds: Long) {

    private val logger = LoggerFactory.getLogger(CommandManagerBuilder::class.java)

    private var ownerIds: MutableSet<Long> = ownerIds.toMutableSet()
    private var allowMention: Boolean = false
    private var autoRegister: Boolean = false

    private var listener: CompositeCommandListener = CompositeCommandListener()
    private var provider: ICommandProvider? = null
    private var compiler: ICommandCompiler? = null
    private var registry: ICommandRegistry? = null
    private var permissionHandler: IPermissionHandler? = null
    private var context: ICommandContextBuilder? = null
    private var metrics: ICommandMetrics? = null
    private var mediator: IEventMediator? = null


    fun build(): CommandManager {
        val provider = this.provider ?: StaticCommandProvider(emptyList()).also { logger.warn("No Command Provider has been set. Building Command Manager with 0 Commands") }
        val compiler = this.compiler ?: AnnotationCommandCompiler()
        val registry = this.registry ?: MemoryCommandRegistry()
        val mediator = this.mediator ?: NoOpMediator().also { logger.warn("No Event Mediator has been set. Command Manger events will not be called automatically!") }
        val context = this.context ?: BasicCommandContextBuilder().also { logger.warn("No Command Context Builder has been set. Using ${BasicCommandContextBuilder::class.simpleName}. This will not be usable for real scenarios") }
        val permissionHandler = this.permissionHandler ?: DefaultPermissionHandlerBuilder().build()
        val metrics = this.metrics ?: SimpleCommandMetrics()

        if (prefix.contains(' ')) throw IllegalArgumentException("Spaces are not allowed in the prefix: '$prefix'")

        return CommandManager(
            prefix,
            allowMention,
            autoRegister,
            ownerIds,
            provider,
            compiler,
            registry,
            mediator,
            context,
            permissionHandler,
            listener,
            metrics
        )
    }

    fun setPrefix(prefix: String): CommandManagerBuilder {
        this.prefix = prefix
        return this
    }

    fun addOwnerId(ownerId: Long): CommandManagerBuilder {
        this.ownerIds.add(ownerId)
        return this
    }

    fun removeOwnerId(ownerId: Long): CommandManagerBuilder {
        this.ownerIds.remove(ownerId)
        return this
    }

    fun setAllowMention(allowMention: Boolean): CommandManagerBuilder {
        this.allowMention = allowMention
        return this
    }

    fun setAutoRegister(autoRegister: Boolean): CommandManagerBuilder {
        this.autoRegister = autoRegister
        return this
    }

    fun registerCommandListener(listener: ICommandListener): CommandManagerBuilder {
        this.listener.registerListener(listener)
        return this
    }

    fun removeCommandListener(listener: ICommandListener): CommandManagerBuilder {
        this.listener.removeListener(listener)
        return this
    }

    fun setCommandProvider(provider: ICommandProvider): CommandManagerBuilder {
        this.provider = provider
        return this
    }

    fun setCommandCompiler(compiler: ICommandCompiler): CommandManagerBuilder {
        this.compiler = compiler
        return this
    }

    fun setContextBuilder(context: ICommandContextBuilder): CommandManagerBuilder {
        this.context = context
        return this
    }

    fun setPermissionHandler(permissionHandler: IPermissionHandler): CommandManagerBuilder {
        this.permissionHandler = permissionHandler
        return this
    }

    fun setEventMediator(mediator: IEventMediator): CommandManagerBuilder {
        this.mediator = mediator
        return this
    }

    fun setCommandMetrics(metrics: ICommandMetrics): CommandManagerBuilder {
        this.metrics = metrics
        return this
    }
}
