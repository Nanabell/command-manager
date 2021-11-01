package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ITextCommandContext

@Command("fail", "This is a second Example Command")
class FailingCommand : ITextCommand {

    override fun execute(context: ITextCommandContext) {
        throw Exception("This Command will always Fail")
    }

}
