package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext
import java.util.concurrent.atomic.AtomicInteger

@Command("count", "Count the number of arguments into an Atomic Counter")
class ArgumentCounterCommand(private val count: AtomicInteger) : ITextCommand {
    override fun execute(context: ITextCommandContext) {
        count.set(context.arguments.size)
    }
}
