package dev.nanabell.jda.command.manager.predicate

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

fun interface IPredicateResolver {

    fun resolve(command: CompiledCommand, context: ICommandContext): Boolean

}
