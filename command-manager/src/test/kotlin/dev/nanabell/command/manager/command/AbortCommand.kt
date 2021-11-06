package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.exception.CommandAbortedException
import dev.nanabell.command.manager.context.TestCommandContext

@Command("abort", "Example Abort Command")
class AbortCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        throw CommandAbortedException("Example Abort Command")
    }

}
