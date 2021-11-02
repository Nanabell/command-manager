package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.IGuildTextCommandContext
import net.dv8tion.jda.api.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command")
class UserRequireAdminCommand : IGuildTextCommand {

    override fun execute(context: IGuildTextCommandContext) {
        println("Admin Executed Command")
    }

}
