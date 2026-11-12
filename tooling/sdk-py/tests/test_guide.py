"""The guide: every Python block runs as written, and it follows the rules of a public guide."""
import os
import re
import signal
import subprocess
import sys
import tempfile
import unittest

import support

GUIDE = os.environ["CALENDRUM_GUIDE"]
BLOCK = re.compile(r"^```([a-z]*)\n(.*?)^```$", re.MULTILINE | re.DOTALL)


class GuideTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        with open(GUIDE, encoding="utf-8") as file:
            cls.text = file.read()
        cls.blocks = BLOCK.findall(cls.text)

    def run_block(self, code, directory):
        script = os.path.join(directory, "example.py")
        with open(script, "w", encoding="utf-8") as file:
            # On one line, so that a spawned child, which imports this file again, finds the package as well.
            file.write(f"import sys; sys.path.insert(0, {support.PACKAGE!r})\n{code}")
        process = subprocess.Popen([sys.executable, "-I", "-B", script], cwd=directory, stdin=subprocess.DEVNULL, stdout=subprocess.PIPE,
                                   stderr=subprocess.STDOUT, text=True, start_new_session=True)
        try:
            output, _ = process.communicate(timeout=120)
        except subprocess.TimeoutExpired:
            os.killpg(process.pid, signal.SIGKILL)
            output, _ = process.communicate()
            self.fail(f"A block did not finish:\n{code}\n{output}")
        self.assertEqual(0, process.returncode, f"{code}\n{output}")
        return output

    def test_every_python_block_runs(self):
        blocks = [code for language, code in self.blocks if language == "python"]
        self.assertGreaterEqual(len(blocks), 7)
        outputs = []
        for code in blocks:
            with self.subTest(block=code.splitlines()[-1]), tempfile.TemporaryDirectory() as directory:
                with open(os.path.join(directory, "event.ics"), "wb") as file:
                    file.write(support.ICS.encode("utf-8"))
                outputs.append(self.run_block(code, directory))
        joined = "\n".join(outputs)
        self.assertIn("OPS-UNKNOWN-ZONE", joined)
        self.assertIn("True 1", joined, "the example of a truncated result is truncated")
        self.assertIn("[1]", joined, "the example with spawn gets its answer")

    def test_the_languages_of_the_blocks(self):
        self.assertEqual({"bash", "python"}, {language for language, _ in self.blocks})

    def test_it_follows_the_rules_of_a_public_guide(self):
        self.assertNotRegex(self.text, r"docs/|D-\d+|P-\d+")
        self.assertEqual(set(), {host for host in re.findall(r"https?://([^/\s)]+)", self.text) if not host.endswith("example.com")})

    def test_it_names_all_it_has_to(self):
        for name in (*support.OPERATIONS, "call", "versions", "TruncatedWarning", "ForkedProcessError", "LibraryError", "SIGABRT",
                     "KeyboardInterrupt", "spawn", "forkserver", "--preload", "Only the macOS wheel"):
            self.assertIn(name, self.text)

    def test_the_wheel_in_the_example_has_the_current_version(self):
        with open(os.path.join(support.PACKAGE, "calendrum", "_version.py"), encoding="utf-8") as file:
            version = re.search(r'"(.+)"', file.read()).group(1)
        self.assertIn(f"calendrum-{version}-py3-none-macosx_12_0_arm64.whl", self.text)


class DocstringsTest(unittest.TestCase):
    def test_everything_public_has_a_docstring(self):
        import calendrum

        for name in calendrum.__all__:
            if name != "__version__":
                self.assertTrue(getattr(calendrum, name).__doc__, name)


if __name__ == "__main__":
    unittest.main()
