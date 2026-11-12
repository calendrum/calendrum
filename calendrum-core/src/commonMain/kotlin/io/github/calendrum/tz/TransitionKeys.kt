package io.github.calendrum.tz

// Search keys of a transition table. On JS every `Long` operation is a call on an object, and the binary
// search with its comparisons was a quarter of `toInstant`; there the keys are `Double` copies of the instants and local
// starts (epoch seconds up to 9999 are exact, farther ones still compare in order), which made `toInstant` 89 % faster.
// On the JVM, Native and Wasm the same `Double` keys cost 3 to 27 %, so there `searchKeys` gives `null` and the search
// runs over the `Long` values as it always did.

internal expect fun searchKeys(values: LongArray, size: Int): DoubleArray?

// The index of the last value at or before [key], or -1.
internal expect fun lastAtOrBefore(values: LongArray, keys: DoubleArray?, size: Int, key: Long): Int

// `true` when [key] is before `values[index] + seconds`.
internal expect fun isBefore(key: Long, values: LongArray, keys: DoubleArray?, index: Int, seconds: Int): Boolean
