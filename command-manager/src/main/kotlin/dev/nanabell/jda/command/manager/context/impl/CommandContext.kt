package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.*

open class CommandContext(event: MessageReceivedEvent, override val arguments: Array<String>) : ICommandContext {
    override val uuid: UUID = UUID.randomUUID()

    override val author: User = event.author
    override val member: Member? = event.member
    override val channel: MessageChannel = event.channel
    override val guild: Guild? = if (event.isFromGuild) event.guild else null

    override fun reply(message: String) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(member: Member, vararg permissions: Permission): Boolean {
        return member.hasPermission(*permissions)
    }

}
