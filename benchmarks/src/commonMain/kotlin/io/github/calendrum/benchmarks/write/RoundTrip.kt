package io.github.calendrum.benchmarks.write

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.ByteArraySink
import io.github.calendrum.syntax.ICalWriter
import io.github.calendrum.syntax.WriteOptions

internal val canonicalWriter: ICalWriter = ICal.writer(WriteOptions.builder().byteStable(false).build())

internal fun firstDifference(expected: ByteArray, actual: ByteArray): String? {
    val common = minOf(expected.size, actual.size)
    for (index in 0 until common) {
        if (expected[index] != actual[index]) return "bytes differ at $index"
    }
    return if (expected.size == actual.size) null else "${actual.size} bytes, expected ${expected.size}"
}

// The same corpus with the VTIMEZONE blocks of each calendar moved after the other components, cut out of the input by
// the spans of the tree; the calendar properties are all before the children in our corpora.
internal fun moveTimeZonesLast(bytes: ByteArray): ByteArray {
    val out = ByteArraySink(bytes.size)
    for (calendar in ICal.parse(bytes).components) {
        val span = calendar.span!!
        val children = calendar.components
        val first = children.first().span!!.startOffset.toInt()
        val last = children.last().span!!.endOffset.toInt()
        out.write(bytes, span.startOffset.toInt(), first - span.startOffset.toInt())
        for (zones in listOf(false, true)) {
            for (child in children) {
                if ((child.name == TIME_ZONE) != zones) continue
                val childSpan = child.span!!
                out.write(bytes, childSpan.startOffset.toInt(), (childSpan.endOffset - childSpan.startOffset).toInt())
            }
        }
        out.write(bytes, last, span.endOffset.toInt() - last)
    }
    return out.toByteArray()
}

internal const val TIME_ZONE: String = "VTIMEZONE"
