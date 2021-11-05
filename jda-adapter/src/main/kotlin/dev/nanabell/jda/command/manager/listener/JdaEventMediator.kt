package dev.nanabell.jda.command.manager.listener

import dev.nanabell.jda.command.manager.event.IEventListener
import dev.nanabell.jda.command.manager.event.IEventMediator
import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent as JdaSlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent as JdaMessageReceivedEvent

class JdaEventMediator : IEventMediator, EventListener {

    private lateinit var commandListener: IEventListener

    override fun registerCommandManager(listener: IEventListener) {
        this.commandListener = listener
    }

    override fun onEvent(event: GenericEvent) {
        if (!this::commandListener.isInitialized) return

        when (event) {
            is JdaMessageReceivedEvent -> commandListener.onMessageReceived(
                MessageReceivedEvent(
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

            is JdaSlashCommandEvent -> commandListener.onSlashCommand(
                SlashCommandEvent(
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
