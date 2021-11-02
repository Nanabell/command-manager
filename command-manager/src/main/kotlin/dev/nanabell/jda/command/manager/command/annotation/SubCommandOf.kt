package dev.nanabell.jda.command.manager.command.annotation

import dev.nanabell.jda.command.manager.command.ICommand
import dev.nanabell.jda.command.manager.context.ICommandContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCommandOf(val subCommandOf: KClass<out ICommand>)
