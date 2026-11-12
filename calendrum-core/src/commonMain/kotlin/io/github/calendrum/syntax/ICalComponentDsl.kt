@file:JvmName("ICalComponentDsl")

package io.github.calendrum.syntax

import kotlin.jvm.JvmName

/**
 * Builds a component with a Kotlin DSL.
 *
 * ```
 * val event = ICalComponent("VEVENT") {
 *     property("UID", "42@example.com")
 *     component("VALARM") { property("ACTION", "DISPLAY") }
 * }
 * ```
 *
 * @param name the component name, letters, digits and hyphens.
 * @param block adds properties and nested components.
 * @throws IllegalArgumentException if [name] or a name inside [block] is invalid.
 */
public fun ICalComponent(name: String, block: ICalComponent.Builder.() -> Unit): ICalComponent =
    ICalComponent.builder(name).apply(block).build()
