package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.SlashCommandEvent

interface ICommandContextBuilder {

    fun fromMessage(event: MessageReceivedEvent, owners: Set<Long>, arguments: Array<String>): ICommandContext

    fun fromCommand(event: SlashCommandEvent, owners: Set<Long>): ICommandContext

}
