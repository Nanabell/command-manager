package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.IMessageEvent
import dev.nanabell.jda.command.manager.event.ICommandEvent

class TestCommandContextBuilder(
    private val selfUserId: Long,
    private val hasUserPermission: Boolean,
    private val hasSelfPermission: Boolean
) : ICommandContextBuilder {

    override fun fromMessage(
        event: IMessageEvent,
        owners: Set<Long>,
        arguments: Array<String>
    ): ICommandContext {
        return TestCommandContext(owners, arguments, event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

    override fun fromCommand(event: ICommandEvent, owners: Set<Long>): ICommandContext {
        return TestCommandContext(owners, emptyArray(), event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

}
