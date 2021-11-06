package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("fail", "This is a second Example Command")
class FailingCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        throw Exception("This Command will always Fail")
    }

}
