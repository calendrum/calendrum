package io.github.calendrum.benchmarks.corpus

internal fun writeInvite(sink: ByteSink, rng: SplitMix64, index: Int) {
    val writer = ContentLineWriter(sink)
    val zone = TimeZoneData.zones[index % TimeZoneData.zones.size]
    val meetingDate = CORPUS_BASE_DATE.plusDays((index % 90).toLong())
    val organizerName = FakeData.personName(rng)
    val attendeeCount = rng.nextIntRange(INVITE_ATTENDEE_MIN, INVITE_ATTENDEE_MAX)

    writer.raw("BEGIN:VCALENDAR")
    writer.property("PRODID", value = "-//ics-mp//Benchmark Corpus Generator//EN")
    writer.property("VERSION", value = "2.0")
    writer.property("METHOD", value = "REQUEST")
    writer.property("CALSCALE", value = "GREGORIAN")
    TimeZoneData.writeVtimezone(writer, zone)

    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = "invite-$index@example.com")
    writer.property("DTSTAMP", value = utcDateTime(meetingDate.plusDays(-7), 9, 0))
    writer.property("SEQUENCE", value = "0")
    writer.property("CLASS", value = "PUBLIC")
    writer.property("STATUS", value = "CONFIRMED")
    writer.property("CREATED", value = utcDateTime(meetingDate.plusDays(-7), 9, 0))
    writer.property("LAST-MODIFIED", value = utcDateTime(meetingDate.plusDays(-1), 15, 30))
    writer.property(
        "ORGANIZER",
        "CN" to organizerName,
        value = FakeData.mailto(organizerName, index, rng),
    )
    for (attendeeIndex in 0 until attendeeCount) {
        val attendeeName = FakeData.personName(rng)
        writer.property(
            "ATTENDEE",
            "CN" to attendeeName,
            "ROLE" to rng.pick(FakeData.roles),
            "PARTSTAT" to rng.pick(FakeData.partstats),
            "RSVP" to if (rng.chancePercent(70)) "TRUE" else "FALSE",
            "CUTYPE" to "INDIVIDUAL",
            value = FakeData.mailto(attendeeName, index * 100 + attendeeIndex, rng),
        )
    }
    writer.property("SUMMARY", value = escapeText(rng.pick(FakeData.meetingTopics)))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property(
        "DESCRIPTION",
        value = escapeText(FakeData.description(rng, INVITE_DESCRIPTION_SENTENCES)),
    )
    writer.property("CATEGORIES", value = rng.pick(FakeData.categories))
    writer.property(
        "DTSTART",
        "TZID" to zone.tzid,
        value = localDateTime(meetingDate, 10, 0),
    )
    writer.property(
        "DTEND",
        "TZID" to zone.tzid,
        value = localDateTime(meetingDate, 11, 0),
    )
    writer.property("X-MICROSOFT-CDO-BUSYSTATUS", value = "BUSY")
    writer.property("X-MICROSOFT-CDO-IMPORTANCE", value = "1")
    writer.property("X-MICROSOFT-DISALLOW-COUNTER", value = "FALSE")

    writer.raw("BEGIN:VALARM")
    writer.property("ACTION", value = "DISPLAY")
    writer.property("DESCRIPTION", value = "Reminder")
    writer.property("TRIGGER", value = "-PT15M")
    writer.raw("END:VALARM")

    writer.raw("END:VEVENT")
    writer.raw("END:VCALENDAR")
}

internal fun writeInvite(sink: ByteSink) {
    writeInvite(sink, SplitMix64(CORPUS_SEED), 0)
}

internal fun writeInviteBatch(sink: ByteSink, count: Int = INVITE_BATCH_COUNT) {
    for (index in 0 until count) {
        val rng = SplitMix64(CORPUS_SEED xor (index.toLong() * 0x9E3779B1L))
        writeInvite(sink, rng, index)
    }
}
