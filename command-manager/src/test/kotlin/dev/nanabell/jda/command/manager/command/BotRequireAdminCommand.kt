package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.BotPermission
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission

@BotPermission([Permission.ADMINISTRATOR])
@Command("admin", "Example Admin Command", guildOnly = true)
class BotRequireAdminCommand : ICommand {

    override fun execute(context: ICommandContext) {
        println("Admin Executed Command")
    }

}
