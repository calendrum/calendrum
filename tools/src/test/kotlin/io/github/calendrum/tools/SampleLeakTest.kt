@file:OptIn(InternalICalApi::class)

package io.github.calendrum.tools

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.anonymize.Anonymizer
import kotlin.io.path.readBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SampleLeakTest {
    @Test
    fun leaksNoWordAddressUidOrLinkOfTheSamples() {
        assertTrue(SAMPLES.size >= 6)
        for (path in SAMPLES) {
            val source = path.readBytes()
            for (weeks in listOf(0, 2)) {
                val result = Anonymizer(KEY, weeks).anonymize(source)
                assertTrue(result.isVerified, "${path.fileName}: ${result.summary}")
                val parsed = ICal.parse(result.bytes)
                assertEquals(ICal.parse(source).charset, parsed.charset, path.fileName.toString())
                val output = String(result.bytes, charset(parsed.charset)).lowercase()
                val words = sensitiveWords(source, result.bytes)
                assertTrue(words.size > 10, "${path.fileName}: ${words.size} sensitive words")
                val leaked = words.filter { it.lowercase() in output } +
                    sensitiveValues(source).filter { it in output }.map { "a whole value" }
                assertEquals(emptyList(), leaked, path.fileName.toString())
            }
        }
    }

    private companion object {
        val KEY = ByteArray(32) { (it * 13 + 5).toByte() }
    }
}
