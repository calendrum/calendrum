package io.github.calendrum.syntax

internal interface ValueHook {
    fun component(component: ICalComponent, reader: ContentReader)

    fun property(property: ICalProperty, componentId: Int, reader: ContentReader)
}
