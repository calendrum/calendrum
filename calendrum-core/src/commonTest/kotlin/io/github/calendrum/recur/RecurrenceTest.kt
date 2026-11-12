package io.github.calendrum.recur

import io.github.calendrum.ProblemCode
import io.github.calendrum.value.Frequency
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlin.time.Instant

class RecurrenceTest {
    private fun dates(vararg lines: String, options: RecurrenceOptions = RecurrenceOptions.Default): List<String> =
        Sequence { event(*lines).recurrenceSet!!.iterator(options) }.map(::local).toList()

    private fun withProblems(sink: ProblemSink, policy: StartMismatch = StartMismatch.Rfc): RecurrenceOptions =
        RecurrenceOptions.builder().problemListener(sink).startMismatch(policy).build()

    @Test
    fun gregorianSkipOfRfc7529() {
        val forward = dates("DTSTART;VALUE=DATE:20120229", "RRULE:RSCALE=GREGORIAN;FREQ=YEARLY;SKIP=FORWARD;COUNT=6")
        assertEquals(
            listOf("2012-02-29", "2013-03-01", "2014-03-01", "2015-03-01", "2016-02-29", "2017-03-01"),
            forward,
        )
        val plain = dates("DTSTART;VALUE=DATE:20120229", "RRULE:FREQ=YEARLY;COUNT=3")
        assertEquals(listOf("2012-02-29", "2016-02-29", "2020-02-29"), plain)
        val backward = dates("DTSTART;VALUE=DATE:20120229", "RRULE:RSCALE=GREGORIAN;FREQ=YEARLY;SKIP=BACKWARD;COUNT=3")
        assertEquals(listOf("2012-02-29", "2013-02-28", "2014-02-28"), backward)
        val monthEnd = dates("DTSTART:20260131T100000", "RRULE:RSCALE=GREGORIAN;FREQ=MONTHLY;SKIP=BACKWARD;COUNT=4")
        assertEquals(listOf("2026-01-31T10:00", "2026-02-28T10:00", "2026-03-31T10:00", "2026-04-30T10:00"), monthEnd)
        val monthForward = dates("DTSTART:20260131T100000", "RRULE:RSCALE=GREGORIAN;FREQ=MONTHLY;SKIP=FORWARD;COUNT=4")
        assertEquals(
            listOf("2026-01-31T10:00", "2026-03-01T10:00", "2026-03-31T10:00", "2026-05-01T10:00"),
            monthForward,
        )
        val leapMonth =
            dates("DTSTART;VALUE=DATE:20260510", "RRULE:RSCALE=GREGORIAN;FREQ=YEARLY;BYMONTH=5L;SKIP=FORWARD;COUNT=2")
        assertEquals(listOf("2026-05-10", "2026-06-10"), leapMonth)
    }

    @Test
    fun otherCalendarsAreNotExpanded() {
        for (rule in listOf(
            "RSCALE=CHINESE;FREQ=YEARLY",
            "RSCALE=ETHIOPIC;FREQ=MONTHLY;BYMONTH=13",
            "RSCALE=HEBREW;FREQ=YEARLY;BYMONTH=5L;BYMONTHDAY=8;SKIP=FORWARD",
        )) {
            val sink = ProblemSink()
            val event = event("DTSTART;VALUE=DATE:20130210", "RRULE:$rule")
            assertEquals(emptyList(), event.rrule!!.expand(event.start!!, withProblems(sink)).toList())
            assertEquals(listOf("ICAL-R004"), sink.codes)
            val set = Sequence { event.recurrenceSet!!.iterator() }.map(::local).toList()
            assertEquals(listOf("2013-02-10"), set)
        }
    }

    @Test
    fun startThatIsNotAnInstance() {
        val lines = arrayOf("DTSTART:20260902T090000", "RRULE:FREQ=WEEKLY;BYDAY=MO;COUNT=3")
        val rfc = ProblemSink()
        assertEquals(
            listOf("2026-09-02T09:00", "2026-09-07T09:00", "2026-09-14T09:00"),
            dates(*lines, options = withProblems(rfc)),
        )
        assertEquals("ICAL-R003", rfc.codes.single())
        assertEquals(
            listOf("2026-09-07T09:00", "2026-09-14T09:00", "2026-09-21T09:00"),
            dates(*lines, options = withProblems(ProblemSink(), StartMismatch.Exclude)),
        )
        assertEquals(
            listOf("2026-09-02T09:00", "2026-09-07T09:00", "2026-09-14T09:00", "2026-09-21T09:00"),
            dates(*lines, options = withProblems(ProblemSink(), StartMismatch.Include)),
        )
        val rule = event(*lines).rrule!!
        val start = ICalDateTime.Floating(LocalDateTime(2026, 9, 2, 9, 0))
        assertEquals(3, rule.expand(start).count())
        val matching = ProblemSink()
        assertEquals(
            listOf("2026-09-07T09:00", "2026-09-14T09:00"),
            dates("DTSTART:20260907T090000", "RRULE:FREQ=WEEKLY;COUNT=2", options = withProblems(matching)),
        )
        assertEquals(emptyList(), matching.codes)
    }

    @Test
    fun untilForms() {
        val sink = ProblemSink()
        val zoned = dates(
            "DTSTART;TZID=Europe/Berlin:20260901T090000",
            "RRULE:FREQ=DAILY;UNTIL=20260903T070000Z",
            options = withProblems(sink),
        )
        assertEquals(listOf("2026-09-01T09:00", "2026-09-02T09:00"), zoned)
        assertEquals(listOf("ICAL-R006"), sink.codes)
        val berlin = RecurrenceOptions.builder().untilOffset { kotlinx.datetime.UtcOffset(hours = 2) }.build()
        val exact = dates(
            "DTSTART;TZID=Europe/Berlin:20260901T090000",
            "RRULE:FREQ=DAILY;UNTIL=20260903T070000Z",
            options = berlin,
        )
        assertEquals(listOf("2026-09-01T09:00", "2026-09-02T09:00", "2026-09-03T09:00"), exact)
        val dateUntil = ProblemSink()
        assertEquals(
            listOf("2026-09-01T09:00", "2026-09-02T09:00"),
            dates("DTSTART:20260901T090000", "RRULE:FREQ=DAILY;UNTIL=20260902", options = withProblems(dateUntil)),
        )
        assertEquals(listOf("ICAL-R005"), dateUntil.codes)
        val allDay = ProblemSink()
        assertEquals(
            listOf("2026-09-01", "2026-09-02"),
            dates(
                "DTSTART;VALUE=DATE:20260901",
                "RRULE:FREQ=DAILY;UNTIL=20260902T235959Z",
                options = withProblems(allDay),
            ),
        )
        assertEquals(listOf("ICAL-R005"), allDay.codes)
        val utc = dates("DTSTART:20260901T090000Z", "RRULE:FREQ=DAILY;UNTIL=20260902T090000Z")
        assertEquals(listOf("2026-09-01T09:00:00Z", "2026-09-02T09:00:00Z"), utc)
    }

    @Test
    fun timePartsOfDates() {
        val sink = ProblemSink()
        assertEquals(
            listOf("2026-09-01", "2026-09-02", "2026-09-03"),
            dates("DTSTART;VALUE=DATE:20260901", "RRULE:FREQ=DAILY;BYHOUR=10,11;COUNT=3", options = withProblems(sink)),
        )
        assertEquals(listOf("ICAL-R007"), sink.codes)
        assertEquals(
            listOf("2026-09-01", "2026-09-02", "2026-09-03"),
            dates("DTSTART;VALUE=DATE:20260901", "RRULE:FREQ=HOURLY;COUNT=3"),
        )
        val leap = ProblemSink()
        assertEquals(
            listOf("2026-09-01T09:00:30", "2026-09-01T09:01:30"),
            dates(
                "DTSTART:20260901T090030",
                "RRULE:FREQ=MINUTELY;BYSECOND=30,60;COUNT=2",
                options = withProblems(leap),
            ),
        )
        assertEquals(listOf("ICAL-R008"), leap.codes)
    }

    @Test
    fun limitsEndTheSequence() {
        val sink = ProblemSink()
        val never = dates(
            "DTSTART:20260101T090000",
            "RRULE:FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30",
            options = withProblems(sink, StartMismatch.Exclude),
        )
        assertEquals(emptyList(), never)
        assertEquals(listOf("ICAL-R002", "ICAL-R003"), sink.codes)
        val beyond = ProblemSink()
        assertEquals(
            emptyList(),
            dates(
                "DTSTART:20260101T090000",
                "RRULE:FREQ=YEARLY;BYMONTH=2;BYMONTHDAY=30",
                options = withProblems(beyond, StartMismatch.Exclude),
            ),
        )
        assertEquals(listOf("ICAL-R003"), beyond.codes)
        val daily = ProblemSink()
        val options = RecurrenceOptions.builder()
            .problemListener(daily)
            .limits(RecurrenceLimits.builder().maxOccurrences(5).build())
            .build()
        assertEquals(5, dates("DTSTART:20260101T090000", "RRULE:FREQ=SECONDLY", options = options).size)
        assertEquals(listOf("ICAL-R001"), daily.codes)
        val idle = RecurrenceOptions.builder()
            .limits(RecurrenceLimits.builder().maxIterationsWithoutMatch(10).build())
            .startMismatch(StartMismatch.Exclude)
            .build()
        assertEquals(emptyList(), dates("DTSTART:20260101T090000", "RRULE:FREQ=MINUTELY;BYMONTH=6", options = idle))
        val leapDay = dates(
            "DTSTART:20240229T090000",
            "RRULE:FREQ=MINUTELY;INTERVAL=7;BYMONTH=2;BYMONTHDAY=29;BYHOUR=9;BYMINUTE=0,7;COUNT=3",
        )
        assertEquals(listOf("2024-02-29T09:00", "2024-02-29T09:07"), leapDay)
    }

    @Test
    fun selectorsAreSets() {
        val tuesdays = dates("DTSTART:20260901T090000", "RRULE:FREQ=WEEKLY;BYDAY=TU,TH,TU;COUNT=12")
        assertEquals(12, tuesdays.size)
        assertEquals(12, tuesdays.toSet().size)
        val positions = dates("DTSTART:20260901T090000", "RRULE:FREQ=MONTHLY;BYMONTHDAY=1,2;BYSETPOS=1,-2;COUNT=2")
        assertEquals(listOf("2026-09-01T09:00", "2026-10-01T09:00"), positions)
        val tuesday = dates("DTSTART:20260901T090000", "RRULE:FREQ=MONTHLY;BYDAY=TU;BYMONTHDAY=1,2;COUNT=3")
        assertEquals(listOf("2026-09-01T09:00", "2026-12-01T09:00", "2027-02-02T09:00"), tuesday)
        val overlap = dates("DTSTART:20260901T090000", "RRULE:FREQ=MONTHLY;BYMONTHDAY=1,2,3;BYSETPOS=1,1,-3;COUNT=2")
        assertEquals(listOf("2026-09-01T09:00", "2026-10-01T09:00"), overlap)
    }

    @Test
    fun yearlyRuleWithAUtcStart() {
        val dates = dates("DTSTART:20260115T230000Z", "RRULE:FREQ=YEARLY;COUNT=3")
        assertEquals(listOf("2026-01-15T23:00:00Z", "2027-01-15T23:00:00Z", "2028-01-15T23:00:00Z"), dates)
        val byMonth = dates("DTSTART:20261231T233000Z", "RRULE:FREQ=YEARLY;BYMONTH=12;BYMONTHDAY=31;COUNT=2")
        assertEquals(listOf("2026-12-31T23:30:00Z", "2027-12-31T23:30:00Z"), byMonth)
    }

    @Test
    fun recurrenceSetCombinesAndExcludes() {
        val set = event(
            "DTSTART;TZID=Europe/Berlin:20260901T090000",
            "RRULE:FREQ=DAILY;COUNT=5",
            "RDATE;TZID=Europe/Berlin:20260903T090000,20260910T090000",
            "RDATE;VALUE=PERIOD;TZID=Europe/Berlin:20260904T120000/PT1H",
            "EXDATE;TZID=Europe/Berlin:20260902T090000",
            "EXDATE;VALUE=DATE:20260904",
        ).recurrenceSet!!
        assertTrue(set.isFinite)
        assertEquals(
            listOf("2026-09-01T09:00", "2026-09-03T09:00", "2026-09-05T09:00", "2026-09-10T09:00"),
            Sequence { set.iterator() }.map(::local).toList(),
        )
        assertEquals(
            listOf("2026-09-03T09:00", "2026-09-05T09:00"),
            set.between(
                ICalDateTime.Floating(LocalDateTime(2026, 9, 3, 9, 0)),
                ICalDateTime.Floating(LocalDateTime(2026, 9, 10, 9, 0)),
            ).map(::local).toList(),
        )
        val exrule = event(
            "DTSTART:20260901T090000",
            "RRULE:FREQ=DAILY;COUNT=6",
            "EXRULE:FREQ=DAILY;INTERVAL=2",
        ).recurrenceSet!!
        assertEquals(1, exrule.exceptionRules.size)
        assertEquals(
            listOf("2026-09-02T09:00", "2026-09-04T09:00", "2026-09-06T09:00"),
            Sequence { exrule.iterator() }.map(::local).toList(),
        )
        val datesOnly = event(
            "DTSTART;VALUE=DATE:20260901",
            "RDATE;VALUE=DATE:20260801,20260901,20261001",
        ).recurrenceSet!!
        assertEquals(
            listOf("2026-08-01", "2026-09-01", "2026-10-01"),
            Sequence {
                datesOnly.iterator()
            }.map(::local).toList(),
        )
        assertNull(event("SUMMARY:x").recurrenceSet)
    }

    @Test
    fun builderAndForms() {
        val start = ICalDateTime.Date(LocalDate(2026, 9, 1))
        val set = RecurrenceSet.builder(start)
            .rule(Recur.builder(Frequency.WEEKLY).count(3).build())
            .date(ICalDateTime.Date(LocalDate(2026, 9, 2)))
            .period(
                ICalPeriod(
                    ICalDateTime.Date(LocalDate(2026, 9, 3)),
                    null,
                    io.github.calendrum.value.ICalDuration.of(kotlin.time.Duration.parse("P1D")),
                ),
            )
            .exception(ICalDateTime.Date(LocalDate(2026, 9, 8)))
            .build()
        val values = Sequence { set.iterator() }.toList()
        assertSame(start, values.first())
        assertEquals(listOf("2026-09-01", "2026-09-02", "2026-09-03", "2026-09-15"), values.map(::local))
        assertTrue(values.all { it is ICalDateTime.Date })
        assertFalse(RecurrenceSet.builder(start).rule(Recur.builder(Frequency.DAILY).build()).build().isFinite)
        val utcRule = Recur.builder(Frequency.HOURLY).interval(12).count(3).build()
        val utc = utcRule.expand(ICalDateTime.Utc(Instant.parse("2026-03-28T22:00:00Z"))).toList()
        assertEquals(
            listOf("2026-03-28T22:00:00Z", "2026-03-29T10:00:00Z", "2026-03-29T22:00:00Z"),
            utc.map(::local),
        )
        val zoned = Recur.builder(Frequency.DAILY).count(2).build()
            .expand(ICalDateTime.Zoned(LocalDateTime(2026, 3, 28, 2, 30), "Europe/Berlin")).toList()
        assertEquals(
            listOf("2026-03-28T02:30[Europe/Berlin]", "2026-03-29T02:30[Europe/Berlin]"),
            zoned.map {
                it.toString()
            },
        )
    }

    @Test
    fun windowsSkipAhead() {
        val rule = event("DTSTART:20000101T080000", "RRULE:FREQ=WEEKLY;BYDAY=MO,FR").rrule!!
        val start = ICalDateTime.Floating(LocalDateTime(2000, 1, 1, 8, 0))
        val from = ICalDateTime.Floating(LocalDateTime(2026, 9, 1, 0, 0))
        val to = ICalDateTime.Floating(LocalDateTime(2026, 9, 15, 0, 0))
        val window = rule.expand(start, from, to, RecurrenceOptions.Default).map(::local).toList()
        assertEquals(listOf("2026-09-04T08:00", "2026-09-07T08:00", "2026-09-11T08:00", "2026-09-14T08:00"), window)
        val full = rule.expand(start).dropWhile { local(it) < "2026-09-01" }.takeWhile { local(it) < "2026-09-15" }
        assertEquals(window, full.map(::local).toList())
        assertEquals(ProblemCode.of("ICAL-R003"), ProblemCode.StartNotInRule)
    }
}

class RecurrenceProblemCodesTest {
    @Test
    fun theRecurrenceSeriesFollowsTheModelSeries() {
        val codes = listOf(
            ProblemCode.RecurrenceLimitReached,
            ProblemCode.RecurrenceNoMatch,
            ProblemCode.StartNotInRule,
            ProblemCode.RscaleNotSupported,
            ProblemCode.UntilTypeMismatch,
            ProblemCode.UntilWithoutZone,
            ProblemCode.TimePartWithDate,
            ProblemCode.LeapSecondIgnored,
            ProblemCode.FormMismatch,
        )
        val first = ProblemCode.MissingRequiredField.index + 1
        assertEquals(codes, ProblemCode.known.drop(first).take(codes.size))
        for ((index, code) in codes.withIndex()) {
            assertEquals("ICAL-R00${index + 1}", code.id)
            assertSame(code, ProblemCode.of(code.id.lowercase()))
            assertTrue(code.message.isNotEmpty())
        }
        val severities = codes.map { it.severity.name.first() }.joinToString("")
        assertEquals("WWWWWIWII", severities)
    }
}
