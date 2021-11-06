package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.TestCommandContext

class UnregisteredCommand : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
    }
}
