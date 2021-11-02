package dev.nanabell.jda.command.manager.permission.check.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.permission.check.IPermissionCheck
import dev.nanabell.jda.command.manager.permission.check.PermissionResult

class OwnerOnlyCheck : IPermissionCheck {

    override fun check(command: CompiledCommand, context: ICommandContext): PermissionResult {
        if (command.ownerOnly && !context.ownerIds.contains(context.author.idLong))
            return PermissionResult.fail("This Command can only be ran by the Bot Owner!")

        return PermissionResult.success()
    }

}
