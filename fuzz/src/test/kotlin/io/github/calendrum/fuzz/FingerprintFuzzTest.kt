@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.syntax.Fingerprints
import io.github.calendrum.syntax.ICalComponent

class FingerprintFuzzTest {
    @FuzzTest
    @DictionaryFile(resourcePath = "ics.dict")
    fun fingerprint(data: FuzzedDataProvider) {
        val seed = data.consumeLong()
        val density = data.consumeInt(1, 16)
        val input = data.consumeRemainingAsBytes()
        val fingerprints = ICal.fingerprints(input)
        checkEncoding(fingerprints)
        checkIndex(input, fingerprints)
        checkRefolding(input, fingerprints, seed, density)
    }

    private fun checkEncoding(fingerprints: Fingerprints) {
        val text = fingerprints.encodeToString()
        val decoded = Fingerprints.decodeFromString(text)
        invariant(decoded == fingerprints && decoded.encodeToString() == text) { "encodeToString does not round-trip" }
        val diff = fingerprints.diff(decoded)
        invariant(diff.unchangedCount == fingerprints.entries.size) { "fingerprints differ from themselves: $diff" }
        invariant(diff.added.isEmpty() && diff.removed.isEmpty() && diff.changed.isEmpty()) { "self diff $diff" }
    }

    private fun checkIndex(input: ByteArray, fingerprints: Fingerprints) {
        val result = ICal.parse(input)
        if (result.isTruncated) return
        val children = ArrayList<ICalComponent>()
        for (component in result.components) {
            if (component.name == "VCALENDAR") children.addAll(component.components) else children += component
        }
        val entries = fingerprints.entries
        invariant(entries.size == children.size) { "${entries.size} fingerprints for ${children.size} components" }
        val parser = ICal.parser()
        for (index in entries.indices) {
            val entry = entries[index]
            val child = children[index]
            val span = child.span!!
            invariant(entry.key.name == child.name) { "entry $index is ${entry.key}, the component is ${child.name}" }
            invariant(entry.startOffset == span.startOffset && entry.endOffset == span.endOffset) {
                "entry $index at ${entry.startOffset}..${entry.endOffset}, the component at $span"
            }
            val parsed = parser.parseComponent(input, entry)
            invariant(parsed == child) { "parseComponent of entry $index differs from the tree" }
            invariant(parsed.span!!.startOffset == span.startOffset && parsed.span!!.line == span.line) {
                "parseComponent of entry $index is at ${parsed.span}, the component at $span"
            }
        }
    }

    private fun checkRefolding(input: ByteArray, fingerprints: Fingerprints, seed: Long, density: Int) {
        val original = ICal.parse(input)
        if (original.charset != "UTF-8" || original.isTruncated) return
        val refolded = refold(input, seed, density)
        val result = ICal.parse(refolded)
        if (result.charset != "UTF-8" || result.isTruncated) return
        val after = ICal.fingerprints(refolded)
        invariant(after.entries.map { it.key } == fingerprints.entries.map { it.key }) { "refolding changed the keys" }
        invariant(after.entries.map { it.hash } == fingerprints.entries.map { it.hash }) { "refolding changed a hash" }
        invariant(after.diff(fingerprints).unchangedCount == fingerprints.entries.size) { "refolding changed the diff" }
    }

    // Inserts CRLF + space between two content bytes of a physical line (never in the first four bytes, a possible byte
    // order mark), with an LCG step deciding where; the hashes must not change when both inputs are UTF-8 and not
    // truncated (folding can move the charset decision window).
    private fun refold(input: ByteArray, seed: Long, density: Int): ByteArray {
        val out = ArrayList<Byte>(input.size * 2)
        var state = seed
        var inLine = false
        for (index in input.indices) {
            val byte = input[index]
            val lineEnd = byte == CR || byte == LF
            if (inLine && !lineEnd && index >= SIGNATURE) {
                state = state * MULTIPLIER + INCREMENT
                if ((state ushr SHIFT).toInt() % density == 0) {
                    out += CR
                    out += LF
                    out += SPACE
                }
            }
            out += byte
            inLine = !lineEnd
        }
        return out.toByteArray()
    }

    private companion object {
        const val SIGNATURE = 4
        const val MULTIPLIER = 6364136223846793005L
        const val INCREMENT = 1442695040888963407L
        const val SHIFT = 33
    }
}
