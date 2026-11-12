@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.readByteArray
import platform.posix.EINTR
import platform.posix._O_BINARY
import platform.posix._exit
import platform.posix._isatty
import platform.posix._setmode
import platform.posix.errno
import platform.posix.read
import platform.posix.write

// The text mode of the C runtime would turn LF into CRLF and read Ctrl-Z as the end of the input.
public actual fun standardInput(): RawSource {
    _setmode(0, _O_BINARY)
    return StandardInput
}

public actual fun standardOutput(): RawSink {
    _setmode(1, _O_BINARY)
    return StandardOutput
}

public actual fun checkedStandardOutput(): RawSink {
    _setmode(1, _O_BINARY)
    return CheckedOutput
}

public actual fun standardError(): RawSink {
    _setmode(2, _O_BINARY)
    return StandardError
}

public actual fun standardOutputIsTerminal(): Boolean = _isatty(1) != 0

private const val READ_SIZE = 65536L

private object StandardInput : RawSource {
    override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
        val chunk = ByteArray(minOf(byteCount, READ_SIZE).toInt())
        while (true) {
            val count = chunk.usePinned { read(0, it.addressOf(0), chunk.size.convert()) }
            when {
                count > 0 -> {
                    sink.write(chunk, 0, count)
                    return count.toLong()
                }

                count < 0 && errno == EINTR -> continue

                else -> return -1
            }
        }
    }

    override fun close() = Unit
}

private object StandardOutput : RawSink {
    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        var offset = 0
        bytes.usePinned {
            while (offset < bytes.size) {
                val count = write(1, it.addressOf(offset), (bytes.size - offset).convert())
                when {
                    count > 0 -> offset += count
                    count < 0 && errno == EINTR -> continue
                    else -> _exit(0)
                }
            }
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}

private object CheckedOutput : RawSink {
    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        var offset = 0
        bytes.usePinned {
            while (offset < bytes.size) {
                val count = write(1, it.addressOf(offset), (bytes.size - offset).convert())
                when {
                    count > 0 -> offset += count
                    count < 0 && errno == EINTR -> continue
                    else -> throw IOException("Cannot write to standard output")
                }
            }
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}

private object StandardError : RawSink {
    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        var offset = 0
        bytes.usePinned {
            while (offset < bytes.size) {
                val count = write(2, it.addressOf(offset), (bytes.size - offset).convert())
                when {
                    count > 0 -> offset += count

                    count < 0 && errno == EINTR -> continue

                    // Nobody is left to tell.
                    else -> return
                }
            }
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}
