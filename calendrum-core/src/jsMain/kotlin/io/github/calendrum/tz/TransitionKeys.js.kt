package io.github.calendrum.tz

internal actual fun searchKeys(values: LongArray, size: Int): DoubleArray? = DoubleArray(size) { values[it].toDouble() }

internal actual fun lastAtOrBefore(values: LongArray, keys: DoubleArray?, size: Int, key: Long): Int {
    val target = key.toDouble()
    val search = keys!!
    var low = 0
    var high = size - 1
    var found = -1
    while (low <= high) {
        val middle = (low + high) ushr 1
        if (search[middle] <= target) {
            found = middle
            low = middle + 1
        } else {
            high = middle - 1
        }
    }
    return found
}

internal actual fun isBefore(key: Long, values: LongArray, keys: DoubleArray?, index: Int, seconds: Int): Boolean =
    key.toDouble() < keys!![index] + seconds
