@file:OptIn(ExperimentalObjCName::class)

package io.github.calendrum.model

import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.describe
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.encodeDuration
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

/**
 * The value of a `LOCATION` property: the place as text
 * ([RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)).
 *
 * Build one with `Location("Room 101")`. Immutable.
 *
 * @property text the location text, unescaped.
 * @property altRep the `ALTREP` parameter, a URI with a richer description of the place, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones, so that writing keeps them.
 */
public class Location internal constructor(
    public val text: String,
    public val altRep: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(text: String) : this(text, null, null, emptyList())

    /** Returns `true` when [other] is a Location equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Location && text == other.text && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = text.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Location", "parameters", parameters.size)
}

/**
 * The value of a `CONTACT` property: contact information as text
 * ([RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)).
 *
 * Build one with `Contact("Jane, +1 555 0100")`. Immutable.
 *
 * @property text the contact text, unescaped.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Contact internal constructor(
    public val text: String,
    public val altRep: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(text: String) : this(text, null, null, emptyList())

    /** Returns `true` when [other] is a Contact equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Contact && text == other.text && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = text.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Contact", "parameters", parameters.size)
}

/**
 * The value of a `CATEGORIES` property: one or more category names
 * ([RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)).
 *
 * Build one with `Categories(listOf("Work", "Weekly"))`; the list must not be empty. Immutable.
 *
 * @property values the category names, unescaped.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Categories internal constructor(
    public val values: List<String>,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(values: List<String>) : this(nonEmptyTexts(values), null, emptyList())

    /** Returns `true` when [other] is a Categories equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Categories && values == other.values && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = values.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Categories", "values", values.size)
}

/**
 * The value of a `RESOURCES` property: one or more resources needed for an activity, as text
 * ([RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)).
 *
 * Build one with `Resources(listOf("Projector"))`; the list must not be empty. Immutable.
 *
 * @property values the resource names, unescaped.
 * @property altRep the `ALTREP` parameter, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class Resources internal constructor(
    public val values: List<String>,
    public val altRep: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(values: List<String>) : this(nonEmptyTexts(values), null, null, emptyList())

    /** Returns `true` when [other] is a Resources equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Resources && values == other.values && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = values.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Resources", "values", values.size)
}

/**
 * The value of a `RELATED-TO` property: a relationship to another component, by its `UID`
 * ([RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5), with the relation types of
 * [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253)).
 *
 * Build one with `RelatedTo(uid)` (a parent) or with a [RelType] and, for `DEPENDS-ON`, a gap. Immutable.
 *
 * @property uid the `UID` (or, for `VALUE=URI`, the URI) of the related component.
 * @property relType the relation, [RelType.Parent] by default.
 * @property gap the `GAP` of RFC 9253 between the components, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class RelatedTo internal constructor(
    public val uid: String,
    public val relType: RelType,
    public val gap: ICalDuration?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(uid: String) : this(uid, RelType.Parent, null, emptyList())

    public constructor(uid: String, relType: RelType) :
        this(uid, relType, null, listOf(ICalParameter.of(RELTYPE, relType.value)))

    public constructor(uid: String, relType: RelType, gap: ICalDuration) : this(
        uid,
        relType,
        gap,
        listOf(ICalParameter.of(RELTYPE, relType.value), ICalParameter.of(GAP, encodeDuration(gap.value))),
    )

    /** Returns `true` when [other] is a RelatedTo equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is RelatedTo && uid == other.uid && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = uid.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "RelatedTo($relType)"
}

/**
 * The value of a `REQUEST-STATUS` property: the status code of a scheduling request, a description and optional data
 * ([RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)), such as `2.0;Success`.
 *
 * @property code the status code: digits separated by dots, such as `2.0`.
 * @property description the human-readable description, unescaped.
 * @property data extra data, such as the offending property, or `null`.
 * @property language the `LANGUAGE` parameter, or `null`.
 * @property parameters all parameters of the property as written, including unknown ones.
 */
public class RequestStatus internal constructor(
    public val code: String,
    @property:ObjCName("descriptionText")
    public val description: String,
    public val data: String?,
    public val language: String?,
    public val parameters: List<ICalParameter>,
) {
    public constructor(
        code: String,
        description: String,
    ) : this(checkedStatus(code), description, null, null, emptyList())

    public constructor(code: String, description: String, data: String) :
        this(checkedStatus(code), description, data, null, emptyList())

    /** Returns `true` when [other] is a RequestStatus equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean = this === other ||
        (
            other is RequestStatus && code == other.code && description == other.description &&
                data == other.data && parameters == other.parameters
            )

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = ((code.hashCode() * 31 + description.hashCode()) * 31 + data.hashCode()) * 31 +
        parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "RequestStatus($code)"
}
