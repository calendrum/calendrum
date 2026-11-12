plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }

    jvm()
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.io.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jvmTest.dependencies {
            implementation(project.dependencies.platform(libs.junit.bom))
            implementation(libs.junit.jupiter)
            runtimeOnly(libs.junit.platform.launcher)
        }
        val posixMain by creating {
            dependsOn(nativeMain.get())
        }
        macosMain.get().dependsOn(posixMain)
        linuxMain.get().dependsOn(posixMain)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
