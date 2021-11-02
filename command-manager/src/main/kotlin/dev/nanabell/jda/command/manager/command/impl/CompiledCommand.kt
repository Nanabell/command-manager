package dev.nanabell.jda.command.manager.command.impl

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.ISlashCommand
import net.dv8tion.jda.api.Permission
import kotlin.reflect.KClass

data class CompiledCommand(
    val command: ICommand,
    val commandPath: String,
    val name: String,
    val description: String,
    val guildOnly: Boolean,
    val requirePermission: Boolean,
    val subcommandOf: KClass<out ICommand>?,
    val ownerOnly: Boolean,
    val userPermission: Array<Permission>,
    val botPermission: Array<Permission>,
) {
    val isSlashCommand: Boolean = command is ISlashCommand

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompiledCommand

        if (command != other.command) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (guildOnly != other.guildOnly) return false
        if (requirePermission != other.requirePermission) return false
        if (subcommandOf != other.subcommandOf) return false
        if (ownerOnly != other.ownerOnly) return false
        if (!userPermission.contentEquals(other.userPermission)) return false
        if (!botPermission.contentEquals(other.botPermission)) return false
        if (isSlashCommand != other.isSlashCommand) return false
        if (commandPath != other.commandPath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = command.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + guildOnly.hashCode()
        result = 31 * result + requirePermission.hashCode()
        result = 31 * result + (subcommandOf?.hashCode() ?: 0)
        result = 31 * result + ownerOnly.hashCode()
        result = 31 * result + userPermission.contentHashCode()
        result = 31 * result + botPermission.contentHashCode()
        result = 31 * result + isSlashCommand.hashCode()
        result = 31 * result + commandPath.hashCode()
        return result
    }

    override fun toString(): String {
        return "${command::class.simpleName}(commandPath='/$commandPath', name='$name', description='$description', guildOnly=$guildOnly, requirePermission=$requirePermission, subcommandOf=$subcommandOf, ownerOnly=$ownerOnly, userPermission=${userPermission.contentToString()}, botPermission=${botPermission.contentToString()}, isSlashCommand=$isSlashCommand)"
    }


}
