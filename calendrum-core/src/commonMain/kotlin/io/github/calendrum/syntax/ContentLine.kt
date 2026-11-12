@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// One logical line as byte ranges into `bytes`, which is either the lexer input or its fold buffer. Parameters and
// values sit in flat `IntArray`s (a parameter: name start, name end, first value, value count; a value: start, end,
// flags) instead of objects or maps: a typical property has zero to three parameters. Strings are created only by the
// accessors that return `String`; the byte ranges let the next task intern known names and compare them without
// allocation.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ContentLine internal constructor() {
    public var bytes: ByteArray = ByteArray(0)
        private set
    internal var start: Int = 0
        private set
    internal var end: Int = 0
        private set
    public var nameEnd: Int = 0
        private set
    public var nameId: Int = KnownNames.UNKNOWN
        private set
    public var valueStart: Int = 0
        private set
    internal var validUtf8: Boolean = true
        private set

    public var startOffset: Int = 0
        private set
    public var endOffset: Int = 0
        private set
    public var line: Int = 0
        private set
    public var column: Int = 0
        private set
    internal var endLine: Int = 0
        private set

    public var parameterCount: Int = 0
        private set
    private var parameters = IntArray(PARAMETER_STRIDE * 4)
    private var valueCount = 0
    private var values = IntArray(VALUE_STRIDE * 4)

    internal var segmentCount: Int = 0
        private set
    private var segmentStarts = IntArray(4)
    private var segmentOffsets = IntArray(4)
    private var segmentLines = IntArray(4)
    private var segmentColumns = IntArray(4)

    private var scratch = ByteArray(0)

    public val nameStart: Int get() = start

    public val valueEnd: Int get() = end

    internal val isFolded: Boolean get() = segmentCount > 1

    // A known name returns the canonical string of `KnownNames`, the same instance every time, so reading names
    // allocates nothing; the spelling as written stays in `bytes`.
    public fun name(): String {
        val id = nameId
        return if (id >= 0) KnownNames.name(id) else decodeUtf8(bytes, start, nameEnd, validUtf8)
    }

    public fun rawValue(): String = decodeUtf8(bytes, valueStart, end, validUtf8)

    // The byte ranges of names and values in `bytes`. They are shared through `@InternalICalApi` so that the writer
    // benchmark can read a corpus into plain lines with them, and so that the `Lexer` fuzz target can check that the
    // name, parameter and value ranges lie inside the line and follow each other.
    public fun parameterNameStart(parameter: Int): Int = parameters[checkParameter(parameter) * PARAMETER_STRIDE]

    public fun parameterNameEnd(parameter: Int): Int = parameters[checkParameter(parameter) * PARAMETER_STRIDE + 1]

    // Looked up on first use and cached in the parameter record; `UNRESOLVED_NAME` means "valid name, not looked up
    // yet", `KnownNames.UNKNOWN` means unknown or invalid.
    public fun parameterNameId(parameter: Int): Int {
        val index = checkParameter(parameter) * PARAMETER_STRIDE
        val id = parameters[index + 4]
        if (id != UNRESOLVED_NAME) return id
        val resolved = KnownNames.find(bytes, parameters[index], parameters[index + 1])
        parameters[index + 4] = resolved
        return resolved
    }

    public fun parameterName(parameter: Int): String {
        val id = parameterNameId(parameter)
        if (id >= 0) return KnownNames.name(id)
        return decodeUtf8(bytes, parameterNameStart(parameter), parameterNameEnd(parameter), validUtf8)
    }

    public fun parameterValueCount(parameter: Int): Int = parameters[checkParameter(parameter) * PARAMETER_STRIDE + 3]

    public fun parameterValueStart(parameter: Int, value: Int): Int = values[valueIndex(parameter, value)]

    public fun parameterValueEnd(parameter: Int, value: Int): Int = values[valueIndex(parameter, value) + 1]

    // The raw records for the tree builder: the name id as stored (possibly `UNRESOLVED_NAME`, so an unfolded line
    // costs no lookup at parse time) and the flags of a value (`VALUE_HAS_CARET` tells the tree to apply RFC 6868).
    internal fun storedParameterNameId(parameter: Int): Int = parameters[
        checkParameter(parameter) * PARAMETER_STRIDE +
            4,
    ]

    internal fun parameterValueFlags(parameter: Int, value: Int): Int = values[valueIndex(parameter, value) + 2]

    internal fun isParameterValueQuoted(parameter: Int, value: Int): Boolean =
        values[valueIndex(parameter, value) + 2] and VALUE_QUOTED != 0

    // RFC 6868 decoding, left to right: `^n` becomes LF, `^^` becomes `^`, `^'` becomes `"`; any other `^` stays as it
    // is, together with the following character. Only values containing `^` take this path. The property value is never
    // decoded here (TEXT decoding belongs to the value layer).
    public fun parameterValue(parameter: Int, value: Int): String {
        val index = valueIndex(parameter, value)
        val from = values[index]
        val to = values[index + 1]
        if (values[index + 2] and VALUE_HAS_CARET == 0) return decodeUtf8(bytes, from, to, validUtf8)
        if (scratch.size < to - from) scratch = ByteArray(to - from)
        val out = scratch
        var count = 0
        var i = from
        while (i < to) {
            val byte = bytes[i]
            if (byte == CARET && i + 1 < to) {
                val decoded = when (bytes[i + 1]) {
                    LOWER_N -> LF
                    CARET -> CARET
                    APOSTROPHE -> DQUOTE
                    else -> null
                }
                if (decoded != null) {
                    out[count++] = decoded
                    i += 2
                    continue
                }
            }
            out[count++] = byte
            i++
        }
        return decodeUtf8(out, 0, count, validUtf8)
    }

    // `segmentCount == 0` means an unfolded line: its only segment starts at `start`, at `startOffset`, `line` and
    // `column`, so the lexer records segments only for folded lines (one call and four array writes less per line).
    internal fun offsetAt(index: Int): Int {
        if (segmentCount == 0) return startOffset + (index - start)
        val segment = segmentOf(index)
        return segmentOffsets[segment] + (index - segmentStarts[segment])
    }

    internal fun lineAt(index: Int): Int = if (segmentCount == 0) line else segmentLines[segmentOf(index)]

    internal fun columnAt(index: Int): Int {
        if (segmentCount == 0) return column + (index - start)
        val segment = segmentOf(index)
        return segmentColumns[segment] + (index - segmentStarts[segment])
    }

    override fun toString(): String = "ContentLine at $line:$column (offsets $startOffset..$endOffset)"

    internal fun reset(bytes: ByteArray, start: Int, end: Int, validUtf8: Boolean) {
        this.bytes = bytes
        this.start = start
        this.end = end
        this.validUtf8 = validUtf8
        nameEnd = start
        nameId = KnownNames.UNKNOWN
        valueStart = end
        parameterCount = 0
        valueCount = 0
    }

    internal fun clearSegments() {
        segmentCount = 0
    }

    internal fun addSegment(bufferStart: Int, offset: Int, line: Int, column: Int) {
        if (segmentCount == segmentStarts.size) {
            val size = segmentCount * 2
            segmentStarts = segmentStarts.copyOf(size)
            segmentOffsets = segmentOffsets.copyOf(size)
            segmentLines = segmentLines.copyOf(size)
            segmentColumns = segmentColumns.copyOf(size)
        }
        segmentStarts[segmentCount] = bufferStart
        segmentOffsets[segmentCount] = offset
        segmentLines[segmentCount] = line
        segmentColumns[segmentCount] = column
        segmentCount++
    }

    internal fun setSpan(startOffset: Int, endOffset: Int, line: Int, column: Int, endLine: Int) {
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.line = line
        this.column = column
        this.endLine = endLine
    }

    internal fun setName(end: Int, id: Int) {
        nameEnd = end
        nameId = id
    }

    internal fun setValueStart(index: Int) {
        valueStart = index
    }

    internal fun addValue(from: Int, to: Int, flags: Int) {
        val index = valueCount * VALUE_STRIDE
        if (index + VALUE_STRIDE > values.size) values = values.copyOf(values.size * 2)
        values[index] = from
        values[index + 1] = to
        values[index + 2] = flags
        valueCount++
    }

    internal val nextValueIndex: Int get() = valueCount

    internal fun addParameter(nameStart: Int, nameEnd: Int, nameId: Int, firstValue: Int) {
        val index = parameterCount * PARAMETER_STRIDE
        if (index + PARAMETER_STRIDE > parameters.size) parameters = parameters.copyOf(parameters.size * 2)
        parameters[index] = nameStart
        parameters[index + 1] = nameEnd
        parameters[index + 2] = firstValue
        parameters[index + 3] = valueCount - firstValue
        parameters[index + 4] = nameId
        parameterCount++
    }

    private fun checkParameter(parameter: Int): Int {
        if (parameter !in 0 until parameterCount) throw IndexOutOfBoundsException("parameter $parameter")
        return parameter
    }

    private fun valueIndex(parameter: Int, value: Int): Int {
        val base = checkParameter(parameter) * PARAMETER_STRIDE
        if (value !in 0 until parameters[base + 3]) throw IndexOutOfBoundsException("value $value")
        return (parameters[base + 2] + value) * VALUE_STRIDE
    }

    // Binary search over segment starts: maps an index in `bytes` to the physical line it came from.
    private fun segmentOf(index: Int): Int {
        var low = 0
        var high = segmentCount - 1
        while (low < high) {
            val middle = (low + high + 1) ushr 1
            if (segmentStarts[middle] <= index) low = middle else high = middle - 1
        }
        return low
    }

    private companion object {
        private const val PARAMETER_STRIDE = 5
        private const val VALUE_STRIDE = 3
    }
}

internal const val UNRESOLVED_NAME: Int = -2

internal const val VALUE_QUOTED: Int = 1
internal const val VALUE_HAS_CARET: Int = 2

internal const val HTAB: Byte = 0x09
internal const val LF: Byte = 0x0A
internal const val CR: Byte = 0x0D
internal const val SPACE: Byte = 0x20
internal const val DQUOTE: Byte = 0x22
internal const val APOSTROPHE: Byte = 0x27
internal const val COMMA: Byte = 0x2C
internal const val HYPHEN: Byte = 0x2D
internal const val COLON: Byte = 0x3A
internal const val SEMICOLON: Byte = 0x3B
internal const val EQUALS: Byte = 0x3D
internal const val CARET: Byte = 0x5E
internal const val LOWER_N: Byte = 0x6E

private const val DIGIT_ZERO: Byte = 0x30
private const val DIGIT_NINE: Byte = 0x39
private const val UPPER_A: Byte = 0x41
private const val UPPER_Z: Byte = 0x5A
private const val LOWER_A: Byte = 0x61
private const val LOWER_Z: Byte = 0x7A

// Characters allowed in property, parameter and component names by RFC 5545 §3.1 (`iana-token`, `x-name`): letters,
// digits and `-`.
internal fun isNameByte(byte: Byte): Boolean = byte == HYPHEN ||
    (byte >= DIGIT_ZERO && byte <= DIGIT_NINE) ||
    (byte >= UPPER_A && byte <= UPPER_Z) ||
    (byte >= LOWER_A && byte <= LOWER_Z)
