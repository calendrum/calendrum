@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum

import io.github.calendrum.model.Attendee
import io.github.calendrum.model.BuildDefaults
import io.github.calendrum.model.BusyStatus
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
import io.github.calendrum.model.Rfc7986
import io.github.calendrum.model.UidGenerator
import io.github.calendrum.model.ValueType
import io.github.calendrum.model.VendorProperties
import io.github.calendrum.model.calendars
import io.github.calendrum.model.decodeAny
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class ReadmeExtensionExamplesTest {
    private val bytes = (
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//t//t//EN\r\nNAME:Lessons\r\nCOLOR:teal\r\n" +
            "REFRESH-INTERVAL;VALUE=DURATION:P1D\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTART:20260901T090000Z\r\n" +
            "SUMMARY:Algebra\r\nLOCATION:Room 1\r\nATTENDEE:mailto:ann@example.com\r\nX-ACME-COLOR:blue\r\n" +
            "X-MICROSOFT-CDO-BUSYSTATUS:OOF\r\n" +
            "CONFERENCE;VALUE=URI;FEATURE=VIDEO;LABEL=Call:https://meet.example/1\r\n" +
            "BEGIN:VLOCATION\r\nUID:l\r\nNAME:Hall\r\nEND:VLOCATION\r\n" +
            "BEGIN:VALARM\r\nUID:a\r\nACTION:DISPLAY\r\nTRIGGER:-PT5M\r\n" +
            "ACKNOWLEDGED:20260901T085500Z\r\nEND:VALARM\r\n" +
            "END:VEVENT\r\nEND:VCALENDAR\r\n"
        ).encodeToByteArray()

    private val printed = ArrayList<String>()

    private fun println(message: Any?) {
        printed += message.toString()
    }

    private object HexColor : PropertyCodec<Int> {
        override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> {
            val value = property.value.removePrefix("#").toIntOrNull(16)
                ?: return Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, 0))
            return Decoded.Value(value, emptyList())
        }

        override fun encode(value: Int, context: EncodeContext): ICalProperty =
            ICalProperty.of(context.name, "#" + value.toString(16))
    }

    private val color = PropertyKey("X-ACME-COLOR", HexColor)

    private val defaults = BuildDefaults(
        object : Clock {
            override fun now(): Instant = Instant.parse("2026-09-01T08:00:00Z")
        },
        UidGenerator { "uid" },
    )

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

    @Test
    fun theExtensionSnippetOfTheReadmeCompilesAndWorks() {
        val calendar = ICal.parse(bytes).calendars.single()
        println("${calendar.names} ${calendar.color} ${calendar.refreshInterval}")
        for (event in calendar.events) {
            event.conferences.forEach { println("${it.label}: ${it.uri} ${it.features}") }
            event.locations.forEach { println("${it.name} ${it.structuredData.firstOrNull()?.text}") }
            event.alarms.filter { it.acknowledged != null }.forEach { println("dismissed ${it.uid}") }
        }
        val registry = ICalRegistry.builder()
            .install(Rfc5545).install(Rfc7986).install(VendorProperties)
            .install {
                it.property(PropertyDefinition(color, ValueType.Text, emptySet(), emptySet(), emptyMap(), "Acme"))
            }
            .build()
        val checked = ICal.parser {
            this.registry = registry
            decodeEagerly = true
        }.parse(bytes)
        val busy = checked.components.single().components.first().decodeAny("X-MICROSOFT-CDO-BUSYSTATUS", registry)
        assertEquals(
            listOf("[Lessons] teal P1D", "Call: https://meet.example/1 [VIDEO]", "Hall null", "dismissed a"),
            printed,
        )
        assertEquals(listOf("ICAL-V001"), checked.problems.map { it.code.id })
        assertEquals(BusyStatus.Oof, (busy as Decoded.Value).value)
    }

    @Test
    fun theSchemaSnippetOfTheReadmeCompilesAndWorks() {
        val lessons: List<Lesson> = ICal.parser().decode(bytes, LessonSchema)
        val result = ICal.parser().decodeWithProblems(bytes, LessonSchema)
        val feed: ByteArray = ICal.writer().encode(lessons, LessonSchema, { prodId = "-//Uni//Schedule//EN" }, defaults)
        val expected = Lesson("1", lessons[0].start, "Algebra", "Room 1", listOf("mailto:ann@example.com"))
        assertEquals(listOf(expected), lessons)
        assertEquals(0, result.skipped)
        assertEquals(lessons, ICal.parser().decode(feed, LessonSchema))
    }
}
