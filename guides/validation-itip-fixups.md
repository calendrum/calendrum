# Validation, iTIP and fixups

Reading is forgiving by design. This guide covers the tools for the other three questions: *is this file valid?*, *what do I
send in answer to an invitation?*, and *how do I repair what other producers get wrong?* All of them are pure: they take a
calendar and return a new value, with an explicit clock where time matters, and they report problems as data.

## Validation

`Validator.validate(calendar, rules)` checks a typed calendar against a `RuleSet` and returns a `ValidationReport`: a list of
issues, each with an `ICAL-L` code, a severity and a path such as `VCALENDAR[0]/VEVENT[0]/DTEND[0]`. It never throws, and the
rules are data: tables derived from RFC 5545 (cardinalities of properties, where components may appear, exclusive and dependent
properties, value ranges) and from RFC 5546 for each iTIP method.

<!-- sample: validate-report -->
```kotlin
// RuleSet.Rfc5545; a report, never an exception
val report = Validator.validate(calendar)
if (!report.isValid) report.issues.forEach { printed += "${it.code.id} ${it.severity} ${it.path}" }
// METHOD and the RFC 5546 §3.2.2 table
val invitation = Validator.validate(calendar, RuleSet.itip(Method.Request))
```

`isValid` is true when nothing has the `Error` severity; `report.issues(code)` filters by code. Checking a calendar against the
rules of a method (`RuleSet.itip(method)`) also checks that its `METHOD` is that method.

Types: each issue is a `ValidationIssue` (`code`, `severity`, `path`, `span`), and its `path` an `ElementPath`, a list of segments
that name components and properties by name and position among namesakes (and a `parameter`), never by value.

Validation looks at what the file says. Time-dependent facts are only checked when they can be, for example `DTEND` before
`DTSTART` is not checked when a zone does not resolve.

A content line with a control character other than horizontal tab is an `ICAL-L026` error, as RFC 5545 §3.1 forbids it. Parsing
keeps such a line, so that nothing is lost, and byte-stable writing writes it back as it was; but canonical writing, jCal and the
builders refuse it, and in a terminal an escape character in a `SUMMARY` is a command. Drop or replace such lines yourself before
you write a calendar anew: no fixup removes text silently.

### Your own rules

`RuleSet.builder()` starts from the RFC rules. Add or change the cardinality of a property in a component, forbid one, or add
a `ValidationRule`, a function from a component and its path to issues.

<!-- sample: validate-rules -->
```kotlin
val house = RuleSet.builder()
    .cardinality("VEVENT", "SUMMARY", Cardinality.One) // our feed requires a title
    .forbid("VEVENT", "ATTACH")
    .build()
val report = Validator.validate(calendar, house)
```

## iTIP and iMIP

`ITip` (RFC 5546) builds and applies scheduling messages; `IMip` (RFC 6047) helps with the mail around them. Every function
is pure, takes the clock as a parameter and returns a new `VCalendar`; nothing is sent, opened or added to your calendar on its
own.

| Function | Message |
|---|---|
| `ITip.publish`, `request`, `add`, `cancel` | from the organizer: `PUBLISH`, `REQUEST`, `ADD`, `CANCEL` (for an event or a to-do, a series or one instance) |
| `ITip.reply`, `refresh`, `counter` | from an attendee: `REPLY`, `REFRESH`, `COUNTER` |
| `ITip.declineCounter` | from the organizer: `DECLINECOUNTER` |
| `ITip.revise` | bumps `SEQUENCE` when a property listed by RFC 5545 §3.8.7.4 changed |
| `ITip.apply(store, incoming)` | applies a message to a calendar |

`apply` returns an `ITipResult` with the new calendar and an `ITipOutcome`: `Applied`, `Outdated` (a lower `SEQUENCE`, or an
equal one with an earlier `DTSTAMP`), `NotFound`, `ResponseRequired` (`REFRESH` and `COUNTER` change nothing by themselves)
or `Ignored`. A `REQUEST` or `PUBLISH` of a master replaces the whole series of its `UID`; of an instance, only that override.

<!-- sample: itip-flow -->
```kotlin
// METHOD:REQUEST, DTSTAMP from the clock
val request = ITip.request(event, organizer, listOf(bob), clock)
val reply = ITip.reply(request, "mailto:bob@example.com", PartStat.Accepted, clock)
// FROM against ATTENDEE (or ORGANIZER) and SENT-BY
if (IMip.checkSender(reply, smtpFrom) == SenderCheck.Matches) {
    val result = ITip.apply(organizerCalendar, reply) // Applied, Outdated, NotFound…
    if (result.outcome == ITipOutcome.Applied) saved += result.calendar
}
```

(`event` is a `VEvent`, `organizer` an `Organizer`, `bob` an `Attendee`, `organizerCalendar` the organizer's stored
`VCalendar`, `smtpFrom` the address of the mail sender, `clock` a `kotlin.time.Clock` and `saved` a mutable list.)

### Mail handling

An invitation arrives by mail, and a mail can lie. `IMip.checkSender` compares the `From` address with the party the message
claims to come from (the organizer for messages from the organizer, an attendee for replies) and tells you if it `Matches`,
acts on behalf of the party through `SENT-BY`, is a `Mismatch` or cannot be checked (`Unknown`). `IMip.untrustedContent` lists
what a client must not fetch or render without care: links (`URL`, `ATTACH` by reference, `CONFERENCE`), markup
(`X-ALT-DESC`, `STYLED-DESCRIPTION`) and inline attachments; the list of kinds grows, so it is marked `@ExperimentalICalApi`
(see [Compatibility](compatibility.md#api-stability)) and a `when` over `UntrustedKind` keeps an `else` branch.
`IMip.contentType` gives the `Content-Type` for an outgoing message.

Types: `untrustedContent` returns `UntrustedContent` items, each with the `path`, the `kind` (`UntrustedKind`) and the `property`;
`checkSender` returns a `SenderCheck`.

<!-- sample: itip-revise -->
```kotlin
// SEQUENCE + 1 when DTSTART, DTEND, RRULE… changed
val moved = ITip.revise(previous, edited)
val links = IMip.untrustedContent(request) // URLs, HTML, attachments: show, never fetch
val contentType = IMip.contentType(Method.Request) // text/calendar; method=REQUEST; charset=UTF-8
```

## Fixups

Real feeds are wrong in recurring ways, and the fixes are known. A `Fixup` is an opt-in repair: it returns a new calendar and a
list of changes (the name of the fix and the path, never a value), or the same calendar when nothing needed repair.

| Fixup | Repairs |
|---|---|
| `windowsTzids` | Windows and Outlook `TZID`s, IANA links and `/mozilla.org/` prefixes become IANA ids |
| `xWrTimezone` | floating times get the zone named by the non-standard `X-WR-TIMEZONE`, as Google and Apple Calendar mean it |
| `xWrCalendarName` | adds `NAME` and `DESCRIPTION` of RFC 7986 from `X-WR-CALNAME` and `X-WR-CALDESC` |
| `outlook` | Windows names, all-day events written as midnight date-times, upper-case `MAILTO:` |
| `lotusNotes` | Windows names and empty values such as `DTEND:` |
| `google` | `X-WR-TIMEZONE`, calendar name, a `UNTIL` with a time on a rule of an all-day event, `X-GOOGLE-CONFERENCE` |
| `vcalendar10` | vCalendar 1.0 files: `DCREATED`, quoted-printable text, vCard-style addresses, `AALARM` and `DALARM`, 1.0 rules |
| `byProdId()` | picks one of the above from `PRODID` and `VERSION` |
| `compose(...)` | applies several in order |

<!-- sample: fixup-apply -->
```kotlin
// one repair: floating times get the X-WR-TIMEZONE zone
val result = Fixups.xWrTimezone.apply(calendar)
val start = result.calendar.events.single().start // 2026-10-05T10:00[Europe/Berlin]
val changes = result.changes.map { "${it.fixup} at ${it.path}" } // what changed and where, never the values
// picks Outlook, Lotus Notes, Google or vCalendar 1.0
val byProducer = Fixups.byProdId().apply(calendar)
```

Types: `apply` returns a `FixupResult` with the `calendar`, `isChanged` and the `changes`, each a `FixupChange` with the `fixup` id
and the `path`.

Fixes that work line by line can run while the feed is read, with no second pass over the tree; they expose the same repair as
[interceptors](parsing.md#interceptors). A fix that needs the whole tree has none, and an empty list. Registering
interceptors needs `@OptIn(ExperimentalICalApi::class)`, as does a `Fixup` of your own: the ready-made fixups and `apply` are
frozen for 1.0, the interface that you would implement is not (see [Compatibility](compatibility.md#api-stability)).

<!-- sample: fixup-intercept -->
```kotlin
val parser = ICal.parser {
    // the same repairs, line by line, as the feed is read
    Fixups.lotusNotes.interceptors().forEach { intercept(it) }
}
val result = parser.parse(feed)
```

## The linter

`Linter` combines validation with interoperability rules (`ICAL-L021` to `ICAL-L025`): vendor extensions that clients read
differently, alias time zone names, an upper-case `MAILTO:`, empty values of non-text properties. `LintReport.fixable` lists
what `Linter.fix` can repair, by reusing the fixups. Choose your rules with `LintRules`: switch codes off or on, or replace the
validation rule set.

<!-- sample: lint-fix -->
```kotlin
val report = Linter.lint(calendar) // validation plus interoperability rules
// includes ICAL-L021…L025, the repairs of Fixups
val fixable = report.fixable.map { it.code.id }
val fixed = Linter.fix(calendar, report) // applies the fixable ones
val clean = Linter.lint(fixed).issues.none { it.code == ProblemCode.VendorTimeZone }
// choose your rules
val quiet = Linter.lint(calendar, LintRules.builder().disable(ProblemCode.VendorTimeZone).build())
```

## Next

- Every `ICAL-L` code: [Diagnostics](diagnostics.md#ical-l-validation-and-lint).
- Strict parsing and value problems: [Parsing](parsing.md#lenient-and-strict).
