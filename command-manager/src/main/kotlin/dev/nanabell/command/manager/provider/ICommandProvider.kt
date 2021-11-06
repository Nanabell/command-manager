package dev.nanabell.command.manager.provider

import dev.nanabell.command.manager.command.ICommand

fun interface ICommandProvider {

    fun provide(): Collection<ICommand<*>>

}
