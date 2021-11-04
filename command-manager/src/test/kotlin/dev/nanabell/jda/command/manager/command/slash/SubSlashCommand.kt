package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.TestCommandContext

@SubCommandOf(SlashCommand::class)
@Command("sub", "Example Sub Slash Command")
class SubSlashCommand : ITestSlashCommand {

    override fun execute(context: TestCommandContext) {
        println("This is a Sub Command of /slash")
    }

}
