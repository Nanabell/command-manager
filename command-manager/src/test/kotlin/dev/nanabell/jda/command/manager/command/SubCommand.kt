package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext

@SubCommandOf(DummyCommand::class)
@Command("sub", "Sub Command for Example Command")
class SubCommand : ICommand {

    override fun execute(context: ICommandContext) {
    }

}
