package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ICal
import io.github.calendrum.ICalDsl
import io.github.calendrum.Problem
import io.github.calendrum.ProblemListener
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.InputCharset
import io.github.calendrum.syntax.canonicalLine
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.decodeInput
import io.github.calendrum.syntax.describe

/**
 * Changes a few properties of the components of an `.ics` file and leaves every other byte as it was.
 *
 * A patch finds each component by its `UID` and `RECURRENCE-ID` through fingerprints, so the rest of the file is never
 * parsed. Only the lines that change are rewritten (canonically); the others, including the line ends, stay byte for
 * byte. That keeps the checksum-like identity a CalDAV server derives from the file stable for everything you did not
 * touch. The patch itself is a description of operations; nothing happens until [apply].
 *
 * @param original the bytes of the file to patch.
 * @param block lists the components to change.
 * @sample io.github.calendrum.samples.patchFile
 */
@ExperimentalICalApi
public class ICalPatch(private val original: ByteArray, block: Builder.() -> Unit) {
    private val targets: List<ComponentPatch> = Builder().apply(block).targets.toList()

    /**
     * Runs the patch and returns the new bytes, the selectors that found nothing and the lines that changed.
     *
     * When there is nothing to change the result holds the original array. The original is never modified.
     */
    public fun apply(): PatchResult {
        val fingerprints = ICal.fingerprints(original)
        val source: ByteArray
        val from: Int
        val to: Int
        if (fingerprints.charset == InputCharset.Utf8) {
            source = original
            from = 0
            to = original.size
        } else {
            val decoded = decodeInput(original, null, IgnoredProblems)
            source = decoded.bytes
            from = decoded.offset
            to = decoded.offset + decoded.length
        }
        val edits = ArrayList<LineEdit>()
        val changes = ArrayList<PatchChange>()
        val unmatched = ArrayList<PatchSelector>()
        val parser = ICal.parser()
        val done = BooleanArray(targets.size)
        for (index in targets.indices) {
            if (done[index]) continue
            val target = targets[index]
            val group = ArrayList<ComponentPatch>()
            for (other in index until targets.size) {
                if (!done[other] && targets[other].uid == target.uid &&
                    targets[other].recurrenceId == target.recurrenceId
                ) {
                    group.add(targets[other])
                    done[other] = true
                }
            }
            var found = false
            for (entry in fingerprints.entries) {
                val key = entry.key
                if (key.uid != target.uid || key.recurrenceId != target.recurrenceId) continue
                found = true
                val component = parser.parseComponent(original, entry)
                ComponentEditor(component, group, source, to, edits, changes, unmatched).run()
            }
            if (!found) {
                for (patch in group) unmatched.add(PatchSelector(patch.uid, patch.recurrenceId, null, null))
            }
        }
        changes.sortBy { it.offset }
        val changeList = if (changes.isEmpty()) emptyList() else ArrayView(changes.toTypedArray())
        val unmatchedList = if (unmatched.isEmpty()) emptyList() else ArrayView(unmatched.toTypedArray())
        if (edits.isEmpty()) return PatchResult(original, unmatchedList, changeList)
        return PatchResult(assemble(source, from, to, edits), unmatchedList, changeList)
    }

    /** Returns the number of components to change. */
    override fun toString(): String = describe("ICalPatch", "components", targets.size)

    /** Collects the components to change. Not thread-safe. */
    @ICalDsl
    public class Builder internal constructor() {
        internal val targets = ArrayList<ComponentPatch>()

        /**
         * Adds a component to change, found by its [uid] and [recurrenceId], with the operations that [block] lists.
         *
         * @param uid the `UID` of the component.
         * @param recurrenceId the `RECURRENCE-ID` text of an override, or `null` for the master or a single component.
         * @param block lists the changes.
         */
        public fun component(uid: String, recurrenceId: String?, block: ComponentPatch.() -> Unit): Builder {
            targets.add(ComponentPatch(uid, recurrenceId).apply(block))
            return this
        }

        /** Returns the number of components to change. */
        override fun toString(): String = describe("ICalPatch.Builder", "components", targets.size)
    }
}

/**
 * The changes to one component of an [ICalPatch].
 *
 * Setting replaces the first property of that name (and drops later ones) or adds the property after the last one;
 * removing drops all properties of that name.
 *
 * @property uid the `UID` of the component.
 * @property recurrenceId the `RECURRENCE-ID` of the component, or `null`.
 */
@ICalDsl
public class ComponentPatch internal constructor(public val uid: String, public val recurrenceId: String?) {
    internal val operations = ArrayList<PatchOperation>()

    /** Sets the property of [key] to [value]. */
    public fun <T : Any> set(key: PropertyKey<T>, value: T): ComponentPatch =
        add(PatchOperation(SET, key.name, key.codec.encode(value, EncodeContext(key.name)), null, null))

    /**
     * Sets the property [name] to the text [value], written as is.
     *
     * @throws IllegalArgumentException if [name] or [value] is invalid.
     */
    public fun set(name: String, value: String): ComponentPatch {
        val property = ICalProperty.of(name, value)
        return add(PatchOperation(SET, property.name, property, null, null))
    }

    /** Removes every property of [key]. */
    public fun remove(key: PropertyKey<*>): ComponentPatch = add(PatchOperation(REMOVE, key.name, null, null, null))

    /** Removes every property named [name]. */
    public fun remove(name: String): ComponentPatch = add(PatchOperation(REMOVE, canonicalName(name), null, null, null))

    /**
     * Changes the parameters of the `ATTENDEE` with the address [address], as [block] says. Nothing happens, and the
     * selector is reported as unmatched, when there is no such attendee.
     */
    public fun attendee(address: String, block: PropertyPatch.() -> Unit): ComponentPatch =
        add(PatchOperation(EDIT, ATTENDEE, null, address, PropertyPatch().apply(block)))

    /** Changes the parameters of the first property of [key], as [block] says. */
    public fun property(key: PropertyKey<*>, block: PropertyPatch.() -> Unit): ComponentPatch =
        add(PatchOperation(EDIT, key.name, null, null, PropertyPatch().apply(block)))

    /** Changes the parameters of the first property named [name], as [block] says. */
    public fun property(name: String, block: PropertyPatch.() -> Unit): ComponentPatch =
        add(PatchOperation(EDIT, canonicalName(name), null, null, PropertyPatch().apply(block)))

    private fun add(operation: PatchOperation): ComponentPatch {
        operations.add(operation)
        return this
    }

    /** Returns the number of operations. */
    override fun toString(): String = describe("ComponentPatch", "operations", operations.size)
}

/** The changes to the parameters of one property. */
@ICalDsl
public class PropertyPatch internal constructor() {
    internal val names = ArrayList<String>()
    internal val values = ArrayList<List<String>>()

    /** Sets the parameter of [key] to [value], replacing the parameter of that name. */
    public fun <T : Any> set(key: ParameterKey<T>, value: T): PropertyPatch {
        val parameter = key.codec.encode(value, EncodeContext(key.name))
        return put(parameter.name, parameter.values)
    }

    /**
     * Sets the parameter [name] to [values], replacing the parameter of that name.
     *
     * @throws IllegalArgumentException if no value is given or the name is invalid.
     */
    public fun set(name: String, vararg values: String): PropertyPatch {
        require(values.isNotEmpty()) { "A parameter needs a value" }
        return put(ICalParameter.of(name, *values).name, values.asList())
    }

    /** Removes the parameter of [key]. */
    public fun remove(key: ParameterKey<*>): PropertyPatch = put(key.name, emptyList())

    /** Removes the parameter named [name]. */
    public fun remove(name: String): PropertyPatch = put(canonicalName(name), emptyList())

    private fun put(name: String, value: List<String>): PropertyPatch {
        names.add(name)
        values.add(value)
        return this
    }

    internal fun applyTo(property: ICalProperty): ICalProperty {
        val parameters = ArrayList(property.parameters)
        for (index in names.indices) parameters.putAll(names[index], values[index])
        if (parameters == property.parameters) return property
        val builder = ICalProperty.builder(property.name)
        for (parameter in parameters) builder.parameter(parameter)
        return builder.value(property.value).build()
    }

    /** Returns the number of parameter changes. */
    override fun toString(): String = describe("PropertyPatch", "parameters", names.size)
}

/**
 * The outcome of applying an [ICalPatch].
 *
 * @property bytes the patched file; the original array when nothing changed.
 * @property unmatched the selectors that found no component, property or attendee.
 * @property changes the lines that changed, in file order; they never contain values.
 */
public class PatchResult internal constructor(
    public val bytes: ByteArray,
    public val unmatched: List<PatchSelector>,
    public val changes: List<PatchChange>,
) {
    /** Returns the size of the result and the numbers of unmatched selectors and changes. */
    override fun toString(): String =
        describe("PatchResult", "bytes", bytes.size, "unmatched", unmatched.size, "changes", changes.size)
}

/**
 * Tells which component, property or attendee a patch operation could not find.
 *
 * @property uid the `UID` of the component.
 * @property recurrenceId the `RECURRENCE-ID` of the component, or `null`.
 * @property property the property name, or `null` when the component itself was not found.
 * @property address the attendee address, or `null`.
 */
public class PatchSelector internal constructor(
    public val uid: String,
    public val recurrenceId: String?,
    public val property: String?,
    public val address: String?,
) {
    /** Returns `true` when [other] has the same uid, recurrence id, property and address. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is PatchSelector && uid == other.uid && recurrenceId == other.recurrenceId &&
                property == other.property && address == other.address
            )

    /** Returns a hash code over all fields. */
    override fun hashCode(): Int = ((uid.hashCode() * 31 + recurrenceId.hashCode()) * 31 + property.hashCode()) * 31 +
        address.hashCode()

    /** Returns the property name only, never a value. */
    override fun toString(): String = describe("PatchSelector", "property", property)
}

/**
 * One line that a patch changed.
 *
 * @property kind what happened to the line.
 * @property property the name of the property on the line; never its value.
 * @property offset the offset in bytes of the line in the original.
 * @property line the 1-based line number in the original, or in the new file for an inserted line.
 */
public class PatchChange internal constructor(
    public val kind: Kind,
    public val property: String,
    public val offset: Long,
    public val line: Int,
) {
    /** What a patch did to a line. */
    public enum class Kind {
        /** An existing line was rewritten. */
        REPLACED,

        /** A new line was added after the last property of the component. */
        INSERTED,

        /** A line was deleted. */
        REMOVED,
    }

    /** Returns `true` when [other] has the same kind, property, offset and line. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is PatchChange && kind == other.kind && property == other.property && offset == other.offset &&
                line == other.line
            )

    /** Returns a hash code over all fields. */
    override fun hashCode(): Int = ((kind.hashCode() * 31 + property.hashCode()) * 31 + offset.hashCode()) * 31 + line

    /** Returns the kind, the property name and the line; never a value. */
    override fun toString(): String = describe("PatchChange", "kind", kind, "property", property, "line", line)
}

internal class PatchOperation(
    val kind: Int,
    val name: String,
    val property: ICalProperty?,
    val address: String?,
    val parameters: PropertyPatch?,
)

private class LineEdit(val start: Int, val end: Int, val bytes: ByteArray)

// Applies the operations of the selectors of one component to its property lines with the rules of `ComponentBuilder`
// (set replaces the first and removes the rest, remove removes every one, new ones follow the last property), then
// turns the difference into byte edits: a removed line with its line ending, a replaced line without it, new lines at
// the end of the last property line.
private class ComponentEditor(
    private val component: ICalComponent,
    private val patches: List<ComponentPatch>,
    private val source: ByteArray,
    private val limit: Int,
    private val edits: ArrayList<LineEdit>,
    private val changes: ArrayList<PatchChange>,
    private val unmatched: ArrayList<PatchSelector>,
) {
    private val originals = component.propertyNodes
    private val current = arrayOfNulls<ICalProperty>(originals.size).also { originals.copyInto(it) }
    private val appended = ArrayList<ICalProperty?>()

    fun run() {
        for (patch in patches) {
            for (operation in patch.operations) apply(patch, operation)
        }
        for (index in originals.indices) {
            val property = current[index]
            val original = originals[index]
            if (property === original) continue
            val span = original.span!!
            val start = span.startOffset.toInt()
            val end = span.endOffset.toInt()
            if (property == null) {
                edits.add(LineEdit(start, end + terminatorLength(end), ByteArray(0)))
                changes.add(PatchChange(PatchChange.Kind.REMOVED, original.name, span.startOffset, span.line))
            } else {
                edits.add(LineEdit(start, end, lineBytes(property, styleAt(end), terminated = false)))
                changes.add(PatchChange(PatchChange.Kind.REPLACED, original.name, span.startOffset, span.line))
            }
        }
        val inserted = appended.filterNotNull()
        if (inserted.isEmpty()) return
        val last = originals.last().span!!.endOffset.toInt()
        val at = last + terminatorLength(last)
        val style = styleAt(last)
        val needsBreak = at == limit && at > 0 && source[at - 1] != LF && source[at - 1] != CR
        var bytes = if (needsBreak) style else ByteArray(0)
        for ((index, property) in inserted.withIndex()) {
            bytes += lineBytes(property, style, terminated = !needsBreak || index < inserted.size - 1)
        }
        edits.add(LineEdit(at, at, bytes))
        val line = lineOf(at)
        for ((index, property) in inserted.withIndex()) {
            changes.add(PatchChange(PatchChange.Kind.INSERTED, property.name, at.toLong(), line + index))
        }
    }

    private fun apply(patch: ComponentPatch, operation: PatchOperation) {
        when (operation.kind) {
            SET -> set(operation.name, operation.property!!)

            REMOVE -> {
                for (index in current.indices) if (current[index]?.name == operation.name) current[index] = null
                for (index in appended.indices) if (appended[index]?.name == operation.name) appended[index] = null
            }

            else -> edit(patch, operation)
        }
    }

    private fun set(name: String, property: ICalProperty) {
        var placed = false
        for (index in current.indices) {
            if (current[index]?.name != name) continue
            current[index] = if (placed) null else property
            placed = true
        }
        for (index in appended.indices) {
            if (appended[index]?.name != name) continue
            appended[index] = if (placed) null else property
            placed = true
        }
        if (!placed) appended.add(property)
    }

    private fun edit(patch: ComponentPatch, operation: PatchOperation) {
        val parameters = operation.parameters!!
        for (index in current.indices) {
            val property = current[index] ?: continue
            if (matches(property, operation)) {
                current[index] = parameters.applyTo(property)
                return
            }
        }
        for (index in appended.indices) {
            val property = appended[index] ?: continue
            if (matches(property, operation)) {
                appended[index] = parameters.applyTo(property)
                return
            }
        }
        val selector = PatchSelector(patch.uid, patch.recurrenceId, operation.name, operation.address)
        if (selector !in unmatched) unmatched.add(selector)
    }

    private fun matches(property: ICalProperty, operation: PatchOperation): Boolean {
        if (property.name != operation.name) return false
        val address = operation.address ?: return true
        return sameAddress(property.value, address)
    }

    private fun terminatorLength(at: Int): Int {
        if (at >= limit) return 0
        if (source[at] == CR) return if (at + 1 < limit && source[at + 1] == LF) 2 else 1
        return if (source[at] == LF) 1 else 0
    }

    private fun styleAt(end: Int): ByteArray = when (terminatorLength(end)) {
        1 -> byteArrayOf(source[end])
        else -> byteArrayOf(CR, LF)
    }

    private fun lineOf(offset: Int): Int {
        val span = component.span!!
        var line = span.line
        var index = span.startOffset.toInt()
        while (index < offset) {
            val length = terminatorLength(index)
            if (length > 0) {
                line += 1
                index += length
            } else {
                index += 1
            }
        }
        return line
    }
}

// The canonical line of the writer with its CRLF line breaks replaced by the line ending found in the input after the
// anchor line, so an LF or CR file stays consistent.
private fun lineBytes(property: ICalProperty, style: ByteArray, terminated: Boolean): ByteArray {
    val canonical = canonicalLine(property)
    val body = canonical.size - 2
    var breaks = 0
    for (index in 0 until body) if (canonical[index] == CR) breaks += 1
    val size = body + (breaks + (if (terminated) 1 else 0)) * (style.size - 2)
    val out = ByteArray(size + if (terminated) 2 else 0)
    var at = 0
    var index = 0
    while (index < body) {
        if (canonical[index] == CR) {
            style.copyInto(out, at)
            at += style.size
            index += 2
            continue
        }
        out[at++] = canonical[index++]
    }
    if (terminated) {
        style.copyInto(out, at)
        at += style.size
    }
    return if (at == out.size) out else out.copyOf(at)
}

private fun assemble(source: ByteArray, from: Int, to: Int, edits: ArrayList<LineEdit>): ByteArray {
    edits.sortBy { it.start }
    var size = to - from
    for (edit in edits) size += edit.bytes.size - (edit.end - edit.start)
    val out = ByteArray(size)
    var at = 0
    var position = from
    for (edit in edits) {
        source.copyInto(out, at, position, edit.start)
        at += edit.start - position
        edit.bytes.copyInto(out, at)
        at += edit.bytes.size
        position = edit.end
    }
    source.copyInto(out, at, position, to)
    return out
}

private object IgnoredProblems : ProblemListener {
    override fun onProblem(problem: Problem) = Unit
}

private const val SET = 0
private const val REMOVE = 1
private const val EDIT = 2
private const val ATTENDEE = "ATTENDEE"
private const val CR: Byte = 13
private const val LF: Byte = 10
