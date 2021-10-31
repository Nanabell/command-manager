package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@Command("invalid", "Invalid Sub Slash Command, Depth is more than 3!", subCommandOf = SubSubSlashCommand::class)
class InvalidSubSlashCommand : ISlashCommand {
    override fun execute(context: ISlashCommandContext) {
    }
}
