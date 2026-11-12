package io.github.calendrum.occurrence

import io.github.calendrum.ProblemListener
import io.github.calendrum.model.VAlarm
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.describe
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.KotlinxRules
import io.github.calendrum.tz.ResolverSource
import io.github.calendrum.tz.TimeZoneResolver
import io.github.calendrum.tz.ZoneRules
import io.github.calendrum.tz.ZoneSource
import io.github.calendrum.tz.rules
import io.github.calendrum.value.ICalDateTime
import kotlinx.datetime.TimeZone
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import kotlin.time.Instant

/**
 * One instance of a component in time: a single event, one instance of a recurring event or an override, with absolute
 * start and end.
 *
 * Both ends are instants, computed with the time zones of the options. For a component without an end the end is
 * derived by the rules of [RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2): `DTEND`,
 * `DTSTART` plus `DURATION`, a day for a date, or the start itself. Immutable.
 *
 * @property start the instant the occurrence starts.
 * @property end the instant it ends; equal to [start] for a point in time.
 * @property startLocal the start as the component states it, in its own form.
 * @property recurrenceId the `RECURRENCE-ID` of an instance of a recurring series, or `null` for a single component.
 * @property component the component this occurrence comes from; for an override, the override.
 * @property isCancelled whether the instance is cancelled (`STATUS:CANCELLED`); only shown when the options include
 *   cancelled ones.
 */
public class Occurrence internal constructor(
    public val start: Instant,
    public val end: Instant,
    public val startLocal: ICalDateTime,
    public val recurrenceId: ICalDateTime?,
    public val component: ICalComponent,
    public val isCancelled: Boolean,
    internal val overlap: Int,
    internal val alarmSource: AlarmSource?,
) {
    /** Whether the occurrence starts on a date and not at a time of day. */
    public val isAllDay: Boolean get() = startLocal is ICalDateTime.Date

    @Volatile
    private var alarmList: List<AlarmTime>? = null

    /**
     * The alarms of this occurrence with the absolute time of each, repetitions included up to
     * [OccurrenceOptions.maxAlarmRepetitions]; computed on first use.
     */
    public val alarms: List<AlarmTime>
        get() = alarmList ?: (alarmSource?.alarms(this) ?: emptyList()).also { alarmList = it }

    // The conditions of RFC 4791 §9.9 per component and state, on the half-open range [from, to).
    internal fun intersects(from: Instant, to: Instant): Boolean = when (overlap) {
        OVERLAP_POINT -> from <= start && to > start
        OVERLAP_TODO_DURATION -> from <= end && (to > start || to >= end)
        OVERLAP_TODO_DUE -> (from < end || from <= start) && (to > start || to >= end)
        OVERLAP_DUE_ONLY -> from < end && to >= end
        else -> from < end && to > start
    }

    internal fun contains(instant: Instant): Boolean =
        if (end > start) instant >= start && instant < end else instant == start

    /** Returns `true` when [other] has the same start, end, component, recurrence id and cancellation flag. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is Occurrence && start == other.start && end == other.end && component == other.component &&
                recurrenceId == other.recurrenceId && isCancelled == other.isCancelled
            )

    /** Returns a hash code over the start, the end and the component. */
    override fun hashCode(): Int = (start.hashCode() * 31 + end.hashCode()) * 31 + component.hashCode()

    /** Returns the component name and the start and end instants. */
    override fun toString(): String = "Occurrence(${component.name}, $start..$end)"
}

/**
 * The moment an alarm of an occurrence fires.
 *
 * @property at the instant of the alarm.
 * @property alarm the `VALARM` it comes from.
 * @property repetition 0 for the first firing, 1 and up for the `REPEAT` repetitions.
 */
public class AlarmTime(public val at: Instant, public val alarm: VAlarm, public val repetition: Int) {
    /** Returns `true` when [other] has the same time, alarm and repetition number. */
    override fun equals(other: Any?): Boolean = this === other ||
        (other is AlarmTime && at == other.at && alarm == other.alarm && repetition == other.repetition)

    /** Returns a hash code over the time, the alarm and the repetition. */
    override fun hashCode(): Int = (at.hashCode() * 31 + alarm.hashCode()) * 31 + repetition

    /** Returns the instant and the repetition number. */
    override fun toString(): String = "AlarmTime($at, repetition $repetition)"
}

/**
 * Settings of occurrence computation: time zones, the zone of floating times, recurrence options, cancelled instances,
 * `THISANDFUTURE` overrides, the alarm repetition limit and a problem listener.
 *
 * Immutable and thread-safe. The problems that can be reported are `ICAL-O001` to `ICAL-O005`, `ICAL-Z001` to
 * `ICAL-Z004` and `ICAL-R001` to `ICAL-R009`.
 *
 * @property zones resolves `TZID` values, or `null` for the default: the `VTIMEZONE` components of the calendar and
 *   their aliases.
 * @property floatingZone the zone in which floating times and dates are read, or `null` for none (their occurrences are
 *   then skipped unless [floatingICalZone] is set).
 * @property floatingICalZone the same as a zone of this library, or `null`. It needs no time zone database of the platform,
 *   which Kotlin/JS does not have: a zone from `TzData.resolver` works everywhere. At most one of the two is set.
 * @property recurrence options of the expansion of recurrence rules.
 * @property includeCancelled whether cancelled instances are returned (with `isCancelled` set); false by default.
 * @property thisAndFuture whether `RANGE=THISANDFUTURE` overrides apply to later instances; true by default.
 * @property maxAlarmRepetitions the most repetitions of one alarm that are returned; 100 by default.
 * @property problemListener receives the problems, or `null`.
 */
public class OccurrenceOptions private constructor(
    public val zones: TimeZoneResolver?,
    public val floatingZone: TimeZone?,
    public val floatingICalZone: ICalTimeZone?,
    public val recurrence: RecurrenceOptions,
    public val includeCancelled: Boolean,
    public val thisAndFuture: Boolean,
    public val maxAlarmRepetitions: Int,
    public val problemListener: ProblemListener?,
    // Built from `zones` and the floating zones in `Builder.build()`; `Default` has neither, and the engine then reads the
    // VTIMEZONE of the calendar itself.
    internal val zoneSource: ZoneSource?,
    internal val floatingRules: ZoneRules?,
) {
    /** Returns the settings, with the zone resolver and the floating zone by description. */
    override fun toString(): String = describe(
        "OccurrenceOptions",
        "zones",
        zones ?: "default",
        "floatingZone",
        floatingZone ?: floatingICalZone?.id ?: "none",
        "includeCancelled",
        includeCancelled,
        "thisAndFuture",
        thisAndFuture,
        "maxAlarmRepetitions",
        maxAlarmRepetitions,
    )

    /**
     * Collects [OccurrenceOptions] values. The properties and the functions of the same name do the same. Not
     * thread-safe.
     */
    public class Builder internal constructor() {
        /**
         * Resolves `TZID` values; `null` (the default) means the `VTIMEZONE` components of the calendar and their
         * aliases.
         */
        public var zones: TimeZoneResolver? = null

        /** The zone for floating times and dates, such as the user's zone; `null` by default. */
        public var floatingZone: TimeZone? = null

        /**
         * The zone for floating times and dates as a zone of this library, such as one from `TzData.resolver`; `null` by
         * default. Unlike [floatingZone] it needs no time zone database of the platform.
         */
        public var floatingICalZone: ICalTimeZone? = null

        /** Options for expanding recurrence rules; the default options by default. */
        public var recurrence: RecurrenceOptions = RecurrenceOptions.Default

        /** Return cancelled instances too; false by default. */
        public var includeCancelled: Boolean = false

        /** Apply `RANGE=THISANDFUTURE` overrides to later instances; true by default. */
        public var thisAndFuture: Boolean = true

        /** The most repetitions of one alarm; zero or more, 100 by default. */
        public var maxAlarmRepetitions: Int = DEFAULT_MAX_ALARM_REPETITIONS

        /** Receives the problems; `null` by default. */
        public var problemListener: ProblemListener? = null

        /** Sets [zones] to [value]. */
        public fun zones(value: TimeZoneResolver?): Builder = apply { zones = value }

        /** Sets [floatingZone] to [value]. */
        public fun floatingZone(value: TimeZone?): Builder = apply { floatingZone = value }

        /** Sets [floatingICalZone] to [value]. */
        public fun floatingICalZone(value: ICalTimeZone?): Builder = apply { floatingICalZone = value }

        /** Sets [recurrence] to [value]. */
        public fun recurrence(value: RecurrenceOptions): Builder = apply { recurrence = value }

        /** Sets [includeCancelled] to [value]. */
        public fun includeCancelled(value: Boolean): Builder = apply { includeCancelled = value }

        /** Sets [thisAndFuture] to [value]. */
        public fun thisAndFuture(value: Boolean): Builder = apply { thisAndFuture = value }

        /** Sets [maxAlarmRepetitions] to [value]. */
        public fun maxAlarmRepetitions(value: Int): Builder = apply { maxAlarmRepetitions = value }

        /** Sets [problemListener] to [value]. */
        public fun problemListener(value: ProblemListener?): Builder = apply { problemListener = value }

        /**
         * Creates the options.
         *
         * @throws IllegalArgumentException if [maxAlarmRepetitions] is negative, or if both [floatingZone] and
         *   [floatingICalZone] are set.
         */
        public fun build(): OccurrenceOptions {
            require(maxAlarmRepetitions >= 0) { "maxAlarmRepetitions must not be negative" }
            require(floatingZone == null || floatingICalZone == null) {
                "Set floatingZone or floatingICalZone, not both"
            }
            return OccurrenceOptions(
                zones,
                floatingZone,
                floatingICalZone,
                recurrence,
                includeCancelled,
                thisAndFuture,
                maxAlarmRepetitions,
                problemListener,
                zones?.let(::ResolverSource),
                floatingICalZone?.rules() ?: floatingZone?.let(::KotlinxRules),
            )
        }

        /** Returns the name of the builder. */
        override fun toString(): String = "OccurrenceOptions.Builder"
    }

    /** The default options and a builder. */
    public companion object {
        /**
         * No zone resolver and no floating zone, default recurrence options, cancelled instances left out,
         * `THISANDFUTURE` applied and at most 100 alarm repetitions.
         */
        @JvmField
        public val Default: OccurrenceOptions = OccurrenceOptions(
            null,
            null,
            null,
            RecurrenceOptions.Default,
            false,
            true,
            DEFAULT_MAX_ALARM_REPETITIONS,
            null,
            null,
            null,
        )

        /** Returns a builder that starts from the default options. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

internal fun interface AlarmSource {
    fun alarms(occurrence: Occurrence): List<AlarmTime>
}

internal const val OVERLAP_SPAN = 0
internal const val OVERLAP_POINT = 1
internal const val OVERLAP_TODO_DURATION = 2
internal const val OVERLAP_TODO_DUE = 3
internal const val OVERLAP_DUE_ONLY = 4
private const val DEFAULT_MAX_ALARM_REPETITIONS = 100
