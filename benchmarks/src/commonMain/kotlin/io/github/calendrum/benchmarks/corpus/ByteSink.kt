package io.github.calendrum.benchmarks.corpus

internal fun interface ByteSink {
    fun write(bytes: ByteArray, offset: Int, length: Int)
}

internal fun ByteSink.write(bytes: ByteArray) {
    write(bytes, 0, bytes.size)
}

internal fun ByteSink.writeUtf8(text: String) {
    write(text.encodeToByteArray())
}

internal class ByteArraySink(initialCapacity: Int = 1024) : ByteSink {
    private var buffer: ByteArray = ByteArray(initialCapacity)
    var size: Int = 0
        private set

    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        ensureCapacity(size + length)
        bytes.copyInto(buffer, size, offset, offset + length)
        size += length
    }

    fun toByteArray(): ByteArray = buffer.copyOf(size)

    private fun ensureCapacity(minCapacity: Int) {
        if (minCapacity <= buffer.size) return
        var newCapacity = buffer.size + (buffer.size shr 1)
        if (newCapacity < minCapacity) newCapacity = minCapacity
        buffer = buffer.copyOf(newCapacity)
    }
}

internal class ByteCountingSink : ByteSink {
    var count: Long = 0
        private set

    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        count += length
    }
}

internal class TeeSink(private val first: ByteSink, private val second: ByteSink) : ByteSink {
    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        first.write(bytes, offset, length)
        second.write(bytes, offset, length)
    }
}
