package io.github.calendrum.buildlogic

class BenchmarkProfile(
    val name: String,
    val warmups: Int,
    val iterations: Int,
    val iterationSeconds: Int,
    val jvmForks: Int,
) {
    val nonJvmIterations: Int get() = iterations * jvmForks

    val taskSuffix: String get() = name.replaceFirstChar { it.uppercaseChar() } + "Benchmark"

    companion object {
        val all: List<BenchmarkProfile> = listOf(
            BenchmarkProfile(name = "quick", warmups = 3, iterations = 5, iterationSeconds = 1, jvmForks = 1),
            BenchmarkProfile(name = "full", warmups = 5, iterations = 10, iterationSeconds = 2, jvmForks = 3),
        )
    }
}
