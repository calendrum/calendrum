package io.github.calendrum.tooling.platform

import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.asSource
import kotlinx.io.readByteArray
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException

public actual fun standardInput(): RawSource = System.`in`.asSource()

public actual fun standardOutput(): RawSink = StandardOutput

public actual fun checkedStandardOutput(): RawSink = CheckedOutput

public actual fun standardError(): RawSink = StandardError

// JDK 22 and later also answer for a console that is not a terminal, which only makes the test host careful.
public actual fun standardOutputIsTerminal(): Boolean = System.console() != null

private object CheckedOutput : RawSink {
    private val stream = FileOutputStream(FileDescriptor.out)

    override fun write(source: Buffer, byteCount: Long) = stream.write(source.readByteArray(byteCount.toInt()))

    override fun flush() = Unit

    override fun close() = Unit
}

private object StandardError : RawSink {
    private val stream = FileOutputStream(FileDescriptor.err)

    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        try {
            stream.write(bytes)
        } catch (_: IOException) {
            // Nobody is left to tell.
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}

private object StandardOutput : RawSink {
    private val stream = FileOutputStream(FileDescriptor.out)

    override fun write(source: Buffer, byteCount: Long) {
        val bytes = source.readByteArray(byteCount.toInt())
        try {
            stream.write(bytes)
        } catch (_: IOException) {
            Runtime.getRuntime().halt(0)
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}
