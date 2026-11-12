package io.github.calendrum.tooling.cli

import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// The real binary under real files, pipes and terminals. Each test is skipped on a host that has no binary.
class BinaryTest {
    private val directory = temporaryDirectory()

    private fun file(name: String, text: String): String {
        val file = File(directory, name)
        file.writeBytes(text.encodeToByteArray())
        return file.path
    }

    private fun everything() =
        arrayOf("--from", "2026-01-01T00:00:00Z", "--to", "2090-01-01T00:00:00Z", "--limit", "20000")

    private fun window() = arrayOf("--from", "2026-01-01T00:00:00Z", "--to", "2027-01-01T00:00:00Z")

    @Test
    fun versionAndHelpWork() {
        val version = cli("--version")
        assertEquals(0, version.exit)
        assertEquals("calendrum ${System.getProperty("calendrum.version")}", version.text.lines().first())
        val help = cli("--help")
        assertEquals(0, help.exit)
        for (name in listOf("lint", "fix", "format", "convert", "expand", "diff", "anonymize", "tz", "explain")) {
            assertTrue(name in help.text, name)
            val own = cli(name, "--help")
            assertEquals(0, own.exit, name)
            assertTrue(own.text.startsWith("Usage: calendrum $name"), own.text)
        }
        assertEquals("", help.errors)
    }

    @Test
    fun lintWorksOnFilesAndOnTheStandardInput() {
        val clean = file("clean.ics", CRLF_STANDUP)
        val ran = cli("lint", clean)
        assertEquals(0, ran.exit)
        assertEquals("$clean: no findings\n", ran.text)
        val bad = file("bad.ics", CRLF_STANDUP.replace("DTSTAMP:20260101T000000Z\r\n", ""))
        val failing = cli("lint", bad)
        assertEquals(1, failing.exit, failing.errors)
        assertTrue("$bad: ICAL-L001 error" in failing.text, failing.text)
        val piped = cli("lint", "-", stdin = CRLF_STANDUP.toByteArray())
        assertEquals(0, piped.exit)
        assertEquals("<stdin>: no findings\n", piped.text)
        assertEquals(2, cli("lint", File(directory, "missing.ics").path).exit)
        assertEquals(2, cli("lint").exit)
    }

    @Test
    fun fixAndFormatWork() {
        val path = file("a.ics", calendarOf("ATTENDEE:MAILTO:a@example.com"))
        val fixed = cli("fix", path)
        assertEquals(0, fixed.exit)
        assertTrue("ATTENDEE:mailto:a@example.com\r\n" in fixed.text, fixed.text)
        assertTrue("lint ICAL-L024" in fixed.errors, fixed.errors)
        val formatted = cli("format", file("lf.ics", CRLF_STANDUP.replace("\r\n", "\n")))
        assertEquals(0, formatted.exit)
        assertEquals(CRLF_STANDUP, formatted.text)
        val piped = cli("fix", "-", stdin = calendarOf("ATTENDEE:MAILTO:a@example.com").toByteArray())
        assertEquals(fixed.text, piped.text)
    }

    @Test
    fun convertGoesBothWays() {
        val jcal = cli("convert", file("a.ics", CRLF_STANDUP))
        assertEquals(0, jcal.exit)
        assertTrue(jcal.text.startsWith("[[\"vcalendar\","), jcal.text)
        val back = cli("convert", file("a.json", jcal.text))
        assertEquals(0, back.exit, back.errors)
        assertTrue("SUMMARY:Standup" in back.text, back.text)
        val fromStandardInput = cli("convert", "--from", "jcal", "-", stdin = jcal.text.toByteArray())
        assertEquals(back.text, fromStandardInput.text)
        assertEquals(2, cli("convert", file("broken.json", "[\"vcalendar\",")).exit)
    }

    @Test
    fun expandListsTheOccurrencesInUtcOrInAZone() {
        val weekly = file("w.ics", calendarOf("RRULE:FREQ=WEEKLY;COUNT=3").replace("SUMMARY", "X"))
        val utc = cli("expand", weekly, *window())
        assertEquals(0, utc.exit)
        assertEquals(3, utc.text.lines().filter { it.isNotEmpty() }.size)
        assertTrue(utc.text.startsWith("2026-01-05T10:00:00Z 2026-01-05T11:00:00Z - [a@example.com]\n"), utc.text)
        assertTrue("local" !in utc.text)
        val berlin = cli("expand", weekly, *window(), "--zone", "Europe/Berlin")
        assertTrue("local 2026-01-05T11:00:00" in berlin.text, berlin.text)
        val unknown = cli("expand", weekly, *window(), "--zone", "Mars/Olympus")
        assertEquals(2, unknown.exit)
        assertEquals("", unknown.text)
        assertTrue("OPS-UNKNOWN-ZONE" in unknown.errors, unknown.errors)
    }

    @Test
    fun anOutputThatIsNotCompleteIsExitOneWithAMessage() {
        val weekly = file("w.ics", calendarOf("RRULE:FREQ=WEEKLY;COUNT=10"))
        val cut = cli("expand", weekly, *window(), "--limit", "3")
        assertEquals(1, cut.exit)
        assertEquals(3, cut.text.lines().filter { it.isNotEmpty() }.size)
        assertTrue("The output is incomplete" in cut.errors && "--limit" in cut.errors, cut.errors)
        assertEquals(0, cli("expand", weekly, *window(), "--limit", "10").exit)
    }

    @Test
    fun diffAnonymizeTzAndExplainWork() {
        val before = file("before.ics", CRLF_STANDUP)
        val after = file("after.ics", CRLF_STANDUP.replace("Standup", "Retro"))
        val same = cli("diff", before, before)
        assertEquals(0, same.exit)
        assertEquals("no differences (1 unchanged)\n", same.text)
        val differs = cli("diff", before, after)
        assertEquals(1, differs.exit)
        assertEquals("~ VEVENT a@example.com\n0 unchanged\n", differs.text)
        val secret = file("s.ics", CRLF_STANDUP.replace("Standup", "Salary talk with Alice"))
        val first = cli("anonymize", secret, "--shift-weeks", "2")
        assertEquals(0, first.exit)
        assertTrue("Salary" !in first.text && "Alice" !in first.text, first.text)
        assertTrue("DTSTART:20260119T100000Z" in first.text, first.text)
        assertEquals(2, cli("anonymize", secret, "--shift-weeks", "100000").exit)
        val zone = cli("tz", "Europe/Berlin", "--at", "2026-01-15T12:00:00Z")
        assertEquals(0, zone.exit)
        assertTrue(
            "offset: +01:00 (3600 s)" in zone.text && "next change: 2026-03-29T01:00:00Z" in zone.text,
            zone.text,
        )
        assertTrue("at: " in cli("tz", "Asia/Tokyo").text)
        assertEquals(2, cli("tz", "Mars/Olympus").exit)
        val explained = cli("explain", "ICAL-S003")
        assertEquals(0, explained.exit)
        assertTrue(explained.text.startsWith("ICAL-S003 warning BareLf\n"), explained.text)
        assertEquals(2, cli("explain", "ICAL-X999").exit)
    }

    @Test
    fun anonymizeGivesDifferentPseudonymsOnEachRun() {
        val secret = file("s.ics", CRLF_STANDUP.replace("Standup", "Salary talk with Alice") + "")
        val one = cli("anonymize", secret).text
        val two = cli("anonymize", secret).text
        assertTrue(one != two, "the key of the anonymizer must be random for each call")
    }

    @Test
    fun theEnvironmentIsNotRead() {
        val secret = file("s.ics", CRLF_STANDUP.replace("Standup", "Salary talk with Alice"))
        val builder = ProcessBuilder(binary(), "anonymize", secret, "--json")
        builder.environment()["CALENDRUM_ANON_KEY"] = "0000000000000000000000000000000000000000000000000000000000000000"
        builder.environment()["TZ"] = "Asia/Tokyo"
        val ran = builder.start().let { process ->
            val text = process.inputStream.readBytes().decodeToString()
            process.waitFor()
            text
        }
        val again = cli("anonymize", secret, "--json").text
        // A key from the environment would make the two runs the same.
        assertTrue(ran != again)
        val zone =
            ProcessBuilder(
                binary(),
                "expand",
                file("t.ics", calendarOf().replace("DTSTART:20260105T100000Z", "DTSTART:20260105T100000")),
                *window(),
            )
        zone.environment()["TZ"] = "Asia/Tokyo"
        val process = zone.start()
        val text = process.inputStream.readBytes().decodeToString()
        process.waitFor()
        assertTrue(
            text.startsWith("2026-01-05T10:00:00Z "),
            "floating times are read in UTC whatever the TZ of the machine: $text",
        )
    }

    // Files.

    @Test
    fun anOutputFileIsNotReplacedWithoutForce() {
        val input = file("a.ics", calendarOf("ATTENDEE:MAILTO:a@example.com"))
        val output = file("out.ics", "precious")
        val refused = cli("fix", input, "-o", output)
        assertEquals(2, refused.exit)
        assertEquals("", refused.text)
        assertTrue("already exists" in refused.errors && "--force" in refused.errors, refused.errors)
        assertEquals("precious", File(output).readText())
        val forced = cli("fix", input, "-o", output, "--force")
        assertEquals(0, forced.exit, forced.errors)
        assertTrue("ATTENDEE:mailto:" in File(output).readText())
        assertTrue("lint ICAL-L024" in forced.text, forced.text)
        val fresh = File(directory, "fresh.ics")
        assertEquals(0, cli("format", input, "-o", fresh.path).exit)
        assertTrue(fresh.exists())
        assertEquals(listOf("a.ics", "fresh.ics", "out.ics"), directory.list()!!.sorted())
    }

    @Test
    fun inPlaceReplacesTheFileAtomicallyAndKeepsItsPermissions() {
        val path = file("a.ics", calendarOf("ATTENDEE:MAILTO:a@example.com"))
        Files.setPosixFilePermissions(
            File(path).toPath(),
            setOf(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE),
        )
        val ran = cli("fix", "--in-place", path)
        assertEquals(0, ran.exit, ran.errors)
        assertTrue("ATTENDEE:mailto:" in File(path).readText())
        assertEquals(
            setOf(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE),
            Files.getPosixFilePermissions(File(path).toPath()),
        )
        assertEquals(listOf("a.ics"), directory.list()!!.sorted())
        assertTrue("lint ICAL-L024" in ran.text, ran.text)
    }

    @Test
    fun inPlaceLeavesAFileThatIsAlreadyRightUntouched() {
        val path = file("a.ics", CRLF_STANDUP)
        val before = Files.readAttributes(
            File(path).toPath(),
            java.nio.file.attribute.BasicFileAttributes::class.java,
        ).fileKey()
        assertEquals(0, cli("fix", "--in-place", path).exit)
        assertEquals(0, cli("format", "--in-place", path).exit)
        val after = Files.readAttributes(
            File(path).toPath(),
            java.nio.file.attribute.BasicFileAttributes::class.java,
        ).fileKey()
        assertEquals(before, after, "a file with nothing to change is not rewritten")
        assertEquals(CRLF_STANDUP, File(path).readText())
    }

    @Test
    fun aDirectoryThatCannotBeWrittenLeavesTheSourceWholeAndExitsWithTwo() {
        val locked = File(directory, "locked").also { it.mkdir() }
        val path = File(locked, "a.ics")
        val original = calendarOf("ATTENDEE:MAILTO:a@example.com")
        path.writeText(original)
        assertTrue(locked.setWritable(false))
        try {
            val ran = cli("fix", "--in-place", path.path)
            assertEquals(2, ran.exit, ran.errors)
            assertTrue("Cannot write" in ran.errors && "Permission denied" in ran.errors, ran.errors)
            assertEquals(original, path.readText())
            val toFile = cli("fix", path.path, "-o", File(locked, "out.ics").path)
            assertEquals(2, toFile.exit)
        } finally {
            locked.setWritable(true)
        }
        assertEquals(listOf("a.ics"), locked.list()!!.sorted())
    }

    @Test
    fun inPlaceThroughALinkChangesTheFileAndKeepsTheLink() {
        val real = file("real.ics", calendarOf("ATTENDEE:MAILTO:a@example.com"))
        val link = File(directory, "link.ics")
        Files.createSymbolicLink(link.toPath(), File(real).toPath())
        assertEquals(0, cli("fix", "--in-place", link.path).exit)
        assertTrue(Files.isSymbolicLink(link.toPath()))
        assertTrue("ATTENDEE:mailto:" in File(real).readText())
    }

    @Test
    fun aFifoAndADirectoryAsInputAreRefusedAtOnce() {
        val fifo = File(directory, "fifo.ics")
        assertEquals(0, ProcessBuilder("mkfifo", fifo.path).start().waitFor())
        val ran = cli("lint", fifo.path, timeoutSeconds = 20)
        assertEquals(2, ran.exit)
        assertTrue("Not a regular file" in ran.errors, ran.errors)
        val onDirectory = cli("lint", directory.path, timeoutSeconds = 20)
        assertEquals(2, onDirectory.exit)
        assertTrue("Is a directory" in onDirectory.errors, onDirectory.errors)
        assertEquals(2, cli("lint", "/dev/zero", timeoutSeconds = 20).exit)
    }

    @Test
    fun aLinkToACalendarIsFollowedAndNoExtensionIsNeeded() {
        val real = file("calendar-without-extension", CRLF_STANDUP)
        val link = File(directory, "link")
        Files.createSymbolicLink(link.toPath(), File(real).toPath())
        assertEquals(0, cli("lint", link.path).exit)
    }

    // The standard output.

    @Test
    fun aStandardOutputThatCannotBeWrittenIsExitTwo() {
        val path = file("a.ics", CRLF_STANDUP)
        val closed = cli("format", path, command = listOf("/bin/sh", "-c", "exec \"\$0\" \"\$@\" >&-", binary()))
        assertEquals(2, closed.exit, closed.errors)
        assertTrue("Cannot write to the standard output" in closed.errors, closed.errors)
    }

    private fun bigCalendar(): String = calendarOf("RRULE:FREQ=DAILY;COUNT=20000")

    @Test
    fun aReaderThatGoesAwayIsExitTwoWithoutASignalOrATrace() {
        val path = file("big.ics", bigCalendar())
        val ran = cli(
            "expand",
            path,
            *everything(),
            command = listOf(
                "/bin/bash",
                "-c",
                "\"\$0\" \"\$@\" | head -n 1 > /dev/null; exit \${PIPESTATUS[0]}",
                binary(),
            ),
        )
        assertEquals(2, ran.exit, "not 141, the code of SIGPIPE: ${ran.errors}")
        assertTrue("Cannot write to the standard output" in ran.errors, ran.errors)
        assertTrue("Fatal" !in ran.errors && "Exception" !in ran.errors && "\tat " !in ran.errors, ran.errors)
    }

    @Test
    fun aReaderThatClosesAtOnceIsAlsoExitTwo() {
        val path = file("big.ics", bigCalendar())
        val process = ProcessBuilder(
            binary(),
            "expand",
            path,
            *everything(),
        ).redirectInput(ProcessBuilder.Redirect.from(File("/dev/null"))).start()
        process.inputStream.close()
        assertTrue(process.waitFor(60, TimeUnit.SECONDS))
        assertEquals(2, process.exitValue())
        process.errorStream.close()
    }

    @Test
    fun aLargeOutputIsWrittenWhole() {
        val path = file("big.ics", bigCalendar())
        val ran = cli("expand", path, *everything())
        assertEquals(0, ran.exit, ran.errors)
        assertEquals(20000, ran.text.lines().count { it.isNotEmpty() })
    }

    // Text from the calendar and the terminal.

    private val hostile = calendarOf("SUMMARY:Stand\u001B]0;pwn\u0007up\u009B31m\u007F")

    @Test
    fun anEscapeNeverReachesTheHumanOutput() {
        val path = file("h.ics", hostile)
        for (ran in listOf(
            cli("expand", path, *window()),
            cli("lint", path),
            cli("diff", path, file("o.ics", CRLF_STANDUP)),
        )) {
            assertTrue(ran.out.none { it == 0x1B.toByte() || it == 0x07.toByte() || it == 0x7F.toByte() }, ran.text)
            assertTrue(ran.out.toList().windowed(2).none { it[0] == 0xC2.toByte() && it[1] == 0x9B.toByte() }, ran.text)
        }
        val expanded = cli("expand", path, *window())
        assertTrue("Stand\\x1B]0;pwn\\x07up\\x9B31m\\x7F" in expanded.text, expanded.text)
    }

    @Test
    fun aCalendarGoesByteForByteToAPipeAndToAFile() {
        val path = file("h.ics", hostile)
        val piped = cli("format", path)
        assertEquals(0, piped.exit, piped.errors)
        assertContentEquals(hostile.toByteArray(), piped.out)
        val output = File(directory, "formatted.ics")
        assertEquals(0, cli("format", path, "-o", output.path).exit)
        assertContentEquals(hostile.toByteArray(), output.readBytes())
    }

    @Test
    fun onATerminalACalendarHasNoRawControlsButKeepsItsLines() {
        val path = file("h.ics", hostile)
        val ran = cli("format", path, command = listOf("/usr/bin/script", "-q", "/dev/null", binary()))
        val text = ran.text
        assertTrue(text.contains("SUMMARY:Stand\\x1B]0;pwn\\x07up\\x9B31m\\x7F"), text)
        assertTrue(ran.out.none { it == 0x1B.toByte() || it == 0x07.toByte() || it == 0x7F.toByte() }, text)
        assertTrue(text.contains("BEGIN:VCALENDAR"), text)
        // Where the standard output is a terminal, --json drops the raw characters too and stays the same value.
        val json =
            cli("expand", "--json", path, *window(), command = listOf("/usr/bin/script", "-q", "/dev/null", binary()))
        assertTrue(json.out.none { it == 0x7F.toByte() }, json.text)
        assertTrue(json.out.toList().windowed(2).none { it[0] == 0xC2.toByte() && it[1] == 0x9B.toByte() }, json.text)
        assertTrue("\\u009b" in json.text && "\\u007f" in json.text, json.text)
    }

    @Test
    fun jsonInAPipeHasNoRawEscapeAndTheOperationsCharactersStay() {
        val path = file("h.ics", hostile)
        val ran = cli("expand", "--json", path, *window())
        assertEquals(0, ran.exit, ran.errors)
        assertTrue(ran.out.none { it == 0x1B.toByte() }, "JSON escapes the controls of C0 itself")
        assertTrue("\\u001b" in ran.text, ran.text)
    }

    @Test
    fun usageErrorsAreExitTwoAndWriteNothingToTheStandardOutput() {
        for (args in listOf(
            listOf(),
            listOf("nope"),
            listOf("lint", "--nope", "x"),
            listOf("lint", "a", "b"),
            listOf("fix", "--in-place", "-o", "x", "y"),
        )) {
            val ran = cli(*args.toTypedArray())
            assertEquals(2, ran.exit, args.toString())
            assertEquals("", ran.text, args.toString())
            assertTrue(ran.errors.startsWith("calendrum: "), ran.errors)
        }
    }
}
