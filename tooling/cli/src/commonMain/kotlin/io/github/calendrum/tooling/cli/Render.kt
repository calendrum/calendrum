package io.github.calendrum.tooling.cli

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull

/** What the main output is, which decides how it is made safe for a terminal. */
internal enum class Kind {
    /** Lines the command wrote itself. They are made safe already. */
    HUMAN,

    /** A calendar, or jCal, printed as itself. It is made safe only for a terminal. */
    CALENDAR,

    /** The answer of an operation. It is made safe only for a terminal, and only in ways that keep it the same JSON. */
    JSON,
}

/**
 * What a command has to say.
 *
 * @property main the output of the command: what goes to the standard output or to the file of `-o`.
 * @property diagnostics lines for the standard error: what the reader found in the file.
 * @property report lines about what was done, such as the changes of `fix`: they go to the stream that the result does not.
 * @property keepFile whether `--in-place` leaves the file alone: there is nothing to put in it.
 */
internal class Rendered(
    val main: String,
    val kind: Kind,
    val diagnostics: List<String> = emptyList(),
    val report: List<String> = emptyList(),
    val keepFile: Boolean = false,
)

/** The response of an operation, read. */
internal class Answer(val raw: String, val envelope: JsonObject) {
    val ok: Boolean get() = envelope["ok"].booleanValue() == true
    val truncated: Boolean get() = envelope["truncated"].booleanValue() == true
    val result: JsonObject get() = envelope["result"] as? JsonObject ?: JsonObject(emptyMap())
    val errorCode: String get() = (envelope["error"] as? JsonObject)?.text("code").orEmpty()
    val errorMessage: String get() = (envelope["error"] as? JsonObject)?.text("message").orEmpty()
}

internal fun JsonElement?.booleanValue(): Boolean? = (this as? JsonPrimitive)?.booleanOrNull

internal fun JsonObject.text(name: String): String? = (this[name] as? JsonPrimitive)?.takeIf {
    it.isString
}?.contentOrNull

internal fun JsonObject.number(name: String): Int? = (this[name] as? JsonPrimitive)?.takeIf { !it.isString }?.intOrNull

internal fun JsonObject.objects(name: String): List<JsonObject> =
    (this[name] as? JsonArray)?.filterIsInstance<JsonObject>().orEmpty()

/** The commands whose output is the calendar in another form, and which therefore can lose something of it. */
internal val LOSSY_COMMANDS = setOf("fix", "format", "convert", "expand", "anonymize")

internal const val LOSS_NOTE =
    "The result does not hold all of the input: the reader skipped or changed part of it, as the problems above say."

/**
 * The problems that lose nothing of the input: a different line ending, a byte order mark, a blank line, an `END` that
 * names nothing that is open, a component that was not closed (it is closed), an encoding that the file itself declares.
 * Of the occurrences of a rule, the problems after which every occurrence is still there.
 *
 * It is a list of what is harmless and not of what is harmful, so that a code that comes later counts as a loss until
 * somebody has looked at it. A guessed encoding (`ICAL-S025`, `ICAL-S028`) is a loss: the bytes of the file cannot be
 * had back from the text.
 */
internal val LOSSLESS_CODES = setOf(
    "ICAL-S001", "ICAL-S002", "ICAL-S003", "ICAL-S004", "ICAL-S015", "ICAL-S016", "ICAL-S017", "ICAL-S018", "ICAL-S024",
    "ICAL-R003", "ICAL-R005", "ICAL-R006", "ICAL-R007", "ICAL-R008", "ICAL-R009",
)

/**
 * Whether the answer does not hold all of the input: it is truncated, a problem is beyond the list, or a problem is of a
 * code that is not known to lose nothing.
 */
internal fun losesData(answer: Answer): Boolean {
    if (answer.truncated) return true
    val result = answer.result
    val problems = result.objects("problems")
    val count = result.number("problem_count")
    if (count != null && count > problems.size) return true
    return problems.any { it.text("code") !in LOSSLESS_CODES }
}

/** Whether the input had no calendar, which a command that writes one cannot make into a result. */
internal fun hasNoCalendar(command: String, answer: Answer): Boolean {
    val result = answer.result
    return when (command) {
        "fix", "format", "anonymize" -> result.text("ics").isNullOrEmpty()
        "convert" -> (result["jcal"] as? JsonArray)?.isEmpty() ?: result.text("ics").isNullOrEmpty()
        else -> false
    }
}

private val SEVERITY_RANK = mapOf("info" to 0, "warning" to 1, "error" to 2)

/** The lowest rank of [Rendered] that fails `lint` for `--fail-on`. */
internal fun failRank(failOn: String): Int = SEVERITY_RANK.getValue(failOn)

internal fun renderLint(name: String, answer: Answer): Rendered {
    val result = answer.result
    val lines = ArrayList<String>()
    val counts = IntArray(SEVERITY_RANK.size)
    fun count(severity: String?) {
        val rank = SEVERITY_RANK[severity] ?: return
        counts[rank]++
    }
    for (problem in result.objects("problems")) {
        lines += problemLine(name, problem)
        count(problem.text("severity"))
    }
    // An empty file or a text that is not a calendar must not pass as a clean one.
    if (hasNoCalendarFinding(result)) {
        lines += "${sanitize(name)}: error no calendar in the input"
        count("error")
    }
    for (issue in result.objects("issues")) {
        val fixable = if (issue.booleanValue("fixable")) " [fixable]" else ""
        lines +=
            "${sanitize(
                name,
            )}: ${sanitize(issue.text("code").orEmpty())} ${sanitize(issue.text("severity").orEmpty())} " +
            "${sanitize(issue.text("message").orEmpty())} (${sanitize(issue.text("path").orEmpty())})$fixable"
        count(issue.text("severity"))
    }
    val total = counts.sum()
    lines += if (total == 0) {
        "${sanitize(name)}: no findings"
    } else {
        "${sanitize(
            name,
        )}: ${plural(total, "finding")} (${plural(counts[2], "error")}, ${plural(counts[1], "warning")}, " +
            "${counts[0]} info)"
    }
    return Rendered(lines.joinToString("\n", postfix = "\n"), Kind.HUMAN)
}

/**
 * Whether the answer has something to report in the exit code: a finding of `lint` at or above [failOn], or a difference
 * found by `diff`. It is decided on the answer and not on the text, so that `--json` has the same exit codes.
 */
internal fun hasFindings(command: String, answer: Answer, failOn: String): Boolean {
    val result = answer.result
    return when (command) {
        "lint" -> {
            if (hasNoCalendarFinding(result)) return true
            val threshold = failRank(failOn)
            (result.objects("problems") + result.objects("issues")).any {
                (SEVERITY_RANK[it.text("severity")] ?: 0) >=
                    threshold
            }
        }

        "diff" -> listOf("added", "removed", "changed").any { result.objects(it).isNotEmpty() }

        else -> false
    }
}

private fun hasNoCalendarFinding(result: JsonObject): Boolean = result.number("calendar_count") == 0

private fun JsonObject.booleanValue(name: String): Boolean = this[name].booleanValue() == true

private fun plural(count: Int, word: String): String = if (count == 1) "$count $word" else "$count ${word}s"

internal fun problemLine(name: String, problem: JsonObject): String =
    "${sanitize(name)}:${problem.number("line") ?: 0}:${problem.number("column") ?: 0} " +
        "${sanitize(problem.text("code").orEmpty())} ${sanitize(problem.text("severity").orEmpty())} " +
        sanitize(problem.text("message").orEmpty())

/** The problems of reading, as lines for the standard error. */
internal fun problemLines(name: String, result: JsonObject): List<String> =
    result.objects("problems").map { problemLine(name, it) }

internal fun renderFix(name: String, answer: Answer): Rendered {
    val result = answer.result
    val changes = result.objects("changes").map { change ->
        val code = change.text("code")?.let { " ${sanitize(it)}" }.orEmpty()
        "${sanitize(name)}: ${sanitize(change.text("fixup").orEmpty())}$code ${sanitize(change.text("path").orEmpty())}"
    }
    val changed = result.booleanValue("changed")
    val report = if (changed) changes else listOf("${sanitize(name)}: nothing to fix")
    return Rendered(
        main = result.text("ics").orEmpty(),
        kind = Kind.CALENDAR,
        diagnostics = problemLines(name, result),
        report = report,
        keepFile = !changed,
    )
}

internal fun renderFormat(name: String, answer: Answer): Rendered {
    val result = answer.result
    return Rendered(result.text("ics").orEmpty(), Kind.CALENDAR, diagnostics = problemLines(name, result))
}

internal fun renderConvert(name: String, answer: Answer): Rendered {
    val result = answer.result
    val jcal = result["jcal"]
    val main = if (jcal != null) jcal.toString() + "\n" else result.text("ics").orEmpty()
    return Rendered(main, Kind.CALENDAR, diagnostics = problemLines(name, result))
}

internal fun renderAnonymize(answer: Answer): Rendered = Rendered(answer.result.text("ics").orEmpty(), Kind.CALENDAR)

internal fun renderExpand(name: String, answer: Answer): Rendered {
    val result = answer.result
    val lines = ArrayList<String>()
    for (occurrence in result.objects("occurrences")) {
        val line = StringBuilder()
        line.append(occurrence.text("start")).append(' ').append(occurrence.text("end"))
        line.append(' ').append(sanitize(occurrence.text("summary") ?: "-"))
        occurrence.text("uid")?.let { line.append(" [").append(sanitize(it)).append(']') }
        val component = occurrence.text("component").orEmpty()
        if (component != "VEVENT") line.append(' ').append(sanitize(component))
        if (occurrence.booleanValue("all_day")) line.append(" all-day")
        if (occurrence.booleanValue("cancelled")) line.append(" cancelled")
        occurrence.text("local_start")?.let { line.append(" local ").append(it) }
        lines += line.toString()
    }
    val diagnostics = ArrayList<String>(problemLines(name, result))
    return Rendered(
        if (lines.isEmpty()) "" else lines.joinToString("\n", postfix = "\n"),
        Kind.HUMAN,
        diagnostics = diagnostics,
    )
}

internal fun renderDiff(answer: Answer): Rendered {
    val result = answer.result
    val lines = ArrayList<String>()
    for ((key, mark) in listOf("added" to "+", "removed" to "-", "changed" to "~")) {
        for (item in result.objects(key)) lines += "$mark ${describeComponent(item)}"
    }
    val unchanged = result.number("unchanged_count") ?: 0
    val differs = lines.isNotEmpty()
    lines += if (differs) "$unchanged unchanged" else "no differences ($unchanged unchanged)"
    return Rendered(lines.joinToString("\n", postfix = "\n"), Kind.HUMAN)
}

private fun describeComponent(item: JsonObject): String {
    val parts = ArrayList<String>()
    val index = item.number("calendar_index") ?: 0
    if (index > 0) parts += "calendar $index:"
    parts += sanitize(item.text("name").orEmpty())
    val uid = item.text("uid")
    val tzid = item.text("tzid")
    when {
        uid != null -> parts += sanitize(uid)
        tzid != null -> parts += sanitize(tzid)
        else -> parts += "#${item.number("ordinal") ?: 0}"
    }
    item.text("recurrence_id")?.let { parts += "(${sanitize(it)})" }
    return parts.joinToString(" ")
}

internal fun renderTimeZone(name: String, answer: Answer): Rendered {
    val result = answer.result
    val lines = ArrayList<String>()
    val id = sanitize(result.text("id").orEmpty())
    lines += if (result.booleanValue("alias")) "zone: $id (the identifier of the name given)" else "zone: $id"
    lines += "at: ${sanitize(result.text("at").orEmpty())}"
    lines += "offset: ${sanitize(result.text("offset").orEmpty())} (${result.number("offset_seconds") ?: 0} s)"
    val next = result["next_transition"] as? JsonObject
    if (next != null) {
        lines +=
            "next change: ${sanitize(
                next.text("at").orEmpty(),
            )}, offset then ${offsetText(next.number("offset_seconds_after") ?: 0)}"
    } else {
        lines += "next change: none known"
    }
    lines += "tzdb: ${sanitize(result.text("tzdb_version").orEmpty())}"
    return Rendered(lines.joinToString("\n", postfix = "\n"), Kind.HUMAN, diagnostics = problemLines(name, result))
}

private fun offsetText(seconds: Int): String {
    val sign = if (seconds < 0) '-' else '+'
    val total = kotlin.math.abs(seconds)
    val hours = (total / 3600).toString().padStart(2, '0')
    val minutes = (total % 3600 / 60).toString().padStart(2, '0')
    return "$sign$hours:$minutes"
}

internal fun renderExplain(answer: Answer): Rendered {
    val result = answer.result
    val text = "${sanitize(result.text("code").orEmpty())} ${sanitize(result.text("severity").orEmpty())} " +
        "${sanitize(result.text("constant").orEmpty())}\n${sanitize(result.text("meaning").orEmpty())}\n"
    return Rendered(text, Kind.HUMAN)
}
