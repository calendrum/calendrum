@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InterceptorTest {
    private val feed = crlf(
        "BEGIN:VCALENDAR",
        "PRODID:-//Example//Feed//EN",
        "X-WR-TIMEZONE:Europe/Moscow",
        "BEGIN:VEVENT",
        "UID:1",
        "x-vendor-room;X-FLOOR=3:Room 101",
        "DTSTART:20260101T100000",
        "DESCRIPTION:Long text",
        "attendee;cn=\"Jane\";ROLE=REQ-PARTICIPANT;CN=Twice:mailto:jane@example.com",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "END:VALARM",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private val vendorRoom = LineInterceptor { line -> if (line.name == "X-VENDOR-ROOM") line.rename("LOCATION") }

    private fun event(result: ParseResult): ICalComponent = result.components.single().components.single()

    @Test
    fun renameMovesAVendorProperty() {
        val event = event(ICal.parser { intercept(vendorRoom) }.parse(feed))
        assertNull(event.property("X-VENDOR-ROOM"))
        val location = event.property("LOCATION")!!
        assertEquals("Room 101", location.value)
        assertEquals(listOf("X-FLOOR"), location.parameters.map { it.name })
        assertEquals(listOf("3"), location.parameter("X-FLOOR")!!.values)
        assertEquals(
            listOf("UID", "LOCATION", "DTSTART", "DESCRIPTION", "ATTENDEE"),
            event.properties.map { it.name },
        )
    }

    @Test
    fun dropRemovesTheLine() {
        val parser = ICal.parser { intercept { line -> if (line.name == "DESCRIPTION") line.drop() } }
        val event = event(parser.parse(feed))
        assertNull(event.property("DESCRIPTION"))
        assertEquals(listOf("UID", "X-VENDOR-ROOM", "DTSTART", "ATTENDEE"), event.properties.map { it.name })
        assertEquals(1, event.components.size)
    }

    @Test
    fun parametersAndValuesCanBeEdited() {
        val parser = ICal.parser {
            intercept { line ->
                if (line.name == "ATTENDEE") {
                    line.setParameter("cn", "Jane Doe")
                    line.removeParameter("role")
                    line.setParameter("PARTSTAT", "ACCEPTED", "TENTATIVE")
                    line.setValue("mailto:doe@example.com")
                }
            }
        }
        val attendee = event(parser.parse(feed)).property("ATTENDEE")!!
        assertEquals(listOf("CN", "PARTSTAT"), attendee.parameters.map { it.name })
        assertEquals(listOf("Jane Doe"), attendee.parameter("CN")!!.values)
        assertEquals(listOf("ACCEPTED", "TENTATIVE"), attendee.parameter("PARTSTAT")!!.values)
        assertEquals("mailto:doe@example.com", attendee.value)
    }

    @Test
    fun removingAMissingParameterLeavesTheLineUntouched() {
        var edited = 0
        val parser = ICal.parser {
            intercept { line ->
                line.removeParameter("X-MISSING")
                if (line.name == "ATTENDEE" && line.parameterCount == 3) edited++
            }
        }
        val result = parser.parse(feed)
        assertEquals(1, edited)
        assertEquals(feed, ICal.writer().encodeToString(result.components))
    }

    @Test
    fun interceptorsKeepStateWithinOneParse() {
        var created = 0
        val parser = ICal.parser {
            intercept {
                created++
                object : LineInterceptor {
                    private var zone: String? = null

                    override fun intercept(line: InterceptedLine) {
                        if (line.name == "X-WR-TIMEZONE") zone = line.value.toString()
                        val current = zone ?: return
                        if (line.name == "DTSTART" && line.parameterCount == 0) line.setParameter("TZID", current)
                    }
                }
            }
        }
        val start = event(parser.parse(feed)).property("DTSTART")!!
        assertEquals("Europe/Moscow", start.parameter("TZID")!!.value)
        val withoutZone = feed.replace("X-WR-TIMEZONE:Europe/Moscow\r\n", "")
        assertNull(event(parser.parse(withoutZone)).property("DTSTART")!!.parameter("TZID"))
        assertEquals(2, created)
        parser.reader(feed)
        parser.components(feed.encodeToByteArray())
        assertEquals(4, created)
    }

    @Test
    fun chainRunsInOrderAndStopsAfterDrop() {
        val seenSecond = ArrayList<String>()
        val seenLast = ArrayList<String>()
        val parser = ICal.parser {
            intercept(vendorRoom)
            intercept { line -> seenSecond += line.name }
            intercept { line -> if (line.name == "DESCRIPTION") line.drop() }
            intercept { line -> seenLast += line.name }
        }
        parser.parse(feed)
        val all = listOf(
            "PRODID",
            "X-WR-TIMEZONE",
            "UID",
            "LOCATION",
            "DTSTART",
            "DESCRIPTION",
            "ATTENDEE",
            "ACTION",
        )
        assertEquals(all, seenSecond)
        assertEquals(all - "DESCRIPTION", seenLast)
    }

    @Test
    fun interceptorsSeePropertyLinesWithTheirContext() {
        val seen = ArrayList<String>()
        val input = feed.replace("BEGIN:VALARM", "BEGIN:x-alarm").replace("END:VALARM", "END:X-ALARM")
        ICal.parser { intercept { line -> seen += "${line.depth} ${line.componentName} ${line.name}" } }.parse(input)
        assertEquals(
            listOf(
                "1 VCALENDAR PRODID",
                "1 VCALENDAR X-WR-TIMEZONE",
                "2 VEVENT UID",
                "2 VEVENT X-VENDOR-ROOM",
                "2 VEVENT DTSTART",
                "2 VEVENT DESCRIPTION",
                "2 VEVENT ATTENDEE",
                "3 X-ALARM ACTION",
            ),
            seen,
        )
    }

    @Test
    fun propertiesOutsideComponentsAreNotIntercepted() {
        val seen = ArrayList<String>()
        ICal.parser { intercept { line -> seen += line.name } }.parse("X-STRAY:1\r\n$feed")
        assertTrue("X-STRAY" !in seen)
    }

    @Test
    fun theLineReadsLikeTheCursorAndReflectsEdits() {
        val seen = ArrayList<String>()
        val parser = ICal.parser {
            intercept { line ->
                if (line.name != "ATTENDEE") return@intercept
                seen += describe(line)
                line.rename("x-attendee")
                line.setParameter("X-NEW", "a,b", "c")
                seen += describe(line)
            }
        }
        parser.parse(feed)
        assertEquals(
            listOf(
                "ATTENDEE CN=[Jane] ROLE=[REQ-PARTICIPANT] CN=[Twice] :mailto:jane@example.com",
                "X-ATTENDEE CN=[Jane] ROLE=[REQ-PARTICIPANT] CN=[Twice] X-NEW=[a,b, c] :mailto:jane@example.com",
            ),
            seen,
        )
    }

    @Test
    fun setParameterReplacesTheFirstAndRemovesTheRest() {
        val parser = ICal.parser { intercept { line -> if (line.name == "ATTENDEE") line.setParameter("CN", "One") } }
        val attendee = event(parser.parse(feed)).property("ATTENDEE")!!
        assertEquals(
            listOf("CN" to listOf("One"), "ROLE" to listOf("REQ-PARTICIPANT")),
            attendee.parameters.map { it.name to it.values },
        )
    }

    @Test
    fun editsCheckNamesAndValuesLikeBuilders() {
        val edits = listOf<(InterceptedLine) -> Unit>(
            { it.rename("BAD NAME") },
            { it.rename("begin") },
            { it.rename("") },
            { it.setParameter("A B", "x") },
            { it.setParameter("X-EMPTY") },
            { it.setValue("line\nbreak") },
        )
        for (edit in edits) {
            assertFailsWith<IllegalArgumentException> {
                ICal.parser { intercept { line -> if (line.name == "UID") edit(line) } }.parse(feed)
            }
        }
    }

    @Test
    fun treeCursorAndStreamAgree() {
        val options = ParseOptions.builder()
            .intercept { vendorRoom }
            .intercept { line -> if (line.name == "DESCRIPTION") line.drop() }
            .intercept { line -> if (line.name == "ATTENDEE") line.setParameter("CN", "Changed") }
            .intercept { line -> if (line.name == "ACTION") line.setValue("AUDIO") }
            .build()
        val input = feed.encodeToByteArray()
        assertStreamEquivalent(input, options, what = "intercepted")
        val parser = ICal.parser(options)
        val tree = parser.parse(input).components.single()
        val fromTree = ArrayList<String>()
        val stack = ArrayList<ICalComponent>()
        stack += tree
        while (stack.isNotEmpty()) {
            val node = stack.removeAt(stack.lastIndex)
            for (property in node.properties) fromTree += render(property)
            stack.addAll(node.components.asReversed())
        }
        val reader = parser.reader(input)
        val fromCursor = ArrayList<String>()
        while (true) {
            if ((reader.next() ?: break) != ContentEventType.Property) continue
            fromCursor += buildString {
                append(reader.name)
                for (index in 0 until reader.parameterCount) {
                    append(';').append(reader.parameterName(index)).append('=').append(reader.parameterValues(index))
                }
                append(':').append(reader.value).append('|').append(reader.valueString())
            }
        }
        assertEquals(fromTree.map { it + "|" + it.substringAfter(':') }, fromCursor)
        val streamed = parser.components(input).asSequence().toList()
        assertEquals(tree.components, streamed)
        assertEquals(event(parser.parse(input)), parser.reader(input).firstEvent())
    }

    @Test
    fun interceptedTreeEqualsTheTreeWithTheSameEdits() {
        val options = ParseOptions.builder()
            .intercept(vendorRoom)
            .intercept { line -> if (line.name == "DESCRIPTION") line.drop() }
            .build()
        val edited = feed.replace("x-vendor-room;X-FLOOR=3:Room 101", "LOCATION;X-FLOOR=3:Room 101")
            .replace("DESCRIPTION:Long text\r\n", "")
        assertEquals(ICal.parse(edited).components, ICal.parser(options).parse(feed).components)
    }

    @Test
    fun byteStableWriteChangesOnlyTheEditedLines() {
        val folded = "DESCRIPTION:" + "x".repeat(70) + "\r\n " + "y".repeat(20)
        val input = feed.replace("DESCRIPTION:Long text", folded)
        val result = ICal.parser { intercept(vendorRoom) }.parse(input)
        val written = ICal.writer().encodeToString(result.components)
        assertEquals(input.replace("x-vendor-room;X-FLOOR=3:Room 101", "LOCATION;X-FLOOR=3:Room 101"), written)
        val location = event(result).property("LOCATION")!!
        val original = event(ICal.parse(input)).property("X-VENDOR-ROOM")!!
        assertEquals(original.span!!.startOffset, location.span!!.startOffset)
        assertEquals(original.span!!.endOffset, location.span!!.endOffset)
        assertEquals(original.span!!.line, location.span!!.line)
    }

    @Test
    fun anEmptyInterceptorChangesNothing() {
        val input = feed.encodeToByteArray()
        val plain = ICal.parser()
        val intercepted = ICal.parser { intercept { _ -> } }
        assertEquals(renderParse { plain.parse(input) }, renderParse { intercepted.parse(input) })
        assertEquals(renderEvents(plain.reader(input), true), renderEvents(intercepted.reader(input), true))
        assertEquals(renderComponents(plain.components(input)), renderComponents(intercepted.components(input)))
        assertEquals(feed, ICal.writer().encodeToString(intercepted.parse(input).components))
    }

    @Test
    fun writeInterceptorsSeeTreeLinesBeforeWriting() {
        val tree = ICal.parse(feed).components
        val seen = ArrayList<String>()
        val options = WriteOptions.builder()
            .intercept { line -> seen += "${line.depth} ${line.componentName} ${line.name}" }
            .intercept { line -> if (line.name.startsWith("X-")) line.drop() }
            .intercept { line -> if (line.name == "UID") line.setValue("2") }
            .build()
        val written = ICal.writer(options).encodeToString(tree)
        val expected = feed.replace("X-WR-TIMEZONE:Europe/Moscow\r\n", "")
            .replace("x-vendor-room;X-FLOOR=3:Room 101\r\n", "")
            .replace("UID:1", "UID:2")
        assertEquals(expected, written)
        assertEquals(8, seen.size)
        assertEquals("3 VALARM ACTION", seen.last())
        assertEquals(feed, ICal.writer().encodeToString(tree))
        val built = ICalComponent("VEVENT") { property("X-A", "1") }
        assertEquals("BEGIN:VEVENT\r\nEND:VEVENT\r\n", ICal.writer(options).encodeToString(built))
        val bytes = ICal.writer(options).encodeToByteArray(tree)
        assertEquals(expected, bytes.decodeToString())
    }

    @Test
    fun writeInterceptorsAreCreatedPerWrite() {
        var created = 0
        val options = WriteOptions.builder().intercept {
            created++
            LineInterceptor { }
        }.build()
        val writer = ICal.writer(options)
        val tree = ICal.parse(feed).components
        writer.encodeToString(tree)
        writer.encodeToByteArray(tree)
        assertEquals(2, created)
    }

    @Test
    fun theLineIsOnlyAvailableDuringTheCall() {
        var kept: InterceptedLine? = null
        ICal.parser { intercept { line -> kept = line } }.parse(feed)
        val line = kept!!
        assertFailsWith<IllegalStateException> { line.name }
        assertFailsWith<IllegalStateException> { line.drop() }
        assertEquals("InterceptedLine(none)", line.toString())
    }

    @Test
    fun exceptionsFromAnInterceptorPropagate() {
        val parser = ICal.parser {
            intercept { line -> if (line.name == "UID") throw UnsupportedOperationException("no") }
        }
        assertFailsWith<UnsupportedOperationException> { parser.parse(feed) }
    }

    @Test
    fun theLineIsInactiveAfterAnInterceptorThrows() {
        var kept: InterceptedLine? = null
        val failing = LineInterceptor { line ->
            kept = line
            if (line.name == "UID") throw UnsupportedOperationException("no")
        }
        val tree = ICal.parse(feed).components
        val writer = ICal.writer(WriteOptions.builder().intercept(failing).build())
        assertFailsWith<UnsupportedOperationException> { writer.encodeToString(tree) }
        assertFailsWith<IllegalStateException> { kept!!.name }
        kept = null
        assertFailsWith<UnsupportedOperationException> { ICal.parser { intercept(failing) }.parse(feed) }
        assertFailsWith<IllegalStateException> { kept!!.name }
        kept = null
        val reader = ICal.parser { intercept(failing) }.reader(feed.encodeToByteArray())
        assertFailsWith<UnsupportedOperationException> { while (reader.next() != null) Unit }
        assertEquals("InterceptedLine(none)", kept.toString())
        kept = null
        val content = ContentReader(feed.encodeToByteArray(), ContentSelection.Full, ParseLimits.Default) { }
        content.intercept(LineInterceptor { }, failing)
        assertFailsWith<UnsupportedOperationException> { while (content.next() != ContentReader.END_OF_INPUT) Unit }
        assertFailsWith<IllegalStateException> { kept!!.drop() }
    }

    @Test
    fun optionsListTheirInterceptors() {
        val options = ParseOptions.builder().intercept(vendorRoom).intercept { vendorRoom }.build()
        assertEquals(2, options.interceptors.size)
        assertTrue(options.toString().endsWith("interceptors=2, selection=full, registry=default)"))
        assertEquals(0, ParseOptions.Default.interceptors.size)
        assertEquals(0, WriteOptions.Default.interceptors.size)
    }

    @Test
    fun aLambdaWithoutParametersRegistersAFactory() {
        val event = event(ICal.parser { intercept { vendorRoom } }.parse(feed))
        assertEquals("Room 101", event.property("LOCATION")!!.value)
    }

    private fun describe(line: InterceptedLine): String = buildString {
        append(line.name).append(' ')
        for (index in 0 until line.parameterCount) {
            append(line.parameterName(index)).append('=').append(line.parameterValues(index)).append(' ')
        }
        append(':').append(line.value)
    }

    private fun render(property: ICalProperty): String = buildString {
        append(property.name)
        for (parameter in property.parameters) append(';').append(parameter.name).append('=').append(parameter.values)
        append(':').append(property.value)
    }

    private fun ICalReader.firstEvent(): ICalComponent {
        while (true) {
            if (next() == ContentEventType.Begin && name == "VEVENT") return readComponent()
        }
    }
}
