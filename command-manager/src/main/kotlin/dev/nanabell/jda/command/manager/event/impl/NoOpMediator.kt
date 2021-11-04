package dev.nanabell.jda.command.manager.event.impl

import dev.nanabell.jda.command.manager.event.IEventListener
import dev.nanabell.jda.command.manager.event.IEventMediator

class NoOpMediator : IEventMediator {

    override fun registerCommandManager(listener: IEventListener) {
        // Nothing to do
    }

}
