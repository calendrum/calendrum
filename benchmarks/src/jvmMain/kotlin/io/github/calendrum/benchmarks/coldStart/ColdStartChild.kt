package io.github.calendrum.benchmarks.coldStart

import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: ColdStartChild <variant> <input file>" }
    val input = Files.readAllBytes(Paths.get(args[1]))
    println(firstParseWithRival(args[0], input))
}
