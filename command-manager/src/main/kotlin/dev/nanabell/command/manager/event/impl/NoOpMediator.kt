package dev.nanabell.command.manager.event.impl

import dev.nanabell.command.manager.event.IEventListener
import dev.nanabell.command.manager.event.IEventMediator

class NoOpMediator : IEventMediator {

    override fun registerCommandManager(listener: IEventListener) {
        // Nothing to do
    }

}
