package io.github.calendrum.sdk.c

import io.github.calendrum.ICal
import io.github.calendrum.jcal.JCal
import io.github.calendrum.model.calendars
import io.github.calendrum.occurrence.OccurrenceOptions
import io.github.calendrum.occurrence.occurrences
import io.github.calendrum.tz.ICalTimeZone
import io.github.calendrum.tz.ResolveContext
import io.github.calendrum.tz.TimeZoneResolvers
import io.github.calendrum.tz.UnknownZonePolicy
import io.github.calendrum.tzdata.TzData
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import kotlin.native.CName
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

private const val OK = 0
private const val INVALID_ARGUMENT = 1
private const val INTERNAL_ERROR = 2

// Every exported function catches all exceptions: nothing is thrown across the C boundary. Output buffers are allocated
// with malloc and released by ical_free; on an error the buffer holds {"error": message}. Local times use calendrum-tzdata
// zones, not kotlinx-datetime, so the facade needs no system database.
@CName("ical_abi_version")
fun abiVersion(): Int = 1

@CName("ical_version")
fun version(outJson: CPointer<CPointerVar<ByteVar>>?, outLen: CPointer<ULongVar>?): Int = guarded(outJson, outLen) {
    buildJsonObject {
        put("abi", 1)
        put("library", LIBRARY_VERSION)
    }.toString()
}

@CName("ical_parse")
fun parse(
    ics: CPointer<ByteVar>?,
    len: ULong,
    outJcal: CPointer<CPointerVar<ByteVar>>?,
    outJcalLen: CPointer<ULongVar>?,
    outProblems: CPointer<CPointerVar<ByteVar>>?,
    outProblemsLen: CPointer<ULongVar>?,
): Int = guarded(outJcal, outJcalLen) {
    val (jcal, problems) = parseJson(bytesOf(ics, len))
    if (outProblems != null && outProblemsLen != null) write(problems, outProblems, outProblemsLen)
    jcal
}

@CName("ical_occurrences")
fun occurrences(
    ics: CPointer<ByteVar>?,
    len: ULong,
    fromUtc: CPointer<ByteVar>?,
    toUtc: CPointer<ByteVar>?,
    zone: CPointer<ByteVar>?,
    outJson: CPointer<CPointerVar<ByteVar>>?,
    outLen: CPointer<ULongVar>?,
): Int = guarded(outJson, outLen) {
    occurrencesJson(
        bytesOf(ics, len),
        requireNotNull(fromUtc) { "from is null" }.toKString(),
        requireNotNull(toUtc) { "to is null" }.toKString(),
        zone?.toKString(),
    )
}

@CName("ical_free")
fun release(data: CPointer<ByteVar>?) {
    free(data)
}

internal fun parseJson(ics: ByteArray): Pair<String, String> {
    val result = ICal.parse(ics)
    val problems = buildJsonArray {
        for (problem in result.problems) {
            addJsonObject {
                put("code", problem.code.id)
                put("severity", problem.severity.name)
                put("message", problem.message)
                put("line", problem.span.line)
                put("column", problem.span.column)
            }
        }
    }
    return JCal.encodeToString(result.components) to problems.toString()
}

internal fun occurrencesJson(ics: ByteArray, fromUtc: String, toUtc: String, zone: String?): String {
    val from = Instant.parse(fromUtc)
    val to = Instant.parse(toUtc)
    val local = zone?.let(::zoneOf)
    val calendar = ICal.parse(ics).calendars.firstOrNull()
    return buildJsonArray {
        if (calendar != null) {
            val zones = TimeZoneResolvers.chain(
                TimeZoneResolvers.vtimezone(calendar),
                TzData.resolver,
                unknown = UnknownZonePolicy.Utc,
            )
            val options = OccurrenceOptions.builder().zones(zones).build()
            for (occurrence in calendar.occurrences(from, to, options)) {
                addJsonObject {
                    put("uid", occurrence.component.property("UID")?.value)
                    put("summary", occurrence.component.property("SUMMARY")?.value)
                    put("start", occurrence.start.toString())
                    put("end", occurrence.end.toString())
                    if (local != null) {
                        put("localStart", localOf(occurrence.start, local))
                        put("localEnd", localOf(occurrence.end, local))
                    }
                }
            }
        }
    }.toString()
}

private fun zoneOf(id: String): ICalTimeZone =
    requireNotNull(TzData.resolver.resolve(id, ResolveContext.Empty)) { "unknown time zone $id" }

private fun localOf(instant: Instant, zone: ICalTimeZone): String =
    (instant + zone.offsetAt(instant).totalSeconds.seconds).toLocalDateTime(TimeZone.UTC).toString()

// Copies the input into a ByteArray; inputs over 2 GiB are rejected.
private fun bytesOf(data: CPointer<ByteVar>?, len: ULong): ByteArray {
    require(len <= Int.MAX_VALUE.toULong()) { "input is larger than 2 GiB" }
    if (len == 0UL) return ByteArray(0)
    return requireNotNull(data) { "input is null" }.readBytes(len.toInt())
}

private inline fun guarded(
    outData: CPointer<CPointerVar<ByteVar>>?,
    outLen: CPointer<ULongVar>?,
    body: () -> String,
): Int {
    if (outData == null || outLen == null) return INVALID_ARGUMENT
    outData.pointed.value = null
    outLen.pointed.value = 0UL
    return try {
        write(body(), outData, outLen)
        OK
    } catch (e: IllegalArgumentException) {
        write(error(e), outData, outLen)
        INVALID_ARGUMENT
    } catch (e: Throwable) {
        write(error(e), outData, outLen)
        INTERNAL_ERROR
    }
}

private fun error(e: Throwable): String = buildJsonObject {
    put("error", JsonPrimitive(e.message ?: e.toString()))
}.toString()

private fun write(text: String, outData: CPointer<CPointerVar<ByteVar>>, outLen: CPointer<ULongVar>) {
    val bytes = text.encodeToByteArray()
    val memory = malloc((bytes.size + 1).toULong())?.reinterpret<ByteVar>() ?: error("out of memory")
    if (bytes.isNotEmpty()) bytes.usePinned { memcpy(memory, it.addressOf(0), bytes.size.toULong()) }
    memory[bytes.size] = 0
    outData.pointed.value = memory
    outLen.pointed.value = bytes.size.toULong()
}
