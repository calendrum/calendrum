/*
 * What happens in a child of fork(). A process for each scenario and a limit on each child; a child that does not finish is
 * killed and counted as a hang. The library is opened with dlopen so that a scenario decides whether the parent has loaded it.
 *
 *   fork <library>                        the table
 *   fork --scenario <library> <name>      one scenario
 */
#include <dlfcn.h>
#include <pthread.h>
#include <signal.h>
#include <spawn.h>
#include <stdatomic.h>
#include <sys/wait.h>
#include <unistd.h>

#include "support.h"

extern char **environ;

#define CHILDREN 3
#define CHILD_SECONDS 10.0
#define CHILD_CALLS 300

typedef int32_t (*call_function)(const char *, const uint8_t *, size_t, uint8_t **, size_t *);
typedef void (*free_function)(uint8_t *);
typedef int32_t (*abi_function)(void);

static call_function call_fn;
static free_function free_fn;
static abi_function abi_fn;
static buffer request;

static int load(const char *path) {
    void *library = dlopen(path, RTLD_NOW | RTLD_LOCAL);
    if (!library) return 0;
    call_fn = (call_function) dlsym(library, "calendrum_call");
    free_fn = (free_function) dlsym(library, "calendrum_free");
    abi_fn = (abi_function) dlsym(library, "calendrum_abi_version");
    return call_fn && free_fn && abi_fn;
}

static int work(int calls) {
    for (int i = 0; i < calls; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        if (call_fn("inspect", (uint8_t *) request.data, request.len, &out, &len) != 0) return 0;
        int good = out && strstr((char *) out, "\"ok\":true") != NULL;
        free_fn(out);
        if (!good) return 0;
    }
    return 1;
}

static atomic_int stop_workers = 0;

static void *busy(void *unused) {
    (void) unused;
    while (!stop_workers) work(1);
    return NULL;
}

/* Counts of what the children did. */
static int done_ok, done_wrong, done_hang, done_signal, done_other, last_signal;

static void wait_for(pid_t pid) {
    double deadline = now_seconds() + CHILD_SECONDS;
    int status = 0;
    for (;;) {
        pid_t done = waitpid(pid, &status, WNOHANG);
        if (done == pid) break;
        if (now_seconds() > deadline) {
            kill(pid, SIGKILL);
            waitpid(pid, &status, 0);
            done_hang++;
            return;
        }
        usleep(2000);
    }
    if (WIFSIGNALED(status)) {
        done_signal++;
        last_signal = WTERMSIG(status);
    } else if (WEXITSTATUS(status) == 0) {
        done_ok++;
    } else if (WEXITSTATUS(status) == 1) {
        done_wrong++;
    } else {
        done_other++;
    }
}

typedef enum { CHILD_CALLS_MANY, CHILD_CALLS_ONE, CHILD_LOADS_AND_CALLS, CHILD_ONLY_EXITS, CHILD_ONLY_QUICK_EXITS, CHILD_EXECS } child_kind;

static void child(child_kind kind, const char *path) {
    switch (kind) {
        case CHILD_CALLS_MANY:
            _exit(work(CHILD_CALLS) ? 0 : 1);
        case CHILD_CALLS_ONE:
            _exit(work(1) ? 0 : 1);
        case CHILD_LOADS_AND_CALLS:
            if (!load(path)) _exit(2);
            _exit(work(CHILD_CALLS) ? 0 : 1);
        case CHILD_ONLY_EXITS:
            /* exit() runs what the library registered, as sys.exit in a worker does. */
            exit(0);
        case CHILD_ONLY_QUICK_EXITS:
            _exit(0);
        case CHILD_EXECS: {
            char *const arguments[] = {"/usr/bin/true", NULL};
            execve("/usr/bin/true", arguments, environ);
            _exit(2);
        }
    }
    _exit(2);
}

static int scenario(const char *path, const char *name) {
    request = inspect_request(200);
    child_kind kind = CHILD_CALLS_MANY;
    int parent_calls_after = 1;
    if (strcmp(name, "not-loaded") == 0) {
        kind = CHILD_LOADS_AND_CALLS;
        parent_calls_after = 0;
    } else {
        if (!load(path)) return 3;
        if (strcmp(name, "loaded-not-called") == 0) {
        } else if (strcmp(name, "abi-only") == 0) {
            abi_fn();
        } else {
            if (!work(1)) return 3;
            if (strcmp(name, "called-many") == 0) kind = CHILD_CALLS_MANY;
            else if (strcmp(name, "called-one") == 0) kind = CHILD_CALLS_ONE;
            else if (strcmp(name, "called-exit") == 0) kind = CHILD_ONLY_EXITS;
            else if (strcmp(name, "called-quick-exit") == 0) kind = CHILD_ONLY_QUICK_EXITS;
            else if (strcmp(name, "called-exec") == 0) kind = CHILD_EXECS;
            else if (strcmp(name, "busy-many") != 0 && strcmp(name, "busy-exit") != 0) return 3;
            if (strcmp(name, "busy-exit") == 0) kind = CHILD_ONLY_EXITS;
        }
    }

    pthread_t workers[4];
    int busy_parent = strncmp(name, "busy-", 5) == 0;
    if (busy_parent) {
        for (int i = 0; i < 4; i++) pthread_create(&workers[i], NULL, busy, NULL);
        usleep(100000);
    }
    for (int i = 0; i < CHILDREN; i++) {
        pid_t pid = fork();
        if (pid == 0) child(kind, path);
        if (pid < 0) return 3;
        wait_for(pid);
    }
    if (busy_parent) {
        stop_workers = 1;
        for (int i = 0; i < 4; i++) pthread_join(workers[i], NULL);
    }
    int parent_good = parent_calls_after ? work(50) : 1;
    printf("%-18s ok %d, hang %d, signal %d%s%s, wrong %d, other %d; the parent afterwards: %s\n", name, done_ok, done_hang,
           done_signal, done_signal ? " " : "", done_signal ? strsignal(last_signal) : "", done_wrong, done_other,
           parent_good ? "works" : "BROKEN");
    fflush(stdout);
    /*
     * What the guide says. A collection needs a thread that the child does not have, so work hangs once a collection is due;
     * "called-one" and "busy-exit" are left out, as their outcome is a matter of timing.
     */
    static const char *const works[] = {"not-loaded", "loaded-not-called", "called-exit", "called-quick-exit", "called-exec"};
    static const char *const hangs[] = {"abi-only", "called-many", "busy-many"};
    int as_documented = 1;
    for (size_t i = 0; i < sizeof works / sizeof works[0]; i++) {
        if (strcmp(name, works[i]) == 0 && done_ok != CHILDREN) as_documented = 0;
    }
    for (size_t i = 0; i < sizeof hangs / sizeof hangs[0]; i++) {
        if (strcmp(name, hangs[i]) == 0 && done_hang == 0) as_documented = 0;
    }
    if (!as_documented) printf("%s: not what the guide says, update it\n", name);
    return parent_good && as_documented ? 0 : 1;
}

int main(int argc, char **argv) {
    /* A scenario never reaches the table, so that it cannot start scenarios of its own. */
    if (argc >= 2 && strcmp(argv[1], "--scenario") == 0) {
        if (argc != 4) return 2;
        return scenario(argv[2], argv[3]);
    }
    if (argc != 2) {
        fprintf(stderr, "usage: fork <library>\n");
        return 2;
    }
    static char *const names[] = {"not-loaded", "loaded-not-called", "abi-only", "called-one", "called-many", "called-exit",
                                  "called-quick-exit", "called-exec", "busy-many", "busy-exit"};
    for (size_t i = 0; i < sizeof names / sizeof names[0]; i++) {
        char *const arguments[] = {argv[0], "--scenario", argv[1], names[i], NULL};
        pid_t pid;
        if (posix_spawn(&pid, argv[0], NULL, NULL, arguments, environ) != 0) return 2;
        double deadline = now_seconds() + (CHILD_SECONDS + 2) * CHILDREN + 60;
        int status = 0;
        for (;;) {
            if (waitpid(pid, &status, WNOHANG) == pid) break;
            if (now_seconds() > deadline) {
                kill(pid, SIGKILL);
                waitpid(pid, &status, 0);
                printf("%-18s the parent hangs\n", names[i]);
                failures++;
                status = 0;
                break;
            }
            usleep(5000);
        }
        if (WIFSIGNALED(status) || WEXITSTATUS(status) != 0) {
            printf("%-18s the parent failed, status %d\n", names[i], status);
            failures++;
        }
    }
    printf("fork: %d failed\n", failures);
    return failures ? 1 : 0;
}
