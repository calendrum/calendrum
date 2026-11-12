package io.github.calendrum.benchmarks.corpus

internal fun weeklyOccurrences(dtStart: CivilDate, occurrenceCount: Int): List<CivilDate> =
    (0 until occurrenceCount).map { week -> dtStart.plusWeeks(week.toLong()) }
