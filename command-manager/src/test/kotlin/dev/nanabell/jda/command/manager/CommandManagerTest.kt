package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.exception.MissingAnnotationException
import dev.nanabell.jda.command.manager.command.slash.InvalidSubSlashCommand
import dev.nanabell.jda.command.manager.command.slash.SlashCommand
import dev.nanabell.jda.command.manager.command.slash.SubSlashCommand
import dev.nanabell.jda.command.manager.command.slash.SubSubSlashCommand
import dev.nanabell.jda.command.manager.exception.CommandPathLoopException
import dev.nanabell.jda.command.manager.exception.MissingParentException
import dev.nanabell.jda.command.manager.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.provider.StaticCommandProvider
import gnu.trove.set.hash.TLongHashSet
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import net.dv8tion.jda.api.entities.MessageType
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.internal.JDAImpl
import net.dv8tion.jda.internal.entities.*
import net.dv8tion.jda.internal.interactions.CommandInteractionImpl
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicInteger

@ExtendWith(MockitoExtension::class)
internal class CommandManagerTest {

    @BeforeEach
    internal fun setUp() {
        Metrics.addRegistry(SimpleMeterRegistry())
    }

    @AfterEach
    internal fun tearDown() {
        Metrics.globalRegistry.forEachMeter { Metrics.globalRegistry.remove(it) }
    }

    @Test
    internal fun `Test Loading Single Command`() {
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)
        assertEquals(1, manager.getCommands().size, "Expected only 1 Command to be loaded")
    }

    @Test
    internal fun `Test Loading Multiple Commands`() {
        val provider = StaticCommandProvider(listOf(DummyCommand(), FailingCommand()))
        val manager = CommandManager(";;", provider)
        assertEquals(2, manager.getCommands().size, "Expected only 2 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading Sub Commands`() {
        val provider = StaticCommandProvider(listOf(DummyCommand(), SubCommand()))
        val manager = CommandManager(";;", provider)
        assertEquals(2, manager.getCommands().size, "Expected only 2 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading multiSub Commands`() {
        val provider = StaticCommandProvider(listOf(DummyCommand(), SubCommand(), SubSubCommand()))
        val manager = CommandManager(";;", provider)
        assertEquals(3, manager.getCommands().size, "Expected only 3 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading recursive Commands`() {
        val provider = StaticCommandProvider(listOf(RecursiveCommand1(), RecursiveCommand2()))

        assertThrows(CommandPathLoopException::class.java) {
            CommandManager(";;", provider)
        }
    }

    @Test
    internal fun `Test Loading Unregistered Parent Command`() {
        val provider = StaticCommandProvider(listOf(UnregisteredParentCommand()))

        assertThrows(MissingParentException::class.java) {
            CommandManager(";;", provider)
        }
    }

    @Test
    internal fun `Test Executing Example Text Command`() {
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)


        manager.onMessageReceived(getMessageReceivedEvent(";;example"))
        assertEquals(1, manager.getCommands().size, "Expected only 1 Command to be loaded")
    }

    @Test
    internal fun `Test Try Loading Unknown Command`() {
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)


        manager.onMessageReceived(getMessageReceivedEvent(";;unknown"))
        assertEquals(1.0, Metrics.counter("command.unknown").count(), "Expected only 1 Command to be not Found")
    }

    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command`() {
        val argumentCounter = AtomicInteger(0)
        val provider = StaticCommandProvider(listOf(ArgumentCounterCommand(argumentCounter)))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;count argument1 argument2 argument3"))
        assertEquals(3, argumentCounter.get(), "Expected 3 arguments Command")
    }


    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command No Arguments`() {
        val argumentCounter = AtomicInteger(0)
        val provider = StaticCommandProvider(listOf(ArgumentCounterCommand(argumentCounter)))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;count"))
        assertEquals(0, argumentCounter.get(), "Expected 0 arguments Command")
    }

    @Test
    internal fun `Test Command by Bot is Ignored`() {
        Metrics.addRegistry(SimpleMeterRegistry())
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isBot = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "rejected").count(),
            "Expected 1 Rejected Command"
        )
    }

    @Test
    internal fun `Test Command by Webhook is Ignored`() {
        Metrics.addRegistry(SimpleMeterRegistry())
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isWebhook = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "rejected").count(),
            "Expected 1 Rejected Command"
        )
    }

    @Test
    internal fun `Test Command by System is Ignored`() {
        Metrics.addRegistry(SimpleMeterRegistry())
        val provider = StaticCommandProvider(listOf(DummyCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isSystem = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "rejected").count(),
            "Expected 1 Rejected Command"
        )
    }

    @Test
    internal fun `Test GuildCommand is executed in Guild Context`() {
        val provider = StaticCommandProvider(listOf(GuildCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;guild", isGuild = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 1 Executed Command"
        )
    }

    @Test
    internal fun `Test Command failure is handled`() {
        val provider = StaticCommandProvider(listOf(FailingCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;fail", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "failed").count(), "Expected 1 Failed Command")
    }

    @Test
    internal fun `Test Command abortion is handled`() {
        val provider = StaticCommandProvider(listOf(AbortCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;abort", isGuild = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "aborted").count(),
            "Expected 1 Aborted Command"
        )
    }

    @Test
    internal fun `Test Command does not handle on invalid prefix`() {
        val provider = StaticCommandProvider(listOf(AbortCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent("::abort", isGuild = true))
        assertEquals(
            0.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 0 Executed Command"
        )
    }

    @Test
    internal fun `Test Command throws CommandRejectedException`() {
        val provider = StaticCommandProvider(listOf(RejectedCommand()))
        val manager = CommandManager(";;", provider)

        manager.onMessageReceived(getMessageReceivedEvent(";;rejected", isGuild = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "rejected").count(),
            "Expected 1 Rejected Command"
        )
    }

    @Test
    internal fun `Test Slash Command Executes`() {
        val provider = StaticCommandProvider(listOf(SlashCommand()))
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("slash"))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 1 Executed Command"
        )
    }

    @Test
    internal fun `Test Global Slash Command Executes in Guild`() {
        val provider = StaticCommandProvider(listOf(SlashCommand()))
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("slash", isGuild = true))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 1 Executed Command"
        )
    }

    @Test
    internal fun `Test Guild Slash Command does not Execute in DMs`() {
        val provider = StaticCommandProvider(listOf(GuildSlashCommand()))
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("guild", isGuild = false))
        assertEquals(
            0.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 0 Executed Commands"
        )
    }

    @Test
    internal fun `Test Slash Command that does not Exist`() {
        val provider = StaticCommandProvider(listOf())
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("guild"))
        assertEquals(1.0, Metrics.counter("command.unknown").count(), "Expected 1 Unknown Command")
    }

    @Test
    internal fun `Test Sub Slash Command Executes`() {
        val provider = StaticCommandProvider(listOf(SubSlashCommand(), SlashCommand()))
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("slash", sub = "sub"))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 1 Executed Command"
        )
    }

    @Test
    internal fun `Test SubGroup Sub Slash Command Executes`() {
        val provider = StaticCommandProvider(listOf(SubSubSlashCommand(), SubSlashCommand(), SlashCommand()))
        val manager = CommandManager(";;", provider)

        manager.onSlashCommand(getSlashCommandEvent("slash", sub = "subsub", group = "sub"))
        assertEquals(
            1.0,
            Metrics.counter("command.executed", "status", "success").count(),
            "Expected 1 Executed Command"
        )
    }

    @Test
    internal fun `Test Slash Command with a Depth of more than 3 Fails to Build`() {
        val provider = StaticCommandProvider(
            listOf(
                InvalidSubSlashCommand(),
                SubSubSlashCommand(),
                SubSlashCommand(),
                SlashCommand()
            )
        )
        assertThrows(SlashCommandDepthException::class.java) { CommandManager(";;", provider) }
    }

    @Test
    internal fun `Test Command without Annotation fails to build`() {
        val provider = StaticCommandProvider(listOf(NoAnnotationCommand()))
        assertThrows(MissingAnnotationException::class.java) { CommandManager(";;", provider) }
    }

    private fun getMessageReceivedEvent(
        content: String,
        isBot: Boolean = false,
        isWebhook: Boolean = false,
        isSystem: Boolean = false,
        isGuild: Boolean = false
    ): MessageReceivedEvent {
        val jda = JDAImpl(AuthorizationConfig(""))
        val user = UserImpl(0, jda)
        user.isBot = isBot
        user.isSystem = isSystem

        val msg = ReceivedMessage(
            0,
            if (isGuild) TextChannelImpl(0, GuildImpl(jda, 0)) else PrivateChannelImpl(0, user),
            MessageType.DEFAULT,
            null,
            isWebhook,
            false,
            TLongHashSet(),
            TLongHashSet(),
            false,
            false,
            content,
            "",
            user,
            MemberImpl(GuildImpl(jda, 0), user),
            null,
            OffsetDateTime.MAX,
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            0
        )

        return MessageReceivedEvent(jda, -1, msg)
    }

    private fun getSlashCommandEvent(
        name: String,
        isGuild: Boolean = false,
        group: String? = null,
        sub: String? = null
    ): SlashCommandEvent {
        val jda = JDAImpl(AuthorizationConfig(""))
        val user = UserImpl(0, jda)

        val lenient = Mockito.lenient()
        val interaction = Mockito.mock(CommandInteractionImpl::class.java)
        lenient.`when`(interaction.name).thenReturn(name)
        lenient.`when`(interaction.subcommandGroup).thenReturn(group)
        lenient.`when`(interaction.subcommandName).thenReturn(sub)
        lenient.`when`(interaction.user).thenReturn(user)

        if (isGuild) {
            val guild = GuildImpl(jda, 0)
            lenient.`when`(interaction.guild).thenReturn(guild)
            lenient.`when`(interaction.channel).thenReturn(TextChannelImpl(0, guild))
            lenient.`when`(interaction.member).thenReturn(MemberImpl(guild, user))
        } else {
            lenient.`when`(interaction.channel).thenReturn(PrivateChannelImpl(0, user))
        }

        return SlashCommandEvent(jda, -1, interaction)
    }
}
