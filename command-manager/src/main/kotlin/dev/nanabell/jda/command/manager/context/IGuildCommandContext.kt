package dev.nanabell.jda.command.manager.context

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel

interface IGuildCommandContext : ICommandContext {

    override val channel: TextChannel
    override val guild: Guild
    override val member: Member
}
