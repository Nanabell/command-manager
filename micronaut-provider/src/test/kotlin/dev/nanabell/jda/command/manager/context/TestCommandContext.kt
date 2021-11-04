package dev.nanabell.jda.command.manager.context

import dev.nanabell.jda.command.manager.permission.Permission
import org.slf4j.LoggerFactory

class TestCommandContext(
    override val ownerIds: Set<Long>,
    override val arguments: Array<String>,
    override val authorId: Long,
    override val channelId: Long,
    override val guildId: Long?,
    override val selfUserId: Long,
    private val hasUserPermission: Boolean,
    private val hasSelfPermission: Boolean,
) : ICommandContext {

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
