package dev.nanabell.command.manager.command.slash

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.context.TestCommandContext

@Command("guild", "Example Guild Slash Command", guildOnly = true)
class GuildSlashCommand : ITestSlashCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
