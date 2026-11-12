package io.github.calendrum.lint

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import io.github.calendrum.fixup.Changes
import io.github.calendrum.fixup.Fixups
import io.github.calendrum.fixup.NON_TEXT_PROPERTIES
import io.github.calendrum.fixup.ROOT_PATH
import io.github.calendrum.fixup.copy
import io.github.calendrum.fixup.emptyValues
import io.github.calendrum.fixup.isFloating
import io.github.calendrum.fixup.lowerMailto
import io.github.calendrum.fixup.mapProperties
import io.github.calendrum.fixup.rewrite
import io.github.calendrum.fixup.untilTypes
import io.github.calendrum.model.VCalendar
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.describe
import io.github.calendrum.tz.TimeZoneAliases
import io.github.calendrum.validate.ElementPath
import io.github.calendrum.validate.RuleSet
import io.github.calendrum.validate.ValidationIssue
import io.github.calendrum.validate.Validator
import io.github.calendrum.validate.firstOfEach
import io.github.calendrum.validate.namesakeIndices
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The settings of the linter: the validation rules it builds on and the codes it leaves out.
 *
 * Immutable. Use [Default] for RFC 5545 rules and all checks on, or [builder].
 *
 * @property validation the validation rules that run before the interoperability checks.
 * @property disabled the codes whose issues are dropped from the report.
 */
public class LintRules private constructor(public val validation: RuleSet, public val disabled: Set<ProblemCode>) {
    /** Returns a builder that starts from these rules. */
    public fun toBuilder(): Builder = Builder(validation, disabled)

    /** Returns the number of disabled codes. */
    override fun toString(): String = describe("LintRules", "disabled", disabled.size)

    /** Changes a copy of [LintRules]. Not thread-safe. */
    public class Builder internal constructor(private var validation: RuleSet, disabled: Set<ProblemCode>) {
        private val disabled = LinkedHashSet(disabled)

        /** Sets the validation rules to [rules]. */
        public fun validation(rules: RuleSet): Builder = apply { validation = rules }

        /** Leaves the issues with [code] out of the report. */
        public fun disable(code: ProblemCode): Builder = apply { disabled += code }

        /** Puts [code] back, if it was disabled. */
        public fun enable(code: ProblemCode): Builder = apply { disabled -= code }

        /** Creates the rules. */
        public fun build(): LintRules = LintRules(validation, LinkedHashSet(disabled))

        /** Returns the name of the builder. */
        override fun toString(): String = "LintRules.Builder"
    }

    /** The default rules and a builder. */
    public companion object {
        /** RFC 5545 validation rules with every check on. */
        @JvmField
        public val Default: LintRules = LintRules(RuleSet.Rfc5545, emptySet())

        /** Returns a builder that starts from the default rules. */
        @JvmStatic
        public fun builder(): Builder = Builder(RuleSet.Rfc5545, emptySet())
    }
}

/**
 * The result of linting a calendar.
 *
 * @property issues all issues, from validation (`ICAL-L001` to `ICAL-L020`) and from the interoperability checks
 *   (`ICAL-L021` to `ICAL-L025`).
 * @property fixable the issues that [Linter.fix] can repair.
 */
public class LintReport internal constructor(
    public val issues: List<ValidationIssue>,
    public val fixable: List<ValidationIssue>,
) {
    /** Whether there are no issues at all, of any severity. */
    public val isClean: Boolean get() = issues.isEmpty()

    /** Returns the number of issues and of fixable issues. */
    override fun toString(): String = describe("LintReport", "issues", issues.size, "fixable", fixable.size)
}

// Validation plus interoperability rules; a fix runs per code over the whole calendar, reusing the fixups for the
// interoperability codes.

/**
 * Finds problems that make a calendar fail in real-world clients, in addition to the plain rule checks of the
 * validator.
 *
 * It runs the validator and then checks interoperability: `X-WR-TIMEZONE` beside floating times, non-IANA `TZID`s,
 * `X-WR-CALNAME` without `NAME`, an upper-case `MAILTO:` and empty values of non-text properties. Many of the issues
 * can be repaired automatically with [fix]. Linting does not change the calendar.
 *
 * @sample io.github.calendrum.samples.lintAndFix
 */
public object Linter {
    /**
     * Lints [calendar] with the default rules.
     *
     * @param calendar the calendar to check.
     */
    @JvmStatic
    public fun lint(calendar: VCalendar): LintReport = lint(calendar, LintRules.Default)

    /**
     * Lints [calendar] with the given [rules].
     *
     * @param calendar the calendar to check.
     * @param rules the validation rules and the codes to leave out.
     */
    @JvmStatic
    public fun lint(calendar: VCalendar, rules: LintRules): LintReport {
        val issues = ArrayList<ValidationIssue>()
        issues += Validator.validate(calendar, rules.validation).issues
        interop(calendar.component, issues)
        val kept = issues.filter { it.code !in rules.disabled }
        return LintReport(kept, kept.filter(::isFixable))
    }

    /**
     * Repairs the fixable issues of [report] in [calendar] and returns the new calendar.
     *
     * The repairs are the ones listed by `LintReport.fixable`: empty values, non-IANA `TZID`s, `X-WR-TIMEZONE`,
     * `X-WR-CALNAME`, an upper-case `MAILTO:`, an extra `DURATION` beside `DTEND`, an `UNTIL` of the wrong type, and a
     * value out of range such as `PRIORITY`. Other issues are left for the caller.
     *
     * @param calendar the calendar that was linted.
     * @param report the report of that calendar.
     */
    @JvmStatic
    public fun fix(calendar: VCalendar, report: LintReport): VCalendar {
        var current = calendar
        val codes = report.fixable.map { it.code }.toSet()
        for (code in FIX_ORDER) {
            if (code !in codes) continue
            current = when (code) {
                ProblemCode.AliasTimeZoneId -> Fixups.windowsTzids.apply(current).calendar
                ProblemCode.VendorTimeZone -> Fixups.xWrTimezone.apply(current).calendar
                ProblemCode.VendorCalendarName -> Fixups.xWrCalendarName.apply(current).calendar
                else -> VCalendar(treeFix(current.component, code))
            }
        }
        return current
    }

    /** Returns the name of the object. */
    override fun toString(): String = "Linter"
}

private val FIX_ORDER = listOf(
    ProblemCode.EmptyValue,
    ProblemCode.AliasTimeZoneId,
    ProblemCode.VendorTimeZone,
    ProblemCode.VendorCalendarName,
    ProblemCode.UppercaseMailto,
    ProblemCode.ExclusiveProperties,
    ProblemCode.ValueTypeMismatch,
    ProblemCode.ValueOutOfRange,
)

private fun isFixable(issue: ValidationIssue): Boolean = when (issue.code) {
    ProblemCode.EmptyValue, ProblemCode.AliasTimeZoneId, ProblemCode.VendorTimeZone, ProblemCode.VendorCalendarName,
    ProblemCode.UppercaseMailto, ProblemCode.ValueOutOfRange,
    -> true

    ProblemCode.ExclusiveProperties -> issue.path.segments.lastOrNull()?.name == "DURATION"

    ProblemCode.ValueTypeMismatch -> issue.path.segments.lastOrNull()?.name == "RRULE"

    else -> false
}

private fun treeFix(root: ICalComponent, code: ProblemCode): ICalComponent {
    val changes = Changes("lint")
    return when (code) {
        ProblemCode.EmptyValue -> emptyValues(root, changes)

        ProblemCode.UppercaseMailto -> rewrite(root, ROOT_PATH) { component, path ->
            component.mapProperties(path, changes) { lowerMailto(it) }
        }

        ProblemCode.ExclusiveProperties -> rewrite(root, ROOT_PATH) { component, path ->
            val end = component.property("DTEND") != null || component.property("DUE") != null
            if (!end) component else component.mapProperties(path, changes) { if (it.name == "DURATION") null else it }
        }

        ProblemCode.ValueTypeMismatch -> rewrite(root, ROOT_PATH) { component, path ->
            untilTypes(component, path, changes)
        }

        ProblemCode.ValueOutOfRange -> rewrite(root, ROOT_PATH) { component, path ->
            component.mapProperties(path, changes) { property ->
                val range = RANGES[property.name] ?: return@mapProperties property
                val value = property.value.trim().toIntOrNull() ?: return@mapProperties property
                val clamped = value.coerceIn(range)
                if (clamped == value) property else property.copy(value = clamped.toString())
            }
        }

        else -> root
    }
}

private val RANGES = mapOf("PRIORITY" to 0..9, "PERCENT-COMPLETE" to 0..100)

private fun interop(root: ICalComponent, issues: MutableList<ValidationIssue>) {
    val zone = root.property("X-WR-TIMEZONE")
    if (zone != null && hasFloating(root)) {
        issues +=
            ValidationIssue(
                ProblemCode.VendorTimeZone,
                Severity.Warning,
                ROOT_PATH.child("X-WR-TIMEZONE", 0),
                zone.span,
            )
    }
    val name = root.property("X-WR-CALNAME")
    if (name != null && root.property("NAME") == null) {
        issues +=
            ValidationIssue(
                ProblemCode.VendorCalendarName,
                Severity.Info,
                ROOT_PATH.child("X-WR-CALNAME", 0),
                name.span,
            )
    }
    // The first use of each TZID is checked; TZIDs and names come from the input, so they are sorted, not hashed.
    val found = ArrayList<ValidationIssue>()
    val tzids = ArrayList<String>()
    val slots = ArrayList<Int>()
    walk(root, ROOT_PATH) { component, path ->
        val properties = component.properties
        val indices = namesakeIndices(properties.size) { properties[it].name }
        for (position in properties.indices) {
            val property = properties[position]
            val at = path.child(property.name, indices[position])
            if (property.value.isEmpty() && property.name in NON_TEXT_PROPERTIES) {
                found += ValidationIssue(ProblemCode.EmptyValue, Severity.Warning, at, property.span)
            }
            if (lowerMailto(property) !== property) {
                found += ValidationIssue(ProblemCode.UppercaseMailto, Severity.Info, at, property.span)
            }
            val tzid = if (component.name == "VTIMEZONE" && property.name == "TZID") {
                property.value
            } else {
                property.parameter("TZID")?.value
            }
            if (tzid != null) {
                val target = if (property.name == "TZID") at else at.parameter("TZID")
                tzids += tzid
                slots += found.size
                found += ValidationIssue(ProblemCode.AliasTimeZoneId, Severity.Warning, target, property.span)
            }
        }
    }
    val first = firstOfEach(tzids.size) { a, b -> tzids[a].compareTo(tzids[b]) }
    val dropped = BooleanArray(found.size)
    for (index in tzids.indices) dropped[slots[index]] = !first[index] || !isAlias(tzids[index])
    for (index in found.indices) if (!dropped[index]) issues += found[index]
}

private fun isAlias(tzid: String): Boolean {
    val match = TimeZoneAliases.resolve(tzid) ?: return false
    return !match.guessed && match.ianaId != tzid
}

private fun hasFloating(component: ICalComponent): Boolean =
    component.properties.any { it.name in DATES && isFloating(it) } ||
        component.components.any { it.name != "VTIMEZONE" && hasFloating(it) }

private val DATES = setOf("DTSTART", "DTEND", "DUE", "RECURRENCE-ID", "EXDATE", "RDATE")

private fun walk(component: ICalComponent, path: ElementPath, visit: (ICalComponent, ElementPath) -> Unit) {
    visit(component, path)
    val children = component.components
    val indices = namesakeIndices(children.size) { children[it].name }
    for (position in children.indices) {
        walk(children[position], path.child(children[position].name, indices[position]), visit)
    }
}
