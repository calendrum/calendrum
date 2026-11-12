package io.github.calendrum.syntax

import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.TimeSource

internal fun interface Deadline {
    fun passed(): Boolean
}

internal fun interface TimeBudget {
    fun start(): Deadline
}

internal class ClockBudget(private val source: TimeSource.WithComparableMarks, private val nanos: Long) : TimeBudget {
    override fun start(): Deadline {
        val end = source.markNow() + nanos.nanoseconds
        return Deadline { source.markNow() >= end }
    }
}
