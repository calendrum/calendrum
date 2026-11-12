package io.github.calendrum.samples

import io.github.calendrum.ICal
import io.github.calendrum.itip.IMip
import io.github.calendrum.itip.ITip
import io.github.calendrum.itip.ITipOutcome
import io.github.calendrum.itip.SenderCheck
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.value.ICalDateTime
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

private val fixedClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-10-01T08:00:00Z")
}

private const val MEETING = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\nBEGIN:VEVENT\r\n" +
    "UID:42@example.com\r\nDTSTAMP:20260901T000000Z\r\nDTSTART:20261005T100000Z\r\nDTEND:20261005T110000Z\r\n" +
    "SUMMARY:Weekly sync\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun invitationAndReply() {
    val event = ICal.parse(MEETING).calendars.single().events.single()
    val organizer = Organizer.builder("mailto:alice@example.com").build()
    val bob = Attendee.builder("mailto:bob@example.com").rsvp(true).build()
    val organizerCalendar = VCalendar.builder().prodId("-//Example//Sample//EN").version("2.0").event(event).build()

    val request = ITip.request(event, organizer, listOf(bob), fixedClock)
    val reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, fixedClock)

    assertEquals(SenderCheck.Matches, IMip.checkSender(reply, "bob@example.com"))
    val result = ITip.apply(organizerCalendar, reply)
    assertEquals(ITipOutcome.Applied, result.outcome)
    assertEquals(PartStat.Accepted, result.calendar.events.single().attendees.single().partStat)
}

fun reviseAnEvent() {
    val event = ICal.parse(MEETING).calendars.single().events.single()
    val longer = event.edit { end = ICalDateTime.Utc(Instant.parse("2026-10-05T12:00:00Z")) }

    val revised = ITip.revise(event, longer)

    assertEquals(1, revised.sequence)
}
