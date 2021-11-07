package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.permission.Permission

interface ICommandContext {

    val authorId: Long
    val channelId: Long
    val guildId: Long?
    val selfUserId: Long

    val isFromGuild: Boolean get() = guildId != null

    fun hasPermission(memberId: Long, vararg permission: Permission): Boolean

    fun reply(message: String)
}
