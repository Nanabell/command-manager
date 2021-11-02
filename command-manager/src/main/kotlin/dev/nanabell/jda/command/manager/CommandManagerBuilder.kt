package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.compile.impl.AnnotationCommandCompiler
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.listener.impl.CompositeCommandListener
import dev.nanabell.jda.command.manager.listener.impl.MetricCommandListener
import dev.nanabell.jda.command.manager.permission.IPermissionHandler
import dev.nanabell.jda.command.manager.permission.impl.DefaultPermissionHandlerBuilder
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import dev.nanabell.jda.command.manager.provider.impl.StaticCommandProvider
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics

@Suppress("unused")
class CommandManagerBuilder(private var prefix: String, ownerId: Long) {

    private var ownerIds: MutableSet<Long> = mutableSetOf(ownerId)
    private var allowMention: Boolean = false
    private var autoRegister: Boolean = false

    private var registry: MeterRegistry = Metrics.globalRegistry
    private var listener: CompositeCommandListener = CompositeCommandListener()
    private var provider: ICommandProvider = StaticCommandProvider(emptyList())
    private var compiler: ICommandCompiler = AnnotationCommandCompiler()
    private var permissionHandler: IPermissionHandler? = null

    private var registerMetrics: Boolean = true


    fun build(): CommandManager {
        if (registerMetrics)
            listener.registerListener(MetricCommandListener(registry))

        val permissionHandler = this.permissionHandler ?: DefaultPermissionHandlerBuilder().build()

        return CommandManager(prefix, allowMention, autoRegister, ownerIds, listener, provider, compiler, permissionHandler)
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

    fun setMeterRegistry(registry: MeterRegistry): CommandManagerBuilder {
        this.registry = registry
        return this
    }

    fun setRegisterMetrics(registerMetrics: Boolean): CommandManagerBuilder {
        this.registerMetrics = registerMetrics
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

    fun setPermissionHandler(permissionHandler: IPermissionHandler): CommandManagerBuilder {
        this.permissionHandler = permissionHandler
        return this
    }
}
