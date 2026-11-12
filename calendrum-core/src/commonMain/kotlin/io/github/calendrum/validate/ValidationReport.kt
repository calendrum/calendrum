package io.github.calendrum.validate

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ArrayView
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The address of an element of a calendar, such as `VCALENDAR[0]/VEVENT[3]/ATTENDEE[1]@PARTSTAT`: components and
 * properties by name and index among those of the same name, and optionally a parameter.
 *
 * A path of a missing element has the index 0 and points where the element would be. Immutable.
 *
 * @property segments the components and the property from the root, in order.
 * @property parameter the name of the parameter, or `null` when the path ends at a component or property.
 */
public class ElementPath private constructor(public val segments: List<Segment>, public val parameter: String?) {
    /**
     * One step of an [ElementPath].
     *
     * @property name the name of the component or property.
     * @property index the 0-based index among elements of the same name in the parent.
     */
    public class Segment(public val name: String, public val index: Int) {
        /** Returns `true` when [other] has the same name and index. */
        override fun equals(other: Any?): Boolean =
            this === other || (other is Segment && name == other.name && index == other.index)

        /** Returns a hash code over the name and the index. */
        override fun hashCode(): Int = name.hashCode() * 31 + index

        /** Returns the segment as `NAME[index]`. */
        override fun toString(): String = "$name[$index]"
    }

    /** Returns the path of the child with the given [name] and [index] among children with that name. */
    public fun child(name: String, index: Int): ElementPath = ElementPath(segments + Segment(name, index), null)

    /** Returns the path of the parameter named [name] of the element this path ends at. */
    public fun parameter(name: String): ElementPath = ElementPath(segments, name)

    /** Returns `true` when [other] has the same segments and parameter. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is ElementPath && segments == other.segments && parameter == other.parameter)

    /** Returns a hash code over the segments and the parameter. */
    override fun hashCode(): Int = segments.hashCode() * 31 + parameter.hashCode()

    /** Returns the path as `VCALENDAR[0]/VEVENT[3]/ATTENDEE[1]@PARTSTAT`. */
    override fun toString(): String = segments.joinToString("/") + if (parameter == null) "" else "@$parameter"

    /** Creates paths. */
    public companion object {
        /** The empty path above the calendar. */
        @JvmField
        public val Root: ElementPath = ElementPath(emptyList(), null)

        /** Creates a path from [segments]. */
        @JvmStatic
        public fun of(vararg segments: Segment): ElementPath = ElementPath(segments.toList(), null)
    }
}

/**
 * One problem found by a validation.
 *
 * @property code the code of the `ICAL-L` series.
 * @property severity how serious it is; the severity of the code.
 * @property path the element it refers to.
 * @property span where the element is in the input, or `null` for a calendar that was not parsed.
 */
public class ValidationIssue(
    public val code: ProblemCode,
    public val severity: Severity,
    public val path: ElementPath,
    public val span: SourceSpan?,
) {
    /** Returns `true` when [other] has the same code, severity, path and span. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is ValidationIssue && code == other.code && severity == other.severity && path == other.path &&
                span == other.span
            )

    /** Returns a hash code over the code, the path and the severity. */
    override fun hashCode(): Int = (code.hashCode() * 31 + path.hashCode()) * 31 + severity.hashCode()

    /** Returns the code, severity, message and path; never a value. */
    override fun toString(): String = "${code.id} ${severity.name}: ${code.message} at $path"
}

/**
 * The result of a validation: the issues found.
 *
 * An immutable list of issues; the calendar is valid when none has the severity [io.github.calendrum.Severity.Error].
 */
public class ValidationReport internal constructor(issues: List<ValidationIssue>) {
    /** All issues, in the order they were found. */
    public val issues: List<ValidationIssue> = ArrayView(issues.toTypedArray())

    /** Whether there is no issue with the severity `Error`. Warnings and infos do not make a calendar invalid. */
    public val isValid: Boolean get() = issues.none { it.severity == Severity.Error }

    /** Returns the issues with the given [code]. */
    public fun issues(code: ProblemCode): List<ValidationIssue> = issues.filter { it.code == code }

    /** Returns the number of issues and whether the calendar is valid. */
    override fun toString(): String = "ValidationReport(${issues.size} issues, valid=$isValid)"
}
