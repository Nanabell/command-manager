package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.TestCommandContext

@SubCommandOf(DummyCommand::class)
@Command("sub", "Sub Command for Example Command")
class SubCommand : ITestCommand {

    override fun execute(context: TestCommandContext) {
    }

}
