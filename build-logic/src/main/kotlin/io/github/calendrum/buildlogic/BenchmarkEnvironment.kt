package io.github.calendrum.buildlogic

import groovy.json.JsonOutput
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.toolchain.JavaLauncher
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

// Clears the run directory and writes environment.json. Commands run through ProviderFactory.exec at execution time;
// anything that cannot be found is written as `unknown` instead of failing. The corpora and the competitors are filled
// by the corpus generator and the competitor baseline.
abstract class BenchmarkEnvironment : DefaultTask() {
    @get:Internal
    abstract val profile: Property<String>

    @get:Internal
    abstract val runDirectory: DirectoryProperty

    @get:Internal
    abstract val include: Property<String>

    @get:Internal
    abstract val tools: MapProperty<String, String>

    @get:Internal
    abstract val jmhArguments: ListProperty<String>

    @get:Internal
    abstract val jmhJvmArguments: ListProperty<String>

    @get:Internal
    abstract val nonJvmSettings: MapProperty<String, Any>

    @get:Internal
    abstract val nodeVersions: MapProperty<String, String>

    @get:Internal
    abstract val nativeTargets: ListProperty<String>

    @get:Internal
    abstract val javaLauncher: Property<JavaLauncher>

    @get:Internal
    abstract val corporaDirectory: DirectoryProperty

    @get:Internal
    abstract val competitorVersions: MapProperty<String, String>

    @get:Internal
    abstract val competitorSettingsDirectory: DirectoryProperty

    @get:Inject
    abstract val providers: ProviderFactory

    @TaskAction
    fun write() {
        val directory = runDirectory.get().asFile
        directory.deleteRecursively()
        directory.mkdirs()
        val environment = linkedMapOf(
            "schemaVersion" to 1,
            "profile" to profile.get(),
            "startedAt" to Instant.now().truncatedTo(ChronoUnit.SECONDS).toString(),
            "runDirectory" to directory.name,
            "include" to include.orNull,
            "commit" to (command("git", "rev-parse", "HEAD") ?: UNKNOWN),
            "machine" to machine(),
            "cpu" to cpu(),
            "os" to os(),
            "jdk" to jdk(),
            "jmh" to linkedMapOf(
                "version" to tools.get()["jmh"],
                "arguments" to jmhArguments.get(),
                "jvmArguments" to jmhJvmArguments.get(),
            ),
            "nonJvm" to nonJvmSettings.get(),
            "node" to nodeVersions.get(),
            "native" to linkedMapOf("targets" to nativeTargets.get(), "buildType" to "release"),
            "kotlin" to tools.get()["kotlin"],
            "kotlinxBenchmark" to tools.get()["kotlinxBenchmark"],
            "corpora" to corpora(),
            "competitors" to competitors(),
        )
        val file = directory.resolve(ENVIRONMENT_FILE)
        file.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(environment)) + "\n")
        logger.lifecycle("Benchmark environment: ${file.absolutePath}")
    }

    // Reads `generator.properties` and `SHA256SUMS` from the corpora build directory that `generateCorpora` just
    // populated; falls back to `null` and `[]` when `corporaDirectory` was not wired up (see `usesCorpora` in
    // calendrum.benchmarks). This task never generates corpora itself, it only describes what already ran.
    private fun corpora(): Map<String, Any?> {
        val directory = corporaDirectory.orNull?.asFile
        val propertiesFile = directory?.resolve("generator.properties")
        val sumsFile = directory?.resolve("SHA256SUMS")
        if (directory == null || propertiesFile?.isFile != true || sumsFile?.isFile != true) {
            return linkedMapOf("generator" to null, "seed" to null, "files" to emptyList<Any>())
        }
        val metadata = java.util.Properties().apply { propertiesFile.reader().use { load(it) } }
        val files = sumsFile.readLines()
            .filter { it.isNotBlank() }
            .map { line ->
                val sha256 = line.substringBefore(' ')
                val name = line.substringAfterLast(' ')
                linkedMapOf("name" to name, "sizeBytes" to directory.resolve(name).length(), "sha256" to sha256)
            }
        return linkedMapOf(
            "generator" to metadata.getProperty("version")?.toIntOrNull(),
            "seed" to metadata.getProperty("seed")?.toLongOrNull(),
            "files" to files,
        )
    }

    private fun competitors(): Map<String, Any?> = competitorVersions.get().mapValues { (name, version) ->
        val settingsFile = competitorSettingsDirectory.orNull?.asFile?.resolve("$name.properties")
        val settings = java.util.Properties()
        if (settingsFile?.isFile == true) settingsFile.reader().use { settings.load(it) }
        val values = settings.stringPropertyNames().sorted().associateWith(settings::getProperty)
        linkedMapOf("version" to version, "settings" to values)
    }

    private fun machine(): Map<String, Any?> {
        val env = { name: String -> providers.environmentVariable(name).orNull }
        return if (env("GITHUB_ACTIONS") == "true") {
            linkedMapOf(
                "type" to "github-actions",
                "runnerEnvironment" to (env("RUNNER_ENVIRONMENT") ?: UNKNOWN),
                "runnerName" to (env("RUNNER_NAME") ?: UNKNOWN),
                "runnerOs" to (env("RUNNER_OS") ?: UNKNOWN),
                "runnerArch" to (env("RUNNER_ARCH") ?: UNKNOWN),
                "image" to listOfNotNull(env("ImageOS"), env("ImageVersion")).joinToString(" ").ifEmpty { UNKNOWN },
            )
        } else {
            linkedMapOf("type" to "local")
        }
    }

    private fun cpu(): Map<String, Any?> {
        val osName = System.getProperty("os.name").orEmpty()
        val cpu = when {
            osName.startsWith("Mac") -> macCpu()
            osName.startsWith("Linux") -> linuxCpu()
            osName.startsWith("Windows") -> windowsCpu()
            else -> linkedMapOf()
        }
        cpu.putIfAbsent("model", UNKNOWN)
        cpu.putIfAbsent("physicalCores", UNKNOWN)
        cpu.putIfAbsent("logicalCores", Runtime.getRuntime().availableProcessors())
        cpu.putIfAbsent("maxFrequencyMHz", UNKNOWN)
        cpu.putIfAbsent("turboBoost", UNKNOWN)
        return cpu
    }

    private fun macCpu(): MutableMap<String, Any?> {
        val sysctl = { name: String -> command("sysctl", "-n", name) }
        val cpu = linkedMapOf<String, Any?>()
        sysctl("machdep.cpu.brand_string")?.let { cpu["model"] = it }
        sysctl("hw.physicalcpu")?.toIntOrNull()?.let { cpu["physicalCores"] = it }
        sysctl("hw.logicalcpu")?.toIntOrNull()?.let { cpu["logicalCores"] = it }
        sysctl("hw.perflevel0.physicalcpu")?.toIntOrNull()?.let { cpu["performanceCores"] = it }
        sysctl("hw.perflevel1.physicalcpu")?.toIntOrNull()?.let { cpu["efficiencyCores"] = it }
        sysctl("hw.cpufrequency_max")?.toLongOrNull()?.let { cpu["maxFrequencyMHz"] = it / 1_000_000 }
        return cpu
    }

    private fun linuxCpu(): MutableMap<String, Any?> {
        val cpu = linkedMapOf<String, Any?>()
        val entries = readFile("/proc/cpuinfo")
            ?.lineSequence()
            ?.map { line -> line.substringBefore(':').trim() to line.substringAfter(':', "").trim() }
            ?.toList()
            .orEmpty()
        fun values(key: String) = entries.filter { it.first == key }.map { it.second }
        values("model name").firstOrNull()?.let { cpu["model"] = it }
        values("processor").size.takeIf { it > 0 }?.let { cpu["logicalCores"] = it }
        val cores = values("physical id").zip(values("core id")).toSet()
        cores.size.takeIf { it > 0 }?.let { cpu["physicalCores"] = it }
        val maxKHz = readFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")?.trim()?.toLongOrNull()
        if (maxKHz != null) {
            cpu["maxFrequencyMHz"] = maxKHz / 1_000
        } else {
            values("cpu MHz").firstOrNull()?.toDoubleOrNull()?.let { cpu["currentFrequencyMHz"] = it.toLong() }
        }
        val noTurbo = readFile("/sys/devices/system/cpu/intel_pstate/no_turbo")?.trim()
        val boost = readFile("/sys/devices/system/cpu/cpufreq/boost")?.trim()
        cpu["turboBoost"] = when {
            noTurbo == "0" || boost == "1" -> "enabled"
            noTurbo == "1" || boost == "0" -> "disabled"
            else -> UNKNOWN
        }
        return cpu
    }

    private fun windowsCpu(): MutableMap<String, Any?> {
        val cpu = linkedMapOf<String, Any?>()
        providers.environmentVariable("PROCESSOR_IDENTIFIER").orNull?.let { cpu["model"] = it }
        return cpu
    }

    private fun os(): Map<String, Any?> {
        val os = linkedMapOf<String, Any?>(
            "name" to System.getProperty("os.name"),
            "version" to System.getProperty("os.version"),
            "arch" to System.getProperty("os.arch"),
            "kernel" to (command("uname", "-sr") ?: UNKNOWN),
        )
        readFile("/etc/os-release")
            ?.lineSequence()
            ?.firstOrNull { it.startsWith("PRETTY_NAME=") }
            ?.let { os["distribution"] = it.substringAfter('=').trim('"') }
        return os
    }

    private fun jdk(): Map<String, Any?> {
        val metadata = runCatching { javaLauncher.get().metadata }.getOrNull()
            ?: return linkedMapOf("vendor" to UNKNOWN, "version" to UNKNOWN)
        return linkedMapOf(
            "vendor" to metadata.vendor,
            "version" to metadata.javaRuntimeVersion,
            "home" to metadata.installationPath.asFile.absolutePath,
        )
    }

    private fun command(vararg arguments: String): String? = runCatching {
        val exec = providers.exec {
            commandLine(*arguments)
            isIgnoreExitValue = true
        }
        exec.standardOutput.asText.get().trim().takeIf { exec.result.get().exitValue == 0 && it.isNotEmpty() }
    }.getOrNull()

    private fun readFile(path: String): String? = runCatching { File(path).readText() }.getOrNull()

    companion object {
        const val ENVIRONMENT_FILE: String = "environment.json"
        private const val UNKNOWN = "unknown"
    }
}
