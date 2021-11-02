package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.BotPermission
import dev.nanabell.jda.command.manager.context.IGuildTextCommandContext
import net.dv8tion.jda.api.Permission

@BotPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command")
class BotRequireAdminCommand : IGuildTextCommand {

    override fun execute(context: IGuildTextCommandContext) {
        println("Admin Executed Command")
    }

}
