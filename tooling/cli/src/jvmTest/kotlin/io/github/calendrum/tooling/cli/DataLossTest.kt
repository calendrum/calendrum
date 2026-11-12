package io.github.calendrum.tooling.cli

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// The rule: a result that does not hold all of the input never takes the place of a file. Inputs that a command can wreck,
// on the real binary, and the files after it: byte for byte as they were, with the code 2 and the file named in a sentence.
class DataLossTest {
    private val directory = temporaryDirectory()

    private val head = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//probe//EN\r\n"
    private val foot = "END:VCALENDAR\r\n"

    private fun event(index: Int, extra: String = "", text: String = "x") =
        "BEGIN:VEVENT\r\nUID:$index@example.com\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\n" +
            "SUMMARY:$text\r\n${extra}END:VEVENT\r\n"

    // Each input that wrecks a file, with the reason it is wrecked.
    private fun wrecking(): Map<String, ByteArray> = mapOf(
        "plain text" to "These are my notes.\nNot a calendar at all.\nThird line\n".toByteArray(),
        "an empty file" to ByteArray(0),
        "a binary file" to ByteArray(256 * 40) { (it % 256).toByte() },
        "a JSON file" to "{\"key\":\"value\"}\n".toByteArray(),
        "one skipped line" to (head + event(1) + "this line has no colon\r\n" + event(2) + foot).toByteArray(),
        "text before BEGIN" to ("garbage\r\n" + head + event(1) + foot).toByteArray(),
        "text after END" to (head + event(1) + foot + "more text\r\n").toByteArray(),
        "bad UTF-8" to head.toByteArray() +
            "BEGIN:VEVENT\r\nUID:1@example.com\r\nSUMMARY:ÿþÀ\r\nEND:VEVENT\r\n".toByteArray(Charsets.ISO_8859_1) +
            foot.toByteArray(),
        "a line over 64 KiB" to (head + event(1, text = "y".repeat(70000)) + event(2) + foot).toByteArray(),
        "deep nesting" to (
            head + "BEGIN:X\r\n".repeat(12) + "SUMMARY:deep\r\n" + "END:X\r\n".repeat(12) + event(1) + foot
            ).toByteArray(),
        "a property outside a component" to ("X-OUTSIDE:keep\r\n" + head + event(1) + foot).toByteArray(),
    )

    private fun heavy(): Map<String, ByteArray> = mapOf(
        "150000 events" to (head + (0 until 150000).joinToString("") { event(it) } + foot).toByteArray(),
        "1500000 lines" to (head + event(1, "X-A:1\r\n".repeat(1500000)) + foot).toByteArray(),
    )

    // What a command may rewrite without loss.
    private fun harmless(): Map<String, ByteArray> = mapOf(
        "an unknown property kept" to (head + event(1, "X-MINE:keep me\r\n") + foot).toByteArray(),
        "no END:VCALENDAR" to (head + event(1) + event(2)).toByteArray(),
        "two calendars" to (head + event(1) + foot + head + event(2) + foot).toByteArray(),
        "a bad date" to (head + event(1, "DTEND:not-a-date\r\n") + foot).toByteArray(),
        "a NUL byte" to (head + event(1, text = "a\u0000b") + foot).toByteArray(),
        "a byte order mark" to
            byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()) + (head + event(1) + foot).toByteArray(),
        "bare LF" to (head + event(1) + foot).replace("\r\n", "\n").toByteArray(),
    )

    private fun uids(data: ByteArray): Int = String(data, Charsets.ISO_8859_1).split("UID:").size - 1

    private fun file(name: String, data: ByteArray): File = File(directory, name).also { it.writeBytes(data) }

    private val inPlace = listOf(
        listOf("format", "--in-place"),
        listOf("fix", "--in-place"),
        listOf("fix", "--fixups", "lint", "--in-place"),
    )

    private fun checkWrecked(inputs: Map<String, ByteArray>, commands: List<List<String>>) {
        for ((label, data) in inputs) {
            for (command in commands) {
                val target = file("f.ics", data)
                val ran = cli(*command.toTypedArray(), target.path)
                val what = "$label: ${command.joinToString(" ")}"
                assertContentEquals(data, target.readBytes(), "$what: the file must be as it was")
                assertEquals(2, ran.exit, "$what: ${ran.errors}")
                assertEquals("", ran.text, what)
                assertEquals(listOf("f.ics"), directory.list()!!.sorted(), "$what: no temporary file stays")
                val note = ran.errors
                assertTrue(
                    "Nothing was written: '${target.path}' is as it was." in note || "No calendar found in" in note ||
                        "OPS-TOO-LARGE" in note,
                    "$what: $note",
                )
            }
        }
    }

    @Test
    fun inPlaceLeavesEveryWreckedFileAsItWas() = checkWrecked(wrecking(), inPlace)

    @Test
    fun inPlaceLeavesAHugeFileAsItWas() = checkWrecked(heavy(), listOf(inPlace[0], inPlace[1]))

    @Test
    fun aRepairThatChangesSomethingDoesNotMakeALossyResultWrite() {
        val zone = "BEGIN:VTIMEZONE\r\nTZID:Pacific Standard Time\r\nBEGIN:STANDARD\r\nDTSTART:19700101T000000\r\n" +
            "TZOFFSETFROM:-0800\r\nTZOFFSETTO:-0800\r\nEND:STANDARD\r\nEND:VTIMEZONE\r\n"
        fun repaired(index: Int, text: String = "x") =
            "BEGIN:VEVENT\r\nUID:$index@example.com\r\nDTSTAMP:20260101T000000Z\r\n" +
                "DTSTART;TZID=Pacific Standard Time:20260105T100000\r\nSUMMARY:$text\r\nEND:VEVENT\r\n"
        val inputs = mapOf(
            "a skipped line, a repair applies" to
                head + zone + repaired(1) + "line without a colon\r\n" + repaired(2) + foot,
            "a property outside, a repair applies" to "X-OUTSIDE:keep\r\n" + head + zone + repaired(1) + foot,
            "a line over 64 KiB, a repair applies" to head + zone + repaired(1, "y".repeat(70000)) + repaired(2) + foot,
            "150000 events, a repair applies" to head + zone + (0 until 150000).joinToString("") {
                repaired(it)
            } + foot,
        )
        checkWrecked(
            inputs.mapValues { it.value.toByteArray() },
            listOf(listOf("fix", "--fixups", "windows_tzids", "--in-place"), listOf("fix", "--in-place")),
        )
    }

    @Test
    fun aForcedOutputFileIsLeftAsItWas() {
        val precious = "precious\n".toByteArray()
        for ((label, data) in wrecking() + heavy().filterKeys { it == "150000 events" }) {
            val source = file("f.ics", data)
            val out = file("out.ics", precious)
            val ran = cli("format", source.path, "-o", out.path, "--force")
            assertContentEquals(precious, out.readBytes(), label)
            assertEquals(2, ran.exit, "$label: ${ran.errors}")
            assertEquals(listOf("f.ics", "out.ics"), directory.list()!!.sorted(), label)
        }
    }

    @Test
    fun aNewOutputFileGetsTheLossyResultWithExitOne() {
        val source = file("f.ics", wrecking().getValue("one skipped line"))
        val out = File(directory, "new.ics")
        val ran = cli("format", source.path, "-o", out.path)
        assertEquals(1, ran.exit, ran.errors)
        assertTrue(out.exists())
        assertTrue("does not hold all of the input" in ran.errors, ran.errors)
        // The check that stops `format a.ics > b.ics && mv b.ics a.ics`.
        val piped = cli("format", source.path)
        assertEquals(1, piped.exit)
        assertTrue(piped.text.isNotEmpty())
    }

    @Test
    fun aRewriteThatLosesNothingIsWritten() {
        for ((label, data) in harmless()) {
            val target = file("f.ics", data)
            val ran = cli("format", "--in-place", target.path)
            assertEquals(0, ran.exit, "$label: ${ran.errors}")
            assertEquals(uids(data), uids(target.readBytes()), "$label: every event stays")
            assertEquals(listOf("f.ics"), directory.list()!!.sorted(), label)
        }
    }

    @Test
    fun noCalendarIsExitTwoForEveryCommandThatWritesOne() {
        val inputs = mapOf(
            "notes.txt" to "These are my notes.\nSecond line\n".toByteArray(),
            "empty.ics" to ByteArray(0),
            "only-comment.ics" to "X-A:1\r\n".toByteArray(),
        )
        for ((name, data) in inputs) {
            val path = file(name, data).path
            for (command in listOf("fix", "format", "convert", "anonymize")) {
                val ran = cli(command, path)
                assertEquals(2, ran.exit, "$command $name")
                assertEquals("", ran.text, "$command $name")
                assertTrue("No calendar found in '$path': nothing was written" in ran.errors, ran.errors)
                val json = cli(command, path, "--json")
                assertEquals(2, json.exit, "$command $name --json")
                assertTrue(json.text.startsWith("{\"ops_version\":1,\"ok\":true"), json.text)
            }
            assertEquals(1, cli("lint", path).exit, name)
            assertEquals(1, cli("lint", path, "--rules", "rfc5545").exit, name)
            assertEquals(1, cli("lint", path, "--json").exit, name)
            assertEquals(2, cli("format", path, "-o", File(directory, "$name.out").path).exit)
            assertTrue(!File(directory, "$name.out").exists())
        }
        val emptyJcal = file("empty.json", "[]".toByteArray()).path
        val ran = cli("convert", emptyJcal)
        assertEquals(2, ran.exit)
        assertEquals("", ran.text)
    }

    @Test
    fun aFileWithAHeaderAndNothingElseIsACalendar() {
        val path = file("header.ics", "BEGIN:VCALENDAR\r\nEND:VCALENDAR\r\n".toByteArray()).path
        assertEquals(0, cli("format", path).exit)
    }

    @Test
    fun aJcalFileWithABomIsRead() {
        val jcal = cli("convert", file("a.ics", CRLF_STANDUP.toByteArray()).path).out
        val withBom = file("bom.json", byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()) + jcal)
        val ran = cli("convert", withBom.path)
        assertEquals(0, ran.exit, ran.errors)
        assertTrue("SUMMARY:Standup" in ran.text)
    }

    @Test
    fun aNameWithABackslashIsAName() {
        val source = file("a.ics", CRLF_STANDUP.toByteArray())
        val out = File(directory, "back\\slash.ics")
        val ran = cli("format", source.path, "-o", out.path)
        assertEquals(0, ran.exit, ran.errors)
        assertTrue(out.exists())
        assertEquals(listOf("a.ics", "back\\slash.ics"), directory.list()!!.sorted())
        assertEquals(2, cli("format", source.path, "-o", out.path).exit)
    }

    @Test
    fun aBrokenStandardErrorIsNotASignal() {
        // The reader of the standard error goes away first; the error comes after it, from input that is read later.
        val process = ProcessBuilder(binary(), "convert", "--from", "jcal", "-").redirectOutput(
            ProcessBuilder.Redirect.DISCARD,
        ).start()
        process.errorStream.close()
        process.outputStream.use { it.write("not json".toByteArray()) }
        assertTrue(process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS))
        assertEquals(2, process.exitValue(), "not 141, the code of SIGPIPE")
        val shell = cli(
            "lint",
            File(directory, "absent.ics").path,
            command = listOf("/bin/bash", "-c", "\"\$0\" \"\$@\" 2>&1 | true; exit \${PIPESTATUS[0]}", binary()),
        )
        assertEquals(2, shell.exit)
    }

    @Test
    fun aPrivateCalendarIsNeverReadableByOthers() {
        val big = head + (0 until 14000).joinToString("") { event(it, text = "x".repeat(900)) } + foot
        val target = file("private.ics", big.replace("\r\n", "\n").toByteArray())
        Files.setPosixFilePermissions(
            target.toPath(),
            setOf(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE),
        )
        val ran = cli("format", "--in-place", target.path)
        assertEquals(0, ran.exit, ran.errors)
        assertEquals(
            setOf(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE),
            Files.getPosixFilePermissions(target.toPath()),
        )
    }
}
