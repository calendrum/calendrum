package io.github.calendrum.syntax

import io.github.calendrum.ByteInput
import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.asByteInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StreamEquivalenceTest {
    private val calendar = crlf(
        "BEGIN:VCALENDAR",
        "PRODID:-//Example//Stream//EN",
        "VERSION:2.0",
        "X-WR-TIMEZONE:Europe/Moscow",
        "BEGIN:VTIMEZONE",
        "TZID:Europe/Moscow",
        "BEGIN:STANDARD",
        "DTSTART:19700101T000000",
        "TZOFFSETFROM:+0300",
        "TZOFFSETTO:+0300",
        "END:STANDARD",
        "END:VTIMEZONE",
        "BEGIN:VEVENT",
        "UID:1@example.com",
        "SUMMARY:Лекция по матанализу — ауд. 101",
        "DESCRIPTION:A long description that is folded across several physical lines so that the ",
        " fold crosses block boundaries in the stream; ещё немного кириллицы и эмодзи 😀 здесь",
        "ATTENDEE;CN=\"Doe, Jane\";ROLE=REQ-PARTICIPANT:mailto:jane@example.com",
        "BEGIN:VALARM",
        "ACTION:DISPLAY",
        "TRIGGER:-PT15M",
        "END:VALARM",
        "X-APPLE-AFTER-ALARM:1",
        "END:VEVENT",
        "BEGIN:VTODO",
        "UID:2@example.com",
        "END:VTODO",
        "END:VCALENDAR",
    )

    private val structure: List<String> = listOf(
        "",
        "\r\n",
        "BEGIN:VCALENDAR",
        "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nUID:1\r\n",
        "BEGIN:VCALENDAR\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nBEGIN:VALARM\r\nEND:VEVENT\r\nX:1\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nEND:VCALENDAR\r\nBEGIN:VCALENDAR\r\nX-WR-CALNAME:B\r\n" +
            "BEGIN:VEVENT\r\nUID:b\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n",
        "X-OUTSIDE:1\r\nBEGIN:VEVENT\r\nUID:top\r\nEND:VEVENT\r\nBEGIN:VCALENDAR\r\nBEGIN:VEVENT\r\nEND:VEVENT\r\n" +
            "PRODID:late\r\nEND:VCALENDAR\r\nBEGIN:X-THING\r\nEND:X-THING\r\n",
        "BEGIN:VCALENDAR\r\nBEGIN:VCALENDAR\r\nBEGIN:VTIMEZONE\r\nEND:VTIMEZONE\r\nEND:VCALENDAR\r\nEND:VCALENDAR\r\n",
        "begin:vcalendar\nbegin:vevent\nsummary:lower\nend:vevent\nend:vcalendar\n",
        "BEGIN:VCALENDAR\rBEGIN:VEVENT\rUID:cr\rEND:VEVENT\rEND:VCALENDAR\r",
        " continuation at start\r\nBEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\n\r\n\r\nX:\r\n \r\n \r\nY:a\r\n \r\n b\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nNO COLON HERE\r\nX;P=\"unclosed:v\r\nX;P=a\"b:v\r\nX;=v:1\r\nBEGIN;X=1:VEVENT\r\n" +
            "END:VEVENT\r\nBEGIN:\r\nEND:\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nX:é\r\n é\r\nY:\uD83D\r\n \uDE00\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n",
        "BEGIN:VCALENDAR\r\nX;A=\"a,b\";B=^n^^^':v\r\nEND:VCALENDAR",
    )

    private val invalidUtf8: List<ByteArray> = listOf(
        bytesOf(0x42, 0x3A, 0xC3, 0x0D, 0x0A, 0x20, 0xA9, 0x0D, 0x0A),
        bytesOf(0x42, 0x3A, 0xE2, 0x82, 0x0D, 0x0A, 0x20, 0x0D, 0x0A, 0x20, 0xAC, 0x0D, 0x0A),
        bytesOf(0x42, 0x3A, 0xFF, 0xC0, 0xAF, 0xED, 0xA0, 0x80, 0xF4, 0x90, 0x80, 0x80, 0x0D, 0x0A),
        bytesOf(0x42, 0x3A, 0xF0, 0x9F, 0x98),
        bytesOf(0x00, 0x42, 0x00, 0x3A),
        bytesOf(0x42, 0x3A, 0x00, 0x00, 0x0D, 0x0A),
        bytesOf(0x2B, 0x2F, 0x76, 0x38, 0x42),
        bytesOf(0xFF, 0xFE),
        bytesOf(0xFE, 0xFF, 0x00),
        bytesOf(0xFF, 0xFE, 0x00, 0x00, 0x42),
    )

    @Test
    fun wellFormedCalendar() {
        assertStreamEquivalent(calendar.encodeToByteArray())
        assertStreamEquivalent(calendar.encodeToByteArray(), ParseOptions.builder().retainSource(false).build())
    }

    @Test
    fun structureAndLexerEdgeCases() {
        for ((index, text) in structure.withIndex()) {
            assertStreamEquivalent(text.encodeToByteArray(), what = "structure #$index")
        }
    }

    @Test
    fun invalidUtf8AndSignatures() {
        for ((index, bytes) in invalidUtf8.withIndex()) {
            assertStreamEquivalent(bytes, what = "bytes #$index")
            assertStreamEquivalent(bytes, ParseOptions.builder().charset("UTF-16").build(), what = "bytes #$index")
        }
    }

    @Test
    fun strictModeFailsAtTheSameProblem() {
        val strict = ParseOptions.builder().mode(ParseMode.Strict).build()
        for ((index, text) in structure.withIndex()) {
            assertStreamEquivalent(text.encodeToByteArray(), strict, what = "strict #$index")
        }
    }

    @Test
    fun limits() {
        val options = listOf(
            limits(maxLineOctets = 20),
            limits(maxLines = 7),
            limits(maxProblems = 1),
            limits(maxDepth = 1),
            limits(maxDepth = 2),
            limits(maxComponents = 3),
        )
        val inputs = structure + calendar
        for ((index, limits) in options.withIndex()) {
            val parse = ParseOptions.builder().limits(limits).build()
            for ((inputIndex, text) in inputs.withIndex()) {
                assertStreamEquivalent(text.encodeToByteArray(), parse, what = "limits #$index, input #$inputIndex")
            }
        }
    }

    @Test
    fun charsets() {
        val texts = russianTexts.take(3) + westernTexts.take(3)
        for ((index, text) in texts.withIndex()) {
            val document = calendarWith(text)
            for (charset in TestCharsets.singleByte + TestCharsets.wide) {
                if (!TestCharsets.canEncode(document, charset) && charset !in TestCharsets.wide) continue
                val encoded = TestCharsets.encode(document, charset)
                val what = "text #$index in $charset"
                assertStreamEquivalent(encoded, what = what)
                assertStreamEquivalent(encoded, ParseOptions.builder().charset(charset).build(), what = "$what, hinted")
                assertStreamEquivalent(
                    encoded,
                    ParseOptions.builder().charset("KOI8-R").build(),
                    what = "$what, KOI8-R",
                )
                assertStreamEquivalent(encoded, ParseOptions.builder().charset("UTF-8").build(), what = "$what, UTF-8")
                if (charset in TestCharsets.wide) {
                    assertStreamEquivalent(TestCharsets.bom(charset) + encoded, what = "$what with a byte order mark")
                }
            }
        }
    }

    @Test
    fun wideSurrogatesAcrossBlocks() {
        val text = "BEGIN:VCALENDAR\r\nX:😀a\uD83D\r\nY:\uDE00𝄞\r\nEND:VCALENDAR\r\n"
        for (charset in TestCharsets.wide) {
            val encoded = TestCharsets.encode(text, charset)
            assertStreamEquivalent(TestCharsets.bom(charset) + encoded, what = charset)
            assertStreamEquivalent(encoded, what = "$charset without a byte order mark")
            assertStreamEquivalent(encoded.copyOf(encoded.size - 1), what = "$charset, odd tail")
        }
    }

    @Test
    fun detectionBeyondTheFirstWindow() {
        val prefix = "BEGIN:VCALENDAR\r\n" + "X-PADDING:" + "a".repeat(DETECTION_WINDOW + 100) + "\r\n"
        val cyrillic = "SUMMARY:Расписание занятий на осенний семестр\r\nEND:VCALENDAR\r\n"
        val encoded = (prefix + cyrillic).let { TestCharsets.encode(it, "windows-1251") }
        val blocks = listOf(1, 7, 16, DEFAULT_BLOCK_SIZE)
        assertStreamEquivalent(encoded, blockSizes = blocks, what = "late 1251")
        val hinted = ParseOptions.builder().charset("windows-1251").build()
        assertStreamEquivalent(encoded, hinted, blocks, "late 1251, hinted")
        val result = ICal.parser(hinted).parse(encoded)
        assertEquals("windows-1251", result.charset)
        assertEquals(1, result.problemCount(ProblemCode.TranscodedInput))
        assertTrue(result.problems.single().span.startOffset > DETECTION_WINDOW)
        val wrongHint = ParseOptions.builder().charset("windows-1252").build()
        assertStreamEquivalent(encoded, wrongHint, blocks, "late 1251, hinted 1252")
        val utf8 = (prefix + cyrillic).encodeToByteArray()
        assertStreamEquivalent(utf8, hinted, blocks, "late UTF-8, hinted 1251")
    }

    @Test
    fun longLinesAreDiscardedWithoutChangingTheResult() {
        val long = "x".repeat(20_000)
        val inputs = listOf(
            "BEGIN:VCALENDAR\r\nX:$long\r\nY:after\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nX:short\r\n $long\r\n more\r\nY:after\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nX:${long}é$long\r\né\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nBEGIN:A\r\nBEGIN:B\r\nX:$long\r\n $long\r\nEND:B\r\nEND:A\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nX:" + "ab\r".repeat(5_000) + "\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nX:" + "${"a".repeat(100)}\r\n ".repeat(100) + "end\r\nY:1\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\nX:$long\n $long\r ${long}\u00E9\rY:1\nEND:VCALENDAR",
            " $long\r\nBEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n",
            "BEGIN:VCALENDAR\r\nBEGIN:A\r\nBEGIN:B\r\nX-SKIPPED:$long\n $long\rBEGIN:C\r\n$long\r\nEND:C\r\n" +
                "END:B\r\nEND:A\r\nEND:VCALENDAR\r\n",
        )
        val blocks = listOf(1, 3, 16, 1024, DEFAULT_BLOCK_SIZE)
        val small = ParseOptions.builder().limits(limits(maxLineOctets = 100)).build()
        val shallow = ParseOptions.builder().limits(limits(maxDepth = 1)).build()
        val shallowShort = ParseOptions.builder().limits(limits(maxDepth = 1, maxLineOctets = 100)).build()
        for ((index, text) in inputs.withIndex()) {
            val bytes = text.encodeToByteArray()
            assertStreamEquivalent(bytes, small, blocks, "long #$index")
            assertStreamEquivalent(bytes, shallow, blocks, "long #$index, shallow")
            assertStreamEquivalent(bytes, shallowShort, blocks, "long #$index, shallow and short")
            val cp1251 = TestCharsets.encode(text.replace('é', 'ж'), "windows-1251")
            assertStreamEquivalent(cp1251, small, blocks, "long #$index in 1251")
        }
    }

    private val largeBody: String = buildString {
        append("BEGIN:VCALENDAR\r\n")
        repeat(120) {
            append("BEGIN:VEVENT\r\nUID:").append(it).append("\r\nSUMMARY:Занятие ").append(it)
            append(" \uD83D\uDE00 Réunion — ауд. 101\r\nDESCRIPTION:").append(russianTexts[it % 3].take(90))
            append("\r\nEND:VEVENT\r\n")
        }
        append("END:VCALENDAR\r\n")
    }

    @Test
    fun largeWideInputsAreTranscodedBlockByBlock() {
        val blocks = listOf(1, 3, 16, DEFAULT_BLOCK_SIZE)
        for (charset in TestCharsets.wide) {
            val encoded = TestCharsets.encode(largeBody, charset)
            assertTrue(encoded.size > 2 * WIDE_SAMPLE_BYTES)
            assertStreamEquivalent(TestCharsets.bom(charset) + encoded, blockSizes = blocks, what = "large $charset")
            assertStreamEquivalent(encoded.copyOf(encoded.size - 1), blockSizes = blocks, what = "large $charset, odd")
        }
    }

    @Test
    fun largeWideInputsWithoutByteOrderMark() {
        val blocks = listOf(2, 5, DEFAULT_BLOCK_SIZE)
        for (charset in TestCharsets.wide) {
            val encoded = TestCharsets.encode(largeBody, charset)
            assertStreamEquivalent(encoded, blockSizes = blocks, what = "large $charset without a byte order mark")
        }
    }

    @Test
    fun largeSingleByteInputsAreTranscodedBlockByBlock() {
        val cyrillic = largeBody.replace("\uD83D\uDE00", "").replace("é", "e")
        val cp1251 = TestCharsets.encode(cyrillic.repeat(20), "windows-1251")
        assertTrue(cp1251.size > 2 * DETECTION_WINDOW)
        val few = listOf(7, 4096, DEFAULT_BLOCK_SIZE)
        assertStreamEquivalent(cp1251, blockSizes = few, what = "large 1251")
        assertStreamEquivalent(cp1251, ParseOptions.builder().charset("windows-1251").build(), few, "large 1251, hint")
        val koi8Text = cyrillic.map {
            if (TestCharsets.canEncode(it.toString(), "KOI8-R")) it else '?'
        }.joinToString("")
        val koi8 = TestCharsets.encode(koi8Text, "KOI8-R")
        assertStreamEquivalent(koi8, ParseOptions.builder().charset("KOI8-R").build(), few, "large KOI8-R, hint")
    }

    @Test
    fun randomInputs() {
        val random = Random64(0x5EED)
        val alphabet = bytesOf(
            0x0D, 0x0A, 0x0D, 0x0A, 0x20, 0x09, 0x3A, 0x3A, 0x3B, 0x3B, 0x3D, 0x2C, 0x22, 0x22, 0x5E, 0x6E,
            0x41, 0x42, 0x2D, 0x31, 0x61, 0xC3, 0xA9, 0xE2, 0x82, 0xAC, 0xF0, 0x9F, 0x80, 0xBF, 0xC0, 0xED,
            0xFF, 0xFE, 0xEF, 0xBB, 0x00, 0xE0, 0xF2,
        )
        val fragments = listOf("BEGIN:VCALENDAR\r\n", "END:VCALENDAR\r\n", "BEGIN:VEVENT\r\n", "END:VEVENT\r\n")
            .map { it.encodeToByteArray() }
        val options = listOf(
            ParseOptions.Default,
            ParseOptions.builder().limits(limits(maxLineOctets = 16, maxLines = 40, maxProblems = 3)).build(),
            ParseOptions.builder().charset("windows-1251").build(),
            ParseOptions.builder().mode(ParseMode.Strict).build(),
        )
        repeat(REPEATS) { iteration ->
            val parts = ArrayList<ByteArray>()
            repeat(random.nextInt(12)) {
                parts += if (random.nextInt(3) == 0) {
                    fragments[random.nextInt(fragments.size)]
                } else {
                    ByteArray(random.nextInt(40)) {
                        if (random.nextInt(4) ==
                            0
                        ) {
                            random.nextInt(256).toByte()
                        } else {
                            alphabet[random.nextInt(alphabet.size)]
                        }
                    }
                }
            }
            val input = parts.fold(ByteArray(0)) { acc, part -> acc + part }
            assertStreamEquivalent(input, options[iteration % options.size], what = "random #$iteration")
        }
    }

    @Test
    fun arrayInputReadsTheSameAsTheArray() {
        val bytes = calendar.encodeToByteArray()
        assertEquals(renderResult(ICal.parse(bytes)), renderResult(ICal.parser().parse(bytes.asByteInput())))
        assertEquals(
            renderComponents(ICal.parser().components(bytes)),
            renderComponents(ICal.parser().components(bytes.asByteInput())),
        )
        assertEquals(
            renderEvents(ICal.parser().reader(bytes), true),
            renderEvents(ICal.parser().reader(bytes.asByteInput()), true),
        )
    }

    @Test
    fun byteArrayInputChecksItsArguments() {
        val input = "abc".encodeToByteArray().asByteInput()
        assertFailsWith<IllegalArgumentException> { input.read(ByteArray(2), 1, 2) }
        assertEquals("ByteInput(3 bytes, position 0)", input.toString())
        assertEquals(2, input.read(ByteArray(2), 0, 2))
        assertEquals(1, input.read(ByteArray(2), 0, 2))
        assertEquals(-1, input.read(ByteArray(2), 0, 2))
        val broken = ICal.parser().components(ByteInput { _, _, length -> length + 1 })
        assertFailsWith<IllegalStateException> { broken.hasNext() }
        assertFailsWith<IllegalStateException> { ICal.parser().parse(ByteInput { _, _, _ -> -2 }) }
    }

    private fun calendarWith(text: String): String {
        val folded = text.chunked(40).joinToString("\r\n ")
        return "BEGIN:VCALENDAR\r\nPRODID:test\r\nBEGIN:VEVENT\r\nSUMMARY:$folded\r\nDESCRIPTION:$text\r\n" +
            "END:VEVENT\r\nEND:VCALENDAR\r\n"
    }

    private class Random64(private var state: Long) {
        fun nextInt(bound: Int): Int {
            state += -0x61c8864680b583ebL
            var z = state
            z = (z xor (z ushr 30)) * -0x40a7b892e31b1a47L
            z = (z xor (z ushr 27)) * -0x6b2fb644ecceee15L
            z = z xor (z ushr 31)
            return ((z ushr 33) % bound).toInt()
        }
    }

    private companion object {
        private const val REPEATS = 200
    }
}
