package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ICommandContext

@SubCommandOf(SubCommand::class)
@Command("sub2", "DoubleSub Command Example")
class SubSubCommand : ICommand {

    override fun execute(context: ICommandContext) {
    }

}
