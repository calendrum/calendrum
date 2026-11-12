@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.itip.IMip
import io.github.calendrum.itip.ITip
import io.github.calendrum.itip.ITipOutcome
import io.github.calendrum.itip.SenderCheck
import io.github.calendrum.lint.LintRules
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.Method
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.parser
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

class ValidationGuide {
    private val flawed = ICal.parse(
        icsLines(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//Example//Guides//EN",
            "BEGIN:VEVENT",
            "UID:1@example.com",
            "DTSTAMP:20260101T000000Z",
            "DTSTART:20261005T100000Z",
            "DTEND:20261005T090000Z",
            "ATTACH:https://example.com/a.pdf",
            "END:VEVENT",
            "END:VCALENDAR",
        ),
    ).calendars.single()

    @Test
    fun validation() {
        val calendar = flawed
        val printed = ArrayList<String>()
        // region validate-report
        // RuleSet.Rfc5545; a report, never an exception
        val report = Validator.validate(calendar)
        if (!report.isValid) report.issues.forEach { printed += "${it.code.id} ${it.severity} ${it.path}" }
        // METHOD and the RFC 5546 §3.2.2 table
        val invitation = Validator.validate(calendar, RuleSet.itip(Method.Request))
        // endregion
        assertEquals(listOf("ICAL-L011 Error VCALENDAR[0]/VEVENT[0]/DTEND[0]"), printed)
        assertTrue(invitation.issues(ProblemCode.MethodMismatch).isNotEmpty())
    }

    @Test
    fun houseRules() {
        val calendar = flawed
        // region validate-rules
        val house = RuleSet.builder()
            .cardinality("VEVENT", "SUMMARY", Cardinality.One) // our feed requires a title
            .forbid("VEVENT", "ATTACH")
            .build()
        val report = Validator.validate(calendar, house)
        // endregion
        assertFalse(report.isValid)
        assertEquals(3, report.issues.size)
    }

    private val meeting = ICal.parse(
        icsLines(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "PRODID:-//Example//Guides//EN",
            "BEGIN:VEVENT",
            "UID:42@example.com",
            "DTSTAMP:20260901T000000Z",
            "DTSTART:20261005T100000Z",
            "DTEND:20261005T110000Z",
            "SUMMARY:Sync",
            "END:VEVENT",
            "END:VCALENDAR",
        ),
    ).calendars.single()

    @Test
    fun scheduling() {
        val event = meeting.events.single()
        val organizer = Organizer.builder("mailto:alice@example.com").build()
        val bob = Attendee.builder("mailto:bob@example.com").rsvp(true).build()
        val organizerCalendar = VCalendar.builder().prodId("-//a//EN").version("2.0").event(event).build()
        val smtpFrom = "bob@example.com"
        val clock = fixedClock
        val saved = ArrayList<VCalendar>()
        // region itip-flow
        // METHOD:REQUEST, DTSTAMP from the clock
        val request = ITip.request(event, organizer, listOf(bob), clock)
        val reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, clock)
        // FROM against ATTENDEE (or ORGANIZER) and SENT-BY
        if (IMip.checkSender(reply, smtpFrom) == SenderCheck.Matches) {
            val result = ITip.apply(organizerCalendar, reply) // Applied, Outdated, NotFound…
            if (result.outcome == ITipOutcome.Applied) saved += result.calendar
        }
        // endregion
        assertEquals(PartStat.Accepted, saved.single().events.single().attendees.single().partStat)
    }

    @Test
    fun revisionsAndMailHandling() {
        val previous = meeting.events.single()
        val edited = previous.edit { end = ICalDateTime.Utc(Instant.parse("2026-10-05T12:00:00Z")) }
        val organizer = Organizer.builder("mailto:alice@example.com").build()
        val bob = Attendee.builder("mailto:bob@example.com").build()
        val request = ITip.request(previous, organizer, listOf(bob), fixedClock)
        // region itip-revise
        // SEQUENCE + 1 when DTSTART, DTEND, RRULE… changed
        val moved = ITip.revise(previous, edited)
        val links = IMip.untrustedContent(request) // URLs, HTML, attachments: show, never fetch
        val contentType = IMip.contentType(Method.Request) // text/calendar; method=REQUEST; charset=UTF-8
        // endregion
        assertEquals(1, moved.sequence)
        assertEquals(emptyList(), links.map { it.kind })
        assertEquals("text/calendar; method=REQUEST; charset=UTF-8", contentType)
    }

    private val googleFeed = icsLines(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Google Inc//Google Calendar 70.9054//EN",
        "X-WR-TIMEZONE:Europe/Berlin",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "DTSTAMP:20260901T000000Z",
        "DTSTART:20261005T100000",
        "DTEND:20261005T110000",
        "SUMMARY:Weekly sync",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    @Test
    fun fixups() {
        val calendar = ICal.parse(googleFeed).calendars.single()
        // region fixup-apply
        // one repair: floating times get the X-WR-TIMEZONE zone
        val result = Fixups.xWrTimezone.apply(calendar)
        val start = result.calendar.events.single().start // 2026-10-05T10:00[Europe/Berlin]
        val changes = result.changes.map { "${it.fixup} at ${it.path}" } // what changed and where, never the values
        // picks Outlook, Lotus Notes, Google or vCalendar 1.0
        val byProducer = Fixups.byProdId().apply(calendar)
        // endregion
        assertTrue(result.isChanged)
        assertEquals("2026-10-05T10:00[Europe/Berlin]", start.toString())
        assertEquals(2, changes.size)
        assertTrue(byProducer.isChanged)
    }

    @Test
    fun fixupsWhileReading() {
        val feed = googleFeed.replace("DTEND:20261005T110000", "DTEND:")
        // region fixup-intercept
        val parser = ICal.parser {
            // the same repairs, line by line, as the feed is read
            Fixups.lotusNotes.interceptors().forEach { intercept(it) }
        }
        val result = parser.parse(feed)
        // endregion
        val event = result.components.single().components("VEVENT").single()
        assertEquals(null, event.property("DTEND"))
    }

    @Test
    fun linting() {
        val calendar = ICal.parse(googleFeed).calendars.single()
        // region lint-fix
        val report = Linter.lint(calendar) // validation plus interoperability rules
        // includes ICAL-L021…L025, the repairs of Fixups
        val fixable = report.fixable.map { it.code.id }
        val fixed = Linter.fix(calendar, report) // applies the fixable ones
        val clean = Linter.lint(fixed).issues.none { it.code == ProblemCode.VendorTimeZone }
        // choose your rules
        val quiet = Linter.lint(calendar, LintRules.builder().disable(ProblemCode.VendorTimeZone).build())
        // endregion
        assertFalse(report.isClean)
        assertTrue(fixable.isNotEmpty())
        assertTrue(clean)
        assertTrue(quiet.issues.none { it.code == ProblemCode.VendorTimeZone })
    }
}
