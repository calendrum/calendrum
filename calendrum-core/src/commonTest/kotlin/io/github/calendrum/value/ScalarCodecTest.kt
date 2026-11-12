package io.github.calendrum.value

import io.github.calendrum.ProblemCode
import io.github.calendrum.Severity
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ScalarCodecTest {
    @Test
    fun booleans() {
        assertAccepted("TRUE", strict { decodeBoolean("TRUE", 0, 4) }, ::encodeBoolean)
        assertAccepted("FALSE", strict { decodeBoolean("false", 0, 5) }, ::encodeBoolean)
        assertAccepted("TRUE", strict { decodeBoolean("True", 0, 4) }, ::encodeBoolean)
        assertAccepted("TRUE", lenient { decodeBoolean(" TRUE ", 0, 6) }, ::encodeBoolean, "ICAL-V013@0")
        assertRejected(strict { decodeBoolean("TRUE ", 0, 5) }, "ICAL-V013@4", "$V001@4")
        assertRejected(strict { decodeBoolean("YES", 0, 3) }, "$V001@0")
        assertRejected(strict { decodeBoolean("", 0, 0) }, "$V001@0")
    }

    @Test
    fun integers() {
        for (value in listOf("1234567890", "-1234567890", "432109876", "0", "2147483647", "-2147483648")) {
            assertAccepted(value, strict { decodeInteger(value, 0, value.length) }, ::encodeInteger)
        }
        assertAccepted("1234567890", strict { decodeInteger("+1234567890", 0, 11) }, ::encodeInteger)
        assertAccepted("7", strict { decodeInteger("007", 0, 3) }, ::encodeInteger)
        assertAccepted("5", lenient { decodeInteger("5 ", 0, 2) }, ::encodeInteger, "ICAL-V013@1")
        assertRejected(strict { decodeInteger("2147483648", 0, 10) }, "$V002@0")
        assertRejected(strict { decodeInteger("-2147483649", 0, 11) }, "$V002@0")
        assertRejected(strict { decodeInteger("99999999999999999999", 0, 20) }, "$V002@0")
        assertRejected(strict { decodeInteger("", 0, 0) }, "$V001@0")
        assertRejected(strict { decodeInteger("+", 0, 1) }, "$V001@1")
        assertRejected(strict { decodeInteger("1.5", 0, 3) }, "$V001@0")
        assertRejected(strict { decodeInteger("--1", 0, 3) }, "$V001@1")
    }

    @Test
    fun floats() {
        for (value in listOf("1000000.0000001", "1.333", "-3.14", "37.386013", "-122.082932", "0", "007.50")) {
            assertAccepted(value, strict { decodeFloat(value, 0, value.length) }, ::encodeFloat)
        }
        assertAccepted("3.14", strict { decodeFloat("+3.14", 0, 5) }, ::encodeFloat)
        assertEquals(-3.14, strict { decodeFloat("-3.14", 0, 5) }.result.toDouble())
        assertAccepted("1.5", lenient { decodeFloat(" 1.5", 0, 4) }, ::encodeFloat, "ICAL-V013@0")
        assertRejected(strict { decodeFloat("1.", 0, 2) }, "$V001@2")
        assertRejected(strict { decodeFloat(".5", 0, 2) }, "$V001@0")
        assertRejected(strict { decodeFloat("1e5", 0, 3) }, "$V001@1")
        assertRejected(strict { decodeFloat("1,5", 0, 3) }, "$V001@1")
        assertRejected(strict { decodeFloat("-", 0, 1) }, "$V001@1")
        val value = strict { decodeFloat("1.0", 0, 3) }.result
        assertEquals(value, strict { decodeFloat("+1.0", 0, 4) }.result)
        assertEquals(value.hashCode(), FloatValue("1.0").hashCode())
        assertTrue(value != FloatValue("1.00"))
        assertEquals("FloatValue", value.toString())
    }

    @Test
    fun urisAndCalendarAddresses() {
        for (value in listOf(
            "mailto:jane_doe@example.com",
            "http://example.com/my-report.txt",
            "urn:uuid:1-2",
            "a+b.c-d:x",
        )) {
            assertAccepted(value, strict { decodeUri(value, 0, value.length) }, { it })
        }
        assertAccepted("Chord", lenient { decodeUri("Chord", 0, 5) }, { it }, "ICAL-V021@0")
        assertRejected(strict { decodeUri("Chord", 0, 5) }, "ICAL-V021@0", "$V001@0")
        assertAccepted("jane@example.com", lenient { decodeUri("jane@example.com", 0, 16) }, { it }, "ICAL-V021@0")
        assertAccepted("1x:y", lenient { decodeUri("1x:y", 0, 4) }, { it }, "ICAL-V021@0")
        assertAccepted("a_b:c", lenient { decodeUri("a_b:c", 0, 5) }, { it }, "ICAL-V021@0")
        assertAccepted("mailto:a@b", lenient { decodeUri(" mailto:a@b", 0, 11) }, { it }, "ICAL-V013@0")
        assertRejected(strict { decodeUri("", 0, 0) }, "$V001@0")
        assertEquals("jane@example.com", calAddressKey("MAILTO:Jane@Example.COM"))
        assertEquals("jane@example.com", calAddressKey("mailto:jane@example.com"))
        assertEquals("urn:uuid:x", calAddressKey("urn:uuid:X"))
        assertEquals("mail", calAddressKey("MAIL"))
    }

    @Test
    fun binary() {
        val rfc = "AAABAAEAEBAQAAEABAAoAQAAFgAAACgAAAAQAAAAIAAAAAEABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACAAAAAgIAA"
        assertAccepted(rfc, strict { decodeBinary(rfc, 0, rfc.length) }, ::encodeBinary)
        for (value in listOf("", "QQ==", "QUI=", "QUJD", "+/+/", "SGVsbG8sIHdvcmxkIQ==")) {
            assertAccepted(value, strict { decodeBinary(value, 0, value.length) }, ::encodeBinary)
        }
        assertContentEquals("Hello".encodeToByteArray(), strict { decodeBinary("SGVsbG8=", 0, 8) }.result.bytes)
        assertAccepted("QUJD", lenient { decodeBinary("QU\r\n JD", 0, 7) }, ::encodeBinary, "ICAL-V014@2")
        assertRejected(strict { decodeBinary("QU JD", 0, 5) }, "ICAL-V014@2", "$V001@2")
        assertAccepted("QQ==", lenient { decodeBinary("QQ", 0, 2) }, ::encodeBinary, "ICAL-V015@2")
        assertAccepted("QUI=", lenient { decodeBinary("QUI", 0, 3) }, ::encodeBinary, "ICAL-V015@3")
        assertRejected(strict { decodeBinary("QQ", 0, 2) }, "ICAL-V015@2", "$V001@2")
        assertAccepted("QQ==", strict { decodeBinary("QR==", 0, 4) }, ::encodeBinary)
        assertRejected(strict { decodeBinary("Q", 0, 1) }, "$V001@1")
        assertRejected(strict { decodeBinary("QQ=", 0, 3) }, "$V001@3")
        assertRejected(strict { decodeBinary("Q===", 0, 4) }, "$V001@3")
        assertRejected(strict { decodeBinary("QQ==QQ==", 0, 8) }, "$V001@4")
        assertRejected(strict { decodeBinary("QQ-_", 0, 4) }, "$V001@2")
        val value = strict { decodeBinary("QQ==", 0, 4) }.result
        assertEquals(value, BinaryValue(byteArrayOf(0x41)))
        assertEquals(value.hashCode(), BinaryValue(byteArrayOf(0x41)).hashCode())
        assertEquals("BinaryValue(size=1)", value.toString())
    }

    @Test
    fun text() {
        val rfc = "Project XYZ Final Review\\nConference Room - 3B\\nCome Prepared."
        val decoded = strict { decodeText(rfc, 0, rfc.length) }
        assertEquals("Project XYZ Final Review\nConference Room - 3B\nCome Prepared.", decoded.result)
        assertEquals(rfc, encodeText(decoded.result))
        assertEquals("C:\\dir\\\nnext", strict { decodeText("C:\\\\dir\\\\\\nnext", 0, 15) }.result)
        assertEquals("a\nb;c,d", strict { decodeText("a\\Nb\\;c\\,d", 0, 10) }.result)
        assertAccepted("a\\\\xb", lenient { decodeText("a\\xb", 0, 4) }, ::encodeText, "ICAL-V023@1")
        assertAccepted("a\\\\", lenient { decodeText("a\\", 0, 2) }, ::encodeText, "ICAL-V023@1")
        assertRejected(strict { decodeText("a\\xb", 0, 4) }, "ICAL-V023@1", "$V001@1")
    }

    @Test
    fun textLists() {
        val categories = strict { decodeTextList("APPOINTMENT,EDUCATION", 0, 21) }
        assertEquals(listOf("APPOINTMENT", "EDUCATION"), categories.result)
        assertAccepted("EASEL,PROJECTOR,VCR", strict { decodeTextList("EASEL,PROJECTOR,VCR", 0, 19) }, ::encodeTextList)
        assertEquals(listOf("a,b", "c\\d", "", "e"), strict { decodeTextList("a\\,b,c\\\\d,,e", 0, 12) }.result)
        assertEquals(listOf(""), strict { decodeTextList("", 0, 0) }.result)
        assertEquals(listOf("", ""), strict { decodeTextList(",", 0, 1) }.result)
        assertAccepted("a\\\\x,b", lenient { decodeTextList("a\\x,b", 0, 5) }, ::encodeTextList, "ICAL-V023@1")
        assertRejected(strict { decodeTextList("a,b\\", 0, 4) }, "ICAL-V023@3", "$V001@3")
        assertEquals(listOf("b"), strict { decodeTextList("a,b,c", 2, 3) }.result)
    }

    @Test
    fun problemCodesAreSequentialAndValueFree() {
        val codes = listOf(
            ProblemCode.InvalidValue,
            ProblemCode.OutOfRange,
            ProblemCode.RecurWithoutFreq,
            ProblemCode.LeapSecond,
            ProblemCode.ExtendedIsoForm,
            ProblemCode.FractionalSeconds,
            ProblemCode.DateWithTime,
            ProblemCode.DateAsDateTime,
            ProblemCode.DurationWithoutTimeDesignator,
            ProblemCode.DurationSkipsMinutes,
            ProblemCode.UtcOffsetWithColon,
            ProblemCode.NegativeZeroOffset,
            ProblemCode.Whitespace,
            ProblemCode.Base64Whitespace,
            ProblemCode.Base64Padding,
            ProblemCode.EmptyItem,
            ProblemCode.CountAndUntil,
            ProblemCode.RepeatedRulePart,
            ProblemCode.RulePartNotAllowed,
            ProblemCode.UnknownRulePart,
            ProblemCode.UriWithoutScheme,
            ProblemCode.PeriodNotPositive,
            ProblemCode.UnknownEscape,
            ProblemCode.TzidWithUtc,
            ProblemCode.ValueTypeNotAllowed,
            ProblemCode.ValueTypeNotDeclared,
            ProblemCode.NotUtc,
            ProblemCode.ParameterNotAllowed,
        )
        assertEquals(codes, ProblemCode.known.drop(ProblemCode.SYNTAX_COUNT).take(codes.size))
        val model = ProblemCode.known.drop(ProblemCode.SYNTAX_COUNT + codes.size).take(1)
        assertEquals(listOf(ProblemCode.MissingRequiredField), model)
        for ((index, code) in codes.withIndex()) {
            val number = index + 1
            assertEquals("ICAL-V0" + (if (number < 10) "0" else "") + number, code.id)
            assertSame(code, ProblemCode.of(code.id))
            assertSame(code, ProblemCode.of(code.id.lowercase()))
            assertEquals(ProblemCode.SYNTAX_COUNT + index, code.index)
            assertEquals(if (number <= 3) Severity.Error else Severity.Warning, code.severity)
            assertFalse(code.oncePerInput)
            assertFalse(code.bypassesLimit)
            assertTrue(code.message.isNotEmpty())
        }
        assertEquals("value does not conform to its type, no value decoded", ProblemCode.InvalidValue.message)
        assertEquals("parameter does not apply to this value, ignored", ProblemCode.ParameterNotAllowed.message)
    }

    @Test
    fun facadeDispatchesEveryType() {
        val samples = mapOf(
            ValueCodecs.BINARY to "QUJD",
            ValueCodecs.BOOLEAN to "TRUE",
            ValueCodecs.URI to "mailto:a@example.com",
            ValueCodecs.DATE to "20260901",
            ValueCodecs.DATE_TIME to "20260901T100000",
            ValueCodecs.DURATION to "-PT15M",
            ValueCodecs.FLOAT to "1.5",
            ValueCodecs.INTEGER to "42",
            ValueCodecs.PERIOD to "20260901T100000/PT1H",
            ValueCodecs.RECUR to "FREQ=WEEKLY;BYDAY=MO",
            ValueCodecs.TEXT to "a\\,b",
            ValueCodecs.TEXT_LIST to "a,b",
            ValueCodecs.TIME to "100000",
            ValueCodecs.UTC_OFFSET to "+0300",
            ValueCodecs.DATE_LIST to "20260901,20260902",
            ValueCodecs.DATE_TIME_LIST to "20260901T100000,20260902T100000",
            ValueCodecs.PERIOD_LIST to "20260901T100000/PT1H,20260902T100000/20260902T110000",
        )
        assertEquals(ValueCodecs.TYPE_COUNT, samples.size)
        val recorder = Recorder()
        val codecs = ValueCodecs(true, false, recorder)
        for ((type, value) in samples) {
            val decoded = codecs.decode(type, value, 0, value.length, "Europe/Moscow")!!
            assertEquals(value, codecs.encode(type, decoded), "type $type")
        }
        assertEquals(emptyList(), recorder.entries())
        assertEquals(null, ValueCodecs(true, false, null).decode(ValueCodecs.DATE, "x", 0, 1, null))
        assertFailsWith<IllegalArgumentException> { codecs.decode(ValueCodecs.TYPE_COUNT, "", 0, 0, null) }
        assertFailsWith<IllegalArgumentException> { codecs.encode(-1, "") }
    }
}
