package io.github.calendrum.syntax

import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CharsetDecodingTest {
    private val russian = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//ics-mp//Charset test//RU",
        "BEGIN:VEVENT",
        "UID:charset-1@example.com",
        "DTSTART;TZID=Europe/Moscow:20260901T090000",
        "SUMMARY:Лекция №5: Ёлки\\, ёжики и съезд",
        "LOCATION:Ауд. 214\\, корпус «Б» — вход со двора",
        "DESCRIPTION:Преподаватель — доцент Щукин Э.Ю. Приносите тетради и калькулятор.\\nЗанятие ведётся по у",
        " чебнику\\, глава 3.",
        "ATTENDEE;CN=\"Фёдорова Юлия\";ROLE=REQ-PARTICIPANT:mailto:fedorova@example.com",
        "CATEGORIES:Лекции,Математика",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private val russianKoi8 = russian.replace("№", "N").replace("«", "\"").replace("»", "\"").replace("—", "-")

    private val western = crlf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//ics-mp//Charset test//FR",
        "BEGIN:VEVENT",
        "UID:charset-2@example.com",
        "SUMMARY:Réunion d'équipe – café & crème brûlée",
        "LOCATION:Straße 5\\, Zürich",
        "DESCRIPTION:Œuvres complètes\\; prix 25 € — «Noël» “quoted” ‘single’ … ™ • ‚low„ ƒ ˆ ‰ Š ‹ Ž ˜ š › œ ž Ÿ",
        " † ‡ – ß é œ",
        "ATTENDEE;CN=\"Hélène Müller\":mailto:helene@example.org",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private val latin1 = crlf(
        "BEGIN:VCALENDAR",
        "BEGIN:VEVENT",
        "SUMMARY:Réunion d'équipe\\, café & crème brûlée à Zürich",
        "DESCRIPTION:Straße\\, ½ ¼ ¾ © ® ° ± µ ¶ · × ÷ ¿ ¡ ß ñ ç ø å æ",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private val emoji = crlf(
        "BEGIN:VCALENDAR",
        "BEGIN:VEVENT",
        "SUMMARY:Встреча 🚀 Réunion 📄 会議 ☕",
        "DESCRIPTION:Пары 𝄞 и суррогаты 😀😃",
        "END:VEVENT",
        "END:VCALENDAR",
    )

    private fun textFor(charset: String): String = when (charset) {
        "KOI8-R" -> russianKoi8
        "windows-1251" -> russian
        "ISO-8859-1" -> latin1
        else -> western
    }

    private fun assertSameAsUtf8(text: String, read: CharsetRead, message: String) {
        val expected = read(text.encodeToByteArray())
        assertEquals(expected.events.size, read.events.size, "$message: event count")
        for (index in expected.events.indices) {
            assertEquals(expected.events[index], read.events[index], "$message: event #$index")
        }
        val unexpected = read.problems.filter { it.severity == Severity.Error || it.code == ProblemCode.InvalidUtf8 }
        assertEquals(emptyList(), unexpected, message)
    }

    @Test
    fun singleByteRoundTripWithHint() {
        for (charset in TestCharsets.singleByte) {
            val text = textFor(charset)
            val read = readWithCharset(TestCharsets.encode(text, charset), charset)
            assertSameAsUtf8(text, read, "$charset by hint")
            assertEquals(if (charset == "ISO-8859-1") "windows-1252" else charset, read.lexerCharset.canonicalName)
            assertEquals(DecodedInput.SOURCE_HINT, read.source)
            assertEquals(listOf("ICAL-S024"), read.codes, charset)
        }
    }

    @Test
    fun singleByteRoundTripWithDetection() {
        val expected = mapOf(
            "windows-1251" to "windows-1251",
            "KOI8-R" to "KOI8-R",
            "windows-1252" to "windows-1252",
            "ISO-8859-1" to "windows-1252",
        )
        for (charset in TestCharsets.singleByte) {
            val text = textFor(charset)
            val read = readWithCharset(TestCharsets.encode(text, charset), null)
            assertSameAsUtf8(text, read, "$charset detected")
            assertEquals(expected.getValue(charset), read.lexerCharset.canonicalName, charset)
            assertEquals(DecodedInput.SOURCE_HEURISTIC, read.source)
            assertTrue(read.confidence >= LOW_CONFIDENCE, "$charset confidence ${read.confidence}")
            assertEquals(listOf("ICAL-S025"), read.codes, charset)
        }
    }

    @Test
    fun wideRoundTripWithByteOrderMark() {
        for (text in listOf(russian, western, emoji)) {
            for (charset in TestCharsets.wide) {
                val bytes = TestCharsets.bom(charset) + TestCharsets.encode(text, charset)
                val read = readWithCharset(bytes, null)
                assertSameAsUtf8(text, read, "$charset with BOM")
                assertEquals(charset, read.lexerCharset.canonicalName)
                assertEquals(DecodedInput.SOURCE_BOM, read.source)
                assertEquals(listOf("ICAL-S024"), read.codes)
                val plain = read(bytes)
                assertEquals(read.events, plain.events, "$charset: the UTF-8 constructor also transcodes by BOM")
            }
        }
    }

    @Test
    fun wideRoundTripWithHint() {
        for (text in listOf(russian, emoji)) {
            for (charset in TestCharsets.wide) {
                val read = readWithCharset(TestCharsets.encode(text, charset), charset)
                assertSameAsUtf8(text, read, "$charset by hint")
                assertEquals(DecodedInput.SOURCE_HINT, read.source)
                val family = charset.substring(0, 6)
                val unmarked = readWithCharset(TestCharsets.encode(text, charset), family)
                assertSameAsUtf8(text, unmarked, "$charset by hint $family")
                assertEquals(charset, unmarked.lexerCharset.canonicalName)
                assertEquals(listOf("ICAL-S024"), unmarked.codes)
            }
        }
    }

    @Test
    fun utf8InputIsReadWithoutCopyOrProblems() {
        val bytes = emoji.encodeToByteArray()
        val (decoded, problems) = decode(bytes, null)
        assertTrue(decoded.bytes === bytes)
        assertEquals("UTF-8", decoded.charset)
        assertEquals(DecodedInput.SOURCE_UTF8, decoded.source)
        assertEquals(emptyList(), problems)
        val read = readWithCharset(bytes, null)
        assertSameAsUtf8(emoji, read, "UTF-8")
        assertEquals(emptyList(), read.problems)
        val hinted = readWithCharset(bytes, "utf-8")
        assertEquals(DecodedInput.SOURCE_HINT, hinted.source)
        assertEquals(emptyList(), hinted.problems)
    }

    @Test
    fun utf8ByteOrderMarkWins() {
        val bytes = TestCharsets.bom("UTF-8") + russian.encodeToByteArray()
        val read = readWithCharset(bytes, "windows-1251")
        assertEquals(listOf("ICAL-S027", "ICAL-S001"), read.codes)
        assertEquals(InputCharset.Utf8, read.lexerCharset)
        assertEquals(DecodedInput.SOURCE_BOM, read.source)
        assertEquals(read(bytes).events, read.events)
    }

    @Test
    fun surrogatePairsAndUnpairedSurrogates() {
        fun utf16(bigEndian: Boolean, vararg units: Int): ByteArray = ByteArray(units.size * 2) {
            val unit = units[it / 2]
            val high = it % 2 == 0 == bigEndian
            (if (high) unit shr 8 else unit).toByte()
        }
        for (bigEndian in listOf(false, true)) {
            val charset = if (bigEndian) "UTF-16BE" else "UTF-16LE"
            fun text(vararg units: Int): String = transcodeText(utf16(bigEndian, *units), charset)
            assertEquals("A🚀B", text(0x41, 0xD83D, 0xDE80, 0x42))
            assertEquals("\uFFFDA", text(0xD800, 0x41))
            assertEquals("\uFFFDA", text(0xDC00, 0x41))
            assertEquals("\uFFFD𐀀", text(0xD800, 0xD800, 0xDC00))
            assertEquals("A\uFFFD", text(0x41, 0xD800))
            assertEquals("\uFFFD\uFFFD", text(0xDC00, 0xDBFF))
            val odd = utf16(bigEndian, 0x41, 0x42) + bytesOf(0x43)
            assertEquals("AB\uFFFD", decode(odd, charset).first.text())
            val leadThenOdd = utf16(bigEndian, 0x41, 0xD800) + bytesOf(0x43)
            assertEquals("A\uFFFD", decode(leadThenOdd, charset).first.text())
        }
        for (bigEndian in listOf(false, true)) {
            val charset = if (bigEndian) "UTF-32BE" else "UTF-32LE"
            fun text(vararg points: Int): String =
                transcodeText(TestCharsets.wideUnits(points.toList(), bigEndian), charset)
            assertEquals("A😀B", text(0x41, 0x1F600, 0x42))
            assertEquals("A\uFFFDB", text(0x41, 0xD800, 0x42))
            assertEquals("A\uFFFD\uFFFDB", text(0x41, 0xD800, 0xDC00, 0x42))
            assertEquals("A\uFFFDB", text(0x41, 0x110000, 0x42))
            assertEquals("A\uFFFDB", text(0x41, -1, 0x42))
            assertEquals("A\uFEFF", text(0x41, 0xFEFF))
            for (tail in 1..3) {
                val bytes = TestCharsets.wideUnits(listOf(0x41), bigEndian) + ByteArray(tail) { 0x42 }
                assertEquals("A\uFFFD", decode(bytes, charset).first.text(), "$charset tail $tail")
            }
        }
    }

    @Test
    fun labelsAndAliases() {
        val aliases = mapOf(
            InputCharset.Utf8 to listOf("UTF-8", "utf8", "Utf_8", "unicode-1-1-utf-8"),
            InputCharset.Utf16Le to listOf("UTF-16LE", "utf16le", "x-utf-16le", "UnicodeLittleUnmarked"),
            InputCharset.Utf16Be to listOf("UTF-16BE", "utf16be", "x-utf-16be", "UnicodeBigUnmarked"),
            InputCharset.Utf32Le to listOf("UTF-32LE", "utf32le", "x-utf-32le"),
            InputCharset.Utf32Be to listOf("UTF-32BE", "utf32be", "X-UTF-32BE"),
            InputCharset.Windows1251 to
                listOf("windows-1251", "WINDOWS-1251", "cp1251", "CP-1251", "x-cp1251", "win-1251", "ansi-1251"),
            InputCharset.Koi8R to listOf("KOI8-R", "koi8-r", "koi8_r", "koi8r", "KOI8", "koi", "csKOI8R"),
            InputCharset.Windows1252 to listOf(
                "windows-1252", "cp1252", "x-cp1252", "ansi-1252", "ibm-1252",
                "ISO-8859-1", "iso8859-1", "ISO_8859-1:1987", "latin1", "Latin-1", "l1", "IBM819", "cp819",
                "US-ASCII", "ascii", "us", "ANSI_X3.4-1968", "ISO646-US", "cp367",
            ),
            InputCharset.Utf16 to listOf("UTF-16", "utf16", "unicode", "UCS-2", "ISO-10646-UCS-2"),
            InputCharset.Utf32 to listOf("UTF-32", "utf32", "UCS-4"),
        )
        for ((charset, labels) in aliases) {
            for (label in labels) assertEquals(charset, charsetForLabel(label), label)
        }
        assertEquals(InputCharset.Windows1251, charsetForLabel("text/calendar; charset=\"windows-1251\""))
        assertEquals(InputCharset.Koi8R, charsetForLabel("text/calendar;method=REQUEST; CHARSET = koi8-r ; x=1"))
        assertEquals(InputCharset.Windows1252, charsetForLabel("x/y;ChArSeT=latin1"))
        assertEquals(InputCharset.Utf8, charsetForLabel("charse=1; charset=utf-8"))
        assertNull(charsetForLabel("charset=windows-1250"))
        assertNull(charsetForLabel("charse=utf-8"))
        assertEquals(InputCharset.Utf8, charsetForLabel("  'UTF-8'  "))
        val unknownLabels =
            listOf("", "shift_jis", "gb18030", "utf-7", "iso-8859-5", "windows-1250", "utf-8x", "Кириллица")
        for (unknown in unknownLabels) {
            assertNull(charsetForLabel(unknown), unknown)
        }
    }

    @Test
    fun unknownHintIsReportedAndDetectionContinues() {
        val bytes = TestCharsets.encode(russian, "windows-1251")
        val read = readWithCharset(bytes, "shift_jis")
        assertEquals(listOf("ICAL-S026", "ICAL-S025"), read.codes)
        assertEquals(InputCharset.Windows1251, read.lexerCharset)
        assertSameAsUtf8(russian, read, "unknown hint")
    }

    @Test
    fun hintsThatContradictTheData() {
        val cp1251 = TestCharsets.encode(russian, "windows-1251")
        val utf8Hint = readWithCharset(cp1251, "UTF-8")
        assertEquals(listOf("ICAL-S027", "ICAL-S025"), utf8Hint.codes)
        assertEquals(InputCharset.Windows1251, utf8Hint.lexerCharset)
        assertSameAsUtf8(russian, utf8Hint, "UTF-8 hint on windows-1251")

        val utf8 = russian.encodeToByteArray()
        val cp1251Hint = readWithCharset(utf8, "windows-1251")
        assertEquals(listOf("ICAL-S027"), cp1251Hint.codes)
        assertEquals(InputCharset.Utf8, cp1251Hint.lexerCharset)
        assertSameAsUtf8(russian, cp1251Hint, "windows-1251 hint on UTF-8")

        for (hint in listOf("ISO-8859-1", "windows-1252", "US-ASCII", "KOI8-R")) {
            val read = readWithCharset(cp1251, hint)
            assertEquals(listOf("ICAL-S027", "ICAL-S025"), read.codes, hint)
            assertEquals(InputCharset.Windows1251, read.lexerCharset, hint)
            assertSameAsUtf8(russian, read, "$hint hint on windows-1251")
        }

        val wideHint = readWithCharset(utf8, "UTF-16LE")
        assertEquals(listOf("ICAL-S027"), wideHint.codes)
        assertEquals(InputCharset.Utf8, wideHint.lexerCharset)

        val bigEndian = TestCharsets.encode(russian, "UTF-16BE")
        val wrongOrder = readWithCharset(bigEndian, "UTF-16LE")
        assertEquals(listOf("ICAL-S027", "ICAL-S025"), wrongOrder.codes)
        assertEquals(InputCharset.Utf16Be, wrongOrder.lexerCharset)
        assertSameAsUtf8(russian, wrongOrder, "UTF-16LE hint on UTF-16BE")

        val wrongWidth = readWithCharset(TestCharsets.encode(russian, "UTF-32LE"), "UTF-16")
        assertEquals(InputCharset.Utf32Le, wrongWidth.lexerCharset)
        assertEquals(listOf("ICAL-S027", "ICAL-S025"), wrongWidth.codes)

        val withBom = TestCharsets.bom("UTF-16LE") + TestCharsets.encode(russian, "UTF-16LE")
        assertEquals(listOf("ICAL-S027", "ICAL-S024"), readWithCharset(withBom, "windows-1251").codes)
        assertEquals(listOf("ICAL-S024"), readWithCharset(withBom, "UTF-16").codes)
    }

    @Test
    fun hintsThatAgreeWithShortOrAmbiguousData() {
        val caps = TestCharsets.encode(crlf("BEGIN:X", "SUMMARY:ЛК-1, ЛК-2", "END:X"), "KOI8-R")
        val read = readWithCharset(caps, "KOI8-R")
        assertEquals(listOf("ICAL-S024"), read.codes)
        assertEquals("ЛК-1, ЛК-2", read.events[1].property!!.value)

        val ascii = readWithCharset(crlf("BEGIN:X", "SUMMARY:plain", "END:X").encodeToByteArray(), "windows-1251")
        assertEquals(emptyList(), ascii.codes)
        assertEquals(InputCharset.Windows1251, ascii.lexerCharset)
    }

    @Test
    fun oneBrokenByteKeepsUtf8() {
        val text = russian.encodeToByteArray()
        val index = text.indexOf(0xD0.toByte()) + 20
        val broken = text.copyOf().also { it[index] = 0xFF.toByte() }
        val read = readWithCharset(broken, null)
        assertEquals(InputCharset.Utf8, read.lexerCharset)
        assertEquals(listOf("ICAL-S006"), read.codes)
        assertTrue(read.events.any { '\uFFFD' in (it.property?.value ?: "") })

        val first = text.indexOf(0xD0.toByte())
        val brokenFirst = text.copyOf().also { it[first] = 0xFF.toByte() }
        val stillUtf8 = readWithCharset(brokenFirst, null)
        assertEquals(InputCharset.Utf8, stillUtf8.lexerCharset)
        assertEquals(listOf("ICAL-S006"), stillUtf8.codes)
    }

    @Test
    fun invalidToValidThresholdIsTwoValidSequencesPerInvalidOne() {
        val prefix = "SUMMARY:".encodeToByteArray() + bytesOf(0xFF) + " ab ".encodeToByteArray()
        val twoValid = prefix + "я ю\r\n".encodeToByteArray()
        assertEquals(InputCharset.Utf8, readWithCharset(twoValid, null).lexerCharset)
        val oneValid = prefix + "я\r\n".encodeToByteArray()
        assertTrue(readWithCharset(oneValid, null).lexerCharset.isSingleByte)
    }

    @Test
    fun latinLetterInAsciiWordIsDetected() {
        val bytes = TestCharsets.encode(crlf("BEGIN:VEVENT", "SUMMARY:Café meeting", "END:VEVENT"), "windows-1252")
        val read = readWithCharset(bytes, null)
        assertEquals(InputCharset.Windows1252, read.lexerCharset)
        assertEquals(listOf("ICAL-S025"), read.codes)
        val detected = read.problems[0]
        assertEquals(2, detected.line)
        assertEquals(12, detected.column)
        assertEquals("BEGIN:VEVENT\r\nSUMMARY:Caf".length, detected.offset)
        assertEquals("Café meeting", read.events[1].property!!.value)
    }

    @Test
    fun shortAmbiguousTextIsReportedWithLowConfidence() {
        val read = readWithCharset(TestCharsets.encode(crlf("BEGIN:X", "SUMMARY:ЛК", "END:X"), "KOI8-R"), null)
        assertEquals(listOf("ICAL-S025", "ICAL-S028"), read.codes)
        assertTrue(read.confidence < LOW_CONFIDENCE)
        assertEquals(Severity.Warning, read.problems[1].severity)
    }

    @Test
    fun positionsAreInDecodedUtf8WithTheSameLines() {
        val text = crlf("A:ёж", "B;X=\"Щ\":ю", " я", "C:1")
        val read = readWithCharset(TestCharsets.encode(text, "windows-1251"), "windows-1251")
        val utf8 = read(text.encodeToByteArray())
        assertEquals(utf8.events, read.events)
        assertEquals(listOf(1, 2, 4), read.events.map { it.line })
        assertEquals(listOf(0, 8, 26), read.events.map { it.offset })
    }

    @Test
    fun lazyDetectionEqualsEagerDecoding() {
        val inputs = listOf(
            TestCharsets.encode(russian, "windows-1251"),
            TestCharsets.encode(russianKoi8, "KOI8-R"),
            TestCharsets.encode(western, "windows-1252"),
            russian.encodeToByteArray(),
            bytesOf(0x20) + TestCharsets.encode(russian, "windows-1251"),
            TestCharsets.encode(crlf("X:é", "Y:ab"), "ISO-8859-1"),
            bytesOf(0xC3) + crlf("SUMMARY:ÿ").encodeToByteArray(),
        )
        for ((index, input) in inputs.withIndex()) {
            for (hint in listOf(null, "UTF-8")) {
                val lazy = lexWithCharset(input, hint)
                val (decoded, decodeProblems) = decode(input, hint)
                val eager = lex(decoded.bytes, decoded.offset, decoded.length)
                assertEquals(eager.lines, lazy.lines, "input #$index, hint $hint")
                assertEquals(
                    (decodeProblems + eager.problems).map { it.toString() }.sorted(),
                    lazy.problems.map { it.toString() }.sorted(),
                    "input #$index, hint $hint",
                )
                assertEquals(decoded.charset, lazy.lexer.decodedCharset.canonicalName)
            }
        }
    }

    @Test
    fun selectionDoesNotChangeDetection() {
        val text = crlf(
            "BEGIN:VCALENDAR",
            "X-WR-CALNAME:Расписание группы",
            "BEGIN:VEVENT",
            "SUMMARY:Алгебра",
            "UID:1",
            "DESCRIPTION:Ауд. 5\\, вход со двора",
            "END:VEVENT",
            "BEGIN:VTODO",
            "SUMMARY:Сдать отчёт",
            "END:VTODO",
            "END:VCALENDAR",
        )
        for (charset in listOf("windows-1251", "KOI8-R")) {
            val input = TestCharsets.encode(text, charset)
            val full = readWithCharset(input, null)
            for (mask in Mask.fixed + Mask(setOf("VEVENT"), setOf("DESCRIPTION"), false, false)) {
                val selected = readWithCharset(input, null, mask.selection())
                assertEquals(filter(full.events, mask), selected.events, "$charset with $mask")
                assertEquals(full.lexerCharset, selected.lexerCharset)
            }
        }
    }

    @Test
    fun unsupportedSignaturesAreNotRead() {
        for (tail in listOf("+/v9", "+/v+", "+/v/")) {
            val read = readWithCharset(tail.encodeToByteArray() + crlf("BEGIN:VCALENDAR", "END:VCALENDAR"), null)
            assertEquals(listOf("ICAL-S011"), read.codes, tail)
            assertEquals("unsupported encoding, input not read: UTF-7", read.problems[0].message)
        }
        val signatures = mapOf(
            "UTF-7" to "+/v8".encodeToByteArray(),
            "UTF-1" to bytesOf(0xF7, 0x64, 0x4C),
            "UTF-EBCDIC" to bytesOf(0xDD, 0x73, 0x66, 0x73),
            "SCSU" to bytesOf(0x0E, 0xFE, 0xFF),
            "BOCU-1" to bytesOf(0xFB, 0xEE, 0x28),
            "GB18030" to bytesOf(0x84, 0x31, 0x95, 0x33),
            "EBCDIC" to bytesOf(0xC2, 0xC5, 0xC7, 0xC9, 0xD5, 0x7A, 0xE5, 0xC3),
        )
        for ((name, signature) in signatures) {
            for (charset in listOf(null, "UTF-8")) {
                val read = readWithCharset(signature + crlf("BEGIN:VCALENDAR", "END:VCALENDAR"), charset)
                assertEquals(emptyList(), read.events, name)
                assertEquals(listOf("ICAL-S011"), read.codes, name)
                assertEquals("unsupported encoding, input not read: $name", read.problems[0].message)
            }
            val (decoded, problems) = decode(signature, null)
            assertNull(decoded.charset)
            assertEquals(0, decoded.length)
            assertEquals(listOf("ICAL-S011"), problems.map { it.id })
        }
        assertFalse(readWithCharset(crlf("+/vX:1").encodeToByteArray(), null).events.isEmpty())
    }

    @Test
    fun decoderNeverThrowsOnRandomInput() {
        val random = TestRandom(1251)
        val hints = listOf(
            null, "UTF-8", "UTF-16", "UTF-16LE", "UTF-32BE",
            "windows-1251", "KOI8-R", "cp1252", "latin1", "ascii", "bogus",
        )
        repeat(1_500) { iteration ->
            val size = random.nextInt(200)
            val input = ByteArray(size) {
                when (random.nextInt(6)) {
                    0 -> random.nextInt(256).toByte()
                    1 -> (0xC0 + random.nextInt(64)).toByte()
                    2 -> 0
                    3 -> "\r\n :;,\"=".random(random).code.toByte()
                    else -> (0x41 + random.nextInt(26)).toByte()
                }
            }
            val hint = hints[iteration % hints.size]
            val (decoded, _) = decode(input, hint)
            decoded.text()
            val lazy = lexWithCharset(input, hint)
            val eager = lex(decoded.bytes, decoded.offset, decoded.length)
            assertEquals(eager.lines, lazy.lines, "input #$iteration, hint $hint")
        }
    }

    @Test
    fun severalEmptyContinuationLinesInsideACharacterKeepUtf8() {
        val bytes = "SUMMARY:".encodeToByteArray() +
            bytesOf(0xD0, 0x0D, 0x0A, 0x20, 0x0D, 0x0A, 0x09, 0x0D, 0x0A, 0x20, 0x9F, 0xD1, 0x80, 0x0D, 0x0A)
        val read = readWithCharset(bytes, null)
        assertEquals(InputCharset.Utf8, read.lexerCharset)
        assertEquals(listOf("ICAL-S021"), read.codes)
        assertEquals("Пр", read.events.single().property!!.value)
        val (decoded, problems) = decode(bytes, null)
        assertEquals("UTF-8", decoded.charset)
        assertEquals(emptyList(), problems)
    }

    @Test
    fun wideInputWithoutByteOrderMarkIsDetectedByZeroBytes() {
        val expected = readWithCharset(russian.encodeToByteArray(), null)
        for (charset in TestCharsets.wide) {
            val bytes = TestCharsets.encode(russian, charset)
            val read = readWithCharset(bytes, null)
            assertEquals(charsetForLabel(charset), read.lexerCharset, charset)
            assertEquals(DecodedInput.SOURCE_HEURISTIC, read.source, charset)
            assertEquals(listOf("ICAL-S025"), read.codes, charset)
            assertEquals("$charset, confidence 100%", read.problems.single().message.substringAfter(": "))
            assertEquals(expected.events, read.events, charset)
            val (decoded, problems) = decode(bytes, null)
            assertEquals(charset, decoded.charset)
            assertEquals(russian, decoded.text())
            assertEquals(listOf("ICAL-S025"), problems.map { it.id })

            val hinted = readWithCharset(bytes, "UTF-8")
            assertEquals(listOf("ICAL-S027", "ICAL-S025"), hinted.codes, charset)
            assertEquals(expected.events, hinted.events, charset)
            val singleByteHint = readWithCharset(bytes, "windows-1251")
            assertEquals(listOf("ICAL-S027", "ICAL-S025"), singleByteHint.codes, charset)
        }
    }

    @Test
    fun zeroBytesThatDoNotLookLikeWideTextStayUtf8() {
        val stray = "BEGIN:VCALENDAR\r\nX:a\u0000b\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        assertEquals(InputCharset.Utf8, readWithCharset(stray, null).lexerCharset)
        val lateZeros = "BEGIN:VCALENDAR\r\nX:".encodeToByteArray() + ByteArray(64)
        assertEquals(InputCharset.Utf8, readWithCharset(lateZeros, null).lexerCharset)
        val fewZeros = bytesOf(0x42, 0x00) + "EGIN:VCALENDAR\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        assertEquals(InputCharset.Utf8, readWithCharset(fewZeros, null).lexerCharset)
        val cyrillicFirst = TestCharsets.encode("ЖЖ\r\n" + russian, "UTF-16LE")
        assertFalse(readWithCharset(cyrillicFirst, null).lexerCharset.isWide)
        val text = readWithCharset(TestCharsets.encode(russian, "UTF-16LE"), null)
        assertEquals(InputCharset.Utf16Le, text.lexerCharset)
    }

    private fun transcodeText(bytes: ByteArray, charset: String): String {
        val result = transcode(charsetForLabel(charset)!!, bytes, 0, 0, bytes.size, 0)
        return result.bytes.decodeToString(0, result.end)
    }

    private fun String.random(random: TestRandom): Char = this[random.nextInt(length)]

    private fun lexWithCharset(input: ByteArray, charset: String?): LexResult {
        val problems = mutableListOf<Problem>()
        val lexer = ContentLexer(input, charset, ParseLimits.Default) { problems += it }
        val lines = mutableListOf<Lexed>()
        while (true) lines += (lexer.next() ?: break).snapshot()
        return LexResult(lines, problems, lexer)
    }
}
