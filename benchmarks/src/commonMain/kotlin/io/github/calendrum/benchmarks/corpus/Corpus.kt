package io.github.calendrum.benchmarks.corpus

internal object Corpus {
    const val INVITE: String = "invite"
    const val INVITE_BATCH: String = "invite-batch"
    const val PERSONAL: String = "personal"
    const val UNIVERSITY: String = "university"
    const val HUGE: String = "huge"

    val names: List<String> = listOf(INVITE, INVITE_BATCH, PERSONAL, UNIVERSITY, HUGE)

    fun write(name: String, sink: ByteSink) {
        when (name) {
            INVITE -> writeInvite(sink)
            INVITE_BATCH -> writeInviteBatch(sink)
            PERSONAL -> writePersonal(sink)
            UNIVERSITY -> writeUniversity(sink)
            HUGE -> writeHuge(sink)
            else -> error("Unknown corpus: $name")
        }
    }
}

internal fun corpusBytes(name: String): ByteArray {
    val sink = ByteArraySink()
    Corpus.write(name, sink)
    return sink.toByteArray()
}
