// xxHash64 (seed 0) of the reference implementation over a byte range in one call. Lanes are read little-endian byte by
// byte, the same on every platform. `xxHash64` is `expect`: JVM, Kotlin/Native and Wasm use `xxHash64Longs` with native
// 64-bit arithmetic; JS, where `Long` is an emulated object and every operation allocates, uses the same algorithm on
// pairs of `Int` (`XxHash64.js.kt`). Both give the same bits; `XxHash64Test` compares them on every platform.

package io.github.calendrum.syntax

internal expect fun xxHash64(bytes: ByteArray, from: Int, to: Int): Long

internal fun xxHash64Longs(bytes: ByteArray, from: Int, to: Int): Long {
    var i = from
    var hash: Long
    if (to - from >= XXH_STRIPE) {
        var v1 = XXH_PRIME1 + XXH_PRIME2
        var v2 = XXH_PRIME2
        var v3 = 0L
        var v4 = -XXH_PRIME1
        val last = to - XXH_STRIPE
        while (i <= last) {
            v1 = round(v1, longAt(bytes, i))
            v2 = round(v2, longAt(bytes, i + LONG_BYTES))
            v3 = round(v3, longAt(bytes, i + 2 * LONG_BYTES))
            v4 = round(v4, longAt(bytes, i + 3 * LONG_BYTES))
            i += XXH_STRIPE
        }
        hash = (v1 rotl 1) + (v2 rotl 7) + (v3 rotl 12) + (v4 rotl 18)
        hash = merge(hash, v1)
        hash = merge(hash, v2)
        hash = merge(hash, v3)
        hash = merge(hash, v4)
    } else {
        hash = XXH_PRIME5
    }
    hash += (to - from).toLong()
    while (i + LONG_BYTES <= to) {
        hash = hash xor round(0L, longAt(bytes, i))
        hash = (hash rotl 27) * XXH_PRIME1 + XXH_PRIME4
        i += LONG_BYTES
    }
    if (i + INT_BYTES <= to) {
        hash = hash xor ((xxIntAt(bytes, i).toLong() and UINT_MASK) * XXH_PRIME1)
        hash = (hash rotl 23) * XXH_PRIME2 + XXH_PRIME3
        i += INT_BYTES
    }
    while (i < to) {
        hash = hash xor ((bytes[i].toLong() and BYTE_MASK) * XXH_PRIME5)
        hash = (hash rotl 11) * XXH_PRIME1
        i++
    }
    hash = hash xor (hash ushr 33)
    hash *= XXH_PRIME2
    hash = hash xor (hash ushr 29)
    hash *= XXH_PRIME3
    return hash xor (hash ushr 32)
}

internal fun xxIntAt(bytes: ByteArray, at: Int): Int = (bytes[at].toInt() and 0xFF) or
    ((bytes[at + 1].toInt() and 0xFF) shl 8) or
    ((bytes[at + 2].toInt() and 0xFF) shl 16) or
    ((bytes[at + 3].toInt() and 0xFF) shl 24)

internal const val XXH_STRIPE: Int = 32

private fun round(accumulator: Long, input: Long): Long = ((accumulator + input * XXH_PRIME2) rotl 31) * XXH_PRIME1

private fun merge(accumulator: Long, value: Long): Long = (accumulator xor round(0L, value)) * XXH_PRIME1 + XXH_PRIME4

private infix fun Long.rotl(bits: Int): Long = (this shl bits) or (this ushr (64 - bits))

private fun longAt(bytes: ByteArray, at: Int): Long = (xxIntAt(bytes, at).toLong() and UINT_MASK) or
    (xxIntAt(bytes, at + INT_BYTES).toLong() shl 32)

private const val XXH_PRIME1 = -7046029288634856825L
private const val XXH_PRIME2 = -4417276706812531889L
private const val XXH_PRIME3 = 1609587929392839161L
private const val XXH_PRIME4 = -8796714831421723037L
private const val XXH_PRIME5 = 2870177450012600261L
private const val LONG_BYTES = 8
private const val INT_BYTES = 4
private const val BYTE_MASK = 0xFFL
private const val UINT_MASK = 0xFFFFFFFFL
