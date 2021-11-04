package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.SlashCommandEvent

class TestCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        owners: Set<Long>,
        arguments: Array<String>
    ): ICommandContext {
        throw UnsupportedOperationException()
    }

    override fun fromCommand(event: SlashCommandEvent, owners: Set<Long>): ICommandContext {
        throw UnsupportedOperationException()
    }

}
