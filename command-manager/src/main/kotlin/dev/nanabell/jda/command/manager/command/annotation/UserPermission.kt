package dev.nanabell.jda.command.manager.command.annotation

import net.dv8tion.jda.api.Permission

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class UserPermission(val permissions: Array<Permission>)