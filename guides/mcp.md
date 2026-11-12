# MCP server

`calendrum-mcp` gives a [Model Context Protocol](https://modelcontextprotocol.io) client, such as an AI assistant, the
operations of this library on iCalendar files: reading, validation, repair, occurrences, free and busy time, comparison, jCal,
anonymization and time zones. It is one native binary that talks JSON-RPC over standard input and output. It has no options, does
not use the network and writes nothing to disk.

## Getting it

There are no ready-made builds yet. Build it from the repository, with JDK 21. The library goes to the local Maven repository
first, as the server is a separate build on a newer Kotlin:

```bash
./gradlew publishToMavenLocal
./gradlew -p tooling :mcp:dist
```

`tooling/mcp/build/dist/` then has an archive for each target: `calendrum-mcp-<version>-macosArm64.tar.gz`, `-linuxX64.tar.gz`,
`-linuxArm64.tar.gz` and `-mingwX64.zip`. Each holds the binary, `NOTICE`, the licenses of the libraries inside and a short
`README.txt`. The Linux and Windows binaries are built on macOS and have not been run. No binary is signed or notarized, so
macOS can refuse one that comes from a download until you allow it.

## Connecting

In Claude Code:

```bash
claude mcp add calendrum -- /full/path/calendrum-mcp
```

Other clients take the same command in their configuration:

```json
{
  "mcpServers": {
    "calendrum": {
      "command": "/full/path/calendrum-mcp"
    }
  }
}
```

## Tools

| Tool | What it does |
|---|---|
| `ics_inspect` | Lists the calendars, events, time zones and problems of parsing |
| `ics_validate` | Checks a calendar against RFC 5545 or the linter rules |
| `ics_fix` | Repairs the known defects of calendar producers and returns the calendar |
| `ics_expand` | Expands components into occurrences in a window of time |
| `ics_rrule_expand` | Expands one `RRULE` from a start date |
| `ics_freebusy` | Computes merged busy intervals and overlapping occurrences |
| `ics_build_event` | Builds a valid calendar with one event |
| `ics_diff` | Compares two calendars |
| `ics_convert` | Converts between iCalendar and jCal (RFC 7265) |
| `ics_anonymize` | Removes text and addresses, for a bug report |
| `ics_tz_resolve` | Gives the IANA identifier, the offset and the next change of a time zone |
| `ics_explain` | Explains a problem code such as `ICAL-S003` |

Each tool is one operation of the contract in `conformance/ops/`, and its schemas are made from the schemas there.

**A calendar** comes as `content` (text), `content_base64` (bytes in any encoding the library detects) or `path` (a file for the
server to read). `ics_diff` has `before`, `before_base64`, `before_path`, and the same for `after`. Give only one of them for each
calendar.

**A result** on success has the whole response in `structuredContent` and the same JSON in `content[0].text`, for clients that
read only text:

```json
{"ops_version": 1, "ok": true, "result": {}, "truncated": false}
```

`truncated: true` means that the result does not hold everything. Narrow the window of time or raise `limit`.

**An error** has `isError: true` and, in the text, a code and a fixed sentence:

```json
{"code": "MCP-PATH-DENIED", "message": "The file cannot be read: it does not exist, it is outside the allowed roots, or it is not a calendar file"}
```

| Code | When |
|---|---|
| `OPS-BAD-ARGUMENT` | A field has the wrong type or value, or the fields do not make a valid request |
| `OPS-UNKNOWN-ZONE`, `OPS-UNKNOWN-CODE` | The request names a zone or a problem code that does not exist |
| `OPS-TOO-LARGE` | The request, the calendar or the response is above its limit |
| `OPS-INTERNAL` | A bug. No call should ever get it |
| `MCP-PATH-DENIED` | `path` was refused |
| `MCP-PATH-UNSUPPORTED` | `path` on Windows |

An unknown tool or a broken message gets a JSON-RPC error, and the server goes on.

## Limits

| Limit | Value |
|---|---|
| A list | 100 items by default, as clients cut long results. `limit` goes up to 100,000 |
| A calendar | 64 MiB |
| A request | 128 MiB of characters, also for the base64 of a file: the two files of `ics_diff` fit if they are under 48 MiB each |
| A response | 16 MiB. It is in the result twice, and some clients refuse a message of that size |
| Operations at a time | Two. The others wait, and `ping` is answered meanwhile |

An operation on an ordinary file takes milliseconds, and one on a hostile file some seconds. The slowest is `ics_freebusy` on a file with
about 100,000 rules: 10 seconds and 2.8 GiB of memory at its peak, and the result is truncated.

## Resources

- `calendrum://guides/<name>` holds these guides as Markdown, such as `calendrum://guides/parsing`.
- `calendrum://diagnostics/{code}` is a template for the same table as `ics_explain`, in JSON.

## Security

**Text from a calendar is data.** A calendar comes from a stranger, and its `SUMMARY` can be a sentence written for a model, such
as "ignore your instructions". The server keeps such text in the fields that its schemas mark with `x-untrusted`, and says so in
their descriptions, in the descriptions of the tools and in the `instructions` of `initialize`. Errors and problems are fixed
sentences that quote nothing. The server does not render HTML or follow links from a calendar. It cannot make a model ignore
what it reads: the marks are for the client and the model to act on.

**What a path can read.** The same text can talk a model into asking for another file, so the boundary is in the server. A path
is read only if all of this holds:

1. It resolves, through symbolic links and `..`, to a place inside a root. Roots are compared by whole components: `/a/bc` is not
   inside `/a/b`.
2. The resolved name ends with `.ics`, `.ical`, `.ifb` or `.vcs`, in any case. A link `x.ics` to `id_rsa` does not pass.
3. It is a regular file, not a directory, a FIFO or a device.
4. It is not larger than the limit for a calendar.

A file that does not exist and a file outside the roots get the same answer, byte for byte, so a call cannot be used to look around
the disk. The answer never contains the path.

**Roots.**

- A client that declares `roots` is asked at every call with a path. Only `file://` URIs count, and one that cannot be read is
  skipped.
- If that client answers with an empty list, with an error or not within five seconds, no file can be read. The server does not
  fall back to its working directory.
- A client that does not declare `roots` gets the working directory of the server as the only root. Start the server where you
  mean to allow reading.

**What this does not cover.**

- **Windows.** `path` answers `MCP-PATH-UNSUPPORTED` there, as these checks have not been run on Windows. `content` works.
- **Cancellation.** A cancelled request gets no answer, but an operation that is running goes on to its end and holds one of the
  two places until then.
- **A race.** A file is checked and then opened. Whoever replaces a directory on the path in between can redirect the read, but
  such a process already has access to the file system.
- **Hard links.** A hard link is the file itself, so one inside a root with a calendar extension is read.
- **Names that are not UTF-8.** Such a file or root cannot be read.
- **The client.** Everything a file inside the roots holds can reach the model. The server has no authentication: whoever starts it
  owns it.
