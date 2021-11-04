package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent

class TestCommandContextBuilder(
    private val selfUserId: Long,
    private val hasUserPermission: Boolean,
    private val hasSelfPermission: Boolean
) : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        owners: Set<Long>,
        arguments: Array<String>
    ): ICommandContext {
        return TestCommandContext(owners, arguments, event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

    override fun fromCommand(event: SlashCommandEvent, owners: Set<Long>): ICommandContext {
        return TestCommandContext(owners, emptyArray(), event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

}
