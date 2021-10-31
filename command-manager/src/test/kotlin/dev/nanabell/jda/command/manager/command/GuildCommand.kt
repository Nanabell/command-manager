package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.IGuildCommandContext

@Command("guild", "Example Guild Only Command")
class GuildCommand : IGuildTextCommand {

    override fun execute(context: IGuildCommandContext) {
        println("Executed!")
    }

}
