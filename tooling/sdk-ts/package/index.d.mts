import type {
    AnonymizeResult,
    BuildEventResult,
    ConvertResult,
    DiffResult,
    ExpandResult,
    ExplainResult,
    FixResult,
    FreebusyResult,
    InspectResult,
    RruleExpandResult,
    TzResolveResult,
    ValidateResult,
} from "./models.mjs";

export type * from "./models.mjs";
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
export type { WorkerErrorReason } from "./errors.mjs";

/**
 * A calendar: a string is its text and never a path, and bytes go in any encoding the library detects, such as Windows-1251.
 * To read a file, use `readCalendar` from `calendrum/node`.
 */
export type Calendar = string | Uint8Array;

export interface InspectOptions {
    /** Whether to add the calendar as jCal. */
    readonly includeJcal?: boolean;
    /** The most items of the lists of the response: 1000 by default, 100000 at most. */
    readonly limit?: number;
}

export interface ValidateOptions {
    /** `rfc5545` by default, or `lint`, which also says what `fix` can repair. */
    readonly rules?: "rfc5545" | "lint";
    /** The most items of the lists of the response: 1000 by default, 100000 at most. */
    readonly limit?: number;
}

export interface FixOptions {
    /** The repairs, in order. By default `by_prod_id`, then `lint`. An empty list means no repairs. */
    readonly fixups?: readonly (
        | "by_prod_id"
        | "windows_tzids"
        | "x_wr_timezone"
        | "x_wr_calname"
        | "outlook"
        | "lotus_notes"
        | "google"
        | "vcalendar10"
        | "lint"
    )[];
}

export interface WindowOptions {
    /** The start of the window, a moment in ISO 8601. */
    readonly from: string;
    /** The end of the window, excluded. */
    readonly to: string;
    /**
     * An IANA zone or an alias. It adds local times and is the zone of floating times, which are read in UTC without it.
     */
    readonly zone?: string;
    /** The most items of the lists of the response: 1000 by default, 100000 at most. */
    readonly limit?: number;
}

export interface ExpandOptions extends WindowOptions {
    /** Whether to keep cancelled occurrences. */
    readonly includeCancelled?: boolean;
}

export interface FreebusyOptions extends WindowOptions {
    /** Whether to look for conflicts; true by default. Above 2000 occurrences they are not computed, and the result is truncated. */
    readonly conflicts?: boolean;
}

export interface RruleExpandOptions {
    /** An IANA zone or an alias, for a local start. */
    readonly zone?: string;
    /** The most items of the lists of the response: 1000 by default, 100000 at most. */
    readonly limit?: number;
}

export interface Attendee {
    /** An address; mailto: is added when there is no scheme. */
    readonly address: string;
    readonly name?: string;
    readonly rsvp?: boolean;
}

export interface BuildEventOptions {
    /** In the form of the start; excluded for a date. Give this or `duration`. */
    readonly end?: string;
    /** In the form of RFC 5545 section 3.3.6, such as PT1H. */
    readonly duration?: string;
    /** An IANA zone or an alias. */
    readonly zone?: string;
    /** Whether the start and the end are dates. */
    readonly allDay?: boolean;
    readonly description?: string;
    readonly location?: string;
    /** A rule such as FREQ=WEEKLY;COUNT=3. */
    readonly rrule?: string;
    /** An address; mailto: is added when there is no scheme. */
    readonly organizer?: string;
    readonly attendees?: readonly Attendee[];
    /** Generated when absent. */
    readonly uid?: string;
    /** A moment; the clock by default. */
    readonly dtstamp?: string;
}

export interface DiffOptions {
    /** The most items of the lists of the response: 1000 by default, 100000 at most. */
    readonly limit?: number;
}

export interface AnonymizeOptions {
    /** Moves every date by whole weeks, so the days of the week stay; 0 by default. */
    readonly shiftWeeks?: number;
}

export interface TzResolveOptions {
    /** The moment of the offset; the clock by default. */
    readonly at?: string;
}

/** The input of `convert`: a calendar, or jCal (RFC 7265) as an array of calendars or one calendar. */
export type ConvertInput = Calendar | { readonly jcal: unknown };

/** The `result` of an operation as JSON values, from `call`. */
export type CallResult = Record<string, unknown> & {
    /**
     * Whether the result does not hold everything. In the contract it stands next to `result`, so it is not enumerable here and
     * does not reach JSON.
     */
    readonly truncated: boolean;
};

/** The versions of what is running. */
export interface Versions {
    /** This package. */
    readonly package: string;
    /** The operations contract. */
    readonly contract: number;
    /** The iCalendar library inside. */
    readonly library: string;
    /** The binding inside. */
    readonly sdk: string;
}

/** Reads a calendar and reports its charset, calendars, zones, events and the problems of parsing. */
export declare function inspect(calendar: Calendar, options?: InspectOptions): InspectResult;
/** Checks a calendar and lists its issues. */
export declare function validate(calendar: Calendar, options?: ValidateOptions): ValidateResult;
/** Repairs a calendar and returns the new text with the list of changes. */
export declare function fix(calendar: Calendar, options?: FixOptions): FixResult;
/** Lists the occurrences of a calendar in a window, in the order of each calendar. */
export declare function expand(calendar: Calendar, options: ExpandOptions): ExpandResult;
/** Lists the dates of a recurrence rule. The start is a date, a moment with its offset, or a local time with a zone. */
export declare function rruleExpand(rrule: string, start: string, options?: RruleExpandOptions): RruleExpandResult;
/** Merges the occurrences of a calendar into busy intervals, and finds the pairs that overlap. */
export declare function freebusy(calendar: Calendar, options: FreebusyOptions): FreebusyResult;
/** Writes a valid calendar with one event, with its `VTIMEZONE`. The start is a local time with a zone, a moment, or a date with `allDay`. */
export declare function buildEvent(summary: string, start: string, options?: BuildEventOptions): BuildEventResult;
/** Compares two calendars by the fingerprints of their components; `DTSTAMP` does not count. */
export declare function diff(before: Calendar, after: Calendar, options?: DiffOptions): DiffResult;
/** Converts a calendar to jCal, or jCal to a calendar. */
export declare function convert(input: ConvertInput): ConvertResult;
/** Removes the text and the addresses of a calendar, with a random key for each call that never leaves it. */
export declare function anonymize(calendar: Calendar, options?: AnonymizeOptions): AnonymizeResult;
/** Finds a time zone: its IANA identifier, its offset at a moment and the next change of the offset. */
export declare function tzResolve(name: string, options?: TzResolveOptions): TzResolveResult;
/** Tells the severity, the constant and the meaning of a problem code. */
export declare function explain(code: string): ExplainResult;
/** Runs an operation of the contract directly, by its name there: an object goes in, and the `result` of the response comes out. */
export declare function call(operation: string, request: Record<string, unknown>): CallResult;
/** Returns the versions of this package and of what it carries. */
export declare function versions(): Versions;
