package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.*

sealed class CommandContext(
    override val author: User,
    override val member: Member?,
    override val channel: MessageChannel,
    override val guild: Guild?
) : ICommandContext {

    override val uuid: UUID = UUID.randomUUID()

    override fun reply(message: String) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(member: Member, vararg permissions: Permission): Boolean {
        return member.hasPermission(*permissions)
    }

}
