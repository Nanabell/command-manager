package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.OwnerOnly
import dev.nanabell.command.manager.context.TestCommandContext

@OwnerOnly
@Command("owner", "Owner Only Command Example")
class OwnerOnlyCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Owner Only Command Executed")
    }

}
