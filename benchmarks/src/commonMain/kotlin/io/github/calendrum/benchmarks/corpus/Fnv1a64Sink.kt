// FNV-1a 64-bit (public domain): a tiny streaming hash so the determinism test never has to hold `huge` (~100 MB) in
// memory. Only SHA-256, used for the on-disk baseline, needs `java.security` and is therefore JVM-only (`Sha256Sink` in
// `jvmMain`).

package io.github.calendrum.benchmarks.corpus

private val FNV_OFFSET_BASIS: Long = 0xCBF29CE484222325UL.toLong()
private val FNV_PRIME: Long = 0x100000001B3UL.toLong()

internal class Fnv1a64Sink : ByteSink {
    private var hash: Long = FNV_OFFSET_BASIS

    override fun write(bytes: ByteArray, offset: Int, length: Int) {
        var h = hash
        for (i in offset until offset + length) {
            h = h xor (bytes[i].toLong() and 0xFF)
            h *= FNV_PRIME
        }
        hash = h
    }

    fun hexDigest(): String {
        val chars = CharArray(16)
        for (i in 0 until 16) {
            val nibble = ((hash ushr ((15 - i) * 4)) and 0xF).toInt()
            chars[i] = HEX_DIGITS[nibble]
        }
        return chars.concatToString()
    }

    private companion object {
        val HEX_DIGITS = "0123456789abcdef".toCharArray()
    }
}
