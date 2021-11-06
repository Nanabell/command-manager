package dev.nanabell.command.manager.permission.check

import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.context.ICommandContext

fun interface IPermissionCheck {

    fun check(command: CompiledCommand, context: ICommandContext): PermissionResult

}
