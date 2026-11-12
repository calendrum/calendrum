package io.github.calendrum.benchmarks.stream

import io.github.calendrum.ICal
import io.github.calendrum.benchmarks.corpus.Corpus
import io.github.calendrum.benchmarks.corpus.writeCorpusToFile
import io.github.calendrum.benchmarks.reader.expectedCounts
import io.github.calendrum.components
import io.github.calendrum.syntax.ICalComponent
import java.io.File
import java.io.FileInputStream
import java.lang.management.ManagementFactory
import java.lang.management.MemoryType
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HugeStreamMemoryTest {
    @Test
    fun hugeFeedStreamsInASmallHeap() {
        val file = File.createTempFile("calendrum-huge", ".ics")
        try {
            val (size, _) = writeCorpusToFile(Corpus.HUGE, file)
            val memory = ManagementFactory.getMemoryMXBean()
            val pools = ManagementFactory.getMemoryPoolMXBeans().filter { it.type == MemoryType.HEAP }
            System.gc()
            val baseline = memory.heapMemoryUsage.used
            pools.forEach { it.resetPeakUsage() }
            var live = 0L
            val counts = HashMap<String, Int>()
            var properties = 0
            var attendees = 0
            val started = System.nanoTime()
            FileInputStream(file).use { input ->
                val stream = ICal.parser().components(input)
                for (component in stream) {
                    counts.merge(component.name, 1, Int::plus)
                    val stack = ArrayList<ICalComponent>()
                    stack += component
                    while (stack.isNotEmpty()) {
                        val node = stack.removeAt(stack.lastIndex)
                        properties += node.properties.size
                        attendees += node.properties("ATTENDEE").size
                        stack += node.components
                    }
                    if (counts.values.sum() % SAMPLE_EVERY == 0) {
                        System.gc()
                        live = maxOf(live, memory.heapMemoryUsage.used - baseline)
                    }
                }
                properties += stream.calendarProperties.size
                assertEquals(emptyList(), stream.problems)
            }
            val seconds = (System.nanoTime() - started) / 1e9
            val peak = pools.sumOf { it.peakUsage.used }
            val expected = expectedCounts.getValue(Corpus.HUGE)
            assertEquals(expected.components.getValue("VEVENT"), counts["VEVENT"])
            assertEquals(expected.components.getValue("VTIMEZONE"), counts["VTIMEZONE"])
            assertEquals(expected.properties, properties)
            assertEquals(expected.selectedProperties.getValue("ATTENDEE"), attendees)
            val maxHeap = Runtime.getRuntime().maxMemory()
            println(
                (
                    "huge stream: %d bytes in %.2f s (%.1f MB/s), max heap %.1f MiB, peak heap %.1f MiB, " +
                        "live growth after GC %.2f MiB"
                    ).format(
                    Locale.ROOT,
                    size,
                    seconds,
                    size / 1e6 / seconds,
                    maxHeap / MIB,
                    peak / MIB,
                    live / MIB,
                ),
            )
            assertTrue(maxHeap <= HEAP_LIMIT, "the test must run in a small heap, it has ${maxHeap / MIB} MiB")
            assertTrue(live < LIVE_LIMIT, "live heap grew by ${live / MIB} MiB")
        } finally {
            file.delete()
        }
    }

    private companion object {
        private const val SAMPLE_EVERY = 20_000
        private const val MIB = 1024.0 * 1024.0
        private const val LIVE_LIMIT = 8L * 1024 * 1024
        private const val HEAP_LIMIT = 128L * 1024 * 1024
    }
}
