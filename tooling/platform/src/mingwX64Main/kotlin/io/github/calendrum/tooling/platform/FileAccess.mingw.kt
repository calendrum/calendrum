package io.github.calendrum.tooling.platform

// The checks of a path cannot be run on the development host and an unchecked boundary is not released: nothing is read
// until a CI on Windows can test them.
internal actual val FILES_SUPPORTED: Boolean = false

internal actual fun realPathOf(path: String): String? = null

public actual fun readRegularFile(path: String, maxBytes: Int): FileRead = FileRead.Denied
