package io.github.calendrum.recur

import kotlin.test.Test
import kotlin.test.assertEquals

class RfcRecurrenceExamplesTest {
    private val options = RecurrenceOptions.builder().untilOffset(::newYorkOffset).build()

    private fun expand(start: String, rule: String, vararg more: String): Sequence<String> {
        val event = event("DTSTART;TZID=America/New_York:$start", "RRULE:$rule", *more)
        val set = event.recurrenceSet!!
        return Sequence { set.iterator(options) }.map(::local)
    }

    private fun finite(expected: List<String>, start: String, rule: String, vararg more: String) {
        assertEquals(expected, expand(start, rule, *more).toList(), rule)
    }

    private fun prefix(expected: List<String>, start: String, rule: String, vararg more: String) {
        assertEquals(expected, expand(start, rule, *more).take(expected.size).toList(), rule)
    }

    private fun days(year: Int, month: Int, vararg dates: Int): List<String> = dates.map { day(year, month, it) }

    private fun range(year: Int, month: Int, first: Int, last: Int): List<String> =
        (first..last).map { day(year, month, it) }

    @Test
    fun daily() {
        finite(range(1997, 9, 2, 11), "19970902T090000", "FREQ=DAILY;COUNT=10")
        finite(
            range(1997, 9, 2, 30) + range(1997, 10, 1, 31) + range(1997, 11, 1, 30) + range(1997, 12, 1, 23),
            "19970902T090000",
            "FREQ=DAILY;UNTIL=19971224T000000Z",
        )
        prefix(
            (2..30 step 2).map { day(1997, 9, it) } + (2..30 step 2).map { day(1997, 10, it) } +
                (1..29 step 2).map { day(1997, 11, it) } + days(1997, 12, 1, 3),
            "19970902T090000",
            "FREQ=DAILY;INTERVAL=2",
        )
        finite(days(1997, 9, 2, 12, 22) + days(1997, 10, 2, 12), "19970902T090000", "FREQ=DAILY;INTERVAL=10;COUNT=5")
        val january = range(1998, 1, 1, 31) + range(1999, 1, 1, 31) + range(2000, 1, 1, 31)
        finite(january, "19980101T090000", "FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA")
        finite(january, "19980101T090000", "FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1")
    }

    @Test
    fun weekly() {
        finite(
            days(1997, 9, 2, 9, 16, 23, 30) + days(1997, 10, 7, 14, 21, 28) + days(1997, 11, 4),
            "19970902T090000",
            "FREQ=WEEKLY;COUNT=10",
        )
        finite(
            days(1997, 9, 2, 9, 16, 23, 30) + days(1997, 10, 7, 14, 21, 28) + days(1997, 11, 4, 11, 18, 25) +
                days(1997, 12, 2, 9, 16, 23),
            "19970902T090000",
            "FREQ=WEEKLY;UNTIL=19971224T000000Z",
        )
        prefix(
            days(1997, 9, 2, 16, 30) + days(1997, 10, 14, 28) + days(1997, 11, 11, 25) + days(1997, 12, 9, 23) +
                days(1998, 1, 6, 20) + days(1998, 2, 3, 17),
            "19970902T090000",
            "FREQ=WEEKLY;INTERVAL=2;WKST=SU",
        )
        val tuesdaysAndThursdays = days(1997, 9, 2, 4, 9, 11, 16, 18, 23, 25, 30) + days(1997, 10, 2)
        finite(tuesdaysAndThursdays, "19970902T090000", "FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH")
        finite(tuesdaysAndThursdays, "19970902T090000", "FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH")
        finite(
            days(1997, 9, 1, 3, 5, 15, 17, 19, 29) + days(1997, 10, 1, 3, 13, 15, 17, 27, 29, 31) +
                days(1997, 11, 10, 12, 14, 24, 26, 28) + days(1997, 12, 8, 10, 12, 22),
            "19970901T090000",
            "FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR",
        )
        finite(
            days(1997, 9, 2, 4, 16, 18, 30) + days(1997, 10, 2, 14, 16),
            "19970902T090000",
            "FREQ=WEEKLY;INTERVAL=2;COUNT=8;WKST=SU;BYDAY=TU,TH",
        )
        finite(days(1997, 8, 5, 10, 19, 24), "19970805T090000", "FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO")
        finite(days(1997, 8, 5, 17, 19, 31), "19970805T090000", "FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=SU")
    }

    @Test
    fun monthly() {
        finite(
            days(1997, 9, 5) + days(1997, 10, 3) + days(1997, 11, 7) + days(1997, 12, 5) + days(1998, 1, 2) +
                days(1998, 2, 6) + days(1998, 3, 6) + days(1998, 4, 3) + days(1998, 5, 1) + days(1998, 6, 5),
            "19970905T090000",
            "FREQ=MONTHLY;COUNT=10;BYDAY=1FR",
        )
        finite(
            days(1997, 9, 5) + days(1997, 10, 3) + days(1997, 11, 7) + days(1997, 12, 5),
            "19970905T090000",
            "FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR",
        )
        finite(
            days(1997, 9, 7, 28) + days(1997, 11, 2, 30) + days(1998, 1, 4, 25) + days(1998, 3, 1, 29) +
                days(1998, 5, 3, 31),
            "19970907T090000",
            "FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
        )
        finite(
            days(1997, 9, 22) + days(1997, 10, 20) + days(1997, 11, 17) + days(1997, 12, 22) + days(1998, 1, 19) +
                days(1998, 2, 16),
            "19970922T090000",
            "FREQ=MONTHLY;COUNT=6;BYDAY=-2MO",
        )
        prefix(
            days(1997, 9, 28) + days(1997, 10, 29) + days(1997, 11, 28) + days(1997, 12, 29) + days(1998, 1, 29) +
                days(1998, 2, 26),
            "19970928T090000",
            "FREQ=MONTHLY;BYMONTHDAY=-3",
        )
        finite(
            days(1997, 9, 2, 15) + days(1997, 10, 2, 15) + days(1997, 11, 2, 15) + days(1997, 12, 2, 15) +
                days(1998, 1, 2, 15),
            "19970902T090000",
            "FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15",
        )
        finite(
            days(1997, 9, 30) + days(1997, 10, 1, 31) + days(1997, 11, 1, 30) + days(1997, 12, 1, 31) +
                days(1998, 1, 1, 31) + days(1998, 2, 1),
            "19970930T090000",
            "FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1",
        )
        finite(
            range(1997, 9, 10, 15) + range(1999, 3, 10, 13),
            "19970910T090000",
            "FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15",
        )
        prefix(
            days(1997, 9, 2, 9, 16, 23, 30) + days(1997, 11, 4, 11, 18, 25) + days(1998, 1, 6, 13, 20, 27) +
                days(1998, 3, 3, 10, 17, 24, 31),
            "19970902T090000",
            "FREQ=MONTHLY;INTERVAL=2;BYDAY=TU",
        )
        finite(
            days(2007, 1, 15, 30) + days(2007, 2, 15) + days(2007, 3, 15, 30),
            "20070115T090000",
            "FREQ=MONTHLY;BYMONTHDAY=15,30;COUNT=5",
        )
    }

    @Test
    fun yearly() {
        finite(
            (1997..2001).flatMap { days(it, 6, 10) + days(it, 7, 10) },
            "19970610T090000",
            "FREQ=YEARLY;COUNT=10;BYMONTH=6,7",
        )
        finite(
            days(1997, 3, 10) + listOf(1999, 2001, 2003).flatMap {
                days(it, 1, 10) + days(it, 2, 10) + days(it, 3, 10)
            },
            "19970310T090000",
            "FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3",
        )
        finite(
            days(1997, 1, 1) + days(1997, 4, 10) + days(1997, 7, 19) + days(2000, 1, 1) + days(2000, 4, 9) +
                days(2000, 7, 18) + days(2003, 1, 1) + days(2003, 4, 10) + days(2003, 7, 19) + days(2006, 1, 1),
            "19970101T090000",
            "FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
        )
        prefix(days(1997, 5, 19) + days(1998, 5, 18) + days(1999, 5, 17), "19970519T090000", "FREQ=YEARLY;BYDAY=20MO")
        prefix(
            days(1997, 5, 12) + days(1998, 5, 11) + days(1999, 5, 17),
            "19970512T090000",
            "FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO",
        )
        prefix(
            days(1997, 3, 13, 20, 27) + days(1998, 3, 5, 12, 19, 26) + days(1999, 3, 4, 11, 18, 25),
            "19970313T090000",
            "FREQ=YEARLY;BYMONTH=3;BYDAY=TH",
        )
        prefix(
            days(1997, 6, 5, 12, 19, 26) + days(1997, 7, 3, 10, 17, 24, 31) + days(1997, 8, 7, 14, 21, 28) +
                days(1998, 6, 4, 11, 18, 25) + days(1998, 7, 2, 9, 16, 23, 30) + days(1998, 8, 6, 13, 20, 27) +
                days(1999, 6, 3, 10, 17, 24) + days(1999, 7, 1, 8, 15, 22, 29) + days(1999, 8, 5, 12, 19, 26),
            "19970605T090000",
            "FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8",
        )
        prefix(
            days(1996, 11, 5) + days(2000, 11, 7) + days(2004, 11, 2),
            "19961105T090000",
            "FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8",
        )
    }

    @Test
    fun limitsAndPositions() {
        prefix(
            days(1998, 2, 13) + days(1998, 3, 13) + days(1998, 11, 13) + days(1999, 8, 13) + days(2000, 10, 13),
            "19970902T090000",
            "FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13",
            "EXDATE;TZID=America/New_York:19970902T090000",
        )
        prefix(
            days(1997, 9, 13) + days(1997, 10, 11) + days(1997, 11, 8) + days(1997, 12, 13) + days(1998, 1, 10) +
                days(1998, 2, 7) + days(1998, 3, 7) + days(1998, 4, 11) + days(1998, 5, 9) + days(1998, 6, 13),
            "19970913T090000",
            "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13",
        )
        finite(
            days(1997, 9, 4) + days(1997, 10, 7) + days(1997, 11, 6),
            "19970904T090000",
            "FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3",
        )
        prefix(
            days(1997, 9, 29) + days(1997, 10, 30) + days(1997, 11, 27) + days(1997, 12, 30) + days(1998, 1, 29) +
                days(1998, 2, 26) + days(1998, 3, 30),
            "19970929T090000",
            "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2",
        )
    }

    @Test
    fun subDaily() {
        finite(
            listOf("09:00", "12:00").map { day(1997, 9, 2, it) },
            "19970902T090000",
            "FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T170000Z",
        )
        finite(
            listOf("09:00", "12:00", "15:00").map { day(1997, 9, 2, it) },
            "19970902T090000",
            "FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T210000Z",
        )
        finite(
            listOf("09:00", "09:15", "09:30", "09:45", "10:00", "10:15").map { day(1997, 9, 2, it) },
            "19970902T090000",
            "FREQ=MINUTELY;INTERVAL=15;COUNT=6",
        )
        finite(
            listOf("09:00", "10:30", "12:00", "13:30").map { day(1997, 9, 2, it) },
            "19970902T090000",
            "FREQ=MINUTELY;INTERVAL=90;COUNT=4",
        )
        val times = (9..16).flatMap { hour ->
            listOf(0, 20, 40).map { "${if (hour < 10) "0" else ""}$hour:${if (it == 0) "00" else it}" }
        }
        val twoDays = times.map { day(1997, 9, 2, it) } + times.map { day(1997, 9, 3, it) }
        prefix(twoDays, "19970902T090000", "FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40")
        prefix(twoDays, "19970902T090000", "FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16")
    }
}
