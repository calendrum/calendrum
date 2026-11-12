@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.SourceSpan
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Result of `decodeInput`, shared with the fuzz module: UTF-8 bytes in `bytes[offset, offset + length)` (the input
// itself, not a copy, when it is UTF-8), the canonical name of the encoding (null when the signature is unsupported),
// where it comes from and, for the heuristic, the confidence in percent; 100 otherwise.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class DecodedInput internal constructor(
    public val bytes: ByteArray,
    public val offset: Int,
    public val length: Int,
    public val charset: String?,
    public val source: Int,
    public val confidence: Int,
) {
    override fun toString(): String = "DecodedInput($charset, source $source, confidence $confidence, $length bytes)"

    public companion object {
        public const val SOURCE_UTF8: Int = 0
        public const val SOURCE_BOM: Int = 1
        public const val SOURCE_HINT: Int = 2
        public const val SOURCE_HEURISTIC: Int = 3
    }
}

// The whole decoder at once, with the same functions the lexer uses lazily, so the decoded bytes, the choice and the
// problems equal those of `ContentLexer(bytes, charset, …)`; a UTF-8 BOM stays in the output for the lexer to skip.
// Problems of the heuristic are reported at the first non-ASCII byte, with its line and column.

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun decodeInput(input: ByteArray, charset: String?, onProblem: ProblemListener): DecodedInput {
    val problems = DecodingProblems { code, offset, line, column, detail ->
        val position = offset.toLong()
        onProblem.onProblem(Problem(code, SourceSpan(position, position, line, column), detail))
    }
    val opening = openInput(input, 0, input.size, detect = true, hint = charset, problems)
    if (opening.kind == OPEN_UNSUPPORTED) {
        return DecodedInput(ByteArray(0), 0, 0, null, DecodedInput.SOURCE_BOM, FULL_CONFIDENCE)
    }
    if (opening.kind == OPEN_TRANSCODED) {
        val choice = opening.choice
        val transcoded = transcode(choice.charset, input, opening.from, opening.from, opening.to, 0)
        return DecodedInput(transcoded.bytes, 0, transcoded.end, choice.name, choice.source, choice.confidence)
    }
    val first = if (opening.lazy) firstNonAscii(input, opening.from, opening.to) else -1
    if (first < 0) {
        val choice = opening.choice
        return DecodedInput(
            input,
            opening.from,
            opening.to - opening.from,
            choice.name,
            choice.source,
            choice.confidence,
        )
    }
    var line = 1
    var lineStart = opening.from
    var i = opening.from
    while (i < first) {
        val byte = input[i++]
        if (byte == CR && i < first && input[i] == LF) i++
        if (byte == CR || byte == LF) {
            line++
            lineStart = i
        }
    }
    val offset = first - opening.from
    val column = first - lineStart + 1
    val assumed = opening.choice
    val choice = if (assumed.charset == InputCharset.Utf8) {
        val detected = detectAt(input, opening.from, first, opening.to, assumed)
        if (detected.charset != InputCharset.Utf8) reportDetection(detected, assumed, problems, offset, line, column)
        detected
    } else {
        decideHinted(input, opening.from, first, opening.to, assumed, problems, offset, line, column)
    }
    if (choice.charset == InputCharset.Utf8) {
        return DecodedInput(
            input,
            opening.from,
            opening.to - opening.from,
            choice.name,
            choice.source,
            choice.confidence,
        )
    }
    val transcoded = transcode(choice.charset, input, opening.from, first, opening.to, 0)
    return DecodedInput(transcoded.bytes, 0, transcoded.end, choice.name, choice.source, choice.confidence)
}

internal fun interface DecodingProblems {
    fun report(code: ProblemCode, offset: Int, line: Int, column: Int, detail: String?)
}

internal class CharsetChoice(val charset: InputCharset, val source: Int, val confidence: Int) {
    val name: String get() = charset.canonicalName

    val detail: String
        get() = when (source) {
            DecodedInput.SOURCE_BOM -> "${charset.canonicalName}, byte order mark"
            DecodedInput.SOURCE_HINT -> "${charset.canonicalName}, charset hint"
            DecodedInput.SOURCE_HEURISTIC -> "${charset.canonicalName}, confidence $confidence%"
            else -> charset.canonicalName
        }
}

internal const val OPEN_READ: Int = 0
internal const val OPEN_TRANSCODED: Int = 1
internal const val OPEN_UNSUPPORTED: Int = 2
internal const val FULL_CONFIDENCE: Int = 100

internal class InputOpening(val kind: Int, val from: Int, val to: Int, val lazy: Boolean, val choice: CharsetChoice)

// Steps 1 and 2 of the detection: the signature, then the hint. The UTF-8 entry points (`detect == false`) stop after
// the signature. The result either reads the input as it is (with the lazy decision still pending) or carries the
// transcoded buffer.
internal fun openInput(
    input: ByteArray,
    from: Int,
    to: Int,
    detect: Boolean,
    hint: String?,
    problems: DecodingProblems,
): InputOpening {
    val signature = signatureAt(input, from, to)
    if (signature == SIGNATURE_UNSUPPORTED) {
        problems.report(ProblemCode.UnsupportedEncoding, 0, 1, 1, unsupportedSignatureAt(input, from, to))
        return InputOpening(
            OPEN_UNSUPPORTED,
            from,
            to,
            false,
            CharsetChoice(InputCharset.Utf8, DecodedInput.SOURCE_BOM, 0),
        )
    }
    val hinted = if (detect && hint != null) charsetForLabel(hint) else null
    if (detect && hint != null && hinted == null) problems.report(ProblemCode.UnknownCharsetHint, 0, 1, 1, null)
    if (signature != SIGNATURE_NONE) {
        val charset = signatureCharset(signature)
        if (hinted != null && !hintAgrees(hinted, charset)) {
            problems.report(ProblemCode.CharsetHintContradictsData, 0, 1, 1, "hint ${hinted.canonicalName}")
        }
        val choice = CharsetChoice(charset, DecodedInput.SOURCE_BOM, FULL_CONFIDENCE)
        if (charset == InputCharset.Utf8) return InputOpening(OPEN_READ, from, to, false, choice)
        return transcoded(from + signatureLength(signature), to, choice, problems)
    }
    if (!detect) {
        return InputOpening(
            OPEN_READ,
            from,
            to,
            false,
            CharsetChoice(InputCharset.Utf8, DecodedInput.SOURCE_UTF8, FULL_CONFIDENCE),
        )
    }
    if (hinted == null || !hinted.isWide) {
        val wide = wideCharsetWithoutSignature(input, from, to)
        if (wide != null) {
            if (hinted != null) {
                problems.report(ProblemCode.CharsetHintContradictsData, 0, 1, 1, "hint ${hinted.canonicalName}")
            }
            val choice = CharsetChoice(wide, DecodedInput.SOURCE_HEURISTIC, FULL_CONFIDENCE)
            reportDetection(choice, null, problems, 0, 1, 1)
            return InputOpening(OPEN_TRANSCODED, from, to, false, choice)
        }
    }
    return when {
        hinted == null -> lazyOpening(from, to, DecodedInput.SOURCE_UTF8)
        hinted == InputCharset.Utf8 -> lazyOpening(from, to, DecodedInput.SOURCE_HINT)
        hinted.isWide -> openWide(input, from, to, hinted, problems)
        else -> openSingleByte(input, from, to, hinted, problems)
    }
}

private fun lazyOpening(from: Int, to: Int, source: Int): InputOpening =
    InputOpening(OPEN_READ, from, to, true, CharsetChoice(InputCharset.Utf8, source, FULL_CONFIDENCE))

private fun hintAgrees(hinted: InputCharset, actual: InputCharset): Boolean = hinted == actual ||
    (hinted == InputCharset.Utf16 && (actual == InputCharset.Utf16Le || actual == InputCharset.Utf16Be)) ||
    (hinted == InputCharset.Utf32 && (actual == InputCharset.Utf32Le || actual == InputCharset.Utf32Be))

private fun transcoded(from: Int, to: Int, choice: CharsetChoice, problems: DecodingProblems): InputOpening {
    problems.report(ProblemCode.TranscodedInput, 0, 1, 1, choice.detail)
    return InputOpening(OPEN_TRANSCODED, from, to, false, choice)
}

// No zero byte at all means the data is not UTF-16 or UTF-32 and the hint is ignored (ICAL-S027); a clear zero pattern
// of another order or width wins over the hint; an unclear one trusts the hint, big-endian for an unmarked one (RFC
// 2781 §4.3).
private fun openWide(
    input: ByteArray,
    from: Int,
    to: Int,
    hinted: InputCharset,
    problems: DecodingProblems,
): InputOpening {
    if (!containsZeroByte(input, from, to)) {
        problems.report(ProblemCode.CharsetHintContradictsData, 0, 1, 1, "hint ${hinted.canonicalName}")
        return lazyOpening(from, to, DecodedInput.SOURCE_UTF8)
    }
    val actual = wideCharsetByZeros(input, from, to) ?: when (hinted) {
        InputCharset.Utf16 -> InputCharset.Utf16Be
        InputCharset.Utf32 -> InputCharset.Utf32Be
        else -> hinted
    }
    if (hintAgrees(hinted, actual)) {
        return transcoded(from, to, CharsetChoice(actual, DecodedInput.SOURCE_HINT, FULL_CONFIDENCE), problems)
    }
    problems.report(ProblemCode.CharsetHintContradictsData, 0, 1, 1, "hint ${hinted.canonicalName}")
    val choice = CharsetChoice(actual, DecodedInput.SOURCE_HEURISTIC, FULL_CONFIDENCE)
    reportDetection(choice, null, problems, 0, 1, 1)
    return InputOpening(OPEN_TRANSCODED, from, to, false, choice)
}

// A single-byte hint is trusted unless the data contradicts it: UTF-8 by the evidence rule, or a confident heuristic
// that gives the hinted encoding less than 1%.
private fun openSingleByte(
    input: ByteArray,
    from: Int,
    to: Int,
    hinted: InputCharset,
    problems: DecodingProblems,
): InputOpening {
    val hintChoice = CharsetChoice(hinted, DecodedInput.SOURCE_HINT, FULL_CONFIDENCE)
    val window = detectionEnd(from, to)
    val first = firstNonAscii(input, from, window)
    if (first < 0) return InputOpening(OPEN_READ, from, to, window < to, hintChoice)
    if (!hintContradicted(input, from, first, to, hinted)) {
        problems.report(ProblemCode.TranscodedInput, 0, 1, 1, hintChoice.detail)
        return InputOpening(OPEN_TRANSCODED, from, to, false, hintChoice)
    }
    problems.report(ProblemCode.CharsetHintContradictsData, 0, 1, 1, "hint ${hinted.canonicalName}")
    val choice =
        detectAt(input, from, first, to, CharsetChoice(InputCharset.Utf8, DecodedInput.SOURCE_UTF8, FULL_CONFIDENCE))
    if (choice.charset == InputCharset.Utf8) return InputOpening(OPEN_READ, from, to, false, choice)
    reportDetection(choice, null, problems, 0, 1, 1)
    return InputOpening(OPEN_TRANSCODED, from, to, false, choice)
}

private fun hintContradicted(input: ByteArray, from: Int, first: Int, to: Int, hinted: InputCharset): Boolean {
    val end = detectionEnd(first, to)
    if (isUtf8ByData(input, first, end)) return true
    val guess = SingleByteHeuristic.guess(input, wordStart(input, from, first), end)
    return guess.posterior(hinted) < IMPLAUSIBLE_HINT && guess.confidence >= LOW_CONFIDENCE
}

internal fun decideHinted(
    input: ByteArray,
    from: Int,
    first: Int,
    to: Int,
    hinted: CharsetChoice,
    problems: DecodingProblems,
    offset: Int,
    line: Int,
    column: Int,
): CharsetChoice {
    if (!hintContradicted(input, from, first, to, hinted.charset)) {
        problems.report(ProblemCode.TranscodedInput, offset, line, column, hinted.detail)
        return hinted
    }
    problems.report(ProblemCode.CharsetHintContradictsData, offset, line, column, "hint ${hinted.name}")
    val choice =
        detectAt(input, from, first, to, CharsetChoice(InputCharset.Utf8, DecodedInput.SOURCE_UTF8, FULL_CONFIDENCE))
    if (choice.charset != InputCharset.Utf8) reportDetection(choice, null, problems, offset, line, column)
    return choice
}

// Steps 3 and 4: UTF-8 window, evidence rule, then the heuristic from the start of the word.
internal fun detectAt(input: ByteArray, from: Int, first: Int, to: Int, assumed: CharsetChoice): CharsetChoice {
    val end = detectionEnd(first, to)
    if (utf8WindowValid(input, first, end) || isUtf8ByData(input, first, end)) return assumed
    val guess = SingleByteHeuristic.guess(input, wordStart(input, from, first), end)
    return CharsetChoice(guess.charset, DecodedInput.SOURCE_HEURISTIC, guess.confidence)
}

internal fun reportDetection(
    choice: CharsetChoice,
    assumed: CharsetChoice?,
    problems: DecodingProblems,
    offset: Int,
    line: Int,
    column: Int,
) {
    if (assumed != null && assumed.source == DecodedInput.SOURCE_HINT) {
        problems.report(ProblemCode.CharsetHintContradictsData, offset, line, column, "hint ${assumed.name}")
    }
    problems.report(ProblemCode.DetectedCharset, offset, line, column, choice.detail)
    if (choice.confidence <
        LOW_CONFIDENCE
    ) {
        problems.report(ProblemCode.LowCharsetConfidence, offset, line, column, null)
    }
}

private const val IMPLAUSIBLE_HINT = 1
