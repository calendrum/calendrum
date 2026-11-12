package io.github.calendrum.validate

// Names, TZIDs and paths come from the input, and many strings with one hash code (`AL` and `B-` hash alike on every
// platform) turn a hash table into a list: on JS for any key, on the JVM for keys that are not Comparable. These
// helpers sort instead, so their cost is n log n whatever the input.

// For each of [size] names, how many earlier names are equal: the index of an ElementPath segment among its namesakes.
internal fun namesakeIndices(size: Int, name: (Int) -> String): IntArray {
    val indices = IntArray(size)
    if (size < 2) return indices
    if (size <= SMALL) {
        for (position in 1 until size) {
            val current = name(position)
            var count = 0
            for (earlier in 0 until position) if (name(earlier) == current) count++
            indices[position] = count
        }
        return indices
    }
    val order = sortedOrder(size) { a, b -> name(a).compareTo(name(b)) }
    for (rank in 1 until size) {
        val position = order[rank]
        val previous = order[rank - 1]
        if (name(position) == name(previous)) indices[position] = indices[previous] + 1
    }
    return indices
}

// `true` at the first of each group of equal keys, in the original order.
internal fun firstOfEach(size: Int, compare: (Int, Int) -> Int): BooleanArray {
    val first = BooleanArray(size)
    if (size == 0) return first
    val order = sortedOrder(size, compare)
    first[order[0]] = true
    for (rank in 1 until size) first[order[rank]] = compare(order[rank - 1], order[rank]) != 0
    return first
}

// The positions 0 until [size] sorted by [compare], equal keys in their original order.
private fun sortedOrder(size: Int, compare: (Int, Int) -> Int): Array<Int> {
    val order = Array(size) { it }
    order.sortWith { a, b ->
        val result = compare(a, b)
        if (result != 0) result else a.compareTo(b)
    }
    return order
}

private const val SMALL = 16
