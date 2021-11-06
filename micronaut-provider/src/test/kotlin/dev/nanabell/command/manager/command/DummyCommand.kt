package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

@Command("dummy", "Dummy Command Description")
class DummyCommand : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
    }
}
