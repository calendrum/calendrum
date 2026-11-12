plugins {
    id("calendrum.kmp-library")
}

kotlin {
    sourceSets.commonMain {
        dependencies {
            api(project(":calendrum-core"))
            api(libs.ktor.serialization)
            api(libs.ktor.server.core)
        }
    }
    sourceSets.commonTest {
        dependencies {
            implementation(libs.ktor.server.test.host)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.mock)
        }
    }
}
