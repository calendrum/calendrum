package io.github.calendrum.tooling.ops

import io.github.calendrum.ICal
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.Severity
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.syntax.ParseResult

// The state of one call.
internal class Run(val environment: OpsEnvironment) {
    val out = JsonOut()
    val limits: OpsLimits = environment.limits
    val problems = ProblemSink(limits.maxProblems) { truncated = true }

    /** Whether the response does not hold everything: a limit stopped a list, or the input was not read whole. */
    var truncated = false

    /** Whether a list can take one more item. The reserve is for what comes after the list. */
    fun hasRoom(): Boolean = out.utf8Size < limits.maxResponseBytes - RESPONSE_RESERVE

    fun parse(request: Request): ParseResult = parse(request.input("ics"))

    fun parse(bytes: ByteArray): ParseResult {
        val result = parser.parse(bytes)
        problems.addAll(result.problems)
        if (result.isTruncated) truncated = true
        return result
    }

    fun writeProblems() {
        val sink = problems
        out.name("problems")
        out.beginArray()
        for (problem in sink.items) {
            out.beginObject()
            out.field("code", problem.code.id)
            out.field("severity", severityOf(problem.severity))
            out.field("message", problem.message)
            out.field("line", problem.span.line)
            out.field("column", problem.span.column)
            out.field("offset", problem.span.startOffset)
            out.endObject()
        }
        out.endArray()
        out.field("problem_count", sink.count)
    }

    private companion object {
        const val RESPONSE_RESERVE = 1024L * 1024

        // Options of the library for files from strangers: limits on depth, lines, components, size and time.
        val parser: ICalParser get() = ICal.parser(ParseOptions.Untrusted)
    }
}

internal fun severityOf(severity: Severity): String = severity.name.lowercase()

// Collects the problems of parsing and of the later stages, up to a limit. `lostInput` is told about the problems after
// which the response cannot be complete: a skipped line, a limit of the parser or of the expansion.
internal class ProblemSink(private val limit: Int, private val lostInput: () -> Unit) : ProblemListener {
    val items = ArrayList<Problem>()
    var count = 0
        private set

    fun addAll(problems: List<Problem>) {
        for (problem in problems) onProblem(problem)
    }

    override fun onProblem(problem: Problem) {
        count++
        if (items.size < limit) items += problem
        if (problem.code.id in LOST_INPUT) lostInput()
    }

    private companion object {
        val LOST_INPUT = setOf(
            "ICAL-S010",
            "ICAL-S013",
            "ICAL-S014",
            "ICAL-S022",
            "ICAL-S023",
            "ICAL-S029",
            "ICAL-S030",
            "ICAL-R001",
        )
    }
}
