package io.github.calendrum.validate

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTimedValue

// A guard against quadratic validation: the index of an element among its namesakes used to be found by
// scanning the earlier siblings, so this input took 272 s. The current code takes about half a second alone and up to
// three seconds under the parallel load of `check`; the bound is far from both.
class ValidationScalingTest {
    private val calendar: VCalendar by lazy { ICal.parse(calendar(OVERRIDES, UNKNOWN_PROPERTIES)).calendars.single() }

    @Test
    fun validationOfManyOverridesAndIssuesIsLinear() {
        val (report, time) = measureTimedValue { Validator.validate(calendar) }
        assertTrue(time < BOUND, "validation took $time")
        assertEquals(UNKNOWN_PROPERTIES, report.issues.count { it.code == ProblemCode.UnknownProperty })
        val last = report.issues.last { it.code == ProblemCode.UnknownProperty }
        assertEquals("VCALENDAR[0]/VEVENT[0]/FOO[${UNKNOWN_PROPERTIES - 1}]", last.path.toString())
        assertEquals(OVERRIDES + 1, calendar.component.components.size)
        assertEquals(setOf(ProblemCode.UnknownProperty), report.issues.map { it.code }.toSet())
    }

    @Test
    fun lintOfManyOverridesAndIssuesIsLinear() {
        val (report, time) = measureTimedValue { Linter.lint(calendar) }
        assertTrue(time < BOUND, "lint took $time")
        assertEquals(UNKNOWN_PROPERTIES, report.issues.count { it.code == ProblemCode.UnknownProperty })
    }

    private fun calendar(overrides: Int, unknown: Int): ByteArray {
        val text = StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//ics-mp//scaling//EN\r\n")
        text.append("BEGIN:VEVENT\r\nUID:series\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260101T090000Z\r\n")
        text.append("RRULE:FREQ=MINUTELY\r\n")
        repeat(unknown) { text.append("FOO:").append(it).append("\r\n") }
        repeat(EXDATES) { text.append("EXDATE:2026010").append(1 + it / 1440 % 9).append("T000000Z\r\n") }
        text.append("END:VEVENT\r\n")
        repeat(overrides) {
            val at = "${20260102 + it / 1440 % 27}T${(it / 60 % 24).pad()}${(it % 60).pad()}00Z"
            text.append("BEGIN:VEVENT\r\nUID:series\r\nDTSTAMP:20260101T000000Z\r\nRECURRENCE-ID:").append(at)
            text.append("\r\nDTSTART:").append(at).append("\r\nEND:VEVENT\r\n")
        }
        return text.append("END:VCALENDAR\r\n").toString().encodeToByteArray()
    }

    private fun Int.pad(): String = toString().padStart(2, '0')

    private companion object {
        const val OVERRIDES = 300_000
        const val UNKNOWN_PROPERTIES = 50_000
        const val EXDATES = 20_000
        val BOUND = 20.seconds
    }
}
