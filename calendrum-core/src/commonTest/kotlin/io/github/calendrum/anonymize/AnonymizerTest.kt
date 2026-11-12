package io.github.calendrum.anonymize

import io.github.calendrum.ICal
import io.github.calendrum.ProblemCode
import io.github.calendrum.model.calendars
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.InputCharset
import io.github.calendrum.syntax.SingleByteCharsets
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AnonymizerTest {
    private fun assertRewrites(input: String) {
        val bytes = input.encodeToByteArray()
        val source = ICal.parse(bytes).components
        val result = Anonymizer(KEY, 0).anonymize(bytes)
        assertTrue(sameShape(source, result.components), input)
        assertEquals(ICal.parse(ICal.writer().encodeToString(result.components)).components, result.components, input)
        assertEquals(ICal.parse(result.bytes).components, result.components, input)
        assertFalse("secret" in unfoldedText(result.bytes), input)
    }

    private fun unfoldedText(bytes: ByteArray): String = bytes.decodeToString().replace("\r\n ", "").replace("\r ", "")

    // A "structural" place does not make its value structural. Every word of a person below sits where the
    // policy used to keep the value as it was (found by the acceptance review of 2026-11-08); none of it may stay, and
    // what is structural must.
    @Test
    fun freeTextInStructuralPlacesGoes() {
        val result = anonymize(LEAKS)
        val text = unfoldedText(result.bytes)
        val secrets = listOf(
            "Zorgwin",
            "Quillfeather",
            "5550199",
            "Clinic",
            "Oncology",
            "oncology",
            "diagnosis",
            "vacation",
        )
        for (secret in secrets) {
            assertFalse(secret in text, "$secret stays in\n$text")
        }
        for (structure in listOf(
            "TZOFFSETFROM:+0100", "TZOFFSETTO:+0100", "STATUS:CONFIRMED", "CLASS:PRIVATE", "TRANSP:OPAQUE",
            "PRIORITY:5", "COLOR:teal", "TRIGGER:-PT5M", "ACTION:DISPLAY", "RRULE:FREQ=WEEKLY;COUNT=3", "TZNAME:CET",
            "PARTSTAT=ACCEPTED", "ROLE=REQ-PARTICIPANT", "LANGUAGE=de-CH", "SCHEDULE-STATUS=2.0", "TZID=Europe/Berlin",
        )) {
            assertTrue(structure in text, "$structure is gone from\n$text")
        }
        val output = ICal.parse(result.bytes)
        assertTrue(output.problems.isEmpty(), output.problems.toString())
        // The zone that is not a known one keeps one pseudonym: its VTIMEZONE and the TZID= that refers to it still
        // match.
        val zone = Regex("TZID:(Zone-[0-9a-f]+)").find(text)!!.groupValues[1]
        assertTrue("DTSTART;TZID=$zone:" in text, text)
        assertEquals(3, ICal.parse(result.bytes).calendars.single().events.size)
        assertTrue("CLASS: 1 values are not a structural token" in result.summary, result.summary)
        // A producer the fixups read stays, so that a corpus still tells which repairs a file needs.
        val google = LEAKS.replace(
            "PRODID:-//Zorgwin Clinic//Oncology Planner//EN",
            "PRODID:-//Google Inc//Google Calendar 70.9054//EN",
        )
        assertTrue("PRODID:-//Google Inc//Google Calendar 70.9054//EN" in unfoldedText(anonymize(google).bytes))
    }

    @Test
    fun aProducerStaysOnlyWithTheWordsOfAProduct() {
        for (kept in listOf(
            "-//Google Inc//Google Calendar 70.9054//EN",
            "-//Yandex LLC//Yandex Calendar//EN",
            "-//Microsoft Corporation//Outlook 16.0 MIMEDIR//EN",
            "-//Apple Inc.//macOS 14.0//EN",
            "-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN",
            "-//Sabre//Sabre VObject 4.5.0//EN",
        )) {
            val text = calendar("SUMMARY:x").replace("PRODID:-//Test//Anonymizer//EN", "PRODID:$kept")
            assertTrue("PRODID:$kept" in unfoldedText(anonymize(text).bytes), kept)
        }
        for (replaced in listOf(
            "-//Zorgwin Clinic//Teams planner//EN",
            "-//Google Inc//Anna Quillfeather//EN",
            "-//Zoomorph//EN",
            "-//Google//Календарь клиники//RU",
            "-//Calendar//Planner//EN",
        )) {
            val text = calendar("SUMMARY:x").replace("PRODID:-//Test//Anonymizer//EN", "PRODID:$replaced")
            assertFalse("PRODID:$replaced" in unfoldedText(anonymize(text).bytes), replaced)
        }
    }

    @Test
    fun aDateOrARuleThatIsNotOneIsText() {
        val text = unfoldedText(anonymize(LEAKS, weeks = 2).bytes)
        assertFalse("call back" in text)
        assertFalse("X-NOTE=" in text, text)
        assertTrue("DTSTART;TZID=Europe/Berlin:20260119T090000" in text, "a real date still moves: $text")
    }

    @Test
    fun aLineWithAnOpenQuoteBeforeALinkReadsBack() {
        for (input in UNCLOSED_QUOTES) assertRewrites(input)
    }

    @Test
    fun keepsTheShapeOfTheTree() {
        for (sample in SAMPLES) {
            val result = anonymize(sample)
            assertTrue(result.isVerified, result.summary)
            val source = ICal.parse(sample).components
            val output = ICal.parse(result.bytes)
            assertTrue(sameShape(source, output.components))
            assertEquals(output.components, result.components)
            assertTrue(output.problems.isEmpty(), output.problems.toString())
        }
    }

    @Test
    fun keepsTheLengthAndClassOfEveryCharacter() {
        for (sample in SAMPLES) {
            val pairs = propertyPairs(sample, anonymize(sample).bytes)
            var checked = 0
            for ((source, output) in pairs) {
                if (source.name in TEXT_PROPERTIES) {
                    assertSameClasses(source.value, output.value, source.name)
                    if (source.value.any { it.isLetter() }) assertNotEquals(source.value, output.value, source.name)
                    checked++
                }
                val cn = source.parameter("CN") ?: continue
                assertSameClasses(cn.value, output.parameter("CN")!!.value, "CN")
                assertNotEquals(cn.value, output.parameter("CN")!!.value)
            }
            assertTrue(checked >= 4)
        }
    }

    @Test
    fun keepsTextEscapesInPlace() {
        val result = anonymize(AnonymizerSamples.schedule)
        for ((source, output) in propertyPairs(AnonymizerSamples.schedule, result.bytes)) {
            if (source.name !in TEXT_PROPERTIES) continue
            val value = source.value
            for (index in value.indices) {
                if (value[index] != '\\' || index + 1 == value.length) continue
                assertEquals('\\', output.value[index], source.name)
                assertEquals(value[index + 1], output.value[index + 1], source.name)
            }
        }
    }

    @Test
    fun mapsTheSameAddressToTheSameFakeOne() {
        val result = anonymize(AnonymizerSamples.invite)
        val event = ICal.parse(result.bytes).components[0].components("VEVENT")[0]
        val organizer = event.property("ORGANIZER")!!
        val delegator = event.properties("ATTENDEE")[0].parameter("DELEGATED-FROM")!!.value
        assertTrue(organizer.value.startsWith("MAILTO:u-") && organizer.value.endsWith("@example.invalid"))
        assertEquals(organizer.value.substringAfter(':'), delegator.substringAfter(':'))
        assertTrue(delegator.startsWith("mailto:u-"))
        val sentBy = organizer.parameter("SENT-BY")!!.value
        assertNotEquals(sentBy.substringAfter(':'), organizer.value.substringAfter(':'))
        val schedule = ICal.parse(anonymize(AnonymizerSamples.schedule).bytes).components[0].components("VEVENT")
        assertEquals(schedule[0].property("ORGANIZER"), schedule[1].property("ORGANIZER"))
        val emails = setOf(
            schedule[0].property("ORGANIZER")!!.value,
            *schedule[0].properties("ATTENDEE").map { it.value }.toTypedArray(),
        )
        assertEquals(3, emails.size)
    }

    @Test
    fun keepsOverridesAndRelationsOnTheirSeries() {
        val events = ICal.parse(anonymize(AnonymizerSamples.schedule).bytes).components[0].components("VEVENT")
        val uid = events[0].property("UID")!!.value
        assertTrue(uid.endsWith("@example.invalid"), uid)
        assertEquals(uid, events[1].property("UID")!!.value)
        assertEquals(uid, events[1].property("RELATED-TO")!!.value)
        assertNotEquals(uid, events[2].property("UID")!!.value)
    }

    @Test
    fun repeatsEqualValuesAsEqualReplacements() {
        val events = ICal.parse(anonymize(AnonymizerSamples.schedule).bytes).components[0].components("VEVENT")
        val locations = events.map { it.property("LOCATION")!!.value }.toSet()
        assertEquals(1, locations.size)
        assertEquals(events[0].property("SUMMARY")!!.value, events[1].property("SUMMARY")!!.value)
        assertNotEquals(events[0].property("SUMMARY")!!.value, events[2].property("SUMMARY")!!.value)
        val categories = events[0].property("CATEGORIES")!!.value.split(',')
        assertEquals(2, categories.size)
        val single = anonymize(
            AnonymizerSamples.schedule.replace("CATEGORIES:Учёба,Лекции", "CATEGORIES:Лекции,Учёба"),
        )
        val swapped = ICal.parse(single.bytes).components[0].components("VEVENT")[0].property("CATEGORIES")!!.value
        assertEquals(categories.reversed(), swapped.split(','))
    }

    @Test
    fun isDeterministicWithAKeyAndDiffersBetweenKeys() {
        for (sample in SAMPLES) {
            val first = anonymize(sample)
            val second = anonymize(sample)
            assertContentEquals(first.bytes, second.bytes)
            val other = Anonymizer(ByteArray(16) { 42 }, 0).anonymize(sample.encodeToByteArray())
            assertFalse(first.bytes.contentEquals(other.bytes))
            val pairs = propertyPairs(sample, other.bytes).zip(propertyPairs(sample, first.bytes))
            for ((left, right) in pairs) {
                if (left.first.name == "SUMMARY") assertNotEquals(left.second.value, right.second.value)
            }
        }
    }

    @Test
    fun shiftsDatesByWholeWeeksAndKeepsTheSeries() {
        val weeks = 5
        val result = anonymize(AnonymizerSamples.schedule, weeks = weeks)
        assertTrue(result.isVerified, result.summary)
        for ((source, output) in propertyPairs(AnonymizerSamples.schedule, result.bytes)) {
            when (source.name) {
                "DTSTART", "DTEND", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "EXDATE" -> {
                    val before = dates(source.value)
                    val after = dates(output.value)
                    assertEquals(before.size, after.size)
                    for ((from, to) in before.zip(after)) {
                        val expected = if (source.value == "19700101T000000") 0L else weeks * 7L
                        assertEquals(expected, daysOf(to) - daysOf(from), "${source.name} $from -> $to")
                        assertEquals(weekday(from), weekday(to))
                        assertEquals(from.substring(8), to.substring(8))
                    }
                }

                "RRULE" -> {
                    val until = source.value.substringAfter("UNTIL=").take(8)
                    val shifted = output.value.substringAfter("UNTIL=").take(8)
                    assertEquals(weeks * 7L, daysOf(shifted) - daysOf(until))
                    assertEquals(source.value.substringBefore("UNTIL="), output.value.substringBefore("UNTIL="))
                }
            }
        }
        val source = ICal.parse(AnonymizerSamples.schedule).components[0]
        val output = ICal.parse(result.bytes).components[0]
        assertEquals(source.components("VTIMEZONE"), output.components("VTIMEZONE"))
        assertEquals(weeklyOccurrences(source), weeklyOccurrences(output))
        assertEquals(14, weeklyOccurrences(source))
    }

    @Test
    fun replacesWhatIsNotADateAndReportsIt() {
        val text = calendar("DTSTART:next monday", "DTEND:20260907T100000", "SUMMARY:Встреча")
        val result = anonymize(text, weeks = 1)
        val event = ICal.parse(result.bytes).components[0].components[0]
        // It used to stay as it was; a date place can hold anything, so it is text now, with its shape.
        assertNotEquals("next monday", event.property("DTSTART")!!.value)
        assertEquals("next monday".length, event.property("DTSTART")!!.value.length)
        assertEquals("20260914T100000", event.property("DTEND")!!.value)
        assertTrue("DTSTART: 1 values are not a structural token" in result.summary, result.summary)
        assertFalse("next monday" in result.summary)
    }

    @Test
    fun writesStructuralLinesByteForByte() {
        for (sample in SAMPLES) {
            val input = sample.encodeToByteArray()
            val result = anonymize(sample)
            val output = result.bytes
            var checked = 0
            for ((source, written) in propertyPairs(sample, output)) {
                val structural = source.name in STRUCTURAL || source.name.startsWith("TZ")
                if (!structural || source.parameters.any { it.name !in STRUCTURAL_PARAMETERS }) continue
                assertContentEquals(slice(input, source), slice(output, written), source.name)
                checked++
            }
            assertTrue(checked > 10)
        }
    }

    @Test
    fun keepsHtmlMarkupAndReplacesTheText() {
        val result = anonymize(AnonymizerSamples.invite)
        val pairs = propertyPairs(AnonymizerSamples.invite, result.bytes)
        val (source, output) = pairs.single { it.first.name == "X-ALT-DESC" }
        assertEquals(markup(source.value), markup(output.value))
        assertTrue("&nbsp\\;" in output.value && "&amp\\;" in output.value, output.value)
        assertTrue("<p class=\"" in output.value && "<a href=\"" in output.value)
        assertFalse("MsoNormal" in output.value || "Calibri" in output.value || "morozov" in output.value)
        assertFalse("бюджет" in output.value || "Converted" in output.value)
        assertEquals(source.value.length, output.value.length)
    }

    @Test
    fun replacesBinaryAttachmentsWithBytesOfTheSameLength() {
        val result = anonymize(AnonymizerSamples.invite)
        val (source, output) = propertyPairs(AnonymizerSamples.invite, result.bytes).single {
            it.first.name == "ATTACH"
        }
        assertEquals(source.value.length, output.value.length)
        assertNotEquals(source.value, output.value)
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
        assertTrue(output.value.all { it in alphabet })
        assertEquals(source.value.count { it == '=' }, output.value.count { it == '=' })
    }

    @Test
    fun replacesLinksAndCoordinates() {
        val result = anonymize(AnonymizerSamples.schedule)
        val event = ICal.parse(result.bytes).components[0].components("VEVENT")[2]
        assertTrue(event.property("URL")!!.value.startsWith("https://example.invalid/"))
        assertEquals("0.0;0.0", event.property("GEO")!!.value)
        val conference = event.property("X-GOOGLE-CONFERENCE")!!.value
        assertFalse("google" in conference || "meet" in conference)
    }

    @Test
    fun keepsUrisWithoutASchemeThatAreBareNames() {
        val text = calendar(
            "DESCRIPTION;ALTREP=\"Notes_2\";ALTREP-X=Vault:Встреча",
            "ATTACH;VALUE=URI:Chord",
            "ATTACH:Sosumi",
            "URL:Basso-2",
            "X-SOUND;VALUE=URI:Glass",
            "ATTACH;VALUE=URI:meet.example.org/ivanov",
            "URL:intranet/ivanov",
            "CONFERENCE;VALUE=URI:petrov@firm-example.org",
            "IMAGE;VALUE=URI;DISPLAY=BADGE:Иванов",
            "SOURCE:secret page",
            "BEGIN:VALARM",
            "ACTION:AUDIO",
            "TRIGGER:-PT15M",
            "ATTACH;VALUE=URI:Chord",
            "END:VALARM",
        )
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        val pairs = propertyPairs(text, result.bytes)
        val kept = pairs.filter { (source, _) -> source.value in setOf("Chord", "Sosumi", "Basso-2", "Glass") }
        assertEquals(5, kept.size)
        for ((source, output) in kept) assertEquals(source.value, output.value, source.name)
        val links = pairs.filter { (source, _) ->
            source.name in setOf("ATTACH", "URL", "CONFERENCE", "IMAGE", "SOURCE")
        }
        for ((source, output) in links - kept.toSet()) {
            assertTrue(
                output.value.matches(Regex("https://example\\.invalid/[0-9a-f]{16}")),
                "${source.name}: ${output.value}",
            )
        }
        val description = pairs.single { it.first.name == "DESCRIPTION" }.second
        assertEquals("Notes_2", description.parameter("ALTREP")!!.value)
        assertFalse("Vault" in description.parameter("ALTREP-X")!!.value)
        assertFalse("Встреча" in description.value)
        assertTrue(Regex("ATTACH +1 +0 +3").containsMatchIn(result.summary), result.summary)
        assertTrue(Regex("ALTREP +0 +1").containsMatchIn(result.summary), result.summary)
    }

    @Test
    fun treatsMailtoValuesAndUnknownPropertiesAsSensitive() {
        val text = calendar(
            "CONTACT:mailto:secretary@firm-example.org",
            "X-SECRET-NOTE:Пароль от сейфа — 4815",
            "X-WR-TIMEZONE:Europe/Moscow and my home",
            "X-MICROSOFT-CDO-BUSYSTATUS:BUSY",
            "FOO-BAR;X-TOKEN=Ключ:Значение",
        )
        val result = anonymize(text)
        val event = ICal.parse(result.bytes).components[0].components[0]
        assertTrue(event.property("CONTACT")!!.value.matches(Regex("mailto:u-[0-9a-f]{16}@example\\.invalid")))
        assertFalse("сейфа" in event.property("X-SECRET-NOTE")!!.value)
        // Not a known zone: a pseudonym that a TZID= with the same name would share.
        assertTrue(event.property("X-WR-TIMEZONE")!!.value.matches(Regex("Zone-[0-9a-f]{16}")))
        assertEquals("BUSY", event.property("X-MICROSOFT-CDO-BUSYSTATUS")!!.value)
        assertFalse("Значение" in event.property("FOO-BAR")!!.value)
        assertFalse("Ключ" in event.property("FOO-BAR")!!.parameter("X-TOKEN")!!.value)
    }

    @Test
    fun keepsTheShapeOfInvalidUtf8() {
        val prefix = calendar("SUMMARY:Встреча ").substringBefore("Встреча ")
        val bytes = prefix.encodeToByteArray() + "Встреча ".encodeToByteArray() +
            byteArrayOf(-1, 0x41, -30, -126, 0x42, -16, -97, -104, 0x20, -128) +
            "\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        val source = ICal.parse(bytes)
        val result = Anonymizer(KEY, 0).anonymize(bytes)
        assertTrue(result.isVerified, result.summary)
        val output = ICal.parse(result.bytes)
        assertEquals(1, source.problemCount(ProblemCode.InvalidUtf8))
        assertEquals(1, output.problemCount(ProblemCode.InvalidUtf8))
        val before = source.components[0].components[0].property("SUMMARY")!!.value
        val after = output.components[0].components[0].property("SUMMARY")!!.value
        assertEquals(before.length, after.length)
        for (index in before.indices) assertEquals(before[index] == REPLACEMENT, after[index] == REPLACEMENT, "$index")
        assertTrue("invalid UTF-8 sequences replaced" in result.summary)
    }

    @Test
    fun keepsProblemsOfMalformedLines() {
        val text = calendar(
            "X_NOTE:Личное",
            "SUMMARY;X-FLAG:Встреча",
            "ATTENDEE;CN=\"Анна Смирнова:mailto:anna@example.org",
            "COMMENT;X-A=\"Анна\"хвост:Текст",
        )
        val source = ICal.parse(text)
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        val output = ICal.parse(result.bytes)
        for (code in listOf(ProblemCode.InvalidName, ProblemCode.MalformedParameter, ProblemCode.UnclosedQuote)) {
            assertTrue(source.problemCount(code) > 0, code.id)
            assertEquals(source.problemCount(code), output.problemCount(code), code.id)
        }
        assertFalse("Личное" in result.bytes.decodeToString())
        assertFalse("Анна" in result.bytes.decodeToString())
    }

    @Test
    fun reportsProblemsThatLivedInsideReplacedValues() {
        val text = calendar("ATTENDEE;SENT-BY=\"mailto:boss@example.org:mailto:anna@example.org")
        val source = ICal.parse(text)
        assertTrue(source.problemCount(ProblemCode.UnclosedQuote) > 0)
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        assertTrue("problem codes that lived inside replaced values: ICAL-S009" in result.summary, result.summary)
        assertEquals(0, ICal.parse(result.bytes).problemCount(ProblemCode.UnclosedQuote))
    }

    @Test
    fun restoresLineEndingsAndTheByteOrderMark() {
        val lf = AnonymizerSamples.schedule.replace("\r\n", "\n")
        val result = anonymize(lf)
        assertTrue(result.isVerified, result.summary)
        assertFalse('\r' in result.bytes.decodeToString())
        assertTrue(ICal.parse(result.bytes).problemCount(ProblemCode.BareLf) > 0)
        val bom = byteArrayOf(-17, -69, -65) + AnonymizerSamples.invite.encodeToByteArray()
        val withBom = Anonymizer(KEY, 0).anonymize(bom)
        assertTrue(withBom.isVerified, withBom.summary)
        assertContentEquals(byteArrayOf(-17, -69, -65), withBom.bytes.copyOf(3))
        val open = AnonymizerSamples.invite.removeSuffix("\r\n")
        val unterminated = anonymize(open)
        assertTrue(unterminated.bytes.decodeToString().endsWith("END:VCALENDAR"))
        val mixed = AnonymizerSamples.invite.replaceFirst("\r\n", "\n")
        val oneLf = anonymize(mixed)
        assertTrue(oneLf.isVerified, oneLf.summary)
        assertEquals(
            1,
            oneLf.bytes.decodeToString().count { it == '\n' } - oneLf.bytes.decodeToString().count { it == '\r' },
        )
    }

    @Test
    fun keepsLinesApartWhenLineEndingsChange() {
        val text = "BEGIN:VCALENDAR\n\r VERSION:2.0\nBEGIN:VEVENT\nSUMMARY:Тест\nEND:VEVENT\nEND:VCALENDAR\n"
        val source = ICal.parse(text)
        assertTrue(source.problemCount(ProblemCode.BareCr) > 0)
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        val output = ICal.parse(result.bytes)
        assertTrue(sameShape(source.components, output.components))
        assertEquals("2.0", output.components[0].property("VERSION")!!.value)
    }

    @Test
    fun keepsTheCharsetOfTheInput() {
        val text = AnonymizerSamples.schedule.replace("🎂", "")
        val windows1251 = encodeSingleByte(text, WINDOWS_1251)
        val result = Anonymizer(KEY, 0).anonymize(windows1251)
        assertTrue(result.isVerified, result.summary)
        val output = ICal.parse(result.bytes)
        assertEquals("windows-1251", output.charset)
        assertTrue(output.problemCount(ProblemCode.DetectedCharset) > 0)
        assertFalse(result.bytes.decodeToString().startsWith(Char(0xFEFF).toString()))
        val utf16 = byteArrayOf(-1, -2) + utf16le(AnonymizerSamples.invite)
        val wide = Anonymizer(KEY, 0).anonymize(utf16)
        assertTrue(wide.isVerified, wide.summary)
        assertEquals("UTF-16LE", ICal.parse(wide.bytes).charset)
        assertContentEquals(byteArrayOf(-1, -2), wide.bytes.copyOf(2))
    }

    @Test
    fun neverWritesTheKeyOrTheSourceWordsIntoTheSummary() {
        for (sample in SAMPLES) {
            val result = anonymize(sample)
            val key = KEY.toHex()
            assertFalse(key in result.summary || key in result.bytes.decodeToString())
            assertFalse(result.bytes.toHex().contains(key))
            for (word in sensitiveWords(sample)) {
                if (word.length >= 4) assertFalse(word.lowercase() in result.summary.lowercase(), word)
            }
            assertFalse("key" in result.toString().lowercase())
        }
    }

    @Test
    fun leaksNoWordAddressUidOrLinkOfTheSamples() {
        for (sample in SAMPLES) {
            for (weeks in listOf(0, 3)) {
                val bytes = anonymize(sample, weeks = weeks).bytes
                val output = bytes.decodeToString().lowercase()
                for (word in sensitiveWords(sample, bytes)) {
                    assertFalse(word.lowercase() in output, "leaked word $word")
                }
                for (value in sensitiveValues(sample)) {
                    assertFalse(value.lowercase() in output, "leaked value")
                }
            }
        }
    }

    @Test
    fun keepsTheFoldingOfTheVendor() {
        val summary = "SUMMARY:Совещание по бюджету отдела продаж на следующий квартал"
        val bytes = summary.encodeToByteArray()
        val folded = bytes.copyOfRange(0, 41) + "\r\n\t".encodeToByteArray() + bytes.copyOfRange(41, 60) +
            "\r\n ".encodeToByteArray() + bytes.copyOfRange(60, bytes.size)
        val prefix = calendar("X-MARK:1").substringBefore("X-MARK:1").encodeToByteArray()
        val input = prefix + folded + "\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n".encodeToByteArray()
        assertTrue(bytes[41].toInt() and 0xC0 == 0x80, "the first fold splits a character")
        val result = Anonymizer(KEY, 0).anonymize(input)
        assertTrue(result.isVerified, result.summary)
        val sourceLine = ICal.parse(input).components[0].components[0].property("SUMMARY")!!
        val outputLine = ICal.parse(result.bytes).components[0].components[0].property("SUMMARY")!!
        val before = slice(input, sourceLine)
        val after = slice(result.bytes, outputLine)
        assertEquals(before.size, after.size)
        for (index in before.indices) {
            val structural = before[index] == '\r'.code.toByte() || before[index] == '\n'.code.toByte() ||
                before[index] == '\t'.code.toByte() || before[index] == ' '.code.toByte()
            if (structural) assertEquals(before[index], after[index], "byte $index")
        }
        assertEquals(before[41].toInt() and 0xC0, after[41].toInt() and 0xC0)
        assertNotEquals(before.decodeToString(), after.decodeToString())
    }

    @Test
    fun keepsAlarmUidsTiedToTheirAlarms() {
        val text = calendar(
            "BEGIN:VALARM",
            "UID:1C9E0A3B-5F2D-4C8A-9E7B-3A2F1D0C9B8A",
            "X-WR-ALARMUID:1C9E0A3B-5F2D-4C8A-9E7B-3A2F1D0C9B8A",
            "X-TITLE-HOLDER;X-TITLE=\"Кабинет\\n14 улица Лилий\":geo:48.86,2.37",
            "ACTION:AUDIO",
            "TRIGGER:-PT1H",
            "END:VALARM",
        )
        val alarm = ICal.parse(anonymize(text).bytes).components[0].components[0].components[0]
        assertEquals(alarm.property("UID")!!.value, alarm.property("X-WR-ALARMUID")!!.value)
        val title = alarm.property("X-TITLE-HOLDER")!!.parameter("X-TITLE")!!.value
        assertEquals("\\n", title.substring(7, 9))
        assertFalse("Лилий" in title)
    }

    @Test
    fun replacesLettersOfOtherScriptsWithinTheirClass() {
        val text = calendar(
            "SUMMARY:Συνάντηση 会議の議題 カレンダー اجتماع פגישה 회의 𠀀𠀁 ２０２６ ٢٠٢٦ Ǆemal ªµº",
            "DESCRIPTION:Ärger über Straße — Œuvre\\, ÿ Š 🇷🇺👍🏽",
        )
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        for ((source, output) in propertyPairs(text, result.bytes)) {
            if (source.name != "SUMMARY" && source.name != "DESCRIPTION") continue
            assertSameClasses(source.value, output.value, source.name)
            assertNotEquals(source.value, output.value)
            assertTrue("ªµº" in output.value || source.name == "DESCRIPTION")
        }
    }

    @Test
    fun mapsOtherAddressesAndTypedValues() {
        val text = calendar(
            "ATTENDEE;EMAIL=anna.petrova@firm-example.org:tel:+79161234567",
            "ATTENDEE;CN=Room:urn:uuid:5a1c2d3e-4f50-6172-8394-a5b6c7d8e9f0",
            "ATTENDEE:https://people-example.org/u/anna",
            "ORGANIZER:anna.petrova@firm-example.org",
            "CONTACT:no-reply",
            "REQUEST-STATUS:2.0;Успешно\\; подтверждено",
            "X-LINK;VALUE=URI:https://secret-example.org/a/b",
            "X-WHO;VALUE=CAL-ADDRESS:mailto:boss@firm-example.org",
            "STRUCTURED-DATA;VALUE=BINARY;ENCODING=BASE64:c2VjcmV0",
            "STRUCTURED-DATA;VALUE=TEXT:Секретный текст",
            "X-BLOB;ENCODING=BASE64:c2VjcmV0IHRleHQ=",
        )
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        val event = ICal.parse(result.bytes).components[0].components[0]
        val attendees = event.properties("ATTENDEE").map { it.value }
        assertTrue(attendees[0].matches(Regex("tel:u-[0-9a-f]{16}")), attendees[0])
        assertTrue(attendees[1].matches(Regex("urn:u-[0-9a-f]{16}")), attendees[1])
        assertTrue(attendees[2].matches(Regex("https://example\\.invalid/[0-9a-f]{16}")), attendees[2])
        val email = event.properties("ATTENDEE")[0].parameter("EMAIL")!!.value
        assertTrue(email.matches(Regex("u-[0-9a-f]{16}@example\\.invalid")), email)
        assertEquals(email, event.property("ORGANIZER")!!.value)
        assertEquals("2.0;", event.property("REQUEST-STATUS")!!.value.take(4))
        assertFalse("Успешно" in event.property("REQUEST-STATUS")!!.value)
        assertTrue(event.property("X-LINK")!!.value.startsWith("https://example.invalid/"))
        assertTrue(event.property("X-WHO")!!.value.startsWith("mailto:u-"))
        val data = event.properties("STRUCTURED-DATA")
        assertEquals(8, data[0].value.length)
        assertFalse("Секретный" in data[1].value)
        assertEquals(16, event.property("X-BLOB")!!.value.length)
        val contact = event.property("CONTACT")!!.value
        assertEquals(8, contact.length)
        assertEquals('-', contact[2])
        assertNotEquals("no-reply", contact)
    }

    @Test
    fun keepsOtherCharsetsAndLineEndings() {
        val text = AnonymizerSamples.invite.replace("«", "\"").replace("»", "\"").replace("—", "-")
        for (charset in listOf("KOI8-R", "windows-1252")) {
            val table = SingleByteCharsets.table(
                if (charset ==
                    "KOI8-R"
                ) {
                    InputCharset.Koi8R
                } else {
                    InputCharset.Windows1252
                },
            )
                .concatToString()
            val source = if (charset == "KOI8-R") text else FRENCH
            val bytes = encodeSingleByte(source, table)
            val result = Anonymizer(KEY, 0).anonymize(bytes)
            assertTrue(result.isVerified, "$charset: ${result.summary}")
            assertEquals(charset, ICal.parse(result.bytes).charset, result.summary)
        }
        val cr = anonymize(AnonymizerSamples.invite.replace("\r\n", "\r"))
        assertTrue(cr.isVerified, cr.summary)
        assertFalse('\n' in cr.bytes.decodeToString())
        val utf32 = byteArrayOf(0, 0, -2, -1) + utf32be(AnonymizerSamples.schedule)
        val wide = Anonymizer(KEY, 0).anonymize(utf32)
        assertTrue(wide.isVerified, wide.summary)
        assertEquals("UTF-32BE", ICal.parse(wide.bytes).charset)
        val mixed = AnonymizerSamples.invite.replaceFirst("\r\n", "\r")
        assertTrue(anonymize(mixed).isVerified)
    }

    @Test
    fun keepsHtmlEdgeCases() {
        val text = calendar(
            "X-ALT-DESC;FMTTYPE=text/html:<!DOCTYPE html><p data-x=Секрет id='Тайна'>Привет &#1048\\;&#x418; &" +
                " < b <br/><!-- незакрытый комментарий",
        )
        val result = anonymize(text)
        assertTrue(result.isVerified, result.summary)
        val value = ICal.parse(result.bytes).components[0].components[0].property("X-ALT-DESC")!!.value
        assertTrue(value.startsWith("<!DOCTYPE html><p data-x="), value)
        assertTrue("&#" in value && "&#x" in value && "<br/><!--" in value, value)
        for (word in listOf("Секрет", "Тайна", "Привет", "незакрытый", "комментарий")) assertFalse(word in value, word)
    }

    @Test
    fun keepsTheBlankSegmentBeforeARebuiltLine() {
        val input = "BEGIN:\n\n\t :mailto:".encodeToByteArray()
        val result = anonymizeBytes(input)
        assertTrue(result.isVerified, result.summary)
        val output = ICal.parse(result.bytes)
        assertEquals(result.components, output.components)
        assertTrue(sameShape(ICal.parse(input).components, output.components))
        assertTrue(result.bytes.decodeToString().startsWith("BEGIN:\n\n\t "))
    }

    @Test
    fun rejectsShortKeys() {
        assertFailsWith<IllegalArgumentException> { Anonymizer(ByteArray(15), 0) }
    }

    private fun anonymize(text: String, weeks: Int = 0) = Anonymizer(KEY, weeks).anonymize(text.encodeToByteArray())

    private fun anonymizeBytes(bytes: ByteArray) = Anonymizer(KEY, 0).anonymize(bytes)

    private fun propertyPairs(source: String, output: ByteArray): List<Pair<ICalProperty, ICalProperty>> {
        val left = properties(ICal.parse(source).components)
        val right = properties(ICal.parse(output).components)
        assertEquals(left.size, right.size)
        return left.zip(right)
    }

    private fun slice(bytes: ByteArray, property: ICalProperty): ByteArray {
        val span = property.span!!
        return bytes.copyOfRange(span.startOffset.toInt(), span.endOffset.toInt())
    }

    private fun assertSameClasses(source: String, output: String, name: String) {
        val before = codePoints(source)
        val after = codePoints(output)
        assertEquals(before.size, after.size, name)
        for (index in before.indices) {
            assertEquals(characterClass(before[index]), characterClass(after[index]), "$name at $index")
            assertEquals(utf8Length(before[index]), utf8Length(after[index]), "$name at $index")
        }
    }

    private fun dates(value: String): List<String> = value.split(',').map { it.substringBefore('/') }

    private fun weeklyOccurrences(calendar: ICalComponent): Int {
        val master = calendar.components("VEVENT").first { it.property("RRULE") != null }
        val start = master.property("DTSTART")!!.value
        val until = master.property("RRULE")!!.value.substringAfter("UNTIL=").take(8)
        val excluded = master.property("EXDATE")!!.value.split(',').map { it.take(8) }.toSet()
        var count = 0
        var day = daysOf(start)
        while (day <= daysOf(until)) {
            val date = civil(day)
            if (date !in excluded) count++
            day += 7
        }
        return count
    }

    private fun civil(day: Long): String {
        var year = 1970
        var rest = day
        while (true) {
            val length = if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 366 else 365
            if (rest < length) break
            rest -= length
            year++
        }
        var month = 1
        while (true) {
            val length = when (month) {
                2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
                4, 6, 9, 11 -> 30
                else -> 31
            }
            if (rest < length) break
            rest -= length
            month++
        }
        return "$year" + "$month".padStart(2, '0') + "${rest + 1}".padStart(2, '0')
    }

    private fun markup(html: String): List<String> {
        val tags = ArrayList<String>()
        var index = 0
        while (true) {
            val open = html.indexOf('<', index)
            if (open < 0) break
            val close = html.indexOf('>', open)
            val tag = html.substring(open, close + 1)
            tags += Regex("\"[^\"]*\"").replace(tag, "\"\"").let { if (it.startsWith("<!--")) "<!--" else it }
            index = close + 1
        }
        return tags
    }

    private fun utf16le(text: String): ByteArray {
        val out = ByteArray(text.length * 2)
        for (index in text.indices) {
            out[index * 2] = text[index].code.toByte()
            out[index * 2 + 1] = (text[index].code ushr 8).toByte()
        }
        return out
    }

    private fun utf32be(text: String): ByteArray {
        val points = codePoints(text)
        val out = ByteArray(points.size * 4)
        for (index in points.indices) {
            for (byte in 0 until 4) out[index * 4 + byte] = (points[index] ushr (24 - byte * 8)).toByte()
        }
        return out
    }

    private fun encodeSingleByte(text: String, table: String): ByteArray = ByteArray(text.length) { index ->
        val char = text[index]
        if (char.code < 0x80) char.code.toByte() else (table.indexOf(char) + 0x80).also { check(it >= 0x80) }.toByte()
    }

    private companion object {
        val KEY = ByteArray(32) { it.toByte() }
        val REPLACEMENT = Char(0xFFFD)
        val FRENCH = calendar(
            "SUMMARY:Pédiatre — Léa (vaccins)",
            "LOCATION:Cabinet du Dr Élodie Marchand\\n14 rue des Lilas\\, 75011 Paris",
            "DESCRIPTION:Apporter le carnet de santé et l'ordonnance\\; café à côté de l'hôpital.",
        )
        val SAMPLES = listOf(AnonymizerSamples.schedule, AnonymizerSamples.invite)
        val TEXT_PROPERTIES = setOf(
            "SUMMARY",
            "DESCRIPTION",
            "LOCATION",
            "COMMENT",
            "CONTACT",
            "CATEGORIES",
            "X-WR-CALNAME",
            "X-WR-CALDESC",
        )
        val STRUCTURAL = setOf(
            "VERSION", "PRODID", "CALSCALE", "METHOD", "STATUS", "CLASS", "TRANSP", "SEQUENCE", "PRIORITY", "DTSTART",
            "DTEND", "DTSTAMP", "CREATED", "LAST-MODIFIED", "RECURRENCE-ID", "RRULE", "EXDATE", "TRIGGER", "ACTION",
            "X-WR-TIMEZONE", "X-LIC-LOCATION", "X-MICROSOFT-CDO-BUSYSTATUS", "X-MICROSOFT-CDO-ALLDAYEVENT",
        )
        val STRUCTURAL_PARAMETERS = setOf("TZID", "VALUE", "RELATED", "RELTYPE", "LANGUAGE")
        const val WINDOWS_1251 =
            "ЂЃ‚ѓ„…†‡€‰Љ‹ЊЌЋЏђ‘’“”•–—�™љ›њќћџ ЎўЈ¤Ґ¦§Ё©Є«¬­®Ї°±Ііґµ¶·ё№є»јЅѕї" +
                "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя"
    }
}

internal fun properties(roots: List<ICalComponent>): List<ICalProperty> {
    val out = ArrayList<ICalProperty>()
    val stack = ArrayList(roots.asReversed())
    while (stack.isNotEmpty()) {
        val component = stack.removeAt(stack.lastIndex)
        out += component.properties
        stack.addAll(component.components.asReversed())
    }
    return out
}

internal fun calendar(vararg eventLines: String): String = (
    listOf(
        "BEGIN:VCALENDAR",
        "VERSION:2.0",
        "PRODID:-//Test//Anonymizer//EN",
        "BEGIN:VEVENT",
        "UID:test-1@example.org",
    ) +
        eventLines + listOf("END:VEVENT", "END:VCALENDAR")
    ).joinToString("\r\n", postfix = "\r\n")

internal fun codePoints(text: String): IntArray {
    val out = ArrayList<Int>()
    var index = 0
    while (index < text.length) {
        val char = text[index]
        if (char.isHighSurrogate() && index + 1 < text.length && text[index + 1].isLowSurrogate()) {
            out += 0x10000 + ((char.code - 0xD800) shl 10) + (text[index + 1].code - 0xDC00)
            index += 2
        } else {
            out += char.code
            index++
        }
    }
    return out.toIntArray()
}

internal fun characterClass(codePoint: Int): String {
    if (codePoint >= 0x10000) {
        return when (codePoint) {
            in 0x1F000..0x1FAFF -> "emoji"
            in 0x20000..0x3134F -> "han"
            else -> "supplementary-${codePoint / 128}"
        }
    }
    val char = codePoint.toChar()
    val case = if (char.isUpperCase()) "upper" else "lower"
    return when {
        char.isDigit() -> "digit"
        char in 'a'..'z' || char in 'A'..'Z' -> "latin-$case"
        !char.isLetter() -> "other-$codePoint"
        codePoint in 0xC0..0x24F -> "latin-extended-$case"
        codePoint in 0x400..0x52F -> "cyrillic-$case"
        codePoint in 0x370..0x3FF -> "greek-$case"
        codePoint in 0x3041..0x3096 -> "hiragana"
        codePoint in 0x30A1..0x30FA -> "katakana"
        codePoint in 0x3400..0x9FFF -> "han"
        codePoint in 0xAC00..0xD7A3 -> "hangul"
        else -> "letter-${codePoint / 128}"
    }
}

internal fun utf8Length(codePoint: Int): Int = when {
    codePoint < 0x80 -> 1
    codePoint < 0x800 -> 2
    codePoint < 0x10000 -> 3
    else -> 4
}

private val UNCLOSED_QUOTES = listOf(
    "BEGIN:\r;=\";SCHEMA=:",
    "BEGIN:\r;=\";SCHEMA=x:",
    "BEGIN:\r;=\";SCHEMA=x/y:z",
    "BEGIN:\r;=\";ALTREP=:",
    "BEGIN:\r;=\";DIR=:value",
    "BEGIN:\r;=\";SENT-BY=mailto\\:a@b:",
    "BEGIN:\r;=\";CN=mailto\\:a@b:value",
    "BEGIN:\rX;=\";SCHEMA=:",
    "BEGIN:\rATTENDEE;=\";SCHEMA=x:mailto:a@b",
    "BEGIN:\rUID;=\";SCHEMA=:secret",
    "BEGIN:\rURL;=\";ALTREP=:https://example.com/secret",
    "BEGIN:\r;=\";SCHEMA=secret/secret:",
    "BEGIN:\rSUMMARY;=\";ALTREP=https\\://example.com/secret;CN=secret:secret",
    "BEGIN:\rATTENDEE;=\";DIR=secret/secret;SENT-BY=secret@example.com:mailto:secret@example.com",
    "BEGIN:X\r\nATTENDEE;A=\";SCHEMA=x/y;CN=\"z\":mailto:a@b\r\nEND:X\r\n",
)

private val LEAKS = listOf(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Zorgwin Clinic//Oncology Planner//EN",
    "BEGIN:VTIMEZONE",
    "TZID:Zorgwin Home Zone",
    "COMMENT:Zorgwin lives at 12 Quillfeather Lane",
    "X-ZORG-NOTE:Zorgwin phone 5550199",
    "BEGIN:STANDARD",
    "DTSTART:19700101T000000",
    "TZOFFSETFROM:+0100",
    "TZOFFSETTO:+0100",
    "TZNAME:Quillfeather",
    "COMMENT:Quillfeather standard",
    "END:STANDARD",
    "END:VTIMEZONE",
    "BEGIN:VEVENT",
    "UID:zorgwin-1@quillfeather.example",
    "DTSTAMP:20260101T000000Z",
    "DTSTART;TZID=Zorgwin Home Zone:20260105T100000",
    "DTEND:Zorgwin call back 5550199",
    "SUMMARY:Zorgwin chemotherapy",
    "CLASS:Zorgwin oncology visit",
    "STATUS:Zorgwin diagnosis",
    "COLOR:Zorgwin",
    "RRULE:FREQ=WEEKLY;X-NOTE=Zorgwin",
    "EXDATE:Zorgwin vacation",
    "RECURRENCE-ID:Zorgwin",
    "PRIORITY:Zorgwin",
    "TRIGGER:Zorgwin",
    "ATTENDEE;ROLE=Zorgwin-boss;LANGUAGE=Zorgwin;SCHEDULE-STATUS=Zorgwin:mailto:zorgwin@quillfeather.example",
    "BEGIN:VALARM",
    "ACTION:Zorgwin shout",
    "TRIGGER:-PT5M",
    "DESCRIPTION:Zorgwin pill",
    "END:VALARM",
    "END:VEVENT",
    "BEGIN:VEVENT",
    "UID:2@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART;TZID=Europe/Berlin:20260105T090000",
    "STATUS:CONFIRMED",
    "CLASS:PRIVATE",
    "TRANSP:OPAQUE",
    "PRIORITY:5",
    "COLOR:teal",
    "RRULE:FREQ=WEEKLY;COUNT=3",
    "ATTENDEE;PARTSTAT=ACCEPTED;ROLE=REQ-PARTICIPANT;LANGUAGE=de-CH;SCHEDULE-STATUS=2.0:mailto:a@example.com",
    "BEGIN:VALARM",
    "ACTION:DISPLAY",
    "TRIGGER:-PT5M",
    "DESCRIPTION:x",
    "END:VALARM",
    "END:VEVENT",
    "BEGIN:VEVENT",
    "UID:3@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T090000Z",
    "SUMMARY:plain",
    "END:VEVENT",
    "BEGIN:VTIMEZONE",
    "TZID:Europe/Berlin",
    "BEGIN:STANDARD",
    "DTSTART:19701025T030000",
    "TZOFFSETFROM:+0200",
    "TZOFFSETTO:+0100",
    "TZNAME:CET",
    "END:STANDARD",
    "END:VTIMEZONE",
    "END:VCALENDAR",
).joinToString("") { "$it\r\n" }
