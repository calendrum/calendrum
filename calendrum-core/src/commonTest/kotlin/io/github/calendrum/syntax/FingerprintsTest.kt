@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemListener
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class FingerprintsTest {
    private val feed = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Feed//EN",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Moscow",
        "BEGIN:STANDARD",
        "DTSTART:19700101T000000",
        "TZOFFSETFROM:+0300",
        "TZOFFSETTO:+0300",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "UID:1@example.com",
        "DTSTAMP:20260101T000000Z",
        "SUMMARY:Lecture",
        "ATTENDEE;CN=Jane;ROLE=CHAIR:mailto:jane@example.com",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "END:VALARM",
        "END:VEVENT",
        "BEGIN:VEVENT",
        "UID:1@example.com",
        "RECURRENCE-ID;TZID=Europe/Moscow:20260108T100000",
        "DTSTAMP:20260101T000000Z",
        "SUMMARY:Moved lecture",
        "END:VEVENT",
        "BEGIN:VTODO",
        "SUMMARY:No UID",
        "END:VTODO",
        "BEGIN:VTODO",
        "SUMMARY:No UID either",
        "END:VTODO",
        "END:VCALENDAR",
    )

    private val fingerprints = ICal.fingerprints(feed.encodeToByteArray())

    @Test
    fun childrenOfTheCalendarAreKeyed() {
        val keys = fingerprints.entries.map { it.key }
        assertEquals(listOf("VTIMEZONE", "VEVENT", "VEVENT", "VTODO", "VTODO"), keys.map { it.name })
        assertEquals(listOf(0, 0, 0, 0, 0), keys.map { it.calendarIndex })
        assertEquals(listOf(null, "1@example.com", "1@example.com", null, null), keys.map { it.uid })
        assertEquals(listOf(null, null, "20260108T100000", null, null), keys.map { it.recurrenceId })
        assertEquals(listOf("Europe/Moscow", null, null, null, null), keys.map { it.tzid })
        assertEquals(listOf(-1, -1, -1, 0, 1), keys.map { it.ordinal })
        assertEquals(5, keys.toSet().size)
        assertEquals(1, fingerprints.algorithmVersion)
    }

    @Test
    fun offsetsAreTheSpansOfTheComponents() {
        val children = ICal.parse(feed).components.single().components
        assertEquals(children.map { it.span!!.startOffset }, fingerprints.entries.map { it.startOffset })
        assertEquals(children.map { it.span!!.endOffset }, fingerprints.entries.map { it.endOffset })
    }

    @Test
    fun theHashFollowsTheDefinition() {
        val input = crlf(
            "BEGIN:VCALENDAR",
            "PRODID:ignored",
            "begin:vevent",
            "uid:42",
            "DTSTAMP:20260101T000000Z",
            "attendee;cn=\"Jane\";x-Role=a,b:mailto:jane@example.com",
            "DESCRIPTION:folded ",
            "  line",
            "begin:x-thing",
            "End:X-Thing",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val canonical = crlf(
            "BEGIN:VEVENT",
            "UID:42",
            "ATTENDEE;CN=\"Jane\";X-ROLE=a,b:mailto:jane@example.com",
            "DESCRIPTION:folded  line",
            "BEGIN:X-THING",
            "END:X-THING",
            "END:VEVENT",
        ).encodeToByteArray()
        val entry = ICal.fingerprints(input.encodeToByteArray()).entries.single()
        assertEquals(xxHash64(canonical, 0, canonical.size), entry.hash)
    }

    @Test
    fun referenceValuesAreTheSameOnEveryPlatform() {
        assertEquals(REFERENCE, fingerprints.encodeToString())
    }

    @Test
    fun anUnchangedFileIsUnchanged() {
        val diff = fingerprints.diff(ICal.fingerprints(feed.encodeToByteArray()))
        assertEquals(5, diff.unchangedCount)
        assertTrue(diff.added.isEmpty() && diff.removed.isEmpty() && diff.changed.isEmpty())
    }

    @Test
    fun aNewTimestampIsNotAChange() {
        val restamped = feed.replace("DTSTAMP:20260101T000000Z", "DTSTAMP:20260301T120000Z")
        val diff = ICal.fingerprints(restamped.encodeToByteArray()).diff(fingerprints)
        assertEquals(5, diff.unchangedCount)
        val strict = FingerprintOptions.builder().ignoredProperties(emptySet()).build()
        val before = ICal.fingerprints(feed.encodeToByteArray(), strict)
        val after = ICal.fingerprints(restamped.encodeToByteArray(), strict)
        assertEquals(2, after.diff(before).changed.size)
    }

    @Test
    fun aNewSummaryIsAChange() {
        val edited = feed.replace("SUMMARY:Lecture", "SUMMARY:Seminar")
        val now = ICal.fingerprints(edited.encodeToByteArray())
        val diff = now.diff(fingerprints)
        assertEquals(listOf(now.entries[1]), diff.changed)
        assertEquals(4, diff.unchangedCount)
    }

    @Test
    fun aChangeInANestedComponentChangesItsParent() {
        val edited = feed.replace("ACTION:DISPLAY", "ACTION:AUDIO")
        val diff = ICal.fingerprints(edited.encodeToByteArray()).diff(fingerprints)
        assertEquals(listOf("1@example.com"), diff.changed.map { it.key.uid })
    }

    @Test
    fun foldingLineEndingsAndNameCaseDoNotMatter() {
        val reshaped = feed
            .replace("SUMMARY:Moved lecture", "summary:Moved\r\n  lecture")
            .replace("BEGIN:VALARM", "begin:valarm")
            .replace("END:VALARM", "End:VAlarm")
            .replace("ATTENDEE;CN=Jane;ROLE=CHAIR", "attendee;cn=Jane;Role=CHAIR")
            .replace("\r\n", "\n")
        val folded = reshaped.replace("summary:Moved\n  lecture", "summary:Mov\n\ted\n  lecture")
        assertNotEquals(reshaped, folded)
        for (input in listOf(reshaped, folded)) {
            val diff = ICal.fingerprints(input.encodeToByteArray()).diff(fingerprints)
            assertEquals(5, diff.unchangedCount, input)
        }
        val requoted = feed.replace("CN=Jane", "CN=\"Jane\"")
        assertEquals(1, ICal.fingerprints(requoted.encodeToByteArray()).diff(fingerprints).changed.size)
    }

    @Test
    fun addedAndRemovedComponents() {
        val edited = feed
            .replace(
                "BEGIN:VEVENT\r\nUID:1@example.com\r\nRECURRENCE-ID;TZID=Europe/Moscow:20260108T100000\r\n",
                "BEGIN:VEVENT\r\nUID:2@example.com\r\n",
            )
            .replace("BEGIN:VTODO\r\nSUMMARY:No UID either\r\nEND:VTODO\r\n", "")
        val diff = ICal.fingerprints(edited.encodeToByteArray()).diff(fingerprints)
        assertEquals(listOf("2@example.com"), diff.added.map { it.key.uid })
        assertEquals(listOf("20260108T100000", null), diff.removed.map { it.key.recurrenceId })
        assertEquals(listOf("VEVENT", "VTODO"), diff.removed.map { it.key.name })
        assertEquals(1, diff.removed[1].key.ordinal)
        assertEquals(3, diff.unchangedCount)
        assertTrue(diff.changed.isEmpty())
    }

    @Test
    fun duplicateKeysAreMatchedInOrder() {
        val event = crlf("BEGIN:VEVENT", "UID:same", "SUMMARY:%s", "END:VEVENT")
        val before = calendar(event.replace("%s", "a") + event.replace("%s", "b"))
        val after = calendar(event.replace("%s", "a") + event.replace("%s", "c") + event.replace("%s", "d"))
        val diff = ICal.fingerprints(after).diff(ICal.fingerprints(before))
        assertEquals(1, diff.unchangedCount)
        assertEquals(1, diff.changed.size)
        assertEquals(1, diff.added.size)
        val third = after.decodeToString().indexOf("BEGIN:VEVENT\r\nUID:same\r\nSUMMARY:d")
        assertEquals(third.toLong(), diff.added.single().startOffset)
        assertTrue(diff.removed.isEmpty())
    }

    @Test
    fun severalCalendarsAndTopLevelComponents() {
        val event = crlf("BEGIN:VEVENT", "UID:same", "END:VEVENT")
        val input = calendar(event) + calendar(event) + event.encodeToByteArray()
        val keys = ICal.fingerprints(input).entries.map { it.key }
        assertEquals(listOf(0, 1, -1), keys.map { it.calendarIndex })
        assertEquals(3, keys.toSet().size)
        assertTrue(ICal.fingerprints(ByteArray(0)).entries.isEmpty())
    }

    @Test
    fun anImplicitEndHashesLikeAnExplicitOne() {
        val explicit = ICal.fingerprints(
            crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1", "END:VEVENT", "END:VCALENDAR").encodeToByteArray(),
        )
        val implicit = ICal.fingerprints(
            crlf("BEGIN:VCALENDAR", "BEGIN:VEVENT", "UID:1", "END:VCALENDAR").encodeToByteArray(),
        )
        assertEquals(explicit.entries.single().hash, implicit.entries.single().hash)
        assertEquals(explicit.entries.single().key, implicit.entries.single().key)
    }

    @Test
    fun ignoredPropertiesAreMatchedByCanonicalName() {
        val options = FingerprintOptions.builder().ignoredProperties(setOf("x-sequence", "Summary")).build()
        assertEquals(setOf("X-SEQUENCE", "SUMMARY"), options.ignoredProperties)
        val one = calendar(crlf("BEGIN:VEVENT", "UID:1", "x-Sequence:1", "summary:a", "DTSTAMP:1", "END:VEVENT"))
        val two = calendar(crlf("BEGIN:VEVENT", "UID:1", "X-SEQUENCE:2", "SUMMARY:b", "DTSTAMP:1", "END:VEVENT"))
        assertEquals(1, ICal.fingerprints(two, options).diff(ICal.fingerprints(one, options)).unchangedCount)
        assertEquals(1, ICal.fingerprints(two).diff(ICal.fingerprints(one)).changed.size)
        assertEquals(setOf("DTSTAMP"), FingerprintOptions.Default.ignoredProperties)
        assertFailsWith<IllegalArgumentException> {
            FingerprintOptions.builder().ignoredProperties(setOf("A B")).build()
        }
        assertFailsWith<IllegalArgumentException> {
            FingerprintOptions.builder().ignoredProperties(setOf("END")).build()
        }
    }

    @Test
    fun encodingRoundTrips() {
        val tricky = calendar(
            crlf(
                "BEGIN:VEVENT",
                "UID:tab\there\\back,slash=-",
                "RECURRENCE-ID:-",
                "END:VEVENT",
                "BEGIN:X-ÜBER",
                "END:X-ÜBER",
                "BEGIN:",
                "UID:empty name",
                "END:",
            ),
        )
        for (value in listOf(fingerprints, ICal.fingerprints(tricky), ICal.fingerprints(ByteArray(0)))) {
            val text = value.encodeToString()
            val decoded = Fingerprints.decodeFromString(text)
            assertEquals(value, decoded)
            assertEquals(value.entries, decoded.entries)
            assertEquals(text, decoded.encodeToString())
            assertEquals(value.entries.size, decoded.diff(value).unchangedCount)
        }
        val key = ICal.fingerprints(tricky).entries.first().key
        assertEquals("tab\there\\back,slash=-", key.uid)
        assertEquals("-", key.recurrenceId)
    }

    @Test
    fun malformedTextIsRejected() {
        val text = fingerprints.encodeToString()
        val broken = listOf(
            "",
            "ICSFP 1 UTF-8",
            "ICSFP 1 EBCDIC\n",
            text.replace("ICSFP 1", "ICSFP x"),
            text.replace("\t0\t", "\tzero\t"),
            text.replaceFirst("\t-\t", "\tnull\t"),
            text.dropLast(1),
            text + "extra\n",
        )
        for (value in broken) {
            assertFailsWith<IllegalArgumentException>(value) { Fingerprints.decodeFromString(value) }
        }
        val future = text.replace("ICSFP 1", "ICSFP 2")
        val error = assertFailsWith<IllegalArgumentException> { Fingerprints.decodeFromString(future) }
        assertEquals("Unsupported fingerprint algorithm version 2", error.message)
    }

    @Test
    fun parseComponentEqualsTheSubtreeOfTheFullParse() {
        val inputs = listOf(
            feed.encodeToByteArray(),
            feed.replace("\r\n", "\n").encodeToByteArray(),
            byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()) + feed.encodeToByteArray(),
            windows1251(feed.replace("Lecture", "Лекция")),
            utf16le(feed.replace("Lecture", "Лекция")),
        )
        for (retain in listOf(true, false)) {
            val parser = ICal.parser(ParseOptions.builder().retainSource(retain).build())
            for (input in inputs) {
                val tree = parser.parse(input).components.single().components
                val entries = ICal.fingerprints(input).entries
                val decoded = Fingerprints.decodeFromString(ICal.fingerprints(input).encodeToString()).entries
                assertEquals(tree.size, entries.size)
                for (index in tree.indices) {
                    val expected = buildString { renderComponent(tree[index]) }
                    assertEquals(tree[index], parser.parseComponent(input, entries[index]))
                    assertEquals(
                        expected,
                        buildString {
                            renderComponent(parser.parseComponent(input, entries[index]))
                        },
                    )
                    assertEquals(
                        expected,
                        buildString {
                            renderComponent(parser.parseComponent(input, decoded[index]))
                        },
                    )
                }
            }
        }
    }

    @Test
    fun parseComponentAppliesTheParserOptions() {
        val parser = ICal.parser(
            ParseOptions.builder().intercept { line ->
                if (line.name ==
                    "SUMMARY"
                ) {
                    line.drop()
                }
            }.build(),
        )
        val event = parser.parseComponent(feed.encodeToByteArray(), fingerprints.entries[1])
        assertEquals(null, event.property("SUMMARY"))
        assertFailsWith<IllegalArgumentException> {
            ICal.parser().parseComponent(ByteArray(10), fingerprints.entries[1])
        }
        val blank = ByteArray(fingerprints.entries[1].endOffset.toInt()) { ' '.code.toByte() }
        assertFailsWith<IllegalArgumentException> { ICal.parser().parseComponent(blank, fingerprints.entries[1]) }
    }

    @Test
    fun toStringShowsNoValues() {
        val entry = fingerprints.entries[1]
        for (text in listOf(
            entry.toString(),
            entry.key.toString(),
            fingerprints.toString(),
            fingerprints.diff(fingerprints).toString(),
        )) {
            assertFalse("example.com" in text, text)
        }
        assertEquals("ComponentKey(calendarIndex=0, name=VEVENT, ordinal=-1)", entry.key.toString())
        assertEquals("Fingerprints(entries=5, algorithmVersion=1)", fingerprints.toString())
        assertEquals(
            "FingerprintDiff(added=0, removed=0, changed=0, unchanged=5)",
            fingerprints.diff(fingerprints).toString(),
        )
        assertEquals("FingerprintOptions(ignoredProperties=[DTSTAMP])", FingerprintOptions.Default.toString())
        assertNotEquals(fingerprints.entries[0], fingerprints.entries[1])
    }

    private fun calendar(body: String): ByteArray =
        (crlf("BEGIN:VCALENDAR") + body + crlf("END:VCALENDAR")).encodeToByteArray()

    private fun windows1251(text: String): ByteArray = ByteArray(text.length) { index ->
        val char = text[index]
        when (char) {
            in 'А'..'я' -> (char.code - 'А'.code + 0xC0).toByte()
            else -> char.code.toByte()
        }
    }

    private fun utf16le(text: String): ByteArray {
        val out = ByteArray(2 + text.length * 2)
        out[0] = 0xFF.toByte()
        out[1] = 0xFE.toByte()
        for (index in text.indices) {
            out[2 + index * 2] = text[index].code.toByte()
            out[3 + index * 2] = (text[index].code shr 8).toByte()
        }
        return out
    }

    private companion object {
        const val REFERENCE = "ICSFP 1 UTF-8\n" +
            "0\tVTIMEZONE\t-\t-\t=Europe/Moscow\t-1\t7b39bc31c0ebb8d6\t59\t204\t4\n" +
            "0\tVEVENT\t=1@example.com\t-\t-\t-1\tc7565db4572bcab4\t204\t387\t12\n" +
            "0\tVEVENT\t=1@example.com\t=20260108T100000\t-\t-1\ta63ab9bbbb80ba19\t387\t531\t21\n" +
            "0\tVTODO\t-\t-\t-\t0\ta2c457acd687b4d9\t531\t571\t27\n" +
            "0\tVTODO\t-\t-\t-\t1\tf797f159f0d4b763\t571\t618\t30\n"
    }

    @Test
    fun theLimitsOfReadingAndTheirProblemsCanBeSet() {
        fun calendar(tail: String): ByteArray = (
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\nBEGIN:VEVENT\r\nUID:a\r\nDESCRIPTION:" + "x".repeat(200) +
                tail +
                "\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
            ).encodeToByteArray()

        val heard = ArrayList<String>()
        val options = FingerprintOptions.builder()
            .limits(ParseLimits.builder().maxLineOctets(100).build())
            .problemListener(ProblemListener { heard += it.code.id })
            .build()
        val first = ICal.fingerprints(calendar("A"), options)
        val second = ICal.fingerprints(calendar("B"), options)
        assertTrue(second.diff(first).changed.isEmpty(), "a line above the limit takes no part in the hash")
        assertTrue("ICAL-S010" in heard, "and the listener hears about it")
        assertTrue(
            ICal.fingerprints(calendar("B")).diff(ICal.fingerprints(calendar("A"))).changed.isNotEmpty(),
            "the default limit sees it",
        )
    }

    // The merge of two sorted runs against the plain definition: every newer entry, in input order, takes the first older
    // entry with an equal key that no earlier one took. Keys come from a small pool, so duplicates are common, and the
    // strings "Aa" and "BB" share a hashCode on every platform, so equal hashes have to fall back to the key. Every fifth
    // round is large enough for the radix sort, the rest take the merge sort of small runs.
    @Test
    fun diffMatchesThePlainDefinition() {
        val random = Random(2026)
        val names = listOf("VEVENT", "VTODO", "Aa", "BB")
        val strings = listOf(null, "", "Aa", "BB", "AaAa", "BBBB", "AaBB", "1@example.com")
        fun entry(index: Int): ComponentFingerprint {
            val key = ComponentKey(
                calendarIndex = random.nextInt(-1, 2),
                name = names.random(random),
                uid = strings.random(random),
                recurrenceId = if (random.nextInt(4) == 0) strings.random(random) else null,
                tzid = if (random.nextInt(4) == 0) strings.random(random) else null,
                ordinal = if (random.nextBoolean()) -1 else random.nextInt(3),
            )
            return ComponentFingerprint(
                key,
                random.nextLong(3),
                index.toLong(),
                index + 1L,
                index + 1,
                InputCharset.Utf8,
            )
        }
        repeat(500) { round ->
            val large = round % 5 == 0
            val older = Array(if (large) random.nextInt(64, 400) else random.nextInt(40)) { entry(it) }
            val newer = Array(if (large) random.nextInt(64, 400) else random.nextInt(40)) { entry(it) }
            val diff = Fingerprints(newer, 1, InputCharset.Utf8).diff(Fingerprints(older, 1, InputCharset.Utf8))

            val taken = BooleanArray(older.size)
            val added = ArrayList<ComponentFingerprint>()
            val changed = ArrayList<ComponentFingerprint>()
            var unchanged = 0
            for (item in newer) {
                val match = older.indices.firstOrNull { !taken[it] && older[it].key == item.key }
                when {
                    match == null -> added += item

                    else -> {
                        taken[match] = true
                        if (older[match].hash == item.hash) unchanged++ else changed += item
                    }
                }
            }
            val removed = older.filterIndexed { index, _ -> !taken[index] }
            assertEquals(added, diff.added, "added, round $round")
            assertEquals(changed, diff.changed, "changed, round $round")
            assertEquals(removed, diff.removed, "removed, round $round")
            assertEquals(unchanged, diff.unchangedCount, "unchanged, round $round")
        }
    }

    // Many keys whose strings share one hashCode, so their hashes are all equal: the radix sort leaves one run, and the merge
    // sort of that run has to tell them apart and keep duplicates in input order.
    @Test
    fun diffOfKeysWithOneHashCode() {
        val distinct = (0 until 128).map { bits ->
            (0 until 7).joinToString("") {
                if (bits shr it and 1 ==
                    0
                ) {
                    "Aa"
                } else {
                    "BB"
                }
            }
        }
        assertEquals(1, distinct.map { it.hashCode() }.distinct().size)
        fun run(order: List<String>, hash: (String) -> Long): Fingerprints {
            val entries = order.mapIndexed { index, uid ->
                val key = ComponentKey(0, "VEVENT", uid, null, null, -1)
                ComponentFingerprint(key, hash(uid), index.toLong(), index + 1L, index + 1, InputCharset.Utf8)
            }
            return Fingerprints(entries.toTypedArray(), 1, InputCharset.Utf8)
        }
        val older = run(distinct + distinct.take(3)) { 1L }
        val newer = run(distinct.reversed() + distinct.take(2)) { if (it == distinct[5]) 2L else 1L }
        val diff = newer.diff(older)
        assertEquals(listOf(distinct[5]), diff.changed.map { it.key.uid })
        assertEquals(distinct.size + 2, diff.unchangedCount + diff.changed.size)
        assertEquals(listOf(distinct[2]), diff.removed.map { it.key.uid })
        assertEquals(distinct.size + 3 - 1, diff.removed.single().startOffset.toInt())
        assertTrue(diff.added.isEmpty())
    }

    @Test
    fun sortedPositionsIsASortAndKeepsTheOrderOfEqualItems() {
        val random = Random(5)
        for (size in listOf(0, 1, 2, 3, 7, 8, 9, 64, 65, 1000, 1023, 1025)) {
            val values = IntArray(size) { random.nextInt(10) }
            val order = io.github.calendrum.sortedPositions(size) { a, b ->
                val result = values[a].compareTo(values[b])
                if (result != 0) result else a.compareTo(b)
            }
            assertEquals((0 until size).toList(), order.sorted(), "a permutation of $size")
            for (index in 1 until size) {
                val before = order[index - 1]
                val after = order[index]
                assertTrue(
                    values[before] < values[after] || (values[before] == values[after] && before < after),
                    "size $size",
                )
            }
        }
    }
}
