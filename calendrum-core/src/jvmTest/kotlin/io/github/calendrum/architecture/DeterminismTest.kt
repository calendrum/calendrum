package io.github.calendrum.architecture

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeterminismTest {
    private val root: File = File(System.getProperty("calendrum.spec")).parentFile

    private val forbidden = listOf(
        "TimeZone.currentSystemDefault",
        "Clock.System",
        "TimeZone.getDefault",
        "ZoneId.systemDefault",
        "Clock.systemDefaultZone",
        "System.currentTimeMillis",
        "Instant.now()",
        "LocalDateTime.now",
        "LocalDate.now",
    )

    @Test
    fun noImplicitTimeZoneOrClockInLibraryCode() {
        val modules = listOf("calendrum-core", "calendrum-tzdata")
        val sources = modules.flatMap { module ->
            File(root, "$module/src").listFiles().orEmpty().filter { it.name.endsWith("Main") }
                .flatMap { set -> set.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList() }
        }
        assertTrue(sources.size > 100, "found only ${sources.size} source files under $root")
        val findings = sources.flatMap { file ->
            file.readLines().withIndex()
                .filter { (_, line) -> forbidden.any { line.contains(it) } }
                .map { (index, line) -> "${file.relativeTo(root)}:${index + 1}: ${line.trim()}" }
        }
        assertEquals(emptyList(), findings)
    }
}
