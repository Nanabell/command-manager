package dev.nanabell.jda.command.manager.predicate.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.predicate.IPredicate
import dev.nanabell.jda.command.manager.predicate.IPredicateResolver

class DefaultPredicateResolver(ownerId: Long, coOwnerIds: Set<Long>) : IPredicateResolver {

    private val predicates: MutableSet<IPredicate> = mutableSetOf(
        OwnerOnlyPredicate(ownerId, coOwnerIds),
        UserPermissionPredicate(),
        BotPermissionPredicate()
    )

    override fun resolve(command: CompiledCommand, context: ICommandContext): Boolean {
        return predicates.all { it.check(command, context) }
    }

}
