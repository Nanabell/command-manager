package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("abort", "Example Abort Comamnd")
class AbortCommand : ITextCommand {

    override fun execute(context: ICommandContext) {
        throw CommandAbortedException()
    }

}
