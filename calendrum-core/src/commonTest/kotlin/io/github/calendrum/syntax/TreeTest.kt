package io.github.calendrum.syntax

import io.github.calendrum.ICal
import io.github.calendrum.ICalParseException
import io.github.calendrum.parser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

// Hand-built trees against parsed ones, equality by content, strict names, a chain of 100 000 components for `equals`,
// `hashCode` and `toString` (no stack overflow on any platform), and a broken input with a secret value that must not
// appear in any message or `toString`.
class TreeTest {
    private val parsed = ICal.parse(
        crlf(
            "BEGIN:VEVENT",
            "uid:42@example.com",
            "ATTENDEE;cn=Jane;ROLE=CHAIR:mailto:jane@example.com",
            "x-tag:blue",
            "BEGIN:VALARM",
            "ACTION:DISPLAY",
            "END:VALARM",
            "END:VEVENT",
        ),
    ).components.single()

    @Test
    fun buildersMatchParsedTrees() {
        val built = ICalComponent.builder("vevent")
            .property(ICalProperty.of("UID", "42@example.com"))
            .property(
                ICalProperty.builder("Attendee")
                    .parameter("CN", "Jane")
                    .parameter(ICalParameter.of("role", "CHAIR"))
                    .value("mailto:jane@example.com")
                    .build(),
            )
            .property("X-TAG", "blue")
            .component(ICalComponent.builder("VALARM").property("ACTION", "DISPLAY").build())
            .build()
        assertEquals(parsed, built)
        assertEquals(built, parsed)
        assertEquals(parsed.hashCode(), built.hashCode())
        assertEquals("VEVENT", built.name)
        assertNull(built.span)
        assertNull(built.properties[0].span)
        assertSame(parsed.name, built.name)
        assertEquals("X-TAG", built.properties[2].name)
        assertEquals(listOf("CN", "ROLE"), built.property("attendee")!!.parameters.map { it.name })
    }

    @Test
    fun dslBuildsTheSameTree() {
        val built = ICalComponent("VEVENT") {
            property("UID", "42@example.com")
            property(
                ICalProperty.builder("ATTENDEE").parameter("CN", "Jane").parameter("ROLE", "CHAIR")
                    .value("mailto:jane@example.com").build(),
            )
            property("X-TAG", "blue")
            component("VALARM") {
                property("ACTION", "DISPLAY")
            }
        }
        assertEquals(parsed, built)
    }

    @Test
    fun contentDecidesEquality() {
        val base = ICalComponent("X") { property("A", "1") }
        assertNotEquals(base, ICalComponent("X") { property("A", "2") })
        assertNotEquals(base, ICalComponent("Y") { property("A", "1") })
        assertNotEquals(base, ICalComponent("X") { property("B", "1") })
        assertNotEquals(base, ICalComponent("X") {})
        assertNotEquals(
            ICalComponent("X") {
                property("A", "1")
                property("B", "2")
            },
            ICalComponent("X") {
                property("B", "2")
                property("A", "1")
            },
        )
        assertNotEquals(
            ICalComponent("X") {
                component("A") {}
                component("B") {}
            },
            ICalComponent("X") {
                component("B") {}
                component("A") {}
            },
        )
        assertNotEquals(
            ICalComponent("X") { component("A") { component("B") {} } },
            ICalComponent("X") {
                component("A") {}
                component("B") {}
            },
        )
        val one = ICalProperty.builder("A").parameter("P", "1", "2").value("v").build()
        assertEquals(one, ICalProperty.builder("a").parameter("p", "1", "2").value("v").build())
        assertNotEquals(one, ICalProperty.builder("A").parameter("P", "2", "1").value("v").build())
        assertNotEquals(one, ICalProperty.builder("A").parameter("P", "1").parameter("P", "2").value("v").build())
        assertNotEquals(one, ICalProperty.builder("A").parameter("Q", "1", "2").value("v").build())
        assertEquals(ICalParameter.of("X-A", "1"), ICalParameter.of("x-a", "1"))
        assertNotEquals(ICalParameter.of("X-A", "1"), ICalParameter.of("X-A", "1", "2"))
        assertFalse(base.equals("X"))
        assertFalse(base.properties[0].equals(null))
    }

    @Test
    fun propertyValuesAndParametersFromBuilders() {
        val property = ICalProperty.builder("DESCRIPTION").build()
        assertEquals("", property.value)
        assertEquals(emptyList(), property.parameters)
        assertNull(property.parameter("CN"))
        val parameter = ICalParameter.of("MEMBER", "a", "b")
        assertEquals("a", parameter.value)
        assertEquals(listOf("a", "b"), parameter.values)
        val values = arrayOf("x")
        val copied = ICalParameter.of("CN", *values)
        values[0] = "changed"
        assertEquals("x", copied.value)
    }

    @Test
    fun namesAreCheckedStrictly() {
        for (bad in listOf("", "X TAG", "SUMMARY:", "ÄÖ", "X_TAG", "A;B")) {
            assertFailsWith<IllegalArgumentException>(bad) { ICalComponent.builder(bad) }
            assertFailsWith<IllegalArgumentException>(bad) { ICalProperty.of(bad, "v") }
            assertFailsWith<IllegalArgumentException>(bad) { ICalProperty.builder(bad) }
            assertFailsWith<IllegalArgumentException>(bad) { ICalParameter.of(bad, "v") }
        }
        assertFailsWith<IllegalArgumentException> { ICalProperty.of("begin", "VEVENT") }
        assertFailsWith<IllegalArgumentException> { ICalProperty.of("END", "VEVENT") }
        assertFailsWith<IllegalArgumentException> { ICalParameter.of("CN") }
        assertFailsWith<IllegalArgumentException> { ICalComponent("VEVENT") { property("bad name", "v") } }
        assertEquals("X-VENDOR-1", ICalComponent.builder("x-vendor-1").build().name)
    }

    @Test
    fun deepTreesDoNotOverflowTheStack() {
        fun chain(depth: Int, leaf: String): ICalComponent {
            var component = ICalComponent("LEAF") { property("X-VALUE", leaf) }
            repeat(depth) { component = ICalComponent.builder("NODE").component(component).build() }
            return component
        }
        val first = chain(DEEP, "a")
        val second = chain(DEEP, "a")
        val different = chain(DEEP, "b")
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertNotEquals(first, different)
        assertEquals("ICalComponent(name=NODE, properties=0, components=1)", first.toString())
        assertTrue(first.components.toString().length < 100)
    }

    @Test
    fun wideTreesCompareIteratively() {
        val builder = ICalComponent.builder("VCALENDAR")
        repeat(10_000) { index -> builder.component("VEVENT") { property("UID", "$index") } }
        val wide = builder.build()
        val parsed = ICal.parse(
            crlf("BEGIN:VCALENDAR") +
                (0 until 10_000).joinToString("") { crlf("BEGIN:VEVENT", "UID:$it", "END:VEVENT") } +
                crlf("END:VCALENDAR"),
        ).components.single()
        assertEquals(wide, parsed)
        assertEquals(wide.hashCode(), parsed.hashCode())
    }

    @Test
    fun nothingPrintsValues() {
        val secret = "SECRET-VALUE-4242"
        val text = crlf(
            "BEGIN:VCALENDAR",
            "BEGIN:VEVENT",
            "SUMMARY;X-NOTE=$secret:$secret",
            "ATTENDEE;CN=\"$secret:mailto:$secret",
            "X-$secret",
            "DESCRIPTION;NOEQ:$secret",
            "BEGIN:VALARM",
            "END:VEVENT",
            "END:$secret",
            "BEGIN:VEVENT",
            "UID:$secret",
        ).replace("BEGIN:VALARM\r\n", "BEGIN:VALARM\n")
        val result = ICal.parse(text)
        assertTrue(result.problems.size >= 5, "problems: ${result.problems}")
        val printed = ArrayList<String>()
        printed += result.toString()
        for (problem in result.problems) {
            printed += problem.toString()
            printed += problem.message
            printed += problem.span.toString()
        }
        val stack = ArrayDeque(result.components)
        while (stack.isNotEmpty()) {
            val component = stack.removeLast()
            printed += component.toString()
            printed += component.properties.toString()
            printed += component.components.toString()
            for (property in component.properties) {
                printed += property.toString()
                printed += property.parameters.toString()
            }
            stack.addAll(component.components)
        }
        val strict = ICal.parser { mode = ParseMode.Strict }
        val error = assertFailsWith<ICalParseException> { strict.parse(text) }
        printed += error.toString()
        printed += error.message.orEmpty()
        printed += ICalProperty.of("X-A", secret).toString()
        printed += ICalParameter.of("X-A", secret).toString()
        for (line in printed) assertFalse(secret in line, line)
        val summary = result.components.single().components[0].property("SUMMARY")!!
        assertEquals(secret, summary.value)
        assertEquals(secret, summary.parameter("X-NOTE")!!.value)
    }

    private companion object {
        const val DEEP = 100_000
    }
}
