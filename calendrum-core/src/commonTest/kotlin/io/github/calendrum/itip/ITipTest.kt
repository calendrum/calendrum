@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.itip

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.Method
import io.github.calendrum.model.Organizer
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.Validator
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class ITipTest {
    private var now = Instant.parse("2026-10-01T08:00:00Z")
    private val clock = object : Clock {
        override fun now(): Instant = now
    }
    private val organizer = Organizer.builder("mailto:a@example.com").cn("A").build()
    private val b = Attendee.builder("mailto:b@example.com").cn("B").rsvp(true).build()
    private val c = Attendee.builder("mailto:c@example.com").cn("C").rsvp(true).build()

    private fun store(prodId: String): VCalendar.Builder = VCalendar.builder().prodId(prodId).version("2.0")

    private fun parse(text: String): VCalendar = ICal.parse(text).calendars.single()

    private fun event(vararg lines: String): VEvent = parse(
        (
            listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") +
                listOf("BEGIN:VEVENT", "UID:meeting-1", "DTSTAMP:20260901T000000Z") +
                lines.toList() + listOf("END:VEVENT", "END:VCALENDAR")
            ).joinToString("") { "$it\r\n" },
    ).events.single()

    private val weekly = event(
        "DTSTART:20261005T100000Z",
        "DTEND:20261005T110000Z",
        "SUMMARY:Weekly sync",
        "RRULE:FREQ=WEEKLY;COUNT=4",
    )

    private fun assertValid(message: VCalendar) {
        val method = message.method!!
        val report = Validator.validate(message, RuleSet.itip(method))
        assertTrue(report.isValid, "${method.value}: ${report.issues.map { "${it.code.id} ${it.path}" }}")
        val again = parse(ICal.writer().encodeToString(message.component))
        assertTrue(Validator.validate(again, RuleSet.itip(method)).isValid)
    }

    private fun starts(calendar: VCalendar): List<String> = calendar.occurrences(
        Instant.parse("2026-10-01T00:00:00Z"),
        Instant.parse("2026-12-01T00:00:00Z"),
        OccurrenceOptions.Default,
    ).map { it.start.toString() }.toList()

    private fun partStat(calendar: VCalendar, address: String, index: Int = 0): PartStat? =
        calendar.events[index].attendees.firstOrNull { it.address == address }?.partStat

    @Test
    fun requestReplyApply() {
        val request = ITip.request(weekly, organizer, listOf(b, c), clock)
        assertValid(request)
        assertEquals(Method.Request, request.method)
        assertEquals(0, request.events.single().sequence)
        assertEquals("2026-10-01T08:00:00Z", request.events.single().stamp.toString())
        val organizerStore = store("-//a//EN").event(request.events.single()).build()
        val attendeeStore = ITip.apply(store("-//b//EN").build(), request)
        assertEquals(ITipOutcome.Applied, attendeeStore.outcome)
        assertEquals(1, attendeeStore.calendar.events.size)
        assertNull(attendeeStore.calendar.method)

        now = Instant.parse("2026-10-01T09:00:00Z")
        val accept = ITip.reply(request, "b@example.com", PartStat.Accepted, clock)
        assertValid(accept)
        assertEquals(listOf("mailto:b@example.com"), accept.events.single().attendees.map { it.address })
        assertEquals(PartStat.Accepted, accept.events.single().attendees.single().partStat)
        val decline = ITip.reply(request, "mailto:C@example.com", PartStat.Declined, clock)
        val afterAccept = ITip.apply(organizerStore, accept)
        assertEquals(ITipOutcome.Applied, afterAccept.outcome)
        val afterBoth = ITip.apply(afterAccept.calendar, decline).calendar
        assertEquals(PartStat.Accepted, partStat(afterBoth, "mailto:b@example.com"))
        assertEquals(PartStat.Declined, partStat(afterBoth, "mailto:c@example.com"))
        assertEquals("B", afterBoth.events.single().attendees.first().cn)
        assertEquals(4, starts(afterBoth).size)

        val crasher = ITip.reply(request, "mailto:d@example.com", PartStat.Tentative, clock)
        val crashed = ITip.apply(afterBoth, crasher).calendar
        assertEquals(PartStat.Tentative, partStat(crashed, "mailto:d@example.com"))
    }

    @Test
    fun revisionsAndSequence() {
        val first = ITip.request(weekly, organizer, listOf(b), clock)
        val stored = first.events.single()
        val retitled = ITip.revise(stored, stored.edit { summary = "Sync" })
        assertEquals(0, retitled.sequence)
        val moved = ITip.revise(
            stored,
            stored.edit {
                start = ICalDateTime.Utc(Instant.parse("2026-10-06T10:00:00Z"))
                end = ICalDateTime.Utc(Instant.parse("2026-10-06T11:00:00Z"))
            },
        )
        assertEquals(1, moved.sequence)
        assertEquals(5, ITip.revise(stored, stored.edit { sequence = 5 }).sequence)

        val attendee = ITip.apply(store("-//b//EN").build(), first).calendar
        now = Instant.parse("2026-10-02T08:00:00Z")
        val second = ITip.request(moved, organizer, listOf(b), clock)
        assertValid(second)
        val updated = ITip.apply(attendee, second)
        assertEquals(ITipOutcome.Applied, updated.outcome)
        assertEquals(1, updated.calendar.events.single().sequence)
        assertEquals("2026-10-06T10:00:00Z", updated.calendar.events.single().start.toString())
        val stale = ITip.apply(updated.calendar, first)
        assertEquals(ITipOutcome.Outdated, stale.outcome)
        assertSame(updated.calendar, stale.calendar)

        val oldReply = ITip.reply(first, "mailto:b@example.com", PartStat.Accepted, clock)
        val organizerStore = store("-//a//EN").event(second.events.single()).build()
        assertEquals(ITipOutcome.Outdated, ITip.apply(organizerStore, oldReply).outcome)
        assertEquals(ITipOutcome.NotFound, ITip.apply(VCalendar.builder().build(), oldReply).outcome)
    }

    @Test
    fun cancellations() {
        val request = ITip.request(weekly, organizer, listOf(b), clock)
        val attendee = ITip.apply(store("-//b//EN").build(), request).calendar
        val one = ITip.cancel(request.events.single(), ICalDateTime.Utc(Instant.parse("2026-10-12T10:00:00Z")), clock)
        assertValid(one)
        assertEquals(1, one.events.single().sequence)
        assertEquals("2026-10-12T10:00:00Z", one.events.single().recurrenceId.toString())
        assertEquals("2026-10-12T11:00:00Z", one.events.single().end.toString())
        val withoutOne = ITip.apply(attendee, one)
        assertEquals(ITipOutcome.Applied, withoutOne.outcome)
        assertEquals(
            listOf("2026-10-05T10:00:00Z", "2026-10-19T10:00:00Z", "2026-10-26T10:00:00Z"),
            starts(withoutOne.calendar),
        )
        assertEquals(1, withoutOne.calendar.events.single().sequence)

        val all = ITip.cancel(request.events.single(), clock)
        assertValid(all)
        val cancelled = ITip.apply(withoutOne.calendar, all).calendar
        assertEquals("CANCELLED", cancelled.events.single().status?.value)
        assertEquals(emptyList(), starts(cancelled))
        assertEquals(ITipOutcome.NotFound, ITip.apply(VCalendar.builder().build(), all).outcome)
    }

    @Test
    fun addRefreshCounterDecline() {
        val request = ITip.request(weekly, organizer, listOf(b), clock)
        val attendee = ITip.apply(store("-//b//EN").build(), request).calendar
        val extra = event("DTSTART:20261110T100000Z", "DTEND:20261110T120000Z", "SUMMARY:Extra sync")
        val add = ITip.add(request.events.single(), extra, clock)
        assertValid(add)
        assertEquals("meeting-1", add.events.single().uid)
        val added = ITip.apply(attendee, add).calendar
        assertEquals(5, starts(added).size)
        assertEquals(2, added.events.size)
        assertEquals(ITipOutcome.NotFound, ITip.apply(VCalendar.builder().build(), add).outcome)

        val refresh = ITip.refresh(attendee.events.single(), "mailto:b@example.com", clock)
        assertValid(refresh)
        val organizerStore = store("-//a//EN").event(request.events.single()).build()
        val refreshed = ITip.apply(organizerStore, refresh)
        assertEquals(ITipOutcome.ResponseRequired, refreshed.outcome)
        assertSame(organizerStore, refreshed.calendar)

        val proposal = attendee.events.single().edit {
            start = ICalDateTime.Utc(Instant.parse("2026-10-05T12:00:00Z"))
            end = ICalDateTime.Utc(Instant.parse("2026-10-05T13:00:00Z"))
        }
        val counter = ITip.counter(proposal, "mailto:b@example.com", clock)
        assertValid(counter)
        assertEquals(ITipOutcome.ResponseRequired, ITip.apply(organizerStore, counter).outcome)
        val decline = ITip.declineCounter(counter, clock)
        assertValid(decline)
        assertEquals(ITipOutcome.Ignored, ITip.apply(attendee, decline).outcome)
        assertFailsWith<IllegalArgumentException> {
            ITip.reply(VCalendar.builder().build(), "mailto:b@example.com", PartStat.Accepted, clock)
        }
    }

    @Test
    fun instanceRepliesAndRequests() {
        val request = ITip.request(weekly, organizer, listOf(b), clock)
        val organizerStore = store("-//a//EN").event(request.events.single()).build()
        val instance = ITip.request(
            ITip.cancel(request.events.single(), ICalDateTime.Utc(Instant.parse("2026-10-19T10:00:00Z")), clock)
                .events.single().edit {
                    status = null
                    sequence = 0
                    summary = "Weekly sync, room 2"
                },
            organizer,
            listOf(b),
            clock,
        )
        val reply = ITip.reply(instance, "mailto:b@example.com", PartStat.Declined, clock)
        assertValid(reply)
        val applied = ITip.apply(organizerStore, reply).calendar
        assertEquals(2, applied.events.size)
        assertEquals(PartStat.Declined, partStat(applied, "mailto:b@example.com", 1))
        assertEquals(PartStat.NeedsAction, partStat(applied, "mailto:b@example.com", 0))
        assertEquals("2026-10-19T11:00:00Z", applied.events[1].end.toString())
        assertEquals(4, starts(applied).size)

        val attendee = ITip.apply(store("-//b//EN").build(), request).calendar
        val moved = ITip.apply(attendee, instance)
        assertEquals(ITipOutcome.Applied, moved.outcome)
        assertEquals(
            listOf(null, "Weekly sync, room 2"),
            moved.calendar.events.map { if (it.recurrenceId == null) null else it.summary },
        )
    }

    // Parsing keeps a control character or a parameter without a value, rebuilding the attendee does not.
    @Test
    fun aReplyWhoseAttendeeCannotBeRebuiltIsIgnored() {
        val request = ITip.request(weekly, organizer, listOf(b), clock)
        val organizerStore = store("-//a//EN").event(request.events.single()).build()
        val instance = "RECURRENCE-ID:20261019T100000Z"
        for (
        (attendee, recurrenceId) in listOf(
            "ATTENDEE;PARTSTAT=ACC\u0001EPTED:mailto:b@example.com" to null,
            "ATTENDEE;PARTSTAT=DECLINED;DELEGATED-TO=\"mailto:c\u0007@example.com\":mailto:b@example.com" to null,
            "ATTENDEE;PARTSTAT=ACCEPTED;DELEGATED-FROM=\"mailto:\u001f@example.com\":mailto:b@example.com" to instance,
            "ATTENDEE;PARTSTAT=ACCEPTED:mailto:new\u0000@example.com" to null,
            "ATTENDEE;X-EMPTY;PARTSTAT=ACCEPTED:mailto:new@example.com" to instance,
        )
        ) {
            val reply = parse(
                listOfNotNull(
                    "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//b//EN", "METHOD:REPLY", "BEGIN:VEVENT",
                    "UID:${request.events.single().uid}", "DTSTAMP:20261002T000000Z", "SEQUENCE:0", recurrenceId,
                    attendee, "END:VEVENT", "END:VCALENDAR",
                ).joinToString("") { "$it\r\n" },
            )
            val result = ITip.apply(organizerStore, reply)
            assertEquals(ITipOutcome.Ignored, result.outcome, attendee)
            assertSame(organizerStore, result.calendar)
        }
        val canonical = ICal.writer(WriteOptions.builder().byteStable(false).build())
        val accepted = ITip.apply(organizerStore, ITip.reply(request, "mailto:b@example.com", PartStat.Accepted, clock))
        assertEquals(ITipOutcome.Applied, accepted.outcome)
        canonical.encodeToString(accepted.calendar.component)
    }

    // The series are found by an index of UIDs; the order of the store is the one that inserting after the
    // master, replacing in place and removing gave on a list, also for a series whose members are not adjacent.
    @Test
    fun seriesWhoseMembersAreNotAdjacentKeepTheirPlaces() {
        fun component(name: String, uid: String, vararg lines: String) =
            listOf("BEGIN:$name", "UID:$uid", "DTSTAMP:20260901T000000Z", *lines, "END:$name")
        fun calendar(method: String?, vararg components: List<String>) = parse(
            (
                listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") +
                    listOfNotNull(method?.let { "METHOD:$it" }) +
                    components.flatMap { it } + "END:VCALENDAR"
                ).joinToString("") { "$it\r\n" },
        )
        fun order(calendar: VCalendar): List<String> = calendar.component.components.map { child ->
            child.name + " " + child.property("UID")?.value +
                (child.property("RECURRENCE-ID")?.let { " @" + it.value } ?: "")
        }
        val master = arrayOf("DTSTART:20261005T100000Z", "RRULE:FREQ=WEEKLY;COUNT=6", "ATTENDEE:mailto:x@example.com")
        val store = calendar(
            null,
            component("VEVENT", "a", *master),
            component("VEVENT", "b", "DTSTART:20261006T100000Z"),
            component("VEVENT", "a", "RECURRENCE-ID:20261012T100000Z", "DTSTART:20261012T110000Z"),
            component("VTODO", "c"),
        )
        val replied = ITip.apply(
            store,
            calendar(
                "REPLY",
                component(
                    "VEVENT",
                    "a",
                    "RECURRENCE-ID:20261019T100000Z",
                    "ATTENDEE;PARTSTAT=ACCEPTED:mailto:x@example.com",
                ),
            ),
        ).calendar
        assertEquals(
            listOf("VEVENT a", "VEVENT a @20261019T100000Z", "VEVENT b", "VEVENT a @20261012T100000Z", "VTODO c"),
            order(replied),
        )
        val cancelled = ITip.apply(
            replied,
            calendar("CANCEL", component("VEVENT", "a", "RECURRENCE-ID:20261012T100000Z", "SEQUENCE:1")),
        ).calendar
        assertEquals(listOf("VEVENT a", "VEVENT a @20261019T100000Z", "VEVENT b", "VTODO c"), order(cancelled))
        val requested = ITip.apply(
            cancelled,
            calendar(
                "REQUEST",
                component("VEVENT", "b", "RECURRENCE-ID:20261013T100000Z", "DTSTART:20261013T120000Z"),
                component("VEVENT", "a", "SEQUENCE:2", *master),
                component("VEVENT", "a", "SEQUENCE:2", "RECURRENCE-ID:20261026T100000Z", "DTSTART:20261026T120000Z"),
            ),
        ).calendar
        assertEquals(
            listOf("VEVENT a", "VEVENT a @20261026T100000Z", "VEVENT b", "VEVENT b @20261013T100000Z", "VTODO c"),
            order(requested),
        )
        val added = ITip.apply(
            requested,
            calendar("ADD", component("VEVENT", "a", "SEQUENCE:3", "DTSTART:20261031T100000Z")),
        ).calendar
        assertEquals(
            listOf(
                "VEVENT a",
                "VEVENT a @20261031T100000Z",
                "VEVENT a @20261026T100000Z",
                "VEVENT b",
                "VEVENT b @20261013T100000Z",
                "VTODO c",
            ),
            order(added),
        )
    }

    @Test
    fun todosAndPublish() {
        val todo = parse(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n" +
                "BEGIN:VTODO\r\nUID:todo-1\r\nDTSTAMP:20260901T000000Z\r\n" +
                "DTSTART:20261005T100000Z\r\nDUE:20261010T100000Z\r\nSUMMARY:Report\r\nEND:VTODO\r\nEND:VCALENDAR\r\n",
        ).todos.single()
        val request = ITip.request(todo, organizer, listOf(b), clock)
        assertValid(request)
        assertEquals(0, request.todos.single().priority)
        val reply = ITip.reply(request, "mailto:b@example.com", PartStat.InProcess, clock)
        assertValid(reply)
        val store = store("-//a//EN").todo(request.todos.single()).build()
        val applied = ITip.apply(store, reply).calendar
        assertEquals(PartStat.InProcess, applied.todos.single().attendees.single().partStat)
        assertValid(ITip.cancel(request.todos.single(), clock))
        assertValid(ITip.refresh(request.todos.single(), "mailto:b@example.com", clock))

        val feed = parse(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nX-WR-CALNAME:Team\r\nBEGIN:VTIMEZONE\r\nTZID:X\r\n" +
                "BEGIN:STANDARD\r\nDTSTART:19700101T000000\r\nTZOFFSETFROM:+0100\r\nTZOFFSETTO:+0100\r\n" +
                "END:STANDARD\r\nEND:VTIMEZONE\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260901T000000Z\r\n" +
                "DTSTART;TZID=X:20261005T100000\r\nSUMMARY:Talk\r\nORGANIZER:mailto:a@example.com\r\n" +
                "ATTENDEE:mailto:b@example.com\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
        )
        val published = ITip.publish(feed, clock)
        assertValid(published)
        assertEquals("Team", published.component.property("X-WR-CALNAME")?.value)
        assertEquals(1, published.timeZones.size)
        assertEquals(emptyList(), published.events.single().attendees)
        val subscriber = ITip.apply(store("-//s//EN").build(), published).calendar
        assertEquals(listOf("X"), subscriber.timeZones.map { it.tzid })
        assertEquals(ITipOutcome.Ignored, ITip.apply(subscriber, feed).outcome)
        assertTrue(ITipResult(subscriber, ITipOutcome.Applied, Method.Publish).toString().contains("PUBLISH"))
    }

    @Test
    fun iMipHelpers() {
        assertEquals("text/calendar; method=REQUEST; charset=UTF-8", IMip.contentType(Method.Request))
        val delegated = Organizer.builder("mailto:boss@example.com").sentBy("mailto:assistant@example.com").build()
        val request = ITip.request(weekly, delegated, listOf(b), clock)
        assertEquals(SenderCheck.Matches, IMip.checkSender(request, "BOSS@example.com"))
        assertEquals(SenderCheck.SentBy, IMip.checkSender(request, "mailto:assistant@example.com"))
        assertEquals(SenderCheck.Mismatch, IMip.checkSender(request, "mallory@example.com"))
        val reply = ITip.reply(request, "mailto:b@example.com", PartStat.Accepted, clock)
        assertEquals(SenderCheck.Matches, IMip.checkSender(reply, "b@example.com"))
        assertEquals(SenderCheck.Mismatch, IMip.checkSender(reply, "boss@example.com"))
        assertEquals(SenderCheck.Unknown, IMip.checkSender(VCalendar.builder().event(weekly).build(), "a@example.com"))

        val risky = parse(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\nMETHOD:REQUEST\r\nBEGIN:VEVENT\r\nUID:1\r\n" +
                "DTSTAMP:20260901T000000Z\r\nDTSTART:20261005T100000Z\r\nURL:https://evil.example/\r\n" +
                "DESCRIPTION;ALTREP=\"https://evil.example/d\":Hi\r\nX-ALT-DESC;FMTTYPE=text/html:<b>Hi</b>\r\n" +
                "ATTACH;ENCODING=BASE64;VALUE=BINARY:AAAA\r\nATTACH:https://evil.example/a.pdf\r\n" +
                "END:VEVENT\r\nEND:VCALENDAR\r\n",
        )
        assertEquals(
            listOf(
                "VCALENDAR[0]/VEVENT[0]/URL[0] Link",
                "VCALENDAR[0]/VEVENT[0]/DESCRIPTION[0]@ALTREP Link",
                "VCALENDAR[0]/VEVENT[0]/X-ALT-DESC[0] Html",
                "VCALENDAR[0]/VEVENT[0]/ATTACH[0] Attachment",
                "VCALENDAR[0]/VEVENT[0]/ATTACH[1] Link",
            ),
            IMip.untrustedContent(risky).map { "${it.path} ${it.kind}" },
        )
        assertEquals("URL", IMip.untrustedContent(risky).first().property.name)
        assertTrue(IMip.untrustedContent(risky).first().toString().startsWith("UntrustedContent("))
    }
}
