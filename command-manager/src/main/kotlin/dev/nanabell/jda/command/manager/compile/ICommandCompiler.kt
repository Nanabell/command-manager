package dev.nanabell.jda.command.manager.compile

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.compile.exception.CommandCompileException

fun interface ICommandCompiler {

    @Throws(CommandCompileException::class)
    fun compile(command: ICommand): CompiledCommand

}
