package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.context.ICommandContext

interface ICommand {

    @Throws(CommandException::class)
    fun execute(context: ICommandContext)

    fun onReject(context: ICommandContext, e: CommandRejectedException) {
        context.reply("Your Command has been rejected!\n`${e.message}`")
    }

}
