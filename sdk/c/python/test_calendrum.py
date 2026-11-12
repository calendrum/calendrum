import threading
import unittest
from pathlib import Path

from calendrum import ICAL_INVALID_ARGUMENT, Calendrum, CalendrumError

SAMPLE = (Path(__file__).resolve().parents[3] / "examples" / "shared" / "sample.ics").read_bytes()


class CalendrumTest(unittest.TestCase):
    lib = Calendrum()

    def test_version(self):
        self.assertEqual(1, self.lib.abi_version())
        version = self.lib.version()
        self.assertEqual(1, version["abi"])
        self.assertTrue(version["library"])

    def test_parse(self):
        jcal, problems = self.lib.parse(SAMPLE)
        self.assertEqual(1, len(jcal))
        self.assertEqual("vcalendar", jcal[0][0])
        events = [component for component in jcal[0][2] if component[0] == "vevent"]
        self.assertEqual(2, len(events))
        self.assertEqual([], problems)

    def test_problems(self):
        _, problems = self.lib.parse(b"BEGIN:VCALENDAR\r\nnot a line\r\nEND:VCALENDAR\r\n")
        self.assertTrue(problems)
        self.assertTrue(problems[0]["code"].startswith("ICAL-"))

    def test_occurrences(self):
        found = self.lib.occurrences(SAMPLE, "2026-09-01T00:00:00Z", "2026-10-01T00:00:00Z", "Europe/Berlin")
        self.assertEqual(4, len(found))
        self.assertEqual("2026-09-07T08:00:00Z", found[0]["start"])
        self.assertEqual("2026-09-07T10:00", found[0]["localStart"])
        self.assertEqual("Distributed systems seminar (moved)", found[1]["summary"])
        self.assertNotIn("localStart", self.lib.occurrences(SAMPLE, "2026-09-01T00:00:00Z", "2026-09-08T00:00:00Z")[0])

    def test_invalid_argument(self):
        with self.assertRaises(CalendrumError) as raised:
            self.lib.occurrences(SAMPLE, "yesterday", "2026-10-01T00:00:00Z")
        self.assertEqual(ICAL_INVALID_ARGUMENT, raised.exception.code)
        with self.assertRaises(CalendrumError):
            self.lib.occurrences(SAMPLE, "2026-09-01T00:00:00Z", "2026-10-01T00:00:00Z", "Mars/Olympus")

    def test_empty_input(self):
        jcal, _ = self.lib.parse(b"")
        self.assertEqual([], jcal)

    def test_threads(self):
        expected = self.lib.occurrences(SAMPLE, "2026-09-01T00:00:00Z", "2026-12-01T00:00:00Z", "Europe/Berlin")
        expected_jcal, _ = self.lib.parse(SAMPLE)
        failures = []

        def work():
            try:
                for _ in range(50):
                    if self.lib.parse(SAMPLE)[0] != expected_jcal:
                        failures.append("parse")
                    found = self.lib.occurrences(
                        SAMPLE, "2026-09-01T00:00:00Z", "2026-12-01T00:00:00Z", "Europe/Berlin",
                    )
                    if found != expected:
                        failures.append("occurrences")
            except Exception as error:
                failures.append(repr(error))

        threads = [threading.Thread(target=work) for _ in range(8)]
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()
        self.assertEqual([], failures)


if __name__ == "__main__":
    unittest.main()
