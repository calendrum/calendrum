package io.github.calendrum.syntax

import io.github.calendrum.ICalParseException
import io.github.calendrum.SourceSpan
import kotlin.Throws

// A pull cursor over `ContentReader`, created by `ICalParser.reader`. Nothing is allocated per event: names of known
// components and properties are the interned strings, `value` is one reusable view. Properties outside components
// (ICAL-S021) are skipped, so the events match the tree of `parse` on the same input. Accessors of the current event
// throw IllegalStateException without one or on the wrong event.
/**
 * A pull cursor over iCalendar content: it moves from event to event and exposes the current one without building a
 * tree.
 *
 * Create one with `ICal.parser().reader(...)`. [next] moves to the next [ContentEventType]: `Begin` of a component, a
 * `Property` or `End` of a component. The accessors describe the current event. Nothing is allocated per event, and
 * [value] is a view that is valid only until the next call of [next]. At a `Begin` event [readComponent] builds the
 * subtree of that component and leaves the cursor at its `End`.
 *
 * Problems go to the `problemListener` of the options; in strict mode [next] throws at the first error and keeps
 * throwing. A cursor is not thread-safe.
 */
public class ICalReader internal constructor(
    private val reader: ContentReader,
    private val retainSource: Boolean,
    private val inputSize: Int,
    private val streamed: Boolean,
) {
    private var event = NONE
    private var failure: ICalParseException? = null
    private var builder: TreeBuilder? = null

    // Names of unknown components by depth, decoded on Begin: at End the reader line is the END line, another line or
    // nothing, and `ContentReader` keeps only the first 255 bytes.
    private var componentNames = arrayOfNulls<String>(INITIAL_DEPTH)
    private var propertyName: String? = null
    private var chars = CharArray(0)
    private val view = CharsView()
    private var valueDecoded = false

    /**
     * The nesting depth of the current event: 1 for a top-level component and its properties, 0 when there is no
     * current event.
     */
    public val depth: Int
        get() = if (event == BEGIN || event == PROPERTY || event == END) reader.depth else 0

    /**
     * The canonical name of the current component (at `Begin` and `End`) or property (at `Property`).
     *
     * @throws IllegalStateException if there is no current event.
     */
    public val name: String
        get() = when (event) {
            BEGIN, END -> {
                val id = reader.componentId
                if (id >= 0) KnownNames.name(id) else componentNames[reader.depth]!!
            }

            PROPERTY -> {
                val id = reader.line.nameId
                val edits = reader.edits
                when {
                    edits != null -> edits.editedName
                    id >= 0 -> KnownNames.name(id)
                    else -> propertyName ?: unknownPropertyName()
                }
            }

            else -> noEvent()
        }

    /**
     * Whether the current event is an `End` the input does not contain: a component closed implicitly (`ICAL-S016`,
     * `ICAL-S018`).
     */
    public val isImplicitEnd: Boolean get() = event == END && reader.isImplicit

    // Decoded once per event into `chars`, which grows to the longest value; the view is valid until `next()`.

    /**
     * The value of the current property as a view over a reused buffer; copy it ([valueString]) to keep it past the
     * next [next].
     *
     * @throws IllegalStateException if the current event is not a `Property`.
     */
    public val value: CharSequence
        get() {
            checkProperty("value")
            val edits = reader.edits
            if (edits != null) return edits.editedValue
            if (!valueDecoded) {
                val line = reader.line
                val length = line.valueEnd - line.valueStart
                if (chars.size < length) chars = CharArray(maxOf(length, chars.size * 2, MIN_CHARS))
                view.reset(chars, decodeUtf8Into(line.bytes, line.valueStart, line.valueEnd, chars))
                valueDecoded = true
            }
            return view
        }

    /**
     * The number of parameters of the current property.
     *
     * @throws IllegalStateException if the current event is not a `Property`.
     */
    public val parameterCount: Int
        get() {
            checkProperty("parameterCount")
            return reader.edits?.editedParameters?.size ?: reader.line.parameterCount
        }

    /**
     * The offset in bytes from the start of the input where the current event starts.
     *
     * @throws IllegalStateException if there is no current event.
     */
    public val offset: Long
        get() {
            checkEvent()
            return reader.origin + reader.offset
        }

    /**
     * The 1-based number of the physical line where the current event starts.
     *
     * @throws IllegalStateException if there is no current event.
     */
    public val line: Int
        get() {
            checkEvent()
            return reader.lineNumber
        }

    /**
     * The 1-based column in bytes where the current event starts on its line.
     *
     * @throws IllegalStateException if there is no current event.
     */
    public val column: Int
        get() {
            checkEvent()
            return reader.column
        }

    /**
     * Moves to the next event.
     *
     * @return the new event, or `null` at the end of the input.
     * @throws ICalParseException in strict mode, at the first error; later calls rethrow it.
     */
    @Throws(ICalParseException::class)
    public fun next(): ContentEventType? {
        failure?.let { throw it }
        if (event == FINISHED) return null
        propertyName = null
        valueDecoded = false
        try {
            while (true) {
                when (reader.next()) {
                    ContentReader.END_OF_INPUT -> {
                        event = FINISHED
                        return null
                    }

                    ContentReader.BEGIN -> {
                        begin()
                        return ContentEventType.Begin
                    }

                    ContentReader.PROPERTY -> if (reader.depth > 0) {
                        event = PROPERTY
                        return ContentEventType.Property
                    }

                    else -> {
                        event = END
                        return ContentEventType.End
                    }
                }
            }
        } catch (e: ICalParseException) {
            fail(e)
        }
    }

    /**
     * Returns the value of the current property as a new string.
     *
     * @throws IllegalStateException if the current event is not a `Property`.
     */
    public fun valueString(): String {
        checkProperty("valueString")
        val edits = reader.edits
        if (edits != null) return edits.editedValue
        if (valueDecoded) return view.toString()
        val line = reader.line
        return decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8)
    }

    /**
     * Returns the canonical name of the parameter at [index] of the current property.
     *
     * @param index from 0 until [parameterCount].
     * @throws IllegalStateException if the current event is not a `Property`.
     */
    public fun parameterName(index: Int): String {
        checkProperty("parameterName")
        val edits = reader.edits
        if (edits != null) return edits.editedParameters[index].name
        val line = reader.line
        val id = line.parameterNameId(index)
        return if (id >= 0) KnownNames.name(id) else canonicalName(line.parameterName(index))
    }

    /**
     * Returns the values of the parameter at [index] of the current property, unquoted.
     *
     * @param index from 0 until [parameterCount].
     * @throws IllegalStateException if the current event is not a `Property`.
     */
    public fun parameterValues(index: Int): List<String> {
        checkProperty("parameterValues")
        val edits = reader.edits
        if (edits != null) return edits.editedParameters[index].values
        val line = reader.line
        val count = line.parameterValueCount(index)
        if (count == 0) return ArrayView(EMPTY_STRINGS)
        return ArrayView(Array(count) { line.parameterValue(index, it) })
    }

    // The logical line of the event without its line ending, as a property span in the tree; a point for an implicit
    // End.

    /**
     * Returns the position of the current event: the whole line of a `Begin`, `Property` or `End`, or a point for an
     * implicit end.
     *
     * @throws IllegalStateException if there is no current event.
     */
    public fun span(): SourceSpan {
        checkEvent()
        if (event == END && reader.isImplicit) {
            val point = reader.origin + reader.offset
            return SourceSpan(point, point, reader.lineNumber, reader.column)
        }
        val line = reader.line
        val origin = reader.origin
        return SourceSpan(origin + line.startOffset, origin + line.endOffset, line.line, line.column)
    }

    // Builds the subtree of the current Begin with the `TreeBuilder` of the parser (one per cursor, so its stacks and
    // the `retainSource = false` chunks are reused) and leaves the cursor on the End of that component.

    /**
     * Builds the component of the current `Begin` event, including all it contains, and moves to its `End`.
     *
     * @throws IllegalStateException if the current event is not a `Begin`.
     * @throws ICalParseException in strict mode, at the first error.
     */
    @Throws(ICalParseException::class)
    public fun readComponent(): ICalComponent {
        failure?.let { throw it }
        check(event == BEGIN) { "readComponent() needs the current event to be Begin" }
        propertyName = null
        valueDecoded = false
        val builder = builder ?: TreeBuilder(reader, retainSource, inputSize, streamed).also { builder = it }
        try {
            val component = builder.readComponent()
            event = END
            return component
        } catch (e: ICalParseException) {
            fail(e)
        }
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String {
        val type = when (event) {
            BEGIN -> ContentEventType.Begin.name
            PROPERTY -> ContentEventType.Property.name
            END -> ContentEventType.End.name
            FINISHED -> "finished"
            else -> "none"
        }
        return describe("ICalReader", "event", type, "depth", depth)
    }

    private fun begin() {
        event = BEGIN
        val depth = reader.depth
        val names = componentNames
        if (depth >= names.size) componentNames = names.copyOf(maxOf(names.size * 2, depth + 1))
        componentNames[depth] = if (reader.componentId >= 0) {
            null
        } else {
            val line = reader.line
            canonicalName(decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8))
        }
    }

    private fun unknownPropertyName(): String {
        val name = canonicalName(reader.line.name())
        propertyName = name
        return name
    }

    // A strict error leaves the lexer in the middle of a line, so the cursor stays broken: every later call throws the
    // same exception.
    private fun fail(e: ICalParseException): Nothing {
        failure = e
        event = FINISHED
        throw e
    }

    private fun checkEvent() {
        if (event != BEGIN && event != PROPERTY && event != END) noEvent()
    }

    private fun checkProperty(what: String) {
        if (event != PROPERTY) {
            checkEvent()
            throw IllegalStateException("$what is only available on a Property event")
        }
    }

    private fun noEvent(): Nothing = throw IllegalStateException("There is no current event")

    private companion object {
        private const val NONE = 0
        private const val BEGIN = 1
        private const val PROPERTY = 2
        private const val END = 3
        private const val FINISHED = 4
        private const val INITIAL_DEPTH = 8
        private const val MIN_CHARS = 256

        private val EMPTY_STRINGS: Array<String> = emptyArray()
    }
}
