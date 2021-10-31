package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("recurse1", "Example Recursive Command 1", subCommandOf = RecursiveCommand2::class)
class RecursiveCommand1 : ITextCommand {

    override fun execute(context: ICommandContext) {
    }

}
