package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext

@SubCommandOf(SlashCommand::class)
@Command("sub", "Example Sub Slash Command")
class SubSlashCommand : ISlashCommand {

    override fun execute(context: ICommandContext) {
        println("This is a Sub Command of /slash")
    }

}
