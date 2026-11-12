package io.github.calendrum.syntax

import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * A parameter of a property, such as `TZID=Europe/Moscow`: a name and one or more values ([RFC 5545
 * §3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.2)).
 *
 * Parameters of parsed input decode their name and values on first access. The quotes around a value are removed and a
 * `^'`, `^n` or `^^` escape of [RFC 6868](https://www.rfc-editor.org/rfc/rfc6868) is decoded. Equality compares the
 * name and the values; it ignores spelling and position. A parameter is immutable and can be read from any thread.
 */
public class ICalParameter internal constructor(
    private val source: ByteArray?,
    private val data: IntArray?,
    private val at: Int,
    private val textFlags: Int,
    name: String?,
    values: List<String>?,
) {
    @Volatile
    private var nameCache: String? = name

    @Volatile
    private var valuesCache: List<String>? = values

    /** The canonical name in upper case, such as `TZID`. */
    public val name: String
        get() {
            val cached = nameCache
            if (cached != null) return cached
            val data = data!!
            val source = source!!
            var id = data[at + PARAMETER_NAME_ID]
            if (id ==
                UNRESOLVED_NAME
            ) {
                id =
                    KnownNames.find(source, data[at + PARAMETER_NAME_START], data[at + PARAMETER_NAME_END])
            }
            val decoded = if (id >= 0) {
                KnownNames.name(id)
            } else {
                canonicalName(source, data[at + PARAMETER_NAME_START], data[at + PARAMETER_NAME_END], textFlags)
            }
            nameCache = decoded
            return decoded
        }

    /**
     * All values, in order, with quotes removed. The list is never empty for a parameter built by [of]; a parameter
     * read from input without `=` has none.
     */
    public val values: List<String>
        get() {
            val cached = valuesCache
            if (cached != null) return cached
            val data = data!!
            val source = source!!
            val count = data[at + PARAMETER_VALUE_COUNT]
            val decoded = Array(count) { index ->
                val value = at + PARAMETER_FIRST_VALUE + index * PARAMETER_VALUE_FIELDS
                val caret = data[value + PARAMETER_VALUE_FLAGS] and VALUE_HAS_CARET != 0
                decodeText(source, data[value], data[value + 1], textFlags, caret)
            }
            val list = ArrayView(decoded)
            valuesCache = list
            return list
        }

    // `value` is the first value or an empty string: a parsed parameter without `=` (ICAL-S012) has no values. A
    // hand-built one needs at least one, as `ContentLineWriter` does.

    /** The first value, or an empty string when there is none. */
    public val value: String
        get() {
            val values = values
            return if (values.isEmpty()) "" else values[0]
        }

    internal val valueCount: Int get() = data?.get(at + PARAMETER_VALUE_COUNT) ?: values.size

    internal fun valueSourceStart(index: Int): Int {
        val data = data ?: return -1
        if (index >= data[at + PARAMETER_VALUE_COUNT]) return data[at + PARAMETER_NAME_END]
        return data[at + PARAMETER_FIRST_VALUE + index * PARAMETER_VALUE_FIELDS]
    }

    /** Returns `true` when [other] is a parameter with the same name and values. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ICalParameter) return false
        return valueCount == other.valueCount && name == other.name && values == other.values
    }

    /** Returns a hash code over the name and the values. */
    override fun hashCode(): Int = 31 * name.hashCode() + values.hashCode()

    /** Returns the name and the number of values; never a value. */
    override fun toString(): String = describe("ICalParameter", "name", name, "values", valueCount)

    /** Creates parameters by hand. */
    public companion object {
        /**
         * Creates a parameter with the given [name] and [values].
         *
         * @param name letters, digits and hyphens only; the stored name is upper case.
         * @param values at least one value.
         * @throws IllegalArgumentException if [name] is invalid or no value is given.
         */
        @JvmStatic
        public fun of(name: String, vararg values: String): ICalParameter {
            val canonical = checkedName(name, PARAMETER_NAME)
            if (values.isEmpty()) throw IllegalArgumentException("A parameter needs at least one value")
            return ICalParameter(null, null, 0, 0, canonical, ArrayView(values.copyOf()))
        }

        private const val PARAMETER_NAME = "parameter name"
    }
}

internal fun parameterOf(name: String, values: Array<String>): ICalParameter =
    ICalParameter(null, null, 0, 0, name, ArrayView(values))
