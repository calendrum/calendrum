package io.github.calendrum.validate

import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyCodec
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.decodeWith
import io.github.calendrum.model.get
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.toInstant
import io.github.calendrum.value.ICalDateTime
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Checks a calendar against a set of rules and reports what is wrong, without ever throwing.
 *
 * The default rules are those of RFC 5545 ([RuleSet.Rfc5545]); [RuleSet.itip] adds the tables of RFC 5546 for one
 * method, and [RuleSet.builder] makes your own. Validation looks at a parsed calendar and does not change it.
 *
 * @sample io.github.calendrum.samples.validateCalendar
 * @sample io.github.calendrum.samples.validateWithHouseRules
 */
public object Validator {
    /**
     * Validates [calendar] against [rules] and returns the issues found.
     *
     * Each issue has a code of the `ICAL-L` series, a severity, the path of the offending element and, for a parsed
     * calendar, its position. At most one issue is reported for each code and path. Properties that do not decode are
     * reported as `ICAL-L016`; the details are the `ICAL-V` problems of reading. Time zones are resolved with the
     * default resolver of the calendar, so a `DTEND` in another zone than `DTSTART` is compared correctly, and a
     * comparison that needs an unknown zone is skipped.
     *
     * @param calendar the calendar to check.
     * @param rules the rules; RFC 5545 by default. From Java call `Validator.validate(calendar)` or pass the rules.
     * @sample io.github.calendrum.samples.validateCalendar
     */
    @JvmStatic
    @JvmOverloads
    public fun validate(calendar: VCalendar, rules: RuleSet = RuleSet.Rfc5545): ValidationReport {
        val validation = Validation(calendar, rules)
        validation.run()
        val issues = validation.issues
        val first = firstOfEach(issues.size) { a, b -> compareIssues(issues[a], issues[b]) }
        return ValidationReport(issues.filterIndexed { index, _ -> first[index] })
    }

    /** Returns the name of the object. */
    override fun toString(): String = "Validator"
}

// Walks the tree once: cardinalities of the component (VALARM by its ACTION form, an unknown action as AUDIO), values
// that do not decode, the constraint rows of the component, then the iTIP presence of the method over the RFC 5545
// cardinalities; missing VTIMEZONEs are checked once per TZID over the whole calendar. One issue per code and path.
// n log n in the size of the tree: the index of an element among its namesakes comes from one sort of the siblings,
// never from a scan of the earlier ones (that made 24,000 events 80 times slower than 1,500), and nothing from the
// input is a key of a hash table (see Namesakes.kt).
private class Validation(private val calendar: VCalendar, private val rules: RuleSet) {
    val issues = ArrayList<ValidationIssue>()
    private val root = calendar.component
    private val registry = ICalRegistry.Default
    private val hasMethod = root.property("METHOD") != null
    private val zones by lazy { TimeZoneResolvers.default(calendar) }

    fun run() {
        val pending = ArrayList<Triple<ICalComponent, ElementPath, String?>>()
        pending += Triple(root, ElementPath.Root.child(root.name, 0), null)
        while (pending.isNotEmpty()) {
            val (component, path, parent) = pending.removeAt(pending.size - 1)
            check(component, path, parent)
            val children = component.componentNodes
            val positions = namesakeIndices(children.size) { children[it].name }
            for (index in children.indices.reversed()) {
                val child = children[index]
                pending += Triple(child, path.child(child.name, positions[index]), component.name)
            }
        }
        timeZones()
        if (rules.method != null) itip()
    }

    private fun report(code: ProblemCode, path: ElementPath, span: SourceSpan?) {
        issues += ValidationIssue(code, code.severity, path, span)
    }

    // The path of a property that is the [index]-th of its name in the component; the first of a name is 0.
    private fun propertyPath(path: ElementPath, property: ICalProperty, index: Int = 0): ElementPath =
        path.child(property.name, index)

    private fun check(component: ICalComponent, path: ElementPath, parent: String?) {
        val name = component.name
        val key = tableKey(component, path)
        val table = rules.cardinality[key]
        if (table != null) cardinality(component, path, table)
        if (parent != null) {
            val allowed = rules.children[parent]
            if (allowed != null && name in rules.children && name !in allowed) {
                report(ProblemCode.ComponentNotAllowed, path, component.span)
            }
        }
        for (constraint in rules.constraints) {
            if (constraint.component == name) constraint(component, path, constraint)
        }
        if (table != null) values(component, path)
        val overlay = rules.itipProperties[name]
        if (overlay != null && name != "VCALENDAR") presence(component, path, overlay)
        controlCharacters(component, path)
        for (rule in rules.custom) issues += rule.validate(component, path)
    }

    // RFC 5545 §3.1: no CONTROL character but HTAB in a content line. Parsing keeps such a line and every
    // writer that writes text anew refuses it, so a calendar that holds one is not valid. The positions among
    // namesakes come from one sort, and only when there is something to report.
    private fun controlCharacters(component: ICalComponent, path: ElementPath) {
        val properties = component.propertyNodes
        if (properties.none { it.hasControlCharacter() }) return
        val positions = namesakeIndices(properties.size) { properties[it].name }
        for (index in properties.indices) {
            val property = properties[index]
            if (property.hasControlCharacter()) {
                report(ProblemCode.ControlCharacter, propertyPath(path, property, positions[index]), property.span)
            }
        }
    }

    private fun tableKey(component: ICalComponent, path: ElementPath): String {
        if (component.name != "VALARM") return component.name
        val action = component.property("ACTION")?.value?.trim()?.uppercase() ?: return "VALARM-AUDIO"
        val key = "VALARM-$action"
        if (rules.cardinality.containsKey(key)) return key
        report(ProblemCode.UnknownAlarmAction, path, component.property("ACTION")?.span)
        return "VALARM-AUDIO"
    }

    private fun cardinality(component: ICalComponent, path: ElementPath, table: Map<String, String>) {
        val properties = component.propertyNodes
        val indices = namesakeIndices(properties.size) { properties[it].name }
        val firsts = HashMap<String, ICalProperty>()
        val seconds = HashMap<String, ICalProperty>()
        for (position in properties.indices) {
            val property = properties[position]
            if (property.name !in table) continue
            when (indices[position]) {
                0 -> firsts[property.name] = property
                1 -> seconds[property.name] = property
            }
        }
        for ((property, code) in table) {
            val first = firsts[property]
            val second = seconds[property]
            when {
                first == null && (code == "1" || code == "+") ->
                    report(ProblemCode.MissingProperty, path.child(property, 0), component.span)

                second != null && (code == "1" || code == "?") ->
                    report(ProblemCode.TooManyProperties, propertyPath(path, second, 1), second.span)

                second != null && code == "?s" ->
                    report(ProblemCode.RepeatedProperty, propertyPath(path, second, 1), second.span)

                first != null && code == FORBIDDEN ->
                    report(ProblemCode.PropertyNotAllowed, propertyPath(path, first), first.span)
            }
        }
        for (position in properties.indices) {
            val property = properties[position]
            val name = property.name
            if (name in table || name.startsWith("X-")) continue
            val known = registry.property(name) != null || name in tabledNames
            val code = if (known) ProblemCode.PropertyNotAllowed else ProblemCode.UnknownProperty
            report(code, propertyPath(path, property, indices[position]), property.span)
        }
    }

    private val tabledNames: Set<String> by lazy { rules.cardinality.values.flatMapTo(HashSet()) { it.keys } }

    private fun values(component: ICalComponent, path: ElementPath) {
        val properties = component.propertyNodes
        var indices: IntArray? = null
        for (position in properties.indices) {
            val property = properties[position]
            val definition = registry.property(property.nameId, property.name, component.name) ?: continue

            @Suppress("UNCHECKED_CAST")
            val decoded = property.decodeWith(definition.key.codec as PropertyCodec<Any>, DecodeContext.Lenient)
            if (decoded is Decoded.Malformed) {
                val known = indices ?: namesakeIndices(properties.size) { properties[it].name }.also { indices = it }
                report(ProblemCode.MalformedValue, propertyPath(path, property, known[position]), property.span)
            }
        }
    }

    private fun presence(component: ICalComponent, path: ElementPath, table: Map<String, String>) {
        for ((name, code) in table) {
            val nodes = component.propertyNodes.filter { it.name == name }
            val children = component.componentNodes.filter { it.name == name }
            val count = nodes.size + children.size
            val span = nodes.firstOrNull()?.span ?: children.firstOrNull()?.span ?: component.span
            val at = if (nodes.isNotEmpty()) propertyPath(path, nodes.first()) else path.child(name, 0)
            when {
                count == 0 && (code == "1" || code == "+") -> report(ProblemCode.MissingProperty, at, component.span)

                count > 1 && (code == "1" || code == "?") -> report(ProblemCode.TooManyProperties, at, span)

                count > 0 && code == FORBIDDEN && children.isNotEmpty() -> report(
                    ProblemCode.ComponentNotAllowed,
                    at,
                    span,
                )

                count > 0 && code == FORBIDDEN -> report(ProblemCode.PropertyNotAllowed, at, span)
            }
        }
    }

    private fun constraint(component: ICalComponent, path: ElementPath, constraint: Constraint) {
        val names = constraint.names
        when (constraint.rule) {
            "has-component" -> if (component.componentNodes.isEmpty()) {
                report(ProblemCode.MissingComponent, path, component.span)
            }

            "has-child" -> if (component.componentNodes.none { it.name in names }) {
                report(ProblemCode.MissingComponent, path.child(names[0], 0), component.span)
            }

            "exclusive" -> {
                val present = names.mapNotNull { name -> component.property(name) }
                if (present.size > 1) {
                    report(ProblemCode.ExclusiveProperties, propertyPath(path, present[1]), present[1].span)
                }
            }

            "required-without-method" -> if (!hasMethod && component.property(names[0]) == null) {
                report(ProblemCode.MissingProperty, path.child(names[0], 0), component.span)
            }

            "requires" -> {
                val first = component.property(names[0])
                if (first != null && names.drop(1).any { component.property(it) == null }) {
                    report(ProblemCode.MissingDependency, propertyPath(path, first), first.span)
                }
            }

            "together" -> {
                val present = names.mapNotNull { component.property(it) }
                if (present.isNotEmpty() && present.size < names.size) {
                    report(ProblemCode.MissingDependency, propertyPath(path, present[0]), present[0].span)
                }
            }

            "same-type" -> sameType(component, path, names[0], names[1])

            "after" -> after(component, path, names[0], names[1])

            "date-duration" -> {
                val start = component[Properties.DTSTART]
                val duration = component[Properties.DURATION]
                val property = component.property("DURATION")
                if (start is ICalDateTime.Date && duration != null && property != null &&
                    (duration.hours != 0 || duration.minutes != 0 || duration.seconds != 0)
                ) {
                    report(ProblemCode.DateWithTimeDuration, propertyPath(path, property), property.span)
                }
            }

            "until-type" -> untilType(component, path)

            "values" -> {
                val property = component.property(names[0]) ?: return
                if (property.value.trim().uppercase() !in names.drop(1)) {
                    report(ProblemCode.ValueNotAllowed, propertyPath(path, property), property.span)
                }
            }

            "range" -> {
                val property = component.property(names[0]) ?: return
                val value = property.value.trim().toIntOrNull() ?: return
                if (value < names[1].toInt() || value > names[2].toInt()) {
                    report(ProblemCode.ValueOutOfRange, propertyPath(path, property), property.span)
                }
            }

            "local-time" -> {
                val property = component.property(names[0]) ?: return
                if (component[Properties.DTSTART] !is ICalDateTime.Floating) {
                    report(ProblemCode.NotLocalTime, propertyPath(path, property), property.span)
                }
            }
        }
    }

    private fun dateTime(component: ICalComponent, name: String): ICalDateTime? = when (name) {
        "DTSTART" -> component[Properties.DTSTART]
        "DTEND" -> component[Properties.DTEND]
        "DUE" -> component[Properties.DUE]
        "RECURRENCE-ID" -> component[Properties.RECURRENCE_ID]?.value
        else -> null
    }

    private fun form(value: ICalDateTime): Int = when (value) {
        is ICalDateTime.Date -> 0
        is ICalDateTime.Floating -> 1
        else -> 2
    }

    private fun sameType(component: ICalComponent, path: ElementPath, name: String, other: String) {
        val value = dateTime(component, name) ?: return
        val reference = dateTime(component, other) ?: return
        if (form(value) != form(reference)) {
            val property = component.property(name)!!
            report(ProblemCode.ValueTypeMismatch, propertyPath(path, property), property.span)
        }
    }

    private fun after(component: ICalComponent, path: ElementPath, name: String, other: String) {
        val end = dateTime(component, name) ?: return
        val start = dateTime(component, other) ?: return
        val ordered = compare(end, start) ?: return
        if (ordered <= 0) {
            val property = component.property(name)!!
            report(ProblemCode.EndBeforeStart, propertyPath(path, property), property.span)
        }
    }

    private fun compare(a: ICalDateTime, b: ICalDateTime): Int? = when {
        a is ICalDateTime.Date && b is ICalDateTime.Date -> a.date.compareTo(b.date)

        a is ICalDateTime.Floating && b is ICalDateTime.Floating -> a.dateTime.compareTo(b.dateTime)

        a is ICalDateTime.Zoned && b is ICalDateTime.Zoned && a.tzid == b.tzid -> a.dateTime.compareTo(b.dateTime)

        a is ICalDateTime.Utc && b is ICalDateTime.Utc -> a.instant.compareTo(b.instant)

        else -> {
            val left = a.toInstant(zones)
            val right = b.toInstant(zones)
            if (left == null || right == null) null else left.compareTo(right)
        }
    }

    private fun untilType(component: ICalComponent, path: ElementPath) {
        val start = component[Properties.DTSTART] ?: return
        var index = -1
        for (property in component.propertyNodes) {
            if (property.name != "RRULE") continue
            index++
            @Suppress("UNCHECKED_CAST")
            val decoded = property.decodeWith(Properties.RRULE.codec, DecodeContext.Lenient)
            val until = (decoded as? Decoded.Value)?.value?.until ?: continue
            val expected = when (start) {
                is ICalDateTime.Date -> until is ICalDateTime.Date
                is ICalDateTime.Floating -> until is ICalDateTime.Floating
                else -> until is ICalDateTime.Utc
            }
            if (!expected) report(ProblemCode.ValueTypeMismatch, propertyPath(path, property, index), property.span)
        }
    }

    // The first use of each TZID without a VTIMEZONE, in breadth-first order; TZIDs are sorted, not hashed.
    private fun timeZones() {
        val defined = ArrayList<String>()
        for (child in root.componentNodes) {
            if (child.name == "VTIMEZONE") child.property("TZID")?.let { defined += it.value }
        }
        defined.sort()
        val missing = ArrayList<String>()
        val at = ArrayList<ElementPath>()
        val spans = ArrayList<SourceSpan?>()
        val pending = ArrayList<Pair<ICalComponent, ElementPath>>()
        val top = root.componentNodes
        val topIndices = namesakeIndices(top.size) { top[it].name }
        val rootPath = ElementPath.Root.child(root.name, 0)
        for (position in top.indices) {
            val child = top[position]
            if (child.name != "VTIMEZONE") pending += child to rootPath.child(child.name, topIndices[position])
        }
        var next = 0
        while (next < pending.size) {
            val (component, path) = pending[next++]
            val properties = component.propertyNodes
            var indices: IntArray? = null
            for (position in properties.indices) {
                val property = properties[position]
                val tzid = property.parameter("TZID")?.value ?: continue
                if (defined.binarySearch(tzid) >= 0) continue
                val known = indices ?: namesakeIndices(properties.size) { properties[it].name }.also { indices = it }
                missing += tzid
                at += propertyPath(path, property, known[position]).parameter("TZID")
                spans += property.span
            }
            val children = component.componentNodes
            val childIndices = namesakeIndices(children.size) { children[it].name }
            for (position in children.indices) {
                pending += children[position] to path.child(children[position].name, childIndices[position])
            }
        }
        val first = firstOfEach(missing.size) { a, b -> missing[a].compareTo(missing[b]) }
        for (index in missing.indices) {
            if (first[index]) report(ProblemCode.MissingTimeZone, at[index], spans[index])
        }
    }

    private fun itip() {
        val method = rules.method ?: return
        val rootPath = ElementPath.Root.child(root.name, 0)
        val declared = root.property("METHOD")
        if (declared == null || declared.value.trim().uppercase() != method.value) {
            report(ProblemCode.MethodMismatch, rootPath.child("METHOD", 0), declared?.span ?: root.span)
        }
        rules.itipProperties["VCALENDAR"]?.let { presence(root, rootPath, it) }
        val main = root.componentNodes.firstOrNull { it.name != "VTIMEZONE" && !it.name.startsWith("X-") } ?: return
        val table = rules.itipCalendar[main.name]
        if (table == null) {
            report(ProblemCode.MethodNotDefined, rootPath.child(main.name, 0), main.span)
            return
        }
        presence(root, rootPath, table.filterKeys { it != "VTIMEZONE" || table[it] == FORBIDDEN })
    }

    override fun toString(): String = "Validation"
}

// By code, then by path: the order of the check for "one issue per code and path".
private fun compareIssues(a: ValidationIssue, b: ValidationIssue): Int {
    var result = a.code.id.compareTo(b.code.id)
    if (result != 0) return result
    val left = a.path.segments
    val right = b.path.segments
    for (index in 0 until minOf(left.size, right.size)) {
        result = left[index].name.compareTo(right[index].name)
        if (result != 0) return result
        result = left[index].index.compareTo(right[index].index)
        if (result != 0) return result
    }
    result = left.size.compareTo(right.size)
    if (result != 0) return result
    val first = a.path.parameter
    val second = b.path.parameter
    return when {
        first == second -> 0
        first == null -> -1
        second == null -> 1
        else -> first.compareTo(second)
    }
}
