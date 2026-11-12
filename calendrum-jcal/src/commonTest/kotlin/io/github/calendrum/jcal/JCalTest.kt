@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.jcal

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyDefinition
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.Rfc5545
import io.github.calendrum.model.ValueType
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.WriteOptions
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class JCalTest {
    private fun parse(vararg lines: String): List<ICalComponent> =
        ICal.parse(lines.joinToString("") { "$it\r\n" }).components

    private val calendar = parse(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//t//EN",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Berlin",
        "BEGIN:DAYLIGHT",
        "DTSTART:19810329T020000",
        "TZOFFSETFROM:+0100",
        "TZOFFSETTO:+0200",
        "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU",
        "END:DAYLIGHT",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "UID:1",
        "DTSTAMP:20260601T080000Z",
        "DTSTART;TZID=Europe/Berlin:20260601T100000",
        "DTEND;TZID=Europe/Berlin:20260601T113000",
        "RRULE:FREQ=WEEKLY;BYDAY=MO,WE;UNTIL=20260701T080000Z;INTERVAL=1",
        "EXDATE;TZID=Europe/Berlin:20260603T100000,20260610T100000",
        "RDATE;VALUE=PERIOD:20260605T100000Z/20260605T110000Z,20260606T100000Z/PT1H",
        "SUMMARY:Planning\\, review\\; notes\\nline two",
        "CATEGORIES:WORK,PLANNING",
        "GEO:37.386013;-122.082932",
        "PRIORITY:5",
        "REQUEST-STATUS:2.0;Success",
        "ATTENDEE;CN=Bob;ROLE=REQ-PARTICIPANT;DELEGATED-TO=\"mailto:a@example.com\",\"mailto:b@example.com\":mailto:bob@example.com",
        "URL:https://example.com/e/1",
        "ATTACH;VALUE=BINARY;ENCODING=BASE64;FMTTYPE=text/plain:SGVsbG8=",
        "X-FLAG;VALUE=BOOLEAN:TRUE",
        "X-AT;VALUE=TIME:120000,133000Z",
        "X-RATE;VALUE=FLOAT:-1.50",
        "X-DAY;VALUE=DATE:20260601",
        "X-RAW:anything;goes,here",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER;RELATED=END:-PT15M",
        "DURATION:PT5M",
        "REPEAT:2",
        "DESCRIPTION:Soon",
        "END:VALARM",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    @Test
    fun everyValueTypeIsWrittenAndReadBack() {
        val text = JCal.encodeToString(calendar)
        assertEquals(EXPECTED, text)
        val back = JCal.decodeFromString(text)
        assertEquals(emptyList(), back.problems)
        assertEquals(calendar, back.components)
        assertEquals(Json.parseToJsonElement(text), JCal.encodeToJsonElement(back.components))
    }

    @Test
    fun aSingleComponentAndAListAreBothRead() {
        val single = JCal.decodeFromString("""["vcalendar",[["version",{},"text","2.0"]],[]]""")
        assertEquals(emptyList(), single.problems)
        assertEquals(parse("BEGIN:VCALENDAR", "VERSION:2.0", "END:VCALENDAR"), single.components)
        val list = JCal.decodeFromString("""[["vcalendar",[],[]],["vcalendar",[],[]]]""")
        assertEquals(2, list.components.size)
        assertEquals(emptyList(), JCal.decodeFromString("[]").components)
    }

    @Test
    fun malformedInputIsReportedAndSkipped() {
        assertEquals(listOf(ProblemCode.InvalidJson), JCal.decodeFromString("[\"vcalendar\",").problems.map { it.code })
        assertEquals(listOf(ProblemCode.MalformedJCalComponent), codes("""{"a":1}"""))
        assertEquals(listOf(ProblemCode.MalformedJCalComponent), codes("""[["vcalendar",[]]]"""))
        assertEquals(listOf(ProblemCode.MalformedJCalComponent), codes("""[["v calendar",[],[]]]"""))
        assertEquals(listOf(ProblemCode.MalformedJCalProperty), codes("""["x",[["summary",{},"text"]],[]]"""))
        assertEquals(listOf(ProblemCode.MalformedJCalProperty), codes("""["x",[["summary",[],"text","a"]],[]]"""))
        assertEquals(listOf(ProblemCode.MalformedJCalProperty), codes("""["x",[["summary",{},"text",{"a":1}]],[]]"""))
        assertEquals(listOf(ProblemCode.MalformedJCalProperty), codes("""["x",[["summary",{},"text","a\u0001"]],[]]"""))
        val parameter = JCal.decodeFromString(
            """["x",[["summary",{"cn":1,"value":"TEXT","role":["A","B"]},"text","a"]],[]]""",
        )
        assertEquals(List(2) { ProblemCode.MalformedJCalParameter }, parameter.problems.map { it.code })
        assertEquals("ROLE", parameter.components.single().properties.single().parameters.single().name)
        assertEquals("$[1][0][1].cn", parameter.problems[0].message.substringAfter(": "))
        val mismatch = JCal.decodeFromString(
            """["x",[["dtstart",{},"date-time","tomorrow"],["x-n",{},"integer","5"]],[]]""",
        )
        assertEquals(List(2) { ProblemCode.JCalValueMismatch }, mismatch.problems.map { it.code })
        assertEquals(
            listOf("tomorrow", "5"),
            mismatch.components.single().properties.map { it.value },
        )
        val type = JCal.decodeFromString("""["x",[["x-a",{},"x-score","7"],["x-b",{},"no type","8"]],[]]""")
        assertEquals(listOf(ProblemCode.UnknownJCalType), type.problems.map { it.code })
        assertEquals(
            parse("BEGIN:X", "X-A;VALUE=X-SCORE:7", "X-B:8", "END:X"),
            type.components,
        )
    }

    @Test
    fun malformedValuesAreWrittenAsUnknown() {
        val tree = parse(
            "BEGIN:VEVENT",
            "DTSTART:tomorrow",
            "GEO:north",
            "X-N;VALUE=INTEGER:many",
            "SUMMARY;CN=A;CN=B:x",
            "END:VEVENT",
        )
        val text = JCal.encodeToString(tree)
        assertEquals(
            "[[\"vevent\",[[\"dtstart\",{},\"unknown\",\"tomorrow\"],[\"geo\",{},\"unknown\",\"north\"]," +
                "[\"x-n\",{},\"unknown\",\"many\"],[\"summary\",{\"cn\":[\"A\",\"B\"]},\"text\",\"x\"]],[]]]",
            text,
        )
        val back = JCal.decodeFromString(text).components.single().properties
        assertEquals(listOf("tomorrow", "north", "many", "x"), back.map { it.value })
    }

    @Test
    fun theRegistryGivesTypesToExtensions() {
        val score = PropertyDefinition(
            PropertyKey("X-SCORE", Properties.SEQUENCE.codec),
            ValueType.Integer,
            emptySet(),
            emptySet(),
            mapOf("VEVENT" to Cardinality.Optional),
            "test",
        )
        val registry = ICalRegistry.builder().install(Rfc5545).property(score).build()
        val options = JCalOptions.builder().registry(registry).build()
        val tree = parse("BEGIN:VEVENT", "X-SCORE:42", "END:VEVENT")
        val plain = JCal.encodeToString(tree)
        val typed = JCal.encodeToString(tree, options)
        assertEquals("""[["vevent",[["x-score",{},"unknown","42"]],[]]]""", plain)
        assertEquals("""[["vevent",[["x-score",{},"integer",42]],[]]]""", typed)
        assertEquals(tree, JCal.decodeFromString(typed, options).components)
        assertEquals(
            parse("BEGIN:VEVENT", "X-SCORE;VALUE=INTEGER:42", "END:VEVENT"),
            JCal.decodeFromString(typed).components,
        )
        assertTrue(options.toString().startsWith("JCalOptions(registry="))
        assertEquals("JCalOptions.Builder", JCalOptions.builder().toString())
        assertEquals("JCal", JCal.toString())
        assertEquals("JCalResult(1 components, 0 problems)", JCal.decodeFromString(typed).toString())
    }

    @Test
    fun anEmptyListIsAnEmptyArray() {
        assertEquals(JsonArray(emptyList()), JCal.encodeToJsonElement(emptyList()))
    }

    @Test
    fun everyRecurrencePartAndListKeepsItsOrder() {
        val tree = parse(
            "BEGIN:VEVENT",
            "RRULE:FREQ=YEARLY;COUNT=10;BYSECOND=0;BYMINUTE=30;BYHOUR=9;BYMONTHDAY=1,-1;BYYEARDAY=100;BYWEEKNO=20;" +
                "BYSETPOS=-1;WKST=SU;RSCALE=GREGORIAN;SKIP=FORWARD;BYMONTH=2L;X-NAME=foo",
            "DTSTART:20260601",
            "X-N;VALUE=INTEGER:1,-2",
            "X-F;VALUE=FLOAT:1.5,2",
            "X-OFF;VALUE=BOOLEAN:FALSE",
            "X-BAD;VALUE=BOOLEAN:maybe",
            "BEGIN:VALARM",
            "TRIGGER:20260601T080000Z",
            "END:VALARM",
            "END:VEVENT",
        )
        val text = JCal.encodeToString(tree)
        assertEquals(
            """[["vevent",[["rrule",{},"recur",{"freq":"YEARLY","count":10,"bysecond":0,"byminute":30,"byhour":9,""" +
                """"bymonthday":[1,-1],"byyearday":100,"byweekno":20,"bysetpos":-1,"wkst":"SU",""" +
                """"rscale":"GREGORIAN",""" +
                """"skip":"FORWARD","bymonth":"2L","x-name":"foo"}],["dtstart",{},"date","2026-06-01"],""" +
                """["x-n",{},"integer",1,-2],["x-f",{},"float",1.5,2],["x-off",{},"boolean",false],""" +
                """["x-bad",{},"unknown","maybe"]],""" +
                """[["valarm",[["trigger",{},"date-time","2026-06-01T08:00:00Z"]],[]]]]]""",
            text,
        )
        val back = JCal.decodeFromString(text)
        assertEquals(emptyList(), back.problems)
        assertEquals(tree[0].properties[0], back.components[0].properties[0])
        assertEquals(text, JCal.encodeToString(back.components))
    }

    @Test
    fun malformedValuesInsideStructuresAreReported() {
        val samples = mapOf(
            """["x",[["request-status",{},"text",[{"a":1}]]],[]]""" to ProblemCode.MalformedJCalProperty,
            """["x",[["rrule",{},"recur",{"byday":[{"a":1}]}]],[]]""" to ProblemCode.MalformedJCalProperty,
            """["x",[["rrule",{},"recur",{"byday":{"a":1}}]],[]]""" to ProblemCode.MalformedJCalProperty,
            """["x",[["rrule",{},"recur",{"freq":null}]],[]]""" to ProblemCode.MalformedJCalProperty,
            """["x",[["rrule",{},"recur",{"freq":"DAILY","until":"soon"}]],[]]""" to ProblemCode.JCalValueMismatch,
            """["x",[["x-b",{},"boolean",1]],[]]""" to ProblemCode.JCalValueMismatch,
            """["x",[["duration",{},"duration","PT1X"]],[]]""" to ProblemCode.JCalValueMismatch,
            """["x",[["summary",{"cn":{"a":1}},"text","a"]],[]]""" to ProblemCode.MalformedJCalParameter,
            """["x",[["summary",{"cn":["a",1]},"text","a"]],[]]""" to ProblemCode.MalformedJCalParameter,
        )
        for ((text, code) in samples) assertEquals(listOf(code), codes(text), text)
        val escaped = JCal.decodeFromString("""["x",[["x-t",{},"text","a\\b\r\nc\rd"]],[]]""")
        assertEquals(emptyList(), escaped.problems)
        assertEquals("a\\\\b\\nc\\nd", escaped.components.single().properties.single().value)
        assertEquals("JCalReader", JCalReader(ICalRegistry.Default, ArrayList(), 16).toString())
        assertEquals("JCalWriter", JCalWriter(ICalRegistry.Default).toString())
        val names = listOf(
            ICalSerializers.dateTime,
            ICalSerializers.duration,
            ICalSerializers.recur,
            ICalSerializers.period,
        ).map { it.toString() }
        assertEquals(
            listOf(
                "ICalSerializers.dateTime",
                "ICalSerializers.duration",
                "ICalSerializers.recur",
                "ICalSerializers.period",
            ),
            names,
        )
    }

    @Test
    fun deeplyNestedComponentsAreCutAtTheDepthLimit() {
        val text = buildString {
            append("[\"vcalendar\",[],[")
            repeat(100_000) { append("[\"x-a\",[],[") }
            repeat(100_000) { append("]]") }
            append("]]")
        }
        val result = JCal.decodeFromString(text)
        assertEquals(listOf(ProblemCode.JCalTooDeep), result.problems.map { it.code })
        assertEquals(emptyList(), result.components)
        val element = Json.parseToJsonElement(nested(30))
        val tree = JCal.decodeFromJsonElement(element)
        assertEquals(listOf(ProblemCode.JCalTooDeep), tree.problems.map { it.code })
        assertEquals(16, levels(tree.components.single()))
        val shallow = JCal.decodeFromJsonElement(element, JCalOptions.builder().maxDepth(4).build())
        assertEquals(4, levels(shallow.components.single()))
        assertEquals(emptyList(), JCal.decodeFromString(nested(15)).problems)
        assertEquals(16, levels(JCal.decodeFromString(nested(15)).components.single()))
        val cut = JCal.decodeFromString(nested(17))
        assertEquals(listOf(ProblemCode.JCalTooDeep), cut.problems.map { it.code })
        assertEquals(16, levels(cut.components.single()))
        assertEquals(listOf(ProblemCode.JCalTooDeep), JCal.decodeFromString(nested(30)).problems.map { it.code })
        assertEquals(16, JCalOptions.Default.maxDepth)
        assertFailsWith<IllegalArgumentException> { JCalOptions.builder().maxDepth(0).build() }
        assertFailsWith<SerializationException> { Json.decodeFromString(ICalSerializers.component, nested(30)) }
    }

    // Found by fuzzing: parsing keeps a control character and a parameter without a value, reading cannot
    // build a node with them, so writing throws as canonical writing does instead of losing them on
    // the way back. The message has no text of the value.
    @Test
    fun whatCanonicalWritingRejectsIsNotWrittenAsJCal() {
        for (line in listOf(
            "SUMMARY:a\u001ab",
            "SUMMARY;X-P=a\u007fb:c",
            "SUMMARY;X-P:c",
            "X-A\u0001B:c",
            "SUMMARY;X\u0001:c",
        )) {
            val tree = parse("BEGIN:VCALENDAR", "BEGIN:VEVENT", line, "END:VEVENT", "END:VCALENDAR")
            val canonical = ICal.writer(WriteOptions.builder().byteStable(false).build())
            assertFailsWith<IllegalArgumentException>(line) { canonical.encodeToString(tree.single()) }
            val error = assertFailsWith<IllegalArgumentException>(line) { JCal.encodeToString(tree) }
            assertTrue("a\u001ab" !in error.message.orEmpty() && "a\u007fb" !in error.message.orEmpty(), error.message)
            assertFailsWith<IllegalArgumentException>(line) { JCal.encodeToJsonElement(tree) }
            assertFailsWith<IllegalArgumentException>(line) {
                Json.encodeToString(ICalSerializers.component, tree.single())
            }
        }
        val tab = parse("BEGIN:VCALENDAR", "BEGIN:VEVENT", "SUMMARY;X-P=a\tb:c\td", "END:VEVENT", "END:VCALENDAR")
        assertEquals(tab, JCal.decodeFromString(JCal.encodeToString(tab)).components)
    }

    // Found by the fuzzing campaign: a VALUE that is not a type name was written as `unknown` and dropped,
    // so the second round typed the value by the registry. A type name jCal does not define is written as that name
    // and reads back as VALUE; anything else in VALUE throws.
    @Test
    fun aDeclaredTypeReadsBackOrIsNotWritten() {
        val custom = parse("BEGIN:VEVENT", "DTSTART;VALUE=X-SLOT:20060102T120000", "X-N;VALUE=X-COUNT:7", "END:VEVENT")
        val json = JCal.encodeToString(custom)
        assertTrue("[\"dtstart\",{},\"x-slot\",\"20060102T120000\"]" in json, json)
        val back = JCal.decodeFromString(json)
        assertEquals(emptyList(), back.problems)
        assertEquals(custom, back.components)
        assertEquals(json, JCal.encodeToString(back.components))
        for (line in listOf("DTSTART;VALUE=USseEart/~:20060102T120000", "DTSTART;VALUE=DATE,DATE-TIME:20060102")) {
            val tree = parse("BEGIN:VEVENT", line, "END:VEVENT")
            val error = assertFailsWith<IllegalArgumentException>(line) { JCal.encodeToString(tree) }
            assertEquals("Cannot write property DTSTART: The VALUE parameter is not a type name", error.message)
        }
    }

    // A calendar with [depth] nested components below it, as jCal.
    private fun nested(depth: Int): String = buildString {
        append("[\"vcalendar\",[],[")
        repeat(depth) { append("[\"x-a\",[[\"x-p\",{\"x-q\":[\"a\",\"b\"]},\"text\",[\"a\",\"b\"]]],[") }
        repeat(depth) { append("]]") }
        append("]]")
    }

    private fun levels(root: ICalComponent): Int {
        var levels = 0
        var component: ICalComponent? = root
        while (component != null) {
            levels++
            component = component.components.singleOrNull()
        }
        return levels
    }

    private fun codes(text: String): List<ProblemCode> = JCal.decodeFromString(text).problems.map { it.code }

    private companion object {
        const val EXPECTED =
            """[["vcalendar",[["version",{},"text","2.0"],["prodid",{},"text","-//t//EN"]],""" +
                """[["vtimezone",[["tzid",{},"text","Europe/Berlin"]],""" +
                """[["daylight",[["dtstart",{},"date-time","1981-03-29T02:00:00"],""" +
                """["tzoffsetfrom",{},"utc-offset","+01:00"],["tzoffsetto",{},"utc-offset","+02:00"],""" +
                """["rrule",{},"recur",{"freq":"YEARLY","bymonth":3,"byday":"-1SU"}]],[]]]],""" +
                """["vevent",[["uid",{},"text","1"],["dtstamp",{},"date-time","2026-06-01T08:00:00Z"],""" +
                """["dtstart",{"tzid":"Europe/Berlin"},"date-time","2026-06-01T10:00:00"],""" +
                """["dtend",{"tzid":"Europe/Berlin"},"date-time","2026-06-01T11:30:00"],""" +
                """["rrule",{},"recur",{"freq":"WEEKLY","byday":["MO","WE"],""" +
                """"until":"2026-07-01T08:00:00Z","interval":1}],""" +
                """["exdate",{"tzid":"Europe/Berlin"},"date-time","2026-06-03T10:00:00","2026-06-10T10:00:00"],""" +
                """["rdate",{},"period","2026-06-05T10:00:00Z/2026-06-05T11:00:00Z","2026-06-06T10:00:00Z/PT1H"],""" +
                """["summary",{},"text","Planning, review; notes\nline two"],""" +
                """["categories",{},"text","WORK","PLANNING"],["geo",{},"float",[37.386013,-122.082932]],""" +
                """["priority",{},"integer",5],["request-status",{},"text",["2.0","Success"]],""" +
                """["attendee",{"cn":"Bob","role":"REQ-PARTICIPANT",""" +
                """"delegated-to":["mailto:a@example.com","mailto:b@example.com"]},""" +
                """"cal-address","mailto:bob@example.com"],""" +
                """["url",{},"uri","https://example.com/e/1"],""" +
                """["attach",{"encoding":"BASE64","fmttype":"text/plain"},"binary","SGVsbG8="],""" +
                """["x-flag",{},"boolean",true],["x-at",{},"time","12:00:00","13:30:00Z"],""" +
                """["x-rate",{},"float",-1.50],["x-day",{},"date","2026-06-01"],""" +
                """["x-raw",{},"unknown","anything;goes,here"]],[["valarm",[["action",{},"text","DISPLAY"],""" +
                """["trigger",{"related":"END"},"duration","-PT15M"],["duration",{},"duration","PT5M"],""" +
                """["repeat",{},"integer",2],["description",{},"text","Soon"]],[]]]]]]]"""
    }

    @Test
    fun aNumberThatADoubleCannotHoldStaysAsText() {
        fun encoded(line: String): String = JCal.encodeToString(
            ICal.parse("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n$line\r\nEND:VCALENDAR\r\n").components,
        )
        val geo = encoded("GEO:37.386013;-122.082932")
        assertTrue("\"float\",[37.386013,-122.082932]" in geo, geo)
        assertTrue(
            "\"float\",0.123456789012345]" in encoded("X-F;VALUE=FLOAT:0.123456789012345"),
            encoded("X-F;VALUE=FLOAT:0.123456789012345"),
        )
        assertTrue("\"0.1234567890123456\"" in encoded("X-F;VALUE=FLOAT:0.1234567890123456"))
        assertTrue("\"12345678901234567890\"" in encoded("X-F;VALUE=FLOAT:12345678901234567890"))
        val digits = "1".repeat(5000)
        assertTrue("\"$digits;2\"" in encoded("GEO:$digits;2"))
        // One significant digit, but below the smallest normal double: a reader would get fewer digits or 0.
        val tiny = "0." + "0".repeat(400) + "1"
        assertTrue("\"$tiny\"" in encoded("X-F;VALUE=FLOAT:$tiny"), encoded("X-F;VALUE=FLOAT:$tiny"))
        assertTrue("\"$tiny;1.5\"" in encoded("GEO:$tiny;1.5"))
        val subnormal = "0." + "0".repeat(308) + "5"
        assertTrue("\"$subnormal\"" in encoded("X-F;VALUE=FLOAT:$subnormal"))
        val smallestNormal = "0." + "0".repeat(306) + "3"
        assertTrue("\"float\",$smallestNormal]" in encoded("X-F;VALUE=FLOAT:$smallestNormal"))
        assertTrue("\"float\",0.000]" in encoded("X-F;VALUE=FLOAT:0.000"))
    }
}
