package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class UserRequireAdminCommand : ICommand {

    override fun execute(context: ICommandContext) {
        println("Admin Executed Command")
    }

}
