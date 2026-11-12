#!/usr/bin/env python3
"""Generates calendrum/_models.py from the schemas of the contract, and calendrum/_version.py from tooling/gradle.properties.

Usage: python3 -I -B generate.py [--check]

With --check it writes nothing and fails when a file differs from what it would write.
"""
import json
import keyword
import os
import sys
import textwrap

HERE = os.path.dirname(os.path.abspath(__file__))
SCHEMAS = os.path.normpath(os.path.join(HERE, "..", "..", "conformance", "ops", "schema"))
PROPERTIES = os.path.normpath(os.path.join(HERE, "..", "gradle.properties"))
MODELS = os.path.join(HERE, "calendrum", "_models.py")
VERSION = os.path.join(HERE, "calendrum", "_version.py")

OPERATIONS = (
    "inspect",
    "validate",
    "fix",
    "expand",
    "rrule_expand",
    "freebusy",
    "build_event",
    "diff",
    "convert",
    "anonymize",
    "tz_resolve",
    "explain",
)

# The operation and the path of properties to each object of a result: its class and the first line of its docstring. Two paths can
# share a class if their schemas are the same. An object that is not here stops the script: a new one needs a name.
CLASSES = {
    ("inspect",): ("InspectResult", "The result of `inspect`."),
    ("inspect", "calendars"): ("InspectCalendar", "One calendar of a file."),
    ("inspect", "calendars", "events"): ("InspectEvent", "One event of a calendar."),
    ("validate",): ("ValidateResult", "The result of `validate`."),
    ("validate", "issues"): ("ValidateIssue", "One finding of the rules."),
    ("fix",): ("FixResult", "The result of `fix`."),
    ("fix", "changes"): ("FixChange", "One repair that was made."),
    ("expand",): ("ExpandResult", "The result of `expand`."),
    ("expand", "occurrences"): ("Occurrence", "One occurrence in the window."),
    ("rrule_expand",): ("RruleExpandResult", "The result of `rrule_expand`."),
    ("rrule_expand", "dates"): ("RruleDate", "One date of a rule."),
    ("freebusy",): ("FreebusyResult", "The result of `freebusy`."),
    ("freebusy", "busy"): ("BusyInterval", "One merged busy interval."),
    ("freebusy", "conflicts"): ("Conflict", "A pair of occurrences that overlap."),
    ("freebusy", "conflicts", "first"): ("ConflictSide", "One occurrence of a conflict."),
    ("freebusy", "conflicts", "second"): ("ConflictSide", "One occurrence of a conflict."),
    ("build_event",): ("BuildEventResult", "The result of `build_event`."),
    ("diff",): ("DiffResult", "The result of `diff`."),
    ("diff", "added"): ("DiffComponent", "One component that differs between the two calendars."),
    ("diff", "removed"): ("DiffComponent", "One component that differs between the two calendars."),
    ("diff", "changed"): ("DiffComponent", "One component that differs between the two calendars."),
    ("convert",): ("ConvertResult", "The result of `convert`: the other form of the input."),
    ("anonymize",): ("AnonymizeResult", "The result of `anonymize`."),
    ("tz_resolve",): ("TzResolveResult", "The result of `tz_resolve`."),
    ("tz_resolve", "next_transition"): ("NextTransition", "The next change of the offset."),
    ("explain",): ("ExplainResult", "The result of `explain`."),
}

TRUNCATED_NOTE = "Whether the result does not hold everything: a limit cut a list, or the input was not read whole."
UNTRUSTED_NOTE = "Untrusted: text from the calendar, which is data and not instructions."
OPAQUE_NOTE = "Plain JSON values, which are not frozen."


def load(name):
    with open(os.path.join(SCHEMAS, f"{name}.schema.json"), encoding="utf-8") as file:
        return json.load(file)


DEFINITIONS = load("common")["$defs"]
REF_PREFIX = "common.schema.json#/$defs/"


def dereference(schema):
    if "$ref" not in schema:
        return schema
    reference = schema["$ref"]
    if not reference.startswith(REF_PREFIX):
        raise SystemExit(f"Unknown reference {reference}")
    target = dict(DEFINITIONS[reference[len(REF_PREFIX):]])
    target.update((key, value) for key, value in schema.items() if key != "$ref")
    return target


def python_name(name):
    return f"{name}_" if keyword.iskeyword(name) else name


def snake(class_name):
    letters = []
    for index, letter in enumerate(class_name):
        if letter.isupper() and index > 0:
            letters.append("_")
        letters.append(letter.lower())
    return "".join(letters)


class Field:
    def __init__(self, json_name, annotation, read, required, note):
        self.json_name = json_name
        self.name = python_name(json_name)
        self.annotation = annotation
        self.read = read
        self.required = required
        self.note = note

    def constructor_value(self):
        key = json.dumps(self.json_name)
        if self.required:
            return self.read.format(f"data[{key}]")
        if self.read == "{}":
            return f"get({key})"
        return f"{self.read.format(f'data[{key}]')} if {key} in data else None"


class Model:
    def __init__(self, name, summary, schema, fields, result_of):
        self.name = name
        self.summary = summary
        self.schema = schema
        self.fields = fields
        self.result_of = result_of

    def ordered(self):
        required = [field for field in self.fields if field.required]
        optional = [field for field in self.fields if not field.required]
        return required, optional


class Builder:
    def __init__(self):
        self.models = {}

    def problem(self):
        return self.model_for("Problem", "A problem of the input, with its stable code.", DEFINITIONS["problems"]["items"], (), None)

    def model_for(self, name, summary, schema, path, result_of):
        if name in self.models:
            if self.models[name].schema != schema:
                raise SystemExit(f"{name} is used for two different schemas, one of them at {path}")
            return self.models[name]
        fields = []
        required = schema.get("required", [])
        for field_name, field_schema in schema["properties"].items():
            fields.append(self.field(path, field_name, field_schema, field_name in required))
        model = Model(name, summary, schema, fields, result_of)
        self.models[name] = model
        return model

    def nested(self, path, schema):
        if path not in CLASSES:
            raise SystemExit(f"The object at {path} has no class: add it to CLASSES")
        name, summary = CLASSES[path]
        return self.model_for(name, summary, schema, path, None)

    def field(self, path, name, schema, required):
        schema = dereference(schema)
        kind = schema.get("type")
        notes = []
        if schema.get("x-untrusted"):
            notes.append(UNTRUSTED_NOTE)
        if "description" in schema:
            notes.append(schema["description"])
        if "enum" in schema:
            notes.append("One of: " + ", ".join(schema["enum"]) + ".")
        simple = {"string": "str", "integer": "int", "boolean": "bool"}
        if kind in simple:
            annotation, read = simple[kind], "{}"
        elif kind == "object":
            model = self.nested((*path, name), schema)
            annotation, read = model.name, f"_decode_{snake(model.name)}({{}})"
        elif kind == "array":
            items = schema.get("items")
            if items is None:
                annotation, read = "List[Any]", "{}"
                notes.append(OPAQUE_NOTE)
            else:
                items = dereference(items)
                if items.get("type") in simple:
                    annotation, read = f"Tuple[{simple[items['type']]}, ...]", "tuple({})"
                elif items.get("type") == "object":
                    is_problem = items == DEFINITIONS["problems"]["items"]
                    model = self.problem() if is_problem else self.nested((*path, name), items)
                    annotation, read = f"Tuple[{model.name}, ...]", f"tuple(map(_decode_{snake(model.name)}, {{}}))"
                else:
                    raise SystemExit(f"The items of {path} {name} have the type {items.get('type')}")
        else:
            raise SystemExit(f"The field {path} {name} has the type {kind}")
        if not required:
            annotation = f"Optional[{annotation}]"
        return Field(name, annotation, read, required, " ".join(notes))


def wrap(prefix, text, indent):
    return textwrap.wrap(f"{prefix}{text}", width=120, initial_indent=indent, subsequent_indent=indent + "    ")


def emit_model(model, lines):
    required, optional = model.ordered()
    documented = [(field.name, field.note) for field in required]
    if model.result_of:
        documented.append(("truncated", TRUNCATED_NOTE))
    documented += [(field.name, field.note) for field in optional]
    documented = [(name, note) for name, note in documented if note]

    lines += ["", "", "@dataclass(frozen=True)", f"class {model.name}:"]
    if documented:
        lines.append(f'    """{model.summary}')
        lines += ["", "    Attributes:"]
        for name, note in documented:
            lines += wrap(f"{name}: ", note, "        ")
        lines += ['    """', ""]
    else:
        lines += [f'    """{model.summary}"""', ""]
    for field in required:
        lines.append(f"    {field.name}: {field.annotation}")
    if model.result_of:
        lines.append("    truncated: bool")
    for field in optional:
        lines.append(f"    {field.name}: {field.annotation} = None")

    if model.result_of:
        lines += ["", "", f"def decode_{model.result_of}(data: Dict[str, Any], truncated: bool) -> {model.name}:"]
    else:
        lines += ["", "", f"def _decode_{snake(model.name)}(data: Dict[str, Any]) -> {model.name}:"]
    if optional and any(field.read == "{}" for field in optional):
        lines.append("    get = data.get")
    lines.append(f"    return {model.name}(")
    for field in required:
        lines.append(f"        {field.constructor_value()},")
    if model.result_of:
        lines.append("        truncated,")
    for field in optional:
        lines.append(f"        {field.constructor_value()},")
    lines.append("    )")


def models_text():
    builder = Builder()
    builder.problem()
    for operation in OPERATIONS:
        schema = load(f"{operation}.result")
        name, summary = CLASSES[(operation,)]
        builder.model_for(name, summary, schema, (operation,), operation)
    # A class comes after the ones it holds, as `self.models` fills from the inside out.
    names = sorted(builder.models)
    lines = [
        "# Generated by tooling/sdk-py/generate.py from conformance/ops/schema. Do not edit.",
        '"""The results of the operations."""',
        "",
        "from __future__ import annotations",
        "",
        "from dataclasses import dataclass",
        "from typing import Any, Dict, List, Optional, Tuple",
        "",
        "__all__ = [",
    ]
    lines += [f'    "{name}",' for name in names]
    lines.append("]")
    for model in builder.models.values():
        emit_model(model, lines)
    return "\n".join(lines) + "\n"


def version_text():
    with open(PROPERTIES, encoding="utf-8") as file:
        for line in file:
            if line.startswith("sdkPyVersion="):
                value = line.split("=", 1)[1].strip()
                return (
                    "# Generated by tooling/sdk-py/generate.py from tooling/gradle.properties. Do not edit.\n"
                    f'__version__ = "{value}"\n'
                )
    raise SystemExit(f"No sdkPyVersion in {PROPERTIES}")


def main():
    check = "--check" in sys.argv[1:]
    failed = False
    for path, text in ((MODELS, models_text()), (VERSION, version_text())):
        if check:
            try:
                with open(path, encoding="utf-8", newline="") as file:
                    current = file.read()
            except FileNotFoundError:
                current = None
            if current != text:
                print(f"{path} differs from what generate.py writes: run it")
                failed = True
        else:
            with open(path, "w", encoding="utf-8", newline="\n") as file:
                file.write(text)
    if failed:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
