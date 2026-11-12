plugins {
    kotlin("jvm")
    id("calendrum.lint")
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

dependencies {
    testImplementation(project(":calendrum-core"))
    testImplementation(project(":calendrum-jcal"))
    testImplementation(project(":calendrum-kotlinx-io"))
    testImplementation(project(":calendrum-okio"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.jazzer.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

val fuzzTargets = listOf(
    "Lexer",
    "Reader",
    "WriterRoundTrip",
    "Text",
    "Utf8Differential",
    "CharsetDifferential",
    "TreeRoundTrip",
    "Anonymizer",
    "StreamingDifferential",
    "Interceptor",
    "Fingerprint",
    "ValueCodecs",
    "Patch",
    "Recurrence",
    "Fixups",
    "TimeZone",
    "Occurrences",
    "Validation",
    "ITip",
    "JCal",
    "Schema",
    "Selection",
    "Adapters",
    "Canonical",
)
val fuzzDuration = providers.gradleProperty("fuzz.duration").orElse("10m")

// Settings shared by the regression run and the fuzzing tasks. `jazzer.instrument`: by default Jazzer instruments only
// class directories on the classpath, and `calendrum-core` arrives as a jar, so without it the fuzzer would get no
// coverage from the core. The JUnit default timeout of a test template method is what Jazzer turns into the libFuzzer
// `-timeout` (10 s per input); the own timeouts of JUnit are disabled, otherwise JUnit would cut the whole fuzzing run,
// which is one test template invocation. The heap size is the memory limit: Jazzer turns the RSS limit of libFuzzer
// off. `-XX:+EnableDynamicAgentLoading` silences the JDK 21 warning about the Jazzer agent.
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    maxHeapSize = "1g"
    jvmArgs("-XX:+EnableDynamicAgentLoading", "-XX:-OmitStackTraceInFastThrow")
    systemProperty("jazzer.instrument", "io.github.calendrum.**")
    systemProperty("junit.jupiter.execution.timeout.mode", "disabled")
    systemProperty("junit.jupiter.execution.timeout.testtemplate.method.default", "10 s")
}

tasks.test {
    // Regression mode always, even if the developer exported JAZZER_FUZZ.
    environment("JAZZER_FUZZ", "0")
}

val fuzzTasks = fuzzTargets.map { target ->
    tasks.register<Test>("fuzz$target") {
        group = "verification"
        description = "Fuzzes the $target target for -Pfuzz.duration (default 10m)."
        val test = sourceSets.test.get()
        testClassesDirs = test.output.classesDirs
        classpath = test.runtimeClasspath
        filter.includeTestsMatching("io.github.calendrum.fuzz.${target}FuzzTest")
        environment("JAZZER_FUZZ", "1")
        val duration = fuzzDuration
        jvmArgumentProviders.add(CommandLineArgumentProvider { listOf("-Djazzer.max_duration=${duration.get()}") })
        testLogging.showStandardStreams = true
        outputs.upToDateWhen { false }
        outputs.cacheIf { false }
    }
}
fuzzTasks.forEachIndexed { index, task -> task.configure { mustRunAfter(fuzzTasks.subList(0, index)) } }

// `fuzz.target` takes one target or a comma-separated list, so that `fuzz.yml` runs a group of targets per job.
tasks.register("fuzz") {
    group = "verification"
    description =
        "Fuzzes -Pfuzz.target (one or more of ${fuzzTargets.joinToString()}, comma-separated) or every target in turn."
    val chosen = providers.gradleProperty("fuzz.target").orNull
        ?.split(',')
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
    val unknown = chosen.orEmpty().filter { it !in fuzzTargets }
    require(unknown.isEmpty()) {
        "Unknown fuzz.target=${unknown.joinToString()}, expected one of: ${fuzzTargets.joinToString()}"
    }
    dependsOn(if (chosen == null) fuzzTasks else chosen.map { fuzzTasks[fuzzTargets.indexOf(it)] })
}
