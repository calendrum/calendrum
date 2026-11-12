@file:OptIn(ExperimentalJsExport::class)

package io.github.calendrum.tooling.sdkts

import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tooling.ops.OpsEnvironment
import io.github.calendrum.tooling.ops.OpsLimits
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

// Long is emulated on JS and a date costs several times what it does on Native. With this floor the budget of the contract holds
// for the most series a file can have: 99,999 series cost 5 million dates and not 100 million, which is seconds and not minutes with
// the event loop stopped. It changes a result only above 50,000 series with a rule, and such a result says it is truncated.
private const val MIN_DATES_PER_SERIES = 50

private val ENVIRONMENT = OpsEnvironment.Default.let { default ->
    val limits = default.limits
    OpsEnvironment(
        default.clock,
        default.randomBytes,
        OpsLimits(
            maxRequestChars = limits.maxRequestChars,
            maxInputBytes = limits.maxInputBytes,
            maxDepth = limits.maxDepth,
            defaultItems = limits.defaultItems,
            maxItems = limits.maxItems,
            maxResponseBytes = limits.maxResponseBytes,
            maxProblems = limits.maxProblems,
            maxExpansionWork = limits.maxExpansionWork,
            maxConflictOccurrences = limits.maxConflictOccurrences,
            minDatesPerSeries = MIN_DATES_PER_SERIES,
        ),
    )
}

/** Runs one operation of the contract. Never throws: every failure comes back in the JSON of the response. */
@JsExport
public fun call(operation: String, requestJson: String): String = Ops.call(operation, requestJson, ENVIRONMENT)

/** Returns the versions as a JSON object with the integer `contract` and the strings `library` and `sdk`. */
@JsExport
public fun versions(): String = """{"contract":${Ops.VERSION},"library":"$LIBRARY_VERSION","sdk":"$SDK_VERSION"}"""
