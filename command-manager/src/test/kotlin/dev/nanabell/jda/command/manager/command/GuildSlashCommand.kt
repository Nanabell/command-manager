package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.IGuildCommandContext

@Command("guild", "Example Guild Slash Command")
class GuildSlashCommand : IGuildSlashCommand {

    override fun execute(context: IGuildCommandContext) {
    }

}
