package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.provider.ICommandProvider
import dev.nanabell.jda.command.manager.provider.StaticCommandProvider
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics

@Suppress("unused")
class CommandManagerBuilder {

    private var prefix: String? = null
    private var allowMention: Boolean = false
    private var autoRegister: Boolean = false

    private var provider: ICommandProvider = StaticCommandProvider(emptyList())
    private var registry: MeterRegistry = Metrics.globalRegistry


    fun build(): CommandManager {
        return CommandManager(checkNotNull(prefix), provider, allowMention, autoRegister, registry)
    }

    fun setPrefix(prefix: String): CommandManagerBuilder {
        this.prefix = prefix
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

    fun setCommandProvider(provider: ICommandProvider): CommandManagerBuilder {
        this.provider = provider
        return this
    }

    fun setMeterRegistry(registry: MeterRegistry): CommandManagerBuilder {
        this.registry = registry
        return this
    }
}
