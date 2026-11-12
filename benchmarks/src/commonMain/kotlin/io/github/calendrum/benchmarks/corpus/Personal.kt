package io.github.calendrum.benchmarks.corpus

internal fun writePersonal(sink: ByteSink) {
    val writer = ContentLineWriter(sink)
    val rng = SplitMix64(CORPUS_SEED xor 0x50455253L)

    writer.raw("BEGIN:VCALENDAR")
    writer.property("PRODID", value = "-//ics-mp//Benchmark Corpus Generator//EN")
    writer.property("VERSION", value = "2.0")
    writer.property("CALSCALE", value = "GREGORIAN")
    for (zone in TimeZoneData.zones) {
        TimeZoneData.writeVtimezone(writer, zone)
    }

    for (index in 0 until PERSONAL_EVENT_COUNT) {
        writePersonalEvent(writer, rng, index)
    }
    writer.raw("END:VCALENDAR")
}

private fun writePersonalEvent(writer: ContentLineWriter, rng: SplitMix64, index: Int) {
    val zone = rng.pick(TimeZoneData.zones)
    val allDay = rng.chancePercent(PERSONAL_ALL_DAY_PERCENT)
    val recurring = !allDay && rng.chancePercent(PERSONAL_RECURRING_PERCENT)
    val eventDate = CORPUS_BASE_DATE.plusDays(rng.nextIntRange(-200, 200).toLong())
    val hour = rng.nextIntRange(7, 20)

    writer.raw("BEGIN:VEVENT")
    writer.property("UID", value = "personal-$index@example.com")
    writer.property("DTSTAMP", value = utcDateTime(eventDate, 8, 0))
    writer.property("CREATED", value = utcDateTime(eventDate.plusDays(-3), 8, 0))
    writer.property("LAST-MODIFIED", value = utcDateTime(eventDate.plusDays(-1), 12, 0))
    writer.property("SEQUENCE", value = "0")
    writer.property("SUMMARY", value = escapeText(rng.pick(FakeData.meetingTopics)))
    writer.property("LOCATION", value = escapeText(rng.pick(FakeData.locations)))
    writer.property(
        "DESCRIPTION",
        value = escapeText(FakeData.description(rng, PERSONAL_DESCRIPTION_SENTENCES)),
    )
    writer.property("CATEGORIES", value = rng.pick(FakeData.categories))

    if (allDay) {
        writer.property("DTSTART", "VALUE" to "DATE", value = eventDate.basicDate())
        writer.property("DTEND", "VALUE" to "DATE", value = eventDate.plusDays(1).basicDate())
    } else {
        writer.property("DTSTART", "TZID" to zone.tzid, value = localDateTime(eventDate, hour, 0))
        writer.property("DTEND", "TZID" to zone.tzid, value = localDateTime(eventDate, hour + 1, 0))
        if (recurring) {
            val freq = rng.pick(RECURRING_FREQUENCIES)
            val until = untilUtcAfter(eventDate.plusWeeks(rng.nextIntRange(4, 20).toLong()), zone.standardOffsetMinutes)
            writer.property(
                "RRULE",
                value = "FREQ=$freq;BYDAY=${eventDate.weekday.icsCode};UNTIL=$until",
            )
        }
    }

    if (rng.chancePercent(PERSONAL_ALARM_PERCENT)) {
        writer.raw("BEGIN:VALARM")
        writer.property("ACTION", value = "DISPLAY")
        writer.property("DESCRIPTION", value = "Reminder")
        writer.property("TRIGGER", value = "-PT${rng.pick(listOf(5, 10, 15, 30, 60))}M")
        writer.raw("END:VALARM")
    }

    if (rng.chancePercent(PERSONAL_X_PROP_PERCENT)) {
        if (rng.chancePercent(50)) {
            writer.property("X-APPLE-CALENDAR-COLOR", value = "#${hexColor(index)}")
            writer.property("X-APPLE-TRAVEL-ADVISORY-BEHAVIOR", value = "AUTOMATIC")
        } else {
            writer.property("X-GOOGLE-CALENDAR-CONTENT-TITLE", value = "Personal")
            writer.property("X-GOOGLE-CONFERENCE-DATA-VERSION", value = "1")
        }
    }

    writer.raw("END:VEVENT")
}

private val RECURRING_FREQUENCIES: List<String> = listOf("WEEKLY", "WEEKLY", "WEEKLY", "MONTHLY")

private val HEX_DIGITS: CharArray = "0123456789ABCDEF".toCharArray()

private fun hexColor(index: Int): String {
    val value = (index * 137) and 0xFFFFFF
    val chars = CharArray(6)
    for (i in 0 until 6) {
        chars[5 - i] = HEX_DIGITS[(value ushr (i * 4)) and 0xF]
    }
    return chars.concatToString()
}
