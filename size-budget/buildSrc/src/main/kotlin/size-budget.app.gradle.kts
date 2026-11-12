import io.github.calendrum.sizebudget.MeasureIosSize
import io.github.calendrum.sizebudget.MeasureJsSize
import io.github.calendrum.sizebudget.MeasureJvmSize
import io.github.calendrum.sizebudget.MeasureSize
import io.github.calendrum.sizebudget.MeasureWasmJsSize
import io.github.calendrum.sizebudget.R8Arguments
import io.github.calendrum.sizebudget.SIZE_USAGE
import io.github.calendrum.sizebudget.SizePlatform
import io.github.calendrum.sizebudget.selectedSizePlatforms
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
}

val budget = the<VersionCatalogsExtension>().named("budget")
val platforms = selectedSizePlatforms

val moduleNames = mapOf("app-baseline" to "size-plain", "app-ics" to "size-calendrum")
val webModuleName = moduleNames[name] ?: error("No web module name for $path, expected one of ${moduleNames.keys}")
val frameworkName = "SizeApp"

kotlin {
    if (SizePlatform.JVM in platforms || SizePlatform.ANDROID in platforms) {
        jvm()
    }
    if (SizePlatform.JS in platforms) {
        js {
            outputModuleName = webModuleName
            browser()
            binaries.executable()
        }
    }
    if (SizePlatform.WASM_JS in platforms) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            outputModuleName = webModuleName
            browser()
            binaries.executable()
        }
    }
    if (SizePlatform.IOS in platforms) {
        iosArm64 {
            binaries.framework(listOf(NativeBuildType.RELEASE)) {
                baseName = frameworkName
                isStatic = false
            }
        }
    }

    sourceSets.commonMain {
        kotlin.srcDir(rootProject.layout.projectDirectory.dir("shared/kotlin"))
    }
}

tasks.withType<KotlinWebpack>().configureEach {
    mainOutputFileName = "$webModuleName.js"
    output.library = webModuleName
}

val measurements = mutableMapOf<SizePlatform, TaskProvider<out MeasureSize>>()

if (SizePlatform.JVM in platforms) {
    val r8 = configurations.dependencyScope("r8")
    val r8Classpath = configurations.resolvable("r8Classpath") {
        extendsFrom(r8.get())
        attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
    dependencies {
        addProvider(r8.name, budget.findLibrary("r8").get())
    }

    val r8Program = files(tasks.named("jvmJar"), configurations.named("jvmRuntimeClasspath"))
    val r8Arguments = objects.newInstance<R8Arguments>().apply {
        program.from(r8Program)
        rules = rootProject.layout.projectDirectory.file("r8-rules.pro")
        output = layout.buildDirectory.file("size/r8/app.jar")
        mapping = layout.buildDirectory.file("size/r8/mapping.txt")
    }

    val r8Jvm = tasks.register<JavaExec>("r8Jvm") {
        group = "build"
        description = "Shrinks and obfuscates the JVM app with its runtime classpath using R8, as in a release build."
        classpath(r8Classpath)
        mainClass = "com.android.tools.r8.R8"
        r8Arguments.jdkHome = javaLauncher.map { it.metadata.installationPath.asFile.absolutePath }
            .orElse(providers.systemProperty("java.home"))
        argumentProviders.add(r8Arguments)
    }

    val jdkVersion = r8Jvm.flatMap { task ->
        task.javaLauncher.map { it.metadata.javaRuntimeVersion }
            .orElse(providers.systemProperty("java.runtime.version"))
    }
    measurements[SizePlatform.JVM] = tasks.register<MeasureJvmSize>("measureJvmSize") {
        r8Jar = r8Jvm.map { r8Arguments.output.get() }
        r8Inputs.from(r8Program)
        tools.put("r8", budget.findVersion("r8").get().requiredVersion)
        tools.put("jdk", jdkVersion)
    }
}

if (SizePlatform.JS in platforms) {
    measurements[SizePlatform.JS] = tasks.register<MeasureJsSize>("measureJsSize") {
        jsDir = tasks.named<KotlinWebpack>("jsBrowserProductionWebpack").flatMap { it.outputDirectory }
    }
}

if (SizePlatform.WASM_JS in platforms) {
    measurements[SizePlatform.WASM_JS] = tasks.register<MeasureWasmJsSize>("measureWasmJsSize") {
        wasmJsDir = tasks.named<KotlinWebpack>("wasmJsBrowserProductionWebpack").flatMap { it.outputDirectory }
    }
}

if (SizePlatform.IOS in platforms) {
    val framework = kotlin.iosArm64().binaries.getFramework(NativeBuildType.RELEASE)
    val xcodeVersion = providers.exec { commandLine("xcodebuild", "-version") }.standardOutput.asText
        .map { it.lineSequence().first().removePrefix("Xcode").trim() }
    measurements[SizePlatform.IOS] = tasks.register<MeasureIosSize>("measureIosSize") {
        frameworkDir = layout.dir(framework.linkTaskProvider.map { it.outputFile.get() })
        tools.put("xcode", xcodeVersion)
    }
}

val kotlinVersion = getKotlinPluginVersion()
measurements.forEach { (platform, measurement) ->
    measurement.configure {
        group = "verification"
        description = "Measures the ${platform.title} artifact of this app."
        tools.put("kotlin", kotlinVersion)
        output = layout.buildDirectory.file("size/$name.json")
    }
}

configurations.consumable("sizeMeasurements") {
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(SIZE_USAGE))
    measurements.values.forEach { measurement ->
        outgoing.artifact(measurement.flatMap { it.output })
    }
}
