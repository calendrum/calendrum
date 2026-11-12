"""What a calendar can be on the way in, and that a plain string is never a path."""
import base64
import json
import os
import pathlib
import tempfile
import unittest
import warnings

import support

import calendrum
from calendrum import _native, _operations


class Inside(os.PathLike):
    """A path that is not a pathlib path: what counts is the protocol."""

    def __init__(self, path):
        self.path = path

    def __fspath__(self):
        return self.path


def with_long_line(tail):
    """A calendar whose line is above the limit for files from strangers: 64 KiB. Only the tail differs."""
    return support.ICS.replace("SUMMARY:Plain", "DESCRIPTION:" + "x" * 70000 + tail)


class LimitsTest(unittest.TestCase):
    def test_a_change_beyond_the_limits_is_not_silent(self):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", calendrum.TruncatedWarning)
            result = calendrum.diff(with_long_line("A"), with_long_line("B"))
        self.assertTrue(result.truncated, "no fingerprint sees that line, so the answer cannot be complete")
        self.assertIn("ICAL-S010", [problem.code for problem in result.problems])
        self.assertGreaterEqual(result.problem_count, 1)

    def test_what_anonymize_could_not_read_is_not_silent(self):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", calendrum.TruncatedWarning)
            result = calendrum.anonymize(with_long_line("A"))
        self.assertTrue(result.truncated)
        self.assertIn("ICAL-S010", [problem.code for problem in result.problems])
        self.assertNotIn("x" * 100, result.ics)

    def test_ordinary_calendars_are_not_truncated(self):
        self.assertFalse(calendrum.diff(support.ICS, support.ICS).truncated)
        self.assertEqual(0, calendrum.anonymize(support.ICS).problem_count)

    def test_a_line_that_cannot_be_read_makes_a_calendar_invalid(self):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", calendrum.TruncatedWarning)
            result = calendrum.validate(with_long_line("A"))
        self.assertFalse(result.valid)

    def test_a_number_a_double_cannot_hold_stays_as_text_in_jcal(self):
        for property_line, digits in (("GEO:" + "1" * 5000 + ";2", "1" * 5000), ("X-F;VALUE=FLOAT:" + "2" * 5000, "2" * 5000),
                                      ("X-G;VALUE=FLOAT:12345678901234567890", "12345678901234567890")):
            calendar = support.ICS.replace("SUMMARY:Plain", property_line)
            jcal = calendrum.convert(calendar).jcal
            self.assertIn(digits, json.dumps(jcal), "the digits are all there")
            self.assertIn(digits, calendrum.convert(jcal=jcal).ics.replace("\r\n ", ""), "and come back")
            self.assertIsNotNone(calendrum.inspect(calendar, include_jcal=True).jcal)

    def test_an_ordinary_number_stays_a_number_in_jcal(self):
        jcal = calendrum.convert(support.ICS.replace("SUMMARY:Plain", "GEO:37.386013;-122.082932")).jcal
        self.assertIn("[37.386013, -122.082932]", json.dumps(jcal))


class InputTest(unittest.TestCase):
    def setUp(self):
        directory = tempfile.TemporaryDirectory()
        self.addCleanup(directory.cleanup)
        self.directory = pathlib.Path(directory.name)
        self.file = self.directory / "event.ics"
        self.file.write_bytes(support.ICS.encode("utf-8"))

    def test_str_bytes_and_a_path_read_the_same(self):
        text = calendrum.inspect(support.ICS)
        self.assertEqual(text, calendrum.inspect(support.ICS.encode("utf-8")))
        self.assertEqual(text, calendrum.inspect(self.file))
        self.assertEqual(text, calendrum.inspect(Inside(str(self.file))))
        self.assertEqual(text, calendrum.inspect(Inside(os.fsencode(self.file))))

    def test_each_form_goes_in_its_own_field(self):
        seen = []
        original = _native.call

        def spy(operation, request):
            seen.append(json.loads(request))
            return original(operation, request)

        _native.call = spy
        self.addCleanup(setattr, _native, "call", original)
        calendrum.inspect(support.ICS)
        calendrum.inspect(support.ICS.encode("utf-8"))
        calendrum.inspect(self.file)
        encoded = base64.b64encode(support.ICS.encode("utf-8")).decode("ascii")
        self.assertEqual([{"ics": support.ICS}, {"ics_base64": encoded}, {"ics_base64": encoded}], seen)

    def test_bytes_keep_their_encoding(self):
        cyrillic = support.ICS.replace("Plain", "Лекция").encode("windows-1251")
        self.file.write_bytes(cyrillic)
        for calendar in (cyrillic, self.file):
            result = calendrum.inspect(calendar)
            self.assertEqual("windows-1251", result.charset.lower())
            self.assertEqual("Лекция", result.calendars[0].events[0].summary)

    def test_a_plain_string_is_never_a_path(self):
        for text in (str(self.file), "event.ics", "/etc/hosts"):
            result = calendrum.inspect(text)
            self.assertEqual((), result.calendars, text)

    def test_every_operation_with_a_calendar_takes_a_path(self):
        self.assertTrue(calendrum.validate(self.file).valid)
        self.assertEqual(calendrum.fix(support.ICS).ics, calendrum.fix(self.file).ics)
        self.assertEqual(3, calendrum.expand(self.file, **support.WINDOW).count)
        self.assertEqual(calendrum.freebusy(support.ICS, **support.WINDOW), calendrum.freebusy(self.file, **support.WINDOW))
        self.assertEqual(0, len(calendrum.diff(self.file, support.ICS).changed))
        self.assertEqual(0, len(calendrum.diff(support.ICS, self.file).changed))
        self.assertEqual(calendrum.convert(support.ICS).jcal, calendrum.convert(self.file).jcal)
        self.assertIn("BEGIN:VCALENDAR", calendrum.anonymize(self.file).ics)

    def test_a_path_that_does_not_exist(self):
        with self.assertRaises(FileNotFoundError):
            calendrum.inspect(self.directory / "missing.ics")

    def test_a_directory_is_refused(self):
        with self.assertRaises(calendrum.BadArgumentError):
            calendrum.inspect(self.directory)

    def test_a_device_is_refused_without_being_read(self):
        with self.assertRaises(calendrum.BadArgumentError):
            calendrum.inspect(pathlib.Path("/dev/zero"))

    def test_a_fifo_does_not_wait_for_a_writer(self):
        code, output = support.run_scenario("fifo", str(self.directory), limit=30)
        self.assertEqual(0, code, output)

    def test_a_file_above_the_limit_is_refused_without_being_read(self):
        large = self.directory / "large.ics"
        with open(large, "wb") as file:
            file.truncate(64 * 1024 * 1024 + 1)
        with self.assertRaises(calendrum.TooLargeError) as caught:
            calendrum.inspect(large)
        self.assertEqual("OPS-TOO-LARGE", caught.exception.code)

    def test_bytes_above_the_limit_are_refused_before_any_copy(self):
        with self.assertRaises(calendrum.TooLargeError):
            calendrum.inspect(bytes(64 * 1024 * 1024 + 1))

    def test_a_file_at_the_limit_is_read(self):
        at_limit = self.directory / "limit.ics"
        with open(at_limit, "wb") as file:
            file.truncate(64 * 1024 * 1024)
        self.assertEqual(64 * 1024 * 1024, len(_operations._read_file(at_limit)))

    def test_a_file_that_grows_past_the_limit_is_refused(self):
        grown = self.directory / "grown.ics"
        grown.write_bytes(b"x")
        original = os.fstat

        def small_at_first(descriptor):
            with open(grown, "r+b") as file:
                file.truncate(64 * 1024 * 1024 + 5)
            return original(descriptor)

        os.fstat = small_at_first
        self.addCleanup(setattr, os, "fstat", original)
        with self.assertRaises(calendrum.TooLargeError):
            _operations._read_file(grown)

    def test_other_types_are_refused(self):
        for value in (None, 5, ["BEGIN:VCALENDAR"], bytearray(b"x")):
            with self.subTest(value=type(value).__name__):
                with self.assertRaises(TypeError):
                    calendrum.inspect(value)

    def test_arguments_after_the_calendar_are_named(self):
        with self.assertRaises(TypeError):
            calendrum.inspect(support.ICS, True)
        with self.assertRaises(TypeError):
            calendrum.expand(support.ICS, "2026-01-01T00:00:00Z", "2026-02-01T00:00:00Z")

    def test_the_file_descriptor_is_closed(self):
        before = len(os.listdir("/dev/fd"))
        for path in (self.file, self.directory, self.directory / "missing.ics"):
            for _ in range(50):
                try:
                    calendrum.inspect(path)
                except (OSError, calendrum.CalendrumError):
                    pass
        self.assertEqual(before, len(os.listdir("/dev/fd")))


if __name__ == "__main__":
    unittest.main()
