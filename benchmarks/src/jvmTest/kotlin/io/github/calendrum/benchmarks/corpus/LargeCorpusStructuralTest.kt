package io.github.calendrum.benchmarks.corpus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// The `invite-batch` and `university` cases of the common corpus tests, JVM only. The debug test binaries of
// Kotlin/Native and JS spent most of their time there (the selection invariant on `university` alone took 25 s on
// macosArm64); `invite` and `personal` keep every check on every platform, and `CorpusDeterminismTest` keeps all four
// corpora there, so the corpora stay byte-identical across platforms.
class LargeCorpusStructuralTest {
    @Test
    fun inviteBatch() {
        val bytes = corpusBytes(Corpus.INVITE_BATCH)
        assertWellFormed(bytes, Corpus.INVITE_BATCH)
        val events = parseEvents(unfoldLines(bytes))
        assertEquals(INVITE_BATCH_COUNT, events.size)
        assertEquals(INVITE_BATCH_COUNT, events.map { it.uid }.toSet().size)
    }

    @Test
    fun university() {
        val bytes = corpusBytes(Corpus.UNIVERSITY)
        assertWellFormed(bytes, Corpus.UNIVERSITY)
        val events = parseEvents(unfoldLines(bytes))

        val masters = events.filter { it.recurrenceId == null }
        val overrides = events.filter { it.recurrenceId != null }
        assertEquals(UNIVERSITY_COURSE_SERIES_COUNT, masters.size)
        assertEquals(UNIVERSITY_COURSE_SERIES_COUNT, masters.map { it.uid }.toSet().size)

        val mastersByUid = masters.associateBy { it.uid }
        var totalOccurrences = 0L
        for (master in masters) {
            val (tzid, dtstartValue) = requireNotNull(master.dtstart)
            val dtstartDate = CivilDate.fromEpochDay(daysFromBasicDate(dtstartValue))
            val occurrences = weeklyOccurrences(dtstartDate, UNIVERSITY_SEMESTER_WEEKS)
            totalOccurrences += occurrences.size
            val excluded = master.exdates.map { (exTzid, exValue) ->
                assertEquals(tzid, exTzid, "EXDATE TZID must match DTSTART TZID")
                daysFromBasicDate(exValue)
            }.toSet()
            for (excludedDay in excluded) {
                assertTrue(
                    occurrences.any { it.epochDay == excludedDay },
                    "EXDATE $excludedDay is not an occurrence of ${master.uid}",
                )
            }
        }

        for (override in overrides) {
            val master = requireNotNull(mastersByUid[override.uid]) {
                "override UID ${override.uid} has no master series"
            }
            val (tzid, recurrenceValue) = requireNotNull(override.recurrenceId)
            val (masterTzid, masterDtstart) = requireNotNull(master.dtstart)
            assertEquals(masterTzid, tzid, "RECURRENCE-ID TZID must match master DTSTART TZID")
            val recurrenceDay = daysFromBasicDate(recurrenceValue)
            val dtstartDate = CivilDate.fromEpochDay(daysFromBasicDate(masterDtstart))
            val occurrences = weeklyOccurrences(dtstartDate, UNIVERSITY_SEMESTER_WEEKS)
            assertTrue(
                occurrences.any { it.epochDay == recurrenceDay },
                "RECURRENCE-ID $recurrenceValue of ${override.uid} is not an occurrence of its series",
            )
            val excludedDays = master.exdates.map { (_, exValue) -> daysFromBasicDate(exValue) }.toSet()
            assertTrue(
                recurrenceDay !in excludedDays,
                "RECURRENCE-ID $recurrenceValue of ${override.uid} is also excluded via EXDATE",
            )
        }

        val overridePercent = overrides.size * 100.0 / totalOccurrences
        assertPercentInRange(overridePercent, UNIVERSITY_OVERRIDE_PERCENT, "override")
    }
}
