#!/usr/bin/env python3
"""Measures the package: the size of the wheels, the import, the first answer, and `expand` through each layer against plain ctypes.

Usage: python3 -I -B measure.py <directory holding the package> <directory of the wheels> <feed.ics> [runs]

The feed comes from sdk/c/bench/make_feed.py. Each run is a process of its own, and the result is the median of the runs.
"""
import glob
import json
import os
import resource
import shutil
import statistics
import subprocess
import sys
import time

WINDOW = {"from": "2026-01-01T00:00:00Z", "to": "2027-01-01T00:00:00Z", "zone": "Europe/Berlin", "limit": 100000}
ROUNDS = 100


def peak_mib():
    value = resource.getrusage(resource.RUSAGE_SELF).ru_maxrss
    return value / (1024 * 1024) if sys.platform == "darwin" else value / 1024


def import_run(package):
    sys.path.insert(0, package)
    start = time.perf_counter()
    import calendrum

    imported = time.perf_counter()
    ctypes_at_import = "ctypes" in sys.modules
    calendrum.explain("ICAL-S003")
    answered = time.perf_counter()
    print(json.dumps({
        "import ms": (imported - start) * 1e3,
        "import to the first answer ms": (answered - imported) * 1e3,
        "peak RSS after the first answer MiB": peak_mib(),
        "ctypes imported by the import": 1.0 if ctypes_at_import else 0.0,
    }))


def calls_run(package, feed):
    sys.path.insert(0, package)
    import calendrum
    from calendrum import _native

    with open(feed, encoding="utf-8", newline="") as file:
        text = file.read()
    fields = {"from_": WINDOW["from"], "to": WINDOW["to"], "zone": WINDOW["zone"], "limit": WINDOW["limit"]}
    request = {"ics": text, **WINDOW}
    payload = json.dumps(request).encode("utf-8")
    occurrences = calendrum.expand(text, **fields).count

    def timed(name, action):
        for _ in range(ROUNDS // 10):
            action()
        start = time.perf_counter()
        for _ in range(ROUNDS):
            action()
        return name, (time.perf_counter() - start) / ROUNDS * 1e3

    raw = _native.call(b"expand", payload)
    results = dict((
        timed("native call, bytes in and out", lambda: _native.call(b"expand", payload)),
        timed("json.loads of the response", lambda: json.loads(raw)),
        timed("call(), a dict in and out", lambda: calendrum.call("expand", request)),
        timed("expand(), with the models", lambda: calendrum.expand(text, **fields)),
    ))
    results["occurrences"] = float(occurrences)
    results["peak RSS MiB"] = peak_mib()
    print(json.dumps(results))


def median_runs(command, runs):
    values = {}
    for _ in range(runs):
        line = subprocess.check_output(command, text=True).strip().splitlines()[-1]
        for key, value in json.loads(line).items():
            values.setdefault(key, []).append(value)
    return {key: (statistics.median(found), min(found), max(found)) for key, found in values.items()}


def report(label, results):
    print(label)
    for name, (middle, low, high) in results.items():
        print(f"  {name}: {middle:.3f} ({low:.3f}-{high:.3f})")


def main():
    arguments = sys.argv[1:]
    if arguments[0] == "--import-run":
        import_run(arguments[1])
        return
    if arguments[0] == "--calls-run":
        calls_run(arguments[1], arguments[2])
        return
    package = os.path.abspath(arguments[0])
    wheels = arguments[1]
    feed = os.path.abspath(arguments[2])
    runs = int(arguments[3]) if len(arguments) > 3 else 20

    for path in sorted(glob.glob(os.path.join(wheels, "*.whl"))):
        print(f"{os.path.basename(path)}: {os.path.getsize(path)} B")
    me = os.path.abspath(__file__)
    for python in ("python3", "/usr/bin/python3"):
        version = subprocess.check_output([python, "-I", "-c", "import sys; print(sys.version.split()[0])"], text=True).strip()
        report(f"Python {version}: the import, {runs} processes; median (min-max)",
               median_runs([python, "-I", "-B", me, "--import-run", package], runs))
        report(f"Python {version}: expand of the year on the feed, {max(runs // 4, 3)} processes of {ROUNDS} calls, ms for a call",
               median_runs([python, "-I", "-B", me, "--calls-run", package, feed], max(runs // 4, 3)))
    if shutil.which("pmset"):
        print(subprocess.check_output(["pmset", "-g", "batt"], text=True).splitlines()[0])


if __name__ == "__main__":
    main()
