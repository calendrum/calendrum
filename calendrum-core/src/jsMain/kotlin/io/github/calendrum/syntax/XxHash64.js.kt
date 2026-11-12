package io.github.calendrum.syntax

internal actual fun xxHash64(bytes: ByteArray, from: Int, to: Int): Long = IntPairXxHash64().hash(bytes, from, to)

// One 64-bit register as `high`/`low`; each step works in place. `multiply` is the 16-bit-limb product of the stdlib JS
// `Long.multiply` (every partial sum stays below 2^32, so `Int` wrapping keeps the bits), `add` carries through an
// unsigned comparison, `rotateLeft` takes 0 < bits < 32. The constants are the halves of the 64-bit primes and of the
// initial lanes `PRIME1 + PRIME2` and `-PRIME1`.
private class IntPairXxHash64 {
    private var high = 0
    private var low = 0

    fun hash(bytes: ByteArray, from: Int, to: Int): Long {
        var i = from
        if (to - from >= XXH_STRIPE) {
            var v1High = V1_HIGH
            var v1Low = V1_LOW
            var v2High = P2_HIGH
            var v2Low = P2_LOW
            var v3High = 0
            var v3Low = 0
            var v4High = V4_HIGH
            var v4Low = V4_LOW
            val last = to - XXH_STRIPE
            while (i <= last) {
                load(bytes, i)
                round(v1High, v1Low)
                v1High = high
                v1Low = low
                load(bytes, i + LONG_BYTES)
                round(v2High, v2Low)
                v2High = high
                v2Low = low
                load(bytes, i + 2 * LONG_BYTES)
                round(v3High, v3Low)
                v3High = high
                v3Low = low
                load(bytes, i + 3 * LONG_BYTES)
                round(v4High, v4Low)
                v4High = high
                v4Low = low
                i += XXH_STRIPE
            }
            set(v1High, v1Low)
            rotateLeft(1)
            var sumHigh = high
            var sumLow = low
            set(v2High, v2Low)
            rotateLeft(7)
            add(sumHigh, sumLow)
            sumHigh = high
            sumLow = low
            set(v3High, v3Low)
            rotateLeft(12)
            add(sumHigh, sumLow)
            sumHigh = high
            sumLow = low
            set(v4High, v4Low)
            rotateLeft(18)
            add(sumHigh, sumLow)
            merge(v1High, v1Low)
            merge(v2High, v2Low)
            merge(v3High, v3Low)
            merge(v4High, v4Low)
        } else {
            set(P5_HIGH, P5_LOW)
        }
        add(0, to - from)
        while (i + LONG_BYTES <= to) {
            val hashHigh = high
            val hashLow = low
            load(bytes, i)
            round(0, 0)
            xor(hashHigh, hashLow)
            rotateLeft(27)
            multiply(P1_HIGH, P1_LOW)
            add(P4_HIGH, P4_LOW)
            i += LONG_BYTES
        }
        if (i + INT_BYTES <= to) {
            val hashHigh = high
            val hashLow = low
            set(0, xxIntAt(bytes, i))
            multiply(P1_HIGH, P1_LOW)
            xor(hashHigh, hashLow)
            rotateLeft(23)
            multiply(P2_HIGH, P2_LOW)
            add(P3_HIGH, P3_LOW)
            i += INT_BYTES
        }
        while (i < to) {
            val hashHigh = high
            val hashLow = low
            set(0, bytes[i].toInt() and 0xFF)
            multiply(P5_HIGH, P5_LOW)
            xor(hashHigh, hashLow)
            rotateLeft(11)
            multiply(P1_HIGH, P1_LOW)
            i++
        }
        xorShiftRight(33)
        multiply(P2_HIGH, P2_LOW)
        xorShiftRight(29)
        multiply(P3_HIGH, P3_LOW)
        xorShiftRight(32)
        return (high.toLong() shl 32) or (low.toLong() and 0xFFFFFFFFL)
    }

    private fun round(accumulatorHigh: Int, accumulatorLow: Int) {
        multiply(P2_HIGH, P2_LOW)
        add(accumulatorHigh, accumulatorLow)
        rotateLeft(31)
        multiply(P1_HIGH, P1_LOW)
    }

    private fun merge(valueHigh: Int, valueLow: Int) {
        val accumulatorHigh = high
        val accumulatorLow = low
        set(valueHigh, valueLow)
        round(0, 0)
        xor(accumulatorHigh, accumulatorLow)
        multiply(P1_HIGH, P1_LOW)
        add(P4_HIGH, P4_LOW)
    }

    private fun load(bytes: ByteArray, at: Int) {
        low = xxIntAt(bytes, at)
        high = xxIntAt(bytes, at + INT_BYTES)
    }

    private fun set(valueHigh: Int, valueLow: Int) {
        high = valueHigh
        low = valueLow
    }

    private fun add(valueHigh: Int, valueLow: Int) {
        val sum = low + valueLow
        val carry = if ((sum xor Int.MIN_VALUE) < (low xor Int.MIN_VALUE)) 1 else 0
        high += valueHigh + carry
        low = sum
    }

    private fun xor(valueHigh: Int, valueLow: Int) {
        high = high xor valueHigh
        low = low xor valueLow
    }

    private fun rotateLeft(bits: Int) {
        val oldHigh = high
        val oldLow = low
        high = (oldHigh shl bits) or (oldLow ushr (32 - bits))
        low = (oldLow shl bits) or (oldHigh ushr (32 - bits))
    }

    private fun xorShiftRight(bits: Int) {
        if (bits >= 32) {
            low = low xor (high ushr (bits - 32))
            return
        }
        low = low xor ((low ushr bits) or (high shl (32 - bits)))
        high = high xor (high ushr bits)
    }

    private fun multiply(factorHigh: Int, factorLow: Int) {
        val a48 = high ushr 16
        val a32 = high and HALF_MASK
        val a16 = low ushr 16
        val a00 = low and HALF_MASK
        val b48 = factorHigh ushr 16
        val b32 = factorHigh and HALF_MASK
        val b16 = factorLow ushr 16
        val b00 = factorLow and HALF_MASK
        var c00 = a00 * b00
        var c16 = c00 ushr 16
        c00 = c00 and HALF_MASK
        c16 += a16 * b00
        var c32 = c16 ushr 16
        c16 = c16 and HALF_MASK
        c16 += a00 * b16
        c32 += c16 ushr 16
        c16 = c16 and HALF_MASK
        c32 += a32 * b00
        var c48 = c32 ushr 16
        c32 = c32 and HALF_MASK
        c32 += a16 * b16
        c48 += c32 ushr 16
        c32 = c32 and HALF_MASK
        c32 += a00 * b32
        c48 += c32 ushr 16
        c32 = c32 and HALF_MASK
        c48 += a48 * b00 + a32 * b16 + a16 * b32 + a00 * b48
        high = ((c48 and HALF_MASK) shl 16) or c32
        low = (c16 shl 16) or c00
    }
}

private const val LONG_BYTES = 8
private const val INT_BYTES = 4
private const val HALF_MASK = 0xFFFF
private const val P1_HIGH = 0x9e3779b1.toInt()
private const val P1_LOW = 0x85ebca87.toInt()
private const val P2_HIGH = 0xc2b2ae3d.toInt()
private const val P2_LOW = 0x27d4eb4f
private const val P3_HIGH = 0x165667b1
private const val P3_LOW = 0x9e3779f9.toInt()
private const val P4_HIGH = 0x85ebca77.toInt()
private const val P4_LOW = 0xc2b2ae63.toInt()
private const val P5_HIGH = 0x27d4eb2f
private const val P5_LOW = 0x165667c5
private const val V1_HIGH = 0x60ea27ee
private const val V1_LOW = 0xadc0b5d6.toInt()
private const val V4_HIGH = 0x61c8864e
private const val V4_LOW = 0x7a143579
