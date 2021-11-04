package dev.nanabell.jda.command.manager.event

data class SlashCommandEvent(
    val commandPath: String,
    val authorId: Long,
    val channelId: Long,
    val guildId: Long?,
    val raw: Any,
) {
    val isFromGuild: Boolean get() = guildId != null
}
