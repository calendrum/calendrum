package io.github.calendrum.syntax

import io.github.calendrum.ICalDsl
import io.github.calendrum.SourceSpan
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * A content line of a component: a name, parameters and a value, such as `SUMMARY:Weekly sync` or
 * `DTSTART;TZID=Europe/Moscow:20261001T100000` ([RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1)).
 *
 * The value is the text of the line after the colon, unfolded but otherwise as written: not unescaped and not
 * interpreted. The typed model in `io.github.calendrum.model` decodes it into dates, durations and other types. A property
 * read from input decodes its name, value and parameters on first access, and keeps its original spelling and folding
 * for byte-stable writing; a property built by hand has no [span].
 *
 * Equality compares the name, the parameters and the value. A property is immutable and can be read from any thread.
 */
public class ICalProperty internal constructor(
    private val source: ByteArray?,
    private val start: Int,
    private val nameEnd: Int,
    private val valueStart: Int,
    private val end: Int,
    internal val nameId: Int,
    private val textFlags: Int,
    private val parameterData: IntArray?,
    private val offsetShift: Double,
    private val line: Int,
    private val column: Int,
    name: String?,
    value: String?,
    parameters: Array<ICalParameter>?,
) {
    // `@Volatile` single-check caches: racing threads decode the same value and one write wins; objects of parameters
    // are created on first access.
    @Volatile
    private var nameCache: String? = name

    // Holds either the decoded raw value (`String`) or a `ValueSlot` with that string, the codec, the decode context
    // and the result of the last typed decode. One field for both keeps the node as small as before the typed model, so
    // parsing to the tree pays nothing for the cache.
    @Volatile
    private var valueCache: Any? = value

    @Volatile
    private var parametersCache: Array<ICalParameter>? = parameters

    /**
     * The canonical name in upper case, such as `DTSTART`; the names the standard defines share one string instance.
     */
    public val name: String
        get() {
            if (nameId >= 0) return KnownNames.name(nameId)
            val cached = nameCache
            if (cached != null) return cached
            val decoded = canonicalName(source!!, start, nameEnd, textFlags)
            nameCache = decoded
            return decoded
        }

    /** The parameters in source order. The list is empty for none. */
    public val parameters: List<ICalParameter>
        get() = ArrayView(parameterArray())

    /**
     * The value as written after the colon, with folding removed. It is not unescaped; an empty string for no value.
     */
    public val value: String
        get() {
            val cached = valueCache
            if (cached is String) return cached
            if (cached is ValueSlot) return cached.text
            val decoded = decodeText(source!!, valueStart, end, textFlags, caret = false)
            valueCache = decoded
            return decoded
        }

    /**
     * Where the property is in the input, or `null` for a property that was built by hand. For input that was changed
     * by an interceptor it is the position of the original line.
     */
    public val span: SourceSpan?
        get() = if (line == 0) {
            null
        } else {
            SourceSpan((start + offsetShift).toLong(), (end + offsetShift).toLong(), line, column)
        }

    internal val parameterCount: Int
        get() {
            val data = parameterData
            if (data != null) return data[0]
            return parametersCache?.size ?: 0
        }

    /**
     * Returns the first parameter named [name], ignoring ASCII case, or `null` when there is none.
     *
     * @param name a parameter name such as `TZID`.
     */
    public fun parameter(name: String): ICalParameter? {
        if (parameterCount == 0) return null
        val query = canonicalName(name)
        for (parameter in parameterArray()) {
            if (parameter.name == query) return parameter
        }
        return null
    }

    internal val isParsed: Boolean get() = source != null

    internal fun cachedValue(codec: Any, context: Any): Any? {
        val cached = valueCache
        return if (cached is ValueSlot && cached.codec === codec && cached.context === context) cached.result else null
    }

    internal fun cacheValue(codec: Any, context: Any, result: Any) {
        valueCache = ValueSlot(value, codec, context, result)
    }

    internal fun valueSpan(charIndex: Int): SourceSpan {
        if (line == 0) return SourceSpan(charIndex.toLong(), charIndex.toLong(), 0, 0)
        val source = source ?: return spanAtStart()
        return spanAt(source, sourceIndexOfChar(source, charIndex))
    }

    internal fun parameterSpan(parameter: ICalParameter, valueIndex: Int): SourceSpan {
        if (line == 0) return SourceSpan(0L, 0L, 0, 0)
        val source = source ?: return spanAtStart()
        val at = parameter.valueSourceStart(valueIndex)
        return if (at < 0) spanAtStart() else spanAt(source, at)
    }

    private fun spanAtStart(): SourceSpan {
        val offset = (start + offsetShift).toLong()
        return SourceSpan(offset, offset, line, column)
    }

    // Counts the line breaks of folding between the start of the property and the byte, so a problem on a continuation
    // line gets that physical line and a column from its first byte (the folding space is column 1); on the first
    // physical line the column is the column of the property plus the byte distance.
    private fun spanAt(source: ByteArray, index: Int): SourceSpan {
        var physical = line
        var lineStart = -1
        if (textFlags and TEXT_FOLDED != 0) {
            var i = start
            while (i < index) {
                val byte = source[i]
                if (byte == CR || byte == LF) {
                    if (byte == CR && i + 1 < end && source[i + 1] == LF) i += 1
                    i += 1
                    physical += 1
                    lineStart = i
                    continue
                }
                i += 1
            }
        }
        val offset = (index + offsetShift).toLong()
        val at = if (lineStart < 0) column + (index - start) else index - lineStart + 1
        return SourceSpan(offset, offset, physical, at)
    }

    // Maps a UTF-16 index of the decoded value back to the source byte it came from: folds (a line break and one space
    // or tab) are skipped and invalid UTF-8 is walked exactly as `decodeUtf8Into` decodes it, one U+FFFD per maximal
    // invalid subsequence, two chars for a supplementary code point.
    private fun sourceIndexOfChar(source: ByteArray, charIndex: Int): Int {
        val folded = textFlags and TEXT_FOLDED != 0
        var chars = 0
        var i = skipFold(source, valueStart, folded)
        while (i < end && chars < charIndex) {
            val lead = source[i].toInt() and 0xFF
            i = skipFold(source, i + 1, folded)
            if (lead < UTF8_CONTINUATION_LOW) {
                chars += 1
                continue
            }
            val info = utf8LeadInfo(lead)
            if (info == 0) {
                chars += 1
                continue
            }
            var need = utf8LeadNeed(info)
            var low = utf8LeadLow(info)
            var high = utf8LeadHigh(info)
            val supplementary = need == 3
            while (need > 0 && i < end) {
                val next = source[i].toInt() and 0xFF
                if (next < low || next > high) break
                low = UTF8_CONTINUATION_LOW
                high = UTF8_CONTINUATION_HIGH
                need -= 1
                i = skipFold(source, i + 1, folded)
            }
            chars += if (need == 0 && supplementary) 2 else 1
        }
        return if (i > end) end else i
    }

    private fun skipFold(source: ByteArray, from: Int, folded: Boolean): Int {
        if (!folded) return from
        var i = from
        while (i < end) {
            val byte = source[i]
            if (byte != CR && byte != LF) return i
            if (byte == CR && i + 1 < end && source[i + 1] == LF) i += 1
            i += 2
        }
        return i
    }

    // Copy without a span, for a property built from an anonymized line.
    internal fun detached(): ICalProperty = ICalProperty(
        source,
        start,
        nameEnd,
        valueStart,
        end,
        nameId,
        textFlags,
        parameterData,
        0.0,
        0,
        0,
        nameCache,
        cachedText(),
        parametersCache,
    )

    private fun cachedText(): String? {
        val cached = valueCache
        return if (cached is ValueSlot) cached.text else cached as String?
    }

    // Whether the line holds a CONTROL character of RFC 5545 §3.1 other than HTAB, read from the bytes, so nothing is
    // decoded: a control character is one byte in UTF-8, the encoding of the tree. CR and LF inside the range are folds.
    // A property built by hand has none, as the builder refuses them.
    internal fun hasControlCharacter(): Boolean {
        val bytes = source ?: return false
        for (index in start until end) {
            val byte = bytes[index]
            if (byte >= 0 && (byte < SPACE_BYTE || byte == DEL_BYTE) && byte != HTAB && byte != CR &&
                byte != LF
            ) {
                return true
            }
        }
        return false
    }

    internal fun writeSource(out: ContentLineWriter) {
        out.rawLine(source!!, start, end, textFlags and TEXT_FOLDED != 0)
    }

    internal fun parameterNodes(): Array<ICalParameter> = parameterArray()

    // The values of one parameter straight from the packed node data, without creating `ICalParameter` objects:
    // `Referenced` pruning reads the TZID of every selected property, and going through `parameters` made the Schedule
    // preset slower than a full parse.
    internal fun collectParameterValues(id: Int, out: MutableCollection<String>) {
        val data = parameterData
        if (data == null || parametersCache != null) {
            for (parameter in parameterArray()) {
                if (parameter.name == KnownNames.name(id)) out.addAll(parameter.values)
            }
            return
        }
        val source = source!!
        var at = 1
        for (parameter in 0 until data[0]) {
            val nameId = data[at + PARAMETER_NAME_ID]
            val matched = if (nameId == UNRESOLVED_NAME) {
                KnownNames.isName(id, source, data[at + PARAMETER_NAME_START], data[at + PARAMETER_NAME_END])
            } else {
                nameId == id
            }
            val count = data[at + PARAMETER_VALUE_COUNT]
            if (matched) {
                for (index in 0 until count) {
                    val value = at + PARAMETER_FIRST_VALUE + index * PARAMETER_VALUE_FIELDS
                    val caret = data[value + PARAMETER_VALUE_FLAGS] and VALUE_HAS_CARET != 0
                    out.add(decodeText(source, data[value], data[value + 1], textFlags, caret))
                }
            }
            at += PARAMETER_FIRST_VALUE + count * PARAMETER_VALUE_FIELDS
        }
    }

    internal fun hasName(queryId: Int, query: String): Boolean =
        if (queryId >= 0) nameId == queryId else nameId < 0 && name == query

    private fun parameterArray(): Array<ICalParameter> {
        val cached = parametersCache
        if (cached != null) return cached
        val data = parameterData ?: return EMPTY_PARAMETERS
        val count = data[0]
        val source = source!!
        var at = 1
        val array = Array(count) {
            val parameter = ICalParameter(source, data, at, textFlags, null, null)
            at += PARAMETER_FIRST_VALUE + data[at + PARAMETER_VALUE_COUNT] * PARAMETER_VALUE_FIELDS
            parameter
        }
        parametersCache = array
        return array
    }

    /** Returns `true` when [other] is a property with the same name, parameters and value. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ICalProperty) return false
        val count = parameterCount
        if (count != other.parameterCount) return false
        if (nameId != other.nameId || (nameId < 0 && name != other.name)) return false
        if (value != other.value) return false
        if (count == 0) return true
        val left = parameterArray()
        val right = other.parameterArray()
        for (index in 0 until count) {
            if (left[index] != right[index]) return false
        }
        return true
    }

    /** Returns a hash code over the name, the parameters and the value. */
    override fun hashCode(): Int {
        var result = name.hashCode()
        for (parameter in parameterArray()) result = 31 * result + parameter.hashCode()
        return 31 * result + value.hashCode()
    }

    /** Returns the name and the number of parameters; never the value. */
    override fun toString(): String = describe("ICalProperty", "name", name, "parameters", parameterCount)

    /** Collects the parameters and the value of a new [ICalProperty]. A builder is not thread-safe. */
    @ICalDsl
    public class Builder internal constructor(private val name: String) {
        private val parameters = ArrayList<ICalParameter>()
        private var value = ""

        /** Appends [parameter] after the parameters added so far. */
        public fun parameter(parameter: ICalParameter): Builder {
            parameters += parameter
            return this
        }

        /**
         * Appends a parameter with the given [name] and [values].
         *
         * @throws IllegalArgumentException if [name] is invalid or no value is given.
         */
        public fun parameter(name: String, vararg values: String): Builder = parameter(ICalParameter.of(name, *values))

        /**
         * Sets the value, which is written as is: escape `TEXT` yourself or use the typed model. The default is an
         * empty string.
         */
        public fun value(value: String): Builder {
            this.value = value
            return this
        }

        /**
         * Creates the property.
         *
         * @throws IllegalArgumentException if the value contains a control character other than a tab.
         */
        public fun build(): ICalProperty = manual(name, value, parameters.toTypedArray())
    }

    /** Creates properties by hand. */
    public companion object {
        /**
         * Creates a property with no parameters.
         *
         * @param name letters, digits and hyphens only; the stored name is upper case; `BEGIN` and `END` are not
         *   allowed.
         * @param value the value as it is written; a control character other than a tab is not allowed.
         * @throws IllegalArgumentException if [name] or [value] is invalid.
         */
        @JvmStatic
        public fun of(name: String, value: String): ICalProperty = manual(checkedPropertyName(name), value, null)

        /**
         * Returns a builder for a property named [name].
         *
         * @param name letters, digits and hyphens only; `BEGIN` and `END` are not allowed.
         * @throws IllegalArgumentException if [name] is invalid.
         */
        @JvmStatic
        public fun builder(name: String): Builder = Builder(checkedPropertyName(name))

        private fun manual(name: String, value: String, parameters: Array<ICalParameter>?): ICalProperty {
            val array = if (parameters == null || parameters.isEmpty()) null else parameters
            val checked = checkedValue(value)
            return ICalProperty(null, 0, 0, 0, 0, KnownNames.find(name), 0, null, 0.0, 0, 0, name, checked, array)
        }
    }
}

private val EMPTY_PARAMETERS: Array<ICalParameter> = emptyArray()

internal class ValueSlot(val text: String, val codec: Any, val context: Any, val result: Any)

private const val SPACE_BYTE: Byte = 0x20
private const val DEL_BYTE: Byte = 0x7F
