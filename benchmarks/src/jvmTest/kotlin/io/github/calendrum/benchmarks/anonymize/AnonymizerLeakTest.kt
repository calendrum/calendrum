@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.anonymize

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.anonymize.Anonymizer
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import io.github.calendrum.syntax.ContentEventType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// The main leak test: every word of four or more letters or digits from a sensitive value, and every address, UID and
// link, must not occur in the output, case-insensitively. Sensitivity is the own list of the test, not the policy of
// the anonymizer. Words that also occur in kept text (names, dates, PRODID and the other kept values of the input and
// of the output, since a shift creates new digit runs, and `example.invalid`) are not counted.
class AnonymizerLeakTest {
    @Test
    fun invite() {
        assertNoLeak(Corpus.INVITE)
    }

    @Test
    fun inviteBatch() {
        assertNoLeak(Corpus.INVITE_BATCH)
    }

    @Test
    fun personal() {
        assertNoLeak(Corpus.PERSONAL)
    }

    @Test
    fun university() {
        assertNoLeak(Corpus.UNIVERSITY)
    }

    @Test
    fun universityShifted() {
        assertNoLeak(Corpus.UNIVERSITY, shiftWeeks = 3)
    }

    @Test
    fun huge() {
        assertNoLeak(Corpus.HUGE)
    }

    private fun assertNoLeak(corpus: String, shiftWeeks: Int = 0) {
        val source = corpusBytes(corpus)
        val output = anonymize(source, shiftWeeks)
        val secrets = Secrets.of(source, output)
        assertTrue(secrets.words.size > 20, "$corpus: ${secrets.words.size} sensitive words")
        val text = String(output, Charsets.UTF_8).lowercase().toCharArray()
        val leaked = PatternIndex(secrets.words + secrets.values).findIn(text)
        assertEquals(emptyList(), leaked.map { if (it in secrets.values) "a whole value" else it }, corpus)
    }

    private fun anonymize(source: ByteArray, shiftWeeks: Int): ByteArray {
        val result = Anonymizer(KEY, shiftWeeks).anonymize(source)
        assertTrue(result.isVerified, result.summary)
        return result.bytes
    }

    private class Secrets(val words: Set<String>, val values: Set<String>) {
        companion object {
            fun of(source: ByteArray, output: ByteArray): Secrets {
                val words = HashSet<String>()
                val values = HashSet<String>()
                val kept = StringBuilder(FIXED_WORDS.joinToString("\n"))
                scan(source, words, values, kept)
                scan(output, HashSet(), HashSet(), kept)
                val keptText = kept.toString().lowercase().toCharArray()
                val candidates = words.map { it.lowercase() }.filterTo(HashSet()) { it.length >= GRAM }
                val alsoKept = PatternIndex(candidates).findIn(keptText).toSet()
                return Secrets(candidates - alsoKept, values.filterTo(HashSet()) { it.length >= GRAM })
            }

            private fun scan(
                source: ByteArray,
                words: MutableSet<String>,
                values: MutableSet<String>,
                kept: StringBuilder,
            ) {
                val reader = ICal.parser().reader(source)
                var zoneDepth = 0
                while (true) {
                    when (reader.next() ?: break) {
                        ContentEventType.Begin -> {
                            kept.append('\n').append(reader.name)
                            if (zoneDepth == 0 && reader.name == "VTIMEZONE") zoneDepth = reader.depth
                        }

                        ContentEventType.End -> if (reader.depth == zoneDepth) zoneDepth = 0

                        ContentEventType.Property -> {
                            val name = reader.name
                            val value = reader.valueString()
                            val inZone = zoneDepth > 0
                            kept.append('\n').append(name)
                            val sensitive = if (inZone) name == "TZURL" else name !in PRESERVED_PROPERTIES
                            if (sensitive) words += words(unescape(value)) else kept.append('\n').append(value)
                            if (sensitive && name in IDENTIFIERS) values += identifier(value)
                            for (index in 0 until reader.parameterCount) {
                                val parameter = reader.parameterName(index)
                                kept.append('\n').append(parameter)
                                val secret = !inZone && parameter !in PRESERVED_PARAMETERS
                                for (item in reader.parameterValues(index)) {
                                    if (secret) words += words(item) else kept.append('\n').append(item)
                                    if (secret && parameter in IDENTIFIERS) values += identifier(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Multi-pattern search over 100 MB: patterns are grouped by their first four characters in an open-addressing
    // table, the text is scanned once. Addresses are searched without `mailto:`, otherwise one group would hold every
    // address and match every written `mailto:`.
    private class PatternIndex(patterns: Collection<String>) {
        private val keys: LongArray
        private val buckets: Array<List<String>>

        init {
            val grouped = patterns.filter { it.length >= GRAM }.groupBy { key(it, 0) }
            var size = 16
            while (size < grouped.size * 2) size *= 2
            keys = LongArray(size)
            val table = arrayOfNulls<List<String>>(size)
            for ((key, group) in grouped) {
                var slot = slot(key, size)
                while (table[slot] != null) slot = (slot + 1) and (size - 1)
                keys[slot] = key
                table[slot] = group
            }
            buckets = Array(size) { table[it] ?: emptyList() }
        }

        fun findIn(text: CharArray): List<String> {
            val found = LinkedHashSet<String>()
            for (index in 0..text.size - GRAM) {
                val key = key(text, index)
                var slot = slot(key, keys.size)
                while (buckets[slot].isNotEmpty()) {
                    if (keys[slot] == key) {
                        for (pattern in buckets[slot]) {
                            if (matches(text, index, pattern)) found += pattern
                        }
                        break
                    }
                    slot = (slot + 1) and (keys.size - 1)
                }
            }
            return found.toList()
        }

        private fun matches(text: CharArray, index: Int, pattern: String): Boolean {
            if (index + pattern.length > text.size) return false
            for (offset in pattern.indices) {
                if (text[index + offset] != pattern[offset]) return false
            }
            return true
        }

        private fun slot(key: Long, size: Int): Int = ((key * -0x61c8864680b583ebL) ushr 40).toInt() and (size - 1)

        private fun key(text: String, index: Int): Long = (text[index].code.toLong() shl 48) or
            (text[index + 1].code.toLong() shl 32) or (text[index + 2].code.toLong() shl 16) or
            text[index + 3].code.toLong()

        private fun key(text: CharArray, index: Int): Long = (text[index].code.toLong() shl 48) or
            (text[index + 1].code.toLong() shl 32) or (text[index + 2].code.toLong() shl 16) or
            text[index + 3].code.toLong()
    }

    private companion object {
        const val GRAM = 4
        val KEY = ByteArray(32) { (it * 31 + 7).toByte() }
        val FIXED_WORDS = listOf("mailto", "example.invalid", "https")
        val IDENTIFIERS = setOf(
            "ORGANIZER", "ATTENDEE", "UID", "RELATED-TO", "URL", "CONFERENCE", "IMAGE", "SOURCE", "EMAIL", "SENT-BY",
            "DELEGATED-TO", "DELEGATED-FROM", "MEMBER", "ALTREP", "DIR",
        )
        val PRESERVED_PROPERTIES = setOf(
            "VERSION", "PRODID", "CALSCALE", "METHOD", "STATUS", "CLASS", "TRANSP", "SEQUENCE", "PRIORITY", "DTSTART",
            "DTEND", "DUE", "DURATION", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "RRULE", "RDATE",
            "EXDATE", "TRIGGER", "REPEAT", "ACTION", "TZID", "TZNAME", "TZOFFSETFROM", "TZOFFSETTO",
            "PERCENT-COMPLETE", "COMPLETED", "FREEBUSY", "X-WR-TIMEZONE", "X-MICROSOFT-CDO-BUSYSTATUS",
            "X-MICROSOFT-CDO-IMPORTANCE", "X-MICROSOFT-DISALLOW-COUNTER", "X-APPLE-CALENDAR-COLOR",
            "X-APPLE-TRAVEL-ADVISORY-BEHAVIOR",
        )
        val PRESERVED_PARAMETERS = setOf(
            "VALUE", "TZID", "RANGE", "RELATED", "ROLE", "PARTSTAT", "RSVP", "CUTYPE", "FBTYPE", "ENCODING", "FMTTYPE",
            "RELTYPE", "LANGUAGE", "SIZE",
        )

        fun identifier(value: String): String = value.lowercase().removePrefix("mailto:")

        fun words(text: String): List<String> {
            val out = ArrayList<String>()
            val current = StringBuilder()
            for (char in text) {
                if (char.isLetterOrDigit()) {
                    current.append(char)
                } else if (current.isNotEmpty()) {
                    out += current.toString()
                    current.clear()
                }
            }
            if (current.isNotEmpty()) out += current.toString()
            return out
        }

        fun unescape(value: String): String = buildString {
            var index = 0
            while (index < value.length) {
                val char = value[index]
                if (char == '\\' && index + 1 < value.length && value[index + 1] in "nN,;\\") {
                    val next = value[index + 1]
                    append(if (next == 'n' || next == 'N') '\n' else next)
                    index += 2
                } else {
                    append(char)
                    index++
                }
            }
        }
    }
}
