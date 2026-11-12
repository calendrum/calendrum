package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.syntax.ParseResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.github.calendrum.kotlinxio.components as kotlinxComponents
import io.github.calendrum.kotlinxio.parse as kotlinxParse
import io.github.calendrum.kotlinxio.write as kotlinxWrite
import io.github.calendrum.okio.components as okioComponents
import io.github.calendrum.okio.parse as okioParse
import io.github.calendrum.okio.write as okioWrite
import kotlinx.io.Buffer as KotlinxBuffer
import kotlinx.io.readByteArray as kotlinxReadByteArray
import okio.Buffer as OkioBuffer

// A source that hands out 1, 7, 13, 4093 or 64 KiB bytes per read must give the same components, problems, charset and
// component stream as the ByteArray, and a sink the same bytes as encodeToByteArray.
class AdapterEquivalenceTest {
    @Test
    fun kotlinxIoAndOkioReadEveryCaseLikeBytes() {
        val failures = ArrayList<String>()
        var compared = 0
        for (case in Suite.cases) {
            val input = case.file(Suite.INPUT)?.bytes ?: continue
            val meta = case.file(Suite.META)?.let { readJson(it.bytes).value } as? JsonObject ?: continue
            val parser = ICal.parser(parseOptions(meta))
            val expected = outcome { parser.parse(input) }
            val kotlinx = outcome { parser.kotlinxParse(KotlinxBuffer().apply { write(input) }) }
            val okio = outcome { parser.okioParse(OkioBuffer().write(input)) }
            if (kotlinx != expected) failures += "${case.directory}: kotlinx-io differs"
            if (okio != expected) failures += "${case.directory}: Okio differs"
            val stream = runCatching { parser.components(input).asSequence().toList() }.getOrNull()
            if (stream != null) {
                val viaKotlinx = parser.kotlinxComponents(KotlinxBuffer().apply { write(input) }).asSequence().toList()
                val viaOkio = parser.okioComponents(OkioBuffer().write(input)).asSequence().toList()
                if (viaKotlinx != stream) failures += "${case.directory}: kotlinx-io component stream differs"
                if (viaOkio != stream) failures += "${case.directory}: Okio component stream differs"
            }
            val tree = runCatching { parser.parse(input).components }.getOrNull() ?: continue
            val bytes = ICal.writer().encodeToByteArray(tree)
            val kotlinxSink = KotlinxBuffer()
            ICal.writer().kotlinxWrite(tree, kotlinxSink)
            val okioSink = OkioBuffer()
            ICal.writer().okioWrite(tree, okioSink)
            if (!bytes.contentEquals(kotlinxSink.kotlinxReadByteArray())) {
                failures +=
                    "${case.directory}: kotlinx-io write"
            }
            if (!bytes.contentEquals(okioSink.readByteArray())) failures += "${case.directory}: Okio write"
            compared++
        }
        assertEquals(emptyList(), failures)
        assertTrue(compared > Suite.cases.size / 2, "compared $compared")
    }

    private fun outcome(parse: () -> ParseResult): String = try {
        val result = parse()
        result.components.hashCode().toString() + result.components.toString() +
            result.problems.map { "${it.code.id}@${it.span.startOffset}" } + result.charset
    } catch (e: ICalParseException) {
        "rejected " + e.problems.map { "${it.code.id}@${it.span.startOffset}" }
    }
}
