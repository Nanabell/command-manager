package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.IGuildTextCommandContext

@Command("guild", "Example Guild Only Command")
class GuildCommand : IGuildTextCommand {

    override fun execute(context: IGuildTextCommandContext) {
        println("Executed!")
    }

}
