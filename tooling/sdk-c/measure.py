#!/usr/bin/env python3
"""Measures the C library on macosArm64: the size, the price of a call from C and from Python, and the price of loading. With the
other libraries it adds their sizes and what wheels need to know: dependencies and the highest version of each symbol.

Usage: python3 -I measure.py <dylib> <feed.ics> [runs] [--others <linuxX64 so> <linuxArm64 so> <mingwX64 dll>]

The feed comes from sdk/c/bench/make_feed.py. Each run is a process of its own, and the result is the median of the runs.
"""
import ctypes
import gzip
import json
import os
import re
import shutil
import statistics
import subprocess
import sys
import tempfile
import time

HERE = os.path.dirname(os.path.abspath(__file__))
WINDOW = {"from": "2026-01-01T00:00:00Z", "to": "2027-01-01T00:00:00Z", "zone": "Europe/Berlin", "limit": 100000}
Bytes = ctypes.POINTER(ctypes.c_uint8)


def gzip_size(path):
    with open(path, "rb") as file:
        return len(gzip.compress(file.read(), 9))


def report_size(path):
    print(f"{os.path.basename(path)}: {os.path.getsize(path)} B raw, gzip -9 {gzip_size(path)} B")


def report_stripped(path, directory):
    stripped = os.path.join(directory, "stripped.dylib")
    subprocess.check_call(["strip", "-x", "-o", stripped, path])
    print(f"strip -x: {os.path.getsize(stripped)} B, gzip -9 {gzip_size(stripped)} B (spike: 4186960 raw, 3043824 stripped)")


def requests_of(feed):
    with open(feed, encoding="utf-8", newline="") as file:
        text = file.read()
    return {
        "explain": ("explain", b'{"code":"ICAL-S003"}', 20000),
        "inspect": ("inspect", json.dumps({"ics": text}).encode(), 300),
        "expand": ("expand", json.dumps({"ics": text, **WINDOW}).encode(), 100),
    }


def python_run(path, feed):
    """One run in this process' child: the milliseconds of a call of each kind through ctypes."""
    library = ctypes.CDLL(path)
    library.calendrum_call.restype = ctypes.c_int32
    library.calendrum_call.argtypes = [ctypes.c_char_p, ctypes.c_char_p, ctypes.c_size_t, ctypes.POINTER(Bytes),
                                       ctypes.POINTER(ctypes.c_size_t)]
    library.calendrum_free.argtypes = [Bytes]
    library.calendrum_free.restype = None
    results = {}
    for name, (operation, request, rounds) in requests_of(feed).items():
        operation_bytes = operation.encode()
        size = len(request)

        def once():
            out, length = Bytes(), ctypes.c_size_t()
            code = library.calendrum_call(operation_bytes, request, size, ctypes.byref(out), ctypes.byref(length))
            if code != 0:
                raise SystemExit(f"code {code}")
            library.calendrum_free(out)

        for _ in range(max(rounds // 10, 5)):
            once()
        start = time.perf_counter()
        for _ in range(rounds):
            once()
        results[name] = (time.perf_counter() - start) / rounds * 1e3
    print(json.dumps(results))


def median_runs(command, runs):
    values = {}
    for _ in range(runs):
        line = subprocess.check_output(command, text=True).strip().splitlines()[-1]
        for key, value in json.loads(line).items():
            values.setdefault(key, []).append(value)
    return {key: (statistics.median(found), min(found), max(found)) for key, found in values.items()}


def report_load(path, program, runs):
    rows = []
    for _ in range(runs):
        output = subprocess.check_output([program, path], text=True)
        rows.append([float(value) for value in output.splitlines()[0].split()[1:]])
    names = ["dlopen ms", "first function ms", "first explain ms", "dlopen to the first response ms", "RSS before MiB", "RSS loaded MiB",
             "RSS runtime started MiB", "RSS first response MiB", "RSS after 20 expand MiB", "threads before", "threads loaded",
             "threads runtime started", "threads first response", "threads after 20 expand"]
    for index, name in enumerate(names):
        column = [row[index] for row in rows]
        print(f"  {name}: median {statistics.median(column):.3f}, min {min(column):.3f}, max {max(column):.3f}")


def report_other(path):
    report_size(path)
    text = subprocess.check_output(["objdump", "-p", path], text=True)
    if path.endswith(".so"):
        print("  DT_NEEDED: " + ", ".join(re.findall(r"NEEDED\s+(\S+)", text)))
        symbols = subprocess.check_output(["objdump", "-T", path], text=True)
        versions = {}
        for family, version in re.findall(r"\b(GLIBC|GLIBCXX|CXXABI|GCC)_([0-9.]+)\b", symbols):
            key = tuple(int(part) for part in version.split("."))
            if family not in versions or key > versions[family][0]:
                versions[family] = (key, version)
        print("  highest versions: " + ", ".join(f"{family}_{found[1]}" for family, found in sorted(versions.items())))
    else:
        print("  imports: " + ", ".join(sorted(set(re.findall(r"DLL Name: (\S+)", text)))))


def main():
    arguments = sys.argv[1:]
    if arguments and arguments[0] == "--python-run":
        python_run(arguments[1], arguments[2])
        return
    others = []
    if "--others" in arguments:
        at = arguments.index("--others")
        others = arguments[at + 1:]
        arguments = arguments[:at]
    path = os.path.abspath(arguments[0])
    feed = arguments[1]
    runs = int(arguments[2]) if len(arguments) > 2 else 20

    with tempfile.TemporaryDirectory() as directory:
        report_size(path)
        report_stripped(path, directory)
        minimum = subprocess.check_output(["otool", "-l", path], text=True)
        print("minimum macOS: " + re.search(r"LC_BUILD_VERSION.*?minos (\S+)", minimum, re.DOTALL).group(1))
        for other in others:
            report_other(other)

        library_directory = os.path.dirname(path)
        bench = os.path.join(directory, "bench")
        load_cost = os.path.join(directory, "load_cost")
        test = os.path.join(HERE, "test")
        include = os.path.join(HERE, "include")
        common = ["clang", "-std=c11", "-O2", f"-I{test}", f"-I{include}"]
        subprocess.check_call([*common, os.path.join(test, "bench.c"), f"-L{library_directory}", "-lcalendrum",
                               f"-Wl,-rpath,{library_directory}", "-o", bench])
        subprocess.check_call([*common, os.path.join(test, "load_cost.c"), "-o", load_cost])
        print(f"{runs} runs, a process for each; median (min-max), ms for a call")
        for label, command in (("C", [bench, feed]),
                               ("Python ctypes", [sys.executable, "-I", os.path.abspath(__file__), "--python-run", path, feed])):
            for name, (middle, low, high) in median_runs(command, runs).items():
                print(f"  {label}, {name}: {middle:.4f} ({low:.4f}-{high:.4f})")
        print(f"loading, {runs} runs:")
        report_load(path, load_cost, runs)
    if shutil.which("pmset"):
        print(subprocess.check_output(["pmset", "-g", "batt"], text=True).splitlines()[0])


if __name__ == "__main__":
    main()
