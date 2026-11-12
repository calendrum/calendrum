@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.ByteInput
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.Severity
import io.github.calendrum.SourceSpan
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Byte-level lexer for iCalendar content lines (RFC 5545 §3.1), reached by other Calendrum modules through
// `@InternalICalApi`. Pull style: `next()` returns the next logical line or `null` at the end. The returned
// `ContentLine` is reused and is valid only until the next call. Offsets in lines and problems are relative to
// `offset`, the first byte of the range. Problems go to `sink` as they are found; `problemCount` counts every
// occurrence, including those not reported.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ContentLexer internal constructor(
    input: ByteArray,
    offset: Int,
    length: Int,
    private val limits: ParseLimits,
    private val sink: ProblemListener,
    private val detect: Boolean,
    private val charsetHint: String?,
) {
    internal constructor(
        input: ByteArray,
        offset: Int,
        length: Int,
        limits: ParseLimits,
        sink: ProblemListener,
    ) : this(input, offset, length, limits, sink, false, null)

    public constructor(
        input: ByteArray,
        limits: ParseLimits,
        sink: ProblemListener,
    ) : this(input, 0, input.size, limits, sink, false, null)

    public constructor(
        input: ByteArray,
        charset: String?,
        limits: ParseLimits,
        sink: ProblemListener,
    ) : this(input, 0, input.size, limits, sink, true, charset)

    internal constructor(
        stream: ByteInput,
        blockSize: Int,
        limits: ParseLimits,
        sink: ProblemListener,
        detect: Boolean,
        charsetHint: String?,
    ) : this(ByteArray(0), 0, 0, limits, sink, detect, charsetHint) {
        require(blockSize > 0) { "blockSize must be positive" }
        this.stream = stream
        this.blockSize = blockSize
        window = object : StreamWindow {
            override fun prepareLine() = this@ContentLexer.prepareLine()

            override fun dropLongLine() = this@ContentLexer.dropLongLine()

            override fun ensureAhead(index: Int, count: Int) = this@ContentLexer.ensureAhead(index, count)

            override fun startTranscoded(charset: InputCharset, from: Int) =
                this@ContentLexer.startStreamTranscoded(charset, from)

            override fun transcodeRest(charset: InputCharset, index: Int) =
                this@ContentLexer.transcodeStreamRest(charset, index)
        }
    }

    private var input = input
    private var base = offset
    private var end = offset + length
    private var position = offset
    private var physicalLine = 1
    private var lineStart = offset
    private var started = false
    private var finished = false
    private var atInputStart = true

    internal val line: ContentLine = ContentLine()
    private var folded = ByteArray(0)

    private val counts = IntArray(ProblemCode.SYNTAX_COUNT)
    private var valueCounts: IntArray? = null
    private var reported = 0
    private var problemLimitReached = false

    private var pendingNeed = 0
    private var pendingLow = 0
    private var pendingHigh = 0
    private var pendingStart = 0
    private var pendingLine = 0
    private var pendingColumn = 0
    private var invalidStart = -1
    private var invalidLine = 0
    private var invalidColumn = 0

    private var window: StreamWindow? = null
    private var stream: ByteInput? = null
    private var blockSize = 0
    private var streamEnded = false
    private var transcoding: InputCharset? = null
    private var rawBuffer: ByteArray? = null
    private var rawCount = 0

    // `maxInputOctets` costs nothing per line: a byte array is cut once before it is read, back to the end of its last
    // logical line within the limit (`lastLineEnd`); a stream counts the bytes of every block it reads (`overLimit`)
    // and `prepareLine` drops the line that the cut ends. Either way the lexer then meets the end of the input, and
    // only there `overflowed` is looked at, to report ICAL-S030.
    private var unread = limits.maxInputOctets
    private var overflowed = false

    // The end of a byte array cut at `maxInputOctets`, before `lastLineEnd` moves `end` back: the charset detection
    // samples up to here, as a stream, whose window reaches the limit, does, so both decide alike.
    private var cutEnd = 0

    // A line whose unfolded content is already over `maxLineOctets` and that has taken more than `max(2 * blockSize,
    // 4096)` bytes of the window is certainly dropped: read, it gives ICAL-S010; skipped (selection, depth limit), it
    // gives nothing. It is not kept whole: `dropLongLine` repeats what `skipUnwantedLine` or `readLogicalLine` would do
    // with it (the same skip decision, ICAL-S005 on the first line, the charset decision at the first non-ASCII byte,
    // bare CR and LF, physical lines, ICAL-S010 at the line start) while `dropToBreak` throws away what it has passed,
    // keeping `KEEP_BEHIND` bytes for the charset heuristic, which may look up to 64 bytes back for the start of a
    // word. The memory of a stream is thus bounded by `maxLineOctets` rather than by the longest line. Content is
    // counted in raw bytes before a charset decision; transcoding never makes text shorter, so a line over the limit in
    // raw bytes is over it after transcoding too.
    private var longLine = false

    // Offsets inside the lexer are `Int`s relative to the window (`index - base`); `origin` is the stream offset of
    // that zero point and only moves when the window is shifted: at the start of a line (`compactWindow`) or inside a
    // dropped line (`dropToBreak`). Everything that outlives a line (component offsets in `ContentReader`, node spans,
    // problems) adds `origin`. `originValue` is the same number as a `Double` for `ICalProperty`, see there. For a byte
    // array both stay 0.
    internal var origin: Long = 0L
        private set
    internal var originValue: Double = 0.0
        private set

    // One comparison per logical line covers both `maxLines` and the time budget: without a budget the checkpoint is
    // `maxLines`; with one the deadline is checked before the first line and every `TIME_CHECK_LINES` lines. Exceeding
    // either stops the lexer (`truncated`), so `ContentReader` does not report ICAL-S018.
    private var checkpoint = limits.maxLines
    private var deadline: Deadline? = null

    // Set by `ICalParser` from `ParseOptions`; a lexer or reader built directly (internal API) has no time budget even
    // if its `ParseLimits` carry `maxTime`.
    internal var budget: TimeBudget? = null

    internal var truncated: Boolean = false
        private set

    internal var logicalLineCount: Int = 0
        private set

    // Set by `ContentReader` before every `next()`: `SKIP_NONE` reads every line (the `Full` selection pays nothing),
    // `SKIP_UNSELECTED` skips property lines the selection does not want, `SKIP_ALL` skips everything except BEGIN and
    // END (inside a skipped component or a subtree over the depth limit).
    internal var skipMode: Int = SKIP_NONE
    internal var selection: ContentSelection = ContentSelection.Full
    internal var skippedLineCount: Int = 0

    // A line that the skip check keeps has already had its name read and looked up; `parseLine` reuses the end and the
    // id for the same start in the same input array instead of reading the name again. Only known names, never while
    // the charset is being detected, and the check clears it before every line, so a moved window or transcoded input
    // never sees a stale name.
    private var checkedNameStart = -1
    private var checkedNameEnd = 0
    private var checkedNameId = KnownNames.UNKNOWN
        private set

    internal val offset: Int get() = position - base

    internal val currentLine: Int get() = physicalLine

    internal val currentColumn: Int get() = position - lineStart + 1

    private var detecting = false

    internal val decodedSize: Int get() = end - base

    internal var decodedCharset: InputCharset = InputCharset.Utf8
        private set
    internal var charsetSource: Int = DecodedInput.SOURCE_UTF8
        private set
    internal var charsetConfidence: Int = FULL_CONFIDENCE
        private set

    // Set by `ContentReader` before the first `next()`. `strict`: see `emit()`.
    internal var strict: Boolean = false

    // The bytes are the UTF-8 encoding of a `String`, so no signature other than the UTF-8 BOM (U+FEFF) means anything;
    // a text starting with `+/v8` is not UTF-7.
    internal var textInput: Boolean = false

    // The buffer the offsets of the current line refer to (the input, or its transcoded copy with the same prefix) and
    // its base; the tree builder maps folded lines into it with `ContentLine.offsetAt`.
    internal val source: ByteArray get() = input

    internal val sourceBase: Int get() = base

    init {
        require(offset >= 0 && length >= 0 && offset <= input.size - length) {
            "Range $offset+$length is out of bounds for size ${input.size}"
        }
    }

    internal fun problemCount(code: ProblemCode): Int =
        if (code.index < 0 || code.index >= counts.size) 0 else counts[code.index]

    internal fun problemCounts(): IntArray = counts.copyOf()

    internal fun valueProblemCounts(): IntArray? = valueCounts?.copyOf()

    internal fun emitValue(problem: Problem) {
        val code = problem.code
        val index = code.index - ProblemCode.SYNTAX_COUNT
        if (index >= 0) {
            val values =
                valueCounts ?: IntArray(ProblemCode.known.size - ProblemCode.SYNTAX_COUNT).also { valueCounts = it }
            values[index]++
        }
        if (!(strict && code.severity == Severity.Error)) {
            if (reported >= limits.maxProblems) {
                if (!problemLimitReached) {
                    problemLimitReached = true
                    counts[ProblemCode.TooManyProblems.index]++
                    sink.onProblem(Problem(ProblemCode.TooManyProblems, problem.span, null))
                }
                return
            }
            reported++
        }
        sink.onProblem(problem)
    }

    // Sets the origin and the first line number before the first line is read.
    internal fun startAt(origin: Long, line: Int) {
        check(!started) { "The lexer has already started" }
        this.origin = origin
        originValue = origin.toDouble()
        physicalLine = line
    }

    internal fun stop() {
        started = true
        finished = true
    }

    // `logicalLineCount` counts every logical line, blank and skipped ones included, so the line limit also bounds
    // inputs made only of blank lines. Once the limit is reached the lexer reports ICAL-S014 and stops: the lines
    // returned so far are the partial result.
    public fun next(): ContentLine? {
        if (!started) {
            started = true
            startClock()
            if (!openInput()) {
                finished = true
            } else if (overflowed && window == null) {
                end = lastLineEnd()
            }
        }
        while (!finished) {
            window?.prepareLine()
            if (position >= end) {
                if (overflowed) inputTooLarge()
                finished = true
                break
            }
            if (logicalLineCount >= checkpoint && checkpointStops()) break
            logicalLineCount++
            if (longLine) {
                window!!.dropLongLine()
                continue
            }
            if (skipMode != SKIP_NONE && !atInputStart && skipUnwantedLine()) continue
            if (!readLogicalLine()) continue
            if (invalidStart >= 0) report(ProblemCode.InvalidUtf8, invalidStart, invalidLine, invalidColumn)
            if (parseLine()) return line
        }
        return null
    }

    private fun inputTooLarge() {
        report(ProblemCode.InputTooLarge, position, physicalLine, position - lineStart + 1)
        truncated = true
    }

    // The end of the last logical line whose line ending and the byte after it (to know that no fold follows) lie
    // before `end`, or `position` when there is none. Only line breaks are compared, so the decoded UTF-8 of a
    // transcoded input and the raw bytes of an ASCII-compatible one are scanned alike.
    private fun lastLineEnd(): Int {
        var i = end - 2
        while (i >= position) {
            val byte = input[i]
            if ((byte == LF || (byte == CR && input[i + 1] != LF)) && !isFoldByte(input[i + 1])) return i + 1
            i--
        }
        return position
    }

    // Counts [count] bytes just read from the stream against `maxInputOctets` and returns how many of them are past
    // the limit; past it, the stream is not read any more.
    private fun overLimit(count: Int): Int {
        unread -= count
        if (unread >= 0) return 0
        val excess = (-unread).toInt()
        unread = 0
        overflowed = true
        streamEnded = true
        return excess
    }

    private fun startClock() {
        deadline = budget?.start() ?: return
        checkpoint = 0
    }

    private fun checkpointStops(): Boolean {
        if (logicalLineCount >= limits.maxLines) {
            report(ProblemCode.TooManyLines, position, physicalLine, position - lineStart + 1)
        } else {
            if (!deadline!!.passed()) {
                checkpoint = if (limits.maxLines - logicalLineCount > TIME_CHECK_LINES) {
                    logicalLineCount + TIME_CHECK_LINES
                } else {
                    limits.maxLines
                }
                return false
            }
            report(ProblemCode.TimeBudgetExceeded, position, physicalLine, position - lineStart + 1)
        }
        truncated = true
        finished = true
        return true
    }

    // Runs once, on the first `next()`. The common case costs a few byte comparisons and no allocation: no signature
    // and either the UTF-8 constructor or no charset hint. Otherwise the shared `openInput` decides: an unsupported
    // signature stops the lexer with one ICAL-S011; a UTF-16/32 byte order mark or a single-byte hint replaces the
    // input with its UTF-8 transcoding and moves `base` to 0, so offsets are in the decoded text; a UTF-8 BOM is
    // skipped (ICAL-S001, Info); otherwise the lexer reads the bytes as UTF-8 and, in the detecting mode, decides at
    // the first non-ASCII byte.
    // A stream first reads the bytes the decision looks at: 8 for a signature, 1024 for the zero bytes of UTF-16/32
    // without a BOM (only if one of the first four bytes is zero), 128 KiB with a charset hint. `zeroInSignature` keeps
    // the common case at four byte comparisons.
    private fun openInput(): Boolean {
        if (window == null && end - position > limits.maxInputOctets) {
            end = position + limits.maxInputOctets.toInt()
            cutEnd = end
            overflowed = true
        }
        window?.ensureAhead(position, OPENING_SIGNATURE)
        val signature = if (textInput) textSignatureAt(input, position, end) else signatureAt(input, position, end)
        if (signature == SIGNATURE_NONE &&
            (!detect || (charsetHint == null && !zeroInSignature(input, position, end)))
        ) {
            detecting = detect
            return true
        }
        if (detect) window?.ensureAhead(position, if (charsetHint == null) WIDE_SAMPLE_BYTES else 2 * DETECTION_WINDOW)
        val opening = openInput(input, position, end, detect, charsetHint, decodingProblems())
        record(opening.choice)
        when (opening.kind) {
            OPEN_UNSUPPORTED -> return false
            OPEN_TRANSCODED -> startTranscoded(opening.choice.charset, opening.from)
            else -> detecting = opening.lazy
        }
        if (signature == SIGNATURE_UTF8) {
            report(ProblemCode.Utf8Bom, position, 1, 1)
            position += signatureLength(signature)
        }
        return true
    }

    private fun startTranscoded(charset: InputCharset, from: Int) {
        val window = window
        if (window != null) {
            window.startTranscoded(charset, from)
            return
        }
        val transcoded = transcode(charset, input, from, from, end, 0)
        input = transcoded.bytes
        base = 0
        position = 0
        lineStart = 0
        end = transcoded.end
    }

    private fun startStreamTranscoded(charset: InputCharset, from: Int) {
        val raw = input.copyOfRange(from, end)
        position = 0
        lineStart = 0
        end = 0
        startTranscoding(charset)
        appendDecoded(raw, 0, raw.size, streamEnded)
    }

    // Called once, at the first byte >= 0x80 seen by either the read path or the skip path, so the decision point does
    // not depend on the selection. Everything before that byte is ASCII and reads the same in every supported encoding,
    // so the lines already returned stay valid. A valid UTF-8 window keeps the input as it is; otherwise the evidence
    // rule or the heuristic decides. On a switch the new buffer keeps the prefix at the same indices and holds the
    // transcoded rest after it, so the line being scanned continues in the new buffer without a restart, without
    // repeated problems, and with the offsets that eager decoding of the same input gives.
    // A stream reads up to `DETECTION_WINDOW` bytes past the first non-ASCII byte first; the decision never looks
    // further on either path, so both decide the same. With a single-byte hint whose first non-ASCII byte came after
    // the first window, the decision is `decideHinted`: the eager rule of the opening, taken lazily.
    private fun detectEncoding(index: Int) {
        detecting = false
        window?.ensureAhead(index, DETECTION_WINDOW)
        val to = detectionEnd(index, maxOf(end, cutEnd))
        val from = maxOf(base, 0)
        val offset = index - base
        val column = index - lineStart + 1
        val assumed = CharsetChoice(decodedCharset, charsetSource, charsetConfidence)
        val choice: CharsetChoice
        if (assumed.charset == InputCharset.Utf8) {
            if (utf8WindowValid(input, index, to)) return
            choice = detectAt(input, from, index, to, assumed)
            if (choice.charset == InputCharset.Utf8) return
            reportDetection(choice, assumed, decodingProblems(), offset, physicalLine, column)
        } else {
            choice = decideHinted(input, from, index, to, assumed, decodingProblems(), offset, physicalLine, column)
            if (choice.charset == InputCharset.Utf8) {
                record(choice)
                return
            }
        }
        transcodeRest(choice.charset, index)
        record(choice)
    }

    private fun transcodeRest(charset: InputCharset, index: Int) {
        val window = window
        if (window != null) {
            window.transcodeRest(charset, index)
            return
        }
        val transcoded = transcode(charset, input, base, index, end, base)
        input = transcoded.bytes
        end = transcoded.end
    }

    private fun transcodeStreamRest(charset: InputCharset, index: Int) {
        val raw = input.copyOfRange(index, end)
        end = index
        startTranscoding(charset)
        appendDecoded(raw, 0, raw.size, streamEnded)
    }

    private fun record(choice: CharsetChoice) {
        decodedCharset = choice.charset
        charsetSource = choice.source
        charsetConfidence = choice.confidence
    }

    private fun decodingProblems(): DecodingProblems =
        DecodingProblems { code, offset, line, column, detail -> emit(code, offset, line, column, detail) }

    private fun ensureAhead(index: Int, count: Int) {
        while (end - index < count) {
            if (!fill()) return
        }
    }

    // Appends at least one byte to the window or returns false at the end. Without a transcoder it reads straight into
    // the window; with one it reads into `rawBuffer` after the undecodable remainder of the previous block and appends
    // the UTF-8.
    private fun fill(): Boolean {
        val stream = stream ?: return false
        while (!streamEnded) {
            if (transcoding == null) {
                val size = readSize()
                reserve(size)
                val count = readInput(stream, input, end, size)
                if (count < 0) {
                    streamEnded = true
                    return false
                }
                val kept = count - overLimit(count)
                end += kept
                if (kept > 0) return true
                continue
            }
            val raw = rawBuffer!!
            val count = readInput(stream, raw, rawCount, raw.size - rawCount)
            val before = end
            if (count < 0) {
                streamEnded = true
                appendDecoded(raw, 0, rawCount, true)
            } else {
                val kept = count - overLimit(count)
                appendDecoded(raw, 0, rawCount + kept, streamEnded)
            }
            if (end > before) return true
        }
        return false
    }

    private fun startTranscoding(charset: InputCharset) {
        transcoding = charset
        rawBuffer = ByteArray(blockSize + MAX_UNIT_REMAINDER)
        rawCount = 0
    }

    // Block-wise transcoding with the same result as transcoding the whole input: only whole code units are decoded,
    // and a UTF-16 lead surrogate at the end of a block waits for the next block (`decodableEnd`); at the end of the
    // stream the remainder is decoded as the tail of the whole input would be (one U+FFFD).
    private fun appendDecoded(source: ByteArray, from: Int, to: Int, final: Boolean) {
        val charset = transcoding!!
        val cut = if (final) to else decodableEnd(charset, source, from, to)
        reserve(decodeToUtf8(charset, source, from, cut, null, 0))
        end = decodeToUtf8(charset, source, from, cut, input, end)
        val left = to - cut
        if (left > 0) source.copyInto(rawBuffer!!, 0, cut, to)
        rawCount = left
    }

    private fun reserve(size: Int) {
        if (input.size - end >= size) return
        val required = end.toLong() + size
        require(required <= MAX_WINDOW) { "A logical line of $required bytes does not fit into the input window" }
        var capacity = maxOf(input.size.toLong(), MIN_WINDOW.toLong())
        while (capacity < required) capacity *= 2
        input = input.copyOf(minOf(capacity, MAX_WINDOW).toInt())
    }

    // The one hook of a stream in the line loop, before every logical line: it shifts the window if needed and reads
    // until the whole logical line and the byte after its line ending (to know that no fold follows) are in the window,
    // or the input ends. After it the line is read by exactly the code that reads a byte array, which sees the end of
    // the window only where a byte array would see the end of the input. That code is therefore unchanged: a first
    // design that refilled inside the scan loops (and at every lookahead) cost the byte-array lexer 3-18% on the JVM,
    // although the extra branches were never taken, because the JIT shapes the bigger loops worse. The price is a
    // second, simpler scan of each line (only for CR and LF) on the stream path.
    private fun prepareLine() {
        longLine = false
        compactWindow()
        val limit = maxOf(limits.maxLineOctets, 0)
        val window = maxOf(2 * blockSize, MIN_LONG_LINE)
        var i = position
        if (atInputStart && available(i) && isFoldByte(input[i])) i++
        var segment = i
        var content = 0
        while (true) {
            while (true) {
                val bytes = input
                val stop = end
                while (i < stop) {
                    val byte = bytes[i].toInt() and 0xFF
                    if (byte > CR_CODE || (byte != LF_CODE && byte != CR_CODE)) i++ else break
                }
                if (i < end) break
                if (content + (i - segment) > limit && i - position > window) {
                    longLine = true
                    return
                }
                if (!fill()) {
                    cutLine()
                    return
                }
            }
            content += i - segment
            var next = i + 1
            if (input[i] == CR && available(next) && input[next] == LF) next++
            if (!available(next)) {
                cutLine()
                return
            }
            if (!isFoldByte(input[next])) return
            if (content > limit && next - position > window) {
                longLine = true
                return
            }
            i = next + 1
            segment = i
        }
    }

    // The input ended before the line and the byte after its ending were read; if `maxInputOctets` cut it there, the
    // line is incomplete and is not read.
    private fun cutLine() {
        if (overflowed) end = position
    }

    private fun available(index: Int): Boolean {
        while (index >= end) {
            if (!fill()) return false
        }
        return true
    }

    // Reads start at 4 KiB and double with the window up to `blockSize`, so a small input (an iMIP invitation) does not
    // allocate a 64 KiB window; a large one reaches blocks of `blockSize` after a few reads.
    private fun readSize(): Int = if (input.size >=
        blockSize
    ) {
        blockSize
    } else {
        minOf(blockSize, maxOf(FIRST_READ, input.size))
    }

    // Called only at the start of a logical line, when nothing refers to the window any more: the returned
    // `ContentLine`, the raw bytes a folded line maps back to and the indices of the lexer itself all belong to the
    // line being read. It moves the unread tail to the front only when there is no room for the next read at the end
    // and at least one read's worth of bytes has been consumed, so each shift copies less than it frees and the copying
    // stays below one byte per input byte (a first version shifted whenever there was no room and copied 32 times the
    // input). Otherwise `reserve` grows the window; inside a line the window only grows, which keeps every index valid.
    private fun compactWindow() {
        val size = readSize()
        if (input.size - end >= size) return
        val keep = minOf(position, lineStart)
        if (keep < size) return
        input.copyInto(input, 0, keep, end)
        end -= keep
        position -= keep
        lineStart -= keep
        moveOrigin(keep)
    }

    private fun moveOrigin(by: Int) {
        origin += by
        originValue = origin.toDouble()
    }

    private fun dropLongLine() {
        pendingNeed = 0
        if (skipMode != SKIP_NONE && !atInputStart) {
            val from = skipStart()
            if (from >= 0) {
                dropBytes(from)
                skippedLineCount++
                return
            }
        }
        val firstLine = physicalLine
        var segmentStart = position
        if (atInputStart) {
            atInputStart = false
            if (isFoldByte(input[position])) {
                report(ProblemCode.ContinuationAtStart, position, physicalLine, position - lineStart + 1)
                segmentStart++
            }
        }
        val firstColumn = segmentStart - lineStart + 1
        val start = origin + (segmentStart - base)
        dropBytes(segmentStart)
        emitAt(ProblemCode.LineTooLong, start, firstLine, firstColumn, null)
    }

    private fun dropBytes(from: Int) {
        var i = from
        while (true) {
            i = dropToBreak(i)
            val next = consumeStreamLineEnding(i)
            if (available(next) && isFoldByte(input[next])) {
                i = next + 1
            } else {
                position = next
                return
            }
        }
    }

    private fun dropToBreak(from: Int): Int {
        var i = from
        while (true) {
            val bytes = input
            val limit = end
            while (i < limit) {
                val byte = bytes[i]
                if (byte == LF || byte == CR) return i
                if (byte < 0 && detecting) break
                i++
            }
            if (i < end) {
                detectEncoding(i)
                continue
            }
            val shift = i - KEEP_BEHIND
            if (shift >= maxOf(blockSize, MIN_LONG_LINE)) {
                input.copyInto(input, 0, shift, end)
                end -= shift
                position -= shift
                lineStart -= shift
                moveOrigin(shift)
                i -= shift
            }
            if (!fill()) return i
        }
    }

    private fun consumeStreamLineEnding(index: Int): Int {
        if (!available(index)) return end
        val next = if (input[index] == CR) {
            if (available(index + 1) && input[index + 1] == LF) {
                index + 2
            } else {
                report(ProblemCode.BareCr, index, physicalLine, index - lineStart + 1)
                index + 1
            }
        } else {
            report(ProblemCode.BareLf, index, physicalLine, index - lineStart + 1)
            index + 1
        }
        physicalLine++
        lineStart = next
        return next
    }

    // Selective parsing. Reads only the name, from the line start up to `;` or `:`, looks it up and, if the line is not
    // needed, jumps to the end of the logical line. BEGIN and END are never skipped. The peek gives up (the line takes
    // the normal path) when the line is blank, is the first line of the input, or has a fold inside the name: the
    // reader then filters the parsed line by the same selection, so the result never depends on this shortcut. A line
    // without `:` or `;` can never become an event, so it is skipped silently. A name longer than any known name is
    // unknown without looking further. Skipped lines are counted as logical lines (for `maxLines`) but get no
    // diagnostics: no UTF-8 check, no length limit, no grammar.
    private fun skipUnwantedLine(): Boolean {
        val from = skipStart()
        if (from < 0) return false
        skipToLineEnd(from)
        skippedLineCount++
        return true
    }

    // The skip decision alone, shared with `dropLongLine`: the index to skip from, or -1 when the line must be read.
    private fun skipStart(): Int {
        checkedNameStart = -1
        val bytes = input
        val limit = end
        val nameStart = position
        val nameLimit = minOf(limit, nameStart + selection.longestName + 1)
        var i = nameStart
        while (i < nameLimit) {
            val byte = bytes[i]
            if (byte > SEMICOLON) {
                i++
                continue
            }
            if (byte == COLON || byte == SEMICOLON) break
            if (byte == LF || byte == CR) {
                if (i == nameStart || isFolded(i)) return -1
                break
            }
            i++
        }
        val terminated = i < limit && (bytes[i] == COLON || bytes[i] == SEMICOLON)
        val lineEnds = i == limit || ((bytes[i] == LF || bytes[i] == CR) && !isFolded(i))
        if (terminated || !lineEnds) {
            val id = if (terminated) KnownNames.find(bytes, nameStart, i) else KnownNames.UNKNOWN
            if (id == KnownNames.BEGIN || id == KnownNames.END) return -1
            if (skipMode == SKIP_UNSELECTED) {
                val included = when {
                    id >= 0 -> selection.includesProperty(id)
                    terminated -> selection.includesUnknownProperty(bytes, nameStart, i)
                    else -> selection.includesLongUnknownProperty
                }
                if (included) {
                    if (id >= 0 && !detecting) {
                        checkedNameStart = nameStart
                        checkedNameEnd = i
                        checkedNameId = id
                    }
                    return -1
                }
            }
        }
        return if (detecting) nameStart else i
    }

    // Whether the line ending at `lineEnd` (CR, LF or CRLF) is followed by a continuation line, without consuming
    // anything.
    private fun isFolded(lineEnd: Int): Boolean {
        var next = lineEnd + 1
        if (input[lineEnd] == CR && next < end && input[next] == LF) next++
        return next < end && isFoldByte(input[next])
    }

    // The fastest loop of the lexer: one unsigned comparison per byte for everything above CR, then line endings and
    // folds exactly as `readLogicalLine` treats them, so line numbers and the once-per-input bare CR/LF problems stay
    // the same. While the charset is undecided, the line goes to `skipToLineEndDetecting`, and `skipUnwantedLine`
    // starts the skip at the name, which it has only peeked at, so no byte escapes the detection.
    private fun skipToLineEnd(from: Int) {
        if (detecting) {
            skipToLineEndDetecting(from)
            return
        }
        val bytes = input
        val limit = end
        var i = from
        while (true) {
            while (i < limit) {
                val byte = bytes[i].toInt() and 0xFF
                if (byte > CR_CODE || (byte != LF_CODE && byte != CR_CODE)) i++ else break
            }
            val next = consumeLineEnding(i)
            if (next < limit && isFoldByte(bytes[next])) {
                i = next + 1
            } else {
                position = next
                return
            }
        }
    }

    // The skip path of selective parsing must also see the first non-ASCII byte, or a selection would move the decision
    // point and change offsets. Used only until the decision; the unsigned fast loop of `skipToLineEnd`, kept free of
    // the check for the same reason as `scanSegment`, takes over after it.
    private fun skipToLineEndDetecting(from: Int) {
        var i = from
        while (true) {
            i = skipAsciiWhileDetecting(i)
            if (!detecting) {
                skipToLineEnd(i)
                return
            }
            val next = consumeLineEnding(i)
            if (next < end && isFoldByte(input[next])) {
                i = next + 1
            } else {
                position = next
                return
            }
        }
    }

    private fun skipAsciiWhileDetecting(from: Int): Int {
        val bytes = input
        val limit = end
        var i = from
        while (i < limit) {
            val byte = bytes[i]
            if (byte > CR) {
                i++
            } else if (byte < 0) {
                detectEncoding(i)
                return i
            } else if (byte == LF || byte == CR) {
                return i
            } else {
                i++
            }
        }
        return i
    }

    // Unfolding happens on bytes, before any UTF-8 decoding: a line ending followed by one SPACE or HTAB is removed
    // together with that one byte, for CRLF, bare CR and bare LF alike. A line without folds is not copied: the
    // `ContentLine` points into `input`. Only a folded line is joined, into the reused `folded` buffer. Every physical
    // piece is recorded as a segment, so positions inside a joined line map back to input offsets, lines and columns. A
    // continuation line at the very start of the input is read as the start of the first line (ICAL-S005), as if an
    // empty line preceded it. A logical line over `maxLineOctets` stops being copied, is still scanned to its end and
    // is then skipped (ICAL-S010).
    private fun readLogicalLine(): Boolean {
        val firstLine = physicalLine
        var segmentStart = position
        if (atInputStart) {
            atInputStart = false
            if (isFoldByte(input[position])) {
                report(ProblemCode.ContinuationAtStart, position, physicalLine, position - lineStart + 1)
                segmentStart++
            }
        }
        val firstColumn = segmentStart - lineStart + 1
        pendingNeed = 0
        invalidStart = -1
        var segmentEnd = scanSegment(segmentStart)
        var next = consumeLineEnding(segmentEnd)
        line.clearSegments()

        if (next >= end || !isFoldByte(input[next])) {
            position = next
            finishPendingSequence()
            val length = segmentEnd - segmentStart
            if (length > limits.maxLineOctets) {
                report(ProblemCode.LineTooLong, segmentStart, firstLine, firstColumn)
                return false
            }
            if (length == 0) {
                report(ProblemCode.BlankLine, segmentStart, firstLine, firstColumn)
                return false
            }
            line.reset(input, segmentStart, segmentEnd, invalidStart < 0)
            line.setSpan(segmentStart - base, segmentEnd - base, firstLine, firstColumn, firstLine)
            return true
        }

        var used = segmentEnd - segmentStart
        var overflow = used > limits.maxLineOctets
        if (!overflow) {
            ensureFoldedCapacity(used)
            input.copyInto(folded, 0, segmentStart, segmentEnd)
            line.addSegment(0, segmentStart - base, firstLine, firstColumn)
        }
        var lastLine = firstLine
        while (next < end && isFoldByte(input[next])) {
            val continuationStart = next + 1
            val continuationLine = physicalLine
            segmentEnd = scanSegment(continuationStart)
            val length = segmentEnd - continuationStart
            if (!overflow) {
                if (length > limits.maxLineOctets - used) {
                    overflow = true
                } else {
                    ensureFoldedCapacity(used + length)
                    input.copyInto(folded, used, continuationStart, segmentEnd)
                    line.addSegment(used, continuationStart - base, continuationLine, 2)
                    used += length
                }
            }
            lastLine = continuationLine
            next = consumeLineEnding(segmentEnd)
        }
        position = next
        finishPendingSequence()
        if (overflow) {
            report(ProblemCode.LineTooLong, segmentStart, firstLine, firstColumn)
            return false
        }
        if (used == 0) {
            report(ProblemCode.BlankLine, segmentStart, firstLine, firstColumn)
            return false
        }
        line.reset(folded, 0, used, invalidStart < 0)
        line.setSpan(segmentStart - base, segmentEnd - base, firstLine, firstColumn, lastLine)
        return true
    }

    // One loop both finds the line ending and validates UTF-8. ASCII bytes take the fast path; a lead byte starts a
    // sequence whose continuation bytes are checked against the ranges of Unicode Table 3-7, so overlong forms,
    // surrogates and code points above U+10FFFF are invalid. A sequence cut by a line ending stays pending: if a fold
    // follows, it continues in the next segment, otherwise it is invalid. Only the first invalid position of a logical
    // line is kept, for one ICAL-S006 per line.
    //
    // While the charset is undecided the whole segment goes to `scanSegmentDetecting`, a plain ASCII loop that stops at
    // the first byte >= 0x80, decides there and hands the rest of the segment back to `scanSegment`, now on the final
    // buffer. A `detecting` check inside the loop, even on the non-ASCII branch only, cost 13% of lexer throughput on
    // JVM corpora with much non-ASCII text (the JIT shapes the loop worse); one check per segment costs nothing
    // measurable.
    // No branch of the byte loop ends in a value: each ends in `continue`, `return` or a plain statement. An
    // `if`/`else` chain whose branches end in `i++` (an `Int`) and in an assignment (`Unit`) has the type `Any`, and
    // Kotlin/Native boxes the `i++` of every byte into a `kotlin.Int` object even when the `if` is a statement (only
    // -128..127 are cached): that was a third of the lexer time on `macosArm64`.
    //
    // A lead byte first tries the fast path: a two-byte sequence (C2..DF) is checked inline, longer ones by
    // `utf8CompleteSequenceEnd`; only a sequence that is invalid or reaches past the segment (a fold inside it, the end
    // of the input) takes `startSequence`/`continueSequence` with the pending state in fields. A complete valid
    // sequence leaves no pending state either way.
    private fun scanSegment(from: Int): Int {
        if (detecting) return scanSegmentDetecting(from)
        val bytes = input
        val limit = end
        var i = from
        if (pendingNeed > 0) i = continueSequence(i)
        while (i < limit) {
            val byte = bytes[i]
            if (byte > CR) {
                i++
                continue
            }
            if (byte < 0) {
                val next = i + 1
                if (byte >= UTF8_TWO_BYTE_LEAD && byte < UTF8_THREE_BYTE_LEAD && next < limit &&
                    bytes[next] < UTF8_LEAD_FIRST
                ) {
                    i = next + 1
                    continue
                }
                val sequenceEnd = utf8CompleteSequenceEnd(bytes, i, limit)
                i = if (sequenceEnd > 0) sequenceEnd else startSequence(i)
                continue
            }
            if (byte == LF || byte == CR) return i
            i++
        }
        return i
    }

    private fun scanSegmentDetecting(from: Int): Int {
        val bytes = input
        val limit = end
        var i = from
        while (i < limit) {
            val byte = bytes[i]
            if (byte < 0) {
                detectEncoding(i)
                return scanSegment(i)
            }
            if (byte == LF || byte == CR) return i
            i++
        }
        return i
    }

    private fun startSequence(index: Int): Int {
        val info = utf8LeadInfo(input[index].toInt() and 0xFF)
        if (info == 0) {
            markInvalid(index, physicalLine, index - lineStart + 1)
            return index + 1
        }
        pendingNeed = utf8LeadNeed(info)
        pendingLow = utf8LeadLow(info)
        pendingHigh = utf8LeadHigh(info)
        pendingStart = index
        pendingLine = physicalLine
        pendingColumn = index - lineStart + 1
        return continueSequence(index + 1)
    }

    private fun continueSequence(from: Int): Int {
        var i = from
        while (pendingNeed > 0 && i < end) {
            val byte = input[i].toInt() and 0xFF
            if (byte == LF.toInt() || byte == CR.toInt()) return i
            if (byte < pendingLow || byte > pendingHigh) {
                pendingNeed = 0
                markInvalid(pendingStart, pendingLine, pendingColumn)
                return i
            }
            pendingLow = UTF8_CONTINUATION_LOW
            pendingHigh = UTF8_CONTINUATION_HIGH
            pendingNeed--
            i++
        }
        return i
    }

    private fun finishPendingSequence() {
        if (pendingNeed > 0) {
            pendingNeed = 0
            markInvalid(pendingStart, pendingLine, pendingColumn)
        }
    }

    private fun markInvalid(index: Int, line: Int, column: Int) {
        if (invalidStart < 0) {
            invalidStart = index
            invalidLine = line
            invalidColumn = column
        }
    }

    // CRLF is canonical. A bare CR or LF is reported only once per input (`oncePerInput`), later ones are only counted,
    // so a large file with LF endings gives one problem, not millions.
    private fun consumeLineEnding(index: Int): Int {
        if (index >= end) return end
        val next = if (input[index] == CR) {
            if (index + 1 < end && input[index + 1] == LF) {
                index + 2
            } else {
                report(ProblemCode.BareCr, index, physicalLine, index - lineStart + 1)
                index + 1
            }
        } else {
            report(ProblemCode.BareLf, index, physicalLine, index - lineStart + 1)
            index + 1
        }
        physicalLine++
        lineStart = next
        return next
    }

    // The buffer doubles, but never past `maxLineOctets` unless a single request needs more, so a hostile input cannot
    // make it grow beyond the line limit.
    private fun ensureFoldedCapacity(size: Int) {
        if (folded.size >= size) return
        var capacity = if (folded.isEmpty()) INITIAL_FOLDED_CAPACITY else folded.size
        while (capacity < size) {
            capacity = if (capacity > Int.MAX_VALUE / 2) Int.MAX_VALUE else capacity * 2
        }
        folded = folded.copyOf(minOf(capacity, maxOf(size, limits.maxLineOctets)))
    }

    // Grammar `name *(";" param) ":" value`. The name ends at the first `;` or `:`, a parameter name at `=`, `;` or
    // `:`. Names keep their bytes as written; characters other than letters, digits and `-`, or an empty name, give
    // ICAL-S008. A parameter without `=` is kept with no values (ICAL-S012). The value is everything after the first
    // `:` outside quotes, raw. No `:` means ICAL-S007 and the line is skipped.
    //
    // The property name is looked up in `KnownNames` once its end is known; parameter names are only marked as valid
    // (`UNRESOLVED_NAME`) and looked up on demand by `ContentLine.parameterNameId`, which saves a lookup per parameter
    // for consumers that never ask.
    // The property name loop only looks for `;` or `:`. A name found in `KnownNames` is valid by construction (known
    // names are letters, digits and `-`, matched byte by byte), so the name bytes are checked (`firstInvalidNameByte`)
    // only when the lookup fails; the problems and their order are those of a single loop over all bytes.
    private fun parseLine(): Boolean {
        val bytes = line.bytes
        val lineEnd = line.end
        var i = line.start
        val id: Int
        if (i == checkedNameStart && bytes === input) {
            checkedNameStart = -1
            i = checkedNameEnd
            id = checkedNameId
        } else {
            while (i < lineEnd) {
                val byte = bytes[i]
                if (byte == SEMICOLON || byte == COLON) break
                i++
            }
            if (i == lineEnd) return missingColon()
            id = KnownNames.find(bytes, line.start, i)
        }
        var invalid = if (id == KnownNames.UNKNOWN) firstInvalidNameByte(bytes, line.start, i) else -1
        line.setName(i, id)
        if (invalid >= 0) {
            reportAt(ProblemCode.InvalidName, invalid)
        } else if (i == line.start) {
            reportAt(ProblemCode.InvalidName, i)
        }

        while (bytes[i] == SEMICOLON) {
            i++
            val nameStart = i
            invalid = -1
            while (i < lineEnd) {
                val byte = bytes[i]
                if (byte == EQUALS || byte == SEMICOLON || byte == COLON) break
                if (invalid < 0 && !isNameByte(byte)) invalid = i
                i++
            }
            if (i == lineEnd) return missingColon()
            val nameEnd = i
            val nameId = if (invalid < 0) UNRESOLVED_NAME else KnownNames.UNKNOWN
            val firstValue = line.nextValueIndex
            if (invalid >= 0) {
                reportAt(ProblemCode.InvalidName, invalid)
            } else if (nameEnd == nameStart && bytes[i] == EQUALS) {
                reportAt(ProblemCode.InvalidName, nameStart)
            }
            if (bytes[i] != EQUALS) {
                reportAt(ProblemCode.MalformedParameter, nameStart)
                line.addParameter(nameStart, nameEnd, nameId, firstValue)
                continue
            }
            i++
            while (true) {
                i = if (i < lineEnd && bytes[i] == DQUOTE) readQuotedValue(i) else readUnquotedValue(i)
                if (i >= lineEnd) return missingColon()
                if (bytes[i] != COMMA) break
                i++
            }
            line.addParameter(nameStart, nameEnd, nameId, firstValue)
        }
        line.setValueStart(i + 1)
        return true
    }

    private fun firstInvalidNameByte(bytes: ByteArray, from: Int, to: Int): Int {
        for (index in from until to) {
            if (!isNameByte(bytes[index])) return index
        }
        return -1
    }

    private fun readUnquotedValue(from: Int): Int {
        val bytes = line.bytes
        val lineEnd = line.end
        var i = from
        var flags = 0
        var stray = -1
        while (i < lineEnd) {
            val byte = bytes[i]
            if (byte == COMMA || byte == SEMICOLON || byte == COLON) break
            if (byte == CARET) {
                flags = VALUE_HAS_CARET
            } else if (byte == DQUOTE && stray < 0) {
                stray = i
            }
            i++
        }
        if (stray >= 0) reportAt(ProblemCode.MalformedParameter, stray)
        line.addValue(from, i, flags)
        return i
    }

    // A quoted value may contain `;`, `:` and `,`. If the closing quote is missing, the value is re-read literally from
    // the opening quote up to the next `,`, `;` or `:` (ICAL-S009). This happens at most once per line: no quote
    // follows an unclosed one, so the re-scan keeps the lexer linear. Text after a closing quote is handled the same
    // way (ICAL-S012): the whole value, quotes included, is taken literally.
    private fun readQuotedValue(quote: Int): Int {
        val bytes = line.bytes
        val lineEnd = line.end
        var i = quote + 1
        var flags = VALUE_QUOTED
        while (i < lineEnd) {
            val byte = bytes[i]
            if (byte == DQUOTE) break
            if (byte == CARET) flags = flags or VALUE_HAS_CARET
            i++
        }
        if (i == lineEnd) {
            reportAt(ProblemCode.UnclosedQuote, quote)
            return readLiteralValue(quote, quote + 1)
        }
        val after = i + 1
        if (after < lineEnd && !isValueDelimiter(bytes[after])) {
            reportAt(ProblemCode.MalformedParameter, after)
            return readLiteralValue(quote, after)
        }
        line.addValue(quote + 1, i, flags)
        return after
    }

    private fun readLiteralValue(from: Int, scanFrom: Int): Int {
        val bytes = line.bytes
        val lineEnd = line.end
        var flags = 0
        for (k in from until scanFrom) {
            if (bytes[k] == CARET) flags = VALUE_HAS_CARET
        }
        var i = scanFrom
        while (i < lineEnd) {
            val byte = bytes[i]
            if (isValueDelimiter(byte)) break
            if (byte == CARET) flags = VALUE_HAS_CARET
            i++
        }
        line.addValue(from, i, flags)
        return i
    }

    private fun missingColon(): Boolean {
        emit(ProblemCode.MissingColon, line.startOffset, line.line, line.column, null)
        return false
    }

    // Problems of the structural reader go through the lexer so both share one set of counters and one problem limit;
    // `offset` is relative to the start of the range.
    internal fun reportStructure(code: ProblemCode, offset: Int, line: Int, column: Int) {
        emit(code, offset, line, column, null)
    }

    private fun reportAt(code: ProblemCode, index: Int) {
        report(code, line.offsetAt(index) + base, line.lineAt(index), line.columnAt(index))
    }

    private fun report(code: ProblemCode, index: Int, line: Int, column: Int) {
        emit(code, index - base, line, column, null)
    }

    // Order of checks: count the occurrence; drop repeats of once-per-input codes; then apply `maxProblems`. ICAL-S011,
    // ICAL-S014 and ICAL-S023 bypass the limit because they explain a truncated result; so do the charset codes
    // ICAL-S024 to ICAL-S028. In the strict mode every code of severity `Error` bypasses the limit as well: the parser
    // throws at the first error, so it must never be only counted. The first problem over the limit is replaced by one
    // ICAL-S013; later ones are only counted. `report()` converts a buffer index into the offset in the decoded text
    // that `emit()` takes.
    private fun emit(code: ProblemCode, offset: Int, line: Int, column: Int, detail: String?) {
        emitAt(code, origin + offset, line, column, detail)
    }

    private fun emitAt(code: ProblemCode, offset: Long, line: Int, column: Int, detail: String?) {
        val count = ++counts[code.index]
        if (code.oncePerInput && count > 1) return
        if (!code.bypassesLimit && !(strict && code.severity == Severity.Error)) {
            if (reported >= limits.maxProblems) {
                if (!problemLimitReached) {
                    problemLimitReached = true
                    counts[ProblemCode.TooManyProblems.index]++
                    sink.onProblem(problemAt(ProblemCode.TooManyProblems, offset, line, column, null))
                }
                return
            }
            reported++
        }
        sink.onProblem(problemAt(code, offset, line, column, detail))
    }

    private companion object {
        private const val INITIAL_FOLDED_CAPACITY = 256
        private const val TIME_CHECK_LINES = 1024
        private const val OPENING_SIGNATURE = 8
        private const val MAX_UNIT_REMAINDER = 4
        private const val KEEP_BEHIND = 128
        private const val MIN_LONG_LINE = 4096
        private const val MIN_WINDOW = 256
        private const val FIRST_READ = 4096
        private const val MAX_WINDOW = Int.MAX_VALUE - 8L

        fun problemAt(code: ProblemCode, offset: Long, line: Int, column: Int, detail: String?): Problem =
            Problem(code, SourceSpan(offset, offset, line, column), detail)

        private const val LF_CODE = 0x0A
        private const val CR_CODE = 0x0D
    }
}

// File-level function, not a member of a companion object: a call to a companion member goes through the companion
// instance (an initialization check on Kotlin/Native, a `getInstance` call on JS and Wasm). Moving the lexer helpers
// out of the companion made `reader.lex` 1 to 4 % faster on Wasm and changed nothing measurable elsewhere; keeping a
// reference to `KnownNames` in a field helped Wasm too but cost Kotlin/Native up to 4 % and was dropped.
private fun isFoldByte(byte: Byte): Boolean = byte == SPACE || byte == HTAB

private fun isValueDelimiter(byte: Byte): Boolean = byte == COMMA || byte == SEMICOLON || byte == COLON

internal const val SKIP_NONE: Int = 0
internal const val SKIP_UNSELECTED: Int = 1
internal const val SKIP_ALL: Int = 2

// Loops on 0 and checks the count against the request, so a broken `ByteInput` fails loudly instead of corrupting the
// window.
internal fun readInput(input: ByteInput, buffer: ByteArray, offset: Int, length: Int): Int {
    while (true) {
        val count = input.read(buffer, offset, length)
        if (count > 0) {
            check(count <= length) { "ByteInput.read returned $count for a request of $length bytes" }
            return count
        }
        if (count < 0) {
            check(count == -1) { "ByteInput.read returned $count" }
            return -1
        }
    }
}

// The stream-only methods of the lexer (`prepareLine`, `dropLongLine`, `ensureAhead`, the stream halves of
// `startTranscoded` and `transcodeRest`, and everything they call: `fill`, `reserve`, `appendDecoded`,
// `compactWindow`...) stay private members, but the shared code reaches them only through this interface, whose one
// implementation is created in the stream constructor. An application that never reads a stream never reaches that
// constructor, so dead-code elimination (R8, Kotlin/JS, Wasm, Native) drops all of them: this cut 3.5 KB of JS.
internal interface StreamWindow {
    fun prepareLine()

    fun dropLongLine()

    fun ensureAhead(index: Int, count: Int)

    fun startTranscoded(charset: InputCharset, from: Int)

    fun transcodeRest(charset: InputCharset, index: Int)
}
