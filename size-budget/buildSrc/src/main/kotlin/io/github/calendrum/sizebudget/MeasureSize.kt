package io.github.calendrum.sizebudget

import groovy.json.JsonOutput
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File

@DisableCachingByDefault(because = "Reading file sizes is cheaper than caching them")
abstract class MeasureSize : DefaultTask() {
    @get:Internal
    protected abstract val platform: SizePlatform

    @get:Input
    abstract val tools: MapProperty<String, String>

    @get:OutputFile
    abstract val output: RegularFileProperty

    protected abstract fun Measurements.measure()

    @TaskAction
    fun write() {
        val measurements = Measurements(platform).apply { measure() }
        val result = linkedMapOf(
            "platform" to platform.id,
            "tools" to tools.get().toSortedMap(),
            "metrics" to measurements.metrics,
            "details" to measurements.details,
        )
        output.get().asFile.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(result)) + "\n")
    }

    protected fun files(dir: DirectoryProperty, extension: String): List<File> = dir.get().asFileTree
        .matching { include("**/*.$extension") }
        .files
        .sortedBy { it.invariantSeparatorsPath }

    protected fun List<File>.relativeTo(dir: DirectoryProperty): List<String> =
        map { it.relativeTo(dir.get().asFile).invariantSeparatorsPath }

    class Measurements internal constructor(private val platform: SizePlatform) {
        internal val metrics = mutableListOf<Map<String, Any>>()
        internal val details = linkedMapOf<String, Any>()

        fun metric(key: String, name: String, unit: String, value: Long) {
            metrics += linkedMapOf("id" to "${platform.id}.$key", "name" to name, "unit" to unit, "value" to value)
        }

        fun detail(key: String, value: Any) {
            details[key] = value
        }
    }
}

abstract class MeasureJvmSize : MeasureSize() {
    @get:Internal
    override val platform: SizePlatform = SizePlatform.JVM

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val r8Jar: RegularFileProperty

    @get:Classpath
    abstract val r8Inputs: ConfigurableFileCollection

    override fun Measurements.measure() {
        val jar = r8Jar.get().asFile
        metric("r8.bytes", "jar, bytes", UNIT_BYTES, jar.length())
        metric("r8.classes", "classes", UNIT_CLASSES, classCount(jar))
        detail(
            "r8Input",
            r8Inputs.files.map { file ->
                linkedMapOf("file" to file.name, "bytes" to file.length(), "classes" to classCount(file))
            },
        )
    }
}

abstract class MeasureJsSize : MeasureSize() {
    @get:Internal
    override val platform: SizePlatform = SizePlatform.JS

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val jsDir: DirectoryProperty

    override fun Measurements.measure() {
        val js = files(jsDir, "js")
        metric("bytes", ".js, bytes", UNIT_BYTES, js.sumOf { it.length() })
        metric("gzip", ".js gzip -9, bytes", UNIT_BYTES, js.sumOf { gzipSize(it) })
        detail("files", js.relativeTo(jsDir))
    }
}

abstract class MeasureWasmJsSize : MeasureSize() {
    @get:Internal
    override val platform: SizePlatform = SizePlatform.WASM_JS

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val wasmJsDir: DirectoryProperty

    override fun Measurements.measure() {
        val wasm = files(wasmJsDir, "wasm")
        metric("wasm.bytes", ".wasm, bytes", UNIT_BYTES, wasm.sumOf { it.length() })
        metric("wasm.gzip", ".wasm gzip -9, bytes", UNIT_BYTES, wasm.sumOf { gzipSize(it) })
        val loader = files(wasmJsDir, "mjs") + files(wasmJsDir, "js")
        metric("js.bytes", "loader .js, bytes", UNIT_BYTES, loader.sumOf { it.length() })
        metric("js.gzip", "loader .js gzip -9, bytes", UNIT_BYTES, loader.sumOf { gzipSize(it) })
        detail("files", (wasm + loader).relativeTo(wasmJsDir))
    }
}

abstract class MeasureAndroidSize : MeasureSize() {
    @get:Internal
    override val platform: SizePlatform = SizePlatform.ANDROID

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val apkDir: DirectoryProperty

    override fun Measurements.measure() {
        val apks = files(apkDir, "apk")
        val apk = apks.singleOrNull() ?: error("Expected one APK in ${apkDir.get()}, found ${apks.relativeTo(apkDir)}")
        val dex = dexFiles(apk)
        check(dex.isNotEmpty()) { "No classes*.dex in $apk" }
        metric("dex.bytes", "classes*.dex, bytes", UNIT_BYTES, dex.sumOf { it.bytes })
        metric("dex.classes", "dex classes", UNIT_CLASSES, dex.sumOf { it.classes })
        detail("apk", apk.name)
        detail("dex", dex.map { linkedMapOf("file" to it.name, "bytes" to it.bytes, "classes" to it.classes) })
    }
}

abstract class MeasureIosSize : MeasureSize() {
    @get:Internal
    override val platform: SizePlatform = SizePlatform.IOS

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val frameworkDir: DirectoryProperty

    override fun Measurements.measure() {
        val framework = frameworkDir.get().asFile
        val binary = framework.resolve(framework.name.removeSuffix(".framework"))
        check(binary.isFile) { "No framework binary $binary" }
        metric("framework.bytes", "framework binary, bytes", UNIT_BYTES, binary.length())
        metric("sections.bytes", "code and data sections, bytes", UNIT_BYTES, machOSectionBytes(binary))
        detail("binary", "${framework.name}/${binary.name}")
    }
}
