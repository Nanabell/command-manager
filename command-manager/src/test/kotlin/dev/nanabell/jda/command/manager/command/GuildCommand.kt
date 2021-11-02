package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("guild", "Example Guild Only Command", guildOnly = true)
class GuildCommand : ICommand {

    override fun execute(context: ICommandContext) {
        println("Executed!")
    }

}
