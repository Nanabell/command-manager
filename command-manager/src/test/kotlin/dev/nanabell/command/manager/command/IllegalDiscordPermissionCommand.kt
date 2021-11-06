package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.UserPermission
import dev.nanabell.command.manager.context.TestCommandContext
import dev.nanabell.command.manager.permission.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("illegal", "Illegal Command Requiring Permission as Global Command")
class IllegalDiscordPermissionCommand : ITestCommand {
    override suspend fun execute(context: TestCommandContext) {
        println("This should not be executed normally!")
    }
}
