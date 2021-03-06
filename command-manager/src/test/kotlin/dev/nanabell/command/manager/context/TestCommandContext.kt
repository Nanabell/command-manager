package dev.nanabell.command.manager.context

import dev.nanabell.command.manager.permission.Permission
import org.slf4j.LoggerFactory

class TestCommandContext(
    override val arguments: Array<String>,
    override val content: String,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val selfUserId: Long,
    private val hasUserPermission: Boolean,
    private val hasSelfPermission: Boolean,
) : ITextCommandContext, ISlashCommandContext {

    private val logger = LoggerFactory.getLogger(::TestCommandContext::class.java)

    override fun hasPermission(memberId: Long, vararg permission: Permission): Boolean {
        return when (memberId) {
            authorId -> hasUserPermission
            selfUserId -> hasSelfPermission
            else -> false
        }
    }

    override fun reply(message: String) {
        logger.info("${TestCommandContext::class.simpleName} Reply: $message")
    }

}
