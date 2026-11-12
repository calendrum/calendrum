package io.github.calendrum.model

import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.describe

/**
 * The value of an `IMAGE` property: a picture of the component, by URI or inline
 * ([RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)).
 *
 * Build one with `Image("https://example.com/logo.png")` or from bytes and a media type. Immutable.
 *
 * @property uri the location of the image, or `null` for an inline image.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property display how the image is meant to be shown (`DISPLAY`); [ImageDisplay.Badge] by default.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Image internal constructor(
    public val uri: String?,
    private val content: ByteArray?,
    public val formatType: String?,
    public val display: List<ImageDisplay>,
    public val altRep: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(uri: String) : this(uri, null, null, defaultDisplay(), null, uriParameters(null))

    public constructor(uri: String, formatType: String) :
        this(uri, null, formatType, defaultDisplay(), null, uriParameters(formatType))

    public constructor(bytes: ByteArray, formatType: String) :
        this(null, bytes.copyOf(), formatType, defaultDisplay(), null, binaryParameters(formatType))

    /** Whether the image is inline (base64 encoded in the text form) and not given by a URI. */
    public val isBinary: Boolean get() = content != null

    /** Returns a copy of the inline content, or `null` when the image is given by a URI. */
    public fun bytes(): ByteArray? = content?.copyOf()

    internal val bytesView: ByteArray? get() = content

    /** Returns `true` when [other] is a Image equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is Image && uri == other.uri && sameBytes(content, other.content) &&
                parameters == other.parameters
            )

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int =
        (uri.hashCode() * 31 + (content?.contentHashCode() ?: 0)) * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Image", "binary", isBinary, "parameters", parameters.size)
}

/**
 * The value of a `CONFERENCE` property: a way to join an online or phone meeting
 * ([RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)).
 *
 * Build one with `Conference("https://meet.example.com/abc")`. Immutable.
 *
 * @property uri the address to join, such as an https or tel URI.
 * @property features what the conference offers (`FEATURE`).
 * @property label a human-readable label (`LABEL`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Conference internal constructor(
    public val uri: String,
    public val features: List<ConferenceFeature>,
    public val label: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(uri: String) : this(uri, emptyList(), null, null, conferenceParameters(emptyList(), null))

    public constructor(uri: String, features: List<ConferenceFeature>, label: String?) :
        this(uri, frozenCopy(features), label, null, conferenceParameters(features, label))

    /** Returns `true` when [other] is a Conference equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Conference && uri == other.uri && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = uri.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Conference", "features", features.size)
}

/**
 * The value of a `STYLED-DESCRIPTION` property: a description in a rich format, such as HTML, as text or by URI
 * ([RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)).
 *
 * The markup is untrusted input: sanitize it before showing it. Immutable.
 *
 * @property value the markup or the URI.
 * @property valueType [ValueType.Text] or [ValueType.Uri].
 * @property formatType the media type (`FMTTYPE`), such as `text/html`, or `null`.
 * @property isDerived whether the text is derived from the plain `DESCRIPTION` (`DERIVED`).
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class StyledDescription internal constructor(
    public val value: String,
    public val valueType: ValueType,
    public val formatType: String?,
    public val isDerived: Boolean,
    public val altRep: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(value: String, valueType: ValueType) :
        this(value, textOrUri(valueType), null, false, null, null, typedParameters(valueType, null, null))

    public constructor(value: String, valueType: ValueType, formatType: String) :
        this(value, textOrUri(valueType), formatType, false, null, null, typedParameters(valueType, formatType, null))

    /** Returns `true` when [other] is a StyledDescription equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is StyledDescription && value == other.value && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = value.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("StyledDescription", "valueType", valueType, "formatType", formatType)
}

/**
 * The value of a `STRUCTURED-DATA` property: machine-readable data about the component, inline or by URI
 * ([RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)).
 *
 * @property valueType [ValueType.Text], [ValueType.Uri] or [ValueType.Binary].
 * @property text the text or the URI, or `null` for binary data.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property schema the schema URI (`SCHEMA`), or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class StructuredData internal constructor(
    public val valueType: ValueType,
    public val text: String?,
    private val content: ByteArray?,
    public val formatType: String?,
    public val schema: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(value: String, valueType: ValueType) :
        this(textOrUri(valueType), value, null, null, null, typedParameters(valueType, null, null))

    public constructor(value: String, valueType: ValueType, formatType: String, schema: String) :
        this(textOrUri(valueType), value, null, formatType, schema, typedParameters(valueType, formatType, schema))

    public constructor(bytes: ByteArray, formatType: String, schema: String) : this(
        ValueType.Binary,
        null,
        bytes.copyOf(),
        formatType,
        schema,
        binaryParameters(formatType) + ICalParameter.of(SCHEMA, schema),
    )

    /** Whether the data is binary (base64 encoded in the text form). */
    public val isBinary: Boolean get() = content != null

    /** Returns a copy of the binary content, or `null` when the data is text or a URI. */
    public fun bytes(): ByteArray? = content?.copyOf()

    internal val bytesView: ByteArray? get() = content

    /** Returns `true` when [other] is a StructuredData equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is StructuredData && text == other.text && sameBytes(content, other.content) &&
                parameters == other.parameters
            )

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int =
        (text.hashCode() * 31 + (content?.contentHashCode() ?: 0)) * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("StructuredData", "valueType", valueType, "formatType", formatType)
}

/**
 * The value of a `LINK` property: a typed link to another resource
 * ([RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)).
 *
 * @property value the URI, `UID` or XML reference.
 * @property valueType [ValueType.Uri], [ValueType.Uid] or [ValueType.XmlReference].
 * @property linkRel the relation type (`LINKREL`), usually a URI or IANA link relation name, or `null`.
 * @property formatType the media type (`FMTTYPE`), or `null`.
 * @property label a human-readable label (`LABEL`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Link internal constructor(
    public val value: String,
    public val valueType: ValueType,
    public val linkRel: String?,
    public val formatType: String?,
    public val label: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(value: String, valueType: ValueType, linkRel: String) : this(
        value,
        linkType(valueType),
        linkRel,
        null,
        null,
        null,
        listOf(ICalParameter.of(VALUE, valueType.value), ICalParameter.of(LINKREL, linkRel)),
    )

    /** Returns `true` when [other] is a Link equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Link && value == other.value && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = value.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Link", "valueType", valueType, "linkRel", linkRel)
}

internal fun defaultDisplay(): List<ImageDisplay> = listOf(ImageDisplay.Badge)

private fun uriParameters(formatType: String?): List<ICalParameter> {
    val value = ICalParameter.of(VALUE, "URI")
    return if (formatType == null) listOf(value) else listOf(value, ICalParameter.of(FMTTYPE, formatType))
}

private fun conferenceParameters(features: List<ConferenceFeature>, label: String?): List<ICalParameter> {
    val result = ArrayList<ICalParameter>(3)
    result += ICalParameter.of(VALUE, "URI")
    if (features.isNotEmpty()) result += ICalParameter.of(FEATURE, *Array(features.size) { features[it].value })
    if (label != null) result += ICalParameter.of(LABEL, label)
    return frozenCopy(result)
}

private fun typedParameters(valueType: ValueType, formatType: String?, schema: String?): List<ICalParameter> {
    val result = ArrayList<ICalParameter>(3)
    result += ICalParameter.of(VALUE, valueType.value)
    if (formatType != null) result += ICalParameter.of(FMTTYPE, formatType)
    if (schema != null) result += ICalParameter.of(SCHEMA, schema)
    return frozenCopy(result)
}

private fun textOrUri(valueType: ValueType): ValueType {
    require(valueType == ValueType.Text || valueType == ValueType.Uri) { "The value type is TEXT or URI" }
    return valueType
}

private fun linkType(valueType: ValueType): ValueType {
    require(valueType == ValueType.Uri || valueType == ValueType.Uid || valueType == ValueType.XmlReference) {
        "The value type of a LINK is URI, UID or XML-REFERENCE"
    }
    return valueType
}

private fun sameBytes(left: ByteArray?, right: ByteArray?): Boolean =
    if (left == null || right == null) left === right else left.contentEquals(right)
