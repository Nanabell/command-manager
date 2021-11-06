package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

@Command("example", "Example Command")
class DummyCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Example Commend Executed with arugments: ${context.arguments.joinToString()}")
    }

}
