package io.github.calendrum

/**
 * A position in the input: byte offsets from its start, a line and a column.
 *
 * Offsets count bytes of the input, from 0; for input that was transcoded they count bytes of the decoded UTF-8 text.
 * The line and the column both start at 1, and the column is counted in bytes from the start of the physical line.
 * Nodes built by hand have no input: their span is all zeros, and a value of a hand-built property has a column of 0.
 */
public class SourceSpan internal constructor(
    /** The offset of the first byte of the span. */
    public val startOffset: Long,
    /** The offset just after the last byte of the span; equal to [startOffset] for a point. */
    public val endOffset: Long,
    /** The 1-based number of the physical line where the span starts, or 0 when there is no input. */
    public val line: Int,
    /** The 1-based column in bytes on that line, or 0 when there is no input. */
    public val column: Int,
) {
    /** Returns `true` when [other] is a span with the same offsets, line and column. */
    override fun equals(other: Any?): Boolean = other is SourceSpan &&
        startOffset == other.startOffset &&
        endOffset == other.endOffset &&
        line == other.line &&
        column == other.column

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int {
        var result = startOffset.hashCode()
        result = 31 * result + endOffset.hashCode()
        result = 31 * result + line
        return 31 * result + column
    }

    /** Returns the line, the column and the offsets, such as `3:7 (offsets 12..12)`. */
    override fun toString(): String = "$line:$column (offsets $startOffset..$endOffset)"
}
