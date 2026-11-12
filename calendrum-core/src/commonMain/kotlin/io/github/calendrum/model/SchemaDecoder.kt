@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.model

import io.github.calendrum.InternalICalApi
import io.github.calendrum.Problem
import io.github.calendrum.ProblemCode
import io.github.calendrum.SourceSpan
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ICalParser
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.ProblemCollector
import io.github.calendrum.syntax.TreeBuilder
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.decodeUtf8
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Drives the content reader like the tree builder but keeps only slots: frames by depth for the root schema and its
// child schemas, the first property of a single field decides (as the accessors do), a malformed one leaves a sentinel
// so that a later duplicate does not replace it. Built-in codecs decode the reader's line; a user codec or an
// intercepted line gets a node from `TreeBuilder.readProperty`, counted by `schemaPropertyNodes`.
internal class SchemaDecoder<T : Any>(
    private val reader: ContentReader,
    private val schema: IcsSchema<T>,
    strict: Boolean,
    private val inputSize: Int,
) {
    private val context = if (strict) DecodeContext.Strict else DecodeContext.Lenient

    private var frames = arrayOfNulls<Frame>(INITIAL_FRAMES)

    private var top: Frame? = null

    private var fallback: TreeBuilder? = null

    var skipped: Int = 0
        private set

    var nodes: Int = 0
        private set

    fun next(): T? {
        while (true) {
            when (reader.next()) {
                ContentReader.END_OF_INPUT -> return null

                ContentReader.BEGIN -> begin()

                ContentReader.PROPERTY -> property()

                else -> {
                    val item = end()
                    if (item != null) return item
                }
            }
        }
    }

    private fun begin() {
        val depth = reader.depth
        val current = top
        val target: IcsSchema<*> = if (current == null) {
            if (!matches(schema)) return
            schema
        } else {
            if (current.depth != depth - 1) return
            current.schema.compiled.child(componentName()) ?: return
        }
        if (depth >= frames.size) frames = frames.copyOf(maxOf(frames.size * 2, depth + 1))
        val frame = frames[depth]?.takeIf { it.schema === target } ?: Frame(target).also { frames[depth] = it }
        frame.open(depth, current, beginSpan())
        top = frame
    }

    private fun matches(target: IcsSchema<*>): Boolean {
        val id = reader.componentId
        return if (target.componentId >= 0) id == target.componentId else id < 0 && componentName() == target.component
    }

    private fun componentName(): String {
        val id = reader.componentId
        if (id >= 0) return KnownNames.name(id)
        val line = reader.line
        return canonicalName(decodeUtf8(line.bytes, line.valueStart, line.valueEnd, line.validUtf8))
    }

    private fun beginSpan(): SourceSpan {
        val line = reader.line
        return SourceSpan(reader.origin + line.startOffset, reader.origin + line.endOffset, line.line, line.column)
    }

    private fun property() {
        val frame = top ?: return
        if (frame.depth != reader.depth) return
        val line = reader.line
        val id = line.nameId
        val name = if (id >= 0) KnownNames.name(id) else canonicalName(line.name())
        val indices = frame.schema.compiled.fields(id, name) ?: return
        val slots = frame.slots
        for (index in indices) {
            val field = frame.schema.fields[index]
            if (field.kind != IcsSchema.LIST && slots[index] != null) continue
            when (val decoded = decode(field.key!!)) {
                is Decoded.Value -> {
                    report(decoded.problems)
                    val value = field.decode?.invoke(decoded.value) ?: decoded.value
                    if (field.kind == IcsSchema.LIST) {
                        @Suppress("UNCHECKED_CAST")
                        val list = (slots[index] as ArrayList<Any?>?) ?: ArrayList<Any?>().also { slots[index] = it }
                        list += value
                    } else {
                        slots[index] = value
                    }
                }

                is Decoded.Malformed -> {
                    val warnings = decoded.warnings
                    if (warnings.isEmpty()) reader.reportValue(decoded.problem) else report(warnings)
                    if (field.kind != IcsSchema.LIST) slots[index] = IcsSchema.MALFORMED
                }

                Decoded.Missing -> Unit
            }
        }
    }

    private fun decode(key: PropertyKey<*>): Decoded<Any> {
        val codec = key.codec
        if (codec is StandardCodec<*> && reader.edits == null) return codec.decodeLine(reader, context)
        val builder = fallback ?: TreeBuilder(reader, false, inputSize, true).also { fallback = it }
        nodes += 1
        @Suppress("UNCHECKED_CAST")
        return (codec as PropertyCodec<Any>).decode(builder.readProperty(), context)
    }

    private fun end(): T? {
        val frame = top ?: return null
        if (frame.depth != reader.depth) return null
        top = frame.parent
        val missing = frame.missing()
        if (missing != null) {
            reader.reportValue(Problem(ProblemCode.MissingRequiredField, frame.span!!, missing))
            if (frame.parent == null) skipped += 1
            frame.clear()
            return null
        }
        val item = frame.schema.create(frame.values)
        frame.clear()
        val parent = frame.parent
        if (parent == null) {
            @Suppress("UNCHECKED_CAST")
            return item as T
        }
        val index = parent.schema.compiled.childField(frame.schema.component)

        @Suppress("UNCHECKED_CAST")
        val list = (parent.slots[index] as ArrayList<Any?>?) ?: ArrayList<Any?>().also { parent.slots[index] = it }
        list += item
        return null
    }

    private fun report(problems: List<Problem>) {
        for (problem in problems) reader.reportValue(problem)
    }

    private class Frame(val schema: IcsSchema<*>) {
        val slots: Array<Any?> = arrayOfNulls(schema.fields.size)
        val values: IcsSchema.Values = IcsSchema.Values(schema, slots)
        var depth: Int = 0
        var parent: Frame? = null
        var span: SourceSpan? = null

        fun open(depth: Int, parent: Frame?, span: SourceSpan) {
            this.depth = depth
            this.parent = parent
            this.span = span
            clear()
        }

        fun missing(): String? {
            for (field in schema.fields) {
                if (field.kind != IcsSchema.REQUIRED) continue
                val slot = slots[field.index]
                if (slot == null || slot === IcsSchema.MALFORMED) return field.name
            }
            return null
        }

        fun clear() {
            slots.fill(null)
        }
    }

    private companion object {
        const val INITIAL_FRAMES = 8
    }
}

/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public fun schemaPropertyNodes(parser: ICalParser, bytes: ByteArray, schema: IcsSchema<*>): Int {
    val decoder = parser.schemaDecoder(bytes, schema, ProblemCollector(null, false, false))
    while (decoder.next() != null) Unit
    return decoder.nodes
}
