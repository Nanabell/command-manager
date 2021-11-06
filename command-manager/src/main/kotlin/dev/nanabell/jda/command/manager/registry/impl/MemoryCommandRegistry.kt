package dev.nanabell.jda.command.manager.registry.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.registry.ICommandRegistry
import dev.nanabell.jda.command.manager.registry.exception.CommandNotUniqueException
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Predicate
import kotlin.concurrent.read
import kotlin.concurrent.write

class MemoryCommandRegistry : ICommandRegistry {

    private val lock = ReentrantReadWriteLock()
    private val commands: MutableSet<CompiledCommand> = mutableSetOf()

    override val size: Int get() = commands.size

    override fun registerCommand(command: CompiledCommand, override: Boolean) {
        val existing = findCommand(command.commandPath)
        if (existing != null) {
            if (!override)  throw CommandNotUniqueException(command, existing)

            deregisterCommand(existing)
        }

        lock.write { commands.add(command) }
    }

    override fun deregisterCommand(command: CompiledCommand) {
        lock.write { commands.remove(command) }
    }

    override fun findCommand(commandPath: String): CompiledCommand? {
        return lock.read { commands.find { it.commandPath == commandPath } }
    }

    override fun findTextCommand(commandPath: String, predicate: Predicate<CompiledCommand>): CompiledCommand? {
        return lock.read { commands.filter { !it.isSlashCommand }.filter { predicate.test(it) }.find { it.commandPath == commandPath } }
    }

    override fun findSlashCommand(commandPath: String, predicate: Predicate<CompiledCommand>): CompiledCommand? {
        return lock.read { commands.filter { it.isSlashCommand }.filter { predicate.test(it) }.find { it.commandPath == commandPath } }
    }

    override fun getAll(): Collection<CompiledCommand> {
        return commands
    }
}
