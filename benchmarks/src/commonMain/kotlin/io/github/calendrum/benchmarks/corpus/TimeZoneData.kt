// VTIMEZONE blocks for 7 IANA tzdb zones, written out as literal content lines in the Outlook/Google style rather than
// computed, because the DST transition rule is exactly the published rule and does not need re-deriving. Source: IANA
// tzdb (public domain) as of 2026. `Europe/Moscow` and `Asia/Tokyo` have no DST, so a single `STANDARD` block suffices.

package io.github.calendrum.benchmarks.corpus

// The winter (non-DST) UTC offset of a zone, `standardOffsetMinutes`, is used only to turn a local date into an
// approximate UTC instant for `RRULE;UNTIL` (`untilUtcAfter`): a full day of slack absorbs the DST wobble, so exact
// offset tracking is not needed.
internal class ZoneDefinition(val tzid: String, val standardOffsetMinutes: Int, val lines: List<String>)

internal object TimeZoneData {
    val zones: List<ZoneDefinition> = listOf(
        ZoneDefinition(
            tzid = "Europe/Moscow",
            standardOffsetMinutes = 180,
            lines = listOf(
                "BEGIN:STANDARD",
                "TZOFFSETFROM:+0300",
                "TZOFFSETTO:+0300",
                "TZNAME:MSK",
                "DTSTART:19700101T000000",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            // EU rule: DST from the last Sunday of March to the last Sunday of October.
            tzid = "Europe/Berlin",
            standardOffsetMinutes = 60,
            lines = listOf(
                "BEGIN:DAYLIGHT",
                "TZOFFSETFROM:+0100",
                "TZOFFSETTO:+0200",
                "TZNAME:CEST",
                "DTSTART:19700329T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU",
                "END:DAYLIGHT",
                "BEGIN:STANDARD",
                "TZOFFSETFROM:+0200",
                "TZOFFSETTO:+0100",
                "TZNAME:CET",
                "DTSTART:19701025T030000",
                "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            tzid = "Europe/London",
            standardOffsetMinutes = 0,
            lines = listOf(
                "BEGIN:DAYLIGHT",
                "TZOFFSETFROM:+0000",
                "TZOFFSETTO:+0100",
                "TZNAME:BST",
                "DTSTART:19700329T010000",
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU",
                "END:DAYLIGHT",
                "BEGIN:STANDARD",
                "TZOFFSETFROM:+0100",
                "TZOFFSETTO:+0000",
                "TZNAME:GMT",
                "DTSTART:19701025T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            // US rule since the 2007 Energy Policy Act: DST from the 2nd Sunday of March to the 1st Sunday of November.
            tzid = "America/New_York",
            standardOffsetMinutes = -300,
            lines = listOf(
                "BEGIN:DAYLIGHT",
                "TZOFFSETFROM:-0500",
                "TZOFFSETTO:-0400",
                "TZNAME:EDT",
                "DTSTART:20070311T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU",
                "END:DAYLIGHT",
                "BEGIN:STANDARD",
                "TZOFFSETFROM:-0400",
                "TZOFFSETTO:-0500",
                "TZNAME:EST",
                "DTSTART:20071104T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            tzid = "America/Los_Angeles",
            standardOffsetMinutes = -480,
            lines = listOf(
                "BEGIN:DAYLIGHT",
                "TZOFFSETFROM:-0800",
                "TZOFFSETTO:-0700",
                "TZNAME:PDT",
                "DTSTART:20070311T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU",
                "END:DAYLIGHT",
                "BEGIN:STANDARD",
                "TZOFFSETFROM:-0700",
                "TZOFFSETTO:-0800",
                "TZNAME:PST",
                "DTSTART:20071104T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            tzid = "Asia/Tokyo",
            standardOffsetMinutes = 540,
            lines = listOf(
                "BEGIN:STANDARD",
                "TZOFFSETFROM:+0900",
                "TZOFFSETTO:+0900",
                "TZNAME:JST",
                "DTSTART:19700101T000000",
                "END:STANDARD",
            ),
        ),
        ZoneDefinition(
            // Southern hemisphere: DST from the 1st Sunday of October to the 1st Sunday of April, current rule since
            // 2008.
            tzid = "Australia/Sydney",
            standardOffsetMinutes = 600,
            lines = listOf(
                "BEGIN:DAYLIGHT",
                "TZOFFSETFROM:+1000",
                "TZOFFSETTO:+1100",
                "TZNAME:AEDT",
                "DTSTART:20081005T020000",
                "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=1SU",
                "END:DAYLIGHT",
                "BEGIN:STANDARD",
                "TZOFFSETFROM:+1100",
                "TZOFFSETTO:+1000",
                "TZNAME:AEST",
                "DTSTART:20080406T030000",
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU",
                "END:STANDARD",
            ),
        ),
    )

    fun writeVtimezone(writer: ContentLineWriter, zone: ZoneDefinition) {
        writer.raw("BEGIN:VTIMEZONE")
        writer.raw("TZID:${zone.tzid}")
        for (line in zone.lines) {
            writer.raw(line)
        }
        writer.raw("END:VTIMEZONE")
    }
}
