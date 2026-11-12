import io.github.calendrum.buildlogic.TargetSet
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.kover")
    id("org.jetbrains.dokka")
    id("calendrum.publishing")
    id("calendrum.lint")
}

// Targets to declare, chosen by `calendrum.targets`: `all` is tiers 1-3, `tier1` tier 1 only, `jvm` the cheapest local
// loop. Kotlin/Native downloads the toolchain of every declared target that the host can link, so the host sets stay
// small: `web` is JVM, JS and Wasm with no Kotlin/Native at all, `apple` is JVM and Apple targets, `windows` is JVM and
// mingwX64.
val targetSet = TargetSet.of(providers)

// Target families a module leaves out because its dependency does not publish them (Okio has no Android Native, Ktor no
// WASI), from `calendrum.library.exclude` in the module's gradle.properties.
val excludedTargets = (findProperty("calendrum.library.exclude") as String?).orEmpty()
    .split(',')
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .toSet()

// A JVM-only module has no klib dump and no Objective-C header; `jvmRelease` raises the bytecode and the JDK API of one
// module (ical4j 4 needs Java 11).
val jvmOnly = findProperty("calendrum.library.jvmOnly") == "true"
val jvmRelease = (findProperty("calendrum.library.jvmRelease") as String?)?.toInt() ?: 8

fun wanted(name: String): Boolean = name !in excludedTargets

val kotlinPluginVersion = getKotlinPluginVersion()

// A static debug framework of `iosSimulatorArm64` exists only to get the Objective-C header that Swift sees;
// `checkObjcHeader` (in `check`) compares it with `api/<module>.h`, `updateObjcHeader` writes it. Only on macOS with
// Apple targets declared.
val objcHeaders = !jvmOnly && targetSet.declaredIn("tier1", "apple") && HostManager.hostIsMac
val frameworkName = project.name.split('-').joinToString("") { part -> part.replaceFirstChar { it.uppercaseChar() } }

// The root of the checkout; the base for relative source paths in klibs.
val checkoutDirectory = layout.settingsDirectory.asFile.absolutePath

kotlin {
    explicitApi()

    // Consumers on the minimum supported Kotlin must be able to use our artifacts.
    coreLibrariesVersion = "2.3.0"

    // Only JVM and common metadata get the older stdlib: the Kotlin/JS and Wasm compilers refuse a stdlib older than
    // themselves, so non-JVM targets depend on the stdlib of the compiler version.
    targets.configureEach {
        if (platformType != KotlinPlatformType.jvm && platformType != KotlinPlatformType.common) {
            compilations.named("main") {
                defaultSourceSet.dependencies { api(kotlin("stdlib", kotlinPluginVersion)) }
            }

            // A klib records the absolute path of every source file in its IR, so a build in another directory or on
            // another host would give different bytes. `-Xklib-relative-path-base` makes the paths relative to the
            // checkout. Only klib targets get it: the JVM compiler does not know the flag, and warnings are errors.
            compilations.configureEach {
                compileTaskProvider.configure {
                    compilerOptions.freeCompilerArgs.add("-Xklib-relative-path-base=$checkoutDirectory")
                }
            }
        }
    }
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_3
        apiVersion = KotlinVersion.KOTLIN_2_3
        allWarningsAsErrors = true
    }

    jvm {
        compilerOptions {
            jvmTarget = if (jvmRelease == 8) JvmTarget.JVM_1_8 else JvmTarget.fromTarget(jvmRelease.toString())

            // Compile against the Java 8 API even when the build runs on a newer JDK.
            freeCompilerArgs.add(if (jvmRelease == 8) "-Xjdk-release=1.8" else "-Xjdk-release=$jvmRelease")
        }
    }
    if (!jvmOnly && targetSet.declaredIn("tier1", "apple")) {
        iosArm64()
        iosSimulatorArm64 {
            if (objcHeaders) {
                binaries.framework(listOf(NativeBuildType.DEBUG)) {
                    baseName = frameworkName
                    isStatic = true
                }
            }
        }
        macosArm64()
    }
    if (!jvmOnly && targetSet.declaredIn("tier1")) {
        linuxX64()
    }

    if (!jvmOnly && targetSet.declaredIn("web")) {
        js { nodejs() }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs { nodejs() }
    }
    if (!jvmOnly && targetSet.isAll) {
        linuxArm64()
    }
    if (!jvmOnly && targetSet.declaredIn("windows")) {
        mingwX64()
    }
    if (!jvmOnly && targetSet.declaredIn("apple")) {
        iosX64()
    }

    // Tier 3. Never add watchosArm32 (deprecated since Kotlin 2.4.20) or watchosX64 (deprecated since 2.3.20).
    if (!jvmOnly && targetSet.declaredIn("web") && wanted("wasmWasi")) {
        @OptIn(ExperimentalWasmDsl::class)
        wasmWasi { nodejs() }
    }
    if (!jvmOnly && targetSet.declaredIn("apple")) {
        watchosArm64()
        watchosDeviceArm64()
        watchosSimulatorArm64()
        tvosArm64()
        tvosSimulatorArm64()

        // Deprecated since Kotlin 2.3.20 and scheduled for removal; kept while kotlinx libraries still publish them.
        @Suppress("DEPRECATION")
        macosX64()
        @Suppress("DEPRECATION")
        tvosX64()
    }
    if (!jvmOnly && targetSet.isAll && wanted("androidNative")) {
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX86()
        androidNativeX64()
    }

    @OptIn(ExperimentalAbiValidation::class)
    abiValidation {
        enabled = true

        // One klib dump covers every klib target, so only the full target set can check or update it.
        klib { enabled = targetSet.isAll && !jvmOnly }
    }

    sourceSets {
        // Not published, so it follows the compiler; Kotlin/JS and Wasm require that.
        commonTest.dependencies {
            implementation(kotlin("test", kotlinPluginVersion))
        }
    }
}

kover {
    reports {
        verify {
            rule {
                minBound(95)
            }
        }
    }
}

// The JVM tests again with another default time zone of the JVM and of the process: nothing may depend on it, since
// the library never uses the default time zone implicitly.
val zoneTests = listOf("Kiritimati" to "Pacific/Kiritimati", "StJohns" to "America/St_Johns").map { (suffix, zone) ->
    tasks.register<Test>("jvmTest$suffix") {
        group = "verification"
        description = "Runs the JVM tests with $zone as the default time zone of the JVM and of the process."
        val jvmTest = tasks.named<Test>("jvmTest")
        testClassesDirs = files(jvmTest.map { it.testClassesDirs })
        classpath = files(jvmTest.map { it.classpath })
        systemProperty("user.timezone", zone)
        environment("TZ", zone)
        filter.excludeTestsMatching("*StressTest")
        filter.isFailOnNoMatchingTests = false
        mustRunAfter(jvmTest)
    }
}

tasks.named("check") {
    dependsOn(zoneTests)
}

// Apache-2.0 §4: every copy carries the license and the notices. A klib has no place for other files, so its POM names
// the license (calendrum.publishing). The type is the base class: the sources jars of the Kotlin plugin are of it, and
// the bundling Jar that `Jar` means here is a subclass that leaves them out.
val notice = layout.settingsDirectory.file("NOTICE")
val license = layout.settingsDirectory.file("LICENSE")

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    into("META-INF") { from(notice, license) }
}

if (objcHeaders) {
    val generatedHeader = layout.buildDirectory.file(
        "bin/iosSimulatorArm64/debugFramework/$frameworkName.framework/Headers/$frameworkName.h",
    )
    val snapshotName = "${project.name}.h"
    val headerSnapshot = layout.projectDirectory.file("api/$snapshotName")
    val headerUpdate = "./gradlew :${project.name}:updateObjcHeader"
    val checkObjcHeader = tasks.register("checkObjcHeader") {
        group = "verification"
        description = "Checks that the Objective-C header of the iOS framework matches api/${project.name}.h."
        dependsOn("linkDebugFrameworkIosSimulatorArm64")
        inputs.file(generatedHeader).withPathSensitivity(PathSensitivity.NONE).withPropertyName("generatedHeader")
        inputs.files(headerSnapshot).withPathSensitivity(PathSensitivity.NONE).withPropertyName("headerSnapshot")
        doLast {
            val expected = headerSnapshot.asFile
            val actual = generatedHeader.get().asFile
            if (!expected.isFile) throw GradleException("${expected.name} is missing, run $headerUpdate")
            if (expected.readText() != actual.readText()) {
                throw GradleException("The Objective-C header differs from ${expected.name}, run $headerUpdate")
            }
        }
    }
    tasks.register<Copy>("updateObjcHeader") {
        group = "verification"
        description = "Writes the Objective-C header of the iOS framework to api/${project.name}.h."
        dependsOn("linkDebugFrameworkIosSimulatorArm64")
        from(generatedHeader)
        into(layout.projectDirectory.dir("api"))
        rename { snapshotName }
    }
    tasks.named("check") {
        dependsOn(checkObjcHeader)
    }
}

// Mocha's default 2 s timeout is close to the time of the slowest JS tests of the core (`StreamEquivalenceTest`
// transcodes large inputs in 1-2 s) and fails them when Gradle links Kotlin/Native test binaries in parallel. The
// `wasmJs` test tasks run their own `KotlinWasmNode` framework, not Mocha, hence the name filter, as in
// `calendrum.benchmarks`.
tasks.withType<KotlinJsTest>().matching { !it.name.contains("wasmJs") }.configureEach {
    useMocha { timeout = "60s" }
}

// "report" lists undocumented public declarations without failing; "strict" turns every Dokka warning into an error.
val kdocStrict = providers.gradleProperty("calendrum.kdoc").map { it == "strict" }.orElse(false)
val sourceLinkRemote = providers.environmentVariable("GITHUB_REPOSITORY").map { "https://github.com/$it/blob/main" }

dokka {
    moduleName = project.name
    dokkaPublications.html {
        failOnWarning = kdocStrict
    }
    dokkaSourceSets.configureEach {
        reportUndocumented = true
        skipEmptyPackages = true
        // Fetching package lists from kotlinlang.org would make the build depend on the network and on its answer.
        enableKotlinStdLibDocumentationLink = false
        // The shared source set is analysed without its dependencies, so kotlinx-datetime types stay unresolved.
        if (name == "commonMain") classpath.from(kotlin.jvm().compilations.getByName("main").compileDependencyFiles)
        // A sample root may belong to one source set only; the samples run on every target, so common is enough.
        val samplesDirectory = layout.projectDirectory.dir("src/commonTest/kotlin/io/github/calendrum/samples")
        if (name == "commonMain" && samplesDirectory.asFile.isDirectory) samples.from(samplesDirectory)
        val moduleDocumentation = layout.projectDirectory.file("module.md")
        if (moduleDocumentation.asFile.isFile) includes.from(moduleDocumentation)
        sourceLinkRemote.orNull?.let { remote ->
            sourceLink {
                localDirectory = layout.projectDirectory.dir("src")
                remoteUrl("$remote/${project.name}/src")
            }
        }
    }
}

val checkKDoc = tasks.register("checkKDoc") {
    group = "verification"
    description = "Fails on an undocumented public declaration or a broken KDoc link when -Pcalendrum.kdoc=strict."
    dependsOn("dokkaGeneratePublicationHtml")
}

tasks.named("check") {
    dependsOn(checkKDoc)
}

// Java sources of the JVM target (the Java API tests in src/jvmTest/java) compile for Java 8 like the Kotlin code;
// without it the JVM-target validation of KGP fails (Java 21 against Kotlin 1.8). `-options` hides javac's warning that
// release 8 is obsolete; other lint warnings are errors, as for Kotlin.
tasks.withType<JavaCompile>().configureEach {
    options.release = jvmRelease
    options.compilerArgs.addAll(listOf("-Xlint:all,-options,-classfile", "-Werror"))
}
