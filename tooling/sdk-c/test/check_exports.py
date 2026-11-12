"""What each library exports is the list in link/symbols.txt, on every target the tools of the host can read: Apple's nm and objdump
are LLVM and understand ELF and PE as well. So is every list that the linkers are given.

Usage: python3 -I check_exports.py <link directory> <macosArm64 dylib> <linuxX64 so> <linuxArm64 so> <mingwX64 dll>
"""
import re
import subprocess
import sys


def output(*command):
    return subprocess.run(command, check=True, capture_output=True, text=True).stdout


def macho(path):
    return sorted(line.split()[-1].removeprefix("_") for line in output("nm", "-gU", path).splitlines())


def elf(path):
    return sorted(line.split()[-1] for line in output("nm", "-D", "--defined-only", path).splitlines())


def pe(path):
    text = output("objdump", "-p", path)
    table = text[text.index("Export Table:"):]
    return sorted(re.findall(r"^\s+\d+\s+0x[0-9a-f]+\s+(\S+)$", table, re.MULTILINE))


def main():
    directory, dylib, linux_x64, linux_arm64, dll = sys.argv[1:]
    with open(f"{directory}/symbols.txt", encoding="utf-8") as file:
        expected = sorted(file.read().split())
    failed = 0

    def compare(name, actual):
        nonlocal failed
        same = actual == expected
        failed += not same
        print(f"{name}: {'the same' if same else f'DIFFERENT, {actual}'} ({len(actual)} symbols)")

    with open(f"{directory}/calendrum.exp", encoding="utf-8") as file:
        compare("calendrum.exp", sorted(name.removeprefix("_") for name in file.read().split()))
    with open(f"{directory}/calendrum.ver", encoding="utf-8") as file:
        compare("calendrum.ver", sorted(re.findall(r"^\s*(calendrum_\w+);", file.read(), re.MULTILINE)))
    with open(f"{directory}/calendrum.def", encoding="utf-8") as file:
        compare("calendrum.def", sorted(re.findall(r"^\s+(calendrum_\w+)\s*$", file.read(), re.MULTILINE)))
    compare("macosArm64, nm -gU", macho(dylib))
    compare("linuxX64, nm -D --defined-only", elf(linux_x64))
    compare("linuxArm64, nm -D --defined-only", elf(linux_arm64))
    compare("mingwX64, objdump -p", pe(dll))
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
