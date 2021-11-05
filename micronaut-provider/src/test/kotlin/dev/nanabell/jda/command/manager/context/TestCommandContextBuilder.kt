package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent

class TestCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        arguments: Array<String>
    ): ICommandContext {
        throw UnsupportedOperationException()
    }

    override fun fromCommand(event: SlashCommandEvent): ICommandContext {
        throw UnsupportedOperationException()
    }

}
