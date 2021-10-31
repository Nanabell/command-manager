package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandException
import dev.nanabell.jda.command.manager.context.ICommandContext
import kotlin.jvm.Throws

sealed interface IBaseCommand<T : ICommandContext> {

    @Throws(CommandException::class)
    fun execute(context: T)

}
