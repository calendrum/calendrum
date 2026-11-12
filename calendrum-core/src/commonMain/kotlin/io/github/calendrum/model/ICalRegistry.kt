package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.checkedName
import io.github.calendrum.syntax.describe
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * The table of what every property, parameter and component is: its value type, allowed types, parameters, cardinality
 * and where it comes from.
 *
 * The registry drives eager decoding, strict mode and `decodeAny`, and tells the validator what a known name is.
 * [Default] holds RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253. Add your own `X-` properties, or the vendor
 * properties of [VendorProperties], by building a registry with [builder]. A registry is immutable and has no global
 * state: pass it to the parser through `ParseOptions`.
 *
 * @property extensions the extensions installed in this registry, in order.
 */
public class ICalRegistry private constructor(
    private val knownProperties: Array<Array<PropertyDefinition>?>,
    private val otherProperties: Map<String, Array<PropertyDefinition>>,
    private val knownParameters: Array<ParameterDefinition?>,
    private val otherParameters: Map<String, ParameterDefinition>,
    private val knownComponents: Array<ComponentDefinition?>,
    private val otherComponents: Map<String, ComponentDefinition>,
    public val extensions: List<ICalExtension>,
) {
    /**
     * Returns the definition of the property [name], ignoring ASCII case, or `null` when the registry does not know it.
     * When several components define the property differently, the first definition is returned.
     */
    public fun property(name: String): PropertyDefinition? = propertyDefinitions(name)?.get(0)

    /**
     * Returns the definition of the parameter [name], ignoring ASCII case, or `null` when the registry does not know
     * it.
     */
    public fun parameter(name: String): ParameterDefinition? {
        val id = KnownNames.find(name)
        return if (id >= 0) knownParameters[id] else otherParameters[canonicalName(name)]
    }

    /**
     * Returns the definition of the component [name], ignoring ASCII case, or `null` when the registry does not know
     * it.
     */
    public fun component(name: String): ComponentDefinition? {
        val id = KnownNames.find(name)
        return if (id >= 0) knownComponents[id] else otherComponents[canonicalName(name)]
    }

    // The definition for a property of a component: the one whose cardinalities name the component, else the first of
    // the name (a property outside its components is still decoded, as before the registry).
    internal fun property(id: Int, name: String, component: String): PropertyDefinition? {
        val all = (if (id >= 0) knownProperties[id] else otherProperties[name]) ?: return null
        if (all.size == 1) return all[0]
        for (definition in all) {
            if (definition.cardinality.containsKey(component)) return definition
        }
        return all[0]
    }

    internal fun knowsComponent(id: Int, name: String): Boolean =
        (if (id >= 0) knownComponents[id] else otherComponents[name]) != null

    private fun propertyDefinitions(name: String): Array<PropertyDefinition>? {
        val id = KnownNames.find(name)
        return if (id >= 0) knownProperties[id] else otherProperties[canonicalName(name)]
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("ICalRegistry", "extensions", extensions.size)

    /** Collects definitions and extensions into an [ICalRegistry]. Not thread-safe. */
    @ExperimentalICalApi
    public class Builder internal constructor() {
        private val properties = LinkedHashMap<String, ArrayList<PropertyDefinition>>()
        private val parameters = LinkedHashMap<String, ParameterDefinition>()
        private val components = LinkedHashMap<String, ComponentDefinition>()
        private val installed = ArrayList<ICalExtension>()

        /**
         * Installs [extension] once: a second install of the same extension does nothing. The extension adds its
         * definitions to this builder.
         */
        public fun install(extension: ICalExtension): Builder {
            for (existing in installed) {
                if (existing == extension) return this
            }
            installed += extension
            extension.contribute(this)
            return this
        }

        // Definitions of one key merge: cardinalities (later ones win per component), parameters and types are unions,
        // sources are joined with `; ` — this is how RFC 7986/9073/9074/9253 extend RFC 5545 properties. A definition
        // with another key replaces the definitions of the name it overlaps by component (or all, when either has no
        // cardinalities); definitions of disjoint components stay side by side (STATUS of VEVENT, VTODO, VJOURNAL)
        // .

        /**
         * Adds a property definition. A definition for a key that is already defined is merged into it; one for the
         * same name in other components is kept beside it.
         */
        public fun property(definition: PropertyDefinition): Builder {
            val name = definition.name
            val all = properties.getOrPut(name) { ArrayList(1) }
            for (index in all.indices) {
                if (all[index].key === definition.key) {
                    all[index] = all[index].merged(definition)
                    return this
                }
            }
            all.removeAll { overlaps(it.cardinality, definition.cardinality) }
            all += definition
            return this
        }

        /** Adds a parameter definition, replacing a definition of another key for the same name. */
        public fun parameter(definition: ParameterDefinition): Builder {
            val name = definition.name
            val existing = parameters[name]
            parameters[name] = if (existing != null && existing.key === definition.key) {
                ParameterDefinition(existing.key, joined(existing.source, definition.source))
            } else {
                definition
            }
            return this
        }

        /** Adds a component definition; the children of a definition for the same name are merged. */
        public fun component(definition: ComponentDefinition): Builder {
            val name = definition.name
            val existing = components[name]
            components[name] = if (existing == null) {
                definition
            } else {
                ComponentDefinition(
                    name,
                    existing.children + definition.children,
                    joined(existing.source, definition.source),
                )
            }
            return this
        }

        /** Creates the registry. */
        public fun build(): ICalRegistry {
            val knownProperties = arrayOfNulls<Array<PropertyDefinition>>(KnownNames.count)
            val otherProperties = HashMap<String, Array<PropertyDefinition>>()
            for ((name, all) in properties) {
                if (all.isEmpty()) continue
                val array = all.toTypedArray()
                val id = KnownNames.find(name)
                if (id >= 0) knownProperties[id] = array else otherProperties[name] = array
            }
            val knownParameters = arrayOfNulls<ParameterDefinition>(KnownNames.count)
            val otherParameters = HashMap<String, ParameterDefinition>()
            for ((name, definition) in parameters) {
                val id = KnownNames.find(name)
                if (id >= 0) knownParameters[id] = definition else otherParameters[name] = definition
            }
            val knownComponents = arrayOfNulls<ComponentDefinition>(KnownNames.count)
            val otherComponents = HashMap<String, ComponentDefinition>()
            for ((name, definition) in components) {
                val id = KnownNames.find(name)
                if (id >= 0) knownComponents[id] = definition else otherComponents[name] = definition
            }
            val extensions: List<ICalExtension> =
                if (installed.isEmpty()) emptyList() else ArrayView(installed.toTypedArray())
            return ICalRegistry(
                knownProperties,
                otherProperties,
                knownParameters,
                otherParameters,
                knownComponents,
                otherComponents,
                extensions,
            )
        }

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("ICalRegistry.Builder", "extensions", installed.size)
    }

    /** The default registry and a builder. */
    public companion object {
        @Volatile
        private var defaultRegistry: ICalRegistry? = null

        // A getter with a cache, not a companion `val`: a `val` would build every definition, key and codec in any
        // application that touches the class; `ParseOptions` keeps `null` for the default for the same reason.

        /** The registry with RFC 5545, RFC 7986, RFC 9073, RFC 9074 and RFC 9253; created on first use. */
        @OptIn(ExperimentalICalApi::class)
        @JvmStatic
        public val Default: ICalRegistry
            get() = defaultRegistry ?: builder()
                .install(Rfc5545)
                .install(Rfc7986)
                .install(Rfc9073)
                .install(Rfc9074)
                .install(Rfc9253)
                .build()
                .also { defaultRegistry = it }

        /** Returns an empty builder; install [Rfc5545] and the other extensions you need. */
        @ExperimentalICalApi
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

/** A set of definitions that can be installed into a registry, such as a standard or a vendor's properties. */
public fun interface ICalExtension {
    /** Adds this extension's definitions to [registry]. */
    @ExperimentalICalApi
    public fun contribute(registry: ICalRegistry.Builder)
}

/** How many times a property may occur in a component. */
public enum class Cardinality {
    /** Exactly once. */
    One,

    /** At most once. */
    Optional,

    /** Any number of times. */
    Many,

    /** At least once. */
    OneOrMore,
}

/**
 * What the registry knows about a property: its key, value types, allowed parameters and how often it occurs in each
 * component. Immutable.
 *
 * @property key the typed key that decodes the property.
 * @property defaultType the value type used when no `VALUE` parameter says otherwise.
 * @property source the standard or vendor that defines it, such as `RFC 5545 §3.8.2.4`.
 */
public class PropertyDefinition(
    public val key: PropertyKey<*>,
    public val defaultType: ValueType,
    allowedTypes: Set<ValueType>,
    parameters: Set<String>,
    cardinality: Map<String, Cardinality>,
    public val source: String,
) {
    /** The canonical name of the property. */
    public val name: String get() = key.name

    /** The value types the property may have, including the default one. */
    public val allowedTypes: Set<ValueType> = frozenSet(allowedTypes + defaultType)

    /** The names of the parameters that apply to the property. */
    public val parameters: Set<String> = frozenSet(parameters.map { checkedName(it, "parameter name") })

    /** How often the property occurs in each component, by component name. */
    public val cardinality: Map<String, Cardinality> = componentMap(cardinality)

    internal fun merged(other: PropertyDefinition): PropertyDefinition = PropertyDefinition(
        key,
        defaultType,
        allowedTypes + other.allowedTypes,
        parameters + other.parameters,
        cardinality + other.cardinality,
        joined(source, other.source),
    )

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("PropertyDefinition", "name", name, "source", source)
}

/**
 * What the registry knows about a parameter. Immutable.
 *
 * @property key the typed key that decodes the parameter.
 * @property source the standard or vendor that defines it.
 */
public class ParameterDefinition(public val key: ParameterKey<*>, public val source: String) {
    /** The canonical name of the parameter. */
    public val name: String get() = key.name

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("ParameterDefinition", "name", name, "source", source)
}

/**
 * What the registry knows about a component. Immutable.
 *
 * @property source the standard or vendor that defines it.
 */
public class ComponentDefinition(name: String, children: Set<String>, public val source: String) {
    /** The canonical name of the component. */
    public val name: String = checkedName(name, "component name")

    /** The names of the components that may be nested in it. */
    public val children: Set<String> = frozenSet(children.map { checkedName(it, "component name") })

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("ComponentDefinition", "name", name, "source", source)
}

private fun overlaps(left: Map<String, Cardinality>, right: Map<String, Cardinality>): Boolean {
    if (left.isEmpty() || right.isEmpty()) return true
    for (component in left.keys) {
        if (right.containsKey(component)) return true
    }
    return false
}

private fun joined(left: String, right: String): String = if (left == right) left else "$left; $right"

private fun <T> frozenSet(values: Collection<T>): Set<T> = if (values.isEmpty()) emptySet() else LinkedHashSet(values)

private fun componentMap(values: Map<String, Cardinality>): Map<String, Cardinality> {
    if (values.isEmpty()) return emptyMap()
    val result = LinkedHashMap<String, Cardinality>()
    for ((component, cardinality) in values) result[checkedName(component, "component name")] = cardinality
    return result
}
