package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext
import java.lang.Exception

@Command("fail", "This is a second Example Command")
class FailingCommand : ITextCommand {

    override fun execute(context: ICommandContext) {
        throw Exception("This Command will always Fail")
    }

}
