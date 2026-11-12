package io.github.calendrum.model

import io.github.calendrum.ICalDsl
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.canonicalName
import io.github.calendrum.syntax.describe
import kotlin.jvm.JvmStatic

/**
 * The value of an `ATTENDEE` property: a calendar user taking part, with the parameters that describe their role and
 * answer ([RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)).
 *
 * The typed fields are views of the parameters; [parameters] keeps all of them, unknown ones included, so a value
 * written back is not changed. Build one with [builder], change one with [toBuilder]. Immutable.
 *
 * @property address the calendar address, usually `mailto:jane@example.com`.
 * @property parameters all parameters as written.
 * @property cn the common name (`CN`), or `null`.
 * @property role the role (`ROLE`); [Role.ReqParticipant] when absent.
 * @property partStat the participation status (`PARTSTAT`); [PartStat.NeedsAction] when absent.
 * @property rsvp whether a reply is requested (`RSVP`); false when absent.
 * @property cuType the calendar user type (`CUTYPE`); [CuType.Individual] when absent.
 * @property delegatedTo the addresses the attendance was delegated to (`DELEGATED-TO`).
 * @property delegatedFrom the addresses that delegated their attendance to this user (`DELEGATED-FROM`).
 * @property member the groups the user is a member of (`MEMBER`).
 * @property sentBy the address that acts on behalf of the user (`SENT-BY`), or `null`.
 * @property dir the directory entry URI (`DIR`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property email the e-mail address of [RFC 7986](https://www.rfc-editor.org/rfc/rfc7986#section-6.2) (`EMAIL`), or
 *   `null`.
 */
public class Attendee internal constructor(
    public val address: String,
    public val parameters: List<ICalParameter>,
    public val cn: String?,
    public val role: Role,
    public val partStat: PartStat,
    public val rsvp: Boolean,
    public val cuType: CuType,
    public val delegatedTo: List<String>,
    public val delegatedFrom: List<String>,
    public val member: List<String>,
    public val sentBy: String?,
    public val dir: String?,
    public val language: String?,
    public val email: String?,
) {
    /** Returns a builder that starts from this attendee, parameters included. */
    public fun toBuilder(): Builder = Builder(address, parameters)

    /** Returns `true` when [other] is a Attendee equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Attendee && address == other.address && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = address.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Attendee", "parameters", parameters.size)

    /**
     * Collects the address and the parameters of an [Attendee]. The properties and the functions of the same name do
     * the same; the functions chain. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(address: String, parameters: List<ICalParameter>) {
        private val parameters = ArrayList(parameters)

        /** The calendar address. */
        public var address: String = address

        /** The common name (`CN`); assigning `null` removes the parameter. */
        public var cn: String?
            get() = parameters.valueOf(CN)
            set(value) {
                parameters.put(CN, value)
            }

        /** The role (`ROLE`); [Role.ReqParticipant] when absent. */
        public var role: Role
            get() = parameters.tokenOf(ROLE)?.let { Role.of(it) } ?: Role.ReqParticipant
            set(value) {
                parameters.put(ROLE, value.value)
            }

        /** The participation status (`PARTSTAT`); [PartStat.NeedsAction] when absent. */
        public var partStat: PartStat
            get() = parameters.tokenOf(PARTSTAT)?.let { PartStat.of(it) } ?: PartStat.NeedsAction
            set(value) {
                parameters.put(PARTSTAT, value.value)
            }

        /** Whether a reply is requested (`RSVP`); false when absent. */
        public var rsvp: Boolean
            get() = parameters.tokenOf(RSVP)?.let { enumToken(it) == TRUE } ?: false
            set(value) {
                parameters.put(RSVP, if (value) TRUE else FALSE)
            }

        /** The calendar user type (`CUTYPE`); [CuType.Individual] when absent. */
        public var cuType: CuType
            get() = parameters.tokenOf(CUTYPE)?.let { CuType.of(it) } ?: CuType.Individual
            set(value) {
                parameters.put(CUTYPE, value.value)
            }

        /** The addresses the attendance was delegated to (`DELEGATED-TO`); an empty list removes the parameter. */
        public var delegatedTo: List<String>
            get() = parameters.valuesOf(DELEGATED_TO)
            set(value) {
                parameters.putAll(DELEGATED_TO, value)
            }

        /**
         * The addresses that delegated their attendance to this user (`DELEGATED-FROM`); an empty list removes the
         * parameter.
         */
        public var delegatedFrom: List<String>
            get() = parameters.valuesOf(DELEGATED_FROM)
            set(value) {
                parameters.putAll(DELEGATED_FROM, value)
            }

        /** The groups the user is a member of (`MEMBER`); an empty list removes the parameter. */
        public var member: List<String>
            get() = parameters.valuesOf(MEMBER)
            set(value) {
                parameters.putAll(MEMBER, value)
            }

        /** The address that acts on behalf of the user (`SENT-BY`); assigning `null` removes the parameter. */
        public var sentBy: String?
            get() = parameters.valueOf(SENT_BY)
            set(value) {
                parameters.put(SENT_BY, value)
            }

        /** The directory entry URI (`DIR`); assigning `null` removes the parameter. */
        public var dir: String?
            get() = parameters.valueOf(DIR)
            set(value) {
                parameters.put(DIR, value)
            }

        /** The `LANGUAGE`; assigning `null` removes the parameter. */
        public var language: String?
            get() = parameters.valueOf(LANGUAGE)
            set(value) {
                parameters.put(LANGUAGE, value)
            }

        /** The `EMAIL` address of RFC 7986; assigning `null` removes the parameter. */
        public var email: String?
            get() = parameters.valueOf(EMAIL)
            set(value) {
                parameters.put(EMAIL, value)
            }

        /** Sets [address] to [value] and returns this builder. */
        public fun address(value: String): Builder = apply { address = value }

        /** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
        public fun cn(value: String?): Builder = apply { cn = value }

        /** Sets [role] to [value] and returns this builder. */
        public fun role(value: Role): Builder = apply { role = value }

        /** Sets [partStat] to [value] and returns this builder. */
        public fun partStat(value: PartStat): Builder = apply { partStat = value }

        /** Sets [rsvp] to [value] and returns this builder. */
        public fun rsvp(value: Boolean): Builder = apply { rsvp = value }

        /** Sets [cuType] to [value] and returns this builder. */
        public fun cuType(value: CuType): Builder = apply { cuType = value }

        /** Sets [delegatedTo] to [values] and returns this builder. */
        public fun delegatedTo(vararg values: String): Builder = apply { delegatedTo = values.asList() }

        /** Sets [delegatedFrom] to [values] and returns this builder. */
        public fun delegatedFrom(vararg values: String): Builder = apply { delegatedFrom = values.asList() }

        /** Sets [member] to [values] and returns this builder. */
        public fun member(vararg values: String): Builder = apply { member = values.asList() }

        /** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
        public fun sentBy(value: String?): Builder = apply { sentBy = value }

        /** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
        public fun dir(value: String?): Builder = apply { dir = value }

        /** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
        public fun language(value: String?): Builder = apply { language = value }

        /** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
        public fun email(value: String?): Builder = apply { email = value }

        /**
         * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
         * typed field.
         */
        public fun parameter(name: String, vararg values: String): Builder =
            apply { parameters.putAll(name, values.asList()) }

        /** Removes the parameter [name]. */
        public fun removeParameter(name: String): Builder = apply { parameters.putAll(name, emptyList()) }

        internal fun toProperty(): ICalProperty = participant(ATTENDEE, parameters, address)

        /**
         * Creates the attendee.
         *
         * @throws IllegalArgumentException if the address is not a valid calendar address.
         */
        public fun build(): Attendee {
            val decoded = AttendeeCodec().decode(toProperty(), DecodeContext.Lenient)
            require(decoded is Decoded.Value) { "Invalid attendee address" }
            return decoded.value
        }

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("Attendee.Builder", "parameters", parameters.size)
    }

    /** Creates builders for [Attendee]. */
    public companion object {
        /** Returns a builder for an attendee with the given calendar [address]. */
        @JvmStatic
        public fun builder(address: String): Builder = Builder(address, emptyList())
    }
}

/**
 * The value of an `ORGANIZER` property: the calendar user who organizes the event
 * ([RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)).
 *
 * [parameters] keeps all parameters, unknown ones included. Build one with [builder], change one with [toBuilder].
 * Immutable.
 *
 * @property address the calendar address, usually `mailto:alice@example.com`.
 * @property parameters all parameters as written.
 * @property cn the common name (`CN`), or `null`.
 * @property sentBy the address that acts on behalf of the organizer (`SENT-BY`), or `null`.
 * @property dir the directory entry URI (`DIR`), or `null`.
 * @property language the `LANGUAGE`, or `null`.
 * @property email the e-mail address of RFC 7986 (`EMAIL`), or `null`.
 */
public class Organizer internal constructor(
    public val address: String,
    public val parameters: List<ICalParameter>,
    public val cn: String?,
    public val sentBy: String?,
    public val dir: String?,
    public val language: String?,
    public val email: String?,
) {
    /** Returns a builder that starts from this organizer, parameters included. */
    public fun toBuilder(): Builder = Builder(address, parameters)

    /** Returns `true` when [other] is a Organizer equal to this one, comparing their content. */
    override fun equals(other: Any?): Boolean =
        this === other || (other is Organizer && address == other.address && parameters == other.parameters)

    /** Returns a hash code that agrees with [equals]. */
    override fun hashCode(): Int = address.hashCode() * 31 + parameters.hashCode()

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = describe("Organizer", "parameters", parameters.size)

    /**
     * Collects the address and the parameters of an [Organizer]. The properties and the functions of the same name do
     * the same; the functions chain. Not thread-safe.
     */
    @ICalDsl
    public class Builder internal constructor(address: String, parameters: List<ICalParameter>) {
        private val parameters = ArrayList(parameters)

        /** The calendar address. */
        public var address: String = address

        /** The common name (`CN`); assigning `null` removes the parameter. */
        public var cn: String?
            get() = parameters.valueOf(CN)
            set(value) {
                parameters.put(CN, value)
            }

        /** The address that acts on behalf of the user (`SENT-BY`); assigning `null` removes the parameter. */
        public var sentBy: String?
            get() = parameters.valueOf(SENT_BY)
            set(value) {
                parameters.put(SENT_BY, value)
            }

        /** The directory entry URI (`DIR`); assigning `null` removes the parameter. */
        public var dir: String?
            get() = parameters.valueOf(DIR)
            set(value) {
                parameters.put(DIR, value)
            }

        /** The `LANGUAGE`; assigning `null` removes the parameter. */
        public var language: String?
            get() = parameters.valueOf(LANGUAGE)
            set(value) {
                parameters.put(LANGUAGE, value)
            }

        /** The `EMAIL` address of RFC 7986; assigning `null` removes the parameter. */
        public var email: String?
            get() = parameters.valueOf(EMAIL)
            set(value) {
                parameters.put(EMAIL, value)
            }

        /** Sets [address] to [value] and returns this builder. */
        public fun address(value: String): Builder = apply { address = value }

        /** Sets [cn] to [value] and returns this builder; `null` removes the parameter. */
        public fun cn(value: String?): Builder = apply { cn = value }

        /** Sets [sentBy] to [value] and returns this builder; `null` removes the parameter. */
        public fun sentBy(value: String?): Builder = apply { sentBy = value }

        /** Sets [dir] to [value] and returns this builder; `null` removes the parameter. */
        public fun dir(value: String?): Builder = apply { dir = value }

        /** Sets [language] to [value] and returns this builder; `null` removes the parameter. */
        public fun language(value: String?): Builder = apply { language = value }

        /** Sets [email] to [value] and returns this builder; `null` removes the parameter. */
        public fun email(value: String?): Builder = apply { email = value }

        /**
         * Sets the parameter [name] to [values], replacing it; no values remove it. Use it for parameters without a
         * typed field.
         */
        public fun parameter(name: String, vararg values: String): Builder =
            apply { parameters.putAll(name, values.asList()) }

        /** Removes the parameter [name]. */
        public fun removeParameter(name: String): Builder = apply { parameters.putAll(name, emptyList()) }

        /**
         * Creates the organizer.
         *
         * @throws IllegalArgumentException if the address is not a valid calendar address.
         */
        public fun build(): Organizer {
            val decoded = OrganizerCodec().decode(participant(ORGANIZER, parameters, address), DecodeContext.Lenient)
            require(decoded is Decoded.Value) { "Invalid organizer address" }
            return decoded.value
        }

        /** Returns a short description for diagnostics; its format is not a contract. */
        override fun toString(): String = describe("Organizer.Builder", "parameters", parameters.size)
    }

    /** Creates builders for [Organizer]. */
    public companion object {
        /** Returns a builder for an organizer with the given calendar [address]. */
        @JvmStatic
        public fun builder(address: String): Builder = Builder(address, emptyList())
    }
}

private fun participant(name: String, parameters: List<ICalParameter>, address: String): ICalProperty {
    val builder = ICalProperty.builder(name)
    for (parameter in parameters) builder.parameter(parameter)
    return builder.value(address).build()
}

internal fun List<ICalParameter>.valueOf(name: String): String? {
    for (parameter in this) {
        if (parameter.name == name) return parameter.value
    }
    return null
}

private fun List<ICalParameter>.tokenOf(name: String): String? = valueOf(name)?.ifEmpty { null }

private fun List<ICalParameter>.valuesOf(name: String): List<String> {
    for (parameter in this) {
        if (parameter.name == name) return parameter.values
    }
    return emptyList()
}

private fun ArrayList<ICalParameter>.put(name: String, value: String?) {
    putAll(name, if (value == null) emptyList() else listOf(value))
}

internal fun ArrayList<ICalParameter>.putAll(name: String, values: List<String>) {
    val canonical = canonicalName(name)
    var index = -1
    var other = size - 1
    while (other >= 0) {
        if (this[other].name == canonical) {
            if (index >= 0) removeAt(index)
            index = other
        }
        other -= 1
    }
    if (values.isEmpty()) {
        if (index >= 0) removeAt(index)
        return
    }
    val parameter = ICalParameter.of(canonical, *values.toTypedArray())
    if (index >= 0) this[index] = parameter else add(parameter)
}

private const val ATTENDEE = "ATTENDEE"
private const val ORGANIZER = "ORGANIZER"
private const val TRUE = "TRUE"
private const val FALSE = "FALSE"
