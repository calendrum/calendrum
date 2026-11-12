package io.github.calendrum.syntax

import io.github.calendrum.ProblemListener
import io.github.calendrum.SortedStringMap

// One pass over `ContentReader` with the `Full` selection, the default limits and charset detection without a hint. The
// canonical text of the current component is collected in `canonical` (`BEGIN:`/`END:` and the upper-cased name from a
// small stack, since implicit ends have no line; property names and parameter names upper-cased, everything else as the
// bytes of the unfolded line) and hashed once at its END: one call instead of a streaming state, so JS crosses into the
// hash once per component; the buffer grows to the largest component and is reused.
internal class FingerprintScanner(private val options: FingerprintOptions) {
    private var canonical = ByteArray(INITIAL_CANONICAL)
    private var canonicalSize = 0
    private var names = ByteArray(INITIAL_NAMES)
    private var nameStarts = IntArray(INITIAL_DEPTH)
    private var root = 0
    private var calendars = 0
    private var calendar = -1
    private var componentName = ""
    private var uid: String? = null
    private var recurrenceId: String? = null
    private var tzid: String? = null
    private var startOffset = 0L
    private var line = 0

    // Component names come from the input: counted in trees, not hashed (see InputKeys.kt).
    private var ordinals = SortedStringMap<Int>()
    private val outsideOrdinals = SortedStringMap<Int>()
    private val pending = ArrayList<Pending>()

    // Entries are built at the end because the charset is decided at the first non-ASCII byte, possibly after some
    // components.
    fun scan(bytes: ByteArray): Fingerprints {
        val reader = ContentReader(
            bytes,
            0,
            bytes.size,
            ContentSelection.Full,
            options.limits,
            options.problemListener ?: IGNORE_PROBLEMS,
            true,
            null,
        )
        while (true) {
            when (reader.next()) {
                ContentReader.END_OF_INPUT -> break
                ContentReader.BEGIN -> begin(reader)
                ContentReader.PROPERTY -> if (root > 0) property(reader)
                else -> if (root > 0) end(reader)
            }
        }
        val charset = reader.decodedCharset
        val entries = Array(pending.size) { pending[it].fingerprint(charset) }
        return Fingerprints(entries, FINGERPRINT_ALGORITHM_VERSION, charset)
    }

    // Depth 1 VCALENDAR opens a calendar (properties at depth 1 are not hashed); any other BEGIN with no open root
    // starts a fingerprint: a child of the calendar (depth 2) or a top-level component outside one (depth 1,
    // calendarIndex -1).
    private fun begin(reader: ContentReader) {
        val depth = reader.depth
        val contentLine = reader.line
        if (root == 0) {
            if (depth == 1 && reader.componentId == KnownNames.VCALENDAR) {
                calendar = calendars++
                ordinals = SortedStringMap()
                return
            }
            root = depth
            val id = reader.componentId
            componentName = if (id >= 0) KnownNames.name(id) else canonicalName(valueOf(contentLine))
            uid = null
            recurrenceId = null
            tzid = null
            startOffset = reader.componentStartOffset
            line = reader.lineNumber
            canonicalSize = 0
        }
        val level = depth - root
        if (level + 1 >= nameStarts.size) nameStarts = nameStarts.copyOf(nameStarts.size * 2)
        val start = if (level == 0) 0 else nameStarts[level]
        val length = contentLine.valueEnd - contentLine.valueStart
        if (names.size < start + length) names = names.copyOf(maxOf(names.size * 2, start + length))
        var at = start
        for (index in contentLine.valueStart until contentLine.valueEnd) {
            names[at++] = upperCase(contentLine.bytes[index])
        }
        nameStarts[level] = start
        nameStarts[level + 1] = at
        append(BEGIN_PREFIX, 0, BEGIN_PREFIX.size)
        append(names, start, at)
        append(CRLF, 0, CRLF.size)
    }

    private fun property(reader: ContentReader) {
        val contentLine = reader.line
        if (reader.depth == root) {
            when (contentLine.nameId) {
                UID -> if (uid == null) uid = valueOf(contentLine)
                RECURRENCE_ID -> if (recurrenceId == null) recurrenceId = valueOf(contentLine)
                TZID -> if (tzid == null) tzid = valueOf(contentLine)
            }
        }
        if (options.ignores(contentLine)) return
        val bytes = contentLine.bytes
        appendUpper(bytes, contentLine.nameStart, contentLine.nameEnd)
        var at = contentLine.nameEnd
        for (parameter in 0 until contentLine.parameterCount) {
            val nameStart = contentLine.parameterNameStart(parameter)
            val nameEnd = contentLine.parameterNameEnd(parameter)
            append(bytes, at, nameStart)
            appendUpper(bytes, nameStart, nameEnd)
            at = nameEnd
        }
        append(bytes, at, contentLine.valueEnd)
        append(CRLF, 0, CRLF.size)
    }

    private fun end(reader: ContentReader) {
        val level = reader.depth - root
        append(END_PREFIX, 0, END_PREFIX.size)
        append(names, nameStarts[level], nameStarts[level + 1])
        append(CRLF, 0, CRLF.size)
        if (level > 0) return
        pending += Pending(key(), xxHash64(canonical, 0, canonicalSize), startOffset, reader.componentEndOffset, line)
        root = 0
    }

    // Ranges shorter than `SHORT_COPY` (CRLF, prefixes, names) are copied by a loop: on JS `copyInto` makes a
    // `subarray` view per call, which costs more than the copy itself (fingerprints were 14 % slower on JS).
    private fun append(bytes: ByteArray, from: Int, to: Int) {
        var at = reserve(to - from)
        val target = canonical
        if (to - from >= SHORT_COPY) {
            bytes.copyInto(target, at, from, to)
            return
        }
        for (index in from until to) target[at++] = bytes[index]
    }

    private fun appendUpper(bytes: ByteArray, from: Int, to: Int) {
        var at = reserve(to - from)
        val target = canonical
        for (index in from until to) target[at++] = upperCase(bytes[index])
    }

    private fun reserve(length: Int): Int {
        val at = canonicalSize
        if (canonical.size - at < length) canonical = canonical.copyOf(maxOf(canonical.size * 2, at + length))
        canonicalSize = at + length
        return at
    }

    // VTIMEZONE by TZID, others by UID with RECURRENCE-ID, the rest by an ordinal among components of the same name in
    // the same calendar (top-level components outside calendars count separately).
    private fun key(): ComponentKey {
        val outside = root == 1
        val owner = if (outside) -1 else calendar
        val zone = componentName == VTIMEZONE_NAME
        val tzid = tzid
        val uid = uid
        return when {
            zone && tzid != null -> ComponentKey(owner, componentName, null, null, tzid, -1)

            !zone && uid != null -> ComponentKey(owner, componentName, uid, recurrenceId, null, -1)

            else -> {
                val counters = if (outside) outsideOrdinals else ordinals
                val ordinal = counters[componentName] ?: 0
                counters[componentName] = ordinal + 1
                ComponentKey(owner, componentName, null, null, null, ordinal)
            }
        }
    }

    private fun valueOf(line: ContentLine): String =
        decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8)

    private class Pending(
        val key: ComponentKey,
        val hash: Long,
        val startOffset: Long,
        val endOffset: Long,
        val line: Int,
    ) {
        fun fingerprint(charset: InputCharset): ComponentFingerprint =
            ComponentFingerprint(key, hash, startOffset, endOffset, line, charset)
    }

    private companion object {
        private const val INITIAL_CANONICAL = 4096
        private const val SHORT_COPY = 16
        private const val INITIAL_NAMES = 64
        private const val INITIAL_DEPTH = 8
        private const val VTIMEZONE_NAME = "VTIMEZONE"

        private val BEGIN_PREFIX = "BEGIN:".encodeToByteArray()
        private val END_PREFIX = "END:".encodeToByteArray()
        private val CRLF = "\r\n".encodeToByteArray()
        private val UID = KnownNames.find("UID")
        private val RECURRENCE_ID = KnownNames.find("RECURRENCE-ID")
        private val TZID = KnownNames.find("TZID")
        private val IGNORE_PROBLEMS = ProblemListener { }
    }
}

// File-level function for the same reason as `isFoldByte` in `ContentLexer.kt`: no companion instance on the call path.
private fun upperCase(byte: Byte): Byte =
    if (byte >= LOWER_A && byte <= LOWER_Z) (byte.toInt() - CASE_BIT).toByte() else byte

private const val LOWER_A: Byte = 0x61
private const val LOWER_Z: Byte = 0x7A
private const val CASE_BIT = 0x20
