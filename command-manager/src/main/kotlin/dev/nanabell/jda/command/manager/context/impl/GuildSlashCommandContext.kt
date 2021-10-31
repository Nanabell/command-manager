package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.IGuildSlashCommandContext
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class GuildSlashCommandContext(event: SlashCommandEvent) : SlashCommandContext(event), IGuildSlashCommandContext {
    override val member: Member = event.member!!
    override val channel: TextChannel = event.textChannel
    override val guild: Guild = event.guild!!
}
