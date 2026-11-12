package io.github.calendrum.syntax

// Strings of wasmJs are JS strings: an ASCII slice is widened into a CharArray and made a string by the js-string
// builtin, without the general UTF-8 decoder of the stdlib (it was a fifth of `parse.treeReadAll`).
internal actual fun decodeValidUtf8(bytes: ByteArray, start: Int, end: Int): String {
    var index = start
    while (index < end) {
        if (bytes[index] < 0) return bytes.decodeToString(start, end)
        index++
    }
    val chars = CharArray(end - start)
    for (offset in chars.indices) chars[offset] = bytes[start + offset].toInt().toChar()
    return chars.concatToString()
}
