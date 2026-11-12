#!/usr/bin/env python3
"""Measures a server binary: its size, the time to the answer to `initialize`, memory after it, `tools/list`, and `ics_expand`
on a file given by path.

Usage: measure.py <binary> <feed.ics> [runs]

The feed comes from sdk/c/bench/make_feed.py. The server runs in its directory: a client without roots may read there.
"""
import gzip
import json
import os
import shutil
import statistics
import subprocess
import sys
import tempfile
import time

INITIALIZE = {
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize",
    "params": {
        "protocolVersion": "2025-11-25",
        "capabilities": {},
        "clientInfo": {"name": "measure", "version": "0"},
    },
}
INITIALIZED = {"jsonrpc": "2.0", "method": "notifications/initialized"}
LIST = {"jsonrpc": "2.0", "id": 2, "method": "tools/list"}
WARM_CALLS = 20


def expand(feed):
    arguments = {
        "path": feed,
        "from": "2026-01-01T00:00:00Z",
        "to": "2027-01-01T00:00:00Z",
        "zone": "Europe/Berlin",
        "limit": 100000,
    }
    return {"jsonrpc": "2.0", "id": 3, "method": "tools/call", "params": {"name": "ics_expand", "arguments": arguments}}


def send(process, message):
    process.stdin.write((json.dumps(message) + "\n").encode())
    process.stdin.flush()


def timed(process, message):
    start = time.perf_counter()
    send(process, message)
    line = process.stdout.readline()
    # Before the parsing: that is the time of this script, and a large answer takes it a while.
    elapsed = (time.perf_counter() - start) * 1000
    answer = json.loads(line)
    assert answer["id"] == message["id"] and "result" in answer, answer
    return elapsed, answer["result"]


def run_once(binary, feed):
    start = time.perf_counter()
    process = subprocess.Popen(
        [binary],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        cwd=os.path.dirname(feed),
    )
    send(process, INITIALIZE)
    answer = json.loads(process.stdout.readline())
    started = (time.perf_counter() - start) * 1000
    assert answer["id"] == 1 and "result" in answer, answer
    rss = int(subprocess.check_output(["ps", "-o", "rss=", "-p", str(process.pid)]).strip())
    send(process, INITIALIZED)
    listed, tools = timed(process, LIST)
    assert len(tools["tools"]) == 12, tools
    expanded, result = timed(process, expand(feed))
    assert not result["isError"], result
    assert result["structuredContent"]["result"]["count"] == 2000, result["structuredContent"]["result"]["count"]
    # The first call of a process pays for everything that initializes on first use, as a client that starts a server does.
    again = [timed(process, expand(feed))[0] for _ in range(WARM_CALLS)]
    process.stdin.close()
    code = process.wait(timeout=10)
    assert process.stderr.read() == b"", "standard error is not empty"
    return started, rss, listed, expanded, statistics.median(again), code, answer["result"]["protocolVersion"]


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
    binary = os.path.abspath(sys.argv[1])
    feed = os.path.abspath(sys.argv[2])
    runs = int(sys.argv[3]) if len(sys.argv) > 3 else 20
    report_size(binary)
    results = [run_once(binary, feed) for _ in range(runs)]
    times = [result[0] for result in results]
    print(f"initialize: median {statistics.median(times):.1f} ms, max {max(times):.1f} ms, runs {runs}")
    print(f"rss after initialize: median {statistics.median(result[1] for result in results) / 1024:.1f} MiB")
    print(f"tools/list: median {statistics.median(result[2] for result in results):.1f} ms")
    print(f"ics_expand with path, first call: median {statistics.median(result[3] for result in results):.1f} ms")
    print(f"ics_expand with path, later calls: median {statistics.median(result[4] for result in results):.1f} ms")
    print(f"exit codes: {sorted(set(result[5] for result in results))}")
    print(f"protocol: {results[0][6]}")


if __name__ == "__main__":
    main()
