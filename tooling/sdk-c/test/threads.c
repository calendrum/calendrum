/*
 * Foreign threads: 8 threads with 1000 calls each give what one thread gives, a buffer can be released on another thread, threads
 * that come and go leave nothing behind. Prints the scaling for the table of the docs.
 */
#include <pthread.h>
#include <stdatomic.h>

#include "calendrum.h"
#include "support.h"

typedef struct {
    const char *operation;
    buffer request;
    uint8_t *response;
    size_t response_len;
} sample_call;

static sample_call samples[4];
#define SAMPLES (sizeof samples / sizeof samples[0])

static atomic_int mismatches = 0;

static void *same_answers(void *argument) {
    long calls = (long) argument;
    for (long i = 0; i < calls; i++) {
        sample_call *s = &samples[(size_t) i % SAMPLES];
        uint8_t *out = NULL;
        size_t len = 0;
        int32_t code = calendrum_call(s->operation, (uint8_t *) s->request.data, s->request.len, &out, &len);
        if (code != CALENDRUM_OK || len != s->response_len || memcmp(out, s->response, len) != 0) mismatches++;
        calendrum_free(out);
    }
    return NULL;
}

/* One thread allocates, another releases: a ring of finished responses between them. */
#define RING 64
static uint8_t *_Atomic ring[RING];
static atomic_int produced = 0;

static void *producer(void *unused) {
    (void) unused;
    for (int i = 0; i < 4000; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        if (calendrum_call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &out, &len) != CALENDRUM_OK) mismatches++;
        uint8_t *expected = NULL;
        while (!atomic_compare_exchange_weak(&ring[i % RING], &expected, out)) expected = NULL;
        produced++;
    }
    return NULL;
}

static void *releaser(void *unused) {
    (void) unused;
    for (int i = 0; i < 4000; i++) {
        uint8_t *out;
        while (!(out = atomic_exchange(&ring[i % RING], NULL))) {
        }
        if (strncmp((char *) out, "{\"ops_version\":1,", 17) != 0) mismatches++;
        calendrum_free(out);
    }
    return NULL;
}

static void *one_call(void *unused) {
    (void) unused;
    uint8_t *out = NULL;
    size_t len = 0;
    if (calendrum_call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &out, &len) != CALENDRUM_OK) mismatches++;
    calendrum_free(out);
    return NULL;
}

static buffer scaling_request;

static void *scaling_loop(void *argument) {
    long calls = (long) argument;
    for (long i = 0; i < calls; i++) {
        uint8_t *out = NULL;
        size_t len = 0;
        calendrum_call("inspect", (uint8_t *) scaling_request.data, scaling_request.len, &out, &len);
        calendrum_free(out);
    }
    return NULL;
}

static double throughput(int threads, long calls_per_thread) {
    pthread_t ids[8];
    double start = now_seconds();
    for (int i = 0; i < threads; i++) pthread_create(&ids[i], NULL, scaling_loop, (void *) calls_per_thread);
    for (int i = 0; i < threads; i++) pthread_join(ids[i], NULL);
    return (double) threads * (double) calls_per_thread / (now_seconds() - start);
}

int main(void) {
    samples[0] = (sample_call){"inspect", inspect_request(3), NULL, 0};
    samples[1] = (sample_call){"expand", expand_request(3), NULL, 0};
    samples[2] = (sample_call){"validate", inspect_request(2), NULL, 0};
    buffer explain = {0};
    buffer_puts(&explain, "{\"code\":\"ICAL-S003\"}");
    samples[3] = (sample_call){"explain", explain, NULL, 0};

    /* On the main thread of a process that has not called yet. */
    for (size_t i = 0; i < SAMPLES; i++) {
        sample_call *s = &samples[i];
        CHECK(calendrum_call(s->operation, (uint8_t *) s->request.data, s->request.len, &s->response, &s->response_len) ==
                  CALENDRUM_OK, "%s", s->operation);
        CHECK(strstr((char *) s->response, "\"ok\":true") != NULL, "%s: %s", s->operation, s->response);
    }

    pthread_t ids[8];
    for (int i = 0; i < 8; i++) pthread_create(&ids[i], NULL, same_answers, (void *) 1000L);
    for (int i = 0; i < 8; i++) pthread_join(ids[i], NULL);
    CHECK(mismatches == 0, "8 threads x 1000 calls: %d differ", (int) mismatches);
    printf("8 threads x 1000 calls: %d differ\n", (int) mismatches);

    pthread_t a, b;
    pthread_create(&b, NULL, releaser, NULL);
    pthread_create(&a, NULL, producer, NULL);
    pthread_join(a, NULL);
    pthread_join(b, NULL);
    CHECK(mismatches == 0 && produced == 4000, "allocated on one thread and released on another");
    printf("allocated on one thread, released on another: %d buffers\n", (int) produced);

    /*
     * A thread per call, as a pool that grows and shrinks does it. A thread that ends leaves its memory to the next collection,
     * and these calls allocate too little to ask for one: RSS rises to a plateau and comes back with the first real work.
     */
    double before = rss_mib();
    double peak = 0;
    for (int round = 0; round < 6000; round++) {
        pthread_t id;
        pthread_create(&id, NULL, one_call, NULL);
        pthread_join(id, NULL);
        if (round % 500 == 0 && rss_mib() > peak) peak = rss_mib();
    }
    CHECK(mismatches == 0, "a thread for each call");
    scaling_request = inspect_request(200);
    scaling_loop((void *) 200L);
    double after = rss_mib();
    int threads = thread_count();
    CHECK(after - before < 64, "6000 short threads: RSS went from %.1f to %.1f MiB and stayed", before, after);
    CHECK(peak < 4096, "6000 short threads: the peak of RSS is %.0f MiB", peak);
    printf("6000 short threads: RSS %.1f MiB, peak %.1f, after some work %.1f, threads now %d\n", before, peak, after, threads);

    printf("inspect of 200 events, calls per second:\n");
    scaling_loop((void *) 200L);
    double one = 0;
    int counts[] = {1, 2, 4, 8};
    for (int i = 0; i < 4; i++) {
        double rate = throughput(counts[i], 400);
        if (i == 0) one = rate;
        printf("  %d threads: %.0f (%.2fx)\n", counts[i], rate, rate / one);
    }
    printf("threads: %d failed\n", failures);
    return failures ? 1 : 0;
}
