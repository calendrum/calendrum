plugins {
    id("calendrum.kmp-library")
}

kotlin {
    sourceSets.jvmMain {
        dependencies {
            api(project(":calendrum-core"))
            api(libs.ical4j.interop)
        }
    }
}

val conformanceCases = layout.settingsDirectory.dir("conformance/ical")
val realCorpus = layout.settingsDirectory.dir("corpus/real")

tasks.withType<Test>().matching { it.name.startsWith("jvmTest") }.configureEach {
    systemProperty("calendrum.conformance", conformanceCases.asFile.absolutePath)
    systemProperty("calendrum.corpus", realCorpus.asFile.absolutePath)
    inputs.dir(conformanceCases).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("conformanceCases")
    inputs.files(fileTree(realCorpus)).withPathSensitivity(PathSensitivity.RELATIVE).withPropertyName("realCorpus")
}
