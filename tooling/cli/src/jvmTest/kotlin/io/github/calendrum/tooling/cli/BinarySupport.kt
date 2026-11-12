package io.github.calendrum.tooling.cli

import org.junit.jupiter.api.Assumptions.assumeTrue
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.test.fail

/** The release binary of this host, built by Gradle before the tests. A test needing it is skipped on other hosts. */
internal fun binary(): String {
    val path = System.getProperty("calendrum.cli.binary")
    assumeTrue(path != null, "No binary for this host")
    return path!!
}

internal fun temporaryDirectory(): File = Files.createTempDirectory("calendrum-cli").toFile().canonicalFile

internal val CRLF_STANDUP = listOf(
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

internal fun calendarOf(vararg extra: String): String = listOf(
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Example//Test//EN",
    "BEGIN:VEVENT",
    "UID:a@example.com",
    "DTSTAMP:20260101T000000Z",
    "DTSTART:20260105T100000Z",
    "DURATION:PT1H",
).plus(extra).plus(listOf("END:VEVENT", "END:VCALENDAR", "")).joinToString("\r\n")

/** What the command left behind. */
internal class Ran(val exit: Int, val out: ByteArray, val err: ByteArray) {
    val text: String get() = out.decodeToString()
    val errors: String get() = err.decodeToString()
}

/**
 * Runs the real binary. Its standard output and error go to files, so that a command that writes a lot cannot wait for a
 * reader that is waiting for it.
 */
internal fun cli(
    vararg args: String,
    stdin: ByteArray? = null,
    directory: File? = null,
    timeoutSeconds: Long = 60,
    command: List<String> = listOf(binary()),
): Ran {
    val out = File.createTempFile("calendrum-cli", ".out")
    val err = File.createTempFile("calendrum-cli", ".err")
    try {
        val builder = ProcessBuilder(command + args)
            .redirectOutput(out)
            .redirectError(err)
        if (directory != null) builder.directory(directory)
        if (stdin == null) builder.redirectInput(ProcessBuilder.Redirect.from(File("/dev/null")))
        val process = builder.start()
        if (stdin != null) {
            thread(isDaemon = true) {
                try {
                    process.outputStream.use { it.write(stdin) }
                } catch (_: java.io.IOException) {
                    // The command stopped reading: it has what it needed, or it failed.
                }
            }
        }
        if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            fail("The command did not stop in $timeoutSeconds seconds: ${args.toList()}")
        }
        return Ran(process.exitValue(), out.readBytes(), err.readBytes())
    } finally {
        out.delete()
        err.delete()
    }
}
