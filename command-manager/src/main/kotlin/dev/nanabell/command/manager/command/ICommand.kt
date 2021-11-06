package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.exception.CommandException
import dev.nanabell.command.manager.command.exception.CommandRejectedException
import dev.nanabell.command.manager.context.ICommandContext

interface ICommand<in T : ICommandContext>{

    @Throws(CommandException::class)
    suspend fun execute(context: T)

    fun onReject(context: T, e: CommandRejectedException) {
        context.reply("Your Command has been rejected!\n`${e.message}`")
    }

}
