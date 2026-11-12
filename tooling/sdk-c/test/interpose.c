/*
 * A library for the tests that makes memory run out on request. dyld applies an interposer only from an image loaded when the
 * process starts, and to every other image, so this has to be a dynamic library of its own.
 */
#include <stdatomic.h>
#include <stdlib.h>
#include <sys/mman.h>

#include "interpose.h"

static _Atomic size_t refused_malloc = 0;
static _Atomic int mmap_refused = 0;

void interpose_refuse_malloc_of(size_t size) { refused_malloc = size; }

size_t interpose_refused_malloc(void) { return refused_malloc; }

void interpose_refuse_mmap(int refused) { mmap_refused = refused; }

static void *failing_malloc(size_t size) {
    size_t refused = refused_malloc;
    if (size != 0 && size == refused) {
        refused_malloc = 0;
        return NULL;
    }
    return malloc(size);
}

static void *failing_mmap(void *address, size_t length, int protection, int flags, int fd, off_t offset) {
    if (mmap_refused && (flags & MAP_ANON)) return MAP_FAILED;
    return mmap(address, length, protection, flags, fd, offset);
}

__attribute__((used, section("__DATA,__interpose"))) static struct {
    const void *replacement;
    const void *replaced;
} interposers[] = {
    {(const void *) failing_malloc, (const void *) malloc},
    {(const void *) failing_mmap, (const void *) mmap},
};
