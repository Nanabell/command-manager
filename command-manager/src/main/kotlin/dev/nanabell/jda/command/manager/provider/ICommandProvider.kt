package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.command.IBaseCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface ICommandProvider {

    fun provide(): Collection<IBaseCommand<out ICommandContext>>

}
