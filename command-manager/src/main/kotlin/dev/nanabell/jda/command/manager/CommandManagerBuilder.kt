package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.compile.impl.AnnotationCommandCompiler
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.listener.impl.CompositeCommandListener
import dev.nanabell.jda.command.manager.listener.impl.MetricCommandListener
import dev.nanabell.jda.command.manager.predicate.IPredicateResolver
import dev.nanabell.jda.command.manager.predicate.impl.DefaultPredicateResolver
import dev.nanabell.jda.command.manager.provider.ICommandProvider
import dev.nanabell.jda.command.manager.provider.impl.StaticCommandProvider
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics

@Suppress("unused")
class CommandManagerBuilder(private var prefix: String, private var ownerId: Long) {

    private var coOwnerIds: MutableSet<Long> = mutableSetOf()
    private var allowMention: Boolean = false
    private var autoRegister: Boolean = false

    private var registry: MeterRegistry = Metrics.globalRegistry
    private var listener: CompositeCommandListener = CompositeCommandListener()
    private var provider: ICommandProvider = StaticCommandProvider(emptyList())
    private var compiler: ICommandCompiler = AnnotationCommandCompiler()

    private var resolver: IPredicateResolver? = null

    private var registerMetrics: Boolean = true


    fun build(): CommandManager {
        if (registerMetrics)
            listener.registerListener(MetricCommandListener(registry))

        var resolver = this.resolver
        if (resolver == null) {
            resolver = DefaultPredicateResolver(ownerId, coOwnerIds)
        }

        return CommandManager(prefix, allowMention, autoRegister, listener, provider, compiler, resolver)
    }

    fun setPrefix(prefix: String): CommandManagerBuilder {
        this.prefix = prefix
        return this
    }

    fun setOwnerId(ownerId: Long): CommandManagerBuilder {
        this.ownerId = ownerId
        return this
    }

    fun setCoOwnerIds(coOwnerIds: Set<Long>): CommandManagerBuilder {
        this.coOwnerIds = coOwnerIds.toMutableSet()
        return this
    }

    fun setCoOwnerIds(vararg coOwnerIds: Long): CommandManagerBuilder {
        this.coOwnerIds = coOwnerIds.toMutableSet()
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
}
