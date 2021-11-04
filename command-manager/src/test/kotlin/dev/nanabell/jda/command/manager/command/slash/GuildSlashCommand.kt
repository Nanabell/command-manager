package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("guild", "Example Guild Slash Command", guildOnly = true)
class GuildSlashCommand : ITestSlashCommand {

    override fun execute(context: TestCommandContext) {
    }

}
