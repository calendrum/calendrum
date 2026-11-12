@file:OptIn(InternalICalApi::class)

package io.github.calendrum.benchmarks.values

import biweekly.io.text.ICalReader
import io.github.calendrum.InternalICalApi
import io.github.calendrum.value.ValueCodecs
import io.github.calendrum.value.ValueProblems
import net.fortuna.ical4j.model.Recur
import java.nio.file.Files
import java.nio.file.Paths
import java.time.temporal.Temporal
import kotlin.test.Test
import kotlin.test.assertEquals

// Records which RECUR values Calendrum, ical4j and biweekly accept into `build/reports/recur-differential.md`; only the
// RFC examples must be accepted by Calendrum without problems — differences with the competitors are reported, not
// asserted.
class RecurDifferentialTest {
    @Test
    fun recurAgainstIcal4jAndBiweekly() {
        val report =
            StringBuilder(
                "| RRULE | Calendrum lenient | Calendrum strict | ical4j 4.3.0 | biweekly 0.6.8 |\n|---|---|---|---|---|\n",
            )
        var disagreements = 0
        for (rule in RFC_RULES + EDGE_RULES) {
            val problems = ArrayList<String>()
            val lenient = ValueCodecs(false, false, ValueProblems { code, _ -> problems += code.id })
                .decode(ValueCodecs.RECUR, rule, 0, rule.length, null)
            val strict = ValueCodecs(true, false, null).decode(ValueCodecs.RECUR, rule, 0, rule.length, null)
            val ical4j = ical4j(rule)
            val biweekly = biweekly(rule)
            if (rule in RFC_RULES) {
                assertEquals(listOf(), problems, rule)
                assertEquals(true, strict != null, rule)
            }
            val ours = (if (lenient != null) "accepted" else "rejected") +
                (if (problems.isEmpty()) "" else " (" + problems.distinct().joinToString() + ")")
            val agree = (lenient != null) == ical4j.startsWith("accepted") &&
                (lenient != null) == biweekly.startsWith("accepted")
            if (!agree) disagreements += 1
            if (rule in EDGE_RULES || !agree) {
                report.append(
                    "| `$rule` | $ours | ${if (strict != null) "accepted" else "rejected"} | $ical4j | $biweekly |\n",
                )
            }
        }
        report.append(
            "\nRFC examples: ${RFC_RULES.size}, edge cases: ${EDGE_RULES.size}, lenient disagreements: $disagreements\n",
        )
        println(report)
        val output = Paths.get(System.getProperty("user.dir")).resolve("build/reports/recur-differential.md")
        Files.createDirectories(output.parent)
        Files.write(output, report.toString().toByteArray())
    }

    private fun ical4j(rule: String): String = try {
        Recur<Temporal>(rule)
        "accepted"
    } catch (e: Exception) {
        "rejected (${e.javaClass.simpleName})"
    }

    private fun biweekly(rule: String): String {
        val text = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nBEGIN:VEVENT\r\nRRULE:$rule\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        ICalReader(text).use { reader ->
            val calendar = reader.readNext()
            val accepted = calendar?.events?.firstOrNull()?.recurrenceRule != null
            val warnings = reader.warnings.size
            return (if (accepted) "accepted" else "rejected") + (if (warnings > 0) " ($warnings warnings)" else "")
        }
    }

    private companion object {
        val RFC_RULES = listOf(
            "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU;BYHOUR=8,9;BYMINUTE=30",
            "FREQ=DAILY;COUNT=10",
            "FREQ=DAILY;UNTIL=19971224T000000Z",
            "FREQ=DAILY;INTERVAL=10;COUNT=5",
            "FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA",
            "FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR",
            "FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
            "FREQ=MONTHLY;BYMONTHDAY=-3",
            "FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
            "FREQ=YEARLY;BYDAY=20MO",
            "FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO",
            "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2",
            "FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40",
            "FREQ=WEEKLY;INTERVAL=2;COUNT=4;BYDAY=TU,SU;WKST=MO",
            "FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=19730429T070000Z",
            "RSCALE=GREGORIAN;FREQ=YEARLY;SKIP=FORWARD",
            "RSCALE=HEBREW;FREQ=YEARLY;BYMONTH=5L;BYMONTHDAY=8;SKIP=FORWARD",
        )
        val EDGE_RULES = listOf(
            "freq=weekly;byday=mo,we",
            "FREQ=DAILY;COUNT=3;UNTIL=20261231T000000Z",
            "FREQ=DAILY;COUNT=3;COUNT=5",
            "FREQ=DAILY;",
            "FREQ=DAILY;;COUNT=2",
            "FREQ=WEEKLY;BYDAY=MO,,TU",
            "COUNT=3",
            "FREQ=FORTNIGHTLY",
            "FREQ=DAILY;INTERVAL=0",
            "FREQ=DAILY;COUNT=-1",
            "FREQ=MONTHLY;BYMONTHDAY=32",
            "FREQ=MONTHLY;BYMONTHDAY=0",
            "FREQ=YEARLY;BYMONTH=13",
            "FREQ=YEARLY;BYWEEKNO=54",
            "FREQ=DAILY;BYHOUR=24",
            "FREQ=MINUTELY;BYSECOND=60",
            "FREQ=MONTHLY;BYDAY=0MO",
            "FREQ=MONTHLY;BYDAY=54MO",
            "FREQ=WEEKLY;BYDAY=1MO",
            "FREQ=MONTHLY;BYWEEKNO=1",
            "FREQ=WEEKLY;BYMONTHDAY=1",
            "FREQ=MONTHLY;BYSETPOS=1",
            "FREQ=WEEKLY;WKST=XX",
            "FREQ=DAILY;X-NAME=value",
            "FREQ=DAILY;UNTIL=20260901",
            "FREQ=DAILY;UNTIL=2026-09-01T10:00:00Z",
            "FREQ=DAILY;UNTIL=20161231T235960Z",
            "FREQ=DAILY;UNTIL=20260230",
            "FREQ=YEARLY;BYMONTH=5L",
            "FREQ=MONTHLY;SKIP=OMIT",
            " FREQ=DAILY",
        )
    }
}
