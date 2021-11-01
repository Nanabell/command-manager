package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("recurse2", "Example Recursive Command 2", subCommandOf = RecursiveCommand1::class)
class RecursiveCommand2 : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
