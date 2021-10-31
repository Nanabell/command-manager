package dev.nanabell.jda.command.manager.command.exception

import dev.nanabell.jda.command.manager.command.Command
import dev.nanabell.jda.command.manager.command.IBaseCommand
import dev.nanabell.jda.command.manager.context.ICommandContext

class MissingAnnotationException(command: IBaseCommand<out ICommandContext>) :
    Exception("Unable to Compile Command ${command::class.qualifiedName}. Class is Missing ${Command::class.qualifiedName} Annotation!") {
}
