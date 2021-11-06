package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("guild", "Example Guild Only Command", guildOnly = true)
class GuildCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Executed!")
    }

}
