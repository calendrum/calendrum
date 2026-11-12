"""The errors: each code has its class, none of them carries the input, and they pass between processes."""
import json
import pickle
import traceback
import unittest
import warnings

import support

import calendrum
from calendrum import _errors, _native, _operations

MARKER = "ZZMARKZZ"


def carried_text(error):
    """Everything an exception shows or holds, along its whole chain."""
    parts = ["".join(traceback.format_exception(type(error), error, error.__traceback__))]
    seen = error
    while seen is not None:
        parts += [str(seen), repr(seen), repr(seen.args), repr(vars(seen)), repr(getattr(seen, "__notes__", ""))]
        seen = seen.__cause__ or seen.__context__
    return "\n".join(parts)


class ErrorsTest(unittest.TestCase):
    def test_every_code_has_its_class(self):
        for code, wanted in (
            ("OPS-BAD-ARGUMENT", calendrum.BadArgumentError),
            ("OPS-UNKNOWN-OPERATION", calendrum.UnknownOperationError),
            ("OPS-UNKNOWN-ZONE", calendrum.UnknownZoneError),
            ("OPS-UNKNOWN-CODE", calendrum.UnknownCodeError),
            ("OPS-TOO-LARGE", calendrum.TooLargeError),
            ("OPS-INTERNAL", calendrum.InternalError),
        ):
            error = _errors.error_for(code, "text")
            self.assertIs(wanted, type(error))
            self.assertIsInstance(error, calendrum.CalendrumError)
            self.assertEqual((code, "text"), (error.code, error.message))
            self.assertEqual(f"{code}: text", str(error))

    def test_a_code_this_version_does_not_know_gets_the_base_class(self):
        self.assertIs(calendrum.CalendrumError, type(_errors.error_for("OPS-NEW", "text")))

    def test_the_real_library_gives_these_classes(self):
        for code, call in (
            (calendrum.BadArgumentError, lambda: calendrum.expand(support.ICS, from_="x", to="y")),
            (calendrum.UnknownOperationError, lambda: calendrum.call("nope", {})),
            (calendrum.UnknownZoneError, lambda: calendrum.tz_resolve("Nowhere/Nothing")),
            (calendrum.UnknownCodeError, lambda: calendrum.explain("ICAL-NOPE")),
            (calendrum.TooLargeError, lambda: calendrum.inspect(b" " * (64 * 1024 * 1024 + 1))),
        ):
            with self.assertRaises(code):
                call()

    def test_a_library_error_is_not_an_error_of_an_operation(self):
        self.assertFalse(issubclass(calendrum.LibraryError, calendrum.CalendrumError))
        self.assertFalse(issubclass(calendrum.ForkedProcessError, calendrum.CalendrumError))
        self.assertTrue(issubclass(calendrum.ForkedProcessError, calendrum.LibraryError))

    def test_errors_pass_between_processes(self):
        for error in (
            calendrum.CalendrumError("OPS-NEW", "text"),
            calendrum.BadArgumentError("OPS-BAD-ARGUMENT", "text"),
            calendrum.TooLargeError("OPS-TOO-LARGE", "text"),
            calendrum.LibraryError("text"),
            calendrum.LibraryError("text", 2),
            calendrum.ForkedProcessError("text"),
        ):
            copy = pickle.loads(pickle.dumps(error))
            self.assertIs(type(error), type(copy))
            self.assertEqual(str(error), str(copy))
            self.assertEqual(error.code, copy.code)
            self.assertEqual(error.message, copy.message)

    def test_the_input_is_not_in_any_error(self):
        attempts = {
            "rule": lambda: calendrum.rrule_expand(MARKER, "2026-01-01T00:00:00Z"),
            "start": lambda: calendrum.rrule_expand("FREQ=DAILY", MARKER),
            "zone": lambda: calendrum.tz_resolve(MARKER),
            "code": lambda: calendrum.explain(MARKER),
            "operation": lambda: calendrum.call(MARKER, {}),
            "window": lambda: calendrum.expand(support.ICS, from_=MARKER, to=MARKER),
            "rules": lambda: calendrum.validate(support.ICS, rules=MARKER),
            "fixup": lambda: calendrum.fix(support.ICS, fixups=[MARKER]),
            "jcal": lambda: calendrum.convert(jcal=MARKER),
            "lone surrogate": lambda: calendrum.inspect(f"\ud800{MARKER}"),
            "surrogate in the operation": lambda: calendrum.call(f"\ud800{MARKER}", {}),
            "zero in the operation": lambda: calendrum.call(f"explain\0{MARKER}", {"code": "ICAL-S003"}),
            "event": lambda: calendrum.build_event(MARKER, MARKER),
            "type of a calendar": lambda: calendrum.inspect(12345),
            "type of an operation": lambda: calendrum.call(12345, {}),
            "type of a request": lambda: calendrum.call("explain", [MARKER]),
            "field of a request": lambda: calendrum.call("explain", {"code": 5, MARKER: MARKER}),
            "a key next to what cannot be JSON": lambda: calendrum.call("inspect", {MARKER: float("nan")}),
            "a key next to another type": lambda: calendrum.call("inspect", {MARKER: object()}),
        }
        for name, attempt in attempts.items():
            with self.subTest(attempt=name):
                with self.assertRaises((calendrum.CalendrumError, TypeError)) as caught:
                    attempt()
                self.assertNotIn(MARKER, carried_text(caught.exception))

    def test_a_request_nested_too_deeply_is_a_bad_argument(self):
        for depth in (100, 100000):
            nested = []
            for _ in range(depth):
                nested = [nested]
            with self.subTest(depth=depth), self.assertRaises(calendrum.BadArgumentError):
                calendrum.convert(jcal=nested)

    def test_a_zero_in_the_name_is_not_another_operation(self):
        with self.assertRaises(calendrum.UnknownOperationError):
            calendrum.call("explain\0inspect", {"code": "ICAL-S003"})


class BrokenResponseTest(unittest.TestCase):
    """What the library cannot give today, and a next version might: it fails clearly, without the text of the response."""

    def setUp(self):
        self.original = _native.call
        self.addCleanup(setattr, _native, "call", self.original)

    def answer(self, raw):
        _native.call = lambda operation, request: raw

    def test_responses_that_do_not_follow_the_contract(self):
        for name, raw in (
            ("not JSON", f"not json {MARKER}".encode()),
            ("not UTF-8", b"\xff\xfe" + MARKER.encode()),
            ("an array", json.dumps([MARKER]).encode()),
            ("no result", json.dumps({"ok": True, MARKER: 1}).encode()),
            ("a result of the wrong type", json.dumps({"ok": True, "result": MARKER}).encode()),
            ("no error", json.dumps({"ok": False, MARKER: 1}).encode()),
            ("a result without its fields", json.dumps({"ok": True, "result": {MARKER: 1}}).encode()),
        ):
            with self.subTest(response=name):
                self.answer(raw)
                with self.assertRaises(calendrum.LibraryError) as caught:
                    calendrum.explain("ICAL-S003")
                self.assertNotIn(MARKER, carried_text(caught.exception))
                self.assertIsNone(caught.exception.__context__)

    def test_a_code_of_the_boundary(self):
        library = _native._library or _native._for_this_process()
        with self.assertRaises(calendrum.LibraryError) as caught:
            library._take(2, None, None)
        self.assertEqual(2, caught.exception.code)
        self.assertIn("code 2", str(caught.exception))


class TruncatedTest(unittest.TestCase):
    def test_a_truncated_result_warns_at_the_line_that_called(self):
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            result = calendrum.expand(support.ICS, limit=1, **support.WINDOW)
        self.assertTrue(result.truncated)
        self.assertEqual(1, result.count)
        self.assertEqual([calendrum.TruncatedWarning], [item.category for item in caught])
        self.assertEqual(__file__, caught[0].filename)
        self.assertNotIn("Plain", str(caught[0].message))

    def test_call_warns_as_well_and_keeps_the_flag(self):
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            result = calendrum.call("expand", {"ics": support.ICS, "from": support.WINDOW["from_"], "to": support.WINDOW["to"], "limit": 1})
        self.assertTrue(result.truncated)
        self.assertIsInstance(result, dict)
        self.assertNotIn("truncated", result)
        self.assertEqual(__file__, caught[0].filename)

    def test_a_program_can_turn_the_warning_into_an_error(self):
        with warnings.catch_warnings():
            warnings.simplefilter("error", calendrum.TruncatedWarning)
            with self.assertRaises(calendrum.TruncatedWarning):
                calendrum.expand(support.ICS, limit=1, **support.WINDOW)

    def test_a_whole_result_does_not_warn(self):
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            result = calendrum.expand(support.ICS, **support.WINDOW)
        self.assertFalse(result.truncated)
        self.assertEqual([], caught)
        self.assertEqual(3, result.count)

    def test_every_result_says_it(self):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", calendrum.TruncatedWarning)
            two = support.ICS.replace("END:VCALENDAR", support.ICS[support.ICS.index("BEGIN:VEVENT"):support.ICS.index("END:VCALENDAR")]
                                      + "END:VCALENDAR")
            self.assertTrue(calendrum.inspect(two, limit=1).truncated)
            self.assertTrue(calendrum.rrule_expand("FREQ=DAILY", "2026-01-01T00:00:00Z", limit=1).truncated)
        self.assertFalse(calendrum.anonymize(support.ICS).truncated)
        self.assertFalse(calendrum.explain("ICAL-S003").truncated)
        self.assertIs(_operations.CallResult, type(calendrum.call("explain", {"code": "ICAL-S003"})))


if __name__ == "__main__":
    unittest.main()
