package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

interface ISlashCommand<in T : ICommandContext> : ICommand<T> {

    fun setOptions(): Collection<Any> {
        TODO("Handle OptionData for SlashCommands")
    }

}
