package dev.nanabell.jda.command.manager.listener.impl

import dev.nanabell.jda.command.manager.command.exception.CommandAbortedException
import dev.nanabell.jda.command.manager.command.exception.CommandRejectedException
import dev.nanabell.jda.command.manager.command.impl.CompiledCommand
import dev.nanabell.jda.command.manager.listener.ICommandListener
import dev.nanabell.jda.command.manager.context.ICommandContext
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

class MetricCommandListener(registry: MeterRegistry) : ICommandListener {

    private val logger = LoggerFactory.getLogger(MetricCommandListener::class.java)
    private val timings: MutableMap<UUID, Long> = mutableMapOf()

    private val successCount = registry.counter("command.executed", "status", "success")
    private val rejectCount = registry.counter("command.executed", "status", "rejected")
    private val abortCount = registry.counter("command.executed", "status", "aborted")
    private val failCount = registry.counter("command.executed", "status", "failed")
    private val unknownCount = registry.counter("command.unknown")
    private val executeTimer = registry.timer("command.executed.time")

    override fun onExecute(command: CompiledCommand, context: ICommandContext) {
        timings[context.uuid] = System.currentTimeMillis()
        logger.debug("Executing Command: $command")
    }

    override fun onExecuted(command: CompiledCommand, context: ICommandContext) {
        val start = timings.remove(context.uuid)
        if (start == null) {
            logger.warn("Received onExecuted for unknown command context: ${context.uuid}")
            return
        }

        val duration = System.currentTimeMillis() - start
        executeTimer.record(duration, TimeUnit.MILLISECONDS)
        successCount.increment()

        logger.debug("Command ${command.command::class.qualifiedName} has finished Executing in ${duration}ms")
    }

    override fun onRejected(command: CompiledCommand, context: ICommandContext, e: CommandRejectedException) {
        logger.debug("Command ${command.command::class.qualifiedName} has been Rejected", e)
        rejectCount.increment()
    }

    override fun onAborted(command: CompiledCommand, context: ICommandContext, e: CommandAbortedException) {
        logger.debug("Command ${command.command::class.qualifiedName} has been Aborted", e)
        abortCount.increment()
    }

    override fun onFailed(command: CompiledCommand, context: ICommandContext, throwable: Throwable) {
        logger.error("Command ${command.command::class.qualifiedName} has failed!", throwable)
        failCount.increment()
    }

    override fun onUnknown(commandPath: String) {
        logger.debug("Unable to find Command with Path: /$commandPath")
        unknownCount.increment()
    }

}
