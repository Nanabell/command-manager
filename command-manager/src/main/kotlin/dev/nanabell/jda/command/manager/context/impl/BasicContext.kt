package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.permission.Permission

class BasicContext(
    override val arguments: Array<String>,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val selfUserId: Long
) : ICommandContext {

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        throw UnsupportedOperationException()
    }

    override fun reply(message: String) {
        throw UnsupportedOperationException()
    }

}
