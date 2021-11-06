package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(RecursiveCommand2::class)
@Command("recurse1", "Example Recursive Command 1")
class RecursiveCommand1 : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
