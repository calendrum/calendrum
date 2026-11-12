@file:JvmName("ICalDurations")

package io.github.calendrum.value

import java.time.Duration
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

/**
 * Returns this duration as a `java.time.Duration`, or `null` when it has days or weeks, whose length depends on the
 * time zone.
 */
public fun ICalDuration.toJavaDuration(): Duration? = toDurationOrNull()?.toJavaDuration()

/** Returns this duration as an exact duration value (hours, minutes and seconds). */
public fun Duration.toICalDuration(): ICalDuration = ICalDuration.of(toKotlinDuration())
