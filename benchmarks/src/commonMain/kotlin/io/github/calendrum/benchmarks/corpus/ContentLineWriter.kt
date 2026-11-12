package io.github.calendrum.benchmarks.corpus

// RFC 5545 §3.1: a physical line is at most 75 octets, CRLF excluded; a continuation line starts with one space that
// itself counts toward that line's 75, hence `FOLD_LIMIT_OCTETS - 1` for every segment after the first.
internal const val FOLD_LIMIT_OCTETS: Int = 75

internal class ContentLineWriter(private val sink: ByteSink) {
    fun property(name: String, vararg params: Pair<String, String>, value: String) {
        val line = StringBuilder(name)
        for ((key, paramValue) in params) {
            line.append(';').append(key).append('=').append(quoteIfNeeded(paramValue))
        }
        line.append(':').append(value)
        writeFolded(line.toString())
    }

    fun raw(line: String) {
        writeFolded(line)
    }

    private fun writeFolded(line: String) {
        val bytes = line.encodeToByteArray()
        var start = 0
        var limit = FOLD_LIMIT_OCTETS
        if (bytes.isEmpty()) {
            sink.writeUtf8(CRLF)
            return
        }
        while (start < bytes.size) {
            var end = minOf(start + limit, bytes.size)
            end = backOffFromContinuationByte(bytes, start, end)
            sink.write(bytes, start, end - start)
            start = end
            if (start < bytes.size) {
                sink.writeUtf8(CRLF_FOLD)
                limit = FOLD_LIMIT_OCTETS - 1
            }
        }
        sink.writeUtf8(CRLF)
    }

    // A UTF-8 continuation byte has the top two bits `10`; backing off until the cursor no longer sits on one
    // guarantees the fold boundary falls between whole characters, never inside a multi-byte sequence.
    private fun backOffFromContinuationByte(bytes: ByteArray, start: Int, end: Int): Int {
        var cursor = end
        while (cursor > start && cursor < bytes.size && (bytes[cursor].toInt() and 0xC0) == 0x80) {
            cursor--
        }
        return cursor
    }

    private companion object {
        const val CRLF = "\r\n"
        const val CRLF_FOLD = "\r\n "
    }
}

// RFC 5545 §3.3.11 TEXT escaping: backslash, semicolon, comma and literal newline. Colon is not escaped in TEXT.
internal fun escapeText(value: String): String {
    val builder = StringBuilder(value.length)
    for (ch in value) {
        when (ch) {
            '\\' -> builder.append("\\\\")
            ';' -> builder.append("\\;")
            ',' -> builder.append("\\,")
            '\n' -> builder.append("\\n")
            else -> builder.append(ch)
        }
    }
    return builder.toString()
}

// RFC 5545 §3.2 param-value: a value containing `:`, `;` or `,` must be quoted; the generator never emits a DQUOTE
// inside a param value, so a plain wrap is enough.
internal fun quoteIfNeeded(paramValue: String): String {
    val needsQuoting = paramValue.any { it == ':' || it == ';' || it == ',' }
    return if (needsQuoting) "\"$paramValue\"" else paramValue
}
