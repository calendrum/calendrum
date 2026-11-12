@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Severity
import io.github.calendrum.value.ValueCodecs
import io.github.calendrum.value.ValueProblems

class ValueCodecsFuzzTest {
    // Input: byte 0 picks the value type ('a' + the `ValueCodecs` type, modulo 17), byte 1 holds flags (bit 1: the
    // "next minute" leap second policy, bit 2: a TZID), the rest is the value in UTF-8. Both modes run; the value is
    // also decoded inside `<<…>>` with `from`/`to` to check that nothing outside the range is read.
    @FuzzTest
    @DictionaryFile(resourcePath = "value.dict")
    fun valueCodecs(input: ByteArray) {
        if (input.size < 2) return
        val type = Math.floorMod(input[0] - 'a'.code.toByte(), ValueCodecs.TYPE_COUNT)
        val flags = input[1].toInt()
        val nextMinute = flags and 2 != 0
        val tzid = if (flags and 4 != 0) "Europe/Moscow" else null
        val text = String(input, 2, input.size - 2, Charsets.UTF_8)
        val textType = type == ValueCodecs.TEXT || type == ValueCodecs.TEXT_LIST

        // TEXT values of a content line never contain CR or LF, and the encoder writes them as `\n`, so they cannot
        // round-trip; such inputs are skipped.
        if (textType && text.any { it == '\r' || it == '\n' }) return
        val padded = "<<$text>>"
        val decoded = arrayOfNulls<Any>(2)
        for (mode in 0..1) {
            val strict = mode == 1
            val problems = ArrayList<String>()
            var errors = 0
            val sink = ValueProblems { code, offset ->
                invariant(offset in 0..text.length) { "${code.id} at $offset outside '$text'" }
                problems += "${code.id}@$offset"
                if (code.severity == Severity.Error) errors++
            }
            val codecs = ValueCodecs(strict, nextMinute, sink)
            val value = codecs.decode(type, padded, 2, padded.length - 2, tzid)
            invariant(value != null || errors > 0) { "type $type '$text' rejected without an error: $problems" }
            invariant(!strict || value == null || errors == 0) { "type $type '$text' strict with errors: $problems" }
            val whole = ValueCodecs(strict, nextMinute, null).decode(type, text, 0, text.length, tzid)
            invariant(whole == value) { "type $type '$text' depends on the surrounding text" }
            decoded[mode] = value
            if (value == null) continue
            val encoded = codecs.encode(type, value)
            val again = ValueCodecs(false, false, null).decode(type, encoded, 0, encoded.length, tzid)
            invariant(again == value) { "type $type '$text' -> '$encoded' does not read back" }
            invariant(codecs.encode(type, again!!) == encoded) { "type $type '$text' -> '$encoded' not idempotent" }
        }
        if (decoded[1] != null) {
            invariant(decoded[0] == decoded[1]) { "type $type '$text' differs between strict and lenient" }
        }
    }
}
