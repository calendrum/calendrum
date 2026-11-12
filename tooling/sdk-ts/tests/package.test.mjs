// The shape of the package: what it imports, what its types declare, and that it cannot be published by accident.
import assert from "node:assert/strict";
import { existsSync, readFileSync, readdirSync } from "node:fs";
import { dirname, join, relative, resolve } from "node:path";
import { test } from "node:test";
import { PACKAGE, calendrum, loadJson, moduleOf, nodePart } from "./support.mjs";

const IMPORT = /(?:^|\n)\s*(?:import|export)\b[^'"\n;]*?(?:\n[^'"\n;]*?)*?\bfrom\s*['"]([^'"]+)['"]|(?:^|\n)\s*import\s*['"]([^'"]+)['"]/g;

function importsOf(file) {
    const text = readFileSync(file, "utf8");
    return [...text.matchAll(IMPORT)].map((match) => match[1] ?? match[2]);
}

// Every module reachable from an entry, and every specifier that does not point at a file of the package.
function graph(entry) {
    const seen = new Set();
    const outside = new Set();
    const pending = [join(PACKAGE, entry)];
    while (pending.length > 0) {
        const file = pending.pop();
        if (seen.has(file)) continue;
        seen.add(file);
        for (const specifier of importsOf(file)) {
            if (specifier.startsWith(".")) {
                const target = resolve(dirname(file), specifier);
                assert.ok(existsSync(target), `${relative(PACKAGE, file)} imports ${specifier}, which is not in the package`);
                pending.push(target);
            } else {
                outside.add(specifier);
            }
        }
    }
    return { seen, outside };
}

test("the main entry imports nothing of Node and nothing from outside the package", () => {
    const { seen, outside } = graph("index.mjs");
    assert.deepEqual([...outside], []);
    assert.ok(seen.size > 8, "the walk reached the output of the compiler");
});

test("the entry for Node imports only what it says", () => {
    const { outside } = graph("node.mjs");
    assert.deepEqual([...outside].sort(), ["node:child_process", "node:fs", "node:url"]);
});

test("the process of a worker needs only the core and a thread, and so it cannot start another process", () => {
    const { outside, seen } = graph("worker-main.mjs");
    assert.deepEqual([...outside], ["node:worker_threads"]);
    assert.ok(![...seen].some((file) => file.endsWith("node.mjs")));
});

function declaredValues(file) {
    const text = readFileSync(join(PACKAGE, file), "utf8");
    const names = new Set();
    for (const match of text.matchAll(/^export declare (?:function|class|const) (\w+)/gm)) names.add(match[1]);
    for (const block of text.matchAll(/^export \{([^}]*)\} from/gm)) {
        for (const name of block[1].split(",")) {
            if (name.trim()) names.add(name.trim());
        }
    }
    return names;
}

test("every module in lib is reached from an entry: nothing stale goes into the package", () => {
    const reached = new Set([...graph("index.mjs").seen, ...graph("worker-main.mjs").seen]);
    const stale = readdirSync(join(PACKAGE, "lib")).filter((name) => name.endsWith(".mjs") && !reached.has(join(PACKAGE, "lib", name)));
    assert.deepEqual(stale, []);
});

test("the types declare exactly what the modules export", () => {
    assert.deepEqual(declaredValues("index.d.mts"), new Set(Object.keys(calendrum)));
    assert.deepEqual(declaredValues("node.d.mts"), new Set(Object.keys(nodePart)));
});

test("every method of the worker is declared, and the operations match the functions", () => {
    const text = readFileSync(join(PACKAGE, "node.d.mts"), "utf8");
    const methods = Object.getOwnPropertyNames(nodePart.CalendrumWorker.prototype).filter((name) => name !== "constructor");
    for (const name of methods) assert.ok(text.includes(`    ${name}(`), name);
    for (const name of Object.keys(calendrum)) {
        if (/^[a-z]/.test(name) && name !== "versions") assert.ok(methods.includes(name), `the worker lacks ${name}`);
    }
});

test("the types have no $metadata$ and no internal names of the compiler", () => {
    for (const file of readdirSync(PACKAGE).filter((name) => name.endsWith(".d.mts"))) {
        const text = readFileSync(join(PACKAGE, file), "utf8");
        assert.ok(!text.includes("$metadata$"), file);
        assert.ok(!/__doNotUseOrImplementIt|kotlin\./.test(text), file);
    }
    assert.deepEqual(readdirSync(join(PACKAGE, "lib")).filter((name) => name.endsWith(".d.mts") || name.endsWith(".map")), []);
});

test("package.json keeps it from being published and has no dependencies", () => {
    const manifest = loadJson(join(PACKAGE, "package.json"));
    assert.equal(manifest.private, true);
    assert.equal(manifest.name, "calendrum");
    assert.equal(manifest.type, "module");
    assert.equal(manifest.dependencies, undefined);
    assert.equal(manifest.license, "Apache-2.0");
    assert.deepEqual(Object.keys(manifest.exports), [".", "./node"]);
    for (const entry of Object.values(manifest.exports)) {
        assert.ok(existsSync(join(PACKAGE, entry.types)));
        assert.ok(existsSync(join(PACKAGE, entry.default)));
    }
});

test("it carries its license and notices", () => {
    assert.match(readFileSync(join(PACKAGE, "LICENSE"), "utf8"), /Apache License\s+Version 2\.0, January 2004/);
    assert.ok(readFileSync(join(PACKAGE, "NOTICE"), "utf8").length > 0);
    assert.match(readFileSync(join(PACKAGE, "THIRD-PARTY-LICENSES.txt"), "utf8"), /kotlinx-datetime/);
});

test("the code of the package reads no environment, and only the worker starts a process", () => {
    for (const file of readdirSync(PACKAGE).filter((name) => name.endsWith(".mjs"))) {
        const text = readFileSync(join(PACKAGE, file), "utf8");
        if (file !== "node.mjs") assert.ok(!text.includes("child_process"), file);
        for (const word of [file === "node.mjs" ? "process.env.HOME" : "process.env", "eval(", "node:net", "node:http", "fetch("]) {
            assert.ok(!text.includes(word), `${file}: ${word}`);
        }
    }
});

test("the models do not leak out of the public entries", async () => {
    const manifest = loadJson(join(PACKAGE, "package.json"));
    assert.equal(manifest.exports["./models.mjs"], undefined);
    const models = await moduleOf("models.mjs");
    for (const name of Object.keys(models)) assert.equal(calendrum[name], undefined, name);
});
