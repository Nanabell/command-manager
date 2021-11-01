package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface ICommandProvider {

    fun provide(): Collection<ICommand<out ICommandContext>>

}
