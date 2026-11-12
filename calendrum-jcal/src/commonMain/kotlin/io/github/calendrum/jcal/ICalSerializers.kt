package io.github.calendrum.jcal

import io.github.calendrum.Problem
import io.github.calendrum.Severity
import io.github.calendrum.model.DecodeContext
import io.github.calendrum.model.Decoded
import io.github.calendrum.model.ICalRegistry
import io.github.calendrum.model.Properties
import io.github.calendrum.model.PropertyKey
import io.github.calendrum.syntax.ICalComponent
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.Recur
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlin.jvm.JvmStatic

// String forms that any format can hold: dates and date-times as in jCal, a zoned one with the TZID in brackets
// (`2026-06-01T10:00:00[Europe/Berlin]`), DURATION and RECUR as in iCalendar, a period as start/end or start/duration;
// values are parsed strictly by the codecs of the core. A component is jCal and needs the JSON format.
/**
 * Serializers for kotlinx-serialization that read and write the values of the library as their jCal text, without a
 * compiler plugin or reflection.
 *
 * Use them explicitly: `Json.encodeToString(ICalSerializers.dateTime, start)`. Values are single JSON strings, except
 * [component], which is the jCal array of a component and works only with the JSON format.
 *
 * @sample io.github.calendrum.samples.jcalSerializersForKotlinxSerialization
 */
public object ICalSerializers {
    /**
     * Serializes a date or date-time as its jCal string: `2026-10-01`, `2026-10-01T10:00:00` (floating) or
     * `2026-10-01T07:00:00Z` (UTC). A zoned value gets its zone in brackets, `2026-10-01T10:00:00[Europe/Berlin]`, so
     * no value is lost (the bracket form is this library's extension; standard jCal puts the zone in the `tzid`
     * parameter).
     */
    @get:JvmStatic
    public val dateTime: KSerializer<ICalDateTime> get() = DateTimeSerializer

    /** Serializes a duration as its iCalendar text, such as `PT1H30M`. */
    @get:JvmStatic
    public val duration: KSerializer<ICalDuration> get() = DurationSerializer

    /** Serializes a recurrence rule as its iCalendar text, such as `FREQ=WEEKLY;COUNT=10`. */
    @get:JvmStatic
    public val recur: KSerializer<Recur> get() = RecurSerializer

    /**
     * Serializes a period as `start/end` or `start/duration`, with the start and the end in the form of [dateTime].
     */
    @get:JvmStatic
    public val period: KSerializer<ICalPeriod> get() = PeriodSerializer

    /**
     * Serializes a component as its jCal array (`["vevent", [...properties], [...components]]`). It works only with the
     * JSON format; encoding throws `IllegalArgumentException` on what [JCal.encodeToString] cannot write (a control
     * character, a parameter without a value), and decoding fails with a `SerializationException` when the input is not
     * a valid jCal component.
     */
    @get:JvmStatic
    public val component: KSerializer<ICalComponent> get() = ComponentSerializer

    /** Returns the name of the object. */
    override fun toString(): String = "ICalSerializers"
}

internal object DateTimeSerializer : KSerializer<ICalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.calendrum.value.ICalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ICalDateTime) {
        encoder.encodeString(serialDateTime(value))
    }

    override fun deserialize(decoder: Decoder): ICalDateTime = parseDateTime(decoder.decodeString())
        ?: throw SerializationException("Not a jCal date or date-time")

    override fun toString(): String = "ICalSerializers.dateTime"
}

internal object DurationSerializer : KSerializer<ICalDuration> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.calendrum.value.ICalDuration", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ICalDuration) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ICalDuration =
        decodeValue(Properties.DURATION, decoder.decodeString()) ?: throw SerializationException("Not a duration")

    override fun toString(): String = "ICalSerializers.duration"
}

internal object RecurSerializer : KSerializer<Recur> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.calendrum.value.Recur", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Recur) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Recur =
        decodeValue(Properties.RRULE, decoder.decodeString()) ?: throw SerializationException("Not a recurrence rule")

    override fun toString(): String = "ICalSerializers.recur"
}

internal object PeriodSerializer : KSerializer<ICalPeriod> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.calendrum.value.ICalPeriod", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ICalPeriod) {
        val end = value.end
        encoder.encodeString(
            serialDateTime(value.start) + "/" + (if (end != null) serialDateTime(end) else value.duration),
        )
    }

    override fun deserialize(decoder: Decoder): ICalPeriod =
        parsePeriod(decoder.decodeString()) ?: throw SerializationException("Not a period")

    override fun toString(): String = "ICalSerializers.period"
}

internal object ComponentSerializer : KSerializer<ICalComponent> {
    override val descriptor: SerialDescriptor = JsonArray.serializer().descriptor

    override fun serialize(encoder: Encoder, value: ICalComponent) {
        val json =
            encoder as? JsonEncoder ?: throw SerializationException("An ICalComponent is written only as jCal JSON")
        json.encodeJsonElement(JCalWriter(ICalRegistry.Default).component(value))
    }

    override fun deserialize(decoder: Decoder): ICalComponent {
        val json =
            decoder as? JsonDecoder ?: throw SerializationException("An ICalComponent is read only from jCal JSON")
        val problems = ArrayList<Problem>()
        val component = JCalReader(ICalRegistry.Default, problems, JCalOptions.Default.maxDepth)
            .component(json.decodeJsonElement(), "$", 1)
        val error = problems.firstOrNull { it.severity == Severity.Error }
        if (component == null || error != null) {
            throw SerializationException("Not a jCal component: ${(error ?: problems.firstOrNull())?.code?.id}")
        }
        return component
    }

    override fun toString(): String = "ICalSerializers.component"
}

internal fun serialDateTime(value: ICalDateTime): String =
    if (value is ICalDateTime.Zoned) jcalDateTime(value) + "[" + value.tzid + "]" else jcalDateTime(value)

internal fun parseDateTime(text: String): ICalDateTime? {
    val bracket = text.indexOf('[')
    val tzid = if (bracket >= 0 && text.endsWith("]")) text.substring(bracket + 1, text.length - 1) else null
    val local = if (bracket >= 0) text.substring(0, bracket) else text
    if (bracket >= 0 && (tzid.isNullOrEmpty() || local.endsWith("Z"))) return null
    val ical = (if (local.length == 10) jcalDateToIcal(local) else jcalDateTimeToIcal(local)) ?: return null
    val property = ICalProperty.builder("DTSTART")
    if (local.length == 10) property.parameter("VALUE", "DATE")
    if (tzid != null) property.parameter("TZID", tzid)
    return try {
        decodeProperty(Properties.DTSTART, property.value(ical).build())
    } catch (_: IllegalArgumentException) {
        null
    }
}

private fun parsePeriod(text: String): ICalPeriod? {
    val slash = text.indexOf('/')
    if (slash < 0) return null
    val start = parseDateTime(text.substring(0, slash)) ?: return null
    val rest = text.substring(slash + 1)
    if (isDuration(rest)) {
        val duration = decodeValue(Properties.DURATION, rest) ?: return null
        return ICalPeriod(start, null, duration)
    }
    val end = parseDateTime(rest) ?: return null
    return ICalPeriod(start, end, null)
}

private fun <T : Any> decodeValue(key: PropertyKey<T>, text: String): T? = try {
    decodeProperty(key, ICalProperty.of(key.name, text))
} catch (_: IllegalArgumentException) {
    null
}

private fun <T : Any> decodeProperty(key: PropertyKey<T>, property: ICalProperty): T? {
    val decoded = key.codec.decode(property, DecodeContext.Strict)
    return if (decoded is Decoded.Value) decoded.value else null
}
