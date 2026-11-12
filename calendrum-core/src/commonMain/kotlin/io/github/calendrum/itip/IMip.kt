package io.github.calendrum.itip

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.model.Method
import io.github.calendrum.model.Properties
import io.github.calendrum.model.VCalendar
import io.github.calendrum.model.get
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.syntax.describe
import io.github.calendrum.validate.ElementPath
import kotlin.jvm.JvmStatic

/**
 * The result of comparing the mail sender with the party a message claims to come from ([RFC 6047
 * §3](https://www.rfc-editor.org/rfc/rfc6047#section-3)).
 */
public enum class SenderCheck {
    /**
     * The sender is the organizer (for messages from the organizer) or one of the attendees (for replies and requests
     * from attendees).
     */
    Matches,

    /** The sender is not the party itself but is named in its `SENT-BY`, acting on its behalf. */
    SentBy,

    /** The sender is none of the parties the message names. Treat the message with suspicion. */
    Mismatch,

    /**
     * The check cannot be made: the message has no `METHOD`, no schedulable component, or no organizer or attendee to
     * compare.
     */
    Unknown,
}

/**
 * The kind of content in a calendar that a client must not fetch or render without care.
 *
 * The list may grow in a minor release as new kinds of links or markup appear, so a `when` over it keeps an `else`
 * branch; it is reached through [IMip.untrustedContent], which is not frozen for 1.0.
 */
public enum class UntrustedKind {
    /** A link a client may fetch (`URL`, `ATTACH` by reference, `CONFERENCE`, `ALTREP` and so on). */
    Link,

    /** Markup, such as `X-ALT-DESC` or `STYLED-DESCRIPTION`, that must be sanitized before it is shown. */
    Html,

    /** An inline binary attachment (`ATTACH` or `IMAGE` encoded in base64). */
    Attachment,
}

/**
 * One piece of untrusted content found in a calendar by [IMip.untrustedContent].
 *
 * @property path where it is in the calendar.
 * @property kind what sort of content it is.
 * @property property the property that holds it.
 */
public class UntrustedContent internal constructor(
    public val path: ElementPath,
    public val kind: UntrustedKind,
    public val property: ICalProperty,
) {
    /** Returns the path and the kind; never the content. */
    override fun toString(): String = describe("UntrustedContent", "path", path, "kind", kind)
}

/**
 * Helpers for iTIP messages carried by e-mail ([RFC 6047](https://www.rfc-editor.org/rfc/rfc6047)): the content type,
 * the check of the sender, and a list of the content a client should not trust.
 *
 * All functions are pure.
 */
public object IMip {
    /**
     * Returns the `Content-Type` header value for a message with the given [method], such as `text/calendar;
     * method=REQUEST; charset=UTF-8`.
     *
     * @param method the `METHOD` of the message.
     */
    @JvmStatic
    public fun contentType(method: Method): String = "text/calendar; method=${method.value}; charset=UTF-8"

    /**
     * Compares the sender of the mail, [from], with the party the message says it comes from.
     *
     * For messages from the organizer (`PUBLISH`, `REQUEST`, `ADD`, `CANCEL`, `DECLINECOUNTER`) the `ORGANIZER` is
     * compared; for messages from an attendee (`REPLY`, `REFRESH`, `COUNTER`) the `ATTENDEE`s. A `SENT-BY` address
     * counts as [SenderCheck.SentBy]. If a message has several components the worst result wins.
     *
     * @param message the iTIP message.
     * @param from the address in the `From` header, with or without `mailto:`.
     * @sample io.github.calendrum.samples.invitationAndReply
     */
    @JvmStatic
    public fun checkSender(message: VCalendar, from: String): SenderCheck {
        val method = message.method?.value ?: return SenderCheck.Unknown
        val fromAttendee = method in ATTENDEE_METHODS
        if (!fromAttendee && method !in ORGANIZER_METHODS) return SenderCheck.Unknown
        val components = message.component.components.filter { it.name in SCHEDULED }
        if (components.isEmpty()) return SenderCheck.Unknown
        var result = SenderCheck.Matches
        for (component in components) {
            val check = if (fromAttendee) attendeeCheck(component, from) else organizerCheck(component, from)
            if (check.ordinal > result.ordinal) result = check
        }
        return result
    }

    /**
     * Lists the links, markup and inline attachments in [calendar] that a client should show but never fetch or render
     * blindly.
     *
     * @param calendar the message or calendar to scan.
     */
    @ExperimentalICalApi
    @JvmStatic
    public fun untrustedContent(calendar: VCalendar): List<UntrustedContent> {
        val found = ArrayList<UntrustedContent>()
        collect(calendar.component, ElementPath.Root.child(calendar.component.name, 0), found)
        return found
    }

    /** Returns the name of the object. */
    override fun toString(): String = "IMip"
}

private fun organizerCheck(component: ICalComponent, from: String): SenderCheck {
    val organizer = component[Properties.ORGANIZER] ?: return SenderCheck.Unknown
    return when {
        sameParty(organizer.address, from) -> SenderCheck.Matches
        organizer.sentBy?.let { sameParty(it, from) } == true -> SenderCheck.SentBy
        else -> SenderCheck.Mismatch
    }
}

private fun attendeeCheck(component: ICalComponent, from: String): SenderCheck {
    val attendees = component.attendeeList()
    if (attendees.isEmpty()) return SenderCheck.Unknown
    return when {
        attendees.any { sameParty(it.address, from) } -> SenderCheck.Matches
        attendees.any { attendee -> attendee.sentBy?.let { sameParty(it, from) } == true } -> SenderCheck.SentBy
        else -> SenderCheck.Mismatch
    }
}

private fun collect(component: ICalComponent, path: ElementPath, found: MutableList<UntrustedContent>) {
    val counts = HashMap<String, Int>()
    for (property in component.properties) {
        val index = counts[property.name] ?: 0
        counts[property.name] = index + 1
        val at = path.child(property.name, index)
        val kind = kindOf(property)
        if (kind != null) found += UntrustedContent(at, kind, property)
        for (parameter in property.parameters) {
            if (parameter.name in
                LINK_PARAMETERS
            ) {
                found += UntrustedContent(at.parameter(parameter.name), UntrustedKind.Link, property)
            }
        }
    }
    val children = HashMap<String, Int>()
    for (child in component.components) {
        val index = children[child.name] ?: 0
        children[child.name] = index + 1
        collect(child, path.child(child.name, index), found)
    }
}

private fun kindOf(property: ICalProperty): UntrustedKind? = when (property.name) {
    "X-ALT-DESC", "STYLED-DESCRIPTION" -> UntrustedKind.Html

    "ATTACH", "IMAGE" -> if (property.parameter("ENCODING")?.value.equals("BASE64", ignoreCase = true)) {
        UntrustedKind.Attachment
    } else {
        UntrustedKind.Link
    }

    in LINK_PROPERTIES -> UntrustedKind.Link

    else -> null
}

private val ORGANIZER_METHODS = setOf("PUBLISH", "REQUEST", "ADD", "CANCEL", "DECLINECOUNTER")

private val ATTENDEE_METHODS = setOf("REPLY", "REFRESH", "COUNTER")

private val LINK_PROPERTIES = setOf("URL", "CONFERENCE", "LINK", "SOURCE", "TZURL", "X-GOOGLE-CONFERENCE")

private val LINK_PARAMETERS = setOf("ALTREP", "DIR")
