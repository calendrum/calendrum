package io.github.calendrum.benchmarks.corpus

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal object RealCorpus {
    const val UNIVERSITY: String = "university/university-group-schedule-01"

    fun bytes(file: String): ByteArray = Files.readAllBytes(directory().resolve("$file.ics"))

    // corpus/real/ stays with the maintainer (.gitignore): without it the benchmarks and tests of real files are left out.
    fun isPresent(): Boolean = find()?.resolve("$UNIVERSITY.ics")?.let(Files::isRegularFile) == true

    fun events(file: String): Int {
        val meta = String(Files.readAllBytes(directory().resolve("$file.meta.json")), Charsets.UTF_8)
        val events = EVENTS.find(meta) ?: throw IllegalStateException("No \"events\" in the manifest of $file")
        return events.groupValues[1].toInt()
    }

    // JMH forks inherit the working directory of the benchmark task (the `benchmarks` project directory) but only the
    // `-D` flags the convention passes; walking up from `user.dir` to `corpus/real` finds the file without putting an
    // absolute path into the fork flags and `environment.json`.
    private fun directory(): Path =
        find() ?: throw IllegalStateException("No $RELATIVE above ${System.getProperty("user.dir")}")

    private fun find(): Path? {
        var directory: Path? = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        while (directory != null) {
            val candidate = directory.resolve(RELATIVE)
            if (Files.isDirectory(candidate)) return candidate
            directory = directory.parent
        }
        return null
    }

    private const val RELATIVE = "corpus/real"
    private val EVENTS = Regex("\"events\": ([0-9]+)")
}
