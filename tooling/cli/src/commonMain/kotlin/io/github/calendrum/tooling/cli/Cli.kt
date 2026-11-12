package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.ops.Ops
import io.github.calendrum.tooling.ops.OpsEnvironment
import io.github.calendrum.tooling.ops.OpsLimits
import io.github.calendrum.tooling.platform.FileWrite
import io.github.calendrum.tooling.platform.UserFileRead
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.io.encoding.Base64

private const val STDIN = "-"
private const val STDIN_NAME = "<stdin>"
private const val OK = 0
private const val FINDINGS = 1
private const val FAILED = 2

/**
 * Runs one command line and gives the exit code. Nothing is written but through [console].
 *
 * The code is 0 if all is well, 1 if there is something to report (a finding above the threshold, a difference, an output
 * that is not complete) and 2 if the command could not do its work.
 */
internal fun runCli(args: List<String>, console: Console, environment: OpsEnvironment = OpsEnvironment.Default): Int {
    val invocation = try {
        parseArguments(args)
    } catch (e: UsageError) {
        return fail(console, e.text, hint = true)
    }
    return try {
        when (invocation) {
            Invocation.Help -> {
                console.writeOutput(generalHelp(CLI_VERSION).encodeToByteArray())
                OK
            }

            Invocation.Version -> {
                console.writeOutput(
                    "calendrum $CLI_VERSION\nlibrary $LIBRARY_VERSION\ncontract ${Ops.VERSION}\n".encodeToByteArray(),
                )
                OK
            }

            is Invocation.CommandHelp -> {
                console.writeOutput(commandHelp(invocation.command).encodeToByteArray())
                OK
            }

            is Invocation.Run -> execute(invocation.parsed, console, environment)
        }
    } catch (e: IOException) {
        fail(console, "Cannot write to the standard output: ${sanitize(e.message.orEmpty())}")
    } catch (e: UsageError) {
        fail(console, e.text, hint = true)
    } catch (e: Failure) {
        fail(console, e.text)
    }
}

/** A command that cannot do its work. [text] is a sentence for the person. */
private class Failure(val text: String) : Exception(text)

private fun fail(console: Console, text: String, hint: Boolean = false): Int {
    console.writeError("calendrum: $text${if (hint) "\nTry 'calendrum --help'." else ""}\n")
    return FAILED
}

private class Input(val name: String, val bytes: ByteArray)

private fun execute(parsed: Parsed, console: Console, environment: OpsEnvironment): Int {
    val command = parsed.command
    checkCombinations(parsed)
    val inputs = parsed.positionals.take(command.files).map { readInput(it, console, environment.limits) }
    val request = buildRequest(parsed, inputs)
    val answer = answerOf(Ops.call(command.operation, request, environment))
    val name = inputs.firstOrNull()?.name ?: parsed.positionals.firstOrNull().orEmpty()
    val json = parsed.has("json")
    val output = parsed.value("output")
    val toFile = output != null && output != STDIN
    val inPlace = parsed.has("in-place")

    // An error never reaches a file: with --json the answer is for the standard output, whatever -o says.
    val noCalendar = answer.ok && hasNoCalendar(command.name, answer)
    if (!answer.ok || noCalendar) {
        val note = if (noCalendar) {
            "No calendar found in '${sanitize(name)}': nothing was written"
        } else {
            "${sanitize(answer.errorMessage)} (${sanitize(answer.errorCode)})"
        }
        if (json) console.writeOutput((answer.raw + "\n").encodeToByteArray())
        val lines = ArrayList<String>()
        if (noCalendar && !json) lines += problemLines(name, answer.result)
        lines += "calendrum: $note"
        console.writeError(lines.joinToString("\n", postfix = "\n"))
        return FAILED
    }

    val rendered = if (json) Rendered(answer.raw + "\n", Kind.JSON) else render(parsed, answer, name)
    var exit = if (hasFindings(command.name, answer, parsed.value("fail-on") ?: "warning")) FINDINGS else OK
    val notes = ArrayList<String>()
    // A result that does not hold all of the input must never take the place of a file.
    val lossy = command.name in LOSSY_COMMANDS && losesData(answer)
    val refuses = lossy && (inPlace || (toFile && parsed.has("force")))
    if (answer.truncated) {
        notes += truncationNote(command, answer.result)
    } else if (lossy) {
        notes += LOSS_NOTE
    }
    if ((answer.truncated || lossy) && exit == OK) exit = FINDINGS

    var failure: String? = null
    if (refuses) {
        val target = if (inPlace) parsed.positionals[0] else output.orEmpty()
        notes +=
            "Nothing was written: '${sanitize(
                target,
            )}' is as it was. To see the result, write it to a new file with -o."
        exit = FAILED
    } else {
        failure = deliver(parsed, console, rendered, inputs.firstOrNull(), toFile, inPlace)
    }
    val side = if (json) emptyList() else rendered.diagnostics
    val report = if (json || refuses) emptyList() else rendered.report
    val reportToOutput = toFile || inPlace
    if (reportToOutput && report.isNotEmpty() && failure == null) {
        console.writeOutput(report.joinToString("\n", postfix = "\n").encodeToByteArray())
    }
    val lines = ArrayList<String>()
    lines += side
    if (!reportToOutput) lines += report
    for (note in notes) lines += "calendrum: $note"
    if (failure != null) {
        lines += "calendrum: $failure"
        exit = FAILED
    }
    if (lines.isNotEmpty()) console.writeError(lines.joinToString("\n", postfix = "\n"))
    return exit
}

private fun render(parsed: Parsed, answer: Answer, name: String): Rendered = when (parsed.command.name) {
    "lint" -> renderLint(name, answer)
    "fix" -> renderFix(name, answer)
    "format" -> renderFormat(name, answer)
    "convert" -> renderConvert(name, answer)
    "expand" -> renderExpand(name, answer)
    "diff" -> renderDiff(answer)
    "anonymize" -> renderAnonymize(answer)
    "tz" -> renderTimeZone(name, answer)
    "explain" -> renderExplain(answer)
    else -> error("No renderer for ${parsed.command.name}")
}

/** Puts the output where it was asked to go. Gives the sentence for the failure, or `null`. */
private fun deliver(
    parsed: Parsed,
    console: Console,
    rendered: Rendered,
    first: Input?,
    toFile: Boolean,
    inPlace: Boolean,
): String? {
    val raw = rendered.main.encodeToByteArray()
    if (inPlace) {
        if (rendered.keepFile) return null
        if (parsed.command.name == "format" && raw.contentEquals(first?.bytes)) return null
        val path = parsed.positionals[0]
        return writeFailure(path, console.writeFile(path, raw, replace = true))
    }
    if (toFile) {
        val path = parsed.value("output").orEmpty()
        return writeFailure(path, console.writeFile(path, raw, replace = parsed.has("force")))
    }
    if (rendered.main.isEmpty()) return null
    val text = if (console.outputIsTerminal) {
        when (rendered.kind) {
            Kind.HUMAN -> rendered.main
            Kind.CALENDAR -> sanitize(rendered.main, keepLineBreaks = true)
            Kind.JSON -> escapeJsonForTerminal(rendered.main)
        }
    } else {
        rendered.main
    }
    console.writeOutput(text.encodeToByteArray())
    return null
}

private fun writeFailure(path: String, result: FileWrite): String? = when (result) {
    FileWrite.Done -> null
    FileWrite.Exists -> "'${sanitize(path)}' already exists: use --force to replace it"
    is FileWrite.Failed -> "Cannot write '${sanitize(path)}': ${sanitize(result.reason)}"
}

private fun checkCombinations(parsed: Parsed) {
    val failOn = parsed.value("fail-on")
    if (failOn != null && failOn != "warning" &&
        failOn != "error"
    ) {
        throw UsageError("The option --fail-on is warning or error")
    }
    val from = parsed.value("from")
    if (parsed.command.name == "convert" && from != null && from != "ics" && from != "jcal") {
        throw UsageError("The option --from is ics or jcal")
    }
    for (option in listOf("limit", "shift-weeks")) {
        val value = parsed.value(option)
        if (value != null && value.toIntOrNull() == null) throw UsageError("The option --$option needs a whole number")
    }
    val inPlace = parsed.has("in-place")
    if (inPlace && parsed.value("output") != null) throw UsageError("--in-place and -o cannot be used together")
    if (inPlace && parsed.has("json")) throw UsageError("--in-place cannot be used with --json")
    if (inPlace && parsed.positionals[0] == STDIN) throw UsageError("--in-place needs a file, not the standard input")
    if (parsed.has("force") && parsed.value("output") == null) throw UsageError("--force is for -o")
    if (parsed.command.files == 2 && parsed.positionals.take(2).all { it == STDIN }) {
        throw UsageError("Only one of the two files can be the standard input")
    }
}

private fun readInput(path: String, console: Console, limits: OpsLimits): Input {
    val read = if (path ==
        STDIN
    ) {
        console.readStandardInput(limits.maxInputBytes)
    } else {
        console.readFile(path, limits.maxInputBytes)
    }
    val name = if (path == STDIN) STDIN_NAME else path
    return when (read) {
        is UserFileRead.Bytes -> Input(name, read.bytes)
        UserFileRead.TooLarge -> throw Failure("'${sanitize(name)}' is larger than ${limits.maxInputBytes / MIB} MiB")
        is UserFileRead.Failed -> throw Failure("Cannot read '${sanitize(name)}': ${sanitize(read.reason)}")
    }
}

private const val MIB = 1024 * 1024

private fun buildRequest(parsed: Parsed, inputs: List<Input>): String {
    val fields = LinkedHashMap<String, JsonElement>()
    fun calendar(field: String, input: Input) {
        fields["${field}_base64"] = JsonPrimitive(Base64.encode(input.bytes))
    }
    fun text(option: String, field: String = option) {
        parsed.value(option)?.let { fields[field] = JsonPrimitive(it) }
    }
    fun number(option: String, field: String) {
        val value = parsed.value(option) ?: return
        val number = value.toIntOrNull() ?: throw UsageError("The option --$option needs a whole number")
        fields[field] = JsonPrimitive(number)
    }
    when (parsed.command.name) {
        "lint" -> {
            calendar("ics", inputs[0])
            // The contract checks RFC 5545 only unless told otherwise, and this command is a linter.
            fields["rules"] = JsonPrimitive(parsed.value("rules") ?: "lint")
            number("limit", "limit")
        }

        "fix" -> {
            calendar("ics", inputs[0])
            parsed.value("fixups")?.let { fixups ->
                fields["fixups"] =
                    JsonArray(if (fixups.isEmpty()) emptyList() else fixups.split(',').map(::JsonPrimitive))
            }
        }

        "format" -> {
            calendar("ics", inputs[0])
            fields["fixups"] = JsonArray(emptyList())
        }

        "convert" -> {
            val from = parsed.value("from")
            val jcal = from == "jcal" || (from == null && parsed.positionals[0].lowercase().endsWith(".json"))
            if (jcal) fields["jcal"] = parseJcal(inputs[0]) else calendar("ics", inputs[0])
        }

        "expand" -> {
            calendar("ics", inputs[0])
            text("from")
            text("to")
            text("zone")
            number("limit", "limit")
            if (parsed.has("include-cancelled")) fields["include_cancelled"] = JsonPrimitive(true)
        }

        "diff" -> {
            calendar("before", inputs[0])
            calendar("after", inputs[1])
            number("limit", "limit")
        }

        "anonymize" -> {
            calendar("ics", inputs[0])
            number("shift-weeks", "shift_weeks")
        }

        "tz" -> {
            fields["name"] = JsonPrimitive(parsed.positionals[0])
            text("at")
        }

        "explain" -> fields["code"] = JsonPrimitive(parsed.positionals[0])
    }
    return JsonObject(fields).toString()
}

private const val MAX_JCAL_DEPTH = 64

// The file is JSON from a person, and it is parsed here only to be put into the request as a value. The operation checks
// the depth again for the whole request, but this parser must not meet a file that is nested beyond reason first.
private fun parseJcal(input: Input): JsonElement {
    val text = try {
        input.bytes.decodeToString(throwOnInvalidSequence = true).removePrefix("\uFEFF")
    } catch (_: CharacterCodingException) {
        throw Failure("'${sanitize(input.name)}' is not valid UTF-8")
    }
    var depth = 0
    var inString = false
    var index = 0
    while (index < text.length) {
        val char = text[index]
        if (inString) {
            if (char == '\\') {
                index++
            } else if (char == '"') {
                inString = false
            }
        } else if (char == '"') {
            inString = true
        } else if (char == '[' || char == '{') {
            if (++depth > MAX_JCAL_DEPTH) throw Failure("'${sanitize(input.name)}' is nested too deep")
        } else if (char == ']' || char == '}') {
            depth--
        }
        index++
    }
    return try {
        Json.parseToJsonElement(text)
    } catch (_: SerializationException) {
        throw Failure("'${sanitize(input.name)}' is not valid JSON")
    }
}

private fun answerOf(raw: String): Answer {
    val envelope = try {
        Json.parseToJsonElement(raw) as? JsonObject
    } catch (_: SerializationException) {
        null
    }
    return Answer(raw, envelope ?: throw Failure("The operation gave an answer that is not JSON"))
}

private val INPUT_LIMIT_CODES =
    setOf("ICAL-S010", "ICAL-S013", "ICAL-S014", "ICAL-S022", "ICAL-S023", "ICAL-S029", "ICAL-S030", "ICAL-R001")

/** Why the output is not complete and which option helps, if one does. */
internal fun truncationNote(command: Command, result: JsonObject): String {
    val readerLimit = result.objects("problems").any { it.text("code") in INPUT_LIMIT_CODES }
    val lists = command.options.any { it.name == "limit" }
    val note = StringBuilder("The output is incomplete")
    when {
        readerLimit && lists -> {
            note.append(": a limit was reached. The reader did not read the whole input, which no option changes. ")
            note.append("Raise --limit if a list was cut too (the largest value is 100000)")
        }

        readerLimit -> note.append(
            ": the reader did not read the whole input because of a limit that no option changes",
        )

        lists -> {
            note.append(": a limit was reached. Raise --limit (the largest value is 100000)")
            if (command.name == "expand") note.append(" or narrow the window with --from and --to")
        }

        else -> note.append(": a limit was reached that no option changes")
    }
    return note.append('.').toString()
}
