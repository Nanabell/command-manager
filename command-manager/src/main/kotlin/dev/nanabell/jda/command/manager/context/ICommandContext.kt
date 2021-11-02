package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.interactions.Interaction
import java.util.*

interface ICommandContext {

    val uuid: UUID
    val ownerIds: Set<Long>

    val author: User
    val member: Member?
    val channel: MessageChannel
    val guild: Guild?

    val arguments: Array<String>
    val interaction: Interaction?

    val selfUser: SelfUser
        get() = author.jda.selfUser

    val selfMember: Member?
        get() = guild?.selfMember

    val isFromGuild: Boolean
        get() = guild != null

    fun reply(message: String)

    fun hasPermission(member: Member, vararg permissions: Permission): Boolean
    fun hasPermission(member: Member, guildChannel: GuildChannel, vararg permissions: Permission): Boolean
}
