package dev.nanabell.command.manager.compile.exception

import dev.nanabell.command.manager.command.Command
import dev.nanabell.command.manager.command.ICommand
import kotlin.reflect.KClass

open class CommandCompileException(override val message: String) : Exception()
class MissingCommandAnnotationException(command: KClass<out ICommand<*>>) : CommandCompileException("Command Class ${command.qualifiedName} is missing @${Command::class.qualifiedName} Annotation")
class RecursiveCommandPathException(command: KClass<out ICommand<*>>, commands: Set<KClass<out ICommand<*>>>) : CommandCompileException("${command.qualifiedName} has Recursive Command Path! ${commands.map { it.qualifiedName }.joinToString(" -> ")}")
class SlashCommandDepthException(command: KClass<out ICommand<*>>, depth: Int) : CommandCompileException("Command ${command.qualifiedName} has a Command Path depth of $depth. Discord only supports 2 (SubCommand and SubCommandGroup)")
