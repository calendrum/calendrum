// AGP 9 compiles Kotlin itself; the Kotlin Android plugin is only put on the classpath (`apply false`) so that the
// built-in Kotlin is 2.3.21 instead of the 2.2.10 that AGP depends on.
plugins {
    id("com.android.application") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.compose") apply false
}
