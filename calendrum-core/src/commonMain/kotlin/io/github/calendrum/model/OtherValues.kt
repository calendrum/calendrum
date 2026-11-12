package io.github.calendrum.model

import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.decimalText

/**
 * The value of an `ATTACH` property: a document by reference ([Uri]) or inline ([Binary])
 * ([RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)).
 */
public sealed interface Attach {
    /** The media type of the attachment (`FMTTYPE`), or `null`. */
    public val formatType: String?

    /** All parameters of the property as written, including unknown ones. */
    public val parameters: List<ICalParameter>

    /**
     * An attachment given by a URI.
     *
     * Build one with `Attach.Uri("https://example.com/agenda.pdf")`.
     *
     * @property uri the location of the document.
     */
    public class Uri internal constructor(
        public val uri: String,
        override val formatType: String?,
        override val parameters: List<ICalParameter>,
    ) : Attach {
        public constructor(uri: String) : this(uri, null, emptyList())

        public constructor(uri: String, formatType: String) :
            this(uri, formatType, listOf(ICalParameter.of(FMTTYPE, formatType)))

        /** Returns `true` when [other] is a Uri equal to this one, comparing their content. */
        override fun equals(other: Any?): Boolean =
            this === other || (other is Uri && uri == other.uri && parameters == other.parameters)

        /** Returns a hash code that agrees with [equals]. */
        override fun hashCode(): Int = uri.hashCode() * 31 + parameters.hashCode()

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("Attach.Uri", "parameters", parameters.size)
    }

    /**
     * An attachment whose content is inline, base64 encoded in the text form.
     *
     * Build one with `Attach.Binary(bytes)`; the bytes are copied.
     */
    public class Binary internal constructor(
        private val content: ByteArray,
        override val formatType: String?,
        override val parameters: List<ICalParameter>,
    ) : Attach {
        public constructor(bytes: ByteArray) : this(bytes.copyOf(), null, binaryParameters(null))

        public constructor(bytes: ByteArray, formatType: String) :
            this(bytes.copyOf(), formatType, binaryParameters(formatType))

        /** The length of the content in bytes. */
        public val size: Int get() = content.size

        /** Returns a copy of the content. */
        public fun bytes(): ByteArray = content.copyOf()

        internal val bytesView: ByteArray get() = content

        /** Returns `true` when [other] is a Binary equal to this one, comparing their content. */
        override fun equals(other: Any?): Boolean = this === other ||
            (other is Binary && content.contentEquals(other.content) && parameters == other.parameters)

        /** Returns a hash code that agrees with [equals]. */
        override fun hashCode(): Int = content.contentHashCode() * 31 + parameters.hashCode()

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("Attach.Binary", "size", content.size)
    }
}

/**
 * The value of a `GEO` property: a latitude and a longitude
 * ([RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)).
 *
 * Build one with `Geo(52.52, 13.405)`. Equality compares the decimal text, so a value written back is not changed by
 * rounding.
 *
 * @property latitude the latitude in degrees, from -90 to 90.
 * @property longitude the longitude in degrees, from -180 to 180.
 * @property parameters all parameters of the property as written, including unknown ones.
 * @throws IllegalArgumentException when constructed with a coordinate out of range.
 */
public class Geo internal constructor(
    public val latitude: Double,
    public val longitude: Double,
    internal val latitudeText: String,
    internal val longitudeText: String,
    public val parameters: List<ICalParameter>,
) {
    public constructor(latitude: Double, longitude: Double) : this(
        checkedRange(latitude, LATITUDE_LIMIT),
        checkedRange(longitude, LONGITUDE_LIMIT),
        decimalText(latitude),
        decimalText(longitude),
        emptyList(),
    )

    /** Returns `true` when [other] is a Geo equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is Geo && latitudeText == other.latitudeText && longitudeText == other.longitudeText &&
                parameters == other.parameters
            )

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int =
        (latitudeText.hashCode() * 31 + longitudeText.hashCode()) * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Geo"
}
