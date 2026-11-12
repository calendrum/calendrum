@file:OptIn(InternalICalApi::class)

package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.InternalICalApi
import io.github.calendrum.syntax.escapeText
import io.github.calendrum.syntax.escapeTextList
import io.github.calendrum.syntax.unescapeText

class TextFuzzTest {
    // Round trip rule: unescaping the escaped text gives the original with CRLF and lone CR turned into LF. For lists,
    // the input is split at U+001E into elements; the escaped list, cut at commas that are not escaped, gives back
    // every element.
    @FuzzTest
    @DictionaryFile(resourcePath = "text.dict")
    fun text(input: ByteArray) {
        val value = String(input, Charsets.UTF_8)
        unescapeText(value)
        val escaped = escapeText(value)
        invariant(escaped.none { it == '\r' || it == '\n' }) { "line break in escaped text" }
        invariant(unescapeText(escaped) == normalizeLineBreaks(value)) { "round trip of '$value' via '$escaped'" }
        invariant(StringBuilder().also { escapeText(value, it) }.toString() == escaped) { "Appendable differs" }
        if (value.none { it in SPECIAL }) invariant(escaped == value) { "'$value' changed without special characters" }

        val items = value.split(ITEM_SEPARATOR)
        val list = escapeTextList(items)
        invariant(StringBuilder().also { escapeTextList(items, it) }.toString() == list) { "list Appendable differs" }
        val parts = splitList(list)
        invariant(parts.size == items.size) { "${items.size} items became ${parts.size} in '$list'" }
        for (index in items.indices) {
            invariant(unescapeText(parts[index]) == normalizeLineBreaks(items[index])) {
                "item $index '${items[index]}' in '$list'"
            }
        }
    }

    private fun normalizeLineBreaks(value: String): String = value.replace("\r\n", "\n").replace('\r', '\n')

    private fun splitList(list: String): List<String> {
        val parts = ArrayList<String>()
        val part = StringBuilder()
        var index = 0
        while (index < list.length) {
            val char = list[index]
            when {
                char == '\\' && index + 1 < list.length -> {
                    part.append(char).append(list[index + 1])
                    index += 2
                    continue
                }

                char == ',' -> {
                    parts += part.toString()
                    part.setLength(0)
                }

                else -> part.append(char)
            }
            index++
        }
        parts += part.toString()
        return parts
    }

    private companion object {
        const val ITEM_SEPARATOR = '\u001E'
        const val SPECIAL = "\\;,\r\n"
    }
}
