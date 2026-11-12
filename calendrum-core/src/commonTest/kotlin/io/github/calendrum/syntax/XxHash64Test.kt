package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals

class XxHash64Test {
    @Test
    fun officialVectorsWithSeedZero() {
        val vectors = listOf(
            "" to "ef46db3751d8e999",
            "a" to "d24ec4f1a98c6e5b",
            "abc" to "44bc2cf5ad770999",
            "message digest" to "066ed728fceeb3be",
            "abcdefghijklmnopqrstuvwxyz" to "cfe1f278fa89835c",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789" to "aaa46907d3047814",
            "1234567890".repeat(8) to "e04a477f19ee145d",
        )
        for ((text, expected) in vectors) {
            val bytes = text.encodeToByteArray()
            assertEquals(expected, hex(xxHash64(bytes, 0, bytes.size)), text)
        }
    }

    @Test
    fun sanityBufferOfTheReferenceImplementation() {
        val buffer = sanityBuffer(SANITY_SIZE)
        val vectors = mapOf(
            0 to "ef46db3751d8e999",
            1 to "e934a84adb052768",
            14 to "8282dcc4994e35c8",
            222 to "b641ae8cb691c174",
        )
        for ((length, expected) in vectors) {
            assertEquals(expected, hex(xxHash64(buffer, 0, length)), "length $length")
        }
    }

    @Test
    fun platformHashEqualsTheLongArithmeticForEveryLengthAndOffset() {
        val buffer = sanityBuffer(SANITY_SIZE)
        for (from in 0..XXH_STRIPE) {
            for (to in from..buffer.size) {
                assertEquals(xxHash64Longs(buffer, from, to), xxHash64(buffer, from, to), "range $from..$to")
            }
        }
    }

    @Test
    fun extremeBytesHashTheSameWay() {
        for (fill in intArrayOf(0x00, 0x7F, 0x80, 0xFF)) {
            val buffer = ByteArray(SANITY_SIZE) { fill.toByte() }
            for (length in 0..buffer.size) {
                assertEquals(xxHash64Longs(buffer, 0, length), xxHash64(buffer, 0, length), "fill $fill length $length")
            }
        }
    }

    private fun hex(value: Long): String = value.toULong().toString(HEX_RADIX).padStart(HASH_DIGITS, '0')

    private fun sanityBuffer(size: Int): ByteArray {
        var generator = PRIME32
        return ByteArray(size) {
            val byte = (generator ushr 56).toByte()
            generator *= GENERATOR_STEP
            byte
        }
    }

    private companion object {
        const val SANITY_SIZE = 222
        const val HEX_RADIX = 16
        const val HASH_DIGITS = 16
        const val PRIME32 = 2654435761L
        const val GENERATOR_STEP = -7046029288634856819L
    }
}
