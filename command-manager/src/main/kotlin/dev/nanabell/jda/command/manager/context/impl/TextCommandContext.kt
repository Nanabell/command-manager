package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ITextCommandContext
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

open class TextCommandContext(event: MessageReceivedEvent, override val arguments: Array<String>) : CommandContext(
    event.author,
    event.member,
    event.channel,
    if (event.isFromGuild) event.guild else null
), ITextCommandContext
