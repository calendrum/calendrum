// The guide: every TypeScript block runs as written, and it follows the rules of a public guide.
import assert from "node:assert/strict";
import { mkdirSync, mkdtempSync, readFileSync, rmSync, symlinkSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join } from "node:path";
import { test } from "node:test";
import { GUIDE, ICS, OPERATIONS, PACKAGE, camel, runProgram } from "./support.mjs";

const text = readFileSync(GUIDE, "utf8");
const blocks = [...text.matchAll(/^```([a-z]*)\n([\s\S]*?)^```$/gm)].map((match) => ({ language: match[1], code: match[2] }));

test("every TypeScript block runs", async () => {
    const code = blocks.filter((block) => block.language === "typescript").map((block) => block.code);
    assert.ok(code.length >= 7, String(code.length));
    const directory = mkdtempSync(join(tmpdir(), "calendrum-"));
    const outputs = [];
    try {
        mkdirSync(join(directory, "node_modules"));
        symlinkSync(PACKAGE, join(directory, "node_modules", "calendrum"), "dir");
        writeFileSync(join(directory, "event.ics"), ICS);
        writeFileSync(join(directory, "package.json"), '{"type":"module"}');
        for (const [index, block] of code.entries()) {
            const file = join(directory, `example${index}.ts`);
            writeFileSync(file, block);
            const { code: status, stdout, stderr } = await runProgram([file], { cwd: directory, limit: 120_000 });
            assert.equal(status, 0, `${block}\n${stdout}\n${stderr}`);
            outputs.push(stdout);
        }
    } finally {
        rmSync(directory, { recursive: true, force: true });
    }
    const joined = outputs.join("\n");
    assert.match(joined, /OPS-UNKNOWN-ZONE/);
    assert.match(joined, /true 1/, "the example of a truncated result is truncated");
    assert.match(joined, /timeout/, "the example of a time limit is stopped");
});

test("the languages of the blocks", () => {
    assert.deepEqual(new Set(blocks.map((block) => block.language)), new Set(["bash", "typescript"]));
});

test("it follows the rules of a public guide", () => {
    assert.doesNotMatch(text, /docs\/|D-\d+|P-\d+/);
    const hosts = [...text.matchAll(/https?:\/\/([^/\s)]+)/g)].map((match) => match[1]).filter((host) => !host.endsWith("example.com"));
    assert.deepEqual(hosts, []);
});

test("it names all it has to", () => {
    for (const name of [...OPERATIONS.map(camel), "call", "versions", "readCalendar", "CalendrumWorker", "timeoutMs", "maxMemoryMb",
        "truncated", "WorkerError", "LibraryError", "Only Node on macOS", "worker_threads"]) {
        assert.ok(text.includes(name), name);
    }
});
