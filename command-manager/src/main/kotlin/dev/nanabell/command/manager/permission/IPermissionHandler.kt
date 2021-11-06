package dev.nanabell.command.manager.permission

import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.context.ICommandContext

fun interface IPermissionHandler {

    fun handle(command: CompiledCommand, context: ICommandContext): Boolean

}
