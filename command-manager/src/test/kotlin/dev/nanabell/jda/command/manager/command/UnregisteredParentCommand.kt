package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("no-parent", "This Commands Parent does not Exist as a Command", subCommandOf = UnregisteredCommand::class)
class UnregisteredParentCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
