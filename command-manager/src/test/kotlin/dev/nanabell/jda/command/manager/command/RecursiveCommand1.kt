package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.TestCommandContext

@SubCommandOf(RecursiveCommand2::class)
@Command("recurse1", "Example Recursive Command 1")
class RecursiveCommand1 : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
