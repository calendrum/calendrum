#!/usr/bin/env python3
"""Builds the wheels of the package with nothing but the standard library, by the wheel specification (PEP 427, PEP 639).

Usage: python3 -I -B build_wheels.py --version <version> --license <LICENSE> --notice <NOTICE> --out <directory>
           --target <name> <native library> <license list> [--target ...]

A target name is macosArm64, linuxX64, linuxArm64 or mingwX64. The same inputs give the same bytes.
"""
import argparse
import base64
import hashlib
import os
import re
import sys
import zipfile

HERE = os.path.dirname(os.path.abspath(__file__))
SOURCE = os.path.join(HERE, "calendrum")

TAGS = {
    "macosArm64": ("py3-none-macosx_12_0_arm64", "libcalendrum.dylib"),
    "linuxX64": ("py3-none-manylinux_2_17_x86_64", "libcalendrum.so"),
    "linuxArm64": ("py3-none-manylinux_2_17_aarch64", "libcalendrum.so"),
    "mingwX64": ("py3-none-win_amd64", "calendrum.dll"),
}

# The earliest moment a zip file can hold, so that a rebuild gives the same bytes.
FIXED_TIME = (1980, 1, 1, 0, 0, 0)

SUMMARY = "Reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files (RFC 5545)."
DESCRIPTION = """\
# Calendrum

Reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files (RFC 5545), and looks up time zones and problem
codes. The package has no dependencies: it carries a native library, which loads at the first call.

Once a process has called the library, a child of `fork()` cannot: use `spawn` or `forkserver`, or call the library only in the
workers. Such a child gets `ForkedProcessError`. Running out of memory inside the native library ends the process.

A result holds what a calendar says, and a calendar comes from a stranger: treat its text as data.
"""


def metadata(version):
    # License-Expression and License-File are core metadata 2.4 (PEP 639); with them, no "License ::" classifier is allowed.
    return f"""\
Metadata-Version: 2.4
Name: calendrum
Version: {version}
Summary: {SUMMARY}
License-Expression: Apache-2.0
Classifier: Private :: Do Not Upload
Classifier: Programming Language :: Python :: 3
Classifier: Typing :: Typed
Requires-Python: >=3.9
License-File: LICENSE
License-File: NOTICE
License-File: THIRD-PARTY-LICENSES.txt
Description-Content-Type: text/markdown

{DESCRIPTION}"""


def wheel_metadata(tag):
    return f"Wheel-Version: 1.0\nGenerator: calendrum build_wheels.py\nRoot-Is-Purelib: false\nTag: {tag}\n"


def package_files():
    """The files of the package, and only these: a stray file in the source directory stays out of the wheels."""
    found = []
    for name in sorted(os.listdir(SOURCE)):
        if name.endswith(".py") or name == "py.typed":
            found.append(name)
    if "__init__.py" not in found or "_models.py" not in found:
        raise SystemExit(f"The package in {SOURCE} is not complete")
    return found


def read(path):
    with open(path, "rb") as file:
        return file.read()


def record_line(path, data):
    digest = base64.urlsafe_b64encode(hashlib.sha256(data).digest()).rstrip(b"=").decode("ascii")
    return f"{path},sha256={digest},{len(data)}"


def add(archive, path, data, executable=False):
    info = zipfile.ZipInfo(path, FIXED_TIME)
    info.create_system = 3
    info.compress_type = zipfile.ZIP_DEFLATED
    info.external_attr = ((0o755 if executable else 0o644) | 0o100000) << 16
    archive.writestr(info, data, compresslevel=9)


def build(target, library, licenses, license, notice, version, out):
    tag, library_name = TAGS[target]
    info_directory = f"calendrum-{version}.dist-info"
    entries = [(f"calendrum/{name}", read(os.path.join(SOURCE, name)), False) for name in package_files()]
    entries.append((f"calendrum/{library_name}", read(library), True))
    entries += [
        (f"{info_directory}/METADATA", metadata(version).encode("utf-8"), False),
        (f"{info_directory}/WHEEL", wheel_metadata(tag).encode("utf-8"), False),
        (f"{info_directory}/licenses/LICENSE", read(license), False),
        (f"{info_directory}/licenses/NOTICE", read(notice), False),
        (f"{info_directory}/licenses/THIRD-PARTY-LICENSES.txt", read(licenses), False),
    ]
    record = "".join(f"{record_line(path, data)}\n" for path, data, _ in entries) + f"{info_directory}/RECORD,,\n"
    entries.append((f"{info_directory}/RECORD", record.encode("utf-8"), False))

    path = os.path.join(out, f"calendrum-{version}-{tag}.whl")
    temporary = f"{path}.tmp"
    with zipfile.ZipFile(temporary, "w") as archive:
        for name, data, executable in entries:
            add(archive, name, data, executable)
    os.replace(temporary, path)
    return path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--version", required=True)
    parser.add_argument("--license", required=True)
    parser.add_argument("--notice", required=True)
    parser.add_argument("--out", required=True)
    parser.add_argument("--target", nargs=3, action="append", required=True, metavar=("NAME", "LIBRARY", "LICENSES"))
    arguments = parser.parse_args()

    if not re.fullmatch(r"[0-9]+(\.[0-9]+)*", arguments.version):
        raise SystemExit(f"The version {arguments.version} is not one this script expects")
    with open(os.path.join(SOURCE, "_version.py"), encoding="utf-8") as file:
        if f'__version__ = "{arguments.version}"' not in file.read():
            raise SystemExit("calendrum/_version.py has another version: run generate.py")

    os.makedirs(arguments.out, exist_ok=True)
    for name, library, licenses in arguments.target:
        if name not in TAGS:
            raise SystemExit(f"There is no such target: {name}")
        print(build(name, library, licenses, arguments.license, arguments.notice, arguments.version, arguments.out))


if __name__ == "__main__":
    sys.exit(main())
