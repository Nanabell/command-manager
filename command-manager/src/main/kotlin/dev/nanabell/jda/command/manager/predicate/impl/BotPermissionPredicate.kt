package dev.nanabell.jda.command.manager.predicate.impl

import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.predicate.IPredicate

class BotPermissionPredicate : IPredicate {

    override fun check(command: CompiledCommand, context: ICommandContext): Boolean {
        val botPerms = command.botPermission
        if (botPerms.isNotEmpty()) {
            if (!context.isFromGuild) {
                // listener.onRejected(compiled, context, CommandRejectedException("Global Command has Guild specific Predicates! $compiled"))
                return false
            }

            if (!context.hasPermission(context.selfMember!!, *botPerms)) {
                // listener.onRejected(compiled, context, CommandRejectedException("Bot Missing Permission Requirement"))
                return false
            }
        }

        return true
    }

}
