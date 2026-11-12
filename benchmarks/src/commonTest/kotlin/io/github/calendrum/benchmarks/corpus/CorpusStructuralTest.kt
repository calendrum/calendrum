package io.github.calendrum.benchmarks.corpus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class EventRecord(
    val uid: String,
    val recurrenceId: Pair<String, String>?,
    val rrule: String?,
    val exdates: List<Pair<String, String>>,
    val dtstart: Pair<String, String>?,
    val dtstartAllDay: Boolean,
)

internal fun parseEvents(logicalLines: List<String>): List<EventRecord> {
    val events = mutableListOf<EventRecord>()
    var index = 0
    while (index < logicalLines.size) {
        if (logicalLines[index] == "BEGIN:VEVENT") {
            var uid = ""
            var recurrenceId: Pair<String, String>? = null
            var rrule: String? = null
            val exdates = mutableListOf<Pair<String, String>>()
            var dtstart: Pair<String, String>? = null
            var dtstartAllDay = false
            index++
            while (logicalLines[index] != "END:VEVENT") {
                val line = logicalLines[index]
                when {
                    line.startsWith("UID:") -> uid = line.removePrefix("UID:")

                    line.startsWith("RECURRENCE-ID;TZID=") ->
                        recurrenceId = tzidValue(line.removePrefix("RECURRENCE-ID;"))

                    line.startsWith("RRULE:") -> rrule = line.removePrefix("RRULE:")

                    line.startsWith("EXDATE;TZID=") -> exdates += tzidValue(line.removePrefix("EXDATE;"))

                    line.startsWith("DTSTART;TZID=") -> dtstart = tzidValue(line.removePrefix("DTSTART;"))

                    line.startsWith("DTSTART;VALUE=DATE:") -> dtstartAllDay = true
                }
                index++
            }
            events += EventRecord(uid, recurrenceId, rrule, exdates, dtstart, dtstartAllDay)
        }
        index++
    }
    return events
}

private fun tzidValue(withoutPrefix: String): Pair<String, String> {
    val tzid = withoutPrefix.substringAfter('=').substringBefore(':')
    val value = withoutPrefix.substringAfter(':')
    return tzid to value
}

// The ratios of the generator (recurring, all-day, RECURRENCE-ID overrides) come from independent per-event coin flips
// (`SplitMix64.chancePercent`), so the realized share only converges to the target as the sample grows; ±4 points is
// comfortably inside that noise for the corpus sizes here without masking a real regression.
private const val PERCENT_TOLERANCE: Int = 4

internal fun assertPercentInRange(actual: Double, expected: Int, label: String) {
    val range = (expected - PERCENT_TOLERANCE).toDouble()..(expected + PERCENT_TOLERANCE).toDouble()
    assertTrue(actual in range, "$label share $actual% outside expected range $range")
}

internal fun assertWellFormed(bytes: ByteArray, name: String) {
    val folding = analyzeFolding(bytes)
    assertTrue(folding.violations.isEmpty(), "$name: folding violations ${folding.violations}")
    assertTrue(!folding.hasBareCrOrLf, "$name: bare CR or LF found")
    val logicalLines = unfoldLines(bytes)
    val balance = checkBalance(logicalLines)
    for ((component, delta) in balance) {
        assertEquals(0, delta, "$name: unbalanced BEGIN/END for $component")
    }
}

class CorpusStructuralTest {
    @Test
    fun invite() {
        val bytes = corpusBytes(Corpus.INVITE)
        assertWellFormed(bytes, Corpus.INVITE)
        val events = parseEvents(unfoldLines(bytes))
        assertEquals(1, events.size)
        assertEquals(setOf(events[0].uid), events.map { it.uid }.toSet())
    }

    @Test
    fun personal() {
        val bytes = corpusBytes(Corpus.PERSONAL)
        assertWellFormed(bytes, Corpus.PERSONAL)
        val events = parseEvents(unfoldLines(bytes))
        assertEquals(PERSONAL_EVENT_COUNT, events.size)
        assertEquals(PERSONAL_EVENT_COUNT, events.map { it.uid }.toSet().size)

        val recurringPercent = events.count { it.rrule != null } * 100.0 / events.size
        assertPercentInRange(recurringPercent, PERSONAL_RECURRING_PERCENT, "recurring")
        val allDayPercent = events.count { it.dtstartAllDay } * 100.0 / events.size
        assertPercentInRange(allDayPercent, PERSONAL_ALL_DAY_PERCENT, "all-day")
    }
}

internal fun daysFromBasicDate(basicDateTime: String): Long {
    val year = basicDateTime.substring(0, 4).toInt()
    val month = basicDateTime.substring(4, 6).toInt()
    val day = basicDateTime.substring(6, 8).toInt()
    return daysFromCivil(year, month, day)
}
