package dev.nanabell.jda.command.manager.command.impl

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.IBaseCommand
import dev.nanabell.jda.command.manager.command.IGuildTextCommand
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.command.exception.MissingAnnotationException
import dev.nanabell.jda.command.manager.context.ICommandContext
import kotlin.reflect.KClass

class CompiledCommand(val command: IBaseCommand<out ICommandContext>) {

    val name: String
    val description: String
    val isGuildCommand: Boolean = command is IGuildTextCommand
    val isSlashCommand: Boolean = command is ISlashCommand
    val subcommandOf: KClass<out IBaseCommand<out ICommandContext>>
    var commandPath: String

    init {
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw MissingAnnotationException(command)
        }

        val commandA = command::class.java.getAnnotation(Command::class.java)

        name = commandA.name
        description = commandA.description
        subcommandOf = commandA.subCommandOf
        commandPath = name
    }

    override fun toString(): String {
        return "${command::class.qualifiedName} [path=/$commandPath, guildOnly=$isGuildCommand, isSlash=$isSlashCommand]"
    }
}
