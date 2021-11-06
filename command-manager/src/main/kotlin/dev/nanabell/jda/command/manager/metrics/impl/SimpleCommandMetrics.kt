package dev.nanabell.jda.command.manager.metrics.impl

import dev.nanabell.jda.command.manager.metrics.ICommandMetrics
import java.util.concurrent.atomic.AtomicLong

class SimpleCommandMetrics : ICommandMetrics {

    private val executed = AtomicLong(0)
    private val rejected = AtomicLong(0)
    private val aborted = AtomicLong(0)
    private val failed = AtomicLong(0)
    private val unknown = AtomicLong(0)

    override fun incExecuted() {
        executed.getAndIncrement()
    }

    override fun incRejected() {
        rejected.getAndIncrement()
    }

    override fun incAborted() {
        aborted.getAndIncrement()
    }

    override fun incFailed() {
        failed.getAndIncrement()
    }

    override fun incUnknown() {
        unknown.getAndIncrement()
    }

    override fun record(duration: Long) {
        // Not supported
    }

    override fun getExecuted(): Long {
        return executed.get()
    }

    override fun getRejected(): Long {
        return rejected.get()
    }

    override fun getAborted(): Long {
        return aborted.get()
    }

    override fun getFailed(): Long {
        return failed.get()
    }

    override fun getUnknown(): Long {
        return unknown.get()
    }

    override fun reset() {
        executed.set(0)
        rejected.set(0)
        aborted.set(0)
        failed.set(0)
        unknown.set(0)
    }
}
