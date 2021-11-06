package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.TestCommandContext
import dev.nanabell.jda.command.manager.permission.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class UserRequireAdminCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Admin Executed Command")
    }

}
