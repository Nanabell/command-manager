package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("rejected", "Example Rejected Command")
class RejectedCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
        throw CommandRejectedException("Demo Command")
    }

}
