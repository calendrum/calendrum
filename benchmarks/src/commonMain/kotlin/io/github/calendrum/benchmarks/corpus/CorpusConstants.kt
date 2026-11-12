package io.github.calendrum.benchmarks.corpus

// Bump on any change to generator output, content as well as structure, and regenerate `benchmarks/corpora.sha256`.
// Benchmark results across generator versions are not comparable.
internal const val CORPUS_GENERATOR_VERSION: Int = 1
internal const val CORPUS_SEED: Long = 5545L

internal val CORPUS_BASE_DATE: CivilDate = CivilDate(2026, 9, 1)

internal const val INVITE_ATTENDEE_MIN: Int = 5
internal const val INVITE_ATTENDEE_MAX: Int = 10
internal const val INVITE_DESCRIPTION_SENTENCES: Int = 16

internal const val INVITE_BATCH_COUNT: Int = 1000

internal const val PERSONAL_EVENT_COUNT: Int = 5000
internal const val PERSONAL_RECURRING_PERCENT: Int = 15
internal const val PERSONAL_ALL_DAY_PERCENT: Int = 10
internal const val PERSONAL_ALARM_PERCENT: Int = 30
internal const val PERSONAL_X_PROP_PERCENT: Int = 40
internal const val PERSONAL_DESCRIPTION_SENTENCES: Int = 5

internal const val UNIVERSITY_SEMESTER_WEEKS: Int = 15
internal const val UNIVERSITY_COURSE_SERIES_COUNT: Int = 12_000
internal const val UNIVERSITY_OVERRIDE_PERCENT: Int = 10
internal const val UNIVERSITY_DESCRIPTION_SENTENCES: Int = 3

// Week index (0-based, within the semester) paired with the weekday it falls on; only series meeting on that weekday
// get an `EXDATE` for that week. That is enough to exercise EXDATE without a real academic calendar.
internal val UNIVERSITY_HOLIDAYS: List<Pair<Int, Weekday>> = listOf(
    6 to Weekday.MONDAY,
    11 to Weekday.THURSDAY,
)

internal const val HUGE_TARGET_BYTES: Long = 100_000_000L
