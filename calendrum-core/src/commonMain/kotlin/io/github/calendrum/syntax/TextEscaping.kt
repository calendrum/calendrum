@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// TEXT escaping of RFC 5545 §3.3.11: `\` becomes `\\`, `;` becomes `\;`, `,` becomes `\,`, a line break (CRLF, LF or a
// lone CR) becomes `\n`; `:` is not escaped. One pass without regular expressions (ical4j runs three `replaceAll` per
// property). A value with nothing to escape comes back as the same String. The five functions of this file are shared
// through `@InternalICalApi`: the `Text` fuzz target calls them from the `fuzz` module, and the value layer needs them
// too.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun escapeText(value: CharSequence): String {
    val first = firstTextSpecial(value, 0)
    if (first < 0) return value.toString()
    val out = StringBuilder(value.length + ESCAPE_RESERVE)
    out.append(value, 0, first)
    escapeTextFrom(value, first, out)
    return out.toString()
}

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun escapeText(value: CharSequence, out: Appendable) {
    escapeTextFrom(value, 0, out)
}

// For list values such as CATEGORIES: every element is escaped, the commas between elements are not.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun escapeTextList(values: List<CharSequence>): String {
    val out = StringBuilder()
    escapeTextList(values, out)
    return out.toString()
}

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun escapeTextList(values: List<CharSequence>, out: Appendable) {
    for (index in values.indices) {
        if (index > 0) out.append(',')
        escapeTextFrom(values[index], 0, out)
    }
}

// One pass from left to right, so `\\n` is an escaped backslash followed by `n`, never a line break (a known ical4j
// bug). `\n` and `\N` become LF. An unknown sequence `\x` stays as `\x`, and so does a trailing backslash: lenient
// reading keeps the text instead of throwing. There is no diagnostic here, since this function has neither a position
// nor a problem sink; the value layer reports an unknown sequence as a warning with the position of the backslash
// (`ICAL-V023`) and the strict mode rejects it.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun unescapeText(value: CharSequence): String {
    val length = value.length
    var index = indexOfBackslash(value, 0)
    if (index < 0) return value.toString()
    val out = StringBuilder(length)
    out.append(value, 0, index)
    while (index >= 0) {
        if (index + 1 == length) {
            out.append('\\')
            return out.toString()
        }
        when (val next = value[index + 1]) {
            '\\', ';', ',' -> out.append(next)
            'n', 'N' -> out.append('\n')
            else -> out.append('\\').append(next)
        }
        val from = index + 2
        index = indexOfBackslash(value, from)
        out.append(value, from, if (index < 0) length else index)
    }
    return out.toString()
}

private fun escapeTextFrom(value: CharSequence, from: Int, out: Appendable) {
    val length = value.length
    var run = from
    var index = firstTextSpecial(value, from)
    while (index >= 0) {
        out.append(value, run, index)
        val char = value[index]
        if (char == '\r' || char == '\n') {
            out.append("\\n")
            run = if (char == '\r' && index + 1 < length && value[index + 1] == '\n') index + 2 else index + 1
        } else {
            out.append('\\').append(char)
            run = index + 1
        }
        index = firstTextSpecial(value, run)
    }
    out.append(value, run, length)
}

private fun firstTextSpecial(value: CharSequence, from: Int): Int {
    for (index in from until value.length) {
        when (value[index]) {
            '\\', ';', ',', '\n', '\r' -> return index
            else -> {}
        }
    }
    return -1
}

private fun indexOfBackslash(value: CharSequence, from: Int): Int {
    for (index in from until value.length) {
        if (value[index] == '\\') return index
    }
    return -1
}

private const val ESCAPE_RESERVE = 16
