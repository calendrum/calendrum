package io.github.calendrum.benchmarks.sanity

private const val TAB: Byte = 0x09
private const val LF: Byte = 0x0A
private const val CR: Byte = 0x0D
private const val SPACE: Byte = 0x20

// Counts content lines after unfolding (RFC 5545 section 3.1): a line that starts with a space or a tab continues the
// previous one; empty lines are not counted.
internal fun countLogicalLines(bytes: ByteArray): Int {
    var count = 0
    var atLineStart = true
    for (byte in bytes) {
        if (atLineStart && byte != SPACE && byte != TAB && byte != CR && byte != LF) {
            count++
        }
        atLineStart = byte == LF
    }
    return count
}
