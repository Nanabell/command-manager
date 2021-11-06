package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("rejected", "Example Rejected Command")
class RejectedCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        throw CommandRejectedException("Demo Command")
    }

}
