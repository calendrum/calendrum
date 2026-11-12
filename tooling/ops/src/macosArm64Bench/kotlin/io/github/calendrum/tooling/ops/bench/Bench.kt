package io.github.calendrum.tooling.ops.bench

import io.github.calendrum.ICal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.syntax.ParseOptions
import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.JsonPrimitive
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import kotlin.time.Instant
import kotlin.time.TimeSource

// A response on the input of the C facade spike, bench.kexe <file> <rounds> <calls>: the median of the calls of a round,
// and the median of the rounds. `bare` is the library without the operation around it.
public fun main(args: Array<String>) {
    val ics = readFile(args[0]).decodeToString()
    val rounds = args[1].toInt()
    val calls = args[2].toInt()
    val field = JsonPrimitive(ics).toString()
    val window = """"from":"2026-01-01T00:00:00Z","to":"2027-01-01T00:00:00Z""""
    val expand = """{"ics":$field,$window,"zone":"Europe/Berlin","limit":100000}"""
    val inspect = """{"ics":$field}"""
    val bytes = ics.encodeToByteArray()
    var sink = 0
    fun measure(name: String, block: () -> Int) {
        val medians = ArrayList<Double>()
        repeat(rounds) {
            val times = DoubleArray(calls)
            for (call in 0 until calls) {
                val start = TimeSource.Monotonic.markNow()
                sink += block()
                times[call] = start.elapsedNow().inWholeNanoseconds / 1_000_000.0
            }
            times.sort()
            medians += times[calls / 2]
        }
        medians.sort()
        println("$name: median ${medians[rounds / 2]} ms, rounds $medians")
    }
    measure("bare") {
        val calendar = ICal.parser(ParseOptions.Untrusted).parse(bytes).calendars.first()
        val zones = TimeZoneResolvers.chain(
            TimeZoneResolvers.vtimezone(calendar),
            TzData.resolver,
            unknown = UnknownZonePolicy.Utc,
        )
        val options = OccurrenceOptions.builder().zones(zones).floatingZone(TimeZone.UTC).build()
        calendar.occurrences(FROM, TO, options).count()
    }
    measure("expand") { Ops.call("expand", expand).length }
    measure("inspect") { Ops.call("inspect", inspect).length }
    println("response of expand: ${Ops.call("expand", expand).encodeToByteArray().size} bytes; $sink")
}

private val FROM = Instant.parse("2026-01-01T00:00:00Z")
private val TO = Instant.parse("2027-01-01T00:00:00Z")

@OptIn(ExperimentalForeignApi::class)
private fun readFile(path: String): ByteArray {
    val file = checkNotNull(fopen(path, "rb")) { "cannot open the file" }
    fseek(file, 0, SEEK_END)
    val size = ftell(file).toInt()
    fseek(file, 0, SEEK_SET)
    val bytes = ByteArray(size)
    bytes.usePinned { fread(it.addressOf(0), 1u, size.convert(), file) }
    fclose(file)
    return bytes
}
