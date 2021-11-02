package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext

@SubCommandOf(RecursiveCommand2::class)
@Command("recurse1", "Example Recursive Command 1")
class RecursiveCommand1 : ICommand{

    override fun execute(context: ICommandContext) {
    }

}
