package dev.nanabell.command.manager.compile

import dev.nanabell.command.manager.CommandManager
import dev.nanabell.command.manager.command.ICommand
import dev.nanabell.command.manager.command.impl.CompiledCommand
import dev.nanabell.command.manager.compile.exception.CommandCompileException
import dev.nanabell.command.manager.context.ICommandContext

fun interface ICommandCompiler {

    @Throws(CommandCompileException::class)
    fun compile(command: ICommand<ICommandContext>, manager: CommandManager): CompiledCommand

}
