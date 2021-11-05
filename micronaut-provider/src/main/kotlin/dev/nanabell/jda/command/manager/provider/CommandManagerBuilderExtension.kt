package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.CommandManagerBuilder
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Context

@Suppress("unused")
fun CommandManagerBuilder.setMicronautProvider(context: ApplicationContext): CommandManagerBuilder {
    this.setCommandProvider(MicronautCommandProvider(context))
    return this
}
