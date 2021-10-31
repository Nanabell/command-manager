package dev.nanabell.jda.command.manager.exception

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand

class MissingParentException(compiled: CompiledCommand) : Exception("Parent Class ${compiled.subcommandOf} is not registered with the current Command Manager!") {
}
