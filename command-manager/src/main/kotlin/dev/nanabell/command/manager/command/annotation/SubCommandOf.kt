package dev.nanabell.command.manager.command.annotation

import dev.nanabell.command.manager.command.ICommand
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCommandOf(val subCommandOf: KClass<out ICommand<*>>)
