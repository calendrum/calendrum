package io.github.calendrum.model

import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.Recur
import kotlinx.datetime.UtcOffset
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// A key is created with its codec on the first read of its property and cached in its own volatile field. A field
// initialized in the object would create all 48 keys at once and make every codec reachable on Kotlin/Native and Wasm,
// including the one that creates `UtcOffset` and with it the formatting and serialization code of kotlinx-datetime
// . Two threads may create two equal keys; nothing depends on their identity.
/**
 * Typed keys for the standard properties, to read with `component[Properties.DTSTART]`, write with a builder, or use in
 * a schema.
 *
 * The keys are created on first use and shared; there is no registration. For an `X-` property make your own
 * [PropertyKey]. Three keys read `STATUS`, one for each component that has it.
 */
public object Properties {
    @Volatile
    private var calscaleKey: PropertyKey<String>? = null

    /**
     * The key of the `CALSCALE` property ([RFC 5545 §3.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.1)),
     * read as text.
     */
    @JvmStatic
    public val CALSCALE: PropertyKey<String>
        get() = calscaleKey ?: PropertyKey("CALSCALE", TextCodec()).also { calscaleKey = it }

    @Volatile
    private var methodKey: PropertyKey<Method>? = null

    /**
     * The key of the `METHOD` property ([RFC 5545 §3.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.2)), read
     * as a [Method].
     */
    @JvmStatic
    public val METHOD: PropertyKey<Method>
        get() = methodKey ?: PropertyKey("METHOD", MethodCodec()).also { methodKey = it }

    @Volatile
    private var prodidKey: PropertyKey<String>? = null

    /**
     * The key of the `PRODID` property ([RFC 5545 §3.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.3)), read
     * as text.
     */
    @JvmStatic
    public val PRODID: PropertyKey<String>
        get() = prodidKey ?: PropertyKey("PRODID", TextCodec()).also { prodidKey = it }

    @Volatile
    private var versionKey: PropertyKey<String>? = null

    /**
     * The key of the `VERSION` property ([RFC 5545 §3.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.7.4)), read
     * as text.
     */
    @JvmStatic
    public val VERSION: PropertyKey<String>
        get() = versionKey ?: PropertyKey("VERSION", TextCodec()).also { versionKey = it }

    @Volatile
    private var attachKey: PropertyKey<Attach>? = null

    /**
     * The key of the `ATTACH` property ([RFC 5545 §3.8.1.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.1)),
     * read as a [Attach].
     */
    @JvmStatic
    public val ATTACH: PropertyKey<Attach>
        get() = attachKey ?: PropertyKey("ATTACH", AttachCodec()).also { attachKey = it }

    @Volatile
    private var categoriesKey: PropertyKey<Categories>? = null

    /**
     * The key of the `CATEGORIES` property
     * ([RFC 5545 §3.8.1.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.2)), read as a [Categories].
     */
    @JvmStatic
    public val CATEGORIES: PropertyKey<Categories>
        get() = categoriesKey ?: PropertyKey("CATEGORIES", CategoriesCodec()).also { categoriesKey = it }

    @Volatile
    private var classKey: PropertyKey<Classification>? = null

    /**
     * The key of the `CLASS` property ([RFC 5545 §3.8.1.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.3)),
     * read as a [Classification].
     */
    @JvmStatic
    public val CLASS: PropertyKey<Classification>
        get() = classKey ?: PropertyKey("CLASS", ClassificationCodec()).also { classKey = it }

    @Volatile
    private var commentKey: PropertyKey<String>? = null

    /**
     * The key of the `COMMENT` property ([RFC 5545 §3.8.1.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.4)),
     * read as text.
     */
    @JvmStatic
    public val COMMENT: PropertyKey<String>
        get() = commentKey ?: PropertyKey("COMMENT", TextCodec()).also { commentKey = it }

    @Volatile
    private var descriptionKey: PropertyKey<String>? = null

    /**
     * The key of the `DESCRIPTION` property
     * ([RFC 5545 §3.8.1.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.5)), read as text.
     */
    @JvmStatic
    public val DESCRIPTION: PropertyKey<String>
        get() = descriptionKey ?: PropertyKey("DESCRIPTION", TextCodec()).also { descriptionKey = it }

    @Volatile
    private var geoKey: PropertyKey<Geo>? = null

    /**
     * The key of the `GEO` property ([RFC 5545 §3.8.1.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.6)), read
     * as a [Geo].
     */
    @JvmStatic
    public val GEO: PropertyKey<Geo>
        get() = geoKey ?: PropertyKey("GEO", GeoCodec()).also { geoKey = it }

    @Volatile
    private var locationKey: PropertyKey<Location>? = null

    /**
     * The key of the `LOCATION` property ([RFC 5545 §3.8.1.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.7)),
     * read as a [Location].
     */
    @JvmStatic
    public val LOCATION: PropertyKey<Location>
        get() = locationKey ?: PropertyKey("LOCATION", LocationCodec()).also { locationKey = it }

    @Volatile
    private var percentCompleteKey: PropertyKey<Int>? = null

    /**
     * The key of the `PERCENT-COMPLETE` property
     * ([RFC 5545 §3.8.1.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.8)), read as an integer.
     */
    @JvmStatic
    public val PERCENT_COMPLETE: PropertyKey<Int>
        get() = percentCompleteKey ?: PropertyKey("PERCENT-COMPLETE", IntegerCodec()).also { percentCompleteKey = it }

    @Volatile
    private var priorityKey: PropertyKey<Int>? = null

    /**
     * The key of the `PRIORITY` property ([RFC 5545 §3.8.1.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.9)),
     * read as an integer.
     */
    @JvmStatic
    public val PRIORITY: PropertyKey<Int>
        get() = priorityKey ?: PropertyKey("PRIORITY", IntegerCodec()).also { priorityKey = it }

    @Volatile
    private var resourcesKey: PropertyKey<Resources>? = null

    /**
     * The key of the `RESOURCES` property
     * ([RFC 5545 §3.8.1.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.10)), read as a [Resources].
     */
    @JvmStatic
    public val RESOURCES: PropertyKey<Resources>
        get() = resourcesKey ?: PropertyKey("RESOURCES", ResourcesCodec()).also { resourcesKey = it }

    @Volatile
    private var eventStatusKey: PropertyKey<EventStatus>? = null

    /**
     * The key of the `STATUS` property of an event
     * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [EventStatus]; the
     * property has a different set of values in each component.
     */
    @JvmStatic
    public val EVENT_STATUS: PropertyKey<EventStatus>
        get() = eventStatusKey ?: PropertyKey("STATUS", EventStatusCodec()).also { eventStatusKey = it }

    @Volatile
    private var todoStatusKey: PropertyKey<TodoStatus>? = null

    /**
     * The key of the `STATUS` property of a to-do
     * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [TodoStatus]; the
     * property has a different set of values in each component.
     */
    @JvmStatic
    public val TODO_STATUS: PropertyKey<TodoStatus>
        get() = todoStatusKey ?: PropertyKey("STATUS", TodoStatusCodec()).also { todoStatusKey = it }

    @Volatile
    private var journalStatusKey: PropertyKey<JournalStatus>? = null

    /**
     * The key of the `STATUS` property of a journal entry
     * ([RFC 5545 §3.8.1.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.11)), read as a [JournalStatus]; the
     * property has a different set of values in each component.
     */
    @JvmStatic
    public val JOURNAL_STATUS: PropertyKey<JournalStatus>
        get() = journalStatusKey ?: PropertyKey("STATUS", JournalStatusCodec()).also { journalStatusKey = it }

    @Volatile
    private var summaryKey: PropertyKey<String>? = null

    /**
     * The key of the `SUMMARY` property
     * ([RFC 5545 §3.8.1.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.1.12)), read as text.
     */
    @JvmStatic
    public val SUMMARY: PropertyKey<String>
        get() = summaryKey ?: PropertyKey("SUMMARY", TextCodec()).also { summaryKey = it }

    @Volatile
    private var completedKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `COMPLETED` property
     * ([RFC 5545 §3.8.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.1)), read as a UTC date-time.
     */
    @JvmStatic
    public val COMPLETED: PropertyKey<ICalDateTime>
        get() = completedKey ?: PropertyKey("COMPLETED", UtcDateTimeCodec()).also { completedKey = it }

    @Volatile
    private var dtendKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `DTEND` property ([RFC 5545 §3.8.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.2)),
     * read as a date or date-time.
     */
    @JvmStatic
    public val DTEND: PropertyKey<ICalDateTime>
        get() = dtendKey ?: PropertyKey("DTEND", DateTimeCodec()).also { dtendKey = it }

    @Volatile
    private var dueKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `DUE` property ([RFC 5545 §3.8.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.3)), read
     * as a date or date-time.
     */
    @JvmStatic
    public val DUE: PropertyKey<ICalDateTime>
        get() = dueKey ?: PropertyKey("DUE", DateTimeCodec()).also { dueKey = it }

    @Volatile
    private var dtstartKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `DTSTART` property ([RFC 5545 §3.8.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.4)),
     * read as a date or date-time.
     */
    @JvmStatic
    public val DTSTART: PropertyKey<ICalDateTime>
        get() = dtstartKey ?: PropertyKey("DTSTART", DateTimeCodec()).also { dtstartKey = it }

    @Volatile
    private var durationKey: PropertyKey<ICalDuration>? = null

    /**
     * The key of the `DURATION` property ([RFC 5545 §3.8.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.5)),
     * read as a [ICalDuration].
     */
    @JvmStatic
    public val DURATION: PropertyKey<ICalDuration>
        get() = durationKey ?: PropertyKey("DURATION", DurationCodec()).also { durationKey = it }

    @Volatile
    private var freebusyKey: PropertyKey<FreeBusy>? = null

    /**
     * The key of the `FREEBUSY` property ([RFC 5545 §3.8.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.6)),
     * read as a [FreeBusy].
     */
    @JvmStatic
    public val FREEBUSY: PropertyKey<FreeBusy>
        get() = freebusyKey ?: PropertyKey("FREEBUSY", FreeBusyCodec()).also { freebusyKey = it }

    @Volatile
    private var transpKey: PropertyKey<Transparency>? = null

    /**
     * The key of the `TRANSP` property ([RFC 5545 §3.8.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.2.7)),
     * read as a [Transparency].
     */
    @JvmStatic
    public val TRANSP: PropertyKey<Transparency>
        get() = transpKey ?: PropertyKey("TRANSP", TransparencyCodec()).also { transpKey = it }

    @Volatile
    private var tzidKey: PropertyKey<String>? = null

    /**
     * The key of the `TZID` property ([RFC 5545 §3.8.3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.1)),
     * read as text.
     */
    @JvmStatic
    public val TZID: PropertyKey<String>
        get() = tzidKey ?: PropertyKey("TZID", TextCodec()).also { tzidKey = it }

    @Volatile
    private var tznameKey: PropertyKey<String>? = null

    /**
     * The key of the `TZNAME` property ([RFC 5545 §3.8.3.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.2)),
     * read as text.
     */
    @JvmStatic
    public val TZNAME: PropertyKey<String>
        get() = tznameKey ?: PropertyKey("TZNAME", TextCodec()).also { tznameKey = it }

    @Volatile
    private var tzoffsetfromKey: PropertyKey<UtcOffset>? = null

    /**
     * The key of the `TZOFFSETFROM` property
     * ([RFC 5545 §3.8.3.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.3)), read as a [UtcOffset].
     */
    @JvmStatic
    public val TZOFFSETFROM: PropertyKey<UtcOffset>
        get() = tzoffsetfromKey ?: PropertyKey("TZOFFSETFROM", UtcOffsetCodec()).also { tzoffsetfromKey = it }

    @Volatile
    private var tzoffsettoKey: PropertyKey<UtcOffset>? = null

    /**
     * The key of the `TZOFFSETTO` property
     * ([RFC 5545 §3.8.3.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.4)), read as a [UtcOffset].
     */
    @JvmStatic
    public val TZOFFSETTO: PropertyKey<UtcOffset>
        get() = tzoffsettoKey ?: PropertyKey("TZOFFSETTO", UtcOffsetCodec()).also { tzoffsettoKey = it }

    @Volatile
    private var tzurlKey: PropertyKey<String>? = null

    /**
     * The key of the `TZURL` property ([RFC 5545 §3.8.3.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.3.5)),
     * read as a URI.
     */
    @JvmStatic
    public val TZURL: PropertyKey<String>
        get() = tzurlKey ?: PropertyKey("TZURL", UriCodec()).also { tzurlKey = it }

    @Volatile
    private var attendeeKey: PropertyKey<Attendee>? = null

    /**
     * The key of the `ATTENDEE` property ([RFC 5545 §3.8.4.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.1)),
     * read as a [Attendee].
     */
    @JvmStatic
    public val ATTENDEE: PropertyKey<Attendee>
        get() = attendeeKey ?: PropertyKey("ATTENDEE", AttendeeCodec()).also { attendeeKey = it }

    @Volatile
    private var contactKey: PropertyKey<Contact>? = null

    /**
     * The key of the `CONTACT` property ([RFC 5545 §3.8.4.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.2)),
     * read as a [Contact].
     */
    @JvmStatic
    public val CONTACT: PropertyKey<Contact>
        get() = contactKey ?: PropertyKey("CONTACT", ContactCodec()).also { contactKey = it }

    @Volatile
    private var organizerKey: PropertyKey<Organizer>? = null

    /**
     * The key of the `ORGANIZER` property
     * ([RFC 5545 §3.8.4.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.3)), read as a [Organizer].
     */
    @JvmStatic
    public val ORGANIZER: PropertyKey<Organizer>
        get() = organizerKey ?: PropertyKey("ORGANIZER", OrganizerCodec()).also { organizerKey = it }

    @Volatile
    private var recurrenceIdKey: PropertyKey<RecurrenceId>? = null

    /**
     * The key of the `RECURRENCE-ID` property
     * ([RFC 5545 §3.8.4.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.4)), read as a [RecurrenceId].
     */
    @JvmStatic
    public val RECURRENCE_ID: PropertyKey<RecurrenceId>
        get() = recurrenceIdKey ?: PropertyKey("RECURRENCE-ID", RecurrenceIdCodec()).also { recurrenceIdKey = it }

    @Volatile
    private var relatedToKey: PropertyKey<RelatedTo>? = null

    /**
     * The key of the `RELATED-TO` property
     * ([RFC 5545 §3.8.4.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.5)), read as a [RelatedTo].
     */
    @JvmStatic
    public val RELATED_TO: PropertyKey<RelatedTo>
        get() = relatedToKey ?: PropertyKey("RELATED-TO", RelatedToCodec()).also { relatedToKey = it }

    @Volatile
    private var urlKey: PropertyKey<String>? = null

    /**
     * The key of the `URL` property ([RFC 5545 §3.8.4.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.6)), read
     * as a URI.
     */
    @JvmStatic
    public val URL: PropertyKey<String>
        get() = urlKey ?: PropertyKey("URL", UriCodec()).also { urlKey = it }

    @Volatile
    private var uidKey: PropertyKey<String>? = null

    /**
     * The key of the `UID` property ([RFC 5545 §3.8.4.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.4.7)), read
     * as text.
     */
    @JvmStatic
    public val UID: PropertyKey<String>
        get() = uidKey ?: PropertyKey("UID", TextCodec()).also { uidKey = it }

    @Volatile
    private var exdateKey: PropertyKey<ExDate>? = null

    /**
     * The key of the `EXDATE` property ([RFC 5545 §3.8.5.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.1)),
     * read as a [ExDate].
     */
    @JvmStatic
    public val EXDATE: PropertyKey<ExDate>
        get() = exdateKey ?: PropertyKey("EXDATE", ExDateCodec()).also { exdateKey = it }

    @Volatile
    private var rdateKey: PropertyKey<RDate>? = null

    /**
     * The key of the `RDATE` property ([RFC 5545 §3.8.5.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.2)),
     * read as a [RDate].
     */
    @JvmStatic
    public val RDATE: PropertyKey<RDate>
        get() = rdateKey ?: PropertyKey("RDATE", RDateCodec()).also { rdateKey = it }

    @Volatile
    private var rruleKey: PropertyKey<Recur>? = null

    /**
     * The key of the `RRULE` property ([RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3)),
     * read as a [Recur].
     */
    @JvmStatic
    public val RRULE: PropertyKey<Recur>
        get() = rruleKey ?: PropertyKey("RRULE", RecurCodec()).also { rruleKey = it }

    @Volatile
    private var actionKey: PropertyKey<AlarmAction>? = null

    /**
     * The key of the `ACTION` property ([RFC 5545 §3.8.6.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.1)),
     * read as a [AlarmAction].
     */
    @JvmStatic
    public val ACTION: PropertyKey<AlarmAction>
        get() = actionKey ?: PropertyKey("ACTION", AlarmActionCodec()).also { actionKey = it }

    @Volatile
    private var repeatKey: PropertyKey<Int>? = null

    /**
     * The key of the `REPEAT` property ([RFC 5545 §3.8.6.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.2)),
     * read as an integer.
     */
    @JvmStatic
    public val REPEAT: PropertyKey<Int>
        get() = repeatKey ?: PropertyKey("REPEAT", IntegerCodec()).also { repeatKey = it }

    @Volatile
    private var triggerKey: PropertyKey<Trigger>? = null

    /**
     * The key of the `TRIGGER` property ([RFC 5545 §3.8.6.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.6.3)),
     * read as a [Trigger].
     */
    @JvmStatic
    public val TRIGGER: PropertyKey<Trigger>
        get() = triggerKey ?: PropertyKey("TRIGGER", TriggerCodec()).also { triggerKey = it }

    @Volatile
    private var createdKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `CREATED` property ([RFC 5545 §3.8.7.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.1)),
     * read as a UTC date-time.
     */
    @JvmStatic
    public val CREATED: PropertyKey<ICalDateTime>
        get() = createdKey ?: PropertyKey("CREATED", UtcDateTimeCodec()).also { createdKey = it }

    @Volatile
    private var dtstampKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `DTSTAMP` property ([RFC 5545 §3.8.7.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.2)),
     * read as a UTC date-time.
     */
    @JvmStatic
    public val DTSTAMP: PropertyKey<ICalDateTime>
        get() = dtstampKey ?: PropertyKey("DTSTAMP", UtcDateTimeCodec()).also { dtstampKey = it }

    @Volatile
    private var lastModifiedKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `LAST-MODIFIED` property
     * ([RFC 5545 §3.8.7.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.3)), read as a UTC date-time.
     */
    @JvmStatic
    public val LAST_MODIFIED: PropertyKey<ICalDateTime>
        get() = lastModifiedKey ?: PropertyKey("LAST-MODIFIED", UtcDateTimeCodec()).also { lastModifiedKey = it }

    @Volatile
    private var sequenceKey: PropertyKey<Int>? = null

    /**
     * The key of the `SEQUENCE` property ([RFC 5545 §3.8.7.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.7.4)),
     * read as an integer.
     */
    @JvmStatic
    public val SEQUENCE: PropertyKey<Int>
        get() = sequenceKey ?: PropertyKey("SEQUENCE", IntegerCodec()).also { sequenceKey = it }

    @Volatile
    private var requestStatusKey: PropertyKey<RequestStatus>? = null

    /**
     * The key of the `REQUEST-STATUS` property
     * ([RFC 5545 §3.8.8.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.8.3)), read as a [RequestStatus].
     */
    @JvmStatic
    public val REQUEST_STATUS: PropertyKey<RequestStatus>
        get() = requestStatusKey ?: PropertyKey("REQUEST-STATUS", RequestStatusCodec()).also { requestStatusKey = it }

    @Volatile
    private var nameKey: PropertyKey<String>? = null

    /**
     * The key of the `NAME` property ([RFC 7986 §5.1](https://www.rfc-editor.org/rfc/rfc7986#section-5.1)), read as
     * text.
     */
    @JvmStatic
    public val NAME: PropertyKey<String>
        get() = nameKey ?: PropertyKey("NAME", TextCodec()).also { nameKey = it }

    @Volatile
    private var refreshIntervalKey: PropertyKey<ICalDuration>? = null

    /**
     * The key of the `REFRESH-INTERVAL` property ([RFC 7986 §5.7](https://www.rfc-editor.org/rfc/rfc7986#section-5.7)),
     * read as a [ICalDuration].
     */
    @JvmStatic
    public val REFRESH_INTERVAL: PropertyKey<ICalDuration>
        get() = refreshIntervalKey
            ?: PropertyKey("REFRESH-INTERVAL", DeclaredDurationCodec()).also { refreshIntervalKey = it }

    @Volatile
    private var sourceKey: PropertyKey<String>? = null

    /**
     * The key of the `SOURCE` property ([RFC 7986 §5.8](https://www.rfc-editor.org/rfc/rfc7986#section-5.8)), read as a
     * URI.
     */
    @JvmStatic
    public val SOURCE: PropertyKey<String>
        get() = sourceKey ?: PropertyKey("SOURCE", UriCodec()).also { sourceKey = it }

    @Volatile
    private var colorKey: PropertyKey<String>? = null

    /**
     * The key of the `COLOR` property ([RFC 7986 §5.9](https://www.rfc-editor.org/rfc/rfc7986#section-5.9)), read as
     * text.
     */
    @JvmStatic
    public val COLOR: PropertyKey<String>
        get() = colorKey ?: PropertyKey("COLOR", TextCodec()).also { colorKey = it }

    @Volatile
    private var imageKey: PropertyKey<Image>? = null

    /**
     * The key of the `IMAGE` property ([RFC 7986 §5.10](https://www.rfc-editor.org/rfc/rfc7986#section-5.10)), read as
     * a [Image].
     */
    @JvmStatic
    public val IMAGE: PropertyKey<Image>
        get() = imageKey ?: PropertyKey("IMAGE", ImageCodec()).also { imageKey = it }

    @Volatile
    private var conferenceKey: PropertyKey<Conference>? = null

    /**
     * The key of the `CONFERENCE` property ([RFC 7986 §5.11](https://www.rfc-editor.org/rfc/rfc7986#section-5.11)),
     * read as a [Conference].
     */
    @JvmStatic
    public val CONFERENCE: PropertyKey<Conference>
        get() = conferenceKey ?: PropertyKey("CONFERENCE", ConferenceCodec()).also { conferenceKey = it }

    @Volatile
    private var locationTypeKey: PropertyKey<List<String>>? = null

    /**
     * The key of the `LOCATION-TYPE` property ([RFC 9073 §6.1](https://www.rfc-editor.org/rfc/rfc9073#section-6.1)),
     * read as a list of texts.
     */
    @JvmStatic
    public val LOCATION_TYPE: PropertyKey<List<String>>
        get() = locationTypeKey ?: PropertyKey("LOCATION-TYPE", TextListCodec()).also { locationTypeKey = it }

    @Volatile
    private var participantTypeKey: PropertyKey<ParticipantType>? = null

    /**
     * The key of the `PARTICIPANT-TYPE` property ([RFC 9073 §6.2](https://www.rfc-editor.org/rfc/rfc9073#section-6.2)),
     * read as a [ParticipantType].
     */
    @JvmStatic
    public val PARTICIPANT_TYPE: PropertyKey<ParticipantType>
        get() = participantTypeKey
            ?: PropertyKey("PARTICIPANT-TYPE", ParticipantTypeCodec()).also { participantTypeKey = it }

    @Volatile
    private var resourceTypeKey: PropertyKey<ResourceType>? = null

    /**
     * The key of the `RESOURCE-TYPE` property ([RFC 9073 §6.3](https://www.rfc-editor.org/rfc/rfc9073#section-6.3)),
     * read as a [ResourceType].
     */
    @JvmStatic
    public val RESOURCE_TYPE: PropertyKey<ResourceType>
        get() = resourceTypeKey ?: PropertyKey("RESOURCE-TYPE", ResourceTypeCodec()).also { resourceTypeKey = it }

    @Volatile
    private var calendarAddressKey: PropertyKey<String>? = null

    /**
     * The key of the `CALENDAR-ADDRESS` property ([RFC 9073 §6.4](https://www.rfc-editor.org/rfc/rfc9073#section-6.4)),
     * read as a calendar address.
     */
    @JvmStatic
    public val CALENDAR_ADDRESS: PropertyKey<String>
        get() = calendarAddressKey
            ?: PropertyKey("CALENDAR-ADDRESS", CalAddressCodec()).also { calendarAddressKey = it }

    @Volatile
    private var styledDescriptionKey: PropertyKey<StyledDescription>? = null

    /**
     * The key of the `STYLED-DESCRIPTION` property
     * ([RFC 9073 §6.5](https://www.rfc-editor.org/rfc/rfc9073#section-6.5)), read as a [StyledDescription].
     */
    @JvmStatic
    public val STYLED_DESCRIPTION: PropertyKey<StyledDescription>
        get() = styledDescriptionKey
            ?: PropertyKey("STYLED-DESCRIPTION", StyledDescriptionCodec()).also { styledDescriptionKey = it }

    @Volatile
    private var structuredDataKey: PropertyKey<StructuredData>? = null

    /**
     * The key of the `STRUCTURED-DATA` property ([RFC 9073 §6.6](https://www.rfc-editor.org/rfc/rfc9073#section-6.6)),
     * read as a [StructuredData].
     */
    @JvmStatic
    public val STRUCTURED_DATA: PropertyKey<StructuredData>
        get() = structuredDataKey
            ?: PropertyKey("STRUCTURED-DATA", StructuredDataCodec()).also { structuredDataKey = it }

    @Volatile
    private var acknowledgedKey: PropertyKey<ICalDateTime>? = null

    /**
     * The key of the `ACKNOWLEDGED` property ([RFC 9074 §6.1](https://www.rfc-editor.org/rfc/rfc9074#section-6.1)),
     * read as a UTC date-time.
     */
    @JvmStatic
    public val ACKNOWLEDGED: PropertyKey<ICalDateTime>
        get() = acknowledgedKey ?: PropertyKey("ACKNOWLEDGED", UtcDateTimeCodec()).also { acknowledgedKey = it }

    @Volatile
    private var proximityKey: PropertyKey<Proximity>? = null

    /**
     * The key of the `PROXIMITY` property ([RFC 9074 §8.1](https://www.rfc-editor.org/rfc/rfc9074#section-8.1)), read
     * as a [Proximity].
     */
    @JvmStatic
    public val PROXIMITY: PropertyKey<Proximity>
        get() = proximityKey ?: PropertyKey("PROXIMITY", ProximityCodec()).also { proximityKey = it }

    @Volatile
    private var conceptKey: PropertyKey<String>? = null

    /**
     * The key of the `CONCEPT` property ([RFC 9253 §8.1](https://www.rfc-editor.org/rfc/rfc9253#section-8.1)), read as
     * a URI.
     */
    @JvmStatic
    public val CONCEPT: PropertyKey<String>
        get() = conceptKey ?: PropertyKey("CONCEPT", UriCodec()).also { conceptKey = it }

    @Volatile
    private var linkKey: PropertyKey<Link>? = null

    /**
     * The key of the `LINK` property ([RFC 9253 §8.2](https://www.rfc-editor.org/rfc/rfc9253#section-8.2)), read as a
     * [Link].
     */
    @JvmStatic
    public val LINK: PropertyKey<Link>
        get() = linkKey ?: PropertyKey("LINK", LinkCodec()).also { linkKey = it }

    @Volatile
    private var refidKey: PropertyKey<String>? = null

    /**
     * The key of the `REFID` property ([RFC 9253 §8.3](https://www.rfc-editor.org/rfc/rfc9253#section-8.3)), read as
     * text.
     */
    @JvmStatic
    public val REFID: PropertyKey<String>
        get() = refidKey ?: PropertyKey("REFID", TextCodec()).also { refidKey = it }
}
