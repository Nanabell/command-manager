package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@Command("subsub", "Example Sub Sub SlashCommnd", subCommandOf = SubSlashCommand::class)
class SubSubSlashCommand : ISlashCommand {
    override fun execute(context: ISlashCommandContext) {
        println("This is a SubGroup Sub SlashCommand")
    }
}
