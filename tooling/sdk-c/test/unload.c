/*
 * dlopen, a call, dlclose, dlopen again, a call. Also with a thread that has called the library and ends after dlclose, since the
 * end of a thread runs code of the runtime. Prints whether the image really leaves the process.
 *
 *   unload <library>
 */
#include <dlfcn.h>
#include <mach-o/dyld.h>
#include <pthread.h>
#include <unistd.h>

#include "support.h"

typedef int32_t (*call_function)(const char *, const uint8_t *, size_t, uint8_t **, size_t *);
typedef void (*free_function)(uint8_t *);

static int image_is_loaded(void) {
    for (uint32_t i = 0; i < _dyld_image_count(); i++) {
        if (strstr(_dyld_get_image_name(i), "libcalendrum")) return 1;
    }
    return 0;
}

static int call_once(void *library) {
    call_function call = (call_function) dlsym(library, "calendrum_call");
    free_function release = (free_function) dlsym(library, "calendrum_free");
    if (!call || !release) return 0;
    uint8_t *out = NULL;
    size_t len = 0;
    int good = call("explain", (const uint8_t *) "{\"code\":\"ICAL-S003\"}", 20, &out, &len) == 0 && out &&
               strstr((char *) out, "\"ok\":true");
    release(out);
    return good;
}

static void *library_for_thread;
static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t closed = PTHREAD_COND_INITIALIZER;
static int is_closed = 0;
static int thread_called = 0;

static void *call_then_wait(void *unused) {
    (void) unused;
    thread_called = call_once(library_for_thread);
    pthread_mutex_lock(&lock);
    while (!is_closed) pthread_cond_wait(&closed, &lock);
    pthread_mutex_unlock(&lock);
    return NULL;
}

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "usage: unload <library>\n");
        return 2;
    }
    void *first_address = NULL;
    int still_loaded = 0;
    for (int round = 0; round < 5; round++) {
        void *library = dlopen(argv[1], RTLD_NOW | RTLD_LOCAL);
        CHECK(library != NULL, "dlopen round %d: %s", round, dlerror());
        if (!library) return 1;
        void *address = dlsym(library, "calendrum_call");
        if (round == 0) first_address = address;
        CHECK(call_once(library), "a call in round %d", round);
        int code = dlclose(library);
        CHECK(code == 0, "dlclose round %d: %d", round, code);
        still_loaded = image_is_loaded();
        printf("round %d: calendrum_call at %p (%s), after dlclose the image is %s\n", round, address,
               address == first_address ? "same address" : "other address", still_loaded ? "still loaded" : "gone");
        fflush(stdout);
    }

    /* A thread of the runtime's kind outlives the handle. */
    library_for_thread = dlopen(argv[1], RTLD_NOW | RTLD_LOCAL);
    CHECK(library_for_thread != NULL, "dlopen for the thread");
    pthread_t id;
    pthread_create(&id, NULL, call_then_wait, NULL);
    while (!thread_called) usleep(1000);
    CHECK(dlclose(library_for_thread) == 0, "dlclose with a thread alive");
    pthread_mutex_lock(&lock);
    is_closed = 1;
    pthread_cond_broadcast(&closed);
    pthread_mutex_unlock(&lock);
    pthread_join(id, NULL);
    printf("a thread that called and ended after dlclose: no crash\n");

    void *library = dlopen(argv[1], RTLD_NOW | RTLD_LOCAL);
    CHECK(library != NULL && call_once(library), "a call after all that");
    printf("dlclose is %s\n", still_loaded ? "accepted, and the image stays in the process" : "a real unload");
    printf("unload: %d failed\n", failures);
    return failures ? 1 : 0;
}
