package dev.nanabell.jda.command.manager.permission.impl

import dev.nanabell.jda.command.manager.permission.check.IPermissionCheck
import dev.nanabell.jda.command.manager.permission.check.impl.DiscordPermissionCheck
import dev.nanabell.jda.command.manager.permission.check.impl.OwnerOnlyCheck
import org.slf4j.LoggerFactory

@Suppress("unused")
class DefaultPermissionHandlerBuilder(private val withDefaults: Boolean = true) {

    private val logger = LoggerFactory.getLogger(DefaultPermissionHandlerBuilder::class.java)

    private var rootOwner: Boolean = true
    private val checks: MutableSet<IPermissionCheck> = mutableSetOf()
    private val defaults = setOf(
        DiscordPermissionCheck(DiscordPermissionCheck.Target.MEMBER),
        DiscordPermissionCheck(DiscordPermissionCheck.Target.SELF),
        OwnerOnlyCheck()
    )

    fun build(): DefaultPermissionHandler {
        if (withDefaults)
            checks.addAll(defaults)

        logger.debug("Building DefaultPermission Handler with ${checks.size} Permission check/s")
        return DefaultPermissionHandler(checks, rootOwner)
    }

    fun with(check: IPermissionCheck): DefaultPermissionHandlerBuilder {
        checks.add(check)
        return this
    }

    fun disableOwnerRootAccess(): DefaultPermissionHandlerBuilder {
        this.rootOwner = false
        return this
    }
}
