package io.github.calendrum.tooling.ops

import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * The operations behind every transport: the MCP server, the command line and the SDKs for other languages.
 *
 * The contract is the JSON, described in `conformance/ops/README.md`, not this object. Nothing here is published.
 */
public object Ops {
    /** The version of the contract, written into every response as `ops_version`. */
    public const val VERSION: Int = 1

    /** The names of the operations. */
    public val operations: List<String> = listOf(
        "inspect",
        "validate",
        "fix",
        "expand",
        "rrule_expand",
        "freebusy",
        "build_event",
        "diff",
        "convert",
        "anonymize",
        "tz_resolve",
        "explain",
    )

    /** Runs one operation. Never throws: every failure comes back as a JSON error. */
    public fun call(
        operation: String,
        requestJson: String,
        environment: OpsEnvironment = OpsEnvironment.Default,
    ): String = try {
        unguarded(operation, requestJson, environment)
    } catch (_: Throwable) {
        // A fixed text and not the message of the exception: that can quote the input.
        failure(INTERNAL, "The operation failed inside the library")
    }

    /** Like [call], but a bug comes out as the exception it is, for the fuzz target. */
    internal fun unguarded(operation: String, requestJson: String, environment: OpsEnvironment): String = try {
        val request = Request.parse(requestJson, environment.limits)
        val run = Run(environment)
        val out = run.out
        out.beginObject()
        out.field("ops_version", VERSION)
        out.field("ok", true)
        out.name("result")
        when (operation) {
            "inspect" -> inspect(request, run)
            "validate" -> validate(request, run)
            "fix" -> fix(request, run)
            "expand" -> expand(request, run)
            "rrule_expand" -> rruleExpand(request, run)
            "freebusy" -> freeBusy(request, run)
            "build_event" -> buildEvent(request, run)
            "diff" -> diff(request, run)
            "convert" -> convert(request, run)
            "anonymize" -> anonymize(request, run)
            "tz_resolve" -> tzResolve(request, run)
            "explain" -> explain(request, run)
            else -> throw OpsFailure(UNKNOWN_OPERATION, "There is no such operation")
        }
        out.field("truncated", run.truncated)
        out.endObject()
        if (out.utf8Size > environment.limits.maxResponseBytes) {
            failure(TOO_LARGE, "The response is larger than the limit")
        } else {
            out.toString()
        }
    } catch (e: OpsFailure) {
        failure(e.code, e.text)
    }

    private fun failure(code: String, message: String): String {
        val out = JsonOut()
        out.beginObject()
        out.field("ops_version", VERSION)
        out.field("ok", false)
        out.name("error")
        out.beginObject()
        out.field("code", code)
        out.field("message", message)
        out.endObject()
        out.endObject()
        return out.toString()
    }
}

/**
 * What an operation takes from outside, so that a test can fix it.
 *
 * @property clock gives `DTSTAMP` of a built event and the default moment of `tz_resolve`.
 * @property randomBytes gives secret bytes: the anonymization key and the random part of a `UID`.
 * @property limits the bounds of a request and a response.
 */
public class OpsEnvironment(
    public val clock: Clock,
    public val randomBytes: (Int) -> ByteArray,
    public val limits: OpsLimits,
) {
    /** The defaults. */
    public companion object {
        /** The system clock, the secure random generator of the platform and the default limits. */
        public val Default: OpsEnvironment = OpsEnvironment(Clock.System, ::secureBytes, OpsLimits.Default)
    }
}

/**
 * The bounds of one call. The defaults are described in `conformance/ops/README.md`.
 *
 * @property maxRequestChars the longest request, in characters.
 * @property maxInputBytes the largest calendar in a request.
 * @property maxDepth the deepest nesting of a request.
 * @property defaultItems how many items a list has when the request does not say.
 * @property maxItems the largest `limit` a request can ask for.
 * @property maxResponseBytes the largest response, in UTF-8.
 * @property maxProblems how many problems a response lists.
 * @property maxExpansionWork the budget of dates divided among the series of a call, including the dates before the
 *   window, which still count for `COUNT`. A series gets at least [minDatesPerSeries], so many series can exceed it.
 * @property maxConflictOccurrences above this number of occurrences in the window `freebusy` does not look for
 *   conflicts: their number grows with its square.
 * @property minDatesPerSeries the least share of [maxExpansionWork] of a series. A platform where a date costs more can lower it.
 */
public class OpsLimits(
    public val maxRequestChars: Int,
    public val maxInputBytes: Int,
    public val maxDepth: Int,
    public val defaultItems: Int,
    public val maxItems: Int,
    public val maxResponseBytes: Long,
    public val maxProblems: Int,
    public val maxExpansionWork: Int,
    public val maxConflictOccurrences: Int,
    public val minDatesPerSeries: Int = 1000,
) {
    /** The defaults. */
    public companion object {
        /** The limits of a request from a stranger. */
        public val Default: OpsLimits = OpsLimits(
            maxRequestChars = 128 * 1024 * 1024,
            maxInputBytes = 64 * 1024 * 1024,
            maxDepth = 64,
            defaultItems = 1000,
            maxItems = 100_000,
            maxResponseBytes = 16L * 1024 * 1024,
            maxProblems = 1000,
            maxExpansionWork = 5_000_000,
            maxConflictOccurrences = 2000,
        )
    }
}

internal const val BAD_ARGUMENT = "OPS-BAD-ARGUMENT"
internal const val UNKNOWN_OPERATION = "OPS-UNKNOWN-OPERATION"
internal const val UNKNOWN_ZONE = "OPS-UNKNOWN-ZONE"
internal const val UNKNOWN_CODE = "OPS-UNKNOWN-CODE"
internal const val TOO_LARGE = "OPS-TOO-LARGE"
internal const val INTERNAL = "OPS-INTERNAL"

// `text` holds a fixed sentence: no input goes into it.
internal class OpsFailure(val code: String, val text: String) : Exception(text)

internal fun badArgument(text: String): Nothing = throw OpsFailure(BAD_ARGUMENT, text)

// A random UUID comes from the secure generator of the platform on all targets, which spares a function per platform.
@OptIn(ExperimentalUuidApi::class)
private fun secureBytes(size: Int): ByteArray {
    val bytes = ByteArray(size)
    var filled = 0
    while (filled < size) {
        val chunk = Uuid.random().toByteArray()
        val count = minOf(chunk.size, size - filled)
        chunk.copyInto(bytes, filled, 0, count)
        filled += count
    }
    return bytes
}
