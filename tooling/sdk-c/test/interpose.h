#ifndef INTERPOSE_H
#define INTERPOSE_H

#include <stddef.h>

/* The next malloc of exactly this size returns null; 0 for none. */
void interpose_refuse_malloc_of(size_t size);
/* What is still waiting to be refused: 0 once it has happened. */
size_t interpose_refused_malloc(void);
/* While not 0, every anonymous mmap fails. */
void interpose_refuse_mmap(int refused);

#endif
