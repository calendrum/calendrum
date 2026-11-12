// The operations as functions that run on the calling thread. Nothing here imports a module of Node.
import { call as nativeCall, versions as nativeVersions } from "./lib/calendrum-core.mjs";
import { OPERATIONS, checkedCall, readResponse, requestJson, toCallResult, toModel } from "./operations.mjs";
import { VERSION } from "./version.mjs";

export {
    BadArgumentError,
    CalendrumError,
    InternalError,
    LibraryError,
    TooLargeError,
    UnknownCodeError,
    UnknownOperationError,
    UnknownZoneError,
    WorkerError,
} from "./errors.mjs";

function run(name, ...args) {
    const { wire, request, decode } = OPERATIONS[name];
    const { result, truncated } = readResponse(nativeCall(wire, requestJson(request(...args))));
    return toModel(decode, result, truncated);
}

export const inspect = (calendar, options) => run("inspect", calendar, options);
export const validate = (calendar, options) => run("validate", calendar, options);
export const fix = (calendar, options) => run("fix", calendar, options);
export const expand = (calendar, options) => run("expand", calendar, options);
export const rruleExpand = (rrule, start, options) => run("rruleExpand", rrule, start, options);
export const freebusy = (calendar, options) => run("freebusy", calendar, options);
export const buildEvent = (summary, start, options) => run("buildEvent", summary, start, options);
export const diff = (before, after, options) => run("diff", before, after, options);
export const convert = (input) => run("convert", input);
export const anonymize = (calendar, options) => run("anonymize", calendar, options);
export const tzResolve = (name, options) => run("tzResolve", name, options);
export const explain = (code) => run("explain", code);

export function call(operation, request) {
    const { result, truncated } = readResponse(nativeCall(operation, checkedCall(operation, request)));
    return toCallResult(result, truncated);
}

export function versions() {
    const { contract, library, sdk } = JSON.parse(nativeVersions());
    return { package: VERSION, contract, library, sdk };
}
