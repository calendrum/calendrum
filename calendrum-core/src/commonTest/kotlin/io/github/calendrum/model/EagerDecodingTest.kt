package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.parser
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class EagerDecodingTest {
    private val feed = listOf(
        "BEGIN:VCALENDAR",
        "METHOD:REQUEST",
        "BEGIN:VEVENT",
        "UID:1",
        "DTSTART:20260901",
        "DTSTAMP:20260901T100000",
        "X-VENDOR:20261301",
        "BEGIN:VALARM",
        "TRIGGER:19980403T120000Z",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:X-THING",
        "DTSTART:garbage",
        "END:X-THING",
        "END:VCALENDAR",
    ).joinToString("\r\n", postfix = "\r\n")

    private fun codes(problems: List<Problem>): List<String> = problems.map(::describe)

    @Test
    fun lazyByDefault() {
        val result = ICal.parse(feed)
        assertEquals(emptyList(), result.problems)
        assertEquals(0, result.problemCount(ProblemCode.DateAsDateTime))
        assertFalse(ParseOptions.Default.decodeEagerly)
        assertFalse(ParseOptions.builder().build().decodeEagerly)
    }

    @Test
    fun eagerDecodingReportsValueProblemsInDocumentOrder() {
        val heard = ArrayList<Problem>()
        val parser = ICal.parser {
            decodeEagerly = true
            problemListener = ProblemListener { heard += it }
        }
        assertTrue(parser.options.decodeEagerly)
        val result = parser.parse(feed)
        val expected = listOf("ICAL-V008@5:17", "ICAL-V027@6:9", "ICAL-V026@9:9")
        assertEquals(expected, codes(result.problems))
        assertEquals(expected, codes(heard))
        assertEquals(1, result.problemCount(ProblemCode.DateAsDateTime))
        assertEquals(1, result.problemCount(ProblemCode.NotUtc))
        assertEquals(1, result.problemCount(ProblemCode.ValueTypeNotDeclared))
        assertEquals(0, result.problemCount(ProblemCode.InvalidValue))
        assertEquals(0, result.problemCount(ProblemCode.of("ICAL-V099")))
        val event = result.calendars.single().events.single()
        val start = event.component.properties("DTSTART").single()
        assertSame(
            start.decodeWith(Properties.DTSTART.codec, DecodeContext.Lenient),
            event.component.decode(Properties.DTSTART),
        )
        assertIs<ICalDateTime.Date>(event.start)
        assertTrue(ParseOptions.builder().decodeEagerly(true).build().toString().contains("decodeEagerly=true"))
    }

    @Test
    fun strictModeDecodesEagerlyAndThrowsAtTheFirstValueError() {
        val options = ParseOptions.builder().mode(ParseMode.Strict).build()
        assertTrue(options.decodeEagerly)
        val failure = assertFailsWith<ICalParseException> { ICal.parser(options).parse(feed) }
        assertEquals(listOf("ICAL-V008@5:17", "ICAL-V001@5:17"), codes(failure.problems))
        assertSame(ProblemCode.InvalidValue, failure.problems.last().code)
        val clean = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nDTSTART:20260901T100000Z\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val result = ICal.parser(options).parse(clean)
        assertEquals(emptyList(), result.problems)
        val leap = "BEGIN:VEVENT\r\nDTSTART:20261231T235960Z\r\nEND:VEVENT\r\n"
        assertEquals(listOf("ICAL-V004@2:22"), codes(ICal.parser(options).parse(leap).problems))
        val strictSettingAfterLenient = ICal.parser {
            mode = ParseMode.Strict
            decodeEagerly = false
        }
        assertTrue(strictSettingAfterLenient.options.decodeEagerly)
        val lenientAgain = ICal.parser {
            mode = ParseMode.Strict
            mode = ParseMode.Lenient
        }
        assertFalse(lenientAgain.options.decodeEagerly)
    }

    @Test
    fun streamsAndTheCursorDecodeTheirComponents() {
        val eager = ICal.parser { decodeEagerly = true }
        val stream = eager.components(feed.encodeToByteArray())
        val names = stream.asSequence().map { it.name }.toList()
        assertEquals(listOf("VEVENT", "X-THING"), names)
        assertEquals(listOf("ICAL-V008@5:17", "ICAL-V027@6:9", "ICAL-V026@9:9"), codes(stream.problems))
        val method = "BEGIN:VCALENDAR\r\nMETHOD:\r\nBEGIN:VEVENT\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val calendar = eager.components(method.encodeToByteArray())
        calendar.asSequence().toList()
        assertEquals(listOf("ICAL-V001@2:8"), codes(calendar.problems))
        val strict = ICal.parser { mode = ParseMode.Strict }
        val reader = strict.reader(feed)
        assertSame(ContentEventType.Begin, reader.next())
        assertSame(ContentEventType.Property, reader.next())
        assertSame(ContentEventType.Begin, reader.next())
        assertFailsWith<ICalParseException> { reader.readComponent() }
    }

    @Test
    fun parseComponentDecodesToo() {
        val bytes = feed.encodeToByteArray()
        val fingerprint = ICal.fingerprints(bytes).entries.first()
        val heard = ArrayList<Problem>()
        val parser = ICal.parser {
            decodeEagerly = true
            problemListener = ProblemListener { heard += it }
        }
        parser.parseComponent(bytes, fingerprint)
        assertEquals(listOf("ICAL-V008@5:17", "ICAL-V027@6:9", "ICAL-V026@9:9"), codes(heard))
    }

    @Test
    fun valueProblemsShareTheProblemLimit() {
        val lines = (1..5).joinToString("") { "DTSTAMP:20260901T10000$it\r\n" }
        val text = "BEGIN:VEVENT\r\n$lines" + "END:VEVENT\r\n"
        val parser = ICal.parser {
            decodeEagerly = true
            limits = ParseLimits.builder().maxProblems(3).build()
        }
        val result = parser.parse(text)
        assertEquals(
            listOf("ICAL-V027@2:9", "ICAL-V027@3:9", "ICAL-V027@4:9", "ICAL-S013@5:9"),
            codes(result.problems),
        )
        assertEquals(5, result.problemCount(ProblemCode.NotUtc))
        assertEquals(1, result.problemCount(ProblemCode.TooManyProblems))
    }
}
