@file:OptIn(InternalICalApi::class)

package io.github.calendrum.tooling.ops

import io.github.calendrum.ICal
import io.github.calendrum.InternalICalApi
import io.github.calendrum.Severity
import io.github.calendrum.anonymize.Anonymizer
import io.github.calendrum.fixup.Fixup
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.jcal.JCal
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.VEvent
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ComponentFingerprint
import io.github.calendrum.syntax.FingerprintOptions
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.ValidationIssue
import io.github.calendrum.validate.Validator
import kotlinx.serialization.json.JsonArray

internal fun inspect(request: Request, run: Run) {
    val includeJcal = request.boolean("include_jcal") ?: false
    val limit = request.limit()
    val parsed = run.parse(request)
    val out = run.out
    out.beginObject()
    out.field("charset", parsed.charset)
    out.name("calendars")
    out.beginArray()
    var events = 0
    for (calendar in parsed.calendars) {
        out.beginObject()
        out.optional("prod_id", calendar.prodId)
        out.optional("version", calendar.version)
        out.optional("method", calendar.method?.value)
        out.name("names")
        out.beginArray()
        for (name in calendar.names) out.value(name)
        out.endArray()
        out.field("event_count", calendar.events.size)
        out.field("todo_count", calendar.todos.size)
        out.field("journal_count", calendar.journals.size)
        out.field("free_busy_count", calendar.freeBusy.size)
        out.name("time_zones")
        out.beginArray()
        for (zone in calendar.timeZones) zone.tzid?.let { out.value(it) }
        out.endArray()
        out.name("events")
        out.beginArray()
        for (event in calendar.events) {
            if (events == limit || !run.hasRoom()) {
                run.truncated = true
                break
            }
            event(event, run)
            events++
        }
        out.endArray()
        out.endObject()
    }
    out.endArray()
    run.writeProblems()
    if (includeJcal) {
        out.name("jcal")
        out.raw(jcalOf(parsed.components))
    }
    out.endObject()
}

private fun event(event: VEvent, run: Run) {
    val out = run.out
    out.beginObject()
    out.optional("uid", event.uid)
    out.optional("summary", event.summary)
    event.start?.let { writeDateTime("start", it, out) }
    event.end?.let { writeDateTime("end", it, out) }
    out.optional("rrule", event.component.property("RRULE")?.value)
    out.optional("status", event.status?.value)
    out.endObject()
}

internal fun validate(request: Request, run: Run) {
    val rules = request.text("rules") ?: "rfc5545"
    if (rules != "rfc5545" && rules != "lint") badArgument("The field rules must be rfc5545 or lint")
    val limit = request.limit()
    val parsed = run.parse(request)
    val out = run.out
    out.beginObject()
    // A line that could not be read is an error of the file, even if what remains passes every rule.
    var valid = parsed.problems.none { it.severity == Severity.Error }
    var written = 0
    out.name("issues")
    out.beginArray()
    for ((index, calendar) in parsed.calendars.withIndex()) {
        val issues: List<ValidationIssue>
        val fixable: Set<ValidationIssue>
        if (rules == "lint") {
            val report = Linter.lint(calendar)
            issues = report.issues
            fixable = report.fixable.toSet()
        } else {
            issues = Validator.validate(calendar, RuleSet.Rfc5545).issues
            fixable = emptySet()
        }
        for (issue in issues) {
            if (issue.severity == Severity.Error) valid = false
            if (written == limit || !run.hasRoom()) {
                run.truncated = true
                continue
            }
            out.beginObject()
            out.field("calendar_index", index)
            out.field("code", issue.code.id)
            out.field("severity", severityOf(issue.severity))
            out.field("path", issue.path.toString())
            out.field("message", meaningOf(issue.code.id))
            if (rules == "lint") out.field("fixable", issue in fixable)
            out.endObject()
            written++
        }
    }
    out.endArray()
    // An input with no calendar is not a valid one: a file of 0 bytes or of plain text must not pass.
    val calendars = parsed.calendars.size
    out.field("calendar_count", calendars)
    out.field("valid", valid && calendars > 0)
    run.writeProblems()
    out.endObject()
}

internal fun fix(request: Request, run: Run) {
    val names = request.texts("fixups") ?: listOf("by_prod_id", "lint")
    val steps = names.map { name -> if (name == "lint") null else fixupOf(name) }
    val parsed = run.parse(request)
    val out = run.out
    val components = ArrayList<ICalComponent>(parsed.components.size)
    out.beginObject()
    var changed = false
    out.name("changes")
    out.beginArray()
    for (component in parsed.components) {
        val original = VCalendar.of(component)
        if (original == null) {
            components += component
            continue
        }
        var calendar: VCalendar = original
        for (step in steps) {
            if (step != null) {
                val result = step.apply(calendar)
                calendar = result.calendar
                for (change in result.changes) {
                    change(requestName(change.fixup), null, change.path.toString(), run)
                    changed = true
                }
            } else {
                val report = Linter.lint(calendar)
                calendar = Linter.fix(calendar, report)
                for (issue in report.fixable) {
                    change("lint", issue.code.id, issue.path.toString(), run)
                    changed = true
                }
            }
        }
        components += calendar.component
    }
    out.endArray()
    out.field("changed", changed)
    out.field("ics", icsOf(components))
    run.writeProblems()
    out.endObject()
}

// The library names a repair `windows-tzids`, and a request names it `windows_tzids`: a response speaks the language of the
// request, so that a name from it can go back into one.
private fun requestName(fixup: String): String = fixup.replace('-', '_')

private fun change(fixup: String, code: String?, path: String, run: Run) {
    val out = run.out
    if (!run.hasRoom()) {
        run.truncated = true
        return
    }
    out.beginObject()
    out.field("fixup", fixup)
    out.optional("code", code)
    out.field("path", path)
    out.endObject()
}

private fun fixupOf(name: String): Fixup = when (name) {
    "by_prod_id" -> Fixups.byProdId()
    "windows_tzids" -> Fixups.windowsTzids
    "x_wr_timezone" -> Fixups.xWrTimezone
    "x_wr_calname" -> Fixups.xWrCalendarName
    "outlook" -> Fixups.outlook
    "lotus_notes" -> Fixups.lotusNotes
    "google" -> Fixups.google
    "vcalendar10" -> Fixups.vcalendar10
    else -> badArgument("The field fixups has an unknown name")
}

internal fun diff(request: Request, run: Run) {
    val limit = request.limit()
    // The limits for files from strangers, and their problems: what lies beyond a limit has no fingerprint, so that a change there
    // would otherwise look like no change at all.
    val options = FingerprintOptions.builder().limits(
        ParseOptions.Untrusted.limits,
    ).problemListener(run.problems).build()
    val before = ICal.fingerprints(request.input("before"), options)
    val after = ICal.fingerprints(request.input("after"), options)
    val difference = after.diff(before)
    val out = run.out
    out.beginObject()
    var written = 0
    fun list(name: String, items: List<ComponentFingerprint>) {
        out.name(name)
        out.beginArray()
        for (item in items) {
            if (written == limit || !run.hasRoom()) {
                run.truncated = true
                break
            }
            val key = item.key
            out.beginObject()
            out.field("calendar_index", key.calendarIndex)
            out.field("name", key.name)
            out.optional("uid", key.uid)
            out.optional("recurrence_id", key.recurrenceId)
            out.optional("tzid", key.tzid)
            out.field("ordinal", key.ordinal)
            out.endObject()
            written++
        }
        out.endArray()
    }
    list("added", difference.added)
    list("removed", difference.removed)
    list("changed", difference.changed)
    out.field("unchanged_count", difference.unchangedCount)
    run.writeProblems()
    out.endObject()
}

internal fun convert(request: Request, run: Run) {
    val jcal = request.element("jcal")
    val out = run.out
    out.beginObject()
    if (jcal != null) {
        if (request.text("ics") != null ||
            request.text("ics_base64") != null
        ) {
            badArgument("Give only one of ics and jcal")
        }
        if (jcal !is JsonArray) badArgument("The field jcal must be a jCal array")
        val result = try {
            JCal.decodeFromJsonElement(jcal)
        } catch (_: IllegalArgumentException) {
            badArgument("The field jcal is not jCal")
        }
        run.problems.addAll(result.problems)
        out.field("ics", icsOf(result.components))
    } else {
        val parsed = run.parse(request)
        out.name("jcal")
        out.raw(jcalOf(parsed.components))
    }
    run.writeProblems()
    out.endObject()
}

internal fun anonymize(request: Request, run: Run) {
    val shift = request.int("shift_weeks") ?: 0
    if (shift < -MAX_SHIFT_WEEKS || shift > MAX_SHIFT_WEEKS) badArgument("The field shift_weeks is out of range")
    val input = request.input("ics")
    // The key lives only for this call and never leaves it, so two calls give unrelated pseudonyms.
    val result = try {
        Anonymizer(run.environment.randomBytes(ANONYMIZER_KEY_BYTES), shift, ParseOptions.Untrusted).anonymize(input)
    } catch (_: IllegalStateException) {
        // The anonymizer checks a rewritten line against the original and stops rather than let something through.
        badArgument("The calendar has a line that the anonymizer cannot rewrite safely")
    }
    if (!result.isVerified) badArgument("The calendar has a line that the anonymizer cannot rewrite safely")
    run.problems.addAll(result.problems)
    if (result.isTruncated) run.truncated = true
    val out = run.out
    out.beginObject()
    out.field("ics", icsOf(result.components))
    out.field("shift_weeks", shift)
    run.writeProblems()
    out.endObject()
}

// A tree from a stranger can hold what the writers refuse, such as a control character in a value of jCal.
private fun icsOf(components: List<ICalComponent>): String = try {
    ICal.writer().encodeToString(components)
} catch (_: IllegalArgumentException) {
    badArgument("The calendar has a name or a value that cannot be written")
}

private fun jcalOf(components: List<ICalComponent>): String = try {
    JCal.encodeToString(components)
} catch (_: IllegalArgumentException) {
    badArgument("The calendar has a name or a value that jCal cannot hold")
}

private const val ANONYMIZER_KEY_BYTES = 32
private const val MAX_SHIFT_WEEKS = 5200
