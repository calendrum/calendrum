package io.github.calendrum.syntax

internal actual fun decodeValidUtf8(bytes: ByteArray, start: Int, end: Int): String = bytes.decodeToString(start, end)
