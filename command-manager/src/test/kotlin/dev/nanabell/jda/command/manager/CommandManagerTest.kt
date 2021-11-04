package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.slash.*
import dev.nanabell.jda.command.manager.compile.exception.CommandCompileException
import dev.nanabell.jda.command.manager.compile.exception.MissingCommandAnnotationException
import dev.nanabell.jda.command.manager.compile.exception.RecursiveCommandPathException
import dev.nanabell.jda.command.manager.compile.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.context.TestCommandContextBuilder
import dev.nanabell.jda.command.manager.event.impl.MessageReceivedEvent
import dev.nanabell.jda.command.manager.event.impl.SlashCommandEvent
import dev.nanabell.jda.command.manager.metrics.ICommandMetrics
import dev.nanabell.jda.command.manager.metrics.impl.SimpleCommandMetrics
import dev.nanabell.jda.command.manager.provider.impl.StaticCommandProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.atomic.AtomicInteger

@ExtendWith(MockitoExtension::class)
internal class CommandManagerTest {

    private var metrics: ICommandMetrics = SimpleCommandMetrics()

    @AfterEach
    internal fun tearDown() {
        metrics.reset()
    }

    @Test
    internal fun `Test Loading Single Command`() {
        val manager = buildCommandManager(DummyCommand())
        assertEquals(1, manager.getCommands().size, "Expected only 1 Command to be loaded")
    }

    @Test
    internal fun `Test Loading Multiple Commands`() {
        val manager = buildCommandManager(DummyCommand(), FailingCommand())
        assertEquals(2, manager.getCommands().size, "Expected only 2 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading Sub Commands`() {
        val manager = buildCommandManager(DummyCommand(), SubCommand())
        assertEquals(2, manager.getCommands().size, "Expected only 2 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading multiSub Commands`() {
        val manager = buildCommandManager(DummyCommand(), SubCommand(), SubSubCommand())
        assertEquals(3, manager.getCommands().size, "Expected only 3 Commands to be loaded")
    }

    @Test
    internal fun `Test Loading recursive Commands`() {
        assertThrows(RecursiveCommandPathException::class.java) {
           buildCommandManager(RecursiveCommand1(), RecursiveCommand2())
        }
    }

    @Test
    internal fun `Test Loading Unregistered Parent Command`() {
        assertThrows(MissingCommandAnnotationException::class.java) {
           buildCommandManager(UnregisteredParentCommand())
        }
    }

    @Test
    internal fun `Test Executing Example Text Command`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageEvent(";;example"))
        assertEquals(1, manager.getCommands().size, "Expected only 1 Command to be loaded")
    }

    @Test
    internal fun `Test Try Loading Unknown Command`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageEvent(";;unknown"))
        assertEquals(1, metrics.getUnknown(), "Expected only 1 Command to be not Found")
    }

    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command`() {
        val argumentCounter = AtomicInteger(0)
        val manager = buildCommandManager(ArgumentCounterCommand(argumentCounter))

        manager.onMessageReceived(getMessageEvent(";;count argument1 argument2 argument3"))
        assertEquals(3, argumentCounter.get(), "Expected 3 arguments Command")
    }


    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command No Arguments`() {
        val argumentCounter = AtomicInteger(0)
        val manager = buildCommandManager(ArgumentCounterCommand(argumentCounter))

        manager.onMessageReceived(getMessageEvent(";;count"))
        assertEquals(0, argumentCounter.get(), "Expected 0 arguments Command")
    }

    @Test
    internal fun `Test Command by Bot is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageEvent(";;count", isBot = true))
        assertEquals(0, metrics.getExecuted(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Command by Webhook is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageEvent(";;count", isWebhook = true))
        assertEquals(0, metrics.getExecuted(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Command by System is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageEvent(";;count", isSystem = true))
        assertEquals(0, metrics.getExecuted(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test GuildCommand is executed in Guild Context`() {
        val manager = buildCommandManager(GuildCommand())

        manager.onMessageReceived(getMessageEvent(";;guild", guildId = 0))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Command failure is handled`() {
        val manager = buildCommandManager(FailingCommand())

        manager.onMessageReceived(getMessageEvent(";;fail", guildId = 0))
        assertEquals(1, metrics.getFailed(), "Expected 1 Failed Command")
    }

    @Test
    internal fun `Test Command abortion is handled`() {
        val manager = buildCommandManager(AbortCommand())

        manager.onMessageReceived(getMessageEvent(";;abort", guildId = 0))
        assertEquals(1, metrics.getAborted(), "Expected 1 Aborted Command")
    }

    @Test
    internal fun `Test Command does not handle on invalid prefix`() {
        val manager = buildCommandManager(AbortCommand())

        manager.onMessageReceived(getMessageEvent("::abort", guildId = 0))
        assertEquals(0, metrics.getExecuted(), "Expected 0 Executed Command")
    }

    @Test
    internal fun `Test Command throws CommandRejectedException`() {
        val manager = buildCommandManager(RejectedCommand())

        manager.onMessageReceived(getMessageEvent(";;rejected", guildId = 0))
        assertEquals(1, metrics.getRejected(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Slash Command Executes`() {
        val manager = buildCommandManager(SlashCommand())

        manager.onSlashCommand(getCommandEvent("slash"))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Global Slash Command Executes in Guild`() {
        val manager = buildCommandManager(SlashCommand())

        manager.onSlashCommand(getCommandEvent("slash", guildId = 0))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Guild Slash Command does not Execute in DMs`() {
        val manager = buildCommandManager(GuildSlashCommand())

        manager.onSlashCommand(getCommandEvent("guild"))
        assertEquals(1, metrics.getUnknown(), "Expected 0 Executed Commands")
    }

    @Test
    internal fun `Test Slash Command that does not Exist`() {
        val manager = buildCommandManager()

        manager.onSlashCommand(getCommandEvent("guild"))
        assertEquals(1, metrics.getUnknown(), "Expected 1 Unknown Command")
    }

    @Test
    internal fun `Test Sub Slash Command Executes`() {
        val manager = buildCommandManager(SubSlashCommand(), SlashCommand())

        manager.onSlashCommand(getCommandEvent("slash/sub"))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test SubGroup Sub Slash Command Executes`() {
        val manager = buildCommandManager(SubSubSlashCommand(), SubSlashCommand(), SlashCommand())

        manager.onSlashCommand(getCommandEvent("slash/sub/subsub"))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Slash Command with a Depth of more than 3 Fails to Build`() {
        assertThrows(SlashCommandDepthException::class.java) {
            buildCommandManager(InvalidSubSlashCommand(), SubSubSlashCommand(), SubSlashCommand(), SlashCommand())
        }
    }

    @Test
    internal fun `Test Command without Annotation fails to build`() {
        assertThrows(CommandCompileException::class.java) {
            buildCommandManager(NoAnnotationCommand())
        }
    }

    @Test
    internal fun `Test Owner Only Command as Owner Executes Successful`() {
        val manager = buildCommandManager(OwnerOnlyCommand())

        manager.onMessageReceived(getMessageEvent(";;owner", userId = 100))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Owner Only Command as non Owner does not Execute`() {
        val manager = buildCommandManager(OwnerOnlyCommand())

        manager.onMessageReceived(getMessageEvent(";;owner"))
        assertEquals(1, metrics.getRejected(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Owner Only Command as CoOwner Executes Successful`() {
        val manager = buildCommandManager(OwnerOnlyCommand(), extraOwnerId = 0)

        manager.onMessageReceived(getMessageEvent(";;owner"))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test UserPermission Command fails without User Permissions`() {
        val manager = buildCommandManager(UserRequireAdminCommand())

        manager.onMessageReceived(getMessageEvent(";;admin", guildId = 0))
        assertEquals(1, metrics.getRejected(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test UserPermission Command succeeds with User Permissions`() {
        val manager = buildCommandManager(UserRequireAdminCommand(), hasUserPermission = true)

        manager.onMessageReceived(getMessageEvent(";;admin", guildId = 0))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test BotPermission Command fails without Bot Permissions`() {
        val manager = buildCommandManager(BotRequireAdminCommand())

        manager.onMessageReceived(getMessageEvent(";;admin", guildId = 0))
        assertEquals(1, metrics.getRejected(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test BotPermission Command succeeds with Bot Permissions`() {
        val manager = buildCommandManager(BotRequireAdminCommand(), hasSelfPermission = true)

        manager.onMessageReceived(getMessageEvent(";;admin", guildId = 0))
        assertEquals(1, metrics.getExecuted(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Global Command fails when paired with Discord Permission Requirement`() {
        val manager = buildCommandManager(IllegalDiscordPermissionCommand())

        manager.onMessageReceived(getMessageEvent(";;illegal"))
        assertEquals(1, metrics.getRejected(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Owner Override overrides Permission Checks`() {
        val manager = buildCommandManager(IllegalDiscordPermissionCommand())

        manager.onMessageReceived(getMessageEvent(";;illegal", userId = 100))
        assertEquals(1, metrics.getExecuted())
    }


    private fun buildCommandManager(
        vararg commands: ICommand<*>,
        prefix: String = ";;",
        ownerId: Long = 100,
        extraOwnerId: Long? = null,
        selfUserId: Long = 1,
        hasUserPermission: Boolean = false,
        hasSelfPermission: Boolean = false
    ): CommandManager {
        val builder = CommandManagerBuilder(prefix, ownerId)
        if (extraOwnerId != null)
            builder.addOwnerId(extraOwnerId)

        builder.setCommandMetrics(metrics)
        builder.setContextBuilder(TestCommandContextBuilder(selfUserId, hasUserPermission, hasSelfPermission))
        builder.setCommandProvider(StaticCommandProvider(commands.toList()))
        return builder.build()
    }

    private fun getMessageEvent(
        content: String,
        isBot: Boolean = false,
        userId: Long = 0,
        messageId: Long = 0,
        channelId: Long = 0,
        guildId: Long? = null,
        isWebhook: Boolean = false,
        isSystem: Boolean = false
    ): MessageReceivedEvent {
        return MessageReceivedEvent(content, userId, messageId, channelId, guildId, isBot, isWebhook, isSystem, Any())
    }

    private fun getCommandEvent(
        commandPath: String,
        userId: Long = 0,
        channelId: Long = 0,
        guildId: Long? = null,
    ): SlashCommandEvent {
        return SlashCommandEvent(commandPath, userId, channelId, guildId, Any())
    }

}
