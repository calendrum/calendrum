// All the cases of the contract through the package, by the rules of conformance/ops/README.md.
//
// A case goes through the function of its operation where its arguments can say the request, and always through `call` as well. The
// library has a real clock and real random bytes, so the cases that depend on them are checked by what they mean.
import assert from "node:assert/strict";
import { test } from "node:test";
import { OPERATIONS, SCHEMAS, allCases, calendrum, camel, comparable, loadJson, toPlain } from "./support.mjs";

const NEEDS_CLOCK_OR_RANDOM = new Set(["anonymize-shift", "anonymize-text-goes", "build-event-all-day-generated-uid", "tz-resolve-default-moment"]);
const CALENDAR_OPERATIONS = new Set(["inspect", "validate", "fix", "expand", "freebusy", "anonymize"]);
const POSITIONAL = { rrule_expand: ["rrule", "start"], build_event: ["summary", "start"], tz_resolve: ["name"], explain: ["code"] };
const REQUIRED_OPTIONS = { expand: ["from", "to"], freebusy: ["from", "to"] };
const cases = allCases();

function takeCalendar(request, field) {
    if (request[field] !== undefined) {
        const value = request[field];
        delete request[field];
        return value;
    }
    const encoded = request[`${field}_base64`];
    if (encoded === undefined) return undefined;
    delete request[`${field}_base64`];
    return new Uint8Array(Buffer.from(encoded, "base64"));
}

// The arguments of the function for a request, or null where its signature cannot say it.
function functionArguments(operation, original) {
    const request = { ...original };
    const positional = [];
    if (CALENDAR_OPERATIONS.has(operation)) {
        const calendar = takeCalendar(request, "ics");
        if (calendar === undefined) return null;
        positional.push(calendar);
    } else if (operation === "diff") {
        const before = takeCalendar(request, "before");
        const after = takeCalendar(request, "after");
        if (before === undefined || after === undefined) return null;
        positional.push(before, after);
    } else if (operation === "convert") {
        const calendar = takeCalendar(request, "ics");
        if (calendar !== undefined && request.jcal !== undefined) return null;
        positional.push(calendar ?? { jcal: request.jcal });
        return positional;
    } else {
        for (const name of POSITIONAL[operation]) {
            if (request[name] === undefined) return null;
            positional.push(request[name]);
            delete request[name];
        }
    }
    for (const name of REQUIRED_OPTIONS[operation] ?? []) {
        if (request[name] === undefined) return null;
    }
    const names = Object.keys(request);
    if (names.length > 0) positional.push(Object.fromEntries(names.map((name) => [camel(name), request[name]])));
    return positional;
}

function runFunction(testCase) {
    const args = functionArguments(testCase.operation, testCase.request);
    if (args === null) return null;
    try {
        const result = calendrum[camel(testCase.operation)](...args);
        return { ok: true, result: toPlain(result), truncated: result.truncated };
    } catch (error) {
        if (!(error instanceof calendrum.CalendrumError)) throw error;
        return { ok: false, error: { code: error.code, message: error.message }, type: error.constructor };
    }
}

function runCall(testCase) {
    try {
        const result = calendrum.call(testCase.operation, testCase.request);
        return { ok: true, result, truncated: result.truncated };
    } catch (error) {
        if (!(error instanceof calendrum.CalendrumError)) throw error;
        return { ok: false, error: { code: error.code, message: error.message }, type: error.constructor };
    }
}

function shape(ics) {
    return ics.split("\r\n").map((line) => line.split(":")[0].split(";")[0]);
}

function checkByMeaning(testCase, result) {
    const expected = testCase.expected.result;
    if (testCase.operation === "anonymize") {
        for (const secret of ["Salary", "Alice", "boss@example.com"]) assert.ok(!result.ics.includes(secret), secret);
        assert.equal(result.shift_weeks, expected.shift_weeks);
        assert.deepEqual(shape(result.ics), shape(expected.ics));
    } else if (testCase.operation === "build_event") {
        assert.match(result.uid, /^[0-9a-f]{32}@calendrum$/);
        const stamp = /DTSTAMP:(\d{8}T\d{6})Z/.exec(result.ics)[1];
        const moment = Date.parse(`${stamp.slice(0, 4)}-${stamp.slice(4, 6)}-${stamp.slice(6, 8)}T${stamp.slice(9, 11)}:${stamp.slice(11, 13)}:${stamp.slice(13, 15)}Z`);
        assert.ok(Math.abs(Date.now() - moment) < 120_000);
        assert.equal(result.ics.replaceAll(result.uid, expected.uid).replaceAll(stamp, "20260101T000000"), expected.ics);
        assert.equal(calendrum.validate(result.ics).valid, true);
    } else if (testCase.operation === "tz_resolve") {
        assert.equal(result.id, expected.id);
        assert.equal(result.offset_seconds, expected.offset_seconds);
    } else {
        assert.fail(testCase.id);
    }
}

function compare(testCase, answer) {
    const { expected } = testCase;
    assert.equal(answer.ok, expected.ok, JSON.stringify(answer));
    if (!expected.ok) {
        assert.equal(answer.error.code, expected.error.code);
        return;
    }
    assert.equal(answer.truncated, expected.truncated ?? false);
    if (NEEDS_CLOCK_OR_RANDOM.has(testCase.id)) {
        checkByMeaning(testCase, answer.result);
    } else {
        assert.deepEqual(comparable({ ...answer.result }, testCase.ignored), comparable(expected.result, testCase.ignored));
    }
}

test("every case through its function", () => {
    const onlyCall = [];
    let through = 0;
    for (const testCase of cases) {
        const answer = runFunction(testCase);
        if (answer === null) {
            onlyCall.push(testCase.id);
            continue;
        }
        try {
            compare(testCase, answer);
        } catch (error) {
            error.message = `${testCase.id}: ${error.message}`;
            throw error;
        }
        through += 1;
    }
    console.log(`contract: ${cases.length} cases; ${through} through the functions, by meaning: ${[...NEEDS_CLOCK_OR_RANDOM]}; `
        + `only through call: ${onlyCall}`);
    assert.deepEqual(new Set(onlyCall), new Set(["convert-both", "diff-no-after", "explain-missing", "inspect-no-input"]));
});

test("every case through call", () => {
    for (const testCase of cases) {
        try {
            compare(testCase, runCall(testCase));
        } catch (error) {
            error.message = `${testCase.id}: ${error.message}`;
            throw error;
        }
    }
});

test("there is a case for each operation", () => {
    assert.deepEqual(new Set(cases.map((item) => item.operation)), new Set(OPERATIONS));
    assert.equal(cases.length, 60);
});

test("an error comes with its class", () => {
    const classes = {
        "OPS-BAD-ARGUMENT": calendrum.BadArgumentError,
        "OPS-UNKNOWN-ZONE": calendrum.UnknownZoneError,
        "OPS-UNKNOWN-CODE": calendrum.UnknownCodeError,
    };
    const seen = new Set();
    for (const testCase of cases) {
        if (testCase.expected.ok) continue;
        const { code } = testCase.expected.error;
        assert.equal(runCall(testCase).type, classes[code], testCase.id);
        seen.add(code);
    }
    assert.deepEqual(seen, new Set(Object.keys(classes)));
});

test("the functions take every field of the requests", async () => {
    const { readFileSync } = await import("node:fs");
    const { join } = await import("node:path");
    const { PACKAGE } = await import("./support.mjs");
    const types = readFileSync(join(PACKAGE, "index.d.mts"), "utf8");
    const options = {
        inspect: "InspectOptions",
        validate: "ValidateOptions",
        fix: "FixOptions",
        expand: "ExpandOptions",
        rrule_expand: "RruleExpandOptions",
        freebusy: "FreebusyOptions",
        build_event: "BuildEventOptions",
        diff: "DiffOptions",
        anonymize: "AnonymizeOptions",
        tz_resolve: "TzResolveOptions",
    };
    const fieldsOf = (name) => {
        const body = new RegExp(`export interface ${name}(?: extends (\\w+))? \\{([\\s\\S]*?)\\n\\}`).exec(types);
        assert.ok(body, name);
        const own = [...body[2].matchAll(/readonly (\w+)\??:/g)].map((match) => match[1]);
        return body[1] === undefined ? own : [...own, ...fieldsOf(body[1])];
    };
    const positional = { rrule_expand: ["rrule", "start"], build_event: ["summary", "start"], tz_resolve: ["name"] };
    for (const [operation, name] of Object.entries(options)) {
        const schema = loadJson(join(SCHEMAS, `${operation}.request.schema.json`));
        const wanted = Object.keys(schema.properties)
            .filter((field) => !field.endsWith("_base64") && !["ics", "before", "after", ...(positional[operation] ?? [])].includes(field))
            .map(camel);
        assert.deepEqual(new Set(fieldsOf(name)), new Set(wanted), operation);
    }
});
