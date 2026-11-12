package io.github.calendrum.value

import io.github.calendrum.ProblemCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class Recorder : ValueProblems {
    val codes = ArrayList<String>()
    val offsets = ArrayList<Int>()

    override fun report(code: ProblemCode, offset: Int) {
        codes.add(code.id)
        offsets.add(offset)
    }

    fun entries(): List<String> = codes.indices.map { "${codes[it]}@${offsets[it]}" }
}

internal class Decoded<T>(val value: T?, val problems: List<String>) {
    val result: T get() = assertNotNull(value)
}

internal fun <T> lenient(decode: ValueDecoder.() -> T?): Decoded<T> = run(false, false, decode)

internal fun <T> strict(decode: ValueDecoder.() -> T?): Decoded<T> = run(true, false, decode)

internal fun <T> run(strict: Boolean, nextMinute: Boolean, decode: ValueDecoder.() -> T?): Decoded<T> {
    val recorder = Recorder()
    val value = ValueDecoder(strict, nextMinute, recorder).decode()
    return Decoded(value, recorder.entries())
}

internal fun <T> assertAccepted(expected: String, decoded: Decoded<T>, encode: (T) -> String, vararg problems: String) {
    assertEquals(problems.toList(), decoded.problems)
    assertEquals(expected, encode(assertNotNull(decoded.value)))
}

internal fun <T> assertRejected(decoded: Decoded<T>, vararg problems: String) {
    assertNull(decoded.value)
    assertEquals(problems.toList(), decoded.problems)
}

internal const val V001 = "ICAL-V001"
internal const val V002 = "ICAL-V002"

internal fun dateTime(value: String, tzid: String? = null): DateTimeValue =
    assertNotNull(ValueDecoder(false, false, null).decodeDateTime(value, 0, value.length, tzid))
