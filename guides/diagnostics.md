# Diagnostics

Every problem the library reports has a stable code, a severity, a position and a short fixed message. Codes never change
meaning and are never reused, so you can match on them in code, in logs and in tests. Values of properties and parameters
never appear in messages, which keeps calendar data out of logs.

## How problems are reported

- A `Problem` has a `code` (`ProblemCode`), a `severity` (`Info`, `Warning` or `Error`), a `span` (`SourceSpan`) and a
  `message`. `ProblemCode` is an open enumeration: `ProblemCode.of("ICAL-S003")` returns the constant for a known id (the
  case does not matter) and a new object with that id and the `Warning` severity for an unknown one. Two codes are equal when
  their ids are equal.
- The position is a point: the byte offset from the start of the input, the physical line and the column, both from 1. A
  problem inside a folded line points at the byte in the source, not in the unfolded line. For input that is decoded from
  another charset the position counts bytes of the decoded UTF-8 text. For a stream the offset counts from the start of the
  stream.
- `ParseResult.problems` is what the problem listener received, in the same order. It stops at the problem limit (1000 per
  input by default) with a single `ICAL-S013`. `ParseResult.problemCount(code)` counts every case, also the ones beyond the
  limit and the repeats of the once-per-input codes.
- Some codes are reported once per input (`ICAL-S002`, `ICAL-S003`, `ICAL-S004`): the first case with its position, the rest
  are only counted, so a 100 MB file with LF line endings gives one problem instead of a million.
- In lenient mode (the default) parsing never throws on data. In strict mode (`ParseMode.Strict`) the first problem with the
  `Error` severity is reported and `ICalParseException` is thrown with the problems up to and including it.
- Lines that a selection skips are not examined: no UTF-8 check, no length check, no parameter parsing. `BEGIN` and `END`
  lines are always read.
- Validation and lint report in a `ValidationReport` or `LintReport` with a path such as `VCALENDAR[0]/VEVENT[0]/DTEND[0]`;
  recurrence, zone and occurrence problems go to the problem listener of the options you pass.

The tables below list every code of this release, generated from the KDoc of `ProblemCode`. Open `ProblemCode` in the API
documentation for the constants.

## `ICAL-S`: syntax

Reported by the lexer and the tree builder while the text is read. These are the codes that `ParseResult.problemCount` counts for every parse.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-S001` | Info | `ProblemCode.Utf8Bom` | The input starts with a UTF-8 byte order mark. The three bytes are skipped. |
| `ICAL-S002` | Warning | `ProblemCode.BareCr` | A bare CR is used as a line ending. It is accepted as one. Reported once per input; the rest are only counted. |
| `ICAL-S003` | Warning | `ProblemCode.BareLf` | A bare LF is used as a line ending. It is accepted as one. Reported once per input; the rest are only counted. |
| `ICAL-S004` | Warning | `ProblemCode.BlankLine` | A blank line, which the format does not allow. The line is skipped. Reported once per input. |
| `ICAL-S005` | Warning | `ProblemCode.ContinuationAtStart` | The input starts with a continuation line, a space or a tab in the first byte. That one character is removed and the line starts the first logical line. |
| `ICAL-S006` | Warning | `ProblemCode.InvalidUtf8` | A logical line contains an invalid UTF-8 sequence. Each maximal invalid subsequence is replaced with U+FFFD; the line is not skipped. One problem per line. |
| `ICAL-S007` | Error | `ProblemCode.MissingColon` | There is no colon outside quotes after the property name and its parameters. The line is skipped. |
| `ICAL-S008` | Warning | `ProblemCode.InvalidName` | A property or parameter name is empty or contains a character other than letters, digits and a hyphen. The name is kept as it is. |
| `ICAL-S009` | Warning | `ProblemCode.UnclosedQuote` | A quoted parameter value is not closed before the end of the line. The value is taken literally up to the next comma, semicolon or colon, and parsing goes on. |
| `ICAL-S010` | Error | `ProblemCode.LineTooLong` | A logical line is longer than `io.github.calendrum.syntax.ParseLimits.maxLineOctets`. The whole line is skipped and the bytes beyond the limit are not copied. |
| `ICAL-S011` | Error | `ProblemCode.UnsupportedEncoding` | The input is in an encoding the library does not read at all, such as UTF-7, UTF-EBCDIC, SCSU, BOCU-1 or GB18030, recognized by its signature. Nothing is parsed. The explanation names the encoding. |
| `ICAL-S012` | Warning | `ProblemCode.MalformedParameter` | A parameter is malformed: it has no `=`, a quote inside an unquoted value, or text after a closing quote. A parameter without `=` gets no values; otherwise the value is taken literally up to the next delimiter. |
| `ICAL-S013` | Warning | `ProblemCode.TooManyProblems` | The limit on reported problems was reached (`io.github.calendrum.syntax.ParseLimits.maxProblems`). Further problems are only counted; see `io.github.calendrum.syntax.ParseResult.problemCount`. |
| `ICAL-S014` | Error | `ProblemCode.TooManyLines` | The limit on logical lines was reached (`io.github.calendrum.syntax.ParseLimits.maxLines`). Reading stops and the result is truncated. Always reported, even beyond the problem limit. |
| `ICAL-S015` | Warning | `ProblemCode.EndWithoutBegin` | An `END` line with no open component. The line is ignored. |
| `ICAL-S016` | Warning | `ProblemCode.EndClosesNested` | An `END` closes a component that is open deeper in the stack than the top. The components in between are closed implicitly, then the matching one. One problem per `END` line. |
| `ICAL-S017` | Warning | `ProblemCode.UnmatchedEnd` | An `END` names a component that is not open. The line is ignored. |
| `ICAL-S018` | Warning | `ProblemCode.UnclosedComponents` | The input ended while components were open, as in a truncated feed. All of them are closed implicitly. Not reported when reading was stopped by a limit. |
| `ICAL-S019` | Warning | `ProblemCode.InvalidComponentName` | The name in `BEGIN` or `END` is empty or contains a character other than letters, digits and a hyphen. The name is kept as it is, and the component is opened and closed by it, ignoring ASCII case. |
| `ICAL-S020` | Warning | `ProblemCode.ComponentParameters` | `BEGIN` or `END` has parameters. They are ignored. |
| `ICAL-S021` | Warning | `ProblemCode.PropertyOutsideComponent` | A property outside any component, before the first `BEGIN` or between calendars. The property is not part of the tree, but a cursor reports it at depth 0. |
| `ICAL-S022` | Error | `ProblemCode.TooDeep` | Components are nested deeper than `io.github.calendrum.syntax.ParseLimits.maxDepth`. The subtree is skipped up to its `END`, with one problem per subtree. |
| `ICAL-S023` | Error | `ProblemCode.TooManyComponents` | The limit on components was reached (`io.github.calendrum.syntax.ParseLimits.maxComponents`). Open components are closed implicitly, reading stops and the result is truncated. Always reported. |
| `ICAL-S024` | Info | `ProblemCode.TranscodedInput` | The input was transcoded to UTF-8 from the encoding named by a byte order mark or by a charset hint. The explanation names the encoding and where it came from. Positions refer to the decoded text. |
| `ICAL-S025` | Warning | `ProblemCode.DetectedCharset` | The encoding was guessed from the data, by a heuristic or by the placement of zero bytes, and the input is transcoded from there on. The explanation names the encoding and the confidence in percent. |
| `ICAL-S026` | Warning | `ProblemCode.UnknownCharsetHint` | The charset hint is not recognized, either an unknown name or an unsupported encoding. The hint is ignored and the data decides. |
| `ICAL-S027` | Warning | `ProblemCode.CharsetHintContradictsData` | The charset hint contradicts the data, for example a byte order mark of another encoding or clear UTF-8 data under a single-byte hint. The data decides. |
| `ICAL-S028` | Warning | `ProblemCode.LowCharsetConfidence` | The heuristic is less than 90% sure of the encoding, typically for a short text of capital abbreviations where windows-1251 and KOI8-R look alike. The input is read in the chosen encoding; the application may want to ask the user or pass a hint. |
| `ICAL-S029` | Error | `ProblemCode.TimeBudgetExceeded` | The time budget `io.github.calendrum.syntax.ParseLimits.maxTime` ran out. Reading stops at a line boundary and the result is truncated. Always reported. |
| `ICAL-S030` | Error | `ProblemCode.InputTooLarge` | The input is longer than `io.github.calendrum.syntax.ParseLimits.maxInputOctets`. Reading stops before the first logical line that does not end within that many bytes, and the result is truncated. Always reported. |

## `ICAL-V`: values

Reported when a property or parameter value is decoded: on first access, by eager decoding (`decodeEagerly = true`), in strict mode, or by `Decoded`.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-V001` | Error | `ProblemCode.InvalidValue` | A value does not conform to the grammar of its type; no value is decoded. In strict mode it follows the warning code of a lenient repair, with the same position. |
| `ICAL-V002` | Error | `ProblemCode.OutOfRange` | A field is out of range, such as a month, a day, an hour, a UTC offset, an integer beyond 32 bits, or a `BYxxx` part of a recurrence rule. No value is decoded. |
| `ICAL-V003` | Error | `ProblemCode.RecurWithoutFreq` | A recurrence rule has no `FREQ` part ([RFC 5545 §3.3.10](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.10)). No value is decoded. |
| `ICAL-V004` | Warning | `ProblemCode.LeapSecond` | A time has the second 60, which the standard allows. The decoder replaces it by 59 or by the next minute, depending on its policy. |
| `ICAL-V005` | Warning | `ProblemCode.ExtendedIsoForm` | A date or time is written in the extended ISO 8601 form, with separators. It is read as the basic form; in strict mode it is an error. |
| `ICAL-V006` | Warning | `ProblemCode.FractionalSeconds` | A time has fractions of a second, as written by JavaScript generators. The fraction is dropped. |
| `ICAL-V007` | Warning | `ProblemCode.DateWithTime` | A `DATE` value has a time. The time is dropped. |
| `ICAL-V008` | Warning | `ProblemCode.DateAsDateTime` | A `DATE-TIME` value has no time, as in `DTSTART:20260901` without `VALUE=DATE`. It is read as a `DATE`. |
| `ICAL-V009` | Warning | `ProblemCode.DurationWithoutTimeDesignator` | A `DURATION` has time units without the `T` designator, such as `P90M`. It is read as `PT90M`, since the standard has no months in durations. |
| `ICAL-V010` | Warning | `ProblemCode.DurationSkipsMinutes` | A `DURATION` has hours and seconds but no minutes, such as `PT1H30S`. The missing `0M` is added. |
| `ICAL-V011` | Warning | `ProblemCode.UtcOffsetWithColon` | A `UTC-OFFSET` is written with a colon, such as `+03:00`. It is read without it. |
| `ICAL-V012` | Warning | `ProblemCode.NegativeZeroOffset` | The offset `-0000`, which [RFC 5545 §3.3.14](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.14) forbids. It is read as `+0000`. |
| `ICAL-V013` | Warning | `ProblemCode.Whitespace` | Spaces or tabs around a value or a list item. They are dropped. Not reported for `TEXT` and `BINARY`. |
| `ICAL-V014` | Warning | `ProblemCode.Base64Whitespace` | Whitespace inside a `BINARY` value. It is skipped. |
| `ICAL-V015` | Warning | `ProblemCode.Base64Padding` | A `BINARY` value without base64 padding. The padding is added. |
| `ICAL-V016` | Warning | `ProblemCode.EmptyItem` | An empty list item, as in `a,,b`, or an empty rule part, as in `FREQ=DAILY;`. It is skipped. |
| `ICAL-V017` | Warning | `ProblemCode.CountAndUntil` | A recurrence rule has both `COUNT` and `UNTIL`, which the standard forbids. Both are kept; the position is that of the second. |
| `ICAL-V018` | Warning | `ProblemCode.RepeatedRulePart` | A part of a recurrence rule is repeated. The last one is kept. |
| `ICAL-V019` | Warning | `ProblemCode.RulePartNotAllowed` | A rule part is not allowed in this rule, such as `BYWEEKNO` without `YEARLY`, or `BYSETPOS` without another `BYxxx` part. The part is kept. |
| `ICAL-V020` | Warning | `ProblemCode.UnknownRulePart` | An unknown part in a recurrence rule, such as an `X-` name or a future registered part. It is kept, in order, and written after the known ones. |
| `ICAL-V021` | Warning | `ProblemCode.UriWithoutScheme` | A `URI` or `CAL-ADDRESS` without a scheme, such as an address without `mailto:`. It is kept as it is. |
| `ICAL-V022` | Warning | `ProblemCode.PeriodNotPositive` | A `PERIOD` ends no later than it starts, or has a duration that is not positive. It is kept. |
| `ICAL-V023` | Warning | `ProblemCode.UnknownEscape` | A backslash in a `TEXT` value is followed by a character that is not an escape of [RFC 5545 §3.3.11](https://www.rfc-editor.org/rfc/rfc5545#section-3.3.11), or ends the value. Both characters are kept. |
| `ICAL-V024` | Warning | `ProblemCode.TzidWithUtc` | A `TZID` parameter on a UTC `DATE-TIME`, which the standard forbids. The parameter is not applied and the time is UTC. |
| `ICAL-V025` | Warning | `ProblemCode.ValueTypeNotAllowed` | The `VALUE` parameter names a type this property does not take, or is empty. The default type of the property is read. |
| `ICAL-V026` | Warning | `ProblemCode.ValueTypeNotDeclared` | A value of a type other than the default is not declared by `VALUE` or `ENCODING`, as in `TRIGGER:19980101T050000Z`. It is read as the value of that type. |
| `ICAL-V027` | Warning | `ProblemCode.NotUtc` | A value that must be in UTC is not, as in `DTSTAMP`, `CREATED`, `LAST-MODIFIED` and an absolute `TRIGGER`. It is kept as it is. |
| `ICAL-V028` | Warning | `ProblemCode.ParameterNotAllowed` | A parameter does not apply to the value, such as `TZID` on a `DATE` or `RELATED` on an absolute `TRIGGER`. It is not applied. |

## `ICAL-M`: model

Reported by schemas and the typed model.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-M001` | Error | `ProblemCode.MissingRequiredField` | A component of a schema lacks a required property, or the first such property is broken. The component is skipped. The explanation names the property, never its value. |

## `ICAL-R`: recurrence

Reported while a recurrence rule or set is expanded.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-R001` | Warning | `ProblemCode.RecurrenceLimitReached` | The number of occurrences reached the limit while the sequence was not over, or a rule with `COUNT` passed as many dates before the start of a range. The sequence ends. |
| `ICAL-R002` | Warning | `ProblemCode.RecurrenceNoMatch` | Too many periods in a row produced no occurrence, as in `FREQ=DAILY;BYMONTH=2;BYMONTHDAY=30`. The sequence ends. |
| `ICAL-R003` | Warning | `ProblemCode.StartNotInRule` | `DTSTART` is not an instance of its own rule, for which [RFC 5545 §3.8.5.3](https://www.rfc-editor.org/rfc/rfc5545#section-3.8.5.3) leaves the result undefined. What happens is up to the `startMismatch` option of the recurrence options. |
| `ICAL-R004` | Warning | `ProblemCode.RscaleNotSupported` | The `RSCALE` of the rule is not `GREGORIAN`. The rule is not expanded. |
| `ICAL-R005` | Warning | `ProblemCode.UntilTypeMismatch` | The type of `UNTIL` does not match `DTSTART`, such as a `DATE` against a `DATE-TIME` or UTC against local. The two are compared in the closest sensible way. |
| `ICAL-R006` | Info | `ProblemCode.UntilWithoutZone` | `UNTIL` is in UTC while `DTSTART` has a time zone and no `untilOffset` is set, so `UNTIL` is compared with local time. |
| `ICAL-R007` | Warning | `ProblemCode.TimePartWithDate` | The rule has a time part or a frequency shorter than a day while `DTSTART` is a `DATE`. The time parts are ignored. |
| `ICAL-R008` | Info | `ProblemCode.LeapSecondIgnored` | `BYSECOND=60` has no meaning in local time. The value is ignored. |
| `ICAL-R009` | Info | `ProblemCode.FormMismatch` | An `RDATE` or `EXDATE` is in another form than `DTSTART`, such as UTC against a time zone. They are compared by local time. |

## `ICAL-Z`: time zones

Reported while time zones are resolved and `VTIMEZONE` components are compiled.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-Z001` | Warning | `ProblemCode.UnknownTimeZone` | No resolver of the chain knows the `TZID`. The unknown-zone policy decides: floating time, UTC or an exception. |
| `ICAL-Z002` | Info | `ProblemCode.TimeZoneAlias` | The `TZID` was found through an alias, such as an IANA link, a Windows name or a `/mozilla.org/` prefix. |
| `ICAL-Z003` | Warning | `ProblemCode.InvalidVTimeZone` | A `VTIMEZONE` has no `TZID`, no observance with `DTSTART` and `TZOFFSETTO`, or more than 200,000 transitions up to its last start (a hostile zone of endless observances). It is skipped, and the next resolver of the chain looks for the `TZID`. |
| `ICAL-Z004` | Warning | `ProblemCode.TimeZoneGuessed` | The `TZID` was found only by a guess from a city in its name. The zone of the first city mentioned is used. |
| `ICAL-Z005` | Warning | `ProblemCode.IrregularTimeZone` | The current rules of the zone cannot be written as one `RRULE` per observance, so the generated `VTIMEZONE` only approximates them. The full mode gives an exact one. |

## `ICAL-O`: occurrences

Reported while occurrences are computed.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-O001` | Warning | `ProblemCode.OverrideWithoutInstance` | The `RECURRENCE-ID` of an override matches no instance of the master. The override is kept as a single occurrence. |
| `ICAL-O002` | Info | `ProblemCode.ThisAndFutureApplied` | An override with `RANGE=THISANDFUTURE` was applied to the later instances, on a best-effort basis. |
| `ICAL-O003` | Warning | `ProblemCode.AlarmRepeatLimit` | The `REPEAT` of an alarm is beyond the limit of alarm repetitions. The extra repetitions are dropped. |
| `ICAL-O004` | Warning | `ProblemCode.FloatingWithoutZone` | A floating time, a `DATE` or an unresolved `TZID` was met while no floating zone is set. The occurrence is skipped. |
| `ICAL-O005` | Warning | `ProblemCode.OccurrenceWithoutStart` | A component has no `DTSTART` (and a to-do no `DUE` either), so it has no occurrence. |

## `ICAL-L`: validation and lint

Reported in a `ValidationReport` or a `LintReport`, never thrown. Their `path` names the component and property.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-L001` | Error | `ProblemCode.MissingProperty` | A required property is missing. |
| `ICAL-L002` | Error | `ProblemCode.TooManyProperties` | A property occurs more often than allowed. |
| `ICAL-L003` | Warning | `ProblemCode.RepeatedProperty` | A property occurs more than once where the standard says it SHOULD NOT, such as `RRULE`. |
| `ICAL-L004` | Error | `ProblemCode.PropertyNotAllowed` | A known property is in a component where it does not belong, or that the iTIP method forbids. |
| `ICAL-L005` | Info | `ProblemCode.UnknownProperty` | A property known neither to the registry nor to the rule tables, and not an `X-` property. |
| `ICAL-L006` | Error | `ProblemCode.ComponentNotAllowed` | A component inside a parent that does not allow it, or that the iTIP method forbids. |
| `ICAL-L007` | Error | `ProblemCode.MissingComponent` | A required nested component is missing, such as a calendar without components or a `VTIMEZONE` without `STANDARD` or `DAYLIGHT`. |
| `ICAL-L008` | Error | `ProblemCode.ExclusiveProperties` | Properties that exclude each other occur together, such as `DTEND` and `DURATION`. |
| `ICAL-L009` | Error | `ProblemCode.MissingDependency` | A property lacks the one it requires, such as `RRULE` without `DTSTART` or `REPEAT` without `DURATION`. |
| `ICAL-L010` | Error | `ProblemCode.ValueTypeMismatch` | The form of a value differs from that of a related one, such as a `DATE` `DTEND` with a `DATE-TIME` `DTSTART`, or a non-UTC `UNTIL` with a zoned `DTSTART`. |
| `ICAL-L011` | Error | `ProblemCode.EndBeforeStart` | `DTEND` or `DUE` is not later than `DTSTART`. Not checked when a zone does not resolve. |
| `ICAL-L012` | Error | `ProblemCode.DateWithTimeDuration` | A `DURATION` with hours, minutes or seconds beside a `DTSTART` of type `DATE`. |
| `ICAL-L013` | Error | `ProblemCode.ValueNotAllowed` | A value outside the list of the standard, such as a component `STATUS`, `VERSION` or `TRANSP`. |
| `ICAL-L014` | Error | `ProblemCode.ValueOutOfRange` | A number outside its range, such as `PRIORITY` outside 0 to 9 or `PERCENT-COMPLETE` outside 0 to 100. |
| `ICAL-L015` | Error | `ProblemCode.NotLocalTime` | The `DTSTART` of a `VTIMEZONE` observance is not a local time ([RFC 5545 §3.6.5](https://www.rfc-editor.org/rfc/rfc5545#section-3.6.5)). |
| `ICAL-L016` | Error | `ProblemCode.MalformedValue` | A value does not decode as its type; the details are an `ICAL-V` problem from reading. |
| `ICAL-L017` | Error | `ProblemCode.MissingTimeZone` | A `TZID` has no `VTIMEZONE` with that identifier in the calendar ([RFC 5545 §3.2.19](https://www.rfc-editor.org/rfc/rfc5545#section-3.2.19)). Reported once per `TZID`. |
| `ICAL-L018` | Error | `ProblemCode.MethodMismatch` | The calendar has no `METHOD`, or one that differs from the method of the iTIP rule set in use. |
| `ICAL-L019` | Info | `ProblemCode.UnknownAlarmAction` | The `ACTION` of an alarm is not `AUDIO`, `DISPLAY` or `EMAIL`; such an alarm is to be ignored. |
| `ICAL-L020` | Error | `ProblemCode.MethodNotDefined` | The iTIP method is not defined for the component, such as `REFRESH` for a `VJOURNAL`. |
| `ICAL-L021` | Warning | `ProblemCode.VendorTimeZone` | `X-WR-TIMEZONE` beside floating times, which clients understand in different ways. Found by the linter. |
| `ICAL-L022` | Warning | `ProblemCode.AliasTimeZoneId` | A `TZID` is not an IANA zone but a Windows name, a display name, a link or a vendor prefix. Found by the linter, once per `TZID`. |
| `ICAL-L023` | Info | `ProblemCode.VendorCalendarName` | `X-WR-CALNAME` without the standard `NAME` of RFC 7986. Found by the linter. |
| `ICAL-L024` | Info | `ProblemCode.UppercaseMailto` | An `ORGANIZER` or `ATTENDEE` with the scheme in upper case, `MAILTO:`. Found by the linter. |
| `ICAL-L025` | Warning | `ProblemCode.EmptyValue` | An empty value of a non-text property, such as `DTEND:` or `RRULE:`. Found by the linter. |
| `ICAL-L026` | Error | `ProblemCode.ControlCharacter` | A content line holds a control character other than horizontal tab, which RFC 5545 §3.1 forbids. Parsing keeps the line and byte-stable writing writes it back, but canonical writing, jCal and the builders refuse it. Found by the validator and the linter. |

## `ICAL-J`: jCal

Reported by `calendrum-jcal` while jCal (RFC 7265) is read.

| Code | Severity | Constant | Meaning |
|---|---|---|---|
| `ICAL-J001` | Error | `ProblemCode.InvalidJson` | The input is not JSON. Nothing is read. |
| `ICAL-J002` | Error | `ProblemCode.MalformedJCalComponent` | An element is not a jCal component: not an array of a name, an array of properties and an array of components, or the name is not valid. The component is skipped with its children. |
| `ICAL-J003` | Error | `ProblemCode.MalformedJCalProperty` | A property is not an array of a name, an object of parameters, a type and at least one value, or a value has the wrong JSON type. The property is skipped. |
| `ICAL-J004` | Warning | `ProblemCode.MalformedJCalParameter` | A parameter value is not a string or an array of strings, or the `value` parameter appears in the parameter object. The parameter is skipped. |
| `ICAL-J005` | Warning | `ProblemCode.JCalValueMismatch` | A value is not in the form of its type, such as a `date-time` that is not `YYYY-MM-DDTHH:MM:SS`. The value is written as it is. |
| `ICAL-J006` | Warning | `ProblemCode.UnknownJCalType` | The type of a property is not a type of RFC 5545 or RFC 7265. No `VALUE` parameter is written and the value is kept as it is. |
| `ICAL-J007` | Error | `ProblemCode.JCalTooDeep` | jCal components are nested deeper than `JCalOptions.maxDepth` (16 by default). The deeper component is skipped with everything inside it. |
