package dev.nanabell.command.manager.event

import dev.nanabell.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.command.manager.event.impl.SlashCommandEvent

interface IEventListener {

    fun onMessageReceived(event: MessageReceivedEvent)

    fun onSlashCommand(event: SlashCommandEvent)
}
