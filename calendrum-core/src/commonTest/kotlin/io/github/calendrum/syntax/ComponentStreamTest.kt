package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.asByteInput
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ComponentStreamTest {
    private val feed = crlf(
        "BEGIN:VCALENDAR",
        "PRODID:-//Example//Feed//EN",
        "X-WR-TIMEZONE:Europe/Moscow",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Moscow",
        "BEGIN:STANDARD",
        "DTSTART:19700101T000000",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "UID:a",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "END:VALARM",
        "END:VEVENT",
        "X-WR-CALNAME:After the first event",
        "BEGIN:VEVENT",
        "UID:b",
        "END:VEVENT",
        "METHOD:PUBLISH",
        "END:VCALENDAR",
        "BEGIN:VCALENDAR",
        "PRODID:second",
        "BEGIN:VTODO",
        "UID:c",
        "END:VTODO",
        "END:VCALENDAR",
    )

    @Test
    fun childrenOfEachCalendarComeOneByOne() {
        val stream = ICal.parser().components(feed.encodeToByteArray().asByteInput())
        assertEquals(-1, stream.calendarIndex)
        assertEquals(emptyList(), stream.calendarProperties)

        val zone = stream.next()
        assertEquals("VTIMEZONE", zone.name)
        assertEquals("STANDARD", zone.components.single().name)
        assertEquals(0, stream.calendarIndex)
        assertEquals(listOf("PRODID", "X-WR-TIMEZONE"), stream.calendarProperties.map { it.name })

        val first = stream.next()
        assertEquals("a", first.property("UID")!!.value)
        assertEquals("VALARM", first.components.single().name)
        assertEquals(listOf("PRODID", "X-WR-TIMEZONE"), stream.calendarProperties.map { it.name })
        val before = stream.calendarProperties

        val second = stream.next()
        assertEquals("b", second.property("UID")!!.value)
        assertEquals(0, stream.calendarIndex)
        assertEquals(listOf("PRODID", "X-WR-TIMEZONE", "X-WR-CALNAME"), stream.calendarProperties.map { it.name })
        assertEquals(2, before.size)

        assertTrue(stream.hasNext())
        assertEquals(0, stream.calendarIndex)
        assertEquals(
            listOf("PRODID", "X-WR-TIMEZONE", "X-WR-CALNAME", "METHOD"),
            stream.calendarProperties.map { it.name },
        )
        val todo = stream.next()
        assertEquals("VTODO", todo.name)
        assertEquals(1, stream.calendarIndex)
        assertEquals(listOf("second"), stream.calendarProperties.map { it.value })

        assertFalse(stream.hasNext())
        assertFalse(stream.hasNext())
        assertFailsWith<NoSuchElementException> { stream.next() }
        assertEquals(1, stream.calendarIndex)
        assertEquals(emptyList(), stream.problems)
    }

    @Test
    fun calendarPropertiesKeepTheirSpans() {
        val bytes = feed.encodeToByteArray()
        val stream = ICal.parser().components(bytes)
        stream.next()
        val tree = ICal.parse(bytes).components.first()
        assertEquals(tree.properties.take(2).map { it.span }, stream.calendarProperties.map { it.span })
        assertEquals(tree.components.first().span, ICal.parser().components(bytes).next().span)
    }

    @Test
    fun topLevelComponentsOutsideCalendarsComeThemselves() {
        val input = crlf(
            "BEGIN:VEVENT",
            "UID:loose",
            "END:VEVENT",
            "BEGIN:VCALENDAR",
            "PRODID:p",
            "BEGIN:VEVENT",
            "UID:inside",
            "END:VEVENT",
            "END:VCALENDAR",
            "BEGIN:VCARD",
            "FN:x",
            "END:VCARD",
        )
        val stream = ICal.parser().components(input.encodeToByteArray())
        val loose = stream.next()
        assertEquals("loose", loose.property("UID")!!.value)
        assertEquals(-1, stream.calendarIndex)
        assertEquals(emptyList(), stream.calendarProperties)
        stream.next()
        assertEquals(0, stream.calendarIndex)
        val card = stream.next()
        assertEquals("VCARD", card.name)
        assertEquals(-1, stream.calendarIndex)
        assertFalse(stream.hasNext())
    }

    @Test
    fun brokenInputsCloseComponentsImplicitly() {
        val input = crlf(
            "X-OUTSIDE:1",
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:a",
            "BEGIN:VALARM",
            "END:VEVENT",
            "BEGIN:VEVENT",
            "UID:b",
            "END:VCALENDAR",
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:c",
        )
        val bytes = input.encodeToByteArray()
        val stream = ICal.parser().components(bytes)
        val names = stream.asSequence().map { it.property("UID")!!.value }.toList()
        assertEquals(listOf("a", "b", "c"), names)
        assertEquals(1, stream.calendarIndex)
        assertEquals(
            listOf("ICAL-S021", "ICAL-S016", "ICAL-S016", "ICAL-S018"),
            stream.problems.map { it.code.id },
        )
        assertEquals(ICal.parse(bytes).problems.map { it.toString() }, stream.problems.map { it.toString() })
        assertEquals(flatComponents(ICal.parse(bytes)), streamedComponents(ICal.parser().components(bytes)))
    }

    @Test
    fun sequenceIsSinglePass() {
        val stream = ICal.parser().components(feed.encodeToByteArray())
        val sequence = stream.asSequence()
        assertEquals(4, sequence.count())
        assertFailsWith<IllegalStateException> { sequence.count() }
    }

    @Test
    fun strictStreamFailsAndStaysFailed() {
        val input =
            crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:a", "END:VEVENT", "BROKEN", "BEGIN:VEVENT", "END:VEVENT")
        val parser = ICal.parser { mode = ParseMode.Strict }
        val stream = parser.components(input.encodeToByteArray().asByteInput())
        assertEquals("VEVENT", stream.next().name)
        val failure = assertFailsWith<ICalParseException> { stream.hasNext() }
        assertEquals("ICAL-S007", failure.problems.last().code.id)
        assertSame(failure, assertFailsWith<ICalParseException> { stream.next() })
        assertSame(failure, assertFailsWith<ICalParseException> { stream.hasNext() })
    }

    @Test
    fun problemsGoToTheListenerAsTheyAppear() {
        val seen = ArrayList<String>()
        val parser = ICal.parser { problemListener = { seen += it.code.id } }
        val stream = parser.components(crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "END:VEVENT", "X").encodeToByteArray())
        stream.next()
        assertEquals(emptyList(), seen)
        assertFalse(stream.hasNext())
        assertEquals(listOf("ICAL-S007", "ICAL-S018"), seen)
        assertEquals(seen, stream.problems.map { it.code.id })
    }

    @Test
    fun streamedNodesAreCompactCopies() {
        val bytes = feed.encodeToByteArray()
        val events = ICal.parser().components(ChunkedInput(bytes, intArrayOf(5)), 7).asSequence().toList()
        val written = ICal.writer().encodeToByteArray(events)
        val expected = ICal.writer().encodeToByteArray(ICal.parse(bytes).components.flatMap { it.components })
        assertTrue(written.contentEquals(expected))
        assertNull(ICal.parser().components(ByteArray(0)).asSequence().firstOrNull())
    }
}
