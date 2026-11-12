package io.github.calendrum.tooling.ops

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class JsonOutTest {
    private fun roundTrip(text: String): String {
        val out = JsonOut()
        out.beginArray()
        out.value(text)
        out.endArray()
        assertEquals(
            out.toString().encodeToByteArray().size.toLong(),
            out.utf8Size,
            "the size of ${out.toString().length} chars",
        )
        return Json.parseToJsonElement(out.toString()).jsonArray[0].jsonPrimitive.content
    }

    @Test
    fun textComesBackAsItWas() {
        val texts = listOf(
            "",
            "plain",
            "\"quotes\" and \\ backslash",
            "line\r\nbreak\ttab\u000Cfeed\bback",
            "\u0000\u0001\u001F\u007F",
            "кириллица",
            "\u0080\u07FF\u0800\uFFFF",
            "😀 pair",
            "a😀",
            "😀",
        )
        for (text in texts) assertEquals(text, roundTrip(text))
    }

    @Test
    fun deleteAndTheControlsOfC1AreEscaped() {
        for (code in 0x7F..0x9F) {
            val out = JsonOut()
            out.beginArray()
            out.value("a" + code.toChar() + "b")
            out.endArray()
            val expected = "[\"a\\u00" + code.toString(16) + "b\"]"
            assertEquals(expected, out.toString(), "U+${code.toString(16)}")
            assertEquals(out.toString().encodeToByteArray().size.toLong(), out.utf8Size)
            assertEquals(
                "a" + code.toChar() + "b",
                Json.parseToJsonElement(out.toString()).jsonArray[0].jsonPrimitive.content,
            )
        }
        // The neighbours keep the short way: a printable ASCII character, and U+00A0, the first of the rest.
        val out = JsonOut()
        out.beginArray()
        out.value("~\u00A0\u00FF")
        out.endArray()
        assertEquals("[\"~\u00A0\u00FF\"]", out.toString())
    }

    @Test
    fun aSurrogateWithoutItsPairBecomesReplacement() {
        // Built at run time: the compiler of Kotlin/JS writes a lone surrogate in a constant as a question mark.
        val high = charArrayOf(Char(0xD83D)).concatToString()
        val low = charArrayOf(Char(0xDE00)).concatToString()
        assertEquals("a\uFFFDb", roundTrip("a" + high + "b"))
        assertEquals("a\uFFFDb", roundTrip("a" + low + "b"))
        assertEquals("\uFFFD", roundTrip(high))
        assertEquals("\uFFFD\uFFFD", roundTrip(low + high))
    }

    @Test
    fun separatorsAreInPlace() {
        val out = JsonOut()
        out.beginObject()
        out.field("a", 1)
        out.name("b")
        out.beginArray()
        out.value(true)
        out.value(2L)
        out.beginObject()
        out.endObject()
        out.beginArray()
        out.endArray()
        out.endArray()
        out.optional("c", null)
        out.optional("d", "x")
        out.name("e")
        out.raw("[1,2]")
        out.endObject()
        assertEquals("""{"a":1,"b":[true,2,{},[]],"d":"x","e":[1,2]}""", out.toString())
    }

    @Test
    fun momentsHaveTheFormOfInstant() {
        val moments = listOf(
            "1970-01-01T00:00:00Z",
            "2026-09-01T08:05:09Z",
            "2024-02-29T23:59:59Z",
            "2000-03-01T00:00:00Z",
            "1969-12-31T23:59:59Z",
            "1600-02-29T12:00:00Z",
            "0001-01-01T00:00:00Z",
            "0000-01-01T00:00:00Z",
            "9999-12-31T23:59:59Z",
        )
        for (moment in moments) {
            val out = JsonOut()
            out.value(Instant.parse(moment))
            assertEquals("\"$moment\"", out.toString())
        }
        for (day in 0L until 150_000L) {
            val instant = Instant.fromEpochSeconds(-60_000L * 86_400L + day * 86_400L + 3661L)
            val out = JsonOut()
            out.value(instant)
            assertEquals("\"$instant\"", out.toString())
        }
    }

    @Test
    fun otherMomentsFallBackToInstant() {
        for (moment in listOf("2026-09-01T08:05:09.5Z", "+10000-01-01T00:00:00Z", "-0001-01-01T00:00:00Z")) {
            val out = JsonOut()
            out.value(Instant.parse(moment))
            assertEquals("\"${Instant.parse(moment)}\"", out.toString())
        }
    }
}
