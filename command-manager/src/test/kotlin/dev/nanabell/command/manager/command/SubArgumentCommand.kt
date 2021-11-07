package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext
import java.util.concurrent.atomic.AtomicInteger

@SubCommandOf(DummyCommand::class)
@Command("argument", "Sub Command with Arguments")
class SubArgumentCommand(private val arguments: AtomicInteger) : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
        println(context.arguments.joinToString())
        arguments.set(context.arguments.size)
    }
}
