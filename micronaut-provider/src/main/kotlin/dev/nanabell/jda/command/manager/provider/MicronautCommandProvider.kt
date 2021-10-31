package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.IBaseCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import io.micronaut.context.ApplicationContext
import jakarta.inject.Singleton

@Singleton
class MicronautCommandProvider(private val context: ApplicationContext) : ICommandProvider {

    override fun provide(): Collection<IBaseCommand<out ICommandContext>> {
        return context.getBeansOfType(IBaseCommand::class.java)
    }

}
