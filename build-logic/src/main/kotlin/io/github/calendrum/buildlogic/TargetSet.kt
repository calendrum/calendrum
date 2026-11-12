package io.github.calendrum.buildlogic

import org.gradle.api.provider.ProviderFactory

// The target set chosen by the `calendrum.targets` Gradle property (all, tier1, web, apple, windows, jvm; default all),
// read once per project as a configuration input. `declaredIn(sets)`: `all` declares every target; a smaller set
// declares only the targets that name it. Unknown set names fail the build, both in the property and in `declaredIn`: a
// typo in a build script must not silently drop a target.
class TargetSet private constructor(val name: String) {
    val isAll: Boolean get() = name == ALL

    fun declaredIn(vararg sets: String): Boolean {
        require(sets.all { it in NAMES }) { "Unknown target sets: ${sets.filter { it !in NAMES }.joinToString()}" }
        return isAll || name in sets
    }

    companion object {
        private const val PROPERTY = "calendrum.targets"
        private const val ALL = "all"
        private val NAMES = listOf(ALL, "tier1", "web", "apple", "windows", "jvm")

        fun of(providers: ProviderFactory): TargetSet {
            val name = providers.gradleProperty(PROPERTY).getOrElse(ALL)
            require(name in NAMES) { "Unknown $PROPERTY=$name, expected one of: ${NAMES.joinToString()}" }
            return TargetSet(name)
        }
    }
}
