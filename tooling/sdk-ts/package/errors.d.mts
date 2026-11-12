/**
 * An operation refused the request. The message is a fixed sentence, which never quotes the request or the calendar.
 *
 * A problem of a calendar is not an error: the operation succeeds and lists it in `problems`.
 */
export declare class CalendrumError extends Error {
    constructor(code: string, message: string);
    /** A stable code of the contract, such as OPS-BAD-ARGUMENT. */
    readonly code: string;
}

/** A field has the wrong type or value, or the fields do not make a valid request. */
export declare class BadArgumentError extends CalendrumError {}

/** There is no such operation. */
export declare class UnknownOperationError extends CalendrumError {}

/** A zone of the request is not in the time zone data. */
export declare class UnknownZoneError extends CalendrumError {}

/** `explain` does not know the code. */
export declare class UnknownCodeError extends CalendrumError {}

/** The request or the calendar is above the limit, or a response that is not a list is. */
export declare class TooLargeError extends CalendrumError {}

/** A bug of the library. No operation should ever give it. */
export declare class InternalError extends CalendrumError {}

/**
 * The library cannot be used here, or it answered outside the contract.
 *
 * This is not a `CalendrumError`: code that handles a bad calendar must not also hide a broken deployment.
 */
export declare class LibraryError extends Error {
    constructor(message: string);
}

/** A call through `CalendrumWorker` did not get its answer. */
export declare class WorkerError extends LibraryError {
    constructor(reason: WorkerErrorReason, message: string);
    /** Why: the time ran out, the memory ran out, the process died, or the worker was closed. */
    readonly reason: WorkerErrorReason;
}

/** Why a call through `CalendrumWorker` did not get its answer. */
export type WorkerErrorReason = "timeout" | "memory" | "crash" | "closed";
