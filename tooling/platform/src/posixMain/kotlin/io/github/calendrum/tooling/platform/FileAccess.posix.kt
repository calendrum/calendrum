@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
import platform.posix.EINTR
import platform.posix.O_CLOEXEC
import platform.posix.O_NOFOLLOW
import platform.posix.O_NONBLOCK
import platform.posix.O_RDONLY
import platform.posix.S_IFMT
import platform.posix.S_IFREG
import platform.posix.close
import platform.posix.errno
import platform.posix.free
import platform.posix.fstat
import platform.posix.open
import platform.posix.read
import platform.posix.realpath
import platform.posix.stat
import platform.posix.strlen

internal actual val FILES_SUPPORTED: Boolean = true

internal actual fun realPathOf(path: String): String? {
    val resolved = realpath(path, null) ?: return null
    return try {
        // A lossy decoding would make two different names equal, and the comparison with a root depends on them.
        resolved.readBytes(strlen(resolved).toInt()).decodeToString(throwOnInvalidSequence = true)
    } catch (_: CharacterCodingException) {
        null
    } finally {
        free(resolved)
    }
}

public actual fun readRegularFile(path: String, maxBytes: Int): FileRead {
    val fd = openForReading(path)
    if (fd < 0) return FileRead.Denied
    try {
        val size = regularFileSize(fd) ?: return FileRead.Denied
        if (size > maxBytes) return FileRead.TooLarge
        return readAll(fd, size.toInt(), maxBytes)
    } finally {
        close(fd)
    }
}

// O_NONBLOCK keeps open() from waiting for the writer of a FIFO, and on a regular file it changes nothing.
private fun openForReading(path: String): Int {
    while (true) {
        val fd = open(path, O_RDONLY or O_NOFOLLOW or O_NONBLOCK or O_CLOEXEC)
        if (fd >= 0 || errno != EINTR) return fd
    }
}

private fun regularFileSize(fd: Int): Long? = memScoped {
    val status = alloc<stat>()
    if (fstat(fd, status.ptr) != 0) return null
    if ((status.st_mode.toInt() and S_IFMT) != S_IFREG) return null
    status.st_size
}

// The file can change after fstat: what counts is what read() gives, and that stops at the limit.
internal fun readAll(fd: Int, expected: Int, maxBytes: Int): FileRead {
    var bytes = ByteArray(maxOf(expected, MIN_BUFFER).coerceAtMost(maxBytes))
    var filled = 0
    while (true) {
        if (filled == bytes.size) {
            if (filled >= maxBytes) {
                return if (readSome(fd, ByteArray(1), 0, 1) > 0) FileRead.TooLarge else FileRead.Bytes(bytes)
            }
            bytes = bytes.copyOf(minOf(maxBytes.toLong(), bytes.size * 2L).toInt())
        }
        val count = readSome(fd, bytes, filled, bytes.size - filled)
        if (count < 0) return FileRead.Denied
        if (count == 0) return FileRead.Bytes(bytes.copyOf(filled))
        filled += count
    }
}

private fun readSome(fd: Int, bytes: ByteArray, offset: Int, size: Int): Int {
    while (true) {
        val count = bytes.usePinned { read(fd, it.addressOf(offset), size.convert()) }.toInt()
        if (count >= 0 || errno != EINTR) return count
    }
}

private const val MIN_BUFFER = 4096
