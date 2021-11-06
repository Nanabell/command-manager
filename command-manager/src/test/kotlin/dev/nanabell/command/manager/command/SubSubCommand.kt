package dev.nanabell.command.manager.command

import dev.nanabell.command.manager.command.annotation.SubCommandOf
import dev.nanabell.command.manager.context.TestCommandContext

@SubCommandOf(SubCommand::class)
@Command("sub2", "DoubleSub Command Example")
class SubSubCommand : ITestCommand {

    override suspend fun execute(context: TestCommandContext) {
    }

}
