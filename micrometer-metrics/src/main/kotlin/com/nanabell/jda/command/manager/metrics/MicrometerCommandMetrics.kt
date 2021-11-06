package com.nanabell.jda.command.manager.metrics

import dev.nanabell.jda.command.manager.metrics.ICommandMetrics
import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.TimeUnit

class MicrometerCommandMetrics(registry: MeterRegistry) : ICommandMetrics {

    private val executed = registry.counter("command.executed", "status", "success")
    private val rejected = registry.counter("command.executed", "status", "rejected")
    private val aborted = registry.counter("command.executed", "status", "aborted")
    private val failed = registry.counter("command.executed", "status", "failed")
    private val unknown = registry.counter("command.unknown")
    private val timer = registry.timer("command.executed.time")

    override fun incExecuted() {
        executed.increment()
    }

    override fun incRejected() {
        rejected.increment()
    }

    override fun incAborted() {
        aborted.increment()
    }

    override fun incFailed() {
        failed.increment()
    }

    override fun incUnknown() {
        unknown.increment()
    }

    override fun record(duration: Long) {
        timer.record(duration, TimeUnit.MILLISECONDS)
    }

    override fun getExecuted(): Long {
        return executed.count().toLong()
    }

    override fun getRejected(): Long {
        return rejected.count().toLong()
    }

    override fun getAborted(): Long {
        return aborted.count().toLong()
    }

    override fun getFailed(): Long {
        return failed.count().toLong()
    }

    override fun getUnknown(): Long {
        return unknown.count().toLong()
    }

    override fun reset() {
        throw IllegalStateException("Micrometer Metrics cannot be reset!")
    }
}
