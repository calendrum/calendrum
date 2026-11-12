@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.value

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// The `@InternalICalApi` facade for the benchmarks and fuzz modules: the value types stay internal, so it takes and
// returns `Any`.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ValueCodecs(strict: Boolean, leapSecondToNextMinute: Boolean, problems: ValueProblems?) {
    private val decoder = ValueDecoder(strict, leapSecondToNextMinute, problems)

    public fun decode(type: Int, value: CharSequence, from: Int, to: Int, tzid: String?): Any? = when (type) {
        BINARY -> decoder.decodeBinary(value, from, to)
        BOOLEAN -> decoder.decodeBoolean(value, from, to)
        URI -> decoder.decodeUri(value, from, to)
        DATE -> decoder.decodeDate(value, from, to)
        DATE_TIME -> decoder.decodeDateTime(value, from, to, tzid)
        DURATION -> decoder.decodeDuration(value, from, to)
        FLOAT -> decoder.decodeFloat(value, from, to)
        INTEGER -> decoder.decodeInteger(value, from, to)
        PERIOD -> decoder.decodePeriod(value, from, to, tzid)
        RECUR -> decoder.decodeRecur(value, from, to)
        TEXT -> decoder.decodeText(value, from, to)
        TEXT_LIST -> decoder.decodeTextList(value, from, to)
        TIME -> decoder.decodeTime(value, from, to)
        UTC_OFFSET -> decoder.decodeUtcOffset(value, from, to)
        DATE_LIST -> decoder.decodeDateList(value, from, to)
        DATE_TIME_LIST -> decoder.decodeDateTimeList(value, from, to, tzid)
        PERIOD_LIST -> decoder.decodePeriodList(value, from, to, tzid)
        else -> throw IllegalArgumentException("Unknown value type $type")
    }

    @Suppress("UNCHECKED_CAST")
    public fun encode(type: Int, value: Any): String = when (type) {
        BINARY -> encodeBinary(value as BinaryValue)
        BOOLEAN -> encodeBoolean(value as Boolean)
        URI -> value as String
        DATE, DATE_TIME -> encodeDateTime(value as DateTimeValue)
        DURATION -> encodeDuration(value as DurationValue)
        FLOAT -> encodeFloat(value as FloatValue)
        INTEGER -> encodeInteger(value as Int)
        PERIOD -> encodePeriod(value as PeriodValue)
        RECUR -> encodeRecur(value as RecurValue)
        TEXT -> encodeText(value as String)
        TEXT_LIST -> encodeTextList(value as List<String>)
        TIME -> encodeTime(value as TimeValue)
        UTC_OFFSET -> encodeUtcOffset(value as Int)
        DATE_LIST, DATE_TIME_LIST -> encodeDateTimeList(value as List<DateTimeValue>)
        PERIOD_LIST -> encodePeriodList(value as List<PeriodValue>)
        else -> throw IllegalArgumentException("Unknown value type $type")
    }

    public companion object {
        public const val BINARY: Int = 0
        public const val BOOLEAN: Int = 1
        public const val URI: Int = 2
        public const val DATE: Int = 3
        public const val DATE_TIME: Int = 4
        public const val DURATION: Int = 5
        public const val FLOAT: Int = 6
        public const val INTEGER: Int = 7
        public const val PERIOD: Int = 8
        public const val RECUR: Int = 9
        public const val TEXT: Int = 10
        public const val TEXT_LIST: Int = 11
        public const val TIME: Int = 12
        public const val UTC_OFFSET: Int = 13
        public const val DATE_LIST: Int = 14
        public const val DATE_TIME_LIST: Int = 15
        public const val PERIOD_LIST: Int = 16
        public const val TYPE_COUNT: Int = 17
    }
}
