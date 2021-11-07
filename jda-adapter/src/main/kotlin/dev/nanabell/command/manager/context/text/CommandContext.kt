package dev.nanabell.command.manager.context.text

import dev.nanabell.command.manager.context.BaseCommandContext
import dev.nanabell.command.manager.context.ITextCommandContext
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")
class CommandContext(
    override val arguments: Array<String>,
    val message: Message,
    user: User,
    channel: MessageChannel,
    guild: Guild?,
    responseNumber: Long,
) : BaseCommandContext(user, channel, guild, responseNumber), ITextCommandContext {

    override val content: String = message.contentRaw

    constructor(arguments: Array<String>, event: MessageReceivedEvent) : this(
        arguments,
        event.message,
        event.author,
        event.channel,
        if (event.isFromGuild) event.guild else null,
        event.responseNumber
    )

}
