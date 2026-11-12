import { readFileSync } from "node:fs";
import { abiVersion, occurrences, parse } from "../build/dist/js/productionLibrary/calendrum.mjs";
import type { OccurrenceOutput } from "../build/dist/js/productionLibrary/calendrum.mjs";

const file = process.argv[2] ?? new URL("../../../examples/shared/sample.ics", import.meta.url).pathname;
const text: string = readFileSync(file, "utf8");

const parsed = parse(text);
const jcal: unknown[] = JSON.parse(parsed.jcal);
console.log(`abi ${abiVersion()}, ${jcal.length} calendar(s), ${parsed.problems.length} problem(s)`);

const found: OccurrenceOutput[] = occurrences(text, "2026-09-01T00:00:00Z", "2026-10-01T00:00:00Z", "Europe/Berlin");
for (const occurrence of found) {
  console.log(`${occurrence.localStart} ${occurrence.summary}`);
}

const rounds = Number(process.argv[3] ?? 2000);
for (let i = 0; i < rounds / 10; i++) parse(text);
let start = performance.now();
for (let i = 0; i < rounds; i++) parse(text);
const parseMicros = ((performance.now() - start) * 1000) / rounds;
start = performance.now();
for (let i = 0; i < rounds; i++) occurrences(text, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", "Europe/Berlin");
const occurrencesMicros = ((performance.now() - start) * 1000) / rounds;
console.log(`ts parse ${parseMicros.toFixed(1)} us, occurrences ${occurrencesMicros.toFixed(1)} us`);
