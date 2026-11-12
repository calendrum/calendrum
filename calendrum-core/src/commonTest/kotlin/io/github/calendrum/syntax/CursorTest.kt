package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.SourceSpan
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

// Strings with unpaired surrogates are built with `Char(0xD800)`: Kotlin/JS incremental compilation corrupted such
// literals to '?' in files restored from its cache.
class CursorTest {
    private val text = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "BEGIN:VEVENT",
        "uid:42@example.com",
        "ATTENDEE;cn=\"Doe, Jane\";X-NOTE=a^nb;X-EMPTY:mailto:jane@example.com",
        "SUMMARY:Лекция 😀",
        "BEGIN:x-vendor",
        "X-A:1",
        "END:X-VENDOR",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    @Test
    fun eventsFollowTheInput() {
        val reader = ICal.parser().reader(text.encodeToByteArray())
        val events = ArrayList<String>()
        while (true) {
            val type = reader.next() ?: break
            events += "$type ${reader.depth} ${reader.name}"
        }
        assertEquals(
            listOf(
                "Begin 1 VCALENDAR",
                "Property 1 VERSION",
                "Begin 2 VEVENT",
                "Property 2 UID",
                "Property 2 ATTENDEE",
                "Property 2 SUMMARY",
                "Begin 3 X-VENDOR",
                "Property 3 X-A",
                "End 3 X-VENDOR",
                "End 2 VEVENT",
                "End 1 VCALENDAR",
            ),
            events,
        )
        assertNull(reader.next())
        assertNull(reader.next())
        assertEquals(0, reader.depth)
        assertFalse(reader.isImplicitEnd)
    }

    @Test
    fun propertiesExposeValuesAndParameters() {
        val reader = ICal.parser().reader(text)
        advanceTo(reader, "ATTENDEE")
        assertEquals(3, reader.parameterCount)
        assertEquals("CN", reader.parameterName(0))
        assertEquals(listOf("Doe, Jane"), reader.parameterValues(0))
        assertEquals("X-NOTE", reader.parameterName(1))
        assertEquals(listOf("a\nb"), reader.parameterValues(1))
        assertEquals("X-EMPTY", reader.parameterName(2))
        assertEquals(emptyList(), reader.parameterValues(2))
        assertFailsWith<IndexOutOfBoundsException> { reader.parameterName(3) }
        val value = reader.value
        assertTrue(value.contentEquals("mailto:jane@example.com"))
        assertEquals("mailto:jane@example.com", reader.valueString())
        assertSame(value, reader.value)

        assertEquals(ContentEventType.Property, reader.next())
        assertEquals("SUMMARY", reader.name)
        assertEquals(0, reader.parameterCount)
        assertEquals("Лекция 😀", reader.valueString())
        assertSame(value, reader.value)
        assertEquals("Лекция 😀", value.toString())
        assertEquals(9, value.length)
        assertEquals('Л', value[0])
        assertEquals("кц", value.subSequence(2, 4))
        assertFailsWith<IndexOutOfBoundsException> { value[9] }
        assertFailsWith<IndexOutOfBoundsException> { value.subSequence(3, 10) }
        assertEquals("Лекция 😀", reader.valueString())
    }

    @Test
    fun namesAreCanonicalAndKnownOnesShared() {
        val tree = ICal.parse(text).components.single()
        val reader = ICal.parser().reader(text.lowercase())
        reader.next()
        assertSame(tree.name, reader.name)
        reader.next()
        assertSame(tree.properties[0].name, reader.name)
        advanceTo(reader, "ATTENDEE")
        assertSame(tree.components[0].property("ATTENDEE")!!.parameters[0].name, reader.parameterName(0))
        advanceTo(reader, "X-A")
        assertEquals(3, reader.depth)
        assertEquals(ContentEventType.End, reader.next())
        assertEquals("X-VENDOR", reader.name)
    }

    @Test
    fun positionsAndSpansMatchTheTree() {
        val folded =
            crlf(
                "BEGIN:VCALENDAR",
                "BEGIN:VEVENT",
                "SUMM",
                " ARY;X-A=1:Лек",
                " ция",
                "END:VEV",
                " ENT",
                "END:VCALENDAR",
            )
        for (input in listOf(text, folded)) {
            val tree = ICal.parse(input).components
            assertEquals(tree.map { it.shape() }, cursorShapes(ICal.parser().reader(input)), input)
        }
        val reader = ICal.parser().reader(folded)
        repeat(3) { reader.next() }
        assertEquals(SourceSpan(31, 63, 3, 1), reader.span())
        assertEquals(31L, reader.offset)
        assertEquals(3, reader.line)
        assertEquals(1, reader.column)
        assertEquals(ContentEventType.End, reader.next())
        assertEquals(SourceSpan(65, 78, 6, 1), reader.span())
    }

    @Test
    fun implicitEndsAndPropertiesOutsideComponents() {
        val input = crlf(
            "X-WR-CALNAME:before",
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:1",
            "BEGIN:VALARM",
            "ACTION:AUDIO",
            "END:VEVENT",
            "END:VTODO",
            "BEGIN:VEVENT",
            "UID:2",
        )
        val problems = ArrayList<Problem>()
        val reader = ICal.parser { problemListener = ProblemListener { problems += it } }.reader(input)
        val events = ArrayList<String>()
        while (true) {
            val type = reader.next() ?: break
            events += "$type ${reader.name}${if (reader.isImplicitEnd) " implicit" else ""}"
            if (reader.isImplicitEnd) assertEquals(reader.offset, reader.span().endOffset)
        }
        assertEquals(
            listOf(
                "Begin VCALENDAR",
                "Begin VEVENT",
                "Property UID",
                "Begin VALARM",
                "Property ACTION",
                "End VALARM implicit",
                "End VEVENT",
                "Begin VEVENT",
                "Property UID",
                "End VEVENT implicit",
                "End VCALENDAR implicit",
            ),
            events,
        )
        assertEquals(listOf("ICAL-S021", "ICAL-S016", "ICAL-S017", "ICAL-S018"), problems.map { it.code.id })
        assertEquals(ICal.parse(input).components.map { it.shape() }, cursorShapes(ICal.parser().reader(input)))
    }

    @Test
    fun readComponentBuildsTheSameNodes() {
        val tree = ICal.parse(text).components.single()
        val reader = ICal.parser().reader(text.encodeToByteArray())
        assertEquals(ContentEventType.Begin, reader.next())
        assertEquals(ContentEventType.Property, reader.next())
        assertEquals(ContentEventType.Begin, reader.next())
        val event = reader.readComponent()
        assertEquals(tree.components.single(), event)
        assertEquals(tree.components.single().shape(), event.shape())
        assertEquals("VEVENT", reader.name)
        assertEquals(2, reader.depth)
        assertFalse(reader.isImplicitEnd)
        assertEquals(ContentEventType.End, reader.next())
        assertEquals("VCALENDAR", reader.name)
        assertNull(reader.next())

        val whole = ICal.parser().reader(text)
        whole.next()
        assertEquals(tree.shape(), whole.readComponent().shape())
        assertNull(whole.next())
    }

    @Test
    fun readComponentCopiesLinesWithoutTheSource() {
        val bytes = (text + text.replace("42@", "43@")).encodeToByteArray()
        val reader = ICal.parser { retainSource = false }.reader(bytes)
        val calendars = ArrayList<ICalComponent>()
        while (true) {
            val type = reader.next() ?: break
            if (type == ContentEventType.Begin) calendars += reader.readComponent()
        }
        assertEquals(ICal.parse(bytes).components, calendars)
        assertContentEquals(bytes, ICal.writer().encodeToByteArray(calendars))
    }

    @Test
    fun callsOutOfPlaceThrowIllegalState() {
        val reader = ICal.parser().reader(text)
        assertFailsWith<IllegalStateException> { reader.name }
        assertFailsWith<IllegalStateException> { reader.value }
        assertFailsWith<IllegalStateException> { reader.offset }
        assertFailsWith<IllegalStateException> { reader.span() }
        assertFailsWith<IllegalStateException> { reader.readComponent() }
        reader.next()
        assertFailsWith<IllegalStateException> { reader.value }
        assertFailsWith<IllegalStateException> { reader.valueString() }
        assertFailsWith<IllegalStateException> { reader.parameterCount }
        assertFailsWith<IllegalStateException> { reader.parameterName(0) }
        assertFailsWith<IllegalStateException> { reader.parameterValues(0) }
        reader.next()
        assertFailsWith<IllegalStateException> { reader.readComponent() }
        while (reader.next() != null) Unit
        assertFailsWith<IllegalStateException> { reader.name }
        assertFailsWith<IllegalStateException> { reader.line }
        assertFailsWith<IllegalStateException> { reader.readComponent() }
    }

    @Test
    fun strictModeThrowsAndStaysFailed() {
        val input = crlf("BEGIN:VCALENDAR", "VERSION:2.0", "BROKEN", "SUMMARY:after", "END:VCALENDAR")
        val strict = ICal.parser { mode = ParseMode.Strict }
        val reader = strict.reader(input)
        assertEquals(ContentEventType.Begin, reader.next())
        assertEquals(ContentEventType.Property, reader.next())
        val error = assertFailsWith<ICalParseException> { reader.next() }
        assertEquals(listOf("ICAL-S007"), error.problems.map { it.code.id })
        assertSame(error, assertFailsWith<ICalParseException> { reader.next() })
        assertFailsWith<IllegalStateException> { reader.name }

        val building = strict.reader(input)
        building.next()
        assertSame(
            assertFailsWith<ICalParseException> { building.readComponent() },
            assertFailsWith<ICalParseException> { building.next() },
        )

        val lenient = ICal.parser().reader(input)
        var properties = 0
        while (true) {
            val type = lenient.next() ?: break
            if (type == ContentEventType.Property) properties++
        }
        assertEquals(2, properties)
    }

    @Test
    fun inputsAndCharsetsAreTheSameAsForParse() {
        val calendar =
            crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY:Лекция по алгебре", "END:VEVENT", "END:VCALENDAR")
        val cp1251 = TestCharsets.encode(calendar, "windows-1251")
        val reader = ICal.parser().reader(cp1251)
        advanceTo(reader, "SUMMARY")
        assertEquals("Лекция по алгебре", reader.valueString())
        assertEquals(ICal.parse(cp1251).components.map { it.shape() }, cursorShapes(ICal.parser().reader(cp1251)))

        val bytes = "garbage".encodeToByteArray() + calendar.encodeToByteArray() + "tail".encodeToByteArray()
        val ranged = ICal.parser().reader(bytes, 7, calendar.encodeToByteArray().size)
        assertEquals(ContentEventType.Begin, ranged.next())
        assertEquals(0L, ranged.offset)
        assertEquals(
            ICal.parser().parse(bytes, 7, calendar.encodeToByteArray().size).components.map { it.shape() },
            cursorShapes(ICal.parser().reader(bytes, 7, calendar.encodeToByteArray().size)),
        )
        assertFailsWith<IllegalArgumentException> { ICal.parser().reader(bytes, 7, bytes.size) }

        val broken = "BEGIN:X\r\nSUMMARY:a" + Char(0xD800) + "b\r\nEND:X\r\n"
        val text = ICal.parser().reader(broken)
        advanceTo(text, "SUMMARY")
        assertEquals("a�b", text.value.toString())
    }

    @Test
    fun eventsMatchTheTreeOnAssortedInputs() {
        val inputs = WriterRfcExamplesTest.rfc5545Examples.values + listOf(
            "BEGIN:A\nX;P=\"1\":v\nBEGIN:B\nY:2\nEND:B\nZ:3\nEND:A\n",
            "BEGIN:A\r\nBEGIN:B\r\nBEGIN:C\r\nEND:A\r\nEND:C\r\n",
            " BEGIN:A\r\n\r\nX:\r\n\tY\r\nEND:A",
            "",
        )
        for (input in inputs) {
            assertEquals(
                ICal.parse(input).components.map {
                    it.shape()
                },
                cursorShapes(ICal.parser().reader(input)),
                input,
            )
        }
    }

    @Test
    fun toStringShowsNoValues() {
        val reader = ICal.parser().reader(text)
        assertEquals("ICalReader(event=none, depth=0)", reader.toString())
        advanceTo(reader, "SUMMARY")
        assertEquals("ICalReader(event=Property, depth=2)", reader.toString())
        while (reader.next() != null) Unit
        assertEquals("ICalReader(event=finished, depth=0)", reader.toString())
    }

    private fun advanceTo(reader: ICalReader, property: String) {
        while (true) {
            val type = reader.next() ?: throw AssertionError("$property not found")
            if (type == ContentEventType.Property && reader.name == property) return
        }
    }
}
