package dev.nanabell.jda.command.manager.exception

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand

class CommandPathLoopException(compiled: CompiledCommand) : Exception("Unable to load Command $compiled. CommandPath is Recursive!") {
}
