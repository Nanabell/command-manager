package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.context.ICommandContextBuilder
import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent

class BasicCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        arguments: Array<String>
    ): ICommandContext {
        return BasicContext(arguments, event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }

    override fun fromCommand(event: SlashCommandEvent): ICommandContext {
        return BasicContext(emptyArray(), event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }

}
