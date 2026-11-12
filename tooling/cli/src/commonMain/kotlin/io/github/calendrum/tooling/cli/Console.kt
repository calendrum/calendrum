package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.platform.FileWrite
import io.github.calendrum.tooling.platform.UserFileRead
import io.github.calendrum.tooling.platform.checkedStandardOutput
import io.github.calendrum.tooling.platform.readUserFile
import io.github.calendrum.tooling.platform.standardError
import io.github.calendrum.tooling.platform.standardInput
import io.github.calendrum.tooling.platform.standardOutputIsTerminal
import io.github.calendrum.tooling.platform.writeFileAtomically
import kotlinx.io.Buffer
import kotlinx.io.IOException
import kotlinx.io.readByteArray

/** Everything the command touches outside the process, so that a test can stand in for it. */
internal interface Console {
    /** Whether what goes to the standard output is shown on a terminal. */
    val outputIsTerminal: Boolean

    fun readFile(path: String, maxBytes: Int): UserFileRead

    fun readStandardInput(maxBytes: Int): UserFileRead

    /** @throws IOException if the output cannot be written, for instance because the reader has gone. */
    fun writeOutput(bytes: ByteArray)

    fun writeError(text: String)

    fun writeFile(path: String, bytes: ByteArray, replace: Boolean): FileWrite
}

internal object SystemConsole : Console {
    override val outputIsTerminal: Boolean get() = standardOutputIsTerminal()

    override fun readFile(path: String, maxBytes: Int): UserFileRead = readUserFile(path, maxBytes)

    override fun readStandardInput(maxBytes: Int): UserFileRead {
        val source = standardInput()
        val buffer = Buffer()
        var total = 0L
        while (true) {
            val count = source.readAtMostTo(buffer, CHUNK)
            if (count < 0) break
            total += count
            // One byte over the limit is enough to know: the rest is not read.
            if (total > maxBytes) return UserFileRead.TooLarge
        }
        return UserFileRead.Bytes(buffer.readByteArray())
    }

    override fun writeOutput(bytes: ByteArray) {
        val sink = checkedStandardOutput()
        val buffer = Buffer()
        buffer.write(bytes)
        sink.write(buffer, buffer.size)
    }

    override fun writeError(text: String) {
        val sink = standardError()
        val buffer = Buffer()
        buffer.write(text.encodeToByteArray())
        sink.write(buffer, buffer.size)
    }

    override fun writeFile(path: String, bytes: ByteArray, replace: Boolean): FileWrite =
        writeFileAtomically(path, bytes, replace)

    private const val CHUNK = 65536L
}
