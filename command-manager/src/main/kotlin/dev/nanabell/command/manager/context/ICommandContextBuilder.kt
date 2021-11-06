package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent

interface ICommandContextBuilder {

    fun fromMessage(event: MessageReceivedEvent, arguments: Array<String>): ICommandContext

    fun fromCommand(event: SlashCommandEvent): ICommandContext

}
