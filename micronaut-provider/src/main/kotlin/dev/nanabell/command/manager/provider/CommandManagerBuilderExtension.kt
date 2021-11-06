package dev.nanabell.command.manager.provider

import dev.nanabell.command.manager.CommandManagerBuilder
import io.micronaut.context.ApplicationContext

@Suppress("unused")
fun CommandManagerBuilder.setMicronautProvider(context: ApplicationContext): CommandManagerBuilder {
    this.setCommandProvider(MicronautCommandProvider(context))
    return this
}
