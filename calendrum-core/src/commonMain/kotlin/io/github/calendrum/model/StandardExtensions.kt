@file:OptIn(ExperimentalICalApi::class)

package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi

/**
 * The definitions of [RFC 5545](https://www.rfc-editor.org/rfc/rfc5545): every standard property, parameter and
 * component, with value types, allowed parameters and cardinality per component.
 */
public object Rfc5545 : ICalExtension {
    /** Adds the definitions of this standard to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(Properties.CALSCALE, "TEXT", "", "VCALENDAR ?", "RFC 5545 §3.7.1")
        registry.define(Properties.METHOD, "TEXT", "", "VCALENDAR ?", "RFC 5545 §3.7.2")
        registry.define(Properties.PRODID, "TEXT", "", "VCALENDAR 1", "RFC 5545 §3.7.3")
        registry.define(Properties.VERSION, "TEXT", "", "VCALENDAR 1", "RFC 5545 §3.7.4")
        registry.define(
            Properties.ATTACH,
            "URI BINARY",
            "FMTTYPE ENCODING VALUE",
            "VEVENT * VTODO * VJOURNAL * VALARM *",
            "RFC 5545 §3.8.1.1",
        )
        registry.define(Properties.CATEGORIES, "TEXT", "LANGUAGE", "VEVENT * VTODO * VJOURNAL *", "RFC 5545 §3.8.1.2")
        registry.define(Properties.CLASS, "TEXT", "", "VEVENT ? VTODO ? VJOURNAL ?", "RFC 5545 §3.8.1.3")
        registry.define(
            Properties.COMMENT,
            "TEXT",
            "ALTREP LANGUAGE",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * STANDARD * DAYLIGHT *",
            "RFC 5545 §3.8.1.4",
        )
        registry.define(
            Properties.DESCRIPTION,
            "TEXT",
            "ALTREP LANGUAGE",
            "VEVENT ? VTODO ? VJOURNAL * VALARM 1",
            "RFC 5545 §3.8.1.5",
        )
        registry.define(Properties.GEO, "FLOAT", "", "VEVENT ? VTODO ?", "RFC 5545 §3.8.1.6")
        registry.define(Properties.LOCATION, "TEXT", "ALTREP LANGUAGE", "VEVENT ? VTODO ?", "RFC 5545 §3.8.1.7")
        registry.define(Properties.PERCENT_COMPLETE, "INTEGER", "", "VTODO ?", "RFC 5545 §3.8.1.8")
        registry.define(Properties.PRIORITY, "INTEGER", "", "VEVENT ? VTODO ?", "RFC 5545 §3.8.1.9")
        registry.define(Properties.RESOURCES, "TEXT", "ALTREP LANGUAGE", "VEVENT * VTODO *", "RFC 5545 §3.8.1.10")
        registry.define(Properties.EVENT_STATUS, "TEXT", "", "VEVENT ?", "RFC 5545 §3.8.1.11")
        registry.define(Properties.TODO_STATUS, "TEXT", "", "VTODO ?", "RFC 5545 §3.8.1.11")
        registry.define(Properties.JOURNAL_STATUS, "TEXT", "", "VJOURNAL ?", "RFC 5545 §3.8.1.11")
        registry.define(
            Properties.SUMMARY,
            "TEXT",
            "ALTREP LANGUAGE",
            "VEVENT ? VTODO ? VJOURNAL ? VALARM 1",
            "RFC 5545 §3.8.1.12",
        )
        registry.define(Properties.COMPLETED, "DATE-TIME", "", "VTODO ?", "RFC 5545 §3.8.2.1")
        registry.define(Properties.DTEND, "DATE-TIME DATE", "VALUE TZID", "VEVENT ? VFREEBUSY ?", "RFC 5545 §3.8.2.2")
        registry.define(Properties.DUE, "DATE-TIME DATE", "VALUE TZID", "VTODO ?", "RFC 5545 §3.8.2.3")
        registry.define(
            Properties.DTSTART,
            "DATE-TIME DATE",
            "VALUE TZID",
            "VEVENT ? VTODO ? VJOURNAL ? VFREEBUSY ? STANDARD 1 DAYLIGHT 1",
            "RFC 5545 §3.8.2.4",
        )
        registry.define(Properties.DURATION, "DURATION", "", "VEVENT ? VTODO ? VALARM ?", "RFC 5545 §3.8.2.5")
        registry.define(Properties.FREEBUSY, "PERIOD", "FBTYPE", "VFREEBUSY *", "RFC 5545 §3.8.2.6")
        registry.define(Properties.TRANSP, "TEXT", "", "VEVENT ?", "RFC 5545 §3.8.2.7")
        registry.define(Properties.TZID, "TEXT", "", "VTIMEZONE 1", "RFC 5545 §3.8.3.1")
        registry.define(Properties.TZNAME, "TEXT", "LANGUAGE", "STANDARD * DAYLIGHT *", "RFC 5545 §3.8.3.2")
        registry.define(Properties.TZOFFSETFROM, "UTC-OFFSET", "", "STANDARD 1 DAYLIGHT 1", "RFC 5545 §3.8.3.3")
        registry.define(Properties.TZOFFSETTO, "UTC-OFFSET", "", "STANDARD 1 DAYLIGHT 1", "RFC 5545 §3.8.3.4")
        registry.define(Properties.TZURL, "URI", "", "VTIMEZONE ?", "RFC 5545 §3.8.3.5")
        registry.define(
            Properties.ATTENDEE,
            "CAL-ADDRESS",
            "CUTYPE MEMBER ROLE PARTSTAT RSVP DELEGATED-TO DELEGATED-FROM SENT-BY CN DIR LANGUAGE",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VALARM +",
            "RFC 5545 §3.8.4.1",
        )
        registry.define(
            Properties.CONTACT,
            "TEXT",
            "ALTREP LANGUAGE",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY ?",
            "RFC 5545 §3.8.4.2",
        )
        registry.define(
            Properties.ORGANIZER,
            "CAL-ADDRESS",
            "CN DIR SENT-BY LANGUAGE",
            "VEVENT ? VTODO ? VJOURNAL ? VFREEBUSY ?",
            "RFC 5545 §3.8.4.3",
        )
        registry.define(
            Properties.RECURRENCE_ID,
            "DATE-TIME DATE",
            "VALUE TZID RANGE",
            "VEVENT ? VTODO ? VJOURNAL ?",
            "RFC 5545 §3.8.4.4",
        )
        registry.define(Properties.RELATED_TO, "TEXT", "RELTYPE", "VEVENT * VTODO * VJOURNAL *", "RFC 5545 §3.8.4.5")
        registry.define(Properties.URL, "URI", "", "VEVENT ? VTODO ? VJOURNAL ? VFREEBUSY ?", "RFC 5545 §3.8.4.6")
        registry.define(Properties.UID, "TEXT", "", "VEVENT 1 VTODO 1 VJOURNAL 1 VFREEBUSY 1", "RFC 5545 §3.8.4.7")
        registry.define(
            Properties.EXDATE,
            "DATE-TIME DATE",
            "VALUE TZID",
            "VEVENT * VTODO * VJOURNAL *",
            "RFC 5545 §3.8.5.1",
        )
        registry.define(
            Properties.RDATE,
            "DATE-TIME DATE PERIOD",
            "VALUE TZID",
            "VEVENT * VTODO * VJOURNAL * STANDARD * DAYLIGHT *",
            "RFC 5545 §3.8.5.2",
        )
        registry.define(
            Properties.RRULE,
            "RECUR",
            "",
            "VEVENT ? VTODO ? VJOURNAL ? STANDARD ? DAYLIGHT ?",
            "RFC 5545 §3.8.5.3",
        )
        registry.define(Properties.ACTION, "TEXT", "", "VALARM 1", "RFC 5545 §3.8.6.1")
        registry.define(Properties.REPEAT, "INTEGER", "", "VALARM ?", "RFC 5545 §3.8.6.2")
        registry.define(Properties.TRIGGER, "DURATION DATE-TIME", "VALUE RELATED", "VALARM 1", "RFC 5545 §3.8.6.3")
        registry.define(Properties.CREATED, "DATE-TIME", "", "VEVENT ? VTODO ? VJOURNAL ?", "RFC 5545 §3.8.7.1")
        registry.define(
            Properties.DTSTAMP,
            "DATE-TIME",
            "",
            "VEVENT 1 VTODO 1 VJOURNAL 1 VFREEBUSY 1",
            "RFC 5545 §3.8.7.2",
        )
        registry.define(
            Properties.LAST_MODIFIED,
            "DATE-TIME",
            "",
            "VEVENT ? VTODO ? VJOURNAL ? VTIMEZONE ?",
            "RFC 5545 §3.8.7.3",
        )
        registry.define(Properties.SEQUENCE, "INTEGER", "", "VEVENT ? VTODO ? VJOURNAL ?", "RFC 5545 §3.8.7.4")
        registry.define(
            Properties.REQUEST_STATUS,
            "TEXT",
            "LANGUAGE",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY *",
            "RFC 5545 §3.8.8.3",
        )
        registry.parameter(ParameterDefinition(Parameters.ALTREP, "RFC 5545 §3.2.1"))
        registry.parameter(ParameterDefinition(Parameters.CN, "RFC 5545 §3.2.2"))
        registry.parameter(ParameterDefinition(Parameters.CUTYPE, "RFC 5545 §3.2.3"))
        registry.parameter(ParameterDefinition(Parameters.DELEGATED_FROM, "RFC 5545 §3.2.4"))
        registry.parameter(ParameterDefinition(Parameters.DELEGATED_TO, "RFC 5545 §3.2.5"))
        registry.parameter(ParameterDefinition(Parameters.DIR, "RFC 5545 §3.2.6"))
        registry.parameter(ParameterDefinition(Parameters.ENCODING, "RFC 5545 §3.2.7"))
        registry.parameter(ParameterDefinition(Parameters.FMTTYPE, "RFC 5545 §3.2.8"))
        registry.parameter(ParameterDefinition(Parameters.FBTYPE, "RFC 5545 §3.2.9"))
        registry.parameter(ParameterDefinition(Parameters.LANGUAGE, "RFC 5545 §3.2.10"))
        registry.parameter(ParameterDefinition(Parameters.MEMBER, "RFC 5545 §3.2.11"))
        registry.parameter(ParameterDefinition(Parameters.PARTSTAT, "RFC 5545 §3.2.12"))
        registry.parameter(ParameterDefinition(Parameters.RANGE, "RFC 5545 §3.2.13"))
        registry.parameter(ParameterDefinition(Parameters.RELATED, "RFC 5545 §3.2.14"))
        registry.parameter(ParameterDefinition(Parameters.RELTYPE, "RFC 5545 §3.2.15"))
        registry.parameter(ParameterDefinition(Parameters.ROLE, "RFC 5545 §3.2.16"))
        registry.parameter(ParameterDefinition(Parameters.RSVP, "RFC 5545 §3.2.17"))
        registry.parameter(ParameterDefinition(Parameters.SENT_BY, "RFC 5545 §3.2.18"))
        registry.parameter(ParameterDefinition(Parameters.TZID, "RFC 5545 §3.2.19"))
        registry.parameter(ParameterDefinition(Parameters.VALUE, "RFC 5545 §3.2.20"))
        registry.define("VCALENDAR", "VEVENT VTODO VJOURNAL VFREEBUSY VTIMEZONE", "RFC 5545 §3.6")
        registry.define("VEVENT", "VALARM", "RFC 5545 §3.6.1")
        registry.define("VTODO", "VALARM", "RFC 5545 §3.6.2")
        registry.define("VJOURNAL", "", "RFC 5545 §3.6.3")
        registry.define("VFREEBUSY", "", "RFC 5545 §3.6.4")
        registry.define("VTIMEZONE", "STANDARD DAYLIGHT", "RFC 5545 §3.6.5")
        registry.define("STANDARD", "", "RFC 5545 §3.6.5")
        registry.define("DAYLIGHT", "", "RFC 5545 §3.6.5")
        registry.define("VALARM", "", "RFC 5545 §3.6.6")
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Rfc5545"
}

/**
 * The definitions of [RFC 7986](https://www.rfc-editor.org/rfc/rfc7986): `NAME`, `REFRESH-INTERVAL`, `SOURCE`, `COLOR`,
 * `IMAGE`, `CONFERENCE` and the calendar-level `UID`, `URL`, `DESCRIPTION` and `CATEGORIES`, with the parameters
 * `DISPLAY`, `EMAIL`, `FEATURE` and `LABEL`.
 */
public object Rfc7986 : ICalExtension {
    /** Adds the definitions of this standard to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(Properties.NAME, "TEXT", "ALTREP LANGUAGE", "VCALENDAR *", "RFC 7986 §5.1")
        registry.define(Properties.DESCRIPTION, "TEXT", "ALTREP LANGUAGE", "VCALENDAR *", "RFC 7986 §5.2")
        registry.define(Properties.UID, "TEXT", "", "VCALENDAR ?", "RFC 7986 §5.3")
        registry.define(Properties.LAST_MODIFIED, "DATE-TIME", "", "VCALENDAR ?", "RFC 7986 §5.4")
        registry.define(Properties.URL, "URI", "", "VCALENDAR ?", "RFC 7986 §5.5")
        registry.define(Properties.CATEGORIES, "TEXT", "LANGUAGE", "VCALENDAR *", "RFC 7986 §5.6")
        registry.define(Properties.REFRESH_INTERVAL, "DURATION", "VALUE", "VCALENDAR ?", "RFC 7986 §5.7")
        registry.define(Properties.SOURCE, "URI", "", "VCALENDAR ?", "RFC 7986 §5.8")
        registry.define(Properties.COLOR, "TEXT", "", "VCALENDAR ? VEVENT ? VTODO ? VJOURNAL ?", "RFC 7986 §5.9")
        registry.define(
            Properties.IMAGE,
            "URI BINARY",
            "VALUE ENCODING FMTTYPE ALTREP DISPLAY",
            "VCALENDAR * VEVENT * VTODO * VJOURNAL *",
            "RFC 7986 §5.10",
        )
        registry.define(
            Properties.CONFERENCE,
            "URI",
            "VALUE FEATURE LABEL LANGUAGE",
            "VEVENT * VTODO *",
            "RFC 7986 §5.11",
        )
        registry.parameter(ParameterDefinition(Parameters.DISPLAY, "RFC 7986 §6.1"))
        registry.parameter(ParameterDefinition(Parameters.EMAIL, "RFC 7986 §6.2"))
        registry.parameter(ParameterDefinition(Parameters.FEATURE, "RFC 7986 §6.3"))
        registry.parameter(ParameterDefinition(Parameters.LABEL, "RFC 7986 §6.4"))
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Rfc7986"
}

/**
 * The definitions of [RFC 9073](https://www.rfc-editor.org/rfc/rfc9073): the components `PARTICIPANT`, `VLOCATION` and
 * `VRESOURCE`, the properties `LOCATION-TYPE`, `PARTICIPANT-TYPE`, `RESOURCE-TYPE`, `CALENDAR-ADDRESS`,
 * `STYLED-DESCRIPTION` and `STRUCTURED-DATA`, and the parameters `ORDER`, `SCHEMA` and `DERIVED`.
 */
public object Rfc9073 : ICalExtension {
    /** Adds the definitions of this standard to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(Properties.LOCATION_TYPE, "TEXT", "", "VLOCATION ?", "RFC 9073 §6.1")
        registry.define(Properties.PARTICIPANT_TYPE, "TEXT", "", "PARTICIPANT 1", "RFC 9073 §6.2")
        registry.define(Properties.RESOURCE_TYPE, "TEXT", "", "VRESOURCE ?", "RFC 9073 §6.3")
        registry.define(Properties.CALENDAR_ADDRESS, "CAL-ADDRESS", "", "PARTICIPANT ?", "RFC 9073 §6.4")
        registry.define(
            Properties.STYLED_DESCRIPTION,
            "TEXT URI",
            "VALUE ALTREP LANGUAGE FMTTYPE DERIVED",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VALARM * PARTICIPANT *",
            "RFC 9073 §6.5",
        )
        registry.define(
            Properties.STRUCTURED_DATA,
            "TEXT BINARY URI",
            "VALUE ENCODING FMTTYPE SCHEMA",
            "VEVENT * VTODO * VJOURNAL * PARTICIPANT * VLOCATION * VRESOURCE *",
            "RFC 9073 §6.6",
        )
        registry.define(Properties.ATTACH, "URI BINARY", "FMTTYPE ENCODING VALUE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.CATEGORIES, "TEXT", "LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.COMMENT, "TEXT", "ALTREP LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(
            Properties.DESCRIPTION,
            "TEXT",
            "ALTREP LANGUAGE",
            "PARTICIPANT ? VLOCATION ? VRESOURCE ?",
            "RFC 9073 §7",
        )
        registry.define(Properties.GEO, "FLOAT", "", "PARTICIPANT ? VLOCATION ? VRESOURCE ?", "RFC 9073 §7")
        registry.define(Properties.LOCATION, "TEXT", "ALTREP LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.PRIORITY, "INTEGER", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.RESOURCES, "TEXT", "ALTREP LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.EVENT_STATUS, "TEXT", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.SUMMARY, "TEXT", "ALTREP LANGUAGE", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.CONTACT, "TEXT", "ALTREP LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.RELATED_TO, "TEXT", "RELTYPE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.URL, "URI", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.UID, "TEXT", "", "PARTICIPANT 1 VLOCATION 1 VRESOURCE 1", "RFC 9073 §7")
        registry.define(Properties.CREATED, "DATE-TIME", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.DTSTAMP, "DATE-TIME", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.LAST_MODIFIED, "DATE-TIME", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.SEQUENCE, "INTEGER", "", "PARTICIPANT ?", "RFC 9073 §7")
        registry.define(Properties.REQUEST_STATUS, "TEXT", "LANGUAGE", "PARTICIPANT *", "RFC 9073 §7")
        registry.define(Properties.NAME, "TEXT", "ALTREP LANGUAGE", "VLOCATION ? VRESOURCE ?", "RFC 9073 §7")
        registry.parameter(ParameterDefinition(Parameters.ORDER, "RFC 9073 §5.1"))
        registry.parameter(ParameterDefinition(Parameters.SCHEMA, "RFC 9073 §5.2"))
        registry.parameter(ParameterDefinition(Parameters.DERIVED, "RFC 9073 §5.3"))
        registry.define("PARTICIPANT", "VLOCATION VRESOURCE", "RFC 9073 §7.1")
        registry.define("VLOCATION", "", "RFC 9073 §7.2")
        registry.define("VRESOURCE", "", "RFC 9073 §7.3")
        registry.define("VEVENT", "PARTICIPANT VLOCATION VRESOURCE", "RFC 9073 §4")
        registry.define("VTODO", "PARTICIPANT VLOCATION VRESOURCE", "RFC 9073 §4")
        registry.define("VJOURNAL", "PARTICIPANT VLOCATION VRESOURCE", "RFC 9073 §4")
        registry.define("VFREEBUSY", "PARTICIPANT VLOCATION VRESOURCE", "RFC 9073 §4")
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Rfc9073"
}

/**
 * The definitions of [RFC 9074](https://www.rfc-editor.org/rfc/rfc9074): the alarm properties `ACKNOWLEDGED` and
 * `PROXIMITY` and the use of `UID` and `RELATED-TO` in `VALARM`.
 */
public object Rfc9074 : ICalExtension {
    /** Adds the definitions of this standard to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(Properties.UID, "TEXT", "", "VALARM ?", "RFC 9074 §4")
        registry.define(Properties.RELATED_TO, "TEXT", "RELTYPE", "VALARM *", "RFC 9074 §5")
        registry.define(Properties.ACKNOWLEDGED, "DATE-TIME", "", "VALARM ?", "RFC 9074 §6.1")
        registry.define(Properties.PROXIMITY, "TEXT", "", "VALARM ?", "RFC 9074 §8.1")
        registry.define(Properties.URL, "URI", "", "VLOCATION ?", "RFC 9074 §8")
        registry.parameter(ParameterDefinition(Parameters.RELTYPE, "RFC 9074 §7.1"))
        registry.define("VALARM", "VLOCATION", "RFC 9074 §8")
        registry.define("VLOCATION", "", "RFC 9074 §8")
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Rfc9074"
}

/**
 * The definitions of [RFC 9253](https://www.rfc-editor.org/rfc/rfc9253): the properties `LINK`, `CONCEPT` and `REFID`,
 * the parameters `LINKREL` and `GAP`, and the new relation types.
 */
public object Rfc9253 : ICalExtension {
    /** Adds the definitions of this standard to [registry]. */
    override fun contribute(registry: ICalRegistry.Builder) {
        registry.define(
            Properties.CONCEPT,
            "URI",
            "",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VTIMEZONE * STANDARD * DAYLIGHT * VALARM * PARTICIPANT * VLOCATION * VRESOURCE *",
            "RFC 9253 §8.1",
        )
        registry.define(
            Properties.LINK,
            "URI UID XML-REFERENCE",
            "VALUE LINKREL FMTTYPE LABEL LANGUAGE",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VTIMEZONE * STANDARD * DAYLIGHT * VALARM * PARTICIPANT * VLOCATION * VRESOURCE *",
            "RFC 9253 §8.2",
        )
        registry.define(
            Properties.REFID,
            "TEXT",
            "",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VTIMEZONE * STANDARD * DAYLIGHT * VALARM * PARTICIPANT * VLOCATION * VRESOURCE *",
            "RFC 9253 §8.3",
        )
        registry.define(
            Properties.RELATED_TO,
            "TEXT URI UID",
            "VALUE RELTYPE GAP",
            "VEVENT * VTODO * VJOURNAL * VFREEBUSY * VTIMEZONE * STANDARD * DAYLIGHT * VALARM * PARTICIPANT * VLOCATION * VRESOURCE *",
            "RFC 9253 §9.1",
        )
        registry.parameter(ParameterDefinition(Parameters.LINKREL, "RFC 9253 §6.1"))
        registry.parameter(ParameterDefinition(Parameters.GAP, "RFC 9253 §6.2"))
        registry.parameter(ParameterDefinition(Parameters.RELTYPE, "RFC 9253 §4"))
        registry.parameter(ParameterDefinition(Parameters.RELTYPE, "RFC 9253 §5"))
        registry.parameter(ParameterDefinition(Parameters.VALUE, "RFC 9253 §7"))
    }

    /** Returns a short description for diagnostics; its format is not a contract. */
    override fun toString(): String = "Rfc9253"
}

// One definition per row of `spec/properties.csv`: value types (the first is the default), parameters and `<component>
// <code>` pairs as space-separated words, so each extension reads like its rows; `RegistrySpecTest` checks the rows
// against the definitions.
internal fun ICalRegistry.Builder.define(
    key: PropertyKey<*>,
    types: String,
    parameters: String,
    cardinality: String,
    source: String,
) {
    val typeNames = words(types)
    val allowed = LinkedHashSet<ValueType>()
    for (name in typeNames) allowed += ValueType.of(name)
    val components = words(cardinality)
    val map = LinkedHashMap<String, Cardinality>()
    var index = 0
    while (index + 1 < components.size) {
        map[components[index]] = cardinalityOf(components[index + 1])
        index += 2
    }
    property(PropertyDefinition(key, ValueType.of(typeNames[0]), allowed, words(parameters).toSet(), map, source))
}

internal fun ICalRegistry.Builder.define(component: String, children: String, source: String) {
    component(ComponentDefinition(component, words(children).toSet(), source))
}

private fun cardinalityOf(code: String): Cardinality = when (code) {
    "1" -> Cardinality.One
    "?" -> Cardinality.Optional
    "+" -> Cardinality.OneOrMore
    else -> Cardinality.Many
}

private fun words(text: String): List<String> {
    val result = ArrayList<String>()
    var start = 0
    while (start < text.length) {
        var end = start
        while (end < text.length && text[end] != ' ') end++
        if (end > start) result += text.substring(start, end)
        start = end + 1
    }
    return result
}
