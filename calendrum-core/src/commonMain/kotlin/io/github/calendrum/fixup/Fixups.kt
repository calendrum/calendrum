package io.github.calendrum.fixup

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.SortedStringMap
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.edit
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.InterceptedLine
import io.github.calendrum.syntax.LineInterceptor
import io.github.calendrum.syntax.controlIndex
import io.github.calendrum.tz.TimeZoneAliases
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The built-in fixes for calendars from well-known producers, and a way to combine them.
 *
 * Each fix repairs one thing real feeds get wrong and reports what it changed. They are also the repairs behind the
 * lint codes `ICAL-L021` to `ICAL-L025`.
 *
 * @sample io.github.calendrum.samples.fixVendorCalendar
 */
public object Fixups {
    /**
     * Renames `TZID`s that are Windows or Outlook names, IANA links or `/mozilla.org/` prefixes to the IANA identifier,
     * in `VTIMEZONE` components and in parameters, and merges `VTIMEZONE` duplicates that result. Guessed names are
     * left alone.
     */
    @JvmField
    public val windowsTzids: Fixup = TreeFixup("windows-tzids") { root, changes -> windowsTzids(root, changes) }

    /**
     * Gives the floating `DTSTART`, `DTEND`, `DUE`, `RECURRENCE-ID`, `RDATE` and `EXDATE` values of components the
     * `TZID` named by the non-standard `X-WR-TIMEZONE` of the calendar, as Google Calendar and Apple Calendar mean it.
     */
    @JvmField
    public val xWrTimezone: Fixup = TreeFixup("x-wr-timezone") { root, changes -> xWrTimezone(root, changes) }

    /**
     * Adds `NAME` and `DESCRIPTION` of RFC 7986 from `X-WR-CALNAME` and `X-WR-CALDESC` when the standard properties are
     * missing.
     */
    @JvmField
    public val xWrCalendarName: Fixup = TreeFixup("x-wr-calname") { root, changes -> xWrCalendarName(root, changes) }

    /**
     * Repairs for Microsoft Outlook and Exchange feeds: Windows time zone names ([windowsTzids]), all-day events
     * written as midnight date-times (`X-MICROSOFT-CDO-ALLDAYEVENT`) and upper-case `MAILTO:`.
     */
    @JvmField
    public val outlook: Fixup = ComposedFixup(
        listOf(windowsTzids, TreeFixup("outlook") { root, changes -> outlookSpecific(root, changes) }),
    )

    /**
     * Repairs for Lotus Notes and Domino feeds: Windows time zone names and empty values of non-text properties such as
     * `DTEND:`.
     */
    @JvmField
    public val lotusNotes: Fixup = ComposedFixup(
        listOf(
            windowsTzids,
            TreeFixup("lotus-notes", listOf { EmptyValueDropper }) { root, changes -> emptyValues(root, changes) },
        ),
    )

    /**
     * Repairs for Google Calendar feeds: `X-WR-TIMEZONE` ([xWrTimezone]), `X-WR-CALNAME` ([xWrCalendarName]), a `UNTIL`
     * with a time on a rule of an all-day event, and a `CONFERENCE` made from `X-GOOGLE-CONFERENCE`.
     */
    @JvmField
    public val google: Fixup = ComposedFixup(
        listOf(xWrTimezone, xWrCalendarName, TreeFixup("google") { root, changes -> googleSpecific(root, changes) }),
    )

    /**
     * Repairs for vCalendar 1.0 (`VERSION:1.0`) files: `DCREATED` as `CREATED`, quoted-printable text decoded,
     * vCard-style attendee addresses and `STATUS`, `RSVP`, `EXPECT` and `ROLE` parameters mapped to those of iCalendar.
     */
    @JvmField
    public val vcalendar10: Fixup = TreeFixup("vcalendar10", listOf { VCalendar10Lines }) { root, changes ->
        vcalendar10(root, changes)
    }

    /**
     * Returns a fix that picks the right built-in repair from the `PRODID` (and `VERSION:1.0`) of the calendar:
     * Outlook, Lotus Notes or Google. A vCalendar 1.0 file gets the vCalendar 1.0 repair and then that of its producer,
     * so that applying the fix again changes nothing. A calendar from another producer is left as it is.
     */
    @JvmStatic
    public fun byProdId(): Fixup = ByProdId

    /** Combines [fixups] into one that applies them in order. Its changes are those of all of them. */
    @JvmStatic
    public fun compose(vararg fixups: Fixup): Fixup = ComposedFixup(fixups.toList())

    /** Returns the name of the object. */
    override fun toString(): String = "Fixups"
}

// A vCalendar 1.0 file gets the repair of its producer after that of vCalendar 1.0: the latter writes VERSION:2.0, so a
// second pass picks the producer alone and must find nothing left to do (found by fuzzing).
internal fun choose(prodId: String?, version: String?): Fixup? {
    val producer = producerOf(prodId)
    if (version?.trim() != "1.0") return producer
    return if (producer == null) Fixups.vcalendar10 else ComposedFixup(listOf(Fixups.vcalendar10, producer))
}

private fun producerOf(prodId: String?): Fixup? {
    val text = prodId?.lowercase() ?: return null
    return when {
        "microsoft" in text || "outlook" in text || "exchange" in text -> Fixups.outlook
        "lotus" in text || "domino" in text || "notes" in text -> Fixups.lotusNotes
        "google" in text -> Fixups.google
        else -> null
    }
}

@OptIn(ExperimentalICalApi::class)
private object ByProdId : Fixup {
    override val id: String = "by-prodid"

    override fun interceptors(): List<() -> LineInterceptor> = listOf { ProdIdLines() }

    override fun apply(calendar: VCalendar): FixupResult =
        choose(calendar.prodId, calendar.version)?.apply(calendar) ?: FixupResult(calendar, emptyList())

    override fun toString(): String = "Fixup($id)"
}

// Watches PRODID and VERSION of the calendar while parsing and passes the following lines to the interceptors of the
// fixup they select.
@OptIn(ExperimentalICalApi::class)
private class ProdIdLines : LineInterceptor {
    private var prodId: String? = null
    private var version: String? = null
    private var delegates: List<LineInterceptor>? = null

    override fun intercept(line: InterceptedLine) {
        if (line.depth <= 1 && line.componentName == "VCALENDAR") {
            when (line.name) {
                "PRODID" -> prodId = line.value.toString()
                "VERSION" -> version = line.value.toString()
            }
            delegates = choose(prodId, version)?.interceptors()?.map { it() }
            return
        }
        delegates?.forEach { it.intercept(line) }
    }
}

@OptIn(ExperimentalICalApi::class)
private object EmptyValueDropper : LineInterceptor {
    override fun intercept(line: InterceptedLine) {
        if (line.value.isEmpty() && line.name in NON_TEXT_PROPERTIES) line.drop()
    }
}

// TZIDs come from the input: kept in trees, not hashed (see InputKeys.kt).
private fun windowsTzids(root: ICalComponent, changes: Changes): ICalComponent {
    val names = ArrayList<String>()
    collectZones(root, names)
    names.sort()
    val mapping = SortedStringMap<String>()
    for ((index, name) in names.withIndex()) {
        if (index > 0 && name == names[index - 1]) continue
        val match = TimeZoneAliases.resolve(name) ?: continue
        if (!match.guessed && match.ianaId != name) mapping[name] = match.ianaId
    }
    if (mapping.size == 0) return root
    val renamed = rewrite(root, ROOT_PATH) { component, path ->
        component.mapProperties(path, changes) { property ->
            val tzid = property.parameter("TZID")?.value
            when {
                component.name == "VTIMEZONE" && property.name == "TZID" ->
                    mapping[property.value]?.let { property.copy(value = it) } ?: property

                tzid != null -> mapping[tzid]?.let { property.withParameter("TZID", it) } ?: property

                else -> property
            }
        }
    }
    val seen = SortedStringMap<Unit>()
    val children = renamed.components.filter { child ->
        if (child.name != "VTIMEZONE") return@filter true
        val tzid = child.property("TZID")?.value ?: ""
        if (seen.containsKey(tzid)) return@filter false
        seen[tzid] = Unit
        true
    }
    return if (children.size == renamed.components.size) renamed else renamed.edit { children(children) }
}

private fun collectZones(component: ICalComponent, into: MutableList<String>) {
    if (component.name == "VTIMEZONE") component.property("TZID")?.value?.let { into += it }
    for (property in component.properties) property.parameter("TZID")?.value?.let { into += it }
    for (child in component.components) collectZones(child, into)
}

// Only floating DATE-TIME values get the TZID of X-WR-TIMEZONE; UTC and DATE values keep their meaning.
private fun xWrTimezone(root: ICalComponent, changes: Changes): ICalComponent {
    val name = root.property("X-WR-TIMEZONE")?.value?.trim() ?: return root
    val match = TimeZoneAliases.resolve(name) ?: return root
    if (match.guessed) return root
    val zone = match.ianaId
    return rewrite(root, ROOT_PATH) { component, path ->
        if (component.name == "VTIMEZONE" || component.name == "STANDARD" || component.name == "DAYLIGHT") {
            component
        } else {
            component.mapProperties(path, changes) { property ->
                if (property.name in DATE_PROPERTIES &&
                    isFloating(property)
                ) {
                    property.withParameter("TZID", zone)
                } else {
                    property
                }
            }
        }
    }
}

internal fun isFloating(property: ICalProperty): Boolean {
    if (property.parameter("TZID") != null) return false
    val type = property.parameter("VALUE")?.value?.uppercase()
    if (type != null && type != "DATE-TIME") return false
    val values = property.value.split(',')
    return values.all { it.length == 15 && it[8] == 'T' && it.all { char -> char.isDigit() || char == 'T' } }
}

private fun xWrCalendarName(root: ICalComponent, changes: Changes): ICalComponent {
    val additions = ArrayList<ICalProperty>()
    val name = root.property("X-WR-CALNAME")?.value
    // A value with a control character cannot be copied into a new property.
    if (name != null && controlIndex(name) < 0 && root.property("NAME") == null) {
        additions += ICalProperty.of("NAME", name)
    }
    val description = root.property("X-WR-CALDESC")?.value
    if (description != null && controlIndex(description) < 0 && root.property("DESCRIPTION") == null) {
        additions += ICalProperty.of("DESCRIPTION", description)
    }
    if (additions.isEmpty()) return root
    for (property in additions) changes.add(ROOT_PATH.child(property.name, 0))
    return root.edit { for (property in additions) property(property) }
}

private fun outlookSpecific(root: ICalComponent, changes: Changes): ICalComponent =
    rewrite(root, ROOT_PATH) { component, path ->
        val allDay = component.property("X-MICROSOFT-CDO-ALLDAYEVENT")?.value.equals("TRUE", ignoreCase = true)
        component.mapProperties(path, changes) { property ->
            when {
                allDay && (property.name == "DTSTART" || property.name == "DTEND") && atMidnight(property) ->
                    property.copy(
                        value = property.value.substring(0, 8),
                        parameters = property.parameters.filter { it.name != "TZID" && it.name != "VALUE" },
                    ).withParameter("VALUE", "DATE")

                else -> lowerMailto(property)
            }
        }
    }

private fun atMidnight(property: ICalProperty): Boolean {
    val value = property.value
    return value.length == 15 && value.endsWith("T000000") && property.parameter("VALUE")?.value != "DATE"
}

internal fun lowerMailto(property: ICalProperty): ICalProperty {
    if (property.name != "ORGANIZER" && property.name != "ATTENDEE") return property
    val value = property.value
    if (value.length < 7 || !value.regionMatches(0, "mailto:", 0, 7, ignoreCase = true) ||
        value.startsWith("mailto:")
    ) {
        return property
    }
    return property.copy(value = "mailto:" + value.substring(7))
}

internal fun emptyValues(root: ICalComponent, changes: Changes): ICalComponent =
    rewrite(root, ROOT_PATH) { component, path ->
        component.mapProperties(path, changes) { property ->
            if (property.value.isEmpty() && property.name in NON_TEXT_PROPERTIES) null else property
        }
    }

private fun googleSpecific(root: ICalComponent, changes: Changes): ICalComponent =
    rewrite(root, ROOT_PATH) { component, path ->
        val hasConference = component.property("CONFERENCE") != null
        val fixed = untilTypes(component, path, changes)
        val link = fixed.property("X-GOOGLE-CONFERENCE")?.value
        if (hasConference || link == null || link.isEmpty() || controlIndex(link) >= 0) {
            fixed
        } else {
            changes.add(path.child("CONFERENCE", 0))
            fixed.edit {
                property(
                    ICalProperty.builder("CONFERENCE").parameter("VALUE", "URI").parameter("FEATURE", "VIDEO")
                        .value(link).build(),
                )
            }
        }
    }

internal fun untilTypes(
    component: ICalComponent,
    path: io.github.calendrum.validate.ElementPath,
    changes: Changes,
): ICalComponent {
    val start = component.property("DTSTART") ?: return component
    val dateStart = start.parameter("VALUE")?.value?.uppercase() == "DATE" || start.value.length == 8
    if (!dateStart) return component
    return component.mapProperties(path, changes) { property ->
        if (property.name != "RRULE" && property.name != "EXRULE") return@mapProperties property
        val parts = property.value.split(';')
        val until = parts.indexOfFirst { it.startsWith("UNTIL=", ignoreCase = true) }
        if (until < 0 || parts[until].length <= 14) return@mapProperties property
        val date = parts[until].substring(6, 14)
        property.copy(value = parts.toMutableList().also { it[until] = "UNTIL=$date" }.joinToString(";"))
    }
}
