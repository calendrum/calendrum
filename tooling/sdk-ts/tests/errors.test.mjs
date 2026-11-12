// The errors: each code has its class, none of them carries the input, and a truncated result does not pass unseen.
import assert from "node:assert/strict";
import { writeFileSync, mkdtempSync, rmSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { test } from "node:test";
import { pathToFileURL } from "node:url";
import { inspect as show } from "node:util";
import { ICS, PACKAGE, WINDOW, calendrum, moduleOf, runProgram } from "./support.mjs";

const { errorFor } = await moduleOf("errors.mjs");
const { readResponse, toModel } = await moduleOf("operations.mjs");
const models = await moduleOf("models.mjs");
const MARKER = "ZZMARKZZ";

// Everything an error shows or holds, along its whole chain.
function carried(error) {
    const parts = [];
    for (let seen = error; seen !== undefined && seen !== null; seen = seen.cause) {
        parts.push(String(seen), seen.message, seen.stack, show(seen, { depth: 5 }), JSON.stringify(seen));
    }
    return parts.join("\n");
}

test("every code has its class", () => {
    for (const [code, type] of [
        ["OPS-BAD-ARGUMENT", calendrum.BadArgumentError],
        ["OPS-UNKNOWN-OPERATION", calendrum.UnknownOperationError],
        ["OPS-UNKNOWN-ZONE", calendrum.UnknownZoneError],
        ["OPS-UNKNOWN-CODE", calendrum.UnknownCodeError],
        ["OPS-TOO-LARGE", calendrum.TooLargeError],
        ["OPS-INTERNAL", calendrum.InternalError],
    ]) {
        const error = errorFor(code, "text");
        assert.equal(error.constructor, type);
        assert.ok(error instanceof calendrum.CalendrumError);
        assert.ok(error instanceof Error);
        assert.equal(error.code, code);
        assert.equal(error.message, "text");
        assert.equal(error.name, type.name);
    }
});

test("a code this version does not know gets the base class", () => {
    assert.equal(errorFor("OPS-NEW", "text").constructor, calendrum.CalendrumError);
});

test("the real library gives these classes", () => {
    assert.throws(() => calendrum.expand(ICS, { from: "x", to: "y" }), calendrum.BadArgumentError);
    assert.throws(() => calendrum.call("nope", {}), calendrum.UnknownOperationError);
    assert.throws(() => calendrum.tzResolve("Nowhere/Nothing"), calendrum.UnknownZoneError);
    assert.throws(() => calendrum.explain("ICAL-NOPE"), calendrum.UnknownCodeError);
    assert.throws(() => calendrum.inspect(new Uint8Array(64 * 1024 * 1024 + 1)), calendrum.TooLargeError);
});

test("a library error is not an error of an operation", () => {
    assert.ok(!(new calendrum.LibraryError("x") instanceof calendrum.CalendrumError));
    assert.ok(new calendrum.WorkerError("timeout", "x") instanceof calendrum.LibraryError);
    assert.equal(new calendrum.WorkerError("timeout", "x").reason, "timeout");
});

test("the input is not in any error", () => {
    const circular = {};
    circular[MARKER] = circular;
    const attempts = {
        rule: () => calendrum.rruleExpand(MARKER, "2026-01-01T00:00:00Z"),
        start: () => calendrum.rruleExpand("FREQ=DAILY", MARKER),
        zone: () => calendrum.tzResolve(MARKER),
        code: () => calendrum.explain(MARKER),
        operation: () => calendrum.call(MARKER, {}),
        window: () => calendrum.expand(ICS, { from: MARKER, to: MARKER }),
        rules: () => calendrum.validate(ICS, { rules: MARKER }),
        fixup: () => calendrum.fix(ICS, { fixups: [MARKER] }),
        jcal: () => calendrum.convert({ jcal: MARKER }),
        event: () => calendrum.buildEvent(MARKER, MARKER),
        "type of a calendar": () => calendrum.inspect({ [MARKER]: 1 }),
        "name of an option": () => calendrum.expand(ICS, { [MARKER]: MARKER }),
        "type of a request": () => calendrum.call("explain", [MARKER]),
        "field of a request": () => calendrum.call("explain", { code: 5, [MARKER]: MARKER }),
        "a request that cannot be JSON": () => calendrum.call("explain", circular),
        "a value that cannot be JSON": () => calendrum.call("explain", { [MARKER]: 1n }),
    };
    for (const [name, attempt] of Object.entries(attempts)) {
        let caught;
        try {
            attempt();
        } catch (error) {
            caught = error;
        }
        assert.ok(caught, `${name} has to fail`);
        assert.ok(!carried(caught).includes(MARKER), `${name}: ${carried(caught)}`);
    }
});

test("what JSON would change in silence is refused", () => {
    for (const attempt of [
        () => calendrum.anonymize(ICS, { shiftWeeks: Number.NaN }),
        () => calendrum.expand(ICS, { ...WINDOW, limit: Number.POSITIVE_INFINITY }),
        () => calendrum.inspect("BEGIN:VCALENDAR\r\nSUMMARY:a\ud800b\r\nEND:VCALENDAR\r\n"),
        () => calendrum.call("explain", { code: "\ud800" }),
    ]) {
        assert.throws(attempt, calendrum.BadArgumentError);
    }
});

test("bytes above the limit are refused before any copy", () => {
    assert.throws(() => calendrum.inspect(new Uint8Array(400 * 1024 * 1024)), calendrum.TooLargeError);
});

test("a request nested too deeply is a bad argument", () => {
    for (const depth of [100, 100_000]) {
        let nested = [];
        for (let index = 0; index < depth; index += 1) nested = [nested];
        assert.throws(() => calendrum.convert({ jcal: nested }), calendrum.BadArgumentError);
    }
});

test("an unknown option names the ones there are", () => {
    assert.throws(() => calendrum.expand(ICS, { ...WINDOW, includeCanceled: true }), /includeCancelled/);
    assert.throws(() => calendrum.inspect(ICS, "lint"), TypeError);
    assert.throws(() => calendrum.inspect(ICS, null), TypeError);
    assert.throws(() => calendrum.inspect(ICS, { __proto__: { limit: 1 }, toString: 1 }), TypeError);
});

test("a response outside the contract fails clearly, without its text", () => {
    for (const [name, text] of [
        ["not JSON", `not json ${MARKER}`],
        ["an array", JSON.stringify([MARKER])],
        ["null", "null"],
        ["no result", JSON.stringify({ ok: true, [MARKER]: 1 })],
        ["a result of the wrong type", JSON.stringify({ ok: true, result: MARKER })],
        ["no error", JSON.stringify({ ok: false, [MARKER]: 1 })],
    ]) {
        let caught;
        try {
            readResponse(text);
        } catch (error) {
            caught = error;
        }
        assert.ok(caught instanceof calendrum.LibraryError, name);
        assert.ok(!carried(caught).includes(MARKER), name);
        assert.equal(caught.cause, undefined, name);
    }
    let caught;
    try {
        toModel(models.decodeExplain, { [MARKER]: 1 }, false);
    } catch (error) {
        caught = error;
    }
    assert.ok(caught instanceof calendrum.LibraryError);
    assert.ok(!carried(caught).includes(MARKER));
});

test("a truncated result says so, and the warning comes once in a process", async () => {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    try {
        const program = join(directory, "program.mjs");
        writeFileSync(program, `
            import { expand, call } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "index.mjs")).href)};
            const ICS = ${JSON.stringify(ICS)};
            const WINDOW = ${JSON.stringify(WINDOW)};
            const warnings = [];
            process.on("warning", (warning) => warnings.push(warning.code));
            const first = expand(ICS, { ...WINDOW, limit: 1 });
            const second = expand(ICS, { ...WINDOW, limit: 1 });
            const whole = expand(ICS, WINDOW);
            const raw = call("expand", { ics: ICS, ...WINDOW, limit: 1 });
            setImmediate(() => console.log(JSON.stringify({
                first: first.truncated, second: second.truncated, whole: whole.truncated, raw: raw.truncated, rawKeys: Object.keys(raw).includes("truncated"),
                json: JSON.stringify(raw).includes("truncated"), warnings,
            })));
        `);
        const { code, stdout, stderr } = await runProgram([program], { limit: 60_000 });
        assert.equal(code, 0, stderr);
        assert.deepEqual(JSON.parse(stdout), {
            first: true,
            second: true,
            whole: false,
            raw: true,
            rawKeys: false,
            json: false,
            warnings: ["CALENDRUM_TRUNCATED"],
        });
        assert.ok(!stderr.includes("Plain"), "no text of the calendar in a warning");
    } finally {
        rmSync(directory, { recursive: true });
    }
});
