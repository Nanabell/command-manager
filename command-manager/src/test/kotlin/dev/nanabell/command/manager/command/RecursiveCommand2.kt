package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(RecursiveCommand1::class)
@Command("recurse2", "Example Recursive Command 2")
class RecursiveCommand2 : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
