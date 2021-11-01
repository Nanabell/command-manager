package dev.nanabell.jda.command.manager.listener.impl

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.context.ICommandContext

class CompositeCommandListener : ICommandListener {

    private val listeners: MutableSet<ICommandListener> = mutableSetOf()

    override fun onExecute(command: CompiledCommand, context: ICommandContext) {
        for (listener in listeners) {
            listener.onExecute(command, context)
        }
    }

    override fun onExecuted(command: CompiledCommand, context: ICommandContext) {
        for (listener in listeners) {
            listener.onExecuted(command, context)
        }
    }

    override fun onRejected(command: CompiledCommand, context: ICommandContext, e: CommandRejectedException) {
        for (listener in listeners) {
            listener.onRejected(command, context, e)
        }
    }

    override fun onAborted(command: CompiledCommand, context: ICommandContext, e: CommandAbortedException) {
        for (listener in listeners) {
            listener.onAborted(command, context, e)
        }
    }

    override fun onFailed(command: CompiledCommand, context: ICommandContext, throwable: Throwable) {
        for (listener in listeners) {
            listener.onFailed(command, context, throwable)
        }
    }

    override fun onUnknown(commandPath: String) {
        for (listener in listeners) {
            listener.onUnknown(commandPath)
        }
    }

    fun registerListener(listener: ICommandListener): Boolean {
        return listeners.add(listener)
    }

    fun removeListener(listener: ICommandListener): Boolean {
        return listeners.remove(listener)
    }

    fun getListeners(): Set<ICommandListener> {
        return listeners
    }

}
