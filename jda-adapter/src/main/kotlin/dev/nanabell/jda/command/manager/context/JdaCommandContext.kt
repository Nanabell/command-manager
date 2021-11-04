package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.permission.Permission
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import dev.nanabell.jda.command.manager.permission.Permission.Companion as CommandPermission
import net.dv8tion.jda.api.Permission as JdaPermission

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
class JdaCommandContext(
    override val ownerIds: Set<Long>,
    override val arguments: Array<String>,
    val user: User,
    val channel: MessageChannel,
    val guild: Guild?,
) : ICommandContext {

    private val jda: JDA = user.jda

    override val authorId: Long = user.idLong
    override val channelId: Long = channel.idLong
    override val guildId: Long? = guild?.idLong
    override val selfUserId: Long = jda.selfUser.idLong

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        val member = guild?.getMemberById(memberId) ?: return true
        return member.hasPermission(channel as GuildChannel, JdaPermission.getPermissions(CommandPermission.getRaw(*permission)))
    }

    override fun reply(message: String) {
        channel.sendMessage(message).queue()
    }

}
