package io.github.calendrum.benchmarks.corpus

internal fun pad2(value: Int): String = value.toString().padStart(2, '0')

internal fun localDateTime(date: CivilDate, hour: Int, minute: Int, second: Int = 0): String =
    "${date.basicDate()}T${pad2(hour)}${pad2(minute)}${pad2(second)}"

internal fun utcDateTime(date: CivilDate, hour: Int, minute: Int, second: Int = 0): String =
    "${localDateTime(date, hour, minute, second)}Z"

// RFC 5545 §3.3.10: UNTIL must be UTC when DTSTART carries a TZID. Converts the local midnight after the last wanted
// occurrence into UTC using the standard offset of the zone; the generator only needs an upper bound, not a
// DST-accurate instant.
internal fun untilUtcAfter(date: CivilDate, standardOffsetMinutes: Int): String {
    val bufferedLocalMidnight = date.plusDays(1)
    val totalMinutes = -standardOffsetMinutes
    val dayShift = floorDivInt(totalMinutes, MINUTES_PER_DAY)
    val minuteOfDay = floorModInt(totalMinutes, MINUTES_PER_DAY)
    val utcDate = bufferedLocalMidnight.plusDays(dayShift.toLong())
    return utcDateTime(utcDate, minuteOfDay / 60, minuteOfDay % 60)
}

private const val MINUTES_PER_DAY: Int = 24 * 60

private fun floorDivInt(a: Int, b: Int): Int {
    var q = a / b
    if ((a xor b) < 0 && q * b != a) q--
    return q
}

private fun floorModInt(a: Int, b: Int): Int = a - floorDivInt(a, b) * b
