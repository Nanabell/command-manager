package dev.nanabell.jda.command.manager.command.annotation

import dev.nanabell.jda.command.manager.permission.Permission

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BotPermission(val permissions: Array<Permission>)
