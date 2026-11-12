package io.github.calendrum.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

// Embeds `conformance/ical/**` and `conformance/schema/**` into generated Kotlin test sources at build time, so the
// suite reads no files at test run time and behaves identically on every platform.
@CacheableTask
abstract class ConformanceSources : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val casesDirectory: DirectoryProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val schemaDirectory: DirectoryProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val output = outputDirectory.get().asFile
        output.deleteRecursively()
        val packageDirectory = File(output, packageName.get().replace('.', '/'))
        packageDirectory.mkdirs()
        val cases = visible(casesDirectory.get().asFile).filter { it.isDirectory }
        val chunks = cases.chunked(CASES_PER_FILE)
        chunks.forEachIndexed { chunk, directories ->
            val source = StringBuilder(header())
            source.append("internal fun cases$chunk(): List<EmbeddedCase> = listOf(\n")
            directories.indices.forEach { source.append("    case${chunk * CASES_PER_FILE + it}(),\n") }
            source.append(")\n")
            directories.forEachIndexed { index, directory ->
                source.append("\nprivate fun case${chunk * CASES_PER_FILE + index}(): EmbeddedCase = EmbeddedCase(\n")
                source.append("    ").append(literal(directory.name)).append(",\n")
                appendFiles(source, visible(directory), "    ")
                source.append(")\n")
            }
            File(packageDirectory, "EmbeddedCases$chunk.kt").writeText(source.toString())
        }
        val index = StringBuilder(header())
        index.append("internal fun embeddedCases(): List<EmbeddedCase> = listOf<EmbeddedCase>()")
        chunks.indices.forEach { index.append(" +\n    cases$it()") }
        index.append("\n\ninternal fun embeddedSchemas(): List<EmbeddedFile> =\n")
        appendFiles(index, visible(schemaDirectory.get().asFile).filter { it.isFile }, "", trailingComma = false)
        File(packageDirectory, "EmbeddedSuite.kt").writeText(index.toString())
    }

    private fun header(): String = "package ${packageName.get()}\n\n"

    // `embeddedSchemas()` returns the `listOf(...)` call directly, so its files must have no trailing comma; the files
    // argument of `EmbeddedCase` is one argument among several, so it needs one (`trailingComma`).
    private fun appendFiles(source: StringBuilder, files: List<File>, indent: String, trailingComma: Boolean = true) {
        source.append(indent).append("listOf(\n")
        for (file in files) {
            val name = if (file.isDirectory) file.name + "/" else file.name
            val bytes = if (file.isDirectory) ByteArray(0) else file.readBytes()
            source.append(indent).append("    EmbeddedFile(\n")
            source.append(indent).append("        ").append(literal(name)).append(",\n")
            source.append(indent).append("        bytes(\n")
            for (start in bytes.indices step CHUNK_BYTES) {
                val end = minOf(start + CHUNK_BYTES, bytes.size)
                source.append(indent).append("            ").append(literal(bytes, start, end)).append(",\n")
            }
            source.append(indent).append("        ),\n")
            source.append(indent).append("    ),\n")
        }
        source.append(indent).append(if (trailingComma) "),\n" else ")\n")
    }

    private fun visible(directory: File): List<File> =
        directory.listFiles().orEmpty().filter { !it.name.startsWith(".") }.sortedBy { it.name }

    private fun literal(text: String): String {
        val out = StringBuilder("\"")
        for (char in text) {
            if (char.code in PRINTABLE && char != '"' && char != '\\' && char != '$') {
                out.append(char)
            } else {
                out.append("\\u").append(char.code.toString(HEX_RADIX).padStart(UNICODE_DIGITS, '0'))
            }
        }
        return out.append('"').toString()
    }

    // Escapes byte values, not characters: each `\uXX` in the generated source becomes one `Char` whose low byte
    // reconstructs the original byte in `bytes(...)`, so arbitrary and invalid-UTF-8 input bytes round-trip exactly,
    // even though the individual bytes of a multi-byte UTF-8 character look like unrelated characters in the generated
    // `.kt` file.
    private fun literal(bytes: ByteArray, start: Int, end: Int): String {
        val out = StringBuilder("\"")
        for (index in start until end) {
            val byte = bytes[index].toInt() and BYTE_MASK
            when {
                byte == '"'.code -> out.append("\\\"")
                byte == '\\'.code -> out.append("\\\\")
                byte == '$'.code -> out.append("\\$")
                byte == '\n'.code -> out.append("\\n")
                byte == '\r'.code -> out.append("\\r")
                byte == '\t'.code -> out.append("\\t")
                byte in PRINTABLE -> out.append(byte.toChar())
                else -> out.append("\\u00").append(HEX[byte shr 4]).append(HEX[byte and NIBBLE_MASK])
            }
        }
        return out.append('"').toString()
    }

    private companion object {
        // One generated file per 100 cases: a single function with a literal per case would eventually cross the 64 KiB
        // bytecode-per-method limit of the JVM as the suite grows.
        const val CASES_PER_FILE = 100

        // String literals are also split every 2048 bytes for the same reason: a very large `input.ics` or schema file
        // as one literal would risk the same limit inside the vararg call of `bytes(...)`.
        const val CHUNK_BYTES = 2048
        const val BYTE_MASK = 0xFF
        const val NIBBLE_MASK = 0xF
        const val HEX = "0123456789ABCDEF"
        const val HEX_RADIX = 16
        const val UNICODE_DIGITS = 4
        val PRINTABLE = 0x20..0x7E
    }
}
