package dev.nanabell.jda.command.manager.predicate

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface IPredicate {

    fun check(command: CompiledCommand, context: ICommandContext): Boolean

}
