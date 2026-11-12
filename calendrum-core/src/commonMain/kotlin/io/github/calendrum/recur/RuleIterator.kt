package io.github.calendrum.recur

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.ProblemListener
import io.github.calendrum.SourceSpan
import io.github.calendrum.value.ABSENT
import io.github.calendrum.value.FORM_DATE
import io.github.calendrum.value.FORM_UTC
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.RecurValue
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant

// Every code once per expansion; there is no result to collect problems in, only the listener.
internal class RecurrenceProblems(private val listener: ProblemListener?, private val span: SourceSpan) {
    private var reported = 0

    fun report(code: ProblemCode) {
        val target = listener ?: return
        val flag = 1 shl (code.index - ProblemCode.RecurrenceLimitReached.index)
        if (reported and flag != 0) return
        reported = reported or flag
        target.onProblem(Problem(code, span, null))
    }

    override fun toString(): String = "RecurrenceProblems"
}

internal val NO_SPAN: SourceSpan get() = SourceSpan(0L, 0L, 0, 0)

internal class LocalPoint(val day: Int, val second: Int, val nanosecond: Int) {
    val key: Long get() = day * SECONDS_PER_DAY_LONG + second

    override fun toString(): String = "LocalPoint"
}

internal fun ICalDateTime.localPoint(): LocalPoint = when (this) {
    is ICalDateTime.Date -> LocalPoint(dayOf(year, month, day), 0, 0)

    is ICalDateTime.Floating -> LocalPoint(dayOf(year, month, day), secondOfDay, nanosecond)

    is ICalDateTime.Zoned -> LocalPoint(dayOf(year, month, day), secondOfDay, nanosecond)

    is ICalDateTime.Utc -> {
        val seconds = instant.epochSeconds
        val days = floorDiv(seconds, SECONDS_PER_DAY_LONG)
        LocalPoint(days.toInt(), (seconds - days * SECONDS_PER_DAY_LONG).toInt(), instant.nanosecondsOfSecond)
    }
}

internal fun ICalDateTime.localKey(): Long = localPoint().key

internal fun localKey(day: Int, second: Int): Long = day * SECONDS_PER_DAY_LONG + second

internal fun dateTimeLike(start: ICalDateTime, day: Int, second: Int, nanosecond: Int): ICalDateTime {
    if (start is ICalDateTime.Utc) {
        return ICalDateTime.Utc(Instant.fromEpochSeconds(day * SECONDS_PER_DAY_LONG + second, nanosecond))
    }
    val civil = civilOf(day)
    val year = civil shr 9
    val month = (civil shr 5) and 15
    val date = civil and 31
    return when (start) {
        is ICalDateTime.Date -> ICalDateTime.Date(year, month, date)
        is ICalDateTime.Zoned -> ICalDateTime.Zoned(year, month, date, second, nanosecond, start.tzid)
        else -> ICalDateTime.Floating(year, month, date, second, nanosecond)
    }
}

internal fun civilOf(epochDay: Int): Int {
    val shifted = epochDay + 719_468
    val era = (if (shifted >= 0) shifted else shifted - 146_096) / 146_097
    val dayOfEra = shifted - era * 146_097
    val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
    val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
    val shiftedMonth = (5 * dayOfYear + 2) / 153
    val date = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
    val month = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
    val year = yearOfEra + era * 400 + (if (month <= 2) 1 else 0)
    return (year shl 9) or (month shl 5) or date
}

internal fun localDateTime(day: Int, second: Int): LocalDateTime {
    val civil = civilOf(day)
    return LocalDateTime(civil shr 9, (civil shr 5) and 15, civil and 31, second / 3600, second / 60 % 60, second % 60)
}

internal fun untilBound(
    rule: RecurValue,
    start: ICalDateTime,
    options: RecurrenceOptions,
    problems: RecurrenceProblems,
): UntilBound? {
    val until = rule.until ?: return null
    val day = dayOf(until.year, until.month, until.day)
    val second = until.hour * 3600 + until.minute * 60 + until.second
    if (start is ICalDateTime.Date) {
        if (!until.isDate) problems.report(ProblemCode.UntilTypeMismatch)
        return UntilBound(day, second, null)
    }
    if (until.form == FORM_DATE) {
        problems.report(ProblemCode.UntilTypeMismatch)
        return UntilBound(day, LAST_SECOND, null)
    }
    val utc = until.form == FORM_UTC
    when (start) {
        is ICalDateTime.Zoned -> {
            if (!utc) {
                problems.report(ProblemCode.UntilTypeMismatch)
                return UntilBound(day, second, null)
            }
            val offset = options.localOffset
            if (offset == null) problems.report(ProblemCode.UntilWithoutZone)
            return UntilBound(day, second, offset)
        }

        is ICalDateTime.Utc -> if (!utc) problems.report(ProblemCode.UntilTypeMismatch)

        else -> if (utc) problems.report(ProblemCode.UntilTypeMismatch)
    }
    return UntilBound(day, second, null)
}

// DTSTART policy, COUNT, the window and the occurrence limit over the generator. The first candidate tells whether
// DTSTART is an instance; Rfc emits DTSTART first and counts it, Include emits it without counting, Exclude leaves it
// to the rule.
internal class RuleIterator(
    rule: RecurValue,
    private val start: ICalDateTime,
    private val options: RecurrenceOptions,
    private val problems: RecurrenceProblems,
    private val from: Long,
    private val to: Long,
) : Iterator<ICalDateTime> {
    private val point = start.localPoint()
    private val count = rule.count
    private val generator: RuleGenerator =
        RuleGenerator(
            rule,
            point.day,
            point.second,
            start is ICalDateTime.Date,
            untilBound(rule, start, options, problems),
            options.limits.maxIterationsWithoutMatch,
            problems,
        )
    private val skipped: Boolean
    private var initial = true
    private var pending = false
    private var counted = 0
    private var emitted = 0
    private var walked = 0
    private var done = generator.unsupported
    private var lookahead = false

    var day: Int = 0
        private set

    var second: Int = 0
        private set

    private var atStart = false

    init {
        skipped = count == ABSENT && from != Long.MIN_VALUE && from > point.key
        if (skipped) {
            val fromDay = floorDiv(from, SECONDS_PER_DAY_LONG).toInt()
            generator.skipTo(fromDay, (from - fromDay * SECONDS_PER_DAY_LONG).toInt())
        }
    }

    val key: Long get() = localKey(day, second)

    fun current(): ICalDateTime = if (atStart) start else dateTimeLike(start, day, second, point.nanosecond)

    // A COUNT rule cannot jump to the window; its walk there is bounded by the limit like the dates it gives, or a
    // window in 9999 of `FREQ=SECONDLY;COUNT=2147483647` would take hours.
    fun advance(): Boolean {
        while (advanceRaw()) {
            val current = localKey(day, second)
            if (current < from) {
                if (++walked <= options.limits.maxOccurrences) continue
                problems.report(ProblemCode.RecurrenceLimitReached)
                break
            }
            if (current >= to) break
            if (emitted >= options.limits.maxOccurrences) {
                problems.report(ProblemCode.RecurrenceLimitReached)
                break
            }
            emitted++
            return true
        }
        done = true
        return false
    }

    private fun advanceRaw(): Boolean {
        if (done) return false
        if (count != ABSENT && counted >= count) return false
        if (initial) {
            initial = false
            if (!skipped) {
                val has = generator.advance()
                if (has && generator.day == point.day && generator.second == point.second) {
                    pending = true
                } else {
                    problems.report(ProblemCode.StartNotInRule)
                    pending = has
                    if (options.startMismatch != StartMismatch.Exclude) {
                        if (options.startMismatch == StartMismatch.Rfc) counted++
                        day = point.day
                        second = point.second
                        atStart = true
                        return true
                    }
                }
            }
        }
        val has = if (pending) {
            pending = false
            true
        } else {
            generator.advance()
        }
        if (!has) return false
        if (count != ABSENT && counted >= count) return false
        counted++
        day = generator.day
        second = generator.second
        atStart = day == point.day && second == point.second
        return true
    }

    override fun hasNext(): Boolean {
        if (lookahead) return true
        if (done) return false
        lookahead = advance()
        return lookahead
    }

    override fun next(): ICalDateTime {
        if (!hasNext()) throw NoSuchElementException()
        lookahead = false
        return current()
    }

    override fun toString(): String = "RuleIterator"
}

private const val LAST_SECOND = 86_399
