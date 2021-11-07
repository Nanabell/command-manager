package dev.nanabell.command.manager.permission.impl

import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.context.ICommandContext
import dev.nanabell.command.manager.permission.IPermissionHandler
import dev.nanabell.command.manager.permission.check.IPermissionCheck
import org.slf4j.LoggerFactory

class DefaultPermissionHandler(
    private val checks: Set<IPermissionCheck>,
    private val rootOwner: Boolean
) : IPermissionHandler {

    private val logger = LoggerFactory.getLogger(DefaultPermissionHandler::class.java)

    override fun handle(command: CompiledCommand, context: ICommandContext): Boolean {
        if (!command.requirePermission) {
            logger.debug("Command $command does not require Permissions")
            return true
        }


        // Add Owner override to any and all Permission Checks
        if (rootOwner && command.manager.ownerIds.contains(context.authorId)) {
            logger.debug("Command $command has been executed by owner ${context.authorId}")
            return true
        }


        for (check in checks) {
            val result = check.check(command, context)
            if (result.success) {
                logger.trace("Permission Check $${check::class.simpleName} passed for command $command-$context")
                continue
            }


            val error = result.error
            if (error != null) {
                logger.debug("Permission Check ${check::class.simpleName} failed for $context with error: $error")

                context.reply("Sorry, $error") // TODO: 02/11/2021 Make this Customizable without replacing Permission Handler!
                return false
            }
        }

        logger.debug("$command passed all Permission Checks in $context")
        return true
    }
}