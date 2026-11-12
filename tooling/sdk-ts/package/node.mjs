// What needs Node: reading a file, and running an operation in a process that can be stopped.
import { fork } from "node:child_process";
import { closeSync, constants, fstatSync, openSync, readSync } from "node:fs";
import { fileURLToPath } from "node:url";
import { BadArgumentError, TooLargeError, WorkerError } from "./errors.mjs";
import { OPERATIONS, checkedCall, readResponse, requestJson, toCallResult, toModel } from "./operations.mjs";

export { WorkerError } from "./errors.mjs";

// The limit of the contract for one calendar. A larger file is refused here, without reading it into memory.
const CALENDAR_LIMIT = 64 * 1024 * 1024;
const SMALLEST_READ = 64 * 1024;

export function readCalendar(path) {
    // O_NONBLOCK: opening a FIFO for reading would otherwise wait for a writer forever.
    const descriptor = openSync(path, constants.O_RDONLY | (constants.O_NONBLOCK ?? 0) | (constants.O_NOCTTY ?? 0));
    try {
        const info = fstatSync(descriptor);
        if (!info.isFile()) throw new BadArgumentError("OPS-BAD-ARGUMENT", "The path is not a regular file");
        if (info.size > CALENDAR_LIMIT) throw new TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit");
        // Up to the limit and not up to the size: a file can grow while it is read, and procfs reports a size of 0.
        const chunks = [];
        let total = 0;
        for (;;) {
            const wanted = Math.max(info.size - total + 1, SMALLEST_READ);
            const chunk = new Uint8Array(Math.min(wanted, CALENDAR_LIMIT + 1 - total));
            const count = readSync(descriptor, chunk, 0, chunk.length, null);
            if (count === 0) break;
            chunks.push(chunk.subarray(0, count));
            total += count;
            if (total > CALENDAR_LIMIT) throw new TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit");
        }
        // A copy of the right size: the buffer of a view holds more than the view, which a caller can trip over.
        const whole = new Uint8Array(total);
        let at = 0;
        for (const chunk of chunks) {
            whole.set(chunk, at);
            at += chunk.length;
        }
        return whole;
    } finally {
        closeSync(descriptor);
    }
}

const ENTRY = fileURLToPath(new URL("./worker-main.mjs", import.meta.url));

// What the end of the message of a dying process is searched in: enough for the line that says why.
const STDERR_KEPT = 64 * 1024;

// The most a timer of Node takes: above it, a timer fires at once.
const LARGEST_NUMBER = 2 ** 31 - 1;

function checkedNumber(value, name) {
    if (value !== undefined && !(typeof value === "number" && value > 0 && value <= LARGEST_NUMBER)) {
        throw new TypeError(`The option ${name} of a worker is a number from 1 to ${LARGEST_NUMBER}`);
    }
}

// A process and not a thread: V8 ends the whole process on an allocation it cannot make, and a limit on the heap of a thread makes
// that likely. Tried on a calendar of 63 MiB, limits of 16, 32 and 128 MiB took down the program that had started the thread.
export class CalendrumWorker {
    #timeoutMs;
    #execArgv;
    #child = null;
    #stderr = "";
    #queue = [];
    #current = null;
    #timer = null;
    #closed = false;

    constructor(options = {}) {
        for (const name of Object.keys(options)) {
            // The name is not quoted, as with the options of an operation.
            if (name !== "timeoutMs" && name !== "maxMemoryMb") throw new TypeError("A worker takes the options timeoutMs, maxMemoryMb");
        }
        checkedNumber(options.timeoutMs, "timeoutMs");
        checkedNumber(options.maxMemoryMb, "maxMemoryMb");
        this.#timeoutMs = options.timeoutMs;
        this.#execArgv = options.maxMemoryMb === undefined ? [] : [`--max-old-space-size=${Math.ceil(options.maxMemoryMb)}`];
    }

    #start() {
        const environment = process.env.SystemRoot === undefined ? {} : { SystemRoot: process.env.SystemRoot };
        let child;
        try {
            child = fork(ENTRY, [], {
                execArgv: this.#execArgv,
                env: environment,
                serialization: "advanced",
                stdio: ["ignore", "ignore", "pipe", "ipc"],
            });
        } catch {
            return null;
        }
        // First of all: a process that cannot start, such as without a free descriptor, says so only here, and without a listener
        // it would take down the program.
        child.on("error", () => {
            if (child === this.#child) this.#failed("crash", "The worker died");
        });
        if (child.pid === undefined || child.stderr === null) return null;
        this.#stderr = "";
        child.stderr.setEncoding("latin1");
        child.stderr.on("data", (text) => {
            if (child === this.#child) this.#stderr = (this.#stderr + text).slice(-STDERR_KEPT);
        });
        child.on("message", (text) => {
            if (child === this.#child) this.#finished(text);
        });
        // `close` and not `exit`: by then the pipe of stderr has been read to its end, with the line that says why.
        child.on("close", () => {
            if (child !== this.#child) return;
            const memory = /out of memory/i.test(this.#stderr);
            // A fixed text and no `cause`: what a process dies of is not for an exception to carry.
            this.#failed(memory ? "memory" : "crash", memory ? "The worker ran out of memory" : "The worker died");
        });
        return child;
    }

    #hold(child, held) {
        const method = held ? "ref" : "unref";
        child[method]();
        child.channel?.[method]();
        child.stderr[method]();
    }

    #pump() {
        if (this.#current !== null) return;
        const next = this.#queue.shift();
        if (next === undefined) {
            // An idle process does not hold the program up: one that never closes its worker still ends, and this process with it.
            if (this.#child !== null) this.#hold(this.#child, false);
            return;
        }
        this.#child ??= this.#start();
        if (this.#child === null) {
            next.reject(new WorkerError("crash", "The worker could not start"));
            this.#pump();
            return;
        }
        this.#hold(this.#child, true);
        this.#current = next;
        if (this.#timeoutMs !== undefined) {
            this.#timer = setTimeout(() => this.#failed("timeout", "The call did not finish in time"), this.#timeoutMs);
        }
        // A failed write means the process is dying, and its `close` tells the call.
        this.#child.send({ operation: next.operation, request: next.request }, () => {});
    }

    #settle() {
        clearTimeout(this.#timer);
        this.#timer = null;
        const finished = this.#current;
        this.#current = null;
        return finished;
    }

    #finished(text) {
        const { resolve, reject, finish } = this.#settle();
        try {
            const { result, truncated } = readResponse(text);
            resolve(finish(result, truncated));
        } catch (error) {
            reject(error);
        }
        this.#pump();
    }

    #failed(reason, message) {
        const child = this.#child;
        // The process is dropped first, so that its own end is not one more failure of the next call.
        this.#child = null;
        child?.kill("SIGKILL");
        this.#settle()?.reject(new WorkerError(reason, message));
        this.#pump();
    }

    #submit(operation, request, finish) {
        return new Promise((resolve, reject) => {
            if (this.#closed) {
                reject(new WorkerError("closed", "The worker is closed"));
                return;
            }
            this.#queue.push({ operation, request, finish, resolve, reject });
            this.#pump();
        });
    }

    async #run(name, args) {
        const { wire, request, decode } = OPERATIONS[name];
        return this.#submit(wire, requestJson(request(...args)), (result, truncated) => toModel(decode, result, truncated));
    }

    inspect(calendar, options) {
        return this.#run("inspect", [calendar, options]);
    }

    validate(calendar, options) {
        return this.#run("validate", [calendar, options]);
    }

    fix(calendar, options) {
        return this.#run("fix", [calendar, options]);
    }

    expand(calendar, options) {
        return this.#run("expand", [calendar, options]);
    }

    rruleExpand(rrule, start, options) {
        return this.#run("rruleExpand", [rrule, start, options]);
    }

    freebusy(calendar, options) {
        return this.#run("freebusy", [calendar, options]);
    }

    buildEvent(summary, start, options) {
        return this.#run("buildEvent", [summary, start, options]);
    }

    diff(before, after, options) {
        return this.#run("diff", [before, after, options]);
    }

    convert(input) {
        return this.#run("convert", [input]);
    }

    anonymize(calendar, options) {
        return this.#run("anonymize", [calendar, options]);
    }

    tzResolve(name, options) {
        return this.#run("tzResolve", [name, options]);
    }

    explain(code) {
        return this.#run("explain", [code]);
    }

    async call(operation, request) {
        return this.#submit(operation, checkedCall(operation, request), toCallResult);
    }

    close() {
        this.#closed = true;
        const child = this.#child;
        this.#child = null;
        const waiting = this.#queue;
        this.#queue = [];
        const current = this.#settle();
        for (const call of current === null ? waiting : [current, ...waiting]) {
            call.reject(new WorkerError("closed", "The worker is closed"));
        }
        if (child === null || child.exitCode !== null || child.signalCode !== null) return Promise.resolve();
        // Held while it ends: an idle process does not keep the program alive, and the program is waiting for this promise.
        this.#hold(child, true);
        return new Promise((resolve) => {
            child.once("close", () => resolve());
            child.kill("SIGKILL");
        });
    }

    [Symbol.asyncDispose]() {
        return this.close();
    }
}
