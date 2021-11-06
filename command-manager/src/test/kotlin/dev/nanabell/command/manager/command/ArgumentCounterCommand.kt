package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext
import java.util.concurrent.atomic.AtomicInteger

@Command("count", "Count the number of arguments into an Atomic Counter")
class ArgumentCounterCommand(private val count: AtomicInteger) : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
        count.set(context.arguments.size)
    }
}
