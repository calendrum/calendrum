package io.github.calendrum.tooling.cli

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ArgumentsTest {
    private fun parse(line: String): Parsed = assertIs<Invocation.Run>(parseArguments(words(line))).parsed

    private fun usage(line: String): String = assertFailsWith<UsageError> { parseArguments(words(line)) }.text

    @Test
    fun everyCommandOfTheTableIsThere() {
        assertEquals(
            listOf("lint", "fix", "format", "convert", "expand", "diff", "anonymize", "tz", "explain"),
            COMMANDS.map { it.name },
        )
    }

    @Test
    fun aFileAndOptionsAreSeparated() {
        val parsed = parse("lint --rules rfc5545 --fail-on=error a.ics")
        assertEquals(listOf("a.ics"), parsed.positionals)
        assertEquals("rfc5545", parsed.value("rules"))
        assertEquals("error", parsed.value("fail-on"))
    }

    @Test
    fun optionsMayComeBeforeOrAfterTheFile() {
        assertEquals("1", parse("expand a.ics $WINDOW --limit 1").value("limit"))
        assertEquals("1", parse("expand --limit 1 a.ics $WINDOW").value("limit"))
    }

    @Test
    fun aDashIsTheStandardInput() {
        assertEquals(listOf("-"), parse("lint -").positionals)
        assertEquals(listOf("-", "after.ics"), parse("diff - after.ics").positionals)
    }

    @Test
    fun theShortOutputOptionTakesItsValueInBothForms() {
        assertEquals("out.ics", parse("fix -o out.ics a.ics").value("output"))
        assertEquals("out.ics", parse("fix -oout.ics a.ics").value("output"))
        assertEquals("out.ics", parse("fix --output out.ics a.ics").value("output"))
    }

    @Test
    fun aValueMayStartWithADash() {
        assertEquals("-2", parse("anonymize --shift-weeks -2 a.ics").value("shift-weeks"))
        assertEquals("-2", parse("anonymize --shift-weeks=-2 a.ics").value("shift-weeks"))
    }

    @Test
    fun twoDashesEndTheOptions() {
        val parsed = parse("lint -- --odd.ics")
        assertEquals(listOf("--odd.ics"), parsed.positionals)
    }

    @Test
    fun switchesAreRemembered() {
        val parsed = parse("fix --in-place --json a.ics")
        assertTrue(parsed.has("in-place"))
        assertTrue(parsed.has("json"))
        assertTrue(!parsed.has("force"))
    }

    @Test
    fun tzAndExplainTakeAWordAndNoFile() {
        assertEquals(listOf("Europe/Berlin"), parse("tz Europe/Berlin --at 2026-01-01T00:00:00Z").positionals)
        assertEquals(listOf("ICAL-S003"), parse("explain ICAL-S003").positionals)
    }

    @Test
    fun helpAndVersionAreRequestsAndNotCommands() {
        assertIs<Invocation.Help>(parseArguments(listOf("--help")))
        assertIs<Invocation.Help>(parseArguments(listOf("-h")))
        assertIs<Invocation.Version>(parseArguments(listOf("--version")))
        assertEquals("lint", assertIs<Invocation.CommandHelp>(parseArguments(listOf("lint", "--help"))).command.name)
        assertEquals("diff", assertIs<Invocation.CommandHelp>(parseArguments(listOf("diff", "-h"))).command.name)
    }

    @Test
    fun wrongUseIsNamedWithoutQuotingEscapes() {
        assertEquals("No command given", usage(""))
        assertEquals("Unknown command 'lnt'", usage("lnt a.ics"))
        assertEquals(
            "Unknown command '\\x1B[2J'",
            assertFailsWith<UsageError> {
                parseArguments(listOf("\u001B[2J"))
            }.text,
        )
        assertEquals("Unknown option --colour for lint", usage("lint --colour a.ics"))
        assertEquals("Unknown option '-x' for lint", usage("lint -x a.ics"))
        assertEquals("No file given", usage("lint"))
        assertEquals("Unexpected argument 'b.ics'", usage("lint a.ics b.ics"))
        assertEquals("The option --rules needs a value", usage("lint a.ics --rules"))
        assertEquals("The option --json takes no value", usage("lint --json=yes a.ics"))
        assertEquals("The option --limit is given twice", usage("lint --limit 1 --limit 2 a.ics"))
        assertEquals("Two files are needed: the earlier calendar and the later one", usage("diff"))
        assertEquals("The later calendar is missing", usage("diff a.ics"))
        assertEquals("No time zone name given", usage("tz"))
        assertEquals("No problem code given", usage("explain"))
    }

    @Test
    fun anOptionOfAnotherCommandIsUnknown() {
        assertEquals("Unknown option --in-place for lint", usage("lint --in-place a.ics"))
        assertEquals("Unknown option --shift-weeks for fix", usage("fix --shift-weeks 1 a.ics"))
    }

    @Test
    fun theHelpOfEveryCommandNamesItsOptions() {
        for (command in COMMANDS) {
            val text = commandHelp(command)
            assertTrue(text.startsWith("Usage: ${command.synopsis}"), command.name)
            for (option in command.options) assertTrue("--${option.name}" in text, "${command.name} ${option.name}")
            assertTrue("--json" in text)
        }
        for (command in COMMANDS) assertTrue(command.name in generalHelp("1"), command.name)
    }
}
