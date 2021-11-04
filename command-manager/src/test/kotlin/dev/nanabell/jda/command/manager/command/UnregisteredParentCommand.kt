package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.TestCommandContext

@SubCommandOf(UnregisteredCommand::class)
@Command("no-parent", "This Commands Parent does not Exist as a Command")
class UnregisteredParentCommand : ITestCommand {

    override fun execute(context: TestCommandContext) {
    }

}
