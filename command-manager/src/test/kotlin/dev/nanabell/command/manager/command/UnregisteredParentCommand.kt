package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(UnregisteredCommand::class)
@Command("no-parent", "This Commands Parent does not Exist as a Command")
class UnregisteredParentCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
