package io.github.calendrum.benchmarks.rrule

import io.github.calendrum.model.Decoded
import io.github.calendrum.model.Properties
import io.github.calendrum.model.decode
import io.github.calendrum.recur.RecurrenceOptions
import io.github.calendrum.recur.StartMismatch
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.Recur
import kotlinx.datetime.LocalDateTime

object RruleRules {
    const val SIMPLE: String = "simple"
    const val COMPLEX: String = "complex"
    const val START: String = "20260105T090000"
    const val DUPLICATE_SELECTORS: String = "FREQ=WEEKLY;BYDAY=TU,TH,TU;COUNT=500"

    val simple: List<String> = listOf(
        "FREQ=WEEKLY;BYDAY=MO,WE",
        "FREQ=DAILY",
        "FREQ=WEEKLY;INTERVAL=2;BYDAY=TU,TH",
        "FREQ=MONTHLY;BYMONTHDAY=5",
        "FREQ=YEARLY",
    )

    val complex: List<String> = listOf(
        "FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-1",
        "FREQ=YEARLY;BYWEEKNO=2,20,40;BYDAY=MO",
        "FREQ=MONTHLY;BYMONTHDAY=-1,-15",
        DUPLICATE_SELECTORS,
        "FREQ=YEARLY;BYMONTH=1,6;BYDAY=-1FR,2MO",
        "FREQ=DAILY;BYHOUR=9,13,17;BYMINUTE=0,30",
    )

    val start: ICalDateTime = ICalDateTime.Floating(LocalDateTime(2026, 1, 5, 9, 0))
    val end: ICalDateTime = ICalDateTime.Floating(LocalDateTime(2036, 1, 5, 9, 0))

    val expectedTotals: Map<String, Int> = mapOf(SIMPLE to 5_348, COMPLEX to 22_842)

    val options: RecurrenceOptions = RecurrenceOptions.builder().startMismatch(StartMismatch.Exclude).build()

    fun texts(set: String): List<String> = when (set) {
        SIMPLE -> simple
        COMPLEX -> complex
        else -> error("Unknown rule set $set")
    }

    fun rules(set: String): List<Recur> = texts(set).map { text ->
        val component = ICalComponent("VEVENT") { property("RRULE", text) }
        (component.decode(Properties.RRULE) as Decoded.Value).value
    }
}
