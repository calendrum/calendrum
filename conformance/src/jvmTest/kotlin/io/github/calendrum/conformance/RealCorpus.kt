package io.github.calendrum.conformance

import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ParseResult
import java.io.File

internal object RealCorpus {
    const val META_SUFFIX = ".meta.json"
    const val PROBLEMS_SUFFIX = ".expected-problems.json"
    const val CALENDAR_SUFFIX = ".ics"
    const val ROOT_PROPERTY = "calendrum.corpus"
    const val UPDATE_COMMAND = "./gradlew :conformance:updateExpected"

    fun root(): File = File(
        System.getProperty(ROOT_PROPERTY) ?: throw IllegalStateException("System property $ROOT_PROPERTY is not set"),
    )

    // corpus/real/ stays with the maintainer (.gitignore): a checkout without it skips the tests of the corpus.
    fun isPresent(root: File): Boolean = calendars(root).isNotEmpty()

    fun sources(root: File): List<File> = File(root, "real").listFiles().orEmpty()
        .filter { it.isDirectory && !it.name.startsWith(".") }
        .sortedBy { it.name }

    fun calendars(root: File): List<File> = sources(root).flatMap { source ->
        source.listFiles().orEmpty().filter { it.isFile && it.name.endsWith(CALENDAR_SUFFIX) }.sortedBy { it.name }
    }

    fun meta(calendar: File): File = sibling(calendar, META_SUFFIX)

    fun expectedProblems(calendar: File): File = sibling(calendar, PROBLEMS_SUFFIX)

    fun schema(root: File, name: String): JsonSchema {
        val json = readJson(File(root, "schema/$name").readBytes())
        check(json.problems.isEmpty()) { "schema/$name: ${json.problems}" }
        return JsonSchema(json.value as JsonObject)
    }

    // Counts come from `ParseResult.problemCount` for `ICAL-S001`…`ICAL-S029`, so they include problems beyond the
    // reporting limit and repeats of once-only codes, which the `problems` list does not show.
    fun problemsJson(result: ParseResult): Json = jsonObject(
        "charset" to JsonString(result.charset),
        "charsetConfidence" to JsonInteger(result.charsetConfidence.toLong()),
        "truncated" to JsonBoolean(result.isTruncated),
        "counts" to JsonObject(
            syntaxCodes.mapNotNull { code ->
                val count = result.problemCount(code)
                if (count == 0) null else code.id to JsonInteger(count.toLong())
            },
        ),
        "problems" to diagnosticsJson(result.problems),
    )

    /**
     * What gives the corpus away: its Cyrillic words of five letters and more, its long content lines, its addresses,
     * which include every UID (the anonymizer ends them with a domain), and its values of 20 characters and more, found
     * anywhere in a text, as in a string of a test. Time zone definitions and product lines are left out: their rules
     * are the same in every calendar.
     */
    class Marks(
        private val words: Set<String>,
        private val lines: Set<String>,
        private val addresses: Set<String>,
        private val values: Map<String, List<String>>,
    ) {
        fun count(text: String): Int = wordsOf(text).count { it in words } +
            text.lineSequence().count { it in lines } +
            addressesOf(text).count { it in addresses } +
            valuesIn(text)

        private fun valuesIn(text: String): Int {
            val found = HashSet<String>()
            for (index in 0..text.length - LONG_VALUE) {
                val candidates = values[text.substring(index, index + LONG_VALUE)] ?: continue
                candidates.filterTo(found) { text.startsWith(it, index) }
            }
            return found.size
        }
    }

    fun marks(calendars: List<File>): Marks {
        val words = HashSet<String>()
        val lines = HashSet<String>()
        val addresses = HashSet<String>()
        val values = HashMap<String, MutableList<String>>()
        for (calendar in calendars) {
            var zone = 0
            for (line in calendar.readText().replace(FOLD, "").lineSequence()) {
                if (line.equals("BEGIN:VTIMEZONE", ignoreCase = true)) zone++
                if (line.equals("END:VTIMEZONE", ignoreCase = true)) zone--
                if (zone > 0 || STRUCTURAL.any { line.startsWith(it, ignoreCase = true) }) continue
                words += wordsOf(line)
                addresses += addressesOf(line)
                if (line.length >= LONG_LINE) lines += line
                val value = line.substringAfter(':', "")
                if (value.length >= LONG_VALUE) values.getOrPut(value.substring(0, LONG_VALUE)) { ArrayList() } += value
            }
        }
        return Marks(words, lines, addresses, values)
    }

    private fun wordsOf(text: String): Set<String> = WORD.findAll(text).mapTo(HashSet()) { it.value }

    private fun addressesOf(text: String): Set<String> = ADDRESS.findAll(text).mapTo(HashSet()) { it.value.lowercase() }

    /**
     * Every text file that goes into the repository: what .gitignore keeps out (build output, the private docs, files
     * named `real-*` or `seed-real-*`, corpus/real/) and third-party data are skipped.
     */
    fun publicFiles(repository: File): List<File> {
        val real = File(repository, "corpus/real")
        return repository.walkTopDown()
            .onEnter { it == repository || (it != real && isPublicDirectory(it.name)) }
            .filter { it.isFile && !isPrivate(it.name) && it.length() <= MAX_FILE && isText(it) }
            .toList()
    }

    private fun isPublicDirectory(name: String): Boolean =
        name !in SKIPPED && !isPrivate(name) && (!name.startsWith(".") || name == ".github")

    private fun isText(file: File): Boolean =
        file.inputStream().use { input -> input.readNBytes(BINARY_PROBE).none { it == 0.toByte() } }

    fun isPrivate(name: String): Boolean = name.startsWith("real-") || name.startsWith("seed-real-")

    private fun sibling(calendar: File, suffix: String): File =
        File(calendar.parentFile, calendar.name.removeSuffix(CALENDAR_SUFFIX) + suffix)

    private val syntaxCodes: List<ProblemCode> = (1..SYNTAX_CODES).map {
        ProblemCode.of("ICAL-S" + it.toString().padStart(CODE_DIGITS, '0'))
    }

    private const val SYNTAX_CODES = 29
    private const val CODE_DIGITS = 3
    private const val LONG_LINE = 40
    private const val LONG_VALUE = 20
    private const val MAX_FILE = 16L * 1024 * 1024
    private const val BINARY_PROBE = 4096
    private val WORD = Regex("[\\u0400-\\u04FF]{5,}")
    private val FOLD = Regex("\\r?\\n[ \\t]")
    private val ADDRESS = Regex("[A-Za-z0-9._%+-]{6,}@[A-Za-z0-9.-]+")
    private val STRUCTURAL =
        listOf("PRODID", "TZID", "X-WR-TIMEZONE", "X-LIC-LOCATION", "BEGIN", "END", "VERSION", "CALSCALE", "METHOD")
    private val SKIPPED = setOf("build", "docs", "node_modules", "bin", "obj", "third_party", "kotlin-js-store")
}
