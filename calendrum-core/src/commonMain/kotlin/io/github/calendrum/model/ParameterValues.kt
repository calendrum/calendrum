package io.github.calendrum.model

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The participation status of a calendar user
 * ([RFC 5545 §3.2.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.12), `PARTSTAT`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class PartStat internal constructor(public val value: String) {
    /** Returns `true` when [other] is a PartStat with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is PartStat && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of PartStat and the lookup by token. */
    public companion object {
        /** `NEEDS-ACTION`. The user has not answered yet; the default. */
        @JvmField
        public val NeedsAction: PartStat = PartStat("NEEDS-ACTION")

        /** `ACCEPTED`. The user will attend. */
        @JvmField
        public val Accepted: PartStat = PartStat("ACCEPTED")

        /** `DECLINED`. The user will not attend. */
        @JvmField
        public val Declined: PartStat = PartStat("DECLINED")

        /** `TENTATIVE`. The user may attend. */
        @JvmField
        public val Tentative: PartStat = PartStat("TENTATIVE")

        /** `DELEGATED`. The user delegated attendance to someone else. */
        @JvmField
        public val Delegated: PartStat = PartStat("DELEGATED")

        /** `COMPLETED`. The user completed the task (to-dos). */
        @JvmField
        public val Completed: PartStat = PartStat("COMPLETED")

        /** `IN-PROCESS`. The user is working on the task (to-dos). */
        @JvmField
        public val InProcess: PartStat = PartStat("IN-PROCESS")

        /**
         * Returns the constant for [value], ignoring case, or a new PartStat holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): PartStat = when (val token = enumToken(value)) {
            "NEEDS-ACTION" -> NeedsAction
            "ACCEPTED" -> Accepted
            "DECLINED" -> Declined
            "TENTATIVE" -> Tentative
            "DELEGATED" -> Delegated
            "COMPLETED" -> Completed
            "IN-PROCESS" -> InProcess
            else -> PartStat(token)
        }
    }
}

/**
 * The role of a calendar user in an event ([RFC 5545 §3.2.16](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.16),
 * `ROLE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Role internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Role with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Role && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Role and the lookup by token. */
    public companion object {
        /** `CHAIR`. The user chairs the event. */
        @JvmField
        public val Chair: Role = Role("CHAIR")

        /** `REQ-PARTICIPANT`. A required participant; the default. */
        @JvmField
        public val ReqParticipant: Role = Role("REQ-PARTICIPANT")

        /** `OPT-PARTICIPANT`. An optional participant. */
        @JvmField
        public val OptParticipant: Role = Role("OPT-PARTICIPANT")

        /** `NON-PARTICIPANT`. Informed only; does not take part. */
        @JvmField
        public val NonParticipant: Role = Role("NON-PARTICIPANT")

        /**
         * Returns the constant for [value], ignoring case, or a new Role holding the upper-cased token when it is not
         * one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Role = when (val token = enumToken(value)) {
            "CHAIR" -> Chair
            "REQ-PARTICIPANT" -> ReqParticipant
            "OPT-PARTICIPANT" -> OptParticipant
            "NON-PARTICIPANT" -> NonParticipant
            else -> Role(token)
        }
    }
}

/**
 * The type of a calendar user ([RFC 5545 §3.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.3), `CUTYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class CuType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a CuType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is CuType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of CuType and the lookup by token. */
    public companion object {
        /** `INDIVIDUAL`. A person; the default. */
        @JvmField
        public val Individual: CuType = CuType("INDIVIDUAL")

        /** `GROUP`. A group of people. */
        @JvmField
        public val Group: CuType = CuType("GROUP")

        /** `RESOURCE`. A resource other than a room, such as a projector. */
        @JvmField
        public val Resource: CuType = CuType("RESOURCE")

        /** `ROOM`. A room. */
        @JvmField
        public val Room: CuType = CuType("ROOM")

        /** `UNKNOWN`. The type is not known. */
        @JvmField
        public val Unknown: CuType = CuType("UNKNOWN")

        /**
         * Returns the constant for [value], ignoring case, or a new CuType holding the upper-cased token when it is not
         * one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): CuType = when (val token = enumToken(value)) {
            "INDIVIDUAL" -> Individual
            "GROUP" -> Group
            "RESOURCE" -> Resource
            "ROOM" -> Room
            "UNKNOWN" -> Unknown
            else -> CuType(token)
        }
    }
}

/**
 * The type of a relationship between components
 * ([RFC 5545 §3.2.15](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.15), `RELTYPE`, with the values of
 * [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253) and [RFC 9074](https://www.rfc-editor.org/rfc/rfc9074)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class RelType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a RelType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is RelType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of RelType and the lookup by token. */
    public companion object {
        /** `PARENT`. The related component is the parent of this one; the default. */
        @JvmField
        public val Parent: RelType = RelType("PARENT")

        /** `CHILD`. The related component is a child of this one. */
        @JvmField
        public val Child: RelType = RelType("CHILD")

        /** `SIBLING`. The related component is a sibling of this one. */
        @JvmField
        public val Sibling: RelType = RelType("SIBLING")

        /** `SNOOZE`. This alarm is a snooze of the related alarm (RFC 9074). */
        @JvmField
        public val Snooze: RelType = RelType("SNOOZE")

        /** `FINISHTOSTART`. A finish-to-start dependency between the two components (RFC 9253). */
        @JvmField
        public val FinishToStart: RelType = RelType("FINISHTOSTART")

        /** `FINISHTOFINISH`. A finish-to-finish dependency between the two components (RFC 9253). */
        @JvmField
        public val FinishToFinish: RelType = RelType("FINISHTOFINISH")

        /** `STARTTOFINISH`. A start-to-finish dependency between the two components (RFC 9253). */
        @JvmField
        public val StartToFinish: RelType = RelType("STARTTOFINISH")

        /** `STARTTOSTART`. A start-to-start dependency between the two components (RFC 9253). */
        @JvmField
        public val StartToStart: RelType = RelType("STARTTOSTART")

        /** `FIRST`. The related component is the first of a series of components (RFC 9253). */
        @JvmField
        public val First: RelType = RelType("FIRST")

        /** `NEXT`. The related component is the next of a series of components (RFC 9253). */
        @JvmField
        public val Next: RelType = RelType("NEXT")

        /** `DEPENDS-ON`. This component depends on the related one (RFC 9253). */
        @JvmField
        public val DependsOn: RelType = RelType("DEPENDS-ON")

        /** `REFID`. The related value is a `REFID` reference identifier, not a `UID` (RFC 9253). */
        @JvmField
        public val RefId: RelType = RelType("REFID")

        /** `CONCEPT`. The related value is a `CONCEPT` URI, not a `UID` (RFC 9253). */
        @JvmField
        public val Concept: RelType = RelType("CONCEPT")

        /**
         * Returns the constant for [value], ignoring case, or a new RelType holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): RelType = when (val token = enumToken(value)) {
            "PARENT" -> Parent
            "CHILD" -> Child
            "SIBLING" -> Sibling
            "SNOOZE" -> Snooze
            "FINISHTOSTART" -> FinishToStart
            "FINISHTOFINISH" -> FinishToFinish
            "STARTTOFINISH" -> StartToFinish
            "STARTTOSTART" -> StartToStart
            "FIRST" -> First
            "NEXT" -> Next
            "DEPENDS-ON" -> DependsOn
            "REFID" -> RefId
            "CONCEPT" -> Concept
            else -> RelType(token)
        }
    }
}

/**
 * The free/busy type of a period ([RFC 5545 §3.2.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.9), `FBTYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class FbType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a FbType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is FbType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of FbType and the lookup by token. */
    public companion object {
        /** `FREE`. The time is free. */
        @JvmField
        public val Free: FbType = FbType("FREE")

        /** `BUSY`. The time is busy; the default. */
        @JvmField
        public val Busy: FbType = FbType("BUSY")

        /** `BUSY-UNAVAILABLE`. The time is busy and unavailable. */
        @JvmField
        public val BusyUnavailable: FbType = FbType("BUSY-UNAVAILABLE")

        /** `BUSY-TENTATIVE`. The time is tentatively busy. */
        @JvmField
        public val BusyTentative: FbType = FbType("BUSY-TENTATIVE")

        /**
         * Returns the constant for [value], ignoring case, or a new FbType holding the upper-cased token when it is not
         * one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): FbType = when (val token = enumToken(value)) {
            "FREE" -> Free
            "BUSY" -> Busy
            "BUSY-UNAVAILABLE" -> BusyUnavailable
            "BUSY-TENTATIVE" -> BusyTentative
            else -> FbType(token)
        }
    }
}

/**
 * The data type of a property value ([RFC 5545 §3.2.20](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.20),
 * `VALUE`, and [RFC 5545 §3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.3)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class ValueType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a ValueType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is ValueType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of ValueType and the lookup by token. */
    public companion object {
        /** `BINARY`. Base64 encoded binary data. */
        @JvmField
        public val Binary: ValueType = ValueType("BINARY")

        /** `BOOLEAN`. `TRUE` or `FALSE`. */
        @JvmField
        public val Boolean: ValueType = ValueType("BOOLEAN")

        /** `CAL-ADDRESS`. A calendar user address. */
        @JvmField
        public val CalAddress: ValueType = ValueType("CAL-ADDRESS")

        /** `DATE`. A calendar date. */
        @JvmField
        public val Date: ValueType = ValueType("DATE")

        /** `DATE-TIME`. A date with a time of day. */
        @JvmField
        public val DateTime: ValueType = ValueType("DATE-TIME")

        /** `DURATION`. A length of time. */
        @JvmField
        public val Duration: ValueType = ValueType("DURATION")

        /** `FLOAT`. A decimal number. */
        @JvmField
        public val Float: ValueType = ValueType("FLOAT")

        /** `INTEGER`. A signed 32-bit integer. */
        @JvmField
        public val Integer: ValueType = ValueType("INTEGER")

        /** `PERIOD`. A precise period of time. */
        @JvmField
        public val Period: ValueType = ValueType("PERIOD")

        /** `RECUR`. A recurrence rule. */
        @JvmField
        public val Recur: ValueType = ValueType("RECUR")

        /** `TEXT`. Human-readable text. */
        @JvmField
        public val Text: ValueType = ValueType("TEXT")

        /** `TIME`. A time of day. */
        @JvmField
        public val Time: ValueType = ValueType("TIME")

        /** `URI`. A uniform resource identifier. */
        @JvmField
        public val Uri: ValueType = ValueType("URI")

        /** `UTC-OFFSET`. An offset from UTC. */
        @JvmField
        public val UtcOffset: ValueType = ValueType("UTC-OFFSET")

        /** `UID`. A `UID` reference (RFC 9253). */
        @JvmField
        public val Uid: ValueType = ValueType("UID")

        /** `XML-REFERENCE`. A reference to an XML element (RFC 9073). */
        @JvmField
        public val XmlReference: ValueType = ValueType("XML-REFERENCE")

        /**
         * Returns the constant for [value], ignoring case, or a new ValueType holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): ValueType = when (val token = enumToken(value)) {
            "BINARY" -> Binary
            "BOOLEAN" -> Boolean
            "CAL-ADDRESS" -> CalAddress
            "DATE" -> Date
            "DATE-TIME" -> DateTime
            "DURATION" -> Duration
            "FLOAT" -> Float
            "INTEGER" -> Integer
            "PERIOD" -> Period
            "RECUR" -> Recur
            "TEXT" -> Text
            "TIME" -> Time
            "URI" -> Uri
            "UTC-OFFSET" -> UtcOffset
            "UID" -> Uid
            "XML-REFERENCE" -> XmlReference
            else -> ValueType(token)
        }
    }
}

/**
 * How an inline value is encoded ([RFC 5545 §3.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.7), `ENCODING`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Encoding internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Encoding with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Encoding && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Encoding and the lookup by token. */
    public companion object {
        /** `8BIT`. The text is written as is; the default. */
        @JvmField
        public val EightBit: Encoding = Encoding("8BIT")

        /** `BASE64`. The value is base64 encoded binary data. */
        @JvmField
        public val Base64: Encoding = Encoding("BASE64")

        /**
         * Returns the constant for [value], ignoring case, or a new Encoding holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Encoding = when (val token = enumToken(value)) {
            "8BIT" -> EightBit
            "BASE64" -> Base64
            else -> Encoding(token)
        }
    }
}

/**
 * Which end of the parent component a relative alarm trigger is measured from
 * ([RFC 5545 §3.2.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.14), `RELATED`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Related internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Related with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Related && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Related and the lookup by token. */
    public companion object {
        /** `START`. The start of the parent; the default. */
        @JvmField
        public val Start: Related = Related("START")

        /** `END`. The end (or due time) of the parent. */
        @JvmField
        public val End: Related = Related("END")

        /**
         * Returns the constant for [value], ignoring case, or a new Related holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Related = when (val token = enumToken(value)) {
            "START" -> Start
            "END" -> End
            else -> Related(token)
        }
    }
}

/**
 * The range of instances a recurrence override applies to
 * ([RFC 5545 §3.2.13](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.13), `RANGE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class RecurrenceRange internal constructor(public val value: String) {
    /** Returns `true` when [other] is a RecurrenceRange with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is RecurrenceRange && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of RecurrenceRange and the lookup by token. */
    public companion object {
        /** `THISANDFUTURE`. The override applies to this instance and all later ones. */
        @JvmField
        public val ThisAndFuture: RecurrenceRange = RecurrenceRange("THISANDFUTURE")

        /**
         * Returns the constant for [value], ignoring case, or a new RecurrenceRange holding the upper-cased token when
         * it is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): RecurrenceRange = when (val token = enumToken(value)) {
            "THISANDFUTURE" -> ThisAndFuture
            else -> RecurrenceRange(token)
        }
    }
}
