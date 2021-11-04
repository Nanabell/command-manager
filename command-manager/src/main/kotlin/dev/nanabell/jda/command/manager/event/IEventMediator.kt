package dev.nanabell.jda.command.manager.event

fun interface IEventMediator {

    fun registerCommandManager(listener: IEventListener)

}
