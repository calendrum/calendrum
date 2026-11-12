@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum

import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.EncodeContext
import io.github.calendrum.model.ICalPatch
import io.github.calendrum.model.Parameters
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyCodec
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.Trigger
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.calendars
import io.github.calendrum.model.decode
import io.github.calendrum.model.get
import io.github.calendrum.syntax.Feature
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Clock

class ReadmeModelExamplesTest {
    private val bytes = listOf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Readme//EN",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "DTSTART;TZID=Europe/Moscow:20261001T100000",
        "ATTENDEE;CN=Jane;PARTSTAT=NEEDS-ACTION:mailto:jane@example.com",
        "ATTENDEE;CN=Bob;PARTSTAT=ACCEPTED:mailto:bob@example.com",
        "X-ACME-COLOR:#ff8800",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:VEVENT",
        "UID:43@example.com",
        "DTSTART:20261002",
        "END:VEVENT",
        "END:VCALENDAR",
    ).joinToString("") { "$it\r\n" }.encodeToByteArray()

    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    private object HexColor : PropertyCodec<Int> {
        override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> =
            Decoded.Value(property.value.substring(1).toInt(16), emptyList())

        override fun encode(value: Int, context: EncodeContext): ICalProperty =
            ICalProperty.of(context.name, "#" + value.toString(16))
    }

    @Test
    fun theModelSnippetOfTheReadmeCompilesAndWorks() {
        val calendar = ICal.parse(bytes).calendars.single()
        for (event in calendar.events) {
            when (val start = event.start ?: continue) {
                is ICalDateTime.Date -> println("all day ${start.date}")
                is ICalDateTime.Utc -> println("at ${start.instant}")
                is ICalDateTime.Zoned -> println("at ${start.dateTime} ${start.tzid}")
                is ICalDateTime.Floating -> println("at ${start.dateTime} local time")
            }
            event.attendees.filter { it.partStat == PartStat.NeedsAction }.forEach { println(it.address) }
            for (alarm in event.alarms) println("${alarm.action} ${alarm.trigger}")
        }
        val event = calendar.events.first()
        when (val decoded = event.component.decode(Properties.DTSTART)) {
            is Decoded.Value -> decoded.problems.forEach(::println)
            is Decoded.Malformed -> println(decoded.problem)
            Decoded.Missing -> println("no DTSTART")
        }
        val cn: String? = event.component.properties("ATTENDEE").first()[Parameters.CN]
        val color = PropertyKey("X-ACME-COLOR", HexColor)
        val eager = ICal.parser { decodeEagerly = true }.parse(bytes)
        eager.problems.filter { it.code.id.startsWith("ICAL-V") }.forEach(::println)
        assertEquals(
            listOf(
                "at 2026-10-01T10:00 Europe/Moscow",
                "mailto:jane@example.com",
                "DISPLAY Trigger.Relative(-PT15M, START)",
                "all day 2026-10-02",
                "ICAL-V008 Warning: DATE-TIME value without a time, read as DATE at 17:17 (offset 403)",
            ),
            printed,
        )
        assertEquals("Jane", cn)
        assertEquals(0xFF8800, event.component[color])
    }

    private var uids = 0

    private fun newUid(): String = "uid-${++uids}@example.com"

    private val uploads = ArrayList<ByteArray>()

    private fun upload(bytes: ByteArray) {
        uploads += bytes
    }

    @Test
    fun theBuildingSnippetOfTheReadmeCompilesAndWorks() {
        val defaults = BuildDefaults(Clock.System) { newUid() }
        val calendar = VCalendar(prodId = "-//Example//Planner 1.0//EN", defaults = defaults) {
            event {
                summary = "Weekly sync"
                start = ICalDateTime.Zoned(LocalDateTime(2026, 10, 1, 10, 0), "Europe/Berlin")
                duration = ICalDuration.ofHours(1)
                rrule(Frequency.WEEKLY) {
                    byDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
                    count(10)
                }
                attendee("mailto:alice@example.com") {
                    cn = "Alice"
                    rsvp = true
                }
                alarm { display(trigger = Trigger.Relative(ICalDuration.ofMinutes(-15)), description = "Soon") }
                property("X-ACME-TAG", "blue")
            }
        }
        val ics = ICal.writer().encodeToString(calendar.component)
        val event = calendar.events.single()
        val accepted = calendar.edit {
            events {
                replace(
                    event,
                    event.edit {
                        sequence = (sequence ?: 0) + 1
                        attendees { replace("mailto:alice@example.com") { partStat = PartStat.Accepted } }
                        remove("X-ACME-TAG")
                    },
                )
            }
        }
        assertEquals("uid-1@example.com", event.uid)
        assertEquals(true, ics.contains("RRULE:FREQ=WEEKLY;COUNT=10;BYDAY=MO,WE\r\n"))
        assertEquals(PartStat.Accepted, accepted.events.single().attendees.single().partStat)
        assertEquals(1, accepted.events.single().sequence)
        val original = ICal.writer().encodeToByteArray(accepted.component)
        val result = ICalPatch(original) {
            component(uid = "uid-1@example.com", recurrenceId = null) {
                attendee("mailto:alice@example.com") { set(Parameters.PARTSTAT, PartStat.Declined) }
                set(Properties.DTSTAMP, ICalDateTime.Utc(Clock.System.now()))
            }
        }.apply()
        upload(result.bytes)
        result.changes.forEach { println("${it.kind} ${it.property} at line ${it.line}") }
        assertEquals(emptyList(), result.unmatched)
        assertEquals(listOf("REPLACED ATTENDEE at line 9", "REPLACED DTSTAMP at line 11"), printed.sorted())
        assertEquals(1, uploads.size)
    }

    @Test
    fun theSelectionSnippetOfTheReadmeCompilesAndWorks() {
        val lessons = ICal.parser {
            select {
                components("VEVENT")
                properties(Properties.UID, Properties.SUMMARY, Properties.LOCATION)
                properties("X-ACME-ROOM")
                need(Feature.Occurrences)
                timeZones = TimeZoneSelection.Referenced
            }
        }.parse(bytes)
        val changes = ICal.parser { select(Selection.SyncMetadata) }.parse(bytes)
        check(lessons.isPartial)
        val preview = ICal.writer(
            WriteOptions.builder().allowPartialWrite(true).build(),
        ).encodeToString(lessons.components)
        val upload = ICal.parser(ParseOptions.Untrusted).parse(bytes)
        assertEquals(
            listOf("UID", "DTSTART"),
            lessons.calendars.single().events.first().component.properties.map {
                it.name
            },
        )
        assertEquals(2, changes.calendars.single().events.size)
        assertEquals(false, preview.contains("ATTENDEE"))
        assertEquals(false, upload.isPartial)
        assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(lessons.components) }
    }
}
