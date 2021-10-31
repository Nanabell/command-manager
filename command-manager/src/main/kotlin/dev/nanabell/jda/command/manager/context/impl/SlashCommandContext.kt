package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ISlashCommandContext
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

open class SlashCommandContext(event: SlashCommandEvent) : ISlashCommandContext {
    override val arguments: Array<String> = emptyArray()
    override val author: User = event.user
    override val member: Member? = event.member
    override val channel: MessageChannel = event.channel
    override val guild: Guild? = event.guild
}
