package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.OwnerOnly
import dev.nanabell.jda.command.manager.context.ICommandContext

@OwnerOnly
@Command("owner", "Owner Only Command Example")
class OwnerOnlyCommand : ICommand{

    override fun execute(context: ICommandContext) {
        println("Owner Only Command Executed")
    }

}
