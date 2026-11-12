// A minimal JSON reader, writer (canonical and compact) and RFC 8259 string escaper, used only by the tests of this
// module: the three file formats of the suite are flat enough that `kotlinx-serialization-json` would add a dependency
// and a compiler plugin for little benefit here.

package io.github.calendrum.conformance

internal sealed class Json

internal data class JsonObject(val members: List<Pair<String, Json>>) : Json() {
    operator fun get(key: String): Json? = members.firstOrNull { it.first == key }?.second

    val keys: List<String> get() = members.map { it.first }
}

internal data class JsonArray(val items: List<Json>) : Json()

internal data class JsonString(val value: String) : Json()

internal data class JsonInteger(val value: Long) : Json()

// A number with a fraction or exponent kept as its text: the suite compares JSON structurally and never rounds through
// Double.
internal data class JsonDecimal(val text: String) : Json()

internal data class JsonBoolean(val value: Boolean) : Json()

internal data object JsonNull : Json()

internal fun jsonObject(vararg members: Pair<String, Json>): JsonObject = JsonObject(members.toList())

internal class JsonSyntaxException(message: String) : Exception(message)

internal fun parseJson(text: String): Json = JsonParser(text).document()

internal fun Json.toCanonicalJson(): String {
    val out = StringBuilder()
    writeCanonical(out, this, 0)
    return out.append('\n').toString()
}

internal fun Json.toCompactJson(): String {
    val out = StringBuilder()
    writeCompact(out, this)
    return out.toString()
}

private fun writeCanonical(out: StringBuilder, value: Json, indent: Int) {
    when (value) {
        is JsonObject -> {
            if (value.members.isEmpty()) {
                out.append("{}")
                return
            }
            out.append("{\n")
            value.members.forEachIndexed { index, (key, member) ->
                repeat(indent + INDENT) { out.append(' ') }
                writeString(out, key)
                out.append(": ")
                writeCanonical(out, member, indent + INDENT)
                if (index < value.members.lastIndex) out.append(',')
                out.append('\n')
            }
            repeat(indent) { out.append(' ') }
            out.append('}')
        }

        is JsonArray -> {
            if (value.items.isEmpty()) {
                out.append("[]")
                return
            }
            out.append("[\n")
            value.items.forEachIndexed { index, item ->
                repeat(indent + INDENT) { out.append(' ') }
                writeCanonical(out, item, indent + INDENT)
                if (index < value.items.lastIndex) out.append(',')
                out.append('\n')
            }
            repeat(indent) { out.append(' ') }
            out.append(']')
        }

        else -> writeCompact(out, value)
    }
}

private fun writeCompact(out: StringBuilder, value: Json) {
    when (value) {
        is JsonObject -> {
            out.append('{')
            value.members.forEachIndexed { index, (key, member) ->
                if (index > 0) out.append(", ")
                writeString(out, key)
                out.append(": ")
                writeCompact(out, member)
            }
            out.append('}')
        }

        is JsonArray -> {
            out.append('[')
            value.items.forEachIndexed { index, item ->
                if (index > 0) out.append(", ")
                writeCompact(out, item)
            }
            out.append(']')
        }

        is JsonString -> writeString(out, value.value)

        is JsonInteger -> out.append(value.value)

        is JsonDecimal -> out.append(value.text)

        is JsonBoolean -> out.append(value.value)

        JsonNull -> out.append("null")
    }
}

private fun writeString(out: StringBuilder, value: String) {
    out.append('"')
    var index = 0
    while (index < value.length) {
        val char = value[index]
        when {
            char == '"' -> out.append("\\\"")

            char == '\\' -> out.append("\\\\")

            char == '\b' -> out.append("\\b")

            char == '\u000C' -> out.append("\\f")

            char == '\n' -> out.append("\\n")

            char == '\r' -> out.append("\\r")

            char == '\t' -> out.append("\\t")

            char < ' ' -> out.append("\\u").append(hex(char))

            char.isHighSurrogate() && index + 1 < value.length && value[index + 1].isLowSurrogate() -> {
                out.append(char).append(value[index + 1])
                index++
            }

            char.isSurrogate() -> out.append("\\u").append(hex(char))

            else -> out.append(char)
        }
        index++
    }
    out.append('"')
}

private fun hex(char: Char): String = char.code.toString(HEX_RADIX).padStart(UNICODE_DIGITS, '0')

private class JsonParser(private val text: String) {
    private var at = 0

    fun document(): Json {
        skipWhitespace()
        val value = value(0)
        skipWhitespace()
        if (at != text.length) fail("unexpected content after the value")
        return value
    }

    private fun value(depth: Int): Json {
        if (depth > MAX_DEPTH) fail("nesting is deeper than $MAX_DEPTH")
        if (at >= text.length) fail("unexpected end of input")
        return when (val char = text[at]) {
            '{' -> obj(depth)
            '[' -> array(depth)
            '"' -> JsonString(string())
            't' -> literal("true", JsonBoolean(true))
            'f' -> literal("false", JsonBoolean(false))
            'n' -> literal("null", JsonNull)
            else -> if (char == '-' || char in '0'..'9') integer() else fail("unexpected character '$char'")
        }
    }

    private fun obj(depth: Int): JsonObject {
        at++
        val members = ArrayList<Pair<String, Json>>()
        skipWhitespace()
        if (peek() == '}') {
            at++
            return JsonObject(members)
        }
        while (true) {
            skipWhitespace()
            if (peek() != '"') fail("expected a key")
            val keyAt = at
            val key = string()
            if (members.any { it.first == key }) {
                at = keyAt
                fail("duplicate key \"$key\"")
            }
            skipWhitespace()
            expect(':')
            skipWhitespace()
            members += key to value(depth + 1)
            skipWhitespace()
            when (peek()) {
                ',' -> at++

                '}' -> {
                    at++
                    return JsonObject(members)
                }

                else -> fail("expected ',' or '}'")
            }
        }
    }

    private fun array(depth: Int): JsonArray {
        at++
        val items = ArrayList<Json>()
        skipWhitespace()
        if (peek() == ']') {
            at++
            return JsonArray(items)
        }
        while (true) {
            skipWhitespace()
            items += value(depth + 1)
            skipWhitespace()
            when (peek()) {
                ',' -> at++

                ']' -> {
                    at++
                    return JsonArray(items)
                }

                else -> fail("expected ',' or ']'")
            }
        }
    }

    private fun string(): String {
        at++
        val out = StringBuilder()
        while (true) {
            if (at >= text.length) fail("unterminated string")
            val char = text[at]
            when {
                char == '"' -> {
                    at++
                    return out.toString()
                }

                char == '\\' -> {
                    at++
                    if (at >= text.length) fail("unterminated escape")
                    when (text[at]) {
                        '"' -> out.append('"')

                        '\\' -> out.append('\\')

                        '/' -> out.append('/')

                        'b' -> out.append('\b')

                        'f' -> out.append('\u000C')

                        'n' -> out.append('\n')

                        'r' -> out.append('\r')

                        't' -> out.append('\t')

                        'u' -> {
                            if (at + UNICODE_DIGITS >= text.length) fail("truncated \\u escape")
                            val digits = text.substring(at + 1, at + 1 + UNICODE_DIGITS)
                            val code = digits.toIntOrNull(HEX_RADIX) ?: fail("invalid \\u escape")
                            if (digits.any { it == '+' || it == '-' }) fail("invalid \\u escape")
                            out.append(code.toChar())
                            at += UNICODE_DIGITS
                        }

                        else -> fail("invalid escape")
                    }
                    at++
                }

                char < ' ' -> fail("unescaped control character in a string")

                else -> {
                    out.append(char)
                    at++
                }
            }
        }
    }

    private fun integer(): Json {
        val start = at
        if (peek() == '-') at++
        if (peek() == '0') {
            at++
        } else if (peek() in '1'..'9') {
            while (peek() in '0'..'9') at++
        } else {
            fail("invalid number")
        }
        if (peek() == 'e' || peek() == 'E') fail("exponents are not supported")
        if (peek() == '.') {
            at++
            if (peek() !in '0'..'9') fail("invalid number")
            while (peek() in '0'..'9') at++
            if (peek() == 'e' || peek() == 'E') fail("exponents are not supported")
            return JsonDecimal(text.substring(start, at))
        }
        val value = text.substring(start, at).toLongOrNull() ?: fail("integer out of range")
        return JsonInteger(value)
    }

    private fun literal(word: String, value: Json): Json {
        if (!text.startsWith(word, at)) fail("invalid literal")
        at += word.length
        return value
    }

    private fun expect(char: Char) {
        if (peek() != char) fail("expected '$char'")
        at++
    }

    private fun peek(): Char = if (at < text.length) text[at] else END

    private fun skipWhitespace() {
        while (at < text.length && (text[at] == ' ' || text[at] == '\n' || text[at] == '\r' || text[at] == '\t')) at++
    }

    private fun fail(message: String): Nothing {
        var line = 1
        var column = 1
        for (index in 0 until minOf(at, text.length)) {
            if (text[index] == '\n') {
                line++
                column = 1
            } else {
                column++
            }
        }
        throw JsonSyntaxException("$message at $line:$column")
    }
}

private const val INDENT = 2
private const val HEX_RADIX = 16
private const val UNICODE_DIGITS = 4
private const val MAX_DEPTH = 512
private const val END = '\u0000'
