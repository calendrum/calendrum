package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.newComponent
import io.github.calendrum.value.ICalDateTime
import kotlin.jvm.JvmStatic
import kotlin.time.Clock

/** Makes the `UID` values of new components. There is no global generator: you pass one in [BuildDefaults]. */
public fun interface UidGenerator {
    /** Returns a new `UID`; it must be globally unique. */
    public fun next(): String

    /** Ready-made generators. */
    public companion object {
        /**
         * Returns a generator of reproducible, name-based UUID version 5 `UID`s: the same [namespace] and natural key
         * always give the same `UID`, which keeps a regenerated feed stable.
         *
         * @param namespace a UUID text, or any other text, which is mapped to a UUID in the DNS namespace.
         * @param naturalKey gives the key that identifies the item; called once per `UID`.
         */
        @JvmStatic
        public fun v5(namespace: String, naturalKey: () -> String): UidGenerator =
            UidGenerator { Canonical.uidV5(namespace, naturalKey()) }
    }
}

/**
 * What a builder uses when a required property is missing: a clock for `DTSTAMP` and a generator for `UID`.
 *
 * The library never reads the system clock or a global generator by itself.
 *
 * @property clock gives the time for `DTSTAMP`.
 * @property uids makes the `UID`s.
 */
public class BuildDefaults(public val clock: Clock, public val uids: UidGenerator) {
    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "BuildDefaults"
}

// A mutable list of property nodes and children over the original node. Unchanged entries stay the same objects;
// `build()` returns the original node when nothing changed. `positions` follows the node's `childPositions` (properties
// written before each child): appending a property moves the children that came right after the last property behind
// it, removing one moves later children up.

/**
 * The common part of the builders of all components: reading and writing properties by key or by name.
 *
 * The typed builders ([VEvent.Builder] and the others) add the accessors of their component. A builder starts empty or
 * from an existing component, shares the nodes it does not touch with it, and is not thread-safe.
 */
@ICalDsl
public abstract class ComponentBuilder<B : ComponentBuilder<B>> internal constructor(
    private val componentName: String,
    private val original: ICalComponent?,
    internal val defaults: BuildDefaults?,
) {
    private val properties: ArrayList<ICalProperty> =
        if (original == null) ArrayList() else ArrayList(original.propertyNodes.asList())

    private val children: ArrayList<ICalComponent> =
        if (original == null) ArrayList() else ArrayList(original.componentNodes.asList())

    private var positions: IntArray? = original?.childPositions?.copyOf()

    private var changed = false

    private var scheduleChanged = false

    @Suppress("UNCHECKED_CAST")
    private val self: B get() = this as B

    /** Returns the value of the first property of [key], or `null`. */
    public operator fun <T : Any> get(key: PropertyKey<T>): T? = read(key)

    /**
     * Sets the property of [key] to [value], replacing the first one in place and dropping later ones; `null` removes
     * the property.
     *
     * @return this builder.
     */
    public fun <T : Any> set(key: PropertyKey<T>, value: T?): B {
        write(key, value)
        return self
    }

    /**
     * Adds a property of [key] with [value] after the existing ones.
     *
     * @return this builder.
     */
    public fun <T : Any> property(key: PropertyKey<T>, value: T): B {
        append(key.codec.encode(value, EncodeContext(key.name)))
        return self
    }

    /**
     * Adds a property with the given [name] and [value] as text.
     *
     * @return this builder.
     * @throws IllegalArgumentException if the name or the value is invalid.
     */
    public fun property(name: String, value: String): B {
        append(ICalProperty.of(name, value))
        return self
    }

    /**
     * Adds [property] as it is.
     *
     * @return this builder.
     */
    public fun property(property: ICalProperty): B {
        append(property)
        return self
    }

    /**
     * Removes every property of [key].
     *
     * @return this builder.
     */
    public fun remove(key: PropertyKey<*>): B {
        removeAll(key.nameId, key.name)
        return self
    }

    /**
     * Removes every property named [name].
     *
     * @return this builder.
     */
    public fun remove(name: String): B {
        val query = canonicalName(name)
        removeAll(KnownNames.find(query), query)
        return self
    }

    internal fun <T : Any> read(key: PropertyKey<T>): T? {
        for (property in properties) {
            if (property.hasName(key.nameId, key.name)) {
                return property.decodeWith(key.codec, DecodeContext.Lenient).valueOrNull()
            }
        }
        return null
    }

    internal fun <T : Any> readAll(key: PropertyKey<T>): List<T> {
        var result: ArrayList<T>? = null
        for (property in properties) {
            if (!property.hasName(key.nameId, key.name)) continue
            val decoded = property.decodeWith(key.codec, DecodeContext.Lenient)
            if (decoded is Decoded.Value) (result ?: ArrayList<T>().also { result = it }).add(decoded.value)
        }
        return result ?: emptyList()
    }

    internal fun <T : Any> write(key: PropertyKey<T>, value: T?) {
        if (key.name == RRULE || key.name == DTSTART) scheduleChanged = true
        if (value == null) {
            removeAll(key.nameId, key.name)
            return
        }
        val encoded = key.codec.encode(value, EncodeContext(key.name))
        val index = indexOf(key.nameId, key.name)
        if (index < 0) {
            append(encoded)
            return
        }
        properties[index] = encoded
        changed = true
        var other = properties.size - 1
        while (other > index) {
            if (properties[other].hasName(key.nameId, key.name)) removeAt(other)
            other -= 1
        }
    }

    internal fun <T : Any> add(key: PropertyKey<T>, value: T) {
        append(key.codec.encode(value, EncodeContext(key.name)))
    }

    internal fun propertyNodes(): List<ICalProperty> = properties

    internal fun replaceAt(index: Int, property: ICalProperty) {
        if (properties[index] === property) return
        properties[index] = property
        changed = true
    }

    internal fun append(property: ICalProperty) {
        val count = properties.size
        properties.add(property)
        val positions = positions
        if (positions != null) {
            for (index in positions.indices) {
                if (positions[index] == count) positions[index] = count + 1
            }
        }
        changed = true
    }

    internal fun removeAt(index: Int) {
        properties.removeAt(index)
        val positions = positions
        if (positions != null) {
            for (child in positions.indices) {
                if (positions[child] > index) positions[child] -= 1
            }
        }
        changed = true
    }

    private fun removeAll(id: Int, name: String) {
        var index = properties.size - 1
        while (index >= 0) {
            if (properties[index].hasName(id, name)) removeAt(index)
            index -= 1
        }
    }

    private fun indexOf(id: Int, name: String): Int {
        for (index in properties.indices) {
            if (properties[index].hasName(id, name)) return index
        }
        return -1
    }

    internal fun childNodes(): List<ICalComponent> = children

    internal fun addChild(child: ICalComponent) {
        children.add(child)
        val positions = positions
        if (positions != null) {
            val grown = positions.copyOf(positions.size + 1)
            grown[positions.size] = properties.size
            this.positions = grown
        }
        changed = true
    }

    internal fun replaceChildAt(index: Int, child: ICalComponent) {
        if (children[index] === child) return
        children[index] = child
        changed = true
    }

    // Keeps the children that [keep] accepts and appends [added] after all properties, in one pass: removing and adding
    // them one by one copies the positions of an interleaved component every time, which was quadratic in the number
    // of children. The result is the same.
    internal fun replaceChildren(keep: (ICalComponent) -> Boolean, added: List<ICalComponent>) {
        val positions = positions
        val kept = ArrayList<ICalComponent>(children.size + added.size)
        val keptPositions = if (positions == null) null else IntArray(children.size + added.size)
        for (index in children.indices) {
            val child = children[index]
            if (!keep(child)) continue
            if (keptPositions != null) keptPositions[kept.size] = positions!![index]
            kept += child
        }
        if (kept.size == children.size && added.isEmpty()) return
        for (child in added) {
            if (keptPositions != null) keptPositions[kept.size] = properties.size
            kept += child
        }
        children.clear()
        children.addAll(kept)
        this.positions = keptPositions?.copyOf(kept.size)
        changed = true
    }

    internal fun removeChildAt(index: Int) {
        children.removeAt(index)
        val positions = positions
        if (positions != null) {
            val shrunk = IntArray(positions.size - 1)
            positions.copyInto(shrunk, 0, 0, index)
            positions.copyInto(shrunk, index, index + 1, positions.size)
            this.positions = shrunk
        }
        changed = true
    }

    // Only a component built from scratch must have a UID and a DTSTAMP; editing a parsed component does not validate
    // it (validation is a separate step).
    internal fun requireIdentity() {
        if (original != null) return
        if (read(Properties.UID) == null) {
            val uids = defaults?.uids ?: throw IllegalStateException("$componentName needs a UID or BuildDefaults")
            write(Properties.UID, uids.next())
        }
        if (read(Properties.DTSTAMP) == null) {
            val clock =
                defaults?.clock ?: throw IllegalStateException("$componentName needs a DTSTAMP or BuildDefaults")
            write(Properties.DTSTAMP, ICalDateTime.Utc(clock.now()))
        }
    }

    internal fun requireUid() {
        if (original != null || read(Properties.UID) != null) return
        val uids = defaults?.uids ?: throw IllegalStateException("$componentName needs a UID or BuildDefaults")
        write(Properties.UID, uids.next())
    }

    internal fun requireParticipantType() {
        if (original != null || read(Properties.PARTICIPANT_TYPE) != null) return
        throw IllegalStateException("$componentName needs a PARTICIPANT-TYPE")
    }

    // RFC 5545 3.3.10: UNTIL has the value type of DTSTART - a date for a date, a local date-time for a floating start,
    // UTC for a UTC or zoned start. Checked for new components and when an edit touched RRULE or DTSTART.
    internal fun checkUntil() {
        if (original != null && !scheduleChanged) return
        val until = read(Properties.RRULE)?.until ?: return
        val start = read(Properties.DTSTART) ?: return
        val matches = when (start) {
            is ICalDateTime.Date -> until is ICalDateTime.Date
            is ICalDateTime.Floating -> until is ICalDateTime.Floating
            else -> until is ICalDateTime.Utc
        }
        check(matches) { "RRULE UNTIL must have the value type of DTSTART in $componentName" }
    }

    internal fun buildNode(): ICalComponent {
        val original = original
        if (original != null && !changed) return original
        val propertyArray = properties.toTypedArray()
        val childArray = children.toTypedArray()
        return original?.edited(propertyArray, childArray, positions)
            ?: newComponent(componentName, propertyArray, childArray)
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = componentName + ".Builder"
}

/**
 * An editable view of the nested components of one kind in a builder, such as the alarms of an event.
 *
 * @property items the components now in the list.
 */
public class ComponentList<T : Any> internal constructor(
    private val owner: ComponentBuilder<*>,
    private val firstName: String,
    private val secondName: String?,
    private val wrap: (ICalComponent) -> T,
    private val unwrap: (T) -> ICalComponent,
) {
    public val items: List<T>
        get() {
            val result = ArrayList<T>()
            for (child in owner.childNodes()) {
                if (matches(child)) result.add(wrap(child))
            }
            return result
        }

    /**
     * Adds [item] after the existing components.
     *
     * @throws IllegalArgumentException if [item] is not of the kind this list holds.
     */
    public fun add(item: T) {
        owner.addChild(checked(unwrap(item)))
    }

    /**
     * Removes [item], found by identity or else by equality.
     *
     * @return `true` when it was found.
     */
    public fun remove(item: T): Boolean {
        val index = indexOf(unwrap(item))
        if (index < 0) return false
        owner.removeChildAt(index)
        return true
    }

    /**
     * Replaces [old] with [new] in place, found by identity or else by equality.
     *
     * @return `true` when [old] was found.
     * @throws IllegalArgumentException if [new] is not of the kind this list holds.
     */
    public fun replace(old: T, new: T): Boolean {
        val index = indexOf(unwrap(old))
        if (index < 0) return false
        owner.replaceChildAt(index, checked(unwrap(new)))
        return true
    }

    private fun indexOf(component: ICalComponent): Int {
        val nodes = owner.childNodes()
        for (index in nodes.indices) {
            if (nodes[index] === component) return index
        }
        for (index in nodes.indices) {
            if (matches(nodes[index]) && nodes[index] == component) return index
        }
        return -1
    }

    private fun matches(child: ICalComponent): Boolean = child.name == firstName || child.name == secondName

    private fun checked(component: ICalComponent): ICalComponent {
        require(matches(component)) { "Expected a $firstName component" }
        return component
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "ComponentList($firstName)"
}

/**
 * An editable view of the `ATTENDEE` properties of a builder, addressed by calendar address (compared ignoring ASCII
 * case).
 *
 * @property items the attendees now in the list.
 */
public class AttendeeList internal constructor(private val owner: ComponentBuilder<*>) {
    public val items: List<Attendee> get() = owner.readAll(Properties.ATTENDEE)

    /** Adds [attendee] after the existing ones. */
    public fun add(attendee: Attendee) {
        owner.add(Properties.ATTENDEE, attendee)
    }

    /**
     * Adds an attendee with the address [address], set up by [block].
     *
     * @param address the calendar address of the new attendee.
     * @param block sets up the attendee.
     */
    public fun add(address: String, block: Attendee.Builder.() -> Unit) {
        add(Attendee.builder(address).apply(block).build())
    }

    /**
     * Changes the attendee with the address [address] by [block]; parameters it does not touch stay. Returns `false`
     * when there is no such attendee.
     *
     * @param address the calendar address to look for, compared ignoring ASCII case.
     * @param block changes the attendee.
     */
    public fun replace(address: String, block: Attendee.Builder.() -> Unit): Boolean {
        val nodes = owner.propertyNodes()
        for (index in nodes.indices) {
            val property = nodes[index]
            if (property.name != ATTENDEE || !sameAddress(property.value, address)) continue
            val builder = Attendee.Builder(property.value, property.parameters)
            builder.block()
            val replacement = builder.toProperty()
            owner.replaceAt(index, if (replacement == property) property else replacement)
            return true
        }
        return false
    }

    /**
     * Removes every attendee with the address [address]. Returns `true` when there was one.
     *
     * @param address the calendar address, compared ignoring ASCII case.
     */
    public fun remove(address: String): Boolean {
        val nodes = owner.propertyNodes()
        var removed = false
        var index = nodes.size - 1
        while (index >= 0) {
            val property = nodes[index]
            if (property.name == ATTENDEE && sameAddress(property.value, address)) {
                owner.removeAt(index)
                removed = true
            }
            index -= 1
        }
        return removed
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "AttendeeList"
}

internal fun sameAddress(left: String, right: String): Boolean {
    if (left.length != right.length) return false
    for (index in left.indices) {
        val a = left[index]
        val b = right[index]
        if (a == b) continue
        if (a.code >= ASCII_LIMIT || b.code >= ASCII_LIMIT) return false
        if (asciiUpper(a) != asciiUpper(b)) return false
    }
    return true
}

private fun asciiUpper(char: Char): Char = if (char in 'a'..'z') char - CASE_SHIFT else char

internal fun <T> frozenCopy(values: List<T>): List<T> {
    if (values.isEmpty()) return emptyList()
    val items = arrayOfNulls<Any?>(values.size)
    for (index in values.indices) items[index] = values[index]
    @Suppress("UNCHECKED_CAST")
    return ArrayView(items) as List<T>
}

private const val ATTENDEE = "ATTENDEE"
private const val RRULE = "RRULE"
private const val DTSTART = "DTSTART"
private const val ASCII_LIMIT = 0x80
private const val CASE_SHIFT = 32
