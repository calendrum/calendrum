package io.github.calendrum.model

import io.github.calendrum.Problem
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.ValueHook

// Eager decoding asks the registry of the parse options (the default registry when none is set, resolved on first use)
// which properties of which components to decode, so own X-properties are checked too.
internal class EagerDecoding(private val chosen: ICalRegistry?) : ValueHook {
    private var resolved: ICalRegistry? = chosen

    private val registry: ICalRegistry
        get() = resolved ?: ICalRegistry.Default.also { resolved = it }

    override fun component(component: ICalComponent, reader: ContentReader) {
        val context = if (reader.strict) DecodeContext.Strict else DecodeContext.Lenient
        val registry = registry
        var stack = arrayOfNulls<ICalComponent>(INITIAL_STACK)
        stack[0] = component
        var top = 1
        while (top > 0) {
            val node = stack[--top]!!
            val name = node.name
            if (registry.knowsComponent(node.nameId, name)) {
                for (property in node.propertyNodes) decode(registry, property, name, context, reader)
            }
            val children = node.componentNodes
            if (top + children.size > stack.size) stack = stack.copyOf(maxOf(stack.size * 2, top + children.size))
            for (index in children.indices.reversed()) stack[top++] = children[index]
        }
    }

    override fun property(property: ICalProperty, componentId: Int, reader: ContentReader) {
        val context = if (reader.strict) DecodeContext.Strict else DecodeContext.Lenient
        if (componentId < 0) return
        decode(registry, property, KnownNames.name(componentId), context, reader)
    }

    override fun toString(): String = if (chosen == null) "EagerDecoding(default)" else "EagerDecoding($chosen)"

    private companion object {
        const val INITIAL_STACK = 8

        fun decode(
            registry: ICalRegistry,
            property: ICalProperty,
            component: String,
            context: DecodeContext,
            reader: ContentReader,
        ) {
            val definition = registry.property(property.nameId, property.name, component) ?: return
            when (val decoded = property.decodeWith(definition.key.codec, context)) {
                is Decoded.Value -> report(decoded.problems, reader)

                is Decoded.Malformed -> {
                    val warnings = decoded.warnings
                    if (warnings.isEmpty()) reader.reportValue(decoded.problem) else report(warnings, reader)
                }

                Decoded.Missing -> Unit
            }
        }

        fun report(problems: List<Problem>, reader: ContentReader) {
            for (problem in problems) reader.reportValue(problem)
        }
    }
}
