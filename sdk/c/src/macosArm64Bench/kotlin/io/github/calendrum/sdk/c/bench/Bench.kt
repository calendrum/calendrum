package io.github.calendrum.sdk.c.bench

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.sdk.c.occurrencesJson
import io.github.calendrum.sdk.c.parseJson
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import kotlin.time.Instant
import kotlin.time.TimeSource

fun main(args: Array<String>) {
    val bytes = readFile(args[0])
    val rounds = args[1].toInt()
    fun parse() = parseJson(bytes).first.length
    fun occurrences() = occurrencesJson(bytes, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", "Europe/Berlin").length
    fun bare(): Int {
        val calendar = ICal.parse(bytes).calendars.first()
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TzData.resolver,
            unknown = UnknownZonePolicy.Utc,
        )
        val options = OccurrenceOptions.builder().zones(zones).build()
        return calendar.occurrences(FROM, TO, options).count()
    }
    repeat(rounds / 10 + 1) { parse() }
    var sink = 0
    var mark = TimeSource.Monotonic.markNow()
    repeat(rounds) { sink += parse() }
    val parse = mark.elapsedNow() / rounds
    mark = TimeSource.Monotonic.markNow()
    repeat(rounds) { sink += occurrences() }
    val occurrences = mark.elapsedNow() / rounds
    mark = TimeSource.Monotonic.markNow()
    repeat(rounds) { sink += bare() }
    val bare = mark.elapsedNow() / rounds
    println("kotlin parseJson ${parse.inWholeNanoseconds / 1000.0} us")
    println("kotlin occurrencesJson ${occurrences.inWholeNanoseconds / 1000.0} us")
    println("kotlin occurrences without JSON ${bare.inWholeNanoseconds / 1000.0} us ($sink)")
}

private val FROM = Instant.parse("2026-01-01T00:00:00Z")
private val TO = Instant.parse("2027-01-01T00:00:00Z")

private fun readFile(path: String): ByteArray {
    val file = fopen(path, "rb") ?: error("cannot open $path")
    try {
        fseek(file, 0, SEEK_END)
        val size = ftell(file).toInt()
        fseek(file, 0, SEEK_SET)
        return memScoped {
            val buffer = allocArray<ByteVar>(size)
            check(fread(buffer, 1u, size.toULong(), file).toInt() == size)
            buffer.readBytes(size)
        }
    } finally {
        fclose(file)
    }
}
