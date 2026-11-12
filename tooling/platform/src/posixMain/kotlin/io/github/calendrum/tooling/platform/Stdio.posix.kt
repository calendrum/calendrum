@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.tooling.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.readByteArray
import platform.posix.EAGAIN
import platform.posix.EINTR
import platform.posix.POLLIN
import platform.posix.POLLOUT
import platform.posix.SIGPIPE
import platform.posix.SIG_IGN
import platform.posix._exit
import platform.posix.errno
import platform.posix.isatty
import platform.posix.poll
import platform.posix.pollfd
import platform.posix.read
import platform.posix.signal
import platform.posix.write

public actual fun standardInput(): RawSource = FdSource(0)

public actual fun standardOutput(): RawSink {
    ignoreBrokenPipes()
    return FdSink(1) { _exit(0) }
}

// Without this the first write after the reader has gone kills the process with a signal, and the code 141.
internal fun ignoreBrokenPipes() {
    signal(SIGPIPE, SIG_IGN)
}

public actual fun checkedStandardOutput(): RawSink {
    ignoreBrokenPipes()
    return checkedSink(1)
}

// The reason is read in the callback, right after the write that failed.
internal fun checkedSink(fd: Int): RawSink = FdSink(fd) { throw IOException(reasonOf(errno)) }

public actual fun standardError(): RawSink {
    ignoreBrokenPipes()
    return FdSink(2) {}
}

public actual fun standardOutputIsTerminal(): Boolean = isatty(1) != 0

private const val READ_SIZE = 65536L

internal class FdSource(private val fd: Int) : RawSource {
    override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
        val chunk = ByteArray(minOf(byteCount, READ_SIZE).toInt())
        while (true) {
            val count = chunk.usePinned { read(fd, it.addressOf(0), chunk.size.convert()) }.toInt()
            when {
                count > 0 -> {
                    sink.write(chunk, 0, count)
                    return count.toLong()
                }

                count == 0 -> return -1

                errno == EINTR -> continue

                // A parent can hand over a pipe that is non-blocking.
                errno == EAGAIN -> waitFor(fd, POLLIN)

                else -> return -1
            }
        }
    }

    override fun close() = Unit
}

internal class FdSink(private val fd: Int, private val onClosed: () -> Unit) : RawSink {
    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        var offset = 0
        bytes.usePinned {
            while (offset < bytes.size) {
                val count = write(fd, it.addressOf(offset), (bytes.size - offset).convert()).toInt()
                when {
                    count > 0 -> offset += count

                    count < 0 && errno == EINTR -> continue

                    count < 0 && errno == EAGAIN -> waitFor(fd, POLLOUT)

                    else -> {
                        onClosed()
                        return
                    }
                }
            }
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}

private fun waitFor(fd: Int, events: Int) {
    memScoped {
        val request = alloc<pollfd>()
        request.fd = fd
        request.events = events.convert()
        // An interrupted wait or an error shows up in the read or the write that comes next.
        poll(request.ptr, 1u.convert(), -1)
    }
}
