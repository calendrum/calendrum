package io.github.calendrum.model

import io.github.calendrum.ICal
import io.github.calendrum.Problem
import io.github.calendrum.parser
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.KnownNames
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal fun propertyOf(line: String, component: String = "VEVENT", retainSource: Boolean = true): ICalProperty {
    val text = "BEGIN:$component\r\n$line\r\nEND:$component\r\n"
    val parser = ICal.parser { this.retainSource = retainSource }
    val result = parser.parse(text)
    return result.components.single().properties.single()
}

internal fun <T : Any> lenient(key: PropertyKey<T>, line: String, component: String = "VEVENT"): Decoded<T> =
    key.codec.decode(propertyOf(line, component), DecodeContext.Lenient)

internal fun <T : Any> strict(key: PropertyKey<T>, line: String, component: String = "VEVENT"): Decoded<T> =
    key.codec.decode(propertyOf(line, component), DecodeContext.Strict)

internal fun <T : Any> value(key: PropertyKey<T>, line: String, component: String = "VEVENT"): T {
    val decoded = lenient(key, line, component)
    val value = assertIs<Decoded.Value<T>>(decoded, line)
    assertEquals(emptyList(), value.problems.map(::describe), line)
    assertIs<Decoded.Value<T>>(strict(key, line, component), "$line in strict mode")
    return value.value
}

internal fun describe(problem: Problem): String = "${problem.code.id}@${problem.span.line}:${problem.span.column}"

internal fun Decoded<*>.codes(): List<String> = when (this) {
    is Decoded.Value -> problems.map(::describe)
    is Decoded.Malformed -> (if (warnings.isEmpty()) listOf(problem) else warnings).map(::describe)
    Decoded.Missing -> emptyList()
}

internal fun <T : Any> assertLenient(decoded: Decoded<T>, vararg codes: String): T {
    val value = assertIs<Decoded.Value<T>>(decoded)
    assertEquals(codes.toList(), decoded.codes())
    return value.value
}

internal fun assertMalformed(decoded: Decoded<*>, vararg codes: String) {
    assertIs<Decoded.Malformed>(decoded)
    assertEquals(codes.toList(), decoded.codes())
}

internal fun <T : Any> encoded(key: PropertyKey<T>, value: T): ICalProperty =
    key.codec.encode(value, EncodeContext(key.name))

internal fun standardKey(property: String, component: String): PropertyKey<*>? =
    ICalRegistry.Default.property(KnownNames.find(property), property, component)?.key

internal fun lineOf(property: ICalProperty): String =
    io.github.calendrum.syntax.canonicalLine(property).decodeToString().removeSuffix("\r\n").replace("\r\n ", "")

internal fun lineOf(parameter: io.github.calendrum.syntax.ICalParameter): String =
    lineOf(ICalProperty.builder("X-P").parameter(parameter).value("x").build()).removePrefix("X-P;").removeSuffix(":x")
