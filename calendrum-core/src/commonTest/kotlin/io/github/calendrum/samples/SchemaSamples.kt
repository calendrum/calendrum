@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.samples

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.EncodeContext
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.IcsSchema
import io.github.calendrum.model.Location
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyCodec
import io.github.calendrum.model.PropertyDefinition
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.Rfc5545
import io.github.calendrum.model.UidGenerator
import io.github.calendrum.model.ValueType
import io.github.calendrum.model.calendars
import io.github.calendrum.model.get
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

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

private const val LESSONS = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\n" +
    "BEGIN:VEVENT\r\nUID:1@example.com\r\nDTSTART:20260901T090000Z\r\nSUMMARY:Algebra\r\nLOCATION:Room 1\r\n" +
    "ATTENDEE:mailto:ann@example.com\r\nEND:VEVENT\r\n" +
    "BEGIN:VEVENT\r\nDTSTART:20260902T090000Z\r\nSUMMARY:No UID\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

fun decodeIntoClasses() {
    val bytes = LESSONS.encodeToByteArray()

    val result = ICal.parser().decodeWithProblems(bytes, LessonSchema)

    assertEquals(listOf("Algebra"), result.items.map { it.summary })
    assertEquals(1, result.skipped)
    assertEquals(ProblemCode.MissingRequiredField, result.problems.single().code)
}

fun encodeFromClasses() {
    val lessons = ICal.parser().decode(LESSONS.encodeToByteArray(), LessonSchema)
    val defaults = BuildDefaults(
        object : Clock {
            override fun now(): Instant = Instant.parse("2026-09-01T00:00:00Z")
        },
        UidGenerator { "generated@example.com" },
    )

    val feed = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Example//Schedule//EN" }, defaults)

    assertEquals(lessons, ICal.parser().decode(feed, LessonSchema))
}

private object HexColor : PropertyCodec<Int> {
    override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> =
        Decoded.Value(property.value.removePrefix("#").toInt(16), emptyList())

    override fun encode(value: Int, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, "#" + value.toString(16))
}

fun ownPropertyAndRegistry() {
    val color = PropertyKey("X-ACME-COLOR", HexColor)
    val event = ICal.parse(
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//Example//Sample//EN\r\nBEGIN:VEVENT\r\nUID:1@example.com\r\n" +
            "DTSTAMP:20260901T000000Z\r\nX-ACME-COLOR:#ff8800\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
    ).calendars.single().events.single()

    assertEquals(0xFF8800, event.component[color])

    val registry = ICalRegistry.builder()
        .install(Rfc5545)
        .install { it.property(PropertyDefinition(color, ValueType.Text, emptySet(), emptySet(), emptyMap(), "Acme")) }
        .build()
    assertEquals("Acme", registry.property("X-ACME-COLOR")?.source)
}
