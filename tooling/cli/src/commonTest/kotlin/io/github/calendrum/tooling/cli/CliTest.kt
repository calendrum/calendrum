package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tooling.platform.FileWrite
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val WARNING_ONLY = calendar(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "X-WR-TIMEZONE:Europe/Berlin",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000",
    "END:VEVENT",
    "END:VCALENDAR",
)

private val NO_STAMP = calendar(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTART:20260105T100000Z",
    "END:VEVENT",
    "END:VCALENDAR",
)

private val MAILTO = calendar(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "ATTENDEE:MAILTO:a@example.com",
    "END:VEVENT",
    "END:VCALENDAR",
)

private fun escaped(summary: String) = calendar(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "DURATION:PT1H",
    "SUMMARY:$summary",
    "END:VEVENT",
    "END:VCALENDAR",
)

class CliTest {
    // The table of exit codes and answers.

    @Test
    fun aCleanCalendarIsExitZero() {
        val outcome = run("lint a.ics", consoleWith("a.ics" to STANDUP))
        assertEquals(0, outcome.exit)
        assertEquals("a.ics: no findings\n", outcome.out)
        assertEquals("", outcome.err)
    }

    @Test
    fun aFindingAboveTheThresholdIsExitOne() {
        val outcome = run("lint a.ics", consoleWith("a.ics" to NO_STAMP))
        assertEquals(1, outcome.exit)
        assertTrue(outcome.out.lines().first().startsWith("a.ics: ICAL-L001 error "), outcome.out)
        assertTrue("(VCALENDAR[0]/VEVENT[0]/DTSTAMP[0])" in outcome.out, outcome.out)
        assertTrue(outcome.out.trimEnd().endsWith("a.ics: 1 finding (1 error, 0 warnings, 0 info)"), outcome.out)
    }

    @Test
    fun theThresholdOfLintIsTheSeverityFromWhichItFails() {
        val files = consoleWith("a.ics" to WARNING_ONLY)
        assertEquals(1, run("lint a.ics", files).exit)
        assertEquals(1, run("lint --fail-on warning a.ics", files).exit)
        val lenient = run("lint --fail-on error a.ics", files)
        assertEquals(0, lenient.exit)
        assertTrue("ICAL-L021 warning" in lenient.out, lenient.out)
    }

    @Test
    fun aFindingOfSeverityInfoNeverFails() {
        val outcome = run("lint a.ics", consoleWith("a.ics" to MAILTO))
        assertEquals(0, outcome.exit)
        assertTrue("ICAL-L024 info" in outcome.out && "[fixable]" in outcome.out, outcome.out)
    }

    @Test
    fun lintUsesTheLinterRulesUnlessToldOtherwise() {
        assertEquals(0, run("lint --rules rfc5545 a.ics", consoleWith("a.ics" to MAILTO)).exit)
        assertTrue("ICAL-L024" in run("lint a.ics", consoleWith("a.ics" to MAILTO)).out)
    }

    @Test
    fun theReaderFindingsHaveAPosition() {
        val bare = STANDUP.replace("\r\n", "\n")
        val outcome = run("lint a.ics", consoleWith("a.ics" to bare))
        assertTrue(Regex("a\\.ics:1:\\d+ ICAL-S003 warning bare LF").containsMatchIn(outcome.out), outcome.out)
        assertEquals(1, outcome.exit)
    }

    @Test
    fun theJsonOfLintIsTheAnswerOfTheContractAndTheExitCodeStaysTheSame() {
        val files = consoleWith("a.ics" to NO_STAMP)
        val outcome = run("lint --json a.ics", files)
        val request = """{"ics_base64":"${kotlin.io.encoding.Base64.encode(
            NO_STAMP.encodeToByteArray(),
        )}","rules":"lint"}"""
        assertEquals(Ops.call("validate", request, FIXED_ENVIRONMENT) + "\n", outcome.out)
        assertEquals(1, outcome.exit)
        assertEquals(0, run("lint --json a.ics", consoleWith("a.ics" to STANDUP)).exit)
    }

    @Test
    fun anErrorOfAnOperationIsExitTwoWithTheSentenceOnTheStandardError() {
        val outcome = run("tz Mars/Olympus")
        assertEquals(2, outcome.exit)
        assertEquals("", outcome.out)
        assertTrue(outcome.err.startsWith("calendrum: "), outcome.err)
        assertTrue("OPS-UNKNOWN-ZONE" in outcome.err, outcome.err)
        assertEquals(2, run("explain ICAL-X999").exit)
        assertEquals(2, run("expand a.ics $WINDOW --zone Mars/Olympus", consoleWith("a.ics" to WEEKLY)).exit)
    }

    @Test
    fun withJsonAnErrorStillPrintsTheAnswerAndExitsWithTwo() {
        val outcome = run("tz --json Mars/Olympus")
        assertEquals(2, outcome.exit)
        assertEquals(Ops.call("tz_resolve", """{"name":"Mars/Olympus"}""", FIXED_ENVIRONMENT) + "\n", outcome.out)
        assertTrue("OPS-UNKNOWN-ZONE" in outcome.err)
    }

    @Test
    fun anOutputThatIsNotCompleteIsExitOneAndSaysWhichOptionHelps() {
        val outcome = run("expand a.ics $WINDOW --limit 2", consoleWith("a.ics" to WEEKLY))
        assertEquals(1, outcome.exit)
        assertEquals(2, outcome.out.trimEnd().lines().size)
        assertTrue("The output is incomplete" in outcome.err, outcome.err)
        assertTrue("--limit" in outcome.err && "narrow the window" in outcome.err, outcome.err)
        val json = run("expand --json a.ics $WINDOW --limit 2", consoleWith("a.ics" to WEEKLY))
        assertEquals(1, json.exit)
        assertTrue("\"truncated\":true" in json.out)
        assertTrue("--limit" in json.err)
    }

    @Test
    fun aLimitThatIsNotReachedIsExitZero() {
        val outcome = run("expand a.ics $WINDOW --limit 4", consoleWith("a.ics" to WEEKLY))
        assertEquals(0, outcome.exit)
        assertEquals("", outcome.err)
        assertEquals(4, outcome.out.trimEnd().lines().size)
    }

    @Test
    fun truncationNotesNameTheOptionOnlyWhereThereIsOne() {
        val diff = COMMANDS.first { it.name == "diff" }
        val convert = COMMANDS.first { it.name == "convert" }
        val empty = JsonObject(emptyMap())
        assertTrue("--limit" in truncationNote(diff, empty))
        assertTrue("--limit" !in truncationNote(convert, empty))
        val reader = Json.parseToJsonElement("""{"problems":[{"code":"ICAL-S010"}]}""").jsonObject
        assertTrue("no option changes" in truncationNote(convert, reader))
        assertTrue("--limit" in truncationNote(diff, reader) && "reader" in truncationNote(diff, reader))
    }

    @Test
    fun diffIsExitOneOnlyWhenTheCalendarsDiffer() {
        val same = run(
            "diff a.ics b.ics",
            consoleWith(
                "a.ics" to STANDUP,
                "b.ics" to STANDUP.replace("DTSTAMP:20260101", "DTSTAMP:20260102"),
            ),
        )
        assertEquals(0, same.exit)
        assertEquals("no differences (1 unchanged)\n", same.out)
        val other = run(
            "diff a.ics b.ics",
            consoleWith(
                "a.ics" to STANDUP,
                "b.ics" to STANDUP.replace("Standup", "Retro"),
            ),
        )
        assertEquals(1, other.exit)
        assertEquals("~ VEVENT a@example.com\n0 unchanged\n", other.out)
        val removed = run(
            "diff a.ics b.ics",
            consoleWith(
                "a.ics" to STANDUP,
                "b.ics" to calendar("BEGIN:VCALENDAR", "VERSION:2.0", "PRODID:x", "END:VCALENDAR"),
            ),
        )
        assertEquals(1, removed.exit)
        assertTrue(removed.out.startsWith("- VEVENT a@example.com\n"), removed.out)
        assertEquals(
            1,
            run(
                "diff --json a.ics b.ics",
                consoleWith(
                    "a.ics" to STANDUP,
                    "b.ics" to STANDUP.replace("Standup", "Retro"),
                ),
            ).exit,
        )
    }

    @Test
    fun theTwoFilesOfDiffGoToTheRightFields() {
        val a = STANDUP
        val b = STANDUP.replace("Standup", "Retro")
        val outcome = run("diff --json a.ics b.ics", consoleWith("a.ics" to a, "b.ics" to b))
        val request = """{"before_base64":"${kotlin.io.encoding.Base64.encode(a.encodeToByteArray())}",""" +
            """"after_base64":"${kotlin.io.encoding.Base64.encode(b.encodeToByteArray())}"}"""
        assertEquals(Ops.call("diff", request, FIXED_ENVIRONMENT) + "\n", outcome.out)
    }

    @Test
    fun theStandardInputIsADash() {
        val outcome = run("lint -", consoleWith(stdin = NO_STAMP))
        assertEquals(1, outcome.exit)
        assertTrue(outcome.out.startsWith("<stdin>: ICAL-L001"), outcome.out)
        assertEquals(0, run("diff - b.ics", consoleWith("b.ics" to STANDUP, stdin = STANDUP)).exit)
    }

    // The ways of using it wrongly.

    @Test
    fun wrongUseIsExitTwoWithAHint() {
        val outcome = run("lint")
        assertEquals(2, outcome.exit)
        assertEquals("calendrum: No file given\nTry 'calendrum --help'.\n", outcome.err)
        assertEquals("", outcome.out)
        assertEquals(2, run("").exit)
        assertEquals(2, run("lint --nope a.ics", consoleWith("a.ics" to STANDUP)).exit)
    }

    @Test
    fun theValuesOfOptionsAreCheckedBeforeAnyFileIsRead() {
        for (line in listOf(
            "lint --fail-on info a.ics",
            "lint --limit many a.ics",
            "convert --from xml a.ics",
            "anonymize --shift-weeks 1.5 a.ics",
        )) {
            val outcome = run(line)
            assertEquals(2, outcome.exit, line)
            assertTrue(
                "Try 'calendrum --help'." in outcome.err && "Cannot read" !in outcome.err,
                "$line: ${outcome.err}",
            )
        }
    }

    @Test
    fun conflictingOptionsAreRefused() {
        val files = consoleWith("a.ics" to MAILTO)
        for (line in listOf(
            "fix --in-place -o out.ics a.ics",
            "fix --in-place --json a.ics",
            "fix --in-place -",
            "format --in-place -",
            "lint --force a.ics",
            "diff - -",
        )) {
            val outcome = run(line, files)
            assertEquals(2, outcome.exit, line)
            assertEquals("", outcome.out, line)
            assertTrue(files.written.isEmpty(), line)
        }
        assertEquals(
            "a.ics" to MAILTO.encodeToByteArray().decodeToString(),
            "a.ics" to files.files.getValue("a.ics").decodeToString(),
        )
    }

    @Test
    fun aFileThatCannotBeReadIsExitTwo() {
        val outcome = run("lint missing.ics")
        assertEquals(2, outcome.exit)
        assertEquals("calendrum: Cannot read 'missing.ics': No such file or directory\n", outcome.err)
    }

    @Test
    fun aFileAboveTheLimitIsExitTwo() {
        val console = FakeConsole()
        console.files["big.ics"] = ByteArray(64 * 1024 * 1024 + 1)
        val outcome = run("lint big.ics", console)
        assertEquals(2, outcome.exit)
        assertEquals("calendrum: 'big.ics' is larger than 64 MiB\n", outcome.err)
    }

    @Test
    fun aNameWithAnEscapeIsNotPrintedAsItIs() {
        val outcome = run("lint \u001B[2Jx.ics")
        assertEquals(2, outcome.exit)
        assertTrue('\u001B' !in outcome.err, outcome.err)
        assertTrue("\\x1B[2Jx.ics" in outcome.err, outcome.err)
    }

    // Writing files.

    @Test
    fun theResultGoesToTheStandardOutputByDefault() {
        val console = consoleWith("a.ics" to MAILTO)
        val outcome = run("fix a.ics", console)
        assertEquals(0, outcome.exit)
        assertTrue("ATTENDEE:mailto:a@example.com" in outcome.out, outcome.out)
        assertTrue(console.written.isEmpty())
        assertEquals("a.ics: lint ICAL-L024 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]\n", outcome.err)
        assertEquals(MAILTO, console.files.getValue("a.ics").decodeToString())
    }

    @Test
    fun anOutputFileIsNotReplacedWithoutForce() {
        val console = consoleWith("a.ics" to MAILTO, "out.ics" to "precious")
        val refused = run("fix a.ics -o out.ics", console)
        assertEquals(2, refused.exit)
        assertEquals("", refused.out)
        assertEquals("calendrum: 'out.ics' already exists: use --force to replace it\n", refused.err)
        assertEquals("precious", console.files.getValue("out.ics").decodeToString())
        console.errors.clear()
        val forced = run("fix a.ics -o out.ics --force", console)
        assertEquals(0, forced.exit)
        assertTrue("ATTENDEE:mailto:" in console.files.getValue("out.ics").decodeToString())
        // The standard output is free, so it has the report.
        assertEquals("a.ics: lint ICAL-L024 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]\n", forced.out)
        assertEquals("", forced.err)
    }

    @Test
    fun aNewOutputFileIsWritten() {
        val console = consoleWith("a.ics" to STANDUP)
        val outcome = run("format a.ics -o out.ics", console)
        assertEquals(0, outcome.exit)
        assertEquals("", outcome.out)
        assertEquals(STANDUP, console.files.getValue("out.ics").decodeToString())
    }

    @Test
    fun aDashAfterTheOutputOptionIsTheStandardOutput() {
        val console = consoleWith("a.ics" to STANDUP)
        val outcome = run("format a.ics -o -", console)
        assertEquals(0, outcome.exit)
        assertEquals(STANDUP, outcome.out)
        assertTrue(console.written.isEmpty())
    }

    @Test
    fun aFailedWriteIsExitTwoAndSaysWhy() {
        val console = consoleWith("a.ics" to STANDUP)
        console.writeResult = FileWrite.Failed("Permission denied")
        val outcome = run("format a.ics -o out.ics", console)
        assertEquals(2, outcome.exit)
        assertEquals("calendrum: Cannot write 'out.ics': Permission denied\n", outcome.err)
        val inPlace = run(
            "fix --in-place a.ics",
            consoleWith("a.ics" to MAILTO).also {
                it.writeResult =
                    FileWrite.Failed("Permission denied")
            },
        )
        assertEquals(2, inPlace.exit)
        assertEquals("calendrum: Cannot write 'a.ics': Permission denied\n", inPlace.err)
    }

    @Test
    fun inPlaceReplacesTheFileAndListsTheChanges() {
        val console = consoleWith("a.ics" to MAILTO)
        val outcome = run("fix --in-place a.ics", console)
        assertEquals(0, outcome.exit)
        assertTrue("ATTENDEE:mailto:" in console.files.getValue("a.ics").decodeToString())
        assertEquals(listOf("a.ics"), console.written)
        assertEquals("a.ics: lint ICAL-L024 VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]\n", outcome.out)
    }

    @Test
    fun inPlaceLeavesAFileThatNeedsNothing() {
        val console = consoleWith("a.ics" to STANDUP)
        val outcome = run("fix --in-place a.ics", console)
        assertEquals(0, outcome.exit)
        assertTrue(console.written.isEmpty())
        assertEquals("a.ics: nothing to fix\n", outcome.out)
        val format = run("format --in-place a.ics", console)
        assertEquals(0, format.exit)
        assertTrue(console.written.isEmpty())
    }

    @Test
    fun formatInPlaceRewritesAFileThatIsNotCanonical() {
        val console = consoleWith("a.ics" to STANDUP.replace("\r\n", "\n"))
        val outcome = run("format --in-place a.ics", console)
        assertEquals(0, outcome.exit)
        assertEquals(listOf("a.ics"), console.written)
        assertEquals(STANDUP, console.files.getValue("a.ics").decodeToString())
        assertTrue("ICAL-S003" in outcome.err)
    }

    @Test
    fun anOperationThatFailsWritesNothing() {
        val console = consoleWith("a.ics" to STANDUP)
        val outcome = run("expand a.ics --from 2026-02-01T00:00:00Z --to 2026-01-01T00:00:00Z -o out.txt", console)
        assertEquals(2, outcome.exit)
        assertTrue(console.written.isEmpty())
        assertTrue("out.txt" !in console.files)
    }

    @Test
    fun aBrokenStandardOutputIsExitTwo() {
        val console = consoleWith("a.ics" to STANDUP)
        console.outputFails = true
        val outcome = run("expand a.ics $WINDOW", console)
        assertEquals(2, outcome.exit)
        assertTrue("Cannot write to the standard output: Broken pipe" in outcome.err, outcome.err)
        console.outputFails = true
        assertEquals(2, run("--version", console).exit)
    }

    // Each command.

    @Test
    fun formatGivesTheCanonicalFormAndFixGivesTheRepairs() {
        assertEquals(STANDUP, run("format a.ics", consoleWith("a.ics" to STANDUP.replace("\r\n", "\n"))).out)
        val fixed = run("fix --fixups lint a.ics", consoleWith("a.ics" to MAILTO))
        assertTrue("mailto:a@example.com" in fixed.out)
        val none = run("fix --fixups= a.ics", consoleWith("a.ics" to MAILTO))
        assertTrue("MAILTO:a@example.com" in none.out, none.out)
        assertEquals(2, run("fix --fixups nothing a.ics", consoleWith("a.ics" to MAILTO)).exit)
    }

    @Test
    fun convertGoesToJcalAndBack() {
        val toJcal = run("convert a.ics", consoleWith("a.ics" to STANDUP))
        assertEquals(0, toJcal.exit)
        assertTrue(toJcal.out.startsWith("[[\"vcalendar\","), toJcal.out)
        assertTrue(toJcal.out.endsWith("\n"))
        val back = run("convert a.json", consoleWith("a.json" to toJcal.out))
        assertEquals(0, back.exit)
        assertTrue(back.out.startsWith("BEGIN:VCALENDAR\r\n"), back.out)
        assertTrue("SUMMARY:Standup" in back.out)
        val named = run("convert --from jcal -", consoleWith(stdin = toJcal.out))
        assertEquals(back.out, named.out)
        val notJcal = run("convert --from ics a.json", consoleWith("a.json" to STANDUP))
        assertTrue(notJcal.out.startsWith("[[\"vcalendar\","))
    }

    @Test
    fun aFileThatIsNotJsonIsExitTwo() {
        val outcome = run("convert a.json", consoleWith("a.json" to "[\"vcalendar\", "))
        assertEquals(2, outcome.exit)
        assertEquals("calendrum: 'a.json' is not valid JSON\n", outcome.err)
        val deep = run("convert a.json", consoleWith("a.json" to "[".repeat(200_000)))
        assertEquals(2, deep.exit)
        assertEquals("calendrum: 'a.json' is nested too deep\n", deep.err)
        assertEquals(
            2,
            run("convert a.json", FakeConsole(files = mutableMapOf("a.json" to byteArrayOf(0xFF.toByte())))).exit,
        )
    }

    @Test
    fun expandPrintsOneLineForEachOccurrence() {
        val outcome = run("expand a.ics $WINDOW --zone Europe/Berlin", consoleWith("a.ics" to WEEKLY))
        assertEquals(0, outcome.exit)
        assertEquals(
            "2026-01-05T10:00:00Z 2026-01-05T11:00:00Z Weekly [weekly@example.com] local 2026-01-05T11:00:00\n" +
                "2026-01-12T10:00:00Z 2026-01-12T11:00:00Z Weekly [weekly@example.com] local 2026-01-12T11:00:00\n" +
                "2026-01-19T10:00:00Z 2026-01-19T11:00:00Z Weekly [weekly@example.com] local 2026-01-19T11:00:00\n" +
                "2026-01-26T10:00:00Z 2026-01-26T11:00:00Z Weekly [weekly@example.com] local 2026-01-26T11:00:00\n",
            outcome.out,
        )
    }

    @Test
    fun withoutAZoneTheTimesAreInUtcAndNothingIsLocal() {
        val outcome = run("expand a.ics $WINDOW", consoleWith("a.ics" to WEEKLY))
        assertTrue("local" !in outcome.out, outcome.out)
        assertTrue(outcome.out.startsWith("2026-01-05T10:00:00Z 2026-01-05T11:00:00Z Weekly"), outcome.out)
    }

    @Test
    fun theWindowOfExpandIsRequiredByTheOperation() {
        val outcome = run("expand a.ics", consoleWith("a.ics" to WEEKLY))
        assertEquals(2, outcome.exit)
        assertTrue("OPS-BAD-ARGUMENT" in outcome.err, outcome.err)
    }

    @Test
    fun anonymizeKeepsTheStructureAndDropsTheText() {
        val text = STANDUP.replace("Standup", "Salary talk with Alice")
        val first = run("anonymize a.ics --shift-weeks 2", consoleWith("a.ics" to text))
        assertEquals(0, first.exit)
        assertTrue("Salary" !in first.out && "Alice" !in first.out, first.out)
        assertTrue("DTSTART:20260119T100000Z" in first.out, first.out)
        assertEquals(2, run("anonymize a.ics --shift-weeks 100000", consoleWith("a.ics" to text)).exit)
    }

    @Test
    fun tzAndExplainPrintTheirAnswers() {
        val zone = run("tz Europe/Berlin --at 2026-01-15T12:00:00Z")
        assertEquals(
            "zone: Europe/Berlin\nat: 2026-01-15T12:00:00Z\noffset: +01:00 (3600 s)\n" +
                "next change: 2026-03-29T01:00:00Z, offset then +02:00\n",
            zone.out.substringBefore("tzdb:"),
        )
        assertTrue(zone.out.contains("tzdb: "))
        val console = FakeConsole()
        val windows =
            Outcome(
                runCli(
                    listOf("tz", "W. Europe Standard Time", "--at", "2026-07-15T12:00:00Z"),
                    console,
                    FIXED_ENVIRONMENT,
                ),
                console,
            )
        assertEquals(0, windows.exit, windows.err)
        assertTrue("zone: Europe/Berlin (the identifier of the name given)" in windows.out, windows.out)
        assertTrue("offset: +02:00" in windows.out, windows.out)
        val explained = run("explain ical-s003")
        assertEquals(0, explained.exit)
        assertTrue(explained.out.startsWith("ICAL-S003 warning BareLf\nA bare LF"), explained.out)
    }

    @Test
    fun theDefaultMomentOfTzIsTheClockOfTheEnvironment() {
        val outcome = run("tz Asia/Tokyo")
        assertTrue("at: 2026-01-01T00:00:00Z" in outcome.out, outcome.out)
        assertTrue("offset: +09:00" in outcome.out, outcome.out)
        assertTrue("next change: none known" in outcome.out, outcome.out)
    }

    @Test
    fun helpAndVersionGoToTheStandardOutputWithExitZero() {
        val help = run("--help")
        assertEquals(0, help.exit)
        assertTrue(help.out.startsWith("calendrum $CLI_VERSION"), help.out)
        assertEquals("", help.err)
        val version = run("--version")
        assertEquals("calendrum $CLI_VERSION\nlibrary $LIBRARY_VERSION\ncontract 1\n", version.out)
        val command = run("expand --help")
        assertEquals(0, command.exit)
        assertTrue(command.out.startsWith("Usage: calendrum expand"), command.out)
    }

    // Text from a calendar and the terminal.

    @Test
    fun anEscapeFromTheCalendarNeverReachesTheHumanOutput() {
        val hostile = escaped("Stand\u001B]0;pwn\u0007up\u009B31m\u007F")
        val console = consoleWith("a.ics" to hostile)
        val outcome = run("expand a.ics $WINDOW", console)
        assertEquals(0, outcome.exit)
        for (char in listOf(
            '\u001B',
            '\u0007',
            '\u009B',
            '\u007F',
        )) {
            assertTrue(char !in outcome.out, "U+${char.code.toString(16)}")
        }
        assertTrue("Stand\\x1B]0;pwn\\x07up\\x9B31m\\x7F" in outcome.out, outcome.out)
        val diff = run(
            "diff a.ics b.ics",
            consoleWith(
                "a.ics" to hostile,
                "b.ics" to hostile.replace("UID:a@", "UID:\u001B[2Jb@"),
            ),
        )
        assertTrue('\u001B' !in diff.out, diff.out)
    }

    @Test
    fun theHumanOutputIsSafeAlsoInAPipe() {
        val hostile = escaped("Stand\u001B[2Jup")
        val piped = run("expand a.ics $WINDOW", consoleWith("a.ics" to hostile, terminal = false))
        assertTrue('\u001B' !in piped.out)
    }

    @Test
    fun aCalendarGoesByteForByteToAPipeAndAFile() {
        val hostile = escaped("Stand\u001B]0;pwn\u0007up")
        val console = consoleWith("a.ics" to hostile, terminal = false)
        val piped = run("format a.ics", console)
        assertContentEquals(hostile.encodeToByteArray(), piped.console.outputBytes)
        val toFile = run("format a.ics -o out.ics", console)
        assertEquals(0, toFile.exit)
        assertContentEquals(hostile.encodeToByteArray(), console.files.getValue("out.ics"))
    }

    @Test
    fun aCalendarOnATerminalIsSafeExceptForItsLineBreaks() {
        val hostile = escaped("Stand\u001B]0;pwn\u0007up\u009B")
        val outcome = run("format a.ics", consoleWith("a.ics" to hostile, terminal = true))
        assertTrue('\u001B' !in outcome.out && '\u009B' !in outcome.out && '\u0007' !in outcome.out)
        assertTrue("SUMMARY:Stand\\x1B]0;pwn\\x07up\\x9B\r\n" in outcome.out, outcome.out)
        assertEquals(
            STANDUP.count {
                it == '\r'
            },
            run("format a.ics", consoleWith("a.ics" to STANDUP, terminal = true)).out.count {
                it ==
                    '\r'
            },
        )
        val toFile = consoleWith("a.ics" to hostile, terminal = true)
        run("format a.ics -o out.ics", toFile)
        assertTrue('\u001B' in toFile.files.getValue("out.ics").decodeToString())
    }

    @Test
    fun jsonOnATerminalKeepsTheSameValueWithoutRawControls() {
        val hostile = escaped("a\u009Bb\u007Fc\u202Ed")
        val files = arrayOf("a.ics" to hostile)
        val terminal = run("expand --json a.ics $WINDOW", consoleWith(*files, terminal = true))
        val pipe = run("expand --json a.ics $WINDOW", consoleWith(*files, terminal = false))
        assertTrue('\u009B' !in terminal.out && '\u007F' !in terminal.out && '\u202E' !in terminal.out, terminal.out)
        assertEquals(Json.parseToJsonElement(pipe.out), Json.parseToJsonElement(terminal.out))
        val summary = Json.parseToJsonElement(terminal.out).jsonObject.getValue("result").jsonObject
        assertTrue("a\u009Bb\u007Fc\u202Ed" in summary.toString())
        assertEquals(JsonPrimitive(true), Json.parseToJsonElement(terminal.out).jsonObject["ok"])
    }

    @Test
    fun convertToJcalOnATerminalIsSafeToo() {
        val hostile = escaped("a\u009Bb\u202Ec")
        val outcome = run("convert a.ics", consoleWith("a.ics" to hostile, terminal = true))
        assertEquals(0, outcome.exit, outcome.err)
        assertTrue('\u009B' !in outcome.out && '\u202E' !in outcome.out, outcome.out)
        assertTrue("a\\x9Bb\\u202Ec" in outcome.out, outcome.out)
        val piped = run("convert a.ics", consoleWith("a.ics" to hostile, terminal = false))
        assertTrue('\u009B' in piped.out)
    }

    @Test
    fun noEnvironmentIsReadAndTheAnonymizerKeyIsNeverShown() {
        val first = run("anonymize --json a.ics", consoleWith("a.ics" to STANDUP))
        val second = run("anonymize --json a.ics", consoleWith("a.ics" to STANDUP))
        // The key is the fixed bytes of the test environment, and it is nowhere in the output.
        val key = ByteArray(32) { it.toByte() }
        assertTrue(key.decodeToString() !in first.out)
        assertEquals(first.out, second.out)
        assertTrue("key" !in first.out.lowercase())
    }

    // Data that must not be lost.

    private val skippedLine = STANDUP.replace("SUMMARY:Standup\r\n", "SUMMARY:Standup\r\nthis line has no colon\r\n")

    private val noText = listOf("notes.txt" to "These are my notes.\nNot a calendar at all.\n", "empty.ics" to "")

    @Test
    fun inPlaceNeverReplacesAFileWithAResultThatLostSomething() {
        for ((command, input) in listOf(
            "format" to skippedLine,
            "fix" to skippedLine,
            "fix --fixups lint" to skippedLine,
        )) {
            val console = consoleWith("a.ics" to input)
            val outcome = run("$command --in-place a.ics", console)
            assertEquals(2, outcome.exit, command)
            assertEquals(input, console.files.getValue("a.ics").decodeToString(), command)
            assertTrue(console.written.isEmpty(), command)
            assertEquals("", outcome.out, command)
            assertTrue("a.ics:1:" !in outcome.err || "ICAL-S007" in outcome.err, outcome.err)
            assertTrue("ICAL-S007" in outcome.err, outcome.err)
            assertTrue("Nothing was written: 'a.ics' is as it was." in outcome.err, outcome.err)
            assertTrue("-o" in outcome.err, outcome.err)
        }
    }

    @Test
    fun aForcedOutputFileIsNeverReplacedWithAResultThatLostSomething() {
        for (command in listOf("format", "fix", "convert")) {
            val console = consoleWith("a.ics" to skippedLine, "out.ics" to "precious")
            val outcome = run("$command a.ics -o out.ics --force", console)
            assertEquals(2, outcome.exit, command)
            assertEquals("precious", console.files.getValue("out.ics").decodeToString(), command)
            assertTrue(console.written.isEmpty(), command)
            assertTrue("Nothing was written: 'out.ics' is as it was." in outcome.err, outcome.err)
        }
        // Not even a name that is free: --force is a request to replace, and a lossy result does not replace.
        val free = consoleWith("a.ics" to skippedLine)
        assertEquals(2, run("format a.ics -o new.ics --force", free).exit)
        assertTrue("new.ics" !in free.files)
    }

    @Test
    fun aNewOutputFileWithoutForceGetsTheResultAndExitOne() {
        val console = consoleWith("a.ics" to skippedLine)
        val outcome = run("format a.ics -o new.ics", console)
        assertEquals(1, outcome.exit)
        assertTrue("new.ics" in console.files)
        assertTrue(LOSS_NOTE in outcome.err, outcome.err)
        assertTrue("ICAL-S007" in outcome.err)
    }

    @Test
    fun aResultThatLostSomethingGoesToTheStandardOutputWithExitOne() {
        // anonymize is not here: its answer has no list of problems, so a skipped line cannot be seen from it.
        for (command in listOf("fix", "format", "convert", "expand $WINDOW")) {
            val outcome = run("$command a.ics", consoleWith("a.ics" to skippedLine))
            assertEquals(1, outcome.exit, command)
            assertTrue(outcome.out.isNotEmpty(), command)
            assertTrue(LOSS_NOTE in outcome.err || "incomplete" in outcome.err, "$command: ${outcome.err}")
        }
    }

    @Test
    fun theSameCodesComeWithJson() {
        val console = consoleWith("a.ics" to skippedLine)
        val outcome = run("format --json a.ics", console)
        assertEquals(1, outcome.exit)
        assertTrue("\"problems\"" in outcome.out)
        assertTrue(LOSS_NOTE in outcome.err)
        val forced = run(
            "format --json a.ics -o out.json --force",
            consoleWith(
                "a.ics" to skippedLine,
                "out.json" to "precious",
            ),
        )
        assertEquals(2, forced.exit)
        assertEquals("", forced.out)
    }

    @Test
    fun aResultThatLostNothingStillReplacesTheFile() {
        // A bare LF, which the reader takes as a line end, loses nothing.
        val console = consoleWith("a.ics" to STANDUP.replace("\r\n", "\n"))
        assertEquals(0, run("format --in-place a.ics", console).exit)
        assertEquals(STANDUP, console.files.getValue("a.ics").decodeToString())
        // A byte order mark and a blank line too.
        val marked = consoleWith("a.ics" to "\uFEFF" + STANDUP.replace("\r\nVERSION", "\r\n\r\nVERSION"))
        val outcome = run("format --in-place a.ics", marked)
        assertEquals(0, outcome.exit, outcome.err)
        assertEquals(STANDUP, marked.files.getValue("a.ics").decodeToString())
    }

    @Test
    fun aTruncatedResultIsNotWrittenOverAFile() {
        val console = consoleWith("a.ics" to WEEKLY, "out.txt" to "precious")
        val outcome = run("expand a.ics $WINDOW --limit 2 -o out.txt --force", console)
        assertEquals(2, outcome.exit)
        assertEquals("precious", console.files.getValue("out.txt").decodeToString())
        assertTrue("The output is incomplete" in outcome.err)
        assertTrue("Nothing was written" in outcome.err)
    }

    @Test
    fun anInputWithNoCalendarIsNeverWritten() {
        val variants = mapOf(
            "fix" to listOf("", "--in-place", "-o out.ics", "-o out.ics --force"),
            "format" to listOf("", "--in-place", "-o out.ics", "-o out.ics --force"),
            "convert" to listOf("", "-o out.ics", "-o out.ics --force"),
            "anonymize" to listOf("", "-o out.ics", "-o out.ics --force"),
        )
        for ((name, text) in noText) {
            for ((command, extras) in variants) {
                for (extra in extras) {
                    val label = "$command $extra $name"
                    val console = consoleWith(name to text, "out.ics" to "precious")
                    val outcome = run("$command $extra $name".replace("  ", " "), console)
                    assertEquals(2, outcome.exit, label)
                    assertEquals("", outcome.out, label)
                    assertTrue(console.written.isEmpty(), label)
                    assertEquals(text, console.files.getValue(name).decodeToString(), label)
                    assertEquals("precious", console.files.getValue("out.ics").decodeToString(), label)
                    assertTrue(
                        "No calendar found in '$name': nothing was written" in outcome.err,
                        "$label: ${outcome.err}",
                    )
                }
            }
        }
    }

    @Test
    fun anEmptyJcalIsNoCalendarEither() {
        val outcome = run("convert a.json -o out.ics", consoleWith("a.json" to "[]"))
        assertEquals(2, outcome.exit)
        assertEquals("", outcome.out)
        assertTrue("No calendar found" in outcome.err)
    }

    @Test
    fun withJsonNoCalendarPrintsTheAnswerAndTheSameCode() {
        val console = consoleWith("empty.ics" to "")
        val outcome = run("format --json empty.ics", console)
        assertEquals(2, outcome.exit)
        assertTrue(outcome.out.startsWith("{\"ops_version\":1,\"ok\":true"), outcome.out)
        assertTrue("No calendar found" in outcome.err)
        val toFile = consoleWith("empty.ics" to "", "out.json" to "precious")
        assertEquals(2, run("format --json empty.ics -o out.json --force", toFile).exit)
        assertEquals("precious", toFile.files.getValue("out.json").decodeToString())
    }

    @Test
    fun anErrorWithJsonIsNeverWrittenToAFile() {
        val console = consoleWith("a.ics" to WEEKLY, "out.json" to "precious")
        val outcome =
            run(
                "expand --json a.ics --from 2026-02-01T00:00:00Z --to 2026-01-01T00:00:00Z -o out.json --force",
                console,
            )
        assertEquals(2, outcome.exit)
        assertTrue("\"ok\":false" in outcome.out)
        assertEquals("precious", console.files.getValue("out.json").decodeToString())
    }

    @Test
    fun lintOfAnInputWithNoCalendarIsAFindingOfLevelError() {
        for ((name, text) in noText) {
            for (rules in listOf("lint", "rfc5545")) {
                for (failOn in listOf("warning", "error")) {
                    val outcome = run("lint --rules $rules --fail-on $failOn $name", consoleWith(name to text))
                    assertEquals(1, outcome.exit, "$name $rules $failOn")
                    assertTrue("$name: error no calendar in the input" in outcome.out, outcome.out)
                    assertTrue(
                        outcome.out.trimEnd().endsWith("(1 error, 0 warnings, 0 info)") || "errors" in outcome.out,
                        outcome.out,
                    )
                }
            }
        }
        assertEquals(1, run("lint --json empty.ics", consoleWith("empty.ics" to "")).exit)
    }

    @Test
    fun theBomOfAJcalFileIsSkipped() {
        val jcal = run("convert a.ics", consoleWith("a.ics" to STANDUP)).out
        val outcome = run("convert a.json", consoleWith("a.json" to "\uFEFF$jcal"))
        assertEquals(0, outcome.exit, outcome.err)
        assertTrue("SUMMARY:Standup" in outcome.out)
    }
}
