package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import java.util.*

interface ICommandContext {

    val arguments: Array<String>

    val author: User
    val member: Member?
    val channel: MessageChannel
    val guild: Guild?

    val uuid: UUID
}
