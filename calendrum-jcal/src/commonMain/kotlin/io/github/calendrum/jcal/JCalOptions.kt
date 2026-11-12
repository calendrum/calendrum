package io.github.calendrum.jcal

import io.github.calendrum.Problem
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.syntax.ICalComponent
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * Settings of jCal reading and writing: the registry that tells the type of every property and the deepest nesting
 * that reading accepts. Immutable.
 *
 * @property registry the registry; [ICalRegistry.Default] unless set. Install your own `X-` properties to give them
 *   types.
 * @property maxDepth the deepest nesting of components that reading accepts, the calendar being 1; 16 unless set, as
 *   `ParseLimits.maxDepth` of the text form. A deeper component is skipped with `ICAL-J007`, so hostile JSON nested
 *   thousands of levels deep cannot exhaust the stack.
 */
public class JCalOptions internal constructor(public val registry: ICalRegistry, public val maxDepth: Int) {
    /** Returns the registry and the depth limit. */
    override fun toString(): String = "JCalOptions(registry=$registry, maxDepth=$maxDepth)"

    /** Collects [JCalOptions] values. Not thread-safe. */
    public class Builder internal constructor() {
        /** The registry that tells the type of every property; the default registry by default. */
        public var registry: ICalRegistry = ICalRegistry.Default

        /** The deepest nesting of components that reading accepts; positive. The default is 16. */
        public var maxDepth: Int = DEFAULT_MAX_DEPTH

        /** Sets [registry] to [value] and returns this builder. */
        public fun registry(value: ICalRegistry): Builder = apply { registry = value }

        /** Sets [maxDepth] to [value] and returns this builder. */
        public fun maxDepth(value: Int): Builder = apply { maxDepth = value }

        /**
         * Creates the options.
         *
         * @throws IllegalArgumentException if [maxDepth] is not positive.
         */
        public fun build(): JCalOptions {
            require(maxDepth > 0) { "maxDepth must be positive" }
            return JCalOptions(registry, maxDepth)
        }

        /** Returns the name of the builder. */
        override fun toString(): String = "JCalOptions.Builder"
    }

    /** The default options and a builder. */
    public companion object {
        /** Options with the default registry and a nesting depth of 16. */
        @JvmField
        public val Default: JCalOptions = JCalOptions(ICalRegistry.Default, DEFAULT_MAX_DEPTH)

        /** Returns a builder that starts from the default options. */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

/**
 * The result of reading jCal.
 *
 * @property components the components that could be read, as a lossless tree.
 * @property problems what was skipped or repaired, with `ICAL-J` codes. The text of a problem names the element in JSON
 *   (such as `$[0][1][3]`); there is no source position in JSON text.
 */
public class JCalResult internal constructor(
    public val components: List<ICalComponent>,
    public val problems: List<Problem>,
) {
    /** Returns the number of components and problems. */
    override fun toString(): String = "JCalResult(${components.size} components, ${problems.size} problems)"
}

// The same bound as the text form (`ParseLimits`): real calendars nest at most four levels.
private const val DEFAULT_MAX_DEPTH = 16
