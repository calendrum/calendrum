package io.github.calendrum.ical4j.guides

import io.github.calendrum.ICal
import io.github.calendrum.ical4j.toCalendrum
import io.github.calendrum.ical4j.toIcal4j
import io.github.calendrum.model.calendars
import net.fortuna.ical4j.data.CalendarBuilder
import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class Ical4jGuide {
    private val text =
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Guides//EN\r\nBEGIN:VEVENT\r\nUID:1@example.com\r\n" +
            "DTSTAMP:20260901T000000Z\r\nDTSTART:20261001T080000Z\r\nSUMMARY:Weekly sync\r\nEND:VEVENT\r\n" +
            "END:VCALENDAR\r\n"

    @Test
    fun bothDirections() {
        val oldCalendar = CalendarBuilder().build(ByteArrayInputStream(text.encodeToByteArray()))
        // region ical4j-both
        val ours = oldCalendar.toCalendrum() // ical4j Calendar → VCalendar, as faithful as ical4j's own output
        val summary = ours.events.single().summary // "Weekly sync"
        val theirs = ICal.parse(text).calendars.single().toIcal4j() // VCalendar → ical4j Calendar
        // endregion
        assertEquals("Weekly sync", summary)
        assertEquals(
            "1@example.com",
            theirs.getComponent<net.fortuna.ical4j.model.component.VEvent>("VEVENT").get().getUid().get().value,
        )
    }
}
