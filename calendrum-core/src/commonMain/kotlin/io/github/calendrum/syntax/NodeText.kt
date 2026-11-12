package io.github.calendrum.syntax

internal const val TEXT_FOLDED: Int = 1
internal const val TEXT_INVALID_UTF8: Int = 2
internal const val TEXT_END_FOLDED: Int = 4

// A node of a selective parse. Kept in the text flags, so the node does not grow; copies made by edits keep it, and the
// writer refuses such a component unless partial writing is allowed.
internal const val TEXT_PARTIAL: Int = 8

internal const val PARAMETER_NAME_START: Int = 0
internal const val PARAMETER_NAME_END: Int = 1
internal const val PARAMETER_NAME_ID: Int = 2
internal const val PARAMETER_VALUE_COUNT: Int = 3
internal const val PARAMETER_FIRST_VALUE: Int = 4
internal const val PARAMETER_VALUE_FIELDS: Int = 3
internal const val PARAMETER_VALUE_FLAGS: Int = 2

// Unfold first (a fold may split `^` from `n`), then RFC 6868, then UTF-8 with U+FFFD for invalid sequences when the
// line had any.
internal fun decodeText(source: ByteArray, from: Int, to: Int, textFlags: Int, caret: Boolean): String {
    val valid = textFlags and TEXT_INVALID_UTF8 == 0
    val folded = textFlags and TEXT_FOLDED != 0
    if (!folded && !caret) return decodeUtf8(source, from, to, valid)
    val out = ByteArray(to - from)
    var count = 0
    var i = from
    while (i < to) {
        val byte = source[i]
        if (folded && (byte == CR || byte == LF)) {
            i++
            if (byte == CR && i < to && source[i] == LF) i++
            i++
            continue
        }
        out[count++] = byte
        i++
    }
    if (caret) count = decodeCarets(out, count)
    return decodeUtf8(out, 0, count, valid)
}

private fun decodeCarets(bytes: ByteArray, length: Int): Int {
    var count = 0
    var i = 0
    while (i < length) {
        val byte = bytes[i]
        if (byte == CARET && i + 1 < length) {
            val decoded = when (bytes[i + 1]) {
                LOWER_N -> LF
                CARET -> CARET
                APOSTROPHE -> DQUOTE
                else -> null
            }
            if (decoded != null) {
                bytes[count++] = decoded
                i += 2
                continue
            }
        }
        bytes[count++] = byte
        i++
    }
    return count
}

// Names are canonical in ASCII upper case; other characters are kept.
internal fun canonicalName(name: String): String {
    var index = 0
    while (index < name.length && name[index] !in 'a'..'z') index++
    if (index == name.length) return name
    val chars = name.toCharArray()
    while (index < chars.size) {
        val char = chars[index]
        if (char in 'a'..'z') chars[index] = char - CASE_OFFSET
        index++
    }
    return chars.concatToString()
}

internal fun canonicalName(source: ByteArray, from: Int, to: Int, textFlags: Int): String =
    canonicalName(decodeText(source, from, to, textFlags, caret = false))

// Names of hand-built nodes follow `iana-token`/`x-name`, as in `ContentLineWriter`; the message has an index, never
// the text. Known names return the interned string.
internal fun checkedName(name: String, kind: String): String {
    if (name.isEmpty()) throw IllegalArgumentException("Empty $kind")
    for (index in name.indices) {
        val char = name[index]
        if (char != '-' && char !in '0'..'9' && char !in 'A'..'Z' && char !in 'a'..'z') {
            throw IllegalArgumentException("Invalid character in a $kind at index $index")
        }
    }
    val id = KnownNames.find(name)
    return if (id >= 0) KnownNames.name(id) else canonicalName(name)
}

// BEGIN and END are structure, not properties: a property with such a name would break the tree when written.
// Shared by `ICalProperty.of`/`builder` and `InterceptedLine.rename`: an iana-token or x-name, not BEGIN or END.
internal fun checkedPropertyName(name: String): String {
    val canonical = checkedName(name, "property name")
    if (canonical == "BEGIN" || canonical == "END") {
        throw IllegalArgumentException("BEGIN and END are not property names")
    }
    return canonical
}

// `iana-token`/`x-name`; the writer decides with it between a canonical and a raw implicit END and whether an error
// message may name a property.
internal fun isName(name: String): Boolean {
    if (name.isEmpty()) return false
    for (char in name) {
        if (char != '-' && char !in '0'..'9' && char !in 'A'..'Z' && char !in 'a'..'z') return false
    }
    return true
}

// Property values of hand-built nodes: no control characters except HTAB, the same rule as `ContentLineWriter.value`.
internal fun checkedValue(value: String): String {
    val index = controlIndex(value)
    if (index >= 0) throw IllegalArgumentException("Control character in a property value at index $index")
    return value
}

// The index of the first control character other than HTAB, or -1: a value with one is kept by parsing but cannot be
// built.
internal fun controlIndex(value: String): Int {
    for (index in value.indices) {
        val char = value[index]
        if ((char < ' ' && char != '\t') || char == DEL) return index
    }
    return -1
}

// Whether canonical writing accepts [property] as it is: the checks of `ContentLineWriter` that a parsed node can fail
// (names, control characters, a parameter without a value). A line break in a parameter value is written as `^n`
// (RFC 6868). Code that rebuilds a parsed node uses it to keep a result that every writer accepts.
internal fun isWritable(property: ICalProperty): Boolean =
    isName(property.name) && controlIndex(property.value) < 0 && property.parameters.all(::isWritable)

internal fun isWritable(parameter: ICalParameter): Boolean =
    isName(parameter.name) && parameter.values.isNotEmpty() && parameter.values.all(::isWritableParameterValue)

internal fun isWritableParameterValue(value: String): Boolean {
    for (char in value) {
        if ((char < ' ' && char != '\t' && char != '\n' && char != '\r') || char == DEL) return false
    }
    return true
}

// A read-only `List` over the array of a node, no copy.
internal class ArrayView<T>(private val items: Array<out T>) :
    AbstractList<T>(),
    RandomAccess {
    override val size: Int get() = items.size

    override fun get(index: Int): T = items[index]
}

private const val CASE_OFFSET = 'a' - 'A'
private const val DEL = '\u007F'
