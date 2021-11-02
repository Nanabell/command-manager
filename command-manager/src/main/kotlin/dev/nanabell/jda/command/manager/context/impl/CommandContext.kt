package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import java.util.*

sealed class CommandContext(
    override val author: User,
    override val member: Member?,
    override val channel: MessageChannel,
    override val guild: Guild?,
    override val ownerIds: Set<Long>
) : ICommandContext {

    override val uuid: UUID = UUID.randomUUID()

    override fun reply(message: String) {
        // TODO("Not yet implemented")
    }

    override fun hasPermission(member: Member, vararg permissions: Permission): Boolean {
        return member.hasPermission(*permissions)
    }

    override fun hasPermission(member: Member, guildChannel: GuildChannel, vararg permissions: Permission): Boolean {
        return member.hasPermission(guildChannel, *permissions)
    }

}
