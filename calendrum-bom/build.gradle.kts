plugins {
    `java-platform`
    id("calendrum.publishing")
    id("calendrum.lint")
}

dependencies {
    constraints {
        api(project(":calendrum-core"))

        // Maven cannot read Gradle module metadata, so Maven users depend on the JVM artifact directly.
        api("$group:calendrum-core-jvm:$version")
        api(project(":calendrum-tzdata"))
        api("$group:calendrum-tzdata-jvm:$version")
        api(project(":calendrum-jcal"))
        api("$group:calendrum-jcal-jvm:$version")
        api(project(":calendrum-kotlinx-io"))
        api("$group:calendrum-kotlinx-io-jvm:$version")
        api(project(":calendrum-okio"))
        api("$group:calendrum-okio-jvm:$version")
        api(project(":calendrum-ktor"))
        api("$group:calendrum-ktor-jvm:$version")
        api(project(":calendrum-ical4j-interop"))
        api("$group:calendrum-ical4j-interop-jvm:$version")
    }
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["javaPlatform"])
        }
    }
}
