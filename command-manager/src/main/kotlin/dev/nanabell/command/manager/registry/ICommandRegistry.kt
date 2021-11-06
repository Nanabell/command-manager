package dev.nanabell.command.manager.registry

import dev.nanabell.command.manager.command.impl.CompiledCommand
import java.util.function.Predicate

interface ICommandRegistry {

    val size: Int

    fun registerCommand(command: CompiledCommand, override: Boolean = false)
    fun deregisterCommand(command: CompiledCommand)

    fun findCommand(commandPath: String): CompiledCommand?
    fun findTextCommand(commandPath: String, predicate: Predicate<CompiledCommand> = Predicate { true }): CompiledCommand?
    fun findSlashCommand(commandPath: String, predicate: Predicate<CompiledCommand> = Predicate { true }): CompiledCommand?

    fun getAll(): Collection<CompiledCommand>

}
