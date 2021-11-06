package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.BotPermission
import dev.nanabell.jda.command.manager.context.TestCommandContext
import dev.nanabell.jda.command.manager.permission.Permission

@BotPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class BotRequireAdminCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Admin Executed Command")
    }

}
