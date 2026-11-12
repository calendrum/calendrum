package io.github.calendrum.benchmarks.baseline

import io.github.calendrum.benchmarks.reader.countedComponents
import io.github.calendrum.benchmarks.reader.expectedCounts

internal class ModelCounts(val components: Map<String, Int>, val properties: Int) {
    override fun equals(other: Any?): Boolean =
        other is ModelCounts && components == other.components && properties == other.properties

    override fun hashCode(): Int = components.hashCode() * 31 + properties

    override fun toString(): String = "components=$components properties=$properties"
}

internal class ModelCounter {
    private val components = HashMap<String, Int>()
    private var properties = 0

    fun component(name: String) {
        components[name] = (components[name] ?: 0) + 1
    }

    fun properties(count: Int) {
        properties += count
    }

    fun result(): ModelCounts {
        val unexpected = components.keys - countedComponents.toSet()
        check(unexpected.isEmpty()) { "Unexpected components: $unexpected" }
        return ModelCounts(countedComponents.associateWith { components[it] ?: 0 }, properties)
    }
}

internal fun generatorCounts(corpus: String): ModelCounts {
    val expected = expectedCounts.getValue(corpus)
    return ModelCounts(expected.components, expected.properties)
}

internal fun requireGeneratorCounts(corpus: String, library: String, actual: ModelCounts) {
    val expected = generatorCounts(corpus)
    check(actual == expected) { "$library on $corpus: expected $expected, got $actual" }
}
