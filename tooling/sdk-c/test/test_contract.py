"""All the cases of the contract through the real library, by the rules of conformance/ops/README.md, with nothing but the standard
library. The library has a real clock and real random bytes, so the cases that depend on them are checked by what they mean.

Run with python3 -I. The environment gives the paths: CALENDRUM_LIBRARY, CALENDRUM_CASES and CALENDRUM_CLI.
"""
import ctypes
import datetime
import json
import os
import re
import subprocess
import tempfile
import threading
import unittest

LIBRARY = os.environ["CALENDRUM_LIBRARY"]
CASES = os.environ["CALENDRUM_CASES"]
CLI = os.environ["CALENDRUM_CLI"]

OPERATIONS = {"inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff", "convert", "anonymize",
              "tz_resolve", "explain"}

# The library has a real clock and real random bytes, and no way to fix them from outside.
NEEDS_CLOCK_OR_RANDOM = {"anonymize-shift", "anonymize-text-goes", "build-event-all-day-generated-uid", "tz-resolve-default-moment"}

KNOWN_KEYWORDS = {"$schema", "$id", "$defs", "$ref", "title", "description", "type", "enum", "oneOf", "properties", "required",
                  "additionalProperties", "items", "x-untrusted"}

_Bytes = ctypes.POINTER(ctypes.c_uint8)


class Library:
    def __init__(self, path):
        self.lib = ctypes.CDLL(path)
        self.lib.calendrum_abi_version.restype = ctypes.c_int32
        self.lib.calendrum_abi_version.argtypes = []
        self.lib.calendrum_version.restype = ctypes.c_int32
        self.lib.calendrum_version.argtypes = [ctypes.POINTER(_Bytes), ctypes.POINTER(ctypes.c_size_t)]
        self.lib.calendrum_call.restype = ctypes.c_int32
        self.lib.calendrum_call.argtypes = [ctypes.c_char_p, ctypes.c_char_p, ctypes.c_size_t, ctypes.POINTER(_Bytes),
                                            ctypes.POINTER(ctypes.c_size_t)]
        self.lib.calendrum_free.restype = None
        self.lib.calendrum_free.argtypes = [_Bytes]

    def _take(self, code, data, size):
        if code != 0:
            raise AssertionError(f"code {code}")
        try:
            return ctypes.string_at(data, size.value)
        finally:
            self.lib.calendrum_free(data)

    def version(self):
        data, size = _Bytes(), ctypes.c_size_t()
        return self._take(self.lib.calendrum_version(ctypes.byref(data), ctypes.byref(size)), data, size)

    def call(self, operation, request):
        data, size = _Bytes(), ctypes.c_size_t()
        code = self.lib.calendrum_call(operation.encode(), request, len(request), ctypes.byref(data), ctypes.byref(size))
        return self._take(code, data, size)


LIB = Library(LIBRARY)


def load_json(path):
    with open(path, encoding="utf-8") as file:
        return json.load(file)


def all_cases():
    found = []
    for name in sorted(os.listdir(CASES)):
        directory = os.path.join(CASES, name)
        if not os.path.exists(os.path.join(directory, "request.json")):
            continue
        with open(os.path.join(directory, "request.json"), "rb") as file:
            request = file.read()
        meta = load_json(os.path.join(directory, "meta.json"))
        found.append({
            "id": name,
            "operation": meta["operation"],
            "request": request,
            "expected": load_json(os.path.join(directory, "expected.json")),
            "ignored": set(meta.get("ignore_fields", [])),
        })
    return found


def comparable(value, ignored):
    if isinstance(value, dict):
        return {key: comparable(item, ignored) for key, item in value.items() if key != "message" and key not in ignored}
    if isinstance(value, list):
        return [comparable(item, ignored) for item in value]
    return value


def has_type(value, name):
    if name == "object":
        return isinstance(value, dict)
    if name == "array":
        return isinstance(value, list)
    if name == "null":
        return value is None
    if name == "string":
        return isinstance(value, str)
    if name == "boolean":
        return isinstance(value, bool)
    if name == "integer":
        return isinstance(value, int) and not isinstance(value, bool)
    return False


class SchemaCheck:
    """The part of JSON Schema that the schemas of the contract use, as SchemaCheck.kt reads it. An unknown keyword is an error."""

    def __init__(self):
        self.directory = os.path.join(CASES, "schema")
        self.definitions = self.load("common")["$defs"]

    def load(self, name):
        return load_json(os.path.join(self.directory, f"{name}.schema.json"))

    def check(self, value, schema, path, errors):
        for key in schema:
            if key not in KNOWN_KEYWORDS:
                errors.append(f"{path}: unknown keyword {key}")
        if "$ref" in schema:
            name = schema["$ref"].removeprefix("common.schema.json#/$defs/")
            if name not in self.definitions:
                errors.append(f"{path}: unknown reference {name}")
            else:
                self.check(value, self.definitions[name], path, errors)
        if "type" in schema:
            types = schema["type"] if isinstance(schema["type"], list) else [schema["type"]]
            if not any(has_type(value, name) for name in types):
                errors.append(f"{path}: not {'|'.join(types)}")
        if "enum" in schema and value not in schema["enum"]:
            errors.append(f"{path}: not in the enum")
        if "oneOf" in schema:
            matches = 0
            for option in schema["oneOf"]:
                inner = []
                self.check(value, option, path, inner)
                matches += not inner
            if matches != 1:
                errors.append(f"{path}: matches {matches} options of oneOf")
        if isinstance(value, dict):
            properties = schema.get("properties", {})
            for name in schema.get("required", []):
                if name not in value:
                    errors.append(f"{path}: no {name}")
            for name, field in value.items():
                if name in properties:
                    self.check(field, properties[name], f"{path}.{name}", errors)
                elif schema.get("additionalProperties") is False:
                    errors.append(f"{path}: unexpected field {name}")
        if isinstance(value, list) and "items" in schema:
            for index, item in enumerate(value):
                self.check(item, schema["items"], f"{path}[{index}]", errors)


class ContractTest(unittest.TestCase):
    cases = all_cases()
    schemas = SchemaCheck()

    def test_there_are_enough_cases(self):
        counts = {}
        for case in self.cases:
            counts[case["operation"]] = counts.get(case["operation"], 0) + 1
        self.assertEqual(OPERATIONS, set(counts))
        for operation, count in counts.items():
            self.assertGreaterEqual(count, 3, operation)

    def test_every_case_gives_the_expected_response(self):
        compared = 0
        for case in self.cases:
            with self.subTest(case=case["id"]):
                raw = LIB.call(case["operation"], case["request"])
                response = json.loads(raw.decode("utf-8"))
                self.assertEqual(1, response["ops_version"])
                self.check_schemas(case, response)
                if case["id"] in NEEDS_CLOCK_OR_RANDOM:
                    self.check_by_meaning(case, response)
                else:
                    self.assertEqual(comparable(case["expected"], case["ignored"]), comparable(response, case["ignored"]))
                    compared += 1
        self.assertEqual(len(self.cases) - len(NEEDS_CLOCK_OR_RANDOM), compared)
        print(f"\ncontract: {compared} cases compared, {len(NEEDS_CLOCK_OR_RANDOM)} by meaning: {sorted(NEEDS_CLOCK_OR_RANDOM)}")

    def check_schemas(self, case, response):
        errors = []
        self.schemas.check(response, self.schemas.load("envelope"), f"{case['id']} envelope", errors)
        if response["ok"]:
            self.schemas.check(response["result"], self.schemas.load(f"{case['operation']}.result"), f"{case['id']} result", errors)
            self.schemas.check(json.loads(case["request"]), self.schemas.load(f"{case['operation']}.request"),
                               f"{case['id']} request", errors)
        self.assertEqual([], errors)

    def check_by_meaning(self, case, response):
        expected = case["expected"]["result"]
        result = response["result"]
        if case["operation"] == "anonymize":
            text = result["ics"]
            for secret in ("Salary", "Alice", "boss@example.com"):
                self.assertNotIn(secret, text)
            self.assertEqual(expected["shift_weeks"], result["shift_weeks"])
            # The same shape: the same lines without what comes from the key.
            self.assertEqual(self.shape(expected["ics"]), self.shape(text))
            again = json.loads(LIB.call(case["operation"], case["request"]).decode("utf-8"))["result"]["ics"]
            if "UID:" in text:
                self.assertNotEqual(text, again, "a key for each call")
        elif case["operation"] == "build_event":
            uid = result["uid"]
            self.assertRegex(uid, r"^[0-9a-f]{32}@calendrum$")
            stamp = re.search(r"DTSTAMP:(\d{8}T\d{6})Z", result["ics"]).group(1)
            moment = datetime.datetime.strptime(stamp, "%Y%m%dT%H%M%S").replace(tzinfo=datetime.timezone.utc)
            age = abs((datetime.datetime.now(datetime.timezone.utc) - moment).total_seconds())
            self.assertLess(age, 120)
            # All the rest is what the case expects.
            normal = result["ics"].replace(uid, expected["uid"]).replace(stamp, "20260101T000000")
            self.assertEqual(expected["ics"], normal)
            valid = LIB.call("validate", json.dumps({"ics": result["ics"]}).encode())
            self.assertTrue(json.loads(valid.decode("utf-8"))["result"]["valid"])
        elif case["operation"] == "tz_resolve":
            self.assertEqual(expected["id"], result["id"])
            self.assertEqual(expected["offset_seconds"], result["offset_seconds"])
        else:
            self.fail(case["id"])

    @staticmethod
    def shape(ics):
        return [line.split(":")[0].split(";")[0] for line in ics.split("\r\n")]

    def test_response_ends_with_zero(self):
        data, size = _Bytes(), ctypes.c_size_t()
        request = b'{"code":"ICAL-S003"}'
        self.assertEqual(0, LIB.lib.calendrum_call(b"explain", request, len(request), ctypes.byref(data), ctypes.byref(size)))
        self.assertEqual(0, data[size.value])
        LIB.lib.calendrum_free(data)

    def test_versions(self):
        version = json.loads(LIB.version().decode("utf-8"))
        self.assertEqual({"abi", "contract", "library", "sdk"}, set(version))
        self.assertEqual(LIB.lib.calendrum_abi_version(), version["abi"])
        self.assertEqual(1, version["contract"])

    def test_threads_of_python(self):
        case = next(c for c in self.cases if c["id"] == "expand-weekly-in-zone")
        expected = LIB.call(case["operation"], case["request"])
        wrong = []

        def work():
            for _ in range(200):
                if LIB.call(case["operation"], case["request"]) != expected:
                    wrong.append(1)

        threads = [threading.Thread(target=work) for _ in range(8)]
        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()
        self.assertEqual([], wrong)

    # The output of --json is the response of Ops.call unchanged, so here is where the two transports meet, byte for byte.
    def test_the_command_line_gives_the_same_bytes(self):
        compared = 0
        with tempfile.TemporaryDirectory() as directory:
            for case in self.cases:
                if case["id"] in NEEDS_CLOCK_OR_RANDOM:
                    continue
                arguments = self.command_line(case, directory)
                if arguments is None:
                    continue
                with self.subTest(case=case["id"]):
                    ran = subprocess.run([CLI, *arguments, "--json"], capture_output=True, stdin=subprocess.DEVNULL, timeout=60, check=False)
                    self.assertEqual(LIB.call(case["operation"], self.as_sent_by_the_command_line(case)) + b"\n", ran.stdout)
                    compared += 1
        self.assertGreaterEqual(compared, 30)
        print(f"\ncommand line: {compared} cases, the same bytes")

    @staticmethod
    def as_sent_by_the_command_line(case):
        """The request with defaults the command line always sends, as it has to say them."""
        request = json.loads(case["request"])
        if case["operation"] == "validate":
            request.setdefault("rules", "rfc5545")
        return json.dumps(request).encode()

    @staticmethod
    def command_line(case, directory):
        """The arguments of the command line for a case, or None where its flags cannot say the request."""
        request = json.loads(case["request"])
        operation = case["operation"]

        def write(name, content):
            path = os.path.join(directory, f"{case['id']}.{name}")
            with open(path, "w", encoding="utf-8", newline="") as file:
                file.write(content if isinstance(content, str) else json.dumps(content))
            return path

        def option(flag, field):
            return [flag, str(request[field])] if field in request else []

        if operation == "validate" and "ics" in request:
            return ["lint", write("ics", request["ics"]), "--rules", request.get("rules", "rfc5545"), *option("--limit", "limit")]
        if operation == "fix" and "ics" in request:
            path = write("ics", request["ics"])
            if "fixups" not in request:
                return ["fix", path]
            if not request["fixups"]:
                return ["format", path]
            return ["fix", path, "--fixups", ",".join(request["fixups"])]
        if operation == "convert":
            if "ics" in request and "jcal" not in request:
                return ["convert", write("ics", request["ics"])]
            if "jcal" in request and "ics" not in request:
                return ["convert", write("json", request["jcal"])]
            return None
        if operation == "expand" and "ics" in request:
            cancelled = ["--include-cancelled"] if request.get("include_cancelled") else []
            return ["expand", write("ics", request["ics"]), *option("--from", "from"), *option("--to", "to"), *option("--zone", "zone"),
                    *option("--limit", "limit"), *cancelled]
        if operation == "diff" and "before" in request and "after" in request:
            return ["diff", write("before.ics", request["before"]), write("after.ics", request["after"]), *option("--limit", "limit")]
        if operation == "tz_resolve" and "name" in request:
            return ["tz", request["name"], *option("--at", "at")]
        if operation == "explain" and "code" in request:
            return ["explain", request["code"]]
        return None


if __name__ == "__main__":
    unittest.main(verbosity=1)
