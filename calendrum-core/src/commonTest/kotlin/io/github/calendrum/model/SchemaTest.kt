@file:OptIn(ExperimentalICalApi::class, InternalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ByteInput
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.InternalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class SchemaTest {
    data class Reminder(val action: AlarmAction, val trigger: Trigger, val description: String?)

    data class Lesson(
        val uid: String,
        val start: ICalDateTime,
        val end: ICalDateTime?,
        val summary: String,
        val location: String?,
        val teachers: List<String>,
        val categories: List<Categories>,
        val reminders: List<Reminder>,
    )

    object ReminderSchema : IcsSchema<Reminder>("VALARM") {
        private val action = field(Properties.ACTION, Reminder::action)
        private val trigger = field(Properties.TRIGGER, Reminder::trigger)
        private val description = optionalField(Properties.DESCRIPTION, Reminder::description)

        override fun create(values: Values): Reminder = Reminder(values[action], values[trigger], values[description])
    }

    object LessonSchema : IcsSchema<Lesson>("VEVENT") {
        private val uid = field(Properties.UID, Lesson::uid)
        private val start = field(Properties.DTSTART, Lesson::start)
        private val end = optionalField(Properties.DTEND, Lesson::end)
        private val summary = field(Properties.SUMMARY, Lesson::summary)
        private val location = optionalField(Properties.LOCATION, { it.location?.let(::Location) }, Location::text)
        private val teachers = listField(
            Properties.ATTENDEE,
            Lesson::teachers,
            decode = { it.address },
            encode = { Attendee.builder(it).build() },
        )
        private val categories = listField(Properties.CATEGORIES, Lesson::categories)
        private val reminders = childField(ReminderSchema, Lesson::reminders)

        override fun create(values: Values): Lesson = Lesson(
            values[uid],
            values[start],
            values[end],
            values[summary],
            values[location],
            values[teachers],
            values[categories],
            values[reminders],
        )
    }

    private val schedule = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//t//EN\r\n" +
        "BEGIN:VEVENT\r\nUID:1\r\nDTSTAMP:20260901T080000Z\r\nDTSTART:20260901T090000Z\r\nDTEND:20260901T103000Z\r\n" +
        "SUMMARY:Algebra\r\nLOCATION:Room 1\r\nATTENDEE;CN=Ann:mailto:ann@example.com\r\n" +
        "ATTENDEE:mailto:bob@example.com\r\nCATEGORIES:math,first\r\nDESCRIPTION:Bring a pencil\r\n" +
        "BEGIN:VALARM\r\nACTION:DISPLAY\r\nTRIGGER:-PT10M\r\nDESCRIPTION:Soon\r\nEND:VALARM\r\nEND:VEVENT\r\n" +
        "BEGIN:VTODO\r\nUID:todo\r\nSUMMARY:Homework\r\nEND:VTODO\r\n" +
        "BEGIN:VEVENT\r\nUID:2\r\nDTSTART;VALUE=DATE:20260902\r\nSUMMARY:Holiday\r\nEND:VEVENT\r\n" +
        "END:VCALENDAR\r\n"

    private fun viaWrappers(text: String): List<Lesson> = ICal.parse(text).calendars.single().events.map { event ->
        Lesson(
            event.uid!!,
            event.start!!,
            event.end,
            event.summary!!,
            event.location,
            event.attendees.map { it.address },
            event.component.all(Properties.CATEGORIES),
            event.alarms.map { Reminder(it.action!!, it.trigger!!, it.description) },
        )
    }

    @Test
    fun aSchemaReadsWhatTheWrappersRead() {
        val lessons = ICal.parser().decode(schedule.encodeToByteArray(), LessonSchema)
        assertEquals(viaWrappers(schedule), lessons)
        assertEquals(2, lessons.size)
        assertEquals(listOf("mailto:ann@example.com", "mailto:bob@example.com"), lessons[0].teachers)
        val reminder = Reminder(AlarmAction.Display, Trigger.Relative(ICalDuration.ofMinutes(-10)), "Soon")
        assertEquals(reminder, lessons[0].reminders.single())
        assertEquals(emptyList(), lessons[1].reminders)
        assertNull(lessons[1].end)
        assertEquals(0, schemaPropertyNodes(ICal.parser(), schedule.encodeToByteArray(), LessonSchema))
    }

    @Test
    fun aSchemaWritesWhatItReads() {
        val lessons = ICal.parser().decode(schedule.encodeToByteArray(), LessonSchema)
        val defaults = BuildDefaults(FixedClock, UidGenerator { "generated" })
        val feed = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Uni//Schedule//EN" }, defaults)
        val text = feed.decodeToString()
        val head = "BEGIN:VCALENDAR\r\nPRODID:-//Uni//Schedule//EN\r\nVERSION:2.0\r\nBEGIN:VEVENT\r\nUID:1\r\n"
        assertTrue(text.startsWith(head))
        assertTrue("DTSTAMP:20260901T080000Z" in text)
        assertEquals(lessons, ICal.parser().decode(feed, LessonSchema))
        val calendar = ICal.parse(feed).calendars.single()
        assertEquals(2, calendar.events.size)
        assertEquals("Soon", calendar.events[0].alarms.single().description)
        assertFailsWith<IllegalStateException> { ICal.writer().encode(lessons, LessonSchema, { }) }
        val withoutDefaults = { ICal.writer().encode(lessons, LessonSchema, { prodId = "-//t//t//EN" }) }
        assertFailsWith<IllegalStateException> { withoutDefaults() }
    }

    @Test
    fun aComponentWithoutARequiredFieldIsSkipped() {
        val text = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nSUMMARY:No start\r\nEND:VEVENT\r\n" +
            "BEGIN:VEVENT\r\nUID:2\r\nDTSTART:2026-13-01\r\nSUMMARY:Bad start\r\nEND:VEVENT\r\n" +
            "BEGIN:VEVENT\r\nUID:3\r\nDTSTART:20260901T090000Z\r\nDTEND:soon\r\nSUMMARY:Bad end\r\n" +
            "ATTENDEE:mailto:a@example.com\r\nATTENDEE;VALUE=TEXT:x\r\n" +
            "BEGIN:VALARM\r\nTRIGGER:-PT5M\r\nEND:VALARM\r\n" +
            "END:VEVENT\r\nEND:VCALENDAR\r\n"
        val result = ICal.parser().decodeWithProblems(text.encodeToByteArray(), LessonSchema)
        assertEquals(2, result.skipped)
        assertEquals(listOf("3"), result.items.map { it.uid })
        val lesson = result.items.single()
        assertNull(lesson.end)
        assertEquals(listOf("mailto:a@example.com", "x"), lesson.teachers)
        assertEquals(emptyList(), lesson.reminders)
        val missing = result.problems.filter { it.code == ProblemCode.MissingRequiredField }
        assertEquals(listOf(2, 6, 18), missing.map { it.span.line })
        assertEquals(listOf("DTSTART", "DTSTART", "ACTION"), missing.map { it.detail })
        assertTrue(result.problems.any { it.code == ProblemCode.ValueTypeNotAllowed })
        assertEquals("ICAL-M001", ProblemCode.MissingRequiredField.id)
        assertEquals("SchemaResult(items=1, problems=${result.problems.size}, skipped=2)", result.toString())
        assertFailsWith<ICalParseException> {
            ICal.parser { mode = ParseMode.Strict }.decode(text.encodeToByteArray(), LessonSchema)
        }
    }

    @Test
    fun aMalformedListItemIsSkipped() {
        val schema = object : IcsSchema<List<RequestStatus>>("VEVENT") {
            private val statuses = listField(Properties.REQUEST_STATUS) { it }

            override fun create(values: Values): List<RequestStatus> = values[statuses]
        }
        val text = "BEGIN:VEVENT\r\nREQUEST-STATUS:2.0;Success\r\nREQUEST-STATUS:fine\r\n" +
            "REQUEST-STATUS:2.1;Fallback\r\nEND:VEVENT\r\n"
        val result = ICal.parser().decodeWithProblems(text.encodeToByteArray(), schema)
        assertEquals(2, result.items.single().size)
        assertEquals(listOf(ProblemCode.InvalidValue), result.problems.map { it.code })
        assertEquals(0, result.skipped)
    }

    @Test
    fun theSchemaIsTheSelection() {
        val text = "BEGIN:VEVENT\r\nUID:1\r\nDTSTART:20260901T090000Z\r\nSUMMARY:S\r\nPRIORITY:high\r\nEND:VEVENT\r\n"
        val strict = ICal.parser { mode = ParseMode.Strict }
        assertFailsWith<ICalParseException> { strict.parse(text) }
        assertEquals(listOf("1"), strict.decode(text.encodeToByteArray(), LessonSchema).map { it.uid })
        val lessons = ICal.parser().decode(SingleByteInput(text.encodeToByteArray()), LessonSchema).toList()
        assertEquals(listOf("S"), lessons.map { it.summary })
    }

    @Test
    fun aUserCodecFallsBackToANode() {
        val counter = PropertyKey(
            "X-COUNT",
            object : PropertyCodec<Int> {
                override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> =
                    Decoded.Value(property.value.length, emptyList())

                override fun encode(value: Int, context: EncodeContext): ICalProperty =
                    ICalProperty.of(context.name, "x".repeat(value))
            },
        )
        val schema = object : IcsSchema<Pair<String, Int?>>("VEVENT") {
            private val uid = field(Properties.UID, Pair<String, Int?>::first)
            private val count = optionalField(counter, Pair<String, Int?>::second)

            override fun create(values: Values): Pair<String, Int?> = values[uid] to values[count]
        }
        val bytes = "BEGIN:VEVENT\r\nUID:1\r\nX-COUNT:xxx\r\nEND:VEVENT\r\n".encodeToByteArray()
        assertEquals(listOf("1" to 3), ICal.parser().decode(bytes, schema))
        assertEquals(1, schemaPropertyNodes(ICal.parser(), bytes, schema))
        val other = object : IcsSchema<String>("VTODO") {
            override fun create(values: Values): String = "x"
        }
        val values = IcsSchema.Values(other, arrayOfNulls(0))
        assertFailsWith<IllegalArgumentException> { values[LessonSchema.fields[0]] }
        assertEquals("IcsSchema(component=VEVENT, fields=8)", LessonSchema.toString())
        assertEquals("IcsSchema.Field(name=VALARM)", LessonSchema.fields[7].toString())
    }

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.parse("2026-09-01T08:00:00Z")
    }

    private class SingleByteInput(private val bytes: ByteArray) : ByteInput {
        private var at = 0

        override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
            if (at == bytes.size) return -1
            buffer[offset] = bytes[at++]
            return 1
        }
    }
}
