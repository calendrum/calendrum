package io.github.calendrum.tooling.mcp

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Assumptions.assumeTrue
import java.io.File
import java.nio.file.Files
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

/** The release binary of this host, built by Gradle before the tests. A test needing it is skipped on other hosts. */
internal fun binary(): String {
    val path = System.getProperty("calendrum.mcp.binary")
    assumeTrue(path != null, "No binary for this host")
    return path!!
}

internal fun temporaryDirectory(): File = Files.createTempDirectory("calendrum-mcp").toFile().canonicalFile

internal val SIMPLE_CALENDAR = listOf(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "DURATION:PT1H",
    "SUMMARY:Standup",
    "END:VEVENT",
    "END:VCALENDAR",
    "",
).joinToString("\r\n")

/** The server under a client that writes JSON-RPC by hand, which also lets it send what no library would. */
internal class RawServer(directory: File) : AutoCloseable {
    private val errors = File.createTempFile("calendrum-mcp", ".stderr")
    val process: Process = ProcessBuilder(binary()).directory(directory).redirectError(errors).start()
    private val lines = LinkedBlockingQueue<String>()
    private var nextId = 1000

    init {
        thread(isDaemon = true) {
            process.inputStream.bufferedReader().forEachLine { lines.put(it) }
        }
    }

    val stderr: String get() = errors.readText()

    fun send(line: String) {
        process.outputStream.write((line + "\n").toByteArray())
        process.outputStream.flush()
    }

    fun nextLine(timeoutSeconds: Long = 60): String =
        lines.poll(timeoutSeconds, TimeUnit.SECONDS) ?: fail("No answer in $timeoutSeconds seconds")

    fun next(timeoutSeconds: Long = 60): JsonObject = Json.parseToJsonElement(nextLine(timeoutSeconds)).jsonObject

    fun noLineWithin(milliseconds: Long): Boolean = lines.poll(milliseconds, TimeUnit.MILLISECONDS) == null

    fun initialize(capabilities: String = "{}", version: String = "2025-11-25"): JsonObject {
        val id = nextId++
        send(
            """{"jsonrpc":"2.0","id":$id,"method":"initialize","params":{"protocolVersion":"$version",""" +
                """"capabilities":$capabilities,"clientInfo":{"name":"test","version":"0"}}}""",
        )
        val answer = next()
        assertEquals(id, answer.getValue("id").jsonPrimitive.content.toInt())
        send("""{"jsonrpc":"2.0","method":"notifications/initialized"}""")
        return answer.getValue("result").jsonObject
    }

    /** Sends a request and waits for its answer. A request of the server on the way goes to [onRequest]. */
    fun request(method: String, params: String = "{}", onRequest: (JsonObject) -> Unit = {}): JsonObject {
        val id = nextId++
        send("""{"jsonrpc":"2.0","id":$id,"method":"$method","params":$params}""")
        return answerTo(id, onRequest)
    }

    fun answerTo(id: Int, onRequest: (JsonObject) -> Unit = {}): JsonObject {
        while (true) {
            val message = next()
            if ("method" in message) {
                onRequest(message)
            } else if (message["id"]?.jsonPrimitive?.content == id.toString()) {
                return message
            }
        }
    }

    fun newId(): Int = nextId++

    fun ping() {
        assertNotNull(request("ping")["result"])
    }

    /** The result of a tool. With [roots], the server gets them as the answer to `roots/list`. */
    fun callTool(name: String, arguments: JsonObject, roots: List<String>? = null): JsonObject {
        val params = buildJsonObject {
            put("name", name)
            put("arguments", arguments)
        }
        val answer = request("tools/call", params.toString()) { asked ->
            if (roots != null && asked["method"]?.jsonPrimitive?.content == "roots/list") answerRoots(asked, roots)
        }
        return assertNotNull(answer["result"], answer.toString()).jsonObject
    }

    fun answerRoots(asked: JsonObject, roots: List<String>) {
        val list = roots.joinToString(",") { """{"uri":${JsonPrimitive(it)}}""" }
        send("""{"jsonrpc":"2.0","id":${asked.getValue("id")},"result":{"roots":[$list]}}""")
    }

    /** Answers `roots/list` when it comes and gives the next line after it, as the bytes that came. */
    fun answerRootsThenRaw(roots: List<String>): String {
        while (true) {
            val line = nextLine()
            val message = Json.parseToJsonElement(line).jsonObject
            if (message["method"]?.jsonPrimitive?.content == "roots/list") answerRoots(message, roots) else return line
        }
    }

    /** Closes the input and gives the code of the exit. */
    fun finish(): Int {
        process.outputStream.close()
        if (!process.waitFor(30, TimeUnit.SECONDS)) fail("The server did not stop at the end of its input")
        return process.exitValue()
    }

    override fun close() {
        process.destroyForcibly()
        errors.delete()
    }
}

internal fun textOf(result: JsonObject): String =
    result.getValue("content").jsonArray[0].jsonObject.getValue("text").jsonPrimitive.content

internal fun errorCodeOf(result: JsonObject): String {
    assertEquals(JsonPrimitive(true), result["isError"], result.toString())
    return Json.parseToJsonElement(textOf(result)).jsonObject.getValue("code").jsonPrimitive.content
}
