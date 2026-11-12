package io.github.calendrum.model

import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.decodeUtf8
import io.github.calendrum.syntax.parameterOf

internal class LineRun(private val reader: ContentReader, context: DecodeContext) : CodecRun(context) {
    private val line = reader.line

    override val value: String = decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8)

    private var parameterArray: Array<ICalParameter>? = null

    override val parameterList: List<ICalParameter>
        get() = if (line.parameterCount == 0) emptyList() else ArrayView(parameters())

    override fun parameter(name: String): ICalParameter? {
        if (line.parameterCount == 0) return null
        val query = canonicalName(name)
        for (parameter in parameters()) {
            if (parameter.name == query) return parameter
        }
        return null
    }

    override fun valueSpan(offset: Int): SourceSpan = spanAt(byteIndexOf(offset))

    override fun parameterSpan(parameter: ICalParameter): SourceSpan {
        val all = parameters()
        for (index in all.indices) {
            if (all[index] !== parameter) continue
            val at = if (line.parameterValueCount(index) == 0) {
                line.parameterNameEnd(index)
            } else {
                line.parameterValueStart(index, 0)
            }
            return spanAt(at)
        }
        return spanAt(line.nameStart)
    }

    private fun parameters(): Array<ICalParameter> {
        val cached = parameterArray
        if (cached != null) return cached
        val array = Array(line.parameterCount) { index ->
            val id = line.parameterNameId(index)
            val name = if (id >= 0) KnownNames.name(id) else canonicalName(line.parameterName(index))
            val count = line.parameterValueCount(index)
            parameterOf(name, Array(count) { line.parameterValue(index, it) })
        }
        parameterArray = array
        return array
    }

    private fun byteIndexOf(charIndex: Int): Int {
        val bytes = line.bytes
        var chars = 0
        var index = line.valueStart
        val end = line.valueEnd
        while (index < end && chars < charIndex) {
            val lead = bytes[index].toInt() and BYTE_MASK
            val length = when {
                lead < UTF8_TWO -> 1
                lead < UTF8_THREE -> 2
                lead < UTF8_FOUR -> 3
                else -> 4
            }
            chars += if (length == 4) 2 else 1
            index = minOf(end, index + length)
        }
        return index
    }

    private fun spanAt(index: Int): SourceSpan {
        val offset = reader.origin + line.offsetAt(index)
        return SourceSpan(offset, offset, line.lineAt(index), line.columnAt(index))
    }

    private companion object {
        const val BYTE_MASK = 0xFF
        const val UTF8_TWO = 0xC0
        const val UTF8_THREE = 0xE0
        const val UTF8_FOUR = 0xF0
    }
}
