@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.guides

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.EncodeContext
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.Parameters
import io.github.calendrum.model.PartStat
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyCodec
import io.github.calendrum.model.PropertyDefinition
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.model.Rfc5545
import io.github.calendrum.model.Rfc7986
import io.github.calendrum.model.ValueType
import io.github.calendrum.model.VendorProperties
import io.github.calendrum.model.calendars
import io.github.calendrum.model.decode
import io.github.calendrum.model.decodeAny
import io.github.calendrum.model.get
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ModelGuide {
    private val extended = icsLines(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Example//Guides//EN",
        "NAME:Lessons",
        "COLOR:teal",
        "REFRESH-INTERVAL;VALUE=DURATION:P1D",
        "BEGIN:VEVENT",
        "UID:1@example.com",
        "DTSTART:20260901T090000Z",
        "SUMMARY:Algebra",
        "X-ACME-COLOR:#ff8800",
        "X-MICROSOFT-CDO-BUSYSTATUS:OOF",
        "CONFERENCE;VALUE=URI;FEATURE=VIDEO;LABEL=Call:https://meet.example.com/1",
        "BEGIN:VLOCATION",
        "UID:l",
        "NAME:Hall",
        "END:VLOCATION",
        "BEGIN:VALARM",
        "UID:a",
        "ACTION:DISPLAY",
        "TRIGGER:-PT5M",
        "ACKNOWLEDGED:20260901T085500Z",
        "END:VALARM",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    @Test
    fun readTheModel() {
        val text = calendarText
        val printed = ArrayList<String>()
        // region model-read
        val calendar = ICal.parse(text).calendars.single()
        for (event in calendar.events) {
            when (val start = event.start ?: continue) { // ICalDateTime: four forms, closed by RFC 5545
                is ICalDateTime.Date -> printed += "all day ${start.date}"

                is ICalDateTime.Utc -> printed += "at ${start.instant}"

                // an instant needs a resolver
                is ICalDateTime.Zoned -> printed += "at ${start.dateTime} ${start.tzid}"

                is ICalDateTime.Floating -> printed += "at ${start.dateTime} local time"
            }
            event.attendees.filter { it.partStat == PartStat.NeedsAction }.forEach { printed += it.address }
            for (alarm in event.alarms) printed += "${alarm.action} ${alarm.trigger}"
        }
        // endregion
        assertEquals(
            listOf(
                "at 2026-10-01T10:00 Europe/Berlin",
                "mailto:jane@example.com",
                "DISPLAY Trigger.Relative(-PT15M, START)",
                "all day 2026-10-02",
            ),
            printed,
        )
    }

    @Test
    fun decodeTellsWhy() {
        val text = icsLines(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:1",
            "DTSTART:2026-13-45",
            "DTEND:20261002T100000",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        val event = ICal.parse(text).calendars.single().events.single()
        // region model-decode
        val start: ICalDateTime? = event.start // null: missing or broken
        val why = when (val decoded = event.component.decode(Properties.DTSTART)) {
            is Decoded.Value -> "read, ${decoded.problems.size} warnings"

            // lenient reading reports ICAL-V…
            is Decoded.Malformed -> decoded.problem.code.id

            // "ICAL-V002", with its position
            Decoded.Missing -> "no DTSTART"
        }
        // endregion
        assertEquals(null, start)
        assertEquals("ICAL-V002", why)
        assertIs<Decoded.Value<*>>(event.component.decode(Properties.DTEND))
    }

    private object HexColor : PropertyCodec<Int> {
        override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Int> {
            val value = property.value.removePrefix("#").toIntOrNull(16)
                ?: return Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, 0))
            return Decoded.Value(value, emptyList())
        }

        override fun encode(value: Int, context: EncodeContext): ICalProperty =
            ICalProperty.of(context.name, "#" + value.toString(16))
    }

    @Test
    fun keys() {
        val event = ICal.parse(extended).calendars.single().events.single()
        val attendeeEvent = ICal.parse(calendarText).calendars.single().events.first()
        // region model-keys
        // a typed parameter key
        val cn: String? = attendeeEvent.component.properties("ATTENDEE").first()[Parameters.CN]
        val color = PropertyKey("X-ACME-COLOR", HexColor) // your own key needs no registration; HexColor is yours
        val rgb: Int? = event.component[color] // 0xFF8800
        // endregion
        assertEquals("Jane", cn)
        assertEquals(0xFF8800, rgb)
    }

    @Test
    fun openEnumerations() {
        // region model-open-enum
        // PartStat.Accepted: tokens are matched without regard to case
        val known = PartStat.of("accepted")
        val vendor = PartStat.of("X-MAYBE-LATER") // unknown tokens are kept, not rejected
        val written = vendor.value // "X-MAYBE-LATER"
        // endregion
        assertEquals(PartStat.Accepted, known)
        assertEquals("X-MAYBE-LATER", written)
    }

    @Test
    fun eagerDecoding() {
        val text = icsLines(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "UID:1",
            "DTSTART;VALUE=DATE-TIME:20261002",
            "END:VEVENT",
            "END:VCALENDAR",
        )
        // region model-eager
        val result = ICal.parser { decodeEagerly = true }.parse(text) // Strict mode does the same and throws
        val valueProblems = result.problems.filter { it.code.id.startsWith("ICAL-V") } // [ICAL-V008]
        // endregion
        assertEquals(listOf("ICAL-V008"), valueProblems.map { it.code.id })
    }

    @Test
    fun extensions() {
        val printed = ArrayList<String>()
        // region model-extensions
        val calendar = ICal.parse(extended).calendars.single()
        // NAME, COLOR, REFRESH-INTERVAL
        printed += "${calendar.names} ${calendar.color} ${calendar.refreshInterval}"
        for (event in calendar.events) {
            event.conferences.forEach { printed += "${it.label}: ${it.uri} ${it.features}" } // CONFERENCE (RFC 7986)
            event.locations.forEach { printed += "${it.name}" } // VLOCATION (RFC 9073)
            event.alarms.filter { it.acknowledged != null }.forEach { printed += "dismissed ${it.uid}" } // RFC 9074
        }
        // endregion
        assertEquals(
            listOf("[Lessons] teal P1D", "Call: https://meet.example.com/1 [VIDEO]", "Hall", "dismissed a"),
            printed,
        )
    }

    @Test
    fun registry() {
        val color = PropertyKey("X-ACME-COLOR", HexColor)
        // region model-registry
        val registry = ICalRegistry.builder()
            .install(Rfc5545).install(Rfc7986).install(VendorProperties) // ICalRegistry.Default has all five RFCs
            .install {
                it.property(PropertyDefinition(color, ValueType.Text, emptySet(), emptySet(), emptyMap(), "Acme"))
            }
            .build()
        val checked = ICal.parser {
            this.registry = registry
            decodeEagerly = true
        }.parse(extended)
        val busy = checked.components.single().components.first().decodeAny("X-MICROSOFT-CDO-BUSYSTATUS", registry)
        // endregion
        assertTrue(busy is Decoded.Value)
    }
}
