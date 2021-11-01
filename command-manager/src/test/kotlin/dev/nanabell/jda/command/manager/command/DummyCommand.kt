package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("example", "Example Command")
class DummyCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
        println("Example Commend Executed with arugments: ${context.arguments.joinToString()}")
    }

}
