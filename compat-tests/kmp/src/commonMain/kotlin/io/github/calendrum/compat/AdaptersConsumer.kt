package io.github.calendrum.compat

import io.github.calendrum.ICal
import kotlinx.io.readByteArray
import io.github.calendrum.kotlinxio.parse as kotlinxParse
import io.github.calendrum.kotlinxio.write as kotlinxWrite
import io.github.calendrum.okio.parse as okioParse
import io.github.calendrum.okio.write as okioWrite

fun summariesThroughKotlinxIo(): List<String> {
    val source = kotlinx.io.Buffer()
    source.write(invitation().encodeToByteArray())
    return ICal.parser().kotlinxParse(source).components.single().components("VEVENT")
        .map { it.property("SUMMARY")?.value.orEmpty() }
}

fun summariesThroughOkio(): List<String> {
    val source = okio.Buffer().write(invitation().encodeToByteArray())
    return ICal.parser().okioParse(source).components.single().components("VEVENT")
        .map { it.property("SUMMARY")?.value.orEmpty() }
}

fun invitationThroughSinks(): List<String> {
    val tree = ICal.parse(invitation()).components
    val kotlinx = kotlinx.io.Buffer()
    ICal.writer().kotlinxWrite(tree, kotlinx)
    val okio = okio.Buffer()
    ICal.writer().okioWrite(tree, okio)
    return listOf(kotlinx.readByteArray().decodeToString(), okio.readUtf8())
}
