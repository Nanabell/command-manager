package dev.nanabell.command.manager.provider

import dev.nanabell.command.manager.CommandManagerBuilder
import dev.nanabell.command.manager.command.DummyCommand
import dev.nanabell.command.manager.context.TestCommandContextBuilder
import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(packages = ["dev.nanabell.command.manager.provider"])
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

        val registry = manager.registry
        Assertions.assertEquals(1, registry.size)

        val dummy = registry.getAll().first()
        Assertions.assertInstanceOf(DummyCommand::class.java, dummy.command)
    }
}
