package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("rejected", "Example Rejected Command")
class RejectedCommand : ITextCommand {

    override fun execute(context: ICommandContext) {
        throw CommandRejectedException("Demo Command")
    }

}
