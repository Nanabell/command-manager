package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.context.ICommandContext
import kotlin.jvm.Throws

sealed interface ICommand<T : ICommandContext> {

    @Throws(CommandException::class)
    fun execute(context: T)

    fun onReject(context: T, e: CommandRejectedException) {
        context.reply("Your Command has been rejected! `${e.message}`")
    }

}
