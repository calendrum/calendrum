package io.github.calendrum.value

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class PublicValuesTest {
    private val decoder = ValueDecoder(false, false, null)

    private fun dateTime(text: String, tzid: String? = null): ICalDateTime =
        decoder.decodeDateTime(text, 0, text.length, tzid)!!.toPublic()

    private fun date(text: String): ICalDateTime = decoder.decodeDate(text, 0, text.length)!!.toPublic()

    @Test
    fun theFourFormsComeFromTheInternalValues() {
        val date = assertIs<ICalDateTime.Date>(date("20260901"))
        assertEquals(LocalDate(2026, 9, 1), date.date)
        val floating = assertIs<ICalDateTime.Floating>(dateTime("20260901T103000"))
        assertEquals(LocalDateTime(2026, 9, 1, 10, 30, 0), floating.dateTime)
        val utc = assertIs<ICalDateTime.Utc>(dateTime("20260901T103000Z"))
        assertEquals(LocalDateTime(2026, 9, 1, 10, 30, 0).toInstant(TimeZone.UTC), utc.instant)
        val zoned = assertIs<ICalDateTime.Zoned>(dateTime("20260901T103000", "Europe/Berlin"))
        assertEquals(LocalDateTime(2026, 9, 1, 10, 30, 0), zoned.dateTime)
        assertEquals("Europe/Berlin", zoned.tzid)
        assertIs<ICalDateTime.Date>(dateTime("20260901"))
    }

    @Test
    fun valuesGoBackToTheSameInternalValue() {
        val samples = listOf(
            "20260901",
            "20260901T103000",
            "20260901T103000Z",
            "00000101",
            "99991231",
            "16010101T000000Z",
            "19691231T235959Z",
            "19700101T000000Z",
            "20380119T031408Z",
            "99991231T235959Z",
            "00000101T000000Z",
            "20240229T120000",
            "20230228",
        )
        for (sample in samples) {
            val internal = decoder.decodeDateTime(sample, 0, sample.length, null)!!
            assertEquals(internal, internal.toPublic().toValue(), sample)
            assertEquals(sample, encodeDateTime(internal.toPublic().toValue()), sample)
        }
        val zoned = decoder.decodeDateTime("20261025T023000", 0, 15, "Europe/Moscow")!!
        assertEquals(zoned, zoned.toPublic().toValue())
        assertEquals("Europe/Moscow", zoned.toPublic().tzidOrNull())
        assertNull(dateTime("20261025T023000").tzidOrNull())
    }

    @Test
    fun boundaryInstantsMatchKotlinxDatetime() {
        val cases = mapOf(
            "00000101T000000Z" to LocalDateTime(0, 1, 1, 0, 0, 0),
            "16010101T000000Z" to LocalDateTime(1601, 1, 1, 0, 0, 0),
            "19691231T235959Z" to LocalDateTime(1969, 12, 31, 23, 59, 59),
            "19700101T000000Z" to LocalDateTime(1970, 1, 1, 0, 0, 0),
            "20380119T031408Z" to LocalDateTime(2038, 1, 19, 3, 14, 8),
            "20240229T235959Z" to LocalDateTime(2024, 2, 29, 23, 59, 59),
            "99991231T235959Z" to LocalDateTime(9999, 12, 31, 23, 59, 59),
        )
        for ((text, local) in cases) {
            val utc = assertIs<ICalDateTime.Utc>(dateTime(text), text)
            assertEquals(local.toInstant(TimeZone.UTC), utc.instant, text)
        }
    }

    @Test
    fun anyInstantInRangeRoundTripsThroughTheCivilCalendar() {
        val random = Random(20261016)
        val first = LocalDateTime(0, 1, 1, 0, 0, 0).toInstant(TimeZone.UTC).epochSeconds
        val last = LocalDateTime(9999, 12, 31, 23, 59, 59).toInstant(TimeZone.UTC).epochSeconds
        repeat(20_000) {
            val second = random.nextLong(first, last + 1)
            val value = utcValue(second)
            assertEquals(second, value.epochSecond())
            assertEquals(Instant.fromEpochSeconds(second), assertIs<ICalDateTime.Utc>(value.toPublic()).instant)
        }
        assertEquals(first, utcValue(first).epochSecond())
        assertEquals(last, utcValue(last).epochSecond())
    }

    @Test
    fun yearsOutsideTheIcalendarRangeAreRejected() {
        assertFailsWith<IllegalArgumentException> {
            ICalDateTime.Utc(LocalDateTime(10000, 1, 1, 0, 0, 0).toInstant(TimeZone.UTC)).toValue()
        }
        assertFailsWith<IllegalArgumentException> {
            ICalDateTime.Utc(LocalDateTime(-1, 12, 31, 23, 59, 59).toInstant(TimeZone.UTC)).toValue()
        }
        assertFailsWith<IllegalArgumentException> { ICalDateTime.Date(LocalDate(10000, 1, 1)).toValue() }
        assertFailsWith<IllegalArgumentException> {
            ICalDateTime.Floating(LocalDateTime(-5, 1, 1, 0, 0, 0)).toValue()
        }
    }

    @Test
    fun utcDropsFractionsOfASecondWhenWritten() {
        val instant = LocalDateTime(2026, 9, 1, 10, 0, 0).toInstant(TimeZone.UTC) + 750.milliseconds
        assertEquals("20260901T100000Z", encodeDateTime(ICalDateTime.Utc(instant).toValue()))
    }

    @Test
    fun dateTimesCompareByContent() {
        assertEquals(dateTime("20260901T100000Z"), dateTime("20260901T100000Z"))
        assertEquals(dateTime("20260901T100000Z").hashCode(), dateTime("20260901T100000Z").hashCode())
        assertNotEquals(dateTime("20260901T100000Z"), dateTime("20260901T100000"))
        assertNotEquals(dateTime("20260901T100000", "A"), dateTime("20260901T100000", "B"))
        assertNotEquals(date("20260901"), dateTime("20260901T000000"))
        assertEquals("2026-09-01", date("20260901").toString())
        assertEquals("2026-09-01T10:00", dateTime("20260901T100000").toString())
        assertEquals("2026-09-01T10:00:00Z", dateTime("20260901T100000Z").toString())
        assertEquals("2026-09-01T10:00[Europe/Berlin]", dateTime("20260901T100000", "Europe/Berlin").toString())
    }

    @Test
    fun builtAndDecodedDateTimesAreTheSameValue() {
        val built = listOf(
            ICalDateTime.Date(LocalDate(2026, 9, 1)),
            ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 10, 30, 5)),
            ICalDateTime.Zoned(LocalDateTime(2026, 9, 1, 10, 30, 5), "Europe/Berlin"),
        )
        val decoded =
            listOf(date("20260901"), dateTime("20260901T103005"), dateTime("20260901T103005", "Europe/Berlin"))
        for (index in built.indices) {
            assertEquals(built[index], decoded[index])
            assertEquals(decoded[index], built[index])
            assertEquals(built[index].hashCode(), decoded[index].hashCode())
            assertEquals(built[index].toString(), decoded[index].toString())
        }
        val decodedDate = assertIs<ICalDateTime.Date>(decoded[0])
        assertTrue(decodedDate.date === decodedDate.date)
        assertNotEquals<ICalDateTime>(
            ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 10, 30, 5, 1)),
            ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 10, 30, 5)),
        )
    }

    @Test
    fun dateTimesPrintLikeKotlinxDatetime() {
        val dates = listOf(LocalDate(-1, 1, 1), LocalDate(0, 2, 29), LocalDate(9999, 12, 31), LocalDate(10_000, 1, 1))
        for (value in dates) assertEquals(value.toString(), ICalDateTime.Date(value).toString())
        val times = listOf(
            LocalDateTime(2026, 9, 1, 0, 0),
            LocalDateTime(2026, 9, 1, 23, 59, 59),
            LocalDateTime(2026, 9, 1, 10, 0, 0, 500_000_000),
            LocalDateTime(2026, 9, 1, 10, 0, 0, 120_000),
            LocalDateTime(2026, 9, 1, 10, 0, 7, 1),
            LocalDateTime(-12_345, 1, 1, 1, 1, 1),
            LocalDateTime(123_456, 1, 1, 1, 1),
        )
        for (value in times) {
            assertEquals(value.toString(), ICalDateTime.Floating(value).toString())
            assertEquals("$value[X]", ICalDateTime.Zoned(value, "X").toString())
        }
    }

    @Test
    fun decimalsBecomeTheNearestDouble() {
        val cases = mapOf(
            "0" to 0.0,
            "-0" to -0.0,
            "37.386013" to 37.386013,
            "-122.082932" to -122.082932,
            "0.1" to 0.1,
            "000.000500" to 0.0005,
            "9007199254740993" to 9_007_199_254_740_992.0,
            "123456789012345678901234567890" to 1.2345678901234568E29,
            "0.000000000000000000000000000001" to 1.0E-30,
            "1" + "0".repeat(400) to Double.POSITIVE_INFINITY,
            "0." + "0".repeat(400) + "1" to 0.0,
        )
        for ((text, expected) in cases) {
            assertEquals(expected.toRawBits(), decimalToDouble(text).toRawBits(), text)
            assertEquals(expected.toRawBits(), FloatValue(text).toDouble().toRawBits(), text)
        }
    }

    @Test
    fun durationsKeepTheirForm() {
        val week = ICalDuration(decoder.decodeDuration("P1W", 0, 3)!!)
        val sevenDays = ICalDuration(decoder.decodeDuration("P7D", 0, 3)!!)
        assertNotEquals(week, sevenDays)
        assertEquals(week.toDateTimePeriod(), sevenDays.toDateTimePeriod())
        assertEquals("P1W", week.toString())
        assertEquals("P7D", sevenDays.toString())
        assertEquals(1, week.weeks)
        assertEquals(7, sevenDays.days)
        val hour = ICalDuration(decoder.decodeDuration("PT1H", 0, 4)!!)
        val sixtyMinutes = ICalDuration(decoder.decodeDuration("PT60M", 0, 5)!!)
        assertNotEquals(hour, sixtyMinutes)
        assertEquals(hour.toDurationOrNull(), sixtyMinutes.toDurationOrNull())
        assertEquals(ICalDuration(decoder.decodeDuration("P1W", 0, 3)!!), week)
        assertEquals(week.hashCode(), ICalDuration(decoder.decodeDuration("P1W", 0, 3)!!).hashCode())
    }

    @Test
    fun durationsConvertExplicitly() {
        val text = "-P1DT2H3M4S"
        val negative = ICalDuration(decoder.decodeDuration(text, 0, text.length)!!)
        assertTrue(negative.isNegative)
        assertEquals(1, negative.days)
        assertEquals(2, negative.hours)
        assertEquals(3, negative.minutes)
        assertEquals(4, negative.seconds)
        assertEquals(DateTimePeriod(days = -1, hours = -2, minutes = -3, seconds = -4), negative.toDateTimePeriod())
        assertNull(negative.toDurationOrNull())
        val reminder = ICalDuration(decoder.decodeDuration("-PT15M", 0, 6)!!)
        assertEquals(-(15.minutes), reminder.toDurationOrNull())
        assertEquals(DateTimePeriod(minutes = -15), reminder.toDateTimePeriod())
        assertNull(ICalDuration.ofWeeks(2).toDurationOrNull())
        assertEquals(DateTimePeriod(days = 14), ICalDuration.ofWeeks(2).toDateTimePeriod())
        assertEquals(Duration.ZERO, ICalDuration.of(Duration.ZERO).toDurationOrNull())
    }

    @Test
    fun durationFactories() {
        assertEquals("PT1H30M", ICalDuration.of(90.minutes).toString())
        assertEquals("PT1H1M1S", ICalDuration.of(3661.seconds).toString())
        assertEquals("PT1H0M1S", ICalDuration.of(3601.seconds).toString())
        assertEquals("PT2H", ICalDuration.of(2.hours).toString())
        assertEquals("PT45S", ICalDuration.of(45.seconds).toString())
        assertEquals("PT0S", ICalDuration.of(Duration.ZERO).toString())
        assertEquals("-PT15M", ICalDuration.of(-(15.minutes)).toString())
        assertEquals("PT36H", ICalDuration.of(36.hours).toString())
        assertEquals(90.minutes, ICalDuration.of(90.minutes).toDurationOrNull())
        assertEquals("P2W", ICalDuration.ofWeeks(2).toString())
        assertEquals("-P1D", ICalDuration.ofDays(-1).toString())
        assertEquals("PT90M", ICalDuration.ofMinutes(90).toString())
        assertEquals("PT3H", ICalDuration.ofHours(3).toString())
        assertEquals("PT0S", ICalDuration.ofSeconds(0).toString())
        assertNotEquals(ICalDuration.ofMinutes(90), ICalDuration.of(90.minutes))
        assertFailsWith<IllegalArgumentException> { ICalDuration.of(1500.milliseconds) }
        assertFailsWith<IllegalArgumentException> { ICalDuration.of(Duration.INFINITE) }
        assertFailsWith<IllegalArgumentException> { ICalDuration.ofDays(Int.MIN_VALUE) }
        for (sample in listOf(ICalDuration.of(3601.seconds), ICalDuration.ofWeeks(-3), ICalDuration.ofMinutes(90))) {
            val text = sample.toString()
            assertEquals(sample, ICalDuration(ValueDecoder(true, false, null).decodeDuration(text, 0, text.length)!!))
        }
    }

    @Test
    fun periodsHaveAnEndOrADuration() {
        val explicit = "19970101T180000Z/19970102T070000Z"
        val byEnd = decoder.decodePeriod(explicit, 0, explicit.length, null)!!.toPublic()
        assertEquals(dateTime("19970101T180000Z"), byEnd.start)
        assertEquals(dateTime("19970102T070000Z"), byEnd.end)
        assertNull(byEnd.duration)
        assertEquals(explicit, encodePeriod(byEnd.toValue()))
        val relative = "19970101T180000Z/PT5H30M"
        val byDuration = decoder.decodePeriod(relative, 0, relative.length, null)!!.toPublic()
        assertNull(byDuration.end)
        assertEquals("PT5H30M", byDuration.duration.toString())
        assertEquals(relative, encodePeriod(byDuration.toValue()))
        assertEquals("1997-01-01T18:00:00Z/PT5H30M", byDuration.toString())
        assertEquals(byDuration, decoder.decodePeriod(relative, 0, relative.length, null)!!.toPublic())
        assertNotEquals(byEnd, byDuration)
        assertFailsWith<IllegalArgumentException> { ICalPeriod(byEnd.start, null, null) }
        assertFailsWith<IllegalArgumentException> { ICalPeriod(byEnd.start, byEnd.end, byDuration.duration) }
    }

    @Test
    fun everyRfcRuleReadsAndPrints() {
        for (rule in RecurCodecTest.RFC_RULES) {
            val value = ValueDecoder(true, false, null).decodeRecur(rule, 0, rule.length)!!
            val recur = Recur(value)
            assertEquals(Frequency.entries[value.freq], recur.frequency, rule)
            assertEquals(value.until?.toPublic(), recur.until, rule)
            assertEquals(if (value.count == ABSENT) null else value.count, recur.count, rule)
            assertEquals(value.byDay?.size ?: 0, recur.byDay.size, rule)
            assertEquals(value.byMonth?.size ?: 0, recur.byMonth.size, rule)
            val printed = encodeRecur(recur.value)
            assertEquals(recur, Recur(ValueDecoder(true, false, null).decodeRecur(printed, 0, printed.length)!!), rule)
            assertEquals(printed, recur.toString(), rule)
        }
    }

    @Test
    fun recurFields() {
        val rule = "FREQ=MONTHLY;COUNT=10;BYDAY=1SU,-1SU,MO;BYSETPOS=-1;WKST=SU;BYHOUR=9;BYMINUTE=30;BYSECOND=0"
        val recur = Recur(ValueDecoder(false, false, null).decodeRecur(rule, 0, rule.length)!!)
        assertEquals(Frequency.MONTHLY, recur.frequency)
        assertEquals(10, recur.count)
        assertEquals(1, recur.interval)
        assertNull(recur.until)
        assertEquals(
            listOf(
                WeekdayNum(1, DayOfWeek.SUNDAY),
                WeekdayNum(-1, DayOfWeek.SUNDAY),
                WeekdayNum(null, DayOfWeek.MONDAY),
            ),
            recur.byDay,
        )
        assertEquals(listOf("1SU", "-1SU", "MO"), recur.byDay.map { it.toString() })
        assertEquals(listOf(-1), recur.bySetPos)
        assertEquals(listOf(9), recur.byHour)
        assertEquals(listOf(30), recur.byMinute)
        assertEquals(listOf(0), recur.bySecond)
        assertEquals(DayOfWeek.SUNDAY, recur.weekStart)
        assertEquals(emptyList(), recur.byMonth)
        assertEquals(emptyList(), recur.byMonthDay)
        assertEquals(emptyList(), recur.byYearDay)
        assertEquals(emptyList(), recur.byWeekNo)
        assertNull(recur.rscale)
        assertNull(recur.skip)
        assertEquals(emptyList(), recur.unknownParts)
        val hebrew = "RSCALE=HEBREW;FREQ=YEARLY;INTERVAL=2;BYMONTH=5L,13;BYMONTHDAY=8;SKIP=FORWARD;UNTIL=20300101"
        val leap = Recur(ValueDecoder(true, false, null).decodeRecur(hebrew, 0, hebrew.length)!!)
        assertEquals(listOf(MonthNum(5, true), MonthNum(13, false)), leap.byMonth)
        assertEquals(listOf("5L", "13"), leap.byMonth.map { it.toString() })
        assertEquals("HEBREW", leap.rscale)
        assertEquals(Skip.FORWARD, leap.skip)
        assertEquals(2, leap.interval)
        assertEquals(listOf(8), leap.byMonthDay)
        assertEquals(ICalDateTime.Date(LocalDate(2030, 1, 1)), leap.until)
        assertNull(leap.weekStart)
    }

    @Test
    fun unknownPartsAreKeptInOrderButNotPrinted() {
        val rule = "FREQ=WEEKLY;X-SECRET=meeting room;BYYEARDAY=1;X-B=2;BYWEEKNO=3"
        val recur = Recur(ValueDecoder(false, false, null).decodeRecur(rule, 0, rule.length)!!)
        assertEquals(listOf(RecurPart("X-SECRET", "meeting room"), RecurPart("X-B", "2")), recur.unknownParts)
        assertEquals(listOf(1), recur.byYearDay)
        assertEquals(listOf(3), recur.byWeekNo)
        assertEquals("FREQ=WEEKLY;BYYEARDAY=1;BYWEEKNO=3", recur.toString())
        assertEquals("FREQ=WEEKLY;BYYEARDAY=1;BYWEEKNO=3;X-SECRET=meeting room;X-B=2", encodeRecur(recur.value))
        assertEquals("RecurPart(X-SECRET)", recur.unknownParts[0].toString())
        assertNotEquals(RecurPart("X-B", "2"), RecurPart("X-B", "3"))
        assertEquals(RecurPart("X-B", "2").hashCode(), RecurPart("X-B", "2").hashCode())
        assertEquals(MonthNum(5, true).hashCode(), MonthNum(5, true).hashCode())
        assertEquals(WeekdayNum(2, DayOfWeek.MONDAY).hashCode(), WeekdayNum(2, DayOfWeek.MONDAY).hashCode())
        assertNotEquals(MonthNum(5, true), MonthNum(5, false))
        assertNotEquals(WeekdayNum(2, DayOfWeek.MONDAY), WeekdayNum(null, DayOfWeek.MONDAY))
        assertEquals(
            recur.hashCode(),
            Recur(ValueDecoder(false, false, null).decodeRecur(rule, 0, rule.length)!!).hashCode(),
        )
        val skips = listOf("OMIT", "BACKWARD", "FORWARD").map {
            val text = "RSCALE=GREGORIAN;FREQ=DAILY;SKIP=$it"
            Recur(ValueDecoder(true, false, null).decodeRecur(text, 0, text.length)!!).skip
        }
        assertEquals(listOf(Skip.OMIT, Skip.BACKWARD, Skip.FORWARD), skips)
        val frequencies = listOf("SECONDLY", "MINUTELY", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY").map {
            val text = "FREQ=$it"
            Recur(ValueDecoder(true, false, null).decodeRecur(text, 0, text.length)!!).frequency
        }
        assertEquals(Frequency.entries, frequencies)
    }
}
