package dev.nanabell.jda.command.manager.permission

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface IPermissionHandler {

    fun handle(command: CompiledCommand, context: ICommandContext): Boolean

}
