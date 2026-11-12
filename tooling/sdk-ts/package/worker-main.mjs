// The process of a CalendrumWorker. A response goes back as the text it is: the other side parses it, as the result lands there.
import { Worker } from "node:worker_threads";
import { call } from "./lib/calendrum-core.mjs";

process.on("message", ({ operation, request }) => {
    process.send(call(operation, request));
});

// The parent is gone, so nobody waits for an answer: this also holds if it was killed.
process.on("disconnect", () => process.exit(0));

// A call blocks this thread, and with it the event above, for as long as a file from a stranger wants: without the parent, also its
// time limit is gone, and the process would run on alone. So a thread of its own looks at the parent.
new Worker(
    `const { parent } = require("node:worker_threads").workerData;
    setInterval(() => { if (process.ppid !== parent) process.kill(process.pid, "SIGKILL"); }, 500);`,
    { eval: true, workerData: { parent: process.ppid } },
).unref();
