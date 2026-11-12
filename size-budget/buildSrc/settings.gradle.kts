dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android(\\..*)?")
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.google\\.android\\..*")
                includeGroup("com.google.testing.platform")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
        create("budget") {
            from(files("../gradle/budget.versions.toml"))
        }
    }
}
