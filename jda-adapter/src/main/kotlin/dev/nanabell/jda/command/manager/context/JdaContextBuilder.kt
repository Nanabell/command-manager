package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent as JdaSlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent as JdaMessageReceivedEvent

class JdaContextBuilder : ICommandContextBuilder {

    override fun fromMessage(event: MessageReceivedEvent, owners: Set<Long>, arguments: Array<String>): ICommandContext {
        val base = event.raw as JdaMessageReceivedEvent
        return JdaCommandContext(owners, arguments, base.author, base.channel, if (base.isFromGuild) base.guild else null)
    }

    override fun fromCommand(event: SlashCommandEvent, owners: Set<Long>): ICommandContext {
        val base = event.raw as JdaSlashCommandEvent
        return JdaCommandContext(owners, emptyArray(), base.user, base.channel, base.guild)
    }

}
