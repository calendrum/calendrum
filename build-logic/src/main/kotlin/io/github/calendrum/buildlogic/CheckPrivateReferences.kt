package io.github.calendrum.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

// The maintainers keep their design documents, decision log and task notes outside the repository, so a file that goes
// into it must not point at them: a reader has nothing to follow. No `docs/` path, no decision number (`D-12`), no
// milestone or task id (`M9-REVIEW`, `P-004`), no design document with a section (`ARCHITECTURE §4`). A comment says
// why in its own words instead.
@CacheableTask
abstract class CheckPrivateReferences : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: ConfigurableFileCollection

    @get:Internal
    abstract val root: DirectoryProperty

    @get:OutputFile
    abstract val report: RegularFileProperty

    @TaskAction
    fun check() {
        val root = root.get().asFile
        val failures = ArrayList<String>()
        for (file in sources.files.sorted()) {
            val path = file.relativeTo(root).invariantSeparatorsPath
            if (path in EXEMPT) continue
            val bytes = file.readBytes()
            if (bytes.take(BINARY_PROBE).any { it == 0.toByte() }) continue
            bytes.decodeToString().lineSequence().forEachIndexed { index, line ->
                if (ALLOWED_LINES.any { it in line }) return@forEachIndexed
                PATTERNS.firstNotNullOfOrNull { it.find(line) }?.let { failures += "$path:${index + 1}: ${it.value}" }
            }
        }
        report.get().asFile.writeText(failures.joinToString("\n", postfix = if (failures.isEmpty()) "" else "\n"))
        if (failures.isNotEmpty()) {
            throw GradleException(
                "These lines point at the private design documents, decisions or tasks; say why in words instead:\n" +
                    failures.joinToString("\n"),
            )
        }
    }

    private companion object {
        const val BINARY_PROBE = 4096
        val PATTERNS = listOf(
            Regex("(?<![A-Za-z0-9])docs/"),
            Regex("(?<![0-9A-Za-z-])D-\\d{2,3}\\b"),
            Regex("(?<![A-Za-z0-9.-])M\\d{1,2}-[A-Z0-9]{1,10}\\b"),
            Regex("(?<![A-Za-z0-9-])[PO]-0\\d{1,2}\\b"),
            Regex("\\b(M-BENCH|ROADMAP)\\b|\\bmilestone M\\d\\b"),
            Regex(
                "\\b(ARCHITECTURE|BENCHMARKS|BUDGETS|COMPATIBILITY|CORPUS|DECISIONS|DEVELOPMENT|DIAGNOSTICS|FEATURES|" +
                    "OPERATIONS|PLATFORMS|RESEARCH|STANDARDS|ICAL4J-LESSONS|CODE_COMMENTS|SDK)(\\.md\\b| §)",
            ),
        )

        // The ignore rules themselves, the excludes of this check, the path filters of the (commented) workflow
        // triggers, and the checks that look for the same references in a guide.
        val EXEMPT = setOf(
            ".gitignore",
            "build-logic/src/main/kotlin/io/github/calendrum/buildlogic/CheckPrivateReferences.kt",
        )
        val ALLOWED_LINES = listOf("'docs/**'", "\"docs/**\"", "r\"docs/|D-\\d+|P-\\d+\"")
    }
}
