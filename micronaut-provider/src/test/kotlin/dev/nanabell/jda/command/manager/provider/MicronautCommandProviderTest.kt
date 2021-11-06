package dev.nanabell.jda.command.manager.provider

import dev.nanabell.jda.command.manager.CommandManagerBuilder
import dev.nanabell.jda.command.manager.command.DummyCommand
import dev.nanabell.jda.command.manager.context.TestCommandContextBuilder
import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(packages = ["dev.nanabell.jda.command.manager.provider"])
internal class MicronautCommandProviderTest {

    @Inject
    lateinit var context: ApplicationContext

    @Test
    internal fun `Test Provider finds all Command Beans`() {
        val list = MicronautCommandProvider(context).provide()
        Assertions.assertEquals(1, list.size)

        val dummy = list.first()
        Assertions.assertInstanceOf(DummyCommand::class.java, dummy)
    }

    @Test
    internal fun `Test Injecting Provider Works`() {
        val manager = CommandManagerBuilder(";;", 0)
            .setMicronautProvider(context)
            .setContextBuilder(TestCommandContextBuilder())
            .build()

        Assertions.assertEquals(1, manager.getCommands().size)

        val dummy = manager.getCommands().first()
        Assertions.assertInstanceOf(DummyCommand::class.java, dummy.command)
    }
}
