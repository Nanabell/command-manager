package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.exception.CommandRejectedException
import dev.nanabell.command.manager.context.TestCommandContext

@Command("rejected", "Example Rejected Command")
class RejectedCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        throw CommandRejectedException("Demo Command")
    }

}
