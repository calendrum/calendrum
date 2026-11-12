package io.github.calendrum.value

/**
 * A `PERIOD` value: a start and either an end or a duration ([RFC 5545
 * §3.3.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.9)), such as `19970101T180000Z/PT5H30M`.
 *
 * Exactly one of [end] and [duration] is set. Immutable.
 *
 * @property start where the period begins; [ICalDateTime.Utc] or a local form.
 * @property end where the period ends, or `null` when the period is given by a [duration].
 * @property duration how long the period lasts, or `null` when it is given by an [end].
 * @throws IllegalArgumentException if both or neither of [end] and [duration] are given.
 */
public class ICalPeriod(
    public val start: ICalDateTime,
    public val end: ICalDateTime?,
    public val duration: ICalDuration?,
) {
    init {
        require((end == null) != (duration == null)) { "A period has either an end or a duration" }
    }

    /** Returns `true` when [other] is a period with equal start, end and duration. */
    override fun equals(other: Any?): Boolean = this === other ||
        (other is ICalPeriod && start == other.start && end == other.end && duration == other.duration)

    /** Returns a hash code over the start, the end and the duration. */
    override fun hashCode(): Int = (start.hashCode() * 31 + end.hashCode()) * 31 + duration.hashCode()

    /**
     * Returns the start and the end or duration separated by a slash, in the forms of [ICalDateTime.toString] and
     * [ICalDuration.toString].
     */
    override fun toString(): String = "$start/${end ?: duration}"
}

internal fun PeriodValue.toPublic(): ICalPeriod =
    ICalPeriod(start.toPublic(), end?.toPublic(), duration?.let(::ICalDuration))

internal fun ICalPeriod.toValue(): PeriodValue = PeriodValue(start.toValue(), end?.toValue(), duration?.value)
