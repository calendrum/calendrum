plugins {
    id("calendrum.lint")
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.kover)
}

dokka {
    moduleName = "calendrum"
}

dependencies {
    // The root Kover report aggregates every published KMP module (the calendrum.kmp-library convention). The list is
    // explicit, like the constraints of the BOM: reading which plugins other projects apply would be cross-project
    // configuration, which project isolation forbids. A new published module adds a line here.
    kover(project(":calendrum-core"))
    kover(project(":calendrum-tzdata"))
    kover(project(":calendrum-jcal"))
    kover(project(":calendrum-kotlinx-io"))
    kover(project(":calendrum-okio"))
    kover(project(":calendrum-ktor"))
    kover(project(":calendrum-ical4j-interop"))
    dokka(project(":calendrum-core"))
    dokka(project(":calendrum-tzdata"))
    dokka(project(":calendrum-jcal"))
    dokka(project(":calendrum-kotlinx-io"))
    dokka(project(":calendrum-okio"))
    dokka(project(":calendrum-ktor"))
    dokka(project(":calendrum-ical4j-interop"))
}

// The consumer templates in compat-tests and the size-budget build are separate Gradle builds and are linted here. A
// file tree with excludes is not walked into build/, .gradle/, .kotlin/ and bin/ (IDE output) at all; Spotless's
// `targetExclude` would walk them to subtract their files, and compat-tests/build and size-budget/**/build hold
// hundreds of megabytes. The examples and sdk builds are linted the same way.
fun sources(directory: String, pattern: String): ConfigurableFileTree = fileTree(directory) {
    include(pattern)
    exclude("**/build/**", "**/.gradle/**", "**/.kotlin/**", "**/bin/**")
}

// build-logic cannot apply its own convention plugins, so the root project lints its sources.
spotless {
    kotlin {
        target(
            "build-logic/src/**/*.kt",
            sources("compat-tests", "**/*.kt"),
            sources("size-budget", "**/*.kt"),
            sources("examples", "**/*.kt"),
            sources("sdk", "**/*.kt"),
        )
    }
    kotlinGradle {
        target(
            "*.gradle.kts",
            "build-logic/*.gradle.kts",
            "build-logic/src/**/*.gradle.kts",
            sources("compat-tests", "**/*.gradle.kts"),
            sources("size-budget", "**/*.gradle.kts"),
            sources("examples", "**/*.gradle.kts"),
            sources("sdk", "**/*.gradle.kts"),
        )
    }
}

// The reverse of GuidesSyncTest: after `format`, `syncGuides` copies the compiled regions into the blocks of
// guides/*.md, and `syncDiagnosticsGuide` the KDoc of the ProblemCode constants into the tables of guides/diagnostics.md.
tasks.register<io.github.calendrum.buildlogic.SyncGuides>("syncGuides") {
    group = "documentation"
    description = "Rewrites every sample block of guides/*.md with the source region of the same name."
    root = layout.projectDirectory
}

tasks.register<io.github.calendrum.buildlogic.SyncDiagnosticsGuide>("syncDiagnosticsGuide") {
    group = "documentation"
    description = "Rewrites the rows of guides/diagnostics.md from the KDoc of the ProblemCode constants."
    root = layout.projectDirectory
}

// Nothing that goes into the repository may point at the private design documents, decisions or tasks of the
// maintainers (CheckPrivateReferences). The tree leaves out what .gitignore keeps out, third-party data and the fuzz
// inputs, whose bytes are random.
val checkPrivateReferences = tasks.register<io.github.calendrum.buildlogic.CheckPrivateReferences>(
    "checkPrivateReferences",
) {
    group = "verification"
    description = "Checks that no file of the repository points at the private design documents, decisions or tasks."
    root = layout.projectDirectory
    sources.from(
        fileTree(".") {
            exclude(
                "**/build/**", "**/.gradle/**", "**/.kotlin/**", "**/bin/**", "**/obj/**", "**/node_modules/**",
                "**/.venv/**", ".idea/**", ".bench-runners/**", "**/.cifuzz-corpus/**", "docs/**", "AGENTS.local.md",
                "corpus/real/**", "**/real-*", "**/real-*/**", "**/seed-real-*", "third_party/**", "gradle/wrapper/**",
                "kotlin-js-store/**", "fuzz/src/test/resources/**",
            )
        },
    )
    report = layout.buildDirectory.file("reports/private-references.txt")
}

tasks.named("check") {
    dependsOn(checkPrivateReferences)
}
