import io.github.calendrum.buildlogic.ValidationRuleSources
import io.github.calendrum.buildlogic.tz.TimeZoneAliasSources

plugins {
    id("calendrum.kmp-library")
}

// `Calendrum.VERSION` is generated from VERSION_NAME in gradle.properties, so it cannot go stale.
val generateLibraryVersion = tasks.register("generateLibraryVersion") {
    val version = project.version.toString()
    val outputDir = layout.buildDirectory.dir("generated/calendrum/commonMain/kotlin")
    inputs.property("version", version)
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("io/github/calendrum/LibraryVersion.kt").asFile
        file.parentFile.mkdirs()
        file.writeText("package io.github.calendrum\n\ninternal const val LIBRARY_VERSION: String = \"$version\"\n")
    }
}

val generateTimeZoneAliases = tasks.register<TimeZoneAliasSources>("generateTimeZoneAliases") {
    description = "Generates the time zone alias tables from the pinned tzdb and CLDR files."
    tzdbDirectory.set(layout.settingsDirectory.dir("third_party/tzdb"))
    windowsZones.set(layout.settingsDirectory.file("third_party/cldr/windowsZones.xml"))
    outputDirectory.set(layout.buildDirectory.dir("generated/calendrum/timeZoneAliases/kotlin"))
}

val generateValidationRules = tasks.register<ValidationRuleSources>("generateValidationRules") {
    description = "Generates the validation rule tables from the spec tables."
    specDirectory.set(layout.settingsDirectory.dir("spec"))
    outputDirectory.set(layout.buildDirectory.dir("generated/calendrum/validationRules/kotlin"))
}

kotlin {
    sourceSets.all {
        // The module that declares the internal API uses it everywhere, main and test code alike; opting in once here
        // keeps `@OptIn` out of every file. Other modules opt in per file.
        languageSettings.optIn("io.github.calendrum.InternalICalApi")
    }
    sourceSets.commonMain {
        kotlin.srcDir(generateLibraryVersion)
        kotlin.srcDir(generateTimeZoneAliases)
        kotlin.srcDir(generateValidationRules)
        dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}

val spec = layout.settingsDirectory.dir("spec")
val thirdParty = layout.settingsDirectory.dir("third_party")

tasks.named<Test>("jvmTest") {
    filter.excludeTestsMatching("io.github.calendrum.recur.MillionRulesStressTest")
}

// `GuidesSyncTest` compares the guides with the sources of the regions in them, which are in other modules and in the
// example; Gradle must rerun it when any of them changes.
val guideSources = layout.settingsDirectory.asFileTree.matching {
    include("guides/**", "README.md", "MIGRATION.md", "examples/ios-swiftui/**/*.swift", "examples/ios-swiftui/**/*.kt")
    include("examples/ios-swiftui/**/*.kts", "calendrum-*/src/**/guides/*.kt", "calendrum-*/src/**/guides/*.java")
    exclude("**/build/**")
}

tasks.withType<Test>().matching { it.name.startsWith("jvmTest") }.configureEach {
    inputs.files(guideSources).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("guideSources")
    systemProperty("calendrum.spec", spec.asFile.absolutePath)
    inputs.dir(spec).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("spec")
    systemProperty("calendrum.thirdParty", thirdParty.asFile.absolutePath)
    inputs.dir(thirdParty).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("thirdParty")
}

val recurrenceStressTest by tasks.registering(Test::class) {
    group = "verification"
    description = "Expands a million random recurrence rules on the JVM and fails on a broken invariant or a slow rule."
    val jvmTest = tasks.named<Test>("jvmTest").get()
    testClassesDirs = jvmTest.testClassesDirs
    classpath = jvmTest.classpath
    filter.includeTestsMatching("io.github.calendrum.recur.MillionRulesStressTest")
    providers.gradleProperty("recurrence.stress.rules").orNull?.let { systemProperty("calendrum.stress.rules", it) }
    outputs.upToDateWhen { false }
    testLogging.showStandardStreams = true
}
