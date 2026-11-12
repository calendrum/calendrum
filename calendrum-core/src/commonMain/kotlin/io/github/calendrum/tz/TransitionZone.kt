@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.tz

import io.github.calendrum.InternalICalApi
import io.github.calendrum.recur.SECONDS_PER_DAY_LONG
import io.github.calendrum.recur.dayOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.number
import kotlin.concurrent.Volatile
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC
import kotlin.time.Instant

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun interface TransitionSource {
    public fun extend(fromYear: Int, toYear: Int, lastOffset: Int, sink: TransitionSink)
}

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class TransitionSink internal constructor() {
    internal var times = LongArray(16)
    internal var offsets = IntArray(16)
    internal var size = 0

    public fun add(epochSecond: Long, offsetAfter: Int) {
        if (size == times.size) {
            times = times.copyOf(size * 2)
            offsets = offsets.copyOf(size * 2)
        }
        times[size] = epochSecond
        offsets[size] = offsetAfter
        size++
    }

    override fun toString(): String = "TransitionSink($size)"
}

// `timeKeys` and `localKeys` are search keys of the platform (`searchKeys`): `Double` copies of the instants and local
// starts on JS, where every `Long` operation is a call on an object, and `null` elsewhere (see TransitionKeys.kt).
internal class TransitionTable(
    val times: LongArray,
    val offsets: IntArray,
    val localStarts: LongArray,
    val timeKeys: DoubleArray?,
    val localKeys: DoubleArray?,
    val size: Int,
    val coveredUntil: Long,
    val coveredUntilLocal: Long,
    val endYear: Int,
)

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public interface ZoneRules {
    public fun offsetSeconds(epochSecond: Long): Int

    public fun offsetForLocal(localSecond: Long): Int
}

// Transitions as parallel arrays: instants, offsets after each transition, and the local time at which each one happens
// (instant + the smaller of the two offsets). A local time inside a gap or an overlap takes the offset before the
// transition (RFC 5545 §3.3.5: a gap moves forward by its length, an overlap takes the first occurrence). Later years
// come from a `TransitionSource` in blocks of 50 years; each extension replaces the immutable table, and two threads
// that extend at once build the same table. A table stops growing at MAX_TRANSITIONS (a real zone has a few thousand up
// to 9999; a hostile VTIMEZONE with a SECONDLY rule would grow by 200,000 per block): later times keep the last offset.
internal class TransitionRules(
    private val initialOffset: Int,
    times: LongArray,
    offsets: IntArray,
    count: Int,
    endYear: Int,
    private val source: TransitionSource?,
) : ZoneRules {
    @Volatile
    private var table: TransitionTable = table(times, offsets, count, endYear)

    override fun offsetSeconds(epochSecond: Long): Int {
        val current = covering(epochSecond)
        val index = lastAtOrBefore(current.times, current.timeKeys, current.size, epochSecond)
        return if (index < 0) initialOffset else current.offsets[index]
    }

    override fun offsetForLocal(localSecond: Long): Int {
        var current = table
        if (localSecond > current.coveredUntilLocal) current = covering(localSecond + SECONDS_PER_DAY_LONG)
        val index = lastAtOrBefore(current.localStarts, current.localKeys, current.size, localSecond)
        if (index < 0) return initialOffset
        val before = if (index == 0) initialOffset else current.offsets[index - 1]
        val after = current.offsets[index]
        return if (isBefore(
                localSecond,
                current.times,
                current.timeKeys,
                index,
                maxOf(before, after),
            )
        ) {
            before
        } else {
            after
        }
    }

    fun transitions(start: Long, end: Long): List<ZoneTransition> {
        val current = covering(end)
        val result = ArrayList<ZoneTransition>()
        var index = lastAtOrBefore(current.times, current.timeKeys, current.size, start - 1) + 1
        while (index < current.size && current.times[index] < end) {
            if (current.times[index] >= start) {
                val before = if (index == 0) initialOffset else current.offsets[index - 1]
                result += ZoneTransition(
                    Instant.fromEpochSeconds(current.times[index]),
                    UtcOffset(seconds = before),
                    UtcOffset(seconds = current.offsets[index]),
                )
            }
            index++
        }
        return result
    }

    private fun covering(epochSecond: Long): TransitionTable {
        var current = table
        while (source != null && epochSecond > current.coveredUntil && current.endYear < MAX_YEAR) {
            val sink = TransitionSink()
            val fromYear = current.endYear + 1
            val toYear = minOf(MAX_YEAR, maxOf(fromYear + EXTENSION_YEARS, yearOf(epochSecond) + 1))
            val lastOffset = if (current.size == 0) initialOffset else current.offsets[current.size - 1]
            source.extend(fromYear, toYear, lastOffset, sink)
            if (current.size + sink.size > MAX_TRANSITIONS) {
                current = table(current.times, current.offsets, current.size, MAX_YEAR)
                table = current
                break
            }
            val times = current.times.copyOf(current.size + sink.size)
            val offsets = current.offsets.copyOf(current.size + sink.size)
            var size = current.size
            var previous = lastOffset
            for (index in 0 until sink.size) {
                if (sink.offsets[index] == previous) continue
                if (size > 0 && sink.times[index] <= times[size - 1]) continue
                times[size] = sink.times[index]
                offsets[size] = sink.offsets[index]
                previous = sink.offsets[index]
                size++
            }
            current = table(times, offsets, size, toYear)
            table = current
        }
        return current
    }

    private fun table(times: LongArray, offsets: IntArray, size: Int, endYear: Int): TransitionTable {
        val locals = LongArray(size)
        for (index in 0 until size) {
            val before = if (index == 0) initialOffset else offsets[index - 1]
            locals[index] = times[index] + minOf(before, offsets[index])
        }
        val until = if (endYear >= MAX_YEAR) Long.MAX_VALUE else (dayOf(endYear + 1, 1, 1) - 2) * SECONDS_PER_DAY_LONG
        val untilLocal = if (until == Long.MAX_VALUE) Long.MAX_VALUE else until - SECONDS_PER_DAY_LONG
        return TransitionTable(
            times,
            offsets,
            locals,
            searchKeys(times, size),
            searchKeys(locals, size),
            size,
            until,
            untilLocal,
            endYear,
        )
    }

    override fun toString(): String = "TransitionRules"
}

// A zone for the engines, in epoch and local seconds (`ZoneRules`). The public zone wraps the rules: Kotlin/Native
// keeps every virtual method of a class that is created or type-checked, and `offsetAt` of a public zone makes a
// UtcOffset, which pulls the formats of kotlinx-datetime.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class TransitionZone internal constructor(override val id: String, internal val rules: TransitionRules) :
    ICalTimeZone,
    ZoneRules {
    public constructor(
        id: String,
        initialOffset: Int,
        times: LongArray,
        offsets: IntArray,
        count: Int,
        endYear: Int,
        source: TransitionSource?,
    ) : this(id, TransitionRules(initialOffset, times, offsets, count, endYear, source))

    override fun offsetAt(instant: Instant): UtcOffset = UtcOffset(seconds = rules.offsetSeconds(instant.epochSeconds))

    override fun offsetSeconds(epochSecond: Long): Int = rules.offsetSeconds(epochSecond)

    override fun toInstant(local: LocalDateTime): Instant {
        val seconds = localSeconds(local)
        return Instant.fromEpochSeconds(seconds - rules.offsetForLocal(seconds), local.nanosecond)
    }

    override fun offsetForLocal(localSecond: Long): Int = rules.offsetForLocal(localSecond)

    override fun transitions(from: Instant, to: Instant): List<ZoneTransition> =
        rules.transitions(from.epochSeconds, to.epochSeconds)

    override fun toString(): String = "ICalTimeZone($id)"
}

// One `Long` multiplication and addition: the seconds of the day are an `Int` (on JS each `Long` operation is a call).
internal fun localSeconds(local: LocalDateTime): Long =
    dayOf(local.year, local.month.number, local.day) * SECONDS_PER_DAY_LONG +
        (local.hour * 3600 + local.minute * 60 + local.second)

internal fun yearOf(epochSecond: Long): Int {
    val days = io.github.calendrum.recur.floorDiv(epochSecond, SECONDS_PER_DAY_LONG)
    return io.github.calendrum.recur.civilOf(days.coerceIn(-1_000_000L, 4_000_000L).toInt()) shr 9
}

private const val MAX_YEAR = 9999
private const val EXTENSION_YEARS = 50

// The search over `Long` keys, the path of every platform but JS. Inline, so that the `actual` of a platform is the loop
// itself, as before the search moved to the platforms.
@Suppress("NOTHING_TO_INLINE")
internal inline fun lastAtOrBeforeLong(values: LongArray, size: Int, key: Long): Int {
    var low = 0
    var high = size - 1
    var found = -1
    while (low <= high) {
        val middle = (low + high) ushr 1
        if (values[middle] <= key) {
            found = middle
            low = middle + 1
        } else {
            high = middle - 1
        }
    }
    return found
}
