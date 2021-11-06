package dev.nanabell.jda.command.manager.metrics

interface ICommandMetrics {

    fun incExecuted()
    fun incRejected()
    fun incAborted()
    fun incFailed()
    fun incUnknown()

    fun record(duration: Long)

    fun getExecuted(): Long
    fun getRejected(): Long
    fun getAborted(): Long
    fun getFailed(): Long
    fun getUnknown(): Long

    fun reset()

}
