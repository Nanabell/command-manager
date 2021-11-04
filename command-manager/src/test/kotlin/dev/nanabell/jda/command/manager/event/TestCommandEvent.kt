package dev.nanabell.jda.command.manager.event

class TestCommandEvent(
    override val commandPath: String,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
) : ICommandEvent {

    override val raw: Any = Any()

}
