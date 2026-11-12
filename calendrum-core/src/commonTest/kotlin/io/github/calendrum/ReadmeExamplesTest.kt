@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum

import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.Fingerprints
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ReadmeExamplesTest {
    private val sameEventText = listOf(
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "ATTENDEE;CN=Jane:mailto:jane@example.com",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "END:VALARM",
        "END:VEVENT",
    ).joinToString("") { "$it\r\n" }

    private val text = listOf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Readme//EN",
        "X-WR-TIMEZONE:Europe/Moscow",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "SUMMARY:Weekly sync",
        "ATTENDEE;CN=Jane:mailto:jane@example.com",
        "DTSTART;TZID=Europe/Moscow:20261001T100000",
        "X-VENDOR-ROOM:Room 101",
        "END:VEVENT",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Moscow",
        "BEGIN:STANDARD",
        "DTSTART:19700101T000000",
        "TZOFFSETFROM:+0300",
        "TZOFFSETTO:+0300",
        "END:STANDARD",
        "END:VTIMEZONE",
        "END:VCALENDAR",
    ).joinToString("") { "$it\r\n" }

    private val bytes = text.encodeToByteArray()

    private class ByteArrayOutput : ByteOutput {
        private var data = ByteArray(0)

        override fun write(buffer: ByteArray, offset: Int, length: Int) {
            data += buffer.copyOfRange(offset, offset + length)
        }

        fun toByteArray(): ByteArray = data
    }

    private class FloatingTimeFix : LineInterceptor {
        override fun intercept(line: InterceptedLine) {
            if (line.name == "DTSTART" && line.parameterCount == 0) line.setParameter("TZID", "Europe/Moscow")
        }
    }

    @Test
    fun lenientParsingReportsProblemsAndNeverThrows() {
        val printed = mutableListOf<String>()
        val result = ICal.parse(bytes)
        for (calendar in result.components) {
            for (event in calendar.components("VEVENT")) {
                val attendee = event.property("ATTENDEE")
                printed += "${event.property("SUMMARY")?.value} with ${attendee?.parameter("CN")?.value}"
            }
        }
        assertEquals(listOf("Weekly sync with Jane"), printed)
        assertTrue(result.problems.isEmpty())
        assertEquals(0, result.problemCount(ProblemCode.BareLf))
        assertEquals("UTF-8", result.charset)

        val broken = ICal.parse("BEGIN:VCALENDAR\nVERSION:2.0\nEND:VCALENDAR\n")
        broken.problems.forEach { printed += "${it.code.id} at ${it.span.line}:${it.span.column}: ${it.message}" }
        assertTrue(broken.problemCount(ProblemCode.BareLf) > 0)
        assertTrue(broken.problems.any { it.code == ProblemCode.BareLf })
    }

    @Test
    fun strictModeThrowsOnTheFirstError() {
        val reported = mutableListOf<Problem>()
        val strict = ICal.parser {
            mode = ParseMode.Strict
            charset = "text/calendar; charset=windows-1251"
            problemListener = ProblemListener { reported += it }
        }
        strict.parse(bytes)
        val broken = "BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        val failure = assertFailsWith<ICalParseException> { strict.parse(broken) }
        assertEquals(Severity.Error, failure.problems.last().severity)
        assertTrue(reported.isNotEmpty())
    }

    @Test
    fun treesAreBuiltByHandAndComparedByContent() {
        val event = ICalComponent("VEVENT") {
            property("UID", "42@example.com")
            property(ICalProperty.builder("ATTENDEE").parameter("CN", "Jane").value("mailto:jane@example.com").build())
            component("VALARM") { property("ACTION", "DISPLAY") }
        }
        check(event == ICal.parse(sameEventText).components.single())
    }

    @Test
    fun writingIsByteStable() {
        val result = ICal.parse(bytes)
        val same: ByteArray = ICal.writer().encodeToByteArray(result.components)
        assertTrue(bytes.contentEquals(same))
        val event = ICalComponent("VEVENT") { property("UID", "42@example.com") }
        val ics: String = ICal.writer().encodeToString(event)
        assertEquals("BEGIN:VEVENT\r\nUID:42@example.com\r\nEND:VEVENT\r\n", ics)
        val normalized = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build())
            .encodeToString(result.components)
        assertTrue(normalized.indexOf("BEGIN:VTIMEZONE") < normalized.indexOf("BEGIN:VEVENT"))
        assertEquals(text.length, normalized.length)
    }

    @Test
    fun theCursorStreamsEventsWithoutBuildingATree() {
        val events = mutableListOf<ICalComponent>()
        val indexed = mutableListOf<Pair<String, Long>>()
        fun index(value: CharSequence, offset: Long) {
            indexed += value.toString() to offset
        }
        val reader = ICal.parser().reader(bytes)
        while (true) {
            when (reader.next() ?: break) {
                ContentEventType.Begin -> if (reader.name == "VEVENT") events += reader.readComponent()
                ContentEventType.Property -> if (reader.name == "X-WR-TIMEZONE") index(reader.value, reader.offset)
                ContentEventType.End -> Unit
            }
        }
        assertEquals(1, events.size)
        assertEquals(listOf("Europe/Moscow"), indexed.map { it.first })
        assertEquals(ICal.parse(bytes).components.single().components("VEVENT"), events)
    }

    @Test
    fun aLargeFeedIsReadComponentByComponent() {
        val seen = mutableListOf<String>()
        fun index(component: ICalComponent, calendarProperties: List<ICalProperty>) {
            seen += "${component.name}:${calendarProperties.count { it.name == "X-WR-TIMEZONE" }}"
        }
        val events = ICal.parser { limits = ParseLimits.builder().maxTime(2.seconds).build() }
            .components(bytes.asByteInput())
        for (component in events) {
            if (component.name == "VEVENT") index(component, events.calendarProperties)
        }
        assertTrue(events.problems.isEmpty())
        assertEquals(listOf("VEVENT:1"), seen)
        val out = ByteArrayOutput()
        ICal.writer().write(ICal.parse(bytes).components, out)
        assertTrue(bytes.contentEquals(out.toByteArray()))
    }

    @Test
    fun interceptorsFixAFeedWhileItIsRead() {
        val fixer = ICal.parser {
            intercept { line -> if (line.name == "X-VENDOR-ROOM") line.rename("LOCATION") }
            intercept { line -> if (line.name == "DESCRIPTION" && line.value.length > 10_000) line.drop() }
            intercept { FloatingTimeFix() }
        }
        val event = fixer.parse(bytes).components.single().components("VEVENT").single()
        assertEquals("Room 101", event.property("LOCATION")?.value)
        assertEquals(null, event.property("X-VENDOR-ROOM"))
        assertEquals("Europe/Moscow", event.property("DTSTART")?.parameter("TZID")?.value)
        val publisher = ICal.writer(
            WriteOptions.builder().intercept { line -> if (line.name.startsWith("X-")) line.drop() }.build(),
        )
        val written = publisher.encodeToString(fixer.parse(bytes).components)
        assertTrue("X-VENDOR-ROOM" !in written)
        assertTrue("X-WR-TIMEZONE" !in written)
        assertTrue("LOCATION:Room 101" in written)
    }

    @Test
    fun fingerprintsShowWhichComponentsChanged() {
        val edited = text.replace("SUMMARY:Weekly sync", "SUMMARY:Daily sync").encodeToByteArray()
        val stored = ICal.fingerprints(bytes).encodeToString()
        val now = ICal.fingerprints(edited)
        val delta = now.diff(Fingerprints.decodeFromString(stored))
        assertEquals(1, delta.changed.size)
        assertEquals(1, delta.unchangedCount)
        assertTrue(delta.added.isEmpty() && delta.removed.isEmpty())
        val changed = delta.changed.map { ICal.parser().parseComponent(edited, it) }
        assertEquals("Daily sync", changed.single().property("SUMMARY")?.value)
        assertEquals(Fingerprints.decodeFromString(now.encodeToString()), now)
    }
}
