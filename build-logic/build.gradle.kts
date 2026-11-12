plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.allopen.gradle.plugin)
    implementation(libs.kotlinx.benchmark.gradle.plugin)
    implementation(libs.kotlinx.kover.gradle.plugin)
    implementation(libs.spotless.gradle.plugin)
}
