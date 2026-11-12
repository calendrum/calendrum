package io.github.calendrum.tooling.platform

import java.io.IOException
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.LinkOption
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

internal actual val FILES_SUPPORTED: Boolean = true

internal actual fun realPathOf(path: String): String? = try {
    Paths.get(path).toRealPath().toString()
} catch (_: IOException) {
    null
} catch (_: InvalidPathException) {
    null
}

// There is no O_NONBLOCK here and opening a FIFO would wait, so the type is checked first. The JVM target exists for
// tests and fuzzing; the native code checks the open descriptor.
public actual fun readRegularFile(path: String, maxBytes: Int): FileRead {
    val file = try {
        Paths.get(path)
    } catch (_: InvalidPathException) {
        return FileRead.Denied
    }
    return try {
        val attributes = Files.readAttributes(file, BasicFileAttributes::class.java, LinkOption.NOFOLLOW_LINKS)
        when {
            !attributes.isRegularFile -> FileRead.Denied

            attributes.size() > maxBytes -> FileRead.TooLarge

            else -> Files.newInputStream(file, LinkOption.NOFOLLOW_LINKS).use { stream ->
                val bytes = stream.readNBytes(maxBytes)
                if (bytes.size == maxBytes && stream.read() >= 0) FileRead.TooLarge else FileRead.Bytes(bytes)
            }
        }
    } catch (_: IOException) {
        FileRead.Denied
    }
}
