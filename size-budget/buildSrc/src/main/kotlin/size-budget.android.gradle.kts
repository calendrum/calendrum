import com.android.build.api.artifact.SingleArtifact
import io.github.calendrum.sizebudget.MeasureAndroidSize
import io.github.calendrum.sizebudget.SIZE_USAGE
import io.github.calendrum.sizebudget.SizePlatform
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.application")
}

val budget = the<VersionCatalogsExtension>().named("budget")
val shared = rootProject.layout.projectDirectory.dir("shared/android")

android {
    namespace = "io.github.calendrum.sizebudget"
    compileSdk = 36
    defaultConfig {
        applicationId = "io.github.calendrum.sizebudget"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                rootProject.layout.projectDirectory.file("android-rules.pro"),
            )
        }
    }
    lint {
        checkReleaseBuilds = false
    }
    sourceSets {
        named("main") {
            manifest.srcFile(shared.file("AndroidManifest.xml"))
            kotlin.directories += shared.dir("kotlin").asFile.path
        }
    }
}

val measureAndroidSize = tasks.register<MeasureAndroidSize>("measureAndroidSize") {
    group = "verification"
    description = "Measures the ${SizePlatform.ANDROID.title} release APK of this app."
    tools.put("kotlin", getKotlinPluginVersion())
    tools.put("agp", budget.findVersion("agp").get().requiredVersion)
    tools.put("agpR8", com.android.tools.r8.Version.LABEL)
    output = layout.buildDirectory.file("size/$name.json")
}

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        measureAndroidSize.configure {
            apkDir = variant.artifacts.get(SingleArtifact.APK)
        }
    }
}

configurations.consumable("sizeMeasurements") {
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(SIZE_USAGE))
    outgoing.artifact(measureAndroidSize.flatMap { it.output })
}
