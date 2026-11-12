package io.github.calendrum.tooling.mcp

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// The boundary of the server on the real file system, through the real binary.
class PathTest {
    private val top = temporaryDirectory()
    private val root = File(top, "root").also { it.mkdirs() }
    private val outside = File(top, "outside").also { it.mkdirs() }
    private val notARoot = File(top, "rootc").also { it.mkdirs() }

    init {
        File(root, "inside.ics").writeText(SIMPLE_CALENDAR)
        File(root, "upper.ICS").writeText(SIMPLE_CALENDAR)
        File(root, "other.ical").writeText(SIMPLE_CALENDAR)
        File(root, "other.ifb").writeText(SIMPLE_CALENDAR)
        File(root, "other.vcs").writeText(SIMPLE_CALENDAR)
        File(root, "notes.txt").writeText(SIMPLE_CALENDAR)
        File(root, "sub").mkdirs()
        File(root, "sub/deep.ics").writeText(SIMPLE_CALENDAR)
        File(root, "directory.ics").mkdirs()
        File(outside, "secret.ics").writeText(SIMPLE_CALENDAR)
        File(notARoot, "next-door.ics").writeText(SIMPLE_CALENDAR)
        link(File(root, "link-out.ics"), File(outside, "secret.ics"))
        link(File(root, "link-to-text.ics"), File(root, "notes.txt"))
        link(File(root, "link-inside.ics"), File(root, "inside.ics"))
        link(File(root, "dir-out"), outside)
        link(File(top, "root-link"), root)
    }

    private fun link(at: File, target: File) {
        Files.createSymbolicLink(at.toPath(), target.toPath())
    }

    @AfterTest
    fun removeTheFiles() {
        top.deleteRecursively()
    }

    private fun uriOf(file: File): String = "file://${file.path}"

    private fun read(server: RawServer, path: String, roots: List<String> = listOf(uriOf(root))): JsonObject =
        server.callTool("ics_inspect", JsonObject(mapOf("path" to JsonPrimitive(path))), roots)

    private fun withServer(capabilities: String = ROOTS, directory: File = top, block: (RawServer) -> Unit) {
        RawServer(directory).use { server ->
            server.initialize(capabilities)
            block(server)
            assertEquals(0, server.finish())
            assertEquals("", server.stderr)
        }
    }

    private fun assertRead(result: JsonObject, name: String) {
        assertEquals(JsonPrimitive(false), result["isError"], "$name: $result")
        val content = assertNotNull(result["structuredContent"], name).jsonObject
        assertEquals(JsonPrimitive(true), content["ok"], name)
    }

    @Test
    fun filesInsideTheRootAreRead() = withServer { server ->
        for (name in listOf(
            "inside.ics",
            "upper.ICS",
            "other.ical",
            "other.ifb",
            "other.vcs",
            "sub/deep.ics",
            "link-inside.ics",
        )) {
            assertRead(read(server, "$root/$name"), name)
        }
        assertRead(read(server, "$root/sub/../inside.ics"), "..")
        assertRead(read(server, "$top/root-link/inside.ics"), "through a link to the root")
    }

    @Test
    fun aRelativePathStartsAtTheWorkingDirectory() = withServer(directory = root) { server ->
        assertRead(read(server, "inside.ics"), "relative")
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, "../outside/secret.ics")))
    }

    @Test
    fun everythingElseGetsTheSameAnswer() = withServer { server ->
        val denied = listOf(
            "$top/does-not-exist.ics",
            "$root/does-not-exist.ics",
            "$outside/secret.ics",
            "$root/../outside/secret.ics",
            "$root/link-out.ics",
            "$root/dir-out/secret.ics",
            "$root/link-to-text.ics",
            "$root/notes.txt",
            "$root/directory.ics",
            "$root",
            "$notARoot/next-door.ics",
            "/etc/passwd",
            "",
            "$root/inside.ics\u0000.txt",
        )
        val answers = denied.map { path -> read(server, path).toString() }
        assertEquals(1, answers.toSet().size, answers.toSet().toString())
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, denied.first())))
        for (path in denied.filter { it.length > 1 }) assertFalse(path in answers.first(), path)
    }

    @Test
    fun noFileAndOutsideTheRootsMatchByteForByte() = withServer { server ->
        val answers = listOf("$root/does-not-exist.ics", "$outside/secret.ics", "/etc/passwd").map { path ->
            val id = server.newId()
            val params = """{"name":"ics_inspect","arguments":{"path":${JsonPrimitive(path)}}}"""
            server.send("""{"jsonrpc":"2.0","id":$id,"method":"tools/call","params":$params}""")
            server.answerRootsThenRaw(listOf(uriOf(root))).replace(""""id":$id""", """"id":0""")
        }
        assertEquals(1, answers.toSet().size, answers.toSet().toString())
    }

    @Test
    fun aFifoDoesNotHoldTheServer() = withServer { server ->
        val fifo = File(root, "fifo.ics")
        assertEquals(0, ProcessBuilder("mkfifo", fifo.path).start().waitFor())
        val started = System.nanoTime()
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, fifo.path)))
        assertTrue(TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - started) < 5)
        server.ping()
    }

    @Test
    fun aFileAboveTheLimitIsNotRead() = withServer { server ->
        val big = File(root, "big.ics")
        RandomAccessFile(big, "rw").use { it.setLength(65L * 1024 * 1024) }
        val started = System.nanoTime()
        assertEquals("OPS-TOO-LARGE", errorCodeOf(read(server, big.path)))
        assertTrue(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started) < 1000, "It must not read the file")
    }

    @Test
    fun aRootCanHaveEscapes() = withServer { server ->
        val spaced = File(top, "with space").also { it.mkdirs() }
        File(spaced, "x.ics").writeText(SIMPLE_CALENDAR)
        assertRead(read(server, "$spaced/x.ics", listOf("file://$top/with%20space")), "%20")
        assertRead(read(server, "$spaced/x.ics", listOf("file://localhost$top/with%20space/")), "localhost")
    }

    @Test
    fun aRootThatCannotBeReadDoesNotExist() = withServer { server ->
        val bad =
            listOf("https://example.com$root", "file:///%zz", "file://example.com$root", "not a uri", "file://$root?x")
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, "$root/inside.ics", bad)))
        assertRead(read(server, "$root/inside.ics", bad + uriOf(root)), "the good one still counts")
        assertEquals(
            "MCP-PATH-DENIED",
            errorCodeOf(read(server, "$root/inside.ics", listOf(uriOf(File(top, "no-such-root"))))),
        )
    }

    @Test
    fun theRootsAreAskedAtEveryCall() = withServer { server ->
        assertRead(read(server, "$root/inside.ics"), "first")
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, "$root/inside.ics", listOf(uriOf(outside)))))
    }

    @Test
    fun aClientWithoutRootsGetsTheWorkingDirectory() = withServer(capabilities = "{}", directory = root) { server ->
        assertRead(
            server.callTool("ics_inspect", JsonObject(mapOf("path" to JsonPrimitive("$root/inside.ics")))),
            "cwd",
        )
        val answer = server.callTool("ics_inspect", JsonObject(mapOf("path" to JsonPrimitive("$outside/secret.ics"))))
        assertEquals("MCP-PATH-DENIED", errorCodeOf(answer))
    }

    @Test
    fun anEmptyListOfRootsAllowsNothing() = withServer(directory = root) { server ->
        assertEquals("MCP-PATH-DENIED", errorCodeOf(read(server, "$root/inside.ics", emptyList())))
    }

    @Test
    fun anErrorInsteadOfRootsAllowsNothing() = withServer(directory = root) { server ->
        val params = """{"name":"ics_inspect","arguments":{"path":${JsonPrimitive("$root/inside.ics")}}}"""
        val answer = server.request("tools/call", params) { asked ->
            server.send("""{"jsonrpc":"2.0","id":${asked.getValue("id")},"error":{"code":-32603,"message":"No"}}""")
        }
        assertEquals("MCP-PATH-DENIED", errorCodeOf(assertNotNull(answer["result"]).jsonObject))
    }

    @Test
    fun aBrokenListOfRootsAllowsNothing() = withServer(directory = root) { server ->
        val params = """{"name":"ics_inspect","arguments":{"path":${JsonPrimitive("$root/inside.ics")}}}"""
        for (broken in listOf("""{"roots":[{"uri":1}]}""", """{"roots":"x"}""", """{"roots":[1]}""")) {
            val answer = server.request("tools/call", params) { asked ->
                if (asked["method"]?.jsonPrimitive?.content == "roots/list") {
                    server.send("""{"jsonrpc":"2.0","id":${asked.getValue("id")},"result":$broken}""")
                }
            }
            assertEquals("MCP-PATH-DENIED", errorCodeOf(assertNotNull(answer["result"]).jsonObject), broken)
        }
    }

    @Test
    fun noAnswerAboutRootsAllowsNothing() = withServer(directory = root) { server ->
        val id = server.newId()
        val params = """{"name":"ics_inspect","arguments":{"path":${JsonPrimitive("$root/inside.ics")}}}"""
        server.send("""{"jsonrpc":"2.0","id":$id,"method":"tools/call","params":$params}""")
        val asked = server.next()
        assertEquals("roots/list", asked.getValue("method").jsonPrimitive.content)
        // The server stays awake while it waits.
        server.ping()
        val answer = server.answerTo(id)
        assertEquals("MCP-PATH-DENIED", errorCodeOf(assertNotNull(answer["result"]).jsonObject))
    }

    @Test
    fun bothCalendarsOfADiffGoThroughTheBoundary() = withServer { server ->
        val inside = JsonPrimitive("$root/inside.ics")
        val same = server.callTool(
            "ics_diff",
            JsonObject(mapOf("before_path" to inside, "after_path" to inside)),
            listOf(uriOf(root)),
        )
        assertEquals(JsonPrimitive(false), same["isError"], same.toString())
        val secret = JsonPrimitive("$outside/secret.ics")
        val first = server.callTool(
            "ics_diff",
            JsonObject(mapOf("before_path" to secret, "after_path" to inside)),
            listOf(uriOf(root)),
        )
        assertEquals("MCP-PATH-DENIED", errorCodeOf(first))
        val second = server.callTool(
            "ics_diff",
            JsonObject(mapOf("before_path" to inside, "after_path" to secret)),
            listOf(uriOf(root)),
        )
        assertEquals("MCP-PATH-DENIED", errorCodeOf(second))
    }

    private companion object {
        const val ROOTS = """{"roots":{"listChanged":false}}"""
    }
}
