package dev.nanabell.command.manager.context.impl

import dev.nanabell.command.manager.context.ICommandContextBuilder
import dev.nanabell.command.manager.context.ISlashCommandContext
import dev.nanabell.command.manager.context.ITextCommandContext
import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent

class BasicCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        arguments: Array<String>
    ): ITextCommandContext {
        return BasicContext(event.content, arguments, event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }

    override fun fromCommand(event: SlashCommandEvent): ISlashCommandContext {
        return BasicContext(event.commandPath, emptyArray(), event.authorId, event.channelId, event.guildId, -1 /*Unknown*/)
    }

}
