package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

class UnregisteredCommand : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
    }
}
