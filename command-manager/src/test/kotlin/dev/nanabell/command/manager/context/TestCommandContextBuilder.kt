package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent

class TestCommandContextBuilder(
    private val selfUserId: Long,
    private val hasUserPermission: Boolean,
    private val hasSelfPermission: Boolean
) : ICommandContextBuilder {

    override fun fromMessage(
        event: MessageReceivedEvent,
        arguments: Array<String>
    ): ITextCommandContext {
        return TestCommandContext(arguments, event.content, event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

    override fun fromCommand(event: SlashCommandEvent): ISlashCommandContext {
        return TestCommandContext(emptyArray(), event.commandPath, event.authorId, event.channelId, event.guildId, selfUserId, hasUserPermission, hasSelfPermission)
    }

}
