package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.permission.Permission

interface ICommandContext {

    val arguments: Array<String>

    val authorId: Long
    val channelId: Long
    val guildId: Long?
    val selfUserId: Long

    val isFromGuild: Boolean get() = guildId != null

    fun hasPermission(memberId: Long, vararg permission: Permission): Boolean

    fun reply(message: String)
}
