plugins {
    id("calendrum.benchmarks")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":calendrum-core"))
            implementation(project(":calendrum-tzdata"))
            implementation(project(":calendrum-kotlinx-io"))
            implementation(project(":calendrum-okio"))
        }
        jvmMain.dependencies {
            implementation(libs.ical4j)
            implementation(libs.biweekly)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Without this, `HugeStructuralTest` (jvmTest) throws `OutOfMemoryError` on the default heap of the JVM test task while
// unfolding the ~100 MB `huge` corpus into logical lines.
tasks.withType<Test>().matching { it.name != "hugeStreamTest" }.configureEach {
    maxHeapSize = "2g"
}

// The fuzz inputs of the `fuzz` module, read as plain files (no access to the model of that project). `FuzzSeedTest`
// gets their directory as a system property; the `seed-invite.ics` copies are inputs of `jvmTest`, so a changed seed
// reruns it.
val fuzzInputs = layout.settingsDirectory.dir("fuzz/src/test/resources/io/github/calendrum/fuzz")

tasks.named<Test>("jvmTest") {
    filter.excludeTestsMatching("io.github.calendrum.benchmarks.stream.HugeStreamMemoryTest")
    systemProperty("calendrum.fuzzInputs", fuzzInputs.asFile.absolutePath)
    inputs.files(fileTree(fuzzInputs) { include("**/seed-invite.ics") })
        .withPathSensitivity(PathSensitivity.RELATIVE)
        .withPropertyName("fuzzSeeds")
    inputs.files(fileTree(layout.settingsDirectory.dir("corpus/real")))
        .withPathSensitivity(PathSensitivity.RELATIVE)
        .withPropertyName("realCorpus")
    inputs.dir(layout.settingsDirectory.dir("conformance/ical"))
        .withPathSensitivity(PathSensitivity.RELATIVE)
        .withPropertyName("conformanceCases")
}

val hugeStreamTest by tasks.registering(Test::class) {
    group = "verification"
    description = "Streams the huge corpus through ComponentStream in a 128 MiB heap and prints the peak heap."
    val jvmTest = tasks.named<Test>("jvmTest").get()
    testClassesDirs = jvmTest.testClassesDirs
    classpath = jvmTest.classpath
    filter.includeTestsMatching("io.github.calendrum.benchmarks.stream.HugeStreamMemoryTest")
    maxHeapSize = "128m"
    testLogging.showStandardStreams = true
}

tasks.named("check") {
    dependsOn(hugeStreamTest)
}

// A `JavaExec` on the classpath of the JVM `main` compilation, mirroring how kotlinx-benchmark already runs
// `org.openjdk.jmh.Main` on the JVM benchmark classpath: the simplest way to run project code from a Gradle task
// without a second build.
val generateCorpora by tasks.registering(JavaExec::class) {
    group = "benchmark"
    description = "Generates the benchmark corpora into build/corpora and writes SHA256SUMS."
    val compilation = kotlin.jvm().compilations.getByName("main")
    classpath(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
    mainClass.set("io.github.calendrum.benchmarks.corpus.GenerateCorporaKt")
    args(layout.buildDirectory.dir("corpora").get().asFile.absolutePath)
    dependsOn("jvmMainClasses")
}

// A `JavaExec` on the JVM test classpath, like `:conformance:updateExpected`: it writes the `invite` corpus into every
// `seed-invite.ics` of the fuzz inputs.
val updateFuzzSeeds by tasks.registering(JavaExec::class) {
    group = "verification"
    description = "Rewrites every seed-invite.ics fuzz input with the invite corpus of the current generator."
    val compilation = kotlin.jvm().compilations.getByName("test")
    classpath(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
    mainClass.set("io.github.calendrum.benchmarks.fuzz.FuzzSeedsKt")
    args(fuzzInputs.asFile.absolutePath)
    dependsOn("jvmTestClasses")
}

val baselineEquivalence by tasks.registering(JavaExec::class) {
    group = "benchmark"
    description =
        "Checks that ical4j, biweekly and the corpus generator see the same corpora; -Pcorpora=a,b limits them."
    val compilation = kotlin.jvm().compilations.getByName("main")
    classpath(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
    mainClass.set("io.github.calendrum.benchmarks.baseline.EquivalenceKt")
    maxHeapSize = "2g"
    jvmArgs("-Duser.timezone=UTC", "-Duser.language=en", "-Duser.country=US", "-Dfile.encoding=UTF-8")
    args(providers.gradleProperty("corpora").map { it.split(',') }.getOrElse(emptyList()))
    dependsOn("jvmMainClasses")
}
