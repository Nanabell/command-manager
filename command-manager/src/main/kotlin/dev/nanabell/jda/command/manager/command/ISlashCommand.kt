package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ISlashCommandContext
import net.dv8tion.jda.api.interactions.commands.build.OptionData

interface ISlashCommand : IBaseCommand<ISlashCommandContext> {

    fun setOptions(): Collection<OptionData> {
        return emptyList()
    }

}
