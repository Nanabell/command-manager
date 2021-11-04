package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import io.micronaut.context.ApplicationContext
import jakarta.inject.Singleton

@Singleton
class MicronautCommandProvider(private val context: ApplicationContext) : ICommandProvider {

     override fun provide(): Collection<ICommand<*>> {
        return context.getBeansOfType(ICommand::class.java)
    }

}
