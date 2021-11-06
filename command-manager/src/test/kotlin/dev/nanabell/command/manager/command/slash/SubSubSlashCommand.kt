package dev.nanabell.command.manager.command.slash

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(SubSlashCommand::class)
@Command("subsub", "Example Sub Sub SlashCommnd")
class SubSubSlashCommand : ITestSlashCommand {
    override suspend fun execute(context: TestCommandContext) {
        println("This is a SubGroup Sub SlashCommand")
    }
}
