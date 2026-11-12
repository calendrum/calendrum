package io.github.calendrum.benchmarks.corpus

import java.io.BufferedOutputStream
import java.io.OutputStream
import java.security.MessageDigest

internal class OutputStreamByteSink(private val output: OutputStream) : ByteSink {
    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        output.write(bytes, offset, length)
    }
}

internal class Sha256Sink : ByteSink {
    private val digest: MessageDigest = MessageDigest.getInstance("SHA-256")

    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        digest.update(bytes, offset, length)
    }

    fun hexDigest(): String = digest.digest().joinToString("") { byte -> "%02x".format(byte) }
}

// Streams straight to disk through a tee of the file, a byte counter and a SHA-256 digest, so writing `huge` never
// holds more than a small buffer in memory.
internal fun writeCorpusToFile(name: String, file: java.io.File): Pair<Long, String> {
    file.parentFile?.mkdirs()
    val counting = ByteCountingSink()
    val sha256 = Sha256Sink()
    BufferedOutputStream(file.outputStream()).use { stream ->
        val fileSink = OutputStreamByteSink(stream)
        val sink = TeeSink(TeeSink(fileSink, counting), sha256)
        Corpus.write(name, sink)
    }
    return counting.count to sha256.hexDigest()
}
