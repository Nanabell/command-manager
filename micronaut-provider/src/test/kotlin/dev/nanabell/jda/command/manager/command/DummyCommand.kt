package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("dummy", "Dummy Command Description")
class DummyCommand : ICommand{
    override fun execute(context: ICommandContext) {
    }
}
