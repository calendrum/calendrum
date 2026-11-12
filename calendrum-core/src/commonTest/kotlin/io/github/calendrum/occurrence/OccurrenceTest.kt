@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.occurrence

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.recur.RecurrenceLimits
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolver
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.ZoneTransition
import io.github.calendrum.tz.instant
import io.github.calendrum.tz.toInstant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class OccurrenceTest {
    private val berlin = listOf(
        "BEGIN:VTIMEZONE", "TZID:Europe/Berlin",
        "BEGIN:DAYLIGHT", "TZOFFSETFROM:+0100", "TZOFFSETTO:+0200", "DTSTART:19810329T020000",
        "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU", "END:DAYLIGHT",
        "BEGIN:STANDARD", "TZOFFSETFROM:+0200", "TZOFFSETTO:+0100", "DTSTART:19961027T030000",
        "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU", "END:STANDARD",
        "END:VTIMEZONE",
    )

    private fun calendar(vararg lines: String): VCalendar = ICal.parse(
        (listOf("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:-//t//EN") + berlin + lines.toList() + "END:VCALENDAR")
            .joinToString("") { "$it\r\n" },
    ).calendars.single()

    private fun utc(text: String): Instant = Instant.parse(text)

    private fun starts(occurrences: Sequence<Occurrence>): List<String> =
        occurrences.map { it.start.toString() }.toList()

    private fun VCalendar.window(
        from: String,
        to: String,
        options: OccurrenceOptions = OccurrenceOptions.Default,
    ): List<Occurrence> = occurrences(utc(from), utc(to), options).toList()

    @Test
    fun localTimeSurvivesDaylightTime() {
        val calendar = calendar(
            "BEGIN:VEVENT",
            "UID:1",
            "DTSTART;TZID=Europe/Berlin:20260323T100000",
            "DTEND;TZID=Europe/Berlin:20260323T113000",
            "RRULE:FREQ=WEEKLY;COUNT=3",
            "END:VEVENT",
        )
        val occurrences = calendar.occurrences(utc("2026-01-01T00:00:00Z"), utc("2027-01-01T00:00:00Z")).toList()
        assertEquals(
            listOf("2026-03-23T09:00:00Z", "2026-03-30T08:00:00Z", "2026-04-06T08:00:00Z"),
            occurrences.map { it.start.toString() },
        )
        assertTrue(occurrences.all { it.end.epochSeconds - it.start.epochSeconds == 5400L })
        assertEquals("2026-03-30T10:00[Europe/Berlin]", occurrences[1].recurrenceId.toString())
        assertFalse(occurrences[0].isAllDay)
    }

    @Test
    fun overridesCancellationsAndExceptions() {
        val sink = ArrayList<Problem>()
        val options = OccurrenceOptions.builder().problemListener(ProblemListener { sink += it }).build()
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:s", "DTSTART;TZID=Europe/Berlin:20260601T100000", "DURATION:PT1H",
            "RRULE:FREQ=DAILY;COUNT=6", "EXDATE;TZID=Europe/Berlin:20260603T100000", "END:VEVENT",
            "BEGIN:VEVENT", "UID:s", "RECURRENCE-ID;TZID=Europe/Berlin:20260602T100000",
            "DTSTART;TZID=Europe/Berlin:20260602T150000", "DURATION:PT2H", "SUMMARY:moved", "END:VEVENT",
            "BEGIN:VEVENT", "UID:s", "RECURRENCE-ID;TZID=Europe/Berlin:20260604T100000",
            "DTSTART;TZID=Europe/Berlin:20260604T100000", "STATUS:CANCELLED", "END:VEVENT",
            "BEGIN:VEVENT", "UID:s", "RECURRENCE-ID;TZID=Europe/Berlin:20260610T100000",
            "DTSTART;TZID=Europe/Berlin:20260610T120000", "SUMMARY:orphan", "END:VEVENT",
        )
        val occurrences = calendar.window("2026-06-01T00:00:00Z", "2026-07-01T00:00:00Z", options)
        assertEquals(
            listOf(
                "2026-06-01T08:00:00Z",
                "2026-06-02T13:00:00Z",
                "2026-06-05T08:00:00Z",
                "2026-06-06T08:00:00Z",
                "2026-06-10T10:00:00Z",
            ),
            occurrences.map { it.start.toString() },
        )
        assertEquals("moved", occurrences[1].component.property("SUMMARY")!!.value)
        assertEquals(7200L, occurrences[1].end.epochSeconds - occurrences[1].start.epochSeconds)
        assertEquals(listOf(ProblemCode.OverrideWithoutInstance), sink.map { it.code })
        val cancelled = OccurrenceOptions.builder().includeCancelled(true).build()
        val all = calendar.occurrences(utc("2026-06-01T00:00:00Z"), utc("2026-07-01T00:00:00Z"), cancelled).toList()
        assertEquals(listOf(false, false, true, false, false, false), all.map { it.isCancelled })
    }

    @Test
    fun thisAndFutureMovesLaterInstances() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:r", "DTSTART;TZID=Europe/Berlin:20260601T100000", "DURATION:PT1H",
            "RRULE:FREQ=DAILY;COUNT=5", "END:VEVENT",
            "BEGIN:VEVENT", "UID:r", "RECURRENCE-ID;RANGE=THISANDFUTURE;TZID=Europe/Berlin:20260603T100000",
            "DTSTART;TZID=Europe/Berlin:20260603T110000", "DURATION:PT30M", "SUMMARY:later", "END:VEVENT",
        )
        val occurrences = calendar.occurrences(utc("2026-06-01T00:00:00Z"), utc("2026-07-01T00:00:00Z")).toList()
        assertEquals(
            listOf(
                "2026-06-01T08:00:00Z",
                "2026-06-02T08:00:00Z",
                "2026-06-03T09:00:00Z",
                "2026-06-04T09:00:00Z",
                "2026-06-05T09:00:00Z",
            ),
            occurrences.map { it.start.toString() },
        )
        assertTrue(occurrences.drop(2).all { it.component.property("SUMMARY")!!.value == "later" })
        assertTrue(occurrences.drop(2).all { it.end.epochSeconds - it.start.epochSeconds == 1800L })
    }

    @Test
    fun allDayEventsNeedAFloatingZone() {
        val calendar = calendar(
            "BEGIN:VEVENT",
            "UID:d",
            "DTSTART;VALUE=DATE:20260325",
            "RRULE:FREQ=DAILY;COUNT=7",
            "END:VEVENT",
        )
        val sink = ArrayList<Problem>()
        val none = OccurrenceOptions.builder().problemListener(ProblemListener { sink += it }).build()
        assertEquals(emptyList(), calendar.window("2026-03-01T00:00:00Z", "2026-04-01T00:00:00Z", none))
        val options = OccurrenceOptions.builder().floatingZone(TimeZone.UTC).build()
        val days = calendar.occurrences(utc("2026-03-27T12:00:00Z"), utc("2026-03-29T00:00:00Z"), options).toList()
        assertEquals(listOf("2026-03-27T00:00:00Z", "2026-03-28T00:00:00Z"), days.map { it.start.toString() })
        assertTrue(days.all { it.isAllDay && it.end.epochSeconds - it.start.epochSeconds == 86_400L })
    }

    @Test
    fun allDayEventsFromDifferentZonesFollowTheViewer() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:google", "DTSTART;VALUE=DATE:20260601", "DTEND;VALUE=DATE:20260602", "END:VEVENT",
            "BEGIN:VEVENT", "UID:outlook", "DTSTART;VALUE=DATE:20260601", "END:VEVENT",
            "BEGIN:VEVENT", "UID:midnight", "DTSTART;TZID=Europe/Berlin:20260601T000000",
            "DTEND;TZID=Europe/Berlin:20260602T000000", "END:VEVENT",
        )
        val fixed = TimeZone.of("+09:00")
        val options = OccurrenceOptions.builder().floatingZone(fixed).build()
        val list = calendar.occurrences(utc("2026-05-30T00:00:00Z"), utc("2026-06-03T00:00:00Z"), options).toList()
        val days = list.filter { it.isAllDay }
        assertEquals(listOf("2026-05-31T15:00:00Z", "2026-05-31T15:00:00Z"), days.map { it.start.toString() })
        assertTrue(days.all { it.end.epochSeconds - it.start.epochSeconds == 86_400L })
        val midnight = list.single { !it.isAllDay }
        assertEquals("2026-05-31T22:00:00Z", midnight.start.toString())
    }

    @Test
    fun nominalDurationsFollowLocalTime() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:n", "DTSTART;TZID=Europe/Berlin:20260328T120000", "DURATION:P1D", "END:VEVENT",
            "BEGIN:VEVENT", "UID:e", "DTSTART;TZID=Europe/Berlin:20260328T120000",
            "DTEND;TZID=Europe/Berlin:20260329T120000",
            "RRULE:FREQ=WEEKLY;COUNT=2", "END:VEVENT",
            "BEGIN:VEVENT", "UID:p", "DTSTART;TZID=Europe/Berlin:20260330T120000", "END:VEVENT",
        )
        val list = calendar.occurrences(utc("2026-03-01T00:00:00Z"), utc("2026-05-01T00:00:00Z")).toList()
        val nominal = list.first { it.component.property("UID")!!.value == "n" }
        assertEquals(23 * 3600L, nominal.end.epochSeconds - nominal.start.epochSeconds)
        val exact = list.filter { it.component.property("UID")!!.value == "e" }
        assertTrue(exact.all { it.end.epochSeconds - it.start.epochSeconds == 23 * 3600L })
        val point = list.first { it.component.property("UID")!!.value == "p" }
        assertEquals(point.start, point.end)
        assertEquals(1, calendar.occurrences(point.start, utc("2026-05-01T00:00:00Z")).count { it == point })
        assertEquals(0, calendar.occurrences(utc("2026-03-01T00:00:00Z"), point.start).count { it == point })
    }

    @Test
    fun todosAndJournals() {
        val calendar = calendar(
            "BEGIN:VTODO", "UID:t1", "DUE:20260601T120000Z", "END:VTODO",
            "BEGIN:VTODO", "UID:t2", "DTSTART:20260601T080000Z", "DUE:20260601T100000Z", "END:VTODO",
            "BEGIN:VTODO", "UID:t3", "SUMMARY:no dates", "END:VTODO",
            "BEGIN:VJOURNAL", "UID:j", "DTSTART;VALUE=DATE:20260601", "END:VJOURNAL",
        )
        val options = OccurrenceOptions.builder().floatingZone(TimeZone.UTC).build()
        val list = calendar.occurrences(utc("2026-06-01T00:00:00Z"), utc("2026-06-02T00:00:00Z"), options).toList()
        assertEquals(listOf("j", "t2", "t1"), list.map { it.component.property("UID")!!.value })
        val late = calendar.window("2026-06-01T12:00:00Z", "2026-06-01T13:00:00Z", options)
        assertEquals(0, late.count { it.component.name == "VTODO" })
        val due = calendar.window("2026-06-01T11:00:00Z", "2026-06-01T12:00:00Z", options)
        assertEquals(1, due.count { it.component.name == "VTODO" })
    }

    @Test
    fun alarmsRepeatWithALimit() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:a", "DTSTART:20260601T100000Z", "DTEND:20260601T110000Z",
            "BEGIN:VALARM", "ACTION:DISPLAY", "TRIGGER:-PT15M", "REPEAT:2", "DURATION:PT5M", "END:VALARM",
            "BEGIN:VALARM", "ACTION:DISPLAY", "TRIGGER;RELATED=END:PT0M", "END:VALARM",
            "BEGIN:VALARM", "ACTION:DISPLAY", "TRIGGER;VALUE=DATE-TIME:20260601T060000Z", "REPEAT:1000000",
            "DURATION:PT1M", "END:VALARM",
            "END:VEVENT",
        )
        val sink = ArrayList<Problem>()
        val options = OccurrenceOptions.builder()
            .maxAlarmRepetitions(3)
            .problemListener(ProblemListener { sink += it })
            .build()
        val alarms = calendar.window("2026-06-01T00:00:00Z", "2026-06-02T00:00:00Z", options).single().alarms
        assertEquals(
            listOf("06:00", "06:01", "06:02", "06:03", "09:45", "09:50", "09:55", "11:00"),
            alarms.map { it.at.toString().substring(11, 16) },
        )
        assertEquals(listOf(0, 1, 2, 3, 0, 1, 2, 0), alarms.map { it.repetition })
        assertEquals(listOf(ProblemCode.AlarmRepeatLimit), sink.map { it.code })
    }

    @Test
    fun theCursorPagesAndFinds() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:w", "DTSTART;TZID=Europe/Berlin:20260601T100000", "DURATION:PT2H",
            "RRULE:FREQ=WEEKLY;BYDAY=MO,WE", "END:VEVENT",
            "BEGIN:VEVENT", "UID:x", "DTSTART;TZID=Europe/Berlin:20260603T100000", "DURATION:PT1H", "END:VEVENT",
        )
        val cursor = calendar.occurrences()
        assertEquals("2026-06-01T08:00:00Z", cursor.next()!!.start.toString())
        assertEquals("2026-06-03T08:00:00Z", cursor.next()!!.start.toString())
        assertEquals("2026-06-03T08:00:00Z", cursor.next()!!.start.toString())
        assertEquals("2026-06-08T08:00:00Z", cursor.next()!!.start.toString())
        assertEquals("2026-12-02T09:00:00Z", cursor.after(utc("2026-11-30T09:00:00Z"))!!.start.toString())
        assertEquals(2, cursor.at(utc("2026-06-03T08:30:00Z")).size)
        assertEquals(1, cursor.at(utc("2026-06-03T09:30:00Z")).size)
        assertEquals(0, cursor.at(utc("2026-06-02T09:30:00Z")).size)
        val first = cursor.page(2, null)
        assertEquals(
            listOf("2026-06-01T08:00:00Z", "2026-06-03T08:00:00Z", "2026-06-03T08:00:00Z"),
            first.map { it.start.toString() },
        )
        val second = cursor.page(2, first.last().start)
        assertEquals(listOf("2026-06-08T08:00:00Z", "2026-06-10T08:00:00Z"), second.map { it.start.toString() })
    }

    @Test
    fun lessonsMinusHolidays() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:l", "DTSTART;TZID=Europe/Berlin:20261005T090000", "DURATION:PT90M",
            "RRULE:FREQ=WEEKLY;COUNT=4", "END:VEVENT",
            "BEGIN:VEVENT", "UID:h", "DTSTART;TZID=Europe/Berlin:20261012T000000", "DURATION:P1D", "END:VEVENT",
        )
        val all = calendar.occurrences(utc("2026-10-01T00:00:00Z"), utc("2026-11-01T00:00:00Z")).toList()
        val lessons = OccurrenceSet.of(all.filter { it.component.property("UID")!!.value == "l" })
        val holidays = OccurrenceSet.of(all.filter { it.component.property("UID")!!.value == "h" })
        assertEquals(3, lessons.minus(holidays).occurrences.size)
        assertEquals(1, lessons.intersect(holidays).occurrences.size)
        assertEquals(5, lessons.union(holidays).occurrences.size)
        assertEquals(lessons, lessons.union(lessons))
    }

    @Test
    fun lastMileHelpers() {
        val calendar = calendar(
            "BEGIN:VEVENT", "UID:m", "DTSTART;TZID=Europe/Berlin:20260601T100000", "DURATION:PT45M", "SEQUENCE:2",
            "LAST-MODIFIED:20260501T000000Z", "STATUS:CANCELLED",
            "ATTENDEE;CUTYPE=ROOM:mailto:room@example.com", "ATTENDEE:mailto:ann@example.com",
            "ATTENDEE;CUTYPE=INDIVIDUAL:mailto:bob@example.com", "END:VEVENT",
            "BEGIN:VEVENT", "UID:d", "DTSTART;VALUE=DATE:20260601", "SEQUENCE:2", "END:VEVENT",
        )
        val zones = TimeZoneResolvers.default(calendar)
        val event = calendar.events[0]
        assertEquals(utc("2026-06-01T08:00:00Z"), event.resolvedStart(zones))
        assertEquals(utc("2026-06-01T08:45:00Z"), event.resolvedEnd(zones))
        assertTrue(event.isCancelled)
        assertFalse(event.isAllDay)
        assertTrue(calendar.events[1].isAllDay)
        assertNull(calendar.events[1].resolvedStart(zones))
        assertEquals(utc("2026-06-02T00:00:00Z"), calendar.events[1].resolvedEnd(zones, TimeZone.UTC))
        assertEquals(
            listOf("mailto:ann@example.com", "mailto:bob@example.com"),
            event.attendees.individuals().map { it.address },
        )
        assertTrue(event.revision() > calendar.events[1].revision())
        assertEquals(Revision(2, utc("2026-05-01T00:00:00Z")), event.revision())
    }

    @Test
    fun outlookZonesFromTheWindowsEpoch() {
        val text = listOf(
            "BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:Microsoft Exchange Server 2010",
            "BEGIN:VTIMEZONE", "TZID:Pacific Standard Time",
            "BEGIN:STANDARD", "DTSTART:16010101T020000", "TZOFFSETFROM:-0700", "TZOFFSETTO:-0800",
            "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=1SU;BYMONTH=11", "END:STANDARD",
            "BEGIN:DAYLIGHT", "DTSTART:16010101T020000", "TZOFFSETFROM:-0800", "TZOFFSETTO:-0700",
            "RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=2SU;BYMONTH=3", "END:DAYLIGHT", "END:VTIMEZONE",
            "BEGIN:VEVENT", "UID:o", "DTSTART;TZID=Pacific Standard Time:20261102T090000",
            "DTEND;TZID=Pacific Standard Time:20261102T100000", "RRULE:FREQ=DAILY;COUNT=2", "END:VEVENT",
            "END:VCALENDAR",
        ).joinToString("") { "$it\r\n" }
        val calendar = ICal.parse(text).calendars.single()
        val starts = starts(calendar.occurrences(utc("2026-10-01T00:00:00Z"), utc("2026-12-01T00:00:00Z")))
        assertEquals(listOf("2026-11-02T17:00:00Z", "2026-11-03T17:00:00Z"), starts)
    }

    @Test
    fun schemaFieldsAsInstants() {
        class Lesson(val uid: String, val start: Instant)
        val calendar = calendar("BEGIN:VEVENT", "UID:1", "DTSTART;TZID=Europe/Berlin:20260601T100000", "END:VEVENT")
        val zones = TimeZoneResolvers.default(calendar)
        val schema = object : IcsSchema<Lesson>("VEVENT") {
            private val uid = field(Properties.UID, Lesson::uid)
            private val start = field(Properties.DTSTART.instant(zones), Lesson::start)

            override fun create(values: Values): Lesson = Lesson(values[uid], values[start])
        }
        val bytes = ICal.writer().encodeToByteArray(calendar.component)
        val lessons = ICal.parser().decode(bytes, schema)
        assertEquals(utc("2026-06-01T08:00:00Z"), lessons.single().start)
    }

    @Test
    fun defaultZonesAreTheFileAndItsAliases() {
        val problems = ArrayList<ProblemCode>()
        val calendar = calendar(
            "BEGIN:VEVENT",
            "UID:a",
            "DTSTART;TZID=W. Europe Standard Time:20260601T100000",
            "END:VEVENT",
            "BEGIN:VEVENT",
            "UID:n",
            "DTSTART;TZID=America/New_York:20260601T100000",
            "END:VEVENT",
        )
        val listener = ProblemListener { problems += it.code }
        val options = OccurrenceOptions.builder().problemListener(listener).build()
        assertEquals(
            listOf("2026-06-01T08:00:00Z"),
            calendar.window("2026-05-01T00:00:00Z", "2026-07-01T00:00:00Z", options).map { it.start.toString() },
        )
        assertEquals(listOf(ProblemCode.TimeZoneAlias, ProblemCode.UnknownTimeZone), problems.distinct())
        val viewer = OccurrenceOptions.builder().floatingZone(TimeZone.UTC).build()
        assertEquals(
            listOf("2026-06-01T08:00:00Z", "2026-06-01T10:00:00Z"),
            calendar.window("2026-05-01T00:00:00Z", "2026-07-01T00:00:00Z", viewer).map { it.start.toString() },
        )
        val zones = TimeZoneResolvers.default(calendar)
        assertEquals(
            "ICalTimeZone(W. Europe Standard Time)",
            zones.resolve("W. Europe Standard Time", ResolveContext.Empty).toString(),
        )
        assertNull(zones.resolve("America/New_York", ResolveContext.Empty))
        assertNull(TimeZoneResolvers.default(null).resolve("Europe/Berlin", ResolveContext.Empty))
    }

    @Test
    fun customZonesAreReadThroughTheirPublicMethods() {
        val plusThree = object : ICalTimeZone {
            override val id: String = "Custom/PlusThree"

            override fun offsetAt(instant: Instant): UtcOffset = UtcOffset(hours = 3)

            override fun toInstant(local: LocalDateTime): Instant = local.toInstant(UtcOffset(hours = 3))

            override fun transitions(from: Instant, to: Instant): List<ZoneTransition> = emptyList()
        }
        val calendar = calendar(
            "BEGIN:VEVENT",
            "UID:c",
            "DTSTART;TZID=Custom/PlusThree:20260601T100000",
            "DURATION:P1D",
            "RRULE:FREQ=DAILY;UNTIL=20260603T070000Z",
            "END:VEVENT",
        )
        val resolver = TimeZoneResolver { tzid, _ -> if (tzid == plusThree.id) plusThree else null }
        val options = OccurrenceOptions.builder().zones(resolver).build()
        val occurrences = calendar.window("2026-05-01T00:00:00Z", "2026-07-01T00:00:00Z", options)
        assertEquals(
            listOf("2026-06-01T07:00:00Z", "2026-06-02T07:00:00Z", "2026-06-03T07:00:00Z"),
            occurrences.map { it.start.toString() },
        )
        assertEquals("2026-06-02T07:00:00Z", occurrences.first().end.toString())
        val start = assertNotNull(calendar.events.first().start)
        assertEquals(utc("2026-06-01T07:00:00Z"), start.toInstant(resolver))
    }

    private fun seriesWith(start: String, extra: String = ""): VCalendar = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//EN\r\n$extra" +
            "BEGIN:VEVENT\r\nUID:a\r\nDTSTAMP:20260101T000000Z\r\n$start\r\nRRULE:FREQ=DAILY\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
    ).calendars.single()

    private val fewDates = RecurrenceLimits.builder().maxOccurrences(3).build()
    private val wideFrom = Instant.parse("2026-01-01T00:00:00Z")
    private val wideTo = Instant.parse("2027-01-01T00:00:00Z")

    private val everyKindOfSeries = mapOf(
        "utc" to seriesWith("DTSTART:20260105T100000Z"),
        "date" to seriesWith("DTSTART;VALUE=DATE:20260105"),
        "floating" to seriesWith("DTSTART:20260105T100000"),
        "unknown zone" to seriesWith("DTSTART;TZID=Nowhere/Nothing:20260105T100000"),
        "zoned" to seriesWith(
            "DTSTART;TZID=Fixed/Zone:20260105T100000",
            "BEGIN:VTIMEZONE\r\nTZID:Fixed/Zone\r\nBEGIN:STANDARD\r\nDTSTART:19700101T000000\r\n" +
                "TZOFFSETFROM:+0300\r\nTZOFFSETTO:+0300\r\nEND:STANDARD\r\nEND:VTIMEZONE\r\n",
        ),
    )

    @Test
    fun theListenerOfTheOptionsHearsTheLimitOfEveryKindOfSeries() {
        for ((name, calendar) in everyKindOfSeries) {
            val heard = ArrayList<Problem>()
            val options = OccurrenceOptions.builder()
                .floatingZone(TimeZone.UTC)
                .recurrence(RecurrenceOptions.builder().limits(fewDates).build())
                .problemListener(ProblemListener { heard += it })
                .build()
            assertEquals(3, calendar.occurrences(wideFrom, wideTo, options).count(), name)
            assertTrue(heard.any { it.code.id == "ICAL-R001" }, "$name: $heard")
        }
    }

    @Test
    fun theListenerOfTheRecurrenceOptionsComesFirst() {
        for ((name, calendar) in everyKindOfSeries) {
            val ofOptions = ArrayList<Problem>()
            val ofRecurrence = ArrayList<Problem>()
            val options = OccurrenceOptions.builder()
                .floatingZone(TimeZone.UTC)
                .recurrence(
                    RecurrenceOptions.builder().limits(fewDates).problemListener(
                        ProblemListener {
                            ofRecurrence += it
                        },
                    ).build(),
                )
                .problemListener(ProblemListener { ofOptions += it })
                .build()
            calendar.occurrences(wideFrom, wideTo, options).count()
            assertTrue(ofRecurrence.any { it.code.id == "ICAL-R001" }, name)
            assertFalse(ofOptions.any { it.code.id == "ICAL-R001" }, name)
        }
    }

    @Test
    fun withoutAnyListenerTheLimitStillHolds() {
        val options = OccurrenceOptions.builder()
            .floatingZone(TimeZone.UTC)
            .recurrence(RecurrenceOptions.builder().limits(fewDates).build())
            .build()
        assertEquals(3, everyKindOfSeries.getValue("utc").occurrences(wideFrom, wideTo, options).count())
    }

    private object ThreeHoursEast : ICalTimeZone {
        override val id: String = "Test/ThreeHoursEast"

        override fun offsetAt(instant: Instant): UtcOffset = UtcOffset(hours = 3)

        override fun toInstant(local: LocalDateTime): Instant = local.toInstant(UtcOffset(hours = 3))

        override fun transitions(from: Instant, to: Instant): List<ZoneTransition> = emptyList()
    }

    @Test
    fun floatingTimesAndDatesAreReadInAZoneOfThisLibrary() {
        val options = OccurrenceOptions.builder().floatingICalZone(ThreeHoursEast).build()
        val floating = seriesWith("DTSTART:20260105T100000").occurrences(wideFrom, wideTo, options).first()
        assertEquals(Instant.parse("2026-01-05T07:00:00Z"), floating.start)
        val date = seriesWith("DTSTART;VALUE=DATE:20260105").occurrences(wideFrom, wideTo, options).first()
        assertEquals(Instant.parse("2026-01-04T21:00:00Z"), date.start)
        assertEquals(ThreeHoursEast, options.floatingICalZone)
        assertNull(options.floatingZone)
        assertTrue("Test/ThreeHoursEast" in options.toString())
    }

    @Test
    fun aZoneFromTheFileWorksAsTheFloatingZoneOnEveryPlatform() {
        val calendar = everyKindOfSeries.getValue("zoned")
        val zone = assertNotNull(TimeZoneResolvers.vtimezone(calendar).resolve("Fixed/Zone", ResolveContext.Empty))
        val options = OccurrenceOptions.builder().floatingICalZone(zone).build()
        val floating = seriesWith("DTSTART:20260105T100000").occurrences(wideFrom, wideTo, options).first()
        assertEquals(Instant.parse("2026-01-05T07:00:00Z"), floating.start)
    }

    @Test
    fun onlyOneFloatingZoneCanBeSet() {
        assertFailsWith<IllegalArgumentException> {
            OccurrenceOptions.builder().floatingZone(TimeZone.UTC).floatingICalZone(ThreeHoursEast).build()
        }
    }
}
