package io.github.calendrum.model

import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod

internal fun utcOnly(value: ICalDateTime): ICalDateTime {
    require(value is ICalDateTime.Utc) { "An absolute trigger is a UTC date-time" }
    return value
}

internal fun dateParameters(value: ICalDateTime, range: RecurrenceRange?): List<ICalParameter> {
    val form = when (value) {
        is ICalDateTime.Date -> ICalParameter.of(VALUE, "DATE")
        is ICalDateTime.Zoned -> ICalParameter.of(TZID, value.tzid)
        else -> null
    }
    val rangeParameter = if (range == null) null else ICalParameter.of(RANGE, range.value)
    return when {
        form == null && rangeParameter == null -> emptyList()
        form == null -> listOf(rangeParameter!!)
        rangeParameter == null -> listOf(form)
        else -> listOf(form, rangeParameter)
    }
}

internal fun sameForm(dates: List<ICalDateTime>): ICalDateTime {
    require(dates.isNotEmpty()) { "A date list is not empty" }
    val first = dates[0]
    for (date in dates) require(sameKind(first, date)) { "The dates of one property have one form" }
    return first
}

private fun sameKind(left: ICalDateTime, right: ICalDateTime): Boolean = when (left) {
    is ICalDateTime.Date -> right is ICalDateTime.Date
    is ICalDateTime.Floating -> right is ICalDateTime.Floating
    is ICalDateTime.Utc -> right is ICalDateTime.Utc
    is ICalDateTime.Zoned -> right is ICalDateTime.Zoned && right.tzid == left.tzid
}

internal fun rdateParameters(dates: List<ICalDateTime>, periods: List<ICalPeriod>): List<ICalParameter> {
    require(dates.isEmpty() != periods.isEmpty()) { "An RDATE has either dates or periods" }
    if (dates.isNotEmpty()) return dateParameters(sameForm(dates), null)
    val start = sameForm(periods.map { it.start })
    require(start !is ICalDateTime.Date) { "A period starts at a date-time" }
    val value = ICalParameter.of(VALUE, "PERIOD")
    return if (start is ICalDateTime.Zoned) listOf(value, ICalParameter.of(TZID, start.tzid)) else listOf(value)
}

internal fun utcPeriods(periods: List<ICalPeriod>): List<ICalPeriod> {
    require(periods.isNotEmpty()) { "A FREEBUSY has periods" }
    for (period in periods) {
        val end = period.end
        require(period.start is ICalDateTime.Utc && (end == null || end is ICalDateTime.Utc)) {
            "FREEBUSY periods are in UTC"
        }
    }
    return frozenCopy(periods)
}

internal fun nonEmptyTexts(values: List<String>): List<String> {
    require(values.isNotEmpty()) { "A text list is not empty" }
    return frozenCopy(values)
}

internal fun checkedStatus(code: String): String {
    require(isStatusCode(code, code.length)) { "A status code is digits separated by dots" }
    return code
}

internal fun binaryParameters(formatType: String?): List<ICalParameter> {
    val encoding = ICalParameter.of(ENCODING, "BASE64")
    val value = ICalParameter.of(VALUE, "BINARY")
    return if (formatType == null) {
        listOf(encoding, value)
    } else {
        listOf(ICalParameter.of(FMTTYPE, formatType), encoding, value)
    }
}

internal fun checkedRange(value: Double, limit: Double): Double {
    require(value >= -limit && value <= limit) { "A coordinate is out of range" }
    return value
}

internal const val LATITUDE_LIMIT = 90.0
internal const val LONGITUDE_LIMIT = 180.0
