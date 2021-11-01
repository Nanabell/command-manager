package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import org.slf4j.LoggerFactory

class StaticCommandProvider(private val commands: Collection<ICommand<out ICommandContext>>) : ICommandProvider {

    private val logger = LoggerFactory.getLogger(StaticCommandProvider::class.java)

    override fun provide(): Collection<ICommand<out ICommandContext>> {
        logger.trace("Providing ${commands.size} Commands")
        return commands
    }

}
