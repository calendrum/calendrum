package io.github.calendrum.tooling.ops

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Clock
import kotlin.time.Instant

// The fixed clock and random bytes of the cases, as conformance/ops/README.md describes them.
internal val FIXED_ENVIRONMENT = OpsEnvironment(
    clock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-01-01T00:00:00Z")
    },
    randomBytes = { size -> ByteArray(size) { it.toByte() } },
    limits = OpsLimits.Default,
)

internal class Case(
    val id: String,
    val operation: String,
    val request: String,
    val expected: String?,
    val ignored: Set<String>,
)

internal fun readText(path: Path): String = SystemFileSystem.source(path).buffered().use { it.readString() }

internal fun allCases(): List<Case> = SystemFileSystem.list(Path(CASES_PATH))
    .filter { path -> SystemFileSystem.exists(Path(path, "request.json")) }
    .sortedBy { it.name }
    .map { directory ->
        val meta = Json.parseToJsonElement(readText(Path(directory, "meta.json"))).jsonObject
        val expected = Path(directory, "expected.json")
        Case(
            id = directory.name,
            operation = meta.getValue("operation").jsonPrimitive.content,
            request = readText(Path(directory, "request.json")),
            expected = if (SystemFileSystem.exists(expected)) readText(expected) else null,
            ignored = (meta["ignore_fields"] as? JsonArray).orEmpty().map { it.jsonPrimitive.content }.toSet(),
        )
    }

// Messages are for people and may change. So are the fields a case names, such as the version of the time zone data.
internal fun comparable(element: JsonElement, ignored: Set<String>): JsonElement = when (element) {
    is JsonObject -> JsonObject(
        element.filterKeys { it != "message" && it !in ignored }.mapValues { comparable(it.value, ignored) },
    )

    is JsonArray -> JsonArray(element.map { comparable(it, ignored) })

    else -> element
}
