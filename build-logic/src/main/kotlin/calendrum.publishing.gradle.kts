// Coordinates and publishing shared by every published module. Local: `./gradlew publishToMavenLocal`. Releases: the
// publish workflow sends them to GitHub Packages.

plugins {
    `maven-publish`
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

// Reproducible archives: no file timestamps, a fixed file order and the same permissions (0644 for files, 0755 for
// directories) on every host, whatever the umask. Gradle 9 already defaults to this; stating it keeps the artifacts
// reproducible if a default or a plugin changes.
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    filePermissions { unix("rw-r--r--") }
    dirPermissions { unix("rwxr-xr-x") }
}

// The license in every POM, the BOM's too: Maven Central requires it, and a klib has no other place for it. The name is the
// SPDX identifier, as the POM reference recommends.
publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            licenses {
                license {
                    name = "Apache-2.0"
                    url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    distribution = "repo"
                }
            }
        }
    }
}

// Only GitHub Actions sets GITHUB_REPOSITORY, so local builds never get this repository. The URL follows the repository
// that runs the workflow, so nothing changes when the repository moves to another account.
val githubRepository = providers.environmentVariable("GITHUB_REPOSITORY")
if (githubRepository.isPresent) {
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"

                // GitHub Packages does not accept upper case in the owner name.
                url = uri("https://maven.pkg.github.com/${githubRepository.get().lowercase()}")

                // The GitHubPackagesUsername and GitHubPackagesPassword Gradle properties, which the publish workflow
                // sets from GITHUB_ACTOR and GITHUB_TOKEN. Gradle reads them only when publishing and keeps the token
                // out of the configuration cache.
                credentials(PasswordCredentials::class)
            }
        }
    }
}
