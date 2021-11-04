package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("abort", "Example Abort Command")
class AbortCommand : ITestCommand {

    override fun execute(context: TestCommandContext) {
        throw CommandAbortedException("Example Abort Command")
    }

}
