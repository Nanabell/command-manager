package dev.nanabell.jda.command.manager

import dev.nanabell.jda.command.manager.command.*
import dev.nanabell.jda.command.manager.command.slash.*
import dev.nanabell.jda.command.manager.compile.exception.CommandCompileException
import dev.nanabell.jda.command.manager.context.ICommandContext
import dev.nanabell.jda.command.manager.exception.CommandPathLoopException
import dev.nanabell.jda.command.manager.exception.MissingParentException
import dev.nanabell.jda.command.manager.exception.SlashCommandDepthException
import dev.nanabell.jda.command.manager.provider.impl.StaticCommandProvider
import gnu.trove.set.hash.TLongHashSet
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import net.dv8tion.jda.api.Permission
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
        assertThrows(CommandPathLoopException::class.java) {
           buildCommandManager(RecursiveCommand1(), RecursiveCommand2())
        }
    }

    @Test
    internal fun `Test Loading Unregistered Parent Command`() {
        assertThrows(MissingParentException::class.java) {
           buildCommandManager(UnregisteredParentCommand())
        }
    }

    @Test
    internal fun `Test Executing Example Text Command`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;example"))
        assertEquals(1, manager.getCommands().size, "Expected only 1 Command to be loaded")
    }

    @Test
    internal fun `Test Try Loading Unknown Command`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;unknown"))
        assertEquals(1.0, Metrics.counter("command.unknown").count(), "Expected only 1 Command to be not Found")
    }

    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command`() {
        val argumentCounter = AtomicInteger(0)
        val manager = buildCommandManager(ArgumentCounterCommand(argumentCounter))

        manager.onMessageReceived(getMessageReceivedEvent(";;count argument1 argument2 argument3"))
        assertEquals(3, argumentCounter.get(), "Expected 3 arguments Command")
    }


    @Test
    internal fun `Test Receiving Correct Argument Count Example Text Command No Arguments`() {
        val argumentCounter = AtomicInteger(0)
        val manager = buildCommandManager(ArgumentCounterCommand(argumentCounter))

        manager.onMessageReceived(getMessageReceivedEvent(";;count"))
        assertEquals(0, argumentCounter.get(), "Expected 0 arguments Command")
    }

    @Test
    internal fun `Test Command by Bot is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isBot = true))
        assertEquals(0.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Command by Webhook is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isWebhook = true))
        assertEquals(0.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Command by System is Ignored`() {
        val manager = buildCommandManager(DummyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;count", isSystem = true))
        assertEquals(0.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test GuildCommand is executed in Guild Context`() {
        val manager = buildCommandManager(GuildCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;guild", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Command failure is handled`() {
        val manager = buildCommandManager(FailingCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;fail", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "failed").count(), "Expected 1 Failed Command")
    }

    @Test
    internal fun `Test Command abortion is handled`() {
        val manager = buildCommandManager(AbortCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;abort", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "aborted").count(), "Expected 1 Aborted Command")
    }

    @Test
    internal fun `Test Command does not handle on invalid prefix`() {
        val manager = buildCommandManager(AbortCommand())

        manager.onMessageReceived(getMessageReceivedEvent("::abort", isGuild = true))
        assertEquals(0.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 0 Executed Command")
    }

    @Test
    internal fun `Test Command throws CommandRejectedException`() {
        val manager = buildCommandManager(RejectedCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;rejected", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "rejected").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Slash Command Executes`() {
        val manager = buildCommandManager(SlashCommand())

        manager.onSlashCommand(getSlashCommandEvent("slash"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Global Slash Command Executes in Guild`() {
        val manager = buildCommandManager(SlashCommand())

        manager.onSlashCommand(getSlashCommandEvent("slash", isGuild = true))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Guild Slash Command does not Execute in DMs`() {
        val manager = buildCommandManager(GuildSlashCommand())

        manager.onSlashCommand(getSlashCommandEvent("guild", isGuild = false))
        assertEquals(1.0, Metrics.counter("command.unknown").count(), "Expected 0 Executed Commands")
    }

    @Test
    internal fun `Test Slash Command that does not Exist`() {
        val manager = buildCommandManager()

        manager.onSlashCommand(getSlashCommandEvent("guild"))
        assertEquals(1.0, Metrics.counter("command.unknown").count(), "Expected 1 Unknown Command")
    }

    @Test
    internal fun `Test Sub Slash Command Executes`() {
        val manager = buildCommandManager(SubSlashCommand(), SlashCommand())

        manager.onSlashCommand(getSlashCommandEvent("slash", sub = "sub"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test SubGroup Sub Slash Command Executes`() {
        val manager = buildCommandManager(SubSubSlashCommand(), SubSlashCommand(), SlashCommand())

        manager.onSlashCommand(getSlashCommandEvent("slash", sub = "subsub", group = "sub"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
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

        manager.onMessageReceived(getMessageReceivedEvent(";;owner", userId = 100))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test Owner Only Command as non Owner does not Execute`() {
        val manager = buildCommandManager(OwnerOnlyCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;owner"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "rejected").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Owner Only Command as CoOwner Executes Successful`() {
        val manager = buildCommandManager(OwnerOnlyCommand(), extraOwnerId = 0)

        manager.onMessageReceived(getMessageReceivedEvent(";;owner"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Executed Command")
    }

    @Test
    internal fun `Test UserPermission Command fails without User Permissions`() {
        val manager = buildCommandManager(UserRequireAdminCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;admin", isGuild = true, userId = 1))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "rejected").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test UserPermission Command succeeds with User Permissions`() {
        val manager = buildCommandManager(UserRequireAdminCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;admin", addPerm = true, isGuild = true, userId = 1))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test BotPermission Command fails without Bot Permissions`() {
        val manager = buildCommandManager(BotRequireAdminCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;admin", isGuild = true, selfId = 1))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "rejected").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test BotPermission Command succeeds with Bot Permissions`() {
        val manager = buildCommandManager(BotRequireAdminCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;admin", addPerm = true, isGuild = true, selfId = 1))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Global Command fails when paired with Discord Permission Requirement`() {
        val manager = buildCommandManager(IllegalDiscordPermissionCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;illegal"))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "rejected").count(), "Expected 1 Rejected Command")
    }

    @Test
    internal fun `Test Owner Override overrides Permission Checks`() {
        val manager = buildCommandManager(IllegalDiscordPermissionCommand())

        manager.onMessageReceived(getMessageReceivedEvent(";;illegal", userId = 100))
        assertEquals(1.0, Metrics.counter("command.executed", "status", "success").count())
    }

    private fun getMessageReceivedEvent(
        content: String,
        isBot: Boolean = false,
        isWebhook: Boolean = false,
        isSystem: Boolean = false,
        isGuild: Boolean = false,
        addPerm: Boolean = false,
        userId: Long = 0,
        selfId: Long = 0
    ): MessageReceivedEvent {
        val jda = JDAImpl(AuthorizationConfig(""))

        val user = UserImpl(userId, jda)
        user.isBot = isBot
        user.isSystem = isSystem

        val guild = GuildImpl(jda, 0)
        jda.selfUser = SelfUserImpl(selfId, jda)

        val adminRole = RoleImpl(1, guild)
        adminRole.setRawPermissions(Permission.getRaw(Permission.ADMINISTRATOR))

        val selfMember = MemberImpl(guild, jda.selfUser)
        if (addPerm) selfMember.roleSet.add(adminRole)
        jda.entityBuilder.updateMemberCache(selfMember)

        val member = MemberImpl(guild, user)
        guild.publicRole = RoleImpl(0, guild)
        if (addPerm) member.roleSet.add(adminRole)

        val msg = ReceivedMessage(
            0,
            if (isGuild) TextChannelImpl(0, guild) else PrivateChannelImpl(0, user),
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
            member,
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
        addPerm: Boolean = false,
        group: String? = null,
        sub: String? = null,
        userId: Long = 0,
        selfId: Long = 0
    ): SlashCommandEvent {
        val jda = JDAImpl(AuthorizationConfig(""))
        val user = UserImpl(userId, jda)

        val lenient = Mockito.lenient()
        val interaction = Mockito.mock(CommandInteractionImpl::class.java)
        lenient.`when`(interaction.name).thenReturn(name)
        lenient.`when`(interaction.subcommandGroup).thenReturn(group)
        lenient.`when`(interaction.subcommandName).thenReturn(sub)
        lenient.`when`(interaction.user).thenReturn(user)

        if (isGuild) {
            val guild = GuildImpl(jda, 0)
            jda.selfUser = SelfUserImpl(selfId, jda)

            val adminRole = RoleImpl(1, guild)
            adminRole.setRawPermissions(Permission.getRaw(Permission.ADMINISTRATOR))

            val selfMember = MemberImpl(guild, jda.selfUser)
            if (addPerm) selfMember.roleSet.add(adminRole)
            jda.entityBuilder.updateMemberCache(selfMember)

            val member = MemberImpl(guild, user)
            guild.publicRole = RoleImpl(0, guild)

            if (addPerm)
                member.roleSet.add(adminRole)

            lenient.`when`(interaction.guild).thenReturn(guild)
            lenient.`when`(interaction.channel).thenReturn(TextChannelImpl(0, guild))
            lenient.`when`(interaction.member).thenReturn(member)
        } else {
            lenient.`when`(interaction.channel).thenReturn(PrivateChannelImpl(0, user))
        }

        return SlashCommandEvent(jda, -1, interaction)
    }

    private fun buildCommandManager(vararg commands: ICommand<out ICommandContext>, prefix: String = ";;", ownerId: Long = 100, extraOwnerId: Long = -1): CommandManager {
        val builder = CommandManagerBuilder(prefix, ownerId)
        if (extraOwnerId != -1L)
            builder.addOwnerId(extraOwnerId)

        builder.setCommandProvider(StaticCommandProvider(commands.toList()))
        return builder.build()
    }
}
