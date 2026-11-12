package io.github.calendrum.validate

import io.github.calendrum.model.Cardinality
import io.github.calendrum.model.Method
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.describe
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * A custom check that runs on every component of the calendar, for rules of your own such as house style.
 *
 * Add one with [RuleSet.Builder.rule].
 */
public fun interface ValidationRule {
    /**
     * Checks one component.
     *
     * @param component the component being checked.
     * @param path where the component is in the calendar.
     * @return the issues found; empty when the component is fine.
     */
    public fun validate(component: ICalComponent, path: ElementPath): List<ValidationIssue>
}

internal class Constraint(val component: String, val rule: String, val names: List<String>)

/**
 * The rules a calendar is checked against: which properties each component requires, allows and forbids, which
 * components may be nested where, constraints between properties, an optional iTIP method with its tables, and custom
 * rules.
 *
 * The built-in tables are data (`properties.csv`, `components.csv`, `constraints.csv` and `itip.csv`), not code. A rule
 * set is immutable; change one with [toBuilder] or start from [builder].
 *
 * @property method the iTIP method the rules are for, or `null` for plain RFC 5545 rules.
 */
public class RuleSet private constructor(
    internal val cardinality: Map<String, Map<String, String>>,
    internal val children: Map<String, Set<String>>,
    internal val constraints: List<Constraint>,
    public val method: Method?,
    internal val itipCalendar: Map<String, Map<String, String>>,
    internal val itipProperties: Map<String, Map<String, String>>,
    internal val custom: List<ValidationRule>,
) {
    /** Returns a builder that starts from these rules. */
    public fun toBuilder(): Builder = Builder(this)

    /** Returns the iTIP method and the number of custom rules. */
    override fun toString(): String = describe("RuleSet", "method", method ?: "none", "custom", custom.size)

    /** Changes a copy of a [RuleSet]. Not thread-safe. */
    public class Builder internal constructor(base: RuleSet) {
        private val cardinality = base.cardinality.mapValues { LinkedHashMap(it.value) }.toMutableMap()
        private val custom = ArrayList(base.custom)
        private val method = base.method
        private val children = base.children
        private val constraints = base.constraints
        private val itipCalendar = base.itipCalendar
        private val itipProperties = base.itipProperties

        /**
         * Sets how many times [property] may occur in [component] to [value], replacing the rule that was there.
         *
         * @param component the component name, such as `VEVENT`.
         * @param property the property name, such as `SUMMARY`.
         * @param value how often the property occurs.
         */
        public fun cardinality(component: String, property: String, value: Cardinality): Builder = apply {
            val code = when (value) {
                Cardinality.One -> "1"
                Cardinality.Optional -> "?"
                Cardinality.Many -> "*"
                Cardinality.OneOrMore -> "+"
            }
            cardinality.getOrPut(component.uppercase()) { LinkedHashMap() }[property.uppercase()] = code
        }

        /**
         * Forbids [property] in [component]; a use of it is reported as `ICAL-L004`.
         *
         * @param component the component name, such as `VEVENT`.
         * @param property the property name, such as `ATTACH`.
         */
        public fun forbid(component: String, property: String): Builder = apply {
            cardinality.getOrPut(component.uppercase()) { LinkedHashMap() }[property.uppercase()] = FORBIDDEN
        }

        /** Adds a custom [rule], which runs after the built-in checks on every component. */
        public fun rule(rule: ValidationRule): Builder = apply { custom += rule }

        /** Creates the rule set. */
        public fun build(): RuleSet =
            RuleSet(cardinality, children, constraints, method, itipCalendar, itipProperties, ArrayList(custom))

        /** Returns the name of the builder. */
        override fun toString(): String = "RuleSet.Builder"
    }

    /** The built-in rule sets and a builder. */
    public companion object {
        @Volatile
        private var rfc: RuleSet? = null

        /** The rules of RFC 5545, without a method. */
        @get:JvmStatic
        public val Rfc5545: RuleSet
            get() = rfc ?: base().also { rfc = it }

        // Rows of spec/itip.csv for the method and the '*' rows: presence 1 is one, 1+ one or more, 0 forbidden, 0+ any
        // number, '0 or 1' optional; rows with an empty parent are about the component itself.

        /**
         * Returns the rules of RFC 5545 plus the table of RFC 5546 for [method]: the calendar must declare that
         * `METHOD`, and which components and properties it may hold depends on the method (`ICAL-L018`, `ICAL-L020`).
         *
         * @param method the iTIP method to check for.
         */
        @JvmStatic
        public fun itip(method: Method): RuleSet {
            val base = Rfc5545
            val calendarRows = HashMap<String, MutableMap<String, String>>()
            val propertyRows = HashMap<String, MutableMap<String, String>>()
            for (line in itipTable().split('\n')) {
                val fields = line.split('\t')
                if (fields.size < 5) continue
                val rowMethod = fields[0]
                if (rowMethod != method.value && rowMethod != "*") continue
                val code = presence(fields[4])
                if (fields[2].isEmpty()) {
                    calendarRows.getOrPut(fields[1]) { LinkedHashMap() }[fields[3]] = code
                } else {
                    propertyRows.getOrPut(fields[2]) { LinkedHashMap() }[fields[3]] = code
                }
            }
            return RuleSet(
                base.cardinality,
                base.children,
                base.constraints,
                method,
                calendarRows,
                propertyRows,
                emptyList(),
            )
        }

        /** Returns a builder that starts from [Rfc5545]. */
        @JvmStatic
        public fun builder(): Builder = Builder(Rfc5545)

        private fun base(): RuleSet {
            val cardinality = HashMap<String, MutableMap<String, String>>()
            for (line in cardinalityTable().split('\n')) {
                val fields = line.split('\t')
                if (fields.size == 3) cardinality.getOrPut(fields[0]) { LinkedHashMap() }[fields[1]] = fields[2]
            }
            val children = HashMap<String, Set<String>>()
            for (line in componentTable().split('\n')) {
                val fields = line.split('\t')
                if (fields.size != 2) continue
                children[fields[0]] = children[fields[0]].orEmpty() + fields[1].split(' ').filter { it.isNotEmpty() }
            }
            val constraints = componentConstraints()
            return RuleSet(cardinality, children, constraints, null, emptyMap(), emptyMap(), emptyList())
        }

        private fun componentConstraints(): List<Constraint> = constraintTable().split('\n').mapNotNull { line ->
            val fields = line.split('\t')
            if (fields.size ==
                3
            ) {
                Constraint(fields[0], fields[1], fields[2].split(' ').filter { it.isNotEmpty() })
            } else {
                null
            }
        }

        private fun presence(text: String): String = when (text) {
            "1" -> "1"
            "1+" -> "+"
            "0" -> FORBIDDEN
            "0+" -> "*"
            else -> "?"
        }
    }
}

internal const val FORBIDDEN = "0"
