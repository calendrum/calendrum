"""What the tests share. The environment gives the paths: CALENDRUM_PACKAGE, CALENDRUM_CASES and CALENDRUM_GUIDE."""
import dataclasses
import json
import os
import signal
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
PACKAGE = os.environ["CALENDRUM_PACKAGE"]
CASES = os.environ["CALENDRUM_CASES"]
SCHEMAS = os.path.join(CASES, "schema")
SCENARIOS = os.path.join(HERE, "scenarios.py")

if PACKAGE not in sys.path:
    sys.path.insert(0, PACKAGE)

OPERATIONS = ("inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff", "convert", "anonymize",
              "tz_resolve", "explain")

ICS = ("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n"
       "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Plain\r\n"
       "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n")
WINDOW = {"from_": "2026-01-01T00:00:00Z", "to": "2026-02-01T00:00:00Z"}


def load_json(path):
    with open(path, encoding="utf-8") as file:
        return json.load(file)


def all_cases():
    found = []
    for name in sorted(os.listdir(CASES)):
        directory = os.path.join(CASES, name)
        if not os.path.exists(os.path.join(directory, "request.json")):
            continue
        meta = load_json(os.path.join(directory, "meta.json"))
        found.append({
            "id": name,
            "operation": meta["operation"],
            "request": load_json(os.path.join(directory, "request.json")),
            "expected": load_json(os.path.join(directory, "expected.json")),
            "ignored": set(meta.get("ignore_fields", [])),
        })
    return found


def comparable(value, ignored):
    """The rules of conformance/ops/README.md: messages and the fields a case lists do not count."""
    if isinstance(value, dict):
        return {key: comparable(item, ignored) for key, item in value.items() if key != "message" and key not in ignored}
    if isinstance(value, list):
        return [comparable(item, ignored) for item in value]
    return value


def to_plain(value, top=True):
    """A model back to the JSON values it came from: absent fields go, and `truncated` belongs to the envelope."""
    if dataclasses.is_dataclass(value):
        plain = {}
        for field in dataclasses.fields(value):
            item = getattr(value, field.name)
            if item is None or (top and field.name == "truncated"):
                continue
            plain[field.name.rstrip("_")] = to_plain(item, False)
        return plain
    if isinstance(value, tuple):
        return [to_plain(item, False) for item in value]
    return value


def run_scenario(name, *arguments, limit=300):
    """Runs a scenario in an interpreter of its own, in a session of its own, so that nothing it started can outlive the limit."""
    process = subprocess.Popen([sys.executable, "-I", "-B", SCENARIOS, name, *arguments], stdin=subprocess.DEVNULL,
                               stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, start_new_session=True)
    try:
        output, _ = process.communicate(timeout=limit)
    except subprocess.TimeoutExpired:
        os.killpg(process.pid, signal.SIGKILL)
        output, _ = process.communicate()
        raise AssertionError(f"{name} did not finish in {limit} s:\n{output}")
    return process.returncode, output
