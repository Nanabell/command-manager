package dev.nanabell.command.manager.command

import jakarta.inject.Singleton

@Singleton
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String,
    val description: String,
    val guildOnly: Boolean = false,
    val requirePermission: Boolean = true
)
