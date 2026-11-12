@file:OptIn(ExperimentalForeignApi::class)

package io.github.calendrum.benchmarks.coldStart

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell

fun main(args: Array<String>) {
    require(args.size == 2) { "Usage: coldStart <variant> <input file>" }
    val input = readFile(args[1])
    println(firstParse(args[0], input))
}

private fun readFile(path: String): ByteArray {
    val file = fopen(path, "rb") ?: error("Cannot open $path")
    try {
        check(fseek(file, 0, SEEK_END) == 0) { "Cannot seek in $path" }
        val size = ftell(file).convert<Int>()
        check(fseek(file, 0, SEEK_SET) == 0) { "Cannot seek in $path" }
        val bytes = ByteArray(size)
        val read = bytes.usePinned { fread(it.addressOf(0), 1.convert(), size.convert(), file) }
        check(read.toLong() == size.toLong()) { "Read $read of $size bytes from $path" }
        return bytes
    } finally {
        fclose(file)
    }
}
