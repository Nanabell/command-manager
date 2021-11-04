package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.IMessageEvent
import dev.nanabell.jda.command.manager.event.ICommandEvent

class TestCommandContextBuilder : ICommandContextBuilder {

    override fun fromMessage(
        event: IMessageEvent,
        owners: Set<Long>,
        arguments: Array<String>
    ): ICommandContext {
        throw UnsupportedOperationException()
    }

    override fun fromCommand(event: ICommandEvent, owners: Set<Long>): ICommandContext {
        throw UnsupportedOperationException()
    }

}
