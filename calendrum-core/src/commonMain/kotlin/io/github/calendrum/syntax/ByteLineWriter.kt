@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.ByteOutput
import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// The byte output: UTF-8 into an own growing buffer. The no-argument constructor keeps everything in memory (`size`,
// `toByteArray`, `takeBytes`); the constructor with a `ByteOutput` flushes the buffer to it once it holds 32 KiB at a
// line end.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ByteLineWriter private constructor(capacity: Int, private val sink: ByteOutput?) : ContentLineWriter() {
    public constructor() : this(INITIAL_CAPACITY, null)

    internal constructor(capacity: Int) : this(maxOf(capacity, INITIAL_CAPACITY), null)

    internal constructor(sink: ByteOutput) : this(INITIAL_CAPACITY, sink)

    private var buffer = ByteArray(capacity)
    private var position = 0
    private var lineStart = 0
    private var lineOctets = 0

    public val size: Int get() = position

    public fun toByteArray(): ByteArray = buffer.copyOf(position)

    // The output without a copy when the buffer was sized exactly (byte-stable writing of a well-formed tree); the
    // writer must not be used afterwards.
    internal fun takeBytes(): ByteArray = if (position == buffer.size) buffer else buffer.copyOf(position)

    internal fun flush() {
        val sink = sink ?: return
        if (position > 0) sink.write(buffer, 0, position)
        position = 0
        lineStart = 0
    }

    override fun resetOutput() {
        position = 0
        lineStart = 0
        lineOctets = 0
    }

    override fun rawLine(source: ByteArray, from: Int, to: Int, folded: Boolean) {
        checkIdle()
        putRaw(source, from, to, folded)
    }

    override fun rawEnd(source: ByteArray, from: Int, to: Int, folded: Boolean) {
        checkIdle()
        ensureCapacity(position + END_PREFIX.length)
        for (index in END_PREFIX.indices) buffer[position++] = END_PREFIX[index].code.toByte()
        putRaw(upperCased(source, from, to), 0, to - from, folded)
    }

    override fun startLine() {
        lineOctets = 0
        lineStart = position
        ensureCapacity(position + LIMIT + FOLD_SLACK)
    }

    override fun finishLine() {
        buffer[position] = CR
        buffer[position + 1] = LF
        position += 2
        if (sink != null && position >= FLUSH_THRESHOLD) flush()
        lineOctets = 0
    }

    override fun discardLine() {
        position = lineStart
        lineOctets = 0
    }

    // One ASCII octet (name characters, delimiters, RFC 6868 escapes) under the same fold rule as `text`.
    override fun putAscii(code: Int) {
        if (lineOctets == LIMIT) {
            position = foldBytes(position)
            lineOctets = 1
        }
        buffer[position++] = code.toByte()
        lineOctets++
    }

    override fun putName(name: CharSequence) {
        for (index in 0 until name.length) {
            val code = name[index].code
            putAscii(if (code >= LOWER_A_CODE) code - CASE_OFFSET else code)
        }
    }

    override fun putPrefix(prefix: String) {
        for (index in prefix.indices) putAscii(prefix[index].code)
    }

    // UTF-8 encoding character by character straight into the buffer, without `encodeToByteArray` per value. `foldAt`
    // is the buffer position where the current physical line reaches 75 octets (CRLF not counted). A code point that
    // does not fit goes to the next physical line, after CRLF and one space: folds fall only between code points, so a
    // multi-byte UTF-8 sequence or a surrogate pair is never cut, and lines are filled greedily (a continuation line
    // holds 74 octets after its space). The buffer always has room for the rest of the current physical line plus one
    // fold (`FOLD_SLACK`), so its capacity is checked once per physical line in `startLine` and `foldBytes`, never per
    // character.
    // `index += 1`, not `index++`, at the end of the branches: the surrogate branch ends in `index += 2` (a statement),
    // so an `index++` there makes Kotlin/Native box an `Int` per character, as in `ContentLexer.scanSegment()`.
    override fun text(value: CharSequence, from: Int, to: Int) {
        var bytes = buffer
        var at = position
        var foldAt = at + LIMIT - lineOctets
        var index = from
        while (index < to) {
            val code = value[index].code
            if (code < UTF8_TWO_BYTES) {
                if (code < SPACE_CODE || code == DEL_CODE) checkValueCharacter(code, index)
                if (at == foldAt) {
                    at = foldBytes(at)
                    bytes = buffer
                    foldAt = at + LIMIT - 1
                }
                bytes[at++] = code.toByte()
                index += 1
            } else if (code < UTF8_THREE_BYTES) {
                if (at + 2 > foldAt) {
                    at = foldBytes(at)
                    bytes = buffer
                    foldAt = at + LIMIT - 1
                }
                bytes[at] = (0xC0 or (code ushr 6)).toByte()
                bytes[at + 1] = (0x80 or (code and 0x3F)).toByte()
                at += 2
                index += 1
            } else if (code and SURROGATE_MASK != SURROGATE_BASE) {
                if (at + 3 > foldAt) {
                    at = foldBytes(at)
                    bytes = buffer
                    foldAt = at + LIMIT - 1
                }
                bytes[at] = (0xE0 or (code ushr 12)).toByte()
                bytes[at + 1] = (0x80 or ((code ushr 6) and 0x3F)).toByte()
                bytes[at + 2] = (0x80 or (code and 0x3F)).toByte()
                at += 3
                index += 1
            } else {
                val point = surrogatePair(value, index, to)
                if (at + 4 > foldAt) {
                    at = foldBytes(at)
                    bytes = buffer
                    foldAt = at + LIMIT - 1
                }
                bytes[at] = (0xF0 or (point ushr 18)).toByte()
                bytes[at + 1] = (0x80 or ((point ushr 12) and 0x3F)).toByte()
                bytes[at + 2] = (0x80 or ((point ushr 6) and 0x3F)).toByte()
                bytes[at + 3] = (0x80 or (point and 0x3F)).toByte()
                at += 4
                index += 2
            }
        }
        position = at
        lineOctets = LIMIT - (foldAt - at)
    }

    // The implicit END of a parsed component with an invalid name is written in upper case. The name is upper-cased on
    // a copy (it is short and the case is rare), so that the copy loop of `putRaw()` has no case test: `byte in
    // LOWER_A..LOWER_Z` on `Byte` ends makes Kotlin/Native create an `IntRange`, rewriting it as two comparisons slowed
    // the byte-stable write on `macosArm64` by 6 to 9 %, and removing the test from the loop made it 5 % faster.
    // `isNameByte()` and `ContentReader.foldCase()` use plain comparisons for the same reason.
    private fun upperCased(source: ByteArray, from: Int, to: Int): ByteArray {
        val name = ByteArray(to - from)
        for (index in name.indices) {
            val code = source[from + index].toInt()
            name[index] = (if (code in LOWER_A_CODE..LOWER_Z_CODE) code - CASE_OFFSET else code).toByte()
        }
        return name
    }

    // Counts the bare CR and LF first, so the buffer grows once to the exact size of the normalized line. The CRLF pairs
    // of a folded line stay as they are, so a folded line without a bare CR or LF is one block copy like any other: the
    // byte-by-byte copy was 94 % of the byte-stable write of an invitation, 70 % of whose bytes are folded.
    private fun putRaw(source: ByteArray, from: Int, to: Int, folded: Boolean) {
        var extra = LINE_END.length
        if (folded) {
            var index = from
            while (index < to) {
                val byte = source[index]
                if (byte == CR && index + 1 < to && source[index + 1] == LF) {
                    index++
                } else if (byte == CR || byte == LF) {
                    extra++
                }
                index++
            }
        }
        ensureCapacity(position + (to - from) + extra)
        val bytes = buffer
        var at = position
        if (extra == LINE_END.length) {
            source.copyInto(bytes, at, from, to)
            at += to - from
        } else {
            var index = from
            while (index < to) {
                val byte = source[index]
                if (byte == CR || byte == LF) {
                    bytes[at] = CR
                    bytes[at + 1] = LF
                    at += 2
                    index += if (byte == CR && index + 1 < to && source[index + 1] == LF) 2 else 1
                    continue
                }
                bytes[at++] = byte
                index++
            }
        }
        bytes[at] = CR
        bytes[at + 1] = LF
        position = at + 2
        lineOctets = 0
        if (sink != null && position >= FLUSH_THRESHOLD) flush()
    }

    private fun foldBytes(at: Int): Int {
        val bytes = buffer
        bytes[at] = CR
        bytes[at + 1] = LF
        bytes[at + 2] = SPACE
        val next = at + 3
        ensureCapacity(next + LIMIT - 1 + FOLD_SLACK)
        return next
    }

    private fun ensureCapacity(required: Int) {
        if (buffer.size >= required) return
        var capacity = maxOf(buffer.size, INITIAL_CAPACITY)
        while (capacity < required) {
            capacity = if (capacity > Int.MAX_VALUE / 2) Int.MAX_VALUE else capacity * 2
        }
        buffer = buffer.copyOf(capacity)
    }

    private companion object {
        private const val FOLD_SLACK = 3
        private const val INITIAL_CAPACITY = 1024
        private const val FLUSH_THRESHOLD = 32 * 1024
    }
}
