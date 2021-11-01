package dev.nanabell.jda.command.manager.predicate.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.predicate.IPredicate

class OwnerOnlyPredicate(private val ownerId: Long, private val coOwnerIds: Set<Long>) : IPredicate {

    override fun check(command: CompiledCommand, context: ICommandContext): Boolean {
        if (command.ownerOnly) {
            val authorId = context.author.idLong
            if (authorId != ownerId && !coOwnerIds.contains(authorId)) {
                // listener.onRejected(command, context, CommandRejectedException("This command can only be ran by the Bot Owner!"))
                return false
            }
        }

        return true
    }

}
