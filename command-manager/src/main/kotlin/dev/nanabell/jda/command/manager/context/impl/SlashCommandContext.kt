package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ISlashCommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.util.*

open class SlashCommandContext(event: SlashCommandEvent) : ISlashCommandContext {
    override val uuid: UUID = UUID.randomUUID()

    override val arguments: Array<String> = emptyArray()
    override val author: User = event.user
    override val member: Member? = event.member
    override val channel: MessageChannel = event.channel
    override val guild: Guild? = event.guild

    override fun reply(message: String) {
        TODO("Not yet implemented")
    }


    override fun hasPermission(member: Member, vararg permissions: Permission): Boolean {
        TODO("Not yet implemented")
    }

}
