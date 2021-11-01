package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("sub2", "DoubleSub Command Example", subCommandOf = SubCommand::class)
class SubSubCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
    }

}
