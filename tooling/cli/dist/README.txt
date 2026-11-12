calendrum: a command line for iCalendar files. It checks, repairs, converts, expands, compares and anonymizes them, and
looks up time zones and problem codes.

    calendrum --help
    calendrum lint feed.ics
    calendrum fix --in-place feed.ics
    calendrum expand feed.ics --from 2026-01-01T00:00:00Z --to 2026-02-01T00:00:00Z --zone Europe/Berlin

Exit codes: 0 when all is well, 1 when there is something to report (the linter found a problem, two calendars differ, the
output is incomplete), 2 when the command could not do its work (wrong use, a file that cannot be read or written, an error
from an operation). Add --json for the answer of the operation as JSON, which scripts can rely on.

It does not use the network, does not read the environment and never uses the time zone of the machine: times are in UTC
unless --zone says otherwise. It reads only the files named on the command line.

Calendrum is licensed under the Apache License, Version 2.0, in LICENSE. NOTICE lists the data in this binary and
THIRD-PARTY-LICENSES.txt the libraries.
