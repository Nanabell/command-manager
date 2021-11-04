package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.TestCommandContext

@SubCommandOf(RecursiveCommand1::class)
@Command("recurse2", "Example Recursive Command 2")
class RecursiveCommand2 : ITestCommand {

    override fun execute(context: TestCommandContext) {
    }

}
