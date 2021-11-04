package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.TestCommandContext

@Command("dummy", "Dummy Command Description")
class DummyCommand : ITestCommand{
    override fun execute(context: TestCommandContext) {
    }
}
