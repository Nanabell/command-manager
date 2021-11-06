package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.BotPermission
import dev.nanabell.command.manager.context.TestCommandContext
import dev.nanabell.command.manager.permission.Permission

@BotPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class BotRequireAdminCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Admin Executed Command")
    }

}
