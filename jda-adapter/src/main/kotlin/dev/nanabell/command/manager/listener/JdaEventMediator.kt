package dev.nanabell.command.manager.listener

import dev.nanabell.command.manager.event.IEventListener
import dev.nanabell.command.manager.event.IEventMediator
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener

class JdaEventMediator : IEventMediator, EventListener {

    private lateinit var commandListener: IEventListener

    override fun registerCommandManager(listener: IEventListener) {
        this.commandListener = listener
    }

    override fun onEvent(event: GenericEvent) {
        if (!this::commandListener.isInitialized) return

        when (event) {
            is MessageReceivedEvent -> commandListener.onMessageReceived(
                dev.nanabell.command.manager.event.impl.MessageReceivedEvent(
                    event.message.contentRaw,
                    event.author.idLong,
                    event.messageIdLong,
                    event.channel.idLong,
                    if (event.isFromGuild) event.guild.idLong else null,
                    event.author.isBot,
                    event.isWebhookMessage,
                    event.author.isSystem,
                    event
                )
            )

            is SlashCommandEvent -> commandListener.onSlashCommand(
                dev.nanabell.command.manager.event.impl.SlashCommandEvent(
                    event.commandPath,
                    event.user.idLong,
                    event.channel.idLong,
                    event.guild?.idLong,
                    event
                )
            )
        }
    }
}
