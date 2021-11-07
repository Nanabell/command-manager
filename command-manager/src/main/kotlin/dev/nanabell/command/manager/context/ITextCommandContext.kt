package dev.nanabell.command.manager.context

interface ITextCommandContext : ICommandContext {

    val content: String
    val arguments: Array<String>

}
