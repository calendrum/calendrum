"""All the cases of the contract through the real library, by the rules of conformance/ops/README.md.

A case goes through the function of its operation where its arguments can say the request, and always through `call` as well. The
library has a real clock and real random bytes, so the cases that depend on them are checked by what they mean.
"""
import base64
import datetime
import inspect as python_inspect
import json
import re
import unittest
import warnings

import support

import calendrum

NEEDS_CLOCK_OR_RANDOM = {"anonymize-shift", "anonymize-text-goes", "build-event-all-day-generated-uid", "tz-resolve-default-moment"}

FUNCTIONS = {name: getattr(calendrum, name) for name in support.OPERATIONS}
CALENDAR_OPERATIONS = {"inspect", "validate", "fix", "expand", "freebusy", "anonymize"}
POSITIONAL = {"rrule_expand": ("rrule", "start"), "build_event": ("summary", "start"), "tz_resolve": ("name",), "explain": ("code",)}
REQUIRED_KEYWORDS = {"expand": ("from", "to"), "freebusy": ("from", "to")}


def calendar_value(request, field):
    """Takes a calendar out of a request: text stays a str, and base64 turns into the bytes it stands for."""
    if field in request:
        return request.pop(field)
    if f"{field}_base64" in request:
        return base64.b64decode(request.pop(f"{field}_base64"))
    return None


def function_arguments(operation, original):
    """The arguments of the function for a request, or None where its signature cannot say it."""
    request = dict(original)
    positional = []
    if operation in CALENDAR_OPERATIONS:
        calendar = calendar_value(request, "ics")
        if calendar is None:
            return None
        positional.append(calendar)
    elif operation == "diff":
        before, after = calendar_value(request, "before"), calendar_value(request, "after")
        if before is None or after is None:
            return None
        positional += [before, after]
    elif operation == "convert":
        calendar = calendar_value(request, "ics")
        if calendar is not None:
            positional.append(calendar)
    else:
        for name in POSITIONAL[operation]:
            if name not in request:
                return None
            positional.append(request.pop(name))
    for name in REQUIRED_KEYWORDS.get(operation, ()):
        if name not in request:
            return None
    if "from" in request:
        request["from_"] = request.pop("from")
    return positional, request


def run_function(case):
    """Calls the function and returns the same shape as a response, or None where only `call` can say the request."""
    arguments = function_arguments(case["operation"], case["request"])
    if arguments is None:
        return None
    positional, keywords = arguments
    try:
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            result = FUNCTIONS[case["operation"]](*positional, **keywords)
    except calendrum.CalendrumError as error:
        return {"ok": False, "error": {"code": error.code, "message": error.message}, "class": type(error)}
    warned = any(item.category is calendrum.TruncatedWarning for item in caught)
    return {"ok": True, "result": support.to_plain(result), "truncated": result.truncated, "warned": warned}


def run_call(case):
    try:
        with warnings.catch_warnings(record=True) as caught:
            warnings.simplefilter("always")
            result = calendrum.call(case["operation"], case["request"])
    except calendrum.CalendrumError as error:
        return {"ok": False, "error": {"code": error.code, "message": error.message}, "class": type(error)}
    warned = any(item.category is calendrum.TruncatedWarning for item in caught)
    return {"ok": True, "result": dict(result), "truncated": result.truncated, "warned": warned}


class ContractTest(unittest.TestCase):
    cases = support.all_cases()

    def compare(self, case, answer):
        expected = case["expected"]
        self.assertEqual(expected["ok"], answer["ok"], answer)
        if not expected["ok"]:
            self.assertEqual(expected["error"]["code"], answer["error"]["code"])
            return
        wanted = expected.get("truncated", False)
        self.assertEqual(wanted, answer["truncated"])
        self.assertEqual(wanted, answer["warned"], "a warning goes with a truncated result, and only with it")
        if case["id"] in NEEDS_CLOCK_OR_RANDOM:
            self.check_by_meaning(case, answer["result"])
        else:
            self.assertEqual(support.comparable(expected["result"], case["ignored"]),
                             support.comparable(answer["result"], case["ignored"]))

    def check_by_meaning(self, case, result):
        expected = case["expected"]["result"]
        if case["operation"] == "anonymize":
            text = result["ics"]
            for secret in ("Salary", "Alice", "boss@example.com"):
                self.assertNotIn(secret, text)
            self.assertEqual(expected["shift_weeks"], result["shift_weeks"])
            self.assertEqual(self.shape(expected["ics"]), self.shape(text))
        elif case["operation"] == "build_event":
            uid = result["uid"]
            self.assertRegex(uid, r"^[0-9a-f]{32}@calendrum$")
            stamp = re.search(r"DTSTAMP:(\d{8}T\d{6})Z", result["ics"]).group(1)
            moment = datetime.datetime.strptime(stamp, "%Y%m%dT%H%M%S").replace(tzinfo=datetime.timezone.utc)
            self.assertLess(abs((datetime.datetime.now(datetime.timezone.utc) - moment).total_seconds()), 120)
            self.assertEqual(expected["ics"], result["ics"].replace(uid, expected["uid"]).replace(stamp, "20260101T000000"))
            self.assertTrue(calendrum.validate(result["ics"]).valid)
        elif case["operation"] == "tz_resolve":
            self.assertEqual(expected["id"], result["id"])
            self.assertEqual(expected["offset_seconds"], result["offset_seconds"])
        else:
            self.fail(case["id"])

    @staticmethod
    def shape(ics):
        return [line.split(":")[0].split(";")[0] for line in ics.split("\r\n")]

    def test_every_case_through_its_function(self):
        only_call = []
        through_function = 0
        for case in self.cases:
            with self.subTest(case=case["id"]):
                answer = run_function(case)
                if answer is None:
                    only_call.append(case["id"])
                    continue
                self.compare(case, answer)
                through_function += 1
        by_meaning = sorted(NEEDS_CLOCK_OR_RANDOM)
        print(f"\ncontract: {len(self.cases)} cases; {through_function} through the functions, {len(by_meaning)} of them by meaning: "
              f"{by_meaning}; only through call: {only_call}")
        self.assertEqual({"diff-no-after", "explain-missing", "inspect-no-input"}, set(only_call))

    def test_every_case_through_call(self):
        for case in self.cases:
            with self.subTest(case=case["id"]):
                self.compare(case, run_call(case))

    def test_an_error_comes_with_its_class(self):
        classes = {
            "OPS-BAD-ARGUMENT": calendrum.BadArgumentError,
            "OPS-UNKNOWN-ZONE": calendrum.UnknownZoneError,
            "OPS-UNKNOWN-CODE": calendrum.UnknownCodeError,
        }
        seen = set()
        for case in self.cases:
            if not case["expected"]["ok"]:
                answer = run_call(case)
                code = case["expected"]["error"]["code"]
                self.assertIs(classes[code], answer["class"], case["id"])
                seen.add(code)
        self.assertEqual(set(classes), seen)

    def test_a_calendar_as_a_path_or_bytes_is_the_same_calendar(self):
        text = next(c for c in self.cases if c["id"] == "expand-weekly-in-zone")["request"]
        window = {"from_": text["from"], "to": text["to"], "zone": text["zone"]}
        as_text = calendrum.expand(text["ics"], **window)
        as_bytes = calendrum.expand(text["ics"].encode("utf-8"), **window)
        self.assertEqual(as_text, as_bytes)

    def test_the_functions_take_every_field_of_the_requests(self):
        for operation, function in FUNCTIONS.items():
            with self.subTest(operation=operation):
                schema = support.load_json(f"{support.SCHEMAS}/{operation}.request.schema.json")
                wanted = set()
                for name in schema["properties"]:
                    if name.endswith("_base64"):
                        continue
                    wanted.add("from_" if name == "from" else name)
                if operation in CALENDAR_OPERATIONS or operation == "convert":
                    wanted.discard("ics")
                    wanted.add("calendar")
                parameters = set(python_inspect.signature(function).parameters)
                self.assertEqual(wanted, parameters)

    def test_only_what_was_given_is_sent(self):
        seen = {}
        original = calendrum._operations._native.call

        def spy(operation, request):
            seen[operation] = json.loads(request)
            return original(operation, request)

        calendrum._operations._native.call = spy
        try:
            calendrum.tz_resolve("Europe/Berlin")
            calendrum.fix(support.ICS, fixups=[])
        finally:
            calendrum._operations._native.call = original
        self.assertEqual({"name": "Europe/Berlin"}, seen[b"tz_resolve"])
        self.assertEqual({"ics": support.ICS, "fixups": []}, seen[b"fix"], "an empty list is a value, and it means no repairs")


if __name__ == "__main__":
    unittest.main()
