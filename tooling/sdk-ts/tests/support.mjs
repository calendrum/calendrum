// What the tests share. The environment gives the paths: CALENDRUM_PACKAGE, CALENDRUM_CASES and CALENDRUM_GUIDE.
import { spawn } from "node:child_process";
import { existsSync, readFileSync, readdirSync } from "node:fs";
import { join } from "node:path";
import { pathToFileURL } from "node:url";

export const PACKAGE = process.env.CALENDRUM_PACKAGE;
export const CASES = process.env.CALENDRUM_CASES;
export const GUIDE = process.env.CALENDRUM_GUIDE;
export const SCHEMAS = join(CASES, "schema");

export const moduleOf = (name) => import(pathToFileURL(join(PACKAGE, name)).href);
export const calendrum = await moduleOf("index.mjs");
export const nodePart = await moduleOf("node.mjs");

export const OPERATIONS = ["inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff", "convert", "anonymize",
    "tz_resolve", "explain"];

export const ICS = "BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//test//EN\r\nBEGIN:VEVENT\r\nUID:a@example.com\r\n"
    + "DTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\nDTEND:20260105T110000Z\r\nSUMMARY:Plain\r\n"
    + "RRULE:FREQ=DAILY;COUNT=3\r\nEND:VEVENT\r\nEND:VCALENDAR\r\n";
export const WINDOW = { from: "2026-01-01T00:00:00Z", to: "2026-02-01T00:00:00Z" };

export const loadJson = (path) => JSON.parse(readFileSync(path, "utf8"));
export const camel = (name) => name.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
export const wire = (name) => name.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`);

export function allCases() {
    const found = [];
    for (const name of readdirSync(CASES).sort()) {
        const directory = join(CASES, name);
        if (!existsSync(join(directory, "request.json"))) continue;
        const meta = loadJson(join(directory, "meta.json"));
        found.push({
            id: name,
            operation: meta.operation,
            request: loadJson(join(directory, "request.json")),
            expected: loadJson(join(directory, "expected.json")),
            ignored: new Set(meta.ignore_fields ?? []),
        });
    }
    return found;
}

// The rules of conformance/ops/README.md: messages and the fields a case lists do not count.
export function comparable(value, ignored) {
    if (Array.isArray(value)) return value.map((item) => comparable(item, ignored));
    if (value !== null && typeof value === "object") {
        const out = {};
        for (const [key, item] of Object.entries(value)) {
            if (key !== "message" && !ignored.has(key)) out[key] = comparable(item, ignored);
        }
        return out;
    }
    return value;
}

// A model back to the JSON values it came from: absent fields go, and `truncated` belongs to the envelope. jCal stays as it is.
export function toPlain(value, top = true) {
    if (Array.isArray(value)) return value.map((item) => toPlain(item, false));
    if (value !== null && typeof value === "object") {
        const out = {};
        for (const [key, item] of Object.entries(value)) {
            if (item === undefined || (top && key === "truncated")) continue;
            out[wire(key)] = key === "jcal" ? item : toPlain(item, false);
        }
        return out;
    }
    return value;
}

// Runs a program in a process group of its own, so that nothing it started can outlive the limit.
export function runProgram(args, { limit = 120_000, cwd, env } = {}) {
    return new Promise((resolve, reject) => {
        const child = spawn(process.execPath, args, { cwd, env: env ?? process.env, detached: true, stdio: ["ignore", "pipe", "pipe"] });
        let output = "";
        let stdout = "";
        let stderr = "";
        child.stdout.on("data", (data) => {
            output += data;
            stdout += data;
        });
        child.stderr.on("data", (data) => {
            output += data;
            stderr += data;
        });
        const timer = setTimeout(() => {
            process.kill(-child.pid, "SIGKILL");
            reject(new Error(`A program did not finish in ${limit} ms:\n${output}`));
        }, limit);
        child.on("error", reject);
        child.on("close", (code, signal) => {
            clearTimeout(timer);
            resolve({ code, signal, output, stdout, stderr });
        });
    });
}

// A calendar of 200 bytes that keeps a process busy for seconds: a million occurrences.
export function slowCalendar(count = 1_000_000) {
    return ICS.replace("DTEND:20260105T110000Z", "DURATION:PT2S").replace("FREQ=DAILY;COUNT=3", `FREQ=SECONDLY;COUNT=${count}`);
}
