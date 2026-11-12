# Performance

The design goal is simple: parsing works on bytes as a stream, values are decoded when you ask, and what you do not select
is skipped. This page is the performance report of the library before 1.0: what was measured, how, and what the numbers say.

> **The run.** Every number below comes from one report, taken over two nights on a developer laptop: Apple M5 (4 performance
> and 6 efficiency cores), macOS 27.0.1, on mains power, with no other program open. The JVM figures were taken on 2026-10-31;
> the other platforms, the start of a process, occurrences and free/busy time on 2026-11-08. A laptop at night is the closest
> to a dedicated machine there is today; a dedicated machine will repeat the report before the first public release. Inside one
> table the figures can be compared; across platforms, only by their order of magnitude.

## Method

- **Tool.** kotlinx-benchmark 0.5.0 on every platform: JMH 1.37 on the JVM, its own harness on Kotlin/Native, JS and Wasm.
- **Profiles.** JVM: 3 forks, 5 warm-up and 10 measured iterations of 2 s each, Temurin 21.0.10 with G1 and a 2 GB heap.
  Native (`macosArm64`): 5 warm-up and 30 measured iterations of 2 s. JS and Wasm, on Node.js 24.10 and 25.0: 3 warm-up and 5
  measured iterations of 1 s. A full profile on Node.js would not fit the night, and the gap between those platforms and the
  JVM is far larger than the difference between the profiles.
- **Error.** The `±` of a score is JMH's 99.9 percent confidence interval. It is not the whole story: on this laptop, runs on
  different days drift by up to 15 percent. Within this report, code that has not changed since the previous run (the rival
  libraries and a plain scan of the bytes) moved by no more than 3.4 percent. Do not read anything into a difference of 5 percent
  or less.
- **Allocation.** JVM only: the bytes allocated per operation, from JMH's GC profiler (`gc.alloc.rate.norm`).
- **Throughput** is in MB of input per second (10^6 bytes): operations per second times the size of the input.
- **Corpora.** Five generated files (generator version 1, fixed seed), so everyone can regenerate the same bytes:

| Corpus | Size | Shape |
|---|--:|---|
| `invite` | 2.8 KB | one invitation with attendees and an alarm |
| `invite-batch` | 3.4 MB | a thousand separate invitations |
| `personal` | 4.0 MB | a personal calendar: time zones, recurring events with exceptions, alarms |
| `university` | 15.8 MB | a university timetable: course series with a rule each, overrides, attendees |
| `huge` | 100 MB | a flat feed of events of the two shapes above, one component per occurrence, no recurrence rules |

- **What is comparable.** ical4j 4.3.0 and biweekly 0.6.8 decode every value as they parse and build their own model. Against
  them, the typed model of this library with every field read is the equal amount of work; a tree, which decodes nothing, is not.
  On Node.js a tree of the 100 MB feed does not fit the heap, so JS and Wasm were measured without `huge`.

## Starting a process

The first parse of the `invite` file in a new process, median of 80 processes, in milliseconds. The process time runs from the
start of the process to its exit, so it includes starting the JVM or the binary:

| | First parse | Whole process |
|---|--:|--:|
| JVM, reading the file only | 0.07 | 34.2 |
| JVM, tree | 13.1 | 49.1 |
| JVM, typed model with every field read | 29.2 | 75.6 |
| JVM, ical4j, with every property read | 83.9 | 119.9 |
| JVM, biweekly, with every property read | 62.9 | 98.0 |
| `macosArm64`, tree | 0.07 | 4.5 |
| `macosArm64`, typed model with every field read | 0.13 | 4.6 |

At equal work the first parse is 2.9 times faster than ical4j and 2.2 times faster than biweekly. Most of its 29 ms on the JVM
is loading the classes of the model and of the values, which happens once per process. On Native the parse is lost in the time it
takes to start the binary.

## Reading a file

Each row does more work than the one above it.

| JVM, MB/s | `personal` | `university` | `huge` |
|---|--:|--:|--:|
| tree (`ICal.parse`): lexing, unfolding, building nodes; values untouched | 974 | 941 | 787 |
| tree and every value read once (decoded to text and cached) | 567 | 563 | 417 |
| typed model with every field read | 334 | 307 | 236 |
| fingerprints of all components (no values parsed) | 824 | 831 | 729 |
| components as a stream (`components`) | 674 | 631 | 607 |
| the event cursor | 1,158 | 1,249 | 1,063 |

The same on the other platforms:

| MB/s, `personal` / `university` | JVM | `macosArm64` | JS (Node.js) | Wasm (Node.js) |
|---|--:|--:|--:|--:|
| tree | 974 / 941 | 569 / 421 | 364 / 274 | 463 / 360 |
| tree and every value read | 567 / 563 | 188 / 150 | 99 / 92 | 96 / 106 |
| typed model | 334 / 307 | 102 / 81 | 45 / 45 | 57 / 60 |
| fingerprints | 824 / 831 | 493 / 395 | 200 / 163 | 425 / 337 |
| the event cursor | 1,158 / 1,249 | 778 / 694 | 524 / 465 | 660 / 587 |

For feeds that do not fit in memory, use the stream or the cursor: the cursor allocates 3 to 7 KB per run on the JVM, whatever
the size of the input.

### Memory of a stream

Measured separately, in an earlier session: a 100 MB feed read as a `ComponentStream` from a file in a JVM heap of 128 MB took
0.25 s, 399 MB/s; the heap peaked at 34.6 MiB and grew by 0.3 MiB after a garbage collection. A stream's memory is bounded by the
block it reads and the longest line, not by the size of the file.

## Writing

| JVM, MB/s | `personal` | `university` | `huge` |
|---|--:|--:|--:|
| byte-stable write of a parsed tree | 2,464 | 2,811 | 2,286 |
| canonical write (every line rewritten) | 929 | 760 | 664 |

A tree read from input keeps the spelling of every line, so byte-stable writing has little to do: a folded line is copied as
bytes. On `personal`, the byte-stable write measures 1,467 MB/s on `macosArm64`, 897 on JS and 1,548 on Wasm.

## Against ical4j and biweekly

The same corpora on the JVM. First the equal amount of work: the typed model with every field read, against each library parsing
and then reading every property.

| JVM, MB/s | `personal` | `university` | `huge` |
|---|--:|--:|--:|
| Calendrum, typed model | 334 | 307 | 236 |
| biweekly | 37.8 | 31.8 | 37.7 |
| ical4j | 18.3 | 15.8 | 18.1 |

That is 6 to 10 times faster than biweekly and 12 to 39 times faster than ical4j on all five corpora, with about 5 to 6 times less
allocation than biweekly and 14 to 64 times less than ical4j.

The levels below are not equal work: they show what each level of this library costs against their full parse and write.

| JVM, MB/s | `personal` | `university` | `huge` |
|---|--:|--:|--:|
| Calendrum tree | 974 | 941 | 787 |
| Calendrum tree and every value read | 567 | 563 | 417 |
| biweekly parse | 41.8 | 35.4 | 38.9 |
| ical4j parse | 19.7 | 16.6 | 19.2 |
| Calendrum write (byte-stable / canonical) | 2,464 / 929 | 2,811 / 760 | 2,286 / 664 |
| biweekly write | 130 | 93.5 | 114 |
| ical4j write | 51.0 | 44.7 | 42.8 |

Per megabyte of the `university` corpus, building the tree allocates 2.2 MB, the typed model 12.6 MB, biweekly 73 MB and
ical4j 243 MB.

Mapping a calendar to your own classes with a [schema](selective-parsing-and-schema.md), against the same mapping written by hand
over each library: 462, 419 and 346 MB/s on `personal`, `university` and `huge`, which is 9 to 12 times faster than biweekly and
20 to 27 times faster than ical4j.

## Recurrence rules

Expanding a set of rules over ten years, operations per second:

| Rules | Calendrum | biweekly | ical4j |
|---|--:|--:|--:|
| simple | 11,620 | 3,625 | 93.1 |
| complex | 3,593 | 956 | 37.3 |

That is 3.2 to 3.8 times faster than biweekly and 96 to 125 times faster than ical4j. On the other platforms (simple / complex):
4,322 / 1,692 on `macosArm64`, 1,148 / 333 on JS, 5,550 / 2,231 on Wasm.

## Time zones

An operation looks up 4,096 moments in one zone, operations per second. `java.time` is a reference, not a target: it caches the
transitions of a zone.

| JVM | Calendrum, IANA data | Calendrum, `VTIMEZONE` | `java.time` | ical4j |
|---|--:|--:|--:|--:|
| offset at a moment | 4,245 | 4,103 | 10,700 | 67.5 |
| local time to an instant | 5,072 | 5,155 | 3,422 | — |

Finding an offset is 63 times faster than ical4j and 2.5 times slower than `java.time`; converting a local time is 1.5 times
faster than `java.time`. A zone compiled from a `VTIMEZONE` is as fast as the bundled IANA data. On the other platforms (offset /
conversion): 1,731 / 2,601 on `macosArm64`, 331 / 222 on JS, 2,385 / 2,743 on Wasm.

## Occurrences

All occurrences of a calendar in the window of a year, windows per second:

| JVM | `personal` | `university` |
|---|--:|--:|
| Calendrum | 227 | 7.69 |
| ical4j | 60.5 | 3.58 |

3.7 and 2.1 times faster than ical4j, with 6.5 and 9 times less allocation. On `personal`: 57.5 windows per second on
`macosArm64`, 20.2 on JS, 66.3 on Wasm.

Since this run, grouping the components of a series and looking up their zones got faster: on `personal` the windows are 16
percent faster on Wasm, 6 percent on the JVM and 5 percent on `macosArm64`, measured on the same machine with runs interleaved.

The occurrences of one semester of a real, anonymized university timetable (488 occurrences, with the time zones of the file):
3,801 windows per second on the JVM, about 1.9 million occurrences per second, with 1.4 KB allocated per occurrence.

## Free and busy time

Merging occurrences that overlap into busy intervals, operations per second:

| Occurrences | JVM | `macosArm64` | JS | Wasm |
|--:|--:|--:|--:|--:|
| 1,000 | 9,036 | 2,511 | 370 | 2,712 |
| 10,000 | 635 | 228 | 33.7 | 226 |
| 100,000 | 62.7 | 18.5 | 3.34 | 16.5 |

The time grows almost in proportion to the input: 6.3 million occurrences per second on the JVM at 100,000.

## Selective parsing

With a [selection](selective-parsing-and-schema.md) the lexer skips what you did not name. JVM, MB/s of input:

| Selection | `personal` | `university` | `huge` |
|---|--:|--:|--:|
| `Full` | 983 | 947 | 790 |
| `Schedule` | 997 | 900 | 794 |
| `SyncMetadata` | 1,770 | 1,526 | 1,542 |
| `FreeBusy` | 1,589 | 1,322 | 1,209 |

`SyncMetadata` is 61 to 95 percent faster than the full tree and allocates 2.5 to 2.7 times less; `FreeBusy` is 40 to 62
percent faster. `Schedule` allocates 13 to 26 percent less than the full tree but is no faster: from 5 percent slower on
`university` to 1 percent faster on `personal`, and 7 percent slower on the real timetable. Select it for the smaller result, not for
speed.

## Decoding values

Values are decoded from ranges of the text, without substrings; the time of a `DATE-TIME` is one packed integer inside the
decoder. The `DTSTART`, `DTEND`, `EXDATE`, `RRULE`, `DURATION`, `TRIGGER` and `ATTENDEE` values of a corpus, decoded leniently,
in million values per second:

| Corpus | Values | JVM | `macosArm64` | JS | Wasm |
|---|--:|--:|--:|--:|--:|
| `invite` | 10 | 84.5 | 34.7 | 35.9 | 34.3 |
| `personal` | 12,224 | 54.1 | 22.0 | 19.3 | 23.1 |
| `university` | 90,700 | 41.4 | 14.0 | 14.6 | 16.2 |
| `huge` | 462,722 | 62.6 | 23.6 | — | — |

At the decoding level the bytes allocated per value are the size of the result itself: a `DATE-TIME` is 48 bytes, a
`DURATION` 40, a recurrence rule about 130 including its arrays. The typed model adds its wrappers on top.

## Validation and lint

`Validator.validate` and `Linter.lint` take time in proportion to the calendar (or `n log n`): the index of an element among
its namesakes and "one issue per code and path" are found by sorting, and no hash table is keyed by strings of the input, so a
crafted calendar cannot make them slow.

| MB/s | `invite` | `personal` | `university` |
|---|--:|--:|--:|
| `validate`, JVM | 1,183 | 791 | 594 |
| `lint`, JVM | 641 | 500 | 352 |
| `validate`, `macosArm64` / JS / Wasm | 258 / 213 / 296 | 132 / 140 / 192 | 94 / 93 / 116 |

The same timetable was validated at 20 MB/s on the JVM before this design: 30 times slower.

## Fixups

A parse with the interceptor of a fixup costs the same as a parse without it (963 to 996 MB/s on `personal`, JVM). Applying the
fixup is what costs: `byProdId` 951, Google 806 and Outlook 626 MB/s for the parse and the repair together.

## Integrations

kotlinx-io and Okio: `parse` copies a `Buffer` of up to 16 MiB into one array and parses it as an array, so it is only 4 to 8
percent slower than parsing a `ByteArray` on the JVM. A larger `Buffer`, as on `huge`, is read block by block and is 29 percent
slower. Writing to a `Sink` is 3 to 10 percent slower than writing to a `ByteArray` on the larger corpora; on the tiny `invite`
the fixed cost of a `Sink` shows (30 to 38 percent).

## Comparing two versions of a feed

The fingerprints of two versions of `university`, one in a hundred events edited, compared: 207 comparisons per second on the JVM
(4.8 ms), 100 on `macosArm64`, 105 on JS and 126 on Wasm. With the fingerprints of the new version taken from its bytes in the same
step: 40 per second on the JVM. Keys come from the input, so no hash table holds them, which keeps a crafted feed from making the
comparison slow. Since this run the comparison orders the keys by a hash with a radix sort instead of comparing their strings:
measured on the same machine, runs interleaved, it is 3.8 times faster on the JVM (768 comparisons per second), 5.0 times on
`macosArm64` (500), 3.5 times on Wasm (454) and 1.4 times on JS (145).

## Run it yourself

```bash
./gradlew :benchmarks:quickBenchmark
```

runs the `quick` profile on every platform of the host. A single platform and suite:

```bash
./gradlew :benchmarks:jvmQuickBenchmark -Pbench.include='\.parse\.'
```

The whole report is two nights of a driver script, which waits for mains power and a quiet machine before each step, repeats a
step that ran on battery and copies the results after each one:

```bash
benchmarks/campaign.sh jvm
benchmarks/campaign.sh native-web
```

The first takes about 7 hours, the second about 6; `--dry-run` prints the steps and the estimate without running them.
`:benchmarks:generateCorpora` writes the corpora with their checksums. Plug the laptop in, keep the lid open and close other
programs: a run on battery power is not comparable. To compare a change, measure before and after in one session, interleaved,
and distrust differences of 5 percent or less.

## Next

[Selective parsing and schemas](selective-parsing-and-schema.md) and [Parsing](parsing.md#streams-and-large-feeds) are the two
ways to pay less.
