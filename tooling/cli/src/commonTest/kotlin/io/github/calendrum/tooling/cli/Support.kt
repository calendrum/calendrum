package io.github.calendrum.tooling.cli

import io.github.calendrum.tooling.ops.OpsEnvironment
import io.github.calendrum.tooling.ops.OpsLimits
import io.github.calendrum.tooling.platform.FileWrite
import io.github.calendrum.tooling.platform.UserFileRead
import kotlinx.io.IOException
import kotlin.time.Clock
import kotlin.time.Instant

internal val FIXED_ENVIRONMENT = OpsEnvironment(
    clock = object : Clock {
        override fun now(): Instant = Instant.parse("2026-01-01T00:00:00Z")
    },
    randomBytes = { size -> ByteArray(size) { it.toByte() } },
    limits = OpsLimits.Default,
)

internal fun calendar(vararg lines: String): String = (lines.toList() + "").joinToString("\r\n")

internal val STANDUP = calendar(
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
)

internal val WEEKLY = calendar(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:weekly@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "DURATION:PT1H",
    "RRULE:FREQ=WEEKLY;COUNT=4",
    "SUMMARY:Weekly",
    "END:VEVENT",
    "END:VCALENDAR",
)

internal const val WINDOW = "--from 2026-01-01T00:00:00Z --to 2026-02-01T00:00:00Z"

internal fun words(text: String): List<String> = text.split(' ').filter { it.isNotEmpty() }

/** A console in memory. */
internal class FakeConsole(
    val files: MutableMap<String, ByteArray> = HashMap(),
    var stdin: ByteArray = ByteArray(0),
    override val outputIsTerminal: Boolean = false,
    var outputFails: Boolean = false,
) : Console {
    val output = ArrayList<Byte>()
    val errors = StringBuilder()
    val written = ArrayList<String>()
    var writeResult: FileWrite? = null

    val outputBytes: ByteArray get() = output.toByteArray()
    val outputText: String get() = outputBytes.decodeToString()

    override fun readFile(path: String, maxBytes: Int): UserFileRead {
        val bytes = files[path] ?: return UserFileRead.Failed("No such file or directory")
        return if (bytes.size > maxBytes) UserFileRead.TooLarge else UserFileRead.Bytes(bytes)
    }

    override fun readStandardInput(maxBytes: Int): UserFileRead =
        if (stdin.size > maxBytes) UserFileRead.TooLarge else UserFileRead.Bytes(stdin)

    override fun writeOutput(bytes: ByteArray) {
        if (outputFails) throw IOException("Broken pipe")
        output.addAll(bytes.toList())
    }

    override fun writeError(text: String) {
        errors.append(text)
    }

    override fun writeFile(path: String, bytes: ByteArray, replace: Boolean): FileWrite {
        writeResult?.let { return it }
        if (!replace && path in files) return FileWrite.Exists
        files[path] = bytes
        written += path
        return FileWrite.Done
    }
}

internal class Outcome(val exit: Int, val console: FakeConsole) {
    val out: String get() = console.outputText
    val err: String get() = console.errors.toString()
}

internal fun run(line: String, console: FakeConsole = FakeConsole()): Outcome =
    Outcome(runCli(words(line), console, FIXED_ENVIRONMENT), console)

internal fun consoleWith(
    vararg files: Pair<String, String>,
    stdin: String? = null,
    terminal: Boolean = false,
): FakeConsole = FakeConsole(
    files = files.associate { it.first to it.second.encodeToByteArray() }.toMutableMap(),
    stdin = stdin?.encodeToByteArray() ?: ByteArray(0),
    outputIsTerminal = terminal,
)
