// Packs a native binary of this build for every target: the binary, LICENSE, NOTICE, the licenses of the libraries inside
// and a README. The one place for it, so that the archives of the server, the command line and the C library cannot drift
// apart.
//
// The applying project sets these extra properties first. The Kotlin plugin is not on the classpath of a script plugin,
// so the project, which has it, hands over what it knows about its binaries.
//   distArchiveBase  a provider of the archive name without target and suffix, such as calendrum-mcp-0.1.0
//   distDescription  what the archive holds, for the task descriptions, such as "the server"
//   distReadme       the README.txt of the archive
//   distLinkTasks    the name of each native target and the task that links its release executable
//   distBinaries     the name of each native target and a provider of the file that task writes
//   distExtraFiles   optional: the name of a native target and more files for its archive, such as a header
import org.gradle.api.tasks.bundling.Compression

@Suppress("UNCHECKED_CAST")
val archiveBase = extra["distArchiveBase"] as Provider<String>
val whatIsPacked = extra["distDescription"] as String
val readme = extra["distReadme"] as RegularFile

@Suppress("UNCHECKED_CAST")
val linkTasks = extra["distLinkTasks"] as Map<String, TaskProvider<*>>

@Suppress("UNCHECKED_CAST")
val binaries = extra["distBinaries"] as Map<String, Provider<File>>

@Suppress("UNCHECKED_CAST")
val extraFiles = if (extra.has("distExtraFiles")) extra["distExtraFiles"] as Map<String, List<Any>> else emptyMap()

apply(from = layout.settingsDirectory.file("gradle/third-party-licenses.gradle.kts"))

@Suppress("UNCHECKED_CAST")
val licensesOf = extra["thirdPartyLicensesOf"] as (String) -> Provider<String>

val distDirectory = layout.buildDirectory.dir("dist")
val noticeFile = layout.settingsDirectory.file("../NOTICE")
val licenseFile = layout.settingsDirectory.file("../LICENSE")

// The linker of the Windows target keeps the COFF symbol table whatever -s says, and the compiler names its static symbols by source
// paths, which hold the home folder of whoever built this. An image needs no such table, and it is the last thing in the file.
linkTasks["mingwX64"]?.configure {
    val image = binaries.getValue("mingwX64")
    doLast {
        val file = image.get()
        val data = file.readBytes()
        val bytes = java.nio.ByteBuffer.wrap(data).order(java.nio.ByteOrder.LITTLE_ENDIAN)
        val coff = bytes.getInt(0x3C) + 4
        val table = bytes.getInt(coff + 8)
        if (table != 0) {
            val strings = table + bytes.getInt(coff + 12) * 18
            // After it comes at most the padding of the file alignment.
            val end = strings + bytes.getInt(strings)
            if (end > data.size || (end until data.size).any { data[it] != 0.toByte() }) {
                throw GradleException("The symbol table of ${file.name} is not the last thing in the file")
            }
            val sections = coff + 20 + bytes.getShort(coff + 16)
            for (index in 0 until bytes.getShort(coff + 2)) {
                if (data[sections + index * 40] == '/'.code.toByte()) {
                    throw GradleException("A section of ${file.name} keeps its name in the symbol table")
                }
            }
            bytes.putInt(coff + 8, 0)
            bytes.putInt(coff + 12, 0)
            file.writeBytes(data.copyOf(table))
        }
    }
}

val distTasks = linkTasks.keys.map { targetName ->
    val taskSuffix = targetName.replaceFirstChar { it.uppercase() }
    val licenses = licensesOf("${targetName}CompileKlibraries")
    val writeLicenses = tasks.register("writeLicenses$taskSuffix") {
        this.description = "Lists the licenses of the libraries in the binary for $targetName."
        val output = layout.buildDirectory.file("licenses/$targetName/THIRD-PARTY-LICENSES.txt")
        inputs.property("licenses", licenses)
        outputs.file(output)
        doLast { output.get().asFile.writeText(licenses.get()) }
    }
    val binary = binaries.getValue(targetName)
    val checkPaths = tasks.register("checkLocalPaths$taskSuffix") {
        this.description = "Fails when the binary for $targetName holds a path of this machine."
        val forbidden = listOf(System.getProperty("user.home"), layout.settingsDirectory.asFile.parentFile.path)
        dependsOn(linkTasks.getValue(targetName))
        inputs.file(binary)
        inputs.property("forbidden", forbidden)
        val marker = layout.buildDirectory.file("checks/local-paths-$targetName.txt")
        outputs.file(marker)
        doLast {
            val text = String(binary.get().readBytes(), Charsets.ISO_8859_1)
            val found = forbidden.filter { text.contains(it) }
            if (found.isNotEmpty()) {
                throw GradleException(
                    "${binary.get().name} for $targetName holds $found: it would go out to whoever gets the archive.",
                )
            }
            marker.get().asFile.apply { parentFile.mkdirs() }.writeText("clean\n")
        }
    }
    val archiveName = archiveBase.map { "$it-$targetName" }
    val configure = { archive: AbstractArchiveTask ->
        archive.group = "distribution"
        archive.description = "Packs $whatIsPacked for $targetName."
        archive.destinationDirectory.set(distDirectory)
        archive.isPreserveFileTimestamps = false
        archive.isReproducibleFileOrder = true
        // The output file of the task is a plain provider and does not carry the dependency on it.
        archive.dependsOn(linkTasks.getValue(targetName), checkPaths)
        archive.from(binaries.getValue(targetName)) {
            filePermissions { unix("755") }
            rename { it.removeSuffix(".kexe") }
        }
        archive.from(licenseFile, noticeFile)
        archive.from(readme)
        archive.from(writeLicenses)
        archive.from(extraFiles[targetName].orEmpty())
    }
    if (targetName == "mingwX64") {
        tasks.register<Zip>("dist$taskSuffix") {
            archiveFileName.set(archiveName.map { "$it.zip" })
            configure(this)
        }
    } else {
        tasks.register<Tar>("dist$taskSuffix") {
            archiveFileName.set(archiveName.map { "$it.tar.gz" })
            compression = Compression.GZIP
            configure(this)
        }
    }
}

tasks.register("dist") {
    group = "distribution"
    this.description = "Packs $whatIsPacked for every target into build/dist."
    dependsOn(distTasks)
}
