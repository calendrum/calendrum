package io.github.calendrum.model

import io.github.calendrum.ExperimentalICalApi
import io.github.calendrum.ProblemCode
import io.github.calendrum.syntax.ArrayView
import io.github.calendrum.syntax.ContentReader
import io.github.calendrum.syntax.ICalParameter
import io.github.calendrum.syntax.ICalProperty
import io.github.calendrum.value.BinaryValue
import io.github.calendrum.value.DateTimeValue
import io.github.calendrum.value.ICalDateTime
import io.github.calendrum.value.ICalDuration
import io.github.calendrum.value.ICalPeriod
import io.github.calendrum.value.PeriodValue
import io.github.calendrum.value.Recur
import io.github.calendrum.value.ValueDecoder
import io.github.calendrum.value.decodeBinary
import io.github.calendrum.value.decodeDate
import io.github.calendrum.value.decodeDateList
import io.github.calendrum.value.decodeDateTime
import io.github.calendrum.value.decodeDateTimeList
import io.github.calendrum.value.decodeDuration
import io.github.calendrum.value.decodeFloat
import io.github.calendrum.value.decodeInteger
import io.github.calendrum.value.decodePeriodList
import io.github.calendrum.value.decodeRecur
import io.github.calendrum.value.decodeText
import io.github.calendrum.value.decodeTextList
import io.github.calendrum.value.decodeUri
import io.github.calendrum.value.decodeUtcOffset
import io.github.calendrum.value.encodeBinary
import io.github.calendrum.value.encodeDateTime
import io.github.calendrum.value.encodeDateTimeList
import io.github.calendrum.value.encodeDuration
import io.github.calendrum.value.encodePeriodList
import io.github.calendrum.value.encodeRecur
import io.github.calendrum.value.encodeText
import io.github.calendrum.value.encodeTextList
import io.github.calendrum.value.encodeUtcOffset
import io.github.calendrum.value.toPublic
import io.github.calendrum.value.toValue
import io.github.calendrum.value.tzidOrNull
import kotlinx.datetime.UtcOffset

// The base of the built-in property codecs: one subclass per kind of value (`TextCodec`, `DateTimeCodec`,
// `UtcOffsetCodec`…), not a `when` on a kind: a key reaches only the decoder of its own kind, so an application carries
// the codecs of the keys it reads and nothing else.
@OptIn(ExperimentalICalApi::class)
internal abstract class StandardCodec<T : Any> : PropertyCodec<T> {
    final override fun decode(property: ICalProperty, context: DecodeContext): Decoded<T> {
        val run = NodeRun(property, context)
        return run.finish(read(run))
    }

    internal fun decodeLine(reader: ContentReader, context: DecodeContext): Decoded<T> {
        val run = LineRun(reader, context)
        return run.finish(read(run))
    }

    abstract fun read(run: CodecRun): T?

    override fun toString(): String = "StandardCodec"
}

internal class TextCodec : StandardCodec<String>() {
    override fun read(run: CodecRun): String? = if (run.valueType(TYPE_TEXT, TYPE_TEXT) ==
        REJECTED
    ) {
        null
    } else {
        run.text()
    }

    override fun encode(value: String, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeText(value))
}

internal class UriCodec : StandardCodec<String>() {
    override fun read(run: CodecRun): String? = if (run.valueType(TYPE_URI, TYPE_URI) == REJECTED) null else run.uri()

    override fun encode(value: String, context: EncodeContext): ICalProperty = ICalProperty.of(context.name, value)
}

internal class IntegerCodec : StandardCodec<Int>() {
    override fun read(run: CodecRun): Int? = run.integer()

    override fun encode(value: Int, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, value.toString())
}

internal class DateTimeCodec : StandardCodec<ICalDateTime>() {
    override fun read(run: CodecRun): ICalDateTime? = run.dateTime()?.toPublic()

    override fun encode(value: ICalDateTime, context: EncodeContext): ICalProperty =
        encodeDateTimeProperty(context.name, value)
}

internal class UtcDateTimeCodec : StandardCodec<ICalDateTime>() {
    override fun read(run: CodecRun): ICalDateTime? = run.utcDateTime()?.toPublic()

    override fun encode(value: ICalDateTime, context: EncodeContext): ICalProperty =
        encodeDateTimeProperty(context.name, value)
}

internal class DurationCodec : StandardCodec<ICalDuration>() {
    override fun read(run: CodecRun): ICalDuration? = run.duration()

    override fun encode(value: ICalDuration, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeDuration(value.value))
}

internal class RecurCodec : StandardCodec<Recur>() {
    override fun read(run: CodecRun): Recur? = run.recur()

    override fun encode(value: Recur, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeRecur(value.value))
}

internal class UtcOffsetCodec : StandardCodec<UtcOffset>() {
    override fun read(run: CodecRun): UtcOffset? = run.offsetSeconds()?.let { UtcOffset(seconds = it) }

    override fun encode(value: UtcOffset, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeUtcOffset(value.totalSeconds))
}

internal class OffsetSecondsCodec : StandardCodec<Int>() {
    override fun read(run: CodecRun): Int? = run.offsetSeconds()

    override fun encode(value: Int, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeUtcOffset(value))
}

internal abstract class TokenCodec<E : Any> : StandardCodec<E>() {
    final override fun read(run: CodecRun): E? {
        if (run.valueType(TYPE_TEXT, TYPE_TEXT) == REJECTED) return null
        val value = run.text() ?: return null
        if (value.isEmpty()) {
            run.report(ProblemCode.InvalidValue, 0)
            return null
        }
        return of(value)
    }

    abstract fun of(value: String): E

    final override fun encode(value: E, context: EncodeContext): ICalProperty =
        ICalProperty.of(context.name, encodeText(value.toString()))
}

internal class ClassificationCodec : TokenCodec<Classification>() {
    override fun of(value: String): Classification = Classification.of(value)
}

internal class EventStatusCodec : TokenCodec<EventStatus>() {
    override fun of(value: String): EventStatus = EventStatus.of(value)
}

internal class TodoStatusCodec : TokenCodec<TodoStatus>() {
    override fun of(value: String): TodoStatus = TodoStatus.of(value)
}

internal class JournalStatusCodec : TokenCodec<JournalStatus>() {
    override fun of(value: String): JournalStatus = JournalStatus.of(value)
}

internal class TransparencyCodec : TokenCodec<Transparency>() {
    override fun of(value: String): Transparency = Transparency.of(value)
}

internal class AlarmActionCodec : TokenCodec<AlarmAction>() {
    override fun of(value: String): AlarmAction = AlarmAction.of(value)
}

internal class MethodCodec : TokenCodec<Method>() {
    override fun of(value: String): Method = Method.of(value)
}

internal class AttachCodec : StandardCodec<Attach>() {
    override fun read(run: CodecRun): Attach? = run.attach()

    override fun encode(value: Attach, context: EncodeContext): ICalProperty = encodeAttach(context.name, value)
}

internal class CategoriesCodec : StandardCodec<Categories>() {
    override fun read(run: CodecRun): Categories? =
        run.textList()?.let { Categories(it, run.text(LANGUAGE), run.parameters()) }

    override fun encode(value: Categories, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeTextList(value.values))
}

internal class ResourcesCodec : StandardCodec<Resources>() {
    override fun read(run: CodecRun): Resources? =
        run.textList()?.let { Resources(it, run.text(ALTREP), run.text(LANGUAGE), run.parameters()) }

    override fun encode(value: Resources, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeTextList(value.values))
}

internal class GeoCodec : StandardCodec<Geo>() {
    override fun read(run: CodecRun): Geo? = run.geo()

    override fun encode(value: Geo, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, value.latitudeText + ";" + value.longitudeText)
}

internal class LocationCodec : StandardCodec<Location>() {
    override fun read(run: CodecRun): Location? =
        run.text()?.let { Location(it, run.text(ALTREP), run.text(LANGUAGE), run.parameters()) }

    override fun encode(value: Location, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeText(value.text))
}

internal class ContactCodec : StandardCodec<Contact>() {
    override fun read(run: CodecRun): Contact? =
        run.text()?.let { Contact(it, run.text(ALTREP), run.text(LANGUAGE), run.parameters()) }

    override fun encode(value: Contact, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeText(value.text))
}

internal class AttendeeCodec : StandardCodec<Attendee>() {
    override fun read(run: CodecRun): Attendee? = run.attendee()

    override fun encode(value: Attendee, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, value.address)
}

internal class OrganizerCodec : StandardCodec<Organizer>() {
    override fun read(run: CodecRun): Organizer? = run.organizer()

    override fun encode(value: Organizer, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, value.address)
}

internal class RecurrenceIdCodec : StandardCodec<RecurrenceId>() {
    override fun read(run: CodecRun): RecurrenceId? = run.recurrenceId()

    override fun encode(value: RecurrenceId, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeDateTime(value.value.toValue()))
}

internal class RelatedToCodec : StandardCodec<RelatedTo>() {
    override fun read(run: CodecRun): RelatedTo? = run.relatedTo()

    override fun encode(value: RelatedTo, context: EncodeContext): ICalProperty = withParameters(
        context.name,
        value.parameters,
        if (isUriValue(value.parameters)) value.uid else encodeText(value.uid),
    )
}

internal class ExDateCodec : StandardCodec<ExDate>() {
    override fun read(run: CodecRun): ExDate? = run.exDate()

    override fun encode(value: ExDate, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodeDateTimeList(value.dates.map { it.toValue() }))
}

internal class RDateCodec : StandardCodec<RDate>() {
    override fun read(run: CodecRun): RDate? = run.rDate()

    override fun encode(value: RDate, context: EncodeContext): ICalProperty = encodeRDate(context.name, value)
}

internal class FreeBusyCodec : StandardCodec<FreeBusy>() {
    override fun read(run: CodecRun): FreeBusy? = run.freeBusy()

    override fun encode(value: FreeBusy, context: EncodeContext): ICalProperty =
        withParameters(context.name, value.parameters, encodePeriodList(value.periods.map { it.toValue() }))
}

internal class TriggerCodec : StandardCodec<Trigger>() {
    override fun read(run: CodecRun): Trigger? = run.trigger()

    override fun encode(value: Trigger, context: EncodeContext): ICalProperty = encodeTrigger(context.name, value)
}

internal class RequestStatusCodec : StandardCodec<RequestStatus>() {
    override fun read(run: CodecRun): RequestStatus? = run.requestStatus()

    override fun encode(value: RequestStatus, context: EncodeContext): ICalProperty =
        encodeRequestStatus(context.name, value)
}

internal fun CodecRun.text(): String? {
    val value = this.value
    return decoder.decodeText(value, 0, value.length)
}

internal fun CodecRun.textList(): List<String>? {
    if (valueType(TYPE_TEXT, TYPE_TEXT) == REJECTED) return null
    val value = this.value
    return decoder.decodeTextList(value, 0, value.length)
}

internal fun CodecRun.uri(): String? {
    val value = this.value
    return decoder.decodeUri(value, 0, value.length)
}

private fun CodecRun.integer(): Int? {
    if (valueType(TYPE_INTEGER, TYPE_INTEGER) == REJECTED) return null
    val value = this.value
    return decoder.decodeInteger(value, 0, value.length)
}

internal fun CodecRun.duration(): ICalDuration? {
    if (valueType(TYPE_DURATION, TYPE_DURATION) == REJECTED) return null
    val value = this.value
    return decoder.decodeDuration(value, 0, value.length)?.let(::ICalDuration)
}

private fun CodecRun.recur(): Recur? {
    if (valueType(TYPE_RECUR, TYPE_RECUR) == REJECTED) return null
    val value = this.value
    return decoder.decodeRecur(value, 0, value.length)?.let(::Recur)
}

private fun CodecRun.offsetSeconds(): Int? {
    if (valueType(TYPE_UTC_OFFSET, TYPE_UTC_OFFSET) == REJECTED) return null
    val value = this.value
    val seconds = decoder.decodeUtcOffset(value, 0, value.length) ?: return null
    if (seconds > MAX_OFFSET || seconds < -MAX_OFFSET) {
        report(ProblemCode.OutOfRange, 0)
        return null
    }
    return seconds
}

private fun CodecRun.dateTime(): DateTimeValue? {
    val type = valueType(TYPE_DATE_TIME or TYPE_DATE, TYPE_DATE_TIME)
    if (type == REJECTED) return null
    return temporal(type == TYPE_DATE)
}

internal fun CodecRun.utcDateTime(): DateTimeValue? {
    if (valueType(TYPE_DATE_TIME, TYPE_DATE_TIME) == REJECTED) return null
    val tzid = parameter(TZID)
    if (tzid != null && !acceptParameter(ProblemCode.ParameterNotAllowed, tzid)) return null
    val value = this.value
    val result = decoder.decodeDateTime(value, 0, value.length, null) ?: return null
    if (!result.isUtc && !accept(ProblemCode.NotUtc, 0)) return null
    return result
}

private fun CodecRun.temporal(date: Boolean): DateTimeValue? {
    val tzid = zone(date) ?: return null
    val value = this.value
    return if (date) {
        decoder.decodeDate(value, 0, value.length)
    } else {
        decoder.decodeDateTime(value, 0, value.length, tzid.ifEmpty { null })
    }
}

private fun CodecRun.zone(date: Boolean): String? {
    val tzid = parameter(TZID) ?: return ""
    if (!date) return tzid.value
    return if (acceptParameter(ProblemCode.ParameterNotAllowed, tzid)) "" else null
}

private fun CodecRun.attach(): Attach? {
    val type = valueType(TYPE_URI or TYPE_BINARY, TYPE_URI)
    if (type == REJECTED) return null
    val encoding = optionalToken(ENCODING) { Encoding.of(it) }
    val declared = declared()
    val value = this.value
    val formatType = text(FMTTYPE)
    val binary = type == TYPE_BINARY || (declared == NOT_DECLARED && encoding == Encoding.Base64)
    if (!binary) return decoder.decodeUri(value, 0, value.length)?.let { Attach.Uri(it, formatType, parameters()) }
    if (declared != TYPE_BINARY && !accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
    if (encoding != Encoding.Base64 && !accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
    val bytes = decoder.decodeBinary(value, 0, value.length) ?: return null
    return Attach.Binary(bytes.bytes, formatType, parameters())
}

private fun CodecRun.geo(): Geo? {
    if (valueType(TYPE_FLOAT, TYPE_FLOAT) == REJECTED) return null
    val value = this.value
    val separator = value.indexOf(';')
    if (separator < 0) {
        report(ProblemCode.InvalidValue, value.length)
        return null
    }
    val latitude = decoder.decodeFloat(value, 0, separator) ?: return null
    shift = separator + 1
    val longitude = decoder.decodeFloat(value, separator + 1, value.length)
    shift = 0
    if (longitude == null) return null
    val north = latitude.toDouble()
    val east = longitude.toDouble()
    if (north < -90.0 || north > 90.0) {
        report(ProblemCode.OutOfRange, 0)
        return null
    }
    if (east < -180.0 || east > 180.0) {
        report(ProblemCode.OutOfRange, separator + 1)
        return null
    }
    return Geo(north, east, latitude.text, longitude.text, parameters())
}

private fun CodecRun.attendee(): Attendee? {
    if (valueType(TYPE_CAL_ADDRESS, TYPE_CAL_ADDRESS) == REJECTED) return null
    val address = uri() ?: return null
    return Attendee(
        address,
        parameters(),
        text(CN),
        token(ROLE, Role.ReqParticipant) { Role.of(it) },
        token(PARTSTAT, PartStat.NeedsAction) { PartStat.of(it) },
        flag(RSVP),
        token(CUTYPE, CuType.Individual) { CuType.of(it) },
        texts(DELEGATED_TO),
        texts(DELEGATED_FROM),
        texts(MEMBER),
        text(SENT_BY),
        text(DIR),
        text(LANGUAGE),
        text(EMAIL),
    )
}

private fun CodecRun.organizer(): Organizer? {
    if (valueType(TYPE_CAL_ADDRESS, TYPE_CAL_ADDRESS) == REJECTED) return null
    val address = uri() ?: return null
    return Organizer(address, parameters(), text(CN), text(SENT_BY), text(DIR), text(LANGUAGE), text(EMAIL))
}

private fun CodecRun.recurrenceId(): RecurrenceId? {
    val value = dateTime() ?: return null
    return RecurrenceId(value.toPublic(), optionalToken(RANGE) { RecurrenceRange.of(it) }, parameters())
}

private fun CodecRun.relatedTo(): RelatedTo? {
    val type = valueType(TYPE_TEXT or TYPE_URI or TYPE_UID, TYPE_TEXT)
    if (type == REJECTED) return null
    val uid = (if (type == TYPE_URI) uri() else text()) ?: return null
    val gap = parameter(GAP)
    val duration = if (gap == null) null else gapOf(gap)
    return RelatedTo(uid, token(RELTYPE, RelType.Parent) { RelType.of(it) }, duration, parameters())
}

private fun CodecRun.gapOf(gap: ICalParameter): ICalDuration? {
    val value = gap.value
    val parsed = ValueDecoder(strict, false, null).decodeDuration(value, 0, value.length)
    if (parsed == null) reportParameter(ProblemCode.InvalidValue, gap)
    return parsed?.let(::ICalDuration)
}

private fun CodecRun.exDate(): ExDate? {
    val type = valueType(TYPE_DATE_TIME or TYPE_DATE, TYPE_DATE_TIME)
    if (type == REJECTED) return null
    val dates = dates(type == TYPE_DATE) ?: return null
    return ExDate(dates, parameters())
}

private fun CodecRun.dates(date: Boolean): List<ICalDateTime>? {
    val tzid = zone(date) ?: return null
    val value = this.value
    val items = if (date) {
        decoder.decodeDateList(value, 0, value.length)
    } else {
        decoder.decodeDateTimeList(value, 0, value.length, tzid.ifEmpty { null })
    } ?: return null
    return ArrayView(Array(items.size) { items[it].toPublic() })
}

private fun CodecRun.rDate(): RDate? {
    var type = valueType(TYPE_DATE_TIME or TYPE_DATE or TYPE_PERIOD, TYPE_DATE_TIME)
    if (type == REJECTED) return null
    val value = this.value
    if (declared() == NOT_DECLARED && value.indexOf('/') >= 0) {
        if (!accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
        type = TYPE_PERIOD
    }
    if (type != TYPE_PERIOD) {
        val dates = dates(type == TYPE_DATE) ?: return null
        return RDate(dates, emptyList(), parameters())
    }
    val periods = periods(zone(false) ?: return null) ?: return null
    return RDate(emptyList(), periods, parameters())
}

private fun CodecRun.periods(tzid: String): List<ICalPeriod>? {
    val value = this.value
    val items = decoder.decodePeriodList(value, 0, value.length, tzid.ifEmpty { null }) ?: return null
    return ArrayView(Array(items.size) { items[it].toPublic() })
}

private fun CodecRun.freeBusy(): FreeBusy? {
    if (valueType(TYPE_PERIOD, TYPE_PERIOD) == REJECTED) return null
    val value = this.value
    val items = decoder.decodePeriodList(value, 0, value.length, null) ?: return null
    if (items.any { !isUtc(it) } && !accept(ProblemCode.NotUtc, 0)) return null
    val periods = ArrayView(Array(items.size) { items[it].toPublic() })
    return FreeBusy(token(FBTYPE, FbType.Busy) { FbType.of(it) }, periods, parameters())
}

private fun isUtc(period: PeriodValue): Boolean = period.start.isUtc && (period.end?.isUtc ?: true)

private fun CodecRun.trigger(): Trigger? {
    val type = valueType(TYPE_DURATION or TYPE_DATE_TIME, TYPE_DURATION)
    if (type == REJECTED) return null
    val value = this.value
    var absolute = type == TYPE_DATE_TIME
    if (!absolute && declared() == NOT_DECLARED && value.isNotEmpty() && value[0] in '0'..'9') {
        if (!accept(ProblemCode.ValueTypeNotDeclared, 0)) return null
        absolute = true
    }
    if (!absolute) {
        val duration = decoder.decodeDuration(value, 0, value.length) ?: return null
        return Trigger.Relative(ICalDuration(duration), token(RELATED, Related.Start) { Related.of(it) }, parameters())
    }
    val related = parameter(RELATED)
    if (related != null && !acceptParameter(ProblemCode.ParameterNotAllowed, related)) return null
    val tzid = parameter(TZID)
    if (tzid != null && !acceptParameter(ProblemCode.ParameterNotAllowed, tzid)) return null
    val time = decoder.decodeDateTime(value, 0, value.length, null) ?: return null
    if (!time.isUtc && !accept(ProblemCode.NotUtc, 0)) return null
    return Trigger.Absolute(time.toPublic(), parameters())
}

private fun CodecRun.requestStatus(): RequestStatus? {
    if (valueType(TYPE_TEXT, TYPE_TEXT) == REJECTED) return null
    val value = this.value
    val first = separator(value, 0)
    if (!isStatusCode(value, first)) {
        report(ProblemCode.InvalidValue, 0)
        return null
    }
    if (first == value.length) {
        report(ProblemCode.InvalidValue, value.length)
        return null
    }
    val second = separator(value, first + 1)
    shift = first + 1
    val description = decoder.decodeText(value, first + 1, second)
    shift = second + 1
    val data = if (second < value.length) decoder.decodeText(value, second + 1, value.length) else null
    shift = 0
    if (description == null || (second < value.length && data == null)) return null
    return RequestStatus(value.substring(0, first), description, data, text(LANGUAGE), parameters())
}

private fun separator(value: String, from: Int): Int {
    var index = from
    while (index < value.length) {
        val char = value[index]
        if (char == '\\') {
            index += 2
            continue
        }
        if (char == ';') return index
        index += 1
    }
    return value.length
}

internal fun isStatusCode(value: String, end: Int): Boolean {
    var dots = 0
    var digits = 0
    for (index in 0 until end) {
        val char = value[index]
        if (char in '0'..'9') {
            digits += 1
            continue
        }
        if (char != '.' || digits == 0) return false
        dots += 1
        digits = 0
    }
    return digits > 0 && dots in 1..2
}

internal fun CodecRun.parameters(): List<ICalParameter> = parameterList

private fun encodeDateTimeProperty(name: String, value: ICalDateTime): ICalProperty {
    val builder = ICalProperty.builder(name)
    if (value is ICalDateTime.Date) builder.parameter(VALUE, "DATE")
    val tzid = value.tzidOrNull()
    if (tzid != null) builder.parameter(TZID, tzid)
    return builder.value(encodeDateTime(value.toValue())).build()
}

private fun encodeAttach(name: String, value: Attach): ICalProperty = when (value) {
    is Attach.Uri -> withParameters(name, value.parameters, value.uri)
    is Attach.Binary -> withParameters(name, value.parameters, encodeBinary(BinaryValue(value.bytesView)))
}

private fun encodeRDate(name: String, value: RDate): ICalProperty {
    val text = if (value.periods.isEmpty()) {
        encodeDateTimeList(value.dates.map { it.toValue() })
    } else {
        encodePeriodList(value.periods.map { it.toValue() })
    }
    return withParameters(name, value.parameters, text)
}

private fun encodeTrigger(name: String, value: Trigger): ICalProperty = when (value) {
    is Trigger.Relative -> withParameters(name, value.parameters, encodeDuration(value.duration.value))
    is Trigger.Absolute -> withParameters(name, value.parameters, encodeDateTime(value.dateTime.toValue()))
}

private fun encodeRequestStatus(name: String, value: RequestStatus): ICalProperty {
    val data = value.data
    val text = value.code + ";" + encodeText(value.description) + (if (data == null) "" else ";" + encodeText(data))
    return withParameters(name, value.parameters, text)
}

internal fun withParameters(name: String, parameters: List<ICalParameter>, value: String): ICalProperty {
    val builder = ICalProperty.builder(name)
    for (parameter in parameters) builder.parameter(parameter)
    return builder.value(value).build()
}

private const val MAX_OFFSET = 18 * 3600
internal const val ALTREP = "ALTREP"
internal const val CN = "CN"
internal const val CUTYPE = "CUTYPE"
internal const val DELEGATED_FROM = "DELEGATED-FROM"
internal const val DELEGATED_TO = "DELEGATED-TO"
internal const val DIR = "DIR"
internal const val EMAIL = "EMAIL"
internal const val ENCODING = "ENCODING"
internal const val FBTYPE = "FBTYPE"
internal const val FMTTYPE = "FMTTYPE"
internal const val LANGUAGE = "LANGUAGE"
internal const val MEMBER = "MEMBER"
internal const val PARTSTAT = "PARTSTAT"
internal const val RANGE = "RANGE"
internal const val RELATED = "RELATED"
internal const val RELTYPE = "RELTYPE"
internal const val ROLE = "ROLE"
internal const val RSVP = "RSVP"
internal const val SENT_BY = "SENT-BY"
internal const val TZID = "TZID"
internal const val DERIVED = "DERIVED"
internal const val DISPLAY = "DISPLAY"
internal const val FEATURE = "FEATURE"
internal const val GAP = "GAP"
internal const val LABEL = "LABEL"
internal const val LINKREL = "LINKREL"
internal const val ORDER = "ORDER"
internal const val SCHEMA = "SCHEMA"

internal fun isUriValue(parameters: List<ICalParameter>): Boolean {
    for (parameter in parameters) {
        if (parameter.name ==
            VALUE
        ) {
            return parameter.value.isNotEmpty() && typeBit(enumToken(parameter.value)) == TYPE_URI
        }
    }
    return false
}
