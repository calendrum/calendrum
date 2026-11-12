package io.github.calendrum.model

import io.github.calendrum.value.ICalDuration
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

/**
 * Typed keys for the standard parameters, to read with `property[Parameters.PARTSTAT]`.
 *
 * The keys are created on first use and shared. For an unknown parameter use `property.parameter(name)` of the syntax
 * layer.
 */
public object Parameters {
    @Volatile
    private var altrepKey: ParameterKey<String>? = null

    /**
     * The key of the `ALTREP` parameter ([RFC 5545 §3.2.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.1)): the
     * alternate text representation, a URI such as an HTML version of a text, read as text.
     */
    @JvmStatic
    public val ALTREP: ParameterKey<String>
        get() = altrepKey
            ?: ParameterKey<String>("ALTREP", StandardParameterCodec(PARAMETER_URI)).also { altrepKey = it }

    @Volatile
    private var cnKey: ParameterKey<String>? = null

    /**
     * The key of the `CN` parameter ([RFC 5545 §3.2.2](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.2)): the
     * common (display) name of a calendar user, read as text.
     */
    @JvmStatic
    public val CN: ParameterKey<String>
        get() = cnKey ?: ParameterKey<String>("CN", StandardParameterCodec(PARAMETER_TEXT)).also { cnKey = it }

    @Volatile
    private var cutypeKey: ParameterKey<CuType>? = null

    /**
     * The key of the `CUTYPE` parameter ([RFC 5545 §3.2.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.3)): the
     * type of a calendar user: individual, group, resource, room or unknown, read as a [CuType].
     */
    @JvmStatic
    public val CUTYPE: ParameterKey<CuType>
        get() = cutypeKey
            ?: ParameterKey<CuType>("CUTYPE", StandardParameterCodec(PARAMETER_CUTYPE)).also { cutypeKey = it }

    @Volatile
    private var delegatedFromKey: ParameterKey<List<String>>? = null

    /**
     * The key of the `DELEGATED-FROM` parameter
     * ([RFC 5545 §3.2.4](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.4)): the calendar addresses that delegated
     * their attendance to this user, read as a list of texts.
     */
    @JvmStatic
    public val DELEGATED_FROM: ParameterKey<List<String>>
        get() = delegatedFromKey
            ?: ParameterKey<List<String>>("DELEGATED-FROM", StandardParameterCodec(PARAMETER_LIST)).also {
                delegatedFromKey =
                    it
            }

    @Volatile
    private var delegatedToKey: ParameterKey<List<String>>? = null

    /**
     * The key of the `DELEGATED-TO` parameter
     * ([RFC 5545 §3.2.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.5)): the calendar addresses this user
     * delegated their attendance to, read as a list of texts.
     */
    @JvmStatic
    public val DELEGATED_TO: ParameterKey<List<String>>
        get() = delegatedToKey
            ?: ParameterKey<List<String>>("DELEGATED-TO", StandardParameterCodec(PARAMETER_LIST)).also {
                delegatedToKey =
                    it
            }

    @Volatile
    private var dirKey: ParameterKey<String>? = null

    /**
     * The key of the `DIR` parameter ([RFC 5545 §3.2.6](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.6)): a URI
     * of a directory entry about the calendar user, read as text.
     */
    @JvmStatic
    public val DIR: ParameterKey<String>
        get() = dirKey ?: ParameterKey<String>("DIR", StandardParameterCodec(PARAMETER_URI)).also { dirKey = it }

    @Volatile
    private var encodingKey: ParameterKey<Encoding>? = null

    /**
     * The key of the `ENCODING` parameter ([RFC 5545 §3.2.7](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.7)):
     * how an inline value is encoded (`8BIT` or `BASE64`), read as a [Encoding].
     */
    @JvmStatic
    public val ENCODING: ParameterKey<Encoding>
        get() = encodingKey
            ?: ParameterKey<Encoding>("ENCODING", StandardParameterCodec(PARAMETER_ENCODING)).also { encodingKey = it }

    @Volatile
    private var fmttypeKey: ParameterKey<String>? = null

    /**
     * The key of the `FMTTYPE` parameter ([RFC 5545 §3.2.8](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.8)): the
     * media type of an attachment or image, read as text.
     */
    @JvmStatic
    public val FMTTYPE: ParameterKey<String>
        get() = fmttypeKey
            ?: ParameterKey<String>("FMTTYPE", StandardParameterCodec(PARAMETER_TEXT)).also { fmttypeKey = it }

    @Volatile
    private var fbtypeKey: ParameterKey<FbType>? = null

    /**
     * The key of the `FBTYPE` parameter ([RFC 5545 §3.2.9](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.9)): the
     * free/busy type of a `FREEBUSY` value, read as a [FbType].
     */
    @JvmStatic
    public val FBTYPE: ParameterKey<FbType>
        get() = fbtypeKey
            ?: ParameterKey<FbType>("FBTYPE", StandardParameterCodec(PARAMETER_FBTYPE)).also { fbtypeKey = it }

    @Volatile
    private var languageKey: ParameterKey<String>? = null

    /**
     * The key of the `LANGUAGE` parameter ([RFC 5545 §3.2.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.10)):
     * the language of a text value, read as text.
     */
    @JvmStatic
    public val LANGUAGE: ParameterKey<String>
        get() = languageKey
            ?: ParameterKey<String>("LANGUAGE", StandardParameterCodec(PARAMETER_TEXT)).also { languageKey = it }

    @Volatile
    private var memberKey: ParameterKey<List<String>>? = null

    /**
     * The key of the `MEMBER` parameter ([RFC 5545 §3.2.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.11)):
     * the groups the calendar user belongs to, read as a list of texts.
     */
    @JvmStatic
    public val MEMBER: ParameterKey<List<String>>
        get() = memberKey
            ?: ParameterKey<List<String>>("MEMBER", StandardParameterCodec(PARAMETER_LIST)).also { memberKey = it }

    @Volatile
    private var partstatKey: ParameterKey<PartStat>? = null

    /**
     * The key of the `PARTSTAT` parameter ([RFC 5545 §3.2.12](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.12)):
     * the participation status of a calendar user, read as a [PartStat].
     */
    @JvmStatic
    public val PARTSTAT: ParameterKey<PartStat>
        get() = partstatKey
            ?: ParameterKey<PartStat>("PARTSTAT", StandardParameterCodec(PARAMETER_PARTSTAT)).also { partstatKey = it }

    @Volatile
    private var rangeKey: ParameterKey<RecurrenceRange>? = null

    /**
     * The key of the `RANGE` parameter ([RFC 5545 §3.2.13](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.13)): the
     * range of instances a recurrence override applies to, read as a [RecurrenceRange].
     */
    @JvmStatic
    public val RANGE: ParameterKey<RecurrenceRange>
        get() = rangeKey
            ?: ParameterKey<RecurrenceRange>("RANGE", StandardParameterCodec(PARAMETER_RANGE)).also { rangeKey = it }

    @Volatile
    private var relatedKey: ParameterKey<Related>? = null

    /**
     * The key of the `RELATED` parameter ([RFC 5545 §3.2.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.14)):
     * which end of the parent component a relative trigger is measured from, read as a [Related].
     */
    @JvmStatic
    public val RELATED: ParameterKey<Related>
        get() = relatedKey
            ?: ParameterKey<Related>("RELATED", StandardParameterCodec(PARAMETER_RELATED)).also { relatedKey = it }

    @Volatile
    private var reltypeKey: ParameterKey<RelType>? = null

    /**
     * The key of the `RELTYPE` parameter ([RFC 5545 §3.2.15](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.15)):
     * the type of a relationship, read as a [RelType].
     */
    @JvmStatic
    public val RELTYPE: ParameterKey<RelType>
        get() = reltypeKey
            ?: ParameterKey<RelType>("RELTYPE", StandardParameterCodec(PARAMETER_RELTYPE)).also { reltypeKey = it }

    @Volatile
    private var roleKey: ParameterKey<Role>? = null

    /**
     * The key of the `ROLE` parameter ([RFC 5545 §3.2.16](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.16)): the
     * role of a calendar user in the event, read as a [Role].
     */
    @JvmStatic
    public val ROLE: ParameterKey<Role>
        get() = roleKey ?: ParameterKey<Role>("ROLE", StandardParameterCodec(PARAMETER_ROLE)).also { roleKey = it }

    @Volatile
    private var rsvpKey: ParameterKey<Boolean>? = null

    /**
     * The key of the `RSVP` parameter ([RFC 5545 §3.2.17](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.17)):
     * whether a reply is requested, read as a flag.
     */
    @JvmStatic
    public val RSVP: ParameterKey<Boolean>
        get() = rsvpKey
            ?: ParameterKey<Boolean>("RSVP", StandardParameterCodec(PARAMETER_BOOLEAN)).also { rsvpKey = it }

    @Volatile
    private var sentByKey: ParameterKey<String>? = null

    /**
     * The key of the `SENT-BY` parameter ([RFC 5545 §3.2.18](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.18)):
     * the calendar user who acts on behalf of the user, read as text.
     */
    @JvmStatic
    public val SENT_BY: ParameterKey<String>
        get() = sentByKey
            ?: ParameterKey<String>("SENT-BY", StandardParameterCodec(PARAMETER_URI)).also { sentByKey = it }

    @Volatile
    private var tzidKey: ParameterKey<String>? = null

    /**
     * The key of the `TZID` parameter ([RFC 5545 §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)): the
     * time zone identifier of a local date-time, read as text.
     */
    @JvmStatic
    public val TZID: ParameterKey<String>
        get() = tzidKey ?: ParameterKey<String>("TZID", StandardParameterCodec(PARAMETER_TEXT)).also { tzidKey = it }

    @Volatile
    private var valueKey: ParameterKey<ValueType>? = null

    /**
     * The key of the `VALUE` parameter ([RFC 5545 §3.2.20](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.20)): the
     * value type of a property, when it is not the default, read as a [ValueType].
     */
    @JvmStatic
    public val VALUE: ParameterKey<ValueType>
        get() = valueKey
            ?: ParameterKey<ValueType>("VALUE", StandardParameterCodec(PARAMETER_VALUE)).also { valueKey = it }

    @Volatile
    private var emailKey: ParameterKey<String>? = null

    /**
     * The key of the `EMAIL` parameter ([RFC 7986 §6.2](https://www.rfc-editor.org/rfc/rfc7986#section-6.2)): an e-mail
     * address of the calendar user, read as text.
     */
    @JvmStatic
    public val EMAIL: ParameterKey<String>
        get() = emailKey ?: ParameterKey<String>("EMAIL", StandardParameterCodec(PARAMETER_TEXT)).also { emailKey = it }

    @Volatile
    private var displayKey: ParameterKey<List<ImageDisplay>>? = null

    /**
     * The key of the `DISPLAY` parameter ([RFC 7986 §6.1](https://www.rfc-editor.org/rfc/rfc7986#section-6.1)): how an
     * image is meant to be shown, read as a [List<ImageDisplay>].
     */
    @JvmStatic
    public val DISPLAY: ParameterKey<List<ImageDisplay>>
        get() = displayKey
            ?: ParameterKey("DISPLAY", TokenListParameterCodec { ImageDisplay.of(it) }).also { displayKey = it }

    @Volatile
    private var featureKey: ParameterKey<List<ConferenceFeature>>? = null

    /**
     * The key of the `FEATURE` parameter ([RFC 7986 §6.3](https://www.rfc-editor.org/rfc/rfc7986#section-6.3)): the
     * features of a conference, read as a [List<ConferenceFeature>].
     */
    @JvmStatic
    public val FEATURE: ParameterKey<List<ConferenceFeature>>
        get() = featureKey
            ?: ParameterKey("FEATURE", TokenListParameterCodec { ConferenceFeature.of(it) }).also { featureKey = it }

    @Volatile
    private var labelKey: ParameterKey<String>? = null

    /**
     * The key of the `LABEL` parameter ([RFC 7986 §6.4](https://www.rfc-editor.org/rfc/rfc7986#section-6.4)): a
     * human-readable label for a URI value, read as text.
     */
    @JvmStatic
    public val LABEL: ParameterKey<String>
        get() = labelKey ?: ParameterKey<String>("LABEL", StandardParameterCodec(PARAMETER_TEXT)).also { labelKey = it }

    @Volatile
    private var orderKey: ParameterKey<Int>? = null

    /**
     * The key of the `ORDER` parameter ([RFC 9073 §5.1](https://www.rfc-editor.org/rfc/rfc9073#section-5.1)): the order
     * of an item among several of the same property, read as an integer.
     */
    @JvmStatic
    public val ORDER: ParameterKey<Int>
        get() = orderKey ?: ParameterKey("ORDER", OrderParameterCodec()).also { orderKey = it }

    @Volatile
    private var schemaKey: ParameterKey<String>? = null

    /**
     * The key of the `SCHEMA` parameter ([RFC 9073 §5.2](https://www.rfc-editor.org/rfc/rfc9073#section-5.2)): the
     * schema URI of structured data, read as text.
     */
    @JvmStatic
    public val SCHEMA: ParameterKey<String>
        get() = schemaKey
            ?: ParameterKey<String>("SCHEMA", StandardParameterCodec(PARAMETER_URI)).also { schemaKey = it }

    @Volatile
    private var derivedKey: ParameterKey<Boolean>? = null

    /**
     * The key of the `DERIVED` parameter ([RFC 9073 §5.3](https://www.rfc-editor.org/rfc/rfc9073#section-5.3)): whether
     * a value is derived from other data and not authoritative, read as a flag.
     */
    @JvmStatic
    public val DERIVED: ParameterKey<Boolean>
        get() = derivedKey
            ?: ParameterKey<Boolean>("DERIVED", StandardParameterCodec(PARAMETER_BOOLEAN)).also { derivedKey = it }

    @Volatile
    private var linkrelKey: ParameterKey<String>? = null

    /**
     * The key of the `LINKREL` parameter ([RFC 9253 §6.1](https://www.rfc-editor.org/rfc/rfc9253#section-6.1)): the
     * relation type of a link, read as text.
     */
    @JvmStatic
    public val LINKREL: ParameterKey<String>
        get() = linkrelKey
            ?: ParameterKey<String>("LINKREL", StandardParameterCodec(PARAMETER_TEXT)).also { linkrelKey = it }

    @Volatile
    private var gapKey: ParameterKey<ICalDuration>? = null

    /**
     * The key of the `GAP` parameter ([RFC 9253 §6.2](https://www.rfc-editor.org/rfc/rfc9253#section-6.2)): the gap
     * between a related component's end and this one's start, read as a [ICalDuration].
     */
    @JvmStatic
    public val GAP: ParameterKey<ICalDuration>
        get() = gapKey ?: ParameterKey("GAP", DurationParameterCodec()).also { gapKey = it }
}
