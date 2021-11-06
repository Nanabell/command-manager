package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.context.TestCommandContext

@Command("guild", "Example Guild Only Command", guildOnly = true)
class GuildCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
        println("Executed!")
    }

}
