// The errors of the package. Their text comes from the contract or from this package, never from the input.

export class CalendrumError extends Error {
    constructor(code, message) {
        super(message);
        this.name = new.target.name;
        this.code = code;
    }
}

export class BadArgumentError extends CalendrumError {}

export class UnknownOperationError extends CalendrumError {}

export class UnknownZoneError extends CalendrumError {}

export class UnknownCodeError extends CalendrumError {}

export class TooLargeError extends CalendrumError {}

export class InternalError extends CalendrumError {}

// Not a CalendrumError: code that handles a bad calendar with `instanceof CalendrumError` must not also hide a broken deployment.
export class LibraryError extends Error {
    constructor(message) {
        super(message);
        this.name = new.target.name;
    }
}

export class WorkerError extends LibraryError {
    constructor(reason, message) {
        super(message);
        this.reason = reason;
    }
}

const BY_CODE = new Map([
    ["OPS-BAD-ARGUMENT", BadArgumentError],
    ["OPS-UNKNOWN-OPERATION", UnknownOperationError],
    ["OPS-UNKNOWN-ZONE", UnknownZoneError],
    ["OPS-UNKNOWN-CODE", UnknownCodeError],
    ["OPS-TOO-LARGE", TooLargeError],
    ["OPS-INTERNAL", InternalError],
]);

// A code this version does not know gets the base class, as the contract can grow.
export function errorFor(code, message) {
    const type = BY_CODE.get(code) ?? CalendrumError;
    return new type(code, message);
}
