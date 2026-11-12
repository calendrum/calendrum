/*
 * Memory that really runs out, inside the runtime: anonymous mmap starts to fail, as a limit on the address space makes it. A
 * Kotlin exception could be caught; the runtime does not throw one for this, it ends the process. That is the expected outcome here,
 * because the guide says so: a change of it has to reach the guide.
 *
 *   out_of_memory          the outcome
 *   out_of_memory --child  the call under the failing mmap
 */
#include <signal.h>
#include <spawn.h>
#include <sys/wait.h>
#include <unistd.h>

#include "calendrum.h"
#include "interpose.h"
#include "support.h"

extern char **environ;

static int child(void) {
    buffer request = expand_request(2000);
    uint8_t *out = NULL;
    size_t len = 0;
    CHECK(calendrum_call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &out, &len) == 0, "before");
    calendrum_free(out);
    interpose_refuse_mmap(1);
    for (int i = 0; i < 2000; i++) {
        int32_t code = calendrum_call("expand", (uint8_t *) request.data, request.len, &out, &len);
        if (code != 0) {
            interpose_refuse_mmap(0);
            printf("code %d after %d calls\n", code, i);
            return 10;
        }
        calendrum_free(out);
    }
    interpose_refuse_mmap(0);
    printf("2000 calls without a failure\n");
    return 11;
}

int main(int argc, char **argv) {
    if (argc == 2 && strcmp(argv[1], "--child") == 0) return child();
    if (argc != 1) return 2;
    char *const arguments[] = {argv[0], "--child", NULL};
    pid_t pid;
    if (posix_spawn(&pid, argv[0], NULL, NULL, arguments, environ) != 0) return 2;
    int status = 0;
    waitpid(pid, &status, 0);
    if (WIFSIGNALED(status)) {
        printf("no memory inside the runtime: the process ends with %s\n", strsignal(WTERMSIG(status)));
        CHECK(WTERMSIG(status) == SIGABRT, "expected SIGABRT");
    } else {
        printf("no memory inside the runtime: the process lives, exit %d\n", WEXITSTATUS(status));
        CHECK(0, "the guide says the process ends: update it");
    }
    printf("out_of_memory: %d failed\n", failures);
    return failures ? 1 : 0;
}
