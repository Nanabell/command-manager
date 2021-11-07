package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent
import dev.nanabell.command.manager.context.slash.CommandContext as SlashCommandContext
import dev.nanabell.command.manager.context.text.CommandContext as TextCommandContext
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent as JdaSlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent as JdaMessageReceivedEvent

class JdaContextBuilder : ICommandContextBuilder {

    override fun fromMessage(event: MessageReceivedEvent, arguments: Array<String>): ITextCommandContext {
        return TextCommandContext(arguments, event.raw as JdaMessageReceivedEvent)
    }

    override fun fromCommand(event: SlashCommandEvent): ISlashCommandContext {
        return SlashCommandContext(event.raw as JdaSlashCommandEvent)
    }

}
