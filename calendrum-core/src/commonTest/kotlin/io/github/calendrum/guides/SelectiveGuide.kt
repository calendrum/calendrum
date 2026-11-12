@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.asByteInput
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Location
import io.github.calendrum.model.Properties
import io.github.calendrum.model.calendars
import io.github.calendrum.parser
import io.github.calendrum.syntax.Feature
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection
import io.github.calendrum.syntax.WriteOptions
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

// region schema-define
data class Lesson(
    val uid: String,
    val start: ICalDateTime,
    val summary: String,
    val room: String?,
    val teachers: List<String>,
)

object LessonSchema : IcsSchema<Lesson>("VEVENT") {
    private val uid = field(Properties.UID, Lesson::uid)
    private val start = field(Properties.DTSTART, Lesson::start)
    private val summary = field(Properties.SUMMARY, Lesson::summary)
    private val room = optionalField(Properties.LOCATION, { it.room?.let(::Location) }, Location::text)
    private val teachers = listField(
        Properties.ATTENDEE,
        Lesson::teachers,
        decode = { it.address },
        encode = { Attendee.builder(it).build() },
    )

    override fun create(values: Values) =
        Lesson(values[uid], values[start], values[summary], values[room], values[teachers])
}
// endregion

class SelectiveGuide {
    private val text = calendarText
    private val bytes = calendarBytes

    @Test
    fun selections() {
        // region selective-select
        val lessons = ICal.parser {
            select {
                components("VEVENT") // VTODO, VALARM… are skipped right in the lexer
                properties(Properties.UID, Properties.SUMMARY, Properties.LOCATION)
                properties("X-ACME-ROOM") // unknown names are matched exactly
                // DTSTART, DTEND, DURATION, RRULE, RDATE, EXDATE, RECURRENCE-ID
                need(Feature.Occurrences)
                timeZones = TimeZoneSelection.Referenced // only the VTIMEZONE components the events refer to
            }
        }.parse(bytes)
        // presets: Schedule, SyncMetadata, FreeBusy
        val changes = ICal.parser { select(Selection.SyncMetadata) }.parse(bytes)
        // endregion
        val event = lessons.calendars.single().events.first().component
        val names = event.properties.map { it.name }
        assertEquals(listOf("UID", "DTSTART", "DTEND", "SUMMARY", "LOCATION", "RRULE"), names)
        assertEquals(2, changes.calendars.single().events.size)
        assertTrue(lessons.components.single().components("VTIMEZONE").isNotEmpty())
        assertEquals(null, event.property("ATTENDEE"))
    }

    @Test
    fun partialResults() {
        val lessons = ICal.parser { select(Selection.Schedule) }.parse(bytes)
        // region selective-partial
        val partial = lessons.isPartial // true: this is not the whole file
        val failure = assertFailsWith<IllegalStateException> { ICal.writer().encodeToString(lessons.components) }
        val preview = ICal.writer(WriteOptions.builder().allowPartialWrite(true).build())
            .encodeToString(lessons.components) // only when you really mean it
        // endregion
        assertTrue(partial)
        assertTrue(failure.message.orEmpty().isNotEmpty())
        assertTrue("BEGIN:VEVENT" in preview)
    }

    @Test
    fun decodeIntoClasses() {
        // region schema-decode
        val lessons: List<Lesson> = ICal.parser().decode(bytes, LessonSchema) // no tree is built
        val result = ICal.parser().decodeWithProblems(bytes, LessonSchema) // items, problems, skipped
        val streamed: Sequence<Lesson> = ICal.parser().decode(bytes.asByteInput(), LessonSchema) // bounded memory
        // endregion
        assertEquals(2, lessons.size)
        assertEquals("Weekly sync", lessons.first().summary)
        assertEquals("Room 101", lessons.first().room)
        assertEquals(listOf("mailto:jane@example.com", "mailto:bob@example.com"), lessons.first().teachers)
        assertEquals(0, result.skipped)
        assertEquals(lessons, streamed.toList())
        val broken = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        val skipped = ICal.parser().decodeWithProblems(broken, LessonSchema)
        assertEquals(1, skipped.skipped)
        assertEquals(listOf("ICAL-M001"), skipped.problems.map { it.code.id })
    }

    @Test
    fun encodeFromClasses() {
        val lessons = ICal.parser().decode(bytes, LessonSchema)
        // region schema-encode
        val defaults = BuildDefaults(fixedClock) { newUid() } // for items that have no UID of their own
        val feed: ByteArray = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Uni//Schedule//EN" }, defaults)
        // endregion
        assertEquals(lessons, ICal.parser().decode(feed, LessonSchema))
    }
}
