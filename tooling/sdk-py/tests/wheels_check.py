"""Checks the wheels. The one for this host is installed into a clean venv and called; the rest are read, and pip is asked about them.

Usage: python3 -I -B wheels_check.py <directory of the wheels> [<python>...]

Nothing comes from the network: pip runs with --no-index. The arguments of the build come from the environment, as
CALENDRUM_WHEEL_ARGUMENTS with one JSON list, to build again and compare the bytes.
"""
import base64
import csv
import glob
import hashlib
import io
import json
import os
import re
import shutil
import stat
import struct
import subprocess
import sys
import tempfile
import unittest
import zipfile

DIRECTORY = sys.argv.pop(1)
PYTHONS = sys.argv[1:]
del sys.argv[1:]
HERE = os.path.dirname(os.path.abspath(__file__))
BUILD = os.path.join(HERE, "..", "build_wheels.py")

TARGETS = {
    "macosx_12_0_arm64": ("libcalendrum.dylib", "mach-o arm64"),
    "manylinux_2_17_x86_64": ("libcalendrum.so", "elf x86-64"),
    "manylinux_2_17_aarch64": ("libcalendrum.so", "elf aarch64"),
    "win_amd64": ("calendrum.dll", "pe x86-64"),
}
EXPECTED_MODULES = {"__init__.py", "_errors.py", "_models.py", "_native.py", "_operations.py", "_version.py", "py.typed"}


def file_kind(data):
    """What a native file is for, from its header: a mix-up of two targets would otherwise pass everything else here."""
    if data[:4] == b"\xcf\xfa\xed\xfe":
        cpu = struct.unpack("<I", data[4:8])[0]
        return "mach-o arm64" if cpu == 0x0100000C else f"mach-o {cpu:#x}"
    if data[:4] == b"\x7fELF":
        machine = struct.unpack("<H", data[18:20])[0]
        return {0x3E: "elf x86-64", 0xB7: "elf aarch64"}.get(machine, f"elf {machine:#x}")
    if data[:2] == b"MZ":
        offset = struct.unpack("<I", data[60:64])[0]
        machine = struct.unpack("<H", data[offset + 4:offset + 6])[0]
        return {0x8664: "pe x86-64"}.get(machine, f"pe {machine:#x}")
    return "unknown"


def wheel_for(platform):
    found = glob.glob(os.path.join(DIRECTORY, f"calendrum-*-py3-none-{platform}.whl"))
    if len(found) != 1:
        raise AssertionError(f"Not one wheel for {platform}: {found}")
    return found[0]


def run(command, limit=300, cwd=None):
    return subprocess.run(command, capture_output=True, text=True, timeout=limit, cwd=cwd, stdin=subprocess.DEVNULL, check=False)


class ArchiveTest(unittest.TestCase):
    def test_there_are_four_wheels(self):
        self.assertEqual(4, len(glob.glob(os.path.join(DIRECTORY, "*.whl"))))

    def test_every_wheel_has_what_it_should(self):
        for platform, (library, kind) in TARGETS.items():
            with self.subTest(platform=platform):
                with zipfile.ZipFile(wheel_for(platform)) as archive:
                    self.assertIsNone(archive.testzip())
                    names = set(archive.namelist())
                    version = re.search(r"calendrum-([0-9.]+)-", os.path.basename(wheel_for(platform))).group(1)
                    info = f"calendrum-{version}.dist-info"
                    self.assertEqual(
                        {f"calendrum/{name}" for name in EXPECTED_MODULES} | {f"calendrum/{library}"}
                        | {f"{info}/{name}" for name in ("METADATA", "WHEEL", "RECORD", "licenses/LICENSE", "licenses/NOTICE",
                                                         "licenses/THIRD-PARTY-LICENSES.txt")},
                        names,
                    )
                    self.assertEqual(kind, file_kind(archive.read(f"calendrum/{library}")))
                    mode = archive.getinfo(f"calendrum/{library}").external_attr >> 16
                    self.assertTrue(stat.S_ISREG(mode) and mode & 0o100, oct(mode))

                    wheel = archive.read(f"{info}/WHEEL").decode()
                    self.assertIn(f"Tag: py3-none-{platform}\n", wheel)
                    self.assertIn("Root-Is-Purelib: false\n", wheel)
                    self.assertIn("Wheel-Version: 1.0\n", wheel)

                    metadata = archive.read(f"{info}/METADATA").decode()
                    self.assertIn("Name: calendrum\n", metadata)
                    self.assertIn(f"Version: {version}\n", metadata)
                    self.assertIn("Requires-Python: >=3.9\n", metadata)
                    self.assertIn("Classifier: Private :: Do Not Upload\n", metadata)
                    self.assertIn("License-Expression: Apache-2.0\n", metadata)
                    self.assertNotRegex(metadata, r"(?m)^(License:|Classifier: License ::)", "only the expression names the license")
                    self.assertNotRegex(metadata, r"(?m)^Requires-Dist:", "no dependencies")
                    self.assertIn("License-File: LICENSE\n", metadata)
                    self.assertIn("License-File: NOTICE\n", metadata)
                    self.assertIn(b"Apache License", archive.read(f"{info}/licenses/LICENSE"))
                    self.assertTrue(archive.read(f"{info}/licenses/NOTICE"))
                    self.assertTrue(archive.read(f"{info}/licenses/THIRD-PARTY-LICENSES.txt"))

    def test_the_paths_of_this_machine_are_not_in_the_libraries(self):
        for platform, (name, _) in TARGETS.items():
            with zipfile.ZipFile(wheel_for(platform)) as archive:
                data = archive.read(f"calendrum/{name}")
            for fragment in (os.path.expanduser("~"), "StudioProjects"):
                self.assertNotIn(fragment.encode("utf-8"), data, platform)

    def test_record_holds_every_file_with_its_hash(self):
        for platform in TARGETS:
            with self.subTest(platform=platform), zipfile.ZipFile(wheel_for(platform)) as archive:
                info = next(name for name in archive.namelist() if name.endswith(".dist-info/RECORD"))
                rows = list(csv.reader(io.StringIO(archive.read(info).decode())))
                recorded = {row[0]: row for row in rows}
                self.assertEqual(set(archive.namelist()), set(recorded))
                self.assertEqual(len(rows), len(recorded))
                for name, (_, digest, size) in recorded.items():
                    if name == info:
                        self.assertEqual(("", ""), (digest, size))
                        continue
                    data = archive.read(name)
                    wanted = base64.urlsafe_b64encode(hashlib.sha256(data).digest()).rstrip(b"=").decode()
                    self.assertEqual(f"sha256={wanted}", digest, name)
                    self.assertEqual(str(len(data)), size, name)

    def test_times_are_fixed(self):
        for platform in TARGETS:
            with zipfile.ZipFile(wheel_for(platform)) as archive:
                self.assertEqual({(1980, 1, 1, 0, 0, 0)}, {info.date_time for info in archive.infolist()})

    def test_a_rebuild_gives_the_same_bytes(self):
        arguments = json.loads(os.environ["CALENDRUM_WHEEL_ARGUMENTS"])
        with tempfile.TemporaryDirectory() as directory:
            done = run([sys.executable, "-I", "-B", BUILD, *arguments, "--out", directory])
            self.assertEqual(0, done.returncode, done.stderr)
            for path in glob.glob(os.path.join(directory, "*.whl")):
                with open(path, "rb") as new, open(os.path.join(DIRECTORY, os.path.basename(path)), "rb") as old:
                    self.assertEqual(hashlib.sha256(old.read()).digest(), hashlib.sha256(new.read()).digest(), path)


class PipTest(unittest.TestCase):
    """pip reads the tags, so this is its word on each wheel, on this host, without running anything from it."""

    @classmethod
    def setUpClass(cls):
        cls.directory = tempfile.mkdtemp()
        cls.addClassCleanup(shutil.rmtree, cls.directory)
        cls.venv = os.path.join(cls.directory, "venv")
        done = run([sys.executable, "-I", "-m", "venv", cls.venv], limit=180)
        if done.returncode != 0:
            raise AssertionError(done.stdout + done.stderr)
        cls.pip = [os.path.join(cls.venv, "bin", "python"), "-m", "pip", "--disable-pip-version-check", "--no-input"]

    def resolve(self, platform):
        target = tempfile.mkdtemp(dir=self.directory)
        return run([*self.pip, "install", "--no-index", "--find-links", DIRECTORY, "--dry-run", "--only-binary=:all:", "--platform", platform,
                    "--python-version", "3.9", "--target", target, "calendrum"])

    def test_pip_agrees_with_the_tag_of_each_wheel(self):
        for platform in TARGETS:
            with self.subTest(platform=platform):
                done = self.resolve(platform)
                self.assertEqual(0, done.returncode, done.stdout + done.stderr)
                self.assertIn("calendrum", done.stdout)

    def test_pip_refuses_what_a_wheel_is_not_for(self):
        for platform in ("manylinux_2_12_x86_64", "manylinux_2_16_aarch64", "macosx_11_0_arm64", "macosx_12_0_x86_64", "win32", "musllinux_1_2_x86_64"):
            with self.subTest(platform=platform):
                self.assertNotEqual(0, self.resolve(platform).returncode)


class InstalledTest(unittest.TestCase):
    """The wheel of this host in a clean venv, called from a clean directory, on each Python."""

    PROGRAM = """
import calendrum, os, sys
assert not os.path.exists(os.path.join(os.getcwd(), "calendrum"))
assert "site-packages" in calendrum.__file__, calendrum.__file__
assert "ctypes" not in sys.modules
result = calendrum.explain("ICAL-S003")
assert result.code == "ICAL-S003"
assert calendrum.versions().package == calendrum.__version__
print("installed", sys.version_info[:2], os.path.dirname(calendrum.__file__))
"""

    def test_install_and_call(self):
        self.assertTrue(PYTHONS, "No Python to install into")
        wheel = wheel_for("macosx_12_0_arm64")
        for python in PYTHONS:
            with self.subTest(python=python), tempfile.TemporaryDirectory() as directory:
                # A path with a space and letters outside ASCII: the library loads by its absolute path.
                venv = os.path.join(directory, "каталог с пробелом")
                done = run([python, "-I", "-m", "venv", venv], limit=180)
                self.assertEqual(0, done.returncode, done.stdout + done.stderr)
                inside = os.path.join(venv, "bin", "python")
                done = run([inside, "-m", "pip", "--disable-pip-version-check", "--no-input", "install", "--no-index", wheel])
                self.assertEqual(0, done.returncode, done.stdout + done.stderr)
                clean = os.path.join(directory, "clean")
                os.mkdir(clean)
                done = run([inside, "-I", "-c", self.PROGRAM], limit=60, cwd=clean)
                self.assertEqual(0, done.returncode, done.stdout + done.stderr)
                print(f"\n{done.stdout.strip()}")
                site = glob.glob(os.path.join(venv, "lib", "python*", "site-packages", "calendrum", "libcalendrum.dylib"))
                self.assertEqual(1, len(site))
                self.assertTrue(os.stat(site[0]).st_mode & 0o100, "the library is executable")


if __name__ == "__main__":
    unittest.main(verbosity=1)
