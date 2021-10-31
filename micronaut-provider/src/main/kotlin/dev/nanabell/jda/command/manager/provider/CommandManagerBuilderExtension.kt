package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.CommandManagerBuilder
import io.micronaut.context.annotation.Context


@Context
class CommandManagerBuilderExtension(provider: MicronautCommandProvider) {

    init {
        CommandManagerBuilderExtension.provider = provider
    }

    companion object {
        internal lateinit var provider: MicronautCommandProvider
    }
}


@Suppress("unused")
fun CommandManagerBuilder.setMicronautProvider(): CommandManagerBuilder {
    this.setCommandProvider(CommandManagerBuilderExtension.provider)
    return this
}
