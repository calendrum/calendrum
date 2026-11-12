package io.github.calendrum.benchmarks.recur

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class RecurrenceDifferentialTest {
    @Test
    fun randomRulesMatchTheOracle() {
        var ical4j = 0
        var biweekly = 0
        val samples = RecurrenceRivals.rules(SEED, RULES)
        for (sample in samples) {
            val oracle = RecurrenceRivals.oracle(sample).take(RecurrenceRivals.LIMIT)
            assertEquals(oracle, RecurrenceRivals.ours(sample).take(RecurrenceRivals.LIMIT), sample.toString())
            if (runCatching { RecurrenceRivals.ical4j(sample).take(RecurrenceRivals.LIMIT) }.getOrNull() !=
                oracle
            ) {
                ical4j++
            }
            if (runCatching { RecurrenceRivals.biweekly(sample) }.getOrNull() != oracle) biweekly++
        }
        println("recurrence differential: ical4j differs on $ical4j, biweekly on $biweekly of ${samples.size} rules")
        assertTrue(ical4j < RULES / 3 && biweekly < RULES / 3, "rivals diverge more than expected: $ical4j, $biweekly")
    }

    @Test
    fun knownDisagreementsStillHold() {
        for (case in DISAGREEMENTS) {
            val sample = RivalRule(case.rule, case.start, case.start.length == 8)
            val ours = RecurrenceRivals.ours(sample)
            assertEquals(RecurrenceRivals.oracle(sample), ours, case.id)
            val rival = runCatching {
                if (case.rival == ICAL4J) RecurrenceRivals.ical4j(sample) else RecurrenceRivals.biweekly(sample)
            }.getOrNull()
            assertNotEquals(ours, rival, "${case.id}: ${case.rival} now agrees, update RESEARCH")
            if (case.agrees != null) {
                val other = if (case.agrees ==
                    ICAL4J
                ) {
                    RecurrenceRivals.ical4j(sample)
                } else {
                    RecurrenceRivals.biweekly(sample)
                }
                assertEquals(ours, other, "${case.id}: ${case.agrees} should agree with us")
            }
        }
    }

    private class Disagreement(
        val id: String,
        val rival: String,
        val rule: String,
        val start: String,
        val agrees: String?,
    )

    private companion object {
        const val SEED = 20261017L
        const val RULES = 3000
        const val ICAL4J = "ical4j"
        const val BIWEEKLY = "biweekly"

        val DISAGREEMENTS = listOf(
            Disagreement("I1", ICAL4J, "FREQ=DAILY;COUNT=34;BYMONTH=11,5,3;BYDAY=TH,TU,TH", "20151228T123000", null),
            Disagreement("I2", ICAL4J, "FREQ=HOURLY;BYMONTHDAY=25,29;BYDAY=SU", "19980609T053000", null),
            Disagreement("I3", ICAL4J, "FREQ=YEARLY;BYMONTH=9,2,10;BYYEARDAY=105,69", "20130706T074500", BIWEEKLY),
            Disagreement("I4", ICAL4J, "FREQ=WEEKLY;BYMONTH=8,12;BYDAY=SU,SA,WE", "20200327T040000", BIWEEKLY),
            Disagreement("I5", ICAL4J, "FREQ=SECONDLY;UNTIL=20020828T110000;BYMINUTE=3", "20020315T180000", BIWEEKLY),
            Disagreement("I6", ICAL4J, "FREQ=YEARLY;BYMONTHDAY=-17,19;BYDAY=-2WE", "20010124T233000", null),
            Disagreement("B1", BIWEEKLY, "FREQ=DAILY;INTERVAL=4;BYDAY=SA,TH;BYHOUR=4,7", "19980704T234500", ICAL4J),
            Disagreement(
                "B2",
                BIWEEKLY,
                "FREQ=MINUTELY;INTERVAL=2;COUNT=10;BYYEARDAY=-184,156,-331",
                "20270201T100000",
                null,
            ),
            Disagreement("B3", BIWEEKLY, "FREQ=HOURLY;COUNT=40;BYHOUR=16,23;BYSETPOS=5", "20131226T140000", null),
            Disagreement("B4", BIWEEKLY, "FREQ=MONTHLY;INTERVAL=2;BYMONTH=5,6;BYDAY=2TU,4TU", "20180427T031500", null),
            Disagreement("B5", BIWEEKLY, "FREQ=SECONDLY;COUNT=31;BYMONTH=6,11,5;BYDAY=WE", "19991117T124500", null),
            Disagreement("B6", BIWEEKLY, "FREQ=YEARLY;COUNT=7;BYMONTHDAY=3", "20070223T083000", ICAL4J),
            Disagreement("B7", BIWEEKLY, "FREQ=YEARLY;INTERVAL=3;BYWEEKNO=22,7;BYHOUR=15,1,4", "20200314T134500", null),
        )
    }
}
