package io.github.calendrum.guides

import io.github.calendrum.ProblemCode
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Keeps the guides honest: every Kotlin, Java and Swift fragment in the Markdown files of `guides/` is the text of a
 * `// region` of a source file that the build compiles (and, for the tests, runs). A fragment is marked by
 * `<!-- sample: name -->` on the line above its fence, and it must equal the region character for character after the
 * common indentation is removed.
 */
class GuidesSyncTest {
    private val root = findRoot()
    private val guides = File(root, "guides").listFiles { file -> file.extension == "md" }.orEmpty()
        .sortedBy { it.name }

    private class Region(val file: File, val lines: List<String>)

    private val regions: Map<String, Region> by lazy { collectRegions() }

    @Test
    fun everyFragmentOfAGuideIsCompiledCode() {
        val failures = ArrayList<String>()
        for (guide in guides) {
            val lines = guide.readLines()
            var index = 0
            while (index < lines.size) {
                val line = lines[index]
                val marker = MARKER.matchEntire(line.trim())
                val fence = FENCE.matchEntire(line.trim())
                if (marker != null) {
                    val name = marker.groupValues[1]
                    val open = index + 1
                    if (open >= lines.size || FENCE.matchEntire(lines[open].trim()) == null) {
                        failures += "${guide.name}:${index + 1}: sample $name is not followed by a fenced block"
                        index += 1
                        continue
                    }
                    val end = closingFence(lines, open)
                    val region = regions[name]
                    if (region == null) {
                        failures += "${guide.name}:${index + 1}: no region named $name"
                    } else if (normalize(lines.subList(open + 1, end)) != normalize(region.lines)) {
                        val where = "${guide.name}:${index + 1}"
                        failures += "$where: the block differs from region $name in ${region.file.name}"
                    }
                    index = end + 1
                } else if (fence != null) {
                    val end = closingFence(lines, index)
                    val language = fence.groupValues[1]
                    if (language in COMPILED) {
                        failures += "${guide.name}:${index + 1}: a $language block without a sample marker"
                    } else if (language !in UNCHECKED) {
                        failures += "${guide.name}:${index + 1}: unknown code language '$language'"
                    }
                    index = end + 1
                } else {
                    index += 1
                }
            }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    @Test
    fun theGuidesExist() {
        assertEquals(EXPECTED_GUIDES, guides.map { it.name }.toSet())
    }

    @Test
    fun relativeLinksPointAtExistingFiles() {
        val failures = ArrayList<String>()
        for (file in guides + File(root, "README.md") + File(root, "MIGRATION.md")) {
            for (match in LINK.findAll(file.readText())) {
                val target = match.groupValues[1].substringBefore('#').substringBefore('?')
                if (target.isEmpty() || target.contains("://") || target.startsWith("mailto:")) continue
                if (!File(file.parentFile, target).exists()) failures += "${file.name}: $target"
            }
        }
        assertTrue(failures.isEmpty(), failures.joinToString("\n"))
    }

    @Test
    fun theReadmeLinksEveryGuide() {
        val readme = File(root, "README.md").readText()
        val missing = guides.filter { "guides/${it.name}" !in readme }.map { it.name }
        assertTrue(missing.isEmpty(), "README.md does not link: $missing")
    }

    @Test
    fun theDiagnosticsGuideListsEveryProblemCode() {
        val text = File(root, "guides/diagnostics.md").readText()
        val missing = ProblemCode.known.map { it.id }.filter { "`$it`" !in text }
        assertTrue(missing.isEmpty(), "guides/diagnostics.md lacks $missing")
    }

    private fun closingFence(lines: List<String>, open: Int): Int {
        var end = open + 1
        while (end < lines.size && lines[end].trim() != "```") end += 1
        if (end >= lines.size) fail("A fenced block that starts at line ${open + 1} is not closed")
        return end
    }

    private fun normalize(lines: List<String>): List<String> {
        val trimmed = lines.map { it.trimEnd() }
        val indent = trimmed.filter { it.isNotEmpty() }.minOfOrNull { it.length - it.trimStart().length } ?: 0
        return trimmed.map { if (it.isEmpty()) it else it.substring(indent) }
            .dropWhile { it.isEmpty() }
            .dropLastWhile { it.isEmpty() }
    }

    private fun collectRegions(): Map<String, Region> {
        val found = HashMap<String, Region>()
        for (file in sources(root)) {
            val lines = file.readLines()
            var index = 0
            while (index < lines.size) {
                val start = REGION.matchEntire(lines[index].trim())
                if (start == null) {
                    index += 1
                    continue
                }
                val name = start.groupValues[1]
                var end = index + 1
                while (end < lines.size && lines[end].trim() != "// endregion") end += 1
                if (end >= lines.size) fail("The region $name in ${file.path} is not closed")
                if (name in found) {
                    fail("The region $name is defined twice: ${found.getValue(name).file.path} and ${file.path}")
                }
                found[name] = Region(file, lines.subList(index + 1, end))
                index = end + 1
            }
        }
        return found
    }

    private fun sources(directory: File): Sequence<File> = directory.walkTopDown()
        .onEnter { it.name !in SKIPPED_DIRECTORIES }
        .filter { it.isFile && it.extension in SOURCE_EXTENSIONS }

    private fun findRoot(): File {
        var directory: File? = File(System.getProperty("user.dir")).absoluteFile
        while (directory != null) {
            if (File(directory, "settings.gradle.kts").isFile && File(directory, "guides").isDirectory) return directory
            directory = directory.parentFile
        }
        fail("No repository root with a guides directory above ${System.getProperty("user.dir")}")
    }

    private companion object {
        val MARKER = Regex("<!-- sample: ([a-z0-9-]+) -->")
        val FENCE = Regex("```([A-Za-z0-9+-]*)")
        val REGION = Regex("// region ([a-z0-9-]+)")
        val LINK = Regex("\\]\\(([^)\\s]+)\\)")
        val COMPILED = setOf("kotlin", "java", "swift")
        val UNCHECKED = setOf(
            "",
            "bash",
            "text",
            "kts",
            "toml",
            "xml",
            "json",
            "ics",
            "properties",
            "yaml",
            "c",
            "python",
            "typescript",
            "csharp",
        )
        val SOURCE_EXTENSIONS = setOf("kt", "kts", "java", "swift")
        val SKIPPED_DIRECTORIES =
            setOf("build", "bin", ".gradle", ".git", ".kotlin", "node_modules", "docs", "kotlin-js-store")
        val EXPECTED_GUIDES = setOf(
            "getting-started.md",
            "parsing.md",
            "model.md",
            "building-and-editing.md",
            "selective-parsing-and-schema.md",
            "recurrence-and-occurrences.md",
            "time-zones.md",
            "validation-itip-fixups.md",
            "integrations.md",
            "java.md",
            "swift.md",
            "compatibility.md",
            "diagnostics.md",
            "performance.md",
            "mcp.md",
            "cli.md",
            "c-api.md",
            "python.md",
            "typescript.md",
            "dotnet.md",
        )
    }
}
