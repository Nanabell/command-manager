package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

@Command("fail", "This is a second Example Command")
class FailingCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        throw Exception("This Command will always Fail")
    }

}
