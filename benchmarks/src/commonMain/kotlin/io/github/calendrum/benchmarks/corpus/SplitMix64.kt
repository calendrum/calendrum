// SplitMix64 (Vigna, public domain): a fixed-width Long PRNG whose output is defined purely by 64-bit arithmetic, so it
// is bit-identical on every Kotlin target and every future compiler version. `kotlin.random.Random(seed)` is explicitly
// disallowed for corpus generation: its sequence is only guaranteed stable within one Kotlin version.

package io.github.calendrum.benchmarks.corpus

// The standard SplitMix64 constants; written via `UL.toLong()` because the `const val` initializer of Kotlin for a hex
// Long literal with the sign bit set is a compile error, unlike Java.
private val GOLDEN_GAMMA: Long = 0x9E3779B97F4A7C15UL.toLong()
private val MIX_1: Long = 0xBF58476D1CE4E5B9UL.toLong()
private val MIX_2: Long = 0x94D049BB133111EBUL.toLong()

internal class SplitMix64(seed: Long) {
    private var state: Long = seed

    fun nextLong(): Long {
        state += GOLDEN_GAMMA
        var z = state
        z = (z xor (z ushr 30)) * MIX_1
        z = (z xor (z ushr 27)) * MIX_2
        return z xor (z ushr 31)
    }

    // Not perfectly uniform for large bounds and not cryptographically secure: fine for a deterministic corpus
    // generator, never used outside `benchmarks`.
    fun nextInt(bound: Int): Int {
        require(bound > 0) { "bound must be positive, was $bound" }
        return ((nextLong() ushr 33) % bound.toLong()).toInt()
    }

    fun nextIntRange(fromInclusive: Int, toInclusive: Int): Int {
        require(toInclusive >= fromInclusive) { "empty range $fromInclusive..$toInclusive" }
        return fromInclusive + nextInt(toInclusive - fromInclusive + 1)
    }

    fun nextDouble(): Double = (nextLong() ushr 11).toDouble() * DOUBLE_ULP

    fun chancePercent(probabilityPercent: Int): Boolean = nextInt(100) < probabilityPercent

    fun <T> pick(items: List<T>): T = items[nextInt(items.size)]

    private companion object {
        const val DOUBLE_ULP: Double = 1.0 / (1L shl 53).toDouble()
    }
}
