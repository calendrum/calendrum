"""The models: nothing of a response is lost, nothing can be changed, and the file is what the script writes."""
import copy
import dataclasses
import inspect as python_inspect
import json
import os
import pickle
import subprocess
import sys
import typing
import unittest
import warnings

import support

import calendrum
from calendrum import _models

GENERATE = os.path.join(support.HERE, "..", "generate.py")
RESULT_CLASSES = {
    "inspect": calendrum.InspectResult,
    "validate": calendrum.ValidateResult,
    "fix": calendrum.FixResult,
    "expand": calendrum.ExpandResult,
    "rrule_expand": calendrum.RruleExpandResult,
    "freebusy": calendrum.FreebusyResult,
    "build_event": calendrum.BuildEventResult,
    "diff": calendrum.DiffResult,
    "convert": calendrum.ConvertResult,
    "anonymize": calendrum.AnonymizeResult,
    "tz_resolve": calendrum.TzResolveResult,
    "explain": calendrum.ExplainResult,
}


def decoder(operation):
    return getattr(_models, f"decode_{operation}")


def walk(value):
    if dataclasses.is_dataclass(value):
        yield value
        for field in dataclasses.fields(value):
            yield from walk(getattr(value, field.name))
    elif isinstance(value, tuple):
        for item in value:
            yield from walk(item)


def untrusted_paths(schema, definitions, path=""):
    """The fields a schema marks, with the class that holds them found by the walk."""
    if "$ref" in schema:
        schema = definitions[schema["$ref"].rsplit("/", 1)[1]]
    for name, field in schema.get("properties", {}).items():
        target = definitions[field["$ref"].rsplit("/", 1)[1]] if "$ref" in field else field
        if target.get("x-untrusted"):
            yield f"{path}{name}"
        inner = target.get("items", target)
        if "$ref" in inner:
            inner = definitions[inner["$ref"].rsplit("/", 1)[1]]
        if "properties" in inner:
            yield from untrusted_paths(inner, definitions, f"{path}{name}.")


class ModelsTest(unittest.TestCase):
    cases = support.all_cases()

    def test_no_field_of_a_response_is_lost(self):
        compared = 0
        for case in self.cases:
            with self.subTest(case=case["id"]):
                try:
                    with warnings.catch_warnings():
                        warnings.simplefilter("ignore", calendrum.TruncatedWarning)
                        live = calendrum.call(case["operation"], case["request"])
                except calendrum.CalendrumError:
                    self.assertFalse(case["expected"]["ok"])
                    continue
                decode = decoder(case["operation"])
                self.assertEqual(dict(live), support.to_plain(decode(live, live.truncated)), "the answer of the library")
                expected = case["expected"]["result"]
                self.assertEqual(expected, support.to_plain(decode(expected, False)), "the expected.json of the case")
                compared += 1
        self.assertGreaterEqual(compared, 40)

    def test_a_field_this_version_does_not_know_is_ignored(self):
        data = {"code": "ICAL-S003", "severity": "error", "constant": "X", "meaning": "Y", "new_field": {"a": 1}}
        result = calendrum._models.decode_explain(data, False)
        self.assertEqual("ICAL-S003", result.code)
        self.assertFalse(hasattr(result, "new_field"))

        nested = {"occurrences": [{"calendar_index": 0, "component": "VEVENT", "start": "s", "end": "e", "all_day": False, "new": 1}],
                  "count": 1, "problems": [], "problem_count": 0, "other": []}
        self.assertEqual("s", calendrum._models.decode_expand(nested, False).occurrences[0].start)

    def test_a_field_that_is_absent_is_none(self):
        result = calendrum.inspect(support.ICS)
        self.assertIsNone(result.jcal)
        self.assertIsNone(result.calendars[0].method)
        self.assertIsNone(result.calendars[0].events[0].status)

    def test_a_result_cannot_be_changed(self):
        result = calendrum.expand(support.ICS, **support.WINDOW)
        with self.assertRaises(dataclasses.FrozenInstanceError):
            result.count = 7
        with self.assertRaises(dataclasses.FrozenInstanceError):
            result.occurrences[0].summary = "other"
        self.assertIsInstance(result.occurrences, tuple)
        self.assertIsInstance(result.problems, tuple)
        names = calendrum.inspect(support.ICS).calendars[0].names
        self.assertIsInstance(names, tuple)

    def test_a_result_passes_between_processes_and_can_be_copied(self):
        with warnings.catch_warnings():
            warnings.simplefilter("ignore", calendrum.TruncatedWarning)
            result = calendrum.expand(support.ICS, limit=1, **support.WINDOW)
            raw = calendrum.call("expand", {"ics": support.ICS, "from": support.WINDOW["from_"], "to": support.WINDOW["to"], "limit": 1})
        self.assertEqual(result, pickle.loads(pickle.dumps(result)))
        self.assertEqual(result, copy.deepcopy(result))
        self.assertEqual(7, dataclasses.replace(result, count=7).count)
        self.assertEqual(result.count, dataclasses.asdict(result)["count"])
        self.assertEqual(hash(result), hash(copy.deepcopy(result)))
        again = pickle.loads(pickle.dumps(raw))
        self.assertEqual(raw, again)
        self.assertTrue(again.truncated)
        self.assertEqual(raw, json.loads(json.dumps(raw)))

    def test_every_model_is_frozen_and_every_result_has_truncated(self):
        classes = [item for item in vars(_models).values() if python_inspect.isclass(item) and dataclasses.is_dataclass(item)]
        self.assertEqual(set(_models.__all__), {item.__name__ for item in classes})
        for item in classes:
            self.assertTrue(item.__dataclass_params__.frozen, item.__name__)
        for operation, item in RESULT_CLASSES.items():
            names = {field.name for field in dataclasses.fields(item)}
            self.assertIn("truncated", names, operation)
            self.assertIs(typing.get_type_hints(decoder(operation))["return"], item)

    def test_untrusted_fields_are_marked_in_the_docstrings(self):
        definitions = support.load_json(f"{support.SCHEMAS}/common.schema.json")["$defs"]
        checked = 0
        for operation, item in RESULT_CLASSES.items():
            schema = support.load_json(f"{support.SCHEMAS}/{operation}.result.schema.json")
            for path in untrusted_paths(schema, definitions):
                owner = item
                parts = path.split(".")
                for part in parts[:-1]:
                    hints = typing.get_type_hints(owner, vars(_models))[part]
                    arguments = [a for a in typing.get_args(hints) if a is not type(None) and a is not Ellipsis]
                    while arguments and typing.get_origin(arguments[0]) is not None:
                        arguments = [a for a in typing.get_args(arguments[0]) if a is not type(None) and a is not Ellipsis]
                    owner = arguments[0] if arguments else hints
                with self.subTest(field=f"{operation}.{path}"):
                    self.assertRegex(owner.__doc__, rf"\n\s+{parts[-1]}: Untrusted")
                    checked += 1
        self.assertGreater(checked, 25)

    def test_the_package_exports_every_model(self):
        for name in _models.__all__:
            self.assertIs(getattr(_models, name), getattr(calendrum, name))
            self.assertIn(name, calendrum.__all__)
        for name in calendrum.__all__:
            self.assertTrue(hasattr(calendrum, name), name)

    def test_the_generated_files_are_what_the_script_writes(self):
        done = subprocess.run([sys.executable, "-I", "-B", GENERATE, "--check"], capture_output=True, text=True, timeout=60, check=False)
        self.assertEqual(0, done.returncode, done.stdout + done.stderr)

    def test_the_package_reads_no_environment_and_opens_nothing_on_its_own(self):
        directory = os.path.dirname(calendrum.__file__)
        for name in os.listdir(directory):
            if name.endswith(".py"):
                with open(os.path.join(directory, name), encoding="utf-8") as file:
                    text = file.read()
                for word in ("environ", "getenv", "subprocess", "socket", "urllib", "http.", "pickle", "eval(", "exec("):
                    self.assertNotIn(word, text, name)

    def test_versions(self):
        found = calendrum.versions()
        self.assertEqual(calendrum.__version__, found.package)
        self.assertEqual(1, found.abi)
        self.assertEqual(1, found.contract)
        self.assertTrue(found.library)
        self.assertTrue(found.sdk)
        with self.assertRaises(dataclasses.FrozenInstanceError):
            found.abi = 2


if __name__ == "__main__":
    unittest.main()
