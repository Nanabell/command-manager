package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.UserPermission
import dev.nanabell.command.manager.context.TestCommandContext
import dev.nanabell.command.manager.permission.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class UserRequireAdminCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Admin Executed Command")
    }

}
