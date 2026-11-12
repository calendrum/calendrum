package io.github.calendrum.model

import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.KnownNames
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDateTime

internal inline fun <T : Any> children(component: ICalComponent, name: String, wrap: (ICalComponent) -> T): List<T> {
    val nodes = component.componentNodes
    val id = KnownNames.find(name)
    var count = 0
    for (node in nodes) {
        if (node.nameId == id) count += 1
    }
    if (count == 0) return emptyList()
    val result = arrayOfNulls<Any>(count)
    var index = 0
    for (node in nodes) {
        if (node.nameId == id) result[index++] = wrap(node)
    }
    @Suppress("UNCHECKED_CAST")
    return ArrayView(result as Array<T>)
}

internal fun texts(component: ICalComponent, key: PropertyKey<String>): List<String> = component.all(key)

internal fun contacts(component: ICalComponent): List<String> {
    val contacts = component.all(Properties.CONTACT)
    return if (contacts.isEmpty()) emptyList() else ArrayView(Array(contacts.size) { contacts[it].text })
}

internal fun categories(component: ICalComponent): List<String> {
    val all = component.all(Properties.CATEGORIES)
    if (all.isEmpty()) return emptyList()
    if (all.size == 1) return all[0].values
    val result = ArrayList<String>()
    for (item in all) result.addAll(item.values)
    return result
}

internal fun resources(component: ICalComponent): List<String> {
    val all = component.all(Properties.RESOURCES)
    if (all.isEmpty()) return emptyList()
    if (all.size == 1) return all[0].values
    val result = ArrayList<String>()
    for (item in all) result.addAll(item.values)
    return result
}

internal fun exdates(component: ICalComponent): List<ICalDateTime> {
    val all = component.all(Properties.EXDATE)
    if (all.isEmpty()) return emptyList()
    if (all.size == 1) return all[0].dates
    val result = ArrayList<ICalDateTime>()
    for (item in all) result.addAll(item.dates)
    return result
}

internal fun rdates(component: ICalComponent): List<ICalDateTime> {
    val all = component.all(Properties.RDATE)
    if (all.isEmpty()) return emptyList()
    if (all.size == 1 && all[0].periods.isEmpty()) return all[0].dates
    val result = ArrayList<ICalDateTime>()
    for (item in all) {
        result.addAll(item.dates)
        for (period in item.periods) result.add(period.start)
    }
    return result
}

internal fun describeWrapper(type: String, component: ICalComponent): String =
    describe(type, "properties", component.propertyNodes.size, "components", component.componentNodes.size)

internal fun named(component: ICalComponent, name: String): Boolean {
    val id = KnownNames.find(name)
    return if (id >= 0) component.nameId == id else component.name == name
}
