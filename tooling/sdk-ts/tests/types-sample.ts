// Checked by types-check.mjs and never run. Each @ts-expect-error is a mistake the types have to catch: the compiler fails on one
// that catches nothing.
import {
    BadArgumentError,
    CalendrumError,
    LibraryError,
    WorkerError,
    buildEvent,
    call,
    convert,
    diff,
    expand,
    explain,
    fix,
    freebusy,
    inspect,
    rruleExpand,
    tzResolve,
    validate,
    versions,
} from "calendrum";
import type { Calendar, CallResult, ExpandResult, InspectResult, Occurrence, Problem, Versions, WorkerErrorReason } from "calendrum";
import { CalendrumWorker, readCalendar } from "calendrum/node";

declare const text: string;
const bytes: Uint8Array = new Uint8Array(0);
const window = { from: "2026-01-01T00:00:00Z", to: "2026-02-01T00:00:00Z" };

const calendars: Calendar[] = [text, bytes, readCalendar("event.ics"), readCalendar(new URL("file:///event.ics"))];

const inspected: InspectResult = inspect(calendars[0]!, { includeJcal: true, limit: 10 });
const count: number = inspected.problemCount;
const summary: string | undefined = inspected.calendars[0]?.events[0]?.summary;
const problem: Problem | undefined = inspected.problems[0];
const expanded: ExpandResult = expand(text, { ...window, zone: "Europe/Berlin", includeCancelled: true });
const occurrence: Occurrence | undefined = expanded.occurrences[0];
const truncated: boolean = expanded.truncated;
const offset: number = tzResolve("Europe/Berlin", { at: "2026-01-01T00:00:00Z" }).offsetSeconds;
const rule: string = rruleExpand("FREQ=DAILY", "2026-01-01").dates[0]!.date;
const built: string = buildEvent("Title", "2026-01-05T10:00:00Z", { duration: "PT1H", attendees: [{ address: "a@example.com", rsvp: true }] }).ics;
const changed: boolean = fix(bytes, { fixups: ["lint", "google"] }).changed;
const valid: boolean = validate(text, { rules: "lint" }).valid;
const busy: number = freebusy(text, { ...window, conflicts: false }).busy.length;
const compared: number = diff(text, bytes, { limit: 5 }).unchangedCount;
const jcal: readonly unknown[] | undefined = convert(text).jcal;
const back: string | undefined = convert({ jcal }).ics;
const meaning: string = explain("ICAL-S003").meaning;
const raw: CallResult = call("explain", { code: "ICAL-S003" });
const rawTruncated: boolean = raw.truncated;
const running: Versions = versions();

async function worker(): Promise<void> {
    await using scoped = new CalendrumWorker({ timeoutMs: 1000, maxMemoryMb: 64 });
    const result: ExpandResult = await scoped.expand(text, window);
    const direct: CallResult = await scoped.call("explain", { code: "ICAL-S003" });
    await scoped.close();
    void [result, direct];
}

function errors(error: unknown): string {
    if (error instanceof WorkerError) {
        const reason: WorkerErrorReason = error.reason;
        return reason;
    }
    if (error instanceof BadArgumentError) return error.code;
    if (error instanceof CalendrumError) return error.message;
    if (error instanceof LibraryError) return error.message;
    return "";
}

// @ts-expect-error A path is not a calendar in any form but a string, which is its text.
inspect(new URL("file:///event.ics"));
// @ts-expect-error A number is not a calendar.
inspect(5);
// @ts-expect-error The window is required.
expand(text);
// @ts-expect-error The end of the window is required.
expand(text, { from: window.from });
// @ts-expect-error A misspelled option.
expand(text, { ...window, includeCanceled: true });
// @ts-expect-error The name in the contract is not the name here.
inspect(text, { include_jcal: true });
// @ts-expect-error There are two sets of rules.
validate(text, { rules: "strict" });
// @ts-expect-error There is no such repair.
fix(text, { fixups: ["everything"] });
// @ts-expect-error A result cannot be changed.
expanded.count = 7;
// @ts-expect-error A list of a result cannot be changed.
expanded.occurrences.push(occurrence!);
// @ts-expect-error The name in the contract is not the name here.
void expanded.problem_count;
// @ts-expect-error An optional field can be absent.
const certain: string = occurrence!.summary;
// @ts-expect-error A method of a worker gives a promise.
const notYet: ExpandResult = new CalendrumWorker().expand(text, window);
// @ts-expect-error An attendee has an address.
buildEvent("Title", "2026-01-05", { attendees: [{ name: "No address" }] });
// @ts-expect-error A worker has only these options.
new CalendrumWorker({ timeout: 5 });

void [calendars, count, summary, problem, truncated, offset, rule, built, changed, valid, busy, compared, back, meaning, rawTruncated, running, worker, errors, certain, notYet];
