package io.github.calendrum.sizebudget

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.util.Locale

@DisableCachingByDefault(because = "Compares with baseline.json and may rewrite it")
abstract class SizeReport : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val baselineApp: ConfigurableFileCollection

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val icsApp: ConfigurableFileCollection

    @get:Input
    abstract val platforms: ListProperty<String>

    @get:Input
    abstract val unavailablePlatforms: MapProperty<String, String>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val budgetFile: RegularFileProperty

    @get:Input
    abstract val calendrumVersion: Property<String>

    @get:Input
    abstract val updateBaseline: Property<Boolean>

    @get:OutputFile
    abstract val jsonReport: RegularFileProperty

    @get:OutputFile
    abstract val markdownReport: RegularFileProperty

    @TaskAction
    fun report() {
        val selected = SizePlatform.entries.filter { it.id in platforms.get() }
        val baseline = AppSize.read(baselineApp.files)
        val ics = AppSize.read(icsApp.files)
        val budget = Budget.read(budgetFile.get().asFile)
        val update = updateBaseline.get()

        require(baseline.metrics.map { it.id } == ics.metrics.map { it.id }) {
            "The baseline and Calendrum apps report different metrics: " +
                "${baseline.metrics.map { it.id }} and ${ics.metrics.map { it.id }}"
        }
        val notMeasured = selected.filter { platform -> ics.metrics.none { it.platform == platform } }
        if (notMeasured.isNotEmpty()) {
            throw GradleException(
                "Selected but not measured: ${notMeasured.joinToString { it.id }}. " +
                    "Every selected platform must report its metrics.",
            )
        }
        val rows = ics.metrics.map { metric ->
            val base = baseline.metrics.first { it.id == metric.id }.value
            Row(metric, base, metric.value, metric.value - base, budget.contributions[metric.id], budget)
        }
        val skipped = budget.contributions.mapNotNull { (id, accepted) ->
            SizePlatform.of(id)?.takeIf { it !in selected }?.let { Skipped(id, it, accepted, skipReason(it)) }
        }.sortedBy { it.platform.ordinal }
        val unknown = budget.contributions.keys.filter { SizePlatform.of(it) == null }
        val platformIds = SizePlatform.entries.joinToString { it.id }
        val unmeasured = budget.contributions.keys
            .filter { id -> SizePlatform.of(id) in selected } - rows.map { it.metric.id }.toSet()
        val problems = buildList {
            rows.filter {
                it.accepted == null
            }.forEach { add("${it.metric.id}: no accepted contribution in baseline.json") }
            unmeasured.forEach { add("$it: in baseline.json but no longer measured") }
            unknown.forEach { add("$it: in baseline.json but belongs to no platform ($platformIds)") }
            rows.filter { it.overBudget }.forEach { row ->
                add(
                    "${row.metric.id}: contribution grew from ${row.accepted} to ${row.contribution} " +
                        "${row.metric.unit}, ${row.change()}; limits are +${formatPercent(budget.percent)} " +
                        "and +${budget.absoluteLimit(row.metric.unit)} ${row.metric.unit}",
                )
            }
        }

        if (update) {
            val contributions = SizePlatform.entries.flatMap { platform ->
                if (platform in selected) {
                    rows.filter { it.metric.platform == platform }.map { it.metric.id to it.contribution }
                } else {
                    skipped.filter { it.platform == platform }.map { it.id to it.accepted }
                }
            }
            budget.write(budgetFile.get().asFile, contributions.toMap(linkedMapOf()))
        }
        val status = when {
            update ->
                "baseline.json updated with the measured contributions; " +
                    "values of platforms that were not measured are kept."

            problems.isEmpty() -> "Within budget."

            else -> "**Over budget:**\n\n" + problems.joinToString("\n") { "- $it" }
        }
        jsonReport.get().asFile.writeText(json(rows, skipped, baseline, ics, budget, update, problems))
        markdownReport.get().asFile.writeText(markdown(selected, rows, skipped, baseline, ics, budget, status))

        if (!update && problems.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Calendrum size budget exceeded:")
                    problems.forEach { appendLine("  - $it") }
                    appendLine("Report: ${markdownReport.get().asFile}")
                    append(
                        "If the growth is intended, accept it with size-budget/run.sh --update-baseline " +
                            "(or -PupdateBaseline=true), commit size-budget/baseline.json and explain the growth.",
                    )
                },
            )
        }
    }

    private fun skipReason(platform: SizePlatform): String = unavailablePlatforms.get()[platform.id]
        ?.let { "not measured on this host: $it" }
        ?: "skipped: not selected"

    private fun json(
        rows: List<Row>,
        skipped: List<Skipped>,
        baseline: AppSize,
        ics: AppSize,
        budget: Budget,
        update: Boolean,
        problems: List<String>,
    ): String {
        val result = linkedMapOf(
            "calendrumVersion" to calendrumVersion.get(),
            "tools" to ics.tools,
            "platforms" to SizePlatform.entries.associate { platform ->
                platform.id to (skipped.firstOrNull { it.platform == platform }?.reason ?: "measured")
            },
            "thresholds" to budget.thresholds,
            "metrics" to rows.map { row ->
                linkedMapOf(
                    "id" to row.metric.id,
                    "platform" to row.metric.platform.title,
                    "metric" to row.metric.name,
                    "unit" to row.metric.unit,
                    "baselineApp" to row.baselineApp,
                    "icsApp" to row.icsApp,
                    "contribution" to row.contribution,
                    "accepted" to row.accepted,
                    "change" to row.accepted?.let { row.contribution - it },
                    "overBudget" to row.overBudget,
                )
            },
            "notMeasured" to skipped.map { row ->
                linkedMapOf("id" to row.id, "accepted" to row.accepted, "reason" to row.reason)
            },
            "baselineUpdated" to update,
            "problems" to problems,
            "details" to linkedMapOf("baselineApp" to baseline.details, "icsApp" to ics.details),
        )
        return JsonOutput.prettyPrint(JsonOutput.toJson(result)) + "\n"
    }

    private fun markdown(
        selected: List<SizePlatform>,
        rows: List<Row>,
        skipped: List<Skipped>,
        baseline: AppSize,
        ics: AppSize,
        budget: Budget,
        status: String,
    ): String = buildString {
        val tools = ics.tools
        val toolList = listOfNotNull(
            tools["kotlin"]?.let { "Kotlin $it" },
            tools["r8"]?.let { "R8 $it on JDK ${tools["jdk"]}" },
            tools["agp"]?.let { "AGP $it with R8 ${tools["agpR8"]}" },
            tools["xcode"]?.let { "Xcode $it" },
        )
        appendLine("## Calendrum size budget")
        appendLine()
        appendLine(
            "Calendrum ${calendrumVersion.get()}; ${toolList.joinToString(", ")}. " +
                "Measured: ${selected.joinToString { it.title }}. " +
                "Contribution = Calendrum app - baseline app. The budget fails " +
                "when a contribution grows by more than ${formatPercent(budget.percent)} and by more than " +
                budget.absoluteLimits.entries.joinToString(" or ") { "${it.value} ${it.key}" } + ".",
        )
        appendLine()
        appendLine("| Platform | Metric | Baseline app | Calendrum app | Contribution | Accepted | Change |")
        appendLine("|---|---|--:|--:|--:|--:|---|")
        rows.forEach { row ->
            val change = row.change() + if (row.overBudget) " **over budget**" else ""
            appendLine(
                "| ${row.metric.platform.title} | ${row.metric.name} | ${row.baselineApp} | ${row.icsApp} | " +
                    "${row.contribution} | ${row.accepted ?: "-"} | $change |",
            )
        }
        skipped.forEach { row ->
            appendLine("| ${row.platform.title} | `${row.id}` | - | - | - | ${row.accepted} | ${row.reason} |")
        }
        appendLine()
        appendLine(status)
        if (SizePlatform.JVM in selected) {
            appendLine()
            appendLine("### For reference")
            appendLine()
            appendLine("R8 on the JVM: the whole runtime classpath in, one jar out.")
            appendLine()
            appendLine("| App | R8 input, bytes | R8 input, classes | R8 output, bytes | R8 output, classes |")
            appendLine("|---|--:|--:|--:|--:|")
            listOf("baseline app" to baseline, "Calendrum app" to ics).forEach { (name, app) ->
                val input = app.r8Input()
                appendLine(
                    "| $name | ${input.sumOf { it.bytes }} | ${input.sumOf { it.classes }} | " +
                        "${app.metric("jvm.r8.bytes")} | ${app.metric("jvm.r8.classes")} |",
                )
            }
            appendLine()
            appendLine("JVM runtime classpath of the Calendrum app, before R8:")
            appendLine()
            appendLine("| File | Bytes | Classes | Also in the baseline app |")
            appendLine("|---|--:|--:|---|")
            val baselineFiles = baseline.r8Input().map { it.file }.toSet()
            ics.r8Input().forEach { input ->
                val shared = if (input.file in baselineFiles) "yes" else "no"
                appendLine("| `${input.file}` | ${input.bytes} | ${input.classes} | $shared |")
            }
        }
    }

    private class Row(
        val metric: Metric,
        val baselineApp: Long,
        val icsApp: Long,
        val contribution: Long,
        val accepted: Long?,
        budget: Budget,
    ) {
        val overBudget: Boolean = accepted != null && budget.exceeded(metric.unit, accepted, contribution)

        fun change(): String {
            val old = accepted ?: return "no accepted value"
            val delta = contribution - old
            if (delta == 0L) return "0"
            val sign = if (delta > 0) "+" else ""
            val percent = if (old > 0) " ($sign${formatPercent(delta * 100.0 / old)})" else ""
            return "$sign$delta$percent"
        }
    }

    private class Skipped(val id: String, val platform: SizePlatform, val accepted: Long, val reason: String)

    private class Metric(
        val id: String,
        val platform: SizePlatform,
        val name: String,
        val unit: String,
        val value: Long,
    )

    private class R8Input(val file: String, val bytes: Long, val classes: Long)

    private class AppSize(val tools: Map<String, String>, val metrics: List<Metric>, val details: Map<String, Any?>) {
        fun metric(id: String): Long = metrics.first { it.id == id }.value

        fun r8Input(): List<R8Input> = (details[SizePlatform.JVM.id] as Map<*, *>)["r8Input"].let { inputs ->
            (inputs as List<*>).map { entry ->
                val input = entry as Map<*, *>
                R8Input(
                    file = input["file"].toString(),
                    bytes = (input["bytes"] as Number).toLong(),
                    classes = (input["classes"] as Number).toLong(),
                )
            }
        }

        companion object {
            fun read(files: Set<File>): AppSize {
                val parts = files.map { JsonSlurper().parse(it) as Map<*, *> }
                    .sortedBy { part -> SizePlatform.entries.first { it.id == part["platform"] }.ordinal }
                val platforms = parts.map { it["platform"].toString() }
                require(platforms.size == platforms.toSet().size) { "Measured twice: $platforms" }
                return AppSize(
                    tools = parts.flatMap { (it["tools"] as Map<*, *>).entries }
                        .associate { it.key.toString() to it.value.toString() }
                        .toSortedMap(),
                    metrics = parts.flatMap { part ->
                        val platform = SizePlatform.entries.first { it.id == part["platform"] }
                        (part["metrics"] as List<*>).map { entry ->
                            val metric = entry as Map<*, *>
                            Metric(
                                id = metric["id"].toString(),
                                platform = platform,
                                name = metric["name"].toString(),
                                unit = metric["unit"].toString(),
                                value = (metric["value"] as Number).toLong(),
                            )
                        }
                    },
                    details = parts.associate { it["platform"].toString() to it["details"] },
                )
            }
        }
    }

    private class Budget(val thresholds: Map<String, Any?>, val contributions: Map<String, Long>) {
        val percent: Double = (thresholds["percent"] as? Number)?.toDouble()
            ?: throw GradleException("baseline.json: thresholds.percent is missing")

        val absoluteLimits: Map<String, Long> = listOf(UNIT_BYTES, UNIT_CLASSES).associateWith { unit ->
            (thresholds[unit] as? Number)?.toLong()
                ?: throw GradleException("baseline.json: thresholds.$unit is missing")
        }

        fun absoluteLimit(unit: String): Long =
            absoluteLimits[unit] ?: throw GradleException("baseline.json: no threshold for unit '$unit'")

        fun exceeded(unit: String, accepted: Long, contribution: Long): Boolean {
            val delta = contribution - accepted
            val overPercent = accepted <= 0 || delta * 100.0 > percent * accepted
            return delta > 0 && delta > absoluteLimit(unit) && overPercent
        }

        fun write(file: File, newContributions: Map<String, Long>) {
            val json = linkedMapOf("thresholds" to thresholds, "contributions" to newContributions)
            file.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(json)) + "\n")
        }

        companion object {
            fun read(file: File): Budget {
                val json = JsonSlurper().parse(file) as Map<*, *>
                val thresholds = (json["thresholds"] as? Map<*, *>)
                    ?: throw GradleException("baseline.json: thresholds are missing")
                val contributions = (json["contributions"] as? Map<*, *>).orEmpty()
                return Budget(
                    thresholds = thresholds.entries.associate { it.key.toString() to it.value },
                    contributions = contributions.entries.associate {
                        it.key.toString() to (it.value as Number).toLong()
                    },
                )
            }
        }
    }

    private companion object {
        fun formatPercent(value: Double): String = String.format(Locale.ROOT, "%.1f%%", value)
    }
}
