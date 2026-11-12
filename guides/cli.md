# Command line

`calendrum` is a command line for iCalendar files. It checks a calendar against the rules, repairs the defects that calendar
producers are known for, converts to and from jCal, lists the occurrences in a window of time, compares two calendars, strips
the text from one for a bug report, and looks up time zones and problem codes. It is one native binary. It does not use the
network, does not read environment variables, and does not use the time zone of the machine.

## Getting it

There are no ready-made builds yet. Build it from the repository, with JDK 21. The library goes to the local Maven repository
first, as the command line is a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :cli:dist
```

`tooling/cli/build/dist/` then has an archive for each target: `calendrum-<version>-macosArm64.tar.gz`, `-linuxX64.tar.gz`,
`-linuxArm64.tar.gz` and `-mingwX64.zip`. Each holds the binary, `NOTICE`, the licenses of the libraries inside and a short
`README.txt`. The binary is about 5 MB. No binary is signed or notarized, so macOS can refuse one that comes from a download
until you allow it.

Only the macOS binary has been run, and it is the one the tests run. The Linux and Windows binaries are built on macOS: they
link, and that is all that is known of them. On Windows the reading of files, the writing of files and the arguments that
are not ASCII use code that has been compiled and has not been run.

## Commands

```text
calendrum lint FILE          check a calendar
calendrum fix FILE           repair known defects
calendrum format FILE        write the canonical form
calendrum convert FILE       iCalendar to jCal, or jCal to iCalendar
calendrum expand FILE        list occurrences in a window
calendrum diff BEFORE AFTER  compare two calendars
calendrum anonymize FILE     remove text and addresses
calendrum tz NAME            look up a time zone
calendrum explain CODE       explain a problem code
```

Also `calendrum --help`, `calendrum COMMAND --help` and `calendrum --version`. A file named `-` is the standard input. A file is
read as bytes, so any encoding the library detects works, and it can be up to 64 MiB. A symbolic link is followed, and the name
needs no extension: the command does what you ask for.

| Command | Options |
|---|---|
| `lint` | `--rules lint\|rfc5545` (`lint` by default), `--fail-on warning\|error` (`warning` by default), `--limit N` |
| `fix` | `--fixups a,b` (the repairs, in order; `by_prod_id` and `lint` by default), `--in-place` |
| `format` | `--in-place` |
| `convert` | `--from ics\|jcal` (`jcal` for a name ending in `.json`, `ics` otherwise) |
| `expand` | `--from MOMENT` and `--to MOMENT` (required), `--zone ZONE`, `--limit N`, `--include-cancelled` |
| `diff` | `--limit N` |
| `anonymize` | `--shift-weeks N` |
| `tz` | `--at MOMENT` |

Every command takes `--json`, `-o FILE`, `--force` and `--help`. The names of the repairs for `--fixups` are `by_prod_id`,
`windows_tzids`, `x_wr_timezone`, `x_wr_calname`, `outlook`, `lotus_notes`, `google`, `vcalendar10` and `lint`. A moment is such
as `2026-01-01T00:00:00Z`.

**`lint`** prints one line for each finding. A finding of the reader has a position, and one of the rules has the path of the
property:

```text
feed.ics:4:1 ICAL-S007 error no ':' after the property name and parameters, line skipped
feed.ics: ICAL-L001 error A required property is missing. (VCALENDAR[0]/VEVENT[0]/DTSTAMP[0])
feed.ics: ICAL-L024 info An ORGANIZER or ATTENDEE with the scheme in upper case, MAILTO:. Found by the linter. (VCALENDAR[0]/VEVENT[0]/ATTENDEE[0]) [fixable]
feed.ics: 3 findings (2 errors, 0 warnings, 1 info)
```

A file with no calendar in it, such as an empty file or plain text, is a finding of the level `error` too, so an empty feed does
not pass a check: `feed.ics: error no calendar in the input`. `explain ICAL-S007` says what a code means.

**`fix`** and **`format`** print the calendar, always as UTF-8 with CRLF line endings. `fix` lists its changes on the standard
error, such as `feed.ics: windows_tzids VCALENDAR[0]/VEVENT[0]/DTSTART[0]`, with the name of each repair spelled as `--fixups` spells it. `format` is `fix` with no repairs at all.

**`expand`** prints one line for each occurrence, in UTC. With `--zone`, the local start is added:

```text
2026-01-05T10:00:00Z 2026-01-05T11:00:00Z Weekly [weekly@example.com] local 2026-01-05T11:00:00
```

Without `--zone` nothing is local, and floating times are read as UTC. The time zone of the machine is never used.

**`diff`** prints `+` for a component that was added, `-` for one that was removed and `~` for one that changed, then the
number of the ones that did not. `DTSTAMP` does not count as a change.

**`anonymize`** keeps the structure and removes text and addresses. A value in a place of structure stays only when it is a
value of structure: `STATUS:CONFIRMED` stays, `STATUS:` followed by a sentence is replaced like any text, and so is a `PRODID`
that does not name a known calendar program. A time zone that is not in the zone database or in the list of Windows gets a
pseudonym, the same one in its `VTIMEZONE` and in every `TZID=`. The key of its pseudonyms is random for each call and is shown
nowhere, so two runs on one file give different output. `--shift-weeks` moves every date by whole weeks.

## Output and exit codes

By default the output is for a person: short lines, and for the findings of `lint` the form `FILE:LINE:COLUMN CODE SEVERITY
MESSAGE`, as other linters have it. With `--json` the command prints the response of the operation, as the contract in
`conformance/ops/` has it, on one line and unchanged. That is what scripts should read. Fields and their meaning do not change
within a version of the contract.

| Code | Meaning |
|---|---|
| `0` | All is well. |
| `1` | There is something to report: `lint` found a problem at or above `--fail-on` (an input with no calendar is one), `diff` found a difference, the output is not complete, or the result does not hold all of the input (see below). |
| `2` | The command could not do its work: wrong use, a file that cannot be read or written, an operation that answered with an error, an input with no calendar for `fix`, `format`, `convert` or `anonymize`, or a result that was refused a file (see below). |

The exit codes are the same with and without `--json`. A message about an error goes to the standard error, as one line that
begins with `calendrum:`. An output that is not complete is never cut silently: the command exits with `1`, says so on the
standard error and, where an option helps, names it:

```text
calendrum: The output is incomplete: a limit was reached. Raise --limit (the largest value is 100000) or narrow the window with --from and --to.
```

An error is never written to a file. With `--json` its answer goes to the standard output, whatever `-o` says.

When the standard output cannot be written, because the disk is full or the reader has gone (`calendrum expand … | head -1`),
the exit code is `2` and there is no signal and no trace.

A list has 1000 items unless `--limit` says otherwise, up to 100000.

## A result that lost something never replaces a file

The reader of the library is forgiving: it skips a line that has no colon, drops a property that stands outside any component,
cuts a line that is longer than 64 KiB, stops at 100,000 components, replaces bytes that are not UTF-8, and guesses an encoding.
That is what a reader of feeds from strangers needs, and it is what a repair of your own file must not do silently. So:

- `fix`, `format`, `convert`, `expand` and `anonymize` look at what the reader said. A result **holds all of the input** if the output is
  not truncated, no problem is beyond the list of problems, and every problem is of a kind that loses nothing: a bare CR or LF
  as a line end (`ICAL-S002`, `S003`), a byte order mark (`S001`), a blank line (`S004`), an `END` with nothing open or one that
  closes a nested component (`S015` to `S017`), components left open at the end (`S018`, they are closed), and an encoding that
  the file itself declares (`S024`). Every other code, and every code that comes later, counts as a loss. A guessed encoding
  (`S025`, `S028`) is a loss: the bytes of the file cannot be had back from the text.
- A result that lost something is **never written over a file**. With `--in-place`, or with `-o` and `--force`, nothing is
  written, the file is as it was, and the exit code is `2`. The standard error has the problems and a sentence that says so:

```text
feed.ics:12:1 ICAL-S007 error no ':' after the property name and parameters, line skipped
calendrum: Nothing was written: 'feed.ics' is as it was. To see the result, write it to a new file with -o.
```

- With `-o` to a name that is free, and without `--force`, the result is written and the exit code is `1`. To the standard
  output it goes with the code `1` too, so `calendrum format a.ics > b.ics && mv b.ics a.ics` does not replace `a.ics`.

An input with no calendar at all, such as an empty file, plain text or a file of another kind, is not made into one: `fix`,
`format`, `convert` and `anonymize` exit with `2`, say `No calendar found in 'FILE': nothing was written`, and write nothing to a
file or to the standard output. With `--json` the answer is printed, as it is, and the code is the same. A calendar that `fix` or `format` would give back as a response above
16 MiB, because the response holds it whole, is refused with `OPS-TOO-LARGE` and the code `2`, and the file is as it was.

## Writing files

The result goes to the standard output. `-o FILE` writes it to a file instead, and `-o -` is the standard output. A file that
exists is not replaced unless `--force` is given. `fix` and `format` also take `--in-place`, which replaces the file that was read;
`--in-place` cannot go with `-o`, `--json` or the standard input.

Repair a file in place, knowing that it is safe:

```bash
calendrum fix --in-place feed.ics
```

It prints the changes, replaces `feed.ics` if the result holds all of it, and otherwise leaves it as it was.

A file is always written whole or not at all. The bytes go to a temporary file in the same directory, are flushed to the disk,
and then take the place of the target in one step. If anything fails, such as a directory that cannot be written, the file that
was there is as it was, no temporary file is left, and the exit code is `2`. A replaced file keeps its permissions, and the
temporary file has them from the first byte, so a private calendar is never readable by others while it is written. A
symbolic link is followed, so the link stays a link. What a replacement does not keep: the owner, the access control lists
and the extended attributes of the file, and a hard link, which is broken: the name now has a new file. On Windows the
permissions are not carried over. With `--in-place`, a file that has nothing to change is not written at all.

With `fix`, the changes go to the stream the result does not: to the standard error when the calendar is on the standard output,
and to the standard output when it is written to a file.

## In a pipeline

Check a feed before publishing it, and fail the job on any problem that is not just a note:

```bash
calendrum lint --fail-on warning feed.ics
```

Fail only on errors, and keep the report as JSON for later:

```bash
calendrum lint --fail-on error --json feed.ics > lint-report.json
```

In a job of any CI system, the step is the same line. The exit code `1` fails it, and so does `2`.

Compare what was published with what is about to be:

```bash
calendrum diff published.ics candidate.ics
case $? in 0) echo "same" ;; 1) echo "the calendar changes" ;; *) echo "diff failed" ;; esac
```

Repair the files of a directory in place, which is safe to interrupt:

```bash
for file in calendars/*.ics; do calendrum fix --in-place "$file"; done
```

Read a feed without saving it:

```bash
curl -s https://example.com/team.ics | calendrum expand - --from 2026-01-01T00:00:00Z --to 2026-02-01T00:00:00Z
```

## Security

**Text from a calendar can steer a terminal.** A `SUMMARY` can hold an escape sequence that changes the title of the window,
moves the cursor over lines already printed or fakes the output of a program. In every output that is written for a person,
each control character is shown as `\xHH`, and that includes `ESC`, the single-character `CSI` (`U+009B`) and `DEL`. A line
or paragraph separator, a mark that changes the direction of text, and a half of a surrogate pair are shown as `\uHHHH`.

Where the standard output is a terminal, the same holds for a calendar and jCal that are printed as themselves, except for
carriage return, line feed and tab, and for `--json`, which gets `\uHHHH` for what could steer a terminal: the same JSON value,
so a program that reads it sees no difference. Into a pipe or a file, a calendar goes byte for byte, because there it is data.
Pipe it to a file and not to `cat` if you do not trust it.

**No environment.** No environment variable is read. The key of `anonymize` is random for each call and cannot be set, so it
cannot be printed or leaked.

**No network, no links.** The command opens only the files named on the command line. A path or a link inside a calendar is
text and is never opened.

**Files.** The command reads what you name, and a symbolic link is followed. It is meant to be run by a person or a script that
you wrote. Do not pass it a path that comes from somebody else.

## Limits

| Limit | Value |
|---|---|
| A calendar | 64 MiB, and for files from strangers: lines up to 64 KiB, 1,000,000 lines, 100,000 components, depth 8, 2 seconds of parsing |
| A list | `--limit`, 1000 by default, 100,000 at most |
| A response | 16 MiB |
| Two files of `diff` | The request holds both in base64, and the limit on it is 128 MiB of characters: they fit if they are under 48 MiB each |

An operation on an ordinary file takes milliseconds. A feed of 200 events with 2000 occurrences is linted in about 9 ms on an
Apple M5, with the start of the process included.
