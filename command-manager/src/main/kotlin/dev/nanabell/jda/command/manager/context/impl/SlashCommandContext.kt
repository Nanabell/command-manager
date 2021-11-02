package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ISlashCommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.util.*

open class SlashCommandContext(event: SlashCommandEvent, ownerIds: Set<Long>) : CommandContext(
    event.user,
    event.member,
    event.channel,
    event.guild,
    ownerIds
), ISlashCommandContext
