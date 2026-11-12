package io.github.calendrum

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * The stable identifier of a kind of [Problem], such as `ICAL-S003`, with its severity and fixed message.
 *
 * This is an open enumeration: every code the library emits is a constant of the companion, [of] returns the constant
 * for an identifier, and an identifier the library does not know yields a new code with severity [Severity.Warning].
 * Codes are equal when their [id] is equal. A code never changes its meaning and is never reused.
 *
 * The series are `ICAL-S` (syntax and encoding), `ICAL-V` (values), `ICAL-M` (model), `ICAL-R` (recurrence), `ICAL-Z`
 * (time zones), `ICAL-O` (occurrences), `ICAL-L` (validation and lint) and `ICAL-J` (jCal).
 */
public class ProblemCode internal constructor(
    /** The identifier of the code, for example `ICAL-S003`. */
    public val id: String,
    /** How serious the problems with this code are. */
    public val severity: Severity,
    internal val index: Int,
    internal val message: String,
    private val flags: Int,
) {
    internal val oncePerInput: Boolean get() = flags and ONCE_PER_INPUT != 0

    internal val bypassesLimit: Boolean get() = flags and BYPASSES_LIMIT != 0

    /** Returns `true` when [other] is a code with the same [id]. */
    override fun equals(other: Any?): Boolean = this === other || (other is ProblemCode && id == other.id)

    /** Returns a hash code over the [id]. */
    override fun hashCode(): Int = id.hashCode()

    /** Returns the [id], such as `ICAL-S003`. */
    override fun toString(): String = id

    /** The constants for every code of the library, and a lookup by identifier. */
    public companion object {
        private const val ONCE_PER_INPUT = 1
        private const val BYPASSES_LIMIT = 2
        private const val ID_PREFIX = "ICAL-S"
        private const val VALUE_ID_PREFIX = "ICAL-V"
        private const val MODEL_ID_PREFIX = "ICAL-M"
        private const val RECURRENCE_ID_PREFIX = "ICAL-R"
        private const val ZONE_ID_PREFIX = "ICAL-Z"
        private const val OCCURRENCE_ID_PREFIX = "ICAL-O"
        private const val LINT_ID_PREFIX = "ICAL-L"
        private const val JCAL_ID_PREFIX = "ICAL-J"
        private const val SEPARATOR = '|'

        // Only the `ICAL-S` codes are counted by the lexer; value codes have indexes after them and are counted by
        // eager decoding, so the counters of every parse do not grow with the value series.
        internal const val SYNTAX_COUNT: Int = 30

        private const val SEVERITIES = "IWWWWWEWWEEWWEWWWWWWWEEIWWWWEE"
        private const val FLAGS = "011100000020020000000022222222"
        private const val MESSAGES =
            "UTF-8 byte order mark skipped|" +
                "bare CR used as a line ending|" +
                "bare LF used as a line ending|" +
                "blank line skipped|" +
                "continuation line without a previous line|" +
                "invalid UTF-8 sequence replaced with U+FFFD|" +
                "no ':' after the property name and parameters, line skipped|" +
                "invalid or empty property or parameter name|" +
                "unclosed quoted parameter value|" +
                "logical line exceeds the length limit, line skipped|" +
                "unsupported encoding, input not read|" +
                "malformed parameter|" +
                "problem limit reached, further problems are only counted|" +
                "logical line limit reached, input truncated|" +
                "END without an open component, ignored|" +
                "END closes nested components implicitly|" +
                "END does not match any open component, ignored|" +
                "input ended with open components, closed implicitly|" +
                "empty or invalid component name in BEGIN or END|" +
                "parameters on BEGIN or END ignored|" +
                "property outside any component|" +
                "component nesting exceeds the depth limit, subtree skipped|" +
                "component limit reached, input truncated|" +
                "input decoded to UTF-8|" +
                "input encoding detected by a heuristic|" +
                "unknown charset hint ignored|" +
                "charset hint contradicts the data, the data decides|" +
                "low confidence in the detected encoding|" +
                "time budget exceeded, input truncated|" +
                "input size limit reached, input truncated|"

        private const val VALUE_SEVERITIES = "EEEWWWWWWWWWWWWWWWWWWWWWWWWW"
        private const val VALUE_MESSAGES =
            "value does not conform to its type, no value decoded|" +
                "date, time or number out of range|" +
                "recurrence rule without FREQ|" +
                "leap second 60 replaced|" +
                "extended ISO 8601 form with separators|" +
                "fractional seconds dropped|" +
                "DATE value with a time, time dropped|" +
                "DATE-TIME value without a time, read as DATE|" +
                "time units in DURATION without the T designator|" +
                "DURATION skips minutes between hours and seconds|" +
                "UTC offset with a colon|" +
                "negative zero UTC offset read as +0000|" +
                "whitespace around a value or list item ignored|" +
                "whitespace inside BINARY ignored|" +
                "BINARY without base64 padding|" +
                "empty list item or rule part skipped|" +
                "recurrence rule with both COUNT and UNTIL|" +
                "repeated rule part, the last one kept|" +
                "rule part not allowed in this rule|" +
                "unknown rule part kept|" +
                "URI without a scheme|" +
                "period does not end after its start|" +
                "unknown backslash escape in TEXT kept|" +
                "TZID on a UTC time ignored|" +
                "VALUE parameter names a type this property does not take|" +
                "value type not declared by its VALUE or ENCODING parameter|" +
                "value is not in UTC|" +
                "parameter does not apply to this value, ignored|"

        private const val MODEL_SEVERITIES = "E"
        private const val MODEL_MESSAGES = "required schema field missing or malformed, component skipped|"

        private const val RECURRENCE_SEVERITIES = "WWWWWIWII"
        private const val RECURRENCE_MESSAGES =
            "recurrence occurrence limit reached, expansion stopped|" +
                "recurrence rule matched nothing within the iteration limit, expansion stopped|" +
                "DTSTART is not an instance of the recurrence rule|" +
                "RSCALE calendar is not supported, rule not expanded|" +
                "UNTIL value type does not match DTSTART|" +
                "UTC UNTIL compared with local time of DTSTART, no time zone given|" +
                "time rule part ignored with a DATE start|" +
                "BYSECOND=60 has no local time, value ignored|" +
                "date-time of another form compared by local time|"

        private const val ZONE_SEVERITIES = "WIWWW"
        private const val ZONE_MESSAGES =
            "unknown TZID, the unknown zone policy applies|" +
                "TZID resolved through an alias|" +
                "VTIMEZONE cannot be compiled, ignored|" +
                "TZID resolved by a guess from its name|" +
                "current rules of the zone are not expressible by one RRULE, VTIMEZONE approximates them|"

        private const val OCCURRENCE_SEVERITIES = "WIWWW"
        private const val OCCURRENCE_MESSAGES =
            "RECURRENCE-ID matches no instance, kept as a single occurrence|" +
                "RANGE=THISANDFUTURE applied to later instances|" +
                "alarm repetitions beyond the limit dropped|" +
                "floating or all-day time without a floating zone, occurrence skipped|" +
                "component without a start, no occurrence|"

        private const val LINT_SEVERITIES = "EEWEIEEEEEEEEEEEEEIEWWIIWE"
        private const val LINT_MESSAGES =
            "required property missing|" +
                "property occurs more often than allowed|" +
                "property should not occur more than once|" +
                "property not allowed in this component|" +
                "unknown property|" +
                "component not allowed here|" +
                "required component missing|" +
                "mutually exclusive properties|" +
                "property requires another property|" +
                "value type differs from the related property|" +
                "end is not after the start|" +
                "duration with a time part for a date start|" +
                "value not allowed|" +
                "value out of range|" +
                "value must be a local time|" +
                "value does not decode|" +
                "TZID without a VTIMEZONE|" +
                "METHOD missing or different|" +
                "unknown alarm action, the alarm is ignored|" +
                "method not defined for this component|" +
                "X-WR-TIMEZONE with floating times|" +
                "TZID is not an IANA time zone|" +
                "X-WR-CALNAME or X-WR-CALDESC without NAME or DESCRIPTION|" +
                "calendar address with an upper case scheme|" +
                "empty value|" +
                "control character in a content line|"

        private const val JCAL_SEVERITIES = "EEEWWWE"
        private const val JCAL_MESSAGES =
            "input is not JSON, nothing read|" +
                "not a jCal component, skipped|" +
                "malformed jCal property, skipped|" +
                "malformed jCal parameter, skipped|" +
                "jCal value does not match its type, copied as text|" +
                "unknown jCal value type, value copied as is|" +
                "jCal components nested deeper than the limit, skipped|"

        internal val known: Array<ProblemCode> = create()

        /** `ICAL-S001` (Info): the input starts with a UTF-8 byte order mark. The three bytes are skipped. */
        @JvmField
        public val Utf8Bom: ProblemCode = known[0]

        /**
         * `ICAL-S002` (Warning): a bare CR is used as a line ending. It is accepted as one. Reported once per input;
         * the rest are only counted.
         */
        @JvmField
        public val BareCr: ProblemCode = known[1]

        /**
         * `ICAL-S003` (Warning): a bare LF is used as a line ending. It is accepted as one. Reported once per input;
         * the rest are only counted.
         */
        @JvmField
        public val BareLf: ProblemCode = known[2]

        /**
         * `ICAL-S004` (Warning): a blank line, which the format does not allow. The line is skipped. Reported once per
         * input.
         */
        @JvmField
        public val BlankLine: ProblemCode = known[3]

        /**
         * `ICAL-S005` (Warning): the input starts with a continuation line, a space or a tab in the first byte. That
         * one character is removed and the line starts the first logical line.
         */
        @JvmField
        public val ContinuationAtStart: ProblemCode = known[4]

        /**
         * `ICAL-S006` (Warning): a logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is
         * replaced with U+FFFD; the line is not skipped. One problem per line.
         */
        @JvmField
        public val InvalidUtf8: ProblemCode = known[5]

        /**
         * `ICAL-S007` (Error): there is no colon outside quotes after the property name and its parameters. The line is
         * skipped.
         */
        @JvmField
        public val MissingColon: ProblemCode = known[6]

        /**
         * `ICAL-S008` (Warning): a property or parameter name is empty or contains a character other than letters,
         * digits and a hyphen. The name is kept as it is.
         */
        @JvmField
        public val InvalidName: ProblemCode = known[7]

        /**
         * `ICAL-S009` (Warning): a quoted parameter value is not closed before the end of the line. The value is taken
         * literally up to the next comma, semicolon or colon, and parsing goes on.
         */
        @JvmField
        public val UnclosedQuote: ProblemCode = known[8]

        /**
         * `ICAL-S010` (Error): a logical line is longer than [io.github.calendrum.syntax.ParseLimits.maxLineOctets]. The
         * whole line is skipped and the bytes beyond the limit are not copied.
         */
        @JvmField
        public val LineTooLong: ProblemCode = known[9]

        /**
         * `ICAL-S011` (Error): the input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC,
         * SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding.
         */
        @JvmField
        public val UnsupportedEncoding: ProblemCode = known[10]

        /**
         * `ICAL-S012` (Warning): a parameter is malformed: it has no `=`, a quote inside an unquoted value, or text
         * after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to
         * the next delimiter.
         */
        @JvmField
        public val MalformedParameter: ProblemCode = known[11]

        /**
         * `ICAL-S013` (Warning): the limit on reported problems was reached
         * ([io.github.calendrum.syntax.ParseLimits.maxProblems]). Further problems are only counted; see
         * [io.github.calendrum.syntax.ParseResult.problemCount].
         */
        @JvmField
        public val TooManyProblems: ProblemCode = known[12]

        /**
         * `ICAL-S014` (Error): the limit on logical lines was reached ([io.github.calendrum.syntax.ParseLimits.maxLines]).
         * Reading stops and the result is truncated. Always reported, even beyond the problem limit.
         */
        @JvmField
        public val TooManyLines: ProblemCode = known[13]

        /** `ICAL-S015` (Warning): an `END` line with no open component. The line is ignored. */
        @JvmField
        public val EndWithoutBegin: ProblemCode = known[14]

        /**
         * `ICAL-S016` (Warning): an `END` closes a component that is open deeper in the stack than the top. The
         * components in between are closed implicitly, then the matching one. One problem per `END` line.
         */
        @JvmField
        public val EndClosesNested: ProblemCode = known[15]

        /** `ICAL-S017` (Warning): an `END` names a component that is not open. The line is ignored. */
        @JvmField
        public val UnmatchedEnd: ProblemCode = known[16]

        /**
         * `ICAL-S018` (Warning): the input ended while components were open, as in a truncated feed. All of them are
         * closed implicitly. Not reported when reading was stopped by a limit.
         */
        @JvmField
        public val UnclosedComponents: ProblemCode = known[17]

        /**
         * `ICAL-S019` (Warning): the name in `BEGIN` or `END` is empty or contains a character other than letters,
         * digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII
         * case.
         */
        @JvmField
        public val InvalidComponentName: ProblemCode = known[18]

        /** `ICAL-S020` (Warning): `BEGIN` or `END` has parameters. They are ignored. */
        @JvmField
        public val ComponentParameters: ProblemCode = known[19]

        /**
         * `ICAL-S021` (Warning): a property outside any component, before the first `BEGIN` or between calendars. The
         * property is not part of the tree, but a cursor reports it at depth 0.
         */
        @JvmField
        public val PropertyOutsideComponent: ProblemCode = known[20]

        /**
         * `ICAL-S022` (Error): components are nested deeper than [io.github.calendrum.syntax.ParseLimits.maxDepth]. The
         * subtree is skipped up to its `END`, with one problem per subtree.
         */
        @JvmField
        public val TooDeep: ProblemCode = known[21]

        /**
         * `ICAL-S023` (Error): the limit on components was reached
         * ([io.github.calendrum.syntax.ParseLimits.maxComponents]). Open components are closed implicitly, reading stops
         * and the result is truncated. Always reported.
         */
        @JvmField
        public val TooManyComponents: ProblemCode = known[22]

        /**
         * `ICAL-S024` (Info): the input was transcoded to UTF-8 from the encoding named by a byte order mark or by a
         * charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text.
         */
        @JvmField
        public val TranscodedInput: ProblemCode = known[23]

        /**
         * `ICAL-S025` (Warning): the encoding was guessed from the data, by a heuristic or by the placement of zero
         * bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in
         * percent.
         */
        @JvmField
        public val DetectedCharset: ProblemCode = known[24]

        /**
         * `ICAL-S026` (Warning): the charset hint is not recognized, either an unknown name or an unsupported encoding.
         * The hint is ignored and the data decides.
         */
        @JvmField
        public val UnknownCharsetHint: ProblemCode = known[25]

        /**
         * `ICAL-S027` (Warning): the charset hint contradicts the data, for example a byte order mark of another
         * encoding or clear UTF-8 data under a single-byte hint. The data decides.
         */
        @JvmField
        public val CharsetHintContradictsData: ProblemCode = known[26]

        /**
         * `ICAL-S028` (Warning): the heuristic is less than 90% sure of the encoding, typically for a short text of
         * capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the
         * application may want to ask the user or pass a hint.
         */
        @JvmField
        public val LowCharsetConfidence: ProblemCode = known[27]

        /**
         * `ICAL-S029` (Error): the time budget [io.github.calendrum.syntax.ParseLimits.maxTime] ran out. Reading stops at a
         * line boundary and the result is truncated. Always reported.
         */
        @JvmField
        public val TimeBudgetExceeded: ProblemCode = known[28]

        /**
         * `ICAL-S030` (Error): the input is longer than [io.github.calendrum.syntax.ParseLimits.maxInputOctets]. Reading
         * stops before the first logical line that does not end within that many bytes, and the result is truncated.
         * Always reported.
         */
        @JvmField
        public val InputTooLarge: ProblemCode = known[29]

        /**
         * `ICAL-V001` (Error): a value does not conform to the grammar of its type; no value is decoded. In strict mode
         * it follows the warning code of a lenient repair, with the same position.
         */
        @JvmField
        public val InvalidValue: ProblemCode = known[30]

        /**
         * `ICAL-V002` (Error): a field is out of range, such as a month, a day, an hour, a UTC offset, an integer
         * beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded.
         */
        @JvmField
        public val OutOfRange: ProblemCode = known[31]

        /**
         * `ICAL-V003` (Error): a recurrence rule has no `FREQ` part ([RFC 5545
         * §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded.
         */
        @JvmField
        public val RecurWithoutFreq: ProblemCode = known[32]

        /**
         * `ICAL-V004` (Warning): a time has the second 60, which the standard allows. The decoder replaces it by 59 or
         * by the next minute, depending on its policy.
         */
        @JvmField
        public val LeapSecond: ProblemCode = known[33]

        /**
         * `ICAL-V005` (Warning): a date or time is written in the extended ISO 8601 form, with separators. It is read
         * as the basic form; in strict mode it is an error.
         */
        @JvmField
        public val ExtendedIsoForm: ProblemCode = known[34]

        /**
         * `ICAL-V006` (Warning): a time has fractions of a second, as written by JavaScript generators. The fraction is
         * dropped.
         */
        @JvmField
        public val FractionalSeconds: ProblemCode = known[35]

        /** `ICAL-V007` (Warning): a `DATE` value has a time. The time is dropped. */
        @JvmField
        public val DateWithTime: ProblemCode = known[36]

        /**
         * `ICAL-V008` (Warning): a `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is
         * read as a `DATE`.
         */
        @JvmField
        public val DateAsDateTime: ProblemCode = known[37]

        /**
         * `ICAL-V009` (Warning): a `DURATION` has time units without the `T` designator, such as `P90M`. It is read as
         * `PT90M`, since the standard has no months in durations.
         */
        @JvmField
        public val DurationWithoutTimeDesignator: ProblemCode = known[38]

        /**
         * `ICAL-V010` (Warning): a `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M`
         * is added.
         */
        @JvmField
        public val DurationSkipsMinutes: ProblemCode = known[39]

        /** `ICAL-V011` (Warning): a `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. */
        @JvmField
        public val UtcOffsetWithColon: ProblemCode = known[40]

        /**
         * `ICAL-V012` (Warning): the offset `-0000`, which [RFC 5545
         * §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`.
         */
        @JvmField
        public val NegativeZeroOffset: ProblemCode = known[41]

        /**
         * `ICAL-V013` (Warning): spaces or tabs around a value or a list item. They are dropped. Not reported for
         * `TEXT` and `BINARY`.
         */
        @JvmField
        public val Whitespace: ProblemCode = known[42]

        /** `ICAL-V014` (Warning): whitespace inside a `BINARY` value. It is skipped. */
        @JvmField
        public val Base64Whitespace: ProblemCode = known[43]

        /** `ICAL-V015` (Warning): a `BINARY` value without base64 padding. The padding is added. */
        @JvmField
        public val Base64Padding: ProblemCode = known[44]

        /**
         * `ICAL-V016` (Warning): an empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is
         * skipped.
         */
        @JvmField
        public val EmptyItem: ProblemCode = known[45]

        /**
         * `ICAL-V017` (Warning): a recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are
         * kept; the position is that of the second.
         */
        @JvmField
        public val CountAndUntil: ProblemCode = known[46]

        /** `ICAL-V018` (Warning): a part of a recurrence rule is repeated. The last one is kept. */
        @JvmField
        public val RepeatedRulePart: ProblemCode = known[47]

        /**
         * `ICAL-V019` (Warning): a rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or
         * `BYSETPOS` without another `BYxxx` part. The part is kept.
         */
        @JvmField
        public val RulePartNotAllowed: ProblemCode = known[48]

        /**
         * `ICAL-V020` (Warning): an unknown part in a recurrence rule, such as an `X-` name or a future registered
         * part. It is kept, in order, and written after the known ones.
         */
        @JvmField
        public val UnknownRulePart: ProblemCode = known[49]

        /**
         * `ICAL-V021` (Warning): a `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is
         * kept as it is.
         */
        @JvmField
        public val UriWithoutScheme: ProblemCode = known[50]

        /**
         * `ICAL-V022` (Warning): a `PERIOD` ends no later than it starts, or has a duration that is not positive. It is
         * kept.
         */
        @JvmField
        public val PeriodNotPositive: ProblemCode = known[51]

        /**
         * `ICAL-V023` (Warning): a backslash in a `TEXT` value is followed by a character that is not an escape of [RFC
         * 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are
         * kept.
         */
        @JvmField
        public val UnknownEscape: ProblemCode = known[52]

        /**
         * `ICAL-V024` (Warning): a `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is
         * not applied and the time is UTC.
         */
        @JvmField
        public val TzidWithUtc: ProblemCode = known[53]

        /**
         * `ICAL-V025` (Warning): the `VALUE` parameter names a type this property does not take, or is empty. The
         * default type of the property is read.
         */
        @JvmField
        public val ValueTypeNotAllowed: ProblemCode = known[54]

        /**
         * `ICAL-V026` (Warning): a value of a type other than the default is not declared by `VALUE` or `ENCODING`, as
         * in `TRIGGER:19980101T050000Z`. It is read as the value of that type.
         */
        @JvmField
        public val ValueTypeNotDeclared: ProblemCode = known[55]

        /**
         * `ICAL-V027` (Warning): a value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an
         * absolute `TRIGGER`. It is kept as it is.
         */
        @JvmField
        public val NotUtc: ProblemCode = known[56]

        /**
         * `ICAL-V028` (Warning): a parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an
         * absolute `TRIGGER`. It is not applied.
         */
        @JvmField
        public val ParameterNotAllowed: ProblemCode = known[57]

        /**
         * `ICAL-M001` (Error): a component of a schema lacks a required property, or the first such property is broken.
         * The component is skipped. The explanation names the property, never its value.
         */
        @JvmField
        public val MissingRequiredField: ProblemCode = known[58]

        /**
         * `ICAL-R001` (Warning): the number of occurrences reached the limit while the sequence was not over, or a rule
         * with `COUNT` passed as many dates before the start of a range. The sequence ends.
         */
        @JvmField
        public val RecurrenceLimitReached: ProblemCode = known[59]

        /**
         * `ICAL-R002` (Warning): too many periods in a row produced no occurrence, as in
         * `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends.
         */
        @JvmField
        public val RecurrenceNoMatch: ProblemCode = known[60]

        /**
         * `ICAL-R003` (Warning): `DTSTART` is not an instance of its own rule, for which [RFC 5545
         * §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens
         * is up to the `startMismatch` option of the recurrence options.
         */
        @JvmField
        public val StartNotInRule: ProblemCode = known[61]

        /** `ICAL-R004` (Warning): the `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. */
        @JvmField
        public val RscaleNotSupported: ProblemCode = known[62]

        /**
         * `ICAL-R005` (Warning): the type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME`
         * or UTC against local. The two are compared in the closest sensible way.
         */
        @JvmField
        public val UntilTypeMismatch: ProblemCode = known[63]

        /**
         * `ICAL-R006` (Info): `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL`
         * is compared with local time.
         */
        @JvmField
        public val UntilWithoutZone: ProblemCode = known[64]

        /**
         * `ICAL-R007` (Warning): the rule has a time part or a frequency shorter than a day while `DTSTART` is a
         * `DATE`. The time parts are ignored.
         */
        @JvmField
        public val TimePartWithDate: ProblemCode = known[65]

        /** `ICAL-R008` (Info): `BYSECOND=60` has no meaning in local time. The value is ignored. */
        @JvmField
        public val LeapSecondIgnored: ProblemCode = known[66]

        /**
         * `ICAL-R009` (Info): an `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time
         * zone. They are compared by local time.
         */
        @JvmField
        public val FormMismatch: ProblemCode = known[67]

        /**
         * `ICAL-Z001` (Warning): no resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating
         * time, UTC or an exception.
         */
        @JvmField
        public val UnknownTimeZone: ProblemCode = known[68]

        /**
         * `ICAL-Z002` (Info): the `TZID` was found through an alias, such as an IANA link, a Windows name or a
         * `/mozilla.org/` prefix.
         */
        @JvmField
        public val TimeZoneAlias: ProblemCode = known[69]

        /**
         * `ICAL-Z003` (Warning): a `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than
         * 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next
         * resolver of the chain looks for the `TZID`.
         */
        @JvmField
        public val InvalidVTimeZone: ProblemCode = known[70]

        /**
         * `ICAL-Z004` (Warning): the `TZID` was found only by a guess from a city in its name. The zone of the first
         * city mentioned is used.
         */
        @JvmField
        public val TimeZoneGuessed: ProblemCode = known[71]

        /**
         * `ICAL-Z005` (Warning): the current rules of the zone cannot be written as one `RRULE` per observance, so the
         * generated `VTIMEZONE` only approximates them. The full mode gives an exact one.
         */
        @JvmField
        public val IrregularTimeZone: ProblemCode = known[72]

        /**
         * `ICAL-O001` (Warning): the `RECURRENCE-ID` of an override matches no instance of the master. The override is
         * kept as a single occurrence.
         */
        @JvmField
        public val OverrideWithoutInstance: ProblemCode = known[73]

        /**
         * `ICAL-O002` (Info): an override with `RANGE=THISANDFUTURE` was applied to the later instances, on a
         * best-effort basis.
         */
        @JvmField
        public val ThisAndFutureApplied: ProblemCode = known[74]

        /**
         * `ICAL-O003` (Warning): the `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra
         * repetitions are dropped.
         */
        @JvmField
        public val AlarmRepeatLimit: ProblemCode = known[75]

        /**
         * `ICAL-O004` (Warning): a floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is
         * set. The occurrence is skipped.
         */
        @JvmField
        public val FloatingWithoutZone: ProblemCode = known[76]

        /**
         * `ICAL-O005` (Warning): a component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence.
         */
        @JvmField
        public val OccurrenceWithoutStart: ProblemCode = known[77]

        /** `ICAL-L001` (Error): a required property is missing. */
        @JvmField
        public val MissingProperty: ProblemCode = known[78]

        /** `ICAL-L002` (Error): a property occurs more often than allowed. */
        @JvmField
        public val TooManyProperties: ProblemCode = known[79]

        /**
         * `ICAL-L003` (Warning): a property occurs more than once where the standard says it SHOULD NOT, such as
         * `RRULE`.
         */
        @JvmField
        public val RepeatedProperty: ProblemCode = known[80]

        /**
         * `ICAL-L004` (Error): a known property is in a component where it does not belong, or that the iTIP method
         * forbids.
         */
        @JvmField
        public val PropertyNotAllowed: ProblemCode = known[81]

        /**
         * `ICAL-L005` (Info): a property known neither to the registry nor to the rule tables, and not an `X-`
         * property.
         */
        @JvmField
        public val UnknownProperty: ProblemCode = known[82]

        /**
         * `ICAL-L006` (Error): a component inside a parent that does not allow it, or that the iTIP method forbids.
         */
        @JvmField
        public val ComponentNotAllowed: ProblemCode = known[83]

        /**
         * `ICAL-L007` (Error): a required nested component is missing, such as a calendar without components or a
         * `VTIMEZONE` without `STANDARD` or `DAYLIGHT`.
         */
        @JvmField
        public val MissingComponent: ProblemCode = known[84]

        /** `ICAL-L008` (Error): properties that exclude each other occur together, such as `DTEND` and `DURATION`. */
        @JvmField
        public val ExclusiveProperties: ProblemCode = known[85]

        /**
         * `ICAL-L009` (Error): a property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT`
         * without `DURATION`.
         */
        @JvmField
        public val MissingDependency: ProblemCode = known[86]

        /**
         * `ICAL-L010` (Error): the form of a value differs from that of a related one, such as a `DATE` `DTEND` with a
         * `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`.
         */
        @JvmField
        public val ValueTypeMismatch: ProblemCode = known[87]

        /**
         * `ICAL-L011` (Error): `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve.
         */
        @JvmField
        public val EndBeforeStart: ProblemCode = known[88]

        /** `ICAL-L012` (Error): a `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. */
        @JvmField
        public val DateWithTimeDuration: ProblemCode = known[89]

        /**
         * `ICAL-L013` (Error): a value outside the list of the standard, such as a component `STATUS`, `VERSION` or
         * `TRANSP`.
         */
        @JvmField
        public val ValueNotAllowed: ProblemCode = known[90]

        /**
         * `ICAL-L014` (Error): a number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE`
         * outside 0 to 100.
         */
        @JvmField
        public val ValueOutOfRange: ProblemCode = known[91]

        /**
         * `ICAL-L015` (Error): the `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545
         * §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)).
         */
        @JvmField
        public val NotLocalTime: ProblemCode = known[92]

        /**
         * `ICAL-L016` (Error): a value does not decode as its type; the details are an `ICAL-V` problem from reading.
         */
        @JvmField
        public val MalformedValue: ProblemCode = known[93]

        /**
         * `ICAL-L017` (Error): a `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545
         * §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`.
         */
        @JvmField
        public val MissingTimeZone: ProblemCode = known[94]

        /**
         * `ICAL-L018` (Error): the calendar has no `METHOD`, or one that differs from the method of the iTIP rule set
         * in use.
         */
        @JvmField
        public val MethodMismatch: ProblemCode = known[95]

        /**
         * `ICAL-L019` (Info): the `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be
         * ignored.
         */
        @JvmField
        public val UnknownAlarmAction: ProblemCode = known[96]

        /**
         * `ICAL-L020` (Error): the iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`.
         */
        @JvmField
        public val MethodNotDefined: ProblemCode = known[97]

        /**
         * `ICAL-L021` (Warning): `X-WR-TIMEZONE` beside floating times, which clients understand in different ways.
         * Found by the linter.
         */
        @JvmField
        public val VendorTimeZone: ProblemCode = known[98]

        /**
         * `ICAL-L022` (Warning): a `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor
         * prefix. Found by the linter, once per `TZID`.
         */
        @JvmField
        public val AliasTimeZoneId: ProblemCode = known[99]

        /** `ICAL-L023` (Info): `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. */
        @JvmField
        public val VendorCalendarName: ProblemCode = known[100]

        /**
         * `ICAL-L024` (Info): an `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the
         * linter.
         */
        @JvmField
        public val UppercaseMailto: ProblemCode = known[101]

        /**
         * `ICAL-L025` (Warning): an empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the
         * linter.
         */
        @JvmField
        public val EmptyValue: ProblemCode = known[102]

        /**
         * `ICAL-L026` (Error): a content line holds a control character other than horizontal tab, in its name, a
         * parameter or its value, which [RFC 5545 §3.1](https://www.rfc-editor.org/rfc/rfc5545#section-3.1) forbids.
         * Parsing keeps the line as it is and byte-stable writing writes it back, but canonical writing, jCal and the
         * builders refuse it. Found by the validator and the linter; the explanation never quotes the line.
         */
        @JvmField
        public val ControlCharacter: ProblemCode = known[103]

        /** `ICAL-J001` (Error): the input is not JSON. Nothing is read. */
        @JvmField
        public val InvalidJson: ProblemCode = known[104]

        /**
         * `ICAL-J002` (Error): an element is not a jCal component: not an array of a name, an array of properties and
         * an array of components, or the name is not valid. The component is skipped with its children.
         */
        @JvmField
        public val MalformedJCalComponent: ProblemCode = known[105]

        /**
         * `ICAL-J003` (Error): a property is not an array of a name, an object of parameters, a type and at least one
         * value, or a value has the wrong JSON type. The property is skipped.
         */
        @JvmField
        public val MalformedJCalProperty: ProblemCode = known[106]

        /**
         * `ICAL-J004` (Warning): a parameter value is not a string or an array of strings, or the `value` parameter
         * appears in the parameter object. The parameter is skipped.
         */
        @JvmField
        public val MalformedJCalParameter: ProblemCode = known[107]

        /**
         * `ICAL-J005` (Warning): a value is not in the form of its type, such as a `date-time` that is not
         * `YYYY-MM-DDTHH:MM:SS`. The value is written as it is.
         */
        @JvmField
        public val JCalValueMismatch: ProblemCode = known[108]

        /**
         * `ICAL-J006` (Warning): the type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is
         * written and the value is kept as it is.
         */
        @JvmField
        public val UnknownJCalType: ProblemCode = known[109]

        /**
         * `ICAL-J007` (Error): jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The
         * deeper component is skipped with everything inside it.
         */
        @JvmField
        public val JCalTooDeep: ProblemCode = known[110]

        /**
         * Returns the code with the given [id], ignoring case.
         *
         * For an identifier the library knows this is the constant; for any other a new code is created with the
         * upper-case identifier and the severity [Severity.Warning].
         *
         * @throws IllegalArgumentException if [id] is empty.
         */
        @JvmStatic
        public fun of(id: String): ProblemCode {
            require(id.isNotEmpty()) { "Empty problem code" }
            val canonical = id.uppercase()
            for (code in known) {
                if (code.id == canonical) return code
            }
            return ProblemCode(canonical, Severity.Warning, -1, canonical, 0)
        }

        private fun create(): Array<ProblemCode> {
            val values = SEVERITIES.length + VALUE_SEVERITIES.length
            val models = values + MODEL_SEVERITIES.length
            val recurrences = models + RECURRENCE_SEVERITIES.length
            val zones = recurrences + ZONE_SEVERITIES.length
            val occurrences = zones + OCCURRENCE_SEVERITIES.length
            val lints = occurrences + LINT_SEVERITIES.length
            val codes = arrayOfNulls<ProblemCode>(lints + JCAL_SEVERITIES.length)
            fill(codes, 0, ID_PREFIX, SEVERITIES, MESSAGES, FLAGS)
            fill(codes, SEVERITIES.length, VALUE_ID_PREFIX, VALUE_SEVERITIES, VALUE_MESSAGES, null)
            fill(codes, values, MODEL_ID_PREFIX, MODEL_SEVERITIES, MODEL_MESSAGES, null)
            fill(codes, models, RECURRENCE_ID_PREFIX, RECURRENCE_SEVERITIES, RECURRENCE_MESSAGES, null)
            fill(codes, recurrences, ZONE_ID_PREFIX, ZONE_SEVERITIES, ZONE_MESSAGES, null)
            fill(codes, zones, OCCURRENCE_ID_PREFIX, OCCURRENCE_SEVERITIES, OCCURRENCE_MESSAGES, null)
            fill(codes, occurrences, LINT_ID_PREFIX, LINT_SEVERITIES, LINT_MESSAGES, null)
            fill(codes, lints, JCAL_ID_PREFIX, JCAL_SEVERITIES, JCAL_MESSAGES, null)
            return Array(codes.size) { codes[it]!! }
        }

        private fun fill(
            codes: Array<ProblemCode?>,
            first: Int,
            prefix: String,
            severities: String,
            messages: String,
            flags: String?,
        ) {
            var start = 0
            for (offset in severities.indices) {
                var end = start
                while (messages[end] != SEPARATOR) end++
                val number = offset + 1
                val severity = when (severities[offset]) {
                    'I' -> Severity.Info
                    'W' -> Severity.Warning
                    else -> Severity.Error
                }
                val id = prefix + (number / 100) + (number / 10 % 10) + (number % 10)
                val flag = if (flags == null) 0 else flags[offset] - '0'
                codes[first + offset] = ProblemCode(id, severity, first + offset, messages.substring(start, end), flag)
                start = end + 1
            }
        }
    }
}
