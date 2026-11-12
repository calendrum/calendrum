package io.github.calendrum.tooling.mcp

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RobustnessTest {
    private val directory = temporaryDirectory()

    @AfterTest
    fun removeTheFiles() {
        directory.deleteRecursively()
    }

    private fun errorCodeOfLine(server: RawServer, line: String): Int {
        server.send(line)
        val answer = server.next()
        return answer.getValue("error").jsonObject.getValue("code").jsonPrimitive.content.toInt()
    }

    @Test
    fun anOlderProtocolIsNegotiated() {
        RawServer(directory).use { server ->
            val result = server.initialize(version = "2025-06-18")
            assertEquals("2025-06-18", result.getValue("protocolVersion").jsonPrimitive.content)
            assertEquals(
                "calendrum-mcp",
                result.getValue("serverInfo").jsonObject.getValue("name").jsonPrimitive.content,
            )
            assertEquals(
                12,
                (server.request("tools/list").getValue("result").jsonObject.getValue("tools") as List<*>).size,
            )
            val answer = server.callTool("ics_explain", JsonObject(mapOf("code" to JsonPrimitive("ICAL-S001"))))
            assertEquals(JsonPrimitive(false), answer["isError"])
            assertEquals(0, server.finish())
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun brokenMessagesGetAnErrorAndTheServerGoesOn() {
        RawServer(directory).use { server ->
            server.initialize()
            assertEquals(-32700, errorCodeOfLine(server, "this is not JSON"))
            assertEquals(-32700, errorCodeOfLine(server, """{"jsonrpc":"2.0","id":1,"""))
            assertEquals(-32700, errorCodeOfLine(server, "\u0000\u0001\u0002"))
            assertEquals(-32600, errorCodeOfLine(server, "[1,2,3]"))
            assertEquals(-32600, errorCodeOfLine(server, "42"))
            assertEquals(-32600, errorCodeOfLine(server, "{}"))
            assertEquals(-32600, errorCodeOfLine(server, """{"jsonrpc":"2.0","id":7}"""))
            assertEquals(-32600, errorCodeOfLine(server, """{"jsonrpc":"2.0","id":{"a":1},"method":"ping"}"""))
            assertEquals(-32600, errorCodeOfLine(server, "[".repeat(100000)))
            assertEquals(
                -32602,
                errorCodeOfLine(server, """{"jsonrpc":"2.0","id":8,"method":"tools/call","params":1}"""),
            )
            assertEquals(
                -32602,
                errorCodeOfLine(server, """{"jsonrpc":"2.0","id":9,"method":"tools/call","params":{"name":1}}"""),
            )
            assertEquals(
                -32602,
                errorCodeOfLine(
                    server,
                    """{"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"x","arguments":1}}""",
                ),
            )
            assertEquals(
                -32602,
                errorCodeOfLine(server, """{"jsonrpc":"2.0","id":11,"method":"tools/call","params":{"name":"x"}}"""),
            )
            assertEquals(-32601, errorCodeOfLine(server, """{"jsonrpc":"2.0","id":12,"method":"no/such/method"}"""))
            server.send("")
            server.send("   ")
            server.ping()
            assertEquals(0, server.finish())
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun theIdOfABrokenRequestIsKept() {
        RawServer(directory).use { server ->
            server.initialize()
            server.send("""{"jsonrpc":"2.0","id":"abc"}""")
            assertEquals(JsonPrimitive("abc"), server.next()["id"])
            server.send("""{"jsonrpc":"2.0","id":7}""")
            assertEquals(JsonPrimitive(7), server.next()["id"])
            server.send("not JSON")
            val noId = server.next()
            assertTrue("id" in noId && noId["id"].toString() == "null", noId.toString())
        }
    }

    @Test
    fun aBrokenAnswerToNothingGetsNoReply() {
        RawServer(directory).use { server ->
            server.initialize()
            server.send("""{"jsonrpc":"2.0","id":123456,"result":{}}""")
            server.send("""{"jsonrpc":"2.0","id":123457,"error":1}""")
            assertTrue(server.noLineWithin(300))
            server.ping()
        }
    }

    @Test
    fun argumentsOfTheWrongTypeGetAnErrorOfTheTool() {
        RawServer(directory).use { server ->
            server.initialize()
            val calls = listOf(
                "ics_inspect" to """{"content":1}""",
                "ics_inspect" to """{"content_base64":[]}""",
                "ics_inspect" to """{"path":{}}""",
                "ics_inspect" to """{"content":"x","limit":"many"}""",
                "ics_diff" to """{"before":true,"after":"x"}""",
                "ics_expand" to """{"content":"x","from":1,"to":2}""",
                "ics_explain" to """{"code":1}""",
                "ics_convert" to """{"jcal":"x"}""",
            )
            for ((tool, arguments) in calls) {
                val answer = server.request("tools/call", """{"name":"$tool","arguments":$arguments}""")
                val result = assertNotNull(answer["result"], "$tool $arguments: $answer").jsonObject
                assertEquals("OPS-BAD-ARGUMENT", errorCodeOf(result), "$tool $arguments")
            }
            server.ping()
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aDeepRequestWithinTheLimitsDoesNotCrash() {
        RawServer(directory).use { server ->
            server.initialize()
            // Within the transport and above the limit of the operations, then within both.
            for (depth in listOf(69, 65, 60)) {
                val jcal = "[".repeat(depth) + "]".repeat(depth)
                val answer = server.request("tools/call", """{"name":"ics_convert","arguments":{"jcal":$jcal}}""")
                assertNotNull(answer["result"], answer.toString())
            }
            server.ping()
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aMessageAboveTheLimitGetsAnErrorAndTheServerGoesOn() {
        RawServer(directory).use { server ->
            server.initialize()
            val block = ByteArray(1024 * 1024) { 'x'.code.toByte() }
            repeat(130) { server.process.outputStream.write(block) }
            server.process.outputStream.write('\n'.code)
            server.process.outputStream.flush()
            val answer = server.next()
            assertEquals(-32600, answer.getValue("error").jsonObject.getValue("code").jsonPrimitive.content.toInt())
            server.ping()
            assertEquals(0, server.finish())
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aCalendarAboveTheFrameOfTheSdkPassesAsContent() {
        RawServer(directory).use { server ->
            server.initialize()
            val padding = "COMMENT:" + "x".repeat(60)
            val events = StringBuilder("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n")
            var index = 0
            while (events.length < 20 * 1024 * 1024) {
                events.append("BEGIN:VEVENT\r\nUID:").append(index++).append("\r\nDTSTAMP:20260101T000000Z\r\n")
                events.append("DTSTART:20260105T100000Z\r\n").append(padding).append("\r\nEND:VEVENT\r\n")
                if (index >= 99_000) break
            }
            events.append("END:VCALENDAR\r\n")
            val result = server.callTool(
                "ics_validate",
                JsonObject(mapOf("content" to JsonPrimitive(events.toString()))),
            )
            assertEquals(JsonPrimitive(false), result["isError"], result.toString().take(300))
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aListIsShortUnlessACallAsks() {
        val events = (1..150).joinToString("") { index ->
            "BEGIN:VEVENT\r\nUID:$index\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nEND:VEVENT\r\n"
        }
        val calendar = JsonPrimitive("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n${events}END:VCALENDAR\r\n")
        val window = mapOf(
            "content" to calendar,
            "from" to JsonPrimitive("2026-01-01T00:00:00Z"),
            "to" to JsonPrimitive("2026-02-01T00:00:00Z"),
        )
        RawServer(directory).use { server ->
            server.initialize()
            val short = server.callTool("ics_expand", JsonObject(window)).getValue("structuredContent").jsonObject
            assertEquals(JsonPrimitive(true), short["truncated"])
            assertEquals(JsonPrimitive(100), short.getValue("result").jsonObject["count"])
            val full = server.callTool("ics_expand", JsonObject(window + ("limit" to JsonPrimitive(150))))
                .getValue("structuredContent").jsonObject
            assertEquals(JsonPrimitive(false), full["truncated"])
            assertEquals(JsonPrimitive(150), full.getValue("result").jsonObject["count"])
            val above = server.callTool("ics_expand", JsonObject(window + ("limit" to JsonPrimitive(100001))))
            assertEquals("OPS-BAD-ARGUMENT", errorCodeOf(above))
        }
    }

    // 99,000 series with a window far ahead: seconds of work for each call.
    private fun heavyFile(): File {
        val file = File(directory, "heavy.ics")
        file.bufferedWriter().use { out ->
            out.write("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:x\r\n")
            repeat(99_000) { index ->
                out.write("BEGIN:VEVENT\r\nUID:$index\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260101T000000Z\r\n")
                out.write("DURATION:PT1S\r\nRRULE:FREQ=SECONDLY;COUNT=1000000\r\nEND:VEVENT\r\n")
            }
            out.write("END:VCALENDAR\r\n")
        }
        return file
    }

    private fun heavyCall(id: Int, file: File): String =
        """{"jsonrpc":"2.0","id":$id,"method":"tools/call","params":{"name":"ics_freebusy","arguments":""" +
            """{"path":${JsonPrimitive(file.path)},"from":"9000-01-01T00:00:00Z","to":"9000-02-01T00:00:00Z"}}}"""

    @Test
    fun pingIsAnsweredDuringTwoHeavyOperations() {
        val file = heavyFile()
        RawServer(directory).use { server ->
            server.initialize()
            val first = server.newId()
            val second = server.newId()
            server.send(heavyCall(first, file))
            server.send(heavyCall(second, file))
            val pending = hashSetOf(first.toString(), second.toString())
            val latencies = ArrayList<Long>()
            var answeredDuringWork = 0
            while (pending.isNotEmpty()) {
                val id = server.newId()
                val sent = System.nanoTime()
                server.send("""{"jsonrpc":"2.0","id":$id,"method":"ping"}""")
                while (true) {
                    val message = server.next(120)
                    val answered = message.getValue("id").jsonPrimitive.content
                    if (answered == id.toString()) {
                        latencies += TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - sent)
                        if (pending.size == 2) answeredDuringWork++
                        break
                    }
                    assertTrue(pending.remove(answered), message.toString().take(300))
                    assertNull(message["error"], message.toString().take(300))
                    assertEquals(
                        JsonPrimitive(false),
                        message.getValue("result").jsonObject["isError"],
                        message.toString().take(300),
                    )
                }
                Thread.sleep(50)
            }
            assertTrue(answeredDuringWork >= 3, "Pings during the work: $answeredDuringWork, latencies $latencies")
            assertTrue(latencies.max() < 1000, "Latencies: $latencies")
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun theEndOfTheInputDuringAnOperationEndsTheProcess() {
        val file = heavyFile()
        RawServer(directory).use { server ->
            server.initialize()
            server.send(heavyCall(server.newId(), file))
            Thread.sleep(200)
            val started = System.nanoTime()
            assertEquals(0, server.finish())
            assertTrue(
                TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - started) < 5,
                "It must not wait for the operation",
            )
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aClosedOutputEndsTheProcessWithoutATrace() {
        RawServer(directory).use { server ->
            server.initialize()
            server.ping()
            server.process.inputStream.close()
            // The first writes can still land in the buffer of the pipe, so keep going until the process is gone.
            val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30)
            while (server.process.isAlive && System.nanoTime() < deadline) {
                try {
                    server.send("""{"jsonrpc":"2.0","id":1,"method":"ping"}""")
                } catch (_: java.io.IOException) {
                    break
                }
                Thread.sleep(10)
            }
            assertTrue(server.process.waitFor(30, TimeUnit.SECONDS), "The process must end")
            assertEquals(0, server.process.exitValue())
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aStoppedAndContinuedServerGoesOn() {
        RawServer(directory).use { server ->
            server.initialize()
            val pid = server.process.pid().toString()
            repeat(3) {
                assertEquals(0, ProcessBuilder("kill", "-STOP", pid).start().waitFor())
                Thread.sleep(50)
                assertEquals(0, ProcessBuilder("kill", "-CONT", pid).start().waitFor())
                server.ping()
            }
            assertEquals(0, server.finish())
            assertEquals("", server.stderr)
        }
    }

    @Test
    fun aRequestCanBeCancelledAndTheServerGoesOn() {
        val file = heavyFile()
        RawServer(directory).use { server ->
            server.initialize()
            val id = server.newId()
            server.send(heavyCall(id, file))
            Thread.sleep(200)
            server.send("""{"jsonrpc":"2.0","method":"notifications/cancelled","params":{"requestId":$id}}""")
            server.ping()
            assertFalse(server.stderr.isNotEmpty(), server.stderr)
        }
    }
}
