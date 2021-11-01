package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("dummy", "Dummy Command Description")
class DummyCommand : ITextCommand {
    override fun execute(context: ITextCommandContext) {
    }
}
