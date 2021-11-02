package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("illegal", "Illegal Command Requiring Permission as Global Command")
class IllegalDiscordPermissionCommand : ICommand {
    override fun execute(context: ICommandContext) {
        println("This should not be executed normally!")
    }
}
