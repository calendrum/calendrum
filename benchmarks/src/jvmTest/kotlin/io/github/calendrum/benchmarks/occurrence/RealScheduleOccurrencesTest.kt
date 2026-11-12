package io.github.calendrum.benchmarks.occurrence

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.assumeRealCorpus
import io.github.calendrum.model.calendars
import io.github.calendrum.model.get
import io.github.calendrum.occurrence.occurrences
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class RealScheduleOccurrencesTest {
    private val from = Instant.parse("2025-09-01T00:00:00Z")
    private val to = Instant.parse("2026-07-01T00:00:00Z")

    fun lines(): List<String> {
        val bytes = Files.readAllBytes(locate("corpus/real").resolve("university/university-group-schedule-01.ics"))
        val calendar = ICal.parse(bytes).calendars.single()
        return calendar.occurrences(from, to).map { occurrence ->
            val uid = occurrence.component.property("UID")?.value ?: "-"
            val kind = when {
                occurrence.component.property("RECURRENCE-ID") != null -> "override"
                occurrence.recurrenceId != null -> "instance"
                else -> "single"
            }
            listOf(occurrence.start, occurrence.end, uid, occurrence.recurrenceId ?: "-", kind).joinToString("\t")
        }.toList()
    }

    @Test
    fun aSemesterOfTheRealScheduleMatchesTheGoldenFile() {
        assumeRealCorpus()
        val actual = lines()
        val golden = locate(
            "benchmarks/src/jvmTest/resources",
        ).resolve("io/github/calendrum/benchmarks/occurrence/real-university-semester.tsv")
        if (System.getenv("CALENDRUM_UPDATE_GOLDEN") != null) {
            Files.write(golden, (actual.joinToString("\n") + "\n").toByteArray())
        }
        val expected = Files.readAllLines(golden).filter { it.isNotEmpty() }
        assertEquals(expected, actual)
    }

    @Test
    fun ical4jAgreesOnEveryStart() {
        assumeRealCorpus()
        val bytes = Files.readAllBytes(locate("corpus/real").resolve("university/university-group-schedule-01.ics"))
        val calendar = net.fortuna.ical4j.data.CalendarBuilder().build(java.io.ByteArrayInputStream(bytes))
        val events = calendar.getComponents<net.fortuna.ical4j.model.component.VEvent>("VEVENT")
        val window = net.fortuna.ical4j.model.Period<java.time.temporal.Temporal>(
            java.time.ZonedDateTime.parse("2025-09-01T00:00:00Z"),
            java.time.ZonedDateTime.parse("2026-07-01T00:00:00Z"),
        )
        val overridden = HashMap<String, MutableSet<java.time.LocalDateTime>>()
        val expected = HashSet<String>()
        for (event in events) {
            val uid = event.getProperty<net.fortuna.ical4j.model.Property>("UID").orElseThrow().value
            val recurrenceId = event.getProperty<net.fortuna.ical4j.model.property.RecurrenceId<*>>("RECURRENCE-ID")
            if (recurrenceId.isPresent) {
                overridden.getOrPut(uid) { HashSet() } += local(recurrenceId.get().date)
                val start =
                    local(event.getProperty<net.fortuna.ical4j.model.property.DtStart<*>>("DTSTART").orElseThrow().date)
                if (start >= java.time.LocalDateTime.parse("2025-09-01T03:00:00") &&
                    start < java.time.LocalDateTime.parse("2026-07-01T03:00:00")
                ) {
                    expected += "$uid $start"
                }
            }
        }
        for (event in events) {
            if (event.getProperty<net.fortuna.ical4j.model.Property>("RECURRENCE-ID").isPresent) continue
            val uid = event.getProperty<net.fortuna.ical4j.model.Property>("UID").orElseThrow().value
            for (period in event.calculateRecurrenceSet<java.time.temporal.Temporal>(window)) {
                val start = local(period.start)
                if (overridden[uid]?.contains(start) == true) continue
                expected += "$uid $start"
            }
        }
        val bytesOurs = Files.readAllBytes(locate("corpus/real").resolve("university/university-group-schedule-01.ics"))
        val actual = ICal.parse(bytesOurs).calendars.single().occurrences(from, to).map { occurrence ->
            val value = if (occurrence.component.property("RECURRENCE-ID") != null) {
                occurrence.component[io.github.calendrum.model.Properties.DTSTART]!!
            } else {
                occurrence.startLocal
            }
            "${occurrence.component.property("UID")!!.value} ${javaLocal(value)}"
        }.toSet()
        assertEquals(emptyList(), (expected - actual).sorted(), "ical4j only")
        assertEquals(emptyList(), (actual - expected).sorted(), "Calendrum only")
    }

    private fun local(value: java.time.temporal.Temporal): java.time.LocalDateTime = when (value) {
        is java.time.ZonedDateTime -> value.toLocalDateTime()
        is java.time.OffsetDateTime -> value.toLocalDateTime()
        is java.time.LocalDateTime -> value
        else -> error("Unexpected temporal ${value::class}")
    }

    private fun javaLocal(value: io.github.calendrum.value.ICalDateTime): java.time.LocalDateTime = when (value) {
        is io.github.calendrum.value.ICalDateTime.Zoned -> java.time.LocalDateTime.parse(value.dateTime.toString())

        is io.github.calendrum.value.ICalDateTime.Floating -> java.time.LocalDateTime.parse(value.dateTime.toString())

        is io.github.calendrum.value.ICalDateTime.Utc ->
            java.time.LocalDateTime.ofInstant(
                java.time.Instant.parse(value.instant.toString()),
                java.time.ZoneOffset.UTC,
            )

        is io.github.calendrum.value.ICalDateTime.Date -> java.time.LocalDate.parse(
            value.date.toString(),
        ).atStartOfDay()
    }

    private fun locate(relative: String): Path {
        var directory: Path? = Paths.get(System.getProperty("user.dir")).toAbsolutePath()
        while (directory != null) {
            val candidate = directory.resolve(relative)
            if (Files.isDirectory(candidate)) return candidate
            directory = directory.parent
        }
        throw IllegalStateException("No $relative above ${System.getProperty("user.dir")}")
    }
}
