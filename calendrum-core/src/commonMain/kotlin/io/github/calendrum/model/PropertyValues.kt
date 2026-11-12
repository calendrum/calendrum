package io.github.calendrum.model

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The status of an event ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11), `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class EventStatus internal constructor(public val value: String) {
    /** Returns `true` when [other] is a EventStatus with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is EventStatus && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of EventStatus and the lookup by token. */
    public companion object {
        /** `TENTATIVE`. The event is tentative. */
        @JvmField
        public val Tentative: EventStatus = EventStatus("TENTATIVE")

        /** `CONFIRMED`. The event is confirmed. */
        @JvmField
        public val Confirmed: EventStatus = EventStatus("CONFIRMED")

        /** `CANCELLED`. The event is cancelled. */
        @JvmField
        public val Cancelled: EventStatus = EventStatus("CANCELLED")

        /**
         * Returns the constant for [value], ignoring case, or a new EventStatus holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): EventStatus = when (val token = enumToken(value)) {
            "TENTATIVE" -> Tentative
            "CONFIRMED" -> Confirmed
            "CANCELLED" -> Cancelled
            else -> EventStatus(token)
        }
    }
}

/**
 * The status of a to-do ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11), `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class TodoStatus internal constructor(public val value: String) {
    /** Returns `true` when [other] is a TodoStatus with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is TodoStatus && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of TodoStatus and the lookup by token. */
    public companion object {
        /** `NEEDS-ACTION`. The to-do needs action. */
        @JvmField
        public val NeedsAction: TodoStatus = TodoStatus("NEEDS-ACTION")

        /** `COMPLETED`. The to-do is completed. */
        @JvmField
        public val Completed: TodoStatus = TodoStatus("COMPLETED")

        /** `IN-PROCESS`. The to-do is in progress. */
        @JvmField
        public val InProcess: TodoStatus = TodoStatus("IN-PROCESS")

        /** `CANCELLED`. The to-do is cancelled. */
        @JvmField
        public val Cancelled: TodoStatus = TodoStatus("CANCELLED")

        /**
         * Returns the constant for [value], ignoring case, or a new TodoStatus holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): TodoStatus = when (val token = enumToken(value)) {
            "NEEDS-ACTION" -> NeedsAction
            "COMPLETED" -> Completed
            "IN-PROCESS" -> InProcess
            "CANCELLED" -> Cancelled
            else -> TodoStatus(token)
        }
    }
}

/**
 * The status of a journal entry ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11),
 * `STATUS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class JournalStatus internal constructor(public val value: String) {
    /** Returns `true` when [other] is a JournalStatus with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is JournalStatus && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of JournalStatus and the lookup by token. */
    public companion object {
        /** `DRAFT`. The entry is a draft. */
        @JvmField
        public val Draft: JournalStatus = JournalStatus("DRAFT")

        /** `FINAL`. The entry is final. */
        @JvmField
        public val Final: JournalStatus = JournalStatus("FINAL")

        /** `CANCELLED`. The entry is cancelled. */
        @JvmField
        public val Cancelled: JournalStatus = JournalStatus("CANCELLED")

        /**
         * Returns the constant for [value], ignoring case, or a new JournalStatus holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): JournalStatus = when (val token = enumToken(value)) {
            "DRAFT" -> Draft
            "FINAL" -> Final
            "CANCELLED" -> Cancelled
            else -> JournalStatus(token)
        }
    }
}

/**
 * The access classification of a component
 * ([RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3), `CLASS`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Classification internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Classification with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Classification && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Classification and the lookup by token. */
    public companion object {
        /** `PUBLIC`. Anyone may see the details; the default. */
        @JvmField
        public val Public: Classification = Classification("PUBLIC")

        /** `PRIVATE`. Only the owner may see the details. */
        @JvmField
        public val Private: Classification = Classification("PRIVATE")

        /** `CONFIDENTIAL`. The details are shown only to a limited set of people. */
        @JvmField
        public val Confidential: Classification = Classification("CONFIDENTIAL")

        /**
         * Returns the constant for [value], ignoring case, or a new Classification holding the upper-cased token when
         * it is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Classification = when (val token = enumToken(value)) {
            "PUBLIC" -> Public
            "PRIVATE" -> Private
            "CONFIDENTIAL" -> Confidential
            else -> Classification(token)
        }
    }
}

/**
 * Whether an event blocks time in a free/busy search
 * ([RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7), `TRANSP`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Transparency internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Transparency with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Transparency && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Transparency and the lookup by token. */
    public companion object {
        /** `OPAQUE`. The event blocks time; the default. */
        @JvmField
        public val Opaque: Transparency = Transparency("OPAQUE")

        /** `TRANSPARENT`. The event does not block time. */
        @JvmField
        public val Transparent: Transparency = Transparency("TRANSPARENT")

        /**
         * Returns the constant for [value], ignoring case, or a new Transparency holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Transparency = when (val token = enumToken(value)) {
            "OPAQUE" -> Opaque
            "TRANSPARENT" -> Transparent
            else -> Transparency(token)
        }
    }
}

/**
 * What an alarm does when it fires ([RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1),
 * `ACTION`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class AlarmAction internal constructor(public val value: String) {
    /** Returns `true` when [other] is a AlarmAction with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is AlarmAction && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of AlarmAction and the lookup by token. */
    public companion object {
        /** `AUDIO`. Plays a sound. */
        @JvmField
        public val Audio: AlarmAction = AlarmAction("AUDIO")

        /** `DISPLAY`. Shows a message. */
        @JvmField
        public val Display: AlarmAction = AlarmAction("DISPLAY")

        /** `EMAIL`. Sends an e-mail. */
        @JvmField
        public val Email: AlarmAction = AlarmAction("EMAIL")

        /**
         * Returns the constant for [value], ignoring case, or a new AlarmAction holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): AlarmAction = when (val token = enumToken(value)) {
            "AUDIO" -> Audio
            "DISPLAY" -> Display
            "EMAIL" -> Email
            else -> AlarmAction(token)
        }
    }
}

/**
 * The iTIP method of a scheduling message ([RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2),
 * `METHOD`, with the semantics of [RFC 5546](https://www.rfc-editor.org/rfc/rfc5546)).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Method internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Method with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Method && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Method and the lookup by token. */
    public companion object {
        /** `PUBLISH`. Publishes a component to anyone, without expecting an answer. */
        @JvmField
        public val Publish: Method = Method("PUBLISH")

        /** `REQUEST`. Asks for attendance, or updates a component sent before. */
        @JvmField
        public val Request: Method = Method("REQUEST")

        /** `REPLY`. An attendee answers a request. */
        @JvmField
        public val Reply: Method = Method("REPLY")

        /** `ADD`. The organizer adds instances to an existing series. */
        @JvmField
        public val Add: Method = Method("ADD")

        /** `CANCEL`. The organizer cancels a component or some of its instances. */
        @JvmField
        public val Cancel: Method = Method("CANCEL")

        /** `REFRESH`. An attendee asks for the latest version. */
        @JvmField
        public val Refresh: Method = Method("REFRESH")

        /** `COUNTER`. An attendee proposes a change. */
        @JvmField
        public val Counter: Method = Method("COUNTER")

        /** `DECLINECOUNTER`. The organizer declines a proposed change. */
        @JvmField
        public val DeclineCounter: Method = Method("DECLINECOUNTER")

        /**
         * Returns the constant for [value], ignoring case, or a new Method holding the upper-cased token when it is not
         * one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Method = when (val token = enumToken(value)) {
            "PUBLISH" -> Publish
            "REQUEST" -> Request
            "REPLY" -> Reply
            "ADD" -> Add
            "CANCEL" -> Cancel
            "REFRESH" -> Refresh
            "COUNTER" -> Counter
            "DECLINECOUNTER" -> DeclineCounter
            else -> Method(token)
        }
    }
}
