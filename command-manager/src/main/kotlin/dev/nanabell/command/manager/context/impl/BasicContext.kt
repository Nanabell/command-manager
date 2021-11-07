package dev.nanabell.command.manager.context.impl

import dev.nanabell.command.manager.context.ISlashCommandContext
import dev.nanabell.command.manager.context.ITextCommandContext
import dev.nanabell.command.manager.permission.Permission

class BasicContext(
    override val content: String,
    override val arguments: Array<String>,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val selfUserId: Long
) : ITextCommandContext, ISlashCommandContext {

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        throw UnsupportedOperationException()
    }

    override fun reply(message: String) {
        throw UnsupportedOperationException()
    }

}
