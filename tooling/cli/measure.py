#!/usr/bin/env python3
"""Measures the command line binary: its size, and the time of `lint`, `format` and `expand` on a feed. With --jvm it also
times the JVM variant, examples/cli, on the same file, for a comparison.

Usage: measure.py <binary> <feed.ics> [runs] [--jvm <bin/calendrum-example-cli>]

The feed comes from sdk/c/bench/make_feed.py. The time of a run is from the start of the process to its end, as a script
that calls the command would see it; the maximum of memory is what the system says for the child.
"""
import gzip
import os
import shutil
import statistics
import subprocess
import sys
import tempfile
import time

WINDOW = ["--from", "2026-01-01T00:00:00Z", "--to", "2027-01-01T00:00:00Z", "--zone", "Europe/Berlin", "--limit", "100000"]


def run_once(command):
    start = time.perf_counter()
    with open(os.devnull, "wb") as sink:
        process = subprocess.Popen(command, stdout=sink, stderr=sink, stdin=subprocess.DEVNULL)
        _, status, usage = os.wait4(process.pid, 0)
    elapsed = (time.perf_counter() - start) * 1000
    process.returncode = os.waitstatus_to_exitcode(status)
    # ru_maxrss is in bytes on macOS and in kilobytes on Linux.
    rss = usage.ru_maxrss / (1 if sys.platform == "darwin" else 1024)
    return elapsed, rss / (1024 * 1024), process.returncode


def measure(label, command, runs):
    # The first run of a new binary on macOS is checked by the system: it is kept out of the median and shown on its own.
    first = run_once(command)
    results = [run_once(command) for _ in range(runs)]
    times = [result[0] for result in results]
    codes = sorted(set(result[2] for result in results))
    print(
        f"{label}: median {statistics.median(times):.1f} ms, min {min(times):.1f}, max {max(times):.1f}, "
        f"first {first[0]:.1f}, peak RSS median {statistics.median(result[1] for result in results):.1f} MiB, "
        f"exit {codes}, runs {runs}"
    )


def gzip_size(path):
    with open(path, "rb") as file:
        return len(gzip.compress(file.read(), 9))


def report_size(binary):
    print(f"size: {os.path.getsize(binary)} B raw, gzip -9 {gzip_size(binary)} B")
    if shutil.which("strip") is None:
        return
    with tempfile.TemporaryDirectory() as directory:
        stripped = os.path.join(directory, "stripped")
        shutil.copy(binary, stripped)
        subprocess.check_call(["strip", "-x", stripped])
        print(f"strip -x: {os.path.getsize(stripped)} B, gzip -9 {gzip_size(stripped)} B")


def main():
    arguments = sys.argv[1:]
    jvm = None
    if "--jvm" in arguments:
        index = arguments.index("--jvm")
        jvm = os.path.abspath(arguments[index + 1])
        del arguments[index:index + 2]
    binary = os.path.abspath(arguments[0])
    feed = os.path.abspath(arguments[1])
    runs = int(arguments[2]) if len(arguments) > 2 else 20
    report_size(binary)
    measure("calendrum lint", [binary, "lint", feed], runs)
    measure("calendrum format", [binary, "format", feed], runs)
    measure("calendrum expand", [binary, "expand", feed] + WINDOW, runs)
    if jvm is not None:
        measure("examples/cli validate (JVM)", [jvm, "validate", feed], runs)


if __name__ == "__main__":
    main()
