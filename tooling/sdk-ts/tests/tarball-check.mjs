// Installs the tarball into a clean directory, without the network, and uses it from ESM and from CommonJS.
//
// Usage: node tarball-check.mjs <calendrum-x.y.z.tgz>
import { execFileSync } from "node:child_process";
import { mkdtempSync, realpathSync, rmSync, writeFileSync } from "node:fs";
import { tmpdir } from "node:os";
import { join, resolve } from "node:path";

const tarball = resolve(process.argv[2]);
const failures = [];

function check(condition, text) {
    console.log(`${condition ? "ok" : "FAILED"}: ${text}`);
    if (!condition) failures.push(text);
}

const names = execFileSync("tar", ["-tzf", tarball], { encoding: "utf8" }).split("\n").filter(Boolean);
check(names.every((name) => name.startsWith("package/")), "every file is under package/, as npm expects");
for (const name of ["package.json", "index.mjs", "index.d.mts", "node.mjs", "node.d.mts", "models.d.mts", "errors.d.mts", "worker-main.mjs", "LICENSE",
    "NOTICE", "THIRD-PARTY-LICENSES.txt", "lib/calendrum-core.mjs"]) {
    check(names.includes(`package/${name}`), `it has ${name}`);
}
check(!names.some((name) => name.endsWith(".map") || name.includes("node_modules") || name.includes(".test.")), "no maps and nothing of the tests");

const directory = realpathSync(mkdtempSync(join(tmpdir(), "calendrum-")));
try {
    writeFileSync(join(directory, "package.json"), '{"name":"consumer","version":"1.0.0","private":true}');
    const npm = ["install", "--offline", "--no-audit", "--no-fund", "--ignore-scripts", "--no-package-lock", tarball];
    execFileSync("npm", npm, { cwd: directory, stdio: "pipe", timeout: 120_000 });
    check(true, "npm installs it without the network");

    const ics = "BEGIN:VCALENDAR\\r\\nVERSION:2.0\\r\\nPRODID:x\\r\\nBEGIN:VEVENT\\r\\nUID:a@example.com\\r\\nDTSTAMP:20260101T000000Z\\r\\nDTSTART:20260105T100000Z\\r\\nEND:VEVENT\\r\\nEND:VCALENDAR\\r\\n";
    writeFileSync(join(directory, "esm.mjs"), `
        import { inspect, versions } from "calendrum";
        import { CalendrumWorker } from "calendrum/node";
        const worker = new CalendrumWorker({ timeoutMs: 30000 });
        const answer = await worker.inspect("${ics}");
        await worker.close();
        console.log(inspect("${ics}").calendars.length, answer.calendars.length, versions().package);
    `);
    const esm = execFileSync(process.execPath, ["esm.mjs"], { cwd: directory, encoding: "utf8", timeout: 60_000 }).trim();
    check(/^1 1 \d/.test(esm), `import and the worker work: ${esm}`);

    writeFileSync(join(directory, "cjs.cjs"), `
        const { inspect } = require("calendrum");
        console.log(inspect("${ics}").calendars.length);
    `);
    const cjs = execFileSync(process.execPath, ["cjs.cjs"], { cwd: directory, encoding: "utf8", timeout: 60_000, stdio: ["ignore", "pipe", "ignore"] }).trim();
    check(cjs === "1", `require works: ${cjs}`);

    let refused = false;
    try {
        execFileSync(process.execPath, ["--input-type=module", "-e", 'await import("calendrum/models.mjs")'], { cwd: directory, stdio: "ignore", timeout: 60_000 });
    } catch {
        refused = true;
    }
    check(refused, "what is not in its exports cannot be imported");
} finally {
    rmSync(directory, { recursive: true, force: true });
}
if (failures.length > 0) process.exit(1);
