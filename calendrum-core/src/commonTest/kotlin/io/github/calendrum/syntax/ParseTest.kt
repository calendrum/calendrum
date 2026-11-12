package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.Severity
import io.github.calendrum.SourceSpan
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// Strings with unpaired surrogates are built with `Char(0xD800)`: Kotlin/JS incremental compilation corrupted such
// literals to '?' in files restored from its cache.
class ParseTest {
    private val invite = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//ics-mp//Parse test//EN",
        "BEGIN:VEVENT",
        "UID:42@example.com",
        "DTSTART;TZID=Europe/Moscow:20260901T090000",
        "SUMMARY:Лекция\\, аудитория 5",
        "ATTENDEE;CN=\"Doe, Jane\";ROLE=REQ-PARTICIPANT;X-NOTE=a^nb^^c^'d^x:mailto:jane@example.com",
        "CATEGORIES:A,B",
        "X-custom-Prop;x-Param=1,2:raw",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:x-vendor",
        "END:X-VENDOR",
        "END:VCALENDAR",
    )

    @Test
    fun parsesTheTreeOfAnInvitation() {
        val result = ICal.parse(invite)
        assertEquals(emptyList(), result.problems)
        assertFalse(result.isTruncated)
        assertEquals("UTF-8", result.charset)
        assertEquals(100, result.charsetConfidence)
        val calendar = result.components.single()
        assertEquals("VCALENDAR", calendar.name)
        assertEquals(listOf("VERSION", "PRODID"), calendar.properties.map { it.name })
        assertEquals(listOf("VEVENT", "X-VENDOR"), calendar.components.map { it.name })

        val event = calendar.components("vevent").single()
        assertEquals(
            listOf("UID", "DTSTART", "SUMMARY", "ATTENDEE", "CATEGORIES", "X-CUSTOM-PROP"),
            event.properties.map { it.name },
        )
        assertEquals("Лекция\\, аудитория 5", event.property("SUMMARY")!!.value)
        assertEquals("20260901T090000", event.property("dtstart")!!.value)
        assertEquals("Europe/Moscow", event.property("DTSTART")!!.parameter("tzid")!!.value)
        assertEquals("A,B", event.property("CATEGORIES")!!.value)

        val attendee = event.property("ATTENDEE")!!
        assertEquals("mailto:jane@example.com", attendee.value)
        assertEquals(listOf("CN", "ROLE", "X-NOTE"), attendee.parameters.map { it.name })
        assertEquals(listOf("Doe, Jane"), attendee.parameter("CN")!!.values)
        assertEquals("a\nb^c\"d^x", attendee.parameter("x-note")!!.value)
        assertNull(attendee.parameter("PARTSTAT"))

        val custom = event.property("x-CUSTOM-prop")!!
        assertEquals("X-CUSTOM-PROP", custom.name)
        assertEquals(listOf("1", "2"), custom.parameter("X-PARAM")!!.values)
        assertEquals("X-PARAM", custom.parameters.single().name)

        val alarm = event.components.single()
        assertEquals("VALARM", alarm.name)
        assertEquals(listOf("DISPLAY"), alarm.properties("ACTION").map { it.value })
        assertEquals(emptyList(), alarm.properties("SUMMARY"))
        assertEquals(emptyList(), alarm.components("VALARM"))
        assertSame(calendar.components[1], calendar.components("X-vendor").single())
    }

    @Test
    fun knownNamesAreInterned() {
        val first = ICal.parse(invite).components.single()
        val second = ICal.parse(invite.lowercase()).components.single()
        assertSame(first.name, second.name)
        assertSame(first.properties[0].name, second.properties[0].name)
        assertEquals("VCALENDAR", second.name)
    }

    @Test
    fun valuesAreDecodedOnceAndCached() {
        val property = ICal.parse(invite).components[0].components[0].property("ATTENDEE")!!
        assertSame(property.value, property.value)
        assertSame(property.parameters[0].values, property.parameters[0].values)
        assertSame(property.parameters[0], property.parameter("CN"))
    }

    @Test
    fun spansPointIntoTheInput() {
        val text = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1", "END:VEVENT", "END:VCALENDAR")
        val calendar = ICal.parse(text).components.single()
        assertEquals(SourceSpan(0, text.length.toLong(), 1, 1), calendar.span)
        val event = calendar.components.single()
        assertEquals(SourceSpan(17, 17 + 14 + 7 + 12L, 2, 1), event.span)
        assertEquals(SourceSpan(31, 36, 3, 1), event.properties.single().span)
    }

    @Test
    fun foldedLinesKeepTheirSourceAndDecodeLikeUnfolded() {
        val folded = crlf(
            "BEGIN:VEVENT",
            "SUMM",
            " ARY;X-P",
            " =\"a,",
            " b\";X-C=^",
            " n:Ле",
            "\tкция ",
            " 5",
            "END:VEVENT",
        ).insertAtByte(0, "")
        val flat = crlf("BEGIN:VEVENT", "SUMMARY;X-P=\"a,b\";X-C=^n:Лекция 5", "END:VEVENT")
        val foldedEvent = ICal.parse(folded).components.single()
        val flatEvent = ICal.parse(flat).components.single()
        val property = foldedEvent.properties.single()
        assertEquals("SUMMARY", property.name)
        assertEquals("Лекция 5", property.value)
        assertEquals(listOf("a,b"), property.parameter("X-P")!!.values)
        assertEquals("\n", property.parameter("X-C")!!.value)
        assertEquals(flatEvent, foldedEvent)
        val span = property.span!!
        assertEquals(14L, span.startOffset)
        assertEquals(2, span.line)
        assertEquals(1, span.column)
        assertEquals(folded.size - 12L - 2, span.endOffset)
    }

    @Test
    fun invalidUtf8IsReplacedInTheTree() {
        val bytes = crlf("BEGIN:X", "SUMMARY:Лекция a", "END:X").insertAtByte(31, "\u0000").also {
            it[31] =
                0xC3.toByte()
        }
        val result = ICal.parse(bytes)
        assertEquals("UTF-8", result.charset)
        assertEquals("Лекция a\uFFFD", result.components.single().properties.single().value)
        assertEquals(listOf("ICAL-S006"), result.problems.map { it.code.id })
    }

    @Test
    fun implicitEndsAndStrayLinesStayInTheTree() {
        val text = crlf(
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
        val result = ICal.parse(text)
        assertEquals(
            listOf("ICAL-S021", "ICAL-S016", "ICAL-S017", "ICAL-S018"),
            result.problems.map { it.code.id },
        )
        val calendar = result.components.single()
        assertEquals(listOf("1", "2"), calendar.components("VEVENT").map { it.property("UID")!!.value })
        assertEquals("AUDIO", calendar.components[0].components.single().property("ACTION")!!.value)
        assertEquals(text.length.toLong(), calendar.span!!.endOffset)
        assertEquals(1, result.problemCount(ProblemCode.PropertyOutsideComponent))
    }

    @Test
    fun severalCalendarsFollowEachOther() {
        val one = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY:one", "END:VEVENT", "END:VCALENDAR")
        val result = ICal.parse(one + one.replace("one", "two"))
        assertEquals(
            listOf("one", "two"),
            result.components.map { it.components.single().property("SUMMARY")!!.value },
        )
    }

    @Test
    fun emptyInputGivesAnEmptyResult() {
        val result = ICal.parse(ByteArray(0))
        assertEquals(emptyList(), result.components)
        assertEquals(emptyList(), result.problems)
        assertEquals(0, result.problemCount(ProblemCode.BareLf))
        assertEquals(0, result.problemCount(ProblemCode.of("ICAL-X001")))
    }

    @Test
    fun listenerSeesProblemsAsTheyComeAndCountsIncludeSuppressed() {
        val text = "BEGIN:X\n" + (1..10).joinToString("") { "BROKEN$it\n" } + "END:X\n"
        val seen = ArrayList<Problem>()
        val parser = ICal.parser {
            limits = ParseLimits.builder().maxProblems(3).build()
            problemListener = ProblemListener { seen += it }
        }
        val result = parser.parse(text)
        assertEquals(seen, result.problems)
        assertEquals(
            listOf("ICAL-S003", "ICAL-S007", "ICAL-S007", "ICAL-S013"),
            result.problems.map { it.code.id },
        )
        assertEquals(10, result.problemCount(ProblemCode.MissingColon))
        assertEquals(12, result.problemCount(ProblemCode.BareLf))
        assertEquals(1, result.problemCount(ProblemCode.TooManyProblems))
        assertEquals("X", result.components.single().name)
    }

    @Test
    fun lenientNeverThrowsAndStrictThrowsOnTheFirstError() {
        val text = crlf("BEGIN:VCALENDAR", "VERSION:2.0", "BROKEN", "SUMMARY:after", "END:VCALENDAR").replace(
            "VERSION:2.0\r\n",
            "VERSION:2.0\n",
        )
        val lenient = ICal.parse(text)
        assertEquals(listOf("ICAL-S003", "ICAL-S007"), lenient.problems.map { it.code.id })
        assertEquals("after", lenient.components.single().property("SUMMARY")!!.value)

        val seen = ArrayList<Problem>()
        val strict = ICal.parser(
            ParseOptions.builder().mode(ParseMode.Strict).problemListener { seen += it }.build(),
        )
        val error = assertFailsWith<ICalParseException> { strict.parse(text) }
        assertEquals(listOf("ICAL-S003", "ICAL-S007"), error.problems.map { it.code.id })
        assertEquals(Severity.Error, error.problems.last().severity)
        assertEquals(3, error.problems.last().span.line)
        assertEquals(seen, error.problems)

        val warningsOnly = strict.parse(text.replace("BROKEN\r\n", ""))
        assertEquals(listOf("ICAL-S003"), warningsOnly.problems.map { it.code.id })
    }

    @Test
    fun strictThrowsEvenPastTheProblemLimit() {
        val strict = ICal.parser {
            mode = ParseMode.Strict
            limits = ParseLimits.builder().maxProblems(0).build()
        }
        val error = assertFailsWith<ICalParseException> { strict.parse("BEGIN:X\nBROKEN\nEND:X\n") }
        assertEquals(listOf("ICAL-S013", "ICAL-S007"), error.problems.map { it.code.id })
    }

    @Test
    fun limitsTruncateTheTree() {
        val lines = ICal.parser { limits = ParseLimits.builder().maxLines(4).build() }
            .parse(crlf("BEGIN:A", "P:1", "BEGIN:B", "Q:2", "R:3", "END:B", "END:A"))
        assertTrue(lines.isTruncated)
        assertEquals(listOf("ICAL-S014"), lines.problems.map { it.code.id })
        assertEquals("Q", lines.components.single().components.single().properties.single().name)

        val components = ICal.parser { limits = ParseLimits.builder().maxComponents(2).build() }
            .parse(crlf("BEGIN:A", "BEGIN:B", "END:B", "BEGIN:C", "END:C", "END:A"))
        assertTrue(components.isTruncated)
        assertEquals(listOf("B"), components.components.single().components.map { it.name })

        val deep = ICal.parser { limits = ParseLimits.builder().maxDepth(1).build() }
            .parse(crlf("BEGIN:A", "BEGIN:B", "X:1", "END:B", "Y:2", "END:A"))
        assertFalse(deep.isTruncated)
        assertEquals(1, deep.problemCount(ProblemCode.TooDeep))
        assertEquals(listOf("Y"), deep.components.single().properties.map { it.name })
        assertEquals(emptyList(), deep.components.single().components)
    }

    @Test
    fun limitsAreValidated() {
        val invalid = listOf<ParseLimits.Builder.() -> Unit>(
            { maxLineOctets(0) },
            { maxLineOctets(-1) },
            { maxLines(0) },
            { maxLines(-1) },
            { maxProblems(-1) },
            { maxDepth(0) },
            { maxDepth(-1) },
            { maxComponents(0) },
            { maxComponents(-1) },
            { maxInputOctets(0) },
            { maxInputOctets(-1) },
            { maxTime(Duration.ZERO) },
            { maxTime((-1).seconds) },
        )
        for (change in invalid) {
            assertFailsWith<IllegalArgumentException> { ParseLimits.builder().apply(change).build() }
        }
        assertEquals(0, ParseLimits.builder().maxProblems(0).build().maxProblems)
        val huge = ParseLimits.builder().maxDepth(Int.MAX_VALUE).build()
        val result = ICal.parser { limits = huge }.parse("BEGIN:A\nBEGIN:B\nEND:B\nEND:A\n")
        assertEquals("B", result.components.single().components.single().name)
        assertEquals(ParseLimits.Default.toString(), ParseLimits.builder().build().toString())
    }

    @Test
    fun rangesAreParsedWithOffsetsFromTheirStart() {
        val text = crlf("BEGIN:X", "SUMMARY:in range", "END:X")
        val bytes = "garbage".encodeToByteArray() + text.encodeToByteArray() + "tail".encodeToByteArray()
        val result = ICal.parser().parse(bytes, 7, text.length)
        val property = result.components.single().properties.single()
        assertEquals("in range", property.value)
        assertEquals(SourceSpan(9, 25, 2, 1), property.span)
        assertFailsWith<IllegalArgumentException> { ICal.parser().parse(bytes, 7, bytes.size) }
    }

    @Test
    fun charsetsAreReportedAndHintsApplyOnlyToBytes() {
        val text = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY:Лекция по алгебре", "END:VEVENT", "END:VCALENDAR")
        val cp1251 = TestCharsets.encode(text, "windows-1251")

        val detected = ICal.parse(cp1251)
        assertEquals("windows-1251", detected.charset)
        assertTrue(detected.charsetConfidence in 90..100, "confidence ${detected.charsetConfidence}")
        assertEquals(listOf("ICAL-S025"), detected.problems.map { it.code.id })
        assertEquals("Лекция по алгебре", detected.components[0].components[0].property("SUMMARY")!!.value)

        val hinted = ICal.parser { charset = "text/calendar; charset=windows-1251" }
        val byHint = hinted.parse(cp1251)
        assertEquals("windows-1251", byHint.charset)
        assertEquals(100, byHint.charsetConfidence)
        assertEquals(listOf("ICAL-S024"), byHint.problems.map { it.code.id })

        val fromString = hinted.parse(text)
        assertEquals("UTF-8", fromString.charset)
        assertEquals(emptyList(), fromString.problems)
        assertEquals("Лекция по алгебре", fromString.components[0].components[0].property("SUMMARY")!!.value)

        val utf16 = TestCharsets.bom("UTF-16LE") + TestCharsets.encode(text, "UTF-16LE")
        val wide = ICal.parse(utf16)
        assertEquals("UTF-16LE", wide.charset)
        assertEquals(detected.components, wide.components)
    }

    @Test
    fun latinAndAsciiLabelsMeanWindows1252() {
        val text = crlf("BEGIN:X", "SUMMARY:Café € “quoted”", "END:X")
        val bytes = TestCharsets.encode(text, "windows-1252")
        for (label in listOf("ISO-8859-1", "latin1", "US-ASCII", "ascii", "windows-1252")) {
            val result = ICal.parser { charset = label }.parse(bytes)
            assertEquals("windows-1252", result.charset, label)
            assertEquals(listOf("ICAL-S024"), result.problems.map { it.code.id }, label)
            assertEquals("Café € “quoted”", result.components.single().properties.single().value, label)
        }
    }

    @Test
    fun stringsAreTextNotBytes() {
        val withBom = ICal.parse("\uFEFFBEGIN:X\r\nEND:X\r\n")
        assertEquals(listOf("ICAL-S001"), withBom.problems.map { it.code.id })
        assertEquals("X", withBom.components.single().name)

        val utf7Lookalike = ICal.parse("+/v8:1\r\nBEGIN:X\r\nEND:X\r\n")
        assertEquals(listOf("ICAL-S008", "ICAL-S021"), utf7Lookalike.problems.map { it.code.id })
        assertEquals("X", utf7Lookalike.components.single().name)

        val broken = ICal.parse("BEGIN:X\r\nSUMMARY:a" + Char(0xD800) + "b" + Char(0xDC00) + "c😀\r\nEND:X\r\n")
        assertEquals("a\uFFFDb\uFFFDc😀", broken.components.single().properties.single().value)
        assertEquals(listOf("ICAL-S006"), broken.problems.map { it.code.id })
        assertEquals(SourceSpan(18, 18, 2, 10), broken.problems.single().span)
    }

    @Test
    fun treesWithoutTheSourceMatchTreesWithIt() {
        val bytes = invite.encodeToByteArray() + crlf("BEGIN:VTODO", "SUMM", " ARY;X-A=\"1", " \":v", "END:VTODO")
        val retained = ICal.parse(bytes)
        val copied = ICal.parser { retainSource = false }.parse(bytes)
        assertEquals(retained.components, copied.components)
        val retainedNodes = flatten(retained.components)
        val copiedNodes = flatten(copied.components)
        assertEquals(retainedNodes.map { it.second }, copiedNodes.map { it.second })
        for (index in retainedNodes.indices) {
            assertEquals(retainedNodes[index].first, copiedNodes[index].first, "node $index")
        }
        val todo = copied.components[1]
        assertEquals("v", todo.property("SUMMARY")!!.value)
        assertEquals("1", todo.property("SUMMARY")!!.parameter("X-A")!!.value)
    }

    @Test
    fun optionsAreBuiltForKotlinAndJava() {
        val listener = ProblemListener { }
        val options = ParseOptions.builder()
            .mode(ParseMode.Strict)
            .limits(ParseLimits.Default)
            .problemListener(listener)
            .retainSource(false)
            .charset("KOI8-R")
            .build()
        assertEquals(ParseMode.Strict, options.mode)
        assertSame(ParseLimits.Default, options.limits)
        assertSame(listener, options.problemListener)
        assertFalse(options.retainSource)
        assertEquals("KOI8-R", options.charset)
        assertSame(options, ICal.parser(options).options)
        assertSame(ParseOptions.Default, ICal.parser().options)
        assertEquals(ParseMode.Lenient, ParseOptions.Default.mode)
        assertTrue(ParseOptions.Default.retainSource)
        assertNull(ParseOptions.Default.charset)
        assertNotNull(ICal.parser().toString())
    }

    private fun flatten(components: List<ICalComponent>): List<Pair<SourceSpan?, String>> {
        val out = ArrayList<Pair<SourceSpan?, String>>()
        val stack = ArrayDeque(components.reversed())
        while (stack.isNotEmpty()) {
            val component = stack.removeLast()
            out += component.span to component.name
            for (property in component.properties) out += property.span to property.name
            for (child in component.components.reversed()) stack.addLast(child)
        }
        return out
    }
}
