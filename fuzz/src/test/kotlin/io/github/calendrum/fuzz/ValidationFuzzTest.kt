package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.lint.Linter
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.validate.ElementPath
import io.github.calendrum.validate.Validator

// Validation and lint of any calendar: nothing throws, every issue of validation is also an issue of lint, there is at
// most one issue per code and path, every path names an element that exists, and the repaired calendar lints again.
class ValidationFuzzTest {
    @FuzzTest
    fun validation(input: ByteArray) {
        for (calendar in ICal.parse(input).calendars) {
            val validation = Validator.validate(calendar).issues
            val lint = Linter.lint(calendar)
            val linted = lint.issues.map { it.code to it.path }.toSet()
            for (issue in validation) invariant(issue.code to issue.path in linted) { "lint lacks $issue" }
            invariant(validation.map { it.code to it.path }.toSet().size == validation.size) { "duplicate issues" }
            for (issue in validation + lint.issues) {
                invariant(exists(calendar.component, issue.path, issue.code.id)) { "no element at ${issue.path}" }
            }
            Linter.lint(Linter.fix(calendar, lint))
        }
    }

    // A missing element is the subject of some issues (a required property, a required child), so only the path up to
    // the parent of the last step must exist; a parameter is not followed.
    private fun exists(root: ICalComponent, path: ElementPath, code: String): Boolean {
        val segments = path.segments
        if (segments.isEmpty() || segments[0].name != root.name || segments[0].index != 0) return false
        var component = root
        for (position in 1 until segments.size) {
            val segment = segments[position]
            val child = component.components.filter { it.name == segment.name }.getOrNull(segment.index)
            if (child != null) {
                component = child
                continue
            }
            if (position != segments.size - 1) return false
            val property = component.properties.filter { it.name == segment.name }.getOrNull(segment.index)
            return property != null || code in ABSENT
        }
        return true
    }

    private companion object {
        // Missing property, missing component, method mismatch and method not defined name what is absent.
        val ABSENT = setOf("ICAL-L001", "ICAL-L007", "ICAL-L018", "ICAL-L020")
    }
}
