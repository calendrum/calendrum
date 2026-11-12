/*
 * The smallest stack that works. A process for each combination, since a stack overflow kills it: a thread with the stack of
 * the given size makes the first call of the process or a later one, on hostile input and on every case of the contract.
 *
 *   stack <cases directory> [required KiB] [--small]           the table; a stack from this size up has to work
 *                                                              --small also tries 32 and 16 KiB, which crash on purpose: each
 *                                                              crash leaves a report in ~/Library/Logs/DiagnosticReports
 *   stack --one <KiB> <first|later> <input> <cases directory>  one combination
 */
#include <pthread.h>
#include <signal.h>
#include <spawn.h>
#include <sys/wait.h>
#include <unistd.h>

#include "calendrum.h"
#include "support.h"

extern char **environ;

typedef struct {
    const char *input;
    const char *cases;
    int failed;
} work;

static void call_checked(work *w, const char *operation, const char *request, size_t len, const char *code) {
    uint8_t *out = NULL;
    size_t out_len = 0;
    int32_t result = calendrum_call(operation, (const uint8_t *) request, len, &out, &out_len);
    if (result != CALENDRUM_OK) w->failed = 1;
    if (out && strstr((char *) out, "OPS-INTERNAL")) w->failed = 1;
    if (code && !(out && strstr((char *) out, code))) w->failed = 1;
    calendrum_free(out);
}

/* 2 million BEGIN without END. */
static buffer nested_begin(void) {
    buffer b = {0};
    const char *line = "BEGIN:X" CRLF;
    size_t size = strlen(line);
    buffer_reserve(&b, 2000000 * size + 16);
    buffer_puts(&b, "{\"ics\":\"");
    for (int i = 0; i < 2000000; i++) {
        memcpy(b.data + b.len, line, size);
        b.len += size;
    }
    buffer_puts(&b, "\"}");
    return b;
}

/* A jCal at the limit of nesting: a component takes two levels, an object of the request one. */
static buffer deep_jcal(int components) {
    buffer b = {0};
    buffer_puts(&b, "{\"jcal\":");
    for (int i = 0; i < components; i++) buffer_puts(&b, i == 0 ? "[\"vcalendar\",[[\"version\",{},\"text\",\"2.0\"]],[" : "[\"x-deep\",[],[");
    for (int i = 0; i < components; i++) buffer_puts(&b, "]]");
    buffer_puts(&b, "}");
    return b;
}

/* The same limit reached inside the value of a property, and inside its parameters. */
static buffer deep_jcal_value(int depth) {
    buffer b = {0};
    buffer_puts(&b, "{\"jcal\":[\"vcalendar\",[[\"x-a\",{},\"unknown\",");
    for (int i = 0; i < depth; i++) buffer_puts(&b, "[");
    for (int i = 0; i < depth; i++) buffer_puts(&b, "]");
    buffer_puts(&b, "]],[]]}");
    return b;
}

static buffer deep_jcal_parameter(int depth) {
    buffer b = {0};
    buffer_puts(&b, "{\"jcal\":[\"vcalendar\",[[\"x-a\",");
    for (int i = 0; i < depth; i++) buffer_puts(&b, "{\"a\":");
    buffer_puts(&b, "1");
    for (int i = 0; i < depth; i++) buffer_puts(&b, "}");
    buffer_puts(&b, ",\"text\",\"v\"]],[]]}");
    return b;
}

/* A nesting far above the limit, of each kind of bracket. */
static buffer deep_json(char open, int depth) {
    buffer b = {0};
    buffer_reserve(&b, (size_t) depth * 8);
    for (int i = 0; i < depth; i++) buffer_puts(&b, open == '[' ? "[" : "{\"a\":");
    return b;
}

static void *run(void *argument) {
    work *w = argument;
    if (strcmp(w->input, "begin") == 0) {
        buffer b = nested_begin();
        call_checked(w, "inspect", b.data, b.len, "\"ok\":true");
        call_checked(w, "validate", b.data, b.len, "\"ok\":true");
        free(b.data);
    } else if (strcmp(w->input, "jcal") == 0) {
        buffer b = deep_jcal(31);
        call_checked(w, "convert", b.data, b.len, "\"ok\":true");
        free(b.data);
        b = deep_jcal(32);
        call_checked(w, "convert", b.data, b.len, "OPS-BAD-ARGUMENT");
        free(b.data);
        b = deep_jcal_value(60);
        call_checked(w, "convert", b.data, b.len, "\"ok\":true");
        free(b.data);
        b = deep_jcal_parameter(60);
        call_checked(w, "convert", b.data, b.len, "\"ok\":true");
        free(b.data);
    } else if (strcmp(w->input, "json") == 0) {
        buffer b = deep_json('[', 2000000);
        call_checked(w, "inspect", b.data, b.len, "OPS-BAD-ARGUMENT");
        free(b.data);
        b = deep_json('{', 2000000);
        call_checked(w, "inspect", b.data, b.len, "OPS-BAD-ARGUMENT");
        free(b.data);
    } else if (strcmp(w->input, "feed") == 0) {
        buffer b = inspect_request(200);
        call_checked(w, "inspect", b.data, b.len, "\"ok\":true");
        free(b.data);
        b = expand_request(200);
        call_checked(w, "expand", b.data, b.len, "\"ok\":true");
        free(b.data);
    } else {
        int count = 0;
        contract_case *cases = load_cases(w->cases, &count);
        for (int i = 0; i < count; i++) call_checked(w, cases[i].operation, cases[i].request, cases[i].request_len, NULL);
    }
    return NULL;
}

static int one(int kib, const char *when, const char *input, const char *cases) {
    if (strcmp(when, "later") == 0) {
        CHECK(calendrum_call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &(uint8_t *){NULL}, &(size_t){0}) == 0, "first call");
    }
    work w = {input, cases, 0};
    pthread_attr_t attributes;
    pthread_attr_init(&attributes);
    if (pthread_attr_setstacksize(&attributes, (size_t) kib * 1024) != 0) return 3;
    pthread_t id;
    if (pthread_create(&id, &attributes, run, &w) != 0) return 3;
    pthread_join(id, NULL);
    return w.failed ? 1 : 0;
}

/* The outcome of one combination, in the words of the table. */
static const char *spawn_one(const char *self, int kib, const char *when, const char *input, const char *cases) {
    char size[16];
    snprintf(size, sizeof size, "%d", kib);
    char *const arguments[] = {(char *) self, "--one", size, (char *) when, (char *) input, (char *) cases, NULL};
    pid_t pid;
    if (posix_spawn(&pid, self, NULL, NULL, arguments, environ) != 0) return "not started";
    double deadline = now_seconds() + 120;
    int status = 0;
    for (;;) {
        pid_t done = waitpid(pid, &status, WNOHANG);
        if (done == pid) break;
        if (now_seconds() > deadline) {
            kill(pid, SIGKILL);
            waitpid(pid, &status, 0);
            return "HANG";
        }
        usleep(5000);
    }
    if (WIFSIGNALED(status)) {
        static char text[32];
        snprintf(text, sizeof text, "CRASH %s", strsignal(WTERMSIG(status)));
        return text;
    }
    if (WEXITSTATUS(status) == 3) return "refused size";
    return WEXITSTATUS(status) == 0 ? "ok" : "WRONG ANSWER";
}

int main(int argc, char **argv) {
    /* A child never reaches the table: a wrong count of arguments here would spawn children without end. */
    if (argc >= 2 && strcmp(argv[1], "--one") == 0) {
        if (argc != 6) return 2;
        return one(atoi(argv[2]), argv[3], argv[4], argv[5]);
    }
    int small = argc == 4 && strcmp(argv[3], "--small") == 0;
    if (argc != 2 && argc != 3 && !small) {
        fprintf(stderr, "usage: stack <cases directory> [required KiB] [--small]\n");
        return 2;
    }
    int required = argc >= 3 ? atoi(argv[2]) : 256;
    static const int sizes[] = {8192, 1024, 512, 256, 128, 64, 32, 16};
    size_t size_count = sizeof sizes / sizeof sizes[0] - (small ? 0 : 2);
    static const char *inputs[] = {"begin", "jcal", "json", "feed", "cases"};
    static const char *whens[] = {"first", "later"};
    int smallest_all = 0;
    for (size_t s = 0; s < size_count; s++) {
        int all_ok = 1;
        for (size_t w = 0; w < 2; w++) {
            for (size_t i = 0; i < sizeof inputs / sizeof inputs[0]; i++) {
                const char *outcome = spawn_one(argv[0], sizes[s], whens[w], inputs[i], argv[1]);
                printf("%5d KiB  %-5s call  %-5s  %s\n", sizes[s], whens[w], inputs[i], outcome);
                fflush(stdout);
                if (strcmp(outcome, "ok") != 0) all_ok = 0;
                if (sizes[s] >= required) CHECK(strcmp(outcome, "ok") == 0, "%d KiB %s %s: %s", sizes[s], whens[w], inputs[i], outcome);
            }
        }
        if (all_ok && (smallest_all == 0 || sizes[s] < smallest_all)) smallest_all = sizes[s];
    }
    printf("the smallest stack with everything working: %d KiB\n", smallest_all);
    printf("stack: %d failed\n", failures);
    return failures ? 1 : 0;
}
