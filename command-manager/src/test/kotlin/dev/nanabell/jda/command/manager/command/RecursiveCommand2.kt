package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@SubCommandOf(RecursiveCommand1::class)
@Command("recurse2", "Example Recursive Command 2")
class RecursiveCommand2 : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
