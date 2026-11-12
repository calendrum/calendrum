package io.github.calendrum.model

import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod

/**
 * The value of a `TRIGGER` property: when an alarm fires
 * ([RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)).
 *
 * Either a time relative to the start or end of the parent component ([Relative]) or an absolute UTC time ([Absolute]).
 * Immutable.
 */
public sealed interface Trigger {
    /** All parameters of the property as written, including unknown ones. */
    public val parameters: List<ICalParameter>

    /**
     * An alarm that fires a [duration] before (negative) or after (positive) the start or end of its parent.
     *
     * Build one with `Trigger.Relative(ICalDuration.ofMinutes(-15))`.
     *
     * @property duration the offset from the related end.
     * @property related which end the offset is measured from; [Related.Start] by default.
     */
    public class Relative internal constructor(
        public val duration: ICalDuration,
        public val related: Related,
        override val parameters: List<ICalParameter>,
    ) : Trigger {
        public constructor(duration: ICalDuration) : this(duration, Related.Start, emptyList())

        public constructor(duration: ICalDuration, related: Related) :
            this(duration, related, listOf(ICalParameter.of(RELATED, related.value)))

        /** Returns `true` when [other] is a Relative equal to this one, comparing their content. */
        override fun equals(other: Any?): Boolean = this === other ||
            (other is Relative && duration == other.duration && parameters == other.parameters)

        /** Returns a hash code that agrees with [equals]. */
        override fun hashCode(): Int = duration.hashCode() * 31 + parameters.hashCode()

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = "Trigger.Relative($duration, $related)"
    }

    /**
     * An alarm that fires at one moment in UTC.
     *
     * Build one with `Trigger.Absolute(ICalDateTime.Utc(...))`.
     *
     * @property dateTime the moment; always UTC.
     * @throws IllegalArgumentException when constructed from a value that is not UTC.
     */
    public class Absolute internal constructor(
        public val dateTime: ICalDateTime,
        override val parameters: List<ICalParameter>,
    ) : Trigger {
        public constructor(dateTime: ICalDateTime) : this(
            utcOnly(dateTime),
            listOf(ICalParameter.of(VALUE, "DATE-TIME")),
        )

        /** Returns `true` when [other] is a Absolute equal to this one, comparing their content. */
        override fun equals(other: Any?): Boolean = this === other ||
            (other is Absolute && dateTime == other.dateTime && parameters == other.parameters)

        /** Returns a hash code that agrees with [equals]. */
        override fun hashCode(): Int = dateTime.hashCode() * 31 + parameters.hashCode()

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = "Trigger.Absolute($dateTime)"
    }
}

/**
 * The value of a `RECURRENCE-ID` property: which instance of a recurring series a component overrides
 * ([RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)).
 *
 * @property value the start of the instance that is overridden, in the form of the series' `DTSTART`.
 * @property range `THISANDFUTURE` when the override applies to all later instances too, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class RecurrenceId internal constructor(
    public val value: ICalDateTime,
    public val range: RecurrenceRange?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(value: ICalDateTime) : this(value, null, dateParameters(value, null))

    public constructor(value: ICalDateTime, range: RecurrenceRange) : this(value, range, dateParameters(value, range))

    /** Returns `true` when [other] is a RecurrenceId equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is RecurrenceId && value == other.value && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = value.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = if (range == null) "RecurrenceId($value)" else "RecurrenceId($value, $range)"
}

/**
 * The value of an `EXDATE` property: dates excluded from a recurrence set
 * ([RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)).
 *
 * @property dates the excluded dates, all of the same form.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class ExDate internal constructor(
    public val dates: List<ICalDateTime>,
    public val parameters: List<ICalParameter>,
) {
    public constructor(dates: List<ICalDateTime>) : this(frozenCopy(dates), dateParameters(sameForm(dates), null))

    /** Returns `true` when [other] is a ExDate equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is ExDate && dates == other.dates && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = dates.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("ExDate", "dates", dates.size)
}

/**
 * The value of an `RDATE` property: dates or periods added to a recurrence set
 * ([RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)).
 *
 * A property holds either dates or periods.
 *
 * @property dates the added dates; empty when the value is a list of periods.
 * @property periods the added periods; empty when the value is a list of dates.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class RDate internal constructor(
    public val dates: List<ICalDateTime>,
    public val periods: List<ICalPeriod>,
    public val parameters: List<ICalParameter>,
) {
    public constructor(dates: List<ICalDateTime>, periods: List<ICalPeriod>) : this(
        frozenCopy(dates),
        frozenCopy(periods),
        rdateParameters(dates, periods),
    )

    /** Returns `true` when [other] is a RDate equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean = this === other ||
        (other is RDate && dates == other.dates && periods == other.periods && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = (dates.hashCode() * 31 + periods.hashCode()) * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("RDate", "dates", dates.size, "periods", periods.size)
}

/**
 * The value of a `FREEBUSY` property: periods of one free/busy type
 * ([RFC 5545 §3.8.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.6)).
 *
 * @property type the free/busy type; [FbType.Busy] by default.
 * @property periods the periods, in UTC.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class FreeBusy internal constructor(
    public val type: FbType,
    public val periods: List<ICalPeriod>,
    public val parameters: List<ICalParameter>,
) {
    public constructor(periods: List<ICalPeriod>) : this(FbType.Busy, utcPeriods(periods), emptyList())

    public constructor(type: FbType, periods: List<ICalPeriod>) :
        this(type, utcPeriods(periods), listOf(ICalParameter.of(FBTYPE, type.value)))

    /** Returns `true` when [other] is a FreeBusy equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is FreeBusy && periods == other.periods && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = periods.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "FreeBusy($type, periods=${periods.size})"
}
