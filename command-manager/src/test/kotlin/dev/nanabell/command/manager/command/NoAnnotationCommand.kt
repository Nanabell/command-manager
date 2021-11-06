package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

class NoAnnotationCommand : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {

    }
}
