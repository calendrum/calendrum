package io.github.calendrum.value

import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalDoubleDifferentialTest {
    @Test
    fun randomDecimalsMatchTheJdk() {
        val random = Random(20261016)
        repeat(200_000) {
            val integerDigits = random.nextInt(0, 25)
            val fractionDigits = random.nextInt(0, 30)
            val text = buildString {
                if (random.nextBoolean()) append('-')
                if (integerDigits == 0) append('0')
                repeat(integerDigits) { append('0' + random.nextInt(10)) }
                if (fractionDigits > 0) {
                    append('.')
                    repeat(fractionDigits) { append('0' + random.nextInt(10)) }
                }
            }
            assertSame(text)
        }
    }

    @Test
    fun halfwayPointsAndTheirNeighboursRoundLikeTheJdk() {
        val random = Random(42)
        repeat(20_000) {
            val bits = random.nextLong() and Long.MAX_VALUE
            val value = Double.fromBits(bits)
            if (value.isNaN() || value.isInfinite()) return@repeat
            val next = Math.nextUp(value)
            if (next.isInfinite()) return@repeat
            val middle = BigDecimal(value).add(BigDecimal(next)).divide(BigDecimal(2))
            val unit = BigDecimal.ONE.movePointLeft(middle.scale() + 5)
            for (candidate in listOf(middle, middle.add(unit), middle.subtract(unit))) {
                if (candidate.signum() < 0) continue
                assertSame(candidate.toPlainString())
            }
        }
    }

    @Test
    fun geoRangeDecimalsMatchTheJdk() {
        val random = Random(7)
        repeat(100_000) {
            val degrees = random.nextInt(0, 181)
            val fraction = (0 until random.nextInt(1, 18)).joinToString("") { (random.nextInt(10)).toString() }
            assertSame("$degrees.$fraction")
            assertSame("-$degrees.$fraction")
        }
    }

    @Test
    fun extremesMatchTheJdk() {
        val cases = listOf(
            "0", "-0", "0.0", "00.000", "1", "9007199254740993", "9007199254740992.5", "0.1", "0.3",
            "179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000",
            "1" + "0".repeat(400),
            "0." + "0".repeat(320) + "1",
            "0." + "0".repeat(323) + "2470328229206232720882538",
            "0." + "0".repeat(323) + "247032822920623272088253",
            "0." + "0".repeat(307) + "22250738585072011360574097967091319759348195463516456480234261098912" +
                "53",
            "0." + "0".repeat(400) + "5",
            "12345678901234567890123456789012345678901234567890.123456789",
            "1." + "0".repeat(1200) + "1",
            "2." + "9".repeat(2000),
            "8" + "9".repeat(900) + ".5",
        )
        for (case in cases) assertSame(case)
        val mid = BigDecimal(Double.MIN_VALUE).divide(BigDecimal(2))
        assertSame(mid.toPlainString())
        assertSame(mid.add(BigDecimal.ONE.movePointLeft(mid.scale() + 3)).toPlainString())
        val overflow = BigDecimal(Double.MAX_VALUE).add(BigDecimal(Math.ulp(Double.MAX_VALUE)).divide(BigDecimal(2)))
        assertSame(overflow.toPlainString())
        assertSame(overflow.subtract(BigDecimal.ONE).toPlainString())
    }

    private fun assertSame(text: String) {
        val expected = java.lang.Double.parseDouble(text)
        val actual = decimalToDouble(text)
        assertEquals(expected.toRawBits(), actual.toRawBits(), "$text: expected $expected, got $actual")
    }
}
