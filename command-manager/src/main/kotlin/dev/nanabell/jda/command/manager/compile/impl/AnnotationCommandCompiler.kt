package dev.nanabell.jda.command.manager.compile.impl

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.annotation.*
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.compile.exception.CommandCompileException
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import kotlin.reflect.KClass

class AnnotationCommandCompiler : ICommandCompiler {

    override fun compile(command: ICommand): CompiledCommand {
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw CommandCompileException("Missing required @Command Annotation from class: ${command::class.qualifiedName}!")
        }

        val commandAnnotation = command::class.java.getAnnotation(Command::class.java)
        val ownerOnly = command::class.java.isAnnotationPresent(OwnerOnly::class.java)

        var subCommandOf: KClass<out ICommand>? = null
        if (command::class.java.isAnnotationPresent(SubCommandOf::class.java)) {
            subCommandOf = command::class.java.getAnnotation(SubCommandOf::class.java).subCommandOf
        }

        var userPermissions = emptyArray<Permission>()
        if (command::class.java.isAnnotationPresent(UserPermission::class.java)) {
            userPermissions = command::class.java.getAnnotation(UserPermission::class.java).permissions
        }

        var botPermissions = emptyArray<Permission>()
        if (command::class.java.isAnnotationPresent(BotPermission::class.java)) {
            botPermissions = command::class.java.getAnnotation(BotPermission::class.java).permissions
        }

        return CompiledCommand(
            command,
            commandAnnotation.name,
            commandAnnotation.description,
            commandAnnotation.guildOnly,
            commandAnnotation.requirePermission,
            subCommandOf,
            ownerOnly,
            userPermissions,
            botPermissions
        )
    }

}
