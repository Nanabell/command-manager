package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.TestCommandContext
import dev.nanabell.jda.command.manager.permission.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("illegal", "Illegal Command Requiring Permission as Global Command")
class IllegalDiscordPermissionCommand : ITestCommand {
    override fun execute(context: TestCommandContext) {
        println("This should not be executed normally!")
    }
}
