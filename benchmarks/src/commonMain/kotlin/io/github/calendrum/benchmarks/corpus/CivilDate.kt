package io.github.calendrum.benchmarks.corpus

internal class CivilDate(val year: Int, val month: Int, val day: Int) {
    val epochDay: Long = daysFromCivil(year, month, day)

    val weekday: Weekday get() = weekdayOf(epochDay)

    fun plusDays(days: Long): CivilDate = fromEpochDay(epochDay + days)

    fun plusWeeks(weeks: Long): CivilDate = plusDays(weeks * 7)

    fun basicDate(): String = year.toString().padStart(4, '0') + month.toString().padStart(2, '0') +
        day.toString().padStart(2, '0')

    override fun toString(): String = basicDate()

    companion object {
        fun fromEpochDay(epochDay: Long): CivilDate {
            val (y, m, d) = civilFromDays(epochDay)
            return CivilDate(y, m, d)
        }
    }
}

internal enum class Weekday(val icsCode: String) {
    MONDAY("MO"),
    TUESDAY("TU"),
    WEDNESDAY("WE"),
    THURSDAY("TH"),
    FRIDAY("FR"),
    SATURDAY("SA"),
    SUNDAY("SU"),
}

// Howard Hinnant's `days_from_civil` and `civil_from_days` (public domain,
// http://howardhinnant.github.io/date_algorithms.html): proleptic Gregorian day-number arithmetic with no floating
// point and no calendar library, so it is identical on every target.
internal fun daysFromCivil(y: Int, m: Int, d: Int): Long {
    val year = if (m <= 2) y - 1 else y
    val era = (if (year >= 0) year else year - 399) / 400
    val yoe = (year - era * 400).toLong()
    val mp = (m + 9) % 12
    val doy = (153 * mp + 2) / 5 + d - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    return era * 146097L + doe - 719468L
}

internal fun civilFromDays(epochDay: Long): Triple<Int, Int, Int> {
    val z = epochDay + 719468L
    val era = (if (z >= 0) z else z - 146096L) / 146097L
    val doe = z - era * 146097L
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y = yoe + era * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val d = (doy - (153 * mp + 2) / 5 + 1).toInt()
    val m = (if (mp < 10) mp + 3 else mp - 9).toInt()
    val year = (if (m <= 2) y + 1 else y).toInt()
    return Triple(year, m, d)
}

// 1970-01-01 (epoch day 0) is a Thursday; `+3` rotates the Mon=0..Sun=6 index onto that fact.
internal fun weekdayOf(epochDay: Long): Weekday {
    val index = (((epochDay + 3) % 7 + 7) % 7).toInt()
    return Weekday.entries[index]
}
