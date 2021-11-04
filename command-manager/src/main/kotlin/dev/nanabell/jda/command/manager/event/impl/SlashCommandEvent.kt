package dev.nanabell.jda.command.manager.event.impl

data class SlashCommandEvent(
    val commandPath: String,
    val authorId: Long,
    val channelId: Long,
    val guildId: Long?,
    val raw: Any,
) {
    val isFromGuild: Boolean get() = guildId != null
}
