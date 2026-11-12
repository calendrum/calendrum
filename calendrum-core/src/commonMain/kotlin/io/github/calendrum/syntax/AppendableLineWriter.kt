@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// The character output: everything goes to an `Appendable` as it is written, so after an IllegalArgumentException the
// part of the line already appended stays there. It has no `size` or `toByteArray`: what used to be a runtime check of
// a single class is now a type.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class AppendableLineWriter(private val out: Appendable) : ContentLineWriter() {
    private var lineOctets = 0
    private var rawChars: CharArray? = null
    private val rawView = CharsView()

    override fun resetOutput() {
        lineOctets = 0
    }

    override fun rawLine(source: ByteArray, from: Int, to: Int, folded: Boolean) {
        checkIdle()
        appendRaw(source, from, to, false)
    }

    override fun rawEnd(source: ByteArray, from: Int, to: Int, folded: Boolean) {
        checkIdle()
        out.append(END_PREFIX)
        appendRaw(source, from, to, true)
    }

    override fun startLine() {
        lineOctets = 0
    }

    override fun finishLine() {
        out.append(LINE_END)
        lineOctets = 0
    }

    override fun discardLine() {
        lineOctets = 0
    }

    override fun putAscii(code: Int) {
        if (lineOctets == LIMIT) {
            out.append(FOLD)
            lineOctets = 1
        }
        out.append(code.toChar())
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

    // The `Appendable` output: runs of source characters between fold points are appended at once, and the octets of
    // every code point are counted exactly as `ByteLineWriter.text` writes them, so the fold points are the same.
    override fun text(value: CharSequence, from: Int, to: Int) {
        var octets = lineOctets
        var run = from
        var index = from
        while (index < to) {
            val code = value[index].code
            var chars = 1
            val length = if (code < UTF8_TWO_BYTES) {
                if (code < SPACE_CODE || code == DEL_CODE) checkValueCharacter(code, index)
                1
            } else if (code < UTF8_THREE_BYTES) {
                2
            } else if (code and SURROGATE_MASK != SURROGATE_BASE) {
                3
            } else {
                surrogatePair(value, index, to)
                chars = 2
                4
            }
            if (octets + length > LIMIT) {
                out.append(value, run, index)
                out.append(FOLD)
                run = index
                octets = 1
            }
            octets += length
            index += chars
        }
        out.append(value, run, to)
        lineOctets = octets
    }

    // Decodes the UTF-8 of the line into a reusable char buffer and appends it in chunks, with U+FFFD for each maximal
    // ill-formed subpart, as the tree decodes. A fold that splits a multi-byte sequence cannot be kept in UTF-16: the
    // fold (every consecutive one) is moved in front of the character, so the logical line is the same and the physical
    // line may grow by up to 3 octets.
    private fun appendRaw(source: ByteArray, from: Int, to: Int, upperCase: Boolean) {
        val chars = rawChars ?: CharArray(RAW_CHUNK).also { rawChars = it }
        var count = 0
        var index = from
        while (index < to) {
            if (count > RAW_CHUNK - RAW_MARGIN) {
                flushRaw(chars, count)
                count = 0
            }
            val lead = source[index].toInt() and 0xFF
            if (lead == CR_CODE || lead == LF_CODE) {
                index = putFold(chars, count, source, index, to)
                count += FOLD.length
                continue
            }
            index++
            if (lead < UTF8_TWO_BYTES) {
                val upper = upperCase && lead >= LOWER_A_CODE && lead <= LOWER_Z_CODE
                chars[count++] = (if (upper) lead - CASE_OFFSET else lead).toChar()
                continue
            }
            val info = utf8LeadInfo(lead)
            if (info == 0) {
                count = putDecoded(chars, count, REPLACEMENT_CODE_POINT)
                continue
            }
            var need = utf8LeadNeed(info)
            var low = utf8LeadLow(info)
            var high = utf8LeadHigh(info)
            var codePoint = lead and (0x7F ushr (need + 1))
            while (need > 0 && index < to) {
                var next = source[index].toInt() and 0xFF
                if (next == CR_CODE || next == LF_CODE) {
                    val after = afterFolds(source, index, to)
                    if (after >= to) break
                    next = source[after].toInt() and 0xFF
                    if (next < low || next > high) break
                    while (index < after) {
                        if (count > RAW_CHUNK - RAW_MARGIN) {
                            flushRaw(chars, count)
                            count = 0
                        }
                        index = putFold(chars, count, source, index, to)
                        count += FOLD.length
                    }
                }
                if (next < low || next > high) break
                codePoint = (codePoint shl 6) or (next and 0x3F)
                low = UTF8_CONTINUATION_LOW
                high = UTF8_CONTINUATION_HIGH
                need--
                index++
            }
            count = putDecoded(chars, count, if (need > 0) REPLACEMENT_CODE_POINT else codePoint)
        }
        flushRaw(chars, count)
        out.append(LINE_END)
        lineOctets = 0
    }

    private fun putFold(chars: CharArray, at: Int, source: ByteArray, lineBreak: Int, to: Int): Int {
        val fold = lineBreakEnd(source, lineBreak, to)
        chars[at] = CR_CODE.toChar()
        chars[at + 1] = LF_CODE.toChar()
        if (fold < to && isFoldByte(source[fold])) {
            chars[at + 2] = source[fold].toInt().toChar()
            return fold + 1
        }
        chars[at + 2] = SPACE_CODE.toChar()
        return fold
    }

    private fun afterFolds(source: ByteArray, from: Int, to: Int): Int {
        var index = from
        while (index < to && (source[index] == CR || source[index] == LF)) {
            val fold = lineBreakEnd(source, index, to)
            if (fold >= to || !isFoldByte(source[fold])) return to
            index = fold + 1
        }
        return index
    }

    private fun flushRaw(chars: CharArray, count: Int) {
        if (count == 0) return
        rawView.reset(chars, count)
        out.append(rawView, 0, count)
    }

    private companion object {
        private const val RAW_CHUNK = 1024
        private const val RAW_MARGIN = 16

        private fun lineBreakEnd(source: ByteArray, index: Int, to: Int): Int =
            if (source[index] == CR && index + 1 < to && source[index + 1] == LF) index + 2 else index + 1

        private fun isFoldByte(byte: Byte): Boolean = byte == SPACE || byte == HTAB
    }
}
