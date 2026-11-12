package io.github.calendrum.syntax

import io.github.calendrum.ICalDsl
import io.github.calendrum.ProblemListener
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * Settings of component fingerprinting: which properties are left out of the hash, and the limits of reading.
 *
 * An immutable value; build one with [builder]. [Default] ignores `DTSTAMP`, which many servers refresh on every export
 * and which would otherwise make every component look changed.
 *
 * @property ignoredProperties the canonical names of the properties that do not take part in the hash.
 * @property limits the limits of reading. What lies beyond them does not take part in any hash, so that a change there is not
 *   seen: for a file from a stranger, set [problemListener] as well.
 * @property problemListener receives the problems of reading, such as a line above the limit (`ICAL-S010`), or `null`.
 */
public class FingerprintOptions internal constructor(
    public val ignoredProperties: Set<String>,
    public val limits: ParseLimits,
    public val problemListener: ProblemListener?,
) {
    private val ignoredIds: IntArray
    private val ignoredNames: Array<ByteArray>

    init {
        val ids = ArrayList<Int>()
        val names = ArrayList<ByteArray>()
        for (name in ignoredProperties) {
            val id = KnownNames.find(name)
            if (id >= 0) ids += id else names += name.encodeToByteArray()
        }
        ignoredIds = ids.toIntArray()
        ignoredNames = names.toTypedArray()
    }

    // Known names are compared by id, other names by their upper-cased bytes.
    internal fun ignores(line: ContentLine): Boolean {
        val id = line.nameId
        if (id >= 0) {
            for (ignored in ignoredIds) {
                if (ignored == id) return true
            }
            return false
        }
        val bytes = line.bytes
        val from = line.nameStart
        val length = line.nameEnd - from
        for (name in ignoredNames) {
            if (name.size == length && sameUpper(name, bytes, from)) return true
        }
        return false
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "FingerprintOptions(ignoredProperties=$ignoredProperties)"

    /** Collects [FingerprintOptions] values. Not thread-safe. */
    @ICalDsl
    public class Builder internal constructor() {
        /** The names of the properties to ignore; `DTSTAMP` by default. */
        public var ignoredProperties: Set<String> = DEFAULT_IGNORED

        /** The limits of reading; [ParseLimits.Default] by default. */
        public var limits: ParseLimits = ParseLimits.Default

        /** Receives the problems of reading; `null` by default. */
        public var problemListener: ProblemListener? = null

        /** Sets [ignoredProperties] to [names]. */
        public fun ignoredProperties(names: Set<String>): Builder {
            ignoredProperties = names
            return this
        }

        /** Sets [limits] to [value]. */
        public fun limits(value: ParseLimits): Builder {
            limits = value
            return this
        }

        /** Sets [problemListener] to [value]. */
        public fun problemListener(value: ProblemListener?): Builder {
            problemListener = value
            return this
        }

        /**
         * Creates the options.
         *
         * @throws IllegalArgumentException if a name is not a valid property name.
         */
        public fun build(): FingerprintOptions {
            val names = LinkedHashSet<String>()
            for (name in ignoredProperties) names += checkedPropertyName(name)
            return FingerprintOptions(names, limits, problemListener)
        }
    }

    /** The shared defaults and a builder. */
    public companion object {
        /** Ignores only `DTSTAMP`. */
        @JvmField
        public val Default: FingerprintOptions = FingerprintOptions(DEFAULT_IGNORED, ParseLimits.Default, null)

        /** Returns a builder that starts from the defaults. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

private val DEFAULT_IGNORED: Set<String> = setOf("DTSTAMP")

private fun sameUpper(name: ByteArray, bytes: ByteArray, from: Int): Boolean {
    for (index in name.indices) {
        val byte = bytes[from + index].toInt()
        val upper = if (byte in LOWER_A..LOWER_Z) byte - CASE_BIT else byte
        if (upper != name[index].toInt()) return false
    }
    return true
}

private const val LOWER_A = 0x61
private const val LOWER_Z = 0x7A
private const val CASE_BIT = 0x20
