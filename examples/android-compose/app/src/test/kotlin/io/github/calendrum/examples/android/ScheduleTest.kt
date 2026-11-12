package io.github.calendrum.examples.android

import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import kotlin.time.Instant

class ScheduleTest {
    @Test
    fun schedule() {
        val feed = File(checkNotNull(System.getProperty("examples.sample"))).readBytes()
        val items = Schedule.from(
            feed,
            Instant.parse("2026-09-01T00:00:00Z"),
            Instant.parse("2026-12-01T00:00:00Z"),
            TimeZone.of("Europe/Berlin"),
        )
        assertEquals(12, items.size)
        assertEquals("2026-09-07 10:00–11:30 Distributed systems seminar", items[0].line())
        assertEquals("2026-09-14 12:00–13:30 Distributed systems seminar (moved)", items[1].line())
        assertEquals("2026-11-23 10:00–11:30 Distributed systems seminar", items[11].line())
        assertEquals("Room 101", items[0].location)
    }

    private fun ScheduleItem.line() = "$day $time $title"
}
