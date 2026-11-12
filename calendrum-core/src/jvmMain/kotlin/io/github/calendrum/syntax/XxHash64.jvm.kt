package io.github.calendrum.syntax

internal actual fun xxHash64(bytes: ByteArray, from: Int, to: Int): Long = xxHash64Longs(bytes, from, to)
