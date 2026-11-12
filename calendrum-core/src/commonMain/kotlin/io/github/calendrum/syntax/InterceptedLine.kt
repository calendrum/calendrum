package io.github.calendrum.syntax

import io.github.calendrum.ExperimentalICalApi

// One reusable object per parse or write, like the cursor. On the read side it is bound once to the `ContentReader` and
// its reused `ContentLine` (`bindRead`) and only the state changes per line (`startRead`, one field write: the depth is
// read from the reader on request); on the write side it points at a node of the tree (`startWrite`). Everything is
// decoded on request; the first edit copies the name, the value and the parameters (`edit()`, which creates the
// parameter list), and from then on reads see the edited line. `state` packs the per-line flags (ACTIVE, EDITED,
// DROPPED, VIEW decoded, NAMED: the unknown name of this line is cached) into one field, so starting a line never
// clears anything else; ACTIVE is set only while the chain runs: a line kept by the caller fails instead of showing
// another line. The `CharsView` of `value`, its char buffer, the parameter list and the name cache are created on first
// use (a parse with an interceptor that only looks allocates two objects, the chain and the line), and the constants
// are file-level: an application that never reads a line carries none of it.
/**
 * The view of one property line that a [LineInterceptor] receives, with functions to change it.
 *
 * The same instance is reused for every line, which is why it is valid only inside [LineInterceptor.intercept]; any
 * access outside that call throws [IllegalStateException]. Reading is cheap and allocation-free; the first change
 * copies the name, the parameters and the value of the line, and later changes work on that copy.
 */
public class InterceptedLine internal constructor() {
    private var reader: ContentReader? = null
    private var source: ContentLine? = null
    private var node: ICalProperty? = null
    private var owner: ICalComponent? = null
    private var level = 0
    private var chars: CharArray? = null
    private var view: CharsView? = null
    private var unknownName: String? = null
    private var unknownNames: NameCache? = null
    private var parameters: ArrayList<ICalParameter>? = null

    private var state = 0

    internal val isEdited: Boolean get() = state and EDITED != 0

    internal val isDropped: Boolean get() = state and DROPPED != 0

    internal var editedName: String = ""
        private set

    internal var editedValue: String = ""
        private set

    internal val editedParameters: List<ICalParameter> get() = parameters ?: emptyList()

    /** The nesting depth of the component that owns the line: 1 for a top-level component. */
    public val depth: Int
        get() {
            checkActive()
            return reader?.depth ?: level
        }

    // On the read side the name comes from the structural stack of `ContentReader`, which keeps the first 255 bytes of
    // an unknown component name; longer names are cut here (the tree keeps the full name).

    /** The canonical name of the component that owns the line, such as `VEVENT`. */
    public val componentName: String
        get() {
            checkActive()
            val owner = owner
            if (owner != null) return owner.name
            val reader = reader!!
            val id = reader.componentId
            return if (id >= 0) KnownNames.name(id) else canonicalName(reader.componentName())
        }

    /** The canonical name of the property, as changed by [rename] if it was. */
    public val name: String
        get() {
            checkActive()
            if (isEdited) return editedName
            val node = node
            if (node != null) return node.name
            val line = source!!
            val id = line.nameId
            if (id >= 0) return KnownNames.name(id)
            if (state and NAMED != 0) return unknownName!!
            val cache = unknownNames ?: NameCache().also { unknownNames = it }
            val decoded = cache.name(line.bytes, line.nameStart, line.nameEnd, line.validUtf8)
            unknownName = decoded
            state = state or NAMED
            return decoded
        }

    /**
     * The value of the line, as changed by [setValue] if it was. It may be a view over a reused buffer, so copy it with
     * `toString()` to keep it.
     */
    public val value: CharSequence
        get() {
            checkActive()
            if (isEdited) return editedValue
            val node = node
            if (node != null) return node.value
            if (state and VIEW == 0) {
                val line = source!!
                val length = line.valueEnd - line.valueStart
                var buffer = chars
                if (buffer == null || buffer.size < length) {
                    buffer = CharArray(maxOf(length, (buffer?.size ?: 0) * 2, MIN_CHARS))
                    chars = buffer
                }
                val created = view ?: CharsView().also { view = it }
                created.reset(buffer, decodeUtf8Into(line.bytes, line.valueStart, line.valueEnd, buffer))
                state = state or VIEW
            }
            return view!!
        }

    /** The number of parameters of the line. */
    public val parameterCount: Int
        get() {
            checkActive()
            if (isEdited) return parameters!!.size
            val node = node
            return node?.parameterCount ?: source!!.parameterCount
        }

    /**
     * Returns the canonical name of the parameter at [index].
     *
     * @param index from 0 until [parameterCount].
     */
    public fun parameterName(index: Int): String {
        checkActive()
        if (isEdited) return parameters!![index].name
        val node = node
        if (node != null) return node.parameterNodes()[index].name
        val line = source!!
        val id = line.parameterNameId(index)
        return if (id >= 0) KnownNames.name(id) else canonicalName(line.parameterName(index))
    }

    /**
     * Returns the values of the parameter at [index], unquoted.
     *
     * @param index from 0 until [parameterCount].
     */
    public fun parameterValues(index: Int): List<String> {
        checkActive()
        if (isEdited) return parameters!![index].values
        val node = node
        if (node != null) return node.parameterNodes()[index].values
        val line = source!!
        val count = line.parameterValueCount(index)
        if (count == 0) return emptyList()
        return ArrayView(Array(count) { line.parameterValue(index, it) })
    }

    /**
     * Changes the name of the property.
     *
     * @throws IllegalArgumentException if [newName] is not a valid property name.
     */
    public fun rename(newName: String) {
        checkActive()
        val checked = checkedPropertyName(newName)
        edit()
        editedName = checked
    }

    /**
     * Replaces the value, which is written as is.
     *
     * @throws IllegalArgumentException if [newValue] has a control character other than a tab.
     */
    public fun setValue(newValue: String) {
        checkActive()
        val checked = checkedValue(newValue)
        edit()
        editedValue = checked
    }

    // Replaces the first parameter of that name and removes the others (a parameter name is meant to be unique), or
    // adds the parameter at the end.

    /**
     * Sets the parameter [name] to [values]: the first parameter of that name is replaced in place, later ones are
     * removed, and a new parameter goes last.
     *
     * @throws IllegalArgumentException if [name] is invalid or no value is given.
     */
    public fun setParameter(name: String, vararg values: String) {
        checkActive()
        val parameter = ICalParameter.of(name, *values)
        val parameters = edit()
        var replaced = false
        var index = 0
        while (index < parameters.size) {
            if (parameters[index].name != parameter.name) {
                index++
            } else if (replaced) {
                parameters.removeAt(index)
            } else {
                parameters[index++] = parameter
                replaced = true
            }
        }
        if (!replaced) parameters += parameter
    }

    // Looks before editing: removing a parameter the line does not have leaves the line unedited, so it is still
    // written byte for byte.

    /** Removes every parameter named [name]; nothing happens when there is none. */
    public fun removeParameter(name: String) {
        checkActive()
        val query = canonicalName(name)
        var found = false
        for (index in 0 until parameterCount) {
            if (parameterName(index) == query) {
                found = true
                break
            }
        }
        if (!found) return
        val parameters = edit()
        var index = 0
        while (index < parameters.size) {
            if (parameters[index].name == query) parameters.removeAt(index) else index++
        }
    }

    /** Removes the whole line from the tree or from the output. Later interceptors do not see it. */
    public fun drop() {
        checkActive()
        state = state or DROPPED
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = if (state and ACTIVE != 0) {
        describe("InterceptedLine", "name", name, "depth", reader?.depth ?: level, "edited", isEdited)
    } else {
        "InterceptedLine(none)"
    }

    internal fun bindRead(reader: ContentReader, line: ContentLine) {
        this.reader = reader
        source = line
        node = null
        owner = null
    }

    internal fun startRead() {
        state = ACTIVE
    }

    internal fun startWrite(property: ICalProperty, component: ICalComponent, depth: Int) {
        reader = null
        source = null
        node = property
        owner = component
        level = depth
        state = ACTIVE
    }

    internal fun clear() {
        state = 0
    }

    internal fun finish(): Boolean {
        val current = state
        state = current and ACTIVE.inv()
        return current and DROPPED == 0
    }

    // The node of an edited line: no source bytes (so the writer writes it canonically) but the span of the original
    // line (start 0, end = line length, shifted to its absolute offset).
    internal fun toProperty(offset: Double, length: Int, line: Int, column: Int): ICalProperty {
        val parameters = parameters
        val array = if (parameters.isNullOrEmpty()) null else parameters.toTypedArray()
        val nameId = KnownNames.find(editedName)
        return ICalProperty(
            null,
            0,
            0,
            0,
            length,
            nameId,
            0,
            null,
            offset,
            line,
            column,
            editedName,
            editedValue,
            array,
        )
    }

    private fun edit(): ArrayList<ICalParameter> {
        if (isEdited) return parameters!!
        val name = name
        val value: String
        val parameters = parameters ?: ArrayList<ICalParameter>().also { parameters = it }
        parameters.clear()
        val node = node
        if (node != null) {
            value = node.value
            for (parameter in node.parameterNodes()) parameters += parameter
        } else {
            val line = source!!
            value = decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8)
            for (index in 0 until line.parameterCount) {
                parameters += ICalParameter(null, null, 0, 0, parameterName(index), parameterValues(index))
            }
        }
        editedName = name
        editedValue = value
        state = state or EDITED
        return parameters
    }

    private fun checkActive() {
        if (state and ACTIVE ==
            0
        ) {
            throw IllegalStateException("The line is only available inside LineInterceptor.intercept")
        }
    }

    // 64 slots of canonical names of unknown properties, keyed by the upper-cased bytes: a check like `line.name ==
    // "X-VENDOR-ROOM"` does not allocate a string per line on feeds full of `X-` properties. Created on the first
    // unknown name, so a parse with an interceptor that never reads names pays nothing for it.
    private class NameCache {
        private val keys = arrayOfNulls<ByteArray>(SIZE)
        private val names = arrayOfNulls<String>(SIZE)

        fun name(bytes: ByteArray, from: Int, to: Int, valid: Boolean): String {
            var hash = to - from
            for (index in from until to) hash = hash * HASH_PRIME + upper(bytes[index])
            val slot = (hash xor (hash ushr HASH_SHIFT)) and (SIZE - 1)
            val key = keys[slot]
            if (key != null && key.size == to - from && matches(key, bytes, from)) return names[slot]!!
            val name = canonicalName(decodeUtf8(bytes, from, to, valid))
            keys[slot] = ByteArray(to - from) { upper(bytes[from + it]).toByte() }
            names[slot] = name
            return name
        }

        private fun matches(key: ByteArray, bytes: ByteArray, from: Int): Boolean {
            for (index in key.indices) {
                if (key[index].toInt() != upper(bytes[from + index])) return false
            }
            return true
        }

        private fun upper(byte: Byte): Int {
            val value = byte.toInt()
            return if (value in LOWER_A..LOWER_Z) value - CASE_BIT else value
        }
    }
}

// The instances of one parse or write, created from the factories of the options; the caller creates a chain only when
// there are factories, so null is the one check per line. `keep()` runs them in order and stops at `drop()`. The first
// interceptor is a field and the others an array that is null for one interceptor, so the usual chain of one is a
// single interface call with no loop. `keep()` has no `try`: a `try` per line, or the loop, makes Kotlin/Native open a
// GC frame on every call (a thread-local lookup, `_tlv_get_addr`), a large part of the cost of an empty interceptor
// there; `ContentReader.next()` and `TreeWriter.write()` deactivate the line when an exception leaves an interceptor,
// once per call instead of once per line. `finish()` clears ACTIVE and answers "kept" from one read of `state`. No
// companion object and no top-level helpers (the second constructor builds the chain from the factories): the chain is
// reachable from every parse, so its code stays in every application.
@OptIn(ExperimentalICalApi::class)
internal class InterceptorChain(private val first: LineInterceptor, private val rest: Array<out LineInterceptor>?) {
    constructor(factories: List<() -> LineInterceptor>) : this(
        factories[0](),
        if (factories.size == 1) null else Array(factories.size - 1) { factories[it + 1]() },
    )

    val line: InterceptedLine = InterceptedLine()

    fun keep(): Boolean = keep(line)

    // The reader passes its own reference to the line, and the chain starts, runs and finishes it through that local:
    // reading `chain.line` again after the call of the interface costs a load per step on the JVM, which does not trust
    // final instance fields. `keep()` is the same for the writer, whose line is started by `startWrite`.
    fun keepRead(line: InterceptedLine): Boolean {
        line.startRead()
        return keep(line)
    }

    private fun keep(line: InterceptedLine): Boolean {
        first.intercept(line)
        val rest = rest ?: return line.finish()
        return keepRest(line, rest)
    }

    private fun keepRest(line: InterceptedLine, rest: Array<out LineInterceptor>): Boolean {
        for (interceptor in rest) {
            if (line.isDropped) break
            interceptor.intercept(line)
        }
        return line.finish()
    }
}

private const val ACTIVE = 1
private const val EDITED = 2
private const val DROPPED = 4
private const val VIEW = 8
private const val NAMED = 16
private const val MIN_CHARS = 256
private const val SIZE = 64
private const val HASH_PRIME = 31
private const val HASH_SHIFT = 16
private const val LOWER_A = 0x61
private const val LOWER_Z = 0x7A
private const val CASE_BIT = 0x20
