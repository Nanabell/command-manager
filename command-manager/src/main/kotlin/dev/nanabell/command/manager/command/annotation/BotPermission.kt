package dev.nanabell.command.manager.command.annotation

import dev.nanabell.command.manager.permission.Permission

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BotPermission(val permissions: Array<Permission>)
