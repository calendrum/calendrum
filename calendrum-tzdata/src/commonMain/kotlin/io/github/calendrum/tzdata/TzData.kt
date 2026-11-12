package io.github.calendrum.tzdata

import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.model.VTimeZone
import io.github.calendrum.problemOf
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.VTimeZoneSource
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneAliases
import io.github.calendrum.tz.TimeZoneResolver
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import io.github.calendrum.value.WeekdayNum
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.time.Instant

/** How [TzData.vtimezone] describes a zone. */
public enum class VTimeZoneMode {
    /**
     * Lists the observed offset changes, with explicit dates as `RDATE`s for the history and rules for the future:
     * exact, but longer.
     */
    Full,

    /**
     * Uses one `RRULE` per observance, which is the shape Outlook and Exchange expect for the current rules of the
     * zone. Where the current rules cannot be written like that, `ICAL-Z005` is reported and the result only
     * approximates them.
     */
    OutlookCompatible,
}

/**
 * The IANA time zone database, packed into the library (the pinned release is listed in `NOTICE`): every zone, as a
 * resolver and as a source of `VTIMEZONE` components. It gives the same results on every platform, since it does not
 * use the platform database.
 *
 * Add [resolver] to a chain of time zone resolvers to resolve `TZID`s with it, or pass [TzData] to
 * `WriteOptions.Builder.addMissingTimeZones` to write the `VTIMEZONE` components a calendar needs.
 */
public object TzData : VTimeZoneSource {
    /** The release of the IANA time zone database in this module, such as `2026b`. */
    @JvmStatic
    public val version: String get() = TZDB_VERSION

    /**
     * A resolver for IANA zone names, and for the aliases of the library (Windows names and links), backed by this
     * module's tables.
     */
    @JvmStatic
    public val resolver: TimeZoneResolver get() = TzDataResolver

    /** The identifiers of all zones in the database. */
    @JvmStatic
    public val zoneIds: Set<String> get() = TzZones.names()

    /**
     * Returns the zone with the identifier [tzid], or `null` when the database has no such zone. Aliases are not
     * resolved here; use [resolver].
     *
     * @param tzid an IANA identifier such as `Europe/Berlin`.
     */
    @JvmStatic
    public fun zone(tzid: String): ICalTimeZone? = TzZones.record(tzid)?.zone(tzid)

    /**
     * Returns a `VTIMEZONE` component for [tzid] in the given [mode], or `null` when the zone is unknown.
     *
     * @param tzid an IANA identifier, or an alias the library knows.
     * @param mode how to describe the rules.
     * @param from the first moment the component must cover, or `null` for the whole history; earlier history is left
     *   out in [VTimeZoneMode.Full].
     */
    @JvmStatic
    @JvmOverloads
    public fun vtimezone(tzid: String, mode: VTimeZoneMode, from: Instant? = null): VTimeZone? =
        vtimezone(tzid, mode, from, null)

    /**
     * Returns a `VTIMEZONE` component for [tzid] in the given [mode] and reports `ICAL-Z005` to [problemListener] when
     * [VTimeZoneMode.OutlookCompatible] can only approximate the rules.
     *
     * @param tzid an IANA identifier, or an alias the library knows.
     * @param mode how to describe the rules.
     * @param from the first moment the component must cover, or `null` for the whole history.
     * @param problemListener receives `ICAL-Z005`, or `null`.
     */
    @JvmStatic
    public fun vtimezone(
        tzid: String,
        mode: VTimeZoneMode,
        from: Instant?,
        problemListener: ProblemListener?,
    ): VTimeZone? {
        val record = recordOf(tzid) ?: return null
        if (mode == VTimeZoneMode.OutlookCompatible && problemListener != null && !followsTailRules(tzid, record)) {
            problemListener.onProblem(problemOf(ProblemCode.IrregularTimeZone, null))
        }
        return when (mode) {
            VTimeZoneMode.Full -> fullZone(tzid, record, from?.epochSeconds ?: Long.MIN_VALUE)
            VTimeZoneMode.OutlookCompatible -> outlookZone(tzid, record)
        }
    }

    /**
     * Returns a `VTIMEZONE` component for [tzid], or `null` for an unknown zone: the Outlook-compatible form when the
     * zone follows its current rules with one `RRULE` each, and the full form otherwise.
     *
     * @param tzid an IANA identifier, or an alias the library knows.
     */
    override fun vtimezone(tzid: String): ICalComponent? {
        val record = recordOf(tzid) ?: return null
        val zone = if (followsTailRules(
                tzid,
                record,
            )
        ) {
            outlookZone(tzid, record)
        } else {
            fullZone(tzid, record, Long.MIN_VALUE)
        }
        return zone.component
    }

    private fun recordOf(tzid: String): ZoneRecord? =
        TzZones.record(tzid) ?: TimeZoneAliases.resolve(tzid)?.let { TzZones.record(it.ianaId) }

    /** Returns the name and the release of the database. */
    override fun toString(): String = "TzData($TZDB_VERSION)"
}

internal object TzDataResolver : TimeZoneResolver {
    override fun resolve(tzid: String, context: ResolveContext): ICalTimeZone? {
        TzData.zone(tzid)?.let { return it }
        val alias = TimeZoneAliases.resolve(tzid) ?: return null
        val zone = TzData.zone(alias.ianaId) ?: return null
        TimeZoneAliases.report(alias, context)
        return RenamedZone(tzid, zone)
    }

    override fun toString(): String = "TzData.resolver"
}

internal class RenamedZone(override val id: String, private val zone: ICalTimeZone) : ICalTimeZone {
    override fun offsetAt(instant: Instant): UtcOffset = zone.offsetAt(instant)

    override fun toInstant(local: LocalDateTime): Instant = zone.toInstant(local)

    override fun transitions(from: Instant, to: Instant): List<io.github.calendrum.tz.ZoneTransition> =
        zone.transitions(from, to)

    override fun toString(): String = "ICalTimeZone($id)"
}

private fun fullZone(tzid: String, record: ZoneRecord, from: Long): VTimeZone {
    val builder = VTimeZone.builder().tzid(tzid)
    val explicitEnd = if (record.tail.isEmpty()) {
        Long.MAX_VALUE
    } else {
        epochDay(record.tailStartYear, 1, 1) *
            SECONDS_PER_DAY
    }
    var offsetBefore = record.initialOffset
    var firstIndex = 0
    while (firstIndex < record.times.size && record.times[firstIndex] < from) {
        offsetBefore = record.offsets[record.indexes[firstIndex]]
        firstIndex++
    }
    val groups = LinkedHashMap<String, MutableList<ICalDateTime>>()
    val keys = LinkedHashMap<String, IntArray>()
    run {
        val index = if (firstIndex == 0) record.initial else record.indexes[firstIndex - 1]
        val start = if (from == Long.MIN_VALUE) EPOCH_1970 else from
        val key = "${record.offsets[index]}|${record.offsets[index]}|$index"
        groups.getOrPut(key) { ArrayList() } += local(start, record.offsets[index])
        keys[key] = intArrayOf(record.offsets[index], record.offsets[index], index)
    }
    for (position in firstIndex until record.times.size) {
        val at = record.times[position]
        if (at >= explicitEnd) break
        val index = record.indexes[position]
        val key = "$offsetBefore|${record.offsets[index]}|$index"
        groups.getOrPut(key) { ArrayList() } += local(at, offsetBefore)
        keys[key] = intArrayOf(offsetBefore, record.offsets[index], index)
        offsetBefore = record.offsets[index]
    }
    for ((key, onsets) in groups) {
        val (fromOffset, toOffset, index) = keys.getValue(key).let { Triple(it[0], it[1], it[2]) }
        val observance = observance(record.daylight[index])
            .start(onsets.first())
            .offsetFrom(UtcOffset(seconds = fromOffset))
            .offsetTo(UtcOffset(seconds = toOffset))
            .name(record.abbreviations[index])
        if (onsets.size > 1) observance.rdate(*onsets.drop(1).toTypedArray())
        builder.observance(observance.build())
    }
    if (record.tail.isNotEmpty()) {
        for (rule in record.tail) builder.observance(tailObservance(rule, record, record.tailStartYear))
    }
    return builder.build()
}

private fun outlookZone(tzid: String, record: ZoneRecord): VTimeZone {
    val builder = VTimeZone.builder().tzid(tzid)
    if (record.tail.isEmpty()) {
        val index = if (record.indexes.isEmpty()) record.initial else record.indexes[record.indexes.size - 1]
        val offset = UtcOffset(seconds = record.offsets[index])
        builder.observance(
            observance(false)
                .start(ICalDateTime.Floating(LocalDateTime(WINDOWS_EPOCH_YEAR, 1, 1, 0, 0)))
                .offsetFrom(offset)
                .offsetTo(offset)
                .name(record.abbreviations[index])
                .build(),
        )
        return builder.build()
    }
    for (rule in record.tail) builder.observance(tailObservance(rule, record, WINDOWS_EPOCH_YEAR))
    return builder.build()
}

// A rule at a UTC or standard time can fall on another day in wall time (1:00u at UTC-2 is 23:00 the day before): the
// weekday and the BYMONTHDAY range move with it, which is exact while the range stays within the month.
private fun tailObservance(rule: TailRule, record: ZoneRecord, startYear: Int): VTimeZone.Observance {
    val wall = wallTime(rule)
    val shift = floorDiv(wall.toLong(), SECONDS_PER_DAY).toInt()
    val time = wall - shift * SECONDS_PER_DAY.toInt()
    val day = ruleDay(startYear, rule.month, rule.mode, rule.day, rule.dayOfWeek) + shift
    return observance(record.daylight[rule.after])
        .start(local(day * SECONDS_PER_DAY + time, 0))
        .offsetFrom(UtcOffset(seconds = rule.offsetBefore))
        .offsetTo(UtcOffset(seconds = rule.offsetAfter))
        .name(record.abbreviations[rule.after])
        .rrule(recurrence(rule, shift))
        .build()
}

internal fun wallTime(rule: TailRule): Int = when (rule.definition) {
    DEFINITION_UTC -> rule.time + rule.offsetBefore
    DEFINITION_STANDARD -> rule.time + rule.offsetBefore - rule.standardOffset
    else -> rule.time
}

// A zone is regular when the Outlook form (the tail rules from 1601) gives the same transitions as the table from 2026
// to 2037: every tail rule is exact in RRULE, the offset at the start of 2026 is the one the rules give, and the
// offset-changing transitions of the table in the window are exactly those of the rules. Zones without tail rules are
// regular when their last offset is already in force. The writer source writes the full history for the other zones;
// the test of every zone checks the classification against the compiled Outlook form.
internal fun followsTailRules(id: String, record: ZoneRecord): Boolean {
    val start = epochDay(FIRST_CURRENT_YEAR, 1, 1) * SECONDS_PER_DAY
    val end = epochDay(LAST_CURRENT_YEAR + 1, 1, 1) * SECONDS_PER_DAY
    val zone = record.zone(id)
    val startOffset = zone.offsetAt(Instant.fromEpochSeconds(start)).totalSeconds
    val actual = ArrayList<Long>()
    for (transition in zone.transitions(Instant.fromEpochSeconds(start), Instant.fromEpochSeconds(end))) {
        if (transition.offsetBefore == transition.offsetAfter) continue
        actual += transition.at.epochSeconds
        actual += transition.offsetAfter.totalSeconds.toLong()
    }
    if (record.tail.isEmpty()) {
        val last = if (record.indexes.isEmpty()) record.initial else record.indexes[record.indexes.size - 1]
        return actual.isEmpty() && startOffset == record.offsets[last]
    }
    if (!record.tail.all { isExactRule(it) }) return false
    var offset = Int.MIN_VALUE
    val generated = ArrayList<Long>()
    for (year in FIRST_CURRENT_YEAR - 1..LAST_CURRENT_YEAR + 1) {
        for (rule in record.tail.sortedBy { transitionEpoch(it, year) }) {
            val at = transitionEpoch(rule, year)
            if (at < start) {
                offset = rule.offsetAfter
            } else if (at < end && rule.offsetBefore != rule.offsetAfter) {
                generated += at
                generated += rule.offsetAfter.toLong()
            }
        }
    }
    return offset == startOffset && generated == actual
}

internal fun isExactRule(rule: TailRule): Boolean {
    val shift = floorDiv(wallTime(rule).toLong(), SECONDS_PER_DAY).toInt()
    if (shift == 0) return true
    val range = dayRange(rule, shift) ?: return false
    return range.first >= 1 && range.last <= minimumLength(rule.month)
}

private fun dayRange(rule: TailRule, shift: Int): IntRange? = when {
    rule.dayOfWeek !in 0..6 -> (rule.day + shift)..(rule.day + shift)

    rule.mode == MODE_LAST -> {
        val length = monthLength(2001, rule.month)
        if (rule.month == 2) null else (length - 6 + shift)..(length + shift)
    }

    rule.mode == MODE_AFTER -> (rule.day + shift)..(rule.day + 6 + shift)

    else -> (rule.day - 6 + shift)..(rule.day + shift)
}

private fun minimumLength(month: Int): Int = if (month == 2) 28 else monthLength(2001, month)

private fun recurrence(rule: TailRule, shift: Int): Recur {
    val builder = Recur.builder(Frequency.YEARLY).byMonth(rule.month)
    val weekday = if (rule.dayOfWeek in 0..6) DayOfWeek.entries[floorMod(rule.dayOfWeek + shift, 7)] else null
    if (weekday == null) return builder.byMonthDay((rule.day + shift).coerceIn(1, 31)).build()
    if (shift == 0) {
        when {
            rule.mode == MODE_LAST -> builder.byDay(WeekdayNum(-1, weekday))

            rule.mode == MODE_AFTER && (rule.day - 1) % 7 == 0 && rule.day <= 22 ->
                builder.byDay(WeekdayNum((rule.day - 1) / 7 + 1, weekday))

            rule.mode == MODE_AFTER -> builder.byDay(weekday).byMonthDay(*days(rule.day, rule.day + 6))

            else -> builder.byDay(weekday).byMonthDay(*days(rule.day - 6, rule.day))
        }
        return builder.build()
    }
    val range = dayRange(rule, shift) ?: return builder.byDay(WeekdayNum(-1, weekday)).build()
    return builder.byDay(weekday).byMonthDay(*days(range.first, range.last)).build()
}

private fun days(from: Int, to: Int): IntArray = (maxOf(1, from)..minOf(31, to)).toList().toIntArray()

private fun observance(daylight: Boolean): VTimeZone.Observance.Builder =
    if (daylight) VTimeZone.Daylight.builder() else VTimeZone.Standard.builder()

private fun local(epochSecond: Long, offset: Int): ICalDateTime {
    val seconds = epochSecond + offset
    val day = floorDiv(seconds, SECONDS_PER_DAY)
    val second = (seconds - day * SECONDS_PER_DAY).toInt()
    val date = kotlinx.datetime.LocalDate.fromEpochDays(day.toInt())
    return ICalDateTime.Floating(
        LocalDateTime(
            date.year,
            date.month,
            date.day,
            second / 3600,
            second / 60 % 60,
            second % 60,
        ),
    )
}

private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if (value % divisor != 0L && (value < 0) != (divisor < 0)) quotient - 1 else quotient
}

private const val EPOCH_1970 = 0L
private const val WINDOWS_EPOCH_YEAR = 1601
internal const val FIRST_CURRENT_YEAR = 2026
internal const val LAST_CURRENT_YEAR = 2037
