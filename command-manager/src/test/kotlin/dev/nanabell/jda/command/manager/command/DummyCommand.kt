package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("example", "Example Command")
class DummyCommand : ICommand{

    override fun execute(context: ICommandContext) {
        println("Example Commend Executed with arugments: ${context.arguments.joinToString()}")
    }

}
