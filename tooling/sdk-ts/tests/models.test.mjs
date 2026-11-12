// The models: nothing of a response is lost, new fields are ignored, and untrusted fields are marked in the types.
import assert from "node:assert/strict";
import { spawnSync } from "node:child_process";
import { readFileSync } from "node:fs";
import { dirname, join } from "node:path";
import { test } from "node:test";
import { fileURLToPath } from "node:url";
import { ICS, OPERATIONS, PACKAGE, SCHEMAS, WINDOW, allCases, calendrum, camel, loadJson, moduleOf, toPlain } from "./support.mjs";

const models = await moduleOf("models.mjs");
const here = dirname(fileURLToPath(import.meta.url));
const decoderOf = (operation) => {
    const name = camel(operation);
    return models[`decode${name[0].toUpperCase()}${name.slice(1)}`];
};
const quietly = (action) => {
    const before = process.listenerCount("warning");
    assert.equal(typeof before, "number");
    return action();
};

test("no field of a response is lost", () => {
    let compared = 0;
    for (const testCase of allCases()) {
        let live;
        try {
            live = quietly(() => calendrum.call(testCase.operation, testCase.request));
        } catch (error) {
            assert.ok(error instanceof calendrum.CalendrumError, testCase.id);
            assert.equal(testCase.expected.ok, false, testCase.id);
            continue;
        }
        const decode = decoderOf(testCase.operation);
        assert.deepEqual(toPlain(decode(live, live.truncated)), { ...live }, `${testCase.id}: the answer of the library`);
        const { result } = testCase.expected;
        assert.deepEqual(toPlain(decode(result, false)), result, `${testCase.id}: the expected.json`);
        compared += 1;
    }
    assert.ok(compared >= 40, String(compared));
});

test("a field this version does not know is ignored", () => {
    const result = models.decodeExplain({ code: "ICAL-S003", severity: "error", constant: "X", meaning: "Y", new_field: { a: 1 } }, false);
    assert.equal(result.code, "ICAL-S003");
    assert.equal("newField" in result, false);
    assert.equal("new_field" in result, false);
    const nested = models.decodeExpand({
        occurrences: [{ calendar_index: 0, component: "VEVENT", start: "s", end: "e", all_day: false, added: 1 }],
        count: 1,
        problems: [],
        problem_count: 0,
        other: [],
    }, false);
    assert.equal(nested.occurrences[0].start, "s");
    assert.equal("added" in nested.occurrences[0], false);
});

test("a required field that is absent is refused", () => {
    assert.throws(() => models.decodeExplain({ code: "ICAL-S003" }, false), TypeError);
});

test("names are camelCase and values are what the contract gives", () => {
    const result = calendrum.expand(ICS, WINDOW);
    assert.equal(result.problemCount, 0);
    assert.equal(result.occurrences[0].calendarIndex, 0);
    assert.equal(result.occurrences[0].allDay, false);
    assert.equal(result.occurrences[0].start, "2026-01-05T10:00:00Z");
    const zone = calendrum.tzResolve("Europe/Berlin", { at: "2026-01-01T00:00:00Z" });
    assert.equal(zone.offsetSeconds, 3600);
    assert.equal(typeof zone.nextTransition.offsetSecondsAfter, "number");
});

test("floating times are read in the zone of the request, from the data of the package", () => {
    const floating = ICS.replace("DTSTART:20260105T100000Z", "DTSTART:20260705T100000").replace("DTEND:20260105T110000Z", "DTEND:20260705T110000");
    const wide = { from: "2026-01-01T00:00:00Z", to: "2027-01-01T00:00:00Z" };
    assert.equal(calendrum.expand(floating, wide).occurrences[0].start, "2026-07-05T10:00:00Z");
    const berlin = calendrum.expand(floating, { ...wide, zone: "Europe/Berlin" }).occurrences[0];
    assert.equal(berlin.start, "2026-07-05T08:00:00Z");
    assert.equal(berlin.localStart, "2026-07-05T10:00:00");
    assert.equal(calendrum.freebusy(floating, { ...wide, zone: "Asia/Tokyo" }).busy[0].start, "2026-07-05T01:00:00Z");
    const allDay = ICS.replace("DTSTART:20260105T100000Z", "DTSTART;VALUE=DATE:20260705").replace("DTEND:20260105T110000Z\r\n", "");
    assert.equal(calendrum.expand(allDay, { ...wide, zone: "Asia/Tokyo" }).occurrences[0].start, "2026-07-04T15:00:00Z");
});

test("a field that is absent is undefined", () => {
    const result = calendrum.inspect(ICS);
    assert.equal(result.jcal, undefined);
    assert.equal(result.calendars[0].method, undefined);
    assert.equal(result.calendars[0].events[0].status, undefined);
    assert.equal(JSON.stringify(result).includes("status"), false, "and it does not reach JSON");
});

function interfacesOf(text) {
    const found = new Map();
    for (const match of text.matchAll(/export interface (\w+) \{([\s\S]*?)\n\}/g)) {
        const fields = new Map();
        let comment = "";
        for (const line of match[2].split("\n")) {
            const field = /^\s+readonly (\w+)\??: (.+);$/.exec(line);
            if (field) {
                fields.set(field[1], { comment, type: field[2] });
                comment = "";
            } else {
                comment += line;
            }
        }
        found.set(match[1], fields);
    }
    return found;
}

test("untrusted fields are marked in the types, down to the nested ones", () => {
    const interfaces = interfacesOf(readFileSync(join(PACKAGE, "models.d.mts"), "utf8"));
    const definitions = loadJson(join(SCHEMAS, "common.schema.json")).$defs;
    const resolve = (schema) => (schema.$ref === undefined ? schema : { ...definitions[schema.$ref.split("/").pop()], ...schema, $ref: undefined });
    let checked = 0;
    const walk = (schema, owner, path) => {
        for (const [name, original] of Object.entries(schema.properties ?? {})) {
            const field = resolve(original);
            const declared = interfaces.get(owner).get(camel(name));
            assert.ok(declared, `${path}.${name}`);
            if (field["x-untrusted"]) {
                assert.match(declared.comment, /Untrusted/, `${path}.${name}`);
                checked += 1;
            }
            const inner = field.items === undefined ? field : resolve(field.items);
            if (inner.properties !== undefined) {
                walk(inner, declared.type.replace("readonly ", "").replace("[]", ""), `${path}.${name}`);
            }
        }
    };
    const resultNames = {
        inspect: "InspectResult", validate: "ValidateResult", fix: "FixResult", expand: "ExpandResult", rrule_expand: "RruleExpandResult",
        freebusy: "FreebusyResult", build_event: "BuildEventResult", diff: "DiffResult", convert: "ConvertResult", anonymize: "AnonymizeResult",
        tz_resolve: "TzResolveResult", explain: "ExplainResult",
    };
    assert.deepEqual(Object.keys(resultNames), OPERATIONS);
    for (const [operation, name] of Object.entries(resultNames)) {
        walk(loadJson(join(SCHEMAS, `${operation}.result.schema.json`)), name, operation);
        assert.ok(interfaces.get(name).has("truncated"), `${name} has truncated`);
    }
    assert.ok(checked > 25, String(checked));
});

test("the generated files are what the script writes", () => {
    const done = spawnSync(process.execPath, [join(here, "..", "generate.mjs"), "--check"], { encoding: "utf8", timeout: 60_000 });
    assert.equal(done.status, 0, done.stdout + done.stderr);
});

test("versions", () => {
    const found = calendrum.versions();
    assert.equal(found.package, loadJson(join(PACKAGE, "package.json")).version);
    assert.equal(found.contract, 1);
    assert.ok(found.library);
    assert.equal(found.sdk, found.package);
});
