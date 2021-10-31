package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@Command("sub", "Example Sub Slash Command", subCommandOf = SlashCommand::class)
class SubSlashCommand : ISlashCommand {

    override fun execute(context: ISlashCommandContext) {
        println("This is a Sub Command of /slash")
    }

}
