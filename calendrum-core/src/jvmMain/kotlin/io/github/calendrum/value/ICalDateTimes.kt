@file:JvmName("ICalDateTimes")

package io.github.calendrum.value

import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import java.time.Instant as JavaInstant

/** Returns the same moment as a `java.time.Instant`. */
public fun ICalDateTime.Utc.toJavaInstant(): JavaInstant = instant.toJavaInstant()

/** Returns this moment as a UTC date-time value. */
public fun JavaInstant.toICalDateTime(): ICalDateTime.Utc = ICalDateTime.Utc(toKotlinInstant())

/** Converts a `java.time.Instant` to a `kotlin.time.Instant`; a static helper for Java callers. */
public fun toKotlinInstant(instant: JavaInstant): Instant = instant.toKotlinInstant()

/** Converts a `kotlin.time.Instant` to a `java.time.Instant`; a static helper for Java callers. */
public fun toJavaInstant(instant: Instant): JavaInstant = instant.toJavaInstant()
