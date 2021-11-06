package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.ICommandContext

interface ISlashCommand<in T : ICommandContext> : ICommand<T> {

    fun setOptions(): Collection<Any> {
        TODO("Handle OptionData for SlashCommands")
    }

}
