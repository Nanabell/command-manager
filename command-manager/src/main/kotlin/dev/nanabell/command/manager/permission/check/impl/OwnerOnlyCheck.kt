package dev.nanabell.command.manager.permission.check.impl

import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.context.ICommandContext
import dev.nanabell.command.manager.permission.check.IPermissionCheck
import dev.nanabell.command.manager.permission.check.PermissionResult

class OwnerOnlyCheck : IPermissionCheck {

    override fun check(command: CompiledCommand, context: ICommandContext): PermissionResult {
        if (command.ownerOnly && !command.manager.ownerIds.contains(context.authorId))
            return PermissionResult.fail("This Command can only be ran by the Bot Owner!")

        return PermissionResult.success()
    }

}
