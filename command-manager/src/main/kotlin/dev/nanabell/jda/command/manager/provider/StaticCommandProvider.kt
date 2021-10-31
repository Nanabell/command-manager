package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.IBaseCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import org.slf4j.LoggerFactory

class StaticCommandProvider(private val commands: Collection<IBaseCommand<out ICommandContext>>) : ICommandProvider {

    private val logger = LoggerFactory.getLogger(StaticCommandProvider::class.java)

    override fun provide(): Collection<IBaseCommand<out ICommandContext>> {
        logger.trace("Providing ${commands.size} Commands")
        return commands
    }

}
