package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ITextCommand
import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.context.ITextCommandContext
import net.dv8tion.jda.api.Permission

@UserPermission([Permission.ADMINISTRATOR])
@Command("illegal", "Illegal Command Requiring Permission as Global Command")
class IllegalDiscordPermissionCommand : ITextCommand {
    override fun execute(context: ITextCommandContext) {
        println("This should not be executed normally!")
    }
}
