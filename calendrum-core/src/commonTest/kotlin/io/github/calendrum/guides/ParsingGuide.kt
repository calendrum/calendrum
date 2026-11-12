@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.Severity
import io.github.calendrum.asByteInput
import io.github.calendrum.parser
import io.github.calendrum.syntax.ContentEventType
import io.github.calendrum.syntax.Fingerprints
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.ParseLimits
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.WriteOptions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ParsingGuide {
    private val bytes = calendarBytes

    @Test
    fun theTree() {
        val text = calendarText
        // region parsing-tree
        val result = ICal.parse(text)
        val calendar = result.components.single() // the VCALENDAR; the tree keeps the file as it was
        val event = calendar.components("VEVENT").first()
        val summary = event.property("SUMMARY")?.value // "Weekly sync"
        val cn = event.properties("ATTENDEE").map { it.parameter("CN")?.value } // ["Jane", "Bob"]
        val sameContent = event == ICal.parse(text).components.single().components("VEVENT").first() // true
        // endregion
        assertEquals("Weekly sync", summary)
        assertEquals("Weekly sync", event.property("summary")?.value)
        assertEquals(listOf("Jane", "Bob"), cn)
        assertTrue(sameContent)
    }

    @Test
    fun problems() {
        // region parsing-problems
        val result = ICal.parse("BEGIN:VCALENDAR\nVERSION:2.0\nBROKEN\nEND:VCALENDAR\n")
        for (problem in result.problems) {
            val at = "${problem.span.line}:${problem.span.column}"
            println("${problem.code.id} ${problem.severity} at $at: ${problem.message}")
        }
        val bareLf = result.problemCount(ProblemCode.BareLf) // counts include cases beyond the reporting limit
        val worst = result.problems.maxOf { it.severity } // Severity.Error (ICAL-S007)
        // endregion
        assertEquals(listOf("ICAL-S003", "ICAL-S007"), result.problems.map { it.code.id })
        assertEquals(4, bareLf)
        assertEquals(Severity.Error, worst)
    }

    @Test
    fun strictMode() {
        val reported = ArrayList<String>()
        // region parsing-strict
        val strict = ICal.parser {
            mode = ParseMode.Strict
            problemListener = ProblemListener { reported += it.code.id } // also called before the exception
        }
        val message = try {
            strict.parse("BEGIN:VCALENDAR\r\nBROKEN\r\nEND:VCALENDAR\r\n")
            null
        } catch (e: ICalParseException) {
            e.problems.last().code.id // the first Error: "ICAL-S007"
        }
        // endregion
        assertEquals("ICAL-S007", message)
        assertEquals(listOf("ICAL-S007"), reported)
        assertFailsWith<ICalParseException> { strict.parse("BROKEN\r\n") }
    }

    @Test
    fun charsets() {
        // "Привет" in windows-1251: the bytes are not valid UTF-8
        val privet = intArrayOf(0xCF, 0xF0, 0xE8, 0xE2, 0xE5, 0xF2).map { it.toByte() }.toByteArray()
        val legacy = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nSUMMARY:".encodeToByteArray() + privet +
            "\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        // region parsing-charset
        val hinted = ICal.parser { charset = "text/calendar; charset=windows-1251" }.parse(legacy) // a hint, checked
        val detected = ICal.parse(legacy) // no hint: detected from the bytes
        val event = hinted.components.single().components("VEVENT").single()
        val summary = event.property("SUMMARY")?.value // "Привет"
        val charset = detected.charset // "windows-1251"
        // endregion
        assertEquals("Привет", summary)
        assertEquals("windows-1251", charset)
        assertEquals("windows-1251", hinted.charset)
        val detectedEvent = detected.components.single().components("VEVENT").single()
        assertEquals("Привет", detectedEvent.property("SUMMARY")?.value)
    }

    @Test
    fun limits() {
        // region parsing-limits
        val limits = ParseLimits.builder()
            .maxLineOctets(64 * 1024)
            .maxComponents(100_000)
            .maxTime(2.seconds) // a budget for hostile input; ICAL-S029 when it runs out
            .build()
        val result = ICal.parser { this.limits = limits }.parse(bytes)
        // depth 8, 64 KiB lines, 100k components, 2 s
        val untrusted = ICal.parser(ParseOptions.Untrusted).parse(bytes)
        // endregion
        assertTrue(result.problems.isEmpty() && untrusted.problems.isEmpty())
        assertEquals(2.seconds, ParseOptions.Untrusted.limits.maxTime)
    }

    @Test
    fun streams() {
        val seen = ArrayList<String>()
        // region parsing-stream
        val events = ICal.parser().components(bytes.asByteInput()) // any ByteInput: a file, a socket, a response body
        for (component in events) { // children of each VCALENDAR, one at a time
            if (component.name == "VEVENT") seen += component.property("UID")?.value ?: continue
        }
        val calendarProperties = events.calendarProperties // VERSION, PRODID… of the calendar being read
        val problems = events.problems // what was found on the way
        // endregion
        assertEquals(listOf("42@example.com", "43@example.com"), seen)
        assertEquals(listOf("VERSION", "PRODID"), calendarProperties.map { it.name })
        assertEquals(emptyList(), problems)
    }

    @Test
    fun cursor() {
        val offsets = ArrayList<Long>()
        // region parsing-cursor
        val reader = ICal.parser().reader(bytes)
        var uids = 0
        while (true) {
            when (reader.next() ?: break) {
                // where the event starts
                ContentEventType.Begin -> if (reader.name == "VEVENT") offsets += reader.offset

                // reader.value is a view
                ContentEventType.Property -> if (reader.name == "UID") uids += 1

                ContentEventType.End -> Unit
            }
        }
        // endregion
        assertEquals(2, uids)
        assertEquals(2, offsets.size)
        assertTrue(offsets[0] < offsets[1])
    }

    @Test
    fun writing() {
        // region parsing-write
        val result = ICal.parse(bytes)
        val same: ByteArray = ICal.writer().encodeToByteArray(result.components) // the file, byte for byte
        val normalized = ICal.writer(WriteOptions.builder().byteStable(false).timeZonesFirst(true).build())
            .encodeToString(result.components) // canonical lines, VTIMEZONE first
        // endregion
        assertTrue(bytes.contentEquals(same))
        assertTrue(normalized.startsWith("BEGIN:VCALENDAR\r\n"))
    }

    private class FloatingTimeFix : LineInterceptor {
        override fun intercept(line: InterceptedLine) {
            if (line.name == "DTSTART" && line.parameterCount == 0) line.setParameter("TZID", "Europe/Berlin")
        }
    }

    @Test
    fun interceptors() {
        val feed = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nDTSTART:20261001T100000\r\nX-VENDOR-ROOM:Room 101\r\n" +
            "DESCRIPTION:text\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        // region parsing-interceptors
        val fixer = ICal.parser {
            intercept { line -> if (line.name == "X-VENDOR-ROOM") line.rename("LOCATION") }
            intercept { line -> if (line.name == "DESCRIPTION" && line.value.length > 10_000) line.drop() }
            // a factory: a stateful interceptor gets a fresh instance per parse
            intercept { FloatingTimeFix() }
        }
        val event = fixer.parse(feed).components.single().components("VEVENT").single()
        val location = event.property("LOCATION")?.value // "Room 101"
        val zone = event.property("DTSTART")?.parameter("TZID")?.value // "Europe/Berlin"
        // endregion
        assertEquals("Room 101", location)
        assertEquals("Europe/Berlin", zone)
        assertEquals(null, event.property("X-VENDOR-ROOM"))
    }

    @Test
    fun fingerprints() {
        val stored = ICal.fingerprints(bytes).encodeToString()
        val feed = calendarText.replace("SUMMARY:Weekly sync", "SUMMARY:Daily sync").encodeToByteArray()
        // region parsing-fingerprints
        val now = ICal.fingerprints(feed) // one pass over the bytes; DTSTAMP is ignored by default
        val delta = now.diff(Fingerprints.decodeFromString(stored)) // added, removed, changed, unchangedCount
        val changed = delta.changed.map { ICal.parser().parseComponent(feed, it) } // the same subtree as a full parse
        val toStore = now.encodeToString() // a compact, versioned text
        // endregion
        assertEquals(1, delta.changed.size)
        assertEquals("Daily sync", changed.single().property("SUMMARY")?.value)
        assertEquals(now, Fingerprints.decodeFromString(toStore))
    }
}
