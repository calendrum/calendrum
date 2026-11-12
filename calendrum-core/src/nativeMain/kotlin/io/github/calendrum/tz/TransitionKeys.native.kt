package io.github.calendrum.tz

internal actual fun searchKeys(values: LongArray, size: Int): DoubleArray? = null

internal actual fun lastAtOrBefore(values: LongArray, keys: DoubleArray?, size: Int, key: Long): Int =
    lastAtOrBeforeLong(values, size, key)

internal actual fun isBefore(key: Long, values: LongArray, keys: DoubleArray?, index: Int, seconds: Int): Boolean =
    key < values[index] + seconds
