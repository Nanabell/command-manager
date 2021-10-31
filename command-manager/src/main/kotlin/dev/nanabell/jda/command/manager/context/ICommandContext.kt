package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

interface ICommandContext {

    val arguments: Array<String>

    val author: User
    val member: Member?
    val channel: MessageChannel
    val guild: Guild?
}
