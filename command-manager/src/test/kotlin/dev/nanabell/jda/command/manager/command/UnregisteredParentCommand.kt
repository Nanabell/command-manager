package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@SubCommandOf(UnregisteredCommand::class)
@Command("no-parent", "This Commands Parent does not Exist as a Command")
class UnregisteredParentCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
