// What a calendar can be on the way in, that a string is never a path, and how a file is read.
import assert from "node:assert/strict";
import { execFileSync } from "node:child_process";
import { mkdirSync, mkdtempSync, openSync, closeSync, ftruncateSync, readdirSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { test } from "node:test";
import { pathToFileURL } from "node:url";
import { ICS, PACKAGE, WINDOW, allCases, calendrum, nodePart, runProgram } from "./support.mjs";

const { readCalendar } = nodePart;
const encoder = new TextEncoder();

function withDirectory(action) {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    const finish = () => rmSync(directory, { recursive: true, force: true });
    try {
        const result = action(directory);
        if (result instanceof Promise) return result.finally(finish);
        finish();
        return result;
    } catch (error) {
        finish();
        throw error;
    }
}

const sparse = (path, size) => {
    const descriptor = openSync(path, "w");
    ftruncateSync(descriptor, size);
    closeSync(descriptor);
};

// A calendar whose line is above the limit for files from strangers: 64 KiB. Only the tail differs.
const withLongLine = (tail) => ICS.replace("SUMMARY:Plain", `DESCRIPTION:${"x".repeat(70000)}${tail}`);

test("a change beyond the limits is not silent", () => {
    const result = calendrum.diff(withLongLine("A"), withLongLine("B"));
    assert.equal(result.truncated, true, "no fingerprint sees that line, so the answer cannot be complete");
    assert.ok(result.problems.some((problem) => problem.code === "ICAL-S010"));
});

test("what anonymize could not read is not silent", () => {
    const result = calendrum.anonymize(withLongLine("A"));
    assert.equal(result.truncated, true);
    assert.ok(result.problems.some((problem) => problem.code === "ICAL-S010"));
    assert.ok(!result.ics.includes("x".repeat(100)));
});

test("ordinary calendars are not truncated", () => {
    assert.equal(calendrum.diff(ICS, ICS).truncated, false);
    assert.equal(calendrum.anonymize(ICS).problemCount, 0);
});

test("a line that cannot be read makes a calendar invalid", () => {
    assert.equal(calendrum.validate(withLongLine("A")).valid, false);
});

test("a number that a double cannot hold stays as text in jCal", () => {
    for (const [line, digits] of [[`GEO:${"1".repeat(5000)};2`, "1".repeat(5000)], [`X-F;VALUE=FLOAT:${"2".repeat(5000)}`, "2".repeat(5000)],
        ["X-G;VALUE=FLOAT:12345678901234567890", "12345678901234567890"]]) {
        const calendar = ICS.replace("SUMMARY:Plain", line);
        const { jcal } = calendrum.convert(calendar);
        assert.ok(JSON.stringify(jcal).includes(digits), "the digits are all there");
        assert.ok(calendrum.convert({ jcal }).ics.replaceAll("\r\n ", "").includes(digits), "and come back");
    }
});

test("an ordinary number stays a number in jCal", () => {
    const { jcal } = calendrum.convert(ICS.replace("SUMMARY:Plain", "GEO:37.386013;-122.082932"));
    assert.ok(JSON.stringify(jcal).includes("[37.386013,-122.082932]"));
});

test("a string, bytes and a Buffer read the same", () => {
    const text = calendrum.inspect(ICS);
    assert.deepEqual(calendrum.inspect(encoder.encode(ICS)), text);
    assert.deepEqual(calendrum.inspect(Buffer.from(ICS)), text);
});

test("a view into a larger buffer is only the view", () => {
    const bytes = encoder.encode(`xxxxx${ICS}yyyyy`);
    assert.deepEqual(calendrum.inspect(bytes.subarray(5, bytes.length - 5)), calendrum.inspect(ICS));
});

test("bytes keep their encoding", () => {
    const testCase = allCases().find((item) => item.id === "inspect-windows-1251");
    const bytes = new Uint8Array(Buffer.from(testCase.request.ics_base64, "base64"));
    assert.ok(bytes.some((byte) => byte >= 0x80), "the case has bytes outside ASCII");
    const { charset, calendars } = testCase.expected.result;
    const result = calendrum.inspect(bytes);
    assert.equal(result.charset, charset);
    assert.equal(result.calendars[0].events[0].summary, calendars[0].events[0].summary);
});

test("a string is never a path", () => withDirectory((directory) => {
    const path = join(directory, "event.ics");
    writeFileSync(path, ICS);
    for (const text of [path, "event.ics", "/etc/hosts"]) assert.deepEqual(calendrum.inspect(text).calendars, [], text);
}));

test("other types are refused", () => {
    for (const value of [undefined, null, 5, ["BEGIN:VCALENDAR"], new ArrayBuffer(4), new Uint16Array(2), new URL("file:///x.ics")]) {
        assert.throws(() => calendrum.inspect(value), TypeError);
    }
    assert.throws(() => calendrum.convert(5), TypeError);
    assert.throws(() => calendrum.diff(ICS, 5), TypeError);
});

test("each operation with a calendar takes bytes", () => {
    const bytes = encoder.encode(ICS);
    assert.equal(calendrum.validate(bytes).valid, true);
    assert.equal(calendrum.fix(bytes).ics, calendrum.fix(ICS).ics);
    assert.equal(calendrum.expand(bytes, WINDOW).count, 3);
    assert.deepEqual(calendrum.freebusy(bytes, WINDOW), calendrum.freebusy(ICS, WINDOW));
    assert.equal(calendrum.diff(bytes, ICS).changed.length, 0);
    assert.equal(calendrum.diff(ICS, bytes).changed.length, 0);
    assert.deepEqual(calendrum.convert(bytes).jcal, calendrum.convert(ICS).jcal);
    assert.ok(calendrum.anonymize(bytes).ics.includes("BEGIN:VCALENDAR"));
});

test("readCalendar reads a file as bytes", () => withDirectory((directory) => {
    const path = join(directory, "event.ics");
    writeFileSync(path, ICS);
    const bytes = readCalendar(path);
    assert.ok(bytes instanceof Uint8Array);
    assert.deepEqual(calendrum.inspect(bytes), calendrum.inspect(ICS));
    assert.deepEqual(readCalendar(pathToFileURL(path)), bytes);
    writeFileSync(path, "");
    assert.equal(readCalendar(path).length, 0);
    writeFileSync(path, ICS);
    assert.equal(readCalendar(path).buffer.byteLength, ICS.length, "no tail of zeros behind the view");
}));

test("readCalendar refuses what is not a regular file", () => withDirectory((directory) => {
    assert.throws(() => readCalendar(join(directory, "missing.ics")), { code: "ENOENT" });
    assert.throws(() => readCalendar(directory), calendrum.BadArgumentError);
    assert.throws(() => readCalendar("/dev/zero"), calendrum.BadArgumentError);
}));

test("readCalendar does not wait for the writer of a FIFO", () => withDirectory(async (directory) => {
    const path = join(directory, "pipe.ics");
    execFileSync("mkfifo", [path]);
    const program = join(directory, "program.mjs");
    writeFileSync(program, `
        import { readCalendar } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
        try { readCalendar(${JSON.stringify(path)}); console.log("read"); } catch (error) { console.log(error.code); }
    `);
    const { stdout } = await runProgram([program], { limit: 30_000 });
    assert.equal(stdout.trim(), "OPS-BAD-ARGUMENT");
}));

test("readCalendar refuses a file above the limit without reading it, and reads one at the limit", () => withDirectory((directory) => {
    const above = join(directory, "above.ics");
    sparse(above, 64 * 1024 * 1024 + 1);
    assert.throws(() => readCalendar(above), calendrum.TooLargeError);
    const at = join(directory, "at.ics");
    sparse(at, 64 * 1024 * 1024);
    assert.equal(readCalendar(at).length, 64 * 1024 * 1024);
}));

test("readCalendar closes the file", () => withDirectory((directory) => {
    const path = join(directory, "event.ics");
    writeFileSync(path, ICS);
    mkdirSync(join(directory, "folder"));
    const before = readdirSync("/dev/fd").length;
    for (let index = 0; index < 50; index += 1) {
        readCalendar(path);
        assert.throws(() => readCalendar(join(directory, "folder")));
    }
    assert.equal(readdirSync("/dev/fd").length, before);
}));

test("bytes work without a Buffer of Node", () => withDirectory(async (directory) => {
    const program = join(directory, "program.mjs");
    writeFileSync(program, `
        const { inspect } = await import(${JSON.stringify(pathToFileURL(join(PACKAGE, "index.mjs")).href)});
        const ICS = ${JSON.stringify(ICS)};
        const event = ICS.slice(ICS.indexOf("BEGIN:VEVENT"), ICS.indexOf("END:VCALENDAR"));
        // Across the size of a chunk of the code without a Buffer, so that the pieces have to join up.
        const large = ICS.replace("END:VCALENDAR", event.repeat(1500) + "END:VCALENDAR");
        const bytes = new TextEncoder().encode(large);
        const wanted = inspect(large);
        globalThis.Buffer = undefined;
        Uint8Array.prototype.toBase64 = undefined;
        const got = inspect(bytes);
        console.log(bytes.length > 3 * 0x8000 && JSON.stringify(got) === JSON.stringify(wanted) ? "same" : "different");
    `);
    const { stdout, stderr } = await runProgram([program], { limit: 60_000 });
    assert.equal(stdout.trim(), "same", stderr);
}));
