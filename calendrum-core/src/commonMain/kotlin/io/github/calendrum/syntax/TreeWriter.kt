package io.github.calendrum.syntax

// Writes a tree over an explicit stack (hand-built trees can be deep). Per open component: the next child, how many
// properties are written and the mode: PLAIN (properties, then children), INTERLEAVED (a parsed node whose properties
// came after children: properties before each child by `childPositions`, in both modes), ZONES then OTHERS
// (`timeZonesFirst` in a VCALENDAR: properties, VTIMEZONE children, the rest).
internal class TreeWriter(
    private val out: ContentLineWriter,
    private val byteStable: Boolean,
    private val timeZonesFirst: Boolean,
    private val interceptors: InterceptorChain?,
    private val allowPartial: Boolean,
    private val missingTimeZones: VTimeZoneSource?,
) {
    private var stack = arrayOfNulls<ICalComponent>(INITIAL_DEPTH)
    private var nextChild = IntArray(INITIAL_DEPTH)
    private var written = IntArray(INITIAL_DEPTH)
    private var modes = IntArray(INITIAL_DEPTH)
    private var top = 0

    fun write(component: ICalComponent) {
        top = 0
        val root = if (missingTimeZones == null) component else withMissingTimeZones(component, missingTimeZones)
        try {
            open(root)
            while (top > 0) {
                val frame = top - 1
                val component = stack[frame]!!
                val child = next(frame, component)
                if (child != null) {
                    open(child)
                } else {
                    close(frame, component)
                    stack[frame] = null
                    top--
                }
            }
        } catch (e: Throwable) {
            interceptors?.line?.clear()
            throw e
        }
    }

    private fun open(component: ICalComponent) {
        if (top == stack.size) {
            val size = top * 2
            stack = stack.copyOf(size)
            nextChild = nextChild.copyOf(size)
            written = written.copyOf(size)
            modes = modes.copyOf(size)
        }
        check(allowPartial || !component.isPartial) {
            "A component from a selective parse is written only with WriteOptions.allowPartialWrite"
        }
        val source = byteStable && component.isParsed
        if (source) component.writeBegin(out) else out.begin(component.name)
        val mode = when {
            timeZonesFirst && component.nameId == KnownNames.VCALENDAR -> ZONES
            component.childPositions != null -> INTERLEAVED
            else -> PLAIN
        }
        val properties = component.propertyNodes
        if (mode != INTERLEAVED) writeProperties(component, top + 1, 0, properties.size)
        stack[top] = component
        nextChild[top] = 0
        written[top] = if (mode == INTERLEAVED) 0 else properties.size
        modes[top] = mode
        top++
    }

    private fun next(frame: Int, component: ICalComponent): ICalComponent? {
        val children = component.componentNodes
        var index = nextChild[frame]
        when (modes[frame]) {
            ZONES -> {
                while (index < children.size && children[index].nameId != KnownNames.VTIMEZONE) index++
                if (index < children.size) {
                    nextChild[frame] = index + 1
                    return children[index]
                }
                modes[frame] = OTHERS
                index = 0
            }

            INTERLEAVED -> if (index < children.size) {
                val before = component.childPositions!![index]
                writeProperties(component, frame + 1, written[frame], before)
                written[frame] = before
            }
        }
        if (modes[frame] == OTHERS) {
            while (index < children.size && children[index].nameId == KnownNames.VTIMEZONE) index++
        }
        if (index == children.size) {
            nextChild[frame] = index
            return null
        }
        nextChild[frame] = index + 1
        return children[index]
    }

    private fun close(frame: Int, component: ICalComponent) {
        writeProperties(component, frame + 1, written[frame], component.propertyNodes.size)
        if (byteStable && component.isParsed) component.writeEnd(out) else out.end(component.name)
    }

    // Write interceptors run per property of a node before it is written (depth = level of the node, 1 = top); a
    // dropped line is skipped, an edited one is written canonically. The `catch` in `write()` clears the line when an
    // interceptor throws, as in `ContentReader.next()`.
    private fun writeProperties(component: ICalComponent, depth: Int, from: Int, to: Int) {
        val properties = component.propertyNodes
        val chain = interceptors
        for (index in from until to) {
            val property = properties[index]
            if (chain != null) {
                val line = chain.line
                line.startWrite(property, component, depth)
                if (!chain.keep()) continue
                if (line.isEdited) {
                    writeCanonical(line.editedName, line.editedParameters.toTypedArray(), line.editedValue)
                    continue
                }
            }
            if (byteStable && property.isParsed) {
                property.writeSource(out)
            } else {
                writeCanonical(property.name, property.parameterNodes(), property.value)
            }
        }
    }

    // A parameter without a value (ICAL-S012) has no strict form; it is rejected before the line starts, so the writer
    // stays between lines. Errors name the property only when its name is valid and never contain values.
    private fun writeCanonical(name: String, parameters: Array<ICalParameter>, value: String) {
        try {
            for (parameter in parameters) {
                if (parameter.valueCount == 0) {
                    throw IllegalArgumentException("A parameter without a value cannot be written")
                }
            }
            out.property(name)
            for (parameter in parameters) {
                out.parameter(parameter.name)
                for (item in parameter.values) out.parameterValue(item)
            }
            out.value(value)
        } catch (e: IllegalArgumentException) {
            val subject = if (isName(name)) "property $name" else "a property"
            throw IllegalArgumentException("Cannot write $subject: ${e.message}", e)
        }
    }

    private companion object {
        private const val INITIAL_DEPTH = 8
        private const val PLAIN = 0
        private const val INTERLEAVED = 1
        private const val ZONES = 2
        private const val OTHERS = 3
    }
}

// One property written canonically, with the checks and the error message of the tree writer.
internal fun canonicalLine(property: ICalProperty): ByteArray {
    val out = ByteLineWriter()
    val parameters = property.parameterNodes()
    val name = property.name
    try {
        for (parameter in parameters) {
            require(parameter.valueCount != 0) { "A parameter without a value cannot be written" }
        }
        out.property(name)
        for (parameter in parameters) {
            out.parameter(parameter.name)
            for (item in parameter.values) out.parameterValue(item)
        }
        out.value(property.value)
    } catch (e: IllegalArgumentException) {
        val subject = if (isName(name)) "property $name" else "a property"
        throw IllegalArgumentException("Cannot write $subject: ${e.message}", e)
    }
    return out.toByteArray()
}
