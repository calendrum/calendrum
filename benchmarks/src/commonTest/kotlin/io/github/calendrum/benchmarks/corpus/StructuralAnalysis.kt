package io.github.calendrum.benchmarks.corpus

internal class FoldingViolation(val physicalLineIndex: Int, val octets: Int)

internal class FoldingReport(
    val physicalLineCount: Int,
    val violations: List<FoldingViolation>,
    val hasBareCrOrLf: Boolean,
)

// Deliberately byte-level, not `String`-based: it has to flag a bare CR or LF and measure octets, and a decoded
// `String` would already have normalized or miscounted both.
internal fun analyzeFolding(bytes: ByteArray): FoldingReport {
    val violations = mutableListOf<FoldingViolation>()
    var hasBareCrOrLf = false
    var lineStart = 0
    var physicalLineIndex = 0
    var i = 0
    while (i < bytes.size) {
        val byte = bytes[i]
        if (byte == CR) {
            if (i + 1 >= bytes.size || bytes[i + 1] != LF) {
                hasBareCrOrLf = true
            } else {
                val length = i - lineStart
                if (length > FOLD_LIMIT_OCTETS) violations += FoldingViolation(physicalLineIndex, length)
                physicalLineIndex++
                i++
                lineStart = i + 1
            }
        } else if (byte == LF) {
            hasBareCrOrLf = true
        }
        i++
    }
    if (lineStart < bytes.size) hasBareCrOrLf = true
    return FoldingReport(physicalLineIndex, violations, hasBareCrOrLf)
}

internal fun unfoldLines(bytes: ByteArray): List<String> {
    val logical = mutableListOf<ByteArrayBuilder>()
    var i = 0
    while (i < bytes.size) {
        val lineStart = i
        while (i < bytes.size && bytes[i] != CR) i++
        val lineEnd = i
        i += 2
        if (lineEnd > lineStart && bytes[lineStart] == SPACE) {
            logical.last().append(bytes, lineStart + 1, lineEnd)
        } else {
            logical += ByteArrayBuilder().also { it.append(bytes, lineStart, lineEnd) }
        }
    }
    return logical.map { it.toByteArray().decodeToString() }
}

// A 32-byte initial capacity, not the 1024 of a fresh `ByteArraySink`: `unfoldLines` allocates one of these per
// physical line, and `huge` has hundreds of thousands of them. At 1024 bytes each that alone was enough to blow a 2 GB
// test heap.
internal class ByteArrayBuilder {
    private val sink = ByteArraySink(initialCapacity = 32)

    fun append(bytes: ByteArray, from: Int, to: Int) {
        sink.write(bytes, from, to - from)
    }

    fun toByteArray(): ByteArray = sink.toByteArray()
}

internal fun checkBalance(logicalLines: List<String>): Map<String, Int> {
    val counts = mutableMapOf<String, Int>()
    for (line in logicalLines) {
        when {
            line.startsWith("BEGIN:") -> {
                val name = line.removePrefix("BEGIN:")
                counts[name] = (counts[name] ?: 0) + 1
            }

            line.startsWith("END:") -> {
                val name = line.removePrefix("END:")
                counts[name] = (counts[name] ?: 0) - 1
            }
        }
    }
    return counts
}

private const val CR: Byte = 0x0D
private const val LF: Byte = 0x0A
private const val SPACE: Byte = 0x20
