// "20-50 thousand VEVENT" for `university` is read as the occurrence count, not the physical component count: almost
// every occurrence is compressed into one `RRULE` per course series, and only series masters plus `RECURRENCE-ID`
// overrides (~10% of occurrences) become separate VEVENT components. With `UNIVERSITY_COURSE_SERIES_COUNT = 12000` and
// 15 weeks per series that is 180 000 occurrences and ~29 700 physical VEVENT, inside the 20-50k component range too,
// but that is a consequence of the constants, not the goal.

package io.github.calendrum.benchmarks.corpus

internal val ACADEMIC_WEEKDAYS: List<Weekday> = listOf(
    Weekday.MONDAY,
    Weekday.TUESDAY,
    Weekday.WEDNESDAY,
    Weekday.THURSDAY,
    Weekday.FRIDAY,
)

internal fun writeUniversity(sink: ByteSink) {
    val writer = ContentLineWriter(sink)
    val rng = SplitMix64(CORPUS_SEED xor 0x554E4956L)

    writer.raw("BEGIN:VCALENDAR")
    writer.property("PRODID", value = "-//ics-mp//Benchmark Corpus Generator//EN")
    writer.property("VERSION", value = "2.0")
    writer.property("CALSCALE", value = "GREGORIAN")
    for (zone in TimeZoneData.zones) {
        TimeZoneData.writeVtimezone(writer, zone)
    }

    for (seriesIndex in 0 until UNIVERSITY_COURSE_SERIES_COUNT) {
        writeCourseSeries(writer, rng, seriesIndex)
    }
    writer.raw("END:VCALENDAR")
}

// `weeklyOccurrences` is reused as it is by `CorpusStructuralTest` to recompute the occurrence dates of a series: it is
// the tiny loop of the generator itself, a plain unrolling of the WEEKLY rule and not a real RRULE engine, so reusing
// it does not smuggle a library dependency into the structural test.
private fun writeCourseSeries(writer: ContentLineWriter, rng: SplitMix64, seriesIndex: Int) {
    val zone = TimeZoneData.zones[seriesIndex % TimeZoneData.zones.size]
    val weekday = ACADEMIC_WEEKDAYS[seriesIndex % ACADEMIC_WEEKDAYS.size]
    val hour = 8 + (seriesIndex % 10)
    val courseName = FakeData.courseNames[seriesIndex % FakeData.courseNames.size]
    val section = FakeData.courseSections[(seriesIndex / FakeData.courseNames.size) % FakeData.courseSections.size]
    val teacherName = FakeData.personName(rng)
    val uid = "university-$seriesIndex@example.com"

    val firstOccurrence = firstOccurrenceOnOrAfter(CORPUS_BASE_DATE, weekday)
    val occurrences = weeklyOccurrences(firstOccurrence, UNIVERSITY_SEMESTER_WEEKS)
    val until = untilUtcAfter(occurrences.last(), zone.standardOffsetMinutes)
    val excludedWeeks = UNIVERSITY_HOLIDAYS.filter { it.second == weekday }.map { it.first }.toSet()

    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = uid)
    writer.property("DTSTAMP", value = utcDateTime(firstOccurrence.plusDays(-14), 9, 0))
    writer.property("CREATED", value = utcDateTime(firstOccurrence.plusDays(-14), 9, 0))
    writer.property("LAST-MODIFIED", value = utcDateTime(firstOccurrence.plusDays(-2), 11, 0))
    writer.property("SEQUENCE", value = "0")
    writer.property("SUMMARY", value = escapeText("$courseName ($section)"))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property(
        "DESCRIPTION",
        value = escapeText(FakeData.description(rng, UNIVERSITY_DESCRIPTION_SENTENCES)),
    )
    writer.property("CATEGORIES", value = "EDUCATION,${rng.pick(FakeData.secondaryCategories)}")
    writer.property(
        "ATTENDEE",
        "CN" to teacherName,
        "ROLE" to "CHAIR",
        "PARTSTAT" to "ACCEPTED",
        "CUTYPE" to "INDIVIDUAL",
        value = FakeData.mailto(teacherName, seriesIndex, rng),
    )
    if (rng.chancePercent(20)) {
        val coTeacherName = FakeData.personName(rng)
        writer.property(
            "ATTENDEE",
            "CN" to coTeacherName,
            "ROLE" to "REQ-PARTICIPANT",
            "PARTSTAT" to "ACCEPTED",
            "CUTYPE" to "INDIVIDUAL",
            value = FakeData.mailto(coTeacherName, seriesIndex + UNIVERSITY_COURSE_SERIES_COUNT, rng),
        )
    }
    writer.property("DTSTART", "TZID" to zone.tzid, value = localDateTime(firstOccurrence, hour, 0))
    writer.property("DTEND", "TZID" to zone.tzid, value = localDateTime(firstOccurrence, hour + 1, 30))
    writer.property(
        "RRULE",
        value = "FREQ=WEEKLY;BYDAY=${weekday.icsCode};UNTIL=$until",
    )
    for (excludedWeek in excludedWeeks.sorted()) {
        if (excludedWeek < occurrences.size) {
            writer.property(
                "EXDATE",
                "TZID" to zone.tzid,
                value = localDateTime(occurrences[excludedWeek], hour, 0),
            )
        }
    }
    writer.raw("END:VEVENT")

    for ((weekIndex, occurrenceDate) in occurrences.withIndex()) {
        if (weekIndex in excludedWeeks) continue
        if (!rng.chancePercent(UNIVERSITY_OVERRIDE_PERCENT)) continue
        writeOverride(writer, rng, uid, zone, occurrenceDate, hour, courseName, section)
    }
}

private fun writeOverride(
    writer: ContentLineWriter,
    rng: SplitMix64,
    uid: String,
    zone: ZoneDefinition,
    occurrenceDate: CivilDate,
    originalHour: Int,
    courseName: String,
    section: String,
) {
    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = uid)
    writer.property(
        "RECURRENCE-ID",
        "TZID" to zone.tzid,
        value = localDateTime(occurrenceDate, originalHour, 0),
    )
    writer.property("DTSTAMP", value = utcDateTime(occurrenceDate.plusDays(-3), 9, 0))
    writer.property("SEQUENCE", value = "1")
    writer.property("SUMMARY", value = escapeText("$courseName ($section) — room changed"))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property("CATEGORIES", value = "EDUCATION")
    writer.property("DTSTART", "TZID" to zone.tzid, value = localDateTime(occurrenceDate, originalHour, 0))
    writer.property("DTEND", "TZID" to zone.tzid, value = localDateTime(occurrenceDate, originalHour + 1, 30))
    writer.raw("END:VEVENT")
}

internal fun firstOccurrenceOnOrAfter(base: CivilDate, weekday: Weekday): CivilDate {
    val diff = (weekday.ordinal - base.weekday.ordinal + 7) % 7
    return base.plusDays(diff.toLong())
}
