@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.ProblemCode
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.ParseMode
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.value.ICalDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RegistryTest {
    private class Rgb(val red: Int, val green: Int, val blue: Int) {
        override fun equals(other: Any?): Boolean =
            other is Rgb && red == other.red && green == other.green && blue == other.blue

        override fun hashCode(): Int = (red * 31 + green) * 31 + blue
    }

    private class RgbCodec : PropertyCodec<Rgb> {
        override fun decode(property: ICalProperty, context: DecodeContext): Decoded<Rgb> {
            val value = property.value
            if (value.length != 7 || value[0] != '#') {
                return Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, 0))
            }
            val number = value.substring(1).toIntOrNull(16)
                ?: return Decoded.Malformed(context.problem(property, ProblemCode.InvalidValue, 1))
            return Decoded.Value(Rgb(number shr 16, (number shr 8) and 0xFF, number and 0xFF), emptyList())
        }

        override fun encode(value: Rgb, context: EncodeContext): ICalProperty =
            ICalProperty.of(context.name, "#" + hex(value.red) + hex(value.green) + hex(value.blue))

        private fun hex(part: Int): String = part.toString(16).padStart(2, '0')
    }

    private val acmeColor = PropertyKey("X-ACME-COLOR", RgbCodec())

    private val acme = ICalExtension { registry ->
        registry.property(
            PropertyDefinition(
                acmeColor,
                ValueType.Text,
                emptySet(),
                emptySet(),
                mapOf("VEVENT" to Cardinality.Optional),
                "Acme",
            ),
        )
    }

    private val input = "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\nX-ACME-COLOR:#zz0000\r\nCOLOR:teal\r\n" +
        "ACKNOWLEDGED:20090604T084500\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"

    @Test
    fun theDefaultRegistryHasTheFiveRfcs() {
        val registry = ICalRegistry.Default
        assertEquals(listOf<ICalExtension>(Rfc5545, Rfc7986, Rfc9073, Rfc9074, Rfc9253), registry.extensions)
        assertSame(registry, ICalRegistry.Default)
        assertSame(Properties.COLOR, registry.property("color")?.key)
        assertNull(registry.property("X-ACME-COLOR"))
        assertNull(registry.property(VendorProperties.MICROSOFT_CDO_BUSYSTATUS.name))
        val description = registry.property("DESCRIPTION")!!
        assertEquals(Cardinality.Many, description.cardinality["VCALENDAR"])
        assertEquals(Cardinality.One, description.cardinality["VALARM"])
        assertEquals(Cardinality.Optional, description.cardinality["VLOCATION"])
        assertEquals("RFC 5545 §3.8.1.5; RFC 7986 §5.2; RFC 9073 §7", description.source)
        val related = registry.property("RELATED-TO")!!
        assertEquals(setOf(ValueType.Text, ValueType.Uri, ValueType.Uid), related.allowedTypes)
        assertEquals(setOf("RELTYPE", "VALUE", "GAP"), related.parameters)
        assertEquals(ValueType.Text, related.defaultType)
        assertEquals(Cardinality.Many, related.cardinality["VALARM"])
        assertTrue(registry.component("vevent")!!.children.containsAll(listOf("VALARM", "PARTICIPANT", "VLOCATION")))
        assertEquals(setOf("VLOCATION"), registry.component("VALARM")!!.children)
        assertSame(Parameters.LINKREL, registry.parameter("LINKREL")?.key)
        assertEquals(
            "RFC 5545 §3.2.15; RFC 9074 §7.1; RFC 9253 §4; RFC 9253 §5",
            registry.parameter("RELTYPE")?.source,
        )
        assertNull(registry.parameter("X-NOPE"))
        assertNull(registry.component("VAVAILABILITY"))
        assertEquals("ICalRegistry(extensions=5)", registry.toString())
    }

    @Test
    fun statusHasOneKeyPerComponent() {
        val registry = ICalRegistry.Default
        assertSame(Properties.EVENT_STATUS, registry.property("STATUS")?.key)
        val calendar = ICal.parse(
            "BEGIN:VCALENDAR\r\nBEGIN:VTODO\r\nSTATUS:IN-PROCESS\r\nEND:VTODO\r\nBEGIN:VJOURNAL\r\nSTATUS:FINAL\r\n" +
                "END:VJOURNAL\r\nEND:VCALENDAR\r\n",
        ).components.single()
        assertEquals(TodoStatus.InProcess, calendar.components[0].decodeAny("STATUS").valueOrNull())
        assertEquals(JournalStatus.Final, calendar.components[1].decodeAny("STATUS").valueOrNull())
    }

    @Test
    fun theBuilderInstallsEachExtensionOnceInOrder() {
        var calls = 0
        val counting = ICalExtension { calls += 1 }
        val registry = ICalRegistry.builder().install(counting).install(Rfc7986).install(counting).build()
        assertEquals(1, calls)
        assertEquals(listOf(counting, Rfc7986), registry.extensions)
        assertNull(registry.property("DTSTART"))
        assertEquals(mapOf("VCALENDAR" to Cardinality.Many), registry.property("DESCRIPTION")!!.cardinality)
        assertEquals("ICalRegistry.Builder(extensions=0)", ICalRegistry.builder().toString())
    }

    @Test
    fun aLaterDefinitionReplacesAnOverlappingOne() {
        val color = PropertyKey("COLOR", RgbCodec())
        val registry = ICalRegistry.builder().install(Rfc7986).property(
            PropertyDefinition(
                color,
                ValueType.Text,
                emptySet(),
                emptySet(),
                mapOf("vevent" to Cardinality.One),
                "Acme",
            ),
        ).build()
        assertSame(color, registry.property("COLOR")?.key)
        assertEquals(mapOf("VEVENT" to Cardinality.One), registry.property("COLOR")!!.cardinality)
        val anywhere = ICalRegistry.builder().install(Rfc5545).property(
            PropertyDefinition(
                PropertyKey("STATUS", TextCodec()),
                ValueType.Text,
                emptySet(),
                emptySet(),
                emptyMap(),
                "x",
            ),
        ).build()
        assertEquals("x", anywhere.property("STATUS")!!.source)
        assertEquals(setOf(ValueType.Text), anywhere.property("STATUS")!!.allowedTypes)
    }

    @Test
    fun eagerDecodingKnowsTheUserExtension() {
        val registry = ICalRegistry.builder().install(Rfc5545).install(Rfc7986).install(Rfc9074).install(acme).build()
        val result = ICal.parser {
            this.registry = registry
            decodeEagerly = true
        }.parse(input)
        assertEquals(listOf("ICAL-V001", "ICAL-V027"), result.problems.map { it.code.id })
        val plain = ICal.parser { decodeEagerly = true }.parse(input)
        assertEquals(listOf("ICAL-V027"), plain.problems.map { it.code.id })
        val strict = ICal.parser {
            this.registry = registry
            mode = ParseMode.Strict
        }
        assertFailsWith<ICalParseException> { strict.parse(input) }
        val options = ParseOptions.builder().registry(registry).build()
        assertSame(registry, options.registry)
        assertSame(ICalRegistry.Default, ParseOptions.Default.registry)
        assertTrue(options.toString().endsWith("registry=ICalRegistry(extensions=4))"))
    }

    @Test
    fun decodeAnyUsesTheRegistry() {
        val event = ICal.parse(input).components.single().components.single()
        assertEquals("teal", event.decodeAny("color").valueOrNull())
        assertLenient(event.decodeAny("ACKNOWLEDGED"), "ICAL-V027@6:14")
        assertEquals("#zz0000", event.decodeAny("X-ACME-COLOR").valueOrNull())
        assertIs<Decoded.Missing>(event.decodeAny("X-NONE"))
        val registry = ICalRegistry.builder().install(acme).build()
        assertIs<Decoded.Malformed>(event.decodeAny("X-ACME-COLOR", registry))
        val good = ICal.parse("BEGIN:VEVENT\r\nX-ACME-COLOR:#ff8000\r\nEND:VEVENT\r\n").components.single()
        assertEquals(Rgb(255, 128, 0), good.decodeAny("X-ACME-COLOR", registry).valueOrNull())
        assertEquals(Rgb(255, 128, 0), good[acmeColor])
        assertEquals("teal", event.decodeAny("COLOR", registry).valueOrNull())
    }

    @Test
    fun vendorPropertiesAreAnExtensionOutsideTheDefault() {
        val registry = ICalRegistry.builder().install(Rfc5545).install(VendorProperties).build()
        val text = "BEGIN:VCALENDAR\r\nX-WR-CALNAME:Work\r\nX-PUBLISHED-TTL:PT1H\r\nBEGIN:VEVENT\r\n" +
            "X-MICROSOFT-CDO-BUSYSTATUS:OOF\r\nX-MICROSOFT-CDO-ALLDAYEVENT:TRUE\r\nX-MICROSOFT-CDO-IMPORTANCE:2\r\n" +
            "X-ALT-DESC;FMTTYPE=text/html:<p>Hi</p>\r\nX-MOZ-LASTACK:20260901T100000Z\r\n" +
            "X-MICROSOFT-CDO-OWNERAPPTID:5876422858\r\n" +
            "X-MICROSOFT-DISALLOW-COUNTER:maybe\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n"
        val result = ICal.parser {
            this.registry = registry
            decodeEagerly = true
        }.parse(text)
        assertEquals(listOf("ICAL-V001"), result.problems.map { it.code.id })
        val calendar = result.components.single()
        val event = calendar.components.single()
        assertEquals("Work", calendar[VendorProperties.WR_CALNAME])
        assertEquals(ICalDuration.ofHours(1), calendar[VendorProperties.PUBLISHED_TTL])
        assertEquals(BusyStatus.Oof, event[VendorProperties.MICROSOFT_CDO_BUSYSTATUS])
        assertEquals(true, event[VendorProperties.MICROSOFT_CDO_ALLDAYEVENT])
        assertEquals(2, event[VendorProperties.MICROSOFT_CDO_IMPORTANCE])
        assertEquals("text/html", event[VendorProperties.ALT_DESC]?.formatType)
        assertEquals("5876422858", event[VendorProperties.MICROSOFT_CDO_OWNERAPPTID])
        assertNull(event[VendorProperties.MICROSOFT_DISALLOW_COUNTER])
        assertEquals(BusyStatus.WorkingElsewhere, BusyStatus.of("workingelsewhere"))
        assertEquals(
            "X-MICROSOFT-CDO-ALLDAYEVENT:FALSE",
            lineOf(encoded(VendorProperties.MICROSOFT_CDO_ALLDAYEVENT, false)),
        )
        assertEquals(20, vendorKeys().size)
        for (key in vendorKeys()) assertSame(key, registry.property(key.name)?.key, key.name)
        assertEquals("VendorProperties", VendorProperties.toString())
    }

    private fun vendorKeys(): List<PropertyKey<*>> = listOf(
        VendorProperties.MICROSOFT_CDO_BUSYSTATUS, VendorProperties.MICROSOFT_CDO_INTENDEDSTATUS,
        VendorProperties.MICROSOFT_CDO_ALLDAYEVENT, VendorProperties.MICROSOFT_CDO_IMPORTANCE,
        VendorProperties.MICROSOFT_CDO_OWNERAPPTID, VendorProperties.MICROSOFT_DISALLOW_COUNTER,
        VendorProperties.ALT_DESC, VendorProperties.WR_CALNAME, VendorProperties.WR_CALDESC,
        VendorProperties.WR_TIMEZONE, VendorProperties.PUBLISHED_TTL, VendorProperties.APPLE_CALENDAR_COLOR,
        VendorProperties.APPLE_TRAVEL_ADVISORY_BEHAVIOR, VendorProperties.APPLE_STRUCTURED_LOCATION,
        VendorProperties.APPLE_DEFAULT_ALARM, VendorProperties.GOOGLE_CONFERENCE, VendorProperties.MOZ_GENERATION,
        VendorProperties.MOZ_LASTACK, VendorProperties.LIC_LOCATION, VendorProperties.YANDEX_VISIBILITY,
    )
}
