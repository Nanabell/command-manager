package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.OwnerOnly
import dev.nanabell.jda.command.manager.context.TestCommandContext

@OwnerOnly
@Command("owner", "Owner Only Command Example")
class OwnerOnlyCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Owner Only Command Executed")
    }

}
