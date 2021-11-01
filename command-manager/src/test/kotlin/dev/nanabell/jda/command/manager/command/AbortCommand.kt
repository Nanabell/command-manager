package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("abort", "Example Abort Command")
class AbortCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
        throw CommandAbortedException("Example Abort Command")
    }

}
