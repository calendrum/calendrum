package io.github.calendrum.anonymize

import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseResult

// Problem codes are compared by presence. A code that is gone is explained when every reported occurrence lies in a
// changed line, or when writing a tree cannot reproduce it (the tree holds no blank lines, stray END lines, properties
// outside components, skipped lines or implicit ends); anything else fails the check.
internal class Verification(
    source: ParseResult,
    tree: List<ICalComponent>,
    output: ParseResult,
    stats: AnonymizationStats,
) {
    val shapePreserved: Boolean = sameShape(source.components, tree)
    val readsBack: Boolean = output.components == tree
    val insideReplaced: List<ProblemCode>
    val notWritten: List<ProblemCode>
    val unexpected: List<ProblemCode>

    init {
        val inside = ArrayList<ProblemCode>()
        val writer = ArrayList<ProblemCode>()
        val other = ArrayList<ProblemCode>()
        for (code in ProblemCode.known) {
            val before = source.problemCount(code) > 0
            val after = output.problemCount(code) > 0
            if (before == after) continue
            when {
                after -> other += code
                code.id in NOT_WRITTEN -> writer += code
                livesInside(code, source, stats) -> inside += code
                else -> other += code
            }
        }
        insideReplaced = inside
        notWritten = writer
        unexpected = other
    }

    val passed: Boolean get() = shapePreserved && readsBack && unexpected.isEmpty()

    private fun livesInside(code: ProblemCode, source: ParseResult, stats: AnonymizationStats): Boolean {
        var found = false
        for (problem in source.problems) {
            if (problem.code != code) continue
            if (!stats.isInsideChanged(problem.span.startOffset)) return false
            found = true
        }
        return found
    }

    private companion object {
        private val NOT_WRITTEN = setOf(
            "ICAL-S001", "ICAL-S002", "ICAL-S003", "ICAL-S004", "ICAL-S005", "ICAL-S007", "ICAL-S010", "ICAL-S011",
            "ICAL-S013", "ICAL-S014", "ICAL-S015", "ICAL-S016", "ICAL-S017", "ICAL-S018", "ICAL-S021", "ICAL-S022",
            "ICAL-S023",
        )
    }
}

internal fun sameShape(left: List<ICalComponent>, right: List<ICalComponent>): Boolean {
    if (left.size != right.size) return false
    val lefts = ArrayList<ICalComponent>(left)
    val rights = ArrayList<ICalComponent>(right)
    while (lefts.isNotEmpty()) {
        val a = lefts.removeAt(lefts.lastIndex)
        val b = rights.removeAt(rights.lastIndex)
        if (a.name != b.name || a.properties.size != b.properties.size || a.components.size != b.components.size) {
            return false
        }
        for (index in a.properties.indices) {
            val p = a.properties[index]
            val q = b.properties[index]
            if (p.name != q.name || p.parameters.size != q.parameters.size) return false
            for (parameter in p.parameters.indices) {
                val x = p.parameters[parameter]
                val y = q.parameters[parameter]
                if (x.name != y.name || x.values.size != y.values.size) return false
            }
        }
        lefts.addAll(a.components)
        rights.addAll(b.components)
    }
    return true
}

internal class AnonymizationReport(
    private val session: AnonymizationSession,
    private val shiftWeeks: Int,
    private val format: OutputFormat,
    private val encoded: Boolean,
    private val inputSize: Int,
    private val outputSize: Int,
    private val source: ParseResult,
    private val output: ParseResult,
    private val verification: Verification,
) {
    fun render(): String = buildString {
        val stats = session.stats
        appendLine("Properties (value replaced / dates shifted / kept):")
        for (name in stats.properties.keys.sorted()) {
            val counts = stats.properties.getValue(name)
            val replaced = counts[AnonymizationStats.REPLACED]
            val shifted = counts[AnonymizationStats.SHIFTED]
            val kept = counts[AnonymizationStats.KEPT]
            appendLine("  ${printable(name).padEnd(NAME_WIDTH)} ${column(replaced)} ${column(shifted)} ${column(kept)}")
        }
        appendLine("Parameters (replaced / kept):")
        for (name in stats.parameters.keys.sorted()) {
            val counts = stats.parameters.getValue(name)
            val replaced = counts[AnonymizationStats.REPLACED]
            val kept = counts[AnonymizationStats.KEPT]
            appendLine("  ${printable(name).padEnd(NAME_WIDTH)} ${column(replaced)} ${column(kept)}")
        }
        append("Lines: ${stats.editedInPlace} edited in place, ${stats.rebuilt} rebuilt")
        appendLine(if (shiftWeeks == 0) "; dates not shifted" else "; dates shifted by $shiftWeeks weeks")
        warnings(stats)
        appendLine("Input: ${source.charset}, $inputSize bytes; problems: ${problems(source)}")
        val charset = if (encoded) format.charset.canonicalName else "UTF-8"
        val mark = if (format.byteOrderMark && encoded) " with a byte order mark" else ""
        appendLine(
            "Output: $charset$mark, line endings ${format.lineEndings}, $outputSize bytes; " +
                "reads as ${output.charset}; problems: ${problems(output)}",
        )
        appendLine("Check: ${if (verification.passed) "passed" else "FAILED"}")
        appendLine("  tree shape preserved: ${yesNo(verification.shapePreserved)}")
        appendLine("  output reads back as the anonymized tree: ${yesNo(verification.readsBack)}")
        appendLine("  problem codes that lived inside replaced values: ${codes(verification.insideReplaced)}")
        appendLine("  problem codes the tree writer does not reproduce: ${codes(verification.notWritten)}")
        append("  unexpected problem code changes: ${codes(verification.unexpected)}")
    }

    private fun StringBuilder.warnings(stats: AnonymizationStats) {
        val lines = ArrayList<String>()
        for (name in stats.unrecognizedDates.keys.sorted()) {
            lines += "${printable(name)}: ${stats.unrecognizedDates.getValue(name)} date values not recognized, " +
                "left as they were"
        }
        for (name in stats.notTokens.keys.sorted()) {
            lines += "${printable(name)}: ${stats.notTokens.getValue(name)} values are not a structural token, " +
                "replaced as text"
        }
        if (stats.rulesMayChange > 0) {
            lines += "${stats.rulesMayChange} recurrence rules may change meaning after the shift " +
                "(MONTHLY, YEARLY, BYMONTH, BYMONTHDAY, BYYEARDAY, BYWEEKNO or BYSETPOS)"
        }
        if (session.invalidSequences > 0) {
            lines +=
                "${session.invalidSequences} invalid UTF-8 sequences replaced by invalid sequences of the same length"
        }
        if (!encoded) lines += "output kept in UTF-8: a character has no form in ${format.charset.canonicalName}"
        if (lines.isEmpty()) {
            appendLine("Warnings: none")
            return
        }
        appendLine("Warnings:")
        for (line in lines) appendLine("  - $line")
    }

    private fun problems(result: ParseResult): String {
        val parts = ArrayList<String>()
        for (code in ProblemCode.known) {
            val count = result.problemCount(code)
            if (count > 0) parts += "${code.id} x$count"
        }
        return if (parts.isEmpty()) "none" else parts.joinToString(", ")
    }

    private fun codes(codes: List<ProblemCode>): String =
        if (codes.isEmpty()) "none" else codes.joinToString(", ") { it.id }

    private fun yesNo(value: Boolean): String = if (value) "yes" else "NO"

    private fun column(count: Int): String = count.toString().padStart(COUNT_WIDTH)

    private fun printable(name: String): String {
        val shown = if (name.length > NAME_WIDTH) name.substring(0, NAME_WIDTH - 1) + "~" else name
        return buildString {
            for (char in shown) append(if (char in ' '..'~') char else '?')
        }
    }

    private companion object {
        private const val NAME_WIDTH = 40
        private const val COUNT_WIDTH = 7
    }
}
