package io.github.calendrum.tzdata

import io.github.calendrum.tz.TransitionSink
import io.github.calendrum.tz.TransitionSource
import io.github.calendrum.tz.TransitionZone
import kotlin.concurrent.Volatile

internal class TailRule(
    val month: Int,
    val mode: Int,
    val day: Int,
    val dayOfWeek: Int,
    val time: Int,
    val definition: Int,
    val standardOffset: Int,
    val offsetBefore: Int,
    val offsetAfter: Int,
    val after: Int,
)

internal class ZoneRecord(
    val offsets: IntArray,
    val daylight: BooleanArray,
    val abbreviations: Array<String>,
    val initial: Int,
    val times: LongArray,
    val indexes: IntArray,
    val tailStartYear: Int,
    val tail: Array<TailRule>,
) {
    val initialOffset: Int get() = offsets[initial]

    fun zone(id: String): TransitionZone {
        val after = IntArray(times.size) { offsets[indexes[it]] }
        val source = if (tail.isEmpty()) null else TransitionSource { from, to, _, sink -> extend(from, to, sink) }
        val end = if (tail.isEmpty()) MAX_YEAR else tailStartYear - 1
        return TransitionZone(id, initialOffset, times, after, times.size, end, source)
    }

    fun extend(fromYear: Int, toYear: Int, sink: TransitionSink) {
        val epochs = LongArray(tail.size)
        for (year in maxOf(fromYear, tailStartYear)..toYear) {
            for ((index, rule) in tail.withIndex()) epochs[index] = transitionEpoch(rule, year)
            val order = tail.indices.sortedBy { epochs[it] }
            for (index in order) sink.add(epochs[index], tail[index].offsetAfter)
        }
    }

    override fun toString(): String = "ZoneRecord(${times.size} transitions, ${tail.size} rules)"
}

internal fun transitionEpoch(rule: TailRule, year: Int): Long {
    val date = ruleDay(year, rule.month, rule.mode, rule.day, rule.dayOfWeek)
    val local = date * SECONDS_PER_DAY + rule.time
    return when (rule.definition) {
        DEFINITION_UTC -> local
        DEFINITION_STANDARD -> local - rule.standardOffset
        else -> local - rule.offsetBefore
    }
}

internal fun ruleDay(year: Int, month: Int, mode: Int, day: Int, dayOfWeek: Int): Long {
    val length = monthLength(year, month)
    return when (mode) {
        MODE_LAST -> {
            val last = epochDay(year, month, length)
            last - floorMod(weekday(last) - dayOfWeek, 7)
        }

        MODE_AFTER -> {
            val start = epochDay(year, month, day)
            start + floorMod(dayOfWeek - weekday(start), 7)
        }

        MODE_BEFORE -> {
            val start = epochDay(year, month, day)
            start - floorMod(weekday(start) - dayOfWeek, 7)
        }

        else -> epochDay(year, month, day)
    }
}

// Zone records are decoded on first use from the packed string: varints of five bits per character of the base64
// alphabet; offsets with a daylight flag and an abbreviation, transitions since 1970 as deltas in minutes (or seconds),
// then the tail rules for the years after the table.
internal object TzZones {
    @Volatile
    private var index: Map<String, Int>? = null

    @Volatile
    private var abbreviationList: List<String>? = null

    fun names(): Set<String> = table().keys

    fun record(id: String): ZoneRecord? {
        val start = table()[id] ?: return null
        return decode(zoneData(), start, abbreviations())
    }

    private fun table(): Map<String, Int> = index ?: HashMap<String, Int>().also { map ->
        for (line in zoneIndex().split('\n')) {
            val tab = line.indexOf('\t')
            map[line.substring(0, tab)] = line.substring(tab + 1).toInt()
        }
        index = map
    }

    private fun abbreviations(): List<String> =
        abbreviationList ?: abbreviationTable().split(',').also { abbreviationList = it }

    private fun decode(data: String, start: Int, names: List<String>): ZoneRecord {
        val reader = PackedReader(data, start)
        val count = reader.unsigned().toInt()
        val offsets = IntArray(count)
        val daylight = BooleanArray(count)
        val abbreviations = Array(count) { "" }
        for (index in 0 until count) {
            offsets[index] = reader.signed()
            daylight[index] = reader.unsigned() == 1L
            abbreviations[index] = names[reader.unsigned().toInt()]
        }
        val initial = reader.unsigned().toInt()
        val transitions = reader.unsigned().toInt()
        val times = LongArray(transitions)
        val indexes = IntArray(transitions)
        var previous = 0L
        for (index in 0 until transitions) {
            val encoded = reader.unsigned()
            val delta = if (encoded and 1L == 0L) encoded / 2 * 60 else encoded / 2
            previous += delta
            times[index] = previous
            indexes[index] = reader.unsigned().toInt()
        }
        val tailStart = reader.unsigned().toInt()
        if (tailStart == 0) {
            return ZoneRecord(offsets, daylight, abbreviations, initial, times, indexes, Int.MAX_VALUE, emptyArray())
        }
        val rules = Array(reader.unsigned().toInt()) {
            val month = reader.unsigned().toInt()
            val mode = reader.unsigned().toInt()
            val day = reader.unsigned().toInt()
            val dayOfWeek = reader.unsigned().toInt()
            val time = reader.signed()
            val definition = reader.unsigned().toInt()
            val standard = reader.signed()
            val before = reader.signed()
            val after = reader.unsigned().toInt()
            TailRule(month, mode, day, dayOfWeek, time, definition, standard, before, offsets[after], after)
        }
        return ZoneRecord(offsets, daylight, abbreviations, initial, times, indexes, tailStart, rules)
    }

    override fun toString(): String = "TzZones"
}

internal class PackedReader(private val data: String, private var position: Int) {
    fun unsigned(): Long {
        var result = 0L
        var shift = 0
        while (true) {
            val digit = digit(data[position++])
            result = result or ((digit and 31).toLong() shl shift)
            if (digit and 32 == 0) return result
            shift += 5
        }
    }

    fun signed(): Int {
        val value = unsigned()
        return if (value and 1L == 0L) (value / 2).toInt() else (-(value + 1) / 2).toInt()
    }

    private fun digit(char: Char): Int = when (char) {
        in 'A'..'Z' -> char - 'A'
        in 'a'..'z' -> char - 'a' + 26
        in '0'..'9' -> char - '0' + 52
        '+' -> 62
        else -> 63
    }

    override fun toString(): String = "PackedReader($position)"
}

internal fun epochDay(year: Int, month: Int, day: Int): Long {
    val y = (if (month <= 2) year - 1 else year).toLong()
    val era = (if (y >= 0) y else y - 399) / 400
    val yearOfEra = y - era * 400
    val shiftedMonth = if (month > 2) month - 3 else month + 9
    val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * 146_097 + dayOfEra - 719_468
}

internal fun monthLength(year: Int, month: Int): Int = when (month) {
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

internal fun weekday(epochDay: Long): Int = floorMod((epochDay + 3).toInt(), 7)

internal fun floorMod(value: Int, divisor: Int): Int {
    val remainder = value % divisor
    return if (remainder < 0) remainder + divisor else remainder
}

internal const val SECONDS_PER_DAY = 86_400L
internal const val MODE_AFTER = 1
internal const val MODE_BEFORE = 2
internal const val MODE_LAST = 3
internal const val DEFINITION_STANDARD = 1
internal const val DEFINITION_UTC = 2
private const val MAX_YEAR = 9999
