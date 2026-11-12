@file:OptIn(InternalICalApi::class)

package io.github.calendrum.tools

import io.github.calendrum.InternalICalApi
import io.github.calendrum.anonymize.Anonymizer
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.SecureRandom
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val command = AnonymizeCommand(System.out, System.err, System::getenv) {
        ByteArray(KEY_BYTES).also { SecureRandom().nextBytes(it) }
    }
    exitProcess(command.run(args.toList()))
}

// Exit codes: 0 when every file passed the check, 1 when some file failed it (its output is still written), 2 for bad
// arguments and input or output errors. The key is never printed.
internal class AnonymizeCommand(
    private val out: PrintStream,
    private val err: PrintStream,
    private val environment: (String) -> String?,
    private val randomKey: () -> ByteArray,
) {
    fun run(args: List<String>): Int {
        val options = parse(args) ?: return usage()
        val input = Paths.get(options.input)
        val output = Paths.get(options.output)
        if (!Files.exists(input)) return fail("the input ${options.input} does not exist")
        val jobs = try {
            jobs(input, output)
        } catch (e: IOException) {
            return fail("cannot read the input: ${e.message}")
        } ?: return EXIT_ERROR
        val key = key(options) ?: return EXIT_ERROR
        out.println(key.source)
        val anonymizer = Anonymizer(key.bytes, options.shiftWeeks)
        var failed = 0
        for (job in jobs) {
            out.println("== ${job.name} ==")
            val result = try {
                anonymizer.anonymize(Files.readAllBytes(job.source))
            } catch (e: IOException) {
                err.println("error: cannot read ${job.name}: ${e.message}")
                failed++
                continue
            }
            try {
                job.target.parent?.let { Files.createDirectories(it) }
                Files.write(job.target, result.bytes)
            } catch (e: IOException) {
                err.println("error: cannot write the output of ${job.name}: ${e.message}")
                failed++
                continue
            }
            out.println(result.summary)
            if (!result.isVerified) failed++
        }
        out.println("Done: ${jobs.size} files, ${jobs.size - failed} passed the check, $failed failed")
        return if (failed == 0) EXIT_OK else EXIT_CHECK_FAILED
    }

    private fun jobs(input: Path, output: Path): List<Job>? {
        val inputReal = input.toRealPath()
        val outputReal = realPath(output)
        if (!Files.isDirectory(inputReal)) {
            val target = if (Files.isDirectory(output)) output.resolve(input.fileName) else output
            if (realPath(target) == inputReal) {
                fail("the output is the input; refusing to overwrite it")
                return null
            }
            return listOf(Job(input.fileName.toString(), input, target))
        }
        if (outputReal == inputReal || outputReal.startsWith(inputReal) || inputReal.startsWith(outputReal)) {
            fail("the output directory must be outside the input directory and must not contain it")
            return null
        }
        if (Files.exists(output) && !Files.isDirectory(output)) {
            fail("the input is a directory, so the output must be a directory too")
            return null
        }
        val files = Files.walk(inputReal).use { stream ->
            stream.filter { Files.isRegularFile(it) }.sorted().toList()
        }
        val inputs = files.map { it.toRealPath() }.toSet()
        val jobs = ArrayList<Job>()
        for (file in files) {
            val relative = inputReal.relativize(file)
            if (relative.fileName.toString().substringAfterLast('.', "").lowercase() !in EXTENSIONS) {
                out.println("skipped: $relative (not a calendar file)")
                continue
            }
            val target = output.resolve(relative.toString())
            if (realPath(target) in inputs) {
                fail("the output of $relative is an input file; refusing to overwrite it")
                return null
            }
            jobs += Job(relative.toString(), file, target)
        }
        return jobs
    }

    // Where the key comes from: `--key-file` (hex digits, surrounding whitespace ignored) or `--key`, never both;
    // without them CALENDRUM_ANON_KEY (an empty value counts as unset); without that, random bytes for this run. A file or
    // the environment keeps the key out of the shell history and the process list. Error messages name the source,
    // never its content.
    private fun key(options: Options): Key? {
        if (options.key != null && options.keyFile != null) {
            fail("pass either --key or --key-file, not both")
            return null
        }
        if (options.key != null) return Key(options.key, GIVEN_KEY)
        if (options.keyFile != null) {
            val text = try {
                String(Files.readAllBytes(Paths.get(options.keyFile)), Charsets.US_ASCII)
            } catch (e: IOException) {
                fail("cannot read the key file ${options.keyFile}: ${e.message}")
                return null
            }
            val key = parseKey(text.trim())
            if (key == null) fail("the key file ${options.keyFile} must hold $MIN_KEY_HEX to $MAX_KEY_HEX hex digits")
            return key?.let { Key(it, FILE_KEY) }
        }
        val variable = environment(KEY_VARIABLE)?.trim()
        if (variable.isNullOrEmpty()) return Key(randomKey(), RANDOM_KEY)
        val key = parseKey(variable)
        if (key == null) fail("$KEY_VARIABLE must hold $MIN_KEY_HEX to $MAX_KEY_HEX hex digits")
        return key?.let { Key(it, VARIABLE_KEY) }
    }

    // Real path of a path that may not exist yet: the deepest existing ancestor is resolved (symbolic links included)
    // and the rest appended, so a new output is compared with the real input.
    private fun realPath(path: Path): Path {
        val absolute = path.toAbsolutePath().normalize()
        var existing: Path? = absolute
        while (existing != null && !Files.exists(existing)) existing = existing.parent
        if (existing == null) return absolute
        return existing.toRealPath().resolve(existing.relativize(absolute)).normalize()
    }

    private fun parse(args: List<String>): Options? {
        val positional = ArrayList<String>()
        var key: ByteArray? = null
        var keyFile: String? = null
        var shiftWeeks = 0
        var index = 0
        while (index < args.size) {
            val arg = args[index]
            when (arg) {
                "--key" -> {
                    key = args.getOrNull(index + 1)?.let(::parseKey) ?: return null
                    index += 2
                }

                "--key-file" -> {
                    keyFile = args.getOrNull(index + 1) ?: return null
                    index += 2
                }

                "--shift-weeks" -> {
                    shiftWeeks = args.getOrNull(index + 1)?.toIntOrNull() ?: return null
                    index += 2
                }

                else -> {
                    if (arg.startsWith("--")) return null
                    positional += arg
                    index++
                }
            }
        }
        if (positional.size != 2) return null
        return Options(positional[0], positional[1], key, keyFile, shiftWeeks)
    }

    private fun parseKey(hex: String): ByteArray? {
        if (hex.length % 2 != 0 || hex.length < MIN_KEY_HEX || hex.length > MAX_KEY_HEX) return null
        if (hex.any { it !in '0'..'9' && it !in 'a'..'f' && it !in 'A'..'F' }) return null
        return ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(HEX).toByte() }
    }

    private fun usage(): Int {
        err.println(USAGE)
        return EXIT_ERROR
    }

    private fun fail(message: String): Int {
        err.println("error: $message")
        return EXIT_ERROR
    }

    private class Options(
        val input: String,
        val output: String,
        val key: ByteArray?,
        val keyFile: String?,
        val shiftWeeks: Int,
    )

    private class Key(val bytes: ByteArray, val source: String)

    private class Job(val name: String, val source: Path, val target: Path)

    private companion object {
        const val EXIT_OK = 0
        const val EXIT_CHECK_FAILED = 1
        const val EXIT_ERROR = 2
        const val MIN_KEY_HEX = 32
        const val MAX_KEY_HEX = 128
        const val HEX = 16
        val EXTENSIONS = setOf("ics", "ical", "icalendar", "ifb", "vcs")
        const val KEY_VARIABLE = "CALENDRUM_ANON_KEY"
        const val RANDOM_KEY = "Key: random for this run and not shown; " +
            "pass --key-file <file> or set $KEY_VARIABLE for a reproducible result"
        const val GIVEN_KEY = "Key: given with --key and not shown"
        const val FILE_KEY = "Key: read from the key file and not shown"
        const val VARIABLE_KEY = "Key: read from $KEY_VARIABLE and not shown"
        val USAGE = """
            Usage: anonymize <input> <output> [--key-file <file> | --key <hex>] [--shift-weeks <n>]
              <input>            a calendar file, or a directory with .ics files (subdirectories included)
              <output>           a file, or a directory that gets the same tree; never the input itself
              --key-file <file>  file with the secret key, 32 to 128 hex digits; the same key gives the same output
              --key <hex>        the key itself; it stays in the shell history and the process list
              --shift-weeks <n>  shift every date by n weeks (days of the week are kept); time zones stay
            Without --key-file and --key the key comes from $KEY_VARIABLE, or else is random for this run.
        """.trimIndent()
    }
}

private const val KEY_BYTES = 32
