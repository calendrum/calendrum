package io.github.calendrum.tools

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readBytes
import kotlin.io.path.writeText
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnonymizeCommandTest {
    private val temp = createTempDirectory("anonymize-test")
    private var randomKeys = 0
    private val environment = HashMap<String, String>()

    @AfterTest
    fun cleanUp() {
        temp.toFile().deleteRecursively()
    }

    @Test
    fun anonymizesAFileIntoAFile() {
        val input = copySample("google-schedule.ics")
        val output = temp.resolve("out.ics")
        val run = run(input, output, "--key", KEY)
        assertEquals(0, run.code, run.all)
        assertTrue(Files.exists(output))
        assertFalse(output.readBytes().contentEquals(input.readBytes()))
        assertTrue("== google-schedule.ics ==" in run.out)
        assertTrue("Check: passed" in run.out)
        assertTrue("Done: 1 files, 1 passed the check, 0 failed" in run.out)
    }

    @Test
    fun anonymizesADirectoryIntoTheSameTree() {
        val input = temp.resolve("in")
        Files.createDirectories(input.resolve("nested"))
        Files.copy(sample("outlook-invite.ics"), input.resolve("invite.ics"))
        Files.copy(sample("apple-family.ics"), input.resolve("nested/apple.ICS"))
        input.resolve("notes.txt").writeText("not a calendar")
        val output = temp.resolve("out")
        val run = run(input, output, "--key", KEY)
        assertEquals(0, run.code, run.all)
        assertTrue(Files.exists(output.resolve("invite.ics")))
        assertTrue(Files.exists(output.resolve("nested/apple.ICS")))
        assertFalse(Files.exists(output.resolve("notes.txt")))
        assertTrue("skipped: notes.txt (not a calendar file)" in run.out)
        assertTrue("Done: 2 files, 2 passed the check, 0 failed" in run.out)
    }

    @Test
    fun refusesToOverwriteTheInput() {
        val input = copySample("google-schedule.ics")
        val before = input.readBytes()
        assertEquals(2, run(input, input).code)
        assertEquals(2, run(input, temp).code)
        assertContentEquals(before, input.readBytes())
        val directory = temp.resolve("dir")
        Files.createDirectories(directory)
        Files.copy(sample("outlook-invite.ics"), directory.resolve("a.ics"))
        assertEquals(2, run(directory, directory).code)
        assertEquals(2, run(directory, directory.resolve("out")).code)
        assertEquals(2, run(directory, temp).code)
        assertFalse(Files.exists(directory.resolve("out")))
        val file = temp.resolve("plain.ics")
        Files.copy(sample("outlook-invite.ics"), file)
        assertEquals(2, run(directory, file).code)
    }

    @Test
    fun reproducesTheOutputWithAKeyAndNotWithoutIt() {
        val input = copySample("outlook-invite.ics")
        val first = temp.resolve("first.ics")
        val second = temp.resolve("second.ics")
        run(input, first, "--key", KEY)
        run(input, second, "--key", KEY)
        assertContentEquals(first.readBytes(), second.readBytes())
        val random = temp.resolve("random.ics")
        val run = run(input, random)
        assertEquals(0, run.code, run.all)
        assertFalse(random.readBytes().contentEquals(first.readBytes()))
        assertTrue("Key: random for this run and not shown" in run.out)
    }

    @Test
    fun readsTheKeyFromAFileOrTheEnvironment() {
        val input = copySample("outlook-invite.ics")
        val given = temp.resolve("given.ics")
        run(input, given, "--key", KEY)
        val keyFile = temp.resolve("key.txt")
        keyFile.writeText("  ${KEY.uppercase()}\n")
        val fromFile = temp.resolve("file.ics")
        val fileRun = run(input, fromFile, "--key-file", keyFile.toString())
        assertEquals(0, fileRun.code, fileRun.all)
        assertTrue("Key: read from the key file and not shown" in fileRun.out)
        assertContentEquals(given.readBytes(), fromFile.readBytes())
        environment["CALENDRUM_ANON_KEY"] = "$KEY\n"
        val fromVariable = temp.resolve("variable.ics")
        val variableRun = run(input, fromVariable)
        assertEquals(0, variableRun.code, variableRun.all)
        assertTrue("Key: read from CALENDRUM_ANON_KEY and not shown" in variableRun.out)
        assertContentEquals(given.readBytes(), fromVariable.readBytes())
        val other = "ff".repeat(32)
        environment["CALENDRUM_ANON_KEY"] = other
        val overridden = temp.resolve("overridden.ics")
        val overriddenRun = run(input, overridden, "--key-file", keyFile.toString())
        assertTrue("Key: read from the key file and not shown" in overriddenRun.out)
        assertContentEquals(given.readBytes(), overridden.readBytes())
        val otherOutput = temp.resolve("other.ics")
        run(input, otherOutput)
        assertFalse(otherOutput.readBytes().contentEquals(given.readBytes()))
        for (printed in listOf(fileRun.all, variableRun.all, overriddenRun.all)) {
            assertFalse(KEY in printed.lowercase() || other in printed.lowercase(), "the key is printed")
        }
    }

    @Test
    fun rejectsBadKeySources() {
        val input = copySample("google-schedule.ics")
        val output = temp.resolve("out.ics")
        val secret = "0123456789abcdef0123456789abcde"
        val shortKey = temp.resolve("short.txt")
        shortKey.writeText(secret)
        val validKey = temp.resolve("valid.txt")
        validKey.writeText(KEY)
        val runs = listOf(
            run(input, output, "--key-file", shortKey.toString()),
            run(input, output, "--key-file", temp.resolve("missing.txt").toString()),
            run(input, output, "--key-file", temp.toString()),
            run(input, output, "--key-file", validKey.toString(), "--key", KEY),
            run(input, output, "--key-file"),
        )
        environment["CALENDRUM_ANON_KEY"] = secret
        val variableRun = run(input, output)
        for (run in runs + variableRun) {
            assertEquals(2, run.code, run.all)
            assertTrue(run.err.isNotEmpty())
            assertFalse(secret in run.all, "the key is printed")
        }
        assertTrue("CALENDRUM_ANON_KEY must hold 32 to 128 hex digits" in variableRun.err, variableRun.err)
        assertFalse(Files.exists(output))
    }

    @Test
    fun neverPrintsValuesOrTheKey() {
        for (path in SAMPLES) {
            val input = copySample(path.fileName.toString())
            val run = run(input, temp.resolve("out-${path.fileName}"), "--key", KEY)
            val printed = run.all.replace(path.fileName.toString(), "").lowercase()
            assertFalse(KEY in printed, "the key is printed")
            for (word in sensitiveWords(path.readBytes())) {
                assertFalse(word.lowercase() in printed, "${path.fileName}: the word $word is printed")
            }
        }
    }

    @Test
    fun rejectsBadArguments() {
        val input = copySample("google-schedule.ics")
        val output = temp.resolve("out.ics").toString()
        for (args in listOf(
            listOf(input.toString()),
            listOf(input.toString(), output, "extra"),
            listOf(input.toString(), output, "--key"),
            listOf(input.toString(), output, "--key", "abc"),
            listOf(input.toString(), output, "--key", "z".repeat(32)),
            listOf(input.toString(), output, "--shift-weeks", "many"),
            listOf(input.toString(), output, "--verbose"),
            listOf(temp.resolve("missing.ics").toString(), output),
        )) {
            val run = run(args)
            assertEquals(2, run.code, args.toString())
            assertTrue(run.err.isNotEmpty())
        }
        assertFalse(Files.exists(Paths.get(output)))
    }

    @Test
    fun shiftsDatesByWeeks() {
        val input = copySample("university-windows-1251.ics")
        val output = temp.resolve("shifted.ics")
        val run = run(input, output, "--key", KEY, "--shift-weeks", "-2")
        assertEquals(0, run.code, run.all)
        assertTrue("dates shifted by -2 weeks" in run.out)
        assertTrue("DTSTART;TZID=Europe/Moscow:20270127T084500" in String(output.readBytes(), charset("windows-1251")))
    }

    private fun copySample(name: String): Path = temp.resolve(name).also { Files.copy(sample(name), it) }

    private fun run(input: Path, output: Path, vararg options: String): Run =
        run(listOf(input.toString(), output.toString()) + options)

    private fun run(args: List<String>): Run {
        val out = ByteArrayOutputStream()
        val err = ByteArrayOutputStream()
        val command = AnonymizeCommand(
            PrintStream(out, true, "UTF-8"),
            PrintStream(err, true, "UTF-8"),
            environment::get,
        ) {
            ByteArray(32) { (randomKeys * 31 + it).toByte() }.also { randomKeys++ }
        }
        val code = command.run(args)
        return Run(code, out.toString("UTF-8"), err.toString("UTF-8"))
    }

    private class Run(val code: Int, val out: String, val err: String) {
        val all: String get() = out + err
    }

    private companion object {
        const val KEY = "00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff"
    }
}
