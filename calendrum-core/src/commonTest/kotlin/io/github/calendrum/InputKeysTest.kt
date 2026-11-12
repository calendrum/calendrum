package io.github.calendrum

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

// The helpers for keys taken from the input against the collections they stand in for. The pools hold strings that share a
// hashCode ("Aa" and "BB" hash alike on every platform), so equal hashes have to fall back to the strings.
class InputKeysTest {
    private val pool =
        listOf("", "Aa", "BB", "AaAa", "AaBB", "BBAa", "BBBB", "Europe/Berlin", "Europe/Moscow", "1@example.com")

    @Test
    fun groupsOfIsWhatALinkedHashMapOfListsGives() {
        val random = Random(1026)
        for (round in 0 until 300) {
            val size = if (round % 4 == 0) random.nextInt(64, 500) else random.nextInt(0, 64)
            val keys = List(size) {
                pool.random(random) +
                    if (random.nextInt(5) == 0) random.nextInt(3).toString() else ""
            }
            val expected = LinkedHashMap<String, MutableList<Int>>()
            keys.forEachIndexed { index, key -> expected.getOrPut(key) { mutableListOf() } += index }
            assertEquals(expected.values.toList(), groupsOf(size) { keys[it] }, "round $round, $size keys")
        }
    }

    @Test
    fun groupsOfKeysWithOneHashCode() {
        val keys = (0 until 512).map { bits ->
            (0 until 9).joinToString("") {
                if (bits shr it and 1 ==
                    0
                ) {
                    "Aa"
                } else {
                    "BB"
                }
            }
        }
        assertEquals(1, keys.map { it.hashCode() }.distinct().size)
        val doubled = keys + keys.reversed()
        val groups = groupsOf(doubled.size) { doubled[it] }
        assertEquals(keys.size, groups.size)
        groups.forEachIndexed { index, group -> assertEquals(listOf(index, doubled.size - 1 - index), group) }
    }

    @Test
    fun orderByHashPutsEqualKeysTogetherInInputOrder() {
        val random = Random(7)
        for (size in listOf(0, 1, 63, 64, 65, 300, 2000)) {
            val keys = List(size) { pool.random(random) }
            val hashes = IntArray(size) { mixHash(keys[it].hashCode()) }
            val order = orderByHash(hashes) { a, b -> keys[a].compareTo(keys[b]) }
            assertEquals((0 until size).toList(), order.sorted(), "a permutation of $size")
            val seen = HashSet<String>()
            var index = 0
            while (index < size) {
                val key = keys[order[index]]
                assertEquals(true, seen.add(key), "$key comes in one run, size $size")
                var end = index + 1
                while (end < size && keys[order[end]] == key) {
                    assertEquals(true, order[end - 1] < order[end], "input order inside a run")
                    end++
                }
                index = end
            }
        }
    }
}
