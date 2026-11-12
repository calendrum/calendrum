package io.github.calendrum.model

import io.github.calendrum.syntax.ICalComponent

// Writes items through the component builders, fields in declaration order; UID and DTSTAMP of VEVENT, VTODO, VJOURNAL
// and VFREEBUSY and UID of VLOCATION, VRESOURCE and PARTICIPANT come from the values or from `BuildDefaults`, as with
// `build()`.
internal fun <T : Any> schemaCalendar(
    items: List<T>,
    schema: IcsSchema<T>,
    calendar: VCalendar.Builder.() -> Unit,
    defaults: BuildDefaults?,
): ICalComponent {
    val builder = VCalendar.Builder(null, defaults)
    builder.calendar()
    checkNotNull(builder.prodId) { "The calendar needs a PRODID" }
    if (builder.version == null) builder.version = VERSION_2_0
    for (item in items) builder.addChild(schemaComponent(schema, item, defaults))
    return builder.build().component
}

// Encoding into a prepared VCalendar: its properties and children stay, the items are appended; Java has no lambda to
// pass.
internal fun <T : Any> schemaCalendar(
    items: List<T>,
    schema: IcsSchema<T>,
    calendar: VCalendar,
    defaults: BuildDefaults?,
): ICalComponent {
    val builder = if (defaults == null) calendar.toBuilder() else calendar.toBuilder(defaults)
    checkNotNull(builder.prodId) { "The calendar needs a PRODID" }
    if (builder.version == null) builder.version = VERSION_2_0
    for (item in items) builder.addChild(schemaComponent(schema, item, defaults))
    return builder.build().component
}

private fun schemaComponent(schema: IcsSchema<*>, item: Any, defaults: BuildDefaults?): ICalComponent {
    val builder = SchemaComponentBuilder(schema.component, defaults)
    for (field in schema.fields) {
        val value = field.get(item)

        @Suppress("UNCHECKED_CAST")
        val key = field.key as PropertyKey<Any>?
        when (field.kind) {
            IcsSchema.REQUIRED -> builder.add(key!!, requireNotNull(value) { "${field.name} is required" })

            IcsSchema.OPTIONAL -> if (value != null) builder.add(key!!, value)

            IcsSchema.LIST -> {
                val encode = field.encode
                for (element in value as List<*>) {
                    builder.add(
                        key!!,
                        encode?.invoke(element) ?: requireNotNull(element) { "${field.name} has a null item" },
                    )
                }
            }

            else -> for (child in value as List<*>) builder.addChild(schemaComponent(field.child!!, child!!, defaults))
        }
    }
    when (schema.component) {
        "VEVENT", "VTODO", "VJOURNAL", "VFREEBUSY" -> builder.requireIdentity()
        "VLOCATION", "VRESOURCE", "PARTICIPANT" -> builder.requireUid()
    }
    return builder.buildNode()
}

private class SchemaComponentBuilder(name: String, defaults: BuildDefaults?) :
    ComponentBuilder<SchemaComponentBuilder>(name, null, defaults)

private const val VERSION_2_0 = "2.0"
