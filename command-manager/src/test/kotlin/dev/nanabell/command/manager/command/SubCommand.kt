package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(DummyCommand::class)
@Command("sub", "Sub Command for Example Command")
class SubCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
