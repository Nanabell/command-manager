package dev.nanabell.jda.command.manager.command.slash

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ISlashCommandContext

@SubCommandOf(SubSubSlashCommand::class)
@Command("invalid", "Invalid Sub Slash Command, Depth is more than 3!")
class InvalidSubSlashCommand : ISlashCommand {
    override fun execute(context: ISlashCommandContext) {
    }
}
