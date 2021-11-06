package dev.nanabell.command.manager.command.slash

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.context.TestCommandContext

@Command("slash", "Example Slash Command")
class SlashCommand : ITestSlashCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Example Slash Command")
    }

}
