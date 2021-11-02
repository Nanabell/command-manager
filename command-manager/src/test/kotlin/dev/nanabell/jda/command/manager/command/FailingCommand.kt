package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext

@Command("fail", "This is a second Example Command")
class FailingCommand : ICommand{

    override fun execute(context: ICommandContext) {
        throw Exception("This Command will always Fail")
    }

}
