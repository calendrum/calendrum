package io.github.calendrum.syntax

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import io.github.calendrum.syntax.ContentReader.Companion.BEGIN
import io.github.calendrum.syntax.ContentReader.Companion.END
import io.github.calendrum.syntax.ContentReader.Companion.END_OF_INPUT
import io.github.calendrum.syntax.ContentReader.Companion.PROPERTY
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContentReaderTest {
    private val calendar = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "BEGIN:VEVENT",
        "UID:1",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "END:VALARM",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private fun ReadResult.outline(): List<String> = events.map { event ->
        when (event.kind) {
            BEGIN -> "B${event.depth}:${event.componentName}"
            END -> (if (event.implicit) "I" else "E") + "${event.depth}:${event.componentName}"
            else -> "P${event.depth}:${event.property!!.name}"
        }
    }

    @Test
    fun eventsFollowTheStructure() {
        val result = read(calendar)
        assertEquals(emptyList(), result.problems)
        assertEquals(
            listOf(
                "B1:VCALENDAR", "P1:VERSION", "B2:VEVENT", "P2:UID", "B3:VALARM", "P3:ACTION", "E3:VALARM",
                "E2:VEVENT", "E1:VCALENDAR",
            ),
            result.outline(),
        )
        val event = result.events[2]
        assertEquals(KnownNames.lookup("VEVENT"), event.componentId)
        assertEquals(calendar.indexOf("BEGIN:VEVENT"), event.offset)
        assertEquals(3, event.line)
        assertEquals(1, event.column)
        val version = result.events[1]
        assertEquals(KnownNames.lookup("VERSION"), version.nameId)
        assertEquals("2.0", version.property!!.value)
    }

    @Test
    fun componentsKnowWhereTheyStartAndEnd() {
        val result = read(calendar)
        for ((begin, end) in listOf(0 to 8, 2 to 7, 4 to 6)) {
            val opening = result.events[begin]
            val closing = result.events[end]
            assertEquals(opening.componentStart, closing.componentStart)
            assertEquals(-1, opening.componentEnd)
            val text = calendar.substring(closing.componentStart, closing.componentEnd)
            assertTrue(text.startsWith("BEGIN:${opening.componentName}\r\n"), text)
            assertTrue(text.endsWith("END:${opening.componentName}\r\n"), text)
        }
        assertEquals(calendar.length, result.events.last().componentEnd)
    }

    @Test
    fun calendarsMayFollowEachOther() {
        val input = calendar + calendar.replace("UID:1", "UID:2") + calendar
        val result = read(input)
        assertEquals(emptyList(), result.problems)
        assertEquals(3, result.events.count { it.kind == BEGIN && it.componentName == "VCALENDAR" })
        assertEquals(27, result.events.size)
        assertEquals(input.length, result.events.last().componentEnd)
    }

    @Test
    fun endWithoutOpenComponentIsIgnored() {
        val result = read(crlf("END:VEVENT", "BEGIN:VCALENDAR", "END:VCALENDAR", "END:VCALENDAR"))
        assertEquals(listOf("ICAL-S015", "ICAL-S015"), result.problems.map { it.id })
        assertEquals(Severity.Warning, result.problems[0].severity)
        assertEquals("END without an open component, ignored", result.problems[0].message)
        assertEquals(listOf("B1:VCALENDAR", "E1:VCALENDAR"), result.outline())
        assertEquals(1, result.problems[0].line)
        assertEquals(4, result.problems[1].line)
    }

    @Test
    fun endOfAnOuterComponentClosesNestedOnes() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "BEGIN:VALARM",
            "BEGIN:X-INNER",
            "END:VEVENT",
            "UID:after",
            "END:VCALENDAR",
        )
        val result = read(input)
        assertEquals(listOf("ICAL-S016"), result.codes)
        assertEquals(5, result.problems.single().line)
        assertEquals(
            listOf(
                "B1:VCALENDAR", "B2:VEVENT", "B3:VALARM", "B4:X-INNER", "I4:X-INNER", "I3:VALARM", "E2:VEVENT",
                "P1:UID", "E1:VCALENDAR",
            ),
            result.outline(),
        )
        val endLine = input.indexOf("END:VEVENT")
        assertEquals(endLine, result.events[4].componentEnd)
        assertEquals(endLine, result.events[5].componentEnd)
        assertEquals(input.indexOf("UID:after"), result.events[6].componentEnd)
        for (event in result.events.subList(4, 7)) {
            assertEquals(endLine, event.offset)
            assertEquals(5, event.line)
        }
    }

    @Test
    fun endThatMatchesNothingIsIgnored() {
        val input = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "END:VTODO", "END:VEVENT", "END:VCALENDAR")
        val result = read(input)
        assertEquals(listOf("ICAL-S017"), result.codes)
        assertEquals(listOf("B1:VCALENDAR", "B2:VEVENT", "E2:VEVENT", "E1:VCALENDAR"), result.outline())
    }

    @Test
    fun endOfInputClosesOpenComponents() {
        val input = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1")
        val result = read(input)
        assertEquals(listOf("ICAL-S018"), result.codes)
        assertEquals(4, result.problems.single().line)
        assertEquals(input.length, result.problems.single().offset)
        assertEquals(listOf("B1:VCALENDAR", "B2:VEVENT", "P2:UID", "I2:VEVENT", "I1:VCALENDAR"), result.outline())
        assertTrue(result.events.drop(3).all { it.componentEnd == input.length && it.offset == input.length })
        assertEquals(END_OF_INPUT, result.reader.next())
        assertEquals(END_OF_INPUT, result.reader.kind)
    }

    @Test
    fun invalidOrEmptyComponentNamesAreKept() {
        val input = crlf("BEGIN:", "BEGIN:X_BAD", "UID:1", "END:x_bad", "END:", "BEGIN:VEVENT ", "END:VEVENT ")
        val result = read(input)
        assertEquals(List(6) { "ICAL-S019" }, result.codes)
        assertEquals(
            listOf("B1:", "B2:X_BAD", "P2:UID", "E2:X_BAD", "E1:", "B1:VEVENT ", "E1:VEVENT "),
            result.outline(),
        )
        assertTrue(result.events.filter { it.kind != PROPERTY }.all { it.componentId == ContentReader.UNKNOWN_NAME })
    }

    @Test
    fun parametersOnBeginAndEndAreIgnored() {
        val result = read(crlf("BEGIN;X=1:VCALENDAR", "END;LANGUAGE=en:VCALENDAR"))
        assertEquals(listOf("ICAL-S020", "ICAL-S020"), result.codes)
        assertEquals(listOf("B1:VCALENDAR", "E1:VCALENDAR"), result.outline())
    }

    @Test
    fun propertiesOutsideComponentsAreKept() {
        val result = read(crlf("PRODID:x", "BEGIN:VCALENDAR", "END:VCALENDAR", "X-TRAILER:1"))
        assertEquals(listOf("ICAL-S021", "ICAL-S021"), result.codes)
        assertEquals(listOf("P0:PRODID", "B1:VCALENDAR", "E1:VCALENDAR", "P0:X-TRAILER"), result.outline())
        assertEquals(ContentReader.UNKNOWN_NAME, result.events[0].componentId)
    }

    @Test
    fun namesAreCaseInsensitive() {
        val input = crlf("begin:vcalendar", "Begin:X-Vendor", "End:x-VENDOR", "END:VCALENDAR")
        val result = read(input)
        assertEquals(emptyList(), result.problems)
        assertEquals(listOf("B1:VCALENDAR", "B2:X-Vendor", "E2:X-Vendor", "E1:VCALENDAR"), result.outline())
        assertEquals(KnownNames.VCALENDAR, result.events[0].componentId)
    }

    @Test
    fun longUnknownNamesAreComparedInFull() {
        val name = "X-" + "A".repeat(400)
        val other = "X-" + "A".repeat(399) + "B"
        val input = crlf("BEGIN:$name", "END:$other", "END:${name.lowercase()}")
        val result = read(input)
        assertEquals(listOf("ICAL-S017"), result.codes)
        assertEquals(listOf(BEGIN, END), result.kinds)
        assertFalse(result.events[1].implicit)
        assertEquals(name.substring(0, 255), result.events[0].componentName)
    }

    @Test
    fun foldedStructureLinesAreRecognised() {
        val result = read(crlf("BEG", " IN:VCAL", " ENDAR", "E", " ND:VCALENDAR"))
        assertEquals(emptyList(), result.problems)
        assertEquals(listOf("B1:VCALENDAR", "E1:VCALENDAR"), result.outline())
    }

    @Test
    fun subtreesBelowTheDepthLimitAreSkipped() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "BEGIN:VALARM",
            "BEGIN:VLOCATION",
            "BEGIN:X-DEEPER",
            "END:X-DEEPER",
            "NAME:hidden",
            "END:VLOCATION",
            "BEGIN:VLOCATION",
            "END:VLOCATION",
            "ACTION:AUDIO",
            "END:VALARM",
            "UID:1",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val result = read(input.encodeToByteArray(), limits = limits(maxDepth = 3))
        assertEquals(listOf("ICAL-S022", "ICAL-S022"), result.codes)
        assertEquals(listOf(4, 9), result.problems.map { it.line })
        assertEquals(
            listOf(
                "B1:VCALENDAR",
                "B2:VEVENT",
                "B3:VALARM",
                "P3:ACTION",
                "E3:VALARM",
                "P2:UID",
                "E2:VEVENT",
                "E1:VCALENDAR",
            ),
            result.outline(),
        )
        assertEquals(3, result.reader.skippedComponents)
        assertEquals(1, result.reader.skippedLines)
    }

    @Test
    fun depthSkipIsClosedByTheEndOfInput() {
        val input = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "BEGIN:VALARM")
        val result = read(input.encodeToByteArray(), limits = limits(maxDepth = 2))
        assertEquals(listOf("ICAL-S022", "ICAL-S018"), result.codes)
        assertEquals(listOf("B1:VCALENDAR", "B2:VEVENT", "I2:VEVENT", "I1:VCALENDAR"), result.outline())
    }

    @Test
    fun componentLimitStopsReading() {
        val input = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "END:VEVENT", "BEGIN:VTODO", "UID:2", "END:VTODO")
        val result = read(input.encodeToByteArray(), limits = limits(maxComponents = 2, maxProblems = 0))
        assertEquals(listOf("ICAL-S023"), result.codes)
        assertEquals(4, result.problems.single().line)
        assertEquals(listOf("B1:VCALENDAR", "B2:VEVENT", "E2:VEVENT", "I1:VCALENDAR"), result.outline())
        assertEquals(input.indexOf("BEGIN:VTODO"), result.events.last().componentEnd)
        assertEquals(END_OF_INPUT, result.reader.next())
    }

    @Test
    fun componentLimitAtTopLevelEndsCleanly() {
        val input = crlf("BEGIN:VCALENDAR", "END:VCALENDAR", "BEGIN:VCALENDAR", "END:VCALENDAR")
        val result = read(input.encodeToByteArray(), limits = limits(maxComponents = 1))
        assertEquals(listOf("ICAL-S023"), result.codes)
        assertEquals(listOf("B1:VCALENDAR", "E1:VCALENDAR"), result.outline())
    }

    @Test
    fun lineLimitClosesComponentsWithoutAnotherProblem() {
        val input = crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1", "END:VEVENT", "END:VCALENDAR")
        val result = read(input.encodeToByteArray(), limits = limits(maxLines = 3))
        assertEquals(listOf("ICAL-S014"), result.codes)
        assertEquals(listOf("B1:VCALENDAR", "B2:VEVENT", "P2:UID", "I2:VEVENT", "I1:VCALENDAR"), result.outline())
        assertEquals(1, result.reader.problemCount(ProblemCode.TooManyLines))
    }

    @Test
    fun structureProblemsShareTheProblemLimit() {
        val input = crlf("END:A", "END:B", "END:C", "END:D")
        val result = read(input.encodeToByteArray(), limits = limits(maxProblems = 2))
        assertEquals(listOf("ICAL-S015", "ICAL-S015", "ICAL-S013"), result.codes)
        assertEquals(4, result.reader.problemCount(ProblemCode.EndWithoutBegin))
    }

    @Test
    fun rangeInputReportsRelativeOffsets() {
        val bytes = ("garbage" + calendar + "tail").encodeToByteArray()
        val problems = mutableListOf<Problem>()
        val reader = ContentReader(bytes, 7, calendar.length, ContentSelection.Full, ParseLimits.Default) {
            problems += it
        }
        val begins = mutableListOf<Int>()
        while (reader.next() != END_OF_INPUT) if (reader.kind == BEGIN) begins += reader.componentStartOffset.toInt()
        assertEquals(emptyList(), problems)
        assertEquals(listOf(0, calendar.indexOf("BEGIN:VEVENT"), calendar.indexOf("BEGIN:VALARM")), begins)
    }

    @Test
    fun eventsAreBalancedOnRandomInput() {
        val random = TestRandom(5545)
        repeat(3_000) { iteration ->
            val input = randomStructuredInput(random)
            val limits = limits(maxDepth = 1 + random.nextInt(5), maxComponents = 1 + random.nextInt(40))
            val result = read(input, ContentSelection.Full, limits)
            var depth = 0
            for (event in result.events) {
                when (event.kind) {
                    BEGIN -> assertEquals(++depth, event.depth, "input #$iteration")
                    END -> assertEquals(depth--, event.depth, "input #$iteration")
                    else -> assertEquals(depth, event.depth, "input #$iteration")
                }
                assertTrue(depth in 0..limits.maxDepth)
            }
            assertEquals(0, depth, "input #$iteration")
            for (problem in result.problems) assertTrue(problem.offset in 0..input.size)
        }
    }
}

private val structuredPieces = listOf(
    "BEGIN:VCALENDAR", "END:VCALENDAR", "BEGIN:VEVENT", "END:VEVENT", "BEGIN:VALARM", "END:VALARM",
    "BEGIN:X-A", "end:x-a", "BEGIN:VTIMEZONE", "END:VTIMEZONE", "BEGIN:STANDARD", "END:STANDARD",
    "BEGIN;X=1:VTODO", "END:VTODO", "BEGIN:", "END:", "SUMMARY:é", "summary;cn=\"a:b\":x", "X-FOO:bar",
    "DTSTART;TZID=Europe/Berlin:1", "UID:1", "no colon", "BEG\r\n IN:VEVENT", "E\n\tND:VEVENT", "",
    "DESCRIPTION:ÿ", "ATTENDEE;CN=\"unclosed:x",
)

internal fun randomStructuredInput(random: TestRandom): ByteArray {
    val out = ArrayList<Byte>()
    repeat(random.nextInt(40)) {
        when (random.nextInt(12)) {
            0 -> repeat(1 + random.nextInt(4)) { out += random.nextInt(256).toByte() }
            1 -> out += byteArrayOf(0xC3.toByte(), 0x28, 0xED.toByte(), 0xA0.toByte(), 0x80.toByte()).toList()
            else -> out += structuredPieces[random.nextInt(structuredPieces.size)].encodeToByteArray().toList()
        }
        out += when (random.nextInt(6)) {
            0 -> listOf<Byte>(0x0A)
            1 -> listOf<Byte>(0x0D)
            else -> listOf<Byte>(0x0D, 0x0A)
        }
    }
    return out.toByteArray()
}
