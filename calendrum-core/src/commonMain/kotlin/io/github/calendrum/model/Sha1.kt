package io.github.calendrum.model

// SHA-1 of FIPS 180-4 for UUIDv5 only: the core has no crypto dependency.
internal fun sha1(input: ByteArray): ByteArray {
    val length = input.size.toLong() * 8
    val padded = ByteArray(((input.size + 8) / 64 + 1) * 64)
    input.copyInto(padded)
    padded[input.size] = 0x80.toByte()
    for (index in 0 until 8) padded[padded.size - 1 - index] = (length ushr (8 * index)).toByte()
    var h0 = 0x67452301
    var h1 = 0xEFCDAB89.toInt()
    var h2 = 0x98BADCFE.toInt()
    var h3 = 0x10325476
    var h4 = 0xC3D2E1F0.toInt()
    val words = IntArray(80)
    for (block in 0 until padded.size / 64) {
        for (index in 0 until 16) {
            val at = block * 64 + index * 4
            words[index] = (padded[at].toInt() and 0xFF shl 24) or (padded[at + 1].toInt() and 0xFF shl 16) or
                (padded[at + 2].toInt() and 0xFF shl 8) or (padded[at + 3].toInt() and 0xFF)
        }
        for (index in 16 until 80) {
            words[index] =
                (words[index - 3] xor words[index - 8] xor words[index - 14] xor words[index - 16]).rotateLeft(1)
        }
        var a = h0
        var b = h1
        var c = h2
        var d = h3
        var e = h4
        for (index in 0 until 80) {
            val (f, k) = when (index / 20) {
                0 -> ((b and c) or (b.inv() and d)) to 0x5A827999
                1 -> (b xor c xor d) to 0x6ED9EBA1
                2 -> ((b and c) or (b and d) or (c and d)) to 0x8F1BBCDC.toInt()
                else -> (b xor c xor d) to 0xCA62C1D6.toInt()
            }
            val next = a.rotateLeft(5) + f + e + k + words[index]
            e = d
            d = c
            c = b.rotateLeft(30)
            b = a
            a = next
        }
        h0 += a
        h1 += b
        h2 += c
        h3 += d
        h4 += e
    }
    val out = ByteArray(20)
    for ((slot, value) in intArrayOf(h0, h1, h2, h3, h4).withIndex()) {
        for (index in 0 until 4) out[slot * 4 + index] = (value ushr (24 - 8 * index)).toByte()
    }
    return out
}
