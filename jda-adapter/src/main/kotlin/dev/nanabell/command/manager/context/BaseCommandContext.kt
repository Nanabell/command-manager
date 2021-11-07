package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.permission.Permission
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")
open class BaseCommandContext(
    val user: User,
    val channel: MessageChannel,
    val guild: Guild?,
    val responseNumber: Long
) : ICommandContext {

    val jda: JDA = user.jda
    val member = guild?.getMember(user)

    override val authorId: Long = user.idLong
    override val channelId: Long = channel.idLong
    override val guildId: Long? = guild?.idLong
    override val selfUserId: Long = jda.selfUser.idLong

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        return member?.hasPermission(
            channel as GuildChannel,
            net.dv8tion.jda.api.Permission.getPermissions(Permission.getRaw(*permission))
        ) ?: true
    }

    override fun reply(message: String) {
        channel.sendMessage(message).queue()
    }

}
