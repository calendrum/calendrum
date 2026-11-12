package io.github.calendrum.tooling.cli

// Text from a calendar can steer a terminal: an escape sequence changes the title of the window, moves the cursor over
// lines that were already printed, or fakes the output of the program. Whatever reaches a person in a human-readable form
// goes through here first.

/**
 * Replaces what could steer a terminal with a visible escape.
 *
 * Control characters of C0 and C1 and DEL become `\xHH`, which covers `ESC` and `CSI` (`U+009B`). The line and paragraph
 * separators and the marks that change the direction of text, which can make a line read differently from what it holds,
 * and a half of a surrogate pair, become `\uHHHH`. A whole surrogate pair is a character and stays.
 *
 * @param keepLineBreaks leaves CR, LF and HT as they are, for a calendar that is printed as itself.
 */
internal fun sanitize(text: String, keepLineBreaks: Boolean = false): String {
    var out: StringBuilder? = null
    var index = 0
    while (index < text.length) {
        val char = text[index]
        val pair = char.isHighSurrogate() && index + 1 < text.length && text[index + 1].isLowSurrogate()
        val escape = when {
            pair -> null
            keepLineBreaks && (char == '\n' || char == '\r' || char == '\t') -> null
            char.code < 0x20 || char.code in 0x7F..0x9F -> "\\x" + hex(char.code, 2)
            isSteering(char) || char.isSurrogate() -> "\\u" + hex(char.code, 4)
            else -> null
        }
        val width = if (pair) 2 else 1
        if (escape != null && out == null) out = StringBuilder(text.length + 16).append(text, 0, index)
        if (out != null) {
            if (escape != null) out.append(escape) else out.append(text, index, index + width)
        }
        index += width
    }
    return out?.toString() ?: text
}

/**
 * The same JSON with what could steer a terminal written as `\uHHHH`: DEL, C1, the separators and the direction marks.
 * Inside a string, which is the only place such a character can be, this is the same value, so a program that reads the JSON
 * does not see a difference. The controls of C0 are escaped by the writer of the operations already.
 */
internal fun escapeJsonForTerminal(json: String): String {
    var out: StringBuilder? = null
    for (index in json.indices) {
        val char = json[index]
        val steering = char.code in 0x7F..0x9F || isSteering(char)
        if (steering && out == null) out = StringBuilder(json.length + 16).append(json, 0, index)
        if (out != null) {
            if (steering) out.append("\\u").append(hex(char.code, 4).lowercase()) else out.append(char)
        }
    }
    return out?.toString() ?: json
}

private fun isSteering(char: Char): Boolean = char == '\u2028' || char == '\u2029' || char == '\u061C' ||
    char == '\u200E' || char == '\u200F' || char in '\u202A'..'\u202E' || char in '\u2066'..'\u2069'

private fun hex(value: Int, width: Int): String = value.toString(16).uppercase().padStart(width, '0')
