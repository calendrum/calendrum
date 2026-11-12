package io.github.calendrum.value

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValueRoundTripTest {
    @Test
    fun decodedValuesRoundTripInBothModes() {
        val random = Random(20261015)
        var decodedCount = 0
        for (iteration in 0 until ITERATIONS) {
            val type = random.nextInt(ValueCodecs.TYPE_COUNT)
            val value = mutate(random, SEEDS.getValue(type).let { it[random.nextInt(it.size)] })
            for (strict in listOf(false, true)) {
                val recorder = Recorder()
                val codecs = ValueCodecs(strict, random.nextBoolean(), recorder)
                val tzid = if (random.nextBoolean()) "Europe/Moscow" else null
                val decoded = codecs.decode(type, value, 0, value.length, tzid)
                assertTrue(recorder.offsets.all { it in 0..value.length }, "offsets for '$value'")
                if (decoded == null) {
                    assertTrue(recorder.codes.any { it == V001 || it == V002 || it == "ICAL-V003" }, "'$value'")
                    continue
                }
                if (strict) assertTrue(recorder.codes.none { it == V001 }, "'$value'")
                decodedCount += 1
                val encoded = codecs.encode(type, decoded)
                val again = ValueCodecs(false, false, null).decode(type, encoded, 0, encoded.length, tzid)
                assertEquals(decoded, again, "type $type '$value' -> '$encoded'")
                assertEquals(encoded, codecs.encode(type, again!!), "type $type '$value'")
            }
        }
        assertTrue(decodedCount > ITERATIONS / 4, "decoded $decodedCount")
    }

    private fun mutate(random: Random, seed: String): String {
        val out = StringBuilder(seed)
        repeat(random.nextInt(3)) {
            if (out.isEmpty()) return@repeat
            val index = random.nextInt(out.length)
            when (random.nextInt(4)) {
                0 -> out.deleteAt(index)

                1 -> out.insert(index, NOISE[random.nextInt(NOISE.length)])

                2 -> {
                    out[index] = NOISE[random.nextInt(NOISE.length)]
                }

                else -> out.insert(index, out.substring(index, minOf(out.length, index + random.nextInt(1, 5))))
            }
        }
        return out.toString()
    }

    private companion object {
        const val ITERATIONS = 20_000
        const val NOISE = "0123456789TZtz+-:.,;=/ PWDHMSLX\\aQ"
        val SEEDS = mapOf(
            ValueCodecs.BINARY to listOf("QUJD", "QQ==", "QUI", "SGVs bG8="),
            ValueCodecs.BOOLEAN to listOf("TRUE", "false", " TRUE"),
            ValueCodecs.URI to listOf("mailto:a@b", "Chord", "http://x/y"),
            ValueCodecs.DATE to listOf("20260901", "2026-09-01", "20240229T100000"),
            ValueCodecs.DATE_TIME to
                listOf("20260901T100000Z", "2026-09-01T10:00:00.5Z", "20161231T235960Z", "20260901"),
            ValueCodecs.DURATION to listOf("P15DT5H0M20S", "P7W", "-PT15M", "P90M", "PT1H30S"),
            ValueCodecs.FLOAT to listOf("1.5", "-3.14", "+0"),
            ValueCodecs.INTEGER to listOf("42", "-2147483648", "+7"),
            ValueCodecs.PERIOD to listOf("19970101T180000Z/PT5H30M", "19970101T180000Z/19970102T070000Z"),
            ValueCodecs.RECUR to listOf(
                "FREQ=MONTHLY;BYDAY=1SU,-1SU;COUNT=10",
                "FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO;UNTIL=20261231",
                "RSCALE=HEBREW;FREQ=YEARLY;BYMONTH=5L;SKIP=FORWARD;X-A=b",
                "FREQ=DAILY;COUNT=2;UNTIL=20261231T000000Z;;",
            ),
            ValueCodecs.TEXT to listOf("a\\nb\\,c", "a\\xb"),
            ValueCodecs.TEXT_LIST to listOf("a,b\\,c", "a\\;b,,c"),
            ValueCodecs.TIME to listOf("100000Z", "10:00:00", "235960"),
            ValueCodecs.UTC_OFFSET to listOf("+0300", "-02:30", "+023017", "-0000"),
            ValueCodecs.DATE_LIST to listOf("20260901,20260902", "20260901,,"),
            ValueCodecs.DATE_TIME_LIST to listOf("20260901T100000Z,20260902T100000Z", "20260901T100000,bad"),
            ValueCodecs.PERIOD_LIST to listOf("19970308T160000Z/PT3H,19970308T200000Z/PT1H"),
        )
    }
}
