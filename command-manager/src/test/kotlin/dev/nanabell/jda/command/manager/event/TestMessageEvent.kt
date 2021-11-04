package dev.nanabell.jda.command.manager.event

class TestMessageEvent(
    override val content: String,
    override val authorId: Long,
    override val messageId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val isBot: Boolean,
    override val isWebhook: Boolean,
    override val isSystem: Boolean,
) : IMessageEvent {

    override val raw: Any = Any()

}
