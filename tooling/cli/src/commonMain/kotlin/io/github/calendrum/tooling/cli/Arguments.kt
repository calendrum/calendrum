package io.github.calendrum.tooling.cli

/** An option of a command. [value] names what it takes, or is `null` for a switch. */
internal class Opt(val name: String, val value: String?, val help: String, val short: Char? = null)

/**
 * A command: how it is called, what it takes, and the operation of the contract behind it.
 *
 * @property files how many files it takes, each a path or `-` for the standard input.
 * @property arguments the words it takes after its name when they are not files, such as a zone or a code.
 */
internal class Command(
    val name: String,
    val operation: String,
    val summary: String,
    val synopsis: String,
    val files: Int,
    val arguments: Int = 0,
    val options: List<Opt> = emptyList(),
    val details: List<String> = emptyList(),
) {
    val positionals: Int get() = files + arguments
}

/** The words of a command line, parsed. */
internal class Parsed(
    val command: Command,
    val positionals: List<String>,
    private val values: Map<String, String>,
    private val switches: Set<String>,
) {
    fun value(name: String): String? = values[name]

    fun has(name: String): Boolean = name in switches
}

/** A wrong command line. [text] is for the person, and a hint where to look is added to it. */
internal class UsageError(val text: String) : Exception(text)

/** What the first words of a command line ask for. */
internal sealed class Invocation {
    object Help : Invocation()

    object Version : Invocation()

    class CommandHelp(val command: Command) : Invocation()

    class Run(val parsed: Parsed) : Invocation()
}

private val OUTPUT_OPTIONS = listOf(
    Opt("json", null, "Print the answer of the operation as JSON, as the contract of the operations has it."),
    Opt("output", "FILE", "Write the output to FILE and not to the standard output. - is the standard output.", 'o'),
    Opt("force", null, "Let -o replace a file that is already there."),
    Opt("help", null, "Show this help.", 'h'),
)

private val IN_PLACE = Opt("in-place", null, "Replace the file with the result. It is written whole or not at all.")

private val LIMIT = Opt("limit", "N", "The most items in a list. 1000 by default, 100000 at most.")

internal val COMMANDS: List<Command> = listOf(
    Command(
        name = "lint",
        operation = "validate",
        summary = "Check a calendar against RFC 5545 or the rules of the linter",
        synopsis = "calendrum lint [options] FILE",
        files = 1,
        options = listOf(
            Opt(
                "rules",
                "lint|rfc5545",
                "The set of rules. lint by default: RFC 5545 and what breaks calendar clients.",
            ),
            Opt("fail-on", "warning|error", "The lowest severity that makes the exit code 1. warning by default."),
            LIMIT,
        ),
        details = listOf(
            "Prints one line for each finding, as FILE:LINE:COLUMN CODE SEVERITY MESSAGE for what the reader",
            "found, and as FILE: CODE SEVERITY MESSAGE (PATH) for what the rules found. Findings of the severity",
            "info never change the exit code.",
        ),
    ),
    Command(
        name = "fix",
        operation = "fix",
        summary = "Repair the known defects of calendar producers",
        synopsis = "calendrum fix [options] FILE",
        files = 1,
        options = listOf(
            Opt(
                "fixups",
                "A,B",
                "The repairs, in order: by_prod_id, windows_tzids, x_wr_timezone, x_wr_calname, outlook, " +
                    "lotus_notes, google, vcalendar10, lint. by_prod_id and lint by default.",
            ),
            IN_PLACE,
        ),
        details = listOf(
            "Prints the repaired calendar and lists the changes on the standard error. The output is always UTF-8.",
        ),
    ),
    Command(
        name = "format",
        operation = "fix",
        summary = "Write a calendar in its canonical form, with no repairs",
        synopsis = "calendrum format [options] FILE",
        files = 1,
        options = listOf(IN_PLACE),
        details = listOf("The same as fix with no repairs. The output is always UTF-8."),
    ),
    Command(
        name = "convert",
        operation = "convert",
        summary = "Convert between iCalendar and jCal (RFC 7265)",
        synopsis = "calendrum convert [options] FILE",
        files = 1,
        options = listOf(
            Opt("from", "ics|jcal", "The form of the input. jcal if the name ends in .json, ics otherwise."),
        ),
        details = listOf("The output is the other form."),
    ),
    Command(
        name = "expand",
        operation = "expand",
        summary = "List the occurrences in a window of time",
        synopsis = "calendrum expand --from MOMENT --to MOMENT [options] FILE",
        files = 1,
        options = listOf(
            Opt("from", "MOMENT", "The start of the window, such as 2026-01-01T00:00:00Z. Required."),
            Opt("to", "MOMENT", "The end of the window, excluded. Required."),
            Opt("zone", "ZONE", "An IANA zone. It adds local times and is the zone of floating times; UTC without it."),
            LIMIT,
            Opt("include-cancelled", null, "Also list the occurrences of cancelled components."),
        ),
        details = listOf("Prints START END SUMMARY [UID] for each occurrence, in UTC."),
    ),
    Command(
        name = "diff",
        operation = "diff",
        summary = "Compare two calendars by their components",
        synopsis = "calendrum diff [options] BEFORE AFTER",
        files = 2,
        options = listOf(LIMIT),
        details = listOf(
            "Prints + for a component that was added, - for one that was removed and ~ for one that changed. DTSTAMP does",
            "not count. The exit code is 1 if the calendars differ.",
        ),
    ),
    Command(
        name = "anonymize",
        operation = "anonymize",
        summary = "Remove text and addresses from a calendar, for a bug report",
        synopsis = "calendrum anonymize [options] FILE",
        files = 1,
        options = listOf(Opt("shift-weeks", "N", "Move every date by whole weeks, so that the days of the week stay.")),
        details = listOf(
            "The key of the pseudonyms is random for each call and is never shown: two runs on the same file give",
            "different output.",
        ),
    ),
    Command(
        name = "tz",
        operation = "tz_resolve",
        summary = "Look up a time zone by its IANA or Windows name",
        synopsis = "calendrum tz [options] NAME",
        files = 0,
        arguments = 1,
        options = listOf(Opt("at", "MOMENT", "The moment of the offset. The current time by default.")),
    ),
    Command(
        name = "explain",
        operation = "explain",
        summary = "Explain a problem code such as ICAL-S003",
        synopsis = "calendrum explain [options] CODE",
        files = 0,
        arguments = 1,
    ),
)

private fun optionsOf(command: Command): List<Opt> = command.options + OUTPUT_OPTIONS

private fun findOption(command: Command, name: String): Opt? = optionsOf(command).firstOrNull { it.name == name }

/** Reads the first words: a command, or a request for help or the version. */
internal fun parseArguments(args: List<String>): Invocation {
    val first = args.firstOrNull() ?: throw UsageError("No command given")
    when (first) {
        "--help", "-h" -> return Invocation.Help
        "--version" -> return Invocation.Version
    }
    val command = COMMANDS.firstOrNull { it.name == first }
        ?: throw UsageError("Unknown command ${quote(first)}")
    val values = LinkedHashMap<String, String>()
    val switches = LinkedHashSet<String>()
    val positionals = ArrayList<String>()
    var optionsEnded = false
    var index = 1
    fun take(option: Opt, value: String?) {
        if (option.value == null) {
            if (value != null) throw UsageError("The option --${option.name} takes no value")
            if (!switches.add(option.name)) throw UsageError("The option --${option.name} is given twice")
        } else {
            if (value == null) throw UsageError("The option --${option.name} needs a value")
            if (option.name in values) throw UsageError("The option --${option.name} is given twice")
            values[option.name] = value
        }
    }
    while (index < args.size) {
        val word = args[index++]
        when {
            optionsEnded -> positionals += word

            word == "--" -> optionsEnded = true

            word.startsWith("--") -> {
                val name = word.substring(2).substringBefore('=')
                val inline = if ('=' in word) word.substringAfter('=') else null
                val option = findOption(command, name)
                    ?: throw UsageError("Unknown option --${sanitize(name)} for ${command.name}")
                // A value may start with a dash: --shift-weeks -2.
                val value = if (option.value != null && inline == null) args.getOrNull(index++) else inline
                take(option, value)
            }

            word.length > 1 && word[0] == '-' -> {
                val option = optionsOf(command).firstOrNull { it.short == word[1] }
                    ?: throw UsageError("Unknown option ${quote(word.substring(0, 2))} for ${command.name}")
                val attached = word.substring(2).ifEmpty { null }
                val value = when {
                    option.value == null -> null
                    attached != null -> attached
                    else -> args.getOrNull(index++)
                }
                if (option.value == null &&
                    attached != null
                ) {
                    throw UsageError("Write ${quote("-" + word[1])} on its own")
                }
                take(option, value)
            }

            else -> positionals += word
        }
    }
    if ("help" in switches) return Invocation.CommandHelp(command)
    if (positionals.size < command.positionals) {
        throw UsageError(missingText(command, positionals.size))
    }
    if (positionals.size > command.positionals) {
        throw UsageError("Unexpected argument ${quote(positionals[command.positionals])}")
    }
    return Invocation.Run(Parsed(command, positionals, values, switches))
}

private fun missingText(command: Command, given: Int): String = when {
    command.files == 2 && given == 0 -> "Two files are needed: the earlier calendar and the later one"
    command.files == 2 -> "The later calendar is missing"
    command.files == 1 -> "No file given"
    command.name == "tz" -> "No time zone name given"
    else -> "No problem code given"
}

private fun quote(text: String): String = "'" + sanitize(text) + "'"

internal fun generalHelp(version: String): String = buildString {
    append("calendrum $version: a command line for iCalendar files\n\n")
    append("Usage: calendrum COMMAND [options] [FILE]\n\n")
    append("Commands:\n")
    for (command in COMMANDS) append("  ").append(command.name.padEnd(NAME_WIDTH)).append(command.summary).append('\n')
    append("\nA file named - is the standard input. Try calendrum COMMAND --help for the options of a command.\n")
    append("\nExit codes: 0 all is well; 1 there is something to report (the linter found a problem, two calendars\n")
    append("differ, the output is incomplete); 2 the command could not do its work.\n")
    append("\nOptions: --help, --version\n")
}

internal fun commandHelp(command: Command): String = buildString {
    append("Usage: ").append(command.synopsis).append("\n\n")
    append(command.summary).append(".\n")
    if (command.details.isNotEmpty()) {
        append('\n')
        command.details.forEach { append(it).append('\n') }
    }
    if (command.files > 0) append("\nA file named - is the standard input.\n")
    append("\nOptions:\n")
    for (option in optionsOf(command)) {
        val short = option.short?.let { "-$it, " }.orEmpty()
        val long = "--" + option.name + (option.value?.let { " $it" } ?: "")
        append("  ").append((short + long).padEnd(OPTION_WIDTH)).append(option.help).append('\n')
    }
}

private const val NAME_WIDTH = 11
private const val OPTION_WIDTH = 24
