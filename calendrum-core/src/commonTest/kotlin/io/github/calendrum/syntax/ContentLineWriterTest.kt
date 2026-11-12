package io.github.calendrum.syntax

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContentLineWriterTest {
    @Test
    fun writesComponentsAndPropertiesWithCrlf() {
        val written = write {
            begin("VCALENDAR")
            line("VERSION", "2.0")
            line("DTSTART", "19980312T083000", "TZID" to listOf("America/New_York"))
            line("X-EMPTY", "")
            end("VCALENDAR")
        }
        assertEquals(
            "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nDTSTART;TZID=America/New_York:19980312T083000\r\nX-EMPTY:\r\n" +
                "END:VCALENDAR\r\n",
            written.text,
        )
    }

    @Test
    fun namesAreWrittenInUpperCase() {
        val written = write {
            begin("vEvent")
            line("x-my-prop2", "Value stays as is", "cn" to listOf("a"), "x-Param" to listOf("b"))
            end("vevent")
        }
        assertEquals("BEGIN:VEVENT\r\nX-MY-PROP2;CN=a;X-PARAM=b:Value stays as is\r\nEND:VEVENT\r\n", written.text)
    }

    @Test
    fun foldPositionsOfAsciiLines() {
        val expected = mapOf(
            74 to listOf(74),
            75 to listOf(75),
            76 to listOf(75, 2),
            149 to listOf(75, 75),
            150 to listOf(75, 75, 2),
            151 to listOf(75, 75, 3),
        )
        for ((length, lengths) in expected) {
            val value = digits(length - 2)
            val logical = "X:$value"
            val written = write { line("X", value) }
            assertEquals(lengths, written.lengths, "logical line of $length octets")
            val text = StringBuilder()
            text.append(logical, 0, minOf(75, logical.length))
            var start = 75
            while (start < logical.length) {
                text.append("\r\n ").append(logical, start, minOf(start + 74, logical.length))
                start += 74
            }
            text.append("\r\n")
            assertEquals(text.toString(), written.text, "logical line of $length octets")
        }
    }

    @Test
    fun foldsBeforeMultiByteCharactersThatDoNotFit() {
        assertEquals("X:" + "a".repeat(72) + "\r\n ж\r\n", write { line("X", "a".repeat(72) + "ж") }.text)
        assertEquals(listOf(75), write { line("X", "a".repeat(71) + "ж") }.lengths)
        assertEquals(listOf(73, 4), write { line("X", "a".repeat(71) + "中") }.lengths)
        assertEquals(listOf(75), write { line("X", "a".repeat(70) + "中") }.lengths)
        assertEquals(listOf(72, 5), write { line("X", "a".repeat(70) + "😀") }.lengths)
        assertEquals(listOf(75), write { line("X", "a".repeat(69) + "😀") }.lengths)
        assertEquals(listOf(75, 73, 5), write { line("X", "a".repeat(73) + "😀".repeat(19)) }.lengths)
    }

    @Test
    fun everyFoldBoundaryKeepsCodePointsWhole() {
        for (piece in listOf("ж", "中", "😀", "aж中😀", "é€𝄞")) {
            for (prefix in 60..80) {
                val value = "a".repeat(prefix) + piece.repeat(60)
                val written = write { line("X", value) }
                assertEquals("X:$value\r\n", written.unfolded(), "piece $piece after $prefix octets")
                assertTrue(written.physicalLines.size > 2)
            }
        }
    }

    @Test
    fun foldsInsideNamesAndParameters() {
        val name = "X-" + "N".repeat(98)
        val written = write {
            line(name, "v", "X-" + "P".repeat(80) to listOf("q".repeat(100), "中".repeat(40)))
        }
        assertTrue(written.lengths.size > 4)
        val line = lex(written.bytes).single()
        assertEquals(name, line.name)
        assertEquals(listOf(Param("X-" + "P".repeat(80), listOf("q".repeat(100), "中".repeat(40)))), line.parameters)
        assertEquals("v", line.value)
    }

    @Test
    fun parameterValuesAreQuotedOnlyWhenNeeded() {
        val written = write {
            line(
                "X",
                "v",
                "A" to listOf("plain value"),
                "B" to listOf("a:b"),
                "C" to listOf("a;b"),
                "D" to listOf("a,b"),
                "E" to listOf("x", "y,z", ""),
                "F" to listOf("tab\there", "é中😀", "=/'"),
            )
        }
        assertEquals(
            "X;A=plain value;B=\"a:b\";C=\"a;b\";D=\"a,b\";E=x,\"y,z\",;F=tab\there,é中😀,=/':v\r\n",
            written.unfolded(),
        )
        val line = lex(written.bytes).single()
        assertEquals(
            listOf(
                Param("A", listOf("plain value")),
                Param("B", listOf("a:b")),
                Param("C", listOf("a;b")),
                Param("D", listOf("a,b")),
                Param("E", listOf("x", "y,z", "")),
                Param("F", listOf("tab\there", "é中😀", "=/'")),
            ),
            line.parameters,
        )
        val quoted = listOf(
            listOf(false),
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(false, true, false),
            listOf(false, false, false),
        )
        assertEquals(quoted, line.quoted)
    }

    @Test
    fun rfc6868EncodesCaretQuoteAndLineBreaks() {
        val written = write {
            line(
                "X",
                "^n^'",
                "A" to listOf("a^b"),
                "B" to listOf("say \"hi\""),
                "C" to listOf("1\n2\r\n3\r4\r"),
                "D" to listOf("^n", "^^"),
                "E" to listOf("\"a\", b"),
            )
        }
        assertEquals(
            "X;A=a^^b;B=say ^'hi^';C=1^n2^n3^n4^n;D=^^n,^^^^;E=\"^'a^', b\":^n^'\r\n",
            written.unfolded(),
        )
        val line = lex(written.bytes).single()
        assertEquals(
            listOf(
                Param("A", listOf("a^b")),
                Param("B", listOf("say \"hi\"")),
                Param("C", listOf("1\n2\n3\n4\n")),
                Param("D", listOf("^n", "^^")),
                Param("E", listOf("\"a\", b")),
            ),
            line.parameters,
        )
        assertEquals("^n^'", line.value)
    }

    @Test
    fun invalidNamesThrowWithoutTheName() {
        val names = listOf("", "SECRET NAME", "SECRET_NAME", "SECRETЖ", "SECRET:X", "SECRET;X", "SECRET=X", "SECRET\"X")
        for (name in names) {
            assertContractViolation(name) { property(name) }
            assertContractViolation(name) {
                property("X")
                parameter(name)
            }
            assertContractViolation(name) {
                property("X")
                parameter("A")
                parameterValue("a")
                parameter(name)
            }
            assertContractViolation(name) { begin(name) }
            assertContractViolation(name) { end(name) }
        }
    }

    @Test
    fun controlCharactersInParameterValuesThrowWithoutTheValue() {
        val codes = (0x00..0x1F).filter { it != 0x09 && it != 0x0A && it != 0x0D } + 0x7F
        for (code in codes) {
            val value = "SECRET" + code.toChar() + "VALUE"
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue(value)
            }
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue("ok")
                parameterValue(value)
            }
        }
    }

    @Test
    fun controlCharactersInPropertyValuesThrowWithoutTheValue() {
        val codes = (0x00..0x1F).filter { it != 0x09 && it != 0x0A && it != 0x0D } + 0x7F
        for (code in codes) {
            val value = "SECRET" + code.toChar() + "VALUE"
            assertContractViolation(value) {
                property("X")
                value(value)
            }
            assertContractViolation(value) {
                property("X")
                value("x".repeat(200) + value)
            }
        }
        val written = write { line("X", "a\tb\u0085c") }
        assertEquals("X:a\tb\u0085c\r\n", written.text)
    }

    @Test
    fun lineBreaksInPropertyValuesThrowWithoutTheValue() {
        val values =
            listOf("SECRET\nVALUE", "SECRET\rVALUE", "SECRET\r\nVALUE", "SECRET\n", "x".repeat(500) + "\rSECRET")
        for (value in values) {
            assertContractViolation(value) {
                property("X")
                value(value)
            }
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue("a")
                value(value)
            }
        }
    }

    @Test
    fun unpairedSurrogatesThrowWithoutTheValue() {
        val high = Char(0xD83D)
        val low = Char(0xDE00)
        for (value in listOf("SECRET$high", "SECRET${low}X", "${high}SECRET", "SECRET$high$high$low")) {
            assertContractViolation(value) {
                property("X")
                value(value)
            }
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue(value)
            }
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue("a,$value")
            }
            assertContractViolation(value) {
                property("X")
                parameter("A")
                parameterValue("^$value")
            }
        }
    }

    @Test
    fun contractViolationDropsTheUnfinishedLine() {
        val writer = ByteLineWriter()
        writer.line("A", "1")
        val size = writer.size
        writer.property("B")
        writer.parameter("P")
        writer.parameterValue("x")
        assertFailsWith<IllegalArgumentException> { writer.value("x".repeat(5_000) + "\n") }
        assertEquals(size, writer.size)
        assertFailsWith<IllegalArgumentException> { writer.property("B C") }
        writer.property("B")
        assertFailsWith<IllegalArgumentException> { writer.parameter("") }
        writer.line("C", "3")
        assertEquals("A:1\r\nC:3\r\n", writer.toByteArray().decodeToString())

        val text = StringBuilder()
        val appending = AppendableLineWriter(text)
        appending.property("B")
        assertFailsWith<IllegalArgumentException> { appending.value("bad\n") }
        appending.line("C", "3")
        assertTrue(text.endsWith("C:3\r\n"))
    }

    @Test
    fun callsOutOfOrderThrowIllegalState() {
        assertFailsWith<IllegalStateException> { AppendableLineWriter(StringBuilder()).value("a") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().parameter("A") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().parameterValue("a") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().value("a") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().apply { property("X") }.parameterValue("a") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().apply { property("X") }.property("Y") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().apply { property("X") }.begin("Y") }
        assertFailsWith<IllegalStateException> { ByteLineWriter().apply { property("X") }.end("Y") }
        assertFailsWith<IllegalStateException> {
            ByteLineWriter().apply {
                property("X")
                parameter("A")
            }.value("v")
        }
        assertFailsWith<IllegalStateException> {
            ByteLineWriter().apply {
                property("X")
                parameter("A")
            }.parameter("B")
        }
    }

    @Test
    fun resetReusesTheBuffer() {
        val writer = ByteLineWriter()
        writer.line("A", "x".repeat(10_000))
        writer.property("B")
        writer.reset()
        assertEquals(0, writer.size)
        writer.line("B", "1")
        assertEquals("B:1\r\n", writer.toByteArray().decodeToString())

        val text = StringBuilder()
        val appending = AppendableLineWriter(text)
        appending.property("A")
        appending.reset()
        appending.line("B", "1")
        assertEquals("AB:1\r\n", text.toString())
    }

    @Test
    fun outputsStayEqualAcrossManyLines() {
        val written = write {
            repeat(200) { index ->
                begin("VEVENT")
                line("SUMMARY", "Событие №$index 🎉 " + "x".repeat(index), "LANGUAGE" to listOf("ru"))
                line("DESCRIPTION", "中文".repeat(index % 50))
                end("VEVENT")
            }
        }
        assertEquals(800, lex(written.bytes).lines.size)
        assertFalse(written.text.contains("\n\n"))
    }

    private fun assertContractViolation(secret: String, block: ContentLineWriter.() -> Unit) {
        for (writer in listOf<ContentLineWriter>(ByteLineWriter(), AppendableLineWriter(StringBuilder()))) {
            val error = assertFailsWith<IllegalArgumentException> { writer.block() }
            val message = error.message.orEmpty()
            assertTrue(message.isNotBlank())
            if (secret.isNotEmpty()) assertFalse(message.contains("SECRET"), "message leaks the value: $message")
            writer.line("NEXT", "ok")
        }
    }

    private fun digits(count: Int): String {
        val chars = CharArray(count) { '0' + it % 10 }
        return chars.concatToString()
    }
}
