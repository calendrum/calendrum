package io.github.calendrum

// Tables keyed by strings from the input (TZIDs, UIDs). Many strings with one hashCode (`AL` and `B-` hash alike on
// every platform) turn a hash table into a list on JS and Kotlin/Native, which have no tree buckets: 32,768 such TZIDs
// took minutes in the occurrences. Compared keys cost a logarithm whatever the strings, and for the
// few time zones of a real calendar they are as fast as a hash table.

// A map from input strings: an AVL tree, so that keys in any order cost O(log n) to insert and to find. A sorted array
// with shifting insertions was quadratic for keys arriving in descending order. Not thread-safe.
internal class SortedStringMap<V> {
    private class Node<V>(val key: String, var value: V) {
        var left: Node<V>? = null
        var right: Node<V>? = null
        var height = 1
    }

    private var root: Node<V>? = null

    var size: Int = 0
        private set

    fun containsKey(key: String): Boolean = find(key) != null

    operator fun get(key: String): V? = find(key)?.value

    operator fun set(key: String, value: V) {
        root = insert(root, key, value)
    }

    private fun find(key: String): Node<V>? {
        var node = root
        while (node != null) {
            val order = key.compareTo(node.key)
            if (order == 0) return node
            node = if (order < 0) node.left else node.right
        }
        return null
    }

    // The recursion is as deep as the tree: at most 1.44 log2(n), under 45 levels for any input that fits in memory.
    private fun insert(node: Node<V>?, key: String, value: V): Node<V> {
        if (node == null) {
            size++
            return Node(key, value)
        }
        val order = key.compareTo(node.key)
        when {
            order < 0 -> node.left = insert(node.left, key, value)

            order > 0 -> node.right = insert(node.right, key, value)

            else -> {
                node.value = value
                return node
            }
        }
        return balance(node)
    }

    private fun balance(node: Node<V>): Node<V> {
        update(node)
        val skew = heightOf(node.left) - heightOf(node.right)
        if (skew > 1) {
            val left = node.left!!
            if (heightOf(left.left) < heightOf(left.right)) node.left = rotateLeft(left)
            return rotateRight(node)
        }
        if (skew < -1) {
            val right = node.right!!
            if (heightOf(right.right) < heightOf(right.left)) node.right = rotateRight(right)
            return rotateLeft(node)
        }
        return node
    }

    private fun rotateRight(node: Node<V>): Node<V> {
        val top = node.left!!
        node.left = top.right
        top.right = node
        update(node)
        update(top)
        return top
    }

    private fun rotateLeft(node: Node<V>): Node<V> {
        val top = node.right!!
        node.right = top.left
        top.left = node
        update(node)
        update(top)
        return top
    }

    private fun update(node: Node<V>) {
        node.height = maxOf(heightOf(node.left), heightOf(node.right)) + 1
    }

    private fun heightOf(node: Node<V>?): Int = node?.height ?: 0

    override fun toString(): String = "SortedStringMap($size)"
}

// The positions 0 until [size] grouped by equal [key], each group in order and the groups in the order of their first
// position, as a LinkedHashMap of lists would give. Ordered by the hash of the key and not by its text: sorting boxed
// positions by the strings made the occurrences of `personal` 12 to 16 percent slower on Native and Wasm.
internal fun groupsOf(size: Int, key: (Int) -> String): List<List<Int>> {
    if (size == 0) return emptyList()
    val keys = Array(size) { key(it) }
    val hashes = IntArray(size) { mixHash(keys[it].hashCode()) }
    val order = orderByHash(hashes) { a, b -> keys[a].compareTo(keys[b]) }
    val groups = ArrayList<List<Int>>()
    // The group whose first position is this one, so that the groups come out in the order of their first position.
    val leader = IntArray(size)
    leader.fill(-1)
    var start = 0
    while (start < size) {
        val first = order[start]
        var end = start + 1
        while (end < size && hashes[order[end]] == hashes[first] && keys[order[end]] == keys[first]) end++
        leader[first] = groups.size
        groups += order.copyOfRange(start, end).asList()
        start = end
    }
    val ordered = ArrayList<List<Int>>(groups.size)
    for (position in 0 until size) {
        if (leader[position] >= 0) ordered += groups[leader[position]]
    }
    return ordered
}

// The finalizer of MurmurHash3, 32 bits: spreads a hashCode over all its bits, which orderByHash reads.
internal fun mixHash(value: Int): Int {
    var hash = value xor (value ushr 16)
    hash *= -0x7a143595
    hash = hash xor (hash ushr 13)
    hash *= -0x3d4d51cb
    return hash xor (hash ushr 16)
}

// Positions in the order of their hash, as unsigned, then of [compare] for equal hashes, then by position: equal keys end
// up next to each other and in input order. The hash only orders and is never the slot of a table, so keys made to share a
// hash cost comparisons and nothing worse. An LSD radix sort with bytes as digits; the rare runs of one hash go to a merge
// sort, which also takes the whole input when every hash is the same: then it costs n log n comparisons. An Int and not a
// Long, as Kotlin/JS emulates Long with objects: the same sort on a Long was four times slower there.
internal inline fun orderByHash(hashes: IntArray, crossinline compare: (Int, Int) -> Int): IntArray {
    val size = hashes.size
    if (size < RADIX_THRESHOLD) {
        return sortedPositions(size) { a, b ->
            val byHash = compareUnsigned(hashes[a], hashes[b])
            if (byHash != 0) {
                byHash
            } else {
                val byKey = compare(a, b)
                if (byKey != 0) byKey else a.compareTo(b)
            }
        }
    }
    var from = IntArray(size) { it }
    var to = IntArray(size)
    val counts = IntArray(RADIX + 1)
    var shift = 0
    while (shift < 32) {
        counts.fill(0)
        for (position in from) counts[((hashes[position] ushr shift) and RADIX_MASK) + 1]++
        for (digit in 1..RADIX) counts[digit] += counts[digit - 1]
        for (position in from) to[counts[(hashes[position] ushr shift) and RADIX_MASK]++] = position
        val swap = from
        from = to
        to = swap
        shift += 8
    }
    var start = 0
    while (start < size) {
        val hash = hashes[from[start]]
        var end = start + 1
        while (end < size && hashes[from[end]] == hash) end++
        if (end - start > 1) {
            val run = from.copyOfRange(start, end)
            val order = sortedPositions(run.size) { a, b ->
                val byKey = compare(run[a], run[b])
                if (byKey != 0) byKey else run[a].compareTo(run[b])
            }
            for (index in order.indices) from[start + index] = run[order[index]]
        }
        start = end
    }
    return from
}

internal const val RADIX = 256
internal const val RADIX_MASK = 0xFF
internal const val RADIX_THRESHOLD = 64

internal fun compareUnsigned(left: Int, right: Int): Int = (left xor Int.MIN_VALUE).compareTo(right xor Int.MIN_VALUE)

// The positions 0 until [size] in the order of [compare], which must be total: break ties by position. A bottom-up merge
// sort over IntArray, so no input makes it quadratic, and no index is boxed, as a comparator over Array<Int> would do for
// every comparison.
internal inline fun sortedPositions(size: Int, crossinline compare: (Int, Int) -> Int): IntArray {
    var from = IntArray(size) { it }
    if (size < 2) return from
    var to = IntArray(size)
    var width = 1
    while (width < size) {
        var start = 0
        while (start < size) {
            val middle = if (width >= size - start) size else start + width
            val end = if (width >= size - middle) size else middle + width
            var left = start
            var right = middle
            var out = start
            while (left < middle && right < end) {
                to[out++] = if (compare(from[left], from[right]) <= 0) from[left++] else from[right++]
            }
            while (left < middle) to[out++] = from[left++]
            while (right < end) to[out++] = from[right++]
            start = end
        }
        val swap = from
        from = to
        to = swap
        width = if (width > size / 2) size else width * 2
    }
    return from
}
