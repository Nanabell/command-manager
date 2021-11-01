package dev.nanabell.jda.command.manager.command

import dev.nanabell.jda.command.manager.context.ICommandContext
import jakarta.inject.Singleton
import net.dv8tion.jda.api.Permission
import kotlin.reflect.KClass

@Singleton
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String,
    val description: String,
    val subCommandOf: KClass<out IBaseCommand<out ICommandContext>> = NullCommand::class,
    val ownerOnly: Boolean = false,
    val userPermission: Array<Permission> = [],
    val botPermission: Array<Permission> = []
)
