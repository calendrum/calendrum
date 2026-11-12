package io.github.calendrum.tooling.ops

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlin.time.Instant

// Writes JSON straight into text. A tree of kotlinx-serialization between the data and the text made a response of the
// C facade spike 3 to 9 times slower than the work itself.
internal class JsonOut {
    // A buffer of its own: the appends of StringBuilder were the largest part of a response on Native, a check of the
    // capacity per character.
    private var buffer = CharArray(INITIAL_CAPACITY)
    private var size = 0
    private var needsComma = false

    // UTF-8 beyond one byte per character, so that the size of the response needs no second pass.
    private var extraBytes = 0L

    val utf8Size: Long get() = size + extraBytes

    override fun toString(): String = buffer.concatToString(0, size)

    fun beginObject() {
        separate()
        put('{')
        needsComma = false
    }

    fun endObject() {
        put('}')
        needsComma = true
    }

    fun beginArray() {
        separate()
        put('[')
        needsComma = false
    }

    fun endArray() {
        put(']')
        needsComma = true
    }

    // Names are constants of this module: ASCII that needs no escaping.
    fun name(name: String) {
        room(name.length + NAME_OVERHEAD)
        val target = buffer
        var at = size
        if (needsComma) target[at++] = ','
        target[at++] = '"'
        for (index in name.indices) target[at++] = name[index]
        target[at++] = '"'
        target[at++] = ':'
        size = at
        needsComma = false
    }

    fun value(value: String) {
        room(value.length + 3)
        if (needsComma) buffer[size++] = ','
        string(value)
        needsComma = true
    }

    fun value(value: Int) = value(value.toLong())

    // Int.toString goes through the C library on Native, which shows in a list of thousands.
    fun value(value: Long) {
        room(MAX_LONG_LENGTH)
        if (needsComma) buffer[size++] = ','
        if (value < 0) {
            put(value.toString())
        } else {
            var digits = 1
            var rest = value / 10
            while (rest != 0L) {
                digits++
                rest /= 10
            }
            var at = size + digits
            size = at
            rest = value
            do {
                buffer[--at] = '0' + (rest % 10).toInt()
                rest /= 10
            } while (rest != 0L)
        }
        needsComma = true
    }

    fun value(value: Boolean) {
        separate()
        put(if (value) "true" else "false")
        needsComma = true
    }

    /** Writes the moment as `2026-09-01T08:00:00Z`, without the cost of `Instant.toString`. */
    fun value(instant: Instant) {
        val seconds = instant.epochSeconds
        if (instant.nanosecondsOfSecond != 0 || seconds < FIRST_SECOND || seconds > LAST_SECOND) {
            value(instant.toString())
            return
        }
        room(DATE_TIME_OVERHEAD)
        if (needsComma) buffer[size++] = ','
        buffer[size++] = '"'
        dateTime(seconds)
        buffer[size++] = 'Z'
        buffer[size++] = '"'
        needsComma = true
    }

    /** Writes the local time that is [localSeconds] after the epoch on the same clock, as `2026-09-01T10:00:00`. */
    fun localValue(localSeconds: Long, fallback: () -> String) {
        if (localSeconds < FIRST_SECOND || localSeconds > LAST_SECOND) {
            value(fallback())
            return
        }
        room(DATE_TIME_OVERHEAD)
        if (needsComma) buffer[size++] = ','
        buffer[size++] = '"'
        dateTime(localSeconds)
        buffer[size++] = '"'
        needsComma = true
    }

    /** Writes a local time as `2026-09-01T10:00:00`, without the cost of the formats of kotlinx-datetime. */
    fun value(local: LocalDateTime) {
        val year = local.year
        if (year < 0 || year > LAST_YEAR || local.nanosecond != 0) {
            value(local.toString())
            return
        }
        room(DATE_TIME_OVERHEAD)
        if (needsComma) buffer[size++] = ','
        buffer[size++] = '"'
        date(year, local.month.number, local.day)
        buffer[size++] = 'T'
        time(local.hour, local.minute, local.second)
        buffer[size++] = '"'
        needsComma = true
    }

    /** Writes a date as `2026-09-01`. */
    fun value(date: LocalDate) {
        val year = date.year
        if (year < 0 || year > LAST_YEAR) {
            value(date.toString())
            return
        }
        room(DATE_TIME_OVERHEAD)
        if (needsComma) buffer[size++] = ','
        buffer[size++] = '"'
        date(year, date.month.number, date.day)
        buffer[size++] = '"'
        needsComma = true
    }

    /** Writes [json], which is already JSON. */
    fun raw(json: String) {
        separate()
        put(json)
        extraBytes += json.encodeToByteArray().size - json.length
        needsComma = true
    }

    fun field(name: String, value: String) {
        name(name)
        value(value)
    }

    fun field(name: String, value: Int) {
        name(name)
        value(value)
    }

    fun field(name: String, value: Long) {
        name(name)
        value(value)
    }

    fun field(name: String, value: Boolean) {
        name(name)
        value(value)
    }

    fun field(name: String, value: Instant) {
        name(name)
        value(value)
    }

    /** Writes the field when there is a value: an absent property is an absent field. */
    fun optional(name: String, value: String?) {
        if (value != null) field(name, value)
    }

    private fun separate() {
        if (needsComma) put(',')
    }

    private fun room(count: Int) {
        if (buffer.size - size >= count) return
        // Faster growth while it is small: a list of thousands of items would otherwise copy itself several times.
        val factor = if (buffer.size < FAST_GROWTH_BELOW) 4 else 2
        val capacity = maxOf(buffer.size * factor, size + count)
        buffer = buffer.copyOf(capacity)
    }

    private fun put(char: Char) {
        room(1)
        buffer[size++] = char
    }

    private fun put(text: String) {
        room(text.length)
        text.toCharArray(buffer, size)
        size += text.length
    }

    private fun string(value: String) {
        val length = value.length
        var target = buffer
        var at = size
        var extra = 0
        target[at++] = '"'
        var index = 0
        while (index < length) {
            val char = value[index]
            val code = char.code
            if (code >= 0x20 && code < 0x7F && char != '"' && char != '\\') {
                target[at++] = char
                index++
                continue
            }
            // DEL and C1 are written as \u00XX below: raw, they steer a terminal that shows the response.
            if (code >= 0xA0 && code < 0x800) {
                extra += 1
                target[at++] = char
                index++
                continue
            }
            if (code >= 0x800 && !char.isSurrogate()) {
                extra += 2
                target[at++] = char
                index++
                continue
            }
            if (char.isHighSurrogate() && index + 1 < length && value[index + 1].isLowSurrogate()) {
                extra += 2
                target[at++] = char
                target[at++] = value[index + 1]
                index += 2
                continue
            }
            size = at
            // An escape is longer than the character: room for it, the rest of the text and the closing quote.
            room(MAX_ESCAPE + length - index)
            escape(char)
            target = buffer
            at = size
            index++
        }
        target[at++] = '"'
        size = at
        extraBytes += extra
    }

    private fun escape(char: Char) {
        when (char) {
            '"' -> put("\\\"")

            '\\' -> put("\\\\")

            '\n' -> put("\\n")

            '\r' -> put("\\r")

            '\t' -> put("\\t")

            '\b' -> put("\\b")

            '\u000C' -> put("\\f")

            else -> if (char.isSurrogate()) {
                // A surrogate without its pair has no UTF-8 form.
                put('\uFFFD')
                extraBytes += 2
            } else {
                val code = char.code
                put("\\u00")
                put(HEX[code shr 4])
                put(HEX[code and 0xF])
            }
        }
    }

    // Civil date from days, from "chrono-compatible low-level date algorithms" by Howard Hinnant.
    private fun dateTime(seconds: Long) {
        val days = seconds.floorDiv(SECONDS_PER_DAY)
        val second = seconds.mod(SECONDS_PER_DAY).toInt()
        val shifted = days + 719_468
        val era = shifted.floorDiv(146_097L)
        val dayOfEra = (shifted - era * 146_097).toInt()
        val yearOfEra = (dayOfEra - dayOfEra / 1460 + dayOfEra / 36_524 - dayOfEra / 146_096) / 365
        val dayOfYear = dayOfEra - (365 * yearOfEra + yearOfEra / 4 - yearOfEra / 100)
        val shiftedMonth = (5 * dayOfYear + 2) / 153
        val day = dayOfYear - (153 * shiftedMonth + 2) / 5 + 1
        val month = if (shiftedMonth < 10) shiftedMonth + 3 else shiftedMonth - 9
        val year = yearOfEra + era.toInt() * 400 + (if (month <= 2) 1 else 0)
        date(year, month, day)
        buffer[size++] = 'T'
        time(second / 3600, second / 60 % 60, second % 60)
    }

    private fun date(year: Int, month: Int, day: Int) {
        two(year / 100)
        two(year % 100)
        buffer[size++] = '-'
        two(month)
        buffer[size++] = '-'
        two(day)
    }

    private fun time(hour: Int, minute: Int, second: Int) {
        two(hour)
        buffer[size++] = ':'
        two(minute)
        buffer[size++] = ':'
        two(second)
    }

    // The callers made room for the whole date and time.
    private fun two(value: Int) {
        buffer[size++] = '0' + value / 10
        buffer[size++] = '0' + value % 10
    }

    private companion object {
        const val SECONDS_PER_DAY = 86_400L

        // The years 0000 to 9999, the ones that have the four digits of the fixed form.
        const val FIRST_SECOND = -62_167_219_200L
        const val LAST_SECOND = 253_402_300_799L
        const val LAST_YEAR = 9999

        const val HEX = "0123456789abcdef"
        const val INITIAL_CAPACITY = 16 * 1024
        const val FAST_GROWTH_BELOW = 1024 * 1024

        // A comma, two quotes and a colon.
        const val NAME_OVERHEAD = 4

        // A comma, two quotes, 2026-09-01T08:00:00 and Z.
        const val DATE_TIME_OVERHEAD = 23

        // \u00XX, or the three bytes of a replacement character.
        const val MAX_ESCAPE = 6

        // A comma and -9223372036854775808.
        const val MAX_LONG_LENGTH = 21
    }
}
