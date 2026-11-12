@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Strict writer of content lines (RFC 5545 §3.1), the core that the tree writer calls for every line. Reached by other
// Calendrum modules through `@InternalICalApi`. The base class holds the grammar and the checks; the output is in the two
// final subclasses: `ByteLineWriter` writes UTF-8 into an own growing buffer (`size`, `toByteArray`, `reset` keeps the
// capacity) and `AppendableLineWriter` appends characters to an `Appendable`. They are separate so that an application
// that writes only bytes or only characters keeps one of them. A line is a call sequence: `property`, then `parameter`
// with one or more `parameterValue`, then `value`; `begin` and `end` write a component line at once. Nothing is
// allocated per line. Calls out of order throw IllegalStateException; data that cannot be written strictly throws
// IllegalArgumentException with a position but never the text. Both outputs fold at the same places, so the UTF-8 of
// the characters equals the bytes.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public abstract class ContentLineWriter internal constructor() {
    private var state = IDLE

    public fun reset() {
        resetOutput()
        state = IDLE
    }

    public fun begin(name: CharSequence) {
        componentLine(BEGIN_PREFIX, name)
    }

    public fun end(name: CharSequence) {
        componentLine(END_PREFIX, name)
    }

    public fun property(name: CharSequence) {
        checkIdle()
        checkName(name, PROPERTY_NAME)
        startLine()
        putName(name)
        state = NAME
    }

    public fun parameter(name: CharSequence) {
        check(state == NAME || state == PARAMETER) { parameterOrderMessage() }
        checkName(name, PARAMETER_NAME)
        putAscii(SEMICOLON_CODE)
        putName(name)
        putAscii(EQUALS_CODE)
        state = PARAMETER_EMPTY
    }

    // Quotes only when the value contains `:`, `;` or `,` (RFC 5545 §3.2). RFC 6868: `^` becomes `^^`, `"` becomes
    // `^'`, LF, CR and CRLF become `^n`; after that no DQUOTE is left, so a quote alone never needs quoting. Control
    // characters other than HTAB cannot be written. The pre-scan decides quoting and rejects controls before anything
    // of the value is written.
    public fun parameterValue(value: CharSequence) {
        check(state == PARAMETER_EMPTY || state == PARAMETER) { "No parameter to add a value to" }
        val length = value.length
        var quoted = false
        var encoded = false
        for (index in 0 until length) {
            val code = value[index].code
            if (code == COLON_CODE || code == SEMICOLON_CODE || code == COMMA_CODE) {
                quoted = true
            } else if (code == CARET_CODE || code == DQUOTE_CODE || code == LF_CODE || code == CR_CODE) {
                encoded = true
            } else if ((code < SPACE_CODE && code != HTAB_CODE) || code == DEL_CODE) {
                fail("Control character in a parameter value at index $index")
            }
        }
        if (state == PARAMETER) putAscii(COMMA_CODE)
        if (quoted) putAscii(DQUOTE_CODE)
        if (encoded) putEncoded(value) else text(value, 0, length)
        if (quoted) putAscii(DQUOTE_CODE)
        state = PARAMETER
    }

    // The raw value is written as is: escaping belongs to the value layer (TEXT: `escapeText`). Control characters
    // other than HTAB are a contract violation (`VALUE-CHAR` of RFC 5545); CR or LF among them, since a TEXT line break
    // should have become `\n`.
    public fun value(value: CharSequence) {
        check(state == NAME || state == PARAMETER) { parameterOrderMessage() }
        putAscii(COLON_CODE)
        text(value, 0, value.length)
        endLine()
    }

    // These abstract functions (`startLine`, `finishLine`, `discardLine`, `putAscii`, `putName`, `putPrefix`, `text`,
    // `resetOutput`, `rawLine`, `rawEnd`) are the only things that differ between the two outputs. They are `internal`,
    // so nothing outside the module can extend the class. `putName` and `putPrefix` are whole loops in the output, not
    // one call per character. An application that uses one output loads one subclass, which lets the JIT and the
    // Kotlin/Native compiler see one receiver (the tree writing on `macosArm64` got 8 to 17 % faster, because the `out
    // == null` test of every call is gone). The price is the JVM micro benchmark `writer.write` on `university`, 10 to
    // 15 % slower in every variant of the split that was measured; the tree-writing benchmarks `write.*` on the JVM do
    // not move.
    //
    // Byte-stable writing of a parsed line: its source bytes (name, parameters, value, original folds) as they were,
    // with every line break normalized to CRLF and CRLF appended. No checks: the line is not built, it is reproduced.
    // `folded` tells whether the range may hold line breaks at all; without them the bytes are copied at once.
    internal abstract fun rawLine(source: ByteArray, from: Int, to: Int, folded: Boolean)

    // The implicit END of a parsed component whose name is not a valid name: `END:` and the source bytes of the name in
    // ASCII upper case, so that parsing the output closes the same BEGIN.
    internal abstract fun rawEnd(source: ByteArray, from: Int, to: Int, folded: Boolean)

    internal abstract fun resetOutput()

    internal abstract fun startLine()

    internal abstract fun finishLine()

    internal abstract fun discardLine()

    internal abstract fun putAscii(code: Int)

    internal abstract fun putName(name: CharSequence)

    internal abstract fun putPrefix(prefix: String)

    internal abstract fun text(value: CharSequence, from: Int, to: Int)

    internal fun checkIdle() {
        check(state == IDLE) { "The previous content line is not finished" }
    }

    // An unpaired surrogate has no UTF-8 form, and the stdlib replaces it differently per platform (`?` on the JVM,
    // U+FFFD elsewhere), so it is a contract violation.
    internal fun surrogatePair(value: CharSequence, index: Int, to: Int): Int {
        val high = value[index].code
        if (high >= LOW_SURROGATE_BASE || index + 1 >= to) unpairedSurrogate(index)
        val low = value[index + 1].code
        if (low and LOW_SURROGATE_MASK != LOW_SURROGATE_BASE) unpairedSurrogate(index)
        return SUPPLEMENTARY_BASE + ((high - SURROGATE_BASE) shl 10) + (low - LOW_SURROGATE_BASE)
    }

    // Called only for C0 controls and DEL, so the common path pays one comparison per character.
    internal fun checkValueCharacter(code: Int, index: Int) {
        if (code == LF_CODE || code == CR_CODE) fail("CR or LF in a property value at index $index")
        if (code != HTAB_CODE) fail("Control character in a property value at index $index")
    }

    private fun componentLine(prefix: String, name: CharSequence) {
        checkIdle()
        checkName(name, COMPONENT_NAME)
        startLine()
        putPrefix(prefix)
        putName(name)
        endLine()
    }

    private fun endLine() {
        finishLine()
        state = IDLE
    }

    private fun parameterOrderMessage(): String =
        if (state == PARAMETER_EMPTY) "The parameter has no value" else "No content line is started"

    // Names are `iana-token` or `x-name`: letters, digits and `-`. They are written in upper case.
    private fun checkName(name: CharSequence, kind: String) {
        if (name.isEmpty()) fail("Empty $kind")
        for (index in 0 until name.length) {
            if (!isNameChar(name[index].code)) fail("Invalid character in a $kind at index $index")
        }
    }

    private fun putEncoded(value: CharSequence) {
        val length = value.length
        var run = 0
        var index = 0
        while (index < length) {
            val code = value[index].code
            val escape = when (code) {
                CARET_CODE -> CARET_CODE
                DQUOTE_CODE -> APOSTROPHE_CODE
                LF_CODE, CR_CODE -> LOWER_N_CODE
                else -> 0
            }
            if (escape == 0) {
                index++
                continue
            }
            text(value, run, index)
            putAscii(CARET_CODE)
            putAscii(escape)
            index += if (code == CR_CODE && index + 1 < length && value[index + 1].code == LF_CODE) 2 else 1
            run = index
        }
        text(value, run, length)
    }

    private fun unpairedSurrogate(index: Int): Nothing = fail("Unpaired surrogate at index $index")

    // An IllegalArgumentException drops the unfinished line from the byte output (an `Appendable` keeps what it already
    // received: `discardLine` of the byte output moves the position back, that of the other output does nothing) and
    // returns the writer to the state between lines, so the next line can still be written.
    internal fun fail(message: String): Nothing {
        if (state != IDLE) {
            discardLine()
            state = IDLE
        }
        throw IllegalArgumentException(message)
    }

    // Shared by the base and the two outputs. Each constant is `internal` on its own: a constant of an internal
    // companion without the modifier is a public static field of the class in the ABI dump.
    internal companion object {
        internal const val LIMIT = 75
        internal const val BEGIN_PREFIX = "BEGIN:"
        internal const val END_PREFIX = "END:"
        internal const val LINE_END = "\r\n"
        internal const val FOLD = "\r\n "

        internal const val HTAB_CODE = 0x09
        internal const val LF_CODE = 0x0A
        internal const val CR_CODE = 0x0D
        internal const val SPACE_CODE = 0x20
        internal const val DEL_CODE = 0x7F
        internal const val CASE_OFFSET = 0x20
        internal const val LOWER_A_CODE = 0x61
        internal const val LOWER_Z_CODE = 0x7A

        internal const val UTF8_TWO_BYTES = 0x80
        internal const val UTF8_THREE_BYTES = 0x800
        internal const val SURROGATE_MASK = 0xF800
        internal const val SURROGATE_BASE = 0xD800

        private const val IDLE = 0
        private const val NAME = 1
        private const val PARAMETER_EMPTY = 2
        private const val PARAMETER = 3

        private const val PROPERTY_NAME = "property name"
        private const val PARAMETER_NAME = "parameter name"
        private const val COMPONENT_NAME = "component name"

        private const val DQUOTE_CODE = 0x22
        private const val APOSTROPHE_CODE = 0x27
        private const val COMMA_CODE = 0x2C
        private const val HYPHEN_CODE = 0x2D
        private const val COLON_CODE = 0x3A
        private const val SEMICOLON_CODE = 0x3B
        private const val EQUALS_CODE = 0x3D
        private const val CARET_CODE = 0x5E
        private const val LOWER_N_CODE = 0x6E

        private const val LOW_SURROGATE_MASK = 0xFC00
        private const val LOW_SURROGATE_BASE = 0xDC00
        private const val SUPPLEMENTARY_BASE = 0x10000

        private fun isNameChar(code: Int): Boolean = code == HYPHEN_CODE ||
            code in '0'.code..'9'.code ||
            code in 'A'.code..'Z'.code ||
            code in 'a'.code..'z'.code
    }
}
