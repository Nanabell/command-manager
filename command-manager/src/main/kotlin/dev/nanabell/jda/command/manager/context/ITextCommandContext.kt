package dev.nanabell.jda.command.manager.context

interface ITextCommandContext : ICommandContext {
    val arguments: Array<String>
}
