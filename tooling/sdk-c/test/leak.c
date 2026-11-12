/*
 * 100 000 calls of inspect and of expand: RSS every 10 000. It has to reach a plateau.
 *
 *   leak [calls] [events]
 */
#include "calendrum.h"
#include "support.h"

static void run(const char *operation, buffer *request, long calls) {
    double samples[64];
    int count = 0;
    double start = now_seconds();
    for (long i = 1; i <= calls; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        CHECK(calendrum_call(operation, (uint8_t *) request->data, request->len, &out, &len) == CALENDRUM_OK, "%s", operation);
        calendrum_free(out);
        if (i % 10000 == 0 && count < 64) samples[count++] = rss_mib();
    }
    printf("%s, %ld calls, %.0f s, RSS in MiB every 10 000:", operation, calls, now_seconds() - start);
    double highest_early = 0, highest_late = 0;
    for (int i = 0; i < count; i++) {
        printf(" %.1f", samples[i]);
        if (i < count / 2) {
            if (samples[i] > highest_early) highest_early = samples[i];
        } else if (samples[i] > highest_late) {
            highest_late = samples[i];
        }
    }
    printf("\n");
    /* A leak grows with the calls: the second half would stand above the first half by about as much as that one grew. */
    CHECK(highest_late <= highest_early * 1.25 + 8, "%s has no plateau: %.1f in the first half, %.1f in the second", operation,
          highest_early, highest_late);
    fflush(stdout);
}

int main(int argc, char **argv) {
    long calls = argc > 1 ? atol(argv[1]) : 100000;
    int events = argc > 2 ? atoi(argv[2]) : 5;
    buffer inspect = inspect_request(events);
    buffer expand = expand_request(events);
    run("inspect", &inspect, calls);
    run("expand", &expand, calls);
    printf("leak: %d failed\n", failures);
    return failures ? 1 : 0;
}
