import io.github.calendrum.sizebudget.SIZE_USAGE
import io.github.calendrum.sizebudget.SizePlatform
import io.github.calendrum.sizebudget.SizeReport
import io.github.calendrum.sizebudget.selectedSizePlatforms
import io.github.calendrum.sizebudget.unavailableSizePlatforms

val sizeUsage = objects.named<Usage>(SIZE_USAGE)
val sizePlatforms = selectedSizePlatforms

val baselineAppDependency = configurations.dependencyScope("baselineApp")
val icsAppDependency = configurations.dependencyScope("icsApp")
val baselineAppSize = configurations.resolvable("baselineAppSize") {
    extendsFrom(baselineAppDependency.get())
    attributes.attribute(Usage.USAGE_ATTRIBUTE, sizeUsage)
}
val icsAppSize = configurations.resolvable("icsAppSize") {
    extendsFrom(icsAppDependency.get())
    attributes.attribute(Usage.USAGE_ATTRIBUTE, sizeUsage)
}

dependencies {
    add(baselineAppDependency.name, project(":app-baseline"))
    add(icsAppDependency.name, project(":app-ics"))
    if (SizePlatform.ANDROID in sizePlatforms) {
        add(baselineAppDependency.name, project(":android-baseline"))
        add(icsAppDependency.name, project(":android-ics"))
    }
}

tasks.register<SizeReport>("sizeReport") {
    group = "verification"
    description = "Measures what Calendrum adds to a consumer app on the selected platforms (-PsizePlatforms) " +
        "and checks it against baseline.json."
    baselineApp.from(baselineAppSize)
    icsApp.from(icsAppSize)
    platforms = sizePlatforms.map { it.id }
    unavailablePlatforms = unavailableSizePlatforms
    budgetFile = layout.projectDirectory.file("baseline.json")
    calendrumVersion = budget.versions.calendrum
    updateBaseline = providers.gradleProperty("updateBaseline").map { it.toBoolean() }.orElse(false)
    jsonReport = layout.buildDirectory.file("size-report.json")
    markdownReport = layout.buildDirectory.file("size-report.md")
}
