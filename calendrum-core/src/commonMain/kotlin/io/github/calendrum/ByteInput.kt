@file:JvmName("ByteInputs")

package io.github.calendrum

import kotlin.jvm.JvmName

// A minimal blocking byte source with the contract of `InputStream.read(buffer, offset, length)`: it returns the number
// of bytes read, at least one when `length > 0`, or -1 at the end. The lexer treats 0 as "read again" and a count above
// `length` or below -1 as a broken input (`IllegalStateException`). Kept in the core instead of kotlinx-io or Okio,
// which get adapter modules.
/**
 * A pull source of bytes, the platform-independent counterpart of an input stream.
 *
 * Adapters for `java.io.InputStream`, kotlinx-io `Source` and Okio `BufferedSource` live in the optional modules. The
 * parser never closes an input; closing is the caller's job.
 */
public fun interface ByteInput {
    /**
     * Reads up to [length] bytes into [buffer] starting at [offset].
     *
     * @return the number of bytes read, at least 1, or -1 at the end of the input.
     */
    public fun read(buffer: ByteArray, offset: Int, length: Int): Int
}

/**
 * Wraps this array as a [ByteInput] that yields its bytes once, from the start.
 *
 * The array is not copied; do not change it while the input is being read.
 */
public fun ByteArray.asByteInput(): ByteInput = ArrayByteInput(this)

// `ByteArray.asByteInput()`: reads the array from the start, in as many bytes as each call asks for.
internal class ArrayByteInput(private val bytes: ByteArray) : ByteInput {
    private var position = 0

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        require(offset >= 0 && length >= 0 && offset <= buffer.size - length) {
            "Range $offset+$length is out of bounds for size ${buffer.size}"
        }
        val available = bytes.size - position
        if (available <= 0) return -1
        val count = minOf(length, available)
        bytes.copyInto(buffer, offset, position, position + count)
        position += count
        return count
    }

    override fun toString(): String = "ByteInput(${bytes.size} bytes, position $position)"
}
