import type {
    AnonymizeOptions,
    BuildEventOptions,
    Calendar,
    CallResult,
    ConvertInput,
    DiffOptions,
    ExpandOptions,
    FixOptions,
    FreebusyOptions,
    InspectOptions,
    RruleExpandOptions,
    TzResolveOptions,
    ValidateOptions,
} from "./index.mjs";
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

export { WorkerError } from "./errors.mjs";

/**
 * Reads a file for an operation: only a regular file, as a FIFO or a device is refused at once, and a file above 64 MiB is refused
 * without reading it. Symbolic links are followed, so do not pass a path that comes from a stranger.
 *
 * @throws BadArgumentError The path is not a regular file.
 * @throws TooLargeError The file is above the limit of a calendar.
 * @throws Error The usual error of Node for a path that cannot be read.
 */
export declare function readCalendar(path: string | URL): Uint8Array;

export interface WorkerOptions {
    /** How long a call may run, from the moment it starts. Past it the process is killed and the call fails with `WorkerError`. */
    readonly timeoutMs?: number;
    /** The limit of the heap of the process. Past it the process dies and the call fails with `WorkerError`; your program stays. */
    readonly maxMemoryMb?: number;
}

/**
 * The operations in a process of their own: the event loop goes on during a call, a call can be stopped, and running out of memory
 * does not end your program.
 *
 * Calls run one at a time, in the order they were made. A call that runs out of time or memory takes the process with it, and the next
 * one gets a new process; a `CalendrumError` does not. An idle worker does not keep your program alive, and the process ends with your
 * program. Close it to end the process sooner; `await using` does that too.
 */
export declare class CalendrumWorker implements AsyncDisposable {
    constructor(options?: WorkerOptions);
    inspect(calendar: Calendar, options?: InspectOptions): Promise<InspectResult>;
    validate(calendar: Calendar, options?: ValidateOptions): Promise<ValidateResult>;
    fix(calendar: Calendar, options?: FixOptions): Promise<FixResult>;
    expand(calendar: Calendar, options: ExpandOptions): Promise<ExpandResult>;
    rruleExpand(rrule: string, start: string, options?: RruleExpandOptions): Promise<RruleExpandResult>;
    freebusy(calendar: Calendar, options: FreebusyOptions): Promise<FreebusyResult>;
    buildEvent(summary: string, start: string, options?: BuildEventOptions): Promise<BuildEventResult>;
    diff(before: Calendar, after: Calendar, options?: DiffOptions): Promise<DiffResult>;
    convert(input: ConvertInput): Promise<ConvertResult>;
    anonymize(calendar: Calendar, options?: AnonymizeOptions): Promise<AnonymizeResult>;
    tzResolve(name: string, options?: TzResolveOptions): Promise<TzResolveResult>;
    explain(code: string): Promise<ExplainResult>;
    /** Runs an operation of the contract directly, as `call` of the package does. */
    call(operation: string, request: Record<string, unknown>): Promise<CallResult>;
    /** Ends the process. The calls in progress and in the queue fail with `WorkerError`, and so does any call after this. */
    close(): Promise<void>;
    [Symbol.asyncDispose](): Promise<void>;
}
