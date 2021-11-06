package dev.nanabell.command.manager.command.slash

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(SlashCommand::class)
@Command("sub", "Example Sub Slash Command")
class SubSlashCommand : ITestSlashCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("This is a Sub Command of /slash")
    }

}
