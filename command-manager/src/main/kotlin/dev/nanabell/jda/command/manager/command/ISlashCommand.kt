package dev.nanabell.jda.command.manager.command

import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface ISlashCommand : ICommand {

    fun setOptions(): Collection<OptionData> {
        return emptyList()
    }

}
