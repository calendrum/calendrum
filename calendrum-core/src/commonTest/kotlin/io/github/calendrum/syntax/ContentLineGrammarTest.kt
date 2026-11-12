package io.github.calendrum.syntax

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals

class ContentLineGrammarTest {
    @Test
    fun nameParametersAndValue() {
        val line = lex("DTSTART;TZID=America/New_York;VALUE=DATE-TIME:19980312T083000\r\n").single()
        assertEquals("DTSTART", line.name)
        assertEquals(
            listOf(Param("TZID", listOf("America/New_York")), Param("VALUE", listOf("DATE-TIME"))),
            line.parameters,
        )
        assertEquals("19980312T083000", line.value)
    }

    @Test
    fun valueIsRawAfterTheFirstColon() {
        val line = lex("DESCRIPTION:a\\, b\\nc: d; e=f,\"g\"\r\n").single()
        assertEquals("a\\, b\\nc: d; e=f,\"g\"", line.value)
        assertEquals(emptyList(), line.parameters)
    }

    @Test
    fun emptyValue() {
        val result = lex("X-EMPTY:\r\n")
        assertEquals("", result.single().value)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun quotedValuesMayContainSeparators() {
        val result = lex(
            "ATTENDEE;DELEGATED-TO=\"mailto:jdoe@example.com\",\"mailto:jqpublic@example.com\":mailto:jsmith@example.com",
        )
        val line = result.single()
        assertEquals(
            listOf(Param("DELEGATED-TO", listOf("mailto:jdoe@example.com", "mailto:jqpublic@example.com"))),
            line.parameters,
        )
        assertEquals(listOf(listOf(true, true)), line.quoted)
        assertEquals("mailto:jsmith@example.com", line.value)
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun colonInsideQuotesIsNotTheValueSeparator() {
        val line = lex("DESCRIPTION;ALTREP=\"cid:part1.0001@example.org\":The Fall'98 Wild Wizards\r\n").single()
        assertEquals(listOf(Param("ALTREP", listOf("cid:part1.0001@example.org"))), line.parameters)
        assertEquals("The Fall'98 Wild Wizards", line.value)
    }

    @Test
    fun semicolonCommaAndColonInsideQuotes() {
        val line = lex("X;P=\"a;b,c:d\",e:v").single()
        assertEquals(listOf(Param("P", listOf("a;b,c:d", "e"))), line.parameters)
        assertEquals(listOf(listOf(true, false)), line.quoted)
        assertEquals("v", line.value)
    }

    @Test
    fun severalCommaSeparatedValues() {
        val line = lex(
            "ATTENDEE;MEMBER=\"mailto:a@example.com\",\"mailto:b@example.com\",c:mailto:x@example.com",
        ).single()
        assertEquals(
            listOf(Param("MEMBER", listOf("mailto:a@example.com", "mailto:b@example.com", "c"))),
            line.parameters,
        )
    }

    @Test
    fun emptyParameterValues() {
        val result = lex(crlf("A;X=:v", "B;X=a,,b:v", "C;X=\"\":v", "D;X=a,:v"))
        assertEquals(
            listOf(
                listOf(Param("X", listOf(""))),
                listOf(Param("X", listOf("a", "", "b"))),
                listOf(Param("X", listOf(""))),
                listOf(Param("X", listOf("a", ""))),
            ),
            result.lines.map { it.parameters },
        )
        assertEquals(emptyList(), result.problems)
    }

    @Test
    fun lowerCaseKnownNamesAreInternedAndKeepTheirSpellingInBytes() {
        val bytes = "dtStart;tzid=Europe/Berlin:20260101T100000\r\n".encodeToByteArray()
        val result = lex(bytes)
        val line = result.single()
        assertEquals("DTSTART", line.name)
        assertEquals(listOf(Param("TZID", listOf("Europe/Berlin"))), line.parameters)
        assertEquals(emptyList(), result.problems)
        val raw = ContentLexer(bytes, ParseLimits.Default) { }.next()!!
        assertEquals(KnownNames.lookup("DTSTART"), raw.nameId)
        assertEquals(KnownNames.lookup("TZID"), raw.parameterNameId(0))
        assertEquals("dtStart", raw.bytes.decodeToString(raw.nameStart, raw.nameEnd))
        assertEquals("tzid", raw.bytes.decodeToString(raw.parameterNameStart(0), raw.parameterNameEnd(0)))
    }

    @Test
    fun unknownNamesAreDecodedAsWritten() {
        val bytes = "x-Vendor;x-Param=1:v\r\n".encodeToByteArray()
        val line = ContentLexer(bytes, ParseLimits.Default) { }.next()!!
        assertEquals(KnownNames.UNKNOWN, line.nameId)
        assertEquals(KnownNames.UNKNOWN, line.parameterNameId(0))
        assertEquals("x-Vendor", line.name())
        assertEquals("x-Param", line.parameterName(0))
    }

    @Test
    fun invalidCharacterInPropertyNameIsReportedAndKept() {
        val result = lex("X_VENDOR:1\r\n")
        assertEquals("X_VENDOR", result.single().name)
        val problem = result.problem(ProblemCode.InvalidName)
        assertEquals(1, problem.offset)
        assertEquals(2, problem.column)
    }

    @Test
    fun emptyPropertyNameIsReported() {
        val result = lex(":value\r\n")
        assertEquals("", result.single().name)
        assertEquals("value", result.single().value)
        assertEquals(listOf("ICAL-S008"), result.codes)
    }

    @Test
    fun invalidCharacterInParameterNameIsReported() {
        val result = lex("SUMMARY;X@Y=1:v\r\n")
        assertEquals(listOf(Param("X@Y", listOf("1"))), result.single().parameters)
        assertEquals(listOf("ICAL-S008"), result.codes)
        assertEquals(9, result.problems[0].offset)
    }

    @Test
    fun emptyParameterNameIsReported() {
        val result = lex("SUMMARY;=1:v\r\n")
        assertEquals(listOf(Param("", listOf("1"))), result.single().parameters)
        assertEquals(listOf("ICAL-S008"), result.codes)
    }

    @Test
    fun missingColonSkipsTheLine() {
        val result = lex(crlf("BEGIN:VCALENDAR", "NOCOLON", "END:VCALENDAR"))
        assertEquals(listOf("BEGIN", "END"), result.lines.map { it.name })
        val problem = result.problem(ProblemCode.MissingColon)
        assertEquals(Severity.Error, problem.severity)
        assertEquals(17, problem.offset)
        assertEquals(2, problem.line)
        assertEquals(1, problem.column)
    }

    @Test
    fun colonOnlyInsideQuotesMeansMissingColon() {
        val result = lex("X;P=\"a:b\"\r\nY:1\r\n")
        assertEquals(listOf("Y"), result.lines.map { it.name })
        assertEquals(listOf("ICAL-S007"), result.codes)
    }

    @Test
    fun missingColonAfterParameterWithoutValue() {
        val result = lex("X;P\r\nY;P=1,\r\n")
        assertEquals(emptyList(), result.lines)
        assertEquals(listOf("ICAL-S007", "ICAL-S007"), result.codes)
    }

    @Test
    fun parameterWithoutEqualsSignIsKeptWithoutValues() {
        val result = lex("TEL;WORK;TYPE=voice:+1-555-0100\r\n")
        val line = result.single()
        assertEquals(listOf(Param("WORK", emptyList()), Param("TYPE", listOf("voice"))), line.parameters)
        assertEquals("+1-555-0100", line.value)
        assertEquals(listOf("ICAL-S012"), result.codes)
        assertEquals(4, result.problems[0].offset)
    }

    @Test
    fun unclosedQuoteIsTakenLiterallyUpToTheNextSeparator() {
        val result = lex("ATTENDEE;CN=\"John Smith:mailto:john@example.com\r\n")
        val line = result.single()
        assertEquals(listOf(Param("CN", listOf("\"John Smith"))), line.parameters)
        assertEquals(listOf(listOf(false)), line.quoted)
        assertEquals("mailto:john@example.com", line.value)
        val problem = result.problem(ProblemCode.UnclosedQuote)
        assertEquals(12, problem.offset)
        assertEquals(13, problem.column)
    }

    @Test
    fun unclosedQuoteKeepsFollowingParameters() {
        val line = lex("X;A=\"one,two;B=3:v").single()
        assertEquals(listOf(Param("A", listOf("\"one", "two")), Param("B", listOf("3"))), line.parameters)
        assertEquals("v", line.value)
    }

    @Test
    fun textAfterClosingQuoteIsTakenLiterally() {
        val result = lex("X;CN=\"Jo\"hn;B=1:v")
        val line = result.single()
        assertEquals(listOf(Param("CN", listOf("\"Jo\"hn")), Param("B", listOf("1"))), line.parameters)
        assertEquals(listOf("ICAL-S012"), result.codes)
        assertEquals(9, result.problems[0].offset)
    }

    @Test
    fun quoteInsideUnquotedValueIsReportedAndKept() {
        val result = lex("X;CN=Jo\"hn:v")
        assertEquals(listOf(Param("CN", listOf("Jo\"hn"))), result.single().parameters)
        assertEquals(listOf("ICAL-S012"), result.codes)
    }

    @Test
    fun parameterByteRangesPointIntoTheLine() {
        val bytes = "X;AB=\"cd\",ef:gh".encodeToByteArray()
        val lexer = ContentLexer(bytes, ParseLimits.Default) { }
        val line = lexer.next()!!
        assertEquals(0, line.nameStart)
        assertEquals(1, line.nameEnd)
        assertEquals(2, line.parameterNameStart(0))
        assertEquals(4, line.parameterNameEnd(0))
        assertEquals(6, line.parameterValueStart(0, 0))
        assertEquals(8, line.parameterValueEnd(0, 0))
        assertEquals(10, line.parameterValueStart(0, 1))
        assertEquals(12, line.parameterValueEnd(0, 1))
        assertEquals(13, line.valueStart)
        assertEquals(15, line.valueEnd)
        assertEquals(bytes, line.bytes)
    }
}
