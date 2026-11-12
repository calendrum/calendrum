package io.github.calendrum.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import java.io.File

// The reverse of GuidesSyncTest: rewrites the block under every `<!-- sample: name -->` of guides/*.md with the text of
// the `// region name` of a source file, its common indentation removed and the indentation of the fence added. The
// guides are edited in place, so the task is untracked and runs every time; it reads the sources itself, as the test
// does, so nothing is resolved at configuration time.
@UntrackedTask(because = "Rewrites the guides in place")
abstract class SyncGuides : DefaultTask() {
    @get:Internal
    abstract val root: DirectoryProperty

    @TaskAction
    fun sync() {
        val root = root.get().asFile
        val regions = collectRegions(root)
        val missing = ArrayList<String>()
        var changed = 0
        for (guide in guides(root)) {
            val lines = guide.readLines()
            val out = ArrayList<String>(lines.size)
            var index = 0
            while (index < lines.size) {
                val marker = MARKER.matchEntire(lines[index].trim())
                out += lines[index]
                if (marker == null || index + 1 >= lines.size || !lines[index + 1].trim().startsWith(FENCE)) {
                    index += 1
                    continue
                }
                val name = marker.groupValues[1]
                val end = closingFence(lines, index + 1, guide)
                val region = regions[name]
                out += lines[index + 1]
                if (region == null) {
                    missing += "${guide.name}:${index + 1}: no region named $name"
                    out.addAll(lines.subList(index + 2, end))
                } else {
                    val indent = lines[index + 1].takeWhile { it == ' ' }
                    out.addAll(normalize(region).map { if (it.isEmpty()) it else indent + it })
                }
                out += lines[end]
                index = end + 1
            }
            if (out != lines) {
                guide.writeText(out.joinToString("\n", postfix = "\n"))
                changed += 1
                logger.lifecycle("Updated ${guide.relativeTo(root)}")
            }
        }
        if (missing.isNotEmpty()) throw GradleException(missing.joinToString("\n"))
        logger.lifecycle("syncGuides: $changed guide(s) changed")
    }

    private fun collectRegions(root: File): Map<String, List<String>> {
        val found = HashMap<String, List<String>>()
        val files = root.walkTopDown().onEnter { it.name !in SKIPPED }.filter { it.isFile && it.extension in SOURCES }
        for (file in files) {
            val lines = file.readLines()
            var index = 0
            while (index < lines.size) {
                val start = REGION.matchEntire(lines[index].trim())
                if (start == null) {
                    index += 1
                    continue
                }
                var end = index + 1
                while (end < lines.size && lines[end].trim() != "// endregion") end += 1
                if (end >=
                    lines.size
                ) {
                    throw GradleException("The region ${start.groupValues[1]} in $file is not closed")
                }
                if (found.put(start.groupValues[1], lines.subList(index + 1, end)) != null) {
                    throw GradleException("The region ${start.groupValues[1]} is defined twice")
                }
                index = end + 1
            }
        }
        return found
    }

    private companion object {
        val MARKER = Regex("<!-- sample: ([a-z0-9-]+) -->")
        val REGION = Regex("// region ([a-z0-9-]+)")
        const val FENCE = "```"
        val SOURCES = setOf("kt", "kts", "java", "swift")
        val SKIPPED = setOf("build", "bin", ".gradle", ".git", ".kotlin", "node_modules", "docs", "kotlin-js-store")
    }
}

// Rewrites the rows of the tables in guides/diagnostics.md from the KDoc of the ProblemCode constants: severity,
// constant and meaning. The KDoc starts with "`ICAL-X000` (Severity): text"; a link `[name]` becomes `name` in
// backticks (a Markdown link `[text](url)` stays), and the first letter is capitalized unless the word is mixed case
// (`jCal`). A code without a row, or a row without a constant, fails the task: placing a new row is a human decision.
@UntrackedTask(because = "Rewrites the diagnostics guide in place")
abstract class SyncDiagnosticsGuide : DefaultTask() {
    @get:Internal
    abstract val root: DirectoryProperty

    @TaskAction
    fun sync() {
        val root = root.get().asFile
        val source = root.resolve("calendrum-core/src/commonMain/kotlin/io/github/calendrum/ProblemCode.kt").readText()
        val codes = LinkedHashMap<String, Triple<String, String, String>>()
        for (match in CONSTANT.findAll(source)) {
            val text = match.groupValues[1].lines().joinToString(" ") { it.trim().removePrefix("*").trim() }.trim()
            val head = HEAD.matchEntire(text) ?: continue
            codes[head.groupValues[1]] = Triple(head.groupValues[2], match.groupValues[2], meaning(head.groupValues[3]))
        }
        val guide = root.resolve("guides/diagnostics.md")
        val lines = guide.readLines()
        val seen = HashSet<String>()
        val problems = ArrayList<String>()
        val out = lines.map { line ->
            val row = ROW.matchEntire(line) ?: return@map line
            val code = row.groupValues[1]
            val (severity, constant, meaning) = codes[code] ?: run {
                problems += "guides/diagnostics.md: no ProblemCode constant for $code"
                return@map line
            }
            seen += code
            "| `$code` | $severity | `ProblemCode.$constant` | $meaning |"
        }
        problems +=
            codes.keys.filter { it !in seen }.map { "guides/diagnostics.md: no row for $it; add it to its table" }
        if (problems.isNotEmpty()) throw GradleException(problems.joinToString("\n"))
        if (out != lines) {
            guide.writeText(out.joinToString("\n", postfix = "\n"))
            logger.lifecycle("Updated guides/diagnostics.md")
        } else {
            logger.lifecycle("syncDiagnosticsGuide: guides/diagnostics.md is up to date")
        }
    }

    private fun meaning(text: String): String {
        val linked = LINK.replace(text) { "`${it.groupValues[1]}`" }
        val word = linked.substringBefore(' ')
        val mixed = word.drop(1).any { it.isUpperCase() }
        return if (mixed || linked.isEmpty()) linked else linked[0].uppercaseChar() + linked.substring(1)
    }

    private companion object {
        val CONSTANT =
            Regex(
                """/\*\*((?:(?!\*/).)*?)\*/\s*@JvmField\s*public val (\w+): ProblemCode""",
                RegexOption.DOT_MATCHES_ALL,
            )
        val HEAD = Regex("""`(ICAL-\w+)` \((\w+)\): (.*)""")
        val LINK = Regex("""\[([^\]]+)\](?!\()""")
        val ROW = Regex("""\| `(ICAL-\w+)` \| \w+ \| `ProblemCode\.\w+` \| .* \|""")
    }
}

private fun guides(root: File): List<File> =
    root.resolve("guides").listFiles { file -> file.extension == "md" }.orEmpty().sortedBy { it.name }

private fun closingFence(lines: List<String>, open: Int, file: File): Int {
    var end = open + 1
    while (end < lines.size && lines[end].trim() != "```") end += 1
    if (end >= lines.size) throw GradleException("${file.name}: the block at line ${open + 1} is not closed")
    return end
}

// The region without the common indentation and the blank lines around it, as GuidesSyncTest compares.
private fun normalize(lines: List<String>): List<String> {
    val trimmed = lines.map { it.trimEnd() }
    val indent = trimmed.filter { it.isNotEmpty() }.minOfOrNull { it.length - it.trimStart().length } ?: 0
    return trimmed.map { if (it.isEmpty()) it else it.substring(indent) }
        .dropWhile { it.isEmpty() }
        .dropLastWhile { it.isEmpty() }
}
