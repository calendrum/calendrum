package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.compareUnsigned
import io.github.calendrum.mixHash
import io.github.calendrum.orderByHash
import kotlin.jvm.JvmStatic

/**
 * Identifies a component of a feed between two fingerprint runs.
 *
 * A `VTIMEZONE` is identified by its `TZID`, and any other component with a `UID` by the pair of `UID` and
 * `RECURRENCE-ID`, within its calendar. A component with neither is identified by its name and its position among
 * components of that name ([ordinal]). Keys are equal when all their parts are.
 *
 * @property calendarIndex the 0-based index of the `VCALENDAR` the component is in, or -1 outside any calendar.
 * @property name the canonical component name.
 * @property uid the `UID` of the component, or `null`.
 * @property recurrenceId the `RECURRENCE-ID` text, or `null`.
 * @property tzid the `TZID` of a `VTIMEZONE`, or `null`.
 * @property ordinal the position among components of the same name without identity, or -1 when it has one.
 */
public class ComponentKey internal constructor(
    public val calendarIndex: Int,
    public val name: String,
    public val uid: String?,
    public val recurrenceId: String?,
    public val tzid: String?,
    public val ordinal: Int,
) {
    /** Returns `true` when [other] is a ComponentKey equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComponentKey) return false
        return calendarIndex == other.calendarIndex &&
            ordinal == other.ordinal &&
            name == other.name &&
            uid == other.uid &&
            recurrenceId == other.recurrenceId &&
            tzid == other.tzid
    }

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int {
        var result = calendarIndex
        result = 31 * result + name.hashCode()
        result = 31 * result + (uid?.hashCode() ?: 0)
        result = 31 * result + (recurrenceId?.hashCode() ?: 0)
        result = 31 * result + (tzid?.hashCode() ?: 0)
        return 31 * result + ordinal
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ComponentKey(calendarIndex=$calendarIndex, name=$name, ordinal=$ordinal)"
}

// `line` and `charset` (internal) are the line of the BEGIN and the charset of the input, for `parseComponent`; they
// are part of the encoded text but not of the public API.

/**
 * The fingerprint of one component: its key, a hash of its content and where it is in the input.
 *
 * Two fingerprints with the same key and hash describe components with the same content, apart from the ignored
 * properties. Use [io.github.calendrum.syntax.ICalParser.parseComponent] to parse the component at its offsets.
 *
 * @property key the identity of the component.
 * @property hash a 64-bit xxHash64 of the unfolded lines of the component with names in upper case and the ignored
 * properties left out; values and the order of lines matter, folding does not.
 * @property startOffset the offset of the first byte of the component's `BEGIN` line.
 * @property endOffset the offset just after the component's `END` line.
 */
public class ComponentFingerprint internal constructor(
    public val key: ComponentKey,
    public val hash: Long,
    public val startOffset: Long,
    public val endOffset: Long,
    internal val line: Int,
    internal val charset: InputCharset,
) {
    /** Returns `true` when [other] is a ComponentFingerprint equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComponentFingerprint) return false
        return hash == other.hash &&
            startOffset == other.startOffset &&
            endOffset == other.endOffset &&
            line == other.line &&
            charset == other.charset &&
            key == other.key
    }

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + hash.hashCode()
        result = 31 * result + startOffset.hashCode()
        return 31 * result + endOffset.hashCode()
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ComponentFingerprint(calendarIndex=${key.calendarIndex}, name=${key.name}, " +
        "offsets=$startOffset..$endOffset)"
}

/**
 * The difference between two fingerprint runs of a feed, as returned by [Fingerprints.diff].
 *
 * @property unchangedCount how many components have the same key and hash in both runs.
 */
public class FingerprintDiff internal constructor(
    addedArray: Array<ComponentFingerprint>,
    removedArray: Array<ComponentFingerprint>,
    changedArray: Array<ComponentFingerprint>,
    public val unchangedCount: Int,
) {
    /** Components that are in the newer run only, with their offsets in the newer input. */
    public val added: List<ComponentFingerprint> = ArrayView(addedArray)

    /** Components that are in the older run only, with their offsets in the older input. */
    public val removed: List<ComponentFingerprint> = ArrayView(removedArray)

    /** Components with the same key but another hash, with their offsets in the newer input. */
    public val changed: List<ComponentFingerprint> = ArrayView(changedArray)

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "FingerprintDiff(added=${added.size}, removed=${removed.size}, " +
        "changed=${changed.size}, unchanged=$unchangedCount)"
}

/**
 * The fingerprints of all components of a feed, in input order, computed in one pass over the bytes without parsing
 * values.
 *
 * Store [encodeToString] and compare it with a later run through [diff] to learn which components were added, removed
 * or changed. Hashes depend on the algorithm, so fingerprints of different [algorithmVersion]s cannot be compared. An
 * immutable value.
 */
public class Fingerprints internal constructor(
    private val entryArray: Array<ComponentFingerprint>,
    /** The version of the hashing algorithm; it changes whenever hashes would differ for the same content. */
    public val algorithmVersion: Int,
    internal val charset: InputCharset,
) {
    /** One fingerprint per child of each `VCALENDAR`, plus any component outside a calendar, in input order. */
    public val entries: List<ComponentFingerprint> = ArrayView(entryArray)

    // Both runs are ordered by the hash of the key, then by the key, then by index, and one merge walks them together, so
    // duplicate keys are matched first to first. Keys hold strings from the input, so no hash table holds them (see
    // InputKeys.kt): the hash only orders, by a radix sort, and keys that share a hash fall back to a merge sort that
    // compares them. Sorting by the strings alone, with boxed indices, made the comparison 4.7 times slower.

    /**
     * Compares these fingerprints with an older run.
     *
     * @param previous the older fingerprints, for example restored by [decodeFromString].
     * @throws IllegalArgumentException if the two runs have different algorithm versions.
     */
    public fun diff(previous: Fingerprints): FingerprintDiff {
        require(previous.algorithmVersion == algorithmVersion) {
            "Cannot compare fingerprints of algorithm versions $algorithmVersion and ${previous.algorithmVersion}"
        }
        val old = previous.entryArray
        val new = entryArray
        val oldHashes = keyHashes(old)
        val newHashes = keyHashes(new)
        val oldOrder = orderByHash(oldHashes) { a, b -> compareKeys(old[a].key, old[b].key) }
        val newOrder = orderByHash(newHashes) { a, b -> compareKeys(new[a].key, new[b].key) }
        val match = IntArray(new.size)
        match.fill(-1)
        val matched = BooleanArray(old.size)
        var inNew = 0
        var inOld = 0
        while (inNew < newOrder.size && inOld < oldOrder.size) {
            val newIndex = newOrder[inNew]
            val oldIndex = oldOrder[inOld]
            val order =
                compareHashedKeys(newHashes[newIndex], new[newIndex].key, oldHashes[oldIndex], old[oldIndex].key)
            when {
                order < 0 -> inNew++

                order > 0 -> inOld++

                else -> {
                    match[newIndex] = oldIndex
                    matched[oldIndex] = true
                    inNew++
                    inOld++
                }
            }
        }
        val added = ArrayList<ComponentFingerprint>()
        val changed = ArrayList<ComponentFingerprint>()
        var unchanged = 0
        for (index in new.indices) {
            val oldIndex = match[index]
            when {
                oldIndex < 0 -> added += new[index]
                old[oldIndex].hash == new[index].hash -> unchanged++
                else -> changed += new[index]
            }
        }
        val removed = ArrayList<ComponentFingerprint>()
        for (index in old.indices) {
            if (!matched[index]) removed += old[index]
        }
        return FingerprintDiff(added.toTypedArray(), removed.toTypedArray(), changed.toTypedArray(), unchanged)
    }

    // The text format is `ICSFP <version> <charset>`, then one line per entry with ten tab-separated fields; `-` is a
    // null string, `=` starts a present one; backslash, tab, LF and CR are escaped. The charset is that of the input,
    // so `parseComponent` knows whether the offsets are in the input bytes or in decoded text.

    /** Returns a compact, versioned text form that [decodeFromString] reads back. */
    @ExperimentalICalApi
    public fun encodeToString(): String = encodeFingerprints(this)

    /** Returns `true` when [other] is a Fingerprints equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Fingerprints) return false
        return algorithmVersion == other.algorithmVersion && charset == other.charset && entries == other.entries
    }

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = 31 * algorithmVersion + entries.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Fingerprints(entries=${entries.size}, algorithmVersion=$algorithmVersion)"

    /** Restores fingerprints from text. */
    public companion object {
        /**
         * Reads the text form made by [encodeToString].
         *
         * @throws IllegalArgumentException if the text is malformed or was made by another algorithm version.
         */
        @ExperimentalICalApi
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        public fun decodeFromString(text: String): Fingerprints = decodeFingerprints(text)
    }
}

internal const val FINGERPRINT_ALGORITHM_VERSION: Int = 1

private const val HEADER = "ICSFP"
private const val FIELDS = 10
private const val HASH_DIGITS = 16
private const val HEX_RADIX = 16

private fun encodeFingerprints(fingerprints: Fingerprints): String {
    val out = StringBuilder(64 + fingerprints.entries.size * 64)
    out.append(HEADER).append(' ').append(fingerprints.algorithmVersion).append(' ')
    out.append(fingerprints.charset.canonicalName).append('\n')
    for (entry in fingerprints.entries) {
        val key = entry.key
        out.append(key.calendarIndex).append('\t')
        escape(out, key.name)
        out.append('\t')
        optional(out, key.uid)
        out.append('\t')
        optional(out, key.recurrenceId)
        out.append('\t')
        optional(out, key.tzid)
        out.append('\t').append(key.ordinal).append('\t')
        val hex = entry.hash.toULong().toString(HEX_RADIX)
        for (pad in hex.length until HASH_DIGITS) out.append('0')
        out.append(hex).append('\t')
        out.append(entry.startOffset).append('\t').append(entry.endOffset).append('\t').append(entry.line).append('\n')
    }
    return out.toString()
}

private fun optional(out: StringBuilder, value: String?) {
    if (value == null) {
        out.append('-')
    } else {
        out.append('=')
        escape(out, value)
    }
}

private fun escape(out: StringBuilder, value: String) {
    for (char in value) {
        when (char) {
            '\\' -> out.append("\\\\")
            '\t' -> out.append("\\t")
            '\n' -> out.append("\\n")
            '\r' -> out.append("\\r")
            else -> out.append(char)
        }
    }
}

private fun decodeFingerprints(text: String): Fingerprints {
    val lines = text.split('\n')
    if (lines.size < 2 || lines.last().isNotEmpty()) malformed(1)
    val header = lines[0].split(' ')
    if (header.size != 3 || header[0] != HEADER) malformed(1)
    val version = header[1].toIntOrNull()
    require(version == FINGERPRINT_ALGORITHM_VERSION) { "Unsupported fingerprint algorithm version ${header[1]}" }
    val charset = InputCharset.entries.firstOrNull { it.canonicalName == header[2] } ?: malformed(1)
    val entries = Array(lines.size - 2) { index -> decodeEntry(lines[index + 1], index + 2, charset) }
    return Fingerprints(entries, FINGERPRINT_ALGORITHM_VERSION, charset)
}

private fun decodeEntry(line: String, number: Int, charset: InputCharset): ComponentFingerprint {
    val fields = line.split('\t')
    if (fields.size != FIELDS) malformed(number)
    val calendar = fields[0].toIntOrNull() ?: malformed(number)
    val name = unescape(fields[1], number)
    val uid = optional(fields[2], number)
    val recurrenceId = optional(fields[3], number)
    val tzid = optional(fields[4], number)
    val ordinal = fields[5].toIntOrNull() ?: malformed(number)
    if (fields[6].length != HASH_DIGITS) malformed(number)
    val hash = fields[6].toULongOrNull(HEX_RADIX)?.toLong() ?: malformed(number)
    val start = fields[7].toLongOrNull() ?: malformed(number)
    val end = fields[8].toLongOrNull() ?: malformed(number)
    val lineNumber = fields[9].toIntOrNull() ?: malformed(number)
    if (calendar < -1 || ordinal < -1 || start < 0 || end < start || lineNumber < 1) {
        malformed(number)
    }
    val key = ComponentKey(calendar, name, uid, recurrenceId, tzid, ordinal)
    return ComponentFingerprint(key, hash, start, end, lineNumber, charset)
}

private fun optional(field: String, number: Int): String? = when {
    field == "-" -> null
    field.startsWith('=') -> unescape(field.substring(1), number)
    else -> malformed(number)
}

private fun unescape(field: String, number: Int): String {
    if (field.indexOf('\\') < 0) return field
    val out = StringBuilder(field.length)
    var index = 0
    while (index < field.length) {
        val char = field[index++]
        if (char != '\\') {
            out.append(char)
            continue
        }
        if (index == field.length) malformed(number)
        when (field[index++]) {
            '\\' -> out.append('\\')
            't' -> out.append('\t')
            'n' -> out.append('\n')
            'r' -> out.append('\r')
            else -> malformed(number)
        }
    }
    return out.toString()
}

private fun malformed(line: Int): Nothing = throw IllegalArgumentException("Malformed fingerprints at line $line")

// Only an order for the sort (orderByHash), never the slot of a table: two keys with one hash are told apart by
// compareKeys.
private fun keyHashes(entries: Array<ComponentFingerprint>): IntArray = IntArray(entries.size) { index ->
    val key = entries[index].key
    var hash = key.calendarIndex
    hash = hash * HASH_MULTIPLIER + key.name.hashCode()
    hash = hash * HASH_MULTIPLIER + (key.uid?.hashCode() ?: -1)
    hash = hash * HASH_MULTIPLIER + (key.recurrenceId?.hashCode() ?: -1)
    hash = hash * HASH_MULTIPLIER + (key.tzid?.hashCode() ?: -1)
    mixHash(hash * HASH_MULTIPLIER + key.ordinal)
}

private const val HASH_MULTIPLIER = 31

private fun compareHashedKeys(leftHash: Int, left: ComponentKey, rightHash: Int, right: ComponentKey): Int {
    val order = compareUnsigned(leftHash, rightHash)
    return if (order != 0) order else compareKeys(left, right)
}

private fun compareKeys(left: ComponentKey, right: ComponentKey): Int {
    if (left.calendarIndex != right.calendarIndex) return left.calendarIndex.compareTo(right.calendarIndex)
    var result = left.name.compareTo(right.name)
    if (result != 0) return result
    result = compareNullable(left.uid, right.uid)
    if (result != 0) return result
    result = compareNullable(left.recurrenceId, right.recurrenceId)
    if (result != 0) return result
    result = compareNullable(left.tzid, right.tzid)
    return if (result != 0) result else left.ordinal.compareTo(right.ordinal)
}

private fun compareNullable(left: String?, right: String?): Int = when {
    left == null -> if (right == null) 0 else -1
    right == null -> 1
    else -> left.compareTo(right)
}
