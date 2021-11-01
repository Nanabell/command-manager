package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.OwnerOnly
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@OwnerOnly
@Command("owner", "Owner Only Command Example")
class OwnerOnlyCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
        println("Owner Only Command Executed")
    }

}
