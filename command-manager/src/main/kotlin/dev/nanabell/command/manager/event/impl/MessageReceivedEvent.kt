package dev.nanabell.command.manager.event.impl

data class MessageReceivedEvent(
    val content: String,
    val authorId: Long,
    val messageId: Long,
    val channelId: Long,
    val guildId: Long?,
    val isBot: Boolean,
    val isWebhook: Boolean,
    val isSystem: Boolean,
    val raw: Any
) {
    val isFromGuild: Boolean get() = guildId != null
}
