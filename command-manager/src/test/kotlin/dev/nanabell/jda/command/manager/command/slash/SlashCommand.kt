package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@Command("slash", "Example Slash Command")
class SlashCommand : ISlashCommand {

    override fun execute(context: ISlashCommandContext) {
        println("Example Slash Command")
    }

}
