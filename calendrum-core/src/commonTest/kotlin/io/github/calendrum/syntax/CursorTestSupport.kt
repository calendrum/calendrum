// Shapes of a tree and of cursor events (names, parameters, values, spans, start of every component) to compare the two
// without `equals`, which ignores spans.

package io.github.calendrum.syntax

import io.github.calendrum.SourceSpan
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal data class PropertyShape(
    val name: String,
    val parameters: List<Pair<String, List<String>>>,
    val value: String,
    val span: SourceSpan?,
)

internal data class NodeShape(
    val name: String,
    val start: Triple<Long, Int, Int>?,
    val properties: List<PropertyShape>,
    val children: List<NodeShape>,
)

internal fun ICalComponent.shape(): NodeShape = NodeShape(
    name,
    span?.let { Triple(it.startOffset, it.line, it.column) },
    properties.map { property ->
        PropertyShape(
            property.name,
            property.parameters.map { it.name to it.values },
            property.value,
            property.span,
        )
    },
    components.map { it.shape() },
)

internal fun cursorShapes(reader: ICalReader): List<NodeShape> {
    class Open(val name: String, val start: Triple<Long, Int, Int>) {
        val properties = ArrayList<PropertyShape>()
        val children = ArrayList<NodeShape>()
    }
    val roots = ArrayList<NodeShape>()
    val stack = ArrayList<Open>()
    while (true) {
        when (reader.next() ?: break) {
            ContentEventType.Begin -> {
                assertEquals(stack.size + 1, reader.depth, "depth of Begin ${reader.name}")
                assertFalse(reader.isImplicitEnd)
                val span = reader.span()
                assertEquals(reader.offset, span.startOffset)
                assertEquals(reader.line, span.line)
                assertEquals(reader.column, span.column)
                stack += Open(reader.name, Triple(reader.offset, reader.line, reader.column))
            }

            ContentEventType.Property -> {
                assertEquals(stack.size, reader.depth, "depth of Property ${reader.name}")
                assertTrue(stack.isNotEmpty(), "a property outside components")
                val parameters = List(reader.parameterCount) { reader.parameterName(it) to reader.parameterValues(it) }
                val value = reader.value.toString()
                assertEquals(value, reader.valueString())
                val span = reader.span()
                assertEquals(reader.offset, span.startOffset)
                stack.last().properties += PropertyShape(reader.name, parameters, value, span)
            }

            ContentEventType.End -> {
                val open = stack.removeAt(stack.lastIndex)
                assertEquals(open.name, reader.name, "name of End")
                assertEquals(stack.size + 1, reader.depth, "depth of End ${reader.name}")
                val node = NodeShape(open.name, open.start, open.properties, open.children)
                if (stack.isEmpty()) roots += node else stack.last().children += node
            }
        }
    }
    assertTrue(stack.isEmpty(), "unbalanced events")
    return roots
}
