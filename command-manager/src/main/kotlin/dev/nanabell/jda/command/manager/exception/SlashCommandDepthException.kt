package dev.nanabell.jda.command.manager.exception

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.context.ICommandContext

class SlashCommandDepthException(depth: Int, command: ICommand<out ICommandContext>) :
    Exception("Command ${command::class.qualifiedName} has a Command Path depth of $depth. Discord only supports 2 (SubCommand and SubCommandGroup)") {
}
