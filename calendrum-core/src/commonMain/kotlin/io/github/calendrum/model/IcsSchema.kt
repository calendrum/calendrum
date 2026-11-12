package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.Selection
import io.github.calendrum.syntax.TimeZoneSelection
import io.github.calendrum.syntax.checkedName
import io.github.calendrum.syntax.describe
import kotlin.concurrent.Volatile

/**
 * Describes how a component maps to your own class, so a feed can be decoded straight into objects, or written from
 * them, with no annotations, no generated code and no reflection.
 *
 * Subclass it as an `object`, declare the fields with [field], [optionalField], [listField] and [childField], and build
 * the object in [create]. The fields are also the selection: the lexer reads only the properties of the schema, and no
 * tree is built. Decode with `ICal.parser().decode(bytes, schema)` or `decodeWithProblems`; write with
 * `ICal.writer().encode(items, schema, ...)`. A component that lacks a required field is skipped with `ICAL-M001`.
 *
 * @param component the name of the component the schema describes, such as `VEVENT`.
 * @sample io.github.calendrum.samples.decodeIntoClasses
 * @sample io.github.calendrum.samples.encodeFromClasses
 */
@SubclassOptInRequired(ExperimentalICalApi::class)
public abstract class IcsSchema<T : Any>(component: String) {
    internal val component: String = checkedName(component, "component name")

    internal val componentId: Int = KnownNames.find(this.component)

    private val fieldList = ArrayList<Field<*>>()

    internal val fields: List<Field<*>> get() = fieldList

    @Volatile
    private var plan: SchemaPlan? = null

    internal val compiled: SchemaPlan get() = plan ?: SchemaPlan(this).also { plan = it }

    /**
     * Builds the object from the decoded [values]; read each declared field with `values[field]`.
     *
     * @param values the decoded values of one component.
     */
    public abstract fun create(values: Values): T

    /**
     * Declares a required field that holds the value of the property [key] as it is decoded; [get] reads it back when
     * writing.
     *
     * @param key the property.
     * @param get reads the value from an object when writing.
     */
    protected fun <V : Any> field(key: PropertyKey<V>, get: (T) -> V): Field<V> =
        add(key, REQUIRED, null, null, null, get)

    /**
     * Declares a required field whose value is converted from the decoded property value by [decode].
     *
     * @param key the property.
     * @param get reads the value from an object when writing; it returns the property value.
     * @param decode converts the decoded value into the type of the field.
     */
    protected fun <V : Any, R : Any> field(key: PropertyKey<V>, get: (T) -> V, decode: (V) -> R): Field<R> =
        add(key, REQUIRED, null, decode, null, get)

    /**
     * Declares an optional field: `null` when the property is missing or does not decode.
     *
     * @param key the property.
     * @param get reads the value from an object when writing; `null` writes nothing.
     */
    protected fun <V : Any> optionalField(key: PropertyKey<V>, get: (T) -> V?): Field<V?> =
        add(key, OPTIONAL, null, null, null, get)

    /**
     * Declares an optional field whose value is converted from the decoded property value by [decode].
     *
     * @param key the property.
     * @param get reads the value from an object when writing; `null` writes nothing.
     * @param decode converts the decoded value into the type of the field.
     */
    protected fun <V : Any, R : Any> optionalField(key: PropertyKey<V>, get: (T) -> V?, decode: (V) -> R): Field<R?> =
        add(key, OPTIONAL, null, decode, null, get)

    /**
     * Declares a field that collects every property [key] into a list; empty when there is none.
     *
     * @param key the property.
     * @param get reads the list from an object when writing; each element becomes a property.
     */
    protected fun <V : Any> listField(key: PropertyKey<V>, get: (T) -> List<V>): Field<List<V>> =
        add(key, LIST, null, null, null, get)

    /**
     * Declares a list field whose elements are converted: [decode] when reading and [encode] when writing.
     *
     * @param key the property.
     * @param get reads the list from an object when writing.
     * @param decode converts each decoded property value into an element.
     * @param encode converts each element back into a property value.
     */
    protected fun <V : Any, R> listField(
        key: PropertyKey<V>,
        get: (T) -> List<R>,
        decode: (V) -> R,
        encode: (R) -> V,
    ): Field<List<R>> = add(key, LIST, null, decode, encode, get)

    /**
     * Declares a field that collects the child components of [schema], such as the alarms of an event.
     *
     * @param schema the schema of the child component.
     * @param get reads the children from an object when writing.
     * @throws IllegalArgumentException if [schema] is this schema.
     */
    protected fun <C : Any> childField(schema: IcsSchema<C>, get: (T) -> List<C>): Field<List<C>> {
        require(schema !== this) { "A schema is not its own child" }
        return add(null, CHILD, schema, null, null, get)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> add(
        key: PropertyKey<*>?,
        kind: Int,
        child: IcsSchema<*>?,
        decode: ((Nothing) -> Any?)?,
        encode: ((Nothing) -> Any)?,
        get: (T) -> Any?,
    ): Field<R> {
        check(plan == null) { "Fields are declared before the schema is used" }
        val field = Field<R>(
            this,
            fieldList.size,
            key,
            kind,
            child,
            decode as ((Any) -> Any?)?,
            encode as ((Any?) -> Any)?,
            get as (Any) -> Any?,
        )
        fieldList += field
        return field
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("IcsSchema", "component", component, "fields", fieldList.size)

    /** A declared field of a schema, to read its value from [Values] in [IcsSchema.create]. */
    public class Field<R> internal constructor(
        internal val schema: IcsSchema<*>,
        internal val index: Int,
        internal val key: PropertyKey<*>?,
        internal val kind: Int,
        internal val child: IcsSchema<*>?,
        internal val decode: ((Any) -> Any?)?,
        internal val encode: ((Any?) -> Any)?,
        internal val get: (Any) -> Any?,
    ) {
        internal val name: String get() = key?.name ?: child!!.component

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("IcsSchema.Field", "name", name)
    }

    // One `Values` and one slot array per schema and depth, reused between components: do not keep `values` after
    // `create` returns.

    /** The decoded values of one component, handed to [IcsSchema.create]. */
    public class Values internal constructor(private val schema: IcsSchema<*>, internal val slots: Array<Any?>) {
        /**
         * Returns the value of [field]: a malformed or missing optional value as `null`, a list field as a list (empty
         * if there is no property).
         *
         * @param field a field of the schema that decodes these values.
         * @throws IllegalArgumentException if [field] belongs to another schema.
         */
        @Suppress("UNCHECKED_CAST")
        public operator fun <R> get(field: Field<R>): R {
            require(field.schema === schema) { "The field belongs to another schema" }
            val slot = slots[field.index]
            return when (field.kind) {
                LIST, CHILD -> if (slot ==
                    null
                ) {
                    emptyList<Any>() as R
                } else {
                    ArrayView((slot as ArrayList<*>).toTypedArray()) as R
                }

                else -> (if (slot === MALFORMED) null else slot) as R
            }
        }

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("IcsSchema.Values", "fields", slots.size)
    }

    internal companion object {
        const val REQUIRED = 0
        const val OPTIONAL = 1
        const val LIST = 2
        const val CHILD = 3

        val MALFORMED: Any = Any()
    }
}

/**
 * The result of decoding into a schema.
 *
 * @property items the objects decoded, in input order.
 * @property problems the problems found, with values never shown.
 * @property skipped the number of components left out because a required field was missing or broken.
 */
public class SchemaResult<T : Any> internal constructor(
    public val items: List<T>,
    public val problems: List<Problem>,
    public val skipped: Int,
) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String =
        describe("SchemaResult", "items", items.size, "problems", problems.size, "skipped", skipped)
}

internal class SchemaPlan(schema: IcsSchema<*>) {
    val selection: Selection

    private val knownFields: Array<IntArray?> = arrayOfNulls(KnownNames.count)

    private val otherFields = HashMap<String, IntArray>()

    private val children = HashMap<String, IcsSchema<*>>()

    private val childIndex = HashMap<String, Int>()

    init {
        for (field in schema.fields) {
            val key = field.key
            if (key == null) {
                val child = field.child!!
                children[child.component] = child
                childIndex[child.component] = field.index
                continue
            }
            val id = key.nameId
            if (id >= 0) {
                knownFields[id] = (knownFields[id] ?: IntArray(0)) + field.index
            } else {
                otherFields[key.name] = (otherFields[key.name] ?: IntArray(0)) + field.index
            }
        }
        selection = selectionOf(schema)
    }

    fun fields(id: Int, name: String): IntArray? = if (id >= 0) knownFields[id] else otherFields[name]

    fun child(name: String): IcsSchema<*>? = children[name]

    fun childField(name: String): Int = childIndex[name]!!
}

private fun selectionOf(schema: IcsSchema<*>): Selection {
    val builder = Selection.builder().timeZones(TimeZoneSelection.None)
    var zones = false
    collect(schema, builder) { zones = true }
    if (zones) builder.timeZones(TimeZoneSelection.All)
    return builder.build()
}

private fun collect(schema: IcsSchema<*>, builder: Selection.Builder, zones: () -> Unit) {
    when (schema.component) {
        "VTIMEZONE", "STANDARD", "DAYLIGHT" -> zones()
        else -> builder.components(schema.component)
    }
    for (field in schema.fields) {
        val key = field.key
        if (key != null) builder.properties(key) else collect(field.child!!, builder, zones)
    }
}
