package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.context.JdaContextBuilder
import dev.nanabell.jda.command.manager.listener.JdaEventMediator
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

fun CommandManagerBuilder.useJDA(jda: JDA): CommandManagerBuilder {
    val mediator = JdaEventMediator()
    jda.addEventListener(mediator)

    this.setContextBuilder(JdaContextBuilder())
    this.setEventMediator(mediator)
    return this
}

fun CommandManagerBuilder.useJDA(jda: JDABuilder): CommandManagerBuilder {
    val mediator = JdaEventMediator()
    jda.addEventListeners(mediator)

    this.setContextBuilder(JdaContextBuilder())
    this.setEventMediator(mediator)
    return this
}
