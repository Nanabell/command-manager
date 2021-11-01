package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.IGuildTextCommandContext
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class GuildTextCommandContext(event: MessageReceivedEvent, arguments: Array<String>) : TextCommandContext(event, arguments), IGuildTextCommandContext {

    override val channel: TextChannel = event.textChannel
    override val guild: Guild = event.guild
    override val member: Member = event.member!!

}
