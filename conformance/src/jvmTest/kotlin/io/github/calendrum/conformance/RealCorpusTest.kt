package io.github.calendrum.conformance

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ICalComponent
import org.junit.Assume.assumeTrue
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class RealCorpusTest {
    private val root = RealCorpus.root()
    private val calendars = RealCorpus.calendars(root)

    @BeforeTest
    fun theCorpusIsHere() {
        assumeTrue("corpus/real/ is not in this checkout: it stays with the maintainer", RealCorpus.isPresent(root))
    }

    // A file that goes into the repository must hold nothing cut from the corpus: such a file is named real-*, which
    // .gitignore keeps out. Paths and counts only, never a value.
    @Test
    fun nothingFromTheCorpusIsPublic() {
        val marks = RealCorpus.marks(calendars)
        val repository = root.parentFile
        val failures = RealCorpus.publicFiles(repository).mapNotNull { file ->
            val count = marks.count(file.readBytes().decodeToString())
            if (count ==
                0
            ) {
                null
            } else {
                "${file.relativeTo(repository).invariantSeparatorsPath}: $count marks of the real corpus"
            }
        }
        report("pieces of it are in files that go into the repository; rename them to real-*", failures)
    }

    @Test
    fun layoutIsComplete() {
        assertTrue(calendars.size >= MIN_FILES, "The real corpus has only ${calendars.size} files under $root")
        val failures = ArrayList<String>()
        val schema = RealCorpus.schema(root, META_SCHEMA)
        for (source in RealCorpus.sources(root)) {
            val names = calendars.filter { it.parentFile == source }.map { it.name.removeSuffix(".ics") }
            for (file in source.listFiles().orEmpty().sortedBy { it.name }) {
                val known = names.any {
                    file.name == "$it.ics" || file.name == it + RealCorpus.META_SUFFIX ||
                        file.name == it + RealCorpus.PROBLEMS_SUFFIX
                }
                if (!known) failures += "${relative(file)}: unexpected entry"
            }
        }
        for (calendar in calendars) {
            val name = relative(calendar)
            if (!RealCorpus.expectedProblems(calendar).isFile) failures += "$name: no ${RealCorpus.PROBLEMS_SUFFIX}"
            val metaFile = RealCorpus.meta(calendar)
            if (!metaFile.isFile) {
                failures += "$name: no ${RealCorpus.META_SUFFIX}"
                continue
            }
            val json = readJson(metaFile.readBytes())
            failures += json.problems.map { "${relative(metaFile)}: $it" }
            val meta = json.value as? JsonObject ?: continue
            failures += schema.validate(meta).map { "${relative(metaFile)}: $it" }
            if (meta.string("file") != calendar.name) failures += "$name: the manifest names another file"
            if (meta.string("source") != calendar.parentFile.name) failures += "$name: the source is not the directory"
            val endings = lineEndings(calendar.readBytes())
            if (meta.string("lineEndings") != endings) failures += "$name: the line endings are $endings"
        }
        report("the real corpus is incomplete", failures)
    }

    @Test
    fun problemsMatchTheExpectedOnes() {
        val failures = ArrayList<String>()
        val schema = RealCorpus.schema(root, PROBLEMS_SCHEMA)
        for (calendar in calendars) {
            val expectedFile = RealCorpus.expectedProblems(calendar)
            if (!expectedFile.isFile) continue
            val expected = readJson(expectedFile.readBytes())
            failures += expected.problems.map { "${relative(expectedFile)}: $it" }
            expected.value?.let { value -> failures += schema.validate(value).map { "${relative(expectedFile)}: $it" } }
            val actual = RealCorpus.problemsJson(ICal.parse(calendar.readBytes()))
            val expectedText = expectedFile.readBytes().decodeToString()
            val actualText = actual.toCanonicalJson()
            if (expectedText == actualText) continue
            failures += "${relative(expectedFile)}:"
            val differences = expected.value?.let { jsonDifferences(it, actual) }.orEmpty()
            failures += differences.ifEmpty { listOf(textDifference(expectedText, actualText)) }.map { "  $it" }
        }
        if (failures.isNotEmpty()) {
            fail(
                "Real corpus: problems differ from the expected ones:\n" + failures.joinToString("\n") + "\n" +
                    "Fix the parser, or, only if the new output is correct, run ${RealCorpus.UPDATE_COMMAND} " +
                    "and review the changes (corpus/README.md).",
            )
        }
    }

    @Test
    fun charsetAndEventsMatchTheManifest() {
        val failures = ArrayList<String>()
        for (calendar in calendars) {
            val meta = RealCorpus.meta(calendar).takeIf { it.isFile }
                ?.let { readJson(it.readBytes()).value } as? JsonObject ?: continue
            val name = relative(calendar)
            val result = ICal.parse(calendar.readBytes())
            val charset = meta.string("charset")
            if (result.charset != charset) failures += "$name: read as ${result.charset}, exported in $charset"
            if (result.charset in SINGLE_BYTE) {
                if (result.charsetConfidence < MIN_CONFIDENCE) {
                    failures += "$name: ${result.charset} with confidence ${result.charsetConfidence}%"
                }
                if (result.problemCount(ProblemCode.LowCharsetConfidence) > 0) failures += "$name: low confidence"
            }
            val events = countComponents(result.components, EVENT)
            val expected = (meta["events"] as? JsonInteger)?.value?.toInt()
            if (events != expected) failures += "$name: $events $EVENT, the manifest says $expected"
        }
        report("the parse differs from the manifest", failures)
    }

    @Test
    fun roundTripIsLossless() {
        val failures = calendars.flatMap { calendar ->
            roundTripFailures(calendar.readBytes(), ICal.parser()).map { "${relative(calendar)}: $it" }
        }
        report("files do not round-trip", failures)
    }

    private fun relative(file: File): String = file.relativeTo(root).invariantSeparatorsPath

    private fun JsonObject.string(key: String): String? = (this[key] as? JsonString)?.value

    private fun lineEndings(bytes: ByteArray): String {
        var crlf = 0
        var cr = 0
        var lf = 0
        for (index in bytes.indices) {
            when (bytes[index]) {
                CR -> if (index + 1 < bytes.size && bytes[index + 1] == LF) crlf++ else cr++
                LF -> if (index == 0 || bytes[index - 1] != CR) lf++
            }
        }
        val kinds = listOf("CRLF" to crlf, "CR" to cr, "LF" to lf).filter { it.second > 0 }
        return kinds.singleOrNull()?.first ?: if (kinds.isEmpty()) "CRLF" else "mixed"
    }

    private fun countComponents(roots: List<ICalComponent>, name: String): Int {
        var count = 0
        val stack = ArrayList(roots)
        while (stack.isNotEmpty()) {
            val component = stack.removeAt(stack.lastIndex)
            if (component.name == name) count++
            stack.addAll(component.components)
        }
        return count
    }

    private fun report(title: String, failures: List<String>) {
        if (failures.isNotEmpty()) fail("Real corpus: $title:\n" + failures.joinToString("\n"))
    }

    private companion object {
        const val MIN_FILES = 1
        const val MIN_CONFIDENCE = 90
        const val META_SCHEMA = "meta.schema.json"
        const val PROBLEMS_SCHEMA = "expected-problems.schema.json"
        const val EVENT = "VEVENT"
        const val CR: Byte = 13
        const val LF: Byte = 10
        val SINGLE_BYTE = setOf("windows-1251", "KOI8-R", "windows-1252")
    }
}
