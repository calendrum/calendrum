@file:OptIn(ExperimentalObjCRefinement::class)

package io.github.calendrum.syntax

import io.github.calendrum.InternalICalApi
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

// Which components and properties to read. Built once, immutable afterwards. Two bit masks over `KnownNames` ids; the
// two flags decide unknown names. The flags are also written into the masks for ids of the wrong kind (a property named
// VEVENT is an unknown property), so every check is one bit.
/**
 * Internal API, not a contract.
 *
 * @suppress
 */
@InternalICalApi
@HiddenFromObjC
public class ContentSelection internal constructor(
    private val componentMask: LongArray,
    private val propertyMask: LongArray,
    private val unknownComponents: Boolean,
    private val unknownProperties: Boolean,
    // Components kept whole: every property and child of them is read whatever the property mask says (time zones,
    // alarms of `need(Alarms)`, free/busy of the preset). One mask of properties serves every selected component, so
    // without this a selection of event properties would cut the time zones apart.
    private val wholeMask: LongArray?,
    // Exact unknown names of a selection. The lexer only knows ids, so the unknown flag lets every unknown line through
    // and the reader compares the name; `null` keeps the flag's meaning.
    internal val componentNames: Array<String>?,
    internal val propertyNames: Array<String>?,
    internal val isFull: Boolean,
) {
    // True when no property can be skipped: the lexer then never peeks at names (the `Full` path).
    internal val allProperties: Boolean = unknownProperties && propertyNames == null && includesEveryKnownProperty()

    // The exact unknown names as ASCII bytes, so the lexer skips an unknown line it does not want without decoding its
    // name; names are compared in upper case, like `canonicalName`. Without keys the old flag decides.
    private val propertyKeys: Array<ByteArray>? = propertyNames?.let { names ->
        Array(names.size) { names[it].encodeToByteArray() }
    }

    // The lexer reads a name up to the longest known or selected name; a longer one cannot match any key, so it is
    // skipped unless the flag takes every unknown name.
    internal val longestName: Int = maxOf(KnownNames.maxLength, propertyKeys?.maxOf { it.size } ?: 0)

    internal val includesLongUnknownProperty: Boolean = unknownProperties && propertyKeys == null

    private fun includesEveryKnownProperty(): Boolean {
        for (id in 0 until KnownNames.count) {
            if (!includesProperty(id)) return false
        }
        return true
    }

    internal fun includesComponent(id: Int): Boolean =
        if (id < 0) unknownComponents else (componentMask[id ushr 6] ushr (id and 63)) and 1L != 0L

    internal fun includesProperty(id: Int): Boolean =
        if (id < 0) unknownProperties else (propertyMask[id ushr 6] ushr (id and 63)) and 1L != 0L

    internal fun includesUnknownProperty(bytes: ByteArray, from: Int, to: Int): Boolean {
        val keys = propertyKeys ?: return unknownProperties
        for (key in keys) {
            if (key.size == to - from && matchesName(key, bytes, from)) return true
        }
        return false
    }

    private fun matchesName(key: ByteArray, bytes: ByteArray, from: Int): Boolean {
        for (index in key.indices) {
            val byte = bytes[from + index].toInt()
            val upper = if (byte in LOWER_A..LOWER_Z) byte - CASE_BIT else byte
            if (upper != key[index].toInt()) return false
        }
        return true
    }

    internal fun isWhole(id: Int): Boolean {
        val mask = wholeMask ?: return false
        return id >= 0 && (mask[id ushr 6] ushr (id and 63)) and 1L != 0L
    }

    // One rule for the reader and for `filterSelection`, so the invariant compares the skipping in the lexer with the
    // same decision taken on a full tree.
    internal inline fun wantsComponent(id: Int, name: () -> String): Boolean {
        val names = componentNames
        if (names == null ||
            (id >= 0 && KnownNames.kinds(id) and KnownNames.COMPONENT != 0)
        ) {
            return includesComponent(id)
        }
        return names.contains(name())
    }

    internal inline fun wantsProperty(id: Int, name: () -> String): Boolean {
        if (!includesProperty(id)) return false
        val names = propertyNames
        if (names == null || (id >= 0 && KnownNames.kinds(id) and KnownNames.PROPERTY != 0)) return true
        return names.contains(name())
    }

    /**
     * Internal API, not a contract.
     *
     * @suppress
     */
    @InternalICalApi
    @HiddenFromObjC
    public class Builder {
        private val components = LongArray(maskSize())
        private val properties = LongArray(maskSize())
        private var unknownComponents = false
        private var unknownProperties = false

        public fun components(vararg names: String): Builder {
            for (name in names) set(components, known(name, KnownNames.COMPONENT))
            return this
        }

        // BEGIN and END are structure, not properties, and are always read.
        public fun properties(vararg names: String): Builder {
            for (name in names) {
                val id = known(name, KnownNames.PROPERTY)
                require(id != KnownNames.BEGIN && id != KnownNames.END) { "BEGIN and END are always read" }
                set(properties, id)
            }
            return this
        }

        public fun unknownComponents(include: Boolean): Builder {
            unknownComponents = include
            return this
        }

        public fun unknownProperties(include: Boolean): Builder {
            unknownProperties = include
            return this
        }

        public fun build(): ContentSelection =
            create(components, properties, unknownComponents, unknownProperties, null, null, null)
    }

    public companion object {
        public val Full: ContentSelection = everything()

        internal fun maskSize(): Int = (KnownNames.count + 63) ushr 6

        // `Full` without `create()`: both masks have the bits of all known ids. The initializer of the class went from
        // 2.8 to 1.2 KB of code on iOS.
        private fun everything(): ContentSelection {
            val mask = LongArray(maskSize())
            for (id in 0 until KnownNames.count) set(mask, id)
            return ContentSelection(mask, mask.copyOf(), true, true, null, null, null, true)
        }

        private fun known(name: String, kind: Int): Int {
            val id = KnownNames.lookup(name)
            require(id >= 0 && KnownNames.kinds(id) and kind != 0) { "Not a known name of this kind: $name" }
            return id
        }

        internal fun set(mask: LongArray, id: Int) {
            mask[id ushr 6] = mask[id ushr 6] or (1L shl (id and 63))
        }

        internal fun create(
            components: LongArray,
            properties: LongArray,
            unknownComponents: Boolean,
            unknownProperties: Boolean,
            whole: LongArray?,
            componentNames: Array<String>?,
            propertyNames: Array<String>?,
        ): ContentSelection {
            val componentMask = LongArray(maskSize())
            val propertyMask = LongArray(maskSize())
            for (id in 0 until KnownNames.count) {
                val kinds = KnownNames.kinds(id)
                val component = if (kinds and KnownNames.COMPONENT != 0) {
                    (components[id ushr 6] ushr (id and 63)) and 1L != 0L
                } else {
                    unknownComponents
                }
                val property = when {
                    id == KnownNames.BEGIN || id == KnownNames.END -> true
                    kinds and KnownNames.PROPERTY != 0 -> (properties[id ushr 6] ushr (id and 63)) and 1L != 0L
                    else -> unknownProperties
                }
                if (component) set(componentMask, id)
                if (property) set(propertyMask, id)
            }
            return ContentSelection(
                componentMask,
                propertyMask,
                unknownComponents,
                unknownProperties,
                whole,
                componentNames,
                propertyNames,
                false,
            )
        }
    }
}

private const val LOWER_A = 0x61
private const val LOWER_Z = 0x7A
private const val CASE_BIT = 0x20
