package dev.nanabell.jda.command.manager.command

import jakarta.inject.Singleton
import net.dv8tion.jda.api.Permission

@Singleton
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String,
    val description: String,
    val userPermission: Array<Permission> = [],
    val botPermission: Array<Permission> = []
)
