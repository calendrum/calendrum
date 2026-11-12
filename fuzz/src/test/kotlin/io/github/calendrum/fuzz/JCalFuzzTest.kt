package io.github.calendrum.fuzz

import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.ICal
import io.github.calendrum.Severity
import io.github.calendrum.jcal.JCal
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty

// jCal reading of any text never throws. Writing a tree throws IllegalArgumentException only where canonical writing
// does (a control character, a parameter without a value, a name that is not a token) or for a VALUE that is
// not one type name; whatever it writes reads back without errors, and the second jCal equals the first
// (writing jCal is a fixed point after one round), unless a declared type could not decode its value (see below).
class JCalFuzzTest {
    @FuzzTest
    fun jCal(input: ByteArray) {
        JCal.decodeFromString(input.decodeToString())
        val components = ICal.parse(input).components
        if (components.isEmpty()) return
        val json = try {
            JCal.encodeToString(components)
        } catch (_: IllegalArgumentException) {
            invariant(!components.all(::canonicallyWritable) || components.any(::hasForeignValueType)) {
                "jCal rejects a tree that canonical writing accepts"
            }
            return
        }
        val back = JCal.decodeFromString(json)
        val errors = back.problems.filter { it.severity == Severity.Error }
        invariant(errors.isEmpty()) { "jCal written by the library reads with ${errors.map { it.code.id }}" }
        val again = JCal.encodeToString(back.components)
        if (again == json) return
        // A value that its declared type cannot decode is written as `unknown`, and reading `unknown` drops VALUE
        // (RFC 7265 §5), so the second jCal may read that text by the default type; it must not change again.
        val lossy = "\"$UNKNOWN\"" in json && components.any(::hasDeclaredType)
        invariant(lossy) { "jCal is not a fixed point after one round" }
        invariant(JCal.encodeToString(JCal.decodeFromString(again).components) == again) {
            "jCal is not a fixed point after two rounds"
        }
    }

    private fun hasDeclaredType(root: ICalComponent): Boolean = anyProperty(root) { property ->
        property.parameters.any { it.name == "VALUE" }
    }

    // Every VALUE parameter, as writing checks them all: a property may repeat it.
    private fun hasForeignValueType(root: ICalComponent): Boolean = anyProperty(root) { property ->
        property.parameters.any { it.name == "VALUE" && (it.values.size != 1 || !isTypeName(it.values[0])) }
    }

    private fun isTypeName(value: String): Boolean =
        value.isNotEmpty() && value.all { it == '-' || (it.isLetterOrDigit() && it.code <= 0x7F) }

    private fun anyProperty(root: ICalComponent, test: (ICalProperty) -> Boolean): Boolean {
        val pending = arrayListOf(root)
        while (pending.isNotEmpty()) {
            val component = pending.removeAt(pending.size - 1)
            if (component.properties.any(test)) return true
            pending.addAll(component.components)
        }
        return false
    }

    private companion object {
        const val UNKNOWN = "unknown"
    }
}
