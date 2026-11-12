// What the entry points share: the requests of the operations, the JSON on both sides, and the warnings.
import { BadArgumentError, LibraryError, TooLargeError, errorFor } from "./errors.mjs";
import * as models from "./models.mjs";

const NOT_THE_CONTRACT = "The library gave a response that does not follow the contract";

// The limit of the contract for one calendar. Larger bytes are refused here, before they are copied into base64.
const CALENDAR_LIMIT = 64 * 1024 * 1024;

function base64Of(bytes) {
    const { Buffer } = globalThis;
    if (Buffer !== undefined) return Buffer.from(bytes.buffer, bytes.byteOffset, bytes.byteLength).toString("base64");
    if (typeof bytes.toBase64 === "function") return bytes.toBase64();
    // Without a Node Buffer or the newer method. A chunk is a multiple of 3 bytes, so that the pieces of base64 join up.
    let text = "";
    for (let start = 0; start < bytes.length; start += 0x8000 * 3) {
        text += btoa(String.fromCharCode.apply(null, bytes.subarray(start, start + 0x8000 * 3)));
    }
    return text;
}

function putCalendar(request, field, calendar) {
    if (typeof calendar === "string") {
        request[field] = calendar;
    } else if (calendar instanceof Uint8Array) {
        if (calendar.length > CALENDAR_LIMIT) throw new TooLargeError("OPS-TOO-LARGE", "The calendar is larger than the limit");
        request[`${field}_base64`] = base64Of(calendar);
    } else {
        throw new TypeError("A calendar is a string or a Uint8Array; to read a file, use readCalendar from calendrum/node");
    }
}

function table(names) {
    return Object.assign(Object.create(null), names);
}

function putOptions(request, operation, given, allowed) {
    if (given === undefined) return request;
    if (given === null || typeof given !== "object" || Array.isArray(given)) {
        throw new TypeError(`The options of ${operation} are an object`);
    }
    for (const key of Object.keys(given)) {
        const wire = allowed[key];
        // The name is not quoted: an object of options can come straight from a request of a stranger.
        if (wire === undefined) throw new TypeError(`${operation} takes the options ${Object.keys(allowed).join(", ")}`);
        if (given[key] !== undefined) request[wire] = given[key];
    }
    return request;
}

const INSPECT = table({ includeJcal: "include_jcal", limit: "limit" });
const VALIDATE = table({ rules: "rules", limit: "limit" });
const FIX = table({ fixups: "fixups" });
const EXPAND = table({ from: "from", to: "to", zone: "zone", limit: "limit", includeCancelled: "include_cancelled" });
const RRULE_EXPAND = table({ zone: "zone", limit: "limit" });
const FREEBUSY = table({ from: "from", to: "to", zone: "zone", conflicts: "conflicts", limit: "limit" });
const BUILD_EVENT = table({
    end: "end",
    duration: "duration",
    zone: "zone",
    allDay: "all_day",
    description: "description",
    location: "location",
    rrule: "rrule",
    organizer: "organizer",
    attendees: "attendees",
    uid: "uid",
    dtstamp: "dtstamp",
});
const DIFF = table({ limit: "limit" });
const ANONYMIZE = table({ shiftWeeks: "shift_weeks" });
const TZ_RESOLVE = table({ at: "at" });

function withCalendar(operation, calendar, given, allowed) {
    const request = {};
    putCalendar(request, "ics", calendar);
    return putOptions(request, operation, given, allowed);
}

// Each operation: its name in the contract, how its arguments make a request, and how a result becomes a model.
export const OPERATIONS = {
    inspect: {
        wire: "inspect",
        request: (calendar, given) => withCalendar("inspect", calendar, given, INSPECT),
        decode: models.decodeInspect,
    },
    validate: {
        wire: "validate",
        request: (calendar, given) => withCalendar("validate", calendar, given, VALIDATE),
        decode: models.decodeValidate,
    },
    fix: {
        wire: "fix",
        request: (calendar, given) => withCalendar("fix", calendar, given, FIX),
        decode: models.decodeFix,
    },
    expand: {
        wire: "expand",
        request: (calendar, given) => withCalendar("expand", calendar, given, EXPAND),
        decode: models.decodeExpand,
    },
    rruleExpand: {
        wire: "rrule_expand",
        request: (rrule, start, given) => putOptions({ rrule, start }, "rruleExpand", given, RRULE_EXPAND),
        decode: models.decodeRruleExpand,
    },
    freebusy: {
        wire: "freebusy",
        request: (calendar, given) => withCalendar("freebusy", calendar, given, FREEBUSY),
        decode: models.decodeFreebusy,
    },
    buildEvent: {
        wire: "build_event",
        request: (summary, start, given) => putOptions({ summary, start }, "buildEvent", given, BUILD_EVENT),
        decode: models.decodeBuildEvent,
    },
    diff: {
        wire: "diff",
        request: (before, after, given) => {
            const request = {};
            putCalendar(request, "before", before);
            putCalendar(request, "after", after);
            return putOptions(request, "diff", given, DIFF);
        },
        decode: models.decodeDiff,
    },
    convert: {
        wire: "convert",
        request: (input) => {
            if (typeof input === "string" || input instanceof Uint8Array) return withCalendar("convert", input);
            if (input === null || typeof input !== "object") {
                throw new TypeError("convert takes a calendar, or an object with the field jcal");
            }
            return { jcal: input.jcal };
        },
        decode: models.decodeConvert,
    },
    anonymize: {
        wire: "anonymize",
        request: (calendar, given) => withCalendar("anonymize", calendar, given, ANONYMIZE),
        decode: models.decodeAnonymize,
    },
    tzResolve: {
        wire: "tz_resolve",
        request: (name, given) => putOptions({ name }, "tzResolve", given, TZ_RESOLVE),
        decode: models.decodeTzResolve,
    },
    explain: {
        wire: "explain",
        request: (code) => ({ code }),
        decode: models.decodeExplain,
    },
};

class NotJson extends Error {}

// What JSON would change in silence. A number that is not finite would turn into null, which means "not given", and a lone surrogate
// would reach the library as a replacement character. The Python package refuses both.
function checked(_key, value) {
    if (typeof value === "number" && !Number.isFinite(value)) throw new NotJson();
    if (typeof value === "string" && value.isWellFormed?.() === false) throw new NotJson();
    return value;
}

// No `cause`: an error of JSON can name the keys of what it could not write.
export function requestJson(request) {
    try {
        return JSON.stringify(request, checked);
    } catch (error) {
        if (error instanceof NotJson) throw new BadArgumentError("OPS-BAD-ARGUMENT", "The request is not valid JSON");
        if (error instanceof RangeError) {
            if (/call stack/i.test(error.message)) throw new BadArgumentError("OPS-BAD-ARGUMENT", "The request is nested too deeply");
            throw new TooLargeError("OPS-TOO-LARGE", "The request is larger than the limit");
        }
        throw new TypeError("The request cannot be written as JSON");
    }
}

const warned = new Set();

// Once for each kind in a process: a server would otherwise write it for every call. The field of the result stays the check.
function warnOnce(code, text) {
    if (warned.has(code)) return;
    warned.add(code);
    globalThis.process?.emitWarning?.(text, { type: "CalendrumWarning", code });
}

function parsed(text) {
    try {
        return JSON.parse(text);
    } catch {
        return null;
    }
}

// Reads a response. An operation that failed comes out as its error, and the result comes out with its truncation.
export function readResponse(text) {
    const response = parsed(text);
    if (response === null || typeof response !== "object") throw new LibraryError(NOT_THE_CONTRACT);
    if (response.ok === true) {
        const { result } = response;
        if (result === null || typeof result !== "object" || Array.isArray(result)) throw new LibraryError(NOT_THE_CONTRACT);
        const truncated = response.truncated === true;
        if (truncated) {
            warnOnce("CALENDRUM_TRUNCATED", "A result does not hold everything: a limit cut a list, or the input was not read whole. Check its field truncated.");
        }
        return { result, truncated };
    }
    const { error } = response;
    if (error === null || typeof error !== "object" || error.code === undefined || error.message === undefined) {
        throw new LibraryError(NOT_THE_CONTRACT);
    }
    throw errorFor(error.code, error.message);
}

export function toModel(decode, result, truncated) {
    try {
        return decode(result, truncated);
    } catch {
        throw new LibraryError(NOT_THE_CONTRACT);
    }
}

// `truncated` stands next to `result` in the contract, so that a key of the result would not be the contract any more.
export function toCallResult(result, truncated) {
    return Object.defineProperty(result, "truncated", { value: truncated, enumerable: false });
}

export function checkedCall(operation, request) {
    if (typeof operation !== "string") throw new TypeError("An operation is a string");
    if (request === null || typeof request !== "object" || Array.isArray(request)) throw new TypeError("A request is an object");
    return requestJson(request);
}
