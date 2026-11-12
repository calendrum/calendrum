@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// Frequent vendor X-properties as an extension outside the default registry: vendors change their behaviour and there
// is no standard.
/**
 * The definitions of frequent vendor extensions (`X-` properties) of Microsoft, Apple, Google, Mozilla, libical and
 * Yandex, so that eager decoding and strict mode know them. They are not part of the default registry: install them
 * with `ICalRegistry.builder().install(Rfc5545).install(VendorProperties)`.
 */
public object VendorProperties : ICalExtension {
    /** Adds the definitions of the vendor properties to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(MICROSOFT_CDO_BUSYSTATUS, "TEXT", "", "VEVENT ?", "Microsoft")
        registry.define(MICROSOFT_CDO_INTENDEDSTATUS, "TEXT", "", "VEVENT ?", "Microsoft")
        registry.define(MICROSOFT_CDO_ALLDAYEVENT, "TEXT", "", "VEVENT ?", "Microsoft")
        registry.define(MICROSOFT_CDO_IMPORTANCE, "INTEGER", "", "VEVENT ? VTODO ?", "Microsoft")
        registry.define(MICROSOFT_CDO_OWNERAPPTID, "TEXT", "", "VEVENT ?", "Microsoft")
        registry.define(MICROSOFT_DISALLOW_COUNTER, "TEXT", "", "VEVENT ?", "Microsoft")
        registry.define(ALT_DESC, "TEXT", "FMTTYPE LANGUAGE", "VEVENT ? VTODO ? VJOURNAL *", "Microsoft")
        registry.define(WR_CALNAME, "TEXT", "", "VCALENDAR ?", "Apple, Google")
        registry.define(WR_CALDESC, "TEXT", "", "VCALENDAR ?", "Apple, Google")
        registry.define(WR_TIMEZONE, "TEXT", "", "VCALENDAR ?", "Apple, Google")
        registry.define(PUBLISHED_TTL, "DURATION", "", "VCALENDAR ?", "Microsoft, Google")
        registry.define(APPLE_CALENDAR_COLOR, "TEXT", "", "VCALENDAR ?", "Apple")
        registry.define(APPLE_TRAVEL_ADVISORY_BEHAVIOR, "TEXT", "", "VEVENT ?", "Apple")
        registry.define(APPLE_STRUCTURED_LOCATION, "URI", "", "VEVENT ? VTODO ?", "Apple")
        registry.define(APPLE_DEFAULT_ALARM, "TEXT", "", "VALARM ?", "Apple")
        registry.define(GOOGLE_CONFERENCE, "URI", "", "VEVENT ?", "Google")
        registry.define(MOZ_GENERATION, "INTEGER", "", "VEVENT ? VTODO ?", "Mozilla")
        registry.define(MOZ_LASTACK, "DATE-TIME", "", "VEVENT ? VTODO ?", "Mozilla")
        registry.define(LIC_LOCATION, "TEXT", "", "VTIMEZONE ?", "libical")
        registry.define(YANDEX_VISIBILITY, "TEXT", "", "VEVENT ?", "Yandex")
    }

    @Volatile
    private var microsoftCdoBusystatusKey: PropertyKey<BusyStatus>? = null

    /** The key of `X-MICROSOFT-CDO-BUSYSTATUS`: how Outlook shows the time of the event. */
    @JvmStatic
    public val MICROSOFT_CDO_BUSYSTATUS: PropertyKey<BusyStatus>
        get() = microsoftCdoBusystatusKey
            ?: PropertyKey("X-MICROSOFT-CDO-BUSYSTATUS", BusyStatusCodec()).also { microsoftCdoBusystatusKey = it }

    @Volatile
    private var microsoftCdoIntendedstatusKey: PropertyKey<BusyStatus>? = null

    /** The key of `X-MICROSOFT-CDO-INTENDEDSTATUS`: the busy status the organizer intends for the attendees. */
    @JvmStatic
    public val MICROSOFT_CDO_INTENDEDSTATUS: PropertyKey<BusyStatus>
        get() = microsoftCdoIntendedstatusKey
            ?: PropertyKey("X-MICROSOFT-CDO-INTENDEDSTATUS", BusyStatusCodec()).also {
                microsoftCdoIntendedstatusKey =
                    it
            }

    @Volatile
    private var microsoftCdoAlldayeventKey: PropertyKey<Boolean>? = null

    /** The key of `X-MICROSOFT-CDO-ALLDAYEVENT`: whether Outlook treats the event as an all-day event. */
    @JvmStatic
    public val MICROSOFT_CDO_ALLDAYEVENT: PropertyKey<Boolean>
        get() = microsoftCdoAlldayeventKey
            ?: PropertyKey("X-MICROSOFT-CDO-ALLDAYEVENT", BooleanTextCodec()).also { microsoftCdoAlldayeventKey = it }

    @Volatile
    private var microsoftCdoImportanceKey: PropertyKey<Int>? = null

    /** The key of `X-MICROSOFT-CDO-IMPORTANCE`: the importance of the event, as an integer. */
    @JvmStatic
    public val MICROSOFT_CDO_IMPORTANCE: PropertyKey<Int>
        get() = microsoftCdoImportanceKey
            ?: PropertyKey("X-MICROSOFT-CDO-IMPORTANCE", IntegerCodec()).also { microsoftCdoImportanceKey = it }

    @Volatile
    private var microsoftCdoOwnerapptidKey: PropertyKey<String>? = null

    /** The key of `X-MICROSOFT-CDO-OWNERAPPTID`: the identifier of the appointment in the owner's mailbox. */
    @JvmStatic
    public val MICROSOFT_CDO_OWNERAPPTID: PropertyKey<String>
        get() = microsoftCdoOwnerapptidKey
            ?: PropertyKey("X-MICROSOFT-CDO-OWNERAPPTID", TextCodec()).also { microsoftCdoOwnerapptidKey = it }

    @Volatile
    private var microsoftDisallowCounterKey: PropertyKey<Boolean>? = null

    /** The key of `X-MICROSOFT-DISALLOW-COUNTER`: whether attendees may not propose a new time. */
    @JvmStatic
    public val MICROSOFT_DISALLOW_COUNTER: PropertyKey<Boolean>
        get() = microsoftDisallowCounterKey
            ?: PropertyKey("X-MICROSOFT-DISALLOW-COUNTER", BooleanTextCodec()).also { microsoftDisallowCounterKey = it }

    @Volatile
    private var altDescKey: PropertyKey<StyledDescription>? = null

    /** The key of `X-ALT-DESC`: an alternative description, usually HTML, written by Outlook. Untrusted markup. */
    @JvmStatic
    public val ALT_DESC: PropertyKey<StyledDescription>
        get() = altDescKey ?: PropertyKey("X-ALT-DESC", StyledDescriptionCodec()).also { altDescKey = it }

    @Volatile
    private var wrCalnameKey: PropertyKey<String>? = null

    /** The key of `X-WR-CALNAME`: the calendar name, written by Apple and Google; the standard form is `NAME`. */
    @JvmStatic
    public val WR_CALNAME: PropertyKey<String>
        get() = wrCalnameKey ?: PropertyKey("X-WR-CALNAME", TextCodec()).also { wrCalnameKey = it }

    @Volatile
    private var wrCaldescKey: PropertyKey<String>? = null

    /**
     * The key of `X-WR-CALDESC`: the calendar description, written by Apple and Google; the standard form is
     * `DESCRIPTION`.
     */
    @JvmStatic
    public val WR_CALDESC: PropertyKey<String>
        get() = wrCaldescKey ?: PropertyKey("X-WR-CALDESC", TextCodec()).also { wrCaldescKey = it }

    @Volatile
    private var wrTimezoneKey: PropertyKey<String>? = null

    /** The key of `X-WR-TIMEZONE`: the default time zone of the calendar, written by Apple and Google. */
    @JvmStatic
    public val WR_TIMEZONE: PropertyKey<String>
        get() = wrTimezoneKey ?: PropertyKey("X-WR-TIMEZONE", TextCodec()).also { wrTimezoneKey = it }

    @Volatile
    private var publishedTtlKey: PropertyKey<ICalDuration>? = null

    /**
     * The key of `X-PUBLISHED-TTL`: how often a subscriber should refresh; the standard form is `REFRESH-INTERVAL`.
     */
    @JvmStatic
    public val PUBLISHED_TTL: PropertyKey<ICalDuration>
        get() = publishedTtlKey ?: PropertyKey("X-PUBLISHED-TTL", DurationCodec()).also { publishedTtlKey = it }

    @Volatile
    private var appleCalendarColorKey: PropertyKey<String>? = null

    /** The key of `X-APPLE-CALENDAR-COLOR`: the color of the calendar in Apple Calendar. */
    @JvmStatic
    public val APPLE_CALENDAR_COLOR: PropertyKey<String>
        get() = appleCalendarColorKey
            ?: PropertyKey("X-APPLE-CALENDAR-COLOR", TextCodec()).also { appleCalendarColorKey = it }

    @Volatile
    private var appleTravelAdvisoryBehaviorKey: PropertyKey<String>? = null

    /** The key of `X-APPLE-TRAVEL-ADVISORY-BEHAVIOR`: whether Apple Calendar adds travel time. */
    @JvmStatic
    public val APPLE_TRAVEL_ADVISORY_BEHAVIOR: PropertyKey<String>
        get() = appleTravelAdvisoryBehaviorKey
            ?: PropertyKey("X-APPLE-TRAVEL-ADVISORY-BEHAVIOR", TextCodec()).also { appleTravelAdvisoryBehaviorKey = it }

    @Volatile
    private var appleStructuredLocationKey: PropertyKey<String>? = null

    /** The key of `X-APPLE-STRUCTURED-LOCATION`: a `geo:` URI with the position of the location. */
    @JvmStatic
    public val APPLE_STRUCTURED_LOCATION: PropertyKey<String>
        get() = appleStructuredLocationKey
            ?: PropertyKey("X-APPLE-STRUCTURED-LOCATION", UriCodec()).also { appleStructuredLocationKey = it }

    @Volatile
    private var appleDefaultAlarmKey: PropertyKey<Boolean>? = null

    /** The key of `X-APPLE-DEFAULT-ALARM`: whether an alarm is the default one of Apple Calendar. */
    @JvmStatic
    public val APPLE_DEFAULT_ALARM: PropertyKey<Boolean>
        get() = appleDefaultAlarmKey
            ?: PropertyKey("X-APPLE-DEFAULT-ALARM", BooleanTextCodec()).also { appleDefaultAlarmKey = it }

    @Volatile
    private var googleConferenceKey: PropertyKey<String>? = null

    /** The key of `X-GOOGLE-CONFERENCE`: the link to a Google Meet conference. */
    @JvmStatic
    public val GOOGLE_CONFERENCE: PropertyKey<String>
        get() = googleConferenceKey ?: PropertyKey("X-GOOGLE-CONFERENCE", UriCodec()).also { googleConferenceKey = it }

    @Volatile
    private var mozGenerationKey: PropertyKey<Int>? = null

    /** The key of `X-MOZ-GENERATION`: the revision counter of a Mozilla Thunderbird item. */
    @JvmStatic
    public val MOZ_GENERATION: PropertyKey<Int>
        get() = mozGenerationKey ?: PropertyKey("X-MOZ-GENERATION", IntegerCodec()).also { mozGenerationKey = it }

    @Volatile
    private var mozLastackKey: PropertyKey<ICalDateTime>? = null

    /** The key of `X-MOZ-LASTACK`: the time Thunderbird last acknowledged the item's alarms. */
    @JvmStatic
    public val MOZ_LASTACK: PropertyKey<ICalDateTime>
        get() = mozLastackKey ?: PropertyKey("X-MOZ-LASTACK", UtcDateTimeCodec()).also { mozLastackKey = it }

    @Volatile
    private var licLocationKey: PropertyKey<String>? = null

    /** The key of `X-LIC-LOCATION`: the name of the zone, written by libical in a `VTIMEZONE`. */
    @JvmStatic
    public val LIC_LOCATION: PropertyKey<String>
        get() = licLocationKey ?: PropertyKey("X-LIC-LOCATION", TextCodec()).also { licLocationKey = it }

    @Volatile
    private var yandexVisibilityKey: PropertyKey<String>? = null

    /** The key of `X-YANDEX-VISIBILITY`: the visibility of an event in Yandex Calendar. */
    @JvmStatic
    public val YANDEX_VISIBILITY: PropertyKey<String>
        get() = yandexVisibilityKey ?: PropertyKey("X-YANDEX-VISIBILITY", TextCodec()).also { yandexVisibilityKey = it }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "VendorProperties"
}
