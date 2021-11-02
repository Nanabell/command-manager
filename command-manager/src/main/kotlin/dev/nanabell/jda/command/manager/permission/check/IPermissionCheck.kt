package dev.nanabell.jda.command.manager.permission.check

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface IPermissionCheck {

    fun check(command: CompiledCommand, context: ICommandContext): PermissionResult

}
