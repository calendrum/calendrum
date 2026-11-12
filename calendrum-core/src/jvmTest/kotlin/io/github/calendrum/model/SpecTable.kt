package io.github.calendrum.model

import java.io.File

internal class SpecTable(val name: String, val header: List<String>, val rows: List<SpecRow>) {
    fun row(name: String): SpecRow = rows.firstOrNull { it.name == name } ?: error("$this: no row $name")

    fun rows(name: String): List<SpecRow> = rows.filter { it.name == name }

    fun names(): List<String> = rows.map { it.name }.distinct()

    fun rfc(rfc: String): List<SpecRow> = rows.filter { it["rfc"] == rfc }

    override fun toString(): String = name

    companion object {
        fun read(file: String): SpecTable {
            val directory = System.getProperty("calendrum.spec") ?: error("calendrum.spec is not set")
            val lines = File(directory, file).readLines().filter { it.isNotEmpty() }
            val header = lines.first().split(',')
            val rows = lines.drop(1).mapIndexed { index, line ->
                val cells = line.split(',')
                check(cells.size == header.size) { "$file:${index + 2}: ${cells.size} cells, expected ${header.size}" }
                SpecRow(file, index + 2, header, cells)
            }
            return SpecTable(file, header, rows)
        }

        val properties: SpecTable by lazy { read("properties.csv") }
        val parameters: SpecTable by lazy { read("parameters.csv") }
        val components: SpecTable by lazy { read("components.csv") }
        val constraints: SpecTable by lazy { read("constraints.csv") }
    }
}

internal class SpecRow(val file: String, val line: Int, private val header: List<String>, val cells: List<String>) {
    val name: String get() = cells[0]

    operator fun get(column: String): String {
        val index = header.indexOf(column)
        check(index >= 0) { "$file:$line: no column $column" }
        return cells[index]
    }

    fun words(column: String): List<String> = get(column).split(' ').filter { it.isNotEmpty() }

    override fun toString(): String = "$file:$line $name"
}

internal const val SPEC_NOT_ALLOWED = ""
internal const val SPEC_ONCE = "1"
internal const val SPEC_OPTIONAL = "?"
internal const val SPEC_OPTIONAL_SHOULD_ONCE = "?s"
internal const val SPEC_MANY = "*"
internal const val SPEC_REQUIRED_MANY = "+"

internal val SPEC_CARDINALITIES =
    setOf(SPEC_NOT_ALLOWED, SPEC_ONCE, SPEC_OPTIONAL, SPEC_OPTIONAL_SHOULD_ONCE, SPEC_MANY, SPEC_REQUIRED_MANY)

internal val SPEC_VALUE_TYPES = setOf(
    "BINARY",
    "BOOLEAN",
    "CAL-ADDRESS",
    "DATE",
    "DATE-TIME",
    "DURATION",
    "FLOAT",
    "INTEGER",
    "PERIOD",
    "RECUR",
    "TEXT",
    "TIME",
    "URI",
    "UTC-OFFSET",
    "UID",
    "XML-REFERENCE",
)

internal val SPEC_RFCS = listOf("5545", "7986", "9073", "9074", "9253")

internal fun isMultiple(cardinality: String): Boolean = cardinality == SPEC_MANY || cardinality == SPEC_REQUIRED_MANY

internal fun componentColumns(component: String): List<String> = SpecTable.components.row(component).words("columns")

internal fun cardinalities(component: String): Map<String, String> {
    val result = LinkedHashMap<String, String>()
    for (column in componentColumns(component)) {
        for (row in SpecTable.properties.rows) {
            val code = row[column]
            if (code == SPEC_NOT_ALLOWED) continue
            val previous = result[row.name]
            result[row.name] = if (previous == null || isMultiple(code)) code else previous
        }
    }
    return result
}
