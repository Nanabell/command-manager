package dev.nanabell.command.manager.context.slash

import dev.nanabell.command.manager.context.BaseCommandContext
import dev.nanabell.command.manager.context.ISlashCommandContext
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")
class CommandContext(
    user: User,
    channel: MessageChannel,
    guild: Guild?,
    responseNumber: Long
) : BaseCommandContext(user, channel, guild, responseNumber), ISlashCommandContext {

    constructor(event: SlashCommandEvent) : this(
        event.user,
        event.channel,
        event.guild,
        event.responseNumber
    )

}
