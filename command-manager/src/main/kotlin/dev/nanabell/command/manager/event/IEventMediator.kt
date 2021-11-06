package dev.nanabell.command.manager.event

fun interface IEventMediator {

    fun registerCommandManager(listener: IEventListener)

}
