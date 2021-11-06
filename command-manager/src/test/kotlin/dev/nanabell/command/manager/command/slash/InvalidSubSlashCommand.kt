package dev.nanabell.command.manager.command.slash

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(SubSubSlashCommand::class)
@Command("invalid", "Invalid Sub Slash Command, Depth is more than 3!")
class InvalidSubSlashCommand : ITestSlashCommand {
    override suspend fun execute(context: TestCommandContext) {
    }
}
