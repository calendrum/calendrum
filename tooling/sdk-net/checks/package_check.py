#!/usr/bin/env python3
"""Checks the package. It is read, installed into a clean project from a local folder, and the blocks of the guide run with it.

Usage: python3 -I -B package_check.py <directory of the package> <guide> <work directory> <dotnet>

Nothing comes from the network: the only source of the clean project is the folder of the package.
"""
import glob
import os
import re
import shutil
import signal
import struct
import subprocess
import sys
import unittest
import xml.etree.ElementTree as ElementTree
import zipfile

DIRECTORY, GUIDE, WORK, DOTNET = sys.argv[1:5]
del sys.argv[1:5]

TARGETS = {
    "runtimes/osx-arm64/native/libcalendrum.dylib": "mach-o arm64",
    "runtimes/linux-x64/native/libcalendrum.so": "elf x86-64",
    "runtimes/linux-arm64/native/libcalendrum.so": "elf aarch64",
    "runtimes/win-x64/native/libcalendrum.dll": "pe x86-64",
}
ICS = ("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n"
       "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Plain\r\n"
       "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n")
BLOCK = re.compile(r"^```([a-z]*)\n(.*?)^```$", re.MULTILINE | re.DOTALL)


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


def package_path():
    found = glob.glob(os.path.join(DIRECTORY, "Calendrum.*.nupkg"))
    if len(found) != 1:
        raise AssertionError(f"Not one package: {found}")
    return found[0]


def environment():
    root = os.path.dirname(DOTNET)
    variables = dict(os.environ, DOTNET_CLI_TELEMETRY_OPTOUT="1", DOTNET_NOLOGO="1", DOTNET_ROOT=root, DOTNET_CLI_UI_LANGUAGE="en")
    # A cache of its own: a package that an earlier run left behind must not pass for the one here.
    variables["NUGET_PACKAGES"] = os.path.join(WORK, "packages")
    variables["MSBUILDDISABLENODEREUSE"] = "1"
    variables["UseSharedCompilation"] = "false"
    return variables


def run(arguments, directory, limit=300):
    process = subprocess.Popen(arguments, cwd=directory, env=environment(), stdin=subprocess.DEVNULL, stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT, text=True, start_new_session=True)
    try:
        output, _ = process.communicate(timeout=limit)
    except subprocess.TimeoutExpired:
        os.killpg(process.pid, signal.SIGKILL)
        output, _ = process.communicate()
        raise AssertionError(f"{arguments} did not finish in {limit} s:\n{output}")
    return process.returncode, output


class ContentsTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.zip = zipfile.ZipFile(package_path())
        cls.names = set(cls.zip.namelist())
        cls.spec = ElementTree.fromstring(cls.zip.read("Calendrum.nuspec"))

    def field(self, name):
        for element in self.spec.iter():
            if element.tag.endswith("}" + name) or element.tag == name:
                return element
        return None

    def test_the_libraries_are_for_their_targets(self):
        for name, kind in TARGETS.items():
            self.assertEqual(kind, file_kind(self.zip.read(name)), name)

    def test_the_paths_of_this_machine_are_not_in_the_libraries(self):
        for name in TARGETS:
            data = self.zip.read(name)
            for fragment in (os.path.expanduser("~"), "StudioProjects"):
                self.assertNotIn(fragment.encode("utf-8"), data, name)

    def test_what_is_inside(self):
        for name in ("lib/net8.0/Calendrum.dll", "lib/net8.0/Calendrum.xml", "LICENSE", "NOTICE", "README.md"):
            self.assertIn(name, self.names)
        for rid in ("osx-arm64", "linux-x64", "linux-arm64", "win-x64"):
            self.assertIn(f"licenses/{rid}/THIRD-PARTY-LICENSES.txt", self.names)
        expected_top = {"lib", "runtimes", "licenses", "LICENSE", "NOTICE", "README.md", "Calendrum.nuspec", "[Content_Types].xml", "_rels",
                        "package"}
        self.assertEqual(expected_top, {name.split("/")[0] for name in self.names})
        self.assertEqual({"lib/net8.0/Calendrum.dll", "lib/net8.0/Calendrum.xml"}, {name for name in self.names if name.startswith("lib/")})

    def test_no_dependencies(self):
        for element in self.spec.iter():
            if element.tag.endswith("}dependency") or element.tag == "dependency":
                self.fail(f"A dependency: {element.attrib}")

    def test_the_license_is_named(self):
        license = self.field("license")
        self.assertIsNotNone(license)
        self.assertEqual(("expression", "Apache-2.0"), (license.get("type"), license.text))
        self.assertIn(b"Apache License", self.zip.read("LICENSE"))

    def test_the_paths_of_this_machine_are_not_in_the_assembly(self):
        data = self.zip.read("lib/net8.0/Calendrum.dll")
        for fragment in (os.path.expanduser("~"), "StudioProjects"):
            self.assertNotIn(fragment.encode("utf-8"), data)
            self.assertNotIn(fragment.encode("utf-16-le"), data)

    def test_the_comments_of_the_public_api_are_there(self):
        text = self.zip.read("lib/net8.0/Calendrum.xml").decode("utf-8")
        for member in ("M:Calendrum.ICal.Expand", "M:Calendrum.ICal.Call", "T:Calendrum.CalendrumException", "P:Calendrum.Occurrence.Summary"):
            self.assertIn(member, text)


def consumer(name, version, code, extra_source=None, properties=""):
    """A clean project with the package from its folder and nothing else, unless a check needs a pack of the SDK."""
    directory = os.path.join(WORK, name)
    shutil.rmtree(directory, ignore_errors=True)
    os.makedirs(directory)
    with open(os.path.join(directory, "nuget.config"), "w", encoding="utf-8") as file:
        file.write('<?xml version="1.0" encoding="utf-8"?>\n<configuration><packageSources><clear />'
                   f'<add key="local" value="{os.path.abspath(DIRECTORY)}" />{extra_source or ""}</packageSources></configuration>\n')
    with open(os.path.join(directory, "Consumer.csproj"), "w", encoding="utf-8") as file:
        file.write('<Project Sdk="Microsoft.NET.Sdk"><PropertyGroup><OutputType>Exe</OutputType><TargetFramework>net10.0</TargetFramework>'
                   f"<Nullable>enable</Nullable><ImplicitUsings>disable</ImplicitUsings><InvariantGlobalization>true</InvariantGlobalization>{properties}"
                   f'</PropertyGroup><ItemGroup><PackageReference Include="Calendrum" Version="{version}" /></ItemGroup></Project>\n')
    with open(os.path.join(directory, "Program.cs"), "w", encoding="utf-8") as file:
        file.write(code)
    with open(os.path.join(directory, "event.ics"), "w", encoding="utf-8", newline="") as file:
        file.write(ICS)
    return directory


def version_of():
    return re.search(r"Calendrum\.(.+)\.nupkg$", package_path()).group(1)


class InstalledTest(unittest.TestCase):
    def test_a_clean_project_finds_the_library(self):
        directory = consumer("clean", version_of(), 'using System;\nusing Calendrum;\n'
                             'Console.WriteLine(ICal.Explain("ICAL-S003").Code);\nConsole.WriteLine(ICal.GetVersions().Package);\n')
        code, output = run([DOTNET, "run", "-c", "Release"], directory)
        self.assertEqual(0, code, output)
        self.assertIn("ICAL-S003", output)
        self.assertIn(version_of(), output)
        self.assertEqual([], glob.glob(os.path.join(directory, "*.dylib")), "it comes from the package, not from here")

    def test_a_published_program_carries_the_library(self):
        directory = consumer("published", version_of(), 'using System;\nusing Calendrum;\nConsole.WriteLine(ICal.Explain("ICAL-S003").Code);\n')
        out = os.path.join(directory, "out")
        code, output = run([DOTNET, "publish", "-c", "Release", "-r", "osx-arm64", "--self-contained", "false", "-o", out], directory)
        self.assertEqual(0, code, output)
        self.assertTrue(os.path.exists(os.path.join(out, "libcalendrum.dylib")), os.listdir(out))
        self.assertNotIn("libcalendrum.dll", os.listdir(out))
        self.assertFalse(os.path.exists(os.path.join(out, "libcalendrum.so")))
        code, output = run([os.path.join(out, "Consumer")], directory)
        self.assertEqual(0, code, output)
        self.assertIn("ICAL-S003", output)


PROBE = """using System;
using System.Text.Json.Nodes;
using Calendrum;

const string Ics = "%s";
Console.WriteLine(ICal.Inspect(Ics).Calendars[0].Events[0].Summary);
Console.WriteLine(ICal.Expand(Ics, "2026-01-01T00:00:00Z", "2026-02-01T00:00:00Z", new ExpandOptions { Zone = "Europe/Berlin", Limit = 2 }).Truncated);
Console.WriteLine(ICal.BuildEvent("x", "2026-01-05", new BuildEventOptions { AllDay = true, Attendees = [new Attendee { Address = "a@example.com" }] }).Uid.Length > 0);
Console.WriteLine(ICal.ConvertFromJcal(ICal.ConvertToJcal(Ics).Jcal!.Value).Ics!.Length > 0);
Console.WriteLine(ICal.Call("explain", new JsonObject { ["code"] = "ICAL-S003", ["list"] = new JsonArray(1, "a", true) }).Result["code"]);
try { ICal.TzResolve("Nowhere/Nothing"); } catch (UnknownZoneException error) { Console.WriteLine(error.Code); }
Console.WriteLine(ICal.GetVersions().Package);
""" % ICS.replace("\r\n", "\\r\\n")


@unittest.skipUnless(os.environ.get("CALENDRUM_CHECK_PUBLISHING") == "1", "Asks nuget.org for the packs of the SDK, of about 340 MB: only on request")
class PublishingTest(unittest.TestCase):
    NUGET = '<add key="nuget.org" value="https://api.nuget.org/v3/index.json" />'

    def publish(self, name, *options):
        directory = consumer(name, version_of(), PROBE, self.NUGET)
        out = os.path.join(directory, "out")
        code, output = run([DOTNET, "publish", "-c", "Release", "-r", "osx-arm64", "-o", out, *options], directory, limit=500)
        self.assertEqual(0, code, output)
        self.assertNotIn(": warning ", output, "a program that treats warnings as errors could not use the package")
        # From another folder, without the SDK in the environment: a published program stands alone.
        variables = {key: value for key, value in environment().items() if key != "DOTNET_ROOT"}
        answer = subprocess.run([os.path.join(out, "Consumer")], cwd="/", env=variables, stdin=subprocess.DEVNULL, capture_output=True, text=True, timeout=60)
        self.assertEqual(0, answer.returncode, answer.stdout + answer.stderr)
        self.assertIn("OPS-UNKNOWN-ZONE", answer.stdout)
        self.assertIn(version_of(), answer.stdout)
        return out

    def test_native_aot(self):
        out = self.publish("aot", "-p:PublishAot=true")
        self.assertIn("libcalendrum.dylib", os.listdir(out))

    def test_a_single_file_with_the_library_next_to_it(self):
        out = self.publish("single", "--self-contained", "-p:PublishSingleFile=true")
        self.assertIn("libcalendrum.dylib", os.listdir(out))

    def test_a_single_file_with_the_library_inside(self):
        out = self.publish("inside", "--self-contained", "-p:PublishSingleFile=true", "-p:IncludeNativeLibrariesForSelfExtract=true")
        self.assertNotIn("libcalendrum.dylib", os.listdir(out))


class GuideTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        with open(GUIDE, encoding="utf-8") as file:
            cls.text = file.read()
        cls.blocks = BLOCK.findall(cls.text)

    def test_every_block_compiles_and_runs(self):
        blocks = [code for language, code in self.blocks if language == "csharp"]
        self.assertGreaterEqual(len(blocks), 7)
        usings = set()
        methods = []
        for index, code in enumerate(blocks):
            body = []
            for line in code.splitlines():
                if re.fullmatch(r"using [A-Za-z.]+;", line):
                    usings.add(line)
                else:
                    body.append(line)
            methods.append(f"static async Task Block{index}()\n{{\n" + "\n".join(body) + "\n}\n")
        calls = "".join(f'Console.WriteLine("-- block {index}");\nawait Block{index}();\n' for index in range(len(blocks)))
        program = "\n".join(sorted(usings | {"using System;", "using System.Threading.Tasks;"})) + "\n\n#pragma warning disable CS1998\n" + calls + "\n" + "\n".join(methods)
        directory = consumer("guide", version_of(), program)
        code, output = run([DOTNET, "run", "-c", "Release"], directory)
        self.assertEqual(0, code, f"{program}\n{output}")
        self.assertIn("OPS-UNKNOWN-ZONE", output)
        self.assertIn("True 1", output, "the example of a truncated result is truncated")

    def test_the_languages_of_the_blocks(self):
        self.assertEqual({"bash", "csharp"}, {language for language, _ in self.blocks})

    def test_it_follows_the_rules_of_a_public_guide(self):
        self.assertNotRegex(self.text, r"docs/|D-\d+|P-\d+")
        self.assertEqual(set(), {host for host in re.findall(r"https?://([^/\s)]+)", self.text) if not host.endswith("example.com")})

    def test_it_names_all_it_has_to(self):
        for name in ("Inspect", "Validate", "Fix", "Expand", "RruleExpand", "Freebusy", "BuildEvent", "Diff", "ConvertToJcal", "ConvertFromJcal",
                     "Anonymize", "TzResolve", "Explain", "Call", "GetVersions", "EnsureComplete", "TruncatedResultException", "LibraryException",
                     "CalendarInput.FromFile", "SIGABRT", "Only the macOS", "Task.Run", "GCHeapHardLimit", "Native AOT"):
            self.assertIn(name, self.text)


if __name__ == "__main__":
    # The version stays the same between builds, so a cache left by an earlier run would hand out the package of that run.
    shutil.rmtree(WORK, ignore_errors=True)
    os.makedirs(WORK)
    unittest.main()
