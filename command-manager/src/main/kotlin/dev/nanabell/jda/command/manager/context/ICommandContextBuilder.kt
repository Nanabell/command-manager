package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.event.IMessageEvent
import dev.nanabell.jda.command.manager.event.ICommandEvent

interface ICommandContextBuilder {

    fun fromMessage(event: IMessageEvent, owners: Set<Long>, arguments: Array<String>): ICommandContext

    fun fromCommand(event: ICommandEvent, owners: Set<Long>): ICommandContext

}
