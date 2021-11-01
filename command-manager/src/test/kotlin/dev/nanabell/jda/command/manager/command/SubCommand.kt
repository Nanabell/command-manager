package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("sub", "Sub Command for Example Command", subCommandOf = DummyCommand::class)
class SubCommand : ITextCommand  {

    override fun execute(context: ITextCommandContext) {
    }

}
