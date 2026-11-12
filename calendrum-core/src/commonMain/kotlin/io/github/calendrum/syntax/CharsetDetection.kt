package io.github.calendrum.syntax

import kotlin.math.pow

internal const val SIGNATURE_NONE: Int = 0
internal const val SIGNATURE_UTF8: Int = 1
internal const val SIGNATURE_UTF16LE: Int = 2
internal const val SIGNATURE_UTF16BE: Int = 3
internal const val SIGNATURE_UTF32LE: Int = 4
internal const val SIGNATURE_UTF32BE: Int = 5
internal const val SIGNATURE_UNSUPPORTED: Int = 6

// Byte order marks and signatures at the start of the input. UTF-32LE (FF FE 00 00) is checked before UTF-16LE (FF FE):
// an iCalendar file never starts with U+0000, so the longer mark wins.
internal fun signatureAt(input: ByteArray, from: Int, to: Int): Int {
    val available = to - from
    fun at(index: Int): Int = if (index < available) input[from + index].toInt() and 0xFF else -1
    val b0 = at(0)
    val b1 = at(1)
    val b2 = at(2)
    val b3 = at(3)
    return when {
        b0 == 0xEF && b1 == 0xBB && b2 == 0xBF -> SIGNATURE_UTF8
        b0 == 0xFF && b1 == 0xFE && b2 == 0 && b3 == 0 -> SIGNATURE_UTF32LE
        b0 == 0 && b1 == 0 && b2 == 0xFE && b3 == 0xFF -> SIGNATURE_UTF32BE
        b0 == 0xFF && b1 == 0xFE -> SIGNATURE_UTF16LE
        b0 == 0xFE && b1 == 0xFF -> SIGNATURE_UTF16BE
        unsupportedSignatureAt(input, from, to) != null -> SIGNATURE_UNSUPPORTED
        else -> SIGNATURE_NONE
    }
}

// The only signature of text encoded from a `String`: U+FEFF as a UTF-8 BOM.
internal fun textSignatureAt(input: ByteArray, from: Int, to: Int): Int = if (to - from >= 3 &&
    input[from] == UTF8_BOM_0 &&
    input[from + 1] == UTF8_BOM_1 &&
    input[from + 2] == UTF8_BOM_2
) {
    SIGNATURE_UTF8
} else {
    SIGNATURE_NONE
}

private const val UTF8_BOM_0: Byte = -0x11
private const val UTF8_BOM_1: Byte = -0x45
private const val UTF8_BOM_2: Byte = -0x41

internal fun signatureLength(signature: Int): Int = when (signature) {
    SIGNATURE_UTF8 -> 3
    SIGNATURE_UTF16LE, SIGNATURE_UTF16BE -> 2
    SIGNATURE_UTF32LE, SIGNATURE_UTF32BE -> 4
    else -> 0
}

internal fun signatureCharset(signature: Int): InputCharset = when (signature) {
    SIGNATURE_UTF16LE -> InputCharset.Utf16Le
    SIGNATURE_UTF16BE -> InputCharset.Utf16Be
    SIGNATURE_UTF32LE -> InputCharset.Utf32Le
    SIGNATURE_UTF32BE -> InputCharset.Utf32Be
    else -> InputCharset.Utf8
}

internal fun unsupportedSignatureAt(input: ByteArray, from: Int, to: Int): String? =
    UnsupportedSignatures.match(input, from, to)

// Encodings we do not decode at all but can recognize reliably at the start of the input (ICAL-S011): the byte order
// marks of UTF-7, UTF-1, UTF-EBCDIC, SCSU, BOCU-1 and GB18030, and `BEGIN` in EBCDIC (code pages 037, 500, 1047 agree
// on these letters). The table of first bytes rejects ordinary input with one lookup. The patterns are strings with one
// character per byte (`÷dL` is F7 64 4C) and the names a parallel array: parsing a packed text with `split`,
// `substringAfter` and `toInt(16)` took 4.2 KB of code on iOS and pulled in the string splitting of the standard
// library.
private object UnsupportedSignatures {
    private val names: Array<String> = arrayOf(
        "UTF-7", "UTF-7", "UTF-7", "UTF-7", "UTF-1", "UTF-EBCDIC", "SCSU", "BOCU-1", "GB18030", "EBCDIC",
    )
    private val patterns: Array<String> = arrayOf(
        "+/v8",
        "+/v9",
        "+/v+",
        "+/v/",
        "\u00F7dL",
        "\u00DDsfs",
        "\u000E\u00FE\u00FF",
        "\u00FB\u00EE(",
        "\u00841\u00953",
        "\u00C2\u00C5\u00C7\u00C9\u00D5",
    )
    private val firstBytes = BooleanArray(BYTE_VALUES)

    init {
        for (pattern in patterns) firstBytes[pattern[0].code] = true
    }

    fun match(input: ByteArray, from: Int, to: Int): String? {
        if (from >= to || !firstBytes[input[from].toInt() and 0xFF]) return null
        for (index in patterns.indices) {
            val pattern = patterns[index]
            if (to - from < pattern.length) continue
            var matched = true
            for (k in pattern.indices) {
                if (input[from + k].toInt() and 0xFF != pattern[k].code) {
                    matched = false
                    break
                }
            }
            if (matched) return names[index]
        }
        return null
    }

    private const val BYTE_VALUES = 256
}

internal fun firstNonAscii(input: ByteArray, from: Int, to: Int): Int {
    for (i in from until to) {
        if (input[i] < 0) return i
    }
    return -1
}

// The cheap test that keeps valid UTF-8 input free: from the first non-ASCII byte, the first 4 multi-byte sequences
// within 64 bytes must be valid UTF-8. A single-byte text almost never passes: in windows-1251 and KOI8-R a lead byte
// is valid only before 0x80..0xBF, which are rare letters and symbols there.
internal fun utf8WindowValid(input: ByteArray, from: Int, to: Int): Boolean {
    val windowEnd = minOf(to, from + UTF8_WINDOW_BYTES)
    var valid = 0
    var i = from
    while (i < windowEnd && valid < UTF8_WINDOW_SEQUENCES) {
        if (input[i] >= 0) {
            i++
            continue
        }
        val next = utf8SequenceEnd(input, i, to)
        if (next < 0) return false
        valid++
        i = next
    }
    return true
}

// The rule for mixed input: up to 4096 sequences from the first non-ASCII byte; UTF-8 stays if there is at least one
// valid multi-byte sequence and at least two valid ones per invalid one. One broken sequence in a UTF-8 file therefore
// never switches the whole file to a single-byte encoding.
internal fun isUtf8ByData(input: ByteArray, from: Int, to: Int): Boolean {
    var valid = 0
    var invalid = 0
    var i = from
    while (i < to && valid + invalid < UTF8_EVIDENCE_SEQUENCES) {
        if (input[i] >= 0) {
            i++
            continue
        }
        val next = utf8SequenceEnd(input, i, to)
        if (next < 0) {
            invalid++
            i = -next
        } else {
            valid++
            i = next
        }
    }
    return valid > 0 && valid >= UTF8_VALID_PER_INVALID * invalid
}

// A sequence may continue after a fold (a line ending plus SPACE or HTAB), as the lexer joins it; a line ending without
// a fold ends it as invalid. The negative result encodes the index where the invalid part ends.
private fun utf8SequenceEnd(input: ByteArray, lead: Int, to: Int): Int {
    val info = utf8LeadInfo(input[lead].toInt() and 0xFF)
    if (info == 0) return -(lead + 1)
    var need = utf8LeadNeed(info)
    var low = utf8LeadLow(info)
    var high = utf8LeadHigh(info)
    var i = lead + 1
    while (need > 0) {
        i = skipFold(input, i, to)
        if (i >= to) return -i
        val byte = input[i].toInt() and 0xFF
        if (byte < low || byte > high) return -i
        low = UTF8_CONTINUATION_LOW
        high = UTF8_CONTINUATION_HIGH
        need--
        i++
    }
    return i
}

private fun skipFold(input: ByteArray, index: Int, to: Int): Int {
    var i = index
    while (i < to) {
        val byte = input[i]
        if (byte != CR && byte != LF) return i
        var next = i + 1
        if (byte == CR && next < to && input[next] == LF) next++
        if (next >= to || (input[next] != SPACE && input[next] != HTAB)) return i
        i = next + 1
    }
    return i
}

internal const val DETECTION_WINDOW: Int = 64 * 1024

internal fun detectionEnd(first: Int, to: Int): Int = if (to - first >
    DETECTION_WINDOW
) {
    first + DETECTION_WINDOW
} else {
    to
}

private const val UTF8_WINDOW_BYTES = 64
private const val UTF8_WINDOW_SEQUENCES = 4
private const val UTF8_EVIDENCE_SEQUENCES = 4096
private const val UTF8_VALID_PER_INVALID = 2

// Byte order of a UTF-16/32 hint without a byte order mark: iCalendar text is mostly ASCII, so zero bytes sit at fixed
// positions of every code unit (odd ones in UTF-16LE, the upper three of four in UTF-32LE for ASCII and the upper two
// for the BMP). The first 1024 bytes decide; null means no clear pattern.
internal fun wideCharsetByZeros(input: ByteArray, from: Int, to: Int): InputCharset? {
    val end = minOf(to, from + WIDE_SAMPLE_BYTES)
    val zeros = IntArray(4)
    for (i in from until end) {
        if (input[i].toInt() == 0) zeros[(i - from) and 3]++
    }
    val groups = (end - from) / 4
    if (zeros[0] + zeros[1] + zeros[2] + zeros[3] == 0) return null
    if (groups > 0 && zeros[3] >= groups && 2 * zeros[2] >= groups) return InputCharset.Utf32Le
    if (groups > 0 && zeros[0] >= groups && 2 * zeros[1] >= groups) return InputCharset.Utf32Be
    val even = zeros[0] + zeros[2]
    val odd = zeros[1] + zeros[3]
    return when {
        odd > 2 * even -> InputCharset.Utf16Le
        even > 2 * odd -> InputCharset.Utf16Be
        else -> null
    }
}

internal const val WIDE_SAMPLE_BYTES: Int = 1024

internal fun zeroInSignature(input: ByteArray, from: Int, to: Int): Boolean {
    for (i in from until minOf(to, from + SIGNATURE_BYTES)) {
        if (input[i].toInt() == 0) return true
    }
    return false
}

internal fun wideCharsetWithoutSignature(input: ByteArray, from: Int, to: Int): InputCharset? {
    if (!zeroInSignature(input, from, to)) return null
    val end = minOf(to, from + WIDE_SAMPLE_BYTES)
    var zeros = 0
    for (i in from until end) {
        if (input[i].toInt() == 0) zeros++
    }
    if (ZERO_SHARE * zeros < end - from) return null
    return wideCharsetByZeros(input, from, to)
}

internal const val SIGNATURE_BYTES: Int = 4
private const val ZERO_SHARE = 4

internal fun containsZeroByte(input: ByteArray, from: Int, to: Int): Boolean {
    for (i in from until minOf(to, from + WIDE_SAMPLE_BYTES)) {
        if (input[i].toInt() == 0) return true
    }
    return false
}

// The heuristic starts at the start of the word that holds the first non-ASCII byte, so `Café` is seen as one word; the
// word is ASCII and at most 64 bytes back.
internal fun wordStart(input: ByteArray, from: Int, index: Int): Int {
    var start = index
    while (start > from && start > index - MAX_WORD_PREFIX) {
        val byte = input[start - 1].toInt()
        if (byte !in UPPER_A_CODE..UPPER_Z_CODE && byte !in LOWER_A_CODE..LOWER_Z_CODE) break
        start--
    }
    return start
}

private const val MAX_WORD_PREFIX = 64
private const val UPPER_A_CODE = 0x41
private const val UPPER_Z_CODE = 0x5A
private const val LOWER_A_CODE = 0x61
private const val LOWER_Z_CODE = 0x7A

internal class SingleByteGuess(val charset: InputCharset, val confidence: Int, private val posteriors: IntArray) {
    fun posterior(charset: InputCharset): Int {
        val index = SingleByteHeuristic.candidates.indexOf(charset)
        return if (index < 0) 0 else posteriors[index]
    }
}

// Chooses among windows-1251, KOI8-R and windows-1252 by a cost in bits (lower is more plausible), computed per word, a
// run of ASCII letters and letters of the candidate. Per word: the case pattern (lower 0.5, capitalized 2, all upper
// 2.5, mixed 9: reading 1251 as KOI8-R or back swaps the case of every letter), for each letter of a Cyrillic candidate
// -log2 of its frequency in Russian (Russian National Corpus figures) plus the vowel, consonant, soft or hard sign and
// short i transitions, for windows-1252 three tiers of accented letters. A Cyrillic letter next to a Latin one costs 8;
// a windows-1252 word made only of accented letters costs 5 per letter. Symbols cost 4, impossible bytes (undefined,
// KOI8-R box drawing) 14, KOI8-R gets 1 as the rarer encoding. All values are in tenths of a bit.
internal object SingleByteHeuristic {
    val candidates: Array<InputCharset> =
        arrayOf(InputCharset.Windows1251, InputCharset.Windows1252, InputCharset.Koi8R)

    // The per-byte data of the heuristic as packed text, one character for each byte 0x80..0xFF of the encoding: `kind
    // or (sound shl 2) or (cost shl 5)`. Kind: 0 impossible, 1 symbol, 2 upper-case letter, 3 lower-case letter; sound
    // (Cyrillic letters): 0 vowel, 1 consonant, 2 sign, 3 short i, 4 other; cost: tenths of a bit of the letter
    // (frequency in Russian for Cyrillic letters, 110 for the rare ones; 40, 65 and 90 for the three tiers of accented
    // Latin letters). They used to be computed on first use with `isLetter`, `isUpperCase` and `lowercaseChar`, which
    // brought the Unicode category and case tables into the binary (`CharCategory` alone 5.8 KB of code on iOS, besides
    // its tables). `CharsetTablesTest` recomputes every entry from the old definition (the symbol list, the Russian
    // letters, vowels and frequencies) and compares.
    private const val CLASSES_WINDOWS_1251: String =
        "\u0DD2\u0DD2\u0011\u0DD3\u0011\u0011\u0011\u0011\u0011\u0011\u0DD2\u0011\u0DD2\u0DD2\u0DD2\u0DD2" +
            "\u0DD3\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0010\u0011\u0DD3\u0011\u0DD3\u0DD3\u0DD3\u0DD3" +
            "\u0011\u0DD2\u0DD3\u0DD2\u0011\u0DD2\u0011\u0011\u0E22\u0011\u0DD2\u0011\u0010\u0011\u0011\u0DD2" +
            "\u0011\u0011\u0DD2\u0DD3\u0DD3\u0011\u0011\u0011\u0E23\u0011\u0DD3\u0011\u0DD3\u0DD2\u0DD3\u0DD3" +
            "\u0482\u0786\u05A6\u0766\u0666\u0482\u0866\u0766\u04C2\u080E\u0606\u05A6\u0646\u04E6\u0402\u0686" +
            "\u0586\u0546\u0506\u06A2\u0AC6\u0866\u09A6\u07A6\u08E6\u0A26\u0E2A\u0722\u074A\u0A62\u0922\u0702" +
            "\u0483\u0787\u05A7\u0767\u0667\u0483\u0867\u0767\u04C3\u080F\u0607\u05A7\u0647\u04E7\u0403\u0687" +
            "\u0587\u0547\u0507\u06A3\u0AC7\u0867\u09A7\u07A7\u08E7\u0A27\u0E2B\u0723\u074B\u0A63\u0923\u0703"

    private const val CLASSES_WINDOWS_1252: String =
        "\u0011\u0010\u0011\u0B53\u0011\u0011\u0011\u0011\u0011\u0011\u0832\u0011\u0832\u0010\u0832\u0010" +
            "\u0010\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0833\u0011\u0833\u0010\u0833\u0832" +
            "\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0010\u0011\u0011\u0011" +
            "\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011\u0011" +
            "\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0512\u0832\u0512\u0512\u0512" +
            "\u0832\u0512\u0832\u0512\u0512\u0512\u0512\u0011\u0512\u0512\u0512\u0512\u0512\u0B52\u0B52\u0513" +
            "\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0513\u0833\u0513\u0513\u0513" +
            "\u0833\u0513\u0833\u0513\u0513\u0513\u0513\u0011\u0513\u0513\u0513\u0513\u0513\u0B53\u0B53\u0833"

    private const val CLASSES_KOI8_R: String =
        "\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010" +
            "\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0011\u0010\u0011\u0011\u0011\u0011" +
            "\u0010\u0010\u0010\u0E23\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010" +
            "\u0010\u0010\u0010\u0E22\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0010\u0011" +
            "\u0923\u0483\u0787\u09A7\u0667\u0483\u0AC7\u0767\u0867\u04C3\u080F\u0607\u05A7\u0647\u04E7\u0403" +
            "\u0687\u0703\u0587\u0547\u0507\u06A3\u0867\u05A7\u074B\u0723\u0767\u08E7\u0A63\u0A27\u07A7\u0E2B" +
            "\u0922\u0482\u0786\u09A6\u0666\u0482\u0AC6\u0766\u0866\u04C2\u080E\u0606\u05A6\u0646\u04E6\u0402" +
            "\u0686\u0702\u0586\u0546\u0506\u06A2\u0866\u05A6\u074A\u0722\u0766\u08E6\u0A62\u0A26\u07A6\u0E2A"

    private val classes: Array<String> = arrayOf(CLASSES_WINDOWS_1251, CLASSES_WINDOWS_1252, CLASSES_KOI8_R)

    private val phoneticStart: IntArray = intArrayOf(16, 7, 120, 60, 30)
    private val phoneticTransition: IntArray = intArrayOf(
        28, 6, 100, 30, 30,
        7, 18, 33, 100, 30,
        15, 12, 120, 100, 30,
        40, 10, 120, 120, 30,
        30, 30, 30, 30, 30,
    )

    internal val kinds: Array<ByteArray> = Array(candidates.size) { ByteArray(HIGH_BYTES) }
    internal val costs: Array<IntArray> = Array(candidates.size) { IntArray(HIGH_BYTES) }
    internal val phonetic: Array<ByteArray> = Array(candidates.size) { ByteArray(HIGH_BYTES) }
    private val cyrillic: BooleanArray = booleanArrayOf(true, false, true)
    private val priors: IntArray = intArrayOf(0, 0, 10)

    init {
        for (index in candidates.indices) {
            val packed = classes[index]
            for (byte in 0 until HIGH_BYTES) {
                val entry = packed[byte].code
                kinds[index][byte] = (entry and KIND_MASK).toByte()
                phonetic[index][byte] = ((entry ushr PHONETIC_SHIFT) and PHONETIC_MASK).toByte()
                costs[index][byte] = entry ushr COST_SHIFT
            }
        }
    }

    // Confidence is the posterior probability with a temperature of 1.5, where candidates that decode every byte seen
    // the same way count together: if only dashes and quotes occur, windows-1251 and windows-1252 give the same text
    // and the choice does not matter.
    fun guess(input: ByteArray, from: Int, to: Int): SingleByteGuess {
        val end = evidenceEnd(input, from, to)
        val scores = IntArray(candidates.size) { score(input, from, end, it) + priors[it] }
        var best = 0
        for (index in 1 until candidates.size) {
            if (scores[index] < scores[best]) best = index
        }
        val weights = DoubleArray(candidates.size) {
            2.0.pow(-(scores[it] - scores[best]) / (COST_SCALE * TEMPERATURE))
        }
        val total = weights.sum()
        val posteriors = IntArray(candidates.size)
        for (index in candidates.indices) {
            var group = 0.0
            for (other in candidates.indices) {
                if (sameDecoding(input, from, end, index, other)) group += weights[other]
            }
            posteriors[index] = (group / total * PERCENT).toInt()
        }
        return SingleByteGuess(candidates[best], posteriors[best], posteriors)
    }

    private fun sameDecoding(input: ByteArray, from: Int, to: Int, first: Int, second: Int): Boolean {
        if (first == second) return true
        val a = SingleByteCharsets.table(candidates[first])
        val b = SingleByteCharsets.table(candidates[second])
        for (i in from until to) {
            val byte = input[i].toInt()
            if (byte < 0 && a[byte + HIGH_BYTES] != b[byte + HIGH_BYTES]) return false
        }
        return true
    }

    // At most 8192 bytes >= 0x80 plus the rest of that physical line (never more than 32768), so a large file costs
    // microseconds.
    private fun evidenceEnd(input: ByteArray, from: Int, to: Int): Int {
        var high = 0
        var i = from
        while (i < to) {
            val byte = input[i]
            if (byte < 0) {
                if (++high >= MAX_EVIDENCE_BYTES) return i + 1
            } else if ((byte == CR || byte == LF) && high >= EVIDENCE_BYTES) {
                return i
            }
            i++
        }
        return to
    }

    // A backslash escape is not part of a word (`\nПривет` starts a Cyrillic word), and a fold inside a word is
    // transparent.
    private fun score(input: ByteArray, from: Int, to: Int, candidate: Int): Int {
        val kind = kinds[candidate]
        val cost = costs[candidate]
        val sound = phonetic[candidate]
        val isCyrillic = cyrillic[candidate]
        var total = 0
        var i = from
        while (i < to) {
            val byte = input[i].toInt()
            if (byte >= 0) {
                if (byte == BACKSLASH) {
                    i += 2
                    continue
                }
                if (!isAsciiLetter(byte)) {
                    i++
                    continue
                }
            } else {
                val k = kind[byte + HIGH_BYTES].toInt()
                if (k == KIND_BAD || k == KIND_SYMBOL) {
                    total += if (k == KIND_BAD) BAD_COST else SYMBOL_COST
                    i++
                    continue
                }
            }
            var letters = 0
            var highLetters = 0
            var upperFirst = false
            var upperRest = 0
            var letterCost = 0
            var phoneticCost = 0
            var previousSound = -1
            while (i < to) {
                val b = input[i].toInt()
                val upper: Boolean
                if (b >= 0) {
                    if (b == CR_CODE || b == LF_CODE) {
                        val next = foldEnd(input, i, to)
                        if (next == i) break
                        i = next
                        continue
                    }
                    if (!isAsciiLetter(b)) break
                    upper = b <= UPPER_Z_CODE
                    previousSound = -1
                } else {
                    val k = kind[b + HIGH_BYTES].toInt()
                    if (k != KIND_UPPER && k != KIND_LOWER) break
                    upper = k == KIND_UPPER
                    highLetters++
                    letterCost += cost[b + HIGH_BYTES]
                    if (isCyrillic) {
                        val s = sound[b + HIGH_BYTES].toInt()
                        if (s == PHONETIC_OTHER) {
                            previousSound = -1
                        } else {
                            phoneticCost += if (previousSound < 0) {
                                phoneticStart[s]
                            } else {
                                phoneticTransition[previousSound * PHONETIC_KINDS + s]
                            }
                            previousSound = s
                        }
                    }
                }
                if (letters == 0) {
                    upperFirst = upper
                } else if (upper) {
                    upperRest++
                }
                letters++
                i++
            }
            if (highLetters == 0) continue
            total += when {
                !upperFirst && upperRest == 0 -> WORD_LOWER_COST
                upperFirst && upperRest == 0 -> WORD_CAPITALIZED_COST
                upperFirst && upperRest == letters - 1 -> WORD_UPPER_COST
                else -> WORD_MIXED_COST
            }
            total += letterCost
            val asciiLetters = letters - highLetters
            if (isCyrillic) {
                total += if (asciiLetters > 0) SCRIPT_MIX_COST else phoneticCost
            } else if (asciiLetters == 0) {
                total += if (highLetters == 1) LATIN_SINGLE_COST else LATIN_PURE_COST * highLetters
            }
        }
        return total
    }

    private fun foldEnd(input: ByteArray, index: Int, to: Int): Int {
        var next = index + 1
        if (input[index] == CR && next < to && input[next] == LF) next++
        return if (next < to && (input[next] == SPACE || input[next] == HTAB)) next + 1 else index
    }

    private fun isAsciiLetter(byte: Int): Boolean =
        byte in UPPER_A_CODE..UPPER_Z_CODE || byte in LOWER_A_CODE..LOWER_Z_CODE

    private const val KIND_BAD = 0
    private const val KIND_SYMBOL = 1
    private const val KIND_UPPER = 2
    private const val KIND_LOWER = 3

    private const val PHONETIC_OTHER = 4
    private const val PHONETIC_KINDS = 5

    private const val KIND_MASK = 3
    private const val PHONETIC_SHIFT = 2
    private const val PHONETIC_MASK = 7
    private const val COST_SHIFT = 5

    private const val COST_SCALE = 10
    private const val TEMPERATURE = 1.5
    private const val PERCENT = 100
    private const val EVIDENCE_BYTES = 8192
    private const val MAX_EVIDENCE_BYTES = 4 * EVIDENCE_BYTES

    private const val BAD_COST = 140
    private const val SYMBOL_COST = 40
    private const val SCRIPT_MIX_COST = 80
    private const val WORD_LOWER_COST = 5
    private const val WORD_CAPITALIZED_COST = 20
    private const val WORD_UPPER_COST = 25
    private const val WORD_MIXED_COST = 90
    private const val LATIN_PURE_COST = 50
    private const val LATIN_SINGLE_COST = 20

    private const val BACKSLASH = 0x5C
    private const val CR_CODE = 0x0D
    private const val LF_CODE = 0x0A
}

internal const val LOW_CONFIDENCE: Int = 90
