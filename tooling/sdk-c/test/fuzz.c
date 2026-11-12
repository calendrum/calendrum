/*
 * Random input for all the operations: random bytes, and the real requests of the contract spoiled in several ways. No crash, and
 * every response is one JSON object with ops_version, no OPS-INTERNAL, and nothing on the standard error.
 *
 * The input runs in worker processes, since a crash kills one and a call cannot be stopped from outside. A worker keeps its current
 * input in a shared file, so the input of a crash or a stall is not lost. A worker that does not finish a call in STALL_SECONDS is
 * killed and replaced, and that fails the test.
 *
 *   fuzz <cases directory> <scratch directory> [seconds] [seed] [workers]
 *   fuzz --worker <cases directory> <state file> <stderr file> <seconds> <seed>
 */
#include <fcntl.h>
#include <libproc.h>
#include <signal.h>
#include <spawn.h>
#include <stdatomic.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <unistd.h>

#include "calendrum.h"
#include "support.h"

extern char **environ;

#define STALL_SECONDS 10.0
#define MAX_WORKER_RSS ((uint64_t) 4 * 1024 * 1024 * 1024)
#define CAPACITY ((size_t) 512 * 1024)

typedef struct {
    atomic_long started;
    atomic_long finished;
    atomic_long ok;
    atomic_long errors;
    atomic_long no_response;
    atomic_long not_json;
    atomic_long internal;
    atomic_long slowest_us;
    char operation[64];
    atomic_size_t input_len;
    uint8_t input[CAPACITY];
} shared_state;

/* A small generator, so that a seed gives the same sequence on any libc. */
typedef struct {
    uint64_t state;
} generator;

static uint64_t next_random(generator *g) {
    uint64_t z = (g->state += 0x9E3779B97F4A7C15ULL);
    z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9ULL;
    z = (z ^ (z >> 27)) * 0x94D049BB133111EBULL;
    return z ^ (z >> 31);
}

static size_t below(generator *g, size_t bound) { return bound ? (size_t) (next_random(g) % bound) : 0; }

/* The grammar of JSON, strictly and without recursion: a stack of what is open. */
static int is_json_object(const uint8_t *s, size_t len) {
    static char stack[4096];
    size_t depth = 0, i = 0;
    enum { VALUE, AFTER_VALUE, KEY_OR_END, KEY, VALUE_OR_END } state = VALUE;
    if (len == 0 || s[0] != '{') return 0;
    while (i < len) {
        uint8_t c = s[i];
        if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
            i++;
            continue;
        }
        switch (state) {
            case VALUE:
            case VALUE_OR_END:
                if (c == ']' && state == VALUE_OR_END) {
                    depth--;
                    i++;
                    state = AFTER_VALUE;
                } else if (c == '{' || c == '[') {
                    if (depth >= sizeof stack) return 0;
                    stack[depth++] = (char) c;
                    i++;
                    state = c == '{' ? KEY_OR_END : VALUE_OR_END;
                } else if (c == '"') {
                    goto string;
                } else if (c == 't' && i + 4 <= len && !memcmp(s + i, "true", 4)) {
                    i += 4;
                    state = AFTER_VALUE;
                } else if (c == 'f' && i + 5 <= len && !memcmp(s + i, "false", 5)) {
                    i += 5;
                    state = AFTER_VALUE;
                } else if (c == 'n' && i + 4 <= len && !memcmp(s + i, "null", 4)) {
                    i += 4;
                    state = AFTER_VALUE;
                } else if (c == '-' || (c >= '0' && c <= '9')) {
                    i++;
                    while (i < len && ((s[i] >= '0' && s[i] <= '9') || s[i] == '.' || s[i] == 'e' || s[i] == 'E' || s[i] == '+' || s[i] == '-')) i++;
                    state = AFTER_VALUE;
                } else {
                    return 0;
                }
                break;
            case KEY_OR_END:
                if (c == '}') {
                    depth--;
                    i++;
                    state = AFTER_VALUE;
                    break;
                }
                /* fall through */
            case KEY:
                if (c != '"') return 0;
            string:
                i++;
                while (i < len && s[i] != '"') {
                    if (s[i] < 0x20) return 0;
                    if (s[i] == '\\') i++;
                    i++;
                }
                if (i >= len) return 0;
                i++;
                if (state == KEY || state == KEY_OR_END) {
                    while (i < len && (s[i] == ' ' || s[i] == '\n')) i++;
                    if (i >= len || s[i] != ':') return 0;
                    i++;
                    state = VALUE;
                } else {
                    state = AFTER_VALUE;
                }
                break;
            case AFTER_VALUE:
                if (depth == 0) return 0;
                if (c == ',') {
                    i++;
                    state = stack[depth - 1] == '{' ? KEY : VALUE;
                } else if (c == '}' && stack[depth - 1] == '{') {
                    depth--;
                    i++;
                } else if (c == ']' && stack[depth - 1] == '[') {
                    depth--;
                    i++;
                } else {
                    return 0;
                }
                break;
        }
    }
    return depth == 0 && state == AFTER_VALUE;
}

static contract_case *cases;
static int case_count;

static const char *operations[] = {"inspect", "validate", "fix", "expand", "rrule_expand", "freebusy", "build_event", "diff",
                                   "convert", "anonymize", "tz_resolve", "explain", "nothing", "", "\xFF\xFE", "inspect\x80"};
#define OPERATIONS (sizeof operations / sizeof operations[0])

static const char *fragments[] = {"\\u0000", "\\ud800", "\"", "\\", "{", "}", "[", "]", ",", ":", "null", "1e999", "-1", "99999999999",
                                  "BEGIN:VEVENT\\r\\n", "END:VCALENDAR\\r\\n", "RRULE:FREQ=SECONDLY\\r\\n", "\xF0\x9F\x93\x85", "\xC0\xAF",
                                  "\xED\xA0\x80", "TZID=", "\"limit\":100000,", "\"ics_base64\":\"", "Etc/GMT+14", "\xFF"};
#define FRAGMENTS (sizeof fragments / sizeof fragments[0])

/* Spoils the request in `out` and returns the new length. */
static size_t spoil(generator *g, const contract_case *c, uint8_t *out, size_t capacity) {
    size_t len = c->request_len < capacity ? c->request_len : capacity;
    memcpy(out, c->request, len);
    /* Mostly one or two changes, so that much of the input stays JSON and reaches the operation. */
    int rounds = 1 + (int) below(g, below(g, 4) ? 2 : 8);
    for (int r = 0; r < rounds; r++) {
        switch (below(g, 6)) {
            case 0:
                if (len) out[below(g, len)] ^= (uint8_t) (1u << below(g, 8));
                break;
            case 1:
                if (len) out[below(g, len)] = (uint8_t) next_random(g);
                break;
            case 2:
                len = below(g, len + 1);
                break;
            case 3: {
                const char *fragment = fragments[below(g, FRAGMENTS)];
                size_t size = strlen(fragment), at = below(g, len + 1);
                if (len + size < capacity) {
                    memmove(out + at + size, out + at, len - at);
                    memcpy(out + at, fragment, size);
                    len += size;
                }
                break;
            }
            case 4:
                if (len > 2) {
                    size_t from = below(g, len), size = below(g, len - from);
                    memmove(out + from, out + from + size, len - from - size);
                    len -= size;
                }
                break;
            default: {
                const contract_case *other = &cases[below(g, (size_t) case_count)];
                size_t at = below(g, len + 1), from = below(g, other->request_len), size = below(g, other->request_len - from);
                if (at + size < capacity) {
                    memcpy(out + at, other->request + from, size);
                    if (at + size > len) len = at + size;
                }
                break;
            }
        }
    }
    return len;
}

static int worker(const char *root, const char *state_path, const char *error_path, double seconds, uint64_t seed) {
    /* Anything the runtime prints goes here: an exception would show up as text, maybe with a piece of the input. */
    fflush(stderr);
    if (!freopen(error_path, "w", stderr)) return 2;
    int fd = open(state_path, O_RDWR);
    if (fd < 0) return 2;
    shared_state *state = mmap(NULL, sizeof(shared_state), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (state == MAP_FAILED) return 2;
    cases = load_cases(root, &case_count);

    generator g = {seed};
    uint8_t *input = state->input;
    double deadline = now_seconds() + seconds;
    while (now_seconds() < deadline) {
        size_t len;
        const char *operation;
        switch (below(&g, 4)) {
            case 0:
                /* Random bytes. */
                len = below(&g, 1 + (below(&g, 2) ? 64 : 4096));
                for (size_t i = 0; i < len; i++) input[i] = (uint8_t) next_random(&g);
                operation = operations[below(&g, OPERATIONS)];
                break;
            case 1: {
                /* A case, spoiled, with another operation. */
                const contract_case *c = &cases[below(&g, (size_t) case_count)];
                len = spoil(&g, c, input, CAPACITY);
                operation = operations[below(&g, OPERATIONS)];
                break;
            }
            default: {
                const contract_case *c = &cases[below(&g, (size_t) case_count)];
                len = spoil(&g, c, input, CAPACITY);
                operation = c->operation;
                break;
            }
        }
        snprintf(state->operation, sizeof state->operation, "%s", operation);
        state->input_len = len;
        state->started++;

        uint8_t *out = NULL;
        size_t out_len = 0;
        double start = now_seconds();
        int32_t code = calendrum_call(operation, input, len, &out, &out_len);
        long took = (long) ((now_seconds() - start) * 1e6);
        if (took > state->slowest_us) state->slowest_us = took;
        state->finished++;
        if (code != CALENDRUM_OK || !out) {
            state->no_response++;
            continue;
        }
        if (out[out_len] != 0 || !is_json_object(out, out_len) || strncmp((char *) out, "{\"ops_version\":1,", 17) != 0) {
            if (state->not_json++ < 5) printf("not JSON, %s: %.300s\n", operation, out);
        } else if (strstr((char *) out, "\"OPS-INTERNAL\"")) {
            if (state->internal++ < 5) printf("OPS-INTERNAL, %s, %zu bytes\n", operation, len);
        } else if (strncmp((char *) out + 17, "\"ok\":true", 9) == 0) {
            state->ok++;
        } else {
            state->errors++;
        }
        calendrum_free(out);
    }
    fflush(stdout);
    return 0;
}

typedef struct {
    pid_t pid;
    shared_state *state;
    char state_path[1024];
    char error_path[1024];
    long heartbeat;
    double heartbeat_at;
} slot;

static long calls, ok_calls, error_calls, no_response, not_json, internal, slowest_us;
static int stalls, crashes, too_large, kept_inputs;
static size_t error_bytes;

static void keep_input(const char *scratch, const char *kind, shared_state *state) {
    char path[1200];
    snprintf(path, sizeof path, "%s/fuzz-%s-%d.bin", scratch, kind, kept_inputs++);
    FILE *file = fopen(path, "wb");
    if (!file) return;
    fwrite(state->input, 1, state->input_len, file);
    fclose(file);
    printf("%s in the operation '%s', %zu bytes of input kept in %s\n", kind, state->operation, (size_t) state->input_len, path);
    fflush(stdout);
}

static void retire(slot *s) {
    calls += s->state->finished;
    ok_calls += s->state->ok;
    error_calls += s->state->errors;
    no_response += s->state->no_response;
    not_json += s->state->not_json;
    internal += s->state->internal;
    if (s->state->slowest_us > slowest_us) slowest_us = s->state->slowest_us;
    size_t len = 0;
    char *errors = read_all(s->error_path, &len);
    if (errors && len) {
        error_bytes += len;
        printf("the standard error of a worker: %.300s\n", errors);
    }
    free(errors);
}

static int start_worker(slot *s, char *self, const char *root, double seconds, uint64_t seed) {
    memset(s->state, 0, sizeof(shared_state));
    char seconds_text[32], seed_text[32];
    snprintf(seconds_text, sizeof seconds_text, "%.3f", seconds);
    snprintf(seed_text, sizeof seed_text, "%llu", (unsigned long long) seed);
    char *const arguments[] = {self, "--worker", (char *) root, s->state_path, s->error_path, seconds_text, seed_text, NULL};
    if (posix_spawn(&s->pid, self, NULL, NULL, arguments, environ) != 0) return 0;
    s->heartbeat = 0;
    s->heartbeat_at = now_seconds();
    return 1;
}

int main(int argc, char **argv) {
    /* A worker never reaches the code that starts workers. */
    if (argc >= 2 && strcmp(argv[1], "--worker") == 0) {
        if (argc != 7) return 2;
        return worker(argv[2], argv[3], argv[4], atof(argv[5]), strtoull(argv[6], NULL, 10));
    }
    if (argc < 3 || argc > 6) {
        fprintf(stderr, "usage: fuzz <cases directory> <scratch directory> [seconds] [seed] [workers]\n");
        return 2;
    }
    const char *root = argv[1], *scratch = argv[2];
    double seconds = argc > 3 ? atof(argv[3]) : 60;
    uint64_t seed = argc > 4 ? strtoull(argv[4], NULL, 10) : 20261105;
    int workers = argc > 5 ? atoi(argv[5]) : 4;
    if (workers < 1 || workers > 8) return 2;
    int count = 0;
    load_cases(root, &count);
    CHECK(count >= 36, "%d cases", count);

    static slot slots[8];
    for (int i = 0; i < workers; i++) {
        slot *s = &slots[i];
        snprintf(s->state_path, sizeof s->state_path, "%s/fuzz-state-%d", scratch, i);
        snprintf(s->error_path, sizeof s->error_path, "%s/fuzz-stderr-%d", scratch, i);
        int fd = open(s->state_path, O_RDWR | O_CREAT | O_TRUNC, 0600);
        if (fd < 0 || ftruncate(fd, sizeof(shared_state)) != 0) return 2;
        s->state = mmap(NULL, sizeof(shared_state), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        close(fd);
        if (s->state == MAP_FAILED) return 2;
    }

    double deadline = now_seconds() + seconds;
    uint64_t next_seed = seed;
    int running = 0;
    for (int i = 0; i < workers; i++) {
        if (!start_worker(&slots[i], argv[0], root, seconds, next_seed++ * 1000003)) return 2;
        running++;
    }
    /* A worker is replaced only while time is left, so the number of processes stays at `workers` and the loop ends. */
    while (running > 0) {
        usleep(20000);
        for (int i = 0; i < workers; i++) {
            slot *s = &slots[i];
            if (s->pid == 0) continue;
            int status = 0;
            int finished = waitpid(s->pid, &status, WNOHANG) == s->pid;
            int killed = 0;
            if (!finished) {
                long beat = s->state->started;
                if (beat != s->heartbeat) {
                    s->heartbeat = beat;
                    s->heartbeat_at = now_seconds();
                }
                struct proc_taskinfo info;
                int large = proc_pidinfo(s->pid, PROC_PIDTASKINFO, 0, &info, sizeof info) == (int) sizeof info &&
                            info.pti_resident_size > MAX_WORKER_RSS;
                if (large || now_seconds() - s->heartbeat_at > STALL_SECONDS) {
                    kill(s->pid, SIGKILL);
                    waitpid(s->pid, &status, 0);
                    killed = 1;
                    stalls++;
                    keep_input(scratch, large ? "large" : "stall", s->state);
                    if (large) too_large++;
                }
            } else if (WIFSIGNALED(status)) {
                crashes++;
                printf("a worker died of %s\n", strsignal(WTERMSIG(status)));
                keep_input(scratch, "crash", s->state);
                killed = 1;
            } else if (WEXITSTATUS(status) != 0) {
                crashes++;
                printf("a worker failed with %d\n", WEXITSTATUS(status));
                keep_input(scratch, "failure", s->state);
                killed = 1;
            }
            if (!finished && !killed) continue;
            retire(s);
            s->pid = 0;
            running--;
            double left = deadline - now_seconds();
            if (left > 0.05) {
                if (!start_worker(s, argv[0], root, left, next_seed++ * 1000003)) return 2;
                running++;
            }
        }
    }
    for (int i = 0; i < workers; i++) {
        remove(slots[i].state_path);
        remove(slots[i].error_path);
    }

    CHECK(crashes == 0, "%d workers crashed", crashes);
    CHECK(error_bytes == 0, "the standard error has %zu bytes", error_bytes);
    CHECK(no_response == 0, "%ld calls without a response", no_response);
    CHECK(not_json == 0, "%ld responses that are not JSON", not_json);
    CHECK(internal == 0, "%ld responses with OPS-INTERNAL", internal);
    CHECK(stalls == 0, "%d stalls", stalls);
    CHECK(calls > 1000, "only %ld calls", calls);
    printf("seed %llu, %d workers, %.0f s, %d cases: %ld calls finished, %ld ok, %ld errors of the contract, the slowest finished call %.1f ms\n",
           (unsigned long long) seed, workers, seconds, count, calls, ok_calls, error_calls, (double) slowest_us / 1e3);
    printf("stalls above %.0f s: %d; above 4 GiB: %d; crashes: %d\n", STALL_SECONDS, stalls, too_large, crashes);
    printf("fuzz: %d failed\n", failures);
    return failures ? 1 : 0;
}
