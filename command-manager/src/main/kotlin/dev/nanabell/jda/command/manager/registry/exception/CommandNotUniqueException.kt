package dev.nanabell.jda.command.manager.registry.exception

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand

class CommandNotUniqueException(command: CompiledCommand, conflict: CompiledCommand) : Exception("Command $command is not Unique in comparison to $conflict!") {
}
