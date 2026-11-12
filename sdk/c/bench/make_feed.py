import sys
from pathlib import Path

sample = (Path(__file__).resolve().parents[3] / "examples" / "shared" / "sample.ics").read_bytes().decode()
head, rest = sample.split("BEGIN:VEVENT", 1)
lines = [head.rstrip("\r\n")]
for i in range(int(sys.argv[2])):
    day = 1 + i % 28
    lines += [
        "BEGIN:VEVENT",
        "UID:event-%d@example.com" % i,
        "DTSTAMP:20260901T080000Z",
        "DTSTART;TZID=Europe/Berlin:202609%02dT%02d0000" % (day, 8 + i % 10),
        "DURATION:PT1H",
        "RRULE:FREQ=WEEKLY;COUNT=10",
        "SUMMARY:Lecture %d\\, part of the course" % i,
        "LOCATION:Room %d" % (100 + i % 50),
        "DESCRIPTION:Notes for lecture %d with a long enough text to fold over the line limit of seventy-five octets" % i,
        "END:VEVENT",
    ]
lines.append("END:VCALENDAR")
Path(sys.argv[1]).write_bytes(("\r\n".join(lines) + "\r\n").encode())
