package dev.nanabell.jda.command.manager.compile.impl

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.command.annotation.BotPermission
import dev.nanabell.jda.command.manager.command.annotation.OwnerOnly
import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.command.annotation.UserPermission
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.ICommandCompiler
import dev.nanabell.jda.command.manager.compile.exception.MissingCommandAnnotationException
import dev.nanabell.jda.command.manager.compile.exception.RecursiveCommandPathException
import dev.nanabell.jda.command.manager.compile.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.permission.Permission
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

class AnnotationCommandCompiler : ICommandCompiler {

    private val logger = LoggerFactory.getLogger(AnnotationCommandCompiler::class.java)

    override fun compile(command: ICommand<ICommandContext>): CompiledCommand {
        logger.debug("Compiling Command ${command::class.qualifiedName}")
        if (!command::class.java.isAnnotationPresent(Command::class.java)) {
            throw MissingCommandAnnotationException(command::class)
        }

        val commandAnnotation = command::class.java.getAnnotation(Command::class.java)
        logger.trace("${command::class.qualifiedName} | name=${commandAnnotation.name}, description=${commandAnnotation.description}, guildOnly=${commandAnnotation.guildOnly}, requirePermission${commandAnnotation.requirePermission}")

        val ownerOnly = command::class.java.isAnnotationPresent(OwnerOnly::class.java)
        logger.trace("${command::class.qualifiedName} | ownerOnly=${ownerOnly}")

        var subCommandOf: KClass<out ICommand<*>>? = null
        if (command::class.java.isAnnotationPresent(SubCommandOf::class.java)) {
            subCommandOf = command::class.java.getAnnotation(SubCommandOf::class.java).subCommandOf
            logger.trace("${command::class.qualifiedName} | subCommandOf=${subCommandOf.qualifiedName}")
        }

        var userPermissions = emptyArray<Permission>()
        if (command::class.java.isAnnotationPresent(UserPermission::class.java)) {
            userPermissions = command::class.java.getAnnotation(UserPermission::class.java).permissions
            logger.trace("${command::class.qualifiedName} | userPermissions=${userPermissions}")
        }

        var botPermissions = emptyArray<Permission>()
        if (command::class.java.isAnnotationPresent(BotPermission::class.java)) {
            botPermissions = command::class.java.getAnnotation(BotPermission::class.java).permissions
            logger.trace("${command::class.qualifiedName} | botPermissions=${botPermissions}")
        }

        val commandPath = buildCommandPath(command::class, subCommandOf, mutableSetOf())
        logger.trace("${command::class.qualifiedName} | commandPath=/${commandPath}")

        if (command is ISlashCommand) {
            val depth = commandPath.count { it == '/' }
            if (depth > 2) {
                throw SlashCommandDepthException(command::class, depth)
            }
        }

        return CompiledCommand(
            command,
            commandPath,
            commandAnnotation.name,
            commandAnnotation.description,
            commandAnnotation.guildOnly,
            commandAnnotation.requirePermission,
            subCommandOf,
            ownerOnly,
            userPermissions,
            botPermissions
        ).also {
            logger.debug("Compiled Command: $it")
        }
    }

    private fun buildCommandPath(command: KClass<out ICommand<*>>, subcommandOf: KClass<out ICommand<*>>?, seen: MutableSet<KClass<out ICommand<*>>>): String {
        if (seen.contains(command)) throw RecursiveCommandPathException(command, seen)
        seen.add(command)

        val name = command.java.getAnnotation(Command::class.java)?.name ?: throw MissingCommandAnnotationException(command)
        if (subcommandOf == null) return name

        val path = buildCommandPath(subcommandOf, subcommandOf.java.getAnnotation(SubCommandOf::class.java)?.subCommandOf, seen)
        return "$path/$name"
    }
}
