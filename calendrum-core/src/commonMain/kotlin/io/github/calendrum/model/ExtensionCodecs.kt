package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.BinaryValue
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ValueDecoder
import io.github.calendrum.value.decodeBinary
import io.github.calendrum.value.decodeDuration
import io.github.calendrum.value.encodeBinary
import io.github.calendrum.value.encodeDuration
import io.github.calendrum.value.encodeText
import io.github.calendrum.value.encodeTextList

internal class CalAddressCodec : StandardCodec<String>() {
    override fun read(run: CodecRun): String? =
        if (run.valueType(TYPE_CAL_ADDRESS, TYPE_CAL_ADDRESS) == REJECTED) null else run.uri()

    override fun encode(value: String, context: EncodeContext): ICalProperty = ICalProperty.of(context.name, value)
}

internal class TextListCodec : StandardCodec<List<String>>() {
    override fun read(run: CodecRun): List<String>? = run.textList()

    override fun encode(value: List<String>, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeTextList(nonEmptyTexts(value)))
}

internal class DeclaredDurationCodec : StandardCodec<ICalDuration>() {
    override fun read(run: CodecRun): ICalDuration? = run.duration()

    override fun encode(value: ICalDuration, context: EncodeContext): ICalProperty =
        ICalProperty.builder(context.name).parameter(VALUE, "DURATION").value(encodeDuration(value.value)).build()
}

internal class BooleanTextCodec : StandardCodec<Boolean>() {
    override fun read(run: CodecRun): Boolean? {
        if (run.valueType(TYPE_TEXT or TYPE_BOOLEAN, TYPE_TEXT) == REJECTED) return null
        val value = run.text() ?: return null
        return when (if (value.isEmpty()) "" else enumToken(value)) {
            "TRUE" -> true

            "FALSE" -> false

            else -> {
                run.report(ProblemCode.InvalidValue, 0)
                null
            }
        }
    }

    override fun encode(value: Boolean, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, if (value) "TRUE" else "FALSE")
}

internal class ParticipantTypeCodec : TokenCodec<ParticipantType>() {
    override fun of(value: String): ParticipantType = ParticipantType.of(value)
}

internal class ResourceTypeCodec : TokenCodec<ResourceType>() {
    override fun of(value: String): ResourceType = ResourceType.of(value)
}

internal class ProximityCodec : TokenCodec<Proximity>() {
    override fun of(value: String): Proximity = Proximity.of(value)
}

internal class BusyStatusCodec : TokenCodec<BusyStatus>() {
    override fun of(value: String): BusyStatus = BusyStatus.of(value)
}

internal class ImageCodec : StandardCodec<Image>() {
    override fun read(run: CodecRun): Image? {
        val type = run.valueType(TYPE_URI or TYPE_BINARY, TYPE_URI)
        if (type == REJECTED) return null
        val formatType = run.text(FMTTYPE)
        val display = run.tokens(DISPLAY) { ImageDisplay.of(it) }.ifEmpty { defaultDisplay() }
        val altRep = run.text(ALTREP)
        if (!run.isBinary(type)) {
            val uri = run.uri() ?: return null
            return Image(uri, null, formatType, display, altRep, run.parameters())
        }
        val bytes = run.binary() ?: return null
        return Image(null, bytes, formatType, display, altRep, run.parameters())
    }

    override fun encode(value: Image, context: EncodeContext): ICalProperty {
        val bytes = value.bytesView
        val text = if (bytes == null) value.uri.orEmpty() else encodeBinary(BinaryValue(bytes))
        return withParameters(context.name, value.parameters, text)
    }
}

internal class ConferenceCodec : StandardCodec<Conference>() {
    override fun read(run: CodecRun): Conference? {
        if (run.valueType(TYPE_URI, TYPE_URI) == REJECTED) return null
        val uri = run.uri() ?: return null
        val features = run.tokens(FEATURE) { ConferenceFeature.of(it) }
        return Conference(uri, features, run.text(LABEL), run.text(LANGUAGE), run.parameters())
    }

    override fun encode(value: Conference, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, value.uri)
}

internal class StyledDescriptionCodec : StandardCodec<StyledDescription>() {
    override fun read(run: CodecRun): StyledDescription? {
        val type = run.valueType(TYPE_TEXT or TYPE_URI, TYPE_TEXT)
        if (type == REJECTED) return null
        val uri = type == TYPE_URI
        val value = (if (uri) run.uri() else run.text()) ?: return null
        return StyledDescription(
            value,
            if (uri) ValueType.Uri else ValueType.Text,
            run.text(FMTTYPE),
            run.flag(DERIVED),
            run.text(ALTREP),
            run.text(LANGUAGE),
            run.parameters(),
        )
    }

    override fun encode(value: StyledDescription, context: EncodeContext): ICalProperty {
        val text = if (value.valueType == ValueType.Uri) value.value else encodeText(value.value)
        return withParameters(context.name, value.parameters, text)
    }
}

internal class StructuredDataCodec : StandardCodec<StructuredData>() {
    override fun read(run: CodecRun): StructuredData? {
        val type = run.valueType(TYPE_TEXT or TYPE_BINARY or TYPE_URI, TYPE_TEXT)
        if (type == REJECTED) return null
        val formatType = run.text(FMTTYPE)
        val schema = run.text(SCHEMA)
        if (run.isBinary(type)) {
            val bytes = run.binary() ?: return null
            return StructuredData(ValueType.Binary, null, bytes, formatType, schema, run.parameters())
        }
        val uri = type == TYPE_URI
        val value = (if (uri) run.uri() else run.text()) ?: return null
        val valueType = if (uri) ValueType.Uri else ValueType.Text
        return StructuredData(valueType, value, null, formatType, schema, run.parameters())
    }

    override fun encode(value: StructuredData, context: EncodeContext): ICalProperty {
        val bytes = value.bytesView
        val text = when {
            bytes != null -> encodeBinary(BinaryValue(bytes))
            value.valueType == ValueType.Uri -> value.text.orEmpty()
            else -> encodeText(value.text.orEmpty())
        }
        return withParameters(context.name, value.parameters, text)
    }
}

internal class LinkCodec : StandardCodec<Link>() {
    override fun read(run: CodecRun): Link? {
        val type = run.valueType(TYPE_URI or TYPE_UID or TYPE_XML_REFERENCE, TYPE_URI)
        if (type == REJECTED) return null
        val value = (if (type == TYPE_UID) run.text() else run.uri()) ?: return null
        val valueType = when (type) {
            TYPE_UID -> ValueType.Uid
            TYPE_XML_REFERENCE -> ValueType.XmlReference
            else -> ValueType.Uri
        }
        return Link(
            value,
            valueType,
            run.text(LINKREL),
            run.text(FMTTYPE),
            run.text(LABEL),
            run.text(LANGUAGE),
            run.parameters(),
        )
    }

    override fun encode(value: Link, context: EncodeContext): ICalProperty {
        val text = if (value.valueType == ValueType.Uid) encodeText(value.value) else value.value
        return withParameters(context.name, value.parameters, text)
    }
}

private fun CodecRun.isBinary(type: Int): Boolean {
    if (type == TYPE_BINARY) return true
    return declared() == NOT_DECLARED && text(ENCODING)?.let { it.isNotEmpty() && enumToken(it) == "BASE64" } == true
}

private fun CodecRun.binary(): ByteArray? {
    if (declared() != TYPE_BINARY && !accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
    val encoding = text(ENCODING)
    val base64 = encoding != null && encoding.isNotEmpty() && enumToken(encoding) == "BASE64"
    if (!base64 && !accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
    val value = this.value
    return decoder.decodeBinary(value, 0, value.length)?.bytes
}

private inline fun <E : Any> CodecRun.tokens(name: String, of: (String) -> E): List<E> {
    val parameter = parameter(name) ?: return emptyList()
    val values = parameter.values
    val result = arrayOfNulls<Any>(values.size)
    var count = 0
    for (value in values) {
        if (value.isEmpty()) {
            reportParameter(ProblemCode.InvalidValue, parameter)
            continue
        }
        result[count++] = of(value)
    }
    if (count == 0) return emptyList()
    @Suppress("UNCHECKED_CAST")
    return ArrayView(result.copyOf(count) as Array<E>)
}

@OptIn(ExperimentalICalApi::class)
internal class OrderParameterCodec : ParameterCodec<Int> {
    override fun decode(property: ICalProperty, parameter: ICalParameter, context: DecodeContext): Decoded<Int> {
        val value = parameter.value
        var result = 0L
        if (value.isEmpty() || value.length > MAX_ORDER_DIGITS) return malformedParameter(property, parameter, context)
        for (char in value) {
            if (char !in '0'..'9') return malformedParameter(property, parameter, context)
            result = result * DECIMAL + (char - '0')
        }
        if (result < 1 || result > Int.MAX_VALUE) return malformedParameter(property, parameter, context)
        return Decoded.Value(result.toInt(), emptyList())
    }

    override fun encode(value: Int, context: EncodeContext): ICalParameter {
        require(value >= 1) { "ORDER is at least 1" }
        return ICalParameter.of(context.name, value.toString())
    }

    override fun toString(): String = "OrderParameterCodec"
}

@OptIn(ExperimentalICalApi::class)
internal class DurationParameterCodec : ParameterCodec<ICalDuration> {
    override fun decode(
        property: ICalProperty,
        parameter: ICalParameter,
        context: DecodeContext,
    ): Decoded<ICalDuration> {
        val value = parameter.value
        val parsed = ValueDecoder(context.isStrict, false, null).decodeDuration(value, 0, value.length)
            ?: return malformedParameter(property, parameter, context)
        return Decoded.Value(ICalDuration(parsed), emptyList())
    }

    override fun encode(value: ICalDuration, context: EncodeContext): ICalParameter =
        ICalParameter.of(context.name, encodeDuration(value.value))

    override fun toString(): String = "DurationParameterCodec"
}

@OptIn(ExperimentalICalApi::class)
internal class TokenListParameterCodec<E : Any>(private val of: (String) -> E) : ParameterCodec<List<E>> {
    override fun decode(property: ICalProperty, parameter: ICalParameter, context: DecodeContext): Decoded<List<E>> {
        val values = parameter.values
        val result = ArrayList<E>(values.size)
        for (value in values) {
            if (value.isEmpty()) return malformedParameter(property, parameter, context)
            result += of(value)
        }
        return Decoded.Value(frozenCopy(result), emptyList())
    }

    override fun encode(value: List<E>, context: EncodeContext): ICalParameter {
        require(value.isNotEmpty()) { "A parameter list is not empty" }
        return ICalParameter.of(context.name, *Array(value.size) { value[it].toString() })
    }

    override fun toString(): String = "TokenListParameterCodec"
}

private fun malformedParameter(
    property: ICalProperty,
    parameter: ICalParameter,
    context: DecodeContext,
): Decoded<Nothing> = Decoded.Malformed(context.problem(property, parameter, ProblemCode.InvalidValue))

private const val MAX_ORDER_DIGITS = 10
private const val DECIMAL = 10
