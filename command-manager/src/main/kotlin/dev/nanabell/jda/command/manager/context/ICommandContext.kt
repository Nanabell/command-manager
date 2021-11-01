package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import java.util.*

interface ICommandContext {

    val uuid: UUID

    val arguments: Array<String>

    val author: User
    val member: Member?
    val channel: MessageChannel
    val guild: Guild?

    val selfUser: SelfUser
        get() = author.jda.selfUser

    val selfMember: Member?
        get() = guild?.selfMember


    val isFromGuild: Boolean
        get() = guild != null

    fun reply(message: String)

    fun hasPermission(member: Member, vararg permissions: Permission): Boolean
}
