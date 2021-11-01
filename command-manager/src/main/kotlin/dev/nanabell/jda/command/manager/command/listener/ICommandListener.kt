package dev.nanabell.jda.command.manager.command.listener

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

interface ICommandListener {

    fun onExecute(command: CompiledCommand, context: ICommandContext)

    fun onExecuted(command: CompiledCommand, context: ICommandContext)

    fun onRejected(command: CompiledCommand, context: ICommandContext, e: CommandRejectedException)

    fun onAborted(command: CompiledCommand, context: ICommandContext, e: CommandAbortedException)

    fun onFailed(command: CompiledCommand, context: ICommandContext, throwable: Throwable)

    fun onUnknown(commandPath: String)

}
