// CalendrumWorker: the same answers as on the calling thread, a call that can really be stopped, and a program that survives what kills
// the process of its worker. Each worker has a limit here, and a test that needs a program of its own runs it in a process group
// that is killed past a limit.
import assert from "node:assert/strict";
import { execFileSync, spawn } from "node:child_process";
import { mkdtempSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { test } from "node:test";
import { pathToFileURL } from "node:url";
import { ICS, PACKAGE, WINDOW, allCases, calendrum, comparable, nodePart, runProgram, slowCalendar } from "./support.mjs";

const { CalendrumWorker, WorkerError } = nodePart;

// `ulimit` lowers what the program may open, so that it runs out of descriptors at once and not after a million of them.
function runProgramWithLimit(program) {
    return new Promise((resolve, reject) => {
        const child = spawn("/bin/sh", ["-c", `ulimit -n 64; exec "$0" "$1"`, process.execPath, program], { detached: true, stdio: ["ignore", "pipe", "pipe"] });
        let stdout = "";
        let stderr = "";
        child.stdout.on("data", (data) => {
            stdout += data;
        });
        child.stderr.on("data", (data) => {
            stderr += data;
        });
        const timer = setTimeout(() => {
            process.kill(-child.pid, "SIGKILL");
            reject(new Error(`A program did not finish in time:\n${stdout}\n${stderr}`));
        }, 30_000);
        child.on("close", (code) => {
            clearTimeout(timer);
            resolve({ code, stdout, stderr });
        });
    });
}

function childrenOf(pid) {
    return execFileSync("pgrep", ["-P", String(pid)], { encoding: "utf8" }).trim().split("\n").filter(Boolean).map(Number);
}

function isAlive(pid) {
    try {
        process.kill(pid, 0);
        return true;
    } catch {
        return false;
    }
}
const NEEDS_CLOCK_OR_RANDOM = new Set(["anonymize-shift", "anonymize-text-goes", "build-event-all-day-generated-uid", "tz-resolve-default-moment"]);

async function withWorker(options, action) {
    const worker = new CalendrumWorker(options);
    try {
        return await action(worker);
    } finally {
        await worker.close();
    }
}

test("the answers are the ones of the calling thread, for every case", () => withWorker({ timeoutMs: 60_000 }, async (worker) => {
    for (const testCase of allCases()) {
        if (NEEDS_CLOCK_OR_RANDOM.has(testCase.id)) continue;
        let wanted;
        try {
            wanted = { ok: calendrum.call(testCase.operation, testCase.request) };
        } catch (error) {
            wanted = { code: error.code };
        }
        try {
            const got = await worker.call(testCase.operation, testCase.request);
            assert.deepEqual(comparable({ ...got }, testCase.ignored), comparable({ ...wanted.ok }, testCase.ignored), testCase.id);
            assert.equal(got.truncated, wanted.ok.truncated, testCase.id);
        } catch (error) {
            if (error.code === undefined) throw error;
            assert.equal(error.code, wanted.code, testCase.id);
        }
    }
}));

test("every function of the package is a method with the same answer", () => withWorker({ timeoutMs: 60_000 }, async (worker) => {
    assert.deepEqual(await worker.inspect(ICS), calendrum.inspect(ICS));
    assert.deepEqual(await worker.validate(ICS, { rules: "lint" }), calendrum.validate(ICS, { rules: "lint" }));
    assert.deepEqual(await worker.fix(ICS), calendrum.fix(ICS));
    assert.deepEqual(await worker.expand(ICS, WINDOW), calendrum.expand(ICS, WINDOW));
    assert.deepEqual(await worker.rruleExpand("FREQ=DAILY;COUNT=2", "2026-01-05T10:00:00Z"), calendrum.rruleExpand("FREQ=DAILY;COUNT=2", "2026-01-05T10:00:00Z"));
    assert.deepEqual(await worker.freebusy(ICS, WINDOW), calendrum.freebusy(ICS, WINDOW));
    const event = { dtstamp: "2026-01-01T00:00:00Z", uid: "a@example.com", duration: "PT1H" };
    assert.deepEqual(await worker.buildEvent("x", "2026-01-05T10:00:00Z", event), calendrum.buildEvent("x", "2026-01-05T10:00:00Z", event));
    assert.deepEqual(await worker.diff(ICS, ICS), calendrum.diff(ICS, ICS));
    assert.deepEqual(await worker.convert(ICS), calendrum.convert(ICS));
    assert.ok((await worker.anonymize(ICS)).ics.includes("BEGIN:VCALENDAR"));
    assert.equal((await worker.tzResolve("Europe/Berlin", { at: "2026-01-01T00:00:00Z" })).offsetSeconds, 3600);
    assert.deepEqual(await worker.explain("ICAL-S003"), calendrum.explain("ICAL-S003"));
}));

test("a mistake in the arguments is a rejection, and not an exception at the call", () => withWorker({}, async (worker) => {
    const pending = worker.inspect(5);
    await assert.rejects(pending, TypeError);
    await assert.rejects(worker.call(5, {}), TypeError);
    await assert.rejects(worker.explain("ICAL-NOPE"), calendrum.UnknownCodeError);
    await assert.rejects(worker.expand(ICS, { unknown: 1 }), TypeError);
}));

test("options of a worker are checked", () => {
    assert.throws(() => new CalendrumWorker({ timeoutMs: 0 }), TypeError);
    assert.throws(() => new CalendrumWorker({ timeoutMs: "5" }), TypeError);
    assert.throws(() => new CalendrumWorker({ maxMemoryMb: -1 }), TypeError);
    assert.throws(() => new CalendrumWorker({ timeoutMs: 2 ** 31 }), TypeError, "a timer of Node would fire at once");
    assert.throws(() => new CalendrumWorker({ maxMemoryMb: 1e21 }), TypeError);
    assert.throws(() => new CalendrumWorker({ timeoutMS: 100 }), /timeoutMs/, "a misspelled option is not silently no limit");
    new CalendrumWorker({ timeoutMs: 2 ** 31 - 1 });
});

test("without free descriptors a call fails, and the program stays", async () => {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    try {
        const program = join(directory, "program.mjs");
        writeFileSync(program, `
            import { closeSync, openSync } from "node:fs";
            import { CalendrumWorker, WorkerError } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
            const held = [];
            try {
                for (;;) held.push(openSync("/dev/null", "r"));
            } catch {
                // Every descriptor is taken. Three go back: Node needs them for itself, and a new process needs more.
                for (let index = 0; index < 3; index += 1) closeSync(held.pop());
            }
            const worker = new CalendrumWorker();
            const answer = await worker.explain("ICAL-S003").then(() => "answered", (error) => (error instanceof WorkerError ? error.reason : "other"));
            setTimeout(() => {
                console.log(answer);
                process.exit(0);
            }, 200);
        `);
        const { code, stdout, stderr } = await runProgramWithLimit(program);
        assert.equal(code, 0, stderr);
        assert.equal(stdout.trim(), "crash", stderr);
    } finally {
        rmSync(directory, { recursive: true });
    }
});

test("calls run in the order they were made", () => withWorker({ timeoutMs: 60_000 }, async (worker) => {
    const answers = await Promise.all(["ICAL-S003", "ICAL-S001", "ICAL-S002", "ICAL-S004"].map((code) => worker.explain(code)));
    assert.deepEqual(answers.map((answer) => answer.code), ["ICAL-S003", "ICAL-S001", "ICAL-S002", "ICAL-S004"]);
}));

test("a timeout stops a real long call, and the event loop lives on", () => withWorker({ timeoutMs: 400 }, async (worker) => {
    const stopwatch = performance.now();
    let ticks = 0;
    const interval = setInterval(() => {
        ticks += 1;
    }, 10);
    try {
        await assert.rejects(worker.freebusy(slowCalendar(), { ...WINDOW, conflicts: false }), (error) => {
            assert.ok(error instanceof WorkerError);
            assert.ok(error instanceof calendrum.LibraryError);
            assert.equal(error.reason, "timeout");
            return true;
        });
    } finally {
        clearInterval(interval);
    }
    const elapsed = performance.now() - stopwatch;
    assert.ok(elapsed < 5000, `it took ${elapsed} ms`);
    assert.ok(ticks > 5, `the event loop ran ${ticks} times in ${elapsed} ms`);
    console.log(`timeout: stopped in ${elapsed.toFixed(0)} ms, ${ticks} ticks of the event loop meanwhile`);
}));

test("the queue goes on after a failure, with a new process", () => withWorker({ timeoutMs: 400 }, async (worker) => {
    const long = worker.freebusy(slowCalendar(), { ...WINDOW, conflicts: false });
    const short = worker.explain("ICAL-S003");
    const after = worker.expand(ICS, WINDOW);
    await assert.rejects(long, { reason: "timeout" });
    assert.equal((await short).code, "ICAL-S003");
    assert.equal((await after).count, 3);
}));

function largeCalendar(megabytes) {
    const description = `DESCRIPTION:${"x".repeat(70)}\r\n${` ${"y".repeat(70)}\r\n`.repeat(40)}`;
    const parts = [ICS.slice(0, ICS.indexOf("BEGIN:VEVENT"))];
    let size = parts[0].length;
    for (let index = 0; size < megabytes * 1048576; index += 1) {
        const event = `BEGIN:VEVENT\r\nUID:${index}@example.com\r\nDTSTAMP:20260101T000000Z\r\nDTSTART:20260105T100000Z\r\n${description}END:VEVENT\r\n`;
        parts.push(event);
        size += event.length;
    }
    parts.push("END:VCALENDAR\r\n");
    return parts.join("");
}

// These limits, with this size, took down the whole program when the worker was a thread. Two of them: each one leaves a report of
// the crash of a process in ~/Library/Logs/DiagnosticReports.
for (const limit of [1, 128]) {
    test(`running out of memory at ${limit} MiB fails the call and not the program`, () => withWorker({ maxMemoryMb: limit, timeoutMs: 120_000 }, async (worker) => {
        await assert.rejects(worker.validate(limit === 1 ? ICS : largeCalendar(63)), (error) => {
            assert.ok(error instanceof WorkerError, String(error));
            // So small a limit can end the process before it can say why.
            assert.ok(error.reason === "memory" || (limit === 1 && error.reason === "crash"), error.reason);
            assert.equal(error.cause, undefined);
            return true;
        });
        assert.equal(calendrum.explain("ICAL-S003").code, "ICAL-S003");
    }));
}

test("after running out of memory the next call gets a new process", () => withWorker({ maxMemoryMb: 64, timeoutMs: 120_000 }, async (worker) => {
    await assert.rejects(worker.validate(largeCalendar(63)), { reason: "memory" });
    assert.equal((await worker.expand(ICS, WINDOW)).count, 3);
}));

test("a process that dies fails its call, and the next call gets a new one", () => withWorker({ timeoutMs: 120_000 }, async (worker) => {
    assert.equal((await worker.expand(ICS, WINDOW)).count, 3);
    const long = worker.freebusy(slowCalendar(), { ...WINDOW, conflicts: false });
    const [pid] = childrenOf(process.pid);
    assert.ok(pid, "the process of the worker is a child of this one");
    process.kill(pid, "SIGKILL");
    await assert.rejects(long, { reason: "crash" });
    assert.equal((await worker.expand(ICS, WINDOW)).count, 3);
}));

for (const [name, ending] of [["killed", 'process.kill(process.pid, "SIGKILL")'], ["ended", "process.exit(0)"]]) {
    test(`a worker in the middle of a call ends when its program is ${name}`, async () => {
        const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
        try {
            const program = join(directory, "program.mjs");
            writeFileSync(program, `
                import { execFileSync } from "node:child_process";
                import { CalendrumWorker } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
                const worker = new CalendrumWorker();
                await worker.explain("ICAL-S003");
                worker.freebusy(${JSON.stringify(slowCalendar())}, { from: "2026-01-01T00:00:00Z", to: "2027-01-01T00:00:00Z", conflicts: false }).catch(() => {});
                setTimeout(() => {
                    console.log(execFileSync("pgrep", ["-P", String(process.pid)], { encoding: "utf8" }).trim().split("\\n")[0]);
                    ${ending};
                }, 200);
            `);
            const { stdout } = await runProgram([program], { limit: 30_000 });
            const pid = Number(stdout.trim());
            assert.ok(pid > 0, stdout);
            const end = Date.now() + 5_000;
            while (isAlive(pid) && Date.now() < end) await new Promise((done) => setTimeout(done, 50));
            const alive = isAlive(pid);
            if (alive) process.kill(pid, "SIGKILL");
            assert.equal(alive, false, "the process of the worker ended soon after its program");
        } finally {
            rmSync(directory, { recursive: true });
        }
    });
}

test("a worker whose program was killed does not stay behind", async () => {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    try {
        const program = join(directory, "program.mjs");
        writeFileSync(program, `
            import { execFileSync } from "node:child_process";
            import { CalendrumWorker } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
            const worker = new CalendrumWorker();
            await worker.explain("ICAL-S003");
            console.log(execFileSync("pgrep", ["-P", String(process.pid)], { encoding: "utf8" }).trim().split("\\n")[0]);
            process.kill(process.pid, "SIGKILL");
        `);
        const { stdout, signal } = await runProgram([program], { limit: 30_000 });
        assert.equal(signal, "SIGKILL");
        const pid = Number(stdout.trim());
        assert.ok(pid > 0, stdout);
        const end = Date.now() + 10_000;
        while (isAlive(pid) && Date.now() < end) await new Promise((done) => setTimeout(done, 50));
        assert.equal(isAlive(pid), false, "the process of the worker ended after its program");
    } finally {
        rmSync(directory, { recursive: true });
    }
});

test("close rejects what is waiting and what comes after", async () => {
    const worker = new CalendrumWorker();
    const long = worker.freebusy(slowCalendar(), { ...WINDOW, conflicts: false });
    const waiting = worker.explain("ICAL-S003");
    const checks = [assert.rejects(long, { reason: "closed" }), assert.rejects(waiting, { reason: "closed" })];
    await worker.close();
    await Promise.all(checks);
    await assert.rejects(worker.explain("ICAL-S003"), { reason: "closed" });
    await worker.close();
});

test("asyncDispose closes", async () => {
    const worker = new CalendrumWorker();
    await worker.explain("ICAL-S003");
    await worker[Symbol.asyncDispose]();
    await assert.rejects(worker.explain("ICAL-S003"), { reason: "closed" });
});

test("a program that ends with awaiting close ends with code 0", async () => {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    try {
        const program = join(directory, "program.mjs");
        writeFileSync(program, `
            import { CalendrumWorker } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
            const worker = new CalendrumWorker();
            console.log((await worker.explain("ICAL-S003")).code);
            await worker.close();
            console.log("closed");
        `);
        const { code, stdout, stderr } = await runProgram([program], { limit: 30_000 });
        assert.equal(code, 0, stderr);
        assert.equal(stdout.trim(), "ICAL-S003\nclosed");
    } finally {
        rmSync(directory, { recursive: true });
    }
});

test("a program that does not close its worker still ends", async () => {
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    try {
        const program = join(directory, "program.mjs");
        writeFileSync(program, `
            import { CalendrumWorker } from ${JSON.stringify(pathToFileURL(join(PACKAGE, "node.mjs")).href)};
            const worker = new CalendrumWorker({ timeoutMs: 30000 });
            console.log((await worker.explain("ICAL-S003")).code);
        `);
        const { code, stdout, stderr } = await runProgram([program], { limit: 30_000 });
        assert.equal(code, 0, stderr);
        assert.equal(stdout.trim(), "ICAL-S003");
    } finally {
        rmSync(directory, { recursive: true });
    }
});

test("an error of the worker carries nothing of the calendar", () => withWorker({ timeoutMs: 200 }, async (worker) => {
    const marked = slowCalendar().replace("SUMMARY:Plain", "SUMMARY:ZZMARKZZ");
    let caught;
    try {
        await worker.freebusy(marked, { ...WINDOW, conflicts: false });
    } catch (error) {
        caught = error;
    }
    assert.ok(caught);
    assert.equal(caught.cause, undefined);
    assert.ok(!`${caught} ${caught.message} ${caught.stack}`.includes("ZZMARKZZ"));
}));
