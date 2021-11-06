package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("example", "Example Command")
class DummyCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Example Commend Executed with arugments: ${context.arguments.joinToString()}")
    }

}
