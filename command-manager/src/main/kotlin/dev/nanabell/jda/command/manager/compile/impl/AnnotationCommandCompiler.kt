package dev.nanabell.jda.command.manager.compile.impl

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.compile.exception.CommandCompileException
import dev.nanabell.jda.command.manager.context.ICommandContext
import kotlin.reflect.KClass

class AnnotationCommandCompiler : ICommandCompiler {

    override fun compile(command: ICommand<out ICommandContext>): CompiledCommand {
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw CommandCompileException("Missing required @Command Annotation from class: ${command::class.qualifiedName}!")
        }

        val commandAnnotation = command::class.java.getAnnotation(Command::class.java)

        var subCommandOf: KClass<out ICommand<out ICommandContext>>? = null
        if (command::class.java.isAnnotationPresent(SubCommandOf::class.java)) {
            subCommandOf = command::class.java.getAnnotation(SubCommandOf::class.java).subCommandOf
        }

        return CompiledCommand(
            command,
            commandAnnotation.name,
            commandAnnotation.description,
            subCommandOf,
            commandAnnotation.ownerOnly,
            commandAnnotation.userPermission,
            commandAnnotation.botPermission
        )
    }

}
