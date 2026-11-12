@file:JvmName("JavaClocks")

package io.github.calendrum.model

import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

/** Adapts this `java.time.Clock` to a `kotlin.time.Clock`, for the APIs that take a clock, such as iTIP. */
public fun java.time.Clock.toKotlinClock(): Clock = JavaClock(this)

/** Creates build defaults from a `java.time.Clock` and a generator of `UID` values; see [BuildDefaults]. */
@JvmName("buildDefaults")
public fun BuildDefaults(clock: java.time.Clock, uids: UidGenerator): BuildDefaults =
    BuildDefaults(clock.toKotlinClock(), uids)

private class JavaClock(private val clock: java.time.Clock) : Clock {
    override fun now(): Instant = clock.instant().toKotlinInstant()

    override fun toString(): String = "Clock($clock)"
}
