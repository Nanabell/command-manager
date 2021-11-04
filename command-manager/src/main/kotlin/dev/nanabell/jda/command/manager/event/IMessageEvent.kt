package dev.nanabell.jda.command.manager.event

interface IMessageEvent {

    val content: String

    val authorId: Long
    val messageId: Long
    val channelId: Long
    val guildId: Long?

    val isBot: Boolean
    val isWebhook: Boolean
    val isSystem: Boolean
    val isFromGuild: Boolean get() = guildId != null

    val raw: Any

}
