package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent

class JdaContextBuilder : ICommandContextBuilder {

    override fun fromMessage(event: MessageReceivedEvent, arguments: Array<String>): ICommandContext {
        val base = event.raw as net.dv8tion.jda.api.events.message.MessageReceivedEvent
        return JdaCommandContext(arguments, base.author, base.channel, if (base.isFromGuild) base.guild else null, base.responseNumber)
    }

    override fun fromCommand(event: SlashCommandEvent): ICommandContext {
        val base = event.raw as net.dv8tion.jda.api.events.interaction.SlashCommandEvent
        return JdaCommandContext(emptyArray(), base.user, base.channel, base.guild, base.responseNumber)
    }

}
