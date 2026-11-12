package io.github.calendrum.syntax

import io.github.calendrum.ICalDsl
import io.github.calendrum.SourceSpan
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// Parsed and hand-built components share one class: a parsed one decodes its name lazily from `nameSource`, a
// hand-built one has it from the start and no span (`line == 0`). A parsed one also keeps its BEGIN line (`beginStart`
// to `nameEnd` in `nameSource`), its END line (`endSource`, null if the END was implicit) and `childPositions` (see
// `TreeBuilder`).
/**
 * A component of an iCalendar tree, such as `VCALENDAR`, `VEVENT` or `VALARM`: a name, its properties and its nested
 * components, in the order of the source ([RFC 5545 §3.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.6)).
 *
 * The tree is lossless and immutable. A component read from input keeps byte ranges of the source and decodes its name
 * and its children's text on first access; with `ParseOptions.retainSource = false` the lines are copied instead of
 * holding the input. Components built by hand with [builder] or the `ICalComponent(name) { ... }` function have no
 * source and no [span]. To change a tree, build a new one: the typed model's `edit` functions share every unchanged
 * node.
 *
 * [equals] and [hashCode] compare content (name, properties in order, nested components) without recursion, so a very
 * deep tree cannot overflow the stack; the source position and the spelling in the input do not matter. [toString]
 * shows names and counts and never values. A component can be read from any thread.
 */
public class ICalComponent internal constructor(
    private val nameSource: ByteArray?,
    private val beginStart: Int,
    private val nameStart: Int,
    private val nameEnd: Int,
    internal val nameId: Int,
    private val textFlags: Int,
    private val endSource: ByteArray?,
    private val endStart: Int,
    private val endEnd: Int,
    internal val childPositions: IntArray?,
    private val propertyArray: Array<ICalProperty>,
    private val componentArray: Array<ICalComponent>,
    private val startOffset: Long,
    private val endOffset: Long,
    private val line: Int,
    private val column: Int,
    name: String?,
) {
    @Volatile
    private var nameCache: String? = name

    /**
     * The canonical name in upper case, such as `VEVENT`; the names the standard defines share one string instance.
     */
    public val name: String
        get() {
            if (nameId >= 0) return KnownNames.name(nameId)
            val cached = nameCache
            if (cached != null) return cached
            val decoded = canonicalName(nameSource!!, nameStart, nameEnd, textFlags)
            nameCache = decoded
            return decoded
        }

    /**
     * The properties of this component, in the order they appear. The list is a read-only view; it is empty for none.
     */
    public val properties: List<ICalProperty> get() = ArrayView(propertyArray)

    /**
     * The directly nested components, in the order they appear. The list is a read-only view; it is empty for none.
     */
    public val components: List<ICalComponent> get() = ArrayView(componentArray)

    /**
     * Where the component starts in the input and where its `END` line ends, or `null` for a component that was built
     * by hand or edited.
     */
    public val span: SourceSpan?
        get() = if (line == 0) null else SourceSpan(startOffset, endOffset, line, column)

    /**
     * Returns the first property named [name], ignoring ASCII case, or `null` when there is none.
     *
     * @param name a property name such as `SUMMARY`.
     */
    public fun property(name: String): ICalProperty? {
        val query = canonicalName(name)
        val queryId = KnownNames.find(query)
        for (property in propertyArray) {
            if (property.hasName(queryId, query)) return property
        }
        return null
    }

    /**
     * Returns all properties named [name], ignoring ASCII case, in source order; the list is empty when there are none.
     *
     * @param name a property name such as `ATTENDEE`.
     */
    public fun properties(name: String): List<ICalProperty> {
        val query = canonicalName(name)
        val queryId = KnownNames.find(query)
        var count = 0
        for (property in propertyArray) {
            if (property.hasName(queryId, query)) count++
        }
        if (count == 0) return ArrayView(EMPTY_PROPERTIES)
        if (count == propertyArray.size) return ArrayView(propertyArray)
        var index = 0
        val matches = arrayOfNulls<ICalProperty>(count)
        for (property in propertyArray) {
            if (property.hasName(queryId, query)) matches[index++] = property
        }
        @Suppress("UNCHECKED_CAST")
        return ArrayView(matches as Array<ICalProperty>)
    }

    /**
     * Returns the directly nested components named [name], ignoring ASCII case, in source order; the list is empty when
     * there are none. Deeper levels are not searched.
     *
     * @param name a component name such as `VEVENT`.
     */
    public fun components(name: String): List<ICalComponent> {
        val query = canonicalName(name)
        val queryId = KnownNames.find(query)
        var count = 0
        for (component in componentArray) {
            if (component.hasName(queryId, query)) count++
        }
        if (count == 0) return ArrayView(EMPTY_COMPONENTS)
        if (count == componentArray.size) return ArrayView(componentArray)
        var index = 0
        val matches = arrayOfNulls<ICalComponent>(count)
        for (component in componentArray) {
            if (component.hasName(queryId, query)) matches[index++] = component
        }
        @Suppress("UNCHECKED_CAST")
        return ArrayView(matches as Array<ICalComponent>)
    }

    internal val isParsed: Boolean get() = nameSource != null

    internal val propertyNodes: Array<ICalProperty> get() = propertyArray

    internal val componentNodes: Array<ICalComponent> get() = componentArray

    // Bytes the component took in the input, END line ending included: the size of its byte-stable output.
    internal val sourceLength: Long get() = if (line == 0) 0L else endOffset - startOffset

    // Copy of a parsed component with other properties and children, for the anonymizer. The child positions stay valid
    // because the number and order of properties do not change.
    internal fun withContent(properties: Array<ICalProperty>, components: Array<ICalComponent>): ICalComponent =
        ICalComponent(
            nameSource,
            beginStart,
            nameStart,
            nameEnd,
            nameId,
            textFlags,
            endSource,
            endStart,
            endEnd,
            childPositions,
            properties,
            components,
            startOffset,
            endOffset,
            line,
            column,
            nameCache,
        )

    // A copy of a parsed component with new properties and children that keeps the source bytes of its `BEGIN` and
    // `END` lines, so byte-stable writing still writes them as they were; the span is dropped, the content no longer
    // matches the input.
    internal fun edited(
        properties: Array<ICalProperty>,
        components: Array<ICalComponent>,
        positions: IntArray?,
    ): ICalComponent = ICalComponent(
        nameSource,
        beginStart,
        nameStart,
        nameEnd,
        nameId,
        textFlags,
        endSource,
        endStart,
        endEnd,
        positions,
        properties,
        components,
        0L,
        0L,
        0,
        0,
        nameCache,
    )

    internal val isPartial: Boolean get() = textFlags and TEXT_PARTIAL != 0

    internal fun withChildren(components: Array<ICalComponent>, positions: IntArray?): ICalComponent = ICalComponent(
        nameSource,
        beginStart,
        nameStart,
        nameEnd,
        nameId,
        textFlags,
        endSource,
        endStart,
        endEnd,
        positions,
        propertyArray,
        components,
        startOffset,
        endOffset,
        line,
        column,
        nameCache,
    )

    internal fun writeBegin(out: ContentLineWriter) {
        out.rawLine(nameSource!!, beginStart, nameEnd, textFlags and TEXT_FOLDED != 0)
    }

    // The source END line; for an implicit END a canonical one, or the raw name when the name is not a valid name.
    internal fun writeEnd(out: ContentLineWriter) {
        val endSource = endSource
        val name = name
        when {
            endSource != null -> out.rawLine(endSource, endStart, endEnd, textFlags and TEXT_END_FOLDED != 0)
            isName(name) -> out.end(name)
            else -> out.rawEnd(nameSource!!, nameStart, nameEnd, textFlags and TEXT_FOLDED != 0)
        }
    }

    private fun hasName(queryId: Int, query: String): Boolean =
        if (queryId >= 0) nameId == queryId else nameId < 0 && name == query

    // No recursion (the CVE of Python `icalendar`): pairs of nodes on an explicit stack; spans and the original
    // spelling are not compared.

    /**
     * Returns `true` when [other] is a component with the same name, properties and nested components, compared without
     * recursion; position and spelling in the source do not matter.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ICalComponent) return false
        var stack = arrayOfNulls<ICalComponent>(INITIAL_STACK)
        stack[0] = this
        stack[1] = other
        var top = 2
        while (top > 0) {
            val right = stack[--top]!!
            val left = stack[--top]!!
            if (left === right) continue
            if (!left.shallowEquals(right)) return false
            val children = left.componentArray
            val others = right.componentArray
            if (top + 2 * children.size >
                stack.size
            ) {
                stack = stack.copyOf(maxOf(stack.size * 2, top + 2 * children.size))
            }
            for (index in children.indices) {
                stack[top++] = children[index]
                stack[top++] = others[index]
            }
        }
        return true
    }

    // A pre-order hash over an explicit stack with a marker when a node is left, so the shape of the tree is part of
    // the hash; equal trees give equal hashes.

    /** Returns a hash code over the name, the properties and the nested components, computed without recursion. */
    override fun hashCode(): Int {
        var result = 1
        var stack = arrayOfNulls<ICalComponent>(INITIAL_STACK)
        stack[0] = this
        var top = 1
        while (top > 0) {
            val component = stack[--top]
            if (component == null) {
                result = 31 * result + EXIT_HASH
                continue
            }
            result = 31 * result + component.shallowHash()
            val children = component.componentArray
            if (top + children.size + 1 >
                stack.size
            ) {
                stack = stack.copyOf(maxOf(stack.size * 2, top + children.size + 1))
            }
            stack[top++] = null
            for (index in children.indices.reversed()) stack[top++] = children[index]
        }
        return result
    }

    private fun shallowEquals(other: ICalComponent): Boolean {
        val properties = propertyArray
        val others = other.propertyArray
        if (properties.size != others.size || componentArray.size != other.componentArray.size) return false
        if (nameId != other.nameId || (nameId < 0 && name != other.name)) return false
        for (index in properties.indices) {
            if (properties[index] != others[index]) return false
        }
        return true
    }

    private fun shallowHash(): Int {
        var result = name.hashCode()
        for (property in propertyArray) result = 31 * result + property.hashCode()
        return result
    }

    /** Returns the name and the number of properties and nested components; never a value. */
    override fun toString(): String =
        describe("ICalComponent", "name", name, "properties", propertyArray.size, "components", componentArray.size)

    /**
     * Collects the properties and nested components of a new [ICalComponent].
     *
     * A builder is not thread-safe. Obtain one with [ICalComponent.builder].
     */
    @ICalDsl
    public class Builder internal constructor(private val name: String) {
        private val properties = ArrayList<ICalProperty>()
        private val components = ArrayList<ICalComponent>()

        /** Appends [property] after the properties added so far. */
        public fun property(property: ICalProperty): Builder {
            properties += property
            return this
        }

        /**
         * Appends a property with the given [name] and [value] and no parameters.
         *
         * @throws IllegalArgumentException if [name] is not a valid property name, or [value] contains a control
         *   character other than a tab.
         */
        public fun property(name: String, value: String): Builder = property(ICalProperty.of(name, value))

        /** Appends [component] after the nested components added so far. */
        public fun component(component: ICalComponent): Builder {
            components += component
            return this
        }

        /**
         * Appends a nested component built by [block].
         *
         * @param name the name of the nested component.
         * @param block fills the builder of the nested component.
         * @throws IllegalArgumentException if [name] is not a valid component name.
         */
        public fun component(name: String, block: Builder.() -> Unit): Builder =
            component(builder(name).apply(block).build())

        /** Creates the component. The builder can be reused: later changes do not affect components already built. */
        public fun build(): ICalComponent {
            val propertyArray = if (properties.isEmpty()) EMPTY_PROPERTIES else properties.toTypedArray()
            val componentArray = if (components.isEmpty()) EMPTY_COMPONENTS else components.toTypedArray()
            return newComponent(name, propertyArray, componentArray)
        }
    }

    /** Creates builders for [ICalComponent]. */
    public companion object {
        /**
         * Returns a builder for a component named [name].
         *
         * @param name letters, digits and hyphens only; the stored name is upper case.
         * @throws IllegalArgumentException if [name] is empty or has another character.
         */
        @JvmStatic
        public fun builder(name: String): Builder = Builder(checkedName(name, "component name"))

        private const val INITIAL_STACK = 16
        private const val EXIT_HASH = 0x2F
    }
}

internal fun newComponent(
    name: String,
    properties: Array<ICalProperty>,
    components: Array<ICalComponent>,
): ICalComponent = ICalComponent(
    null,
    0,
    0,
    0,
    KnownNames.find(name),
    0,
    null,
    0,
    0,
    null,
    properties,
    components,
    0L,
    0L,
    0,
    0,
    name,
)

internal val EMPTY_PROPERTIES: Array<ICalProperty> = emptyArray()

internal val EMPTY_COMPONENTS: Array<ICalComponent> = emptyArray()
