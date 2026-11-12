#!/usr/bin/env node
// Generates the models of the package from the schemas of the contract, and package.json and the version from
// tooling/gradle.properties.
//
// Usage: node generate.mjs [--check]
//
// With --check it writes nothing and fails when a file differs from what it would write.
import { readFileSync, writeFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

const here = dirname(fileURLToPath(import.meta.url));
const schemas = join(here, "..", "..", "conformance", "ops", "schema");
const properties = join(here, "..", "gradle.properties");
const output = join(here, "package");

const OPERATIONS = ["inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff", "convert", "anonymize",
    "tz_resolve", "explain"];

// The operation and the path of properties to each object of a result: its interface and its first line. Two paths can share an
// interface if their schemas are the same. An object that is not here stops the script: a new one needs a name.
const INTERFACES = {
    "inspect": ["InspectResult", "The result of `inspect`."],
    "inspect.calendars": ["InspectCalendar", "One calendar of a file."],
    "inspect.calendars.events": ["InspectEvent", "One event of a calendar."],
    "validate": ["ValidateResult", "The result of `validate`."],
    "validate.issues": ["ValidateIssue", "One finding of the rules."],
    "fix": ["FixResult", "The result of `fix`."],
    "fix.changes": ["FixChange", "One repair that was made."],
    "expand": ["ExpandResult", "The result of `expand`."],
    "expand.occurrences": ["Occurrence", "One occurrence in the window."],
    "rrule_expand": ["RruleExpandResult", "The result of `rruleExpand`."],
    "rrule_expand.dates": ["RruleDate", "One date of a rule."],
    "freebusy": ["FreebusyResult", "The result of `freebusy`."],
    "freebusy.busy": ["BusyInterval", "One merged busy interval."],
    "freebusy.conflicts": ["Conflict", "A pair of occurrences that overlap."],
    "freebusy.conflicts.first": ["ConflictSide", "One occurrence of a conflict."],
    "freebusy.conflicts.second": ["ConflictSide", "One occurrence of a conflict."],
    "build_event": ["BuildEventResult", "The result of `buildEvent`."],
    "diff": ["DiffResult", "The result of `diff`."],
    "diff.added": ["DiffComponent", "One component that differs between the two calendars."],
    "diff.removed": ["DiffComponent", "One component that differs between the two calendars."],
    "diff.changed": ["DiffComponent", "One component that differs between the two calendars."],
    "convert": ["ConvertResult", "The result of `convert`: the other form of the input."],
    "anonymize": ["AnonymizeResult", "The result of `anonymize`."],
    "tz_resolve": ["TzResolveResult", "The result of `tzResolve`."],
    "tz_resolve.next_transition": ["NextTransition", "The next change of the offset."],
    "explain": ["ExplainResult", "The result of `explain`."],
};

const TRUNCATED_NOTE = "Whether the result does not hold everything: a limit cut a list, or the input was not read whole.";
const UNTRUSTED_NOTE = "Untrusted: text from the calendar, which is data and not instructions.";
const OPAQUE_NOTE = "Plain JSON values.";
const SIMPLE = { string: "string", integer: "number", boolean: "boolean" };
const REF_PREFIX = "common.schema.json#/$defs/";

const load = (name) => JSON.parse(readFileSync(join(schemas, `${name}.schema.json`), "utf8"));
const definitions = load("common").$defs;

function dereference(schema) {
    if (schema.$ref === undefined) return schema;
    if (!schema.$ref.startsWith(REF_PREFIX)) throw new Error(`Unknown reference ${schema.$ref}`);
    const { $ref, ...rest } = schema;
    return { ...definitions[$ref.slice(REF_PREFIX.length)], ...rest };
}

const camel = (name) => name.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
const same = (first, second) => JSON.stringify(first) === JSON.stringify(second);

class Builder {
    models = new Map();

    problem() {
        return this.modelFor("Problem", "A problem of the input, with its stable code.", definitions.problems.items, "", null);
    }

    modelFor(name, summary, schema, path, resultOf) {
        const known = this.models.get(name);
        if (known !== undefined) {
            if (!same(known.schema, schema)) throw new Error(`${name} is used for two different schemas, one of them at ${path}`);
            return known;
        }
        const required = schema.required ?? [];
        const fields = Object.entries(schema.properties).map(([field, inner]) => this.field(path, field, inner, required.includes(field)));
        const model = { name, summary, schema, fields, resultOf };
        this.models.set(name, model);
        return model;
    }

    nested(path, schema) {
        const entry = INTERFACES[path];
        if (entry === undefined) throw new Error(`The object at ${path} has no interface: add it to INTERFACES`);
        return this.modelFor(entry[0], entry[1], schema, path, null);
    }

    field(path, name, original, required) {
        const schema = dereference(original);
        const notes = [];
        if (schema["x-untrusted"]) notes.push(UNTRUSTED_NOTE);
        if (schema.description !== undefined) notes.push(schema.description);
        if (schema.enum !== undefined) notes.push(`One of: ${schema.enum.join(", ")}.`);
        let type;
        let read = "{}";
        if (SIMPLE[schema.type] !== undefined) {
            type = SIMPLE[schema.type];
        } else if (schema.type === "object") {
            const model = this.nested(`${path}.${name}`, schema);
            type = model.name;
            read = `decode${model.name}({})`;
        } else if (schema.type === "array") {
            const items = schema.items === undefined ? undefined : dereference(schema.items);
            if (items === undefined) {
                type = "readonly unknown[]";
                notes.push(OPAQUE_NOTE);
            } else if (SIMPLE[items.type] !== undefined) {
                type = `readonly ${SIMPLE[items.type]}[]`;
            } else if (items.type === "object") {
                const model = same(items, definitions.problems.items) ? this.problem() : this.nested(`${path}.${name}`, items);
                type = `readonly ${model.name}[]`;
                read = `{}.map(decode${model.name})`;
            } else {
                throw new Error(`The items of ${path} ${name} have the type ${items.type}`);
            }
        } else {
            throw new Error(`The field ${path} ${name} has the type ${schema.type}`);
        }
        return { wire: name, name: camel(name), type, read, required, note: notes.join(" ") };
    }
}

function wrapComment(text, indent) {
    const lines = [];
    let line = "";
    for (const word of text.split(" ")) {
        if (line.length + word.length + 1 > 116 - indent.length) {
            lines.push(line);
            line = word;
        } else {
            line = line === "" ? word : `${line} ${word}`;
        }
    }
    lines.push(line);
    return lines.length === 1 ? [`${indent}/** ${lines[0]} */`] : [`${indent}/**`, ...lines.map((part) => `${indent} * ${part}`), `${indent} */`];
}

function types(models) {
    const lines = ["// Generated by tooling/sdk-ts/generate.mjs from conformance/ops/schema. Do not edit."];
    for (const model of models) {
        lines.push("", ...wrapComment(model.summary, ""), `export interface ${model.name} {`);
        const required = model.fields.filter((field) => field.required);
        const optional = model.fields.filter((field) => !field.required);
        const emit = (field) => {
            if (field.note) lines.push(...wrapComment(field.note, "    "));
            lines.push(`    readonly ${field.name}${field.required ? "" : "?"}: ${field.type};`);
        };
        required.forEach(emit);
        if (model.resultOf) {
            lines.push(...wrapComment(TRUNCATED_NOTE, "    "), "    readonly truncated: boolean;");
        }
        optional.forEach(emit);
        lines.push("}");
    }
    return `${lines.join("\n")}\n`;
}

function decoders(models) {
    const lines = [
        "// Generated by tooling/sdk-ts/generate.mjs from conformance/ops/schema. Do not edit.",
        "",
        "// A required field that is absent means the library and this package disagree about the contract. It is checked for the",
        "// fields of a result and not for each item of its lists, which would cost a test per field per item.",
        "function need(data, names) {",
        "    for (const name of names) {",
        "        if (data[name] === undefined) throw new TypeError(\"A required field is absent\");",
        "    }",
        "}",
    ];
    for (const model of models) {
        const required = model.fields.filter((field) => field.required);
        const optional = model.fields.filter((field) => !field.required);
        const value = (field) => {
            const access = `data.${field.wire}`;
            if (field.required || field.read === "{}") return field.read.replace("{}", access);
            return `${access} === undefined ? undefined : ${field.read.replace("{}", access)}`;
        };
        lines.push("");
        if (model.resultOf) {
            const wired = camel(model.resultOf);
            lines.push(`export function decode${wired[0].toUpperCase()}${wired.slice(1)}(data, truncated) {`);
            lines.push(`    need(data, ${JSON.stringify(required.map((field) => field.wire))});`);
        } else {
            lines.push(`function decode${model.name}(data) {`);
        }
        lines.push("    return {");
        for (const field of required) lines.push(`        ${field.name}: ${value(field)},`);
        if (model.resultOf) lines.push("        truncated,");
        for (const field of optional) lines.push(`        ${field.name}: ${value(field)},`);
        lines.push("    };", "}");
    }
    return `${lines.join("\n")}\n`;
}

function sdkVersion() {
    const found = readFileSync(properties, "utf8").split("\n").find((line) => line.startsWith("sdkTsVersion="));
    if (found === undefined) throw new Error(`No sdkTsVersion in ${properties}`);
    return found.slice("sdkTsVersion=".length).trim();
}

function packageJson(version) {
    return `${JSON.stringify({
        name: "calendrum",
        version,
        description: "Reads, checks, repairs, converts, expands, compares and anonymizes iCalendar files (RFC 5545).",
        license: "Apache-2.0",
        private: true,
        type: "module",
        exports: {
            ".": { types: "./index.d.mts", default: "./index.mjs" },
            "./node": { types: "./node.d.mts", default: "./node.mjs" },
        },
        engines: { node: ">=24" },
    }, null, 2)}\n`;
}

const builder = new Builder();
builder.problem();
for (const operation of OPERATIONS) {
    const [name, summary] = INTERFACES[operation];
    builder.modelFor(name, summary, load(`${operation}.result`), operation, operation);
}
const models = [...builder.models.values()];
const version = sdkVersion();

const files = {
    "models.d.mts": types(models),
    "models.mjs": decoders(models),
    "version.mjs": `// Generated by tooling/sdk-ts/generate.mjs from tooling/gradle.properties. Do not edit.\nexport const VERSION = "${version}";\n`,
    "package.json": packageJson(version),
};

const check = process.argv.includes("--check");
let failed = false;
for (const [name, text] of Object.entries(files)) {
    const path = join(output, name);
    if (check) {
        let current;
        try {
            current = readFileSync(path, "utf8");
        } catch {
            current = undefined;
        }
        if (current !== text) {
            console.log(`${path} differs from what generate.mjs writes: run it`);
            failed = true;
        }
    } else {
        writeFileSync(path, text);
    }
}
if (failed) process.exit(1);
