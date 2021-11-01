package dev.nanabell.jda.command.manager.command.impl

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.exception.MissingAnnotationException
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import kotlin.reflect.KClass

class CompiledCommand(val command: IBaseCommand<out ICommandContext>) {

    val name: String
    val description: String
    val subcommandOf: KClass<out IBaseCommand<out ICommandContext>>
    val ownerOnly: Boolean
    val userPermission: Array<Permission>
    val botPermission: Array<Permission>

    var commandPath: String

    val isGuildCommand: Boolean = command is IGuildTextCommand || command is IGuildSlashCommand
    val isSlashCommand: Boolean = command is ISlashCommand || command is IGuildSlashCommand

    init {
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw MissingAnnotationException(command)
        }

        val annotation = command::class.java.getAnnotation(Command::class.java)

        name = annotation.name
        description = annotation.description
        subcommandOf = annotation.subCommandOf
        ownerOnly = annotation.ownerOnly
        userPermission = annotation.userPermission
        botPermission = annotation.botPermission

        commandPath = name
    }

    override fun toString(): String {
        return "${command::class.qualifiedName} [path=/$commandPath, guildOnly=$isGuildCommand, isSlash=$isSlashCommand]"
    }
}
