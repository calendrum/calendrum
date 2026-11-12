// `huge` is a flat stream of `personal`- and `university`-shaped VEVENT (no RRULE, one component per occurrence),
// alternated and given a globally unique UID, until the running byte count crosses `HUGE_TARGET_BYTES`. Series and
// RECURRENCE-ID machinery is deliberately not reused here: flattening keeps the byte target exact and the generator
// streaming (`ByteCountingSink` via `TeeSink`, never buffering the whole corpus to know its size).

package io.github.calendrum.benchmarks.corpus

internal fun writeHuge(sink: ByteSink) {
    val counting = ByteCountingSink()
    val tee = TeeSink(sink, counting)
    val writer = ContentLineWriter(tee)
    val rng = SplitMix64(CORPUS_SEED xor 0x48554745L)

    writer.raw("BEGIN:VCALENDAR")
    writer.property("PRODID", value = "-//ics-mp//Benchmark Corpus Generator//EN")
    writer.property("VERSION", value = "2.0")
    writer.property("CALSCALE", value = "GREGORIAN")
    for (zone in TimeZoneData.zones) {
        TimeZoneData.writeVtimezone(writer, zone)
    }

    var index = 0
    while (counting.count < HUGE_TARGET_BYTES) {
        if (index % 2 == 0) {
            writeHugePersonalEvent(writer, rng, index)
        } else {
            writeHugeUniversityEvent(writer, rng, index)
        }
        index++
    }
    writer.raw("END:VCALENDAR")
}

private fun writeHugePersonalEvent(writer: ContentLineWriter, rng: SplitMix64, index: Int) {
    val zone = rng.pick(TimeZoneData.zones)
    val eventDate = CORPUS_BASE_DATE.plusDays(rng.nextIntRange(-400, 400).toLong())
    val hour = rng.nextIntRange(7, 20)

    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = "huge-personal-$index@example.com")
    writer.property("DTSTAMP", value = utcDateTime(eventDate, 8, 0))
    writer.property("SEQUENCE", value = "0")
    writer.property("SUMMARY", value = escapeText(rng.pick(FakeData.meetingTopics)))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property(
        "DESCRIPTION",
        value = escapeText(FakeData.description(rng, PERSONAL_DESCRIPTION_SENTENCES)),
    )
    writer.property("CATEGORIES", value = rng.pick(FakeData.categories))
    writer.property("DTSTART", "TZID" to zone.tzid, value = localDateTime(eventDate, hour, 0))
    writer.property("DTEND", "TZID" to zone.tzid, value = localDateTime(eventDate, hour + 1, 0))
    writer.raw("END:VEVENT")
}

private fun writeHugeUniversityEvent(writer: ContentLineWriter, rng: SplitMix64, index: Int) {
    val zone = rng.pick(TimeZoneData.zones)
    val weekday = ACADEMIC_WEEKDAYS[index % ACADEMIC_WEEKDAYS.size]
    val eventDate = firstOccurrenceOnOrAfter(CORPUS_BASE_DATE.plusDays(rng.nextIntRange(0, 100).toLong()), weekday)
    val hour = 8 + (index % 10)
    val courseName = FakeData.courseNames[index % FakeData.courseNames.size]
    val teacherName = FakeData.personName(rng)

    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = "huge-university-$index@example.com")
    writer.property("DTSTAMP", value = utcDateTime(eventDate.plusDays(-3), 9, 0))
    writer.property("SEQUENCE", value = "0")
    writer.property("SUMMARY", value = escapeText(courseName))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property("CATEGORIES", value = "EDUCATION")
    writer.property(
        "ATTENDEE",
        "CN" to teacherName,
        "ROLE" to "CHAIR",
        "PARTSTAT" to "ACCEPTED",
        "CUTYPE" to "INDIVIDUAL",
        value = FakeData.mailto(teacherName, index, rng),
    )
    writer.property("DTSTART", "TZID" to zone.tzid, value = localDateTime(eventDate, hour, 0))
    writer.property("DTEND", "TZID" to zone.tzid, value = localDateTime(eventDate, hour + 1, 30))
    writer.raw("END:VEVENT")
}
