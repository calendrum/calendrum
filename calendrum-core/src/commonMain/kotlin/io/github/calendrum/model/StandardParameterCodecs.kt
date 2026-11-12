package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty

internal const val PARAMETER_TEXT = 0
internal const val PARAMETER_URI = 1
internal const val PARAMETER_LIST = 2
internal const val PARAMETER_BOOLEAN = 3
internal const val PARAMETER_CUTYPE = 4
internal const val PARAMETER_ENCODING = 5
internal const val PARAMETER_FBTYPE = 6
internal const val PARAMETER_PARTSTAT = 7
internal const val PARAMETER_RANGE = 8
internal const val PARAMETER_RELATED = 9
internal const val PARAMETER_RELTYPE = 10
internal const val PARAMETER_ROLE = 11
internal const val PARAMETER_VALUE = 12

@OptIn(ExperimentalICalApi::class)
internal class StandardParameterCodec<T : Any>(private val kind: Int) : ParameterCodec<T> {
    @Suppress("UNCHECKED_CAST")
    override fun decode(property: ICalProperty, parameter: ICalParameter, context: DecodeContext): Decoded<T> {
        val value = parameter.value
        val decoded: Any = when (kind) {
            PARAMETER_TEXT, PARAMETER_URI -> value

            PARAMETER_LIST -> parameter.values

            else -> {
                if (value.isEmpty()) return malformed(property, parameter, context)
                when (kind) {
                    PARAMETER_BOOLEAN -> when (enumToken(value)) {
                        "TRUE" -> true
                        "FALSE" -> false
                        else -> return malformed(property, parameter, context)
                    }

                    PARAMETER_CUTYPE -> CuType.of(value)

                    PARAMETER_ENCODING -> Encoding.of(value)

                    PARAMETER_FBTYPE -> FbType.of(value)

                    PARAMETER_PARTSTAT -> PartStat.of(value)

                    PARAMETER_RANGE -> RecurrenceRange.of(value)

                    PARAMETER_RELATED -> Related.of(value)

                    PARAMETER_RELTYPE -> RelType.of(value)

                    PARAMETER_ROLE -> Role.of(value)

                    else -> ValueType.of(value)
                }
            }
        }
        return Decoded.Value(decoded as T, emptyList())
    }

    @Suppress("UNCHECKED_CAST")
    override fun encode(value: T, context: EncodeContext): ICalParameter = when (kind) {
        PARAMETER_LIST -> ICalParameter.of(context.name, *(value as List<String>).toTypedArray())
        PARAMETER_BOOLEAN -> ICalParameter.of(context.name, if (value as Boolean) "TRUE" else "FALSE")
        else -> ICalParameter.of(context.name, value.toString())
    }

    override fun toString(): String = "StandardParameterCodec($kind)"
}

private fun malformed(property: ICalProperty, parameter: ICalParameter, context: DecodeContext): Decoded<Nothing> =
    Decoded.Malformed(context.problem(property, parameter, ProblemCode.InvalidValue))
