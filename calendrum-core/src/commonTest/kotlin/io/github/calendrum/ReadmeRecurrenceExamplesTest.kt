package io.github.calendrum

import io.github.calendrum.model.calendars
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.recur.expand
import io.github.calendrum.recur.recurrenceSet
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ReadmeRecurrenceExamplesTest {
    private val calendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTART;TZID=Europe/Berlin:20261005T100000\r\n" +
            "RRULE:FREQ=WEEKLY;BYDAY=MO,WE;COUNT=6\r\nEXDATE;TZID=Europe/Berlin:20261007T100000\r\nEND:VEVENT\r\n" +
            "END:VCALENDAR\r\n",
    ).calendars.single()
    private val from = ICalDateTime.Floating(LocalDateTime(2026, 10, 6, 0, 0))
    private val to = ICalDateTime.Floating(LocalDateTime(2026, 10, 20, 0, 0))
    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    @Test
    fun theRecurrenceSnippetOfTheReadmeCompilesAndWorks() {
        val total = snippet()
        assertEquals(
            listOf(
                "2026-10-12T10:00[Europe/Berlin]",
                "2026-10-14T10:00[Europe/Berlin]",
                "2026-10-19T10:00[Europe/Berlin]",
            ),
            printed,
        )
        assertEquals(5, total)
    }

    private fun snippet(): Int {
        val event = calendar.events.first()
        val set = event.recurrenceSet ?: return 0
        for (start in set.between(from, to)) println(start)
        val next = event.rrule!!.expand(event.start!!).take(10).toList()
        assertEquals(6, next.size)
        val options = RecurrenceOptions.builder()
            .startMismatch(StartMismatch.Exclude)
            .limits(RecurrenceLimits.builder().maxOccurrences(10_000).build())
            .problemListener { println(it) }
            .build()
        val total = Sequence { set.iterator(options) }.count()
        return total
    }
}
