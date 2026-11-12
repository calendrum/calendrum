/*
 * What loading the library and calling it changes in the process: handlers of signals, the signal mask, the terminate handler of
 * C++, the locale. In C++ for std::get_terminate. A program in Python or on the JVM has its own handlers, so this list is
 * what an SDK has to know; the test fails when a handler changes, as that would need a new line in the guide.
 *
 *   process_state <library>
 */
#include <dlfcn.h>
#include <locale.h>
#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>

#include <exception>

typedef int32_t (*call_function)(const char *, const uint8_t *, size_t, uint8_t **, size_t *);
typedef void (*free_function)(uint8_t *);

struct state {
    struct sigaction actions[NSIG];
    sigset_t mask;
    std::terminate_handler terminate;
    char locale[128];
};

static void capture(state *s) {
    for (int i = 1; i < NSIG; i++) {
        memset(&s->actions[i], 0, sizeof s->actions[i]);
        sigaction(i, nullptr, &s->actions[i]);
    }
    pthread_sigmask(SIG_SETMASK, nullptr, &s->mask);
    s->terminate = std::get_terminate();
    snprintf(s->locale, sizeof s->locale, "%s", setlocale(LC_ALL, nullptr));
}

static const char *handler_name(const struct sigaction &action) {
    if (action.sa_handler == SIG_DFL) return "default";
    if (action.sa_handler == SIG_IGN) return "ignored";
    return "a handler";
}

static int compare(const char *what, const state &before, const state &after) {
    int changes = 0;
    for (int i = 1; i < NSIG; i++) {
        if (before.actions[i].sa_handler != after.actions[i].sa_handler || before.actions[i].sa_flags != after.actions[i].sa_flags) {
            printf("%s: signal %d (%s): %s -> %s\n", what, i, strsignal(i), handler_name(before.actions[i]), handler_name(after.actions[i]));
            changes++;
        }
    }
    if (before.mask != after.mask) {
        printf("%s: the signal mask of the thread changed\n", what);
        changes++;
    }
    if (before.terminate != after.terminate) {
        printf("%s: std::set_terminate was called\n", what);
    }
    if (strcmp(before.locale, after.locale) != 0) {
        printf("%s: the locale changed: %s -> %s\n", what, before.locale, after.locale);
        changes++;
    }
    return changes;
}

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "usage: process_state <library>\n");
        return 2;
    }
    static state before, loaded, called, worked;
    capture(&before);
    void *library = dlopen(argv[1], RTLD_NOW | RTLD_LOCAL);
    if (!library) {
        printf("dlopen: %s\n", dlerror());
        return 1;
    }
    capture(&loaded);
    int changes = compare("dlopen", before, loaded);

    call_function call = reinterpret_cast<call_function>(dlsym(library, "calendrum_call"));
    free_function release = reinterpret_cast<free_function>(dlsym(library, "calendrum_free"));
    uint8_t *out = nullptr;
    size_t len = 0;
    call("explain", reinterpret_cast<const uint8_t *>("{\"code\":\"ICAL-S003\"}"), 20, &out, &len);
    release(out);
    capture(&called);
    changes += compare("the first call", loaded, called);

    static const char request[] = "{\"rrule\":\"FREQ=DAILY;COUNT=20000\",\"start\":\"2026-01-01T00:00:00Z\",\"limit\":100000}";
    for (int i = 0; i < 100; i++) {
        out = nullptr;
        call("rrule_expand", reinterpret_cast<const uint8_t *>(request), sizeof request - 1, &out, &len);
        release(out);
    }
    capture(&worked);
    changes += compare("100 calls with collections", called, worked);

    int shown = 0;
    for (int i = 1; i < NSIG; i++) {
        if (worked.actions[i].sa_handler != SIG_DFL) {
            printf("after the calls: signal %d (%s): %s\n", i, strsignal(i), handler_name(worked.actions[i]));
            shown++;
        }
    }
    printf("signals that are not at their default: %d\n", shown);
    printf("SIGSEGV %s, SIGBUS %s, SIGPIPE %s\n", handler_name(worked.actions[SIGSEGV]), handler_name(worked.actions[SIGBUS]),
           handler_name(worked.actions[SIGPIPE]));
    printf("process_state: %d changes of signals, mask or locale\n", changes);
    return changes ? 1 : 0;
}
