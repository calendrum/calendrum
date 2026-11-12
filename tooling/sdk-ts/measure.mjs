#!/usr/bin/env node
// Measures the package: its size, a minified bundle, the import, the first answer, `expand` through each layer, a worker, and the worst
// case for the event loop.
//
// Usage: node measure.mjs <package directory> <tarball> <feed.ics> [--webpack <node_modules with webpack>] [--runs n] [--worst]
//
// The feed comes from sdk/c/bench/make_feed.py. Each run is a process of its own, and the result is the median of the runs. With
// --webpack the bundle is built as size-budget builds its own: production mode, which minifies with terser.
import { execFileSync } from "node:child_process";
import { mkdtempSync, readFileSync, readdirSync, rmSync, statSync } from "node:fs";
import { createRequire } from "node:module";
import { tmpdir } from "node:os";
import { join, resolve } from "node:path";
import { fileURLToPath, pathToFileURL } from "node:url";
import { gzipSync } from "node:zlib";

const WINDOW = { from: "2026-01-01T00:00:00Z", to: "2027-01-01T00:00:00Z", zone: "Europe/Berlin", limit: 100000 };
const ROUNDS = 100;

function median(values) {
    const sorted = [...values].sort((a, b) => a - b);
    return sorted[Math.floor(sorted.length / 2)];
}

async function importRun(directory) {
    const started = performance.now();
    const calendrum = await import(pathToFileURL(join(directory, "index.mjs")).href);
    const imported = performance.now();
    calendrum.explain("ICAL-S003");
    const answered = performance.now();
    console.log(JSON.stringify({
        "import ms": imported - started,
        "import to the first answer ms": answered - imported,
        "RSS after the first answer MiB": process.memoryUsage().rss / 1048576,
    }));
}

async function callsRun(directory, feed) {
    const calendrum = await import(pathToFileURL(join(directory, "index.mjs")).href);
    const native = await import(pathToFileURL(join(directory, "lib", "calendrum-core.mjs")).href);
    const text = readFileSync(feed, "utf8");
    const request = { ics: text, ...WINDOW };
    const payload = JSON.stringify(request);
    const raw = native.call("expand", payload);
    const timed = (action) => {
        for (let index = 0; index < ROUNDS / 2; index += 1) action();
        const started = performance.now();
        for (let index = 0; index < ROUNDS; index += 1) action();
        return (performance.now() - started) / ROUNDS;
    };
    const results = {
        "native call, strings in and out": timed(() => native.call("expand", payload)),
        "JSON.parse of the response": timed(() => JSON.parse(raw)),
        "call(), an object in and out": timed(() => calendrum.call("expand", request)),
        "expand(), with the models": timed(() => calendrum.expand(text, WINDOW)),
        "inspect() of the feed": timed(() => calendrum.inspect(text)),
        occurrences: calendrum.expand(text, WINDOW).count,
        "RSS MiB": process.memoryUsage().rss / 1048576,
    };
    console.log(JSON.stringify(results));
}

async function workerRun(directory) {
    const { CalendrumWorker } = await import(pathToFileURL(join(directory, "node.mjs")).href);
    const worker = new CalendrumWorker();
    const started = performance.now();
    await worker.explain("ICAL-S003");
    const first = performance.now();
    await worker.explain("ICAL-S003");
    const second = performance.now();
    await worker.close();
    console.log(JSON.stringify({ "a new process to its first answer ms": first - started, "the next call ms": second - first }));
}

// 99,000 series with a rule far from its end, and a window that makes each of them expand to its share.
function worstCalendar(series) {
    const parts = ["BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//example.com//measure//EN\r\n"];
    for (let index = 0; index < series; index += 1) {
        parts.push(`BEGIN:VEVENT\r\nUID:${index}@example.com\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260101T000000Z\r\nDURATION:PT1S\r\nRRULE:FREQ=SECONDLY;COUNT=2000\r\nEND:VEVENT\r\n`);
    }
    parts.push("END:VCALENDAR\r\n");
    return parts.join("");
}

async function worstRun(directory) {
    const { CalendrumWorker } = await import(pathToFileURL(join(directory, "node.mjs")).href);
    const calendrum = await import(pathToFileURL(join(directory, "index.mjs")).href);
    const text = worstCalendar(99_000);
    const window = { from: "9000-01-01T00:00:00Z", to: "9000-01-02T00:00:00Z", zone: "Europe/Berlin" };
    process.on("warning", () => {});
    const started = performance.now();
    const result = calendrum.expand(text, window);
    const elapsed = performance.now() - started;
    const worker = new CalendrumWorker({ timeoutMs: 500 });
    let ticks = 0;
    const interval = setInterval(() => {
        ticks += 1;
    }, 10);
    const stopwatch = performance.now();
    let reason = "finished";
    try {
        await worker.expand(text, window);
    } catch (error) {
        reason = error.reason ?? error.name;
    }
    const stopped = performance.now() - stopwatch;
    clearInterval(interval);
    await worker.close();
    console.log(JSON.stringify({
        "calendar MB": Number((text.length / 1e6).toFixed(1)),
        "expand on the main thread ms": Math.round(elapsed),
        truncated: result.truncated,
        problems: result.problemCount,
        "through a worker with timeoutMs 500": reason,
        "stopped after ms": Math.round(stopped),
        "ticks of the event loop meanwhile": ticks,
    }));
}

function sizes(directory, tarball) {
    const walk = (at) => readdirSync(at, { withFileTypes: true }).flatMap((entry) => (entry.isDirectory() ? walk(join(at, entry.name)) : [join(at, entry.name)]));
    const files = walk(directory);
    const code = files.filter((file) => file.endsWith(".mjs"));
    const total = (list) => list.reduce((sum, file) => sum + statSync(file).size, 0);
    const gzipped = code.reduce((sum, file) => sum + gzipSync(readFileSync(file), { level: 9 }).length, 0);
    console.log(`package: ${files.length} files, ${total(files)} B; .mjs ${total(code)} B, gzip -9 each ${gzipped} B`);
    for (const file of walk(join(directory, "lib")).filter((name) => name.endsWith(".mjs")).sort()) {
        console.log(`  ${file.replace(`${directory}/`, "")}: ${statSync(file).size} B`);
    }
    console.log(`tarball: ${statSync(tarball).size} B`);
}

function bundle(directory, modules) {
    const require = createRequire(join(modules, "x.js"));
    const webpack = require("webpack");
    const out = mkdtempSync(join(tmpdir(), "calendrum-bundle-"));
    return new Promise((done, failed) => {
        webpack({
            mode: "production",
            target: "web",
            entry: join(directory, "index.mjs"),
            devtool: false,
            performance: false,
            output: { path: out, filename: "bundle.js", library: { type: "module" } },
            experiments: { outputModule: true },
        }, (error, stats) => {
            if (error || stats.hasErrors()) {
                failed(error ?? new Error(stats.toString({ all: false, errors: true })));
                return;
            }
            const data = readFileSync(join(out, "bundle.js"));
            console.log(`webpack ${webpack.version}, production, for the browser: ${data.length} B, gzip -9 ${gzipSync(data, { level: 9 }).length} B`);
            rmSync(out, { recursive: true });
            done();
        });
    });
}

function runs(args, count) {
    const values = {};
    for (let index = 0; index < count; index += 1) {
        const line = execFileSync(process.execPath, args, { encoding: "utf8", stdio: ["ignore", "pipe", "ignore"] }).trim().split("\n").pop();
        for (const [key, value] of Object.entries(JSON.parse(line))) (values[key] ??= []).push(value);
    }
    return values;
}

function report(label, values) {
    console.log(label);
    for (const [name, found] of Object.entries(values)) {
        console.log(`  ${name}: ${median(found).toFixed(3)} (${Math.min(...found).toFixed(3)}-${Math.max(...found).toFixed(3)})`);
    }
}

const [first, ...rest] = process.argv.slice(2);
if (first === "--import-run") {
    await importRun(rest[0]);
} else if (first === "--calls-run") {
    await callsRun(rest[0], rest[1]);
} else if (first === "--worker-run") {
    await workerRun(rest[0]);
} else if (first === "--worst-run") {
    await worstRun(rest[0]);
} else {
    const directory = resolve(first);
    const tarball = rest[0];
    const feed = resolve(rest[1]);
    const option = (name) => (rest.includes(name) ? rest[rest.indexOf(name) + 1] : undefined);
    const count = Number(option("--runs") ?? 12);
    const me = fileURLToPath(import.meta.url);
    sizes(directory, tarball);
    if (option("--webpack") !== undefined) await bundle(directory, resolve(option("--webpack")));
    console.log(`Node ${process.version}, ${count} processes; median (min-max)`);
    report("the import", runs([me, "--import-run", directory], count));
    report("a worker", runs([me, "--worker-run", directory], count));
    report(`expand of the year on the feed, ${Math.max(count / 4, 3)} processes of ${ROUNDS} calls, ms for a call`, runs([me, "--calls-run", directory, feed], Math.max(count / 4, 3)));
    if (rest.includes("--worst")) {
        console.log(`the worst case: ${execFileSync(process.execPath, [me, "--worst-run", directory], { encoding: "utf8", stdio: ["ignore", "pipe", "ignore"] }).trim()}`);
    }
}
