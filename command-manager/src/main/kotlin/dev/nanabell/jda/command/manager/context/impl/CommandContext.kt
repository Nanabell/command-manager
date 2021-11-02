package dev.nanabell.jda.command.manager.context.impl

import dev.nanabell.jda.command.manager.context.ICommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.Interaction
import java.util.*

open class CommandContext(
    override val author: User,
    override val member: Member?,
    override val channel: MessageChannel,
    override val guild: Guild?,
    override val ownerIds: Set<Long>,
    override val arguments: Array<String>,
    override val interaction: Interaction?
) : ICommandContext {

    constructor(event: MessageReceivedEvent, ownerIds: Set<Long>, arguments: Array<String>) : this(
        event.author,
        event.member,
        event.channel,
        if (event.isFromGuild) event.guild else null,
        ownerIds,
        arguments,
        null
    )

    constructor(event: SlashCommandEvent, ownerIds: Set<Long>) : this(
        event.user,
        event.member,
        event.channel,
        event.guild,
        ownerIds,
        emptyArray(),
        event.interaction
    )

    override val uuid: UUID = UUID.randomUUID()

    override fun reply(message: String) {
        // TODO("Not yet implemented")
    }

    override fun hasPermission(member: Member, vararg permissions: Permission): Boolean {
        return member.hasPermission(*permissions)
    }

    override fun hasPermission(member: Member, guildChannel: GuildChannel, vararg permissions: Permission): Boolean {
        return member.hasPermission(guildChannel, *permissions)
    }

}
