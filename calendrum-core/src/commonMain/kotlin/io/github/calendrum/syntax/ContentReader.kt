@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.ByteInput
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Structural layer over the lexer: a pull stream of BEGIN, PROPERTY and END events without an allocation per event.
// `next()` returns the kind; the other fields describe the current event and `line` is the property line. All of it is
// valid only until the next call.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ContentReader private constructor(
    private val lexer: ContentLexer,
    private val selection: ContentSelection,
    private val limits: ParseLimits,
) {
    internal constructor(
        input: ByteArray,
        offset: Int,
        length: Int,
        selection: ContentSelection,
        limits: ParseLimits,
        sink: ProblemListener,
        detect: Boolean,
        charsetHint: String?,
    ) : this(ContentLexer(input, offset, length, limits, sink, detect, charsetHint), selection, limits)

    internal constructor(
        stream: ByteInput,
        blockSize: Int,
        selection: ContentSelection,
        limits: ParseLimits,
        sink: ProblemListener,
        detect: Boolean,
        charsetHint: String?,
    ) : this(ContentLexer(stream, blockSize, limits, sink, detect, charsetHint), selection, limits)

    internal constructor(
        input: ByteArray,
        offset: Int,
        length: Int,
        selection: ContentSelection,
        limits: ParseLimits,
        sink: ProblemListener,
    ) : this(input, offset, length, selection, limits, sink, false, null)

    public constructor(
        input: ByteArray,
        selection: ContentSelection,
        limits: ParseLimits,
        onProblem: ProblemListener,
    ) : this(input, 0, input.size, selection, limits, onProblem, false, null)

    public constructor(
        input: ByteArray,
        charset: String?,
        selection: ContentSelection,
        limits: ParseLimits,
        onProblem: ProblemListener,
    ) : this(input, 0, input.size, selection, limits, onProblem, true, charset)

    // An explicit stack of open components in parallel arrays that grow on demand up to `maxDepth + 1` (`ParseLimits`
    // is public, so a large `maxDepth` must not allocate up front); the parser is iterative. Unknown component names
    // are copied into `names` (at most `MAX_STORED_NAME` bytes each) so END can be matched after the BEGIN line buffer
    // is reused.
    private var ids = IntArray(minOf(limits.maxDepth, INITIAL_DEPTH) + 1)
    private var hashes = IntArray(ids.size)
    private var lengths = IntArray(ids.size)
    private var nameStarts = IntArray(ids.size)
    private var startOffsets = LongArray(ids.size)
    private var names = ByteArray(0)
    private var namesTop = 0

    private var openCount = 0

    // `hiddenFrom` is the depth of the outermost open component the selection skips (0: none); everything below it is
    // tracked but not emitted. `overflow` counts BEGIN minus END inside a subtree over the depth limit; that subtree is
    // not tracked at all.
    private var hiddenFrom = 0

    // The level of the outermost component kept whole, like `hiddenFrom` for hidden ones; inside it the lexer skips
    // nothing.
    private var wholeFrom = 0

    // A copy of the selection's flag: the full parse pays one field test per property line and never looks at the mask.
    private val allProperties = selection.allProperties
    private var overflow = 0
    private var componentTotal = 0
    private var droppedLines = 0
    private var finished = false

    private var closing = false
    private var closeTo = 0
    private var closeExplicitly = false
    private var implicitEndOffset = 0
    private var explicitEndOffset = 0
    private var closeOffset = 0
    private var closeLine = 0
    private var closeColumn = 0

    private var lineNameId = KnownNames.UNKNOWN
    private var lineNameHash = 0
    private var lineNameLength = 0

    private var eventNameStart = 0
    private var eventNameEnd = 0

    public var kind: Int = END_OF_INPUT
        private set
    public var depth: Int = 0
        private set
    public var componentId: Int = UNKNOWN_NAME
        private set
    public var isImplicit: Boolean = false
        private set
    public var componentStartOffset: Long = -1L
        get() = if (offsetsCleared) -1L else field
        private set
    public var componentEndOffset: Long = -1L
        get() = if (offsetsCleared) -1L else field
        private set

    // A property event has no component offsets (-1). Writing `-1L` into the two `Long` fields for every property
    // allocated two `Long` objects per line on Kotlin/JS; the flag makes the getters return -1 instead, and the fields
    // keep the last BEGIN/END values.
    private var offsetsCleared = true
    public var offset: Int = 0
        private set
    public var lineNumber: Int = 0
        private set
    public var column: Int = 0
        private set
    public var skippedComponents: Int = 0
        private set

    public val line: ContentLine get() = lexer.line

    public val skippedLines: Int get() = lexer.skippedLineCount + droppedLines

    // What the lexer decided, for tests; final once the input has been read.
    internal val decodedCharset: InputCharset get() = lexer.decodedCharset

    internal val partial: Boolean = !selection.isFull

    internal val charsetSource: Int get() = lexer.charsetSource

    internal val charsetConfidence: Int get() = lexer.charsetConfidence

    internal var strict: Boolean
        get() = lexer.strict
        set(value) {
            lexer.strict = value
        }

    internal var textInput: Boolean
        get() = lexer.textInput
        set(value) {
            lexer.textInput = value
        }

    internal val source: ByteArray get() = lexer.source

    internal val origin: Long get() = lexer.origin

    internal val originValue: Double get() = lexer.originValue

    internal var budget: TimeBudget?
        get() = lexer.budget
        set(value) {
            lexer.budget = value
        }

    internal val sourceBase: Int get() = lexer.sourceBase

    // Setting a chain binds its line to this reader once.
    internal var interceptors: InterceptorChain? = null
        set(value) {
            field = value
            interceptedLine = value?.line
            value?.line?.bindRead(this, lexer.line)
        }

    internal var interceptedLine: InterceptedLine? = null
        private set

    // The edited line of the current property event, or null: what `ICalReader` checks before using the line bytes.
    // `interceptedLine` is the line of the chain, set with it: `TreeBuilder` reads it and checks only its EDITED flag
    // per property, because going through the chain cost the tree 1-3 % with an empty interceptor on the JVM, JS and
    // Wasm. It lives in the reader, not in `TreeBuilder`: there the field made every parse 8 bytes bigger on the JVM
    // (object alignment), here it fits into the padding.
    internal val edits: InterceptedLine?
        get() {
            val line = interceptedLine ?: return null
            return if (line.isEdited) line else null
        }

    init {
        lexer.selection = selection
    }

    public fun componentName(): String {
        val id = componentId
        if (id >= 0) return KnownNames.name(id)
        return decodeUtf8Replacing(names, eventNameStart, eventNameEnd)
    }

    // Pending implicit ENDs are emitted one per call before anything else is read. The lexer skip mode is chosen per
    // line from the state.
    public fun next(): Int {
        try {
            while (true) {
                if (closing) {
                    if (closeStep()) return kind
                    continue
                }
                if (finished) {
                    kind = END_OF_INPUT
                    return kind
                }
                lexer.skipMode = when {
                    overflow > 0 || hiddenFrom > 0 -> SKIP_ALL
                    allProperties || wholeFrom > 0 -> SKIP_NONE
                    else -> SKIP_UNSELECTED
                }
                val line = lexer.next()
                if (line == null) {
                    endOfInput()
                    continue
                }
                when (line.nameId) {
                    KnownNames.BEGIN -> if (begin(line)) return kind
                    KnownNames.END -> end(line)
                    else -> if (property(line) && (interceptors == null || intercepted())) return kind
                }
            }
        } catch (e: Throwable) {
            interceptors?.line?.clear()
            throw e
        }
    }

    // `@InternalICalApi` entry for the benchmarks (`lexerTree.readFullIntercepted`).
    public fun intercept(vararg interceptors: LineInterceptor) {
        val size = interceptors.size
        this.interceptors = when (size) {
            0 -> null
            1 -> InterceptorChain(interceptors[0], null)
            else -> InterceptorChain(interceptors[0], interceptors.copyOfRange(1, size))
        }
    }

    internal fun startAt(origin: Long, line: Int) {
        lexer.startAt(origin, line)
    }

    internal fun problemCount(code: ProblemCode): Int = lexer.problemCount(code)

    internal fun problemCounts(): IntArray = lexer.problemCounts()

    internal fun valueProblemCounts(): IntArray? = lexer.valueProblemCounts()

    internal fun reportValue(problem: Problem) {
        lexer.emitValue(problem)
    }

    internal var values: ValueHook? = null

    // Every BEGIN counts towards `maxComponents`, emitted or not, so the limit gives the same result with any
    // selection. VCALENDAR is always emitted when its parent is (it is a container); any other component only when the
    // selection wants it.
    private fun begin(line: ContentLine): Boolean {
        componentTotal++
        if (componentTotal > limits.maxComponents) {
            report(ProblemCode.TooManyComponents, line)
            lexer.stop()
            finished = true
            startClosing(0, false, line.startOffset, line.startOffset, line)
            return false
        }
        readComponentName(line)
        if (overflow > 0) {
            overflow++
            skippedComponents++
            return false
        }
        if (openCount == limits.maxDepth) {
            report(ProblemCode.TooDeep, line)
            overflow = 1
            skippedComponents++
            return false
        }
        val level = ++openCount
        push(level, line)
        if (hiddenFrom == 0 && (wholeFrom > 0 || lineNameId == KnownNames.VCALENDAR || includes(level))) {
            if (wholeFrom == 0 && selection.isWhole(lineNameId)) wholeFrom = level
            setComponentEvent(BEGIN, level, implicit = false, endOffset = -1)
            setPosition(line.startOffset, line.line, line.column)
            return true
        }
        if (hiddenFrom == 0) hiddenFrom = level
        skippedComponents++
        return false
    }

    // Recovery: an END with no open component or with a name nobody opened is ignored; an END of an outer component
    // closes the nested ones implicitly first.
    private fun end(line: ContentLine) {
        readComponentName(line)
        if (overflow > 0) {
            overflow--
            return
        }
        if (openCount == 0) {
            report(ProblemCode.EndWithoutBegin, line)
            return
        }
        var level = openCount
        while (level > 0 && !matches(level, line)) level--
        if (level == 0) {
            report(ProblemCode.UnmatchedEnd, line)
            return
        }
        if (level < openCount) report(ProblemCode.EndClosesNested, line)
        startClosing(level - 1, true, line.startOffset, lexer.offset, line)
    }

    private fun includes(level: Int): Boolean = selection.wantsComponent(lineNameId) {
        val start = nameStarts[level]
        canonicalName(decodeUtf8Replacing(names, start, start + storedLength(level)))
    }

    private fun wanted(line: ContentLine): Boolean = selection.wantsProperty(line.nameId) { canonicalName(line.name()) }

    // A property outside any component is kept at depth 0 (ICAL-S021): lossless.
    private fun property(line: ContentLine): Boolean {
        if (overflow > 0 || hiddenFrom > 0 || (!allProperties && wholeFrom == 0 && !wanted(line))) {
            droppedLines++
            return false
        }
        if (openCount == 0) report(ProblemCode.PropertyOutsideComponent, line)
        kind = PROPERTY
        depth = openCount
        componentId = if (openCount > 0) ids[openCount] else UNKNOWN_NAME
        eventNameStart = if (openCount > 0) nameStarts[openCount] else 0
        eventNameEnd = if (openCount > 0) eventNameStart + storedLength(openCount) else 0
        isImplicit = false
        offsetsCleared = true
        setPosition(line.startOffset, line.line, line.column)
        return true
    }

    // The chain runs on property events only, after the selection; a dropped line is not an event at all, so the tree,
    // the cursor and `ComponentStream` cannot disagree. Properties outside components (ICAL-S021) are not intercepted,
    // but `clear()` still resets the edit state of the previous line.
    private fun intercepted(): Boolean {
        val line = interceptedLine!!
        if (openCount == 0) {
            line.clear()
            return true
        }
        return interceptors!!.keepRead(line)
    }

    // Closes what is still open. ICAL-S018 is not reported when the lexer stopped at the line limit: ICAL-S014 already
    // explains the truncation.
    private fun endOfInput() {
        finished = true
        if (openCount == 0) return
        if (!lexer.truncated) {
            lexer.reportStructure(ProblemCode.UnclosedComponents, lexer.offset, lexer.currentLine, lexer.currentColumn)
        }
        closing = true
        closeTo = 0
        closeExplicitly = false
        implicitEndOffset = lexer.offset
        explicitEndOffset = lexer.offset
        closeOffset = lexer.offset
        closeLine = lexer.currentLine
        closeColumn = lexer.currentColumn
    }

    private fun startClosing(to: Int, explicit: Boolean, implicitEnd: Int, explicitEnd: Int, line: ContentLine) {
        if (openCount <= to) return
        closing = true
        closeTo = to
        closeExplicitly = explicit
        implicitEndOffset = implicitEnd
        explicitEndOffset = explicitEnd
        closeOffset = line.startOffset
        closeLine = line.line
        closeColumn = line.column
    }

    // Pops one level. Components the selection hid are popped silently; the one matched by an explicit END ends after
    // its END line, the implicit ones at the start of the line that closed them (or at the end of the input).
    private fun closeStep(): Boolean {
        if (openCount <= closeTo) {
            closing = false
            return false
        }
        val level = openCount
        openCount--
        val visible = hiddenFrom == 0 || level < hiddenFrom
        if (level == hiddenFrom) hiddenFrom = 0
        if (level == wholeFrom) wholeFrom = 0
        namesTop = nameStarts[level]
        if (!visible) return false
        val explicit = closeExplicitly && level == closeTo + 1
        setComponentEvent(END, level, !explicit, if (explicit) explicitEndOffset else implicitEndOffset)
        setPosition(closeOffset, closeLine, closeColumn)
        return true
    }

    private fun setComponentEvent(kind: Int, level: Int, implicit: Boolean, endOffset: Int) {
        this.kind = kind
        depth = level
        componentId = ids[level]
        eventNameStart = nameStarts[level]
        eventNameEnd = eventNameStart + storedLength(level)
        isImplicit = implicit
        componentStartOffset = startOffsets[level]
        componentEndOffset = if (endOffset < 0) -1L else lexer.origin + endOffset
        offsetsCleared = false
    }

    private fun setPosition(offset: Int, line: Int, column: Int) {
        this.offset = offset
        lineNumber = line
        this.column = column
    }

    // The value of BEGIN/END is the component name. Invalid characters or an empty name are reported but kept as
    // written. The full-name hash (FNV-1a, ASCII case folded) is only for matching unknown names longer than
    // `MAX_STORED_NAME`.
    private fun readComponentName(line: ContentLine) {
        if (line.parameterCount > 0) report(ProblemCode.ComponentParameters, line)
        val bytes = line.bytes
        val from = line.valueStart
        val to = line.valueEnd
        var valid = to > from
        var hash = HASH_SEED
        for (index in from until to) {
            val byte = bytes[index]
            if (!isNameByte(byte)) valid = false
            hash = (hash xor foldCase(byte)) * HASH_PRIME
        }
        if (!valid) report(ProblemCode.InvalidComponentName, line)
        lineNameId = if (valid) KnownNames.find(bytes, from, to) else KnownNames.UNKNOWN
        lineNameHash = hash
        lineNameLength = to - from
    }

    private fun push(level: Int, line: ContentLine) {
        if (level == ids.size) growStack()
        ids[level] = lineNameId
        hashes[level] = lineNameHash
        lengths[level] = lineNameLength
        startOffsets[level] = lexer.origin + line.startOffset
        nameStarts[level] = namesTop
        if (lineNameId >= 0) return
        val stored = storedLength(level)
        if (names.size < namesTop + stored) names = names.copyOf(maxOf(namesTop + stored, names.size * 2, 64))
        line.bytes.copyInto(names, namesTop, line.valueStart, line.valueStart + stored)
        namesTop += stored
    }

    private fun growStack() {
        val size = if (ids.size > limits.maxDepth / 2) limits.maxDepth + 1 else ids.size * 2
        ids = ids.copyOf(size)
        hashes = hashes.copyOf(size)
        lengths = lengths.copyOf(size)
        nameStarts = nameStarts.copyOf(size)
        startOffsets = startOffsets.copyOf(size)
    }

    private fun storedLength(level: Int): Int = if (ids[level] >= 0) 0 else minOf(lengths[level], MAX_STORED_NAME)

    private fun matches(level: Int, line: ContentLine): Boolean {
        if (ids[level] != lineNameId) return false
        if (lineNameId >= 0) return true
        if (lengths[level] != lineNameLength || hashes[level] != lineNameHash) return false
        val bytes = line.bytes
        val from = line.valueStart
        val start = nameStarts[level]
        for (k in 0 until storedLength(level)) {
            if (foldCase(names[start + k]) != foldCase(bytes[from + k])) return false
        }
        return true
    }

    private fun report(code: ProblemCode, line: ContentLine) {
        lexer.reportStructure(code, line.startOffset, line.line, line.column)
    }

    public companion object {
        public const val END_OF_INPUT: Int = 0
        public const val BEGIN: Int = 1
        public const val PROPERTY: Int = 2
        public const val END: Int = 3
        public const val UNKNOWN_NAME: Int = -1

        private const val MAX_STORED_NAME = 255
        private const val INITIAL_DEPTH = 16
        private const val HASH_SEED = -0x7ee3623b
        private const val HASH_PRIME = 0x01000193
        private const val LOWER_A: Byte = 0x61
        private const val LOWER_Z: Byte = 0x7A
        private const val CASE_BIT = 0x20

        private fun foldCase(byte: Byte): Int {
            val value = byte.toInt()
            return if (byte >= LOWER_A && byte <= LOWER_Z) value - CASE_BIT else value
        }
    }
}
