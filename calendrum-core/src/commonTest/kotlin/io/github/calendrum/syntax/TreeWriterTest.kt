package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class TreeWriterTest {
    private val canonical = ICal.writer(WriteOptions.builder().byteStable(false).build())

    @Test
    fun byteStableWritingReproducesRfcExamples() {
        for ((name, example) in WriterRfcExamplesTest.rfc5545Examples) {
            val bytes = example.encodeToByteArray()
            val tree = ICal.parse(bytes).components
            assertContentEquals(bytes, ICal.writer().encodeToByteArray(tree), name)
            assertEquals(example, ICal.writer().encodeToString(tree), name)
            assertContentEquals(bytes, ICal.writer().encodeToByteArray(tree.single()), name)
            assertEquals(example, ICal.writer().encodeToString(tree.single()), name)
            val normalized = canonical.encodeToString(tree)
            assertEquals(unfold(example), unfold(normalized), name)
            assertEquals(tree, ICal.parse(normalized).components, name)
            assertFoldedCorrectly(Written(normalized.encodeToByteArray(), normalized))
        }
    }

    @Test
    fun builtTreesAreWrittenCanonically() {
        val calendar = ICalComponent("vcalendar") {
            property("VERSION", "2.0")
            component("VEVENT") {
                property("uid", "42@example.com")
                property(
                    ICalProperty.builder("ATTENDEE")
                        .parameter("CN", "Doe, Jane")
                        .parameter("X-NOTE", "say \"hi\"", "a^b")
                        .value("mailto:jane@example.com")
                        .build(),
                )
                property("DESCRIPTION", "Долгое описание. ".repeat(4) + "\tend")
            }
        }
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "BEGIN:VEVENT",
            "UID:42@example.com",
            "ATTENDEE;CN=\"Doe, Jane\";X-NOTE=say ^'hi^',a^^b:mailto:jane@example.com",
            "DESCRIPTION:Долгое описание. Долгое описание. ",
            " Долгое описание. Долгое описание. \tend",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        for (writer in listOf(ICal.writer(), canonical)) {
            assertEquals(expected, writer.encodeToString(calendar))
            assertContentEquals(expected.encodeToByteArray(), writer.encodeToByteArray(calendar))
            val out = StringBuilder("prefix|")
            writer.write(calendar, out)
            assertEquals("prefix|$expected", out.toString())
        }
        assertEquals(calendar, ICal.parse(expected).components.single())
    }

    @Test
    fun nonCanonicalInputKeepsItsSpellingWhenByteStable() {
        val input = "\uFEFFbegin:vcalendar\n" +
            "version:2.0\r" +
            "BEGIN:VEVENT\r\n" +
            "\r\n" +
            "Summ\n\tary;cn=\"Jane\";X-P=\"a:b\";x-q=\"plain\":Hello\r\n  world\n" +
            "ATTENDEE;RSVP=TRUE:mailto:a@example.com\r\n" +
            "END:vevent\r\n" +
            "end:VCALENDAR"
        val bytes = input.encodeToByteArray()
        val tree = ICal.parse(bytes).components
        val stable = "begin:vcalendar\r\n" +
            "version:2.0\r\n" +
            "BEGIN:VEVENT\r\n" +
            "Summ\r\n\tary;cn=\"Jane\";X-P=\"a:b\";x-q=\"plain\":Hello\r\n  world\r\n" +
            "ATTENDEE;RSVP=TRUE:mailto:a@example.com\r\n" +
            "END:vevent\r\n" +
            "end:VCALENDAR\r\n"
        assertEquals(stable, ICal.writer().encodeToString(tree))
        assertContentEquals(stable.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
        val normalized = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "BEGIN:VEVENT",
            "SUMMARY;CN=Jane;X-P=\"a:b\";X-Q=plain:Hello world",
            "ATTENDEE;RSVP=TRUE:mailto:a@example.com",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        assertEquals(normalized, canonical.encodeToString(tree))
        assertEquals(tree, ICal.parse(stable).components)
        assertEquals(tree, ICal.parse(normalized).components)
    }

    @Test
    fun implicitEndsAreWrittenCanonically() {
        val input = crlf("BEGIN:VCALENDAR", "begin:vevent", "UID:1", "BEGIN:x_y", "X:1", "END:VCALENDAR", "BEGIN:VTODO")
        val tree = ICal.parse(input).components
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "begin:vevent",
            "UID:1",
            "BEGIN:x_y",
            "X:1",
            "END:X_Y",
            "END:VEVENT",
            "END:VCALENDAR",
            "BEGIN:VTODO",
            "END:VTODO",
        )
        assertEquals(expected, ICal.writer().encodeToString(tree))
        assertContentEquals(expected.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
        assertEquals(tree, ICal.parse(expected).components)
        assertFailsWith<IllegalArgumentException> { canonical.encodeToString(tree) }
    }

    @Test
    fun implicitEndOfAFoldedInvalidNameKeepsItsFoldsInUpperCase() {
        val input = "BEGIN:VCALENDAR\r\nBEGIN:ab_c\n d_e\r f\r\nX:1\r\nEND:VCALENDAR\r\n"
        val tree = ICal.parse(input).components
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:ab_c",
            " d_e",
            " f",
            "X:1",
            "END:AB_C",
            " D_E",
            " F",
            "END:VCALENDAR",
        )
        assertEquals(expected, ICal.writer().encodeToString(tree))
        assertContentEquals(expected.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
    }

    @Test
    fun propertiesAfterChildComponentsStayInPlace() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VTIMEZONE",
            "TZID:A",
            "END:VTIMEZONE",
            "X-WR-CALNAME:late",
            "BEGIN:VEVENT",
            "BEGIN:VALARM",
            "ACTION:DISPLAY",
            "END:VALARM",
            "UID:1",
            "END:VEVENT",
            "VERSION:2.0",
            "END:VCALENDAR",
        )
        val tree = ICal.parse(input).components
        assertEquals(input, ICal.writer().encodeToString(tree))
        assertEquals(listOf("X-WR-CALNAME", "VERSION"), tree.single().properties.map { it.name })
        assertEquals(input, canonical.encodeToString(tree))
        val sorted = ICalComponent.builder("VCALENDAR")
        for (property in tree.single().properties) sorted.property(property)
        for (child in tree.single().components) sorted.component(child)
        val propertiesFirst = ICal.writer().encodeToString(sorted.build())
        assertEquals(
            crlf(
                "BEGIN:VCALENDAR",
                "X-WR-CALNAME:late",
                "VERSION:2.0",
                "BEGIN:VTIMEZONE",
                "TZID:A",
                "END:VTIMEZONE",
                "BEGIN:VEVENT",
                "BEGIN:VALARM",
                "ACTION:DISPLAY",
                "END:VALARM",
                "UID:1",
                "END:VEVENT",
                "END:VCALENDAR",
            ),
            propertiesFirst,
        )
        assertEquals(tree, ICal.parse(propertiesFirst).components)
        val copied = ICal.parser { retainSource = false }.parse(input).components
        assertEquals(input, ICal.writer().encodeToString(copied))
    }

    @Test
    fun mixedTreesWriteEveryLineByItsOwnRule() {
        val parsed = ICal.parse(crlf("begin:vevent", "uid:1", "Summary;cn=\"x\":Old", "end:vevent")).components.single()
        val edited = ICalComponent.builder("VEVENT")
        for (property in parsed.properties) {
            if (property.name != "SUMMARY") edited.property(property)
        }
        edited.property("SUMMARY", "New")
        val calendar = ICalComponent("VCALENDAR") {
            property("VERSION", "2.0")
            component(parsed)
            component(edited.build())
        }
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "begin:vevent",
            "uid:1",
            "Summary;cn=\"x\":Old",
            "end:vevent",
            "BEGIN:VEVENT",
            "uid:1",
            "SUMMARY:New",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        assertEquals(expected, ICal.writer().encodeToString(calendar))
        assertEquals(
            expected.replace("begin:vevent", "BEGIN:VEVENT").replace("end:vevent", "END:VEVENT")
                .replace("uid:", "UID:").replace("Summary;cn=\"x\"", "SUMMARY;CN=x"),
            canonical.encodeToString(calendar),
        )
    }

    @Test
    fun timeZonesComeFirstInsideCalendars() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "BEGIN:VEVENT",
            "UID:1",
            "END:VEVENT",
            "BEGIN:VTIMEZONE",
            "TZID:A",
            "END:VTIMEZONE",
            "X-WR-CALNAME:after",
            "BEGIN:VTODO",
            "BEGIN:VTIMEZONE",
            "TZID:nested",
            "END:VTIMEZONE",
            "END:VTODO",
            "BEGIN:vtimezone",
            "TZID:B",
            "END:vtimezone",
            "END:VCALENDAR",
        )
        val tree = ICal.parse(input).components
        val expected = crlf(
            "BEGIN:VCALENDAR",
            "VERSION:2.0",
            "X-WR-CALNAME:after",
            "BEGIN:VTIMEZONE",
            "TZID:A",
            "END:VTIMEZONE",
            "BEGIN:vtimezone",
            "TZID:B",
            "END:vtimezone",
            "BEGIN:VEVENT",
            "UID:1",
            "END:VEVENT",
            "BEGIN:VTODO",
            "BEGIN:VTIMEZONE",
            "TZID:nested",
            "END:VTIMEZONE",
            "END:VTODO",
            "END:VCALENDAR",
        )
        val zonesFirst = ICal.writer(WriteOptions.builder().timeZonesFirst(true).build())
        assertEquals(expected, zonesFirst.encodeToString(tree))
        assertContentEquals(expected.encodeToByteArray(), zonesFirst.encodeToByteArray(tree))
        val canonicalZonesFirst = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build())
        assertEquals(expected.replace("vtimezone", "VTIMEZONE"), canonicalZonesFirst.encodeToString(tree))
        assertEquals(input, ICal.writer().encodeToString(tree))
        val sorted = ICal.parse(expected).components.single()
        assertEquals(listOf("VTIMEZONE", "VTIMEZONE", "VEVENT", "VTODO"), sorted.components.map { it.name })
    }

    @Test
    fun controlCharactersFailWhenCanonicalAndStayWhenByteStable() {
        val secret = "SECRET\u0001VALUE"
        val input = crlf("BEGIN:VEVENT", "DESCRIPTION:$secret", "X-TAB:a\tb", "END:VEVENT")
        val tree = ICal.parse(input).components
        assertEquals(input, ICal.writer().encodeToString(tree))
        val error = assertFailsWith<IllegalArgumentException> { canonical.encodeToByteArray(tree) }
        val message = error.message.orEmpty()
        assertTrue("DESCRIPTION" in message, message)
        assertFalse("SECRET" in message, message)
        assertFailsWith<IllegalArgumentException> { canonical.encodeToString(tree) }

        val codes = (0x00..0x1F).filter { it != 0x09 } + 0x7F
        for (code in codes) {
            val value = "SECRET${code.toChar()}VALUE"
            val built = assertFailsWith<IllegalArgumentException> { ICalProperty.of("X", value) }
            assertFalse("SECRET" in built.message.orEmpty())
            assertFailsWith<IllegalArgumentException> { ICalProperty.builder("X").value(value).build() }
            assertFailsWith<IllegalArgumentException> { ICalComponent("X") { property("Y", value) } }
        }
        assertEquals("a\tb", ICalProperty.of("X", "a\tb").value)
        assertEquals("a\u0085b", ICalProperty.of("X", "a\u0085b").value)
    }

    @Test
    fun whatCannotBeWrittenCanonicallyFailsWithoutValues() {
        val cases = listOf(
            crlf("BEGIN:X", "Y;NOVALUE:SECRET", "END:X") to "property Y",
            crlf("BEGIN:X", "BAD_NAME:SECRET", "END:X") to "a property",
            crlf("BEGIN:X", "Y;BAD_PARAM=1:SECRET", "END:X") to "property Y",
            crlf("BEGIN:X", "Y;P=\"a\u0002b\":SECRET", "END:X") to "property Y",
            crlf("BEGIN:X Y", "Z:SECRET", "END:X Y") to "component",
        )
        for ((input, subject) in cases) {
            val tree = ICal.parse(input).components
            assertEquals(input, ICal.writer().encodeToString(tree), input)
            val message = assertFailsWith<IllegalArgumentException>(input) { canonical.encodeToString(tree) }.message
            assertTrue(subject in message.orEmpty(), "$input: $message")
            assertFalse("SECRET" in message.orEmpty(), "$input: $message")
        }
    }

    @Test
    fun bytesMatchTheTextOfTheAppendable() {
        val inputs = WriterRfcExamplesTest.rfc5545Examples.values + crlf(
            "BEGIN:VEVENT",
            "SUMMARY:Лекция 😀 中文",
            "DESCRIPTION:" + "é".repeat(100),
            "END:VEVENT",
        )
        for (input in inputs) {
            val tree = ICal.parse(input).components
            for (writer in listOf(ICal.writer(), canonical)) {
                assertContentEquals(writer.encodeToString(tree).encodeToByteArray(), writer.encodeToByteArray(tree))
            }
        }
    }

    @Test
    fun brokenUtf8InTextOutputReadsBackAsTheSameTree() {
        val input = "BEGIN:X\r\nSUMMARY:a".encodeToByteArray() + bytesOf(0xC3, 0x0D, 0x0A, 0x20, 0xA9) +
            "\r\nDESCRIPTION:\r\n\tЛекция\r\nX-BAD:a".encodeToByteArray() + bytesOf(0xFF) + "b\r\nEND:X\r\n"
        val result = ICal.parse(input)
        assertEquals("UTF-8", result.charset)
        val tree = result.components
        assertEquals("aé", tree.single().property("SUMMARY")!!.value)
        assertEquals("Лекция", tree.single().property("DESCRIPTION")!!.value)
        assertEquals("a\uFFFDb", tree.single().property("X-BAD")!!.value)
        assertContentEquals(input, ICal.writer().encodeToByteArray(tree))
        val text = ICal.writer().encodeToString(tree)
        assertEquals("BEGIN:X\r\nSUMMARY:a\r\n é\r\nDESCRIPTION:\r\n\tЛекция\r\nX-BAD:a\uFFFDb\r\nEND:X\r\n", text)
        assertEquals(tree, ICal.parse(text).components)
    }

    @Test
    fun emptyContinuationLinesInsideACharacterMoveInFrontOfIt() {
        val input = "BEGIN:X\r\nX-PRE:Жжжж\r\nA:".encodeToByteArray() +
            bytesOf(0xE2, 0x0D, 0x0A, 0x20, 0x0A, 0x09, 0x82) +
            bytesOf(0x0D, 0x0A, 0x20, 0xAC) + "\r\nB:x\r\n \r\nEND:X\r\n"
        val tree = ICal.parse(input).components
        assertEquals("€", tree.single().property("A")!!.value)
        assertEquals("x", tree.single().property("B")!!.value)
        val text = ICal.writer().encodeToString(tree)
        assertEquals("BEGIN:X\r\nX-PRE:Жжжж\r\nA:\r\n \r\n\t\r\n €\r\nB:x\r\n \r\nEND:X\r\n", text)
        assertEquals(tree, ICal.parse(text).components)
        val expected = "BEGIN:X\r\nX-PRE:Жжжж\r\nA:".encodeToByteArray() +
            bytesOf(0xE2, 0x0D, 0x0A, 0x20, 0x0D, 0x0A, 0x09, 0x82) +
            bytesOf(0x0D, 0x0A, 0x20, 0xAC) + "\r\nB:x\r\n \r\nEND:X\r\n"
        assertContentEquals(expected, ICal.writer().encodeToByteArray(tree))
    }

    @Test
    fun aBlankLineBeforeAContinuationStaysPartOfTheLine() {
        val input = crlf("BEGIN:A", "", "  X:1", "BEGIN:B", "", " END:B", "END:A")
        val tree = ICal.parse(input).components
        val property = tree.single().properties.single()
        assertEquals(" X", property.name)
        assertEquals(9L, property.span!!.startOffset)
        assertEquals(input, ICal.writer().encodeToString(tree))
        assertContentEquals(input.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
        assertEquals(tree, ICal.parse(input).components)
        assertEquals(tree.map { it.shape() }, cursorShapes(ICal.parser().reader(input)))
    }

    @Test
    fun transcodedInputIsWrittenInUtf8() {
        val text = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "summary:Лекция по алгебре", "END:VEVENT", "END:VCALENDAR")
        val inputs = listOf(
            TestCharsets.encode(text, "windows-1251"),
            TestCharsets.bom("UTF-16LE") + TestCharsets.encode(text, "UTF-16LE"),
        )
        for (bytes in inputs) {
            val tree = ICal.parse(bytes).components
            assertContentEquals(text.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
            assertEquals(text, ICal.writer().encodeToString(tree))
        }
    }

    @Test
    fun treesWithoutTheSourceAreByteStableToo() {
        for (example in WriterRfcExamplesTest.rfc5545Examples.values) {
            val tree = ICal.parser { retainSource = false }.parse(example).components
            assertEquals(example, ICal.writer().encodeToString(tree))
        }
    }

    @Test
    fun listsOfCalendarsAreWrittenOneAfterAnother() {
        val one = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY:one", "END:VEVENT", "END:VCALENDAR")
        val input = one + one.replace("one", "two") + one.replace("one", "three")
        val tree = ICal.parse(input).components
        assertEquals(3, tree.size)
        assertEquals(input, ICal.writer().encodeToString(tree))
        assertContentEquals(input.encodeToByteArray(), ICal.writer().encodeToByteArray(tree))
        val out = StringBuilder()
        ICal.writer().write(tree, out)
        assertEquals(input, out.toString())
        assertEquals("", ICal.writer().encodeToString(emptyList()))
        assertContentEquals(ByteArray(0), ICal.writer().encodeToByteArray(emptyList()))
        val built = List(3) { ICalComponent("X") { property("N", "$it") } }
        val expected = crlf("BEGIN:X", "N:0", "END:X", "BEGIN:X", "N:1", "END:X", "BEGIN:X", "N:2", "END:X")
        assertEquals(expected, ICal.writer().encodeToString(built))
    }

    @Test
    fun deepBuiltTreesAreWrittenWithoutRecursion() {
        var component = ICalComponent("LEAF") { property("X-VALUE", "a") }
        repeat(DEEP) { component = ICalComponent.builder("NODE").component(component).build() }
        val bytes = ICal.writer().encodeToByteArray(component)
        assertEquals(DEEP * ("BEGIN:NODE\r\n".length + "END:NODE\r\n".length) + 33, bytes.size)
        assertEquals(bytes.size, ICal.writer().encodeToString(component).length)
    }

    @Test
    fun optionsAndEntryPoints() {
        val options = WriteOptions.builder().byteStable(false).timeZonesFirst(true).build()
        assertFalse(options.byteStable)
        assertTrue(options.timeZonesFirst)
        assertTrue(WriteOptions.Default.byteStable)
        assertFalse(WriteOptions.Default.timeZonesFirst)
        assertEquals(
            "WriteOptions(byteStable=false, timeZonesFirst=true, interceptors=0, allowPartialWrite=false, missingTimeZones=none)",
            options.toString(),
        )
        assertSame(ICal.writer(), ICal.writer())
        assertEquals(
            "ICalWriter(WriteOptions(byteStable=true, timeZonesFirst=false, interceptors=0, allowPartialWrite=false, missingTimeZones=none))",
            ICal.writer().toString(),
        )
        val builder = WriteOptions.builder()
        builder.byteStable = false
        assertFalse(builder.build().byteStable)
    }

    private companion object {
        const val DEEP = 100_000
    }
}
