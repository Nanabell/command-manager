package dev.nanabell.jda.command.manager.event

interface ICommandEvent {

    val commandPath: String

    val authorId: Long
    val channelId: Long
    val guildId: Long?

    val isFromGuild: Boolean get() = guildId != null

    val raw: Any
}
