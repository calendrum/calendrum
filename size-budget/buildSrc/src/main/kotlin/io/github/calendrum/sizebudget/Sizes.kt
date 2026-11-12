package io.github.calendrum.sizebudget

import org.gradle.api.Project
import java.io.File
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.Deflater
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipFile

const val SIZE_USAGE: String = "calendrum-size-measurements"

const val UNIT_BYTES: String = "bytes"

const val UNIT_CLASSES: String = "classes"

const val SELECTED_PLATFORMS: String = "sizeBudget.platforms"

const val UNAVAILABLE_PLATFORMS: String = "sizeBudget.unavailablePlatforms"

enum class SizePlatform(val id: String, val title: String) {
    JVM("jvm", "JVM (+R8)"),
    JS("js", "JS"),
    WASM_JS("wasmJs", "wasmJs"),
    ANDROID("android", "Android"),
    IOS("ios", "iOS"),
    ;

    companion object {
        fun of(metricId: String): SizePlatform? = entries.firstOrNull { it.id == metricId.substringBefore('.') }
    }
}

val Project.selectedSizePlatforms: Set<SizePlatform>
    get() {
        val ids = gradle.extensions.extraProperties.get(SELECTED_PLATFORMS) as List<*>
        return SizePlatform.entries.filterTo(linkedSetOf()) { it.id in ids }
    }

val Project.unavailableSizePlatforms: Map<String, String>
    get() = (gradle.extensions.extraProperties.get(UNAVAILABLE_PLATFORMS) as Map<*, *>)
        .entries.associate { it.key.toString() to it.value.toString() }

internal fun classCount(jar: File): Long = ZipFile(jar).use { zip ->
    zip.entries().asSequence().count { entry ->
        !entry.isDirectory &&
            entry.name.endsWith(".class") &&
            !entry.name.startsWith("META-INF/") &&
            !entry.name.endsWith("module-info.class")
    }.toLong()
}

internal class DexFile(val name: String, val bytes: Long, val classes: Long)

internal fun dexFiles(apk: File): List<DexFile> = ZipFile(apk).use { zip ->
    zip.entries().asSequence()
        .filter { !it.isDirectory && '/' !in it.name && it.name.startsWith("classes") && it.name.endsWith(".dex") }
        .sortedBy { it.name }
        .map { entry ->
            val header = zip.getInputStream(entry).use { it.readNBytes(DEX_HEADER_SIZE) }
            require(header.size == DEX_HEADER_SIZE && header.copyOfRange(0, 4).contentEquals(DEX_MAGIC)) {
                "${apk.name}!${entry.name} is not a dex file"
            }
            val classes = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt(DEX_CLASS_DEFS_SIZE_OFFSET)
            DexFile(entry.name, entry.size, classes.toLong())
        }
        .toList()
}

private const val DEX_HEADER_SIZE = 0x70

private const val DEX_CLASS_DEFS_SIZE_OFFSET = 0x60

private val DEX_MAGIC = byteArrayOf(0x64, 0x65, 0x78, 0x0a)

internal fun machOSectionBytes(binary: File): Long {
    val buffer = ByteBuffer.wrap(binary.readBytes()).order(ByteOrder.LITTLE_ENDIAN)
    require(buffer.getInt(0) == MH_MAGIC_64) { "$binary is not a little-endian 64-bit Mach-O file" }
    val commands = buffer.getInt(16)
    var command = MACH_HEADER_64_SIZE
    var bytes = 0L
    repeat(commands) {
        if (buffer.getInt(command) == LC_SEGMENT_64) {
            val sections = buffer.getInt(command + 64)
            repeat(sections) { index ->
                val section = command + SEGMENT_COMMAND_64_SIZE + index * SECTION_64_SIZE
                val type = buffer.getInt(section + 64) and SECTION_TYPE
                if (type !in ZEROFILL_TYPES) bytes += buffer.getLong(section + 40)
            }
        }
        command += buffer.getInt(command + 4)
    }
    return bytes
}

private const val MH_MAGIC_64 = 0xfeedfacf.toInt()

private const val MACH_HEADER_64_SIZE = 32

private const val LC_SEGMENT_64 = 0x19

private const val SEGMENT_COMMAND_64_SIZE = 72

private const val SECTION_64_SIZE = 80

private const val SECTION_TYPE = 0xff

private val ZEROFILL_TYPES = setOf(0x1, 0xc, 0x12)

internal fun gzipSize(file: File): Long {
    val counter = CountingOutputStream()
    BestGzipOutputStream(counter).use { gzip -> file.inputStream().use { it.copyTo(gzip) } }
    return counter.count
}

private class BestGzipOutputStream(out: OutputStream) : GZIPOutputStream(out) {
    init {
        def.setLevel(Deflater.BEST_COMPRESSION)
    }
}

private class CountingOutputStream : OutputStream() {
    var count: Long = 0
        private set

    override fun write(b: Int) {
        count++
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        count += len
    }
}
