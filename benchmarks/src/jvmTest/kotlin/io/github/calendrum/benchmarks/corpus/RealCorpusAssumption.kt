package io.github.calendrum.benchmarks.corpus

import org.junit.Assume.assumeTrue

// corpus/real/ stays with the maintainer (.gitignore): a checkout without it skips the tests that read it.
internal fun assumeRealCorpus() {
    assumeTrue("corpus/real/ is not in this checkout: it stays with the maintainer", RealCorpus.isPresent())
}
