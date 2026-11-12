package io.github.calendrum.tooling.mcp

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.DictionaryFile
import com.code_intelligence.jazzer.junit.FuzzTest
import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tooling.ops.OpsEnvironment
import io.github.calendrum.tooling.ops.OpsLimits
import io.github.calendrum.tooling.platform.FileAccess
import io.github.calendrum.tooling.platform.FileRead
import io.github.calendrum.tooling.platform.isInside
import io.github.calendrum.tooling.platform.pathOfFileUri
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class McpFuzzTest {
    // Jazzer fuzzes one method in a run, so one target takes turns between the checks.
    @FuzzTest
    @DictionaryFile(resourcePath = "mcp.dict")
    fun argumentsPathsAndRoots(data: FuzzedDataProvider) {
        when (data.consumeInt(0, 2)) {
            0 -> argumentsBecomeARequest(data)
            1 -> aPathStaysInsideTheRoot(data)
            else -> aRootIsCleanAndComparedByComponents(data)
        }
    }

    // Whatever the arguments, a call gives a request that is one JSON object, without the names of the tool, or a
    // failure with a fixed text. A file is read only through the function that the server gives.
    private fun argumentsBecomeARequest(data: FuzzedDataProvider) {
        val spec = ToolCatalog.specs[data.consumeInt(0, ToolCatalog.specs.size - 1)]
        val mode = data.consumeInt(0, 2)
        val path = SENTINEL + data.consumeString(40)
        val name = data.consumeString(40)
        val rest = data.consumeRemainingAsString()
        val arguments = when (mode) {
            0 -> try {
                Json.parseToJsonElement(rest) as? JsonObject
            } catch (_: SerializationException) {
                null
            } ?: return

            else -> {
                val fields = LinkedHashMap<String, JsonElement>()
                for (calendar in spec.calendars) {
                    if (mode == 1) fields[calendar.pathArgument] = JsonPrimitive(path)
                    if (mode == 2) fields[calendar.argument] = JsonPrimitive(rest)
                    fields[name] = JsonPrimitive(rest)
                }
                JsonObject(fields)
            }
        }
        val request = try {
            requestOf(spec, arguments) { asked ->
                when (asked.length % 3) {
                    0 -> FileRead.Denied
                    1 -> FileRead.TooLarge
                    else -> FileRead.Bytes(asked.encodeToByteArray())
                }
            }
        } catch (failure: ToolFailure) {
            check(failure.code.startsWith("OPS-") || failure.code.startsWith("MCP-")) { "Unknown code" }
            check(SENTINEL !in failure.text) { "A failure quotes the path" }
            return
        }
        val parsed = Json.parseToJsonElement(request).jsonObject
        for (calendar in spec.calendars) {
            check(calendar.pathArgument !in parsed) { "A path reached the operation" }
        }
        val result = resultOf(Ops.call(spec.operation, request, LIMITED))
        check(result.content.single() is TextContent) { "No text" }
        check((result.isError == true) == (result.structuredContent == null)) { "Both or neither" }
    }

    // A path only reads a calendar file inside the root, whatever it says and wherever its links lead.
    private fun aPathStaysInsideTheRoot(data: FuzzedDataProvider) {
        val base = PREFIXES[data.consumeInt(0, PREFIXES.size - 1)]
        val path = base + data.consumeRemainingAsString()
        val read = FileAccess.readInside(path, listOf(ROOT), FileAccess.CALENDAR_EXTENSIONS, 4096)
        if (read is FileRead.Bytes) {
            check(!read.bytes.decodeToString().contains(SECRET)) { "A secret came out" }
            val real = Paths.get(path).toRealPath()
            check(real.startsWith(ROOT)) { "A file outside the root" }
        }
    }

    // The URI of a root gives a clean path or nothing, and the comparison of paths agrees with one by components.
    private fun aRootIsCleanAndComparedByComponents(data: FuzzedDataProvider) {
        val left = data.consumeString(40)
        val right = data.consumeString(40)
        val uri = data.consumeRemainingAsString()
        for (candidate in listOf(uri, "file://$uri")) {
            pathOfFileUri(candidate)?.let { path ->
                check(path.startsWith("/")) { "Not absolute" }
                check(path.none { it.code < 0x20 }) { "A control character" }
            }
        }
        val leftParts = left.split('/').filter { it.isNotEmpty() }
        val rightParts = right.split('/').filter { it.isNotEmpty() }
        val cleanLeft = "/" + leftParts.joinToString("/")
        val cleanRight = "/" + rightParts.joinToString("/")
        val expected = leftParts.size >= rightParts.size && leftParts.subList(0, rightParts.size) == rightParts
        check(isInside(cleanLeft, cleanRight) == expected) { "The comparison differs" }
        check(isInside("$cleanLeft/x", cleanLeft)) { "A child is inside" }
        check(!isInside(cleanLeft + "x", cleanLeft) || cleanLeft == "/") { "A neighbour is not" }
    }

    private companion object {
        const val SECRET = "SECRET"
        const val SENTINEL = "\u0001path"

        val LIMITED = OpsEnvironment(
            OpsEnvironment.Default.clock,
            OpsEnvironment.Default.randomBytes,
            OpsLimits(
                maxRequestChars = 1024 * 1024,
                maxInputBytes = 1024 * 1024,
                maxDepth = 64,
                defaultItems = 100,
                maxItems = 1000,
                maxResponseBytes = 1024L * 1024 * 4,
                maxProblems = 100,
                maxExpansionWork = 200_000,
                maxConflictOccurrences = 200,
            ),
        )

        // A small tree that the fuzzer can walk around in, with what it must not read.
        val TOP: File = Files.createTempDirectory("calendrum-mcp-fuzz").toFile().canonicalFile.also { top ->
            val root = File(top, "root").also { it.mkdirs() }
            val outside = File(top, "outside").also { it.mkdirs() }
            File(top, "rootc").mkdirs()
            File(root, "sub").mkdirs()
            File(root, "a.ics").writeText("BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n")
            File(root, "sub/b.ics").writeText("BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n")
            File(root, "notes.txt").writeText(SECRET)
            File(outside, "c.ics").writeText(SECRET)
            File(top, "rootc/d.ics").writeText(SECRET)
            Files.createSymbolicLink(File(root, "out.ics").toPath(), File(outside, "c.ics").toPath())
            Files.createSymbolicLink(File(root, "dir").toPath(), outside.toPath())
            Files.createSymbolicLink(File(root, "text.ics").toPath(), File(root, "notes.txt").toPath())
            Runtime.getRuntime().addShutdownHook(Thread { top.deleteRecursively() })
        }
        val ROOT: String = File(TOP, "root").path
        val PREFIXES: List<String> = listOf(
            "",
            "$ROOT/",
            "$ROOT/sub/",
            "$ROOT/../",
            "$ROOT/dir/",
            "${TOP.path}/rootc/",
            "${TOP.path}/outside/",
        )
    }
}
