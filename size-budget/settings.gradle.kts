val calendrumVersion = providers.fileContents(layout.rootDirectory.file("../gradle.properties")).asText
    .map { text ->
        text.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.startsWith("VERSION_NAME=") }
            ?.substringAfter('=')
            ?: error("No VERSION_NAME in ../gradle.properties")
    }
    .get()

val sizePlatforms = listOf("jvm", "js", "wasmJs", "android", "ios")
val androidSdk = listOf("ANDROID_HOME", "ANDROID_SDK_ROOT")
    .firstNotNullOfOrNull { variable -> providers.environmentVariable(variable).orNull?.takeIf { it.isNotBlank() } }
val unavailablePlatforms = buildMap {
    when {
        androidSdk == null -> put("android", "no Android SDK, ANDROID_HOME and ANDROID_SDK_ROOT are not set")
        !File(androidSdk).isDirectory -> put("android", "no Android SDK at $androidSdk")
    }
    if (!providers.systemProperty("os.name").get().startsWith("Mac")) {
        put("ios", "linking an iOS framework needs macOS")
    }
}
val requestedPlatforms = providers.gradleProperty("sizePlatforms").orNull
    ?.split(',')
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
val selectedPlatforms = if (requestedPlatforms == null) {
    sizePlatforms - unavailablePlatforms.keys
} else {
    val unknown = requestedPlatforms - sizePlatforms
    require(unknown.isEmpty()) { "Unknown size platforms $unknown in sizePlatforms, expected some of $sizePlatforms" }
    require(requestedPlatforms.isNotEmpty()) { "sizePlatforms is empty, expected some of $sizePlatforms" }
    val unavailable = requestedPlatforms.filter { it in unavailablePlatforms }
    require(unavailable.isEmpty()) {
        "Selected but not available on this host: " +
            unavailable.joinToString("; ") { "$it (${unavailablePlatforms[it]})" }
    }
    sizePlatforms.filter { it in requestedPlatforms }
}
gradle.extensions.extraProperties["sizeBudget.platforms"] = selectedPlatforms
gradle.extensions.extraProperties["sizeBudget.unavailablePlatforms"] = unavailablePlatforms

dependencyResolutionManagement {
    repositories {
        mavenLocal {
            content {
                includeGroup("io.github.calendrum")
            }
        }
        mavenCentral()
        google {
            content {
                includeGroupByRegex("com\\.android(\\..*)?")
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.google\\.android\\..*")
            }
        }
    }
    versionCatalogs {
        create("budget") {
            from(files("gradle/budget.versions.toml"))
            version("calendrum", calendrumVersion)
            library("calendrum-core", "io.github.calendrum", "calendrum-core").versionRef("calendrum")
        }
    }
}

rootProject.name = "calendrum-size-budget"

include(
    ":app-baseline",
    ":app-ics",
)
if ("android" in selectedPlatforms) {
    include(
        ":android-baseline",
        ":android-ics",
    )
}
