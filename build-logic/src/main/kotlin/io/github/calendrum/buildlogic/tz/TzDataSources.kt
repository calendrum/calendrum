package io.github.calendrum.buildlogic.tz

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
abstract class TzDataSources : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val tzdbDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val directory = tzdbDirectory.get().asFile
        val tzdb = Tzdb.read(directory)
        val version = File(directory, "version").readText().trim()
        val names = tzdb.zones.keys.sorted()
        val abbreviations = LinkedHashMap<String, Int>()
        val data = PackedWriter()
        val starts = HashMap<String, Int>()
        for (name in names) {
            starts[name] = data.length
            encode(Tzdb.compile(tzdb, name, EXPLICIT_UNTIL_YEAR), data, abbreviations)
        }
        val index = StringBuilder()
        for (name in (names + tzdb.links.keys).sorted()) {
            val target = if (name in starts) name else resolveLink(tzdb, name)
            val start = starts[target] ?: continue
            if (index.isNotEmpty()) index.append('\n')
            index.append(name).append('\t').append(start)
        }
        val output = outputDirectory.get().asFile
        output.deleteRecursively()
        val packageDirectory = File(output, "io/github/calendrum/tzdata")
        packageDirectory.mkdirs()
        val source = StringBuilder("package io.github.calendrum.tzdata\n\n")
        source.append("internal const val TZDB_VERSION: String = \"").append(version).append("\"\n\n")
        source.append(
            "internal const val TZDB_EXPLICIT_UNTIL_YEAR: Int = ",
        ).append(EXPLICIT_UNTIL_YEAR + 1).append("\n\n")
        appendChunks(source, "ZONE_INDEX", index.toString(), "zoneIndex")
        appendChunks(source, "ABBREVIATIONS", abbreviations.keys.joinToString(","), "abbreviationTable")
        appendChunks(source, "ZONE_DATA", data.toString(), "zoneData")
        File(packageDirectory, "TzDataTables.kt").writeText(source.toString())
    }

    private fun encode(zone: CompiledZone, out: PackedWriter, abbreviations: MutableMap<String, Int>) {
        val epoch1970 = 0L
        var initial = Triple(zone.initialOffset, if (zone.initialSave != 0) 1 else 0, zone.initialAbbreviation)
        val kept = ArrayList<Transition>()
        for (transition in zone.transitions) {
            if (transition.epochSecond <= epoch1970) {
                initial = Triple(transition.offsetAfter, if (transition.save > 0) 1 else 0, transition.abbreviation)
            } else {
                kept += transition
            }
        }
        val offsets = LinkedHashMap<Triple<Int, Int, String>, Int>()
        fun offsetIndex(key: Triple<Int, Int, String>): Int = offsets.getOrPut(key) { offsets.size }
        val initialIndex = offsetIndex(initial)
        val transitionIndexes = kept.map {
            offsetIndex(Triple(it.offsetAfter, if (it.save > 0) 1 else 0, it.abbreviation))
        }
        val tailIndexes = zone.tail.map {
            offsetIndex(Triple(it.offsetAfter, if (it.save > 0) 1 else 0, it.abbreviation))
        }
        out.unsigned(offsets.size)
        for ((key, _) in offsets) {
            out.signed(key.first)
            out.unsigned(key.second)
            out.unsigned(abbreviations.getOrPut(key.third) { abbreviations.size })
        }
        out.unsigned(initialIndex)
        out.unsigned(kept.size)
        var previous = epoch1970
        for ((position, transition) in kept.withIndex()) {
            val delta = transition.epochSecond - previous
            if (delta % 60 == 0L) out.unsigned(delta / 60 * 2) else out.unsigned(delta * 2 + 1)
            out.unsigned(transitionIndexes[position])
            previous = transition.epochSecond
        }
        if (zone.tail.isEmpty()) {
            out.unsigned(0)
            return
        }
        out.unsigned(zone.tailStartYear)
        out.unsigned(zone.tail.size)
        for ((position, rule) in zone.tail.withIndex()) {
            out.unsigned(rule.day.month)
            out.unsigned(rule.day.mode)
            out.unsigned(rule.day.day)
            out.unsigned(rule.day.dayOfWeek?.let { it.value - 1 } ?: 7)
            out.signed(rule.time)
            out.unsigned(rule.definition.ordinal)
            out.signed(rule.standardOffset)
            out.signed(rule.offsetBefore)
            out.unsigned(tailIndexes[position])
        }
    }

    private fun resolveLink(tzdb: TzSource, name: String): String {
        var current = name
        repeat(8) { current = tzdb.links[current] ?: return current }
        return current
    }

    // Each chunk is a function returning a literal: concatenated const strings would be folded by the compiler into one
    // more copy of the whole table, and a JVM string constant holds at most 65535 bytes.
    private fun appendChunks(source: StringBuilder, name: String, text: String, function: String) {
        val chunks = text.chunked(CHUNK)
        chunks.forEachIndexed { index, chunk ->
            source.append("private fun ").append(function).append(index).append("(): String =\n    ")
                .append(literal(chunk)).append("\n\n")
        }
        source.append("internal fun ").append(function).append("(): String =\n    ")
        source.append(chunks.indices.joinToString(" +\n    ") { "$function$it()" }.ifEmpty { "\"\"" })
        source.append("\n\n")
    }

    private fun literal(text: String): String {
        val out = StringBuilder("\"")
        for (char in text) {
            when (char) {
                '\n' -> out.append("\\n")
                '\t' -> out.append("\\t")
                '"', '\\', '$' -> out.append('\\').append(char)
                else -> out.append(char)
            }
        }
        return out.append('"').toString()
    }

    companion object {
        const val EXPLICIT_UNTIL_YEAR: Int = 2037
        private const val CHUNK = 30_000
    }
}

class PackedWriter {
    private val out = StringBuilder()

    val length: Int get() = out.length

    fun unsigned(value: Long) {
        require(value >= 0) { "Negative value $value" }
        var rest = value
        while (true) {
            val digit = (rest and 31).toInt()
            rest = rest ushr 5
            if (rest == 0L) {
                out.append(ALPHABET[digit])
                return
            }
            out.append(ALPHABET[digit or 32])
        }
    }

    fun unsigned(value: Int) {
        unsigned(value.toLong())
    }

    fun signed(value: Int) {
        unsigned(if (value >= 0) value.toLong() * 2 else -value.toLong() * 2 - 1)
    }

    override fun toString(): String = out.toString()

    companion object {
        const val ALPHABET: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    }
}
