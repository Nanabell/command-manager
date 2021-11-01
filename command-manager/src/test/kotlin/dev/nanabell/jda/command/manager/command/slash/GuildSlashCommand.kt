package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.IGuildSlashCommand
import dev.nanabell.jda.command.manager.context.IGuildSlashCommandContext

@Command("guild", "Example Guild Slash Command")
class GuildSlashCommand : IGuildSlashCommand {

    override fun execute(context: IGuildSlashCommandContext) {
    }

}
