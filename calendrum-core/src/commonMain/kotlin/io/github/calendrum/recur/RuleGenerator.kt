package io.github.calendrum.recur

import io.github.calendrum.ProblemCode
import io.github.calendrum.value.ABSENT
import io.github.calendrum.value.FREQ_DAILY
import io.github.calendrum.value.FREQ_HOURLY
import io.github.calendrum.value.FREQ_MINUTELY
import io.github.calendrum.value.FREQ_MONTHLY
import io.github.calendrum.value.FREQ_WEEKLY
import io.github.calendrum.value.FREQ_YEARLY
import io.github.calendrum.value.RecurValue
import io.github.calendrum.value.SKIP_BACKWARD
import io.github.calendrum.value.SKIP_FORWARD
import io.github.calendrum.value.SKIP_OMIT
import io.github.calendrum.value.daysInMonth
import io.github.calendrum.value.isLeapMonth
import io.github.calendrum.value.isLeapYear
import io.github.calendrum.value.monthOf
import io.github.calendrum.value.ordinalOf
import io.github.calendrum.value.weekdayOf

// Candidates of one RRULE in local time, as primitives: a day is an `Int` of days since the epoch, a time is seconds of
// the day. A period of FREQ (year, month, week from WKST, day; a slot for HOURLY and shorter) gives its days in order,
// the day filters keep some of them, and the times of the day (a sorted product of BYHOUR, BYMINUTE and BYSECOND, or
// the minutes and seconds of one hour, or the seconds of one minute) multiply them. BYSETPOS picks indices of that
// product without building it. Defaults from DTSTART are set before expansion.
internal class RuleGenerator(
    rule: RecurValue,
    private val startDay: Int,
    private val startSecond: Int,
    private val dateOnly: Boolean,
    private val bound: UntilBound?,
    private val maxIdle: Int,
    private val problems: RecurrenceProblems,
) {
    var day: Int = 0
        private set

    var second: Int = 0
        private set

    var unsupported: Boolean = false
        private set

    private val freq = rule.freq
    private val interval = if (rule.interval < 1) 1 else rule.interval
    private val weekStart = if (rule.weekStart == ABSENT) 0 else rule.weekStart

    // The lists walked per period are sets: a value repeated a million times in one line (8 MiB) would be walked a
    // million times per period, so repeats are dropped once here; at most 732, 106 and 742 values remain.
    private val yearDays: IntArray? = distinct(rule.byYearDay)
    private val weekNumbers: IntArray? = distinct(rule.byWeekNo)
    private val setPositions: IntArray? = distinct(rule.bySetPos)
    private var skip = SKIP_OMIT
    private var hasMonth = false

    // Masks: `monthMask` — bits 1..12; `monthDayMask` — bits 0..30 for days 1..31 and bits 32..62 for days -1..-31;
    // `weekdayMask` — bits 0..6 from Monday; `hourMask`, `minuteMask`, `secondMask` — one bit per value; per year a
    // `LongArray(6)` of days of the year for BYYEARDAY ∩ BYWEEKNO (`filter`) and numbered BYDAY (`numbered`).
    private var monthMask = 0
    private var hasMonthDay = false
    private var monthDayMask = 0L
    private var hasDay = false
    private var weekdayMask = 0
    private var ordinals: IntArray? = null
    private var hourMask = 0
    private var minuteMask = 0L
    private var secondMask = 0L
    private var times = IntArray(0)

    private var finished = false
    private var idle = 0
    private var lastDay = Int.MIN_VALUE
    private var lastSecond = 0

    private var periodYear = 0
    private var periodMonth = 0
    private var periodDay = 0
    private var slot = 0L
    private var slotBase = 0L
    private var slotStep = 0L

    private var days = IntArray(DAY_BUFFER)
    private var dayCount = 0
    private var periodTimes = times
    private var timeCount = 0
    private var selected = IntArray(0)
    private var useSelection = false
    private var cursor = 0
    private var total = 0
    private var ready = false

    private var cachedA: YearData? = null
    private var cachedB: YearData? = null

    private var civilYear = 0
    private var civilMonth = 0
    private var civilDay = 0

    init {
        val rscale = rule.rscale
        if (rscale != null && rscale != "GREGORIAN") {
            problems.report(ProblemCode.RscaleNotSupported)
            unsupported = true
            finished = true
        } else {
            if (rscale != null && rule.skip != ABSENT) skip = rule.skip
            setUp(rule)
        }
    }

    private fun setUp(rule: RecurValue) {
        civil(startDay)
        val startYear = civilYear
        val startMonth = civilMonth
        val startDate = civilDay
        val startWeekday = weekday(startDay)
        val byMonth = rule.byMonth
        if (byMonth != null) {
            hasMonth = true
            for (entry in byMonth) {
                val month = monthOf(entry)
                if (month < 1 || month > 12) continue
                monthMask = when {
                    !isLeapMonth(entry) -> monthMask or (1 shl month)
                    skip == SKIP_BACKWARD -> monthMask or (1 shl month)
                    skip == SKIP_FORWARD && month < 12 -> monthMask or (1 shl (month + 1))
                    else -> monthMask
                }
            }
        }
        val byMonthDay = rule.byMonthDay
        if (byMonthDay != null) {
            hasMonthDay = true
            for (value in byMonthDay) monthDayMask = monthDayMask or monthDayBit(value)
        }
        val byDay = rule.byDay
        if (byDay != null) {
            hasDay = true
            var numbered = 0
            for (entry in byDay) {
                if (ordinalOf(entry) == 0 || freq < FREQ_MONTHLY) {
                    weekdayMask = weekdayMask or (1 shl weekdayOf(entry))
                } else {
                    numbered++
                }
            }
            if (numbered > 0) {
                val list = IntArray(numbered)
                var index = 0
                for (entry in byDay) {
                    if (ordinalOf(entry) != 0 && freq >= FREQ_MONTHLY) list[index++] = entry
                }
                ordinals = distinct(list)
            }
        }
        if (byDay == null && byMonthDay == null && yearDays == null && weekNumbers == null) {
            when (freq) {
                FREQ_YEARLY -> {
                    if (!hasMonth) {
                        hasMonth = true
                        monthMask = 1 shl startMonth
                    }
                    hasMonthDay = true
                    monthDayMask = monthDayBit(startDate)
                }

                FREQ_MONTHLY -> {
                    hasMonthDay = true
                    monthDayMask = monthDayBit(startDate)
                }

                FREQ_WEEKLY -> {
                    hasDay = true
                    weekdayMask = 1 shl startWeekday
                }
            }
        }
        if (freq == FREQ_YEARLY && byDay == null && yearDays == null) {
            if (weekNumbers != null && byMonthDay == null) {
                hasDay = true
                weekdayMask = 1 shl startWeekday
            } else if (weekNumbers == null && byMonthDay != null && !hasMonth) {
                hasMonth = true
                monthMask = 1 shl startMonth
            }
        }
        if (hasMonth && monthMask == 0) {
            finished = true
            return
        }
        setUpTimes(rule)
        when (freq) {
            FREQ_YEARLY -> periodYear = startYear

            FREQ_MONTHLY -> periodMonth = startYear * 12 + startMonth - 1

            FREQ_WEEKLY -> periodDay = startDay - floorMod(startWeekday - weekStart, 7)

            FREQ_DAILY -> periodDay = startDay

            else -> {
                val unit = unitSeconds()
                slotBase = startDay * SECONDS_PER_DAY_LONG + (startSecond / unit) * unit
                slot = slotBase
                slotStep = interval.toLong() * unit
            }
        }
    }

    private fun setUpTimes(rule: RecurValue) {
        val byHour = rule.byHour
        val byMinute = rule.byMinute
        val bySecond = rule.bySecond
        if (dateOnly && (byHour != null || byMinute != null || bySecond != null || freq < FREQ_DAILY)) {
            problems.report(ProblemCode.TimePartWithDate)
        }
        val hour = if (dateOnly) 0 else startSecond / 3600
        val minute = if (dateOnly) 0 else startSecond / 60 % 60
        val second = if (dateOnly) 0 else startSecond % 60
        hourMask = when {
            !dateOnly && byHour != null -> intMask(byHour)
            freq >= FREQ_DAILY -> 1 shl hour
            else -> ALL_HOURS
        }
        minuteMask = when {
            !dateOnly && byMinute != null -> longMask(byMinute)
            freq >= FREQ_HOURLY -> 1L shl minute
            else -> ALL_MINUTES
        }
        secondMask = when {
            !dateOnly && bySecond != null -> {
                var mask = 0L
                for (value in bySecond) {
                    if (value == 60) problems.report(ProblemCode.LeapSecondIgnored) else mask = mask or (1L shl value)
                }
                mask
            }

            freq >= FREQ_MINUTELY -> 1L shl second

            else -> ALL_MINUTES
        }
        if (hourMask == 0 || minuteMask == 0L || secondMask == 0L) {
            finished = true
            return
        }
        times = when {
            freq >= FREQ_DAILY -> product(hourMask, minuteMask, secondMask)
            freq == FREQ_HOURLY -> product(1, minuteMask, secondMask)
            freq == FREQ_MINUTELY -> product(1, 1L, secondMask)
            else -> IntArray(1)
        }
        periodTimes = times
        if (freq < FREQ_DAILY) periodTimes = IntArray(times.size)
    }

    // Without COUNT, whole periods before the window are skipped by arithmetic; one period less than needed, because
    // SKIP=FORWARD moves a date of a period into the next month.
    fun skipTo(fromDay: Int, fromSecond: Int) {
        if (finished) return
        if (fromDay < startDay || (fromDay == startDay && fromSecond <= startSecond)) return
        when (freq) {
            FREQ_YEARLY -> {
                civil(fromDay)
                val periods = (civilYear - periodYear) / interval - 1
                if (periods > 0) periodYear += periods * interval
            }

            FREQ_MONTHLY -> {
                civil(fromDay)
                val periods = (civilYear * 12 + civilMonth - 1 - periodMonth) / interval - 1
                if (periods > 0) periodMonth += periods * interval
            }

            FREQ_WEEKLY -> {
                val periods = (fromDay - periodDay) / 7 / interval - 1
                if (periods > 0) periodDay += periods * 7 * interval
            }

            FREQ_DAILY -> {
                val periods = (fromDay - periodDay) / interval - 1
                if (periods > 0) periodDay += periods * interval
            }

            else -> {
                val target = fromDay * SECONDS_PER_DAY_LONG + fromSecond
                val periods = (target - slotBase) / slotStep - 1
                if (periods > 0) slot = slotBase + periods * slotStep
            }
        }
    }

    // Candidates before DTSTART and not after the last one are dropped: the first period starts before DTSTART, and
    // SKIP=FORWARD can repeat a day of the next period.
    fun advance(): Boolean {
        while (!finished) {
            if (ready) {
                while (cursor < total) {
                    val index = if (useSelection) selected[cursor] else cursor
                    cursor++
                    val candidateDay = days[index / timeCount]
                    var candidateSecond = periodTimes[index % timeCount]
                    if (candidateDay < startDay || (candidateDay == startDay && candidateSecond < startSecond)) continue
                    if (bound != null && !bound.allows(candidateDay, candidateSecond)) {
                        finished = true
                        return false
                    }
                    if (dateOnly) candidateSecond = 0
                    if (candidateDay < lastDay || (candidateDay == lastDay && candidateSecond <= lastSecond)) continue
                    lastDay = candidateDay
                    lastSecond = candidateSecond
                    idle = 0
                    day = candidateDay
                    second = candidateSecond
                    return true
                }
                ready = false
            }
            if (!nextPeriod()) {
                finished = true
                return false
            }
            if (++idle > maxIdle) {
                problems.report(ProblemCode.RecurrenceNoMatch)
                finished = true
                return false
            }
        }
        return false
    }

    private fun nextPeriod(): Boolean {
        dayCount = 0
        if (freq < FREQ_DAILY) return nextSlot()
        when (freq) {
            FREQ_YEARLY -> {
                if (periodYear > MAX_YEAR) return false
                for (month in 1..12) fillMonth(periodYear, month)
                periodYear = saturatedAdd(periodYear, interval)
            }

            FREQ_MONTHLY -> {
                val year = periodMonth / 12
                if (year > MAX_YEAR) return false
                fillMonth(year, periodMonth % 12 + 1)
                periodMonth = saturatedAdd(periodMonth, interval)
            }

            FREQ_WEEKLY -> {
                if (periodDay > MAX_DAY) return false
                for (offset in 0 until 7) fillDay(periodDay + offset)
                periodDay = saturatedAdd(periodDay, if (interval > Int.MAX_VALUE / 7) Int.MAX_VALUE else interval * 7)
            }

            else -> {
                if (periodDay > MAX_DAY) return false
                fillDay(periodDay)
                periodDay = saturatedAdd(periodDay, interval)
            }
        }
        timeCount = periodTimes.size
        select()
        return true
    }

    // HOURLY, MINUTELY and SECONDLY jump to the next aligned slot of the next day, hour or minute when a day, an hour
    // or a minute fails its filter; each jump counts as an iteration without a match.
    private fun nextSlot(): Boolean {
        while (true) {
            if (slot > MAX_SECOND) return false
            val slotDay = floorDiv(slot, SECONDS_PER_DAY_LONG).toInt()
            val secondOfDay = (slot - slotDay * SECONDS_PER_DAY_LONG).toInt()
            if (!dayPasses(slotDay)) {
                slot = align((slotDay + 1) * SECONDS_PER_DAY_LONG)
                if (++idle > maxIdle) return noMatch()
                continue
            }
            val hour = secondOfDay / 3600
            if ((hourMask shr hour) and 1 == 0) {
                slot = align(slotDay * SECONDS_PER_DAY_LONG + (hour + 1) * 3600L)
                if (++idle > maxIdle) return noMatch()
                continue
            }
            val minute = secondOfDay / 60 % 60
            if (freq < FREQ_HOURLY && (minuteMask shr minute) and 1L == 0L) {
                slot = align(slotDay * SECONDS_PER_DAY_LONG + hour * 3600L + (minute + 1) * 60L)
                if (++idle > maxIdle) return noMatch()
                continue
            }
            if (freq < FREQ_MINUTELY && (secondMask shr (secondOfDay % 60)) and 1L == 0L) {
                slot += slotStep
                if (++idle > maxIdle) return noMatch()
                continue
            }
            days[0] = slotDay
            dayCount = 1
            for (index in times.indices) periodTimes[index] = secondOfDay + times[index]
            timeCount = times.size
            slot += slotStep
            select()
            return true
        }
    }

    private fun noMatch(): Boolean {
        problems.report(ProblemCode.RecurrenceNoMatch)
        finished = true
        return false
    }

    private fun align(target: Long): Long {
        if (target <= slotBase) return slotBase
        val periods = (target - slotBase + slotStep - 1) / slotStep
        return slotBase + periods * slotStep
    }

    private fun select() {
        cursor = 0
        ready = true
        total = dayCount * timeCount
        val positions = setPositions
        if (positions == null || total == 0) {
            useSelection = false
            return
        }
        useSelection = true
        if (selected.size < positions.size) selected = IntArray(positions.size)
        var count = 0
        for (position in positions) {
            val index = if (position > 0) position - 1 else total + position
            if (index < 0 || index >= total) continue
            var at = count
            while (at > 0 && selected[at - 1] > index) at--
            if (at > 0 && selected[at - 1] == index) continue
            selected.copyInto(selected, at + 1, at, count)
            selected[at] = index
            count++
        }
        total = count
    }

    private fun fillMonth(year: Int, month: Int) {
        if (hasMonth && (monthMask shr month) and 1 == 0) return
        val data = yearData(year)
        val length = daysInMonth(year, month)
        val firstDoy = monthStartDoy(year, month)
        val first = data.firstDay + firstDoy
        var weekdayIndex = weekday(first)
        for (date in 1..length) {
            if (dayPasses(date, length, firstDoy + date - 1, weekdayIndex, data)) add(first + date - 1)
            weekdayIndex = if (weekdayIndex == 6) 0 else weekdayIndex + 1
        }
        if (skip != SKIP_OMIT && hasMonthDay && freq >= FREQ_MONTHLY && length < 31) {
            for (date in length + 1..31) {
                if ((monthDayMask shr (date - 1)) and 1L == 0L) continue
                if (skip == SKIP_BACKWARD) {
                    skipped(data, firstDoy + length - 1)
                } else if (month < 12) {
                    skipped(data, firstDoy + length)
                }
            }
        }
    }

    private fun skipped(data: YearData, doy: Int) {
        val target = data.firstDay + doy
        val filter = data.filter
        if (filter != null && !bit(filter, doy)) return
        if (hasDay && (weekdayMask shr weekday(target)) and 1 == 0) {
            val numbered = data.numbered
            if (numbered == null || !bit(numbered, doy)) return
        }
        add(target)
    }

    private fun fillDay(candidate: Int) {
        if (dayPasses(candidate)) add(candidate)
    }

    private fun dayPasses(candidate: Int): Boolean {
        civil(candidate)
        if (civilYear > MAX_YEAR || civilYear < 0) return false
        if (hasMonth && (monthMask shr civilMonth) and 1 == 0) return false
        val data = yearData(civilYear)
        return dayPasses(
            civilDay,
            daysInMonth(civilYear, civilMonth),
            candidate - data.firstDay,
            weekday(candidate),
            data,
        )
    }

    private fun dayPasses(date: Int, monthLength: Int, doy: Int, weekdayIndex: Int, data: YearData): Boolean {
        if (hasMonthDay &&
            (monthDayMask shr (date - 1)) and 1L == 0L &&
            (monthDayMask shr (32 + monthLength - date)) and 1L == 0L
        ) {
            return false
        }
        val filter = data.filter
        if (filter != null && !bit(filter, doy)) return false
        if (hasDay && (weekdayMask shr weekdayIndex) and 1 == 0) {
            val numbered = data.numbered
            if (numbered == null || !bit(numbered, doy)) return false
        }
        return true
    }

    private fun add(candidate: Int) {
        if (dayCount > 0 && days[dayCount - 1] >= candidate) {
            if (days[dayCount - 1] == candidate) return
        }
        if (dayCount == days.size) days = days.copyOf(days.size * 2)
        days[dayCount++] = candidate
    }

    private fun yearData(year: Int): YearData {
        val a = cachedA
        if (a != null && a.year == year) return a
        val b = cachedB
        if (b != null && b.year == year) return b
        val created = createYear(year)
        cachedB = a
        cachedA = created
        return created
    }

    private fun createYear(year: Int): YearData {
        val firstDay = dayOf(year, 1, 1)
        val length = if (isLeapYear(year)) 366 else 365
        var filter: LongArray? = null
        val byYearDay = yearDays
        if (byYearDay != null) {
            val mask = LongArray(MASK_WORDS)
            for (value in byYearDay) {
                val doy = if (value > 0) value - 1 else length + value
                if (doy in 0 until length) set(mask, doy)
            }
            filter = mask
        }
        val byWeekNo = weekNumbers
        if (byWeekNo != null) {
            val mask = weekMask(year, firstDay, length, byWeekNo)
            val previous = filter
            if (previous != null) {
                for (word in 0 until MASK_WORDS) mask[word] = mask[word] and previous[word]
            }
            filter = mask
        }
        val list = ordinals
        val numbered = if (list == null) null else numberedMask(year, firstDay, length, list)
        return YearData(year, firstDay, filter, numbered)
    }

    private fun weekMask(year: Int, firstDay: Int, length: Int, numbers: IntArray): LongArray {
        val mask = LongArray(MASK_WORDS)
        val previous = weekOne(year - 1)
        val current = weekOne(year)
        val next = weekOne(year + 1)
        val afterNext = weekOne(year + 2)
        var weekdayIndex = weekday(firstDay)
        for (doy in 0 until length) {
            val candidate = firstDay + doy
            val weekStartDay = candidate - floorMod(weekdayIndex - weekStart, 7)
            val number: Int
            val weeks: Int
            when {
                weekStartDay >= next -> {
                    number = 1 + (weekStartDay - next) / 7
                    weeks = (afterNext - next) / 7
                }

                weekStartDay >= current -> {
                    number = 1 + (weekStartDay - current) / 7
                    weeks = (next - current) / 7
                }

                else -> {
                    number = 1 + (weekStartDay - previous) / 7
                    weeks = (current - previous) / 7
                }
            }
            for (value in numbers) {
                if (value == number || value == number - weeks - 1) {
                    set(mask, doy)
                    break
                }
            }
            weekdayIndex = if (weekdayIndex == 6) 0 else weekdayIndex + 1
        }
        return mask
    }

    private fun weekOne(year: Int): Int {
        val fourth = dayOf(year, 1, 4)
        return fourth - floorMod(weekday(fourth) - weekStart, 7)
    }

    private fun numberedMask(year: Int, firstDay: Int, length: Int, entries: IntArray): LongArray {
        val mask = LongArray(MASK_WORDS)
        if (freq == FREQ_YEARLY && !hasMonth) {
            numberedRange(mask, firstDay, 0, length, entries)
        } else {
            for (month in 1..12) {
                if (freq == FREQ_YEARLY && (monthMask shr month) and 1 == 0) continue
                val start = monthStartDoy(year, month)
                numberedRange(mask, firstDay, start, start + daysInMonth(year, month), entries)
            }
        }
        return mask
    }

    private fun numberedRange(mask: LongArray, firstDay: Int, from: Int, to: Int, entries: IntArray) {
        val firstWeekday = weekday(firstDay + from)
        val lastWeekday = weekday(firstDay + to - 1)
        for (entry in entries) {
            val ordinal = ordinalOf(entry)
            val target = weekdayOf(entry)
            if (ordinal > 0) {
                val doy = from + floorMod(target - firstWeekday, 7) + (ordinal - 1) * 7
                if (doy < to) set(mask, doy)
            } else {
                val doy = to - 1 - floorMod(lastWeekday - target, 7) - (-ordinal - 1) * 7
                if (doy >= from) set(mask, doy)
            }
        }
    }

    private fun unitSeconds(): Int = when (freq) {
        FREQ_HOURLY -> 3600
        FREQ_MINUTELY -> 60
        else -> 1
    }

    private fun civil(epochDay: Int) {
        val shifted = epochDay + EPOCH_SHIFT
        val era = (if (shifted >= 0) shifted else shifted - DAYS_PER_ERA + 1) / DAYS_PER_ERA
        val dayOfEra = shifted - era * DAYS_PER_ERA
        val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36524 - dayOfEra / 146096) / 365
        val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
        val shiftedMonth = (5 * dayOfYear + 2) / 153
        civilDay = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
        civilMonth = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
        civilYear = yearOfEra + era * 400 + (if (civilMonth <= 2) 1 else 0)
    }

    override fun toString(): String = "RuleGenerator"
}

internal class YearData(val year: Int, val firstDay: Int, val filter: LongArray?, val numbered: LongArray?)

internal class UntilBound(private val day: Int, private val second: Int, private val offset: LocalOffset?) {
    // With an offset function, a candidate L passes when L - offset(L) ≤ UNTIL. Offsets are within ±18 hours, so the
    // function is asked only near the bound.
    fun allows(candidateDay: Int, candidateSecond: Int): Boolean {
        val function = offset ?: return candidateDay < day || (candidateDay == day && candidateSecond <= second)
        val difference = (candidateDay - day) * SECONDS_PER_DAY_LONG + (candidateSecond - second)
        if (difference <= -MAX_OFFSET_SECONDS) return true
        if (difference > MAX_OFFSET_SECONDS) return false
        return difference <= function.offsetSeconds(localKey(candidateDay, candidateSecond))
    }

    override fun toString(): String = "UntilBound"
}

internal const val SECONDS_PER_DAY_LONG = 86_400L
private fun distinct(values: IntArray?): IntArray? =
    if (values == null || values.size < 2) values else values.distinct().toIntArray()

internal const val MAX_YEAR_DAY_LIMIT = 2_932_896
private const val MAX_DAY = MAX_YEAR_DAY_LIMIT
private const val MAX_SECOND = (MAX_YEAR_DAY_LIMIT + 1) * SECONDS_PER_DAY_LONG - 1
private const val MAX_YEAR = 9999
private const val MAX_OFFSET_SECONDS = 64_800L
private const val DAY_BUFFER = 372
private const val MASK_WORDS = 6
private const val ALL_HOURS = 0xFFFFFF
private const val ALL_MINUTES = 0x0FFFFFFFFFFFFFFFL
private const val EPOCH_SHIFT = 719_468
private const val DAYS_PER_ERA = 146_097

internal fun weekday(epochDay: Int): Int = floorMod(epochDay + 3, 7)

internal fun floorMod(value: Int, divisor: Int): Int {
    val remainder = value % divisor
    return if (remainder < 0) remainder + divisor else remainder
}

internal fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    return if (value % divisor != 0L && (value < 0) != (divisor < 0)) quotient - 1 else quotient
}

internal fun dayOf(year: Int, month: Int, day: Int): Int {
    val y = if (month <= 2) year - 1 else year
    val era = (if (y >= 0) y else y - 399) / 400
    val yearOfEra = y - era * 400
    val shiftedMonth = if (month > 2) month - 3 else month + 9
    val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
    val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
    return era * 146_097 + dayOfEra - 719_468
}

internal fun monthStartDoy(year: Int, month: Int): Int {
    val base = when (month) {
        1 -> 0
        2 -> 31
        3 -> 59
        4 -> 90
        5 -> 120
        6 -> 151
        7 -> 181
        8 -> 212
        9 -> 243
        10 -> 273
        11 -> 304
        else -> 334
    }
    return if (month > 2 && isLeapYear(year)) base + 1 else base
}

private fun monthDayBit(value: Int): Long = if (value > 0) 1L shl (value - 1) else 1L shl (32 - value - 1)

private fun intMask(values: IntArray): Int {
    var mask = 0
    for (value in values) mask = mask or (1 shl value)
    return mask
}

private fun longMask(values: IntArray): Long {
    var mask = 0L
    for (value in values) mask = mask or (1L shl value)
    return mask
}

private fun product(hours: Int, minutes: Long, seconds: Long): IntArray {
    var count = 0
    for (hour in 0 until 24) {
        if ((hours shr hour) and 1 == 0) continue
        for (minute in 0 until 60) {
            if ((minutes shr minute) and 1L == 0L) continue
            for (second in 0 until 60) {
                if ((seconds shr second) and 1L != 0L) count++
            }
        }
    }
    val result = IntArray(count)
    var index = 0
    for (hour in 0 until 24) {
        if ((hours shr hour) and 1 == 0) continue
        for (minute in 0 until 60) {
            if ((minutes shr minute) and 1L == 0L) continue
            for (second in 0 until 60) {
                if ((seconds shr second) and 1L != 0L) result[index++] = hour * 3600 + minute * 60 + second
            }
        }
    }
    return result
}

private fun set(mask: LongArray, index: Int) {
    mask[index shr 6] = mask[index shr 6] or (1L shl (index and 63))
}

private fun bit(mask: LongArray, index: Int): Boolean = (mask[index shr 6] shr (index and 63)) and 1L != 0L

private fun saturatedAdd(value: Int, step: Int): Int = if (value > Int.MAX_VALUE - step) Int.MAX_VALUE else value + step
