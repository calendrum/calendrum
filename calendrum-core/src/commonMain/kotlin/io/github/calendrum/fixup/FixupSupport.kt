package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.SortedStringMap
import io.github.calendrum.model.NodeBuilder
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.edit
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.controlIndex
import io.github.calendrum.syntax.isName
import io.github.calendrum.syntax.isWritable
import io.github.calendrum.validate.ElementPath

internal class Changes(private val fixup: String) {
    val list = ArrayList<FixupChange>()

    fun add(path: ElementPath) {
        list += FixupChange(fixup, path)
    }
}

@OptIn(ExperimentalICalApi::class)
internal class TreeFixup(
    override val id: String,
    private val lines: List<() -> LineInterceptor> = emptyList(),
    private val transform: (ICalComponent, Changes) -> ICalComponent,
) : Fixup {
    override fun interceptors(): List<() -> LineInterceptor> = lines

    override fun apply(calendar: VCalendar): FixupResult {
        val changes = Changes(id)
        val result = transform(calendar.component, changes)
        return FixupResult(if (result === calendar.component) calendar else VCalendar(result), changes.list)
    }

    override fun toString(): String = "Fixup($id)"
}

@OptIn(ExperimentalICalApi::class)
internal class ComposedFixup(private val parts: List<Fixup>) : Fixup {
    override val id: String = parts.joinToString("+") { it.id }

    override fun interceptors(): List<() -> LineInterceptor> = parts.flatMap { it.interceptors() }

    override fun apply(calendar: VCalendar): FixupResult {
        var current = calendar
        val changes = ArrayList<FixupChange>()
        for (part in parts) {
            val result = part.apply(current)
            current = result.calendar
            changes += result.changes
        }
        return FixupResult(current, changes)
    }

    override fun toString(): String = "Fixup($id)"
}

internal val ROOT_PATH: ElementPath = ElementPath.Root.child("VCALENDAR", 0)

// Names come from the input, so the indexes among namesakes are counted in trees, not hashed (see InputKeys.kt).
// Children are fixed before their parent; a property transform (`mapProperties`) returns the same property (no change),
// null (removed) or a replacement, and each change is recorded with its path.
internal fun rewrite(
    component: ICalComponent,
    path: ElementPath,
    block: (ICalComponent, ElementPath) -> ICalComponent,
): ICalComponent {
    val counts = SortedStringMap<Int>()
    var changed = false
    val children = component.components.map { child ->
        val index = counts[child.name] ?: 0
        counts[child.name] = index + 1
        val result = rewrite(child, path.child(child.name, index), block)
        if (result !== child) changed = true
        result
    }
    val withChildren = if (changed) component.edit { children(children) } else component
    return block(withChildren, path)
}

internal fun ICalComponent.mapProperties(
    path: ElementPath,
    changes: Changes,
    transform: (ICalProperty) -> ICalProperty?,
): ICalComponent {
    var builder: NodeBuilder? = null
    val counts = SortedStringMap<Int>()
    var removed = 0
    for ((position, property) in properties.withIndex()) {
        val index = counts[property.name] ?: 0
        counts[property.name] = index + 1
        val result = transform(property)
        if (result === property) continue
        val target = builder ?: NodeBuilder(name, this).also { builder = it }
        if (result == null) {
            target.removeAt(position - removed)
            removed += 1
        } else {
            target.replaceAt(position - removed, result)
        }
        changes.add(path.child(property.name, index))
    }
    return builder?.build() ?: this
}

// Parsing keeps what the builder and canonical writing refuse: a name that is not a token, a control character,
// a parameter without a value. Such a property cannot be rebuilt, and a rebuilt node has no source bytes for
// byte-stable writing, so a fixup leaves it as it was instead of throwing or leaving a node no writer accepts
// (found by fuzzing).
internal fun ICalProperty.copy(
    name: String = this.name,
    value: String = this.value,
    parameters: List<ICalParameter> = this.parameters,
): ICalProperty {
    if (!isName(name) || controlIndex(value) >= 0 || !parameters.all(::isWritable)) return this
    val builder = ICalProperty.builder(name)
    for (parameter in parameters) builder.parameter(parameter)
    return builder.value(value).build()
}

internal fun ICalProperty.withParameter(name: String, vararg values: String): ICalProperty =
    copy(parameters = parameters.filter { it.name != name } + ICalParameter.of(name, *values))

internal fun ICalProperty.withoutParameters(vararg names: String): ICalProperty =
    copy(parameters = parameters.filter { it.name !in names })

internal val TEXT_PROPERTIES = setOf(
    "SUMMARY", "DESCRIPTION", "LOCATION", "COMMENT", "CONTACT", "RESOURCES", "CATEGORIES", "TZNAME", "NAME",
)

internal val NON_TEXT_PROPERTIES = setOf(
    "DTSTART", "DTEND", "DUE", "DURATION", "RRULE", "RDATE", "EXDATE", "EXRULE", "RECURRENCE-ID", "ORGANIZER",
    "ATTENDEE", "URL", "GEO", "PRIORITY", "SEQUENCE", "STATUS", "CLASS", "TRANSP", "CREATED", "LAST-MODIFIED",
    "DTSTAMP", "TRIGGER", "REPEAT", "PERCENT-COMPLETE", "COMPLETED", "TZOFFSETFROM", "TZOFFSETTO", "TZID",
)

internal val DATE_PROPERTIES = setOf("DTSTART", "DTEND", "DUE", "RECURRENCE-ID", "EXDATE", "RDATE")
