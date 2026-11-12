package io.github.calendrum.buildlogic.tz

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
abstract class TimeZoneAliasSources : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val tzdbDirectory: DirectoryProperty

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val windowsZones: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    // Writes the compact alias tables: zone names, then link names, both sorted and front-coded; link targets as
    // indexes of zones (a link to a link is resolved here); Windows ids and display names with the index of their
    // target. A name the format cannot hold fails the build.
    @TaskAction
    fun generate() {
        val tzdb = Tzdb.read(tzdbDirectory.get().asFile)
        val zones = tzdb.zones.keys.sorted()
        val links = tzdb.links.entries.sortedBy { it.key }.map { (link, target) -> link to resolveLink(tzdb, target) }
        val windows = ArrayList<Pair<String, String>>()
        val display = ArrayList<Pair<String, String>>()
        var comment: String? = null
        for (raw in windowsZones.get().asFile.readLines()) {
            val line = raw.trim()
            if (line.startsWith("<!--") && line.contains("(UTC")) {
                comment = line.removePrefix("<!--").removeSuffix("-->").trim()
                continue
            }
            if (!line.startsWith("<mapZone") || !line.contains("territory=\"001\"")) continue
            val other = attribute(line, "other")
            val type = attribute(line, "type").substringBefore(' ')
            windows += other to type
            val name = comment
            if (name != null) display += normalizeDisplayName(name) to type
            comment = null
        }
        val version = File(tzdbDirectory.get().asFile, "version").readText().trim()
        val names = zones + links.map { it.first }
        val index = HashMap<String, Int>()
        names.forEachIndexed { position, name -> index.putIfAbsent(name, position) }
        check(names.size <= ALPHABET.length * ALPHABET.length) { "Too many time zone names: ${names.size}" }
        fun target(name: String): String {
            val position = index[name] ?: error("Not a tzdb zone or link: $name")
            return code(position)
        }
        val linkTargets = StringBuilder()
        for ((_, target) in links) {
            val position = index[target] ?: error("Link target is not a zone: $target")
            check(position < zones.size) { "Link target is a link: $target" }
            linkTargets.append(code(position))
        }
        val output = outputDirectory.get().asFile
        output.deleteRecursively()
        val directory = File(output, "io/github/calendrum/tz")
        directory.mkdirs()
        val source = StringBuilder("package io.github.calendrum.tz\n\n")
        source.append("internal const val ALIAS_TZDB_VERSION: String = \"").append(version).append("\"\n\n")
        source.append("internal const val ALIAS_ZONE_COUNT: Int = ").append(zones.size).append("\n\n")
        appendTable(source, "ALIAS_NAMES", frontCoded(zones) + "\n" + frontCoded(links.map { it.first }))
        appendTable(source, "ALIAS_LINKS", linkTargets.toString())
        appendTable(
            source,
            "ALIAS_WINDOWS",
            windows.distinct().joinToString("") { windowsName(it.first) + target(it.second) + "\n" },
        )
        appendTable(
            source,
            "ALIAS_DISPLAY",
            display.distinct().joinToString("") {
                it.first + target(it.second) + "\n"
            },
        )
        File(directory, "TimeZoneAliasData.kt").writeText(source.toString())
    }

    private fun frontCoded(sorted: List<String>): String {
        val out = StringBuilder()
        var previous = ""
        for ((position, name) in sorted.withIndex()) {
            var shared = 0
            val limit = minOf(previous.length, name.length, ALPHABET.length - 1)
            while (shared < limit && previous[shared] == name[shared]) shared++
            if (position > 0) out.append('\n')
            out.append(ALPHABET[shared]).append(name, shared, name.length)
            previous = name
        }
        return out.toString()
    }

    private fun windowsName(name: String): String {
        check(!name.contains(STANDARD_MARK) && !name.contains('\n')) { "Unexpected Windows zone name: $name" }
        return if (name.endsWith(STANDARD_TIME)) name.removeSuffix(STANDARD_TIME) + STANDARD_MARK else name
    }

    private fun code(position: Int): String =
        "${ALPHABET[position / ALPHABET.length]}${ALPHABET[position % ALPHABET.length]}"

    private fun resolveLink(tzdb: TzSource, target: String): String {
        var current = target
        repeat(8) { current = tzdb.links[current] ?: return current }
        return current
    }

    private fun attribute(line: String, name: String): String = line.substringAfter("$name=\"").substringBefore('"')

    private fun appendTable(source: StringBuilder, name: String, text: String) {
        val function = name.lowercase().split('_').mapIndexed { index, part ->
            if (index == 0) part else part.replaceFirstChar { it.uppercase() }
        }.joinToString("")
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

                '"' -> out.append("\\\"")

                '\\' -> out.append("\\\\")

                '$' -> out.append("\\$")

                else -> if (char.code < 0x20 || char.code > 0x7E) {
                    out.append("\\u").append(char.code.toString(16).padStart(4, '0'))
                } else {
                    out.append(char)
                }
            }
        }
        return out.append('"').toString()
    }

    companion object {
        private const val CHUNK = 16_000
        private const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
        private const val STANDARD_TIME = " Standard Time"
        private const val STANDARD_MARK = '*'

        fun normalizeDisplayName(name: String): String {
            var text = name.trim().lowercase()
            if (text.startsWith("(")) text = text.substringAfter(')')
            val out = StringBuilder()
            var pendingComma = false
            var pendingSpace = false
            for (char in text) {
                when {
                    char == ',' || char == '/' -> pendingComma = true

                    char.isWhitespace() -> pendingSpace = true

                    else -> {
                        if (pendingComma && out.isNotEmpty()) {
                            out.append(',')
                        } else if (pendingSpace && out.isNotEmpty()) {
                            out.append(' ')
                        }
                        pendingComma = false
                        pendingSpace = false
                        out.append(char)
                    }
                }
            }
            return out.toString()
        }
    }
}
