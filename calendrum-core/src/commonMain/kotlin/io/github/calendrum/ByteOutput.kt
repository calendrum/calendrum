package io.github.calendrum

// A minimal blocking byte sink with the contract of `OutputStream.write(buffer, offset, length)`: it writes all bytes
// or throws.
/**
 * A push sink of bytes, the platform-independent counterpart of an output stream.
 *
 * The writer never flushes or closes an output; both are the caller's job.
 */
public fun interface ByteOutput {
    /** Writes [length] bytes of [buffer] starting at [offset]. */
    public fun write(buffer: ByteArray, offset: Int, length: Int)
}
