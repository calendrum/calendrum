package io.github.calendrum.buildlogic

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
abstract class ValidationRuleSources : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val specDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    // Packs spec/properties.csv, components.csv, constraints.csv and itip.csv into tab-separated tables, one function
    // per chunk, so that the core reads no files and no resources.
    @TaskAction
    fun generate() {
        val spec = specDirectory.get().asFile
        val cardinalities = LinkedHashMap<String, String>()
        val properties = csv(File(spec, "properties.csv"))
        val header = properties.first()
        val first = header.indexOf("VCALENDAR")
        val last = header.indexOf("rfc")
        for (row in properties.drop(1)) {
            for (column in first until last) {
                val code = row[column]
                if (code.isNotEmpty()) cardinalities["${header[column]}\t${row[0]}"] = code
            }
        }
        val components = csv(File(spec, "components.csv")).drop(1).map { "${it[0]}\t${it[3]}" }
        val constraints = csv(File(spec, "constraints.csv")).drop(1).map { "${it[0]}\t${it[1]}\t${it[2]}" }
        val itip = csv(File(spec, "itip.csv")).drop(1)
            .filter { !it[3].startsWith("IANA-") && !it[3].startsWith("X-") }
            .map { "${it[0]}\t${it[1]}\t${it[2]}\t${it[3]}\t${it[4]}" }
        val output = outputDirectory.get().asFile
        output.deleteRecursively()
        val directory = File(output, "io/github/calendrum/validate")
        directory.mkdirs()
        val source = StringBuilder("package io.github.calendrum.validate\n\n")
        table(source, "cardinalityTable", cardinalities.entries.joinToString("\n") { "${it.key}\t${it.value}" })
        table(source, "componentTable", components.joinToString("\n"))
        table(source, "constraintTable", constraints.joinToString("\n"))
        table(source, "itipTable", itip.joinToString("\n"))
        File(directory, "ValidationTables.kt").writeText(source.toString())
    }

    private fun csv(file: File): List<List<String>> = file.readLines().filter { it.isNotBlank() }.map { it.split(',') }

    private fun table(source: StringBuilder, name: String, text: String) {
        val chunks = text.chunked(CHUNK)
        chunks.forEachIndexed { index, chunk ->
            source.append("private fun ").append(name).append(index).append("(): String =\n    ")
                .append(literal(chunk)).append("\n\n")
        }
        source.append("internal fun ").append(name).append("(): String =\n    ")
        source.append(chunks.indices.joinToString(" +\n    ") { "$name$it()" }.ifEmpty { "\"\"" })
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

    private companion object {
        const val CHUNK = 16_000
    }
}
