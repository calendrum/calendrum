package io.github.calendrum.benchmarks.fuzz

import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.corpusBytes
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// Every `seed-invite.ics` under the fuzz inputs is a copy of the `invite` corpus. The test fails when one differs from
// the current generator; `main` (`:benchmarks:updateFuzzSeeds`) rewrites the stale copies.
internal const val INVITE_SEED: String = "seed-invite.ics"

internal fun inviteSeeds(inputs: Path): List<Path> = Files.walk(inputs).use { stream ->
    stream.filter { Files.isRegularFile(it) && it.fileName.toString() == INVITE_SEED }.sorted().toList()
}

fun main(args: Array<String>) {
    val inputs = Paths.get(args.single())
    val seeds = inviteSeeds(inputs)
    check(seeds.isNotEmpty()) { "No $INVITE_SEED under $inputs" }
    val invite = corpusBytes(Corpus.INVITE)
    for (seed in seeds) {
        val stale = !Files.readAllBytes(seed).contentEquals(invite)
        if (stale) Files.write(seed, invite)
        println("${if (stale) "updated" else "unchanged"}: ${inputs.relativize(seed)}")
    }
}
