package dev.nanabell.jda.command.manager.command.impl

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.IGuildSlashCommand
import dev.nanabell.jda.command.manager.command.IGuildTextCommand
import dev.nanabell.jda.command.manager.command.ISlashCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import kotlin.reflect.KClass

data class CompiledCommand(
    val command: ICommand<out ICommandContext>,
    val name: String,
    val description: String,
    val subcommandOf: KClass<out ICommand<out ICommandContext>>?,
    val ownerOnly: Boolean,
    val userPermission: Array<Permission>,
    val botPermission: Array<Permission>,
    val requirePermission: Boolean
) {

    var commandPath: String = name

    val isGuildCommand: Boolean = command is IGuildTextCommand || command is IGuildSlashCommand
    val isSlashCommand: Boolean = command is ISlashCommand || command is IGuildSlashCommand


    override fun toString(): String {
        return "${command::class.qualifiedName} [path=/$commandPath, guildOnly=$isGuildCommand, isSlash=$isSlashCommand]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompiledCommand

        if (command != other.command) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (subcommandOf != other.subcommandOf) return false
        if (ownerOnly != other.ownerOnly) return false
        if (!userPermission.contentEquals(other.userPermission)) return false
        if (!botPermission.contentEquals(other.botPermission)) return false
        if (requirePermission != other.requirePermission) return false
        if (commandPath != other.commandPath) return false
        if (isGuildCommand != other.isGuildCommand) return false
        if (isSlashCommand != other.isSlashCommand) return false

        return true
    }

    override fun hashCode(): Int {
        var result = command.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (subcommandOf?.hashCode() ?: 0)
        result = 31 * result + ownerOnly.hashCode()
        result = 31 * result + userPermission.contentHashCode()
        result = 31 * result + botPermission.contentHashCode()
        result = 31 * result + requirePermission.hashCode()
        result = 31 * result + commandPath.hashCode()
        result = 31 * result + isGuildCommand.hashCode()
        result = 31 * result + isSlashCommand.hashCode()
        return result
    }

}
