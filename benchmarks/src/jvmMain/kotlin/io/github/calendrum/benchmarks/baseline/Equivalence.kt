package io.github.calendrum.benchmarks.baseline

import io.github.calendrum.benchmarks.corpus.ACADEMIC_WEEKDAYS
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.UNIVERSITY_COURSE_SERIES_COUNT
import io.github.calendrum.benchmarks.corpus.UNIVERSITY_HOLIDAYS
import io.github.calendrum.benchmarks.corpus.UNIVERSITY_SEMESTER_WEEKS
import java.io.ByteArrayOutputStream
import kotlin.system.exitProcess

internal class LibraryReport(
    val library: String,
    val counts: ModelCounts,
    val rewrittenCounts: ModelCounts,
    val values: Int,
    val occurrences: Map<String, Int>?,
)

internal class CorpusEquivalence(
    val corpus: String,
    val generator: ModelCounts,
    val generatorOccurrences: Map<String, Int>?,
    val reports: List<LibraryReport>,
) {
    fun problems(): List<String> = buildList {
        for (report in reports) {
            val prefix = "${report.library} on $corpus"
            if (report.counts != generator) add("$prefix: expected $generator, got ${report.counts}")
            if (report.rewrittenCounts != report.counts) {
                add("$prefix: write and re-read gives ${report.rewrittenCounts}, expected ${report.counts}")
            }
            if (report.values < report.counts.properties) {
                add("$prefix: read ${report.values} values for ${report.counts.properties} properties")
            }
        }
        val occurrences = reports.mapNotNull { report -> report.occurrences?.let { report.library to it } }
        val reference = generatorOccurrences?.let { "generator" to it } ?: occurrences.firstOrNull()
        if (reference != null) {
            for ((library, series) in occurrences) {
                if (library == reference.first) continue
                val differing = (series.keys + reference.second.keys).filter { series[it] != reference.second[it] }
                if (differing.isNotEmpty()) {
                    val sample = differing.take(3).joinToString { "$it: ${series[it]} vs ${reference.second[it]}" }
                    add("$library on $corpus: ${differing.size} series differ from ${reference.first} ($sample)")
                }
            }
        }
    }
}

internal fun ical4jReport(corpus: String): LibraryReport {
    val calendars = baselineInputs(corpus).map(Ical4jBaseline::parse)
    val counts = Ical4jBaseline.counts(calendars)
    val values = calendars.sumOf { Ical4jBaseline.readAll(it, ValueSink {}) }
    val occurrences = if (corpus in rruleCorpora) {
        calendars.flatMap(Ical4jBaseline::recurringMasters)
            .associate { Ical4jBaseline.uid(it) to Ical4jBaseline.occurrences(it).size }
    } else {
        null
    }
    val rewritten = calendars.map { calendar ->
        val output = ByteArrayOutputStream()
        Ical4jBaseline.write(calendar, output)
        Ical4jBaseline.parse(output.toByteArray())
    }
    return LibraryReport(Ical4jBaseline.NAME, counts, Ical4jBaseline.counts(rewritten), values, occurrences)
}

internal fun biweeklyReport(corpus: String): LibraryReport {
    val calendars = baselineInputs(corpus).map(BiweeklyBaseline::parse)
    val counts = BiweeklyBaseline.counts(calendars)
    val values = calendars.sumOf { BiweeklyBaseline.readAll(it, ValueSink {}) }
    val occurrences = if (corpus in rruleCorpora) {
        calendars.flatMap { calendar ->
            BiweeklyBaseline.recurringMasters(calendar).map { event ->
                BiweeklyBaseline.uid(event) to BiweeklyBaseline.occurrences(calendar.timezoneInfo, event)
            }
        }.toMap()
    } else {
        null
    }
    val rewritten = calendars.map { calendar ->
        val output = ByteArrayOutputStream()
        BiweeklyBaseline.write(calendar, output)
        BiweeklyBaseline.parse(output.toByteArray())
    }
    return LibraryReport(BiweeklyBaseline.NAME, counts, BiweeklyBaseline.counts(rewritten), values, occurrences)
}

internal fun generatorOccurrences(corpus: String): Map<String, Int>? {
    if (corpus != Corpus.UNIVERSITY) return null
    return (0 until UNIVERSITY_COURSE_SERIES_COUNT).associate { index ->
        val weekday = ACADEMIC_WEEKDAYS[index % ACADEMIC_WEEKDAYS.size]
        val excluded = UNIVERSITY_HOLIDAYS.count { it.second == weekday && it.first < UNIVERSITY_SEMESTER_WEEKS }
        "university-$index@example.com" to UNIVERSITY_SEMESTER_WEEKS - excluded
    }
}

internal fun corpusEquivalence(corpus: String): CorpusEquivalence = CorpusEquivalence(
    corpus = corpus,
    generator = generatorCounts(corpus),
    generatorOccurrences = generatorOccurrences(corpus),
    reports = listOf(ical4jReport(corpus), biweeklyReport(corpus)),
)

internal fun equivalenceTable(results: List<CorpusEquivalence>): String = buildString {
    appendLine(
        "| Corpus | Source | VCALENDAR | VTIMEZONE | STANDARD | DAYLIGHT | VEVENT | VALARM | Properties | Series | Occurrences |",
    )
    appendLine("|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|")
    for (result in results) {
        val rows = listOf(Triple("generator", result.generator, result.generatorOccurrences)) +
            result.reports.map { Triple(it.library, it.counts, it.occurrences) }
        for ((source, counts, occurrences) in rows) {
            val components = counts.components.values.joinToString(" | ")
            val series = occurrences?.size?.toString() ?: "—"
            val total = occurrences?.values?.sum()?.toString() ?: "—"
            appendLine("| ${result.corpus} | $source | $components | ${counts.properties} | $series | $total |")
        }
    }
}

fun main(args: Array<String>) {
    val corpora = args.toList().ifEmpty { baselineCorpora }
    val results = corpora.map { corpus ->
        val started = System.nanoTime()
        corpusEquivalence(corpus).also {
            System.err.println("$corpus: ${(System.nanoTime() - started) / 1_000_000} ms")
        }
    }
    print(equivalenceTable(results))
    val problems = results.flatMap(CorpusEquivalence::problems)
    if (problems.isNotEmpty()) {
        println()
        problems.forEach { println("- $it") }
        exitProcess(1)
    }
}
