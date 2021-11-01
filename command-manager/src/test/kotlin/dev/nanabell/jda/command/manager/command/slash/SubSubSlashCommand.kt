package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@SubCommandOf(SubSlashCommand::class)
@Command("subsub", "Example Sub Sub SlashCommnd")
class SubSubSlashCommand : ISlashCommand {
    override fun execute(context: ISlashCommandContext) {
        println("This is a SubGroup Sub SlashCommand")
    }
}
