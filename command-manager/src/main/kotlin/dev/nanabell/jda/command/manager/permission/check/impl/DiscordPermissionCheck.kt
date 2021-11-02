package dev.nanabell.jda.command.manager.permission.check.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.permission.check.IPermissionCheck
import dev.nanabell.jda.command.manager.permission.check.PermissionResult
import net.dv8tion.jda.api.entities.GuildChannel
import org.slf4j.LoggerFactory

class DiscordPermissionCheck(private val target: Target) : IPermissionCheck {

    private val logger = LoggerFactory.getLogger(DiscordPermissionCheck::class.java)

    override fun check(command: CompiledCommand, context: ICommandContext): PermissionResult {
        val (requiredPermissions, member) = when (target) {
            Target.MEMBER -> command.userPermission to context.member
            Target.SELF -> command.botPermission to context.selfMember
        }

        if (requiredPermissions.isEmpty())
            return PermissionResult.success()

        if (!context.isFromGuild) {
            logger.error("Received ${this::class.simpleName} request for Command $command outside of a GuildContext. @UserPermission / @BotPermission on a Global Command? Execution has been canceled.")
            return PermissionResult.fail("Invalid DiscordPermission Check on Private Context. Contact Bot Owner")
        }

        if (context.hasPermission(member!!, context.channel as GuildChannel, *requiredPermissions))
            return PermissionResult.success()

        return PermissionResult.fail(
            when (target) {
                Target.MEMBER -> "You Are"
                Target.SELF -> "I am"
            } + " missing the required Discord Permission/s ${requiredPermissions.joinToString()}"
        )
    }

    enum class Target {
        MEMBER,
        SELF
    }
}
