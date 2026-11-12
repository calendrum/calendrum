#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "calendrum.h"

// The same operations as the Kotlin baseline: the empty call, parse to jCal and occurrences of a year as JSON, then
// parsing in 1, 2, 4 and 8 threads; every thread uses its own output buffers.
static uint8_t *sample;
static size_t sample_len;
static long rounds;

static double now(void) {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return ts.tv_sec + ts.tv_nsec / 1e9;
}

static uint8_t *read_file(const char *path, size_t *len) {
    FILE *file = fopen(path, "rb");
    if (!file) {
        perror(path);
        exit(1);
    }
    fseek(file, 0, SEEK_END);
    *len = (size_t) ftell(file);
    fseek(file, 0, SEEK_SET);
    uint8_t *data = malloc(*len);
    if (fread(data, 1, *len, file) != *len) {
        perror(path);
        exit(1);
    }
    fclose(file);
    return data;
}

static void parse_once(void) {
    uint8_t *jcal, *problems;
    size_t jcal_len, problems_len;
    if (ical_parse(sample, sample_len, &jcal, &jcal_len, &problems, &problems_len) != ICAL_OK) exit(2);
    ical_free(jcal);
    ical_free(problems);
}

static void occurrences_once(void) {
    uint8_t *json;
    size_t len;
    if (ical_occurrences(sample, sample_len, "2026-01-01T00:00:00Z", "2027-01-01T00:00:00Z", "Europe/Berlin",
                         &json, &len) != ICAL_OK) exit(3);
    ical_free(json);
}

static void *parse_loop(void *unused) {
    (void) unused;
    for (long i = 0; i < rounds; i++) parse_once();
    return NULL;
}

int main(int argc, char **argv) {
    if (argc < 3) {
        fprintf(stderr, "usage: bench <file.ics> <rounds>\n");
        return 2;
    }
    sample = read_file(argv[1], &sample_len);
    rounds = atol(argv[2]);

    for (long i = 0; i < rounds / 10 + 1; i++) parse_once();

    long calls = 10000000;
    double start = now();
    int32_t sum = 0;
    for (long i = 0; i < calls; i++) sum += ical_abi_version();
    double empty = (now() - start) / calls;

    start = now();
    for (long i = 0; i < rounds; i++) parse_once();
    double parse = (now() - start) / rounds;

    start = now();
    for (long i = 0; i < rounds; i++) occurrences_once();
    double occurrences = (now() - start) / rounds;

    printf("file %zu bytes, %ld rounds\n", sample_len, rounds);
    printf("c ical_abi_version %.1f ns (%d)\n", empty * 1e9, sum == calls);
    printf("c ical_parse %.1f us\n", parse * 1e6);
    printf("c ical_occurrences %.1f us\n", occurrences * 1e6);

    int counts[] = {1, 2, 4, 8};
    for (int c = 0; c < 4; c++) {
        pthread_t threads[8];
        start = now();
        for (int t = 0; t < counts[c]; t++) pthread_create(&threads[t], NULL, parse_loop, NULL);
        for (int t = 0; t < counts[c]; t++) pthread_join(threads[t], NULL);
        double elapsed = now() - start;
        printf("c threads %d: %.0f parses/s\n", counts[c], counts[c] * rounds / elapsed);
    }
    return 0;
}
