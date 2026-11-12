@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum

import io.github.calendrum.itip.IMip
import io.github.calendrum.itip.ITip
import io.github.calendrum.itip.ITipOutcome
import io.github.calendrum.itip.SenderCheck
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Method
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class ReadmeITipExamplesTest {
    private val clock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
    }
    private val event = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260101T000000Z\r\n" +
            "DTSTART:20261005T100000Z\r\nDTEND:20261005T110000Z\r\nSUMMARY:Sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
    ).calendars.single().events.single()
    private val organizer = Organizer.builder("mailto:alice@example.com").build()
    private val bob = Attendee.builder("mailto:bob@example.com").rsvp(true).build()
    private val organizerCalendar = VCalendar.builder().prodId("-//a//EN").version("2.0").event(event).build()
    private val smtpFrom = "bob@example.com"
    private val saved = ArrayList<VCalendar>()

    private fun save(calendar: VCalendar) {
        saved += calendar
    }

    @Test
    fun theITipSnippetOfTheReadmeCompilesAndWorks() {
        val previous = event
        val edited = event.edit { end = ICalDateTime.Utc(Instant.parse("2026-10-05T12:00:00Z")) }
        val request = ITip.request(event, organizer, listOf(bob), clock)
        val reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, clock)
        if (IMip.checkSender(reply, smtpFrom) == SenderCheck.Matches) {
            val result = ITip.apply(organizerCalendar, reply)
            assertEquals(ITipOutcome.Applied, result.outcome)
            save(result.calendar)
        }
        val moved = ITip.revise(previous, edited)
        val links = IMip.untrustedContent(request)
        val contentType = IMip.contentType(Method.Request)
        assertEquals(PartStat.Accepted, saved.single().events.single().attendees.single().partStat)
        assertEquals(1, moved.sequence)
        assertEquals(emptyList(), links)
        assertEquals("text/calendar; method=REQUEST; charset=UTF-8", contentType)
    }
}
