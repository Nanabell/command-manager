package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.command.annotation.SubCommandOf
import dev.nanabell.jda.command.manager.context.ITextCommandContext

@SubCommandOf(SubCommand::class)
@Command("sub2", "DoubleSub Command Example")
class SubSubCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
