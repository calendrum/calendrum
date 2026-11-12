package io.github.calendrum.model

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * How an image is meant to be shown ([RFC 7986 §6.1](https://www.rfc-editor.org/rfc/rfc7986#section-6.1), `DISPLAY`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class ImageDisplay internal constructor(public val value: String) {
    /** Returns `true` when [other] is a ImageDisplay with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is ImageDisplay && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of ImageDisplay and the lookup by token. */
    public companion object {
        /** `BADGE`. A small image shown next to the title. */
        @JvmField
        public val Badge: ImageDisplay = ImageDisplay("BADGE")

        /** `GRAPHIC`. An image shown as a graphic with the content. */
        @JvmField
        public val Graphic: ImageDisplay = ImageDisplay("GRAPHIC")

        /** `FULLSIZE`. A full-size version of an image. */
        @JvmField
        public val Fullsize: ImageDisplay = ImageDisplay("FULLSIZE")

        /** `THUMBNAIL`. A reduced-size version of an image. */
        @JvmField
        public val Thumbnail: ImageDisplay = ImageDisplay("THUMBNAIL")

        /**
         * Returns the constant for [value], ignoring case, or a new ImageDisplay holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): ImageDisplay = when (val token = enumToken(value)) {
            "BADGE" -> Badge
            "GRAPHIC" -> Graphic
            "FULLSIZE" -> Fullsize
            "THUMBNAIL" -> Thumbnail
            else -> ImageDisplay(token)
        }
    }
}

/**
 * A feature of a conference ([RFC 7986 §6.3](https://www.rfc-editor.org/rfc/rfc7986#section-6.3), `FEATURE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class ConferenceFeature internal constructor(public val value: String) {
    /** Returns `true` when [other] is a ConferenceFeature with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is ConferenceFeature && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of ConferenceFeature and the lookup by token. */
    public companion object {
        /** `AUDIO`. Audio conferencing. */
        @JvmField
        public val Audio: ConferenceFeature = ConferenceFeature("AUDIO")

        /** `CHAT`. Chat or instant messaging. */
        @JvmField
        public val Chat: ConferenceFeature = ConferenceFeature("CHAT")

        /** `FEED`. A blog or atom feed. */
        @JvmField
        public val Feed: ConferenceFeature = ConferenceFeature("FEED")

        /** `MODERATOR`. Moderator dial-in. */
        @JvmField
        public val Moderator: ConferenceFeature = ConferenceFeature("MODERATOR")

        /** `PHONE`. Phone conferencing. */
        @JvmField
        public val Phone: ConferenceFeature = ConferenceFeature("PHONE")

        /** `SCREEN`. Screen sharing. */
        @JvmField
        public val Screen: ConferenceFeature = ConferenceFeature("SCREEN")

        /** `VIDEO`. Video conferencing. */
        @JvmField
        public val Video: ConferenceFeature = ConferenceFeature("VIDEO")

        /**
         * Returns the constant for [value], ignoring case, or a new ConferenceFeature holding the upper-cased token
         * when it is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): ConferenceFeature = when (val token = enumToken(value)) {
            "AUDIO" -> Audio
            "CHAT" -> Chat
            "FEED" -> Feed
            "MODERATOR" -> Moderator
            "PHONE" -> Phone
            "SCREEN" -> Screen
            "VIDEO" -> Video
            else -> ConferenceFeature(token)
        }
    }
}

/**
 * The type of a participant ([RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2), `PARTICIPANT-TYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class ParticipantType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a ParticipantType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is ParticipantType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of ParticipantType and the lookup by token. */
    public companion object {
        /** `ACTIVE`. An active participant. */
        @JvmField
        public val Active: ParticipantType = ParticipantType("ACTIVE")

        /** `INACTIVE`. An inactive participant. */
        @JvmField
        public val Inactive: ParticipantType = ParticipantType("INACTIVE")

        /** `SPONSOR`. A sponsor of the activity. */
        @JvmField
        public val Sponsor: ParticipantType = ParticipantType("SPONSOR")

        /** `CONTACT`. A contact for information. */
        @JvmField
        public val Contact: ParticipantType = ParticipantType("CONTACT")

        /** `BOOKING-CONTACT`. A contact for bookings or reservations. */
        @JvmField
        public val BookingContact: ParticipantType = ParticipantType("BOOKING-CONTACT")

        /** `EMERGENCY-CONTACT`. A contact in an emergency. */
        @JvmField
        public val EmergencyContact: ParticipantType = ParticipantType("EMERGENCY-CONTACT")

        /** `PUBLICITY-CONTACT`. A contact for publicity. */
        @JvmField
        public val PublicityContact: ParticipantType = ParticipantType("PUBLICITY-CONTACT")

        /** `PLANNER-CONTACT`. A contact for the planning of the activity. */
        @JvmField
        public val PlannerContact: ParticipantType = ParticipantType("PLANNER-CONTACT")

        /** `PERFORMER`. A performer, such as a musician. */
        @JvmField
        public val Performer: ParticipantType = ParticipantType("PERFORMER")

        /** `SPEAKER`. A speaker. */
        @JvmField
        public val Speaker: ParticipantType = ParticipantType("SPEAKER")

        /**
         * Returns the constant for [value], ignoring case, or a new ParticipantType holding the upper-cased token when
         * it is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): ParticipantType = when (val token = enumToken(value)) {
            "ACTIVE" -> Active
            "INACTIVE" -> Inactive
            "SPONSOR" -> Sponsor
            "CONTACT" -> Contact
            "BOOKING-CONTACT" -> BookingContact
            "EMERGENCY-CONTACT" -> EmergencyContact
            "PUBLICITY-CONTACT" -> PublicityContact
            "PLANNER-CONTACT" -> PlannerContact
            "PERFORMER" -> Performer
            "SPEAKER" -> Speaker
            else -> ParticipantType(token)
        }
    }
}

/**
 * The type of a resource ([RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3), `RESOURCE-TYPE`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class ResourceType internal constructor(public val value: String) {
    /** Returns `true` when [other] is a ResourceType with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is ResourceType && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of ResourceType and the lookup by token. */
    public companion object {
        /** `ROOM`. A room. */
        @JvmField
        public val Room: ResourceType = ResourceType("ROOM")

        /** `PROJECTOR`. A projector. */
        @JvmField
        public val Projector: ResourceType = ResourceType("PROJECTOR")

        /** `REMOTE-CONFERENCE-AUDIO`. A remote conference audio channel. */
        @JvmField
        public val RemoteConferenceAudio: ResourceType = ResourceType("REMOTE-CONFERENCE-AUDIO")

        /** `REMOTE-CONFERENCE-VIDEO`. A remote conference video channel. */
        @JvmField
        public val RemoteConferenceVideo: ResourceType = ResourceType("REMOTE-CONFERENCE-VIDEO")

        /**
         * Returns the constant for [value], ignoring case, or a new ResourceType holding the upper-cased token when it
         * is not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): ResourceType = when (val token = enumToken(value)) {
            "ROOM" -> Room
            "PROJECTOR" -> Projector
            "REMOTE-CONFERENCE-AUDIO" -> RemoteConferenceAudio
            "REMOTE-CONFERENCE-VIDEO" -> RemoteConferenceVideo
            else -> ResourceType(token)
        }
    }
}

/**
 * The proximity condition that fires an alarm ([RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1),
 * `PROXIMITY`).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class Proximity internal constructor(public val value: String) {
    /** Returns `true` when [other] is a Proximity with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is Proximity && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of Proximity and the lookup by token. */
    public companion object {
        /** `ARRIVE`. Fires when the user arrives at the location. */
        @JvmField
        public val Arrive: Proximity = Proximity("ARRIVE")

        /** `DEPART`. Fires when the user leaves the location. */
        @JvmField
        public val Depart: Proximity = Proximity("DEPART")

        /** `CONNECT`. Fires when the user's device connects, for example to a car. */
        @JvmField
        public val Connect: Proximity = Proximity("CONNECT")

        /** `DISCONNECT`. Fires when the user's device disconnects. */
        @JvmField
        public val Disconnect: Proximity = Proximity("DISCONNECT")

        /**
         * Returns the constant for [value], ignoring case, or a new Proximity holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): Proximity = when (val token = enumToken(value)) {
            "ARRIVE" -> Arrive
            "DEPART" -> Depart
            "CONNECT" -> Connect
            "DISCONNECT" -> Disconnect
            else -> Proximity(token)
        }
    }
}

/**
 * The busy status in the Microsoft extension `X-MICROSOFT-CDO-BUSYSTATUS` (a de-facto property of Outlook and Exchange,
 * not a standard).
 *
 * An open enumeration: the constants are the values the standard defines, and [of] turns any token into a value,
 * keeping one it does not know, so reading and writing never lose data. Compare with `==`. Immutable.
 *
 * @property value the token in upper case, as written in iCalendar text.
 */
public class BusyStatus internal constructor(public val value: String) {
    /** Returns `true` when [other] is a BusyStatus with the same token. */
    override fun equals(other: Any?): Boolean = this === other || (other is BusyStatus && value == other.value)

    /** Returns the hash code of the token. */
    override fun hashCode(): Int = value.hashCode()

    /** Returns the token. */
    override fun toString(): String = value

    /** The constants of BusyStatus and the lookup by token. */
    public companion object {
        /** `FREE`. Free. */
        @JvmField
        public val Free: BusyStatus = BusyStatus("FREE")

        /** `TENTATIVE`. Tentatively busy. */
        @JvmField
        public val Tentative: BusyStatus = BusyStatus("TENTATIVE")

        /** `BUSY`. Busy. */
        @JvmField
        public val Busy: BusyStatus = BusyStatus("BUSY")

        /** `OOF`. Out of office. */
        @JvmField
        public val Oof: BusyStatus = BusyStatus("OOF")

        /** `WORKINGELSEWHERE`. Working elsewhere. */
        @JvmField
        public val WorkingElsewhere: BusyStatus = BusyStatus("WORKINGELSEWHERE")

        /**
         * Returns the constant for [value], ignoring case, or a new BusyStatus holding the upper-cased token when it is
         * not one the library knows.
         *
         * @param value a token as written in iCalendar text.
         */
        @JvmStatic
        public fun of(value: String): BusyStatus = when (val token = enumToken(value)) {
            "FREE" -> Free
            "TENTATIVE" -> Tentative
            "BUSY" -> Busy
            "OOF" -> Oof
            "WORKINGELSEWHERE" -> WorkingElsewhere
            else -> BusyStatus(token)
        }
    }
}
